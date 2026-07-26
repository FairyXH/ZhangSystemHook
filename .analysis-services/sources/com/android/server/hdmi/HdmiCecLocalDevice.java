package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
abstract class HdmiCecLocalDevice extends com.android.server.hdmi.HdmiLocalDevice {
    private static final int FOLLOWER_SAFETY_TIMEOUT = 550;
    private static final int MAX_HDMI_ACTIVE_SOURCE_HISTORY = 10;
    private static final int MSG_DISABLE_DEVICE_TIMEOUT = 1;
    private static final int MSG_USER_CONTROL_RELEASE_TIMEOUT = 2;
    private static final java.lang.String TAG = "HdmiCecLocalDevice";
    final java.util.ArrayList<com.android.server.hdmi.HdmiCecFeatureAction> mActions;
    private int mActiveRoutingPath;
    private final java.util.concurrent.ArrayBlockingQueue<com.android.server.hdmi.HdmiCecController.Dumpable> mActiveSourceHistory;
    protected final com.android.server.hdmi.HdmiCecMessageCache mCecMessageCache;
    private android.hardware.hdmi.HdmiDeviceInfo mDeviceInfo;
    private final android.os.Handler mHandler;
    protected int mLastKeyRepeatCount;
    protected int mLastKeycode;
    protected com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback mPendingActionClearedCallback;
    protected int mPreferredAddress;
    com.android.server.hdmi.HdmiCecStandbyModeHandler mStandbyHandler;

    interface PendingActionClearedCallback {
        void onCleared(com.android.server.hdmi.HdmiCecLocalDevice hdmiCecLocalDevice);
    }

    interface StandbyCompletedCallback {
        void onStandbyCompleted();
    }

    protected abstract int getPreferredAddress();

    protected abstract java.util.List<java.lang.Integer> getRcFeatures();

    protected abstract int getRcProfile();

    protected abstract void onAddressAllocated(int i, int i2);

    protected abstract void setPreferredAddress(int i);

    static class ActiveSource {
        int logicalAddress;
        int physicalAddress;

        public ActiveSource() {
            invalidate();
        }

        public ActiveSource(int logical, int physical) {
            this.logicalAddress = logical;
            this.physicalAddress = physical;
        }

        public static com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource of(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource source) {
            return new com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource(source.logicalAddress, source.physicalAddress);
        }

        public static com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource of(int logical, int physical) {
            return new com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource(logical, physical);
        }

        public boolean isValid() {
            return com.android.server.hdmi.HdmiUtils.isValidAddress(this.logicalAddress);
        }

        public void invalidate() {
            this.logicalAddress = -1;
            this.physicalAddress = 65535;
        }

