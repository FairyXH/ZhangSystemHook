package com.android.server.usb.hal.port;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbPortAidl implements com.android.server.usb.hal.port.UsbPortHal {
    public static final int AIDL_USB_DATA_STATUS_DISABLED_CONTAMINANT = 3;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DEBUG = 6;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DOCK = 4;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DOCK_DEVICE_MODE = 8;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_DOCK_HOST_MODE = 7;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_FORCE = 5;
    public static final int AIDL_USB_DATA_STATUS_DISABLED_OVERHEAT = 2;
    public static final int AIDL_USB_DATA_STATUS_ENABLED = 1;
    public static final int AIDL_USB_DATA_STATUS_UNKNOWN = 0;
    private static final java.lang.String USB_AIDL_SERVICE = "android.hardware.usb.IUsb/default";
    private android.os.IBinder mBinder;
    private com.android.server.usb.hal.port.UsbPortAidl.HALCallback mHALCallback;
    private final java.lang.Object mLock = new java.lang.Object();
    private com.android.server.usb.UsbPortManager mPortManager;
    private android.hardware.usb.IUsb mProxy;
    public com.android.internal.util.IndentingPrintWriter mPw;
    private boolean mSystemReady;
    private long mTransactionId;
    private static final java.lang.String TAG = com.android.server.usb.hal.port.UsbPortAidl.class.getSimpleName();
    private static final android.util.LongSparseArray<android.hardware.usb.IUsbOperationInternal> sCallbacks = new android.util.LongSparseArray<>();

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public int getUsbHalVersion() throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                throw new android.os.RemoteException("IUsb not initialized yet");
            }
        }
        com.android.server.usb.UsbPortManager.logAndPrint(4, null, "USB HAL AIDL version: USB_HAL_V2_0");
        return 20;
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void systemReady() {
        this.mSystemReady = true;
    }

    public void serviceDied() {
        com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Usb AIDL hal service died");
        synchronized (this.mLock) {
            com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "MSG_MTK_HAL_STATS: HAL service died");
            ((com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0])).usbGadgetServiceStatusRecord("Usb Gadget hal service died", "mtk hal service died");
            this.mProxy = null;
        }
        connectToProxy(null);
    }

    private void connectToProxy(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            if (this.mProxy != null) {
                return;
            }
            try {
                this.mBinder = android.os.ServiceManager.waitForService(USB_AIDL_SERVICE);
                this.mProxy = android.hardware.usb.IUsb.Stub.asInterface(this.mBinder);
                this.mBinder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.usb.hal.port.UsbPortAidl$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        this.f$0.serviceDied();
                    }
                }, 0);
                this.mProxy.setCallback(this.mHALCallback);
                android.hardware.usb.IUsb iUsb = this.mProxy;
                long j = this.mTransactionId + 1;
                this.mTransactionId = j;
                iUsb.queryPortStatus(j);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(pw, "connectToProxy: usb hal service not responding", e);
            } catch (java.util.NoSuchElementException e2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(pw, "connectToProxy: usb hal service not found. Did the service fail to start?", e2);
            }
        }
    }

    static boolean isServicePresent(com.android.internal.util.IndentingPrintWriter pw) {
        try {
            return android.os.ServiceManager.isDeclared(USB_AIDL_SERVICE);
        } catch (java.util.NoSuchElementException e) {
            com.android.server.usb.UsbPortManager.logAndPrintException(pw, "connectToProxy: usb Aidl hal service not found.", e);
            return false;
        }
    }

    public UsbPortAidl(com.android.server.usb.UsbPortManager portManager, com.android.internal.util.IndentingPrintWriter pw) {
        this.mPortManager = (com.android.server.usb.UsbPortManager) java.util.Objects.requireNonNull(portManager);
        this.mPw = pw;
        this.mHALCallback = new com.android.server.usb.hal.port.UsbPortAidl.HALCallback(null, this.mPortManager, this);
        connectToProxy(this.mPw);
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableContaminantPresenceDetection(java.lang.String portName, boolean enable, long operationID) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID: " + operationID);
                return;
            }
            try {
                this.mProxy.enableContaminantPresenceDetection(portName, enable, operationID);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set contaminant detection. opID:" + operationID, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void queryPortStatus(long operationID) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + operationID);
                return;
            }
            try {
                this.mProxy.queryPortStatus(operationID);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(null, "ServiceStart: Failed to query port status. opID:" + operationID, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchMode(java.lang.String portId, int newMode, long operationID) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + operationID);
                return;
            }
            android.hardware.usb.PortRole newRole = new android.hardware.usb.PortRole();
            newRole.setMode((byte) newMode);
            try {
                this.mProxy.switchRole(portId, newRole, operationID);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB port mode: portId=" + portId + ", newMode=" + android.hardware.usb.UsbPort.modeToString(newMode) + "opID:" + operationID, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchPowerRole(java.lang.String portId, int newPowerRole, long operationID) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + operationID);
                return;
            }
            android.hardware.usb.PortRole newRole = new android.hardware.usb.PortRole();
            newRole.setPowerRole((byte) newPowerRole);
            try {
                this.mProxy.switchRole(portId, newRole, operationID);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB power role: portId=" + portId + ", newPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(newPowerRole) + "opID:" + operationID, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchDataRole(java.lang.String portId, int newDataRole, long operationID) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry ! opID:" + operationID);
                return;
            }
            android.hardware.usb.PortRole newRole = new android.hardware.usb.PortRole();
            newRole.setDataRole((byte) newDataRole);
            try {
                this.mProxy.switchRole(portId, newRole, operationID);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB data role: portId=" + portId + ", newDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(newDataRole) + "opID:" + operationID, e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void resetUsbPort(java.lang.String portName, long operationID, android.hardware.usb.IUsbOperationInternal callback) {
        java.util.Objects.requireNonNull(portName);
        java.util.Objects.requireNonNull(callback);
        long key = operationID;
        synchronized (this.mLock) {
            try {
                if (this.mProxy == null) {
                    com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "resetUsbPort: Proxy is null. Retry !opID:" + operationID);
                    callback.onOperationComplete(1);
                }
                while (sCallbacks.get(key) != null) {
                    key = java.util.concurrent.ThreadLocalRandom.current().nextInt();
                }
                if (key != operationID) {
                    com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "resetUsbPort: operationID exists ! opID:" + operationID + " key:" + key);
                }
                try {
                    sCallbacks.put(key, callback);
                    this.mProxy.resetUsbPort(portName, key);
                } catch (android.os.RemoteException e) {
                    com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "resetUsbPort: Failed to resetUsbPort: portID=" + portName + "opId:" + operationID, e);
                    callback.onOperationComplete(1);
                    sCallbacks.remove(key);
                }
            } catch (android.os.RemoteException e2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "resetUsbPort: Failed to call onOperationComplete portID=" + portName + "opID:" + operationID, e2);
                sCallbacks.remove(key);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public boolean enableUsbData(java.lang.String portName, boolean enable, long operationID, android.hardware.usb.IUsbOperationInternal callback) {
        java.util.Objects.requireNonNull(portName);
        java.util.Objects.requireNonNull(callback);
        long key = operationID;
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mProxy == null) {
                        com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "enableUsbData: Proxy is null. Retry !opID:" + operationID);
                        callback.onOperationComplete(1);
                        return false;
                    }
                    while (sCallbacks.get(key) != null) {
                        key = java.util.concurrent.ThreadLocalRandom.current().nextInt();
                    }
                    if (key != operationID) {
                        com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "enableUsbData: operationID exists ! opID:" + operationID + " key:" + key);
                    }
                    try {
                        sCallbacks.put(key, callback);
                        this.mProxy.enableUsbData(portName, enable, key);
                        return true;
                    } catch (android.os.RemoteException e) {
                        com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableUsbData: Failed to invoke enableUsbData: portID=" + portName + "opID:" + operationID, e);
                        callback.onOperationComplete(1);
                        sCallbacks.remove(key);
                        return false;
                    }
                } catch (android.os.RemoteException e2) {
                    com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableUsbData: Failed to call onOperationComplete portID=" + portName + "opID:" + operationID, e2);
                    sCallbacks.remove(key);
                    return false;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableLimitPowerTransfer(java.lang.String portName, boolean limit, long operationID, android.hardware.usb.IUsbOperationInternal callback) {
        java.util.Objects.requireNonNull(portName);
        long key = operationID;
        synchronized (this.mLock) {
            try {
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableLimitPowerTransfer: Failed to call onOperationComplete portID=" + portName + " opID:" + operationID, e);
            }
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "enableLimitPowerTransfer: Proxy is null. Retry !opID:" + operationID);
                callback.onOperationComplete(1);
                return;
            }
            while (sCallbacks.get(key) != null) {
                key = java.util.concurrent.ThreadLocalRandom.current().nextInt();
            }
            if (key != operationID) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "enableUsbData: operationID exists ! opID:" + operationID + " key:" + key);
            }
            try {
                sCallbacks.put(key, callback);
                this.mProxy.limitPowerTransfer(portName, limit, key);
            } catch (android.os.RemoteException e2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableLimitPowerTransfer: Failed while invoking AIDL HAL portID=" + portName + " opID:" + operationID, e2);
                if (callback != null) {
                    callback.onOperationComplete(1);
                }
                sCallbacks.remove(key);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableUsbDataWhileDocked(java.lang.String portName, long operationID, android.hardware.usb.IUsbOperationInternal callback) {
        java.util.Objects.requireNonNull(portName);
        long key = operationID;
        synchronized (this.mLock) {
            try {
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableUsbDataWhileDocked: Failed to call onOperationComplete portID=" + portName + " opID:" + operationID, e);
            }
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "enableUsbDataWhileDocked: Proxy is null. Retry !opID:" + operationID);
                callback.onOperationComplete(1);
                return;
            }
            while (sCallbacks.get(key) != null) {
                key = java.util.concurrent.ThreadLocalRandom.current().nextInt();
            }
            if (key != operationID) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "enableUsbDataWhileDocked: operationID exists ! opID:" + operationID + " key:" + key);
            }
            try {
                sCallbacks.put(key, callback);
                this.mProxy.enableUsbDataWhileDocked(portName, key);
            } catch (android.os.RemoteException e2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableUsbDataWhileDocked: error while invoking halportID=" + portName + " opID:" + operationID, e2);
                if (callback != null) {
                    callback.onOperationComplete(1);
                }
                sCallbacks.remove(key);
            }
        }
    }

    private static class HALCallback extends android.hardware.usb.IUsbCallback.Stub {
        public com.android.server.usb.UsbPortManager mPortManager;
        public com.android.internal.util.IndentingPrintWriter mPw;
        public com.android.server.usb.hal.port.UsbPortAidl mUsbPortAidl;

        HALCallback(com.android.internal.util.IndentingPrintWriter pw, com.android.server.usb.UsbPortManager portManager, com.android.server.usb.hal.port.UsbPortAidl usbPortAidl) {
            this.mPw = pw;
            this.mPortManager = portManager;
            this.mUsbPortAidl = usbPortAidl;
        }

        private int toPortMode(byte aidlPortMode) {
            switch (aidlPortMode) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Unrecognized aidlPortMode:" + ((int) aidlPortMode));
                    break;
            }
            return 0;
        }

        private int toSupportedModes(byte[] aidlPortModes) {
            int supportedModes = 0;
            for (byte aidlPortMode : aidlPortModes) {
                supportedModes |= toPortMode(aidlPortMode);
            }
            return supportedModes;
        }

        private int toContaminantProtectionStatus(byte aidlContaminantProtection) {
            switch (aidlContaminantProtection) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Unrecognized aidlContaminantProtection:" + ((int) aidlContaminantProtection));
                    break;
            }
            return 0;
        }

        private int toSupportedContaminantProtectionModes(byte[] aidlModes) {
            int supportedContaminantProtectionModes = 0;
            for (byte aidlMode : aidlModes) {
                supportedContaminantProtectionModes |= toContaminantProtectionStatus(aidlMode);
            }
            return supportedContaminantProtectionModes;
        }

        private int toUsbDataStatusInt(byte[] usbDataStatusHal) {
            int usbDataStatus = 0;
            for (byte b : usbDataStatusHal) {
                switch (b) {
                    case 1:
                        usbDataStatus |= 1;
                        break;
                    case 2:
                        usbDataStatus |= 2;
                        break;
                    case 3:
                        usbDataStatus |= 4;
                        break;
                    case 4:
                        usbDataStatus = usbDataStatus | 8 | 64 | 128;
                        break;
                    case 5:
                        usbDataStatus |= 16;
                        break;
                    case 6:
                        usbDataStatus |= 32;
                        break;
                    case 7:
                        usbDataStatus = usbDataStatus | 64 | 8;
                        break;
                    case 8:
                        usbDataStatus = usbDataStatus | 128 | 8;
                        break;
                    default:
                        usbDataStatus |= 0;
                        break;
                }
            }
            com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "AIDL UsbDataStatus:" + usbDataStatus);
            return usbDataStatus;
        }

        private int[] formatComplianceWarnings(int[] complianceWarnings) {
            java.util.Objects.requireNonNull(complianceWarnings);
            android.util.IntArray newComplianceWarnings = new android.util.IntArray();
            java.util.Arrays.sort(complianceWarnings);
            for (int warning : complianceWarnings) {
                if (newComplianceWarnings.indexOf(warning) == -1 && warning >= 1) {
                    if (com.android.internal.hidden_from_bootclasspath.android.hardware.usb.flags.Flags.enableUsbDataComplianceWarning()) {
                        if (warning > 9) {
                            newComplianceWarnings.add(1);
                        } else {
                            newComplianceWarnings.add(warning);
                        }
                    } else if (warning > 4) {
                        newComplianceWarnings.add(1);
                    } else {
                        newComplianceWarnings.add(warning);
                    }
                }
            }
            return newComplianceWarnings.toArray();
        }

        private int toSupportedAltModesInt(android.hardware.usb.AltModeData[] supportedAltModes) {
            int supportedAltModesInt = 0;
            for (android.hardware.usb.AltModeData altModeData : supportedAltModes) {
                switch (altModeData.getTag()) {
                    case 0:
                        supportedAltModesInt |= 1;
                        break;
                }
            }
            return supportedAltModesInt;
        }

        private int toDisplayPortAltModeNumLanesInt(int pinAssignment) {
            switch (pinAssignment) {
                case 1:
                case 3:
                case 5:
                    return 4;
                case 2:
                case 4:
                case 6:
                    return 2;
                default:
                    return 0;
            }
        }

        private android.hardware.usb.DisplayPortAltModeInfo formatDisplayPortAltModeInfo(android.hardware.usb.AltModeData[] supportedAltModes) {
            for (android.hardware.usb.AltModeData altModeData : supportedAltModes) {
                if (altModeData.getTag() == 0) {
                    android.hardware.usb.AltModeData.DisplayPortAltModeData displayPortData = altModeData.getDisplayPortAltModeData();
                    return new android.hardware.usb.DisplayPortAltModeInfo(displayPortData.partnerSinkStatus, displayPortData.cableStatus, toDisplayPortAltModeNumLanesInt(displayPortData.pinAssignment), displayPortData.hpd, displayPortData.linkTrainingStatus);
                }
            }
            return null;
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyPortStatusChange(android.hardware.usb.PortStatus[] currentPortStatus, int retval) {
            android.hardware.usb.PortStatus[] portStatusArr = currentPortStatus;
            if (!this.mUsbPortAidl.mSystemReady) {
                return;
            }
            if (retval != 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "port status enquiry failed");
                return;
            }
            java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> newPortInfo = new java.util.ArrayList<>();
            int numStatus = portStatusArr.length;
            int i = 0;
            while (i < numStatus) {
                android.hardware.usb.PortStatus current = portStatusArr[i];
                com.android.server.usb.hal.port.RawPortInfo temp = new com.android.server.usb.hal.port.RawPortInfo(current.portName, toSupportedModes(current.supportedModes), toSupportedContaminantProtectionModes(current.supportedContaminantProtectionModes), toPortMode(current.currentMode), current.canChangeMode, current.currentPowerRole, current.canChangePowerRole, current.currentDataRole, current.canChangeDataRole, current.supportsEnableContaminantPresenceProtection, toContaminantProtectionStatus(current.contaminantProtectionStatus), current.supportsEnableContaminantPresenceDetection, current.contaminantDetectionStatus, toUsbDataStatusInt(current.usbDataStatus), current.powerTransferLimited, current.powerBrickStatus, current.supportsComplianceWarnings, formatComplianceWarnings(current.complianceWarnings), current.plugOrientation, toSupportedAltModesInt(current.supportedAltModes), formatDisplayPortAltModeInfo(current.supportedAltModes));
                newPortInfo.add(temp);
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "ClientCallback AIDL V1: " + current.portName);
                i++;
                portStatusArr = currentPortStatus;
            }
            this.mPortManager.updatePorts(newPortInfo);
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyRoleSwitchStatus(java.lang.String portName, android.hardware.usb.PortRole role, int retval, long operationID) {
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, portName + " role switch successful. opID:" + operationID);
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + " role switch failed. err:" + retval + "opID:" + operationID);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyQueryPortStatus(java.lang.String portName, int retval, long operationID) {
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, portName + ": opID:" + operationID + " successful");
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + ": opID:" + operationID + " failed. err:" + retval);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyEnableUsbDataStatus(java.lang.String portName, boolean enable, int retval, long operationID) {
            int i;
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "notifyEnableUsbDataStatus:" + portName + ": opID:" + operationID + " enable:" + enable);
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + "notifyEnableUsbDataStatus: opID:" + operationID + " failed. err:" + retval);
            }
            try {
                android.hardware.usb.IUsbOperationInternal iUsbOperationInternal = (android.hardware.usb.IUsbOperationInternal) com.android.server.usb.hal.port.UsbPortAidl.sCallbacks.get(operationID);
                if (retval == 0) {
                    i = 0;
                } else {
                    i = 1;
                }
                iUsbOperationInternal.onOperationComplete(i);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "notifyEnableUsbDataStatus: Failed to call onOperationComplete", e);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyContaminantEnabledStatus(java.lang.String portName, boolean enable, int retval, long operationID) {
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "notifyContaminantEnabledStatus:" + portName + ": opID:" + operationID + " enable:" + enable);
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + "notifyContaminantEnabledStatus: opID:" + operationID + " failed. err:" + retval);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyLimitPowerTransferStatus(java.lang.String portName, boolean limit, int retval, long operationID) {
            int i;
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, portName + ": opID:" + operationID + " successful");
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + "notifyLimitPowerTransferStatus: opID:" + operationID + " failed. err:" + retval);
            }
            try {
                android.hardware.usb.IUsbOperationInternal callback = (android.hardware.usb.IUsbOperationInternal) com.android.server.usb.hal.port.UsbPortAidl.sCallbacks.get(operationID);
                if (callback != null) {
                    android.hardware.usb.IUsbOperationInternal iUsbOperationInternal = (android.hardware.usb.IUsbOperationInternal) com.android.server.usb.hal.port.UsbPortAidl.sCallbacks.get(operationID);
                    if (retval == 0) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    iUsbOperationInternal.onOperationComplete(i);
                }
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "enableLimitPowerTransfer: Failed to call onOperationComplete", e);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyEnableUsbDataWhileDockedStatus(java.lang.String portName, int retval, long operationID) {
            int i;
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, portName + ": opID:" + operationID + " successful");
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + "notifyEnableUsbDataWhileDockedStatus: opID:" + operationID + " failed. err:" + retval);
            }
            try {
                android.hardware.usb.IUsbOperationInternal callback = (android.hardware.usb.IUsbOperationInternal) com.android.server.usb.hal.port.UsbPortAidl.sCallbacks.get(operationID);
                if (callback != null) {
                    android.hardware.usb.IUsbOperationInternal iUsbOperationInternal = (android.hardware.usb.IUsbOperationInternal) com.android.server.usb.hal.port.UsbPortAidl.sCallbacks.get(operationID);
                    if (retval == 0) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    iUsbOperationInternal.onOperationComplete(i);
                }
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "notifyEnableUsbDataWhileDockedStatus: Failed to call onOperationComplete", e);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public void notifyResetUsbPortStatus(java.lang.String portName, int retval, long operationID) {
            int i;
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "notifyResetUsbPortStatus:" + portName + ": opID:" + operationID);
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + "notifyEnableUsbDataStatus: opID:" + operationID + " failed. err:" + retval);
            }
            try {
                android.hardware.usb.IUsbOperationInternal iUsbOperationInternal = (android.hardware.usb.IUsbOperationInternal) com.android.server.usb.hal.port.UsbPortAidl.sCallbacks.get(operationID);
                if (retval == 0) {
                    i = 0;
                } else {
                    i = 1;
                }
                iUsbOperationInternal.onOperationComplete(i);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "notifyResetUsbPortStatus: Failed to call onOperationComplete", e);
            }
        }

        @Override // android.hardware.usb.IUsbCallback
        public java.lang.String getInterfaceHash() {
            return "7fe46e9531884739d925b8caeee9dba5c411e228";
        }

        @Override // android.hardware.usb.IUsbCallback
        public int getInterfaceVersion() {
            return 3;
        }
    }
}
