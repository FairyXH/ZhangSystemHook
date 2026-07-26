package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisHalAidlImpl {
    private static final boolean DEBUG = false;
    private static final java.lang.String INSTANCE_NAME = vendor.pixelworks.hardware.display.IIris.DESCRIPTOR + "/default";
    private static final java.lang.String TAG = "IrisHal";
    private long mCookie;
    private android.os.IBinder.DeathRecipient mDeathRecipient;
    private vendor.pixelworks.hardware.display.IIris mIris;
    private com.pixelworks.hardware.IrisHalAidlImpl.IrisCallback mIrisCallback;
    private boolean mUseDeathRecipient;
    private java.util.ArrayList<android.os.Handler> mServiceReportHandlers = new java.util.ArrayList<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mIsDeclared = isDeclared();

    class IrisCallback extends vendor.pixelworks.hardware.display.IIrisCallback.Stub {
        IrisCallback() {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onFeatureChanged(int type, int[] values) throws android.os.RemoteException {
            java.util.ArrayList<java.lang.Integer> list = new java.util.ArrayList<>();
            for (int i : values) {
                list.add(java.lang.Integer.valueOf(i));
            }
            synchronized (com.pixelworks.hardware.IrisHalAidlImpl.this.mServiceReportHandlers) {
                for (int i2 = 0; i2 < com.pixelworks.hardware.IrisHalAidlImpl.this.mServiceReportHandlers.size(); i2++) {
                    android.os.Handler report = (android.os.Handler) com.pixelworks.hardware.IrisHalAidlImpl.this.mServiceReportHandlers.get(i2);
                    if (report != null) {
                        android.os.Message message = android.os.Message.obtain();
                        message.what = type;
                        if (values.length > 0) {
                            message.arg1 = values[0];
                            android.os.Bundle data = new android.os.Bundle();
                            data.putIntegerArrayList("values", list);
                            message.setData(data);
                        } else {
                            message.arg1 = 0;
                        }
                        report.sendMessage(message);
                    }
                }
            }
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onRefreshRequested(long display) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int onCalibratePatternChanged(long display, int pattern) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public void onDisplayPowerChanged(long display, int mode) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public vendor.pixelworks.hardware.display.ContentSamples onContentSamplingRequested(long display, int action, long maxFrames) throws android.os.RemoteException {
            vendor.pixelworks.hardware.display.ContentSamples samples = new vendor.pixelworks.hardware.display.ContentSamples();
            samples.result = -1;
            return samples;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public int getInterfaceVersion() {
            return 1;
        }

        @Override // vendor.pixelworks.hardware.display.IIrisCallback
        public java.lang.String getInterfaceHash() {
            return "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
        }
    }

    public IrisHalAidlImpl(boolean useDeathRecipient) {
        this.mUseDeathRecipient = useDeathRecipient;
        if (this.mIsDeclared) {
            synchronized (this.mLock) {
                initialize();
            }
            return;
        }
        android.util.Log.d(TAG, "IIris AIDL is not declared");
    }

    public boolean ready() {
        return this.mIris != null;
    }

    public void close() {
        android.util.Log.d(TAG, "Close IIris AIDL");
        synchronized (this.mLock) {
            if (this.mIris != null && this.mDeathRecipient != null) {
                this.mIris.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            }
            this.mIris = null;
            this.mIrisCallback = null;
            this.mDeathRecipient = null;
        }
    }

    public static boolean isDeclared() {
        return android.os.ServiceManager.isDeclared(INSTANCE_NAME);
    }

    public void setServiceReportHandler(android.os.Handler handler) {
        synchronized (this.mServiceReportHandlers) {
            for (int i = 0; i < this.mServiceReportHandlers.size(); i++) {
                this.mServiceReportHandlers.set(i, null);
            }
        }
        addServiceReportHandler(handler);
    }

    public void addServiceReportHandler(android.os.Handler handler) {
        synchronized (this.mServiceReportHandlers) {
            if (!this.mServiceReportHandlers.contains(handler)) {
                this.mServiceReportHandlers.add(handler);
            }
        }
        synchronized (this.mLock) {
            if (this.mIris != null && this.mIrisCallback == null) {
                this.mIrisCallback = new com.pixelworks.hardware.IrisHalAidlImpl.IrisCallback();
                this.mCookie = this.mIrisCallback.hashCode();
                this.mCookie = (this.mCookie << 32) + ((long) (android.os.Process.myPid() << 16)) + ((long) android.os.Process.myTid());
                try {
                    this.mIris.registerCallback(this.mCookie, this.mIrisCallback);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(TAG, "Failed to register IrisCallback", e);
                }
            }
        }
    }

    public void removeServiceReportHandler(android.os.Handler handler) {
        synchronized (this.mServiceReportHandlers) {
            int i = this.mServiceReportHandlers.indexOf(handler);
            if (i >= 0) {
                this.mServiceReportHandlers.set(i, null);
            }
        }
    }

    public int irisConfigureSet(int type, int[] values) throws android.os.RemoteException {
        if (type < 0 || values == null) {
            android.util.Log.e(TAG, "Input parameters are wrong.");
            return -1;
        }
        if (!this.mIsDeclared) {
            android.util.Log.e(TAG, "IIris service is not declared.");
            return -2;
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            if (this.mIris == null) {
                android.util.Log.e(TAG, "Can't get IIris");
                return -2;
            }
            try {
                return this.mIris.irisConfigureSet(type, values);
            } catch (android.os.ServiceSpecificException e) {
                return e.errorCode;
            } catch (java.lang.RuntimeException e2) {
                return -1;
            }
        }
    }

    public com.pixelworks.hardware.IrisHalGetResult irisConfigureGet(int type, int[] values) throws android.os.RemoteException {
        if (type < 0 || values == null) {
            android.util.Log.e(TAG, "Input parameters are wrong.");
            return new com.pixelworks.hardware.IrisHalGetResult();
        }
        if (!this.mIsDeclared) {
            android.util.Log.e(TAG, "IIris service is not declared.");
            return new com.pixelworks.hardware.IrisHalGetResult(-2);
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            if (this.mIris == null) {
                android.util.Log.e(TAG, "Can't get IIris");
                return new com.pixelworks.hardware.IrisHalGetResult(-2);
            }
            try {
                try {
                    int[] outValues = this.mIris.irisConfigureGet(type, values);
                    return new com.pixelworks.hardware.IrisHalGetResult(0, outValues);
                } catch (java.lang.RuntimeException e) {
                    return new com.pixelworks.hardware.IrisHalGetResult();
                }
            } catch (android.os.ServiceSpecificException e2) {
                return new com.pixelworks.hardware.IrisHalGetResult(e2.errorCode);
            }
        }
    }

    public java.lang.String irisConfigureBatch(int type, java.lang.String inJson) throws android.os.RemoteException {
        java.lang.String outJson = "";
        if (type < 0 || inJson == null) {
            android.util.Log.e(TAG, "Input parameters are wrong.");
            return null;
        }
        if (!this.mIsDeclared) {
            android.util.Log.e(TAG, "IIris service is not declared.");
            return null;
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            if (this.mIris != null) {
                try {
                    outJson = this.mIris.irisConfigureBatch(type, inJson);
                } catch (android.os.ServiceSpecificException e) {
                    android.util.Log.e(TAG, "ServiceSpecificException: " + e);
                } catch (java.lang.RuntimeException e2) {
                    android.util.Log.e(TAG, "RuntimeException: " + e2);
                }
            } else {
                android.util.Log.e(TAG, "Query IIris interface failed");
            }
        }
        return outJson;
    }

    public int irisConfigureBuffer(int type, android.os.NativeHandle handle, int size) throws android.os.RemoteException {
        if (type < 0 || handle == null || size <= 0) {
            android.util.Log.e(TAG, "Input parameters are wrong.");
            return -1;
        }
        if (!this.mIsDeclared) {
            android.util.Log.e(TAG, "IIris service is not declared.");
            return -2;
        }
        synchronized (this.mLock) {
            if (this.mIris == null) {
                initialize();
            }
            if (this.mIris == null) {
                android.util.Log.e(TAG, "Can't get IIris");
                return -2;
            }
            try {
                try {
                    try {
                        android.os.ParcelFileDescriptor fd = android.os.ParcelFileDescriptor.dup(handle.getFileDescriptor());
                        return this.mIris.irisConfigureBuffer(type, 0L, fd, size);
                    } catch (java.io.IOException e) {
                        android.util.Log.e(TAG, "Dup file descriptor failed.");
                        return -1;
                    }
                } catch (android.os.ServiceSpecificException e2) {
                    return e2.errorCode;
                }
            } catch (java.lang.RuntimeException e3) {
                return -1;
            }
        }
    }

    private void initialize() {
        boolean needRegister;
        try {
            this.mIris = vendor.pixelworks.hardware.display.IIris.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(INSTANCE_NAME));
            if (this.mIris != null) {
                android.util.Log.d(TAG, "Use IIris AIDL");
                synchronized (this.mServiceReportHandlers) {
                    needRegister = !this.mServiceReportHandlers.isEmpty();
                }
                if (needRegister) {
                    this.mIrisCallback = new com.pixelworks.hardware.IrisHalAidlImpl.IrisCallback();
                    this.mCookie = this.mIrisCallback.hashCode();
                    this.mCookie = (this.mCookie << 32) + ((long) (android.os.Process.myPid() << 16)) + ((long) android.os.Process.myTid());
                    this.mIris.registerCallback(this.mCookie, this.mIrisCallback);
                }
                if (this.mUseDeathRecipient) {
                    this.mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.pixelworks.hardware.IrisHalAidlImpl.1
                        @Override // android.os.IBinder.DeathRecipient
                        public void binderDied() {
                            android.util.Log.w(com.pixelworks.hardware.IrisHalAidlImpl.TAG, "Noticed IIris AIDL death");
                            synchronized (com.pixelworks.hardware.IrisHalAidlImpl.this.mLock) {
                                if (com.pixelworks.hardware.IrisHalAidlImpl.this.mIris != null) {
                                    com.pixelworks.hardware.IrisHalAidlImpl.this.mIris.asBinder().unlinkToDeath(this, 0);
                                }
                                com.pixelworks.hardware.IrisHalAidlImpl.this.mIris = null;
                            }
                        }
                    };
                    this.mIris.asBinder().linkToDeath(this.mDeathRecipient, 0);
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Call IIris API failed", e);
        } catch (java.util.NoSuchElementException e2) {
            this.mIsDeclared = false;
            android.util.Log.e(TAG, "Access IIris failed", e2);
        }
    }
}
