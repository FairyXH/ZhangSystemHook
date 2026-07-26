package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisHal {
    private static final java.lang.String TAG = "IrisHal";
    private com.pixelworks.hardware.IrisHalAidlImpl mAidlImpl;
    private com.pixelworks.hardware.IrisHalHidlImpl mHidlImpl;
    private boolean mUseAidl = false;
    private boolean mHasIris = false;
    private final java.lang.String PROP_IRIS_SUPPORT = "sys.pxlw.iris.support";

    public IrisHal() {
        createHalImpl(true);
    }

    public IrisHal(boolean useDeathRecipient) {
        createHalImpl(useDeathRecipient);
    }

    protected void finalize() throws java.lang.Throwable {
        close();
        super.finalize();
    }

    public void close() {
        if (!this.mHasIris) {
            return;
        }
        if (this.mUseAidl) {
            this.mAidlImpl.close();
        } else {
            this.mHidlImpl.close();
        }
    }

    public void setServiceReportHandler(android.os.Handler handler) {
        if (!this.mHasIris) {
            return;
        }
        if (this.mUseAidl) {
            this.mAidlImpl.setServiceReportHandler(handler);
        } else {
            this.mHidlImpl.setServiceReportHandler(handler);
        }
    }

    public void addServiceReportHandler(android.os.Handler handler) {
        if (!this.mHasIris) {
            return;
        }
        if (this.mUseAidl) {
            this.mAidlImpl.addServiceReportHandler(handler);
        } else {
            this.mHidlImpl.addServiceReportHandler(handler);
        }
    }

    public void removeServiceReportHandler(android.os.Handler handler) {
        if (!this.mHasIris) {
            return;
        }
        if (this.mUseAidl) {
            this.mAidlImpl.removeServiceReportHandler(handler);
        } else {
            this.mHidlImpl.removeServiceReportHandler(handler);
        }
    }

    public int irisConfigureSet(int type, int[] values) {
        if (!this.mHasIris) {
            return 0;
        }
        try {
            if (this.mUseAidl) {
                return this.mAidlImpl.irisConfigureSet(type, values);
            }
            return this.mHidlImpl.irisConfigureSet(type, values);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Access IIris failed", e);
            return -1;
        }
    }

    public com.pixelworks.hardware.IrisHalGetResult irisConfigureGet(int type, int[] values) throws android.os.RemoteException {
        com.pixelworks.hardware.IrisHalGetResult ret = new com.pixelworks.hardware.IrisHalGetResult();
        if (!this.mHasIris) {
            return ret;
        }
        try {
            if (this.mUseAidl) {
                ret = this.mAidlImpl.irisConfigureGet(type, values);
            } else {
                ret = this.mHidlImpl.irisConfigureGet(type, values);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Access IIris failed", e);
        }
        return ret;
    }

    public java.lang.String irisConfigureBatch(int type, java.lang.String jsonIn) throws android.os.RemoteException {
        java.lang.String ret = new java.lang.String("");
        if (!this.mHasIris) {
            return ret;
        }
        try {
            if (this.mUseAidl) {
                ret = this.mAidlImpl.irisConfigureBatch(type, jsonIn);
            } else {
                ret = this.mHidlImpl.irisConfigureBatch(type, jsonIn);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Access IIris failed", e);
        }
        return ret;
    }

    public int irisConfigureBuffer(int type, android.os.NativeHandle handle, int size) throws android.os.RemoteException {
        int ret = -1;
        if (!this.mHasIris) {
            return -1;
        }
        try {
            if (this.mUseAidl) {
                ret = this.mAidlImpl.irisConfigureBuffer(type, handle, size);
            } else {
                ret = this.mHidlImpl.irisConfigureBuffer(type, handle, size);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Access IIris failed", e);
        }
        return ret;
    }

    private void createHalImpl(boolean useDeathRecipient) {
        try {
            if (com.pixelworks.hardware.IrisHalAidlImpl.isDeclared()) {
                this.mAidlImpl = new com.pixelworks.hardware.IrisHalAidlImpl(useDeathRecipient);
                if (this.mAidlImpl.ready()) {
                    this.mUseAidl = true;
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (java.lang.NullPointerException e) {
        } catch (java.lang.SecurityException e2) {
            android.util.Log.w(TAG, "SELinux denied IIris AIDL");
        }
        try {
            if (com.pixelworks.hardware.IrisHalHidlImpl.isDeclared()) {
                this.mHidlImpl = new com.pixelworks.hardware.IrisHalHidlImpl();
                if (this.mHidlImpl.ready()) {
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (java.lang.NullPointerException e3) {
        } catch (java.lang.SecurityException e4) {
            android.util.Log.w(TAG, "SELinux denied IIris HIDL");
        }
        android.util.Log.i(TAG, "No found IIris service");
    }
}
