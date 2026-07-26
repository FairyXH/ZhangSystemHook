package vendor.pixelworks.hardware.display.V1_1;

/* JADX INFO: loaded from: classes4.dex */
public interface IIrisCallback extends vendor.pixelworks.hardware.display.V1_0.IIrisCallback {
    public static final java.lang.String kInterfaceName = "vendor.pixelworks.hardware.display@1.1::IIrisCallback";

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    int onCalibratePatternChanged(long j, int i) throws android.os.RemoteException;

    void onDisplayPowerChanged(long j, int i) throws android.os.RemoteException;

    void onRefreshRequested(long j) throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static vendor.pixelworks.hardware.display.V1_1.IIrisCallback asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof vendor.pixelworks.hardware.display.V1_1.IIrisCallback)) {
            return (vendor.pixelworks.hardware.display.V1_1.IIrisCallback) iface;
        }
        vendor.pixelworks.hardware.display.V1_1.IIrisCallback proxy = new vendor.pixelworks.hardware.display.V1_1.IIrisCallback.Proxy(binder);
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

    static vendor.pixelworks.hardware.display.V1_1.IIrisCallback castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static vendor.pixelworks.hardware.display.V1_1.IIrisCallback getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static vendor.pixelworks.hardware.display.V1_1.IIrisCallback getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static vendor.pixelworks.hardware.display.V1_1.IIrisCallback getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static vendor.pixelworks.hardware.display.V1_1.IIrisCallback getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements vendor.pixelworks.hardware.display.V1_1.IIrisCallback {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of vendor.pixelworks.hardware.display@1.1::IIrisCallback]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.pixelworks.hardware.display.V1_0.IIrisCallback
        public void onFeatureChanged(int type, java.util.ArrayList<java.lang.Integer> values) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_0.IIrisCallback.kInterfaceName);
            _hidl_request.writeInt32(type);
            _hidl_request.writeInt32Vector(values);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback
        public void onRefreshRequested(long display) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName);
            _hidl_request.writeInt64(display);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback
        public int onCalibratePatternChanged(long display, int pattern) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(pattern);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback
        public void onDisplayPowerChanged(long display, int mode) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName);
            _hidl_request.writeInt64(display);
            _hidl_request.writeInt32(mode);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
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

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements vendor.pixelworks.hardware.display.V1_1.IIrisCallback {
        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName, vendor.pixelworks.hardware.display.V1_0.IIrisCallback.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName;
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{19, 117, -38, 65, -31, -102, 115, -95, 104, -36, -111, 29, -99, 88, -112, -74, -59, -73, -30, -109, -48, 94, -11, -4, -113, 24, -14, 10, 94, 123, 85, 80}, new byte[]{40, -119, 3, -47, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_SUPERSPEED_HUB, -27, 10, -12, -27, -37, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HUB, 108, 68, 88, 93, -47, -107, -54, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_REPORT, -39, -49, -62, 74, 104, 25, -63, 26, -108, 81, 26, 90, -71}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // vendor.pixelworks.hardware.display.V1_1.IIrisCallback, vendor.pixelworks.hardware.display.V1_0.IIrisCallback, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName.equals(descriptor)) {
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

        public void onTransact(int _hidl_code, android.os.HwParcel _hidl_request, android.os.HwParcel _hidl_reply, int _hidl_flags) throws android.os.RemoteException {
            switch (_hidl_code) {
                case 1:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_0.IIrisCallback.kInterfaceName);
                    int type = _hidl_request.readInt32();
                    java.util.ArrayList<java.lang.Integer> values = _hidl_request.readInt32Vector();
                    onFeatureChanged(type, values);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName);
                    long display = _hidl_request.readInt64();
                    onRefreshRequested(display);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName);
                    long display2 = _hidl_request.readInt64();
                    int pattern = _hidl_request.readInt32();
                    int _hidl_out_result = onCalibratePatternChanged(display2, pattern);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result);
                    _hidl_reply.send();
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.pixelworks.hardware.display.V1_1.IIrisCallback.kInterfaceName);
                    long display3 = _hidl_request.readInt64();
                    int mode = _hidl_request.readInt32();
                    onDisplayPowerChanged(display3, mode);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
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
