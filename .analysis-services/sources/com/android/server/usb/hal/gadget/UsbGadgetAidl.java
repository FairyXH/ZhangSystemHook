package com.android.server.usb.hal.gadget;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbGadgetAidl implements com.android.server.usb.hal.gadget.UsbGadgetHal {
    private static final java.lang.String TAG = com.android.server.usb.hal.gadget.UsbGadgetAidl.class.getSimpleName();
    private static final java.lang.String USB_GADGET_AIDL_SERVICE = android.hardware.usb.gadget.IUsbGadget.DESCRIPTOR + "/default";
    private final com.android.server.usb.UsbDeviceManager mDeviceManager;
    private android.hardware.usb.gadget.IUsbGadget mGadgetProxy;
    private final java.lang.Object mGadgetProxyLock = new java.lang.Object();
    public final com.android.internal.util.IndentingPrintWriter mPw;
    private com.android.server.usb.hal.gadget.UsbGadgetAidl.UsbGadgetCallback mUsbGadgetCallback;

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public int getGadgetHalVersion() throws android.os.RemoteException {
        synchronized (this.mGadgetProxyLock) {
            if (this.mGadgetProxy == null) {
                throw new android.os.RemoteException("IUsb not initialized yet");
            }
        }
        android.util.Slog.i(TAG, "USB Gadget HAL AIDL version: GADGET_HAL_V2_0");
        return 20;
    }

    public void serviceDied() {
        com.android.server.usb.UsbDeviceManager.logAndPrint(6, this.mPw, "Usb Gadget AIDL hal service died");
        synchronized (this.mGadgetProxyLock) {
            this.mGadgetProxy = null;
        }
        connectToProxy(null);
    }

    private void connectToProxy(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mGadgetProxyLock) {
            if (this.mGadgetProxy != null) {
                return;
            }
            try {
                this.mGadgetProxy = android.hardware.usb.gadget.IUsbGadget.Stub.asInterface(android.os.ServiceManager.waitForService(USB_GADGET_AIDL_SERVICE));
            } catch (java.util.NoSuchElementException e) {
                com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "connectToProxy: usb gadget hal service not found. Did the service fail to start?", e);
            }
        }
    }

    static boolean isServicePresent(com.android.internal.util.IndentingPrintWriter pw) {
        try {
            return android.os.ServiceManager.isDeclared(USB_GADGET_AIDL_SERVICE);
        } catch (java.util.NoSuchElementException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "connectToProxy: usb gadget Aidl hal service not found.", e);
            return false;
        }
    }

    public UsbGadgetAidl(com.android.server.usb.UsbDeviceManager deviceManager, com.android.internal.util.IndentingPrintWriter pw) {
        this.mDeviceManager = (com.android.server.usb.UsbDeviceManager) java.util.Objects.requireNonNull(deviceManager);
        this.mPw = pw;
        connectToProxy(this.mPw);
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void getCurrentUsbFunctions(long operationId) {
        synchronized (this.mGadgetProxyLock) {
            try {
                try {
                    this.mGadgetProxy.getCurrentUsbFunctions(new com.android.server.usb.hal.gadget.UsbGadgetAidl.UsbGadgetCallback(), operationId);
                } catch (android.os.RemoteException e) {
                    com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling getCurrentUsbFunctions, opID:" + operationId, e);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void getUsbSpeed(long operationId) {
        try {
            synchronized (this.mGadgetProxyLock) {
                this.mGadgetProxy.getUsbSpeed(new com.android.server.usb.hal.gadget.UsbGadgetAidl.UsbGadgetCallback(), operationId);
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling getUsbSpeed, opID:" + operationId, e);
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void reset(long operationId) {
        try {
            synchronized (this.mGadgetProxyLock) {
                this.mGadgetProxy.reset(new com.android.server.usb.hal.gadget.UsbGadgetAidl.UsbGadgetCallback(), operationId);
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling getUsbSpeed, opID:" + operationId, e);
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void setCurrentUsbFunctions(int mRequest, long mFunctions, boolean mChargingFunctions, int timeout, long operationId) {
        try {
            this.mUsbGadgetCallback = new com.android.server.usb.hal.gadget.UsbGadgetAidl.UsbGadgetCallback(null, mRequest, mFunctions, mChargingFunctions);
            synchronized (this.mGadgetProxyLock) {
                this.mGadgetProxy.setCurrentUsbFunctions(mFunctions, this.mUsbGadgetCallback, timeout, operationId);
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling setCurrentUsbFunctions: mRequest=" + mRequest + ", mFunctions=" + mFunctions + ", mChargingFunctions=" + mChargingFunctions + ", timeout=" + timeout + ", opID:" + operationId, e);
        }
    }

    private class UsbGadgetCallback extends android.hardware.usb.gadget.IUsbGadgetCallback.Stub {
        public boolean mChargingFunctions;
        public long mFunctions;
        public com.android.internal.util.IndentingPrintWriter mPw;
        public int mRequest;

        UsbGadgetCallback() {
        }

        UsbGadgetCallback(com.android.internal.util.IndentingPrintWriter pw, int request, long functions, boolean chargingFunctions) {
            this.mPw = pw;
            this.mRequest = request;
            this.mFunctions = functions;
            this.mChargingFunctions = chargingFunctions;
        }

        public void setCurrentUsbFunctionsCb(long functions, int status, long transactionId) {
            if (status == 0) {
                com.android.server.usb.UsbDeviceManager.logAndPrint(4, this.mPw, "Usb setCurrentUsbFunctionsCb ,functions:" + functions + " ,status:" + status + " ,transactionId:" + transactionId);
            } else {
                com.android.server.usb.UsbDeviceManager.logAndPrint(6, this.mPw, "Usb setCurrentUsbFunctionsCb failed ,functions:" + functions + " ,status:" + status + " ,transactionId:" + transactionId);
            }
            com.android.server.usb.hal.gadget.UsbGadgetAidl.this.mDeviceManager.setCurrentUsbFunctionsCb(functions, status, this.mRequest, this.mFunctions, this.mChargingFunctions);
        }

        public void getCurrentUsbFunctionsCb(long functions, int status, long transactionId) {
            if (status == 0) {
                com.android.server.usb.UsbDeviceManager.logAndPrint(4, this.mPw, "Usb getCurrentUsbFunctionsCb ,functions:" + functions + " ,status:" + status + " ,transactionId:" + transactionId);
            } else {
                com.android.server.usb.UsbDeviceManager.logAndPrint(6, this.mPw, "Usb getCurrentUsbFunctionsCb failed ,functions:" + functions + " ,status:" + status + " ,transactionId:" + transactionId);
            }
            com.android.server.usb.hal.gadget.UsbGadgetAidl.this.mDeviceManager.getCurrentUsbFunctionsCb(functions, status);
        }

        public void getUsbSpeedCb(int speed, long transactionId) {
            com.android.server.usb.UsbDeviceManager.logAndPrint(4, this.mPw, "getUsbSpeedCb speed:" + speed + " ,transactionId:" + transactionId);
            com.android.server.usb.hal.gadget.UsbGadgetAidl.this.mDeviceManager.getUsbSpeedCb(speed);
        }

        public void resetCb(int status, long transactionId) {
            if (status == 0) {
                com.android.server.usb.UsbDeviceManager.logAndPrint(4, this.mPw, "Usb resetCb status:" + status + " ,transactionId:" + transactionId);
            } else {
                com.android.server.usb.UsbDeviceManager.logAndPrint(6, this.mPw, "Usb resetCb status" + status + " ,transactionId:" + transactionId);
            }
            com.android.server.usb.hal.gadget.UsbGadgetAidl.this.mDeviceManager.resetCb(status);
        }

        public java.lang.String getInterfaceHash() {
            return "cb628c69682659911bca5c1d04042adba7f0de4b";
        }

        public int getInterfaceVersion() {
            return 1;
        }
    }
}
