package com.android.server.usb.hal.port;

/* JADX INFO: loaded from: classes3.dex */
public final class RawPortInfo implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<com.android.server.usb.hal.port.RawPortInfo> CREATOR = new android.os.Parcelable.Creator<com.android.server.usb.hal.port.RawPortInfo>() { // from class: com.android.server.usb.hal.port.RawPortInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public com.android.server.usb.hal.port.RawPortInfo createFromParcel(android.os.Parcel in) {
            java.lang.String id = in.readString();
            int supportedModes = in.readInt();
            int supportedContaminantProtectionModes = in.readInt();
            int currentMode = in.readInt();
            boolean canChangeMode = in.readByte() != 0;
            int currentPowerRole = in.readInt();
            boolean canChangePowerRole = in.readByte() != 0;
            int currentDataRole = in.readInt();
            boolean canChangeDataRole = in.readByte() != 0;
            boolean supportsEnableContaminantPresenceProtection = in.readBoolean();
            int contaminantProtectionStatus = in.readInt();
            boolean supportsEnableContaminantPresenceDetection = in.readBoolean();
            int contaminantDetectionStatus = in.readInt();
            int usbDataStatus = in.readInt();
            boolean powerTransferLimited = in.readBoolean();
            int powerBrickConnectionStatus = in.readInt();
            boolean supportsComplianceWarnings = in.readBoolean();
            int[] complianceWarnings = in.createIntArray();
            int plugState = in.readInt();
            int supportedAltModes = in.readInt();
            android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo = (supportedAltModes & 1) != 0 ? (android.hardware.usb.DisplayPortAltModeInfo) android.hardware.usb.DisplayPortAltModeInfo.CREATOR.createFromParcel(in) : null;
            return new com.android.server.usb.hal.port.RawPortInfo(id, supportedModes, supportedContaminantProtectionModes, currentMode, canChangeMode, currentPowerRole, canChangePowerRole, currentDataRole, canChangeDataRole, supportsEnableContaminantPresenceProtection, contaminantProtectionStatus, supportsEnableContaminantPresenceDetection, contaminantDetectionStatus, usbDataStatus, powerTransferLimited, powerBrickConnectionStatus, supportsComplianceWarnings, complianceWarnings, plugState, supportedAltModes, displayPortAltModeInfo);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public com.android.server.usb.hal.port.RawPortInfo[] newArray(int size) {
            return new com.android.server.usb.hal.port.RawPortInfo[size];
        }
    };
    public boolean canChangeDataRole;
    public boolean canChangeMode;
    public boolean canChangePowerRole;
    public int[] complianceWarnings;
    public int contaminantDetectionStatus;
    public int contaminantProtectionStatus;
    public int currentDataRole;
    public int currentMode;
    public int currentPowerRole;
    public android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo;
    public int plugState;
    public final java.lang.String portId;
    public int powerBrickConnectionStatus;
    public boolean powerTransferLimited;
    public int supportedAltModes;
    public final int supportedContaminantProtectionModes;
    public final int supportedModes;
    public final boolean supportsComplianceWarnings;
    public boolean supportsEnableContaminantPresenceDetection;
    public boolean supportsEnableContaminantPresenceProtection;
    public int usbDataStatus;

    public RawPortInfo(java.lang.String portId, int supportedModes) {
        this.portId = portId;
        this.supportedModes = supportedModes;
        this.supportedContaminantProtectionModes = 0;
        this.supportsEnableContaminantPresenceProtection = false;
        this.contaminantProtectionStatus = 0;
        this.supportsEnableContaminantPresenceDetection = false;
        this.contaminantDetectionStatus = 0;
        this.usbDataStatus = 0;
        this.powerTransferLimited = false;
        this.powerBrickConnectionStatus = 0;
        this.supportsComplianceWarnings = false;
        this.complianceWarnings = new int[0];
        this.plugState = 0;
        this.supportedAltModes = 0;
        this.displayPortAltModeInfo = null;
    }

    public RawPortInfo(java.lang.String portId, int supportedModes, int supportedContaminantProtectionModes, int currentMode, boolean canChangeMode, int currentPowerRole, boolean canChangePowerRole, int currentDataRole, boolean canChangeDataRole, boolean supportsEnableContaminantPresenceProtection, int contaminantProtectionStatus, boolean supportsEnableContaminantPresenceDetection, int contaminantDetectionStatus, int usbDataStatus, boolean powerTransferLimited, int powerBrickConnectionStatus) {
        this(portId, supportedModes, supportedContaminantProtectionModes, currentMode, canChangeMode, currentPowerRole, canChangePowerRole, currentDataRole, canChangeDataRole, supportsEnableContaminantPresenceProtection, contaminantProtectionStatus, supportsEnableContaminantPresenceDetection, contaminantDetectionStatus, usbDataStatus, powerTransferLimited, powerBrickConnectionStatus, false, new int[0], 0, 0, null);
    }

    public RawPortInfo(java.lang.String portId, int supportedModes, int supportedContaminantProtectionModes, int currentMode, boolean canChangeMode, int currentPowerRole, boolean canChangePowerRole, int currentDataRole, boolean canChangeDataRole, boolean supportsEnableContaminantPresenceProtection, int contaminantProtectionStatus, boolean supportsEnableContaminantPresenceDetection, int contaminantDetectionStatus, int usbDataStatus, boolean powerTransferLimited, int powerBrickConnectionStatus, boolean supportsComplianceWarnings, int[] complianceWarnings, int plugState, int supportedAltModes, android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo) {
        this.portId = portId;
        this.supportedModes = supportedModes;
        this.supportedContaminantProtectionModes = supportedContaminantProtectionModes;
        this.currentMode = currentMode;
        this.canChangeMode = canChangeMode;
        this.currentPowerRole = currentPowerRole;
        this.canChangePowerRole = canChangePowerRole;
        this.currentDataRole = currentDataRole;
        this.canChangeDataRole = canChangeDataRole;
        this.supportsEnableContaminantPresenceProtection = supportsEnableContaminantPresenceProtection;
        this.contaminantProtectionStatus = contaminantProtectionStatus;
        this.supportsEnableContaminantPresenceDetection = supportsEnableContaminantPresenceDetection;
        this.contaminantDetectionStatus = contaminantDetectionStatus;
        this.usbDataStatus = usbDataStatus;
        this.powerTransferLimited = powerTransferLimited;
        this.powerBrickConnectionStatus = powerBrickConnectionStatus;
        this.supportsComplianceWarnings = supportsComplianceWarnings;
        this.complianceWarnings = complianceWarnings;
        this.plugState = plugState;
        this.supportedAltModes = supportedAltModes;
        this.displayPortAltModeInfo = displayPortAltModeInfo;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeString(this.portId);
        parcel.writeInt(this.supportedModes);
        parcel.writeInt(this.supportedContaminantProtectionModes);
        parcel.writeInt(this.currentMode);
        parcel.writeByte(this.canChangeMode ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.currentPowerRole);
        parcel.writeByte(this.canChangePowerRole ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.currentDataRole);
        parcel.writeByte(this.canChangeDataRole ? (byte) 1 : (byte) 0);
        parcel.writeBoolean(this.supportsEnableContaminantPresenceProtection);
        parcel.writeInt(this.contaminantProtectionStatus);
        parcel.writeBoolean(this.supportsEnableContaminantPresenceDetection);
        parcel.writeInt(this.contaminantDetectionStatus);
        parcel.writeInt(this.usbDataStatus);
        parcel.writeBoolean(this.powerTransferLimited);
        parcel.writeInt(this.powerBrickConnectionStatus);
        parcel.writeBoolean(this.supportsComplianceWarnings);
        parcel.writeIntArray(this.complianceWarnings);
        parcel.writeInt(this.plugState);
        parcel.writeInt(this.supportedAltModes);
        if ((this.supportedAltModes & 1) != 0) {
            this.displayPortAltModeInfo.writeToParcel(parcel, 0);
        }
    }
}
