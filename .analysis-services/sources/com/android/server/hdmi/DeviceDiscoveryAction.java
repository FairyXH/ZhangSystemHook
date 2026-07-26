package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class DeviceDiscoveryAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_WAITING_FOR_DEVICES = 5;
    private static final int STATE_WAITING_FOR_DEVICE_POLLING = 1;
    private static final int STATE_WAITING_FOR_OSD_NAME = 3;
    private static final int STATE_WAITING_FOR_PHYSICAL_ADDRESS = 2;
    private static final int STATE_WAITING_FOR_POWER = 6;
    private static final int STATE_WAITING_FOR_VENDOR_ID = 4;
    private static final java.lang.String TAG = "DeviceDiscoveryAction";
    private final com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback mCallback;
    private final int mDelayPeriod;
    private final java.util.ArrayList<com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo> mDevices;
    private boolean mIsTvDevice;
    private int mProcessedDeviceCount;
    private int mTimeoutRetry;

    interface DeviceDiscoveryCallback {
        void onDeviceDiscoveryDone(java.util.List<android.hardware.hdmi.HdmiDeviceInfo> list);
    }

    private static final class DeviceInfo {
        private int mDeviceType;
        private java.lang.String mDisplayName;
        private final int mLogicalAddress;
        private int mPhysicalAddress;
        private int mPortId;
        private int mPowerStatus;
        private int mVendorId;

        private DeviceInfo(int logicalAddress) {
            this.mPhysicalAddress = 65535;
            this.mPortId = -1;
            this.mVendorId = android.hardware.audio.common.V2_0.AudioFormat.SUB_MASK;
            this.mPowerStatus = -1;
            this.mDisplayName = "";
            this.mDeviceType = -1;
            this.mLogicalAddress = logicalAddress;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.hardware.hdmi.HdmiDeviceInfo toHdmiDeviceInfo() {
            return android.hardware.hdmi.HdmiDeviceInfo.cecDeviceBuilder().setLogicalAddress(this.mLogicalAddress).setPhysicalAddress(this.mPhysicalAddress).setPortId(this.mPortId).setVendorId(this.mVendorId).setDeviceType(this.mDeviceType).setDisplayName(this.mDisplayName).setDevicePowerStatus(this.mPowerStatus).build();
        }
    }

    DeviceDiscoveryAction(com.android.server.hdmi.HdmiCecLocalDevice source, com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback callback, int delay) {
        super(source);
        this.mDevices = new java.util.ArrayList<>();
        this.mProcessedDeviceCount = 0;
        this.mTimeoutRetry = 0;
        this.mIsTvDevice = localDevice().mService.isTvDevice();
        this.mCallback = (com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback) java.util.Objects.requireNonNull(callback);
        this.mDelayPeriod = delay;
    }

    DeviceDiscoveryAction(com.android.server.hdmi.HdmiCecLocalDevice source, com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback callback) {
        this(source, callback, 0);
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mDevices.clear();
        this.mState = 1;
        pollDevices(new com.android.server.hdmi.HdmiControlService.DevicePollingCallback() { // from class: com.android.server.hdmi.DeviceDiscoveryAction.1
            @Override // com.android.server.hdmi.HdmiControlService.DevicePollingCallback
            public void onPollingFinished(java.util.List<java.lang.Integer> ackedAddress) {
                if (ackedAddress.isEmpty()) {
                    android.util.Slog.v(com.android.server.hdmi.DeviceDiscoveryAction.TAG, "No device is detected.");
                    com.android.server.hdmi.DeviceDiscoveryAction.this.wrapUpAndFinish();
                } else {
                    if (com.android.server.hdmi.DeviceDiscoveryAction.this.mState == 0) {
                        android.util.Slog.v(com.android.server.hdmi.DeviceDiscoveryAction.TAG, "Action was already finished before the callback was called.");
                        com.android.server.hdmi.DeviceDiscoveryAction.this.wrapUpAndFinish();
                        return;
                    }
                    android.util.Slog.v(com.android.server.hdmi.DeviceDiscoveryAction.TAG, "Device detected: " + ackedAddress);
                    com.android.server.hdmi.DeviceDiscoveryAction.this.allocateDevices(ackedAddress);
                    if (com.android.server.hdmi.DeviceDiscoveryAction.this.mDelayPeriod > 0) {
                        com.android.server.hdmi.DeviceDiscoveryAction.this.startToDelayAction();
                    } else {
                        com.android.server.hdmi.DeviceDiscoveryAction.this.startPhysicalAddressStage();
                    }
                }
            }
        }, 131073, 1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allocateDevices(java.util.List<java.lang.Integer> addresses) {
        for (java.lang.Integer i : addresses) {
            com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo info = new com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo(i.intValue());
            this.mDevices.add(info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startToDelayAction() {
        android.util.Slog.v(TAG, "Waiting for connected devices to be ready");
        this.mState = 5;
        checkAndProceedStage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPhysicalAddressStage() {
        android.util.Slog.v(TAG, "Start [Physical Address Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 2;
        checkAndProceedStage();
    }

    private boolean verifyValidLogicalAddress(int address) {
        return address >= 0 && address < 15;
    }

    private void queryPhysicalAddress(int address) {
        if (!verifyValidLogicalAddress(address)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(address, 132)) {
            return;
        }
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGivePhysicalAddress(getSourceAddress(), address));
        addTimer(this.mState, 2000);
    }

    private void delayActionWithTimePeriod(int timeDelay) {
        this.mActionTimer.clearTimerMessage();
        addTimer(this.mState, timeDelay);
    }

    private void startOsdNameStage() {
        android.util.Slog.v(TAG, "Start [Osd Name Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 3;
        checkAndProceedStage();
    }

    private void queryOsdName(int address) {
        if (!verifyValidLogicalAddress(address)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(address, 71)) {
            return;
        }
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveOsdNameCommand(getSourceAddress(), address));
        addTimer(this.mState, 2000);
    }

    private void startVendorIdStage() {
        android.util.Slog.v(TAG, "Start [Vendor Id Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 4;
        checkAndProceedStage();
    }

    private void queryVendorId(int address) {
        if (!verifyValidLogicalAddress(address)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(address, 135)) {
            return;
        }
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveDeviceVendorIdCommand(getSourceAddress(), address));
        addTimer(this.mState, 2000);
    }

    private void startPowerStatusStage() {
        android.util.Slog.v(TAG, "Start [Power Status Stage]:" + this.mDevices.size());
        this.mProcessedDeviceCount = 0;
        this.mState = 6;
        checkAndProceedStage();
    }

    private void queryPowerStatus(int address) {
        if (!verifyValidLogicalAddress(address)) {
            checkAndProceedStage();
            return;
        }
        this.mActionTimer.clearTimerMessage();
        if (mayProcessMessageIfCached(address, 144)) {
            return;
        }
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveDevicePowerStatus(getSourceAddress(), address));
        addTimer(this.mState, 2000);
    }

    private boolean mayProcessMessageIfCached(int address, int opcode) {
        com.android.server.hdmi.HdmiCecMessage message = getCecMessageCache().getMessage(address, opcode);
        if (message != null) {
            processCommand(message);
            return true;
        }
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        switch (this.mState) {
            case 2:
                if (cmd.getOpcode() != 132) {
                    return false;
                }
                handleReportPhysicalAddress(cmd);
                return true;
            case 3:
                if (cmd.getOpcode() == 71) {
                    handleSetOsdName(cmd);
                    return true;
                }
                if (cmd.getOpcode() != 0 || (cmd.getParams()[0] & 255) != 70) {
                    return false;
                }
                handleSetOsdName(cmd);
                return true;
            case 4:
                if (cmd.getOpcode() == 135) {
                    handleVendorId(cmd);
                    return true;
                }
                if (cmd.getOpcode() != 0 || (cmd.getParams()[0] & 255) != 140) {
                    return false;
                }
                handleVendorId(cmd);
                return true;
            case 5:
            default:
                return false;
            case 6:
                if (cmd.getOpcode() == 144) {
                    handleReportPowerStatus(cmd);
                    return true;
                }
                if (cmd.getOpcode() != 0 || (cmd.getParams()[0] & 255) != 144) {
                    return false;
                }
                handleReportPowerStatus(cmd);
                return true;
        }
    }

    private void handleReportPhysicalAddress(com.android.server.hdmi.HdmiCecMessage cmd) {
        com.android.internal.util.Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo current = this.mDevices.get(this.mProcessedDeviceCount);
        if (current.mLogicalAddress != cmd.getSource()) {
            android.util.Slog.w(TAG, "Unmatched address[expected:" + current.mLogicalAddress + ", actual:" + cmd.getSource());
            return;
        }
        byte[] params = cmd.getParams();
        current.mPhysicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(params);
        current.mPortId = getPortId(current.mPhysicalAddress);
        current.mDeviceType = params[2] & 255;
        current.mDisplayName = "";
        if (this.mIsTvDevice) {
            localDevice().mService.getHdmiCecNetwork().updateCecSwitchInfo(current.mLogicalAddress, current.mDeviceType, current.mPhysicalAddress);
        }
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private int getPortId(int physicalAddress) {
        return this.mIsTvDevice ? tv().getPortId(physicalAddress) : source().getPortId(physicalAddress);
    }

    private void handleSetOsdName(com.android.server.hdmi.HdmiCecMessage cmd) {
        com.android.internal.util.Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo current = this.mDevices.get(this.mProcessedDeviceCount);
        if (current.mLogicalAddress != cmd.getSource()) {
            android.util.Slog.w(TAG, "Unmatched address[expected:" + current.mLogicalAddress + ", actual:" + cmd.getSource());
            return;
        }
        java.lang.String displayName = "";
        try {
            if (cmd.getOpcode() != 0) {
                displayName = new java.lang.String(cmd.getParams(), "US-ASCII");
            }
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Slog.w(TAG, "Failed to decode display name: " + cmd.toString());
        }
        current.mDisplayName = displayName;
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private void handleVendorId(com.android.server.hdmi.HdmiCecMessage cmd) {
        com.android.internal.util.Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo current = this.mDevices.get(this.mProcessedDeviceCount);
        if (current.mLogicalAddress != cmd.getSource()) {
            android.util.Slog.w(TAG, "Unmatched address[expected:" + current.mLogicalAddress + ", actual:" + cmd.getSource());
            return;
        }
        if (cmd.getOpcode() != 0) {
            byte[] params = cmd.getParams();
            int vendorId = com.android.server.hdmi.HdmiUtils.threeBytesToInt(params);
            current.mVendorId = vendorId;
        }
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private void handleReportPowerStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        com.android.internal.util.Preconditions.checkState(this.mProcessedDeviceCount < this.mDevices.size());
        com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo current = this.mDevices.get(this.mProcessedDeviceCount);
        if (current.mLogicalAddress != cmd.getSource()) {
            android.util.Slog.w(TAG, "Unmatched address[expected:" + current.mLogicalAddress + ", actual:" + cmd.getSource());
            return;
        }
        if (cmd.getOpcode() != 0) {
            byte[] params = cmd.getParams();
            int powerStatus = params[0] & 255;
            current.mPowerStatus = powerStatus;
        }
        increaseProcessedDeviceCount();
        checkAndProceedStage();
    }

    private void increaseProcessedDeviceCount() {
        this.mProcessedDeviceCount++;
        this.mTimeoutRetry = 0;
    }

    private void removeDevice(int index) {
        this.mDevices.remove(index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wrapUpAndFinish() {
        android.util.Slog.v(TAG, "---------Wrap up Device Discovery:[" + this.mDevices.size() + "]---------");
        java.util.ArrayList<android.hardware.hdmi.HdmiDeviceInfo> result = new java.util.ArrayList<>();
        for (com.android.server.hdmi.DeviceDiscoveryAction.DeviceInfo info : this.mDevices) {
            android.hardware.hdmi.HdmiDeviceInfo cecDeviceInfo = info.toHdmiDeviceInfo();
            android.util.Slog.v(TAG, " DeviceInfo: " + cecDeviceInfo);
            result.add(cecDeviceInfo);
        }
        android.util.Slog.v(TAG, "--------------------------------------------");
        this.mCallback.onDeviceDiscoveryDone(result);
        finish();
        if (this.mIsTvDevice) {
            tv().processAllDelayedMessages();
        }
    }

    private void checkAndProceedStage() {
        if (this.mDevices.isEmpty()) {
            wrapUpAndFinish();
            return;
        }
        if (this.mProcessedDeviceCount == this.mDevices.size()) {
            this.mProcessedDeviceCount = 0;
            switch (this.mState) {
                case 2:
                    startOsdNameStage();
                    break;
                case 3:
                    startVendorIdStage();
                    break;
                case 4:
                    startPowerStatusStage();
                    break;
                case 6:
                    wrapUpAndFinish();
                    break;
            }
            return;
        }
        sendQueryCommand();
    }

    private void sendQueryCommand() {
        int address = this.mDevices.get(this.mProcessedDeviceCount).mLogicalAddress;
        switch (this.mState) {
            case 2:
                queryPhysicalAddress(address);
                break;
            case 3:
                queryOsdName(address);
                break;
            case 4:
                queryVendorId(address);
                break;
            case 5:
                delayActionWithTimePeriod(this.mDelayPeriod);
                break;
            case 6:
                queryPowerStatus(address);
                break;
        }
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState == 0 || this.mState != state) {
            return;
        }
        if (this.mState == 5) {
            startPhysicalAddressStage();
            return;
        }
        int i = this.mTimeoutRetry + 1;
        this.mTimeoutRetry = i;
        if (i < 5) {
            sendQueryCommand();
            return;
        }
        this.mTimeoutRetry = 0;
        android.util.Slog.v(TAG, "Timeout[State=" + this.mState + ", Processed=" + this.mProcessedDeviceCount);
        if (this.mState != 6 && this.mState != 3) {
            removeDevice(this.mProcessedDeviceCount);
        } else {
            increaseProcessedDeviceCount();
        }
        checkAndProceedStage();
    }
}
