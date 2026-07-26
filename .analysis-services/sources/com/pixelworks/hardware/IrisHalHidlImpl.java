package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisHalHidlImpl {
    private static final boolean DEBUG = false;
    private static final java.lang.String INSTANCE_NAME = "default";
    private static final java.lang.String TAG = "IrisHal";
    private long mCookie;
    private android.os.IHwBinder.DeathRecipient mDeathRecipient;
    private vendor.pixelworks.hardware.display.V1_1.IIris mIris;
    private com.pixelworks.hardware.IrisHalHidlImpl.IrisCallback mIrisCallback;
    private vendor.pixelworks.hardware.display.V1_2.IIris mIrisV1_2;
    private java.util.ArrayList<android.os.Handler> mServiceReportHandlers = new java.util.ArrayList<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mIsDeclared = isDeclared();

    class IrisCallback extends vendor.pixelworks.hardware.display.V1_0.IIrisCallback.Stub {
        IrisCallback() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback
        public void onFeatureChanged(int type, java.util.ArrayList<java.lang.Integer> values) throws android.os.RemoteException {
            synchronized (com.pixelworks.hardware.IrisHalHidlImpl.this.mServiceReportHandlers) {
                for (int i = 0; i < com.pixelworks.hardware.IrisHalHidlImpl.this.mServiceReportHandlers.size(); i++) {
                    android.os.Handler report = (android.os.Handler) com.pixelworks.hardware.IrisHalHidlImpl.this.mServiceReportHandlers.get(i);
                    if (report != null) {
                        android.os.Message message = android.os.Message.obtain();
                        message.what = type;
                        if (values.size() > 0) {
                            message.arg1 = values.get(0).intValue();
                            android.os.Bundle data = new android.os.Bundle();
                            data.putIntegerArrayList("values", values);
                            message.setData(data);
                        } else {
                            message.arg1 = 0;
                        }
                        report.sendMessage(message);
                    }
                }
            }
        }
    }

    public IrisHalHidlImpl() {
        if (this.mIsDeclared) {
            synchronized (this.mLock) {
                initialize();
            }
            return;
        }
        android.util.Log.d(TAG, "IIris HIDL is not declared");
    }

    public boolean ready() {
        return this.mIris != null;
    }

    public void close() {
        android.util.Log.d(TAG, "Close IIris HIDL");
        synchronized (this.mLock) {
            try {
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Access IIris failed", e);
            }
            if (this.mIris != null && this.mDeathRecipient != null) {
                this.mIris.unlinkToDeath(this.mDeathRecipient);
                this.mIris = null;
                this.mIrisCallback = null;
                this.mDeathRecipient = null;
            } else {
                this.mIris = null;
                this.mIrisCallback = null;
                this.mDeathRecipient = null;
            }
        }
    }

    public static boolean isDeclared() {
        try {
            android.hidl.manager.V1_0.IServiceManager serviceManager = android.hidl.manager.V1_0.IServiceManager.getService();
            return serviceManager.getTransport(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName, "default") != 0;
        } catch (android.os.RemoteException e) {
            return false;
        }
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
                this.mIrisCallback = new com.pixelworks.hardware.IrisHalHidlImpl.IrisCallback();
                this.mCookie = this.mIrisCallback.hashCode();
                this.mCookie = (this.mCookie << 32) + ((long) (android.os.Process.myPid() << 16)) + ((long) android.os.Process.myTid());
                try {
                    this.mIris.registerCallback2(this.mCookie, this.mIrisCallback);
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
            java.util.ArrayList<java.lang.Integer> v = new java.util.ArrayList<>(values.length);
            for (int i : values) {
                v.add(java.lang.Integer.valueOf(i));
            }
            return this.mIris.irisConfigureSet(type, v);
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
            java.util.ArrayList<java.lang.Integer> v = new java.util.ArrayList<>(values.length);
            for (int i : values) {
                v.add(java.lang.Integer.valueOf(i));
            }
            final com.pixelworks.hardware.IrisHalGetResult ret = new com.pixelworks.hardware.IrisHalGetResult();
            this.mIris.irisConfigureGet(type, v, new vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback() { // from class: com.pixelworks.hardware.IrisHalHidlImpl.1
                @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback
                public void onValues(int result, java.util.ArrayList<java.lang.Integer> values2) {
                    ret.ret = result;
                    ret.values = new int[values2.size()];
                    for (int i2 = 0; i2 < values2.size(); i2++) {
                        ret.values[i2] = values2.get(i2).intValue();
                    }
                }
            });
            return ret;
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
                final com.pixelworks.hardware.IrisHalGetResult ret = new com.pixelworks.hardware.IrisHalGetResult();
                this.mIris.irisConfigureBatch(type, inJson, new vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback() { // from class: com.pixelworks.hardware.IrisHalHidlImpl.2
                    @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback
                    public void onValues(int result, java.lang.String retJson) {
                        ret.ret = result;
                        ret.json = retJson;
                        if (retJson != null) {
                            android.util.Log.d(com.pixelworks.hardware.IrisHalHidlImpl.TAG, "retJson size: [" + retJson.length() + "]");
                        }
                    }
                });
                outJson = ret.json;
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
            if (this.mIrisV1_2 == null) {
                initialize();
            }
            if (this.mIrisV1_2 == null) {
                android.util.Log.e(TAG, "Can't get IIris");
                return -2;
            }
            return this.mIrisV1_2.irisConfigureBuffer(type, 0L, handle, size);
        }
    }

    private void initialize() {
        boolean needRegister = true;
        try {
            this.mIris = vendor.pixelworks.hardware.display.V1_1.IIris.getService(true);
            if (this.mIris != null) {
                android.util.Log.d(TAG, "Use IIris HIDL");
                synchronized (this.mServiceReportHandlers) {
                    if (this.mServiceReportHandlers.isEmpty()) {
                        needRegister = false;
                    }
                }
                if (needRegister) {
                    this.mIrisCallback = new com.pixelworks.hardware.IrisHalHidlImpl.IrisCallback();
                    this.mCookie = this.mIrisCallback.hashCode();
                    this.mCookie = (this.mCookie << 32) + ((long) (android.os.Process.myPid() << 16)) + ((long) android.os.Process.myTid());
                    this.mIris.registerCallback2(this.mCookie, this.mIrisCallback);
                }
                this.mDeathRecipient = new android.os.IHwBinder.DeathRecipient() { // from class: com.pixelworks.hardware.IrisHalHidlImpl.3
                    public void serviceDied(long cookie) {
                        android.util.Log.w(com.pixelworks.hardware.IrisHalHidlImpl.TAG, "Noticed IIris HIDL death");
                        synchronized (com.pixelworks.hardware.IrisHalHidlImpl.this.mLock) {
                            try {
                                com.pixelworks.hardware.IrisHalHidlImpl.this.mIris.unlinkToDeath(this);
                            } catch (android.os.RemoteException e) {
                                android.util.Log.e(com.pixelworks.hardware.IrisHalHidlImpl.TAG, "Access IIris failed", e);
                            }
                            com.pixelworks.hardware.IrisHalHidlImpl.this.mIris = null;
                        }
                    }
                };
                this.mIris.linkToDeath(this.mDeathRecipient, this.mCookie);
                this.mIrisV1_2 = vendor.pixelworks.hardware.display.V1_2.IIris.castFrom((android.os.IHwInterface) this.mIris);
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Call IIris API failed", e);
        } catch (java.util.NoSuchElementException e2) {
            this.mIsDeclared = false;
            android.util.Log.e(TAG, "Access IIris failed", e2);
        }
    }
}