        public boolean equals(int logical, int physical) {
            return this.logicalAddress == logical && this.physicalAddress == physical;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource)) {
                return false;
            }
            com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource that = (com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource) obj;
            return that.logicalAddress == this.logicalAddress && that.physicalAddress == this.physicalAddress;
        }

        public int hashCode() {
            return (this.logicalAddress * 29) + this.physicalAddress;
        }

        public java.lang.String toString() {
            java.lang.String logicalAddressString;
            java.lang.StringBuilder s = new java.lang.StringBuilder();
            if (this.logicalAddress == -1) {
                logicalAddressString = "invalid";
            } else {
                logicalAddressString = java.lang.String.format("0x%02x", java.lang.Integer.valueOf(this.logicalAddress));
            }
            s.append("(").append(logicalAddressString);
            java.lang.String physicalAddressString = this.physicalAddress != 65535 ? java.lang.String.format("0x%04x", java.lang.Integer.valueOf(this.physicalAddress)) : "invalid";
            s.append(", ").append(physicalAddressString).append(")");
            return s.toString();
        }
    }

    protected HdmiCecLocalDevice(com.android.server.hdmi.HdmiControlService service, int deviceType) {
        super(service, deviceType);
        this.mLastKeycode = -1;
        this.mLastKeyRepeatCount = 0;
        this.mActiveSourceHistory = new java.util.concurrent.ArrayBlockingQueue<>(10);
        this.mCecMessageCache = new com.android.server.hdmi.HdmiCecMessageCache();
        this.mActions = new java.util.ArrayList<>();
        this.mHandler = new android.os.Handler() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.hdmi.HdmiCecLocalDevice.this.handleDisableDeviceTimeout();
                        break;
                    case 2:
                        com.android.server.hdmi.HdmiCecLocalDevice.this.handleUserControlReleased();
                        break;
                }
            }
        };
    }

    static com.android.server.hdmi.HdmiCecLocalDevice create(com.android.server.hdmi.HdmiControlService service, int deviceType) {
        switch (deviceType) {
            case 0:
                return new com.android.server.hdmi.HdmiCecLocalDeviceTv(service);
            case 4:
                return new com.android.server.hdmi.HdmiCecLocalDevicePlayback(service);
            case 5:
                return new com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem(service);
            default:
                return null;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void init() {
        assertRunOnServiceThread();
        this.mPreferredAddress = getPreferredAddress();
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            handleDisableDeviceTimeout();
        }
        this.mPendingActionClearedCallback = null;
    }

    protected boolean isInputReady(int deviceId) {
        return true;
    }

    protected boolean canGoToStandby() {
        return true;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int dispatchMessage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int dest = message.getDestination();
        if (dest != this.mDeviceInfo.getLogicalAddress() && dest != 15) {
            return -2;
        }
        if (this.mService.isPowerStandby() && !this.mService.isWakeUpMessageReceived() && this.mStandbyHandler.handleCommand(message)) {
            return -1;
        }
        this.mCecMessageCache.cacheMessage(message);
        return onMessage(message);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected boolean isAlreadyActiveSource(android.hardware.hdmi.HdmiDeviceInfo targetDevice, int targetAddress, android.hardware.hdmi.IHdmiControlCallback callback) {
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource active = getActiveSource();
        if (targetDevice.getDevicePowerStatus() != 0 || !active.isValid() || targetAddress != active.logicalAddress) {
            return false;
        }
        invokeCallback(callback, 0);
        return true;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void clearDeviceInfoList() {
        assertRunOnServiceThread();
        this.mService.getHdmiCecNetwork().clearDeviceList();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected final int onMessage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (dispatchMessageToAction(message)) {
            return -1;
        }
        if (message instanceof com.android.server.hdmi.SetAudioVolumeLevelMessage) {
            return handleSetAudioVolumeLevel((com.android.server.hdmi.SetAudioVolumeLevelMessage) message);
        }
        switch (message.getOpcode()) {
            case 4:
                return handleImageViewOn(message);
            case 10:
                return handleRecordStatus(message);
            case 13:
                return handleTextViewOn(message);
            case 15:
                return handleRecordTvScreen(message);
            case 50:
                return handleSetMenuLanguage(message);
            case 53:
                return handleTimerStatus(message);
            case 54:
                return handleStandby(message);
            case 67:
                return handleTimerClearedStatus(message);
            case 68:
                return handleUserControlPressed(message);
            case 69:
                return handleUserControlReleased();
            case 70:
                return handleGiveOsdName(message);
            case 71:
                return handleSetOsdName(message);
            case 112:
                return handleSystemAudioModeRequest(message);
            case 113:
                return handleGiveAudioStatus(message);
            case 114:
                return handleSetSystemAudioMode(message);
            case 122:
                return handleReportAudioStatus(message);
            case 125:
                return handleGiveSystemAudioModeStatus(message);
            case 126:
                return handleSystemAudioModeStatus(message);
            case 128:
                return handleRoutingChange(message);
            case 129:
                return handleRoutingInformation(message);
            case 130:
                return handleActiveSource(message);
            case 131:
                return handleGivePhysicalAddress(message);
            case 132:
                return handleReportPhysicalAddress(message);
            case 133:
                return handleRequestActiveSource(message);
            case 134:
                return handleSetStreamPath(message);
            case 137:
                return handleVendorCommand(message);
            case 140:
                return handleGiveDeviceVendorId(message);
            case 141:
                return handleMenuRequest(message);
            case 142:
                return handleMenuStatus(message);
            case 143:
                return handleGiveDevicePowerStatus(message);
            case 144:
                return handleReportPowerStatus(message);
            case 145:
                return handleGetMenuLanguage(message);
            case 157:
                return handleInactiveSource(message);
            case 158:
                return handleCecVersion();
            case 159:
                return handleGetCecVersion(message);
            case 160:
                return handleVendorCommandWithId(message);
            case 163:
                return handleReportShortAudioDescriptor(message);
            case 164:
                return handleRequestShortAudioDescriptor(message);
            case 165:
                return handleGiveFeatures(message);
            case 192:
                return handleInitiateArc(message);
            case 193:
                return handleReportArcInitiate(message);
            case 194:
                return handleReportArcTermination(message);
            case 195:
                return handleRequestArcInitiate(message);
            case 196:
                return handleRequestArcTermination(message);
            case 197:
                return handleTerminateArc(message);
            default:
                return -2;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean dispatchMessageToAction(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        boolean processed = false;
        for (com.android.server.hdmi.HdmiCecFeatureAction action : new java.util.ArrayList(this.mActions)) {
            boolean result = action.processCommand(message);
            processed = processed || result;
        }
        return processed;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGivePhysicalAddress(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int physicalAddress = this.mService.getPhysicalAddress();
        if (physicalAddress == 65535) {
            this.mService.maySendFeatureAbortCommand(message, 5);
            return -1;
        }
        com.android.server.hdmi.HdmiCecMessage cecMessage = com.android.server.hdmi.HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(this.mDeviceInfo.getLogicalAddress(), physicalAddress, this.mDeviceType);
        this.mService.sendCecCommand(cecMessage);
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGiveDeviceVendorId(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int vendorId = this.mService.getVendorId();
        if (vendorId == 1) {
            this.mService.maySendFeatureAbortCommand(message, 5);
            return -1;
        }
        com.android.server.hdmi.HdmiCecMessage cecMessage = com.android.server.hdmi.HdmiCecMessageBuilder.buildDeviceVendorIdCommand(this.mDeviceInfo.getLogicalAddress(), vendorId);
        this.mService.sendCecCommand(cecMessage);
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGetCecVersion(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int version = this.mService.getCecVersion();
        com.android.server.hdmi.HdmiCecMessage cecMessage = com.android.server.hdmi.HdmiCecMessageBuilder.buildCecVersion(message.getDestination(), message.getSource(), version);
        this.mService.sendCecCommand(cecMessage);
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleCecVersion() {
        assertRunOnServiceThread();
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleInactiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGetMenuLanguage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        android.util.Slog.w(TAG, "Only TV can handle <Get Menu Language>:" + message.toString());
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSetMenuLanguage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        android.util.Slog.w(TAG, "Only Playback device can handle <Set Menu Language>:" + message.toString());
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGiveOsdName(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        buildAndSendSetOsdName(message.getSource());
        return -1;
    }

    protected void buildAndSendSetOsdName(int dest) {
        final com.android.server.hdmi.HdmiCecMessage cecMessage = com.android.server.hdmi.HdmiCecMessageBuilder.buildSetOsdNameCommand(this.mDeviceInfo.getLogicalAddress(), dest, this.mDeviceInfo.getDisplayName());
        if (cecMessage != null) {
            this.mService.sendCecCommand(cecMessage, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.2
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int error) {
                    if (error != 0) {
                        com.android.server.hdmi.HdmiLogger.debug("Failed to send cec command " + cecMessage, new java.lang.Object[0]);
                    }
                }
            });
        } else {
            android.util.Slog.w(TAG, "Failed to build <Get Osd Name>:" + this.mDeviceInfo.getDisplayName());
        }
    }

    protected int handleRoutingChange(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleRoutingInformation(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleReportPhysicalAddress(com.android.server.hdmi.HdmiCecMessage message) {
        int address = message.getSource();
        if (hasAction(com.android.server.hdmi.DeviceDiscoveryAction.class)) {
            android.util.Slog.i(TAG, "Ignored while Device Discovery Action is in progress: " + message);
            return -1;
        }
        android.hardware.hdmi.HdmiDeviceInfo cecDeviceInfo = this.mService.getHdmiCecNetwork().getCecDeviceInfo(address);
        if (!this.mService.isTvDevice() && cecDeviceInfo != null && cecDeviceInfo.getDisplayName().equals(com.android.server.hdmi.HdmiUtils.getDefaultDeviceName(address))) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveOsdNameCommand(this.mDeviceInfo.getLogicalAddress(), address));
        }
        return -1;
    }

    protected int handleSystemAudioModeStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleGiveSystemAudioModeStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleSetSystemAudioMode(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleSystemAudioModeRequest(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleTerminateArc(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleInitiateArc(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleRequestArcInitiate(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleRequestArcTermination(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleReportArcInitiate(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleReportArcTermination(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleReportAudioStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleGiveAudioStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleRequestShortAudioDescriptor(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleReportShortAudioDescriptor(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleSetAudioVolumeLevel(com.android.server.hdmi.SetAudioVolumeLevelMessage message) {
        return -2;
    }

    protected void preprocessBufferedMessages(java.util.List<com.android.server.hdmi.HdmiCecMessage> bufferedMessages) {
    }

    protected android.hardware.hdmi.DeviceFeatures computeDeviceFeatures() {
        return android.hardware.hdmi.DeviceFeatures.NO_FEATURES_SUPPORTED;
    }

    private void updateDeviceFeatures() {
        setDeviceInfo(getDeviceInfo().toBuilder().setDeviceFeatures(computeDeviceFeatures()).build());
    }

    protected final android.hardware.hdmi.DeviceFeatures getDeviceFeatures() {
        updateDeviceFeatures();
        return getDeviceInfo().getDeviceFeatures();
    }

    protected int handleGiveFeatures(com.android.server.hdmi.HdmiCecMessage message) {
        if (this.mService.getCecVersion() < 6) {
            return 0;
        }
        reportFeatures();
        return -1;
    }

    protected void reportFeatures() {
        int logicalAddress;
        java.util.List<java.lang.Integer> localDeviceTypes = new java.util.ArrayList<>();
        for (com.android.server.hdmi.HdmiCecLocalDevice localDevice : this.mService.getAllCecLocalDevices()) {
            localDeviceTypes.add(java.lang.Integer.valueOf(localDevice.mDeviceType));
        }
        int rcProfile = getRcProfile();
        java.util.List<java.lang.Integer> rcFeatures = getRcFeatures();
        android.hardware.hdmi.DeviceFeatures deviceFeatures = getDeviceFeatures();
        synchronized (this.mLock) {
            logicalAddress = this.mDeviceInfo.getLogicalAddress();
        }
        this.mService.sendCecCommand(com.android.server.hdmi.ReportFeaturesMessage.build(logicalAddress, this.mService.getCecVersion(), localDeviceTypes, rcProfile, rcFeatures, deviceFeatures));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleStandby(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (this.mService.isCecControlEnabled() && !this.mService.isProhibitMode() && this.mService.isPowerOnOrTransient()) {
            this.mService.standby();
            return -1;
        }
        return 1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleUserControlPressed(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        this.mHandler.removeMessages(2);
        if (this.mService.isPowerOnOrTransient() && isPowerOffOrToggleCommand(message)) {
            this.mService.standby();
            return -1;
        }
        if (this.mService.isPowerStandbyOrTransient() && isPowerOnOrToggleCommand(message)) {
            this.mService.wakeUp();
            return -1;
        }
        if (this.mService.getHdmiCecVolumeControl() == 0 && isVolumeOrMuteCommand(message)) {
            return 4;
        }
        if (isPowerOffOrToggleCommand(message) || isPowerOnOrToggleCommand(message)) {
            return -1;
        }
        long downTime = android.os.SystemClock.uptimeMillis();
        byte[] params = message.getParams();
        int keycode = com.android.server.hdmi.HdmiCecKeycode.cecKeycodeAndParamsToAndroidKey(params);
        int keyRepeatCount = 0;
        if (this.mLastKeycode != -1) {
            if (keycode == this.mLastKeycode) {
                keyRepeatCount = this.mLastKeyRepeatCount + 1;
            } else {
                injectKeyEvent(downTime, 1, this.mLastKeycode, 0);
            }
        }
        this.mLastKeycode = keycode;
        this.mLastKeyRepeatCount = keyRepeatCount;
        if (keycode != -1) {
            injectKeyEvent(downTime, 0, keycode, keyRepeatCount);
            this.mHandler.sendMessageDelayed(android.os.Message.obtain(this.mHandler, 2), 550L);
            return -1;
        }
        if (params.length > 0) {
            return handleUnmappedCecKeycode(params[0]);
        }
        return 3;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleUnmappedCecKeycode(int cecKeycode) {
        if (cecKeycode == 101) {
            this.mService.getAudioManager().adjustStreamVolume(3, -100, 1);
            return -1;
        }
        if (cecKeycode != 102) {
            return 3;
        }
        this.mService.getAudioManager().adjustStreamVolume(3, 100, 1);
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleUserControlReleased() {
        assertRunOnServiceThread();
        this.mHandler.removeMessages(2);
        this.mLastKeyRepeatCount = 0;
        if (this.mLastKeycode != -1) {
            long upTime = android.os.SystemClock.uptimeMillis();
            injectKeyEvent(upTime, 1, this.mLastKeycode, 0);
            this.mLastKeycode = -1;
        }
        return -1;
    }

    static void injectKeyEvent(long time, int action, int keycode, int repeat) {
        android.view.KeyEvent keyEvent = android.view.KeyEvent.obtain(time, time, action, keycode, repeat, 0, -1, 0, 8, 33554433, null);
        android.hardware.input.InputManagerGlobal.getInstance().injectInputEvent(keyEvent, 0);
        keyEvent.recycle();
    }

    static boolean isPowerOnOrToggleCommand(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] params = message.getParams();
        if (message.getOpcode() == 68) {
            return params[0] == 64 || params[0] == 109 || params[0] == 107;
        }
        return false;
    }

    static boolean isPowerOffOrToggleCommand(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] params = message.getParams();
        if (message.getOpcode() == 68) {
            return params[0] == 108 || params[0] == 107;
        }
        return false;
    }

    static boolean isVolumeOrMuteCommand(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] params = message.getParams();
        if (message.getOpcode() == 68) {
            return params[0] == 66 || params[0] == 65 || params[0] == 67 || params[0] == 101 || params[0] == 102;
        }
        return false;
    }

    protected int handleTextViewOn(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleImageViewOn(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleSetStreamPath(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleGiveDevicePowerStatus(com.android.server.hdmi.HdmiCecMessage message) {
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportPowerStatus(this.mDeviceInfo.getLogicalAddress(), message.getSource(), this.mService.getPowerStatus()));
        return -1;
    }

    protected int handleMenuRequest(com.android.server.hdmi.HdmiCecMessage message) {
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportMenuStatus(this.mDeviceInfo.getLogicalAddress(), message.getSource(), 0));
        return -1;
    }

    protected int handleMenuStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleVendorCommand(com.android.server.hdmi.HdmiCecMessage message) {
        if (!this.mService.invokeVendorCommandListenersOnReceived(this.mDeviceType, message.getSource(), message.getDestination(), message.getParams(), false)) {
            return 4;
        }
        return -1;
    }

    protected int handleVendorCommandWithId(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] params = message.getParams();
        com.android.server.hdmi.HdmiUtils.threeBytesToInt(params);
        if (message.getDestination() == 15 || message.getSource() == 15) {
            android.util.Slog.v(TAG, "Wrong broadcast vendor command. Ignoring");
            return -1;
        }
        if (!this.mService.invokeVendorCommandListenersOnReceived(this.mDeviceType, message.getSource(), message.getDestination(), params, true)) {
            return 4;
        }
        return -1;
    }

    protected void sendStandby(int deviceId) {
    }

    protected int handleSetOsdName(com.android.server.hdmi.HdmiCecMessage message) {
        return -1;
    }

    protected int handleRecordTvScreen(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleTimerClearedStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleReportPowerStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -1;
    }

    protected int handleTimerStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    protected int handleRecordStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -2;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    final void handleAddressAllocated(int logicalAddress, java.util.List<com.android.server.hdmi.HdmiCecMessage> bufferedMessages, int reason) {
        assertRunOnServiceThread();
        preprocessBufferedMessages(bufferedMessages);
        this.mPreferredAddress = logicalAddress;
        updateDeviceFeatures();
        if (this.mService.getCecVersion() >= 6) {
            reportFeatures();
        }
        onAddressAllocated(logicalAddress, reason);
        setPreferredAddress(logicalAddress);
    }

    int getType() {
        return this.mDeviceType;
    }

    android.hardware.hdmi.HdmiDeviceInfo getDeviceInfo() {
        android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo;
        synchronized (this.mLock) {
            hdmiDeviceInfo = this.mDeviceInfo;
        }
        return hdmiDeviceInfo;
    }

    void setDeviceInfo(android.hardware.hdmi.HdmiDeviceInfo info) {
        synchronized (this.mLock) {
            this.mDeviceInfo = info;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isAddressOf(int addr) {
        assertRunOnServiceThread();
        return addr == this.mDeviceInfo.getLogicalAddress();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void addAndStartAction(com.android.server.hdmi.HdmiCecFeatureAction action) {
        assertRunOnServiceThread();
        this.mActions.add(action);
        if (this.mService.isPowerStandby() || !this.mService.isAddressAllocated()) {
            if (action.getClass() == com.android.server.hdmi.ResendCecCommandAction.class) {
                android.util.Slog.i(TAG, "Not ready to start ResendCecCommandAction. This action is cancelled.");
                removeAction(action);
                return;
            } else {
                android.util.Slog.i(TAG, "Not ready to start action. Queued for deferred start:" + action);
                return;
            }
        }
        action.start();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startNewAvbAudioStatusAction(int targetAddress) {
        assertRunOnServiceThread();
        removeAction(com.android.server.hdmi.AbsoluteVolumeAudioStatusAction.class);
        addAndStartAction(new com.android.server.hdmi.AbsoluteVolumeAudioStatusAction(this, targetAddress));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void removeAvbAudioStatusAction() {
        assertRunOnServiceThread();
        removeAction(com.android.server.hdmi.AbsoluteVolumeAudioStatusAction.class);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void updateAvbVolume(int volumeIndex) {
        assertRunOnServiceThread();
        for (com.android.server.hdmi.AbsoluteVolumeAudioStatusAction action : getActions(com.android.server.hdmi.AbsoluteVolumeAudioStatusAction.class)) {
            action.updateVolume(volumeIndex);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void requestAndUpdateAvbAudioStatus() {
        assertRunOnServiceThread();
        for (com.android.server.hdmi.AbsoluteVolumeAudioStatusAction action : getActions(com.android.server.hdmi.AbsoluteVolumeAudioStatusAction.class)) {
            action.requestAndUpdateAudioStatus();
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void querySetAudioVolumeLevelSupport(final int targetAddress) {
        assertRunOnServiceThread();
        if (this.mService.getCecVersion() >= 6) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveFeatures(getDeviceInfo().getLogicalAddress(), targetAddress));
        }
        java.util.List<com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction> savlDiscoveryActions = getActions(com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction.class);
        if (savlDiscoveryActions.stream().noneMatch(new java.util.function.Predicate() { // from class: com.android.server.hdmi.HdmiCecLocalDevice$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.hdmi.HdmiCecLocalDevice.lambda$querySetAudioVolumeLevelSupport$0(targetAddress, (com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction) obj);
            }
        })) {
            addAndStartAction(new com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction(this, targetAddress, new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.3
                public void onComplete(int result) {
                    if (result == 0) {
                        com.android.server.hdmi.HdmiCecLocalDevice.this.getService().checkAndUpdateAbsoluteVolumeBehavior();
                    }
                }
            }));
        }
    }

    static /* synthetic */ boolean lambda$querySetAudioVolumeLevelSupport$0(int targetAddress, com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction a) {
        return a.getTargetAddress() == targetAddress;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startQueuedActions() {
        assertRunOnServiceThread();
        for (com.android.server.hdmi.HdmiCecFeatureAction action : new java.util.ArrayList(this.mActions)) {
            if (!action.started()) {
                android.util.Slog.i(TAG, "Starting queued action:" + action);
                action.start();
            }
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    <T extends com.android.server.hdmi.HdmiCecFeatureAction> boolean hasAction(java.lang.Class<T> clazz) {
        assertRunOnServiceThread();
        for (com.android.server.hdmi.HdmiCecFeatureAction action : this.mActions) {
            if (action.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    <T extends com.android.server.hdmi.HdmiCecFeatureAction> java.util.List<T> getActions(java.lang.Class<T> cls) {
        assertRunOnServiceThread();
        java.util.ArrayList arrayList = (java.util.List<T>) java.util.Collections.emptyList();
        for (com.android.server.hdmi.HdmiCecFeatureAction hdmiCecFeatureAction : this.mActions) {
            if (hdmiCecFeatureAction.getClass().equals(cls)) {
                if (arrayList.isEmpty()) {
                    arrayList = new java.util.ArrayList();
                }
                arrayList.add(hdmiCecFeatureAction);
            }
        }
        return (java.util.List<T>) arrayList;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void removeAction(com.android.server.hdmi.HdmiCecFeatureAction action) {
        assertRunOnServiceThread();
        action.finish(false);
        this.mActions.remove(action);
        checkIfPendingActionsCleared();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    <T extends com.android.server.hdmi.HdmiCecFeatureAction> void removeAction(java.lang.Class<T> clazz) {
        assertRunOnServiceThread();
        removeActionExcept(clazz, null);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void removeAllActions() {
        assertRunOnServiceThread();
        for (com.android.server.hdmi.HdmiCecFeatureAction action : this.mActions) {
            action.finish(false);
        }
        this.mActions.clear();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    <T extends com.android.server.hdmi.HdmiCecFeatureAction> void removeActionExcept(java.lang.Class<T> clazz, com.android.server.hdmi.HdmiCecFeatureAction exception) {
        assertRunOnServiceThread();
        java.util.Iterator<com.android.server.hdmi.HdmiCecFeatureAction> iter = this.mActions.iterator();
        while (iter.hasNext()) {
            com.android.server.hdmi.HdmiCecFeatureAction action = iter.next();
            if (action != exception && action.getClass().equals(clazz)) {
                action.finish(false);
                iter.remove();
            }
        }
        checkIfPendingActionsCleared();
    }

    protected void checkIfPendingActionsCleared() {
        if (this.mActions.isEmpty() && this.mPendingActionClearedCallback != null) {
            com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback callback = this.mPendingActionClearedCallback;
            this.mPendingActionClearedCallback = null;
            callback.onCleared(this);
        }
    }

    protected void assertRunOnServiceThread() {
        if (android.os.Looper.myLooper() != this.mService.getServiceLooper()) {
            throw new java.lang.IllegalStateException("Should run on service thread.");
        }
    }

    void onHotplug(int portId, boolean connected) {
    }

    final com.android.server.hdmi.HdmiControlService getService() {
        return this.mService;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    final boolean isConnectedToArcPort(int path) {
        assertRunOnServiceThread();
        return this.mService.isConnectedToArcPort(path);
    }

    com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource getActiveSource() {
        return this.mService.getLocalActiveSource();
    }

    void setActiveSource(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource newActive, java.lang.String caller) {
        setActiveSource(newActive.logicalAddress, newActive.physicalAddress, caller);
    }

    void setActiveSource(android.hardware.hdmi.HdmiDeviceInfo info, java.lang.String caller) {
        setActiveSource(info.getLogicalAddress(), info.getPhysicalAddress(), caller);
    }

    void setActiveSource(int logicalAddress, int physicalAddress, java.lang.String caller) {
        this.mService.setActiveSource(logicalAddress, physicalAddress, caller);
        this.mService.setLastInputForMhl(-1);
    }

    int getActivePath() {
        int i;
        synchronized (this.mLock) {
            i = this.mActiveRoutingPath;
        }
        return i;
    }

    void setActivePath(int path) {
        synchronized (this.mLock) {
            this.mActiveRoutingPath = path;
        }
        this.mService.setActivePortId(pathToPortId(path));
    }

    int getActivePortId() {
        int iPathToPortId;
        synchronized (this.mLock) {
            iPathToPortId = this.mService.pathToPortId(this.mActiveRoutingPath);
        }
        return iPathToPortId;
    }

    void setActivePortId(int portId) {
        setActivePath(this.mService.portIdToPath(portId));
    }

    int getPortId(int physicalAddress) {
        return this.mService.pathToPortId(physicalAddress);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    com.android.server.hdmi.HdmiCecMessageCache getCecMessageCache() {
        assertRunOnServiceThread();
        return this.mCecMessageCache;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int pathToPortId(int newPath) {
        assertRunOnServiceThread();
        return this.mService.pathToPortId(newPath);
    }

    protected void onStandby(boolean initiatedByCec, int standbyAction, com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback) {
    }

    protected void onStandby(boolean initiatedByCec, int standbyAction) {
        onStandby(initiatedByCec, standbyAction, null);
    }

    protected void onInitializeCecComplete(int initiatedBy) {
    }

    protected void disableDevice(boolean initiatedByCec, final com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback originalCallback) {
        removeAction(com.android.server.hdmi.SetAudioVolumeLevelDiscoveryAction.class);
        removeAction(com.android.server.hdmi.ActiveSourceAction.class);
        removeAction(com.android.server.hdmi.ResendCecCommandAction.class);
        this.mPendingActionClearedCallback = new com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevice.4
            @Override // com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback
            public void onCleared(com.android.server.hdmi.HdmiCecLocalDevice device) {
                com.android.server.hdmi.HdmiCecLocalDevice.this.mHandler.removeMessages(1);
                originalCallback.onCleared(device);
            }
        };
        this.mHandler.sendMessageDelayed(android.os.Message.obtain(this.mHandler, 1), 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void handleDisableDeviceTimeout() {
        assertRunOnServiceThread();
        java.util.Iterator<com.android.server.hdmi.HdmiCecFeatureAction> iter = this.mActions.iterator();
        while (iter.hasNext()) {
            com.android.server.hdmi.HdmiCecFeatureAction action = iter.next();
            action.finish(false);
            iter.remove();
        }
        if (this.mPendingActionClearedCallback != null) {
            this.mPendingActionClearedCallback.onCleared(this);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void sendKeyEvent(int keyCode, boolean isPressed) {
        assertRunOnServiceThread();
        if (!com.android.server.hdmi.HdmiCecKeycode.isSupportedKeycode(keyCode)) {
            android.util.Slog.w(TAG, "Unsupported key: " + keyCode);
            return;
        }
        java.util.List<com.android.server.hdmi.SendKeyAction> action = getActions(com.android.server.hdmi.SendKeyAction.class);
        int logicalAddress = findKeyReceiverAddress();
        if (logicalAddress == -1 || logicalAddress == this.mDeviceInfo.getLogicalAddress()) {
            android.util.Slog.w(TAG, "Discard key event: " + keyCode + ", pressed:" + isPressed + ", receiverAddr=" + logicalAddress);
        } else if (!action.isEmpty()) {
            action.get(0).processKeyEvent(keyCode, isPressed);
        } else if (isPressed) {
            addAndStartAction(new com.android.server.hdmi.SendKeyAction(this, logicalAddress, keyCode));
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void sendVolumeKeyEvent(int keyCode, boolean isPressed) {
        assertRunOnServiceThread();
        if (this.mService.getHdmiCecVolumeControl() == 0) {
            return;
        }
        if (!com.android.server.hdmi.HdmiCecKeycode.isVolumeKeycode(keyCode)) {
            android.util.Slog.w(TAG, "Not a volume key: " + keyCode);
            return;
        }
        java.util.List<com.android.server.hdmi.SendKeyAction> action = getActions(com.android.server.hdmi.SendKeyAction.class);
        final int logicalAddress = findAudioReceiverAddress();
        if (logicalAddress == -1 || this.mService.getAllCecLocalDevices().stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.hdmi.HdmiCecLocalDevice$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.hdmi.HdmiCecLocalDevice.lambda$sendVolumeKeyEvent$1(logicalAddress, (com.android.server.hdmi.HdmiCecLocalDevice) obj);
            }
        })) {
            android.util.Slog.w(TAG, "Discard volume key event: " + keyCode + ", pressed:" + isPressed + ", receiverAddr=" + logicalAddress);
        } else if (!action.isEmpty()) {
            action.get(0).processKeyEvent(keyCode, isPressed);
        } else if (isPressed) {
            addAndStartAction(new com.android.server.hdmi.SendKeyAction(this, logicalAddress, keyCode));
        }
    }

    static /* synthetic */ boolean lambda$sendVolumeKeyEvent$1(int logicalAddress, com.android.server.hdmi.HdmiCecLocalDevice device) {
        return device.getDeviceInfo().getLogicalAddress() == logicalAddress;
    }

    protected int findKeyReceiverAddress() {
        android.util.Slog.w(TAG, "findKeyReceiverAddress is not implemented");
        return -1;
    }

    protected int findAudioReceiverAddress() {
        android.util.Slog.w(TAG, "findAudioReceiverAddress is not implemented");
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void invokeCallback(android.hardware.hdmi.IHdmiControlCallback callback, int result) {
        assertRunOnServiceThread();
        if (callback == null) {
            return;
        }
        try {
            callback.onComplete(result);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Invoking callback failed:" + e);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void invokeStandbyCompletedCallback(com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback) {
        assertRunOnServiceThread();
        if (callback == null) {
            return;
        }
        callback.onStandbyCompleted();
    }

    void sendUserControlPressedAndReleased(int targetAddress, int cecKeycode) {
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlPressed(this.mDeviceInfo.getLogicalAddress(), targetAddress, cecKeycode));
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildUserControlReleased(this.mDeviceInfo.getLogicalAddress(), targetAddress));
    }

    void addActiveSourceHistoryItem(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource, boolean isActiveSource, java.lang.String caller) {
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSourceHistoryRecord record = new com.android.server.hdmi.HdmiCecLocalDevice.ActiveSourceHistoryRecord(activeSource, isActiveSource, caller);
        if (!this.mActiveSourceHistory.offer(record)) {
            this.mActiveSourceHistory.poll();
            this.mActiveSourceHistory.offer(record);
        }
    }

    public java.util.concurrent.ArrayBlockingQueue<com.android.server.hdmi.HdmiCecController.Dumpable> getActiveSourceHistory() {
        return this.mActiveSourceHistory;
    }

    protected void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("mDeviceType: " + this.mDeviceType);
        pw.println("mPreferredAddress: " + this.mPreferredAddress);
        pw.println("mDeviceInfo: " + this.mDeviceInfo);
        pw.println("mActiveSource: " + getActiveSource());
        pw.println(java.lang.String.format("mActiveRoutingPath: 0x%04x", java.lang.Integer.valueOf(this.mActiveRoutingPath)));
    }

    protected int getActivePathOnSwitchFromActivePortId(int activePortId) {
        int myPhysicalAddress = this.mService.getPhysicalAddress();
        int finalMask = activePortId << 8;
        for (int mask = 3840; mask > 15 && (myPhysicalAddress & mask) != 0; mask >>= 4) {
            finalMask >>= 4;
        }
        return finalMask | myPhysicalAddress;
    }

    private static final class ActiveSourceHistoryRecord extends com.android.server.hdmi.HdmiCecController.Dumpable {
        private final com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource mActiveSource;
        private final java.lang.String mCaller;
        private final boolean mIsActiveSource;

        private ActiveSourceHistoryRecord(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource mActiveSource, boolean mIsActiveSource, java.lang.String caller) {
            this.mActiveSource = mActiveSource;
            this.mIsActiveSource = mIsActiveSource;
            this.mCaller = caller;
        }

        @Override // com.android.server.hdmi.HdmiCecController.Dumpable
        void dump(com.android.internal.util.IndentingPrintWriter pw, java.text.SimpleDateFormat sdf) {
            pw.print("time=");
            pw.print(sdf.format(new java.util.Date(this.mTime)));
            pw.print(" active source=");
            pw.print(this.mActiveSource);
            pw.print(" isActiveSource=");
            pw.print(this.mIsActiveSource);
            pw.print(" from=");
            pw.println(this.mCaller);
        }
    }
}
