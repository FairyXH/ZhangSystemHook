package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecAtomWriter {
    private static final int ERROR_CODE_UNKNOWN = -1;
    protected static final int FEATURE_ABORT_OPCODE_UNKNOWN = 256;

    public void messageReported(com.android.server.hdmi.HdmiCecMessage message, int direction, int callingUid, int errorCode) {
        com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedGenericArgs genericArgs = createMessageReportedGenericArgs(message, direction, errorCode, callingUid);
        com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs specialArgs = createMessageReportedSpecialArgs(message);
        messageReportedBase(genericArgs, specialArgs);
    }

    public void messageReported(com.android.server.hdmi.HdmiCecMessage message, int direction, int callingUid) {
        messageReported(message, direction, callingUid, -1);
    }

    private com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedGenericArgs createMessageReportedGenericArgs(com.android.server.hdmi.HdmiCecMessage message, int direction, int errorCode, int callingUid) {
        int sendMessageResult;
        if (errorCode == -1) {
            sendMessageResult = 0;
        } else {
            sendMessageResult = errorCode + 10;
        }
        return new com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedGenericArgs(callingUid, direction, message.getSource(), message.getDestination(), message.getOpcode(), sendMessageResult);
    }

    private com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs createMessageReportedSpecialArgs(com.android.server.hdmi.HdmiCecMessage message) {
        switch (message.getOpcode()) {
            case 0:
                return createFeatureAbortSpecialArgs(message);
            case 68:
                return createUserControlPressedSpecialArgs(message);
            default:
                return new com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs();
        }
    }

    private com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs createUserControlPressedSpecialArgs(com.android.server.hdmi.HdmiCecMessage message) {
        com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs specialArgs = new com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs();
        if (message.getParams().length > 0) {
            int keycode = message.getParams()[0];
            if (keycode >= 30 && keycode <= 41) {
                specialArgs.mUserControlPressedCommand = 2;
            } else {
                specialArgs.mUserControlPressedCommand = keycode + 256;
            }
        }
        return specialArgs;
    }

    private com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs createFeatureAbortSpecialArgs(com.android.server.hdmi.HdmiCecMessage message) {
        com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs specialArgs = new com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs();
        if (message.getParams().length > 0) {
            specialArgs.mFeatureAbortOpcode = message.getParams()[0] & 255;
            if (message.getParams().length > 1) {
                specialArgs.mFeatureAbortReason = message.getParams()[1] + 10;
            }
        }
        return specialArgs;
    }

    private void messageReportedBase(com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedGenericArgs genericArgs, com.android.server.hdmi.HdmiCecAtomWriter.MessageReportedSpecialArgs specialArgs) {
        writeHdmiCecMessageReportedAtom(genericArgs.mUid, genericArgs.mDirection, genericArgs.mInitiatorLogicalAddress, genericArgs.mDestinationLogicalAddress, genericArgs.mOpcode, genericArgs.mSendMessageResult, specialArgs.mUserControlPressedCommand, specialArgs.mFeatureAbortOpcode, specialArgs.mFeatureAbortReason);
    }

    protected void writeHdmiCecMessageReportedAtom(int uid, int direction, int initiatorLogicalAddress, int destinationLogicalAddress, int opcode, int sendMessageResult, int userControlPressedCommand, int featureAbortOpcode, int featureAbortReason) {
        com.android.internal.util.FrameworkStatsLog.write(310, uid, direction, initiatorLogicalAddress, destinationLogicalAddress, opcode, sendMessageResult, userControlPressedCommand, featureAbortOpcode, featureAbortReason);
    }

    public void activeSourceChanged(int logicalAddress, int physicalAddress, int relationshipToActiveSource) {
        com.android.internal.util.FrameworkStatsLog.write(309, logicalAddress, physicalAddress, relationshipToActiveSource);
    }

    public void earcStatusChanged(boolean isSupported, boolean isEnabled, int oldConnectionState, int newConnectionState, int enumLogReason) {
        int enumOldConnectionState = earcStateToEnum(oldConnectionState);
        int enumNewConnectionState = earcStateToEnum(newConnectionState);
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HDMI_EARC_STATUS_REPORTED, isSupported, isEnabled, enumOldConnectionState, enumNewConnectionState, enumLogReason);
    }

    public void dsmStatusChanged(boolean isSupported, boolean isEnabled, int enumLogReason) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.HDMI_SOUNDBAR_MODE_STATUS_REPORTED, isSupported, isEnabled, enumLogReason);
    }

    private int earcStateToEnum(int earcState) {
        switch (earcState) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 0;
        }
    }

    private class MessageReportedGenericArgs {
        final int mDestinationLogicalAddress;
        final int mDirection;
        final int mInitiatorLogicalAddress;
        final int mOpcode;
        final int mSendMessageResult;
        final int mUid;

        MessageReportedGenericArgs(int uid, int direction, int initiatorLogicalAddress, int destinationLogicalAddress, int opcode, int sendMessageResult) {
            this.mUid = uid;
            this.mDirection = direction;
            this.mInitiatorLogicalAddress = initiatorLogicalAddress;
            this.mDestinationLogicalAddress = destinationLogicalAddress;
            this.mOpcode = opcode;
            this.mSendMessageResult = sendMessageResult;
        }
    }

    private class MessageReportedSpecialArgs {
        int mFeatureAbortOpcode;
        int mFeatureAbortReason;
        int mUserControlPressedCommand;

        private MessageReportedSpecialArgs() {
            this.mUserControlPressedCommand = 0;
            this.mFeatureAbortOpcode = 256;
            this.mFeatureAbortReason = 0;
        }
    }
}
