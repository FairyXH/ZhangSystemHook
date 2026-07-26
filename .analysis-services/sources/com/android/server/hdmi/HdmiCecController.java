package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiCecController {
    private static final int ACTION_ON_RECEIVE_MSG = 2;
    private static final int CEC_DISABLED_DROP_MSG = 4;
    private static final int CEC_DISABLED_IGNORE = 1;
    private static final int CEC_DISABLED_LOG_WARNING = 2;
    private static final byte[] EMPTY_BODY = libcore.util.EmptyArray.BYTE;
    protected static final int HDMI_CEC_HAL_DEATH_COOKIE = 353;
    private static final int INITIAL_HDMI_MESSAGE_HISTORY_SIZE = 250;
    private static final int INVALID_PHYSICAL_ADDRESS = 65535;
    private static final int MAX_DEDICATED_ADDRESS = 11;
    private static final int NUM_LOGICAL_ADDRESS = 16;
    private static final java.lang.String TAG = "HdmiCecController";
    private android.os.Handler mControlHandler;
    private final com.android.server.hdmi.HdmiCecAtomWriter mHdmiCecAtomWriter;
    private android.os.Handler mIoHandler;
    private final com.android.server.hdmi.HdmiCecController.NativeWrapper mNativeWrapperImpl;
    private final com.android.server.hdmi.HdmiControlService mService;
    private final java.util.function.Predicate<java.lang.Integer> mRemoteDeviceAddressPredicate = new java.util.function.Predicate<java.lang.Integer>() { // from class: com.android.server.hdmi.HdmiCecController.1
        @Override // java.util.function.Predicate
        public boolean test(java.lang.Integer address) {
            return !com.android.server.hdmi.HdmiCecController.this.mService.getHdmiCecNetwork().isAllocatedLocalDeviceAddress(address.intValue());
        }
    };
    private final java.util.function.Predicate<java.lang.Integer> mSystemAudioAddressPredicate = new java.util.function.Predicate<java.lang.Integer>() { // from class: com.android.server.hdmi.HdmiCecController.2
        @Override // java.util.function.Predicate
        public boolean test(java.lang.Integer address) {
            return com.android.server.hdmi.HdmiUtils.isEligibleAddressForDevice(5, address.intValue());
        }
    };
    private java.util.concurrent.ArrayBlockingQueue<com.android.server.hdmi.HdmiCecController.Dumpable> mMessageHistory = new java.util.concurrent.ArrayBlockingQueue<>(250);
    private final java.lang.Object mMessageHistoryLock = new java.lang.Object();
    private long mLogicalAddressAllocationDelay = 0;
    private long mPollDevicesDelay = 0;

    interface AllocateAddressCallback {
        void onAllocated(int i, int i2);
    }

    protected interface NativeWrapper {
        void enableCec(boolean z);

        void enableSystemCecControl(boolean z);

        void enableWakeupByOtp(boolean z);

        int nativeAddLogicalAddress(int i);

        void nativeClearLogicalAddress();

        void nativeEnableAudioReturnChannel(int i, boolean z);

        int nativeGetHpdSignalType(int i);

        int nativeGetPhysicalAddress();

        android.hardware.hdmi.HdmiPortInfo[] nativeGetPortInfos();

        int nativeGetVendorId();

        int nativeGetVersion();

        java.lang.String nativeInit();

        boolean nativeIsConnected(int i);

        int nativeSendCecCommand(int i, int i2, byte[] bArr);

        void nativeSetHpdSignalType(int i, int i2);

        void nativeSetLanguage(java.lang.String str);

        void setCallback(com.android.server.hdmi.HdmiCecController.HdmiCecCallback hdmiCecCallback);
    }

    private HdmiCecController(com.android.server.hdmi.HdmiControlService service, com.android.server.hdmi.HdmiCecController.NativeWrapper nativeWrapper, com.android.server.hdmi.HdmiCecAtomWriter atomWriter) {
        this.mService = service;
        this.mNativeWrapperImpl = nativeWrapper;
        this.mHdmiCecAtomWriter = atomWriter;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static com.android.server.hdmi.HdmiCecController create(com.android.server.hdmi.HdmiControlService hdmiControlService, com.android.server.hdmi.HdmiCecAtomWriter hdmiCecAtomWriter) {
        java.lang.Object[] objArr = 0;
        java.lang.Object[] objArr2 = 0;
        com.android.server.hdmi.HdmiCecController hdmiCecControllerCreateWithNativeWrapper = createWithNativeWrapper(hdmiControlService, new com.android.server.hdmi.HdmiCecController.NativeWrapperImplAidl(), hdmiCecAtomWriter);
        if (hdmiCecControllerCreateWithNativeWrapper != null) {
            return hdmiCecControllerCreateWithNativeWrapper;
        }
        com.android.server.hdmi.HdmiLogger.warning("Unable to use CEC and HDMI Connection AIDL HALs", new java.lang.Object[0]);
        com.android.server.hdmi.HdmiCecController hdmiCecControllerCreateWithNativeWrapper2 = createWithNativeWrapper(hdmiControlService, new com.android.server.hdmi.HdmiCecController.NativeWrapperImpl11(), hdmiCecAtomWriter);
        if (hdmiCecControllerCreateWithNativeWrapper2 != null) {
            return hdmiCecControllerCreateWithNativeWrapper2;
        }
        com.android.server.hdmi.HdmiLogger.warning("Unable to use cec@1.1", new java.lang.Object[0]);
        return createWithNativeWrapper(hdmiControlService, new com.android.server.hdmi.HdmiCecController.NativeWrapperImpl(), hdmiCecAtomWriter);
    }

    static com.android.server.hdmi.HdmiCecController createWithNativeWrapper(com.android.server.hdmi.HdmiControlService service, com.android.server.hdmi.HdmiCecController.NativeWrapper nativeWrapper, com.android.server.hdmi.HdmiCecAtomWriter atomWriter) {
        com.android.server.hdmi.HdmiCecController controller = new com.android.server.hdmi.HdmiCecController(service, nativeWrapper, atomWriter);
        java.lang.String nativePtr = nativeWrapper.nativeInit();
        if (nativePtr == null) {
            com.android.server.hdmi.HdmiLogger.warning("Couldn't get tv.cec service.", new java.lang.Object[0]);
            return null;
        }
        controller.init(nativeWrapper);
        return controller;
    }

    private void init(com.android.server.hdmi.HdmiCecController.NativeWrapper nativeWrapper) {
        this.mIoHandler = new android.os.Handler(this.mService.getIoLooper());
        this.mControlHandler = new android.os.Handler(this.mService.getServiceLooper());
        nativeWrapper.setCallback(new com.android.server.hdmi.HdmiCecController.HdmiCecCallback());
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void allocateLogicalAddress(final int deviceType, final int preferredAddress, final com.android.server.hdmi.HdmiCecController.AllocateAddressCallback callback) {
        assertRunOnServiceThread();
        this.mIoHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.3
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.hdmi.HdmiCecController.this.handleAllocateLogicalAddress(deviceType, preferredAddress, callback);
            }
        }, this.mLogicalAddressAllocationDelay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.IoThreadOnly
    public void handleAllocateLogicalAddress(final int deviceType, int preferredAddress, final com.android.server.hdmi.HdmiCecController.AllocateAddressCallback callback) {
        assertRunOnIoThread();
        java.util.List<java.lang.Integer> logicalAddressesToPoll = new java.util.ArrayList<>();
        if (com.android.server.hdmi.HdmiUtils.isEligibleAddressForDevice(deviceType, preferredAddress)) {
            logicalAddressesToPoll.add(java.lang.Integer.valueOf(preferredAddress));
        }
        for (int i = 0; i < 16; i++) {
            if (!logicalAddressesToPoll.contains(java.lang.Integer.valueOf(i)) && com.android.server.hdmi.HdmiUtils.isEligibleAddressForDevice(deviceType, i) && com.android.server.hdmi.HdmiUtils.isEligibleAddressForCecVersion(this.mService.getCecVersion(), i)) {
                logicalAddressesToPoll.add(java.lang.Integer.valueOf(i));
            }
        }
        int logicalAddress = 15;
        java.util.Iterator<java.lang.Integer> it = logicalAddressesToPoll.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            java.lang.Integer logicalAddressToPoll = it.next();
            boolean acked = false;
            int j = 0;
            while (true) {
                if (j >= 3) {
                    break;
                }
                if (!sendPollMessage(logicalAddressToPoll.intValue(), logicalAddressToPoll.intValue(), 1)) {
                    j++;
                } else {
                    acked = true;
                    break;
                }
            }
            if (!acked) {
                logicalAddress = logicalAddressToPoll.intValue();
                break;
            }
        }
        final int assignedAddress = logicalAddress;
        com.android.server.hdmi.HdmiLogger.debug("New logical address for device [%d]: [preferred:%d, assigned:%d]", java.lang.Integer.valueOf(deviceType), java.lang.Integer.valueOf(preferredAddress), java.lang.Integer.valueOf(assignedAddress));
        if (callback != null) {
            runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.4
                @Override // java.lang.Runnable
                public void run() {
                    callback.onAllocated(deviceType, assignedAddress);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] buildBody(int opcode, byte[] params) {
        byte[] body = new byte[params.length + 1];
        body[0] = (byte) opcode;
        java.lang.System.arraycopy(params, 0, body, 1, params.length);
        return body;
    }

    android.hardware.hdmi.HdmiPortInfo[] getPortInfos() {
        return this.mNativeWrapperImpl.nativeGetPortInfos();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int addLogicalAddress(int newLogicalAddress) {
        assertRunOnServiceThread();
        if (com.android.server.hdmi.HdmiUtils.isValidAddress(newLogicalAddress)) {
            return this.mNativeWrapperImpl.nativeAddLogicalAddress(newLogicalAddress);
        }
        return 2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void clearLogicalAddress() {
        assertRunOnServiceThread();
        this.mNativeWrapperImpl.nativeClearLogicalAddress();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getPhysicalAddress() {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeGetPhysicalAddress();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getVersion() {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeGetVersion();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getVendorId() {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeGetVendorId();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void enableWakeupByOtp(boolean enabled) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("enableWakeupByOtp: %b", java.lang.Boolean.valueOf(enabled));
        this.mNativeWrapperImpl.enableWakeupByOtp(enabled);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void enableCec(boolean enabled) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("enableCec: %b", java.lang.Boolean.valueOf(enabled));
        this.mNativeWrapperImpl.enableCec(enabled);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void enableSystemCecControl(boolean enabled) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("enableSystemCecControl: %b", java.lang.Boolean.valueOf(enabled));
        this.mNativeWrapperImpl.enableSystemCecControl(enabled);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setHpdSignalType(int signal, int portId) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("setHpdSignalType: portId %b, signal %b", java.lang.Integer.valueOf(portId), java.lang.Integer.valueOf(signal));
        this.mNativeWrapperImpl.nativeSetHpdSignalType(signal, portId);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getHpdSignalType(int portId) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("getHpdSignalType: portId %b ", java.lang.Integer.valueOf(portId));
        return this.mNativeWrapperImpl.nativeGetHpdSignalType(portId);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setLanguage(java.lang.String language) {
        assertRunOnServiceThread();
        if (!isLanguage(language)) {
            return;
        }
        this.mNativeWrapperImpl.nativeSetLanguage(language);
    }

    void setLogicalAddressAllocationDelay(long delay) {
        this.mLogicalAddressAllocationDelay = delay;
    }

    void setPollDevicesDelay(long delay) {
        this.mPollDevicesDelay = delay;
    }

    static boolean isLanguage(java.lang.String language) {
        if (language == null || language.isEmpty()) {
            return false;
        }
        android.icu.util.ULocale.Builder builder = new android.icu.util.ULocale.Builder();
        try {
            builder.setLanguage(language);
            return true;
        } catch (android.icu.util.IllformedLocaleException e) {
            return false;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void enableAudioReturnChannel(int port, boolean enabled) {
        assertRunOnServiceThread();
        this.mNativeWrapperImpl.nativeEnableAudioReturnChannel(port, enabled);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isConnected(int port) {
        assertRunOnServiceThread();
        return this.mNativeWrapperImpl.nativeIsConnected(port);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void pollDevices(final com.android.server.hdmi.HdmiControlService.DevicePollingCallback callback, final int sourceAddress, int pickStrategy, final int retryCount, final long pollingMessageInterval) {
        assertRunOnServiceThread();
        final java.util.List<java.lang.Integer> pollingCandidates = pickPollCandidates(pickStrategy);
        final java.util.ArrayList<java.lang.Integer> allocated = new java.util.ArrayList<>();
        this.mControlHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$pollDevices$0(sourceAddress, pollingCandidates, retryCount, callback, allocated, pollingMessageInterval);
            }
        }, this.mPollDevicesDelay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pollDevices$0(int sourceAddress, java.util.List pollingCandidates, int retryCount, com.android.server.hdmi.HdmiControlService.DevicePollingCallback callback, java.util.ArrayList allocated, long pollingMessageInterval) {
        runDevicePolling(sourceAddress, pollingCandidates, retryCount, callback, allocated, pollingMessageInterval, false);
    }

    private java.util.List<java.lang.Integer> pickPollCandidates(int pickStrategy) {
        java.util.function.Predicate<java.lang.Integer> pickPredicate;
        int strategy = pickStrategy & 3;
        switch (strategy) {
            case 2:
                pickPredicate = this.mSystemAudioAddressPredicate;
                break;
            default:
                pickPredicate = this.mRemoteDeviceAddressPredicate;
                break;
        }
        int iterationStrategy = 196608 & pickStrategy;
        java.util.ArrayList<java.lang.Integer> pollingCandidates = new java.util.ArrayList<>();
        switch (iterationStrategy) {
            case 65536:
                for (int i = 0; i <= 14; i++) {
                    if (pickPredicate.test(java.lang.Integer.valueOf(i))) {
                        pollingCandidates.add(java.lang.Integer.valueOf(i));
                    }
                }
                return pollingCandidates;
            default:
                for (int i2 = 14; i2 >= 0; i2--) {
                    if (pickPredicate.test(java.lang.Integer.valueOf(i2))) {
                        pollingCandidates.add(java.lang.Integer.valueOf(i2));
                    }
                }
                return pollingCandidates;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void runDevicePolling(final int sourceAddress, final java.util.List<java.lang.Integer> candidates, final int retryCount, final com.android.server.hdmi.HdmiControlService.DevicePollingCallback callback, final java.util.List<java.lang.Integer> allocated, final long pollingMessageInterval, boolean pollStarted) {
        assertRunOnServiceThread();
        if (candidates.isEmpty()) {
            if (callback != null) {
                com.android.server.hdmi.HdmiLogger.debug("[P]:AllocatedAddress=%s", allocated.toString());
                callback.onPollingFinished(allocated);
                return;
            }
            return;
        }
        final java.lang.Integer candidate = candidates.remove(0);
        this.mIoHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.5
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.hdmi.HdmiCecController.this.sendPollMessage(sourceAddress, candidate.intValue(), retryCount)) {
                    allocated.add(candidate);
                }
                com.android.server.hdmi.HdmiCecController.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.hdmi.HdmiCecController.this.runDevicePolling(sourceAddress, candidates, retryCount, callback, allocated, pollingMessageInterval, true);
                    }
                });
            }
        }, pollStarted ? pollingMessageInterval : 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.IoThreadOnly
    public boolean sendPollMessage(int sourceAddress, int destinationAddress, int retryCount) {
        assertRunOnIoThread();
        for (int i = 0; i < retryCount; i++) {
            int ret = this.mNativeWrapperImpl.nativeSendCecCommand(sourceAddress, destinationAddress, EMPTY_BODY);
            if (ret == 0) {
                return true;
            }
            if (ret != 1) {
                com.android.server.hdmi.HdmiLogger.warning("Failed to send a polling message(%d->%d) with return code %d", java.lang.Integer.valueOf(sourceAddress), java.lang.Integer.valueOf(destinationAddress), java.lang.Integer.valueOf(ret));
            }
        }
        return false;
    }

    private void assertRunOnIoThread() {
        if (android.os.Looper.myLooper() != this.mIoHandler.getLooper()) {
            throw new java.lang.IllegalStateException("Should run on io thread.");
        }
    }

    private void assertRunOnServiceThread() {
        if (android.os.Looper.myLooper() != this.mControlHandler.getLooper()) {
            throw new java.lang.IllegalStateException("Should run on service thread.");
        }
    }

    void runOnIoThread(java.lang.Runnable runnable) {
        this.mIoHandler.post(new com.android.server.hdmi.WorkSourceUidPreservingRunnable(runnable));
    }

    void runOnServiceThread(java.lang.Runnable runnable) {
        this.mControlHandler.post(new com.android.server.hdmi.WorkSourceUidPreservingRunnable(runnable));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void flush(final java.lang.Runnable runnable) {
        assertRunOnServiceThread();
        runOnIoThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.6
            @Override // java.lang.Runnable
            public void run() {
                com.android.server.hdmi.HdmiCecController.this.runOnServiceThread(runnable);
            }
        });
    }

    private boolean isAcceptableAddress(int address) {
        if (address == 15) {
            return true;
        }
        return this.mService.getHdmiCecNetwork().isAllocatedLocalDeviceAddress(address);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onReceiveCommand(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled() && !com.android.server.hdmi.HdmiCecMessage.isCecTransportMessage(message.getOpcode())) {
            com.android.server.hdmi.HdmiLogger.warning("Message " + message + " received when cec disabled", new java.lang.Object[0]);
        }
        if (this.mService.isAddressAllocated() && !isAcceptableAddress(message.getDestination())) {
            return;
        }
        int messageState = this.mService.handleCecCommand(message);
        if (messageState == -2) {
            maySendFeatureAbortCommand(message, 0);
        } else if (messageState != -1) {
            maySendFeatureAbortCommand(message, messageState);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void maySendFeatureAbortCommand(com.android.server.hdmi.HdmiCecMessage message, int reason) {
        int originalOpcode;
        assertRunOnServiceThread();
        int src = message.getDestination();
        int dest = message.getSource();
        if (src == 15 || dest == 15 || (originalOpcode = message.getOpcode()) == 0) {
            return;
        }
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildFeatureAbortCommand(src, dest, originalOpcode, reason));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void sendCommand(com.android.server.hdmi.HdmiCecMessage cecMessage) {
        assertRunOnServiceThread();
        sendCommand(cecMessage, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCallingUid() {
        int workSourceUid = android.os.Binder.getCallingWorkSourceUid();
        if (workSourceUid == -1) {
            return android.os.Binder.getCallingUid();
        }
        return workSourceUid;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void sendCommand(final com.android.server.hdmi.HdmiCecMessage cecMessage, final com.android.server.hdmi.HdmiControlService.SendMessageCallback callback) {
        assertRunOnServiceThread();
        final java.util.List<java.lang.String> sendResults = new java.util.ArrayList<>();
        runOnIoThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.7
            @Override // java.lang.Runnable
            public void run() {
                final int errorCode;
                com.android.server.hdmi.HdmiLogger.debug("[S]:" + cecMessage, new java.lang.Object[0]);
                byte[] body = com.android.server.hdmi.HdmiCecController.buildBody(cecMessage.getOpcode(), cecMessage.getParams());
                int retransmissionCount = 0;
                while (true) {
                    errorCode = com.android.server.hdmi.HdmiCecController.this.mNativeWrapperImpl.nativeSendCecCommand(cecMessage.getSource(), cecMessage.getDestination(), body);
                    switch (errorCode) {
                        case 0:
                            sendResults.add("ACK");
                            break;
                        case 1:
                            sendResults.add("NACK");
                            break;
                        case 2:
                            sendResults.add("BUSY");
                            break;
                        case 3:
                            sendResults.add("FAIL");
                            break;
                    }
                    if (errorCode != 0) {
                        int retransmissionCount2 = retransmissionCount + 1;
                        if (retransmissionCount < 1) {
                            retransmissionCount = retransmissionCount2;
                        }
                    }
                }
                if (errorCode != 0) {
                    android.util.Slog.w(com.android.server.hdmi.HdmiCecController.TAG, "Failed to send " + cecMessage + " with errorCode=" + errorCode);
                }
                com.android.server.hdmi.HdmiCecController.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        com.android.server.hdmi.HdmiCecController.this.mHdmiCecAtomWriter.messageReported(cecMessage, 2, com.android.server.hdmi.HdmiCecController.this.getCallingUid(), errorCode);
                        if (callback != null) {
                            callback.onSendCompleted(errorCode);
                        }
                    }
                });
            }
        });
        addCecMessageToHistory(false, cecMessage, sendResults);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void handleIncomingCecCommand(int srcAddress, int dstAddress, byte[] body) {
        assertRunOnServiceThread();
        if (body.length == 0) {
            android.util.Slog.e(TAG, "Message with empty body received.");
            return;
        }
        com.android.server.hdmi.HdmiCecMessage command = com.android.server.hdmi.HdmiCecMessage.build(srcAddress, dstAddress, body[0], java.util.Arrays.copyOfRange(body, 1, body.length));
        if (command.getValidationResult() != 0) {
            android.util.Slog.e(TAG, "Invalid message received: " + command);
        }
        com.android.server.hdmi.HdmiLogger.debug("[R]:" + command, new java.lang.Object[0]);
        addCecMessageToHistory(true, command, null);
        this.mHdmiCecAtomWriter.messageReported(command, incomingMessageDirection(srcAddress, dstAddress), getCallingUid());
        onReceiveCommand(command);
    }

    private int incomingMessageDirection(int srcAddress, int dstAddress) {
        boolean sourceIsLocal = false;
        boolean destinationIsLocal = dstAddress == 15;
        for (com.android.server.hdmi.HdmiCecLocalDevice localDevice : this.mService.getHdmiCecNetwork().getLocalDeviceList()) {
            int logicalAddress = localDevice.getDeviceInfo().getLogicalAddress();
            if (logicalAddress == srcAddress) {
                sourceIsLocal = true;
            }
            if (logicalAddress == dstAddress) {
                destinationIsLocal = true;
            }
        }
        if (sourceIsLocal || !destinationIsLocal) {
            return (sourceIsLocal && destinationIsLocal) ? 4 : 1;
        }
        return 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void handleHotplug(int port, boolean connected) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("Hotplug event:[port:%d, connected:%b]", java.lang.Integer.valueOf(port), java.lang.Boolean.valueOf(connected));
        addHotplugEventToHistory(port, connected);
        this.mService.onHotplug(port, connected);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void addHotplugEventToHistory(int port, boolean connected) {
        assertRunOnServiceThread();
        addEventToHistory(new com.android.server.hdmi.HdmiCecController.HotplugHistoryRecord(port, connected));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void addCecMessageToHistory(boolean isReceived, com.android.server.hdmi.HdmiCecMessage message, java.util.List<java.lang.String> sendResults) {
        assertRunOnServiceThread();
        addEventToHistory(new com.android.server.hdmi.HdmiCecController.MessageHistoryRecord(isReceived, message, sendResults));
    }

    private void addEventToHistory(com.android.server.hdmi.HdmiCecController.Dumpable event) {
        synchronized (this.mMessageHistoryLock) {
            if (!this.mMessageHistory.offer(event)) {
                this.mMessageHistory.poll();
                this.mMessageHistory.offer(event);
            }
        }
    }

    int getMessageHistorySize() {
        int size;
        synchronized (this.mMessageHistoryLock) {
            size = this.mMessageHistory.size() + this.mMessageHistory.remainingCapacity();
        }
        return size;
    }

    boolean setMessageHistorySize(int newSize) {
        if (newSize < 250) {
            return false;
        }
        java.util.concurrent.ArrayBlockingQueue<com.android.server.hdmi.HdmiCecController.Dumpable> newMessageHistory = new java.util.concurrent.ArrayBlockingQueue<>(newSize);
        synchronized (this.mMessageHistoryLock) {
            if (newSize < this.mMessageHistory.size()) {
                for (int i = 0; i < this.mMessageHistory.size() - newSize; i++) {
                    this.mMessageHistory.poll();
                }
            }
            newMessageHistory.addAll(this.mMessageHistory);
            this.mMessageHistory = newMessageHistory;
        }
        return true;
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("CEC message history:");
        pw.increaseIndent();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (com.android.server.hdmi.HdmiCecController.Dumpable record : this.mMessageHistory) {
            record.dump(pw, sdf);
        }
        pw.decreaseIndent();
    }

    private static final class NativeWrapperImplAidl implements com.android.server.hdmi.HdmiCecController.NativeWrapper, android.os.IBinder.DeathRecipient {
        private com.android.server.hdmi.HdmiCecController.HdmiCecCallback mCallback;
        private android.hardware.tv.hdmi.cec.IHdmiCec mHdmiCec;
        private android.hardware.tv.hdmi.connection.IHdmiConnection mHdmiConnection;
        private final java.lang.Object mLock;

        private NativeWrapperImplAidl() {
            this.mLock = new java.lang.Object();
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public java.lang.String nativeInit() {
            if (connectToHal()) {
                return this.mHdmiCec.toString() + " " + this.mHdmiConnection.toString();
            }
            return null;
        }

        boolean connectToHal() {
            this.mHdmiCec = android.hardware.tv.hdmi.cec.IHdmiCec.Stub.asInterface(android.os.ServiceManager.getService(android.hardware.tv.hdmi.cec.IHdmiCec.DESCRIPTOR + "/default"));
            if (this.mHdmiCec == null) {
                com.android.server.hdmi.HdmiLogger.error("Could not initialize HDMI CEC AIDL HAL", new java.lang.Object[0]);
                return false;
            }
            try {
                this.mHdmiCec.asBinder().linkToDeath(this, 0);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't link to death : ", e, new java.lang.Object[0]);
            }
            this.mHdmiConnection = android.hardware.tv.hdmi.connection.IHdmiConnection.Stub.asInterface(android.os.ServiceManager.getService(android.hardware.tv.hdmi.connection.IHdmiConnection.DESCRIPTOR + "/default"));
            if (this.mHdmiConnection == null) {
                com.android.server.hdmi.HdmiLogger.error("Could not initialize HDMI Connection AIDL HAL", new java.lang.Object[0]);
                return false;
            }
            try {
                this.mHdmiConnection.asBinder().linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e2) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't link to death : ", e2, new java.lang.Object[0]);
                return true;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mHdmiCec.asBinder().unlinkToDeath(this, 0);
            this.mHdmiConnection.asBinder().unlinkToDeath(this, 0);
            com.android.server.hdmi.HdmiLogger.error("HDMI Connection or CEC service died, reconnecting", new java.lang.Object[0]);
            connectToHal();
            if (this.mCallback != null) {
                setCallback(this.mCallback);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void setCallback(com.android.server.hdmi.HdmiCecController.HdmiCecCallback callback) {
            this.mCallback = callback;
            try {
                this.mHdmiCec.setCallback(new com.android.server.hdmi.HdmiCecController.HdmiCecCallbackAidl(callback));
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't initialise tv.cec callback : ", e, new java.lang.Object[0]);
            }
            try {
                this.mHdmiConnection.setCallback(new com.android.server.hdmi.HdmiCecController.HdmiConnectionCallbackAidl(callback));
            } catch (android.os.RemoteException e2) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't initialise tv.hdmi callback : ", e2, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeSendCecCommand(int srcAddress, int dstAddress, byte[] body) {
            android.hardware.tv.hdmi.cec.CecMessage message = new android.hardware.tv.hdmi.cec.CecMessage();
            message.initiator = (byte) (srcAddress & 15);
            message.destination = (byte) (dstAddress & 15);
            message.body = body;
            try {
                return this.mHdmiCec.sendMessage(message);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to send CEC message : ", e, new java.lang.Object[0]);
                return 3;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeAddLogicalAddress(int logicalAddress) {
            try {
                return this.mHdmiCec.addLogicalAddress((byte) logicalAddress);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to add a logical address : ", e, new java.lang.Object[0]);
                return 2;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeClearLogicalAddress() {
            try {
                this.mHdmiCec.clearLogicalAddress();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to clear logical address : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetPhysicalAddress() {
            try {
                return this.mHdmiCec.getPhysicalAddress();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get physical address : ", e, new java.lang.Object[0]);
                return 65535;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVersion() {
            try {
                return this.mHdmiCec.getCecVersion();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get cec version : ", e, new java.lang.Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVendorId() {
            try {
                return this.mHdmiCec.getVendorId();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get vendor id : ", e, new java.lang.Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableWakeupByOtp(boolean enabled) {
            try {
                this.mHdmiCec.enableWakeupByOtp(enabled);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed call to enableWakeupByOtp : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableCec(boolean enabled) {
            try {
                this.mHdmiCec.enableCec(enabled);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed call to enableCec : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableSystemCecControl(boolean enabled) {
            try {
                this.mHdmiCec.enableSystemCecControl(enabled);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed call to enableSystemCecControl : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetLanguage(java.lang.String language) {
            try {
                this.mHdmiCec.setLanguage(language);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to set language : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeEnableAudioReturnChannel(int port, boolean flag) {
            try {
                this.mHdmiCec.enableAudioReturnChannel(port, flag);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to enable/disable ARC : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public android.hardware.hdmi.HdmiPortInfo[] nativeGetPortInfos() {
            try {
                android.hardware.tv.hdmi.connection.HdmiPortInfo[] hdmiPortInfos = this.mHdmiConnection.getPortInfo();
                android.hardware.hdmi.HdmiPortInfo[] hdmiPortInfo = new android.hardware.hdmi.HdmiPortInfo[hdmiPortInfos.length];
                int i = 0;
                for (android.hardware.tv.hdmi.connection.HdmiPortInfo portInfo : hdmiPortInfos) {
                    hdmiPortInfo[i] = new android.hardware.hdmi.HdmiPortInfo.Builder(portInfo.portId, portInfo.type, portInfo.physicalAddress).setCecSupported(portInfo.cecSupported).setMhlSupported(false).setArcSupported(portInfo.arcSupported).setEarcSupported(portInfo.eArcSupported).build();
                    i++;
                }
                return hdmiPortInfo;
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get port information : ", e, new java.lang.Object[0]);
                return null;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public boolean nativeIsConnected(int port) {
            try {
                return this.mHdmiConnection.isConnected(port);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get connection info : ", e, new java.lang.Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetHpdSignalType(int signal, int portId) {
            try {
                this.mHdmiConnection.setHpdSignal((byte) signal, portId);
            } catch (android.os.ServiceSpecificException sse) {
                com.android.server.hdmi.HdmiLogger.error("Could not set HPD signal type for portId " + portId + " to " + signal + ". Error: ", java.lang.Integer.valueOf(sse.errorCode));
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Could not set HPD signal type for portId " + portId + " to " + signal + ". Exception: ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetHpdSignalType(int portId) {
            try {
                return this.mHdmiConnection.getHpdSignal(portId);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Could not get HPD signal type for portId " + portId + ". Exception: ", e, new java.lang.Object[0]);
                return 0;
            }
        }
    }

    private static final class NativeWrapperImpl11 implements com.android.server.hdmi.HdmiCecController.NativeWrapper, android.os.IHwBinder.DeathRecipient, android.hardware.tv.cec.V1_0.IHdmiCec.getPhysicalAddressCallback {
        private com.android.server.hdmi.HdmiCecController.HdmiCecCallback mCallback;
        private android.hardware.tv.cec.V1_1.IHdmiCec mHdmiCec;
        private final java.lang.Object mLock;
        private int mPhysicalAddress;

        private NativeWrapperImpl11() {
            this.mLock = new java.lang.Object();
            this.mPhysicalAddress = 65535;
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public java.lang.String nativeInit() {
            if (connectToHal()) {
                return this.mHdmiCec.toString();
            }
            return null;
        }

        boolean connectToHal() {
            try {
                this.mHdmiCec = android.hardware.tv.cec.V1_1.IHdmiCec.getService(true);
                try {
                    this.mHdmiCec.linkToDeath(this, 353L);
                } catch (android.os.RemoteException e) {
                    com.android.server.hdmi.HdmiLogger.error("Couldn't link to death : ", e, new java.lang.Object[0]);
                }
                return true;
            } catch (android.os.RemoteException | java.util.NoSuchElementException e2) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't connect to cec@1.1", e2, new java.lang.Object[0]);
                return false;
            }
        }

        @Override // android.hardware.tv.cec.V1_0.IHdmiCec.getPhysicalAddressCallback
        public void onValues(int result, short addr) {
            if (result == 0) {
                synchronized (this.mLock) {
                    this.mPhysicalAddress = new java.lang.Short(addr).intValue();
                }
            }
        }

        public void serviceDied(long cookie) {
            if (cookie == 353) {
                com.android.server.hdmi.HdmiLogger.error("Service died cookie : " + cookie + "; reconnecting", new java.lang.Object[0]);
                connectToHal();
                if (this.mCallback != null) {
                    setCallback(this.mCallback);
                }
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void setCallback(com.android.server.hdmi.HdmiCecController.HdmiCecCallback callback) {
            this.mCallback = callback;
            try {
                this.mHdmiCec.setCallback_1_1(new com.android.server.hdmi.HdmiCecController.HdmiCecCallback11(callback));
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't initialise tv.cec callback : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeSendCecCommand(int srcAddress, int dstAddress, byte[] body) {
            android.hardware.tv.cec.V1_1.CecMessage message = new android.hardware.tv.cec.V1_1.CecMessage();
            message.initiator = srcAddress;
            message.destination = dstAddress;
            message.body = new java.util.ArrayList<>(body.length);
            for (byte b : body) {
                message.body.add(java.lang.Byte.valueOf(b));
            }
            try {
                return this.mHdmiCec.sendMessage_1_1(message);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to send CEC message : ", e, new java.lang.Object[0]);
                return 3;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeAddLogicalAddress(int logicalAddress) {
            try {
                return this.mHdmiCec.addLogicalAddress_1_1(logicalAddress);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to add a logical address : ", e, new java.lang.Object[0]);
                return 2;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeClearLogicalAddress() {
            try {
                this.mHdmiCec.clearLogicalAddress();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to clear logical address : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetPhysicalAddress() {
            try {
                this.mHdmiCec.getPhysicalAddress(this);
                return this.mPhysicalAddress;
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get physical address : ", e, new java.lang.Object[0]);
                return 65535;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVersion() {
            try {
                return this.mHdmiCec.getCecVersion();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get cec version : ", e, new java.lang.Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVendorId() {
            try {
                return this.mHdmiCec.getVendorId();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get vendor id : ", e, new java.lang.Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public android.hardware.hdmi.HdmiPortInfo[] nativeGetPortInfos() {
            try {
                java.util.ArrayList<android.hardware.tv.cec.V1_0.HdmiPortInfo> hdmiPortInfos = this.mHdmiCec.getPortInfo();
                android.hardware.hdmi.HdmiPortInfo[] hdmiPortInfo = new android.hardware.hdmi.HdmiPortInfo[hdmiPortInfos.size()];
                int i = 0;
                for (android.hardware.tv.cec.V1_0.HdmiPortInfo portInfo : hdmiPortInfos) {
                    hdmiPortInfo[i] = new android.hardware.hdmi.HdmiPortInfo.Builder(portInfo.portId, portInfo.type, java.lang.Short.toUnsignedInt(portInfo.physicalAddress)).setCecSupported(portInfo.cecSupported).setMhlSupported(false).setArcSupported(portInfo.arcSupported).setEarcSupported(false).build();
                    i++;
                }
                return hdmiPortInfo;
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get port information : ", e, new java.lang.Object[0]);
                return null;
            }
        }

        private void nativeSetOption(int flag, boolean enabled) {
            try {
                this.mHdmiCec.setOption(flag, enabled);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to set option : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableWakeupByOtp(boolean enabled) {
            nativeSetOption(1, enabled);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableCec(boolean enabled) {
            nativeSetOption(2, enabled);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableSystemCecControl(boolean enabled) {
            nativeSetOption(3, enabled);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetLanguage(java.lang.String language) {
            try {
                this.mHdmiCec.setLanguage(language);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to set language : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeEnableAudioReturnChannel(int port, boolean flag) {
            try {
                this.mHdmiCec.enableAudioReturnChannel(port, flag);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to enable/disable ARC : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public boolean nativeIsConnected(int port) {
            try {
                return this.mHdmiCec.isConnected(port);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get connection info : ", e, new java.lang.Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetHpdSignalType(int signal, int portId) {
            com.android.server.hdmi.HdmiLogger.error("Failed to set HPD signal type: not supported by HAL.", new java.lang.Object[0]);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetHpdSignalType(int portId) {
            com.android.server.hdmi.HdmiLogger.error("Failed to get HPD signal type: not supported by HAL.", new java.lang.Object[0]);
            return 0;
        }
    }

    private static final class NativeWrapperImpl implements com.android.server.hdmi.HdmiCecController.NativeWrapper, android.os.IHwBinder.DeathRecipient, android.hardware.tv.cec.V1_0.IHdmiCec.getPhysicalAddressCallback {
        private com.android.server.hdmi.HdmiCecController.HdmiCecCallback mCallback;
        private android.hardware.tv.cec.V1_0.IHdmiCec mHdmiCec;
        private final java.lang.Object mLock;
        private int mPhysicalAddress;

        private NativeWrapperImpl() {
            this.mLock = new java.lang.Object();
            this.mPhysicalAddress = 65535;
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public java.lang.String nativeInit() {
            if (connectToHal()) {
                return this.mHdmiCec.toString();
            }
            return null;
        }

        boolean connectToHal() {
            try {
                this.mHdmiCec = android.hardware.tv.cec.V1_0.IHdmiCec.getService(true);
                try {
                    this.mHdmiCec.linkToDeath(this, 353L);
                } catch (android.os.RemoteException e) {
                    com.android.server.hdmi.HdmiLogger.error("Couldn't link to death : ", e, new java.lang.Object[0]);
                }
                return true;
            } catch (android.os.RemoteException | java.util.NoSuchElementException e2) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't connect to cec@1.0", e2, new java.lang.Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void setCallback(com.android.server.hdmi.HdmiCecController.HdmiCecCallback callback) {
            this.mCallback = callback;
            try {
                this.mHdmiCec.setCallback(new com.android.server.hdmi.HdmiCecController.HdmiCecCallback10(callback));
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Couldn't initialise tv.cec callback : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeSendCecCommand(int srcAddress, int dstAddress, byte[] body) {
            android.hardware.tv.cec.V1_0.CecMessage message = new android.hardware.tv.cec.V1_0.CecMessage();
            message.initiator = srcAddress;
            message.destination = dstAddress;
            message.body = new java.util.ArrayList<>(body.length);
            for (byte b : body) {
                message.body.add(java.lang.Byte.valueOf(b));
            }
            try {
                return this.mHdmiCec.sendMessage(message);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to send CEC message : ", e, new java.lang.Object[0]);
                return 3;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeAddLogicalAddress(int logicalAddress) {
            try {
                return this.mHdmiCec.addLogicalAddress(logicalAddress);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to add a logical address : ", e, new java.lang.Object[0]);
                return 2;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeClearLogicalAddress() {
            try {
                this.mHdmiCec.clearLogicalAddress();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to clear logical address : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetPhysicalAddress() {
            try {
                this.mHdmiCec.getPhysicalAddress(this);
                return this.mPhysicalAddress;
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get physical address : ", e, new java.lang.Object[0]);
                return 65535;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVersion() {
            try {
                return this.mHdmiCec.getCecVersion();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get cec version : ", e, new java.lang.Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetVendorId() {
            try {
                return this.mHdmiCec.getVendorId();
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get vendor id : ", e, new java.lang.Object[0]);
                return 1;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public android.hardware.hdmi.HdmiPortInfo[] nativeGetPortInfos() {
            try {
                java.util.ArrayList<android.hardware.tv.cec.V1_0.HdmiPortInfo> hdmiPortInfos = this.mHdmiCec.getPortInfo();
                android.hardware.hdmi.HdmiPortInfo[] hdmiPortInfo = new android.hardware.hdmi.HdmiPortInfo[hdmiPortInfos.size()];
                int i = 0;
                for (android.hardware.tv.cec.V1_0.HdmiPortInfo portInfo : hdmiPortInfos) {
                    hdmiPortInfo[i] = new android.hardware.hdmi.HdmiPortInfo.Builder(portInfo.portId, portInfo.type, java.lang.Short.toUnsignedInt(portInfo.physicalAddress)).setCecSupported(portInfo.cecSupported).setMhlSupported(false).setArcSupported(portInfo.arcSupported).setEarcSupported(false).build();
                    i++;
                }
                return hdmiPortInfo;
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get port information : ", e, new java.lang.Object[0]);
                return null;
            }
        }

        private void nativeSetOption(int flag, boolean enabled) {
            try {
                this.mHdmiCec.setOption(flag, enabled);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to set option : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableWakeupByOtp(boolean enabled) {
            nativeSetOption(1, enabled);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableCec(boolean enabled) {
            nativeSetOption(2, enabled);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void enableSystemCecControl(boolean enabled) {
            nativeSetOption(3, enabled);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetLanguage(java.lang.String language) {
            try {
                this.mHdmiCec.setLanguage(language);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to set language : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeEnableAudioReturnChannel(int port, boolean flag) {
            try {
                this.mHdmiCec.enableAudioReturnChannel(port, flag);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to enable/disable ARC : ", e, new java.lang.Object[0]);
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public boolean nativeIsConnected(int port) {
            try {
                return this.mHdmiCec.isConnected(port);
            } catch (android.os.RemoteException e) {
                com.android.server.hdmi.HdmiLogger.error("Failed to get connection info : ", e, new java.lang.Object[0]);
                return false;
            }
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public void nativeSetHpdSignalType(int signal, int portId) {
            com.android.server.hdmi.HdmiLogger.error("Failed to set HPD signal type: not supported by HAL.", new java.lang.Object[0]);
        }

        @Override // com.android.server.hdmi.HdmiCecController.NativeWrapper
        public int nativeGetHpdSignalType(int portId) {
            com.android.server.hdmi.HdmiLogger.error("Failed to get HPD signal type: not supported by HAL.", new java.lang.Object[0]);
            return 0;
        }

        public void serviceDied(long cookie) {
            if (cookie == 353) {
                com.android.server.hdmi.HdmiLogger.error("Service died cookie : " + cookie + "; reconnecting", new java.lang.Object[0]);
                connectToHal();
                if (this.mCallback != null) {
                    setCallback(this.mCallback);
                }
            }
        }

        @Override // android.hardware.tv.cec.V1_0.IHdmiCec.getPhysicalAddressCallback
        public void onValues(int result, short addr) {
            if (result == 0) {
                synchronized (this.mLock) {
                    this.mPhysicalAddress = new java.lang.Short(addr).intValue();
                }
            }
        }
    }

    final class HdmiCecCallback {
        HdmiCecCallback() {
        }

        public void onCecMessage(final int initiator, final int destination, final byte[] body) {
            com.android.server.hdmi.HdmiCecController.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController$HdmiCecCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCecMessage$0(initiator, destination, body);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCecMessage$0(int initiator, int destination, byte[] body) {
            com.android.server.hdmi.HdmiCecController.this.handleIncomingCecCommand(initiator, destination, body);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHotplugEvent$1(int portId, boolean connected) {
            com.android.server.hdmi.HdmiCecController.this.handleHotplug(portId, connected);
        }

        public void onHotplugEvent(final int portId, final boolean connected) {
            com.android.server.hdmi.HdmiCecController.this.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecController$HdmiCecCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onHotplugEvent$1(portId, connected);
                }
            });
        }
    }

    private static final class HdmiCecCallback10 extends android.hardware.tv.cec.V1_0.IHdmiCecCallback.Stub {
        private final com.android.server.hdmi.HdmiCecController.HdmiCecCallback mHdmiCecCallback;

        HdmiCecCallback10(com.android.server.hdmi.HdmiCecController.HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        @Override // android.hardware.tv.cec.V1_0.IHdmiCecCallback
        public void onCecMessage(android.hardware.tv.cec.V1_0.CecMessage message) throws android.os.RemoteException {
            byte[] body = new byte[message.body.size()];
            for (int i = 0; i < message.body.size(); i++) {
                body[i] = message.body.get(i).byteValue();
            }
            this.mHdmiCecCallback.onCecMessage(message.initiator, message.destination, body);
        }

        @Override // android.hardware.tv.cec.V1_0.IHdmiCecCallback
        public void onHotplugEvent(android.hardware.tv.cec.V1_0.HotplugEvent event) throws android.os.RemoteException {
            this.mHdmiCecCallback.onHotplugEvent(event.portId, event.connected);
        }
    }

    private static final class HdmiCecCallback11 extends android.hardware.tv.cec.V1_1.IHdmiCecCallback.Stub {
        private final com.android.server.hdmi.HdmiCecController.HdmiCecCallback mHdmiCecCallback;

        HdmiCecCallback11(com.android.server.hdmi.HdmiCecController.HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        @Override // android.hardware.tv.cec.V1_1.IHdmiCecCallback
        public void onCecMessage_1_1(android.hardware.tv.cec.V1_1.CecMessage message) throws android.os.RemoteException {
            byte[] body = new byte[message.body.size()];
            for (int i = 0; i < message.body.size(); i++) {
                body[i] = message.body.get(i).byteValue();
            }
            this.mHdmiCecCallback.onCecMessage(message.initiator, message.destination, body);
        }

        @Override // android.hardware.tv.cec.V1_0.IHdmiCecCallback
        public void onCecMessage(android.hardware.tv.cec.V1_0.CecMessage message) throws android.os.RemoteException {
            byte[] body = new byte[message.body.size()];
            for (int i = 0; i < message.body.size(); i++) {
                body[i] = message.body.get(i).byteValue();
            }
            this.mHdmiCecCallback.onCecMessage(message.initiator, message.destination, body);
        }

        @Override // android.hardware.tv.cec.V1_0.IHdmiCecCallback
        public void onHotplugEvent(android.hardware.tv.cec.V1_0.HotplugEvent event) throws android.os.RemoteException {
            this.mHdmiCecCallback.onHotplugEvent(event.portId, event.connected);
        }
    }

    private static final class HdmiCecCallbackAidl extends android.hardware.tv.hdmi.cec.IHdmiCecCallback.Stub {
        private final com.android.server.hdmi.HdmiCecController.HdmiCecCallback mHdmiCecCallback;

        HdmiCecCallbackAidl(com.android.server.hdmi.HdmiCecController.HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
        public void onCecMessage(android.hardware.tv.hdmi.cec.CecMessage message) throws android.os.RemoteException {
            this.mHdmiCecCallback.onCecMessage(message.initiator, message.destination, message.body);
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
        public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
            return "cd956e3a0c2e6ade71693c85e9f0aeffa221ea26";
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
        public int getInterfaceVersion() throws android.os.RemoteException {
            return 1;
        }
    }

    private static final class HdmiConnectionCallbackAidl extends android.hardware.tv.hdmi.connection.IHdmiConnectionCallback.Stub {
        private final com.android.server.hdmi.HdmiCecController.HdmiCecCallback mHdmiCecCallback;

        HdmiConnectionCallbackAidl(com.android.server.hdmi.HdmiCecController.HdmiCecCallback hdmiCecCallback) {
            this.mHdmiCecCallback = hdmiCecCallback;
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
        public void onHotplugEvent(boolean connected, int portId) throws android.os.RemoteException {
            this.mHdmiCecCallback.onHotplugEvent(portId, connected);
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
        public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
            return "85c26fa47f3c3062aa93ffc8bb0897a85c8cb118";
        }

        @Override // android.hardware.tv.hdmi.connection.IHdmiConnectionCallback
        public int getInterfaceVersion() throws android.os.RemoteException {
            return 1;
        }
    }

    public static abstract class Dumpable {
        protected final long mTime = java.lang.System.currentTimeMillis();

        abstract void dump(com.android.internal.util.IndentingPrintWriter indentingPrintWriter, java.text.SimpleDateFormat simpleDateFormat);

        Dumpable() {
        }
    }

    private static final class MessageHistoryRecord extends com.android.server.hdmi.HdmiCecController.Dumpable {
        private final boolean mIsReceived;
        private final com.android.server.hdmi.HdmiCecMessage mMessage;
        private final java.util.List<java.lang.String> mSendResults;

        MessageHistoryRecord(boolean isReceived, com.android.server.hdmi.HdmiCecMessage message, java.util.List<java.lang.String> sendResults) {
            this.mIsReceived = isReceived;
            this.mMessage = message;
            this.mSendResults = sendResults;
        }

        @Override // com.android.server.hdmi.HdmiCecController.Dumpable
        void dump(com.android.internal.util.IndentingPrintWriter pw, java.text.SimpleDateFormat sdf) {
            pw.print(this.mIsReceived ? "[R]" : "[S]");
            pw.print(" time=");
            pw.print(sdf.format(new java.util.Date(this.mTime)));
            pw.print(" message=");
            pw.print(this.mMessage);
            java.lang.StringBuilder results = new java.lang.StringBuilder();
            if (!this.mIsReceived && this.mSendResults != null) {
                results.append(" (");
                results.append(java.lang.String.join(", ", this.mSendResults));
                results.append(")");
            }
            pw.println(results);
        }
    }

    private static final class HotplugHistoryRecord extends com.android.server.hdmi.HdmiCecController.Dumpable {
        private final boolean mConnected;
        private final int mPort;

        HotplugHistoryRecord(int port, boolean connected) {
            this.mPort = port;
            this.mConnected = connected;
        }

        @Override // com.android.server.hdmi.HdmiCecController.Dumpable
        void dump(com.android.internal.util.IndentingPrintWriter pw, java.text.SimpleDateFormat sdf) {
            pw.print("[H]");
            pw.print(" time=");
            pw.print(sdf.format(new java.util.Date(this.mTime)));
            pw.print(" hotplug port=");
            pw.print(this.mPort);
            pw.print(" connected=");
            pw.println(this.mConnected);
        }
    }
}
