package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class DelayedMessageBuffer {
    private final java.util.ArrayList<com.android.server.hdmi.HdmiCecMessage> mBuffer = new java.util.ArrayList<>();
    private final com.android.server.hdmi.HdmiCecLocalDevice mDevice;

    DelayedMessageBuffer(com.android.server.hdmi.HdmiCecLocalDevice device) {
        this.mDevice = device;
    }

    void add(com.android.server.hdmi.HdmiCecMessage message) {
        boolean buffered = true;
        switch (message.getOpcode()) {
            case 114:
            case 192:
                this.mBuffer.add(message);
                break;
            case 130:
                removeActiveSource();
                this.mBuffer.add(message);
                break;
            default:
                buffered = false;
                break;
        }
        if (buffered) {
            com.android.server.hdmi.HdmiLogger.debug("Buffering message:" + message, new java.lang.Object[0]);
        }
    }

    protected void removeActiveSource() {
        java.util.Iterator<com.android.server.hdmi.HdmiCecMessage> iter = this.mBuffer.iterator();
        while (iter.hasNext()) {
            com.android.server.hdmi.HdmiCecMessage message = iter.next();
            if (message.getOpcode() == 130) {
                iter.remove();
            }
        }
    }

    boolean isBuffered(int opcode) {
        for (com.android.server.hdmi.HdmiCecMessage message : this.mBuffer) {
            if (message.getOpcode() == opcode) {
                return true;
            }
        }
        return false;
    }

    void processAllMessages() {
        java.util.ArrayList<com.android.server.hdmi.HdmiCecMessage> copiedBuffer = new java.util.ArrayList<>(this.mBuffer);
        this.mBuffer.clear();
        for (com.android.server.hdmi.HdmiCecMessage message : copiedBuffer) {
            this.mDevice.onMessage(message);
            com.android.server.hdmi.HdmiLogger.debug("Processing message:" + message, new java.lang.Object[0]);
        }
    }

    void processMessagesForDevice(int address) {
        java.util.ArrayList<com.android.server.hdmi.HdmiCecMessage> copiedBuffer = new java.util.ArrayList<>(this.mBuffer);
        this.mBuffer.clear();
        com.android.server.hdmi.HdmiLogger.debug("Checking message for address:" + address, new java.lang.Object[0]);
        for (com.android.server.hdmi.HdmiCecMessage message : copiedBuffer) {
            if (message.getSource() != address) {
                this.mBuffer.add(message);
            } else if (message.getOpcode() == 130 && !this.mDevice.isInputReady(android.hardware.hdmi.HdmiDeviceInfo.idForCecDevice(address))) {
                this.mBuffer.add(message);
            } else {
                this.mDevice.onMessage(message);
                com.android.server.hdmi.HdmiLogger.debug("Processing message:" + message, new java.lang.Object[0]);
            }
        }
    }

    void processActiveSource(int address) {
        java.util.ArrayList<com.android.server.hdmi.HdmiCecMessage> copiedBuffer = new java.util.ArrayList<>(this.mBuffer);
        this.mBuffer.clear();
        for (com.android.server.hdmi.HdmiCecMessage message : copiedBuffer) {
            if (message.getOpcode() == 130 && message.getSource() == address) {
                this.mDevice.onMessage(message);
                com.android.server.hdmi.HdmiLogger.debug("Processing message:" + message, new java.lang.Object[0]);
            } else {
                this.mBuffer.add(message);
            }
        }
    }
}
