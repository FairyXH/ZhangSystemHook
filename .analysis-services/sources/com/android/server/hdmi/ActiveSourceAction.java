package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class ActiveSourceAction extends com.android.server.hdmi.HdmiCecFeatureAction {
    private static final int STATE_FINISHED = 2;
    private static final int STATE_STARTED = 1;
    private final int mDestination;

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    public /* bridge */ /* synthetic */ void addCallback(android.hardware.hdmi.IHdmiControlCallback iHdmiControlCallback) {
        super.addCallback(iHdmiControlCallback);
    }

    ActiveSourceAction(com.android.server.hdmi.HdmiCecLocalDevice source, int destination) {
        super(source);
        this.mDestination = destination;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean start() {
        this.mState = 1;
        int logicalAddress = getSourceAddress();
        int physicalAddress = getSourcePath();
        sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildActiveSource(logicalAddress, physicalAddress));
        if (source().getType() == 4) {
            sendCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportMenuStatus(logicalAddress, this.mDestination, 0));
        }
        source().setActiveSource(logicalAddress, physicalAddress, "ActiveSourceAction");
        this.mState = 2;
        finish();
        return true;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    boolean processCommand(com.android.server.hdmi.HdmiCecMessage cmd) {
        return false;
    }

    @Override // com.android.server.hdmi.HdmiCecFeatureAction
    void handleTimerEvent(int state) {
    }
}
