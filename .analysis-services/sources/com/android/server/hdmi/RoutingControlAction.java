package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class RoutingControlAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    static final int STATE_WAIT_FOR_ROUTING_INFORMATION = 1;
    private static final java.lang.String TAG = "RoutingControlAction";
    private static final int TIMEOUT_ROUTING_INFORMATION_MS = 1000;
    private int mCurrentRoutingPath;
    private final boolean mNotifyInputChange;

    RoutingControlAction(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int path, android.hardware.hdmi.IHdmiControlCallback callback) {
        super(localDevice, callback);
        this.mCurrentRoutingPath = path;
        this.mNotifyInputChange = callback == null;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean start() {
        this.mState = 1;
        addTimer(this.mState, 1000);
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        int opcode = cmd.getOpcode();
        byte[] params = cmd.getParams();
        if (this.mState == 1 && opcode == 129) {
            int routingPath = com.android.server.hdmi.HdmiUtils.twoBytesToInt(params);
            if (!com.android.server.hdmi.HdmiUtils.isInActiveRoutingPath(this.mCurrentRoutingPath, routingPath)) {
                return true;
            }
            this.mCurrentRoutingPath = routingPath;
            removeActionExcept(com.android.server.hdmi.RoutingControlAction.class, this);
            addTimer(this.mState, 1000);
            return true;
        }
        return false;
    }

    private void updateActiveInput() {
        com.android.server.hdmi.HdmiCecLocalDeviceTv tv = tv();
        tv.setPrevPortId(tv.getActivePortId());
        tv.updateActiveInput(this.mCurrentRoutingPath, this.mNotifyInputChange);
    }

    private void sendSetStreamPath() {
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetStreamPath(getSourceAddress(), this.mCurrentRoutingPath));
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public void handleTimerEvent(int timeoutState) {
        if (this.mState != timeoutState || this.mState == 0) {
            android.util.Slog.w("CEC", "Timer in a wrong state. Ignored.");
            return;
        }
        switch (timeoutState) {
            case 1:
                updateActiveInput();
                sendSetStreamPath();
                finishWithCallback(0);
                break;
            default:
                android.util.Slog.e("CEC", "Invalid timeoutState (" + timeoutState + ").");
                break;
        }
    }
}
