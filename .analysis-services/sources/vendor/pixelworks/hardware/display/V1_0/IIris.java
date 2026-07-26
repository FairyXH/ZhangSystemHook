package vendor.pixelworks.hardware.display.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public interface IIris extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "vendor.pixelworks.hardware.display@1.0::IIris";

    @java.lang.FunctionalInterface
    public interface irisConfigureBatchCallback {
        void onValues(int i, java.lang.String str);
    }

    @java.lang.FunctionalInterface
    public interface irisConfigureGetCallback {
        void onValues(int i, java.util.ArrayList<java.lang.Integer> arrayList);
    }

    @java.lang.FunctionalInterface
    public interface panelReadWriteCallback {
        void onValues(int i, java.util.ArrayList<java.lang.Byte> arrayList);
    }

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    void irisConfigureBatch(int i, java.lang.String str, vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback irisconfigurebatchcallback) throws android.os.RemoteException;

    void irisConfigureGet(int i, java.util.ArrayList<java.lang.Integer> arrayList, vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback irisconfiguregetcallback) throws android.os.RemoteException;

    int irisConfigureSet(int i, java.util.ArrayList<java.lang.Integer> arrayList) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    void panelReadWrite(boolean z, byte b, byte b2, boolean z2, java.util.ArrayList<java.lang.Byte> arrayList, byte b3, vendor.pixelworks.hardware.display.V1_0.IIris.panelReadWriteCallback panelreadwritecallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    void registerCallback(vendor.pixelworks.hardware.display.V1_0.IIrisCallback iIrisCallback) throws android.os.RemoteException;

    void registerCallback2(long j, vendor.pixelworks.hardware.display.V1_0.IIrisCallback iIrisCallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static vendor.pixelworks.hardware.display.V1_0.IIris asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof vendor.pixelworks.hardware.display.V1_0.IIris)) {
            return (vendor.pixelworks.hardware.display.V1_0.IIris) iface;
        }
        vendor.pixelworks.hardware.display.V1_0.IIris proxy = new vendor.pixelworks.hardware.display.V1_0.IIris.Proxy(binder);
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

    static vendor.pixelworks.hardware.display.V1_0.IIris castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static vendor.pixelworks.hardware.display.V1_0.IIris getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static vendor.pixelworks.hardware.display.V1_0.IIris getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static vendor.pixelworks.hardware.display.V1_0.IIris getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static vendor.pixelworks.hardware.display.V1_0.IIris getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements vendor.pixelworks.hardware.display.V1_0.IIris {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of vendor.pixelworks.hardware.display@1.0::IIris]@Proxy";
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements vendor.pixelworks.hardware.display.V1_0.IIris {
        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName;
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{126, 28, 77, -7, 102, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -100, 73, 26, 9, -56, 86, -88, 29, -123, -88, 111, -68, 13, 43, 1, 11, -19, 105, -111, 85, 122, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CAPABILITY, -99, -55, 104, -123}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIris, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (vendor.pixelworks.hardware.display.V1_0.IIris.kInterfaceName.equals(descriptor)) {
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
                    irisConfigureGet(type2, values2, new vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureGetCallback() { // from class: vendor.pixelworks.hardware.display.V1_0.IIris.Stub.1
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
                    panelReadWrite(highSpeed, dtype, vc, last, tx, rxLen, new vendor.pixelworks.hardware.display.V1_0.IIris.panelReadWriteCallback() { // from class: vendor.pixelworks.hardware.display.V1_0.IIris.Stub.2
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
                    irisConfigureBatch(type3, json, new vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback() { // from class: vendor.pixelworks.hardware.display.V1_0.IIris.Stub.3
                        @Override // vendor.pixelworks.hardware.display.V1_0.IIris.irisConfigureBatchCallback
                        public void onValues(int result, java.lang.String outJson) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeString(outJson);
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
    }
}
