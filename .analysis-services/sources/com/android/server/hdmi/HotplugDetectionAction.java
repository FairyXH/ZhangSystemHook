package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HotplugDetectionAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int AVR_COUNT_MAX = 3;
    private static final int NUM_OF_ADDRESS = 15;
    public static final int POLLING_BATCH_INTERVAL_MS_FOR_PLAYBACK = 60000;
    public static final int POLLING_BATCH_INTERVAL_MS_FOR_TV = 5000;
    public static final long POLLING_MESSAGE_INTERVAL_MS_FOR_PLAYBACK = 500;
    public static final long POLLING_MESSAGE_INTERVAL_MS_FOR_TV = 0;
    private static final int STATE_WAIT_FOR_NEXT_POLLING = 1;
    private static final java.lang.String TAG = "HotPlugDetectionAction";
    public static final int TIMEOUT_COUNT = 3;
    private int mAvrStatusCount;
    private final boolean mIsTvDevice;
    private int mTimeoutCount;

    HotplugDetectionAction(com.android.server.hdmi.HdmiCecLocalDevice source) {
        super(source);
        this.mTimeoutCount = 0;
        this.mAvrStatusCount = 0;
        this.mIsTvDevice = localDevice().mService.isTvDevice();
    }

    private int getPollingBatchInterval() {
        return this.mIsTvDevice ? 5000 : 60000;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        android.util.Slog.v(TAG, "Hot-plug detection started.");
        this.mState = 1;
        this.mTimeoutCount = 0;
        addTimer(this.mState, getPollingBatchInterval());
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
        if (this.mState == state && this.mState == 1) {
            if (this.mIsTvDevice) {
                this.mTimeoutCount = (this.mTimeoutCount + 1) % 3;
                if (this.mTimeoutCount == 0) {
                    pollAllDevices();
                } else if (tv().isSystemAudioActivated()) {
                    pollAudioSystem();
                }
                addTimer(this.mState, 5000);
                return;
            }
            pollAllDevices();
            addTimer(this.mState, 60000);
        }
    }

    void pollAllDevicesNow() {
        this.mActionTimer.clearTimerMessage();
        this.mTimeoutCount = 0;
        this.mState = 1;
        pollAllDevices();
        addTimer(this.mState, getPollingBatchInterval());
    }

    private void pollAllDevices() {
        android.util.Slog.v(TAG, "Poll all devices.");
        pollDevices(new com.android.server.hdmi.HdmiControlService.DevicePollingCallback() { // from class: com.android.server.hdmi.HotplugDetectionAction.1
            @Override // com.android.server.hdmi.HdmiControlService.DevicePollingCallback
            public void onPollingFinished(java.util.List<java.lang.Integer> ackedAddress) {
                com.android.server.hdmi.HotplugDetectionAction.this.checkHotplug(ackedAddress, false);
                android.util.Slog.v(com.android.server.hdmi.HotplugDetectionAction.TAG, "Finish poll all devices.");
            }
        }, 65537, 1, this.mIsTvDevice ? 0L : 500L);
    }

    private void pollAudioSystem() {
        android.util.Slog.v(TAG, "Poll audio system.");
        pollDevices(new com.android.server.hdmi.HdmiControlService.DevicePollingCallback() { // from class: com.android.server.hdmi.HotplugDetectionAction.2
            @Override // com.android.server.hdmi.HdmiControlService.DevicePollingCallback
            public void onPollingFinished(java.util.List<java.lang.Integer> ackedAddress) {
                com.android.server.hdmi.HotplugDetectionAction.this.checkHotplug(ackedAddress, true);
            }
        }, 65538, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkHotplug(java.util.List<java.lang.Integer> ackedAddress, boolean audioOnly) {
        android.hardware.hdmi.HdmiDeviceInfo avr;
        java.util.List<android.hardware.hdmi.HdmiDeviceInfo> deviceInfoList = localDevice().mService.getHdmiCecNetwork().getDeviceInfoList(false);
        java.util.BitSet currentInfos = infoListToBitSet(deviceInfoList, audioOnly, false);
        java.util.BitSet polledResult = addressListToBitSet(ackedAddress);
        java.util.BitSet removed = complement(currentInfos, polledResult);
        int index = -1;
        while (true) {
            int iNextSetBit = removed.nextSetBit(index + 1);
            index = iNextSetBit;
            if (iNextSetBit == -1) {
                break;
            }
            if (this.mIsTvDevice && index == 5 && (avr = tv().getAvrDeviceInfo()) != null && tv().isConnected(avr.getPortId())) {
                this.mAvrStatusCount++;
                android.util.Slog.w(TAG, "Ack not returned from AVR. count: " + this.mAvrStatusCount);
                if (this.mAvrStatusCount < 3) {
                }
            }
            android.util.Slog.v(TAG, "Remove device by hot-plug detection:" + index);
            removeDevice(index);
        }
        if (!removed.get(5)) {
            this.mAvrStatusCount = 0;
        }
        java.util.BitSet currentInfosWithPhysicalAddress = infoListToBitSet(deviceInfoList, audioOnly, true);
        java.util.BitSet added = complement(polledResult, currentInfosWithPhysicalAddress);
        int index2 = -1;
        while (true) {
            int iNextSetBit2 = added.nextSetBit(index2 + 1);
            index2 = iNextSetBit2;
            if (iNextSetBit2 != -1) {
                android.util.Slog.v(TAG, "Add device by hot-plug detection:" + index2);
                addDevice(index2);
            } else {
                return;
            }
        }
    }

    private static java.util.BitSet infoListToBitSet(java.util.List<android.hardware.hdmi.HdmiDeviceInfo> infoList, boolean audioOnly, boolean requirePhysicalAddress) {
        java.util.BitSet set = new java.util.BitSet(15);
        for (android.hardware.hdmi.HdmiDeviceInfo info : infoList) {
            boolean audioOnlyConditionMet = !audioOnly || info.getDeviceType() == 5;
            boolean requirePhysicalAddressConditionMet = (requirePhysicalAddress && info.getPhysicalAddress() == 65535) ? false : true;
            if (audioOnlyConditionMet && requirePhysicalAddressConditionMet) {
                set.set(info.getLogicalAddress());
            }
        }
        return set;
    }

    private static java.util.BitSet addressListToBitSet(java.util.List<java.lang.Integer> list) {
        java.util.BitSet set = new java.util.BitSet(15);
        for (java.lang.Integer value : list) {
            set.set(value.intValue());
        }
        return set;
    }

    private static java.util.BitSet complement(java.util.BitSet first, java.util.BitSet second) {
        java.util.BitSet clone = (java.util.BitSet) first.clone();
        clone.andNot(second);
        return clone;
    }

    private void addDevice(int addedAddress) {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGivePhysicalAddress(getSourceAddress(), addedAddress));
    }

    private void removeDevice(int removedAddress) {
        if (this.mIsTvDevice) {
            mayChangeRoutingPath(removedAddress);
            mayCancelOneTouchRecord(removedAddress);
            mayDisableSystemAudioAndARC(removedAddress);
        }
        mayCancelDeviceSelect(removedAddress);
        localDevice().mService.getHdmiCecNetwork().removeCecDevice(localDevice(), removedAddress);
    }

    private void mayChangeRoutingPath(int address) {
        android.hardware.hdmi.HdmiDeviceInfo info = localDevice().mService.getHdmiCecNetwork().getCecDeviceInfo(address);
        if (info != null) {
            tv().handleRemoveActiveRoutingPath(info.getPhysicalAddress());
        }
    }

    private void mayCancelDeviceSelect(int address) {
        java.util.List<com.android.server.hdmi.DeviceSelectActionFromTv> actionsFromTv = getActions(com.android.server.hdmi.DeviceSelectActionFromTv.class);
        for (com.android.server.hdmi.DeviceSelectActionFromTv action : actionsFromTv) {
            if (action.getTargetAddress() == address) {
                removeAction(com.android.server.hdmi.DeviceSelectActionFromTv.class);
            }
        }
        java.util.List<com.android.server.hdmi.DeviceSelectActionFromPlayback> actionsFromPlayback = getActions(com.android.server.hdmi.DeviceSelectActionFromPlayback.class);
        for (com.android.server.hdmi.DeviceSelectActionFromPlayback action2 : actionsFromPlayback) {
            if (action2.getTargetAddress() == address) {
                removeAction(com.android.server.hdmi.DeviceSelectActionFromTv.class);
            }
        }
    }

    private void mayCancelOneTouchRecord(int address) {
        java.util.List<com.android.server.hdmi.OneTouchRecordAction> actions = getActions(com.android.server.hdmi.OneTouchRecordAction.class);
        for (com.android.server.hdmi.OneTouchRecordAction action : actions) {
            if (action.getRecorderAddress() == address) {
                removeAction(action);
            }
        }
    }

    private void mayDisableSystemAudioAndARC(int address) {
        if (!com.android.server.hdmi.HdmiUtils.isEligibleAddressForDevice(5, address)) {
            return;
        }
        tv().setSystemAudioMode(false);
        if (tv().isArcEstablished()) {
            tv().enableAudioReturnChannel(false);
            addAndStartAction(new com.android.server.hdmi.RequestArcTerminationAction(localDevice(), address));
        }
    }
}
