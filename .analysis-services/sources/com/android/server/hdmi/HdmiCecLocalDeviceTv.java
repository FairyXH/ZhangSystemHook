package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public final class HdmiCecLocalDeviceTv extends com.android.server.hdmi.HdmiCecLocalDevice {
    private static final java.lang.String TAG = "HdmiCecLocalDeviceTv";

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mArcEstablished;
    private final android.util.SparseBooleanArray mArcFeatureEnabled;
    private final com.android.server.hdmi.DelayedMessageBuffer mDelayedMessageBuffer;
    private int mPrevPortId;
    private final java.lang.Runnable mResetSkipRoutingControlRunnable;
    private com.android.server.hdmi.SelectRequestBuffer mSelectRequestBuffer;
    private boolean mSkipRoutingControl;
    private final android.os.Handler mSkipRoutingControlHandler;
    private boolean mSystemAudioControlFeatureEnabled;
    private boolean mSystemAudioMute;
    private int mSystemAudioVolume;
    private final android.media.tv.TvInputManager.TvInputCallback mTvInputCallback;
    private final java.util.HashMap<java.lang.String, java.lang.Integer> mTvInputs;

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    public /* bridge */ /* synthetic */ java.util.concurrent.ArrayBlockingQueue getActiveSourceHistory() {
        return super.getActiveSourceHistory();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public /* bridge */ /* synthetic */ void invokeStandbyCompletedCallback(com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback standbyCompletedCallback) {
        super.invokeStandbyCompletedCallback(standbyCompletedCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mSkipRoutingControl = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void addTvInput(java.lang.String inputId, int deviceId) {
        assertRunOnServiceThread();
        this.mTvInputs.put(inputId, java.lang.Integer.valueOf(deviceId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void removeTvInput(java.lang.String inputId) {
        assertRunOnServiceThread();
        this.mTvInputs.remove(inputId);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected boolean isInputReady(int deviceId) {
        assertRunOnServiceThread();
        return this.mTvInputs.containsValue(java.lang.Integer.valueOf(deviceId));
    }

    HdmiCecLocalDeviceTv(com.android.server.hdmi.HdmiControlService service) {
        super(service, 0);
        this.mArcEstablished = false;
        this.mArcFeatureEnabled = new android.util.SparseBooleanArray();
        this.mSystemAudioVolume = -1;
        this.mSystemAudioMute = false;
        this.mResetSkipRoutingControlRunnable = new java.lang.Runnable() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        this.mDelayedMessageBuffer = new com.android.server.hdmi.DelayedMessageBuffer(this);
        this.mTvInputCallback = new android.media.tv.TvInputManager.TvInputCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv.1
            @Override // android.media.tv.TvInputManager.TvInputCallback
            public void onInputAdded(java.lang.String inputId) {
                android.hardware.hdmi.HdmiDeviceInfo info;
                android.media.tv.TvInputInfo tvInfo = com.android.server.hdmi.HdmiCecLocalDeviceTv.this.mService.getTvInputManager().getTvInputInfo(inputId);
                if (tvInfo == null || (info = tvInfo.getHdmiDeviceInfo()) == null) {
                    return;
                }
                com.android.server.hdmi.HdmiCecLocalDeviceTv.this.addTvInput(inputId, info.getId());
                if (info.isCecDevice()) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.processDelayedActiveSource(info.getLogicalAddress());
                }
            }

            @Override // android.media.tv.TvInputManager.TvInputCallback
            public void onInputRemoved(java.lang.String inputId) {
                com.android.server.hdmi.HdmiCecLocalDeviceTv.this.removeTvInput(inputId);
            }
        };
        this.mTvInputs = new java.util.HashMap<>();
        this.mPrevPortId = -1;
        this.mSystemAudioControlFeatureEnabled = service.getHdmiCecConfig().getIntValue("system_audio_control") == 1;
        this.mStandbyHandler = new com.android.server.hdmi.HdmiCecStandbyModeHandler(service, this);
        this.mSkipRoutingControlHandler = new android.os.Handler(service.getServiceLooper());
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onAddressAllocated(int logicalAddress, int reason) {
        assertRunOnServiceThread();
        java.util.List<android.hardware.hdmi.HdmiPortInfo> ports = this.mService.getPortInfo();
        for (android.hardware.hdmi.HdmiPortInfo port : ports) {
            this.mArcFeatureEnabled.put(port.getId(), port.isArcSupported());
        }
        this.mService.registerTvInputCallback(this.mTvInputCallback);
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress(), this.mDeviceType));
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildDeviceVendorIdCommand(getDeviceInfo().getLogicalAddress(), this.mService.getVendorId()));
        this.mService.getHdmiCecNetwork().addCecSwitch(this.mService.getHdmiCecNetwork().getPhysicalAddress());
        this.mTvInputs.clear();
        boolean z = false;
        this.mSkipRoutingControl = reason == 3;
        this.mSkipRoutingControlHandler.removeCallbacks(this.mResetSkipRoutingControlRunnable);
        if (this.mSkipRoutingControl) {
            this.mSkipRoutingControlHandler.postDelayed(this.mResetSkipRoutingControlRunnable, 2000L);
        }
        if (reason != 0 && reason != 1) {
            z = true;
        }
        launchRoutingControl(z);
        resetSelectRequestBuffer();
        launchDeviceDiscovery();
        startQueuedActions();
        if (!this.mDelayedMessageBuffer.isBuffered(130)) {
            if (hasAction(com.android.server.hdmi.RequestActiveSourceAction.class)) {
                android.util.Slog.i(TAG, "RequestActiveSourceAction is in progress. Restarting.");
                removeAction(com.android.server.hdmi.RequestActiveSourceAction.class);
            }
            addAndStartAction(new com.android.server.hdmi.RequestActiveSourceAction(this, new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv.2
                public void onComplete(int result) {
                    if (!com.android.server.hdmi.HdmiCecLocalDeviceTv.this.mService.getLocalActiveSource().isValid() && result != 0) {
                        com.android.server.hdmi.HdmiCecLocalDeviceTv.this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildActiveSource(com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getDeviceInfo().getLogicalAddress(), com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getDeviceInfo().getPhysicalAddress()));
                        com.android.server.hdmi.HdmiCecLocalDeviceTv.this.updateActiveSource(com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getDeviceInfo().getLogicalAddress(), com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getDeviceInfo().getPhysicalAddress(), "RequestActiveSourceAction#finishWithCallback()");
                    }
                }
            }));
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void setSelectRequestBuffer(com.android.server.hdmi.SelectRequestBuffer requestBuffer) {
        assertRunOnServiceThread();
        this.mSelectRequestBuffer = requestBuffer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void resetSelectRequestBuffer() {
        assertRunOnServiceThread();
        setSelectRequestBuffer(com.android.server.hdmi.SelectRequestBuffer.EMPTY_BUFFER);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int getPreferredAddress() {
        return 0;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void setPreferredAddress(int addr) {
        android.util.Slog.w(TAG, "Preferred addres will not be stored for TV");
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int dispatchMessage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (this.mService.isPowerStandby() && !this.mService.isWakeUpMessageReceived() && this.mStandbyHandler.handleCommand(message)) {
            return -1;
        }
        return super.onMessage(message);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void deviceSelect(int id, android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo targetDevice = this.mService.getHdmiCecNetwork().getDeviceInfo(id);
        if (targetDevice == null) {
            invokeCallback(callback, 3);
            return;
        }
        int targetAddress = targetDevice.getLogicalAddress();
        if (isAlreadyActiveSource(targetDevice, targetAddress, callback)) {
            return;
        }
        removeAction(com.android.server.hdmi.RequestActiveSourceAction.class);
        if (targetAddress == 0) {
            handleSelectInternalSource();
            setActiveSource(targetAddress, this.mService.getPhysicalAddress(), "HdmiCecLocalDeviceTv#deviceSelect()");
            setActivePath(this.mService.getPhysicalAddress());
            invokeCallback(callback, 0);
            return;
        }
        if (!this.mService.isCecControlEnabled()) {
            setActiveSource(targetDevice, "HdmiCecLocalDeviceTv#deviceSelect()");
            invokeCallback(callback, 6);
        } else {
            removeAction(com.android.server.hdmi.DeviceSelectActionFromTv.class);
            addAndStartAction(new com.android.server.hdmi.DeviceSelectActionFromTv(this, targetDevice, callback));
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleSelectInternalSource() {
        assertRunOnServiceThread();
        if (this.mService.isCecControlEnabled() && getActiveSource().logicalAddress != getDeviceInfo().getLogicalAddress()) {
            updateActiveSource(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress(), "HdmiCecLocalDeviceTv#handleSelectInternalSource()");
            if (this.mSkipRoutingControl) {
                this.mSkipRoutingControl = false;
            } else {
                com.android.server.hdmi.HdmiCecMessage activeSource = com.android.server.hdmi.HdmiCecMessageBuilder.buildActiveSource(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress());
                this.mService.sendCecCommand(activeSource);
            }
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void updateActiveSource(int logicalAddress, int physicalAddress, java.lang.String caller) {
        assertRunOnServiceThread();
        updateActiveSource(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource.of(logicalAddress, physicalAddress), caller);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void updateActiveSource(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource newActive, java.lang.String caller) {
        assertRunOnServiceThread();
        if (getActiveSource().equals(newActive)) {
            return;
        }
        setActiveSource(newActive, caller);
        int logicalAddress = newActive.logicalAddress;
        if (this.mService.getHdmiCecNetwork().getCecDeviceInfo(logicalAddress) != null && logicalAddress != getDeviceInfo().getLogicalAddress() && this.mService.pathToPortId(newActive.physicalAddress) == getActivePortId()) {
            setPrevPortId(getActivePortId());
        }
    }

    int getPrevPortId() {
        int i;
        synchronized (this.mLock) {
            i = this.mPrevPortId;
        }
        return i;
    }

    void setPrevPortId(int portId) {
        synchronized (this.mLock) {
            this.mPrevPortId = portId;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void updateActiveInput(int path, boolean notifyInputChange) {
        assertRunOnServiceThread();
        setActivePath(path);
        if (notifyInputChange) {
            com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource = getActiveSource();
            android.hardware.hdmi.HdmiDeviceInfo info = this.mService.getHdmiCecNetwork().getCecDeviceInfo(activeSource.logicalAddress);
            if (info == null && (info = this.mService.getDeviceInfoByPort(getActivePortId())) == null) {
                info = android.hardware.hdmi.HdmiDeviceInfo.hardwarePort(path, getActivePortId());
            }
            this.mService.invokeInputChangeListener(info);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void doManualPortSwitching(int portId, android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        if (!this.mService.isValidPortId(portId)) {
            invokeCallback(callback, 6);
            return;
        }
        if (portId == getActivePortId()) {
            invokeCallback(callback, 0);
            return;
        }
        getActiveSource().invalidate();
        if (!this.mService.isCecControlEnabled()) {
            setActivePortId(portId);
            invokeCallback(callback, 6);
            return;
        }
        int oldPath = (getActivePortId() == -1 || getActivePortId() == 0) ? getDeviceInfo().getPhysicalAddress() : this.mService.portIdToPath(getActivePortId());
        setActivePath(oldPath);
        if (this.mSkipRoutingControl) {
            this.mSkipRoutingControl = false;
        } else {
            int newPath = this.mService.portIdToPath(portId);
            startRoutingControl(oldPath, newPath, callback);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startRoutingControl(int oldPath, int newPath, android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        if (oldPath == newPath) {
            return;
        }
        com.android.server.hdmi.HdmiCecMessage routingChange = com.android.server.hdmi.HdmiCecMessageBuilder.buildRoutingChange(getDeviceInfo().getLogicalAddress(), oldPath, newPath);
        this.mService.sendCecCommand(routingChange);
        removeAction(com.android.server.hdmi.RoutingControlAction.class);
        addAndStartAction(new com.android.server.hdmi.RoutingControlAction(this, newPath, callback));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int getPowerStatus() {
        assertRunOnServiceThread();
        return this.mService.getPowerStatus();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findKeyReceiverAddress() {
        if (getActiveSource().isValid()) {
            return getActiveSource().logicalAddress;
        }
        android.hardware.hdmi.HdmiDeviceInfo info = this.mService.getHdmiCecNetwork().getDeviceInfoByPath(getActivePath());
        if (info != null) {
            return info.getLogicalAddress();
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findAudioReceiverAddress() {
        return 5;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int logicalAddress = message.getSource();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        android.hardware.hdmi.HdmiDeviceInfo info = this.mService.getHdmiCecNetwork().getCecDeviceInfo(logicalAddress);
        if (info == null) {
            if (!handleNewDeviceAtTheTailOfActivePath(physicalAddress)) {
                com.android.server.hdmi.HdmiLogger.debug("Device info %X not found; buffering the command", java.lang.Integer.valueOf(logicalAddress));
                this.mDelayedMessageBuffer.add(message);
                return -1;
            }
            return -1;
        }
        if (isInputReady(info.getId()) || info.getDeviceType() == 5) {
            this.mService.getHdmiCecNetwork().updateDevicePowerStatus(logicalAddress, 0);
            com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource = com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource.of(logicalAddress, physicalAddress);
            com.android.server.hdmi.ActiveSourceHandler.create(this, null).process(activeSource, info.getDeviceType());
            return -1;
        }
        com.android.server.hdmi.HdmiLogger.debug("Input not ready for device: %X; buffering the command", java.lang.Integer.valueOf(info.getId()));
        this.mDelayedMessageBuffer.add(message);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleStandby(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (getActiveSource().logicalAddress != message.getSource()) {
            android.util.Slog.d(TAG, "<Standby> was not sent by the current active source, ignoring. Current active source has logical address " + getActiveSource().logicalAddress);
            return -1;
        }
        return super.handleStandby(message);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleInactiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (getActiveSource().logicalAddress != message.getSource() || isProhibitMode()) {
            return -1;
        }
        int portId = getPrevPortId();
        if (portId != -1) {
            android.hardware.hdmi.HdmiDeviceInfo inactiveSource = this.mService.getHdmiCecNetwork().getCecDeviceInfo(message.getSource());
            if (inactiveSource == null || this.mService.pathToPortId(inactiveSource.getPhysicalAddress()) == portId) {
                return -1;
            }
            doManualPortSwitching(portId, null);
            setPrevPortId(-1);
        } else {
            getActiveSource().invalidate();
            setActivePath(65535);
            this.mService.invokeInputChangeListener(android.hardware.hdmi.HdmiDeviceInfo.INACTIVE_DEVICE);
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (getDeviceInfo().getLogicalAddress() == getActiveSource().logicalAddress) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildActiveSource(getDeviceInfo().getLogicalAddress(), getActivePath()));
            return -1;
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGetMenuLanguage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!broadcastMenuLanguage(this.mService.getLanguage())) {
            android.util.Slog.w(TAG, "Failed to respond to <Get Menu Language>: " + message.toString());
            return -1;
        }
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean broadcastMenuLanguage(java.lang.String language) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiCecMessage command = com.android.server.hdmi.HdmiCecMessageBuilder.buildSetMenuLanguageCommand(getDeviceInfo().getLogicalAddress(), language);
        if (command != null) {
            this.mService.sendCecCommand(command);
            return true;
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleReportPhysicalAddress(com.android.server.hdmi.HdmiCecMessage message) {
        super.handleReportPhysicalAddress(message);
        int path = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        int address = message.getSource();
        int type = message.getParams()[2];
        if (!this.mService.getHdmiCecNetwork().isInDeviceList(address, path)) {
            handleNewDeviceAtTheTailOfActivePath(path);
        }
        startNewDeviceAction(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource.of(address, path), type);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleTimerStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleRecordStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -1;
    }

    void startNewDeviceAction(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource, int deviceType) {
        for (com.android.server.hdmi.NewDeviceAction action : getActions(com.android.server.hdmi.NewDeviceAction.class)) {
            if (action.isActionOf(activeSource)) {
                return;
            }
        }
        addAndStartAction(new com.android.server.hdmi.NewDeviceAction(this, activeSource.logicalAddress, activeSource.physicalAddress, deviceType));
    }

    private boolean handleNewDeviceAtTheTailOfActivePath(int path) {
        if (isTailOfActivePath(path, getActivePath())) {
            int newPath = this.mService.portIdToPath(getActivePortId());
            setActivePath(newPath);
            startRoutingControl(getActivePath(), newPath, null);
            return true;
        }
        return false;
    }

    static boolean isTailOfActivePath(int path, int activePath) {
        if (activePath == 0) {
            return false;
        }
        for (int i = 12; i >= 0; i -= 4) {
            int curActivePath = (activePath >> i) & 15;
            if (curActivePath == 0) {
                return true;
            }
            int curPath = (path >> i) & 15;
            if (curPath != curActivePath) {
                return false;
            }
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingChange(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        byte[] params = message.getParams();
        int currentPath = com.android.server.hdmi.HdmiUtils.twoBytesToInt(params);
        if (com.android.server.hdmi.HdmiUtils.isAffectingActiveRoutingPath(getActivePath(), currentPath)) {
            getActiveSource().invalidate();
            removeAction(com.android.server.hdmi.RoutingControlAction.class);
            int newPath = com.android.server.hdmi.HdmiUtils.twoBytesToInt(params, 2);
            addAndStartAction(new com.android.server.hdmi.RoutingControlAction(this, newPath, null));
            return -1;
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleReportAudioStatus(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (this.mService.getHdmiCecVolumeControl() == 0) {
            return 4;
        }
        boolean mute = com.android.server.hdmi.HdmiUtils.isAudioStatusMute(message);
        int volume = com.android.server.hdmi.HdmiUtils.getAudioStatusVolume(message);
        setAudioStatus(mute, volume);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleTextViewOn(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (getAutoWakeup()) {
            this.mService.wakeUp();
            return -1;
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleImageViewOn(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        return handleTextViewOn(message);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void launchDeviceDiscovery() {
        assertRunOnServiceThread();
        com.android.server.hdmi.DeviceDiscoveryAction action = new com.android.server.hdmi.DeviceDiscoveryAction(this, new com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv.3
            @Override // com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback
            public void onDeviceDiscoveryDone(java.util.List<android.hardware.hdmi.HdmiDeviceInfo> deviceInfos) {
                for (android.hardware.hdmi.HdmiDeviceInfo info : deviceInfos) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.mService.getHdmiCecNetwork().addCecDevice(info);
                }
                com.android.server.hdmi.HdmiCecLocalDeviceTv.this.mSelectRequestBuffer.process();
                com.android.server.hdmi.HdmiCecLocalDeviceTv.this.resetSelectRequestBuffer();
                java.util.List<com.android.server.hdmi.HotplugDetectionAction> hotplugActions = com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getActions(com.android.server.hdmi.HotplugDetectionAction.class);
                if (hotplugActions.isEmpty()) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.addAndStartAction(new com.android.server.hdmi.HotplugDetectionAction(com.android.server.hdmi.HdmiCecLocalDeviceTv.this));
                }
                java.util.List<com.android.server.hdmi.PowerStatusMonitorAction> powerStatusActions = com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getActions(com.android.server.hdmi.PowerStatusMonitorAction.class);
                if (powerStatusActions.isEmpty()) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.addAndStartAction(new com.android.server.hdmi.PowerStatusMonitorAction(com.android.server.hdmi.HdmiCecLocalDeviceTv.this));
                }
                android.hardware.hdmi.HdmiDeviceInfo avr = com.android.server.hdmi.HdmiCecLocalDeviceTv.this.getAvrDeviceInfo();
                if (avr != null) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.onNewAvrAdded(avr);
                } else {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.setSystemAudioMode(false);
                }
            }
        });
        addAndStartAction(action);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onNewAvrAdded(android.hardware.hdmi.HdmiDeviceInfo avr) {
        assertRunOnServiceThread();
        addAndStartAction(new com.android.server.hdmi.SystemAudioAutoInitiationAction(this, avr.getLogicalAddress()));
        if (!isDirectConnectAddress(avr.getPhysicalAddress())) {
            startArcAction(false);
        } else if (isConnected(avr.getPortId()) && isArcFeatureEnabled(avr.getPortId()) && !hasAction(com.android.server.hdmi.SetArcTransmissionStateAction.class)) {
            startArcAction(true);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void changeSystemAudioMode(boolean enabled, android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled() || hasAction(com.android.server.hdmi.DeviceDiscoveryAction.class)) {
            setSystemAudioMode(false);
            invokeCallback(callback, 6);
            return;
        }
        android.hardware.hdmi.HdmiDeviceInfo avr = getAvrDeviceInfo();
        if (avr == null) {
            setSystemAudioMode(false);
            invokeCallback(callback, 3);
        } else {
            addAndStartAction(new com.android.server.hdmi.SystemAudioActionFromTv(this, avr.getLogicalAddress(), enabled, callback));
        }
    }

    void setSystemAudioMode(boolean on) {
        if (!isSystemAudioControlFeatureEnabled() && on) {
            com.android.server.hdmi.HdmiLogger.debug("Cannot turn on system audio mode because the System Audio Control feature is disabled.", new java.lang.Object[0]);
            return;
        }
        com.android.server.hdmi.HdmiLogger.debug("System Audio Mode change[old:%b new:%b]", java.lang.Boolean.valueOf(this.mService.isSystemAudioActivated()), java.lang.Boolean.valueOf(on));
        updateAudioManagerForSystemAudio(on);
        synchronized (this.mLock) {
            if (this.mService.isSystemAudioActivated() != on) {
                this.mService.setSystemAudioActivated(on);
                this.mService.announceSystemAudioModeChange(on);
            }
            if (on && !this.mArcEstablished) {
                startArcAction(true);
            } else if (!on) {
                startArcAction(false);
            }
        }
    }

    private void updateAudioManagerForSystemAudio(boolean on) {
        int device = this.mService.getAudioManager().setHdmiSystemAudioSupported(on);
        com.android.server.hdmi.HdmiLogger.debug("[A]UpdateSystemAudio mode[on=%b] output=[%X]", java.lang.Boolean.valueOf(on), java.lang.Integer.valueOf(device));
    }

    boolean isSystemAudioActivated() {
        if (!hasSystemAudioDevice()) {
            return false;
        }
        return this.mService.isSystemAudioActivated();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setSystemAudioControlFeatureEnabled(boolean enabled) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mSystemAudioControlFeatureEnabled = enabled;
        }
        if (hasSystemAudioDevice()) {
            changeSystemAudioMode(enabled, null);
        }
    }

    boolean isSystemAudioControlFeatureEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSystemAudioControlFeatureEnabled;
        }
        return z;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void enableArc(java.util.List<byte[]> supportedSads) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("Set Arc Status[old:%b new:true]", java.lang.Boolean.valueOf(this.mArcEstablished));
        enableAudioReturnChannel(true);
        notifyArcStatusToAudioService(true, supportedSads);
        this.mArcEstablished = true;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void disableArc() {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("Set Arc Status[old:%b new:false]", java.lang.Boolean.valueOf(this.mArcEstablished));
        enableAudioReturnChannel(false);
        notifyArcStatusToAudioService(false, new java.util.ArrayList());
        this.mArcEstablished = false;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void enableAudioReturnChannel(boolean enabled) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo avr = getAvrDeviceInfo();
        if (avr != null && avr.getPortId() != -1) {
            this.mService.enableAudioReturnChannel(avr.getPortId(), enabled);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isConnected(int portId) {
        assertRunOnServiceThread();
        return this.mService.isConnected(portId);
    }

    private void notifyArcStatusToAudioService(boolean z, java.util.List<byte[]> list) {
        this.mService.getAudioManager().setWiredDeviceConnectionState(new android.media.AudioDeviceAttributes(2, 10, "", "", new java.util.ArrayList(), (java.util.List) list.stream().map(new java.util.function.Function() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.hdmi.HdmiCecLocalDeviceTv.lambda$notifyArcStatusToAudioService$1((byte[]) obj);
            }
        }).collect(java.util.stream.Collectors.toList())), z ? 1 : 0);
    }

    static /* synthetic */ android.media.AudioDescriptor lambda$notifyArcStatusToAudioService$1(byte[] sad) {
        return new android.media.AudioDescriptor(1, 0, sad);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isArcEstablished() {
        assertRunOnServiceThread();
        if (this.mArcEstablished) {
            for (int i = 0; i < this.mArcFeatureEnabled.size(); i++) {
                if (this.mArcFeatureEnabled.valueAt(i)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void changeArcFeatureEnabled(int portId, boolean enabled) {
        assertRunOnServiceThread();
        if (this.mArcFeatureEnabled.get(portId) == enabled) {
            return;
        }
        this.mArcFeatureEnabled.put(portId, enabled);
        android.hardware.hdmi.HdmiDeviceInfo avr = getAvrDeviceInfo();
        if (avr == null || avr.getPortId() != portId) {
            return;
        }
        if (enabled && !this.mArcEstablished) {
            startArcAction(true);
        } else if (!enabled && this.mArcEstablished) {
            startArcAction(false);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isArcFeatureEnabled(int portId) {
        assertRunOnServiceThread();
        return this.mArcFeatureEnabled.get(portId);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startArcAction(boolean enabled) {
        startArcAction(enabled, null);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startArcAction(boolean enabled, android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo info = getAvrDeviceInfo();
        if (info == null) {
            android.util.Slog.w(TAG, "Failed to start arc action; No AVR device.");
            invokeCallback(callback, 3);
            return;
        }
        if (!canStartArcUpdateAction(info.getLogicalAddress(), enabled)) {
            android.util.Slog.w(TAG, "Failed to start arc action; ARC configuration check failed.");
            if (enabled && !isConnectedToArcPort(info.getPhysicalAddress())) {
                displayOsd(1);
            }
            invokeCallback(callback, 6);
            return;
        }
        if (enabled && this.mService.earcBlocksArcConnection()) {
            android.util.Slog.i(TAG, "ARC connection blocked because eARC connection is established or being established.");
            invokeCallback(callback, 6);
            return;
        }
        if (enabled) {
            removeAction(com.android.server.hdmi.RequestArcTerminationAction.class);
            if (hasAction(com.android.server.hdmi.RequestArcInitiationAction.class)) {
                com.android.server.hdmi.RequestArcInitiationAction existingInitiationAction = (com.android.server.hdmi.RequestArcInitiationAction) getActions(com.android.server.hdmi.RequestArcInitiationAction.class).get(0);
                existingInitiationAction.addCallback(callback);
                return;
            } else {
                addAndStartAction(new com.android.server.hdmi.RequestArcInitiationAction(this, info.getLogicalAddress(), callback));
                return;
            }
        }
        removeAction(com.android.server.hdmi.RequestArcInitiationAction.class);
        if (hasAction(com.android.server.hdmi.RequestArcTerminationAction.class)) {
            com.android.server.hdmi.RequestArcTerminationAction existingTerminationAction = (com.android.server.hdmi.RequestArcTerminationAction) getActions(com.android.server.hdmi.RequestArcTerminationAction.class).get(0);
            existingTerminationAction.addCallback(callback);
        } else {
            addAndStartAction(new com.android.server.hdmi.RequestArcTerminationAction(this, info.getLogicalAddress(), callback));
        }
    }

    private boolean isDirectConnectAddress(int physicalAddress) {
        return (61440 & physicalAddress) == physicalAddress;
    }

    void setAudioStatus(boolean mute, int volume) {
        if (!isSystemAudioActivated() || this.mService.getHdmiCecVolumeControl() == 0) {
            return;
        }
        synchronized (this.mLock) {
            this.mSystemAudioMute = mute;
            this.mSystemAudioVolume = volume;
            displayOsd(2, mute ? 101 : volume);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void changeVolume(int curVolume, int delta, int maxVolume) {
        assertRunOnServiceThread();
        if (getAvrDeviceInfo() == null || delta == 0 || !isSystemAudioActivated() || this.mService.getHdmiCecVolumeControl() == 0) {
            return;
        }
        int targetVolume = curVolume + delta;
        int cecVolume = com.android.server.hdmi.VolumeControlAction.scaleToCecVolume(targetVolume, maxVolume);
        synchronized (this.mLock) {
            if (cecVolume == this.mSystemAudioVolume) {
                this.mService.setAudioStatus(false, com.android.server.hdmi.VolumeControlAction.scaleToCustomVolume(this.mSystemAudioVolume, maxVolume));
                return;
            }
            java.util.List<com.android.server.hdmi.VolumeControlAction> actions = getActions(com.android.server.hdmi.VolumeControlAction.class);
            if (actions.isEmpty()) {
                addAndStartAction(new com.android.server.hdmi.VolumeControlAction(this, getAvrDeviceInfo().getLogicalAddress(), delta > 0));
            } else {
                actions.get(0).handleVolumeChange(delta > 0);
            }
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void changeMute(boolean mute) {
        assertRunOnServiceThread();
        if (getAvrDeviceInfo() == null || this.mService.getHdmiCecVolumeControl() == 0) {
            return;
        }
        com.android.server.hdmi.HdmiLogger.debug("[A]:Change mute:%b", java.lang.Boolean.valueOf(mute));
        synchronized (this.mLock) {
            if (this.mSystemAudioMute == mute) {
                com.android.server.hdmi.HdmiLogger.debug("No need to change mute.", new java.lang.Object[0]);
            } else if (!isSystemAudioActivated()) {
                com.android.server.hdmi.HdmiLogger.debug("[A]:System audio is not activated.", new java.lang.Object[0]);
            } else {
                removeAction(com.android.server.hdmi.VolumeControlAction.class);
                sendUserControlPressedAndReleased(getAvrDeviceInfo().getLogicalAddress(), com.android.server.hdmi.HdmiCecKeycode.getMuteKey(mute));
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleInitiateArc(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (this.mService.earcBlocksArcConnection()) {
            android.util.Slog.i(TAG, "ARC connection blocked because eARC connection is established or being established.");
            return 1;
        }
        if (!canStartArcUpdateAction(message.getSource(), true)) {
            android.hardware.hdmi.HdmiDeviceInfo avrDeviceInfo = getAvrDeviceInfo();
            if (avrDeviceInfo == null) {
                this.mDelayedMessageBuffer.add(message);
                return -1;
            }
            if (!isConnectedToArcPort(avrDeviceInfo.getPhysicalAddress())) {
                displayOsd(1);
                return 4;
            }
            return 4;
        }
        com.android.server.hdmi.SetArcTransmissionStateAction action = new com.android.server.hdmi.SetArcTransmissionStateAction(this, message.getSource(), true);
        addAndStartAction(action);
        return -1;
    }

    private boolean canStartArcUpdateAction(int avrAddress, boolean enabled) {
        android.hardware.hdmi.HdmiDeviceInfo avr = getAvrDeviceInfo();
        if (avr == null || avrAddress != avr.getLogicalAddress() || !isConnectedToArcPort(avr.getPhysicalAddress())) {
            return false;
        }
        if (!enabled) {
            return true;
        }
        if (!isConnected(avr.getPortId()) || !isArcFeatureEnabled(avr.getPortId()) || !isDirectConnectAddress(avr.getPhysicalAddress())) {
            return false;
        }
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleTerminateArc(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (this.mService.isPowerStandbyOrTransient()) {
            disableArc();
            return -1;
        }
        com.android.server.hdmi.SetArcTransmissionStateAction action = new com.android.server.hdmi.SetArcTransmissionStateAction(this, message.getSource(), false);
        addAndStartAction(action);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSetSystemAudioMode(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        boolean systemAudioStatus = com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(message);
        if (!isMessageForSystemAudio(message)) {
            if (getAvrDeviceInfo() == null) {
                this.mDelayedMessageBuffer.add(message);
            } else {
                com.android.server.hdmi.HdmiLogger.warning("Invalid <Set System Audio Mode> message:" + message, new java.lang.Object[0]);
                return 4;
            }
        } else if (systemAudioStatus && !isSystemAudioControlFeatureEnabled()) {
            com.android.server.hdmi.HdmiLogger.debug("Ignoring <Set System Audio Mode> message because the System Audio Control feature is disabled: %s", message);
            return 4;
        }
        removeAction(com.android.server.hdmi.SystemAudioAutoInitiationAction.class);
        com.android.server.hdmi.SystemAudioActionFromAvr action = new com.android.server.hdmi.SystemAudioActionFromAvr(this, message.getSource(), systemAudioStatus, null);
        addAndStartAction(action);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSystemAudioModeStatus(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!isMessageForSystemAudio(message)) {
            com.android.server.hdmi.HdmiLogger.warning("Invalid <System Audio Mode Status> message:" + message, new java.lang.Object[0]);
            return -1;
        }
        boolean tvSystemAudioMode = isSystemAudioControlFeatureEnabled();
        boolean avrSystemAudioMode = com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(message);
        android.hardware.hdmi.HdmiDeviceInfo avr = getAvrDeviceInfo();
        if (avr == null) {
            setSystemAudioMode(false);
        } else if (avrSystemAudioMode != tvSystemAudioMode) {
            addAndStartAction(new com.android.server.hdmi.SystemAudioActionFromTv(this, avr.getLogicalAddress(), tvSystemAudioMode, null));
        } else {
            setSystemAudioMode(tvSystemAudioMode);
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRecordTvScreen(com.android.server.hdmi.HdmiCecMessage message) {
        java.util.List<com.android.server.hdmi.OneTouchRecordAction> actions = getActions(com.android.server.hdmi.OneTouchRecordAction.class);
        if (!actions.isEmpty()) {
            com.android.server.hdmi.OneTouchRecordAction action = actions.get(0);
            if (action.getRecorderAddress() != message.getSource()) {
                announceOneTouchRecordResult(message.getSource(), 48);
                return 2;
            }
            return 2;
        }
        int recorderAddress = message.getSource();
        byte[] recordSource = this.mService.invokeRecordRequestListener(recorderAddress);
        return startOneTouchRecord(recorderAddress, recordSource);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleTimerClearedStatus(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] params = message.getParams();
        int timerClearedStatusData = params[0] & 255;
        announceTimerRecordingResult(message.getSource(), timerClearedStatusData);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleSetAudioVolumeLevel(com.android.server.hdmi.SetAudioVolumeLevelMessage message) {
        if (this.mService.isSystemAudioActivated()) {
            return 1;
        }
        int audioVolumeLevel = message.getAudioVolumeLevel();
        if (audioVolumeLevel >= 0 && audioVolumeLevel <= 100) {
            this.mService.setStreamMusicVolume(audioVolumeLevel, 0);
            return -1;
        }
        return -1;
    }

    void announceOneTouchRecordResult(int recorderAddress, int result) {
        this.mService.invokeOneTouchRecordResult(recorderAddress, result);
    }

    void announceTimerRecordingResult(int recorderAddress, int result) {
        this.mService.invokeTimerRecordingResult(recorderAddress, result);
    }

    void announceClearTimerRecordingResult(int recorderAddress, int result) {
        this.mService.invokeClearTimerRecordingResult(recorderAddress, result);
    }

    private boolean isMessageForSystemAudio(com.android.server.hdmi.HdmiCecMessage message) {
        return this.mService.isCecControlEnabled() && message.getSource() == 5 && (message.getDestination() == 0 || message.getDestination() == 15) && getAvrDeviceInfo() != null;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    android.hardware.hdmi.HdmiDeviceInfo getAvrDeviceInfo() {
        assertRunOnServiceThread();
        return this.mService.getHdmiCecNetwork().getCecDeviceInfo(5);
    }

    boolean hasSystemAudioDevice() {
        return getSafeAvrDeviceInfo() != null;
    }

    android.hardware.hdmi.HdmiDeviceInfo getSafeAvrDeviceInfo() {
        return this.mService.getHdmiCecNetwork().getSafeCecDeviceInfo(5);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void handleRemoveActiveRoutingPath(int path) {
        assertRunOnServiceThread();
        if (isTailOfActivePath(path, getActivePath())) {
            int newPath = this.mService.portIdToPath(getActivePortId());
            startRoutingControl(getActivePath(), newPath, null);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void launchRoutingControl(boolean routingForBootup) {
        assertRunOnServiceThread();
        if (getActivePortId() != -1 && getActivePortId() != 0) {
            if (!routingForBootup && !isProhibitMode()) {
                int newPath = this.mService.portIdToPath(getActivePortId());
                setActivePath(newPath);
                startRoutingControl(getActivePath(), newPath, null);
                return;
            }
            return;
        }
        int activePath = this.mService.getPhysicalAddress();
        setActivePath(activePath);
        if (!routingForBootup && !this.mDelayedMessageBuffer.isBuffered(130)) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildActiveSource(getDeviceInfo().getLogicalAddress(), activePath));
            updateActiveSource(getDeviceInfo().getLogicalAddress(), activePath, "HdmiCecLocalDeviceTv#launchRoutingControl()");
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onHotplug(int portId, boolean connected) {
        android.hardware.hdmi.HdmiDeviceInfo avr;
        assertRunOnServiceThread();
        if (!connected) {
            this.mService.getHdmiCecNetwork().removeCecSwitches(portId);
        }
        if ((!this.mService.isEarcEnabled() || !this.mService.isEarcSupported()) && (avr = getAvrDeviceInfo()) != null && portId == avr.getPortId() && isConnectedToArcPort(avr.getPhysicalAddress())) {
            com.android.server.hdmi.HdmiLogger.debug("Port ID:%d, 5v=%b", java.lang.Integer.valueOf(portId), java.lang.Boolean.valueOf(connected));
            if (connected) {
                if (this.mArcEstablished) {
                    enableAudioReturnChannel(true);
                }
            } else {
                enableAudioReturnChannel(false);
            }
        }
        java.util.List<com.android.server.hdmi.HotplugDetectionAction> hotplugActions = getActions(com.android.server.hdmi.HotplugDetectionAction.class);
        if (!hotplugActions.isEmpty()) {
            hotplugActions.get(0).pollAllDevicesNow();
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean getAutoWakeup() {
        assertRunOnServiceThread();
        return this.mService.getHdmiCecConfig().getIntValue("tv_wake_on_one_touch_play") == 1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void disableDevice(boolean initiatedByCec, com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback callback) {
        assertRunOnServiceThread();
        this.mService.unregisterTvInputCallback(this.mTvInputCallback);
        removeAction(com.android.server.hdmi.DeviceDiscoveryAction.class);
        removeAction(com.android.server.hdmi.HotplugDetectionAction.class);
        removeAction(com.android.server.hdmi.PowerStatusMonitorAction.class);
        removeAction(com.android.server.hdmi.OneTouchRecordAction.class);
        removeAction(com.android.server.hdmi.TimerRecordingAction.class);
        removeAction(com.android.server.hdmi.NewDeviceAction.class);
        removeAction(com.android.server.hdmi.RequestActiveSourceAction.class);
        if (initiatedByCec || !this.mService.isEarcEnabled()) {
            disableSystemAudioIfExist();
        }
        disableArcIfExist();
        super.disableDevice(initiatedByCec, callback);
        clearDeviceInfoList();
        getActiveSource().invalidate();
        setActivePath(65535);
        checkIfPendingActionsCleared();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void disableSystemAudioIfExist() {
        assertRunOnServiceThread();
        if (getAvrDeviceInfo() == null) {
            return;
        }
        removeAction(com.android.server.hdmi.SystemAudioActionFromAvr.class);
        removeAction(com.android.server.hdmi.SystemAudioActionFromTv.class);
        removeAction(com.android.server.hdmi.SystemAudioAutoInitiationAction.class);
        removeAction(com.android.server.hdmi.VolumeControlAction.class);
        if (!this.mService.isCecControlEnabled()) {
            setSystemAudioMode(false);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void forceDisableArcOnAllPins() {
        java.util.List<android.hardware.hdmi.HdmiPortInfo> ports = this.mService.getPortInfo();
        for (android.hardware.hdmi.HdmiPortInfo port : ports) {
            if (isArcFeatureEnabled(port.getId())) {
                this.mService.enableAudioReturnChannel(port.getId(), false);
            }
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void disableArcIfExist() {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo avr = getAvrDeviceInfo();
        if (avr == null) {
            return;
        }
        removeAllRunningArcAction();
        if (!hasAction(com.android.server.hdmi.RequestArcTerminationAction.class) && isArcEstablished()) {
            addAndStartAction(new com.android.server.hdmi.RequestArcTerminationAction(this, avr.getLogicalAddress()));
        }
        forceDisableArcOnAllPins();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void removeAllRunningArcAction() {
        removeAction(com.android.server.hdmi.RequestArcTerminationAction.class);
        removeAction(com.android.server.hdmi.RequestArcInitiationAction.class);
        removeAction(com.android.server.hdmi.SetArcTransmissionStateAction.class);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onStandby(boolean initiatedByCec, int standbyAction, final com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled()) {
            invokeStandbyCompletedCallback(callback);
            return;
        }
        boolean sendStandbyOnSleep = this.mService.getHdmiCecConfig().getIntValue("tv_send_standby_on_sleep") == 1;
        if (!initiatedByCec && sendStandbyOnSleep) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv.4
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int error) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.invokeStandbyCompletedCallback(callback);
                }
            });
        } else {
            invokeStandbyCompletedCallback(callback);
        }
    }

    boolean isProhibitMode() {
        return this.mService.isProhibitMode();
    }

    boolean isPowerStandbyOrTransient() {
        return this.mService.isPowerStandbyOrTransient();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void displayOsd(int messageId) {
        assertRunOnServiceThread();
        this.mService.displayOsd(messageId);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void displayOsd(int messageId, int extra) {
        assertRunOnServiceThread();
        this.mService.displayOsd(messageId, extra);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    int startOneTouchRecord(int recorderAddress, byte[] recordSource) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled()) {
            android.util.Slog.w(TAG, "Can not start one touch record. CEC control is disabled.");
            announceOneTouchRecordResult(recorderAddress, 51);
            return 1;
        }
        if (!checkRecorder(recorderAddress)) {
            android.util.Slog.w(TAG, "Invalid recorder address:" + recorderAddress);
            announceOneTouchRecordResult(recorderAddress, 49);
            return 1;
        }
        if (!checkRecordSource(recordSource)) {
            android.util.Slog.w(TAG, "Invalid record source." + java.util.Arrays.toString(recordSource));
            announceOneTouchRecordResult(recorderAddress, 50);
            return 2;
        }
        addAndStartAction(new com.android.server.hdmi.OneTouchRecordAction(this, recorderAddress, recordSource));
        android.util.Slog.i(TAG, "Start new [One Touch Record]-Target:" + recorderAddress + ", recordSource:" + java.util.Arrays.toString(recordSource));
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void stopOneTouchRecord(int recorderAddress) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled()) {
            android.util.Slog.w(TAG, "Can not stop one touch record. CEC control is disabled.");
            announceOneTouchRecordResult(recorderAddress, 51);
        } else if (!checkRecorder(recorderAddress)) {
            android.util.Slog.w(TAG, "Invalid recorder address:" + recorderAddress);
            announceOneTouchRecordResult(recorderAddress, 49);
        } else {
            removeAction(com.android.server.hdmi.OneTouchRecordAction.class);
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRecordOff(getDeviceInfo().getLogicalAddress(), recorderAddress));
            android.util.Slog.i(TAG, "Stop [One Touch Record]-Target:" + recorderAddress);
        }
    }

    private boolean checkRecorder(int recorderAddress) {
        android.hardware.hdmi.HdmiDeviceInfo device = this.mService.getHdmiCecNetwork().getCecDeviceInfo(recorderAddress);
        return device != null && com.android.server.hdmi.HdmiUtils.isEligibleAddressForDevice(1, recorderAddress);
    }

    private boolean checkRecordSource(byte[] recordSource) {
        return recordSource != null && android.hardware.hdmi.HdmiRecordSources.checkRecordSource(recordSource);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startTimerRecording(int recorderAddress, int sourceType, byte[] recordSource) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled()) {
            android.util.Slog.w(TAG, "Can not start one touch record. CEC control is disabled.");
            announceTimerRecordingResult(recorderAddress, 3);
        } else if (!checkRecorder(recorderAddress)) {
            android.util.Slog.w(TAG, "Invalid recorder address:" + recorderAddress);
            announceTimerRecordingResult(recorderAddress, 1);
        } else if (!checkTimerRecordingSource(sourceType, recordSource)) {
            android.util.Slog.w(TAG, "Invalid record source." + java.util.Arrays.toString(recordSource));
            announceTimerRecordingResult(recorderAddress, 2);
        } else {
            addAndStartAction(new com.android.server.hdmi.TimerRecordingAction(this, recorderAddress, sourceType, recordSource));
            android.util.Slog.i(TAG, "Start [Timer Recording]-Target:" + recorderAddress + ", SourceType:" + sourceType + ", RecordSource:" + java.util.Arrays.toString(recordSource));
        }
    }

    private boolean checkTimerRecordingSource(int sourceType, byte[] recordSource) {
        return recordSource != null && android.hardware.hdmi.HdmiTimerRecordSources.checkTimerRecordSource(sourceType, recordSource);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void clearTimerRecording(int recorderAddress, int sourceType, byte[] recordSource) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled()) {
            android.util.Slog.w(TAG, "Can not start one touch record. CEC control is disabled.");
            announceClearTimerRecordingResult(recorderAddress, 162);
        } else if (!checkRecorder(recorderAddress)) {
            android.util.Slog.w(TAG, "Invalid recorder address:" + recorderAddress);
            announceClearTimerRecordingResult(recorderAddress, 160);
        } else if (!checkTimerRecordingSource(sourceType, recordSource)) {
            android.util.Slog.w(TAG, "Invalid record source." + java.util.Arrays.toString(recordSource));
            announceClearTimerRecordingResult(recorderAddress, 161);
        } else {
            sendClearTimerMessage(recorderAddress, sourceType, recordSource);
        }
    }

    private void sendClearTimerMessage(final int recorderAddress, int sourceType, byte[] recordSource) {
        com.android.server.hdmi.HdmiCecMessage message;
        switch (sourceType) {
            case 1:
                message = com.android.server.hdmi.HdmiCecMessageBuilder.buildClearDigitalTimer(getDeviceInfo().getLogicalAddress(), recorderAddress, recordSource);
                break;
            case 2:
                message = com.android.server.hdmi.HdmiCecMessageBuilder.buildClearAnalogueTimer(getDeviceInfo().getLogicalAddress(), recorderAddress, recordSource);
                break;
            case 3:
                message = com.android.server.hdmi.HdmiCecMessageBuilder.buildClearExternalTimer(getDeviceInfo().getLogicalAddress(), recorderAddress, recordSource);
                break;
            default:
                android.util.Slog.w(TAG, "Invalid source type:" + recorderAddress);
                announceClearTimerRecordingResult(recorderAddress, 161);
                return;
        }
        this.mService.sendCecCommand(message, new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceTv.5
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                if (error != 0) {
                    com.android.server.hdmi.HdmiCecLocalDeviceTv.this.announceClearTimerRecordingResult(recorderAddress, 161);
                }
            }
        });
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleMenuStatus(com.android.server.hdmi.HdmiCecMessage message) {
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int getRcProfile() {
        return 0;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected java.util.List<java.lang.Integer> getRcFeatures() {
        java.util.List<java.lang.Integer> features = new java.util.ArrayList<>();
        int profile = this.mService.getHdmiCecConfig().getIntValue("rc_profile_tv");
        features.add(java.lang.Integer.valueOf(profile));
        return features;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected android.hardware.hdmi.DeviceFeatures computeDeviceFeatures() {
        int i = 0;
        java.util.List<android.hardware.hdmi.HdmiPortInfo> ports = this.mService.getPortInfo();
        java.util.Iterator<android.hardware.hdmi.HdmiPortInfo> it = ports.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            android.hardware.hdmi.HdmiPortInfo port = it.next();
            if (isArcFeatureEnabled(port.getId())) {
                i = 1;
                break;
            }
        }
        return android.hardware.hdmi.DeviceFeatures.NO_FEATURES_SUPPORTED.toBuilder().setRecordTvScreenSupport(1).setArcTxSupport(i).setSetAudioVolumeLevelSupport(1).build();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void sendStandby(int deviceId) {
        android.hardware.hdmi.HdmiDeviceInfo targetDevice = this.mService.getHdmiCecNetwork().getDeviceInfo(deviceId);
        if (targetDevice == null) {
            return;
        }
        int targetAddress = targetDevice.getLogicalAddress();
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), targetAddress));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void processAllDelayedMessages() {
        assertRunOnServiceThread();
        this.mDelayedMessageBuffer.processAllMessages();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void processDelayedMessages(int address) {
        assertRunOnServiceThread();
        this.mDelayedMessageBuffer.processMessagesForDevice(address);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void processDelayedActiveSource(int address) {
        assertRunOnServiceThread();
        this.mDelayedMessageBuffer.processActiveSource(address);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void dump(com.android.internal.util.IndentingPrintWriter pw) {
        super.dump(pw);
        pw.println("mArcEstablished: " + this.mArcEstablished);
        pw.println("mArcFeatureEnabled: " + this.mArcFeatureEnabled);
        pw.println("mSystemAudioMute: " + this.mSystemAudioMute);
        pw.println("mSystemAudioControlFeatureEnabled: " + this.mSystemAudioControlFeatureEnabled);
        pw.println("mSkipRoutingControl: " + this.mSkipRoutingControl);
        pw.println("mPrevPortId: " + this.mPrevPortId);
    }
}
