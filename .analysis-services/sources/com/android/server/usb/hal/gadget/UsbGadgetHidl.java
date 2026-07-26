package com.android.server.usb.hal.gadget;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbGadgetHidl implements com.android.server.usb.hal.gadget.UsbGadgetHal {
    private static final int USB_GADGET_HAL_DEATH_COOKIE = 2000;
    private com.android.server.usb.UsbDeviceManager mDeviceManager;
    private android.hardware.usb.gadget.V1_0.IUsbGadget mGadgetProxy;
    private final java.lang.Object mGadgetProxyLock = new java.lang.Object();
    private final com.android.internal.util.IndentingPrintWriter mPw;
    private com.android.server.usb.hal.gadget.UsbGadgetHidl.UsbGadgetCallback mUsbGadgetCallback;

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public int getGadgetHalVersion() throws android.os.RemoteException {
        int version;
        synchronized (this.mGadgetProxyLock) {
            if (this.mGadgetProxy == null) {
                throw new android.os.RemoteException("IUsbGadget not initialized yet");
            }
            if (android.hardware.usb.gadget.V1_2.IUsbGadget.castFrom(this.mGadgetProxy) != null) {
                version = 12;
            } else if (android.hardware.usb.gadget.V1_1.IUsbGadget.castFrom(this.mGadgetProxy) != null) {
                version = 11;
            } else {
                version = 10;
            }
            com.android.server.usb.UsbDeviceManager.logAndPrint(4, this.mPw, "USB Gadget HAL HIDL version: " + version);
        }
        return version;
    }

    final class DeathRecipient implements android.os.IHwBinder.DeathRecipient {
        private final com.android.internal.util.IndentingPrintWriter mPw;

        DeathRecipient(com.android.internal.util.IndentingPrintWriter pw) {
            this.mPw = pw;
        }

        public void serviceDied(long cookie) {
            if (cookie == 2000) {
                com.android.server.usb.UsbDeviceManager.logAndPrint(6, this.mPw, "Usb Gadget hal service died cookie: " + cookie);
                synchronized (com.android.server.usb.hal.gadget.UsbGadgetHidl.this.mGadgetProxyLock) {
                    com.android.server.usb.UsbDeviceManager.logAndPrint(6, this.mPw, "MSG_MTK_HAL_STATS: HAL service died");
                    ((com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0])).usbGadgetServiceStatusRecord("Usb Gadget hal service died", "mtk hal service died");
                    com.android.server.usb.hal.gadget.UsbGadgetHidl.this.mGadgetProxy = null;
                }
            }
        }
    }

    final class ServiceNotification extends android.hidl.manager.V1_0.IServiceNotification.Stub {
        ServiceNotification() {
        }

        @Override // android.hidl.manager.V1_0.IServiceNotification
        public void onRegistration(java.lang.String fqName, java.lang.String name, boolean preexisting) {
            com.android.server.usb.UsbDeviceManager.logAndPrint(4, com.android.server.usb.hal.gadget.UsbGadgetHidl.this.mPw, "Usb gadget hal service started " + fqName + " " + name);
            com.android.server.usb.hal.gadget.UsbGadgetHidl.this.connectToProxy(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectToProxy(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mGadgetProxyLock) {
            if (this.mGadgetProxy != null) {
                return;
            }
            try {
                this.mGadgetProxy = android.hardware.usb.gadget.V1_0.IUsbGadget.getService();
                this.mGadgetProxy.linkToDeath(new com.android.server.usb.hal.gadget.UsbGadgetHidl.DeathRecipient(pw), 2000L);
            } catch (android.os.RemoteException e) {
                com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "connectToProxy: usb gadget hal service not responding", e);
            } catch (java.util.NoSuchElementException e2) {
                com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "connectToProxy: usb gadget hal service not found. Did the service fail to start?", e2);
            }
        }
    }

    static boolean isServicePresent(com.android.internal.util.IndentingPrintWriter pw) {
        try {
            android.hardware.usb.gadget.V1_0.IUsbGadget.getService(true);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "IUSBGadget hal service present but failed to get service", e);
        } catch (java.util.NoSuchElementException e2) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "connectToProxy: usb gadget hidl hal service not found.", e2);
            return false;
        }
        return true;
    }

    public UsbGadgetHidl(com.android.server.usb.UsbDeviceManager deviceManager, com.android.internal.util.IndentingPrintWriter pw) {
        this.mDeviceManager = (com.android.server.usb.UsbDeviceManager) java.util.Objects.requireNonNull(deviceManager);
        this.mPw = pw;
        try {
            com.android.server.usb.hal.gadget.UsbGadgetHidl.ServiceNotification serviceNotification = new com.android.server.usb.hal.gadget.UsbGadgetHidl.ServiceNotification();
            boolean ret = android.hidl.manager.V1_0.IServiceManager.getService().registerForNotifications("android.hardware.usb.gadget@1.0::IUsbGadget", "", serviceNotification);
            if (!ret) {
                com.android.server.usb.UsbDeviceManager.logAndPrint(6, pw, "Failed to register service start notification");
            }
            connectToProxy(this.mPw);
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(pw, "Failed to register service start notification", e);
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void getCurrentUsbFunctions(long transactionId) {
        try {
            synchronized (this.mGadgetProxyLock) {
                this.mGadgetProxy.getCurrentUsbFunctions(new com.android.server.usb.hal.gadget.UsbGadgetHidl.UsbGadgetCallback());
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling getCurrentUsbFunctions", e);
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void getUsbSpeed(long transactionId) {
        try {
            synchronized (this.mGadgetProxyLock) {
                if (android.hardware.usb.gadget.V1_2.IUsbGadget.castFrom(this.mGadgetProxy) != null) {
                    android.hardware.usb.gadget.V1_2.IUsbGadget gadgetProxy = android.hardware.usb.gadget.V1_2.IUsbGadget.castFrom(this.mGadgetProxy);
                    gadgetProxy.getUsbSpeed(new com.android.server.usb.hal.gadget.UsbGadgetHidl.UsbGadgetCallback());
                }
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "get UsbSpeed failed", e);
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void reset(long transactionId) {
        try {
            synchronized (this.mGadgetProxyLock) {
                if (android.hardware.usb.gadget.V1_1.IUsbGadget.castFrom(this.mGadgetProxy) != null) {
                    android.hardware.usb.gadget.V1_1.IUsbGadget gadgetProxy = android.hardware.usb.gadget.V1_1.IUsbGadget.castFrom(this.mGadgetProxy);
                    gadgetProxy.reset();
                }
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling reset", e);
        }
    }

    @Override // com.android.server.usb.hal.gadget.UsbGadgetHal
    public void setCurrentUsbFunctions(int mRequest, long mFunctions, boolean mChargingFunctions, int timeout, long operationId) {
        try {
            this.mUsbGadgetCallback = new com.android.server.usb.hal.gadget.UsbGadgetHidl.UsbGadgetCallback(null, mRequest, mFunctions, mChargingFunctions);
            synchronized (this.mGadgetProxyLock) {
                this.mGadgetProxy.setCurrentUsbFunctions(mFunctions, this.mUsbGadgetCallback, timeout);
            }
        } catch (android.os.RemoteException e) {
            com.android.server.usb.UsbDeviceManager.logAndPrintException(this.mPw, "RemoteException while calling setCurrentUsbFunctions mRequest = " + mRequest + ", mFunctions = " + mFunctions + ", timeout = " + timeout + ", mChargingFunctions = " + mChargingFunctions + ", operationId =" + operationId, e);
        }
    }

    private class UsbGadgetCallback extends android.hardware.usb.gadget.V1_2.IUsbGadgetCallback.Stub {
        public boolean mChargingFunctions;
        public long mFunctions;
        public int mRequest;

        UsbGadgetCallback() {
        }

        UsbGadgetCallback(com.android.internal.util.IndentingPrintWriter pw, int request, long functions, boolean chargingFunctions) {
            this.mRequest = request;
            this.mFunctions = functions;
            this.mChargingFunctions = chargingFunctions;
        }

        public void setCurrentUsbFunctionsCb(long functions, int status) {
            com.android.server.usb.hal.gadget.UsbGadgetHidl.this.mDeviceManager.setCurrentUsbFunctionsCb(functions, status, this.mRequest, this.mFunctions, this.mChargingFunctions);
        }

        public void getCurrentUsbFunctionsCb(long functions, int status) {
            com.android.server.usb.hal.gadget.UsbGadgetHidl.this.mDeviceManager.getCurrentUsbFunctionsCb(functions, status);
        }

        public void getUsbSpeedCb(int speed) {
            com.android.server.usb.hal.gadget.UsbGadgetHidl.this.mDeviceManager.getUsbSpeedCb(speed);
        }
    }
}
