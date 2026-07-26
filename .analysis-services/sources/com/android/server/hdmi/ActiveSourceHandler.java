package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class ActiveSourceHandler {
    private static final java.lang.String TAG = "ActiveSourceHandler";
    private final android.hardware.hdmi.IHdmiControlCallback mCallback;
    private final com.android.server.hdmi.HdmiControlService mService;
    private final com.android.server.hdmi.HdmiCecLocalDeviceTv mSource;

    static com.android.server.hdmi.ActiveSourceHandler create(com.android.server.hdmi.HdmiCecLocalDeviceTv source, android.hardware.hdmi.IHdmiControlCallback callback) {
        if (source == null) {
            android.util.Slog.e(TAG, "Wrong arguments");
            return null;
        }
        return new com.android.server.hdmi.ActiveSourceHandler(source, callback);
    }

    private ActiveSourceHandler(com.android.server.hdmi.HdmiCecLocalDeviceTv source, android.hardware.hdmi.IHdmiControlCallback callback) {
        this.mSource = source;
        this.mService = this.mSource.getService();
        this.mCallback = callback;
    }

    void process(com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource newActive, int deviceType) {
        com.android.server.hdmi.HdmiCecLocalDeviceTv tv = this.mSource;
        android.hardware.hdmi.HdmiDeviceInfo device = this.mService.getDeviceInfo(newActive.logicalAddress);
        if (device == null) {
            tv.startNewDeviceAction(newActive, deviceType);
        }
        if (!tv.isProhibitMode()) {
            com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource old = com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource.of(tv.getActiveSource());
            tv.updateActiveSource(newActive, TAG);
            boolean notifyInputChange = this.mCallback == null;
            if (!old.equals(newActive)) {
                tv.setPrevPortId(tv.getActivePortId());
            }
            tv.updateActiveInput(newActive.physicalAddress, notifyInputChange);
            invokeCallback(0);
            return;
        }
        com.android.server.hdmi.HdmiCecLocalDevice.ActiveSource current = tv.getActiveSource();
        if (current.logicalAddress == getSourceAddress()) {
            com.android.server.hdmi.HdmiCecMessage activeSourceCommand = com.android.server.hdmi.HdmiCecMessageBuilder.buildActiveSource(current.logicalAddress, current.physicalAddress);
            this.mService.sendCecCommand(activeSourceCommand);
            tv.updateActiveSource(current, TAG);
            invokeCallback(0);
            return;
        }
        tv.startRoutingControl(newActive.physicalAddress, current.physicalAddress, this.mCallback);
    }

    private final int getSourceAddress() {
        return this.mSource.getDeviceInfo().getLogicalAddress();
    }

    private void invokeCallback(int result) {
        if (this.mCallback == null) {
            return;
        }
        try {
            this.mCallback.onComplete(result);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Callback failed:" + e);
        }
    }
}
