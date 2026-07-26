package vendor.oplus.hardware.felica.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public interface IFelicaDevice extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "vendor.oplus.hardware.felica@1.0::IFelicaDevice";

    @java.lang.FunctionalInterface
    public interface getFelicaLockKeyCallback {
        void onValues(java.util.ArrayList<java.lang.Byte> arrayList, byte b);
    }

    @java.lang.FunctionalInterface
    public interface getFelicaLockStatusCallback {
        void onValues(boolean z, byte b);
    }

    @java.lang.FunctionalInterface
    public interface oplusEngineerNxpPnscrCurrentCallback {
        void onValues(java.util.ArrayList<java.lang.Byte> arrayList, byte b);
    }

    @java.lang.FunctionalInterface
    public interface oplusEngineerNxpPnscrFreqCallback {
        void onValues(java.util.ArrayList<java.lang.Byte> arrayList, byte b);
    }

    @java.lang.FunctionalInterface
    public interface oplusEngineer_NxpPnscrEse_3Callback {
        void onValues(java.util.ArrayList<java.lang.Byte> arrayList, byte b);
    }

    @java.lang.FunctionalInterface
    public interface oplusEngineer_NxpPnscrSpcCallback {
        void onValues(java.util.ArrayList<java.lang.Byte> arrayList, byte b);
    }

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    byte eraseFelicaLockData() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    void getFelicaLockKey(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockKeyCallback getfelicalockkeycallback) throws android.os.RemoteException;

    void getFelicaLockStatus(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockStatusCallback getfelicalockstatuscallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    void oplusEngineerNxpPnscrCurrent(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrCurrentCallback oplusengineernxppnscrcurrentcallback) throws android.os.RemoteException;

    void oplusEngineerNxpPnscrFreq(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrFreqCallback oplusengineernxppnscrfreqcallback) throws android.os.RemoteException;

    void oplusEngineer_NxpPnscrEse_3(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrEse_3Callback oplusengineer_nxppnscrese_3callback) throws android.os.RemoteException;

    void oplusEngineer_NxpPnscrSpc(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrSpcCallback oplusengineer_nxppnscrspccallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    byte setFelicaLockKey(java.util.ArrayList<java.lang.Byte> arrayList) throws android.os.RemoteException;

    byte setFelicaLockStatus(boolean z) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static vendor.oplus.hardware.felica.V1_0.IFelicaDevice asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof vendor.oplus.hardware.felica.V1_0.IFelicaDevice)) {
            return (vendor.oplus.hardware.felica.V1_0.IFelicaDevice) iface;
        }
        vendor.oplus.hardware.felica.V1_0.IFelicaDevice proxy = new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Proxy(binder);
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

    static vendor.oplus.hardware.felica.V1_0.IFelicaDevice castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static vendor.oplus.hardware.felica.V1_0.IFelicaDevice getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static vendor.oplus.hardware.felica.V1_0.IFelicaDevice getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static vendor.oplus.hardware.felica.V1_0.IFelicaDevice getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static vendor.oplus.hardware.felica.V1_0.IFelicaDevice getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements vendor.oplus.hardware.felica.V1_0.IFelicaDevice {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of vendor.oplus.hardware.felica@1.0::IFelicaDevice]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void getFelicaLockStatus(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockStatusCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                boolean _hidl_out_status = _hidl_reply.readBool();
                byte _hidl_out_result = _hidl_reply.readInt8();
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_result);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public byte setFelicaLockStatus(boolean status) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            _hidl_request.writeBool(status);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                byte _hidl_out_result = _hidl_reply.readInt8();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void getFelicaLockKey(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockKeyCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.Byte> _hidl_out_data = _hidl_reply.readInt8Vector();
                byte _hidl_out_status = _hidl_reply.readInt8();
                _hidl_cb.onValues(_hidl_out_data, _hidl_out_status);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public byte setFelicaLockKey(java.util.ArrayList<java.lang.Byte> data) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            _hidl_request.writeInt8Vector(data);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                byte _hidl_out_status = _hidl_reply.readInt8();
                return _hidl_out_status;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public byte eraseFelicaLockData() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                byte _hidl_out_status = _hidl_reply.readInt8();
                return _hidl_out_status;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineer_NxpPnscrSpc(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrSpcCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.Byte> _hidl_out_data = _hidl_reply.readInt8Vector();
                byte _hidl_out_status = _hidl_reply.readInt8();
                _hidl_cb.onValues(_hidl_out_data, _hidl_out_status);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineer_NxpPnscrEse_3(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrEse_3Callback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.Byte> _hidl_out_data = _hidl_reply.readInt8Vector();
                byte _hidl_out_status = _hidl_reply.readInt8();
                _hidl_cb.onValues(_hidl_out_data, _hidl_out_status);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineerNxpPnscrFreq(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrFreqCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.Byte> _hidl_out_data = _hidl_reply.readInt8Vector();
                byte _hidl_out_status = _hidl_reply.readInt8();
                _hidl_cb.onValues(_hidl_out_data, _hidl_out_status);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice
        public void oplusEngineerNxpPnscrCurrent(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrCurrentCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.Byte> _hidl_out_data = _hidl_reply.readInt8Vector();
                byte _hidl_out_status = _hidl_reply.readInt8();
                _hidl_cb.onValues(_hidl_out_data, _hidl_out_status);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
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

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements vendor.oplus.hardware.felica.V1_0.IFelicaDevice {
        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{104, 112, -115, -31, -53, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HID, 1, 82, 9, -63, 117, 82, -56, -93, 11, -20, 46, 113, -44, 38, -16, 43, -99, 59, 111, 59, 108, -28, 107, -47, 50, 107}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    getFelicaLockStatus(new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockStatusCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.1
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockStatusCallback
                        public void onValues(boolean status, byte result) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeBool(status);
                            _hidl_reply.writeInt8(result);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    boolean status = _hidl_request.readBool();
                    byte _hidl_out_result = setFelicaLockStatus(status);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt8(_hidl_out_result);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    getFelicaLockKey(new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockKeyCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.2
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.getFelicaLockKeyCallback
                        public void onValues(java.util.ArrayList<java.lang.Byte> data, byte status2) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt8Vector(data);
                            _hidl_reply.writeInt8(status2);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    java.util.ArrayList<java.lang.Byte> data = _hidl_request.readInt8Vector();
                    byte _hidl_out_status = setFelicaLockKey(data);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt8(_hidl_out_status);
                    _hidl_reply.send();
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    byte _hidl_out_status2 = eraseFelicaLockData();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt8(_hidl_out_status2);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    oplusEngineer_NxpPnscrSpc(new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrSpcCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.3
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrSpcCallback
                        public void onValues(java.util.ArrayList<java.lang.Byte> data2, byte status2) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt8Vector(data2);
                            _hidl_reply.writeInt8(status2);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    oplusEngineer_NxpPnscrEse_3(new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrEse_3Callback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.4
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineer_NxpPnscrEse_3Callback
                        public void onValues(java.util.ArrayList<java.lang.Byte> data2, byte status2) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt8Vector(data2);
                            _hidl_reply.writeInt8(status2);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    oplusEngineerNxpPnscrFreq(new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrFreqCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.5
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrFreqCallback
                        public void onValues(java.util.ArrayList<java.lang.Byte> data2, byte status2) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt8Vector(data2);
                            _hidl_reply.writeInt8(status2);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.oplus.hardware.felica.V1_0.IFelicaDevice.kInterfaceName);
                    oplusEngineerNxpPnscrCurrent(new vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrCurrentCallback() { // from class: vendor.oplus.hardware.felica.V1_0.IFelicaDevice.Stub.6
                        @Override // vendor.oplus.hardware.felica.V1_0.IFelicaDevice.oplusEngineerNxpPnscrCurrentCallback
                        public void onValues(java.util.ArrayList<java.lang.Byte> data2, byte status2) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt8Vector(data2);
                            _hidl_reply.writeInt8(status2);
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
