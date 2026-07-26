package com.android.server.usb.hal.port;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbPortHidl implements com.android.server.usb.hal.port.UsbPortHal {
    private static final int USB_HAL_DEATH_COOKIE = 1000;
    private com.android.server.usb.hal.port.UsbPortHidl.HALCallback mHALCallback;
    private final java.lang.Object mLock = new java.lang.Object();
    private com.android.server.usb.UsbPortManager mPortManager;
    private android.hardware.usb.V1_0.IUsb mProxy;
    public com.android.internal.util.IndentingPrintWriter mPw;
    private boolean mSystemReady;
    private static final java.lang.String TAG = com.android.server.usb.hal.port.UsbPortHidl.class.getSimpleName();
    private static int sUsbDataStatus = 0;

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public int getUsbHalVersion() throws android.os.RemoteException {
        int version;
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                throw new android.os.RemoteException("IUsb not initialized yet");
            }
            if (android.hardware.usb.V1_3.IUsb.castFrom((android.os.IHwInterface) this.mProxy) != null) {
                version = 13;
            } else if (android.hardware.usb.V1_2.IUsb.castFrom((android.os.IHwInterface) this.mProxy) != null) {
                version = 12;
            } else if (android.hardware.usb.V1_1.IUsb.castFrom((android.os.IHwInterface) this.mProxy) != null) {
                version = 11;
            } else {
                version = 10;
            }
            com.android.server.usb.UsbPortManager.logAndPrint(4, null, "USB HAL HIDL version: " + version);
        }
        return version;
    }

    final class DeathRecipient implements android.os.IHwBinder.DeathRecipient {
        public com.android.internal.util.IndentingPrintWriter pw;

        DeathRecipient(com.android.internal.util.IndentingPrintWriter pw) {
            this.pw = pw;
        }

        public void serviceDied(long cookie) {
            if (cookie == 1000) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.pw, "Usb hal service died cookie: " + cookie);
                synchronized (com.android.server.usb.hal.port.UsbPortHidl.this.mLock) {
                    com.android.server.usb.hal.port.UsbPortHidl.this.mProxy = null;
                }
            }
        }
    }

    final class ServiceNotification extends android.hidl.manager.V1_0.IServiceNotification.Stub {
        ServiceNotification() {
        }

        @Override // android.hidl.manager.V1_0.IServiceNotification
        public void onRegistration(java.lang.String fqName, java.lang.String name, boolean preexisting) {
            com.android.server.usb.UsbPortManager.logAndPrint(4, null, "Usb hal service started " + fqName + " " + name);
            com.android.server.usb.hal.port.UsbPortHidl.this.connectToProxy(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectToProxy(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            if (this.mProxy != null) {
                return;
            }
            try {
                this.mProxy = android.hardware.usb.V1_0.IUsb.getService();
                this.mProxy.linkToDeath(new com.android.server.usb.hal.port.UsbPortHidl.DeathRecipient(pw), 1000L);
                this.mProxy.setCallback(this.mHALCallback);
                this.mProxy.queryPortStatus();
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(pw, "connectToProxy: usb hal service not responding", e);
            } catch (java.util.NoSuchElementException e2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(pw, "connectToProxy: usb hal service not found. Did the service fail to start?", e2);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void systemReady() {
        this.mSystemReady = true;
    }

    static boolean isServicePresent(com.android.internal.util.IndentingPrintWriter pw) {
        try {
            android.hardware.usb.V1_0.IUsb.getService(true);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbPortManager.logAndPrintException(pw, "IUSB hal service present but failed to get service", e);
        } catch (java.util.NoSuchElementException e2) {
            com.android.server.usb.UsbPortManager.logAndPrintException(pw, "connectToProxy: usb hidl hal service not found.", e2);
            return false;
        }
        return true;
    }

    public UsbPortHidl(com.android.server.usb.UsbPortManager portManager, com.android.internal.util.IndentingPrintWriter pw) {
        this.mPortManager = (com.android.server.usb.UsbPortManager) java.util.Objects.requireNonNull(portManager);
        this.mPw = pw;
        this.mHALCallback = new com.android.server.usb.hal.port.UsbPortHidl.HALCallback(null, this.mPortManager, this);
        try {
            com.android.server.usb.hal.port.UsbPortHidl.ServiceNotification serviceNotification = new com.android.server.usb.hal.port.UsbPortHidl.ServiceNotification();
            boolean ret = android.hidl.manager.V1_0.IServiceManager.getService().registerForNotifications(android.hardware.usb.V1_0.IUsb.kInterfaceName, "", serviceNotification);
            if (!ret) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, null, "Failed to register service start notification");
            }
            connectToProxy(this.mPw);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbPortManager.logAndPrintException(null, "Failed to register service start notification", e);
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableContaminantPresenceDetection(java.lang.String portName, boolean enable, long transactionId) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry !");
                return;
            }
            try {
                android.hardware.usb.V1_2.IUsb proxy = android.hardware.usb.V1_2.IUsb.castFrom((android.os.IHwInterface) this.mProxy);
                proxy.enableContaminantPresenceDetection(portName, enable);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set contaminant detection", e);
            } catch (java.lang.ClassCastException e2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Method only applicable to V1.2 or above implementation", e2);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void queryPortStatus(long transactionId) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry !");
                return;
            }
            try {
                this.mProxy.queryPortStatus();
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(null, "ServiceStart: Failed to query port status", e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchMode(java.lang.String portId, int newMode, long transactionId) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry !");
                return;
            }
            android.hardware.usb.V1_0.PortRole newRole = new android.hardware.usb.V1_0.PortRole();
            newRole.type = 2;
            newRole.role = newMode;
            try {
                this.mProxy.switchRole(portId, newRole);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB port mode: portId=" + portId + ", newMode=" + android.hardware.usb.UsbPort.modeToString(newRole.role), e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchPowerRole(java.lang.String portId, int newPowerRole, long transactionId) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry !");
                return;
            }
            android.hardware.usb.V1_0.PortRole newRole = new android.hardware.usb.V1_0.PortRole();
            newRole.type = 1;
            newRole.role = newPowerRole;
            try {
                this.mProxy.switchRole(portId, newRole);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB power role: portId=" + portId + ", newPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(newRole.role), e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableLimitPowerTransfer(java.lang.String portName, boolean limit, long transactionId, android.hardware.usb.IUsbOperationInternal callback) {
        try {
            callback.onOperationComplete(2);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to call onOperationComplete", e);
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void enableUsbDataWhileDocked(java.lang.String portName, long transactionId, android.hardware.usb.IUsbOperationInternal callback) {
        try {
            callback.onOperationComplete(2);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to call onOperationComplete", e);
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void switchDataRole(java.lang.String portId, int newDataRole, long transactionId) {
        synchronized (this.mLock) {
            if (this.mProxy == null) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "Proxy is null. Retry !");
                return;
            }
            android.hardware.usb.V1_0.PortRole newRole = new android.hardware.usb.V1_0.PortRole();
            newRole.type = 0;
            newRole.role = newDataRole;
            try {
                this.mProxy.switchRole(portId, newRole);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to set the USB data role: portId=" + portId + ", newDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(newRole.role), e);
            }
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public void resetUsbPort(java.lang.String portName, long transactionId, android.hardware.usb.IUsbOperationInternal callback) {
        try {
            callback.onOperationComplete(2);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to call onOperationComplete. opID:" + transactionId + " portId:" + portName, e);
        }
    }

    @Override // com.android.server.usb.hal.port.UsbPortHal
    public boolean enableUsbData(java.lang.String portName, boolean enable, long transactionId, android.hardware.usb.IUsbOperationInternal callback) {
        int i;
        boolean success;
        try {
            int halVersion = getUsbHalVersion();
            if (halVersion != 13) {
                try {
                    callback.onOperationComplete(2);
                } catch (android.os.RemoteException e) {
                    com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to call onOperationComplete. opID:" + transactionId + " portId:" + portName, e);
                }
                return false;
            }
            synchronized (this.mLock) {
                i = 1;
                try {
                    android.hardware.usb.V1_3.IUsb proxy = android.hardware.usb.V1_3.IUsb.castFrom((android.os.IHwInterface) this.mProxy);
                    success = proxy.enableUsbDataSignal(enable);
                } catch (android.os.RemoteException e2) {
                    com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed enableUsbData: opId:" + transactionId + " portId=" + portName, e2);
                    try {
                        callback.onOperationComplete(1);
                    } catch (android.os.RemoteException r) {
                        com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to call onOperationComplete. opID:" + transactionId + " portId:" + portName, r);
                    }
                    return false;
                }
            }
            if (success) {
                sUsbDataStatus = enable ? 0 : 16;
            }
            if (success) {
                i = 0;
            }
            try {
                callback.onOperationComplete(i);
            } catch (android.os.RemoteException r2) {
                com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to call onOperationComplete. opID:" + transactionId + " portId:" + portName, r2);
            }
            return false;
        } catch (android.os.RemoteException e3) {
            com.android.server.usb.UsbPortManager.logAndPrintException(this.mPw, "Failed to query USB HAL version. opID:" + transactionId + " portId:" + portName, e3);
            return false;
        }
    }

    private static class HALCallback extends android.hardware.usb.V1_2.IUsbCallback.Stub {
        public com.android.server.usb.UsbPortManager mPortManager;
        public com.android.internal.util.IndentingPrintWriter mPw;
        public com.android.server.usb.hal.port.UsbPortHidl mUsbPortHidl;

        HALCallback(com.android.internal.util.IndentingPrintWriter pw, com.android.server.usb.UsbPortManager portManager, com.android.server.usb.hal.port.UsbPortHidl usbPortHidl) {
            this.mPw = pw;
            this.mPortManager = portManager;
            this.mUsbPortHidl = usbPortHidl;
        }

        @Override // android.hardware.usb.V1_0.IUsbCallback
        public void notifyPortStatusChange(java.util.ArrayList<android.hardware.usb.V1_0.PortStatus> currentPortStatus, int retval) {
            if (!this.mUsbPortHidl.mSystemReady) {
                return;
            }
            if (retval != 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "port status enquiry failed");
                return;
            }
            java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> newPortInfo = new java.util.ArrayList<>();
            for (android.hardware.usb.V1_0.PortStatus current : currentPortStatus) {
                com.android.server.usb.hal.port.RawPortInfo temp = new com.android.server.usb.hal.port.RawPortInfo(current.portName, current.supportedModes, 0, current.currentMode, current.canChangeMode, current.currentPowerRole, current.canChangePowerRole, current.currentDataRole, current.canChangeDataRole, false, 0, false, 0, com.android.server.usb.hal.port.UsbPortHidl.sUsbDataStatus, false, 0, false, new int[0], 0, 0, null);
                newPortInfo.add(temp);
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "ClientCallback V1_0: " + current.portName);
            }
            this.mPortManager.updatePorts(newPortInfo);
        }

        @Override // android.hardware.usb.V1_1.IUsbCallback
        public void notifyPortStatusChange_1_1(java.util.ArrayList<android.hardware.usb.V1_1.PortStatus_1_1> currentPortStatus, int retval) {
            if (!this.mUsbPortHidl.mSystemReady) {
                return;
            }
            if (retval != 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "port status enquiry failed");
                return;
            }
            java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> newPortInfo = new java.util.ArrayList<>();
            int numStatus = currentPortStatus.size();
            for (int i = 0; i < numStatus; i++) {
                android.hardware.usb.V1_1.PortStatus_1_1 current = currentPortStatus.get(i);
                com.android.server.usb.hal.port.RawPortInfo temp = new com.android.server.usb.hal.port.RawPortInfo(current.status.portName, current.supportedModes, 0, current.currentMode, current.status.canChangeMode, current.status.currentPowerRole, current.status.canChangePowerRole, current.status.currentDataRole, current.status.canChangeDataRole, false, 0, false, 0, com.android.server.usb.hal.port.UsbPortHidl.sUsbDataStatus, false, 0, false, new int[0], 0, 0, null);
                newPortInfo.add(temp);
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "ClientCallback V1_1: " + current.status.portName);
            }
            this.mPortManager.updatePorts(newPortInfo);
        }

        @Override // android.hardware.usb.V1_2.IUsbCallback
        public void notifyPortStatusChange_1_2(java.util.ArrayList<android.hardware.usb.V1_2.PortStatus> currentPortStatus, int retval) {
            if (!this.mUsbPortHidl.mSystemReady) {
                return;
            }
            if (retval != 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, "port status enquiry failed");
                return;
            }
            java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> newPortInfo = new java.util.ArrayList<>();
            int i = 0;
            for (int numStatus = currentPortStatus.size(); i < numStatus; numStatus = numStatus) {
                android.hardware.usb.V1_2.PortStatus current = currentPortStatus.get(i);
                com.android.server.usb.hal.port.RawPortInfo temp = new com.android.server.usb.hal.port.RawPortInfo(current.status_1_1.status.portName, current.status_1_1.supportedModes, current.supportedContaminantProtectionModes, current.status_1_1.currentMode, current.status_1_1.status.canChangeMode, current.status_1_1.status.currentPowerRole, current.status_1_1.status.canChangePowerRole, current.status_1_1.status.currentDataRole, current.status_1_1.status.canChangeDataRole, current.supportsEnableContaminantPresenceProtection, current.contaminantProtectionStatus, current.supportsEnableContaminantPresenceDetection, current.contaminantDetectionStatus, com.android.server.usb.hal.port.UsbPortHidl.sUsbDataStatus, false, 0, false, new int[0], 0, 0, null);
                newPortInfo.add(temp);
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, "ClientCallback V1_2: " + current.status_1_1.status.portName);
                i++;
            }
            this.mPortManager.updatePorts(newPortInfo);
        }

        @Override // android.hardware.usb.V1_0.IUsbCallback
        public void notifyRoleSwitchStatus(java.lang.String portName, android.hardware.usb.V1_0.PortRole role, int retval) {
            if (retval == 0) {
                com.android.server.usb.UsbPortManager.logAndPrint(4, this.mPw, portName + " role switch successful");
            } else {
                com.android.server.usb.UsbPortManager.logAndPrint(6, this.mPw, portName + " role switch failed");
            }
        }
    }
}
