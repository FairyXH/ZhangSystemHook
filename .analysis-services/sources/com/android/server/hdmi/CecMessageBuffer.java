package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class CecMessageBuffer {
    private java.util.List<com.android.server.hdmi.HdmiCecMessage> mBuffer = new java.util.ArrayList();
    private com.android.server.hdmi.HdmiControlService mHdmiControlService;

    CecMessageBuffer(com.android.server.hdmi.HdmiControlService hdmiControlService) {
        this.mHdmiControlService = hdmiControlService;
    }

    public boolean bufferMessage(com.android.server.hdmi.HdmiCecMessage message) {
        switch (message.getOpcode()) {
            case 4:
            case 13:
                bufferImageOrTextViewOn(message);
                break;
            case 112:
                bufferSystemAudioModeRequest(message);
                break;
            case 128:
                bufferRoutingChange(message);
                break;
            case 130:
                bufferActiveSource(message);
                break;
            case 134:
                bufferSetStreamPath(message);
                break;
        }
        return true;
    }

    public void processMessages() {
        for (final com.android.server.hdmi.HdmiCecMessage message : this.mBuffer) {
            this.mHdmiControlService.runOnServiceThread(new java.lang.Runnable() { // from class: com.android.server.hdmi.CecMessageBuffer.1
                @Override // java.lang.Runnable
                public void run() {
                    com.android.server.hdmi.CecMessageBuffer.this.mHdmiControlService.handleCecCommand(message);
                }
            });
        }
        this.mBuffer.clear();
    }

    private void bufferActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        if (!replaceMessageIfBuffered(message, 130)) {
            this.mBuffer.add(message);
        }
    }

    private void bufferImageOrTextViewOn(com.android.server.hdmi.HdmiCecMessage message) {
        if (!replaceMessageIfBuffered(message, 4) && !replaceMessageIfBuffered(message, 13)) {
            this.mBuffer.add(message);
        }
    }

    private void bufferSystemAudioModeRequest(com.android.server.hdmi.HdmiCecMessage message) {
        if (!replaceMessageIfBuffered(message, 112)) {
            this.mBuffer.add(message);
        }
    }

    private void bufferRoutingChange(com.android.server.hdmi.HdmiCecMessage message) {
        if (!replaceMessageIfBuffered(message, 128)) {
            this.mBuffer.add(message);
        }
    }

    private void bufferSetStreamPath(com.android.server.hdmi.HdmiCecMessage message) {
        if (!replaceMessageIfBuffered(message, 134)) {
            this.mBuffer.add(message);
        }
    }

    public java.util.List<com.android.server.hdmi.HdmiCecMessage> getBuffer() {
        return new java.util.ArrayList(this.mBuffer);
    }

    private boolean replaceMessageIfBuffered(com.android.server.hdmi.HdmiCecMessage message, int opcode) {
        for (int i = 0; i < this.mBuffer.size(); i++) {
            com.android.server.hdmi.HdmiCecMessage bufferedMessage = this.mBuffer.get(i);
            if (bufferedMessage.getOpcode() == opcode) {
                this.mBuffer.set(i, message);
                return true;
            }
        }
        return false;
    }
}
