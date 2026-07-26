package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
abstract class HdmiCecLocalDeviceSource extends com.android.server.hdmi.HdmiCecLocalDevice {
    private static final java.lang.String TAG = "HdmiCecLocalDeviceSource";
    protected boolean mIsSwitchDevice;
    protected int mLocalActivePort;
    protected boolean mRoutingControlFeatureEnabled;
    private int mRoutingPort;

    protected HdmiCecLocalDeviceSource(com.android.server.hdmi.HdmiControlService service, int deviceType) {
        super(service, deviceType);
        this.mIsSwitchDevice = ((java.lang.Boolean) android.sysprop.HdmiProperties.is_switch().orElse(false)).booleanValue();
        this.mRoutingPort = 0;
        this.mLocalActivePort = 0;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void queryDisplayStatus(android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        java.util.List<com.android.server.hdmi.DevicePowerStatusAction> actions = getActions(com.android.server.hdmi.DevicePowerStatusAction.class);
        if (!actions.isEmpty()) {
            android.util.Slog.i(TAG, "queryDisplayStatus already in progress");
            actions.get(0).addCallback(callback);
            return;
        }
        com.android.server.hdmi.DevicePowerStatusAction action = com.android.server.hdmi.DevicePowerStatusAction.create(this, 0, callback);
        if (action == null) {
            android.util.Slog.w(TAG, "Cannot initiate queryDisplayStatus");
            invokeCallback(callback, -1);
        } else {
            addAndStartAction(action);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onHotplug(int portId, boolean connected) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiPortInfo portInfo = this.mService.getPortInfo(portId);
        if (portInfo != null && portInfo.getType() == 1) {
            this.mCecMessageCache.flushAll();
        }
        if (connected) {
            this.mService.wakeUp();
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void sendStandby(int deviceId) {
        assertRunOnServiceThread();
        java.lang.String powerControlMode = this.mService.getHdmiCecConfig().getStringValue("power_control_mode");
        if (powerControlMode.equals("broadcast")) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15));
            return;
        }
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 0));
        if (powerControlMode.equals("to_tv_and_audio_system")) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 5));
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void oneTouchPlay(android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        java.util.List<com.android.server.hdmi.OneTouchPlayAction> actions = getActions(com.android.server.hdmi.OneTouchPlayAction.class);
        if (!actions.isEmpty()) {
            android.util.Slog.i(TAG, "oneTouchPlay already in progress");
            actions.get(0).addCallback(callback);
            return;
        }
        com.android.server.hdmi.OneTouchPlayAction action = com.android.server.hdmi.OneTouchPlayAction.create(this, 0, callback);
        if (action == null) {
            android.util.Slog.w(TAG, "Cannot initiate oneTouchPlay");
            invokeCallback(callback, 5);
        } else {
            addAndStartAction(action);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void toggleAndFollowTvPower() {
        assertRunOnServiceThread();
        if (this.mService.getPowerManager().isInteractive()) {
            this.mService.pauseActiveMediaSessions();
        } else {
            this.mService.wakeUp();
        }
        this.mService.queryDisplayStatus(new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceSource.1
            public void onComplete(int status) {
                if (status == -1) {
                    android.util.Slog.i(com.android.server.hdmi.HdmiCecLocalDeviceSource.TAG, "TV power toggle: TV power status unknown");
                    com.android.server.hdmi.HdmiCecLocalDeviceSource.this.sendUserControlPressedAndReleased(0, 107);
                    return;
                }
                if (status == 0 || status == 2) {
                    android.util.Slog.i(com.android.server.hdmi.HdmiCecLocalDeviceSource.TAG, "TV power toggle: turning off TV");
                    com.android.server.hdmi.HdmiCecLocalDeviceSource.this.sendStandby(0);
                    com.android.server.hdmi.HdmiCecLocalDeviceSource.this.mService.standby();
                } else if (status == 1 || status == 3) {
                    android.util.Slog.i(com.android.server.hdmi.HdmiCecLocalDeviceSource.TAG, "TV power toggle: turning on TV");
                    com.android.server.hdmi.HdmiCecLocalDeviceSource.this.oneTouchPlay(new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceSource.1.1
                        public void onComplete(int result) {
                            if (result != 0) {
                                android.util.Slog.w(com.android.server.hdmi.HdmiCecLocalDeviceSource.TAG, "Failed to complete One Touch Play. result=" + result);
                                com.android.server.hdmi.HdmiCecLocalDeviceSource.this.sendUserControlPressedAndReleased(0, 107);
                            }
                        }
                    });
                }
            }
        });
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onActiveSourceLost() {
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setActiveSource(int logicalAddress, int physicalAddress, java.lang.String caller) {
        boolean wasActiveSource = isActiveSource();
        super.setActiveSource(logicalAddress, physicalAddress, caller);
        if (wasActiveSource && !isActiveSource()) {
            onActiveSourceLost();
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void setActiveSource(int physicalAddress, java.lang.String caller) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource = com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource.of(-1, physicalAddress);
        setActiveSource(activeSource, caller);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int logicalAddress = message.getSource();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource activeSource = com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource.of(logicalAddress, physicalAddress);
        if (!getActiveSource().equals(activeSource)) {
            setActiveSource(activeSource, "HdmiCecLocalDeviceSource#handleActiveSource()");
        }
        updateDevicePowerStatus(logicalAddress, 0);
        if (isRoutingControlFeatureEnabled()) {
            switchInputOnReceivingNewActivePath(physicalAddress);
            return -1;
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        maySendActiveSource(message.getSource());
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSetStreamPath(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        if (physicalAddress == this.mService.getPhysicalAddress() && this.mService.isPlaybackDevice()) {
            setAndBroadcastActiveSource(message, physicalAddress, "HdmiCecLocalDeviceSource#handleSetStreamPath()");
        } else if (physicalAddress != this.mService.getPhysicalAddress() || !isActiveSource()) {
            setActiveSource(physicalAddress, "HdmiCecLocalDeviceSource#handleSetStreamPath()");
        }
        switchInputOnReceivingNewActivePath(physicalAddress);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingChange(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams(), 2);
        if (physicalAddress != this.mService.getPhysicalAddress() || !isActiveSource()) {
            setActiveSource(physicalAddress, "HdmiCecLocalDeviceSource#handleRoutingChange()");
        }
        if (!isRoutingControlFeatureEnabled()) {
            return 4;
        }
        handleRoutingChangeAndInformation(physicalAddress, message);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingInformation(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        if (physicalAddress != this.mService.getPhysicalAddress() || !isActiveSource()) {
            setActiveSource(physicalAddress, "HdmiCecLocalDeviceSource#handleRoutingInformation()");
        }
        if (!isRoutingControlFeatureEnabled()) {
            return 4;
        }
        handleRoutingChangeAndInformation(physicalAddress, message);
        return -1;
    }

    protected void switchInputOnReceivingNewActivePath(int physicalAddress) {
    }

    protected void handleRoutingChangeAndInformation(int physicalAddress, com.android.server.hdmi.HdmiCecMessage message) {
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void disableDevice(boolean initiatedByCec, com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback callback) {
        removeAction(com.android.server.hdmi.OneTouchPlayAction.class);
        removeAction(com.android.server.hdmi.DevicePowerStatusAction.class);
        super.disableDevice(initiatedByCec, callback);
    }

    protected void updateDevicePowerStatus(int logicalAddress, int newPowerStatus) {
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int getRcProfile() {
        return 1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected java.util.List<java.lang.Integer> getRcFeatures() {
        java.util.List<java.lang.Integer> features = new java.util.ArrayList<>();
        com.android.server.hdmi.HdmiCecConfig hdmiCecConfig = this.mService.getHdmiCecConfig();
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_root_menu") == 1) {
            features.add(4);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_setup_menu") == 1) {
            features.add(3);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_contents_menu") == 1) {
            features.add(2);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_top_menu") == 1) {
            features.add(1);
        }
        if (hdmiCecConfig.getIntValue("rc_profile_source_handles_media_context_sensitive_menu") == 1) {
            features.add(0);
        }
        return features;
    }

    protected void setAndBroadcastActiveSource(com.android.server.hdmi.HdmiCecMessage message, int physicalAddress, java.lang.String caller) {
        this.mService.setAndBroadcastActiveSource(physicalAddress, getDeviceInfo().getDeviceType(), message.getSource(), caller);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected boolean isActiveSource() {
        if (getDeviceInfo() == null) {
            return false;
        }
        return getActiveSource().equals(getDeviceInfo().getLogicalAddress(), getDeviceInfo().getPhysicalAddress());
    }

    protected void wakeUpIfActiveSource() {
        if (!isActiveSource()) {
            return;
        }
        this.mService.wakeUp();
    }

    protected void maySendActiveSource(int dest) {
        if (!isActiveSource()) {
            return;
        }
        addAndStartAction(new com.android.server.hdmi.ActiveSourceAction(this, dest));
    }

    protected void setRoutingPort(int portId) {
        synchronized (this.mLock) {
            this.mRoutingPort = portId;
        }
    }

    protected int getRoutingPort() {
        int i;
        synchronized (this.mLock) {
            i = this.mRoutingPort;
        }
        return i;
    }

    protected int getLocalActivePort() {
        int i;
        synchronized (this.mLock) {
            i = this.mLocalActivePort;
        }
        return i;
    }

    protected void setLocalActivePort(int activePort) {
        synchronized (this.mLock) {
            this.mLocalActivePort = activePort;
        }
    }

    boolean isRoutingControlFeatureEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mRoutingControlFeatureEnabled;
        }
        return z;
    }

    protected boolean isSwitchingToTheSameInput(int activePort) {
        return activePort == getLocalActivePort();
    }
}
