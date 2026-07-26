package vendor.pixelworks.hardware.display.V1_2;

/* JADX INFO: loaded from: classes4.dex */
public interface IIris extends vendor.pixelworks.hardware.display.V1_1.IIris {
    public static final java.lang.String kInterfaceName = "vendor.pixelworks.hardware.display@1.2::IIris";

    @java.lang.FunctionalInterface
    public interface getCurrentConfig_1_2Callback {
        void onValues(int i, vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2 irisFixedConfig_1_2);
    }

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    void getCurrentConfig_1_2(long j, vendor.pixelworks.hardware.display.V1_2.IIris.getCurrentConfig_1_2Callback getcurrentconfig_1_2callback) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    int irisConfigureBuffer(int i, long j, android.os.NativeHandle nativeHandle, int i2) throws android.os.RemoteException;

    int irisConfigureMemory(int i, long j, android.os.HidlMemory hidlMemory) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    void setActiveConfig_1_2(long j, vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2 displayConfigVariableInfo_1_2) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    void setLayerBuffer_1_2(long j, long j2, android.os.NativeHandle nativeHandle, int i, vendor.pixelworks.hardware.display.V1_2.BufferInfo_1_2 bufferInfo_1_2) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static vendor.pixelworks.hardware.display.V1_2.IIris asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof vendor.pixelworks.hardware.display.V1_2.IIris)) {
            return (vendor.pixelworks.hardware.display.V1_2.IIris) iface;
        }
        vendor.pixelworks.hardware.display.V1_2.IIris proxy = new vendor.pixelworks.hardware.display.V1_2.IIris.Proxy(binder);
        try {
            for (java.lang.String descriptor : proxy.interfaceChain()) {
                if (descriptor.equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (android.os.RemoteException e) {
        }
        return null;
    }

    static vendor.pixelworks.hardware.display.V1_2.IIris castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static vendor.pixelworks.hardware.display.V1_2.IIris getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static vendor.pixelworks.hardware.display.V1_2.IIris getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static vendor.pixelworks.hardware.display.V1_2.IIris getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static vendor.pixelworks.hardware.display.V1_2.IIris getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements vendor.pixelworks.hardware.display.V1_2.IIris {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of vendor.pixelworks.hardware.display@1.2::IIris]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public int irisConfigureSet(int type, java.util.ArrayList<java.lang.Integer> values) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(values);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void irisConfigureGet(int type, java.util.ArrayList<java.lang.Integer> values, vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(values);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.util.ArrayList<java.lang.Integer> _hidl_out_outValues = _hidl_reply.readInt32Vector();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_outValues);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void registerCallback(vendor.pixelworks.hardware.display.V1_0.IIrisCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void registerCallback2(long cookie, vendor.pixelworks.hardware.display.V1_0.IIrisCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            _hidl_request.writeInt64(cookie);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void panelReadWrite(boolean highSpeed, byte dtype, byte vc, boolean last, java.util.ArrayList<java.lang.Byte> tx, byte rxLen, vendor.pixelworks.hardware.display.V1_0.IIris.panelReadWriteCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            _hidl_request.writeBool(highSpeed);
            _hidl_request.writeInt8(dtype);
            _hidl_request.writeInt8(vc);
            _hidl_request.writeBool(last);
            _hidl_request.writeInt8Vector(tx);
            _hidl_request.writeInt8(rxLen);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.util.ArrayList<java.lang.Byte> _hidl_out_rx = _hidl_reply.readInt8Vector();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_rx);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris
        public void irisConfigureBatch(int type, java.lang.String json, vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeString(json);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.lang.String _hidl_out_outJson = _hidl_reply.readString();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_outJson);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void initialize(vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo info) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            info.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void registerCallback_1_1(long cookie, vendor.pixelworks.hardware.display.V1_1.IIrisCallback callback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(cookie);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void registerSoftIrisClient(vendor.pixelworks.hardware.display.V1_1.ISoftIrisClient client) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeStrongBinder(client == null ? null : client.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void createLayer(long display, long id) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void destroyLayer(long display, long id) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void buildLayerStack(long display, vendor.pixelworks.hardware.display.V1_1.LayerStack layerStack) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            layerStack.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setActiveConfig(long display, vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo info) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            info.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void getLayerToneMappingLut(long display, int type, vendor.pixelworks.hardware.display.V1_1.IIris.getLayerToneMappingLutCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(type);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(14, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                vendor.pixelworks.hardware.display.V1_1.LutData _hidl_out_lutData = new vendor.pixelworks.hardware.display.V1_1.LutData();
                _hidl_out_lutData.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_lutData);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int commitLayerStack(long display, int compType) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(compType);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(15, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void updateDisplayBrightness(long display, int syncMethod, java.util.ArrayList<java.lang.Integer> values, vendor.pixelworks.hardware.display.V1_1.IIris.updateDisplayBrightnessCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(syncMethod);
            _hidl_request.writeInt32Vector(values);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(16, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.util.ArrayList<java.lang.Integer> _hidl_out_pendingValues = _hidl_reply.readInt32Vector();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_pendingValues);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerProperty(long display, int prop, long id) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(prop);
            _hidl_request.writeInt64(id);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(17, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int setPowerMode(long display, int mode, boolean isAfter, boolean fromEvent) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(mode);
            _hidl_request.writeBool(isAfter);
            _hidl_request.writeBool(fromEvent);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(18, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int handleDisplayEvent(long display, int event, int mode) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(event);
            _hidl_request.writeInt32(mode);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(19, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public java.lang.String getDumpString(long display) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(20, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.lang.String _hidl_out_result = _hidl_reply.readString();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setClientTarget(long display, int acquireFence) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(acquireFence);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(21, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerBuffer(long display, long id, android.os.NativeHandle buffer, int acquireFence, vendor.pixelworks.hardware.display.V1_1.BufferInfo bufferInfo) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            _hidl_request.writeNativeHandle(buffer);
            _hidl_request.writeInt32(acquireFence);
            bufferInfo.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(22, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerCompositionType(long display, long id, int type) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            _hidl_request.writeInt32(type);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(23, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerDisplayFrame(long display, long id, vendor.pixelworks.hardware.display.V1_1.HwcRect frame) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            frame.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(24, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerSourceCrop(long display, long id, vendor.pixelworks.hardware.display.V1_1.HwcRect crop) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            crop.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(25, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerTransform(long display, long id, int transform) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            _hidl_request.writeInt32(transform);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(26, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerZOrder(long display, long id, int z) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            _hidl_request.writeInt32(z);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(27, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void changeLayerType(long id, long newDisplay) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(id);
            _hidl_request.writeInt64(newDisplay);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(28, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void isHDR10Plus(boolean isHDR10Plus) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeBool(isHDR10Plus);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(29, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int setColorModeWithRenderIntent(long display, int mode, int intent) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(mode);
            _hidl_request.writeInt32(intent);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(30, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setLayerSetEmpty(long display, boolean empty) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeBool(empty);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(31, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void setDisplayConnected(long display, boolean connnected) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeBool(connnected);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(32, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int setOsdAutoRefresh(int value) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt32(value);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(33, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int configureIrisMaxcll(int hdr_maxcll) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt32(hdr_maxcll);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(34, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void getCurrentConfig(vendor.pixelworks.hardware.display.V1_1.IIris.getCurrentConfigCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(35, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig _hidl_out_info = new vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig();
                _hidl_out_info.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_info);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void reportDualChannelStatus(int status) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt32(status);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(36, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int getOsdStatus(int type) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt32(type);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(37, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int presentDisplay(long display) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(38, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public int present(long display) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(39, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIris
        public void enableSecondaryDisplay(boolean enable) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
            _hidl_request.writeBool(enable);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(40, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public int irisConfigureBuffer(int type, long display, android.os.NativeHandle buffer, int size) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt64(display);
            _hidl_request.writeNativeHandle(buffer);
            _hidl_request.writeInt32(size);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(41, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public int irisConfigureMemory(int type, long display, android.os.HidlMemory buffer) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt64(display);
            _hidl_request.writeHidlMemory(buffer);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(42, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public void setActiveConfig_1_2(long display, vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2 info) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            info.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(43, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public void setLayerBuffer_1_2(long display, long id, android.os.NativeHandle buffer, int acquireFence, vendor.pixelworks.hardware.display.V1_2.BufferInfo_1_2 bufferInfo) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt64(id);
            _hidl_request.writeNativeHandle(buffer);
            _hidl_request.writeInt32(acquireFence);
            bufferInfo.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(44, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris
        public void getCurrentConfig_1_2(long display, vendor.pixelworks.hardware.display.V1_2.IIris.getCurrentConfig_1_2Callback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
            _hidl_request.writeInt64(display);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(45, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2 _hidl_out_info = new vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2();
                _hidl_out_info.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_info);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.String> _hidl_out_descriptors = _hidl_reply.readStringVector();
                return _hidl_out_descriptors;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public java.lang.String interfaceDescriptor() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.lang.String _hidl_out_descriptor = _hidl_reply.readString();
                return _hidl_out_descriptor;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<byte[]> _hidl_out_hashchain = new java.util.ArrayList<>();
                android.os.HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                android.os.HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    long _hidl_array_offset_1 = _hidl_index_0 * 32;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                return _hidl_out_hashchain;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public void ping() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                android.hidl.base.V1_0.DebugInfo _hidl_out_info = new android.hidl.base.V1_0.DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hidl.base.V1_0.IBase.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements vendor.pixelworks.hardware.display.V1_2.IIris {
        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName, vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName, vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{-8, 59, 69, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HID, -122, -112, -99, -32, -118, -67, 122, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_REPORT, 76, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, 114, -21, -118, 85, -43, -67, 73, 5, -71, 45, 74, 119, 58, -46, -46, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -1, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL}, new byte[]{com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_III, 68, 46, -104, 90, -121, -2, -62, com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_II, 39, -110, 45, 69, -5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -22, -39, -26, -98, 68, 74, -113, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, 72, -86, -8, 70, 2, 92, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, -86, 7}, new byte[]{126, 28, 77, -7, 102, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -100, 73, 26, 9, -56, 86, -88, 29, -123, -88, 111, -68, 13, 43, 1, 11, -19, 105, -111, 85, 122, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CAPABILITY, -99, -55, 104, -123}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // vendor.pixelworks.hardware.display.V1_2.IIris, vendor.pixelworks.hardware.display.V1_1.IIris, vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(java.lang.String serviceName) throws android.os.RemoteException {
            registerService(serviceName);
        }

        public java.lang.String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        public void onTransact(int _hidl_code, android.os.HwParcel _hidl_request, final android.os.HwParcel _hidl_reply, int _hidl_flags) throws android.os.RemoteException {
            switch (_hidl_code) {
                case 1:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    int type = _hidl_request.readInt32();
                    java.util.ArrayList<java.lang.Integer> values = _hidl_request.readInt32Vector();
                    int _hidl_out_result = irisConfigureSet(type, values);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    int type2 = _hidl_request.readInt32();
                    java.util.ArrayList<java.lang.Integer> values2 = _hidl_request.readInt32Vector();
                    irisConfigureGet(type2, values2, new vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.1
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback
                        public void onValues(int result, java.util.ArrayList<java.lang.Integer> outValues) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32Vector(outValues);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    vendor.pixelworks.hardware.display.V1_0.IIrisCallback callback = vendor.pixelworks.hardware.display.V1_0.IIrisCallback.asInterface(_hidl_request.readStrongBinder());
                    registerCallback(callback);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    long cookie = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_0.IIrisCallback callback2 = vendor.pixelworks.hardware.display.V1_0.IIrisCallback.asInterface(_hidl_request.readStrongBinder());
                    registerCallback2(cookie, callback2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    boolean highSpeed = _hidl_request.readBool();
                    byte dtype = _hidl_request.readInt8();
                    byte vc = _hidl_request.readInt8();
                    boolean last = _hidl_request.readBool();
                    java.util.ArrayList<java.lang.Byte> tx = _hidl_request.readInt8Vector();
                    byte rxLen = _hidl_request.readInt8();
                    panelReadWrite(highSpeed, dtype, vc, last, tx, rxLen, new vendor.pixelworks.hardware.display.V1_0.IIris.panelReadWriteCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.2
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.panelReadWriteCallback
                        public void onValues(int result, java.util.ArrayList<java.lang.Byte> rx) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt8Vector(rx);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName);
                    int type3 = _hidl_request.readInt32();
                    java.lang.String json = _hidl_request.readString();
                    irisConfigureBatch(type3, json, new vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.3
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback
                        public void onValues(int result, java.lang.String outJson) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeString(outJson);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo info = new vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo();
                    info.readFromParcel(_hidl_request);
                    initialize(info);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long cookie2 = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_1.IIrisCallback callback3 = vendor.pixelworks.hardware.display.V1_1.IIrisCallback.asInterface(_hidl_request.readStrongBinder());
                    registerCallback_1_1(cookie2, callback3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    vendor.pixelworks.hardware.display.V1_1.ISoftIrisClient client = vendor.pixelworks.hardware.display.V1_1.ISoftIrisClient.asInterface(_hidl_request.readStrongBinder());
                    registerSoftIrisClient(client);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 10:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display = _hidl_request.readInt64();
                    long id = _hidl_request.readInt64();
                    createLayer(display, id);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 11:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display2 = _hidl_request.readInt64();
                    long id2 = _hidl_request.readInt64();
                    destroyLayer(display2, id2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 12:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display3 = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_1.LayerStack layerStack = new vendor.pixelworks.hardware.display.V1_1.LayerStack();
                    layerStack.readFromParcel(_hidl_request);
                    buildLayerStack(display3, layerStack);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 13:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display4 = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo info2 = new vendor.pixelworks.hardware.display.V1_1.DisplayConfigVariableInfo();
                    info2.readFromParcel(_hidl_request);
                    setActiveConfig(display4, info2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 14:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display5 = _hidl_request.readInt64();
                    int type4 = _hidl_request.readInt32();
                    getLayerToneMappingLut(display5, type4, new vendor.pixelworks.hardware.display.V1_1.IIris.getLayerToneMappingLutCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.4
                        @Override // vendor.pixelworks.hardware.display.V1_1.IIris.getLayerToneMappingLutCallback
                        public void onValues(int result, vendor.pixelworks.hardware.display.V1_1.LutData lutData) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            lutData.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 15:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display6 = _hidl_request.readInt64();
                    int compType = _hidl_request.readInt32();
                    int _hidl_out_result2 = commitLayerStack(display6, compType);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result2);
                    _hidl_reply.send();
                    return;
                case 16:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display7 = _hidl_request.readInt64();
                    int syncMethod = _hidl_request.readInt32();
                    java.util.ArrayList<java.lang.Integer> values3 = _hidl_request.readInt32Vector();
                    updateDisplayBrightness(display7, syncMethod, values3, new vendor.pixelworks.hardware.display.V1_1.IIris.updateDisplayBrightnessCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.5
                        @Override // vendor.pixelworks.hardware.display.V1_1.IIris.updateDisplayBrightnessCallback
                        public void onValues(int result, java.util.ArrayList<java.lang.Integer> pendingValues) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeInt32Vector(pendingValues);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 17:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display8 = _hidl_request.readInt64();
                    int prop = _hidl_request.readInt32();
                    long id3 = _hidl_request.readInt64();
                    setLayerProperty(display8, prop, id3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 18:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display9 = _hidl_request.readInt64();
                    int mode = _hidl_request.readInt32();
                    boolean isAfter = _hidl_request.readBool();
                    boolean fromEvent = _hidl_request.readBool();
                    int _hidl_out_result3 = setPowerMode(display9, mode, isAfter, fromEvent);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result3);
                    _hidl_reply.send();
                    return;
                case 19:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display10 = _hidl_request.readInt64();
                    int event = _hidl_request.readInt32();
                    int mode2 = _hidl_request.readInt32();
                    int _hidl_out_result4 = handleDisplayEvent(display10, event, mode2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result4);
                    _hidl_reply.send();
                    return;
                case 20:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display11 = _hidl_request.readInt64();
                    java.lang.String _hidl_out_result5 = getDumpString(display11);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeString(_hidl_out_result5);
                    _hidl_reply.send();
                    return;
                case 21:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display12 = _hidl_request.readInt64();
                    int acquireFence = _hidl_request.readInt32();
                    setClientTarget(display12, acquireFence);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 22:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display13 = _hidl_request.readInt64();
                    long id4 = _hidl_request.readInt64();
                    android.os.NativeHandle buffer = _hidl_request.readNativeHandle();
                    int acquireFence2 = _hidl_request.readInt32();
                    vendor.pixelworks.hardware.display.V1_1.BufferInfo bufferInfo = new vendor.pixelworks.hardware.display.V1_1.BufferInfo();
                    bufferInfo.readFromParcel(_hidl_request);
                    setLayerBuffer(display13, id4, buffer, acquireFence2, bufferInfo);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 23:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display14 = _hidl_request.readInt64();
                    long id5 = _hidl_request.readInt64();
                    int type5 = _hidl_request.readInt32();
                    setLayerCompositionType(display14, id5, type5);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 24:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display15 = _hidl_request.readInt64();
                    long id6 = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_1.HwcRect frame = new vendor.pixelworks.hardware.display.V1_1.HwcRect();
                    frame.readFromParcel(_hidl_request);
                    setLayerDisplayFrame(display15, id6, frame);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 25:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display16 = _hidl_request.readInt64();
                    long id7 = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_1.HwcRect crop = new vendor.pixelworks.hardware.display.V1_1.HwcRect();
                    crop.readFromParcel(_hidl_request);
                    setLayerSourceCrop(display16, id7, crop);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 26:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display17 = _hidl_request.readInt64();
                    long id8 = _hidl_request.readInt64();
                    int transform = _hidl_request.readInt32();
                    setLayerTransform(display17, id8, transform);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 27:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display18 = _hidl_request.readInt64();
                    long id9 = _hidl_request.readInt64();
                    int z = _hidl_request.readInt32();
                    setLayerZOrder(display18, id9, z);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 28:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long id10 = _hidl_request.readInt64();
                    long newDisplay = _hidl_request.readInt64();
                    changeLayerType(id10, newDisplay);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 29:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    boolean isHDR10Plus = _hidl_request.readBool();
                    isHDR10Plus(isHDR10Plus);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 30:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display19 = _hidl_request.readInt64();
                    int mode3 = _hidl_request.readInt32();
                    int intent = _hidl_request.readInt32();
                    int _hidl_out_result6 = setColorModeWithRenderIntent(display19, mode3, intent);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result6);
                    _hidl_reply.send();
                    return;
                case 31:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display20 = _hidl_request.readInt64();
                    boolean empty = _hidl_request.readBool();
                    setLayerSetEmpty(display20, empty);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 32:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display21 = _hidl_request.readInt64();
                    boolean connnected = _hidl_request.readBool();
                    setDisplayConnected(display21, connnected);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 33:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int value = _hidl_request.readInt32();
                    int _hidl_out_result7 = setOsdAutoRefresh(value);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result7);
                    _hidl_reply.send();
                    return;
                case 34:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int hdr_maxcll = _hidl_request.readInt32();
                    int _hidl_out_result8 = configureIrisMaxcll(hdr_maxcll);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result8);
                    _hidl_reply.send();
                    return;
                case 35:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    getCurrentConfig(new vendor.pixelworks.hardware.display.V1_1.IIris.getCurrentConfigCallback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.6
                        @Override // vendor.pixelworks.hardware.display.V1_1.IIris.getCurrentConfigCallback
                        public void onValues(int result, vendor.pixelworks.hardware.display.V1_1.IrisFixedConfig info3) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            info3.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 36:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int status = _hidl_request.readInt32();
                    reportDualChannelStatus(status);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 37:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    int type6 = _hidl_request.readInt32();
                    int _hidl_out_result9 = getOsdStatus(type6);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result9);
                    _hidl_reply.send();
                    return;
                case 38:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display22 = _hidl_request.readInt64();
                    int _hidl_out_result10 = presentDisplay(display22);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result10);
                    _hidl_reply.send();
                    return;
                case 39:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    long display23 = _hidl_request.readInt64();
                    int _hidl_out_result11 = present(display23);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result11);
                    _hidl_reply.send();
                    return;
                case 40:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIris.kInterfaceName);
                    boolean enable = _hidl_request.readBool();
                    enableSecondaryDisplay(enable);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 41:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
                    int type7 = _hidl_request.readInt32();
                    long display24 = _hidl_request.readInt64();
                    android.os.NativeHandle buffer2 = _hidl_request.readNativeHandle();
                    int size = _hidl_request.readInt32();
                    int _hidl_out_result12 = irisConfigureBuffer(type7, display24, buffer2, size);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result12);
                    _hidl_reply.send();
                    return;
                case 42:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
                    int type8 = _hidl_request.readInt32();
                    long display25 = _hidl_request.readInt64();
                    android.os.HidlMemory buffer3 = (android.os.HidlMemory) new java.util.function.Function() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris$Stub$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return vendor.pixelworks.hardware.display.V1_2.IIris.Stub.lambda$onTransact$0((android.os.HwParcel) obj);
                        }
                    }.apply(_hidl_request);
                    int _hidl_out_result13 = irisConfigureMemory(type8, display25, buffer3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result13);
                    _hidl_reply.send();
                    return;
                case 43:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
                    long display26 = _hidl_request.readInt64();
                    vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2 info3 = new vendor.pixelworks.hardware.display.V1_2.DisplayConfigVariableInfo_1_2();
                    info3.readFromParcel(_hidl_request);
                    setActiveConfig_1_2(display26, info3);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 44:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
                    long display27 = _hidl_request.readInt64();
                    long id11 = _hidl_request.readInt64();
                    android.os.NativeHandle buffer4 = _hidl_request.readNativeHandle();
                    int acquireFence3 = _hidl_request.readInt32();
                    vendor.pixelworks.hardware.display.V1_2.BufferInfo_1_2 bufferInfo2 = new vendor.pixelworks.hardware.display.V1_2.BufferInfo_1_2();
                    bufferInfo2.readFromParcel(_hidl_request);
                    setLayerBuffer_1_2(display27, id11, buffer4, acquireFence3, bufferInfo2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 45:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_2.IIris.kInterfaceName);
                    long display28 = _hidl_request.readInt64();
                    getCurrentConfig_1_2(display28, new vendor.pixelworks.hardware.display.V1_2.IIris.getCurrentConfig_1_2Callback() { // from class: vendor.pixelworks.hardware.display.V1_2.IIris.Stub.7
                        @Override // vendor.pixelworks.hardware.display.V1_2.IIris.getCurrentConfig_1_2Callback
                        public void onValues(int result, vendor.pixelworks.hardware.display.V1_2.IrisFixedConfig_1_2 info4) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            info4.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 256067662:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    java.util.ArrayList<java.lang.String> _hidl_out_descriptors = interfaceChain();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeStringVector(_hidl_out_descriptors);
                    _hidl_reply.send();
                    return;
                case 256131655:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    android.os.NativeHandle fd = _hidl_request.readNativeHandle();
                    java.util.ArrayList<java.lang.String> options = _hidl_request.readStringVector();
                    debug(fd, options);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 256136003:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    java.lang.String _hidl_out_descriptor = interfaceDescriptor();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeString(_hidl_out_descriptor);
                    _hidl_reply.send();
                    return;
                case 256398152:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    java.util.ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                    _hidl_reply.writeStatus(0);
                    android.os.HwBlob _hidl_blob = new android.os.HwBlob(16);
                    int _hidl_vec_size = _hidl_out_hashchain.size();
                    _hidl_blob.putInt32(8L, _hidl_vec_size);
                    _hidl_blob.putBool(12L, false);
                    android.os.HwBlob childBlob = new android.os.HwBlob(_hidl_vec_size * 32);
                    for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                        long _hidl_array_offset_1 = _hidl_index_0 * 32;
                        byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                        if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                            throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
                        }
                        childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                    }
                    _hidl_blob.putBlob(0L, childBlob);
                    _hidl_reply.writeBuffer(_hidl_blob);
                    _hidl_reply.send();
                    return;
                case 256462420:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    setHALInstrumentation();
                    return;
                case 256660548:
                default:
                    return;
                case 256921159:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    ping();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 257049926:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    android.hidl.base.V1_0.DebugInfo _hidl_out_info = getDebugInfo();
                    _hidl_reply.writeStatus(0);
                    _hidl_out_info.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 257120595:
                    _hidl_request.enforceInterface(android.hidl.base.V1_0.IBase.kInterfaceName);
                    notifySyspropsChanged();
                    return;
            }
        }

        static /* synthetic */ android.os.HidlMemory lambda$onTransact$0(android.os.HwParcel _parcel) {
            try {
                return _parcel.readHidlMemory().dup();
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }
}
