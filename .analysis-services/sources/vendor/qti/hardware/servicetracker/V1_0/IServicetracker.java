package vendor.qti.hardware.servicetracker.V1_0;

/* JADX INFO: loaded from: classes4.dex */
public interface IServicetracker extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "vendor.qti.hardware.servicetracker@1.0::IServicetracker";

    @java.lang.FunctionalInterface
    public interface getClientConnectionsCallback {
        void onValues(int i, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ClientConnection> arrayList);
    }

    @java.lang.FunctionalInterface
    public interface getPidCallback {
        void onValues(int i, int i2);
    }

    @java.lang.FunctionalInterface
    public interface getPidsCallback {
        void onValues(int i, java.util.ArrayList<java.lang.Integer> arrayList);
    }

    @java.lang.FunctionalInterface
    public interface getServiceBCountCallback {
        void onValues(int i, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceRecord> arrayList, int i2);
    }

    @java.lang.FunctionalInterface
    public interface getServiceConnectionsCallback {
        void onValues(int i, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceConnection> arrayList);
    }

    @java.lang.FunctionalInterface
    public interface getclientInfoCallback {
        void onValues(int i, vendor.qti.hardware.servicetracker.V1_0.ClientRecord clientRecord);
    }

    @java.lang.FunctionalInterface
    public interface getserviceInfoCallback {
        void onValues(int i, vendor.qti.hardware.servicetracker.V1_0.ServiceRecord serviceRecord);
    }

    @java.lang.FunctionalInterface
    public interface isServiceBCallback {
        void onValues(int i, boolean z);
    }

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    void bindService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData, vendor.qti.hardware.servicetracker.V1_0.ClientData clientData) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    void destroyService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData) throws android.os.RemoteException;

    void getClientConnections(java.lang.String str, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getClientConnectionsCallback getclientconnectionscallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    void getPid(java.lang.String str, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidCallback getpidcallback) throws android.os.RemoteException;

    void getPids(java.util.ArrayList<java.lang.String> arrayList, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidsCallback getpidscallback) throws android.os.RemoteException;

    void getServiceBCount(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceBCountCallback getservicebcountcallback) throws android.os.RemoteException;

    void getServiceConnections(java.lang.String str, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceConnectionsCallback getserviceconnectionscallback) throws android.os.RemoteException;

    void getclientInfo(java.lang.String str, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getclientInfoCallback getclientinfocallback) throws android.os.RemoteException;

    void getserviceInfo(java.lang.String str, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getserviceInfoCallback getserviceinfocallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    void isServiceB(java.lang.String str, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.isServiceBCallback isservicebcallback) throws android.os.RemoteException;

    void killProcess(int i) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    void startService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData) throws android.os.RemoteException;

    void unbindService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData, vendor.qti.hardware.servicetracker.V1_0.ClientData clientData) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static vendor.qti.hardware.servicetracker.V1_0.IServicetracker asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof vendor.qti.hardware.servicetracker.V1_0.IServicetracker)) {
            return (vendor.qti.hardware.servicetracker.V1_0.IServicetracker) iface;
        }
        vendor.qti.hardware.servicetracker.V1_0.IServicetracker proxy = new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Proxy(binder);
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

    static vendor.qti.hardware.servicetracker.V1_0.IServicetracker castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static vendor.qti.hardware.servicetracker.V1_0.IServicetracker getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static vendor.qti.hardware.servicetracker.V1_0.IServicetracker getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static vendor.qti.hardware.servicetracker.V1_0.IServicetracker getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static vendor.qti.hardware.servicetracker.V1_0.IServicetracker getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements vendor.qti.hardware.servicetracker.V1_0.IServicetracker {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of vendor.qti.hardware.servicetracker@1.0::IServicetracker]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void startService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            serviceData.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void bindService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData, vendor.qti.hardware.servicetracker.V1_0.ClientData clientData) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            serviceData.writeToParcel(_hidl_request);
            clientData.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void unbindService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData, vendor.qti.hardware.servicetracker.V1_0.ClientData clientData) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            serviceData.writeToParcel(_hidl_request);
            clientData.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void destroyService(vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            serviceData.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void killProcess(int pid) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeInt32(pid);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getclientInfo(java.lang.String clientName, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getclientInfoCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeString(clientName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                vendor.qti.hardware.servicetracker.V1_0.ClientRecord _hidl_out_client = new vendor.qti.hardware.servicetracker.V1_0.ClientRecord();
                _hidl_out_client.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_client);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getserviceInfo(java.lang.String serviceName, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getserviceInfoCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeString(serviceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                vendor.qti.hardware.servicetracker.V1_0.ServiceRecord _hidl_out_service = new vendor.qti.hardware.servicetracker.V1_0.ServiceRecord();
                _hidl_out_service.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_service);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getServiceConnections(java.lang.String serviceName, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceConnectionsCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeString(serviceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceConnection> _hidl_out_conn = vendor.qti.hardware.servicetracker.V1_0.ServiceConnection.readVectorFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_conn);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getClientConnections(java.lang.String clientName, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getClientConnectionsCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeString(clientName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ClientConnection> _hidl_out_conn = vendor.qti.hardware.servicetracker.V1_0.ClientConnection.readVectorFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_conn);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getPid(java.lang.String processName, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeString(processName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                int _hidl_out_pid = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_pid);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getPids(java.util.ArrayList<java.lang.String> serviceList, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidsCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeStringVector(serviceList);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                java.util.ArrayList<java.lang.Integer> _hidl_out_pidList = _hidl_reply.readInt32Vector();
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_pidList);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void isServiceB(java.lang.String serviceName, vendor.qti.hardware.servicetracker.V1_0.IServicetracker.isServiceBCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            _hidl_request.writeString(serviceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                boolean _hidl_out_serviceB = _hidl_reply.readBool();
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_serviceB);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker
        public void getServiceBCount(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceBCountCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_status = _hidl_reply.readInt32();
                java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceRecord> _hidl_out_bServiceList = vendor.qti.hardware.servicetracker.V1_0.ServiceRecord.readVectorFromParcel(_hidl_reply);
                int _hidl_out_count = _hidl_reply.readInt32();
                _hidl_cb.onValues(_hidl_out_status, _hidl_out_bServiceList, _hidl_out_count);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
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

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements vendor.qti.hardware.servicetracker.V1_0.IServicetracker {
        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName;
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{-69, -89, 53, -7, 89, -43, -57, -93, 97, -106, -34, 101, 72, -91, -9, 64, -112, 81, 43, -60, -23, -39, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_SUPERSPEED_HUB, -62, -110, 121, -78, -35, 120, 110, -69, -118}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData = new vendor.qti.hardware.servicetracker.V1_0.ServiceData();
                    serviceData.readFromParcel(_hidl_request);
                    startService(serviceData);
                    return;
                case 2:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData2 = new vendor.qti.hardware.servicetracker.V1_0.ServiceData();
                    serviceData2.readFromParcel(_hidl_request);
                    vendor.qti.hardware.servicetracker.V1_0.ClientData clientData = new vendor.qti.hardware.servicetracker.V1_0.ClientData();
                    clientData.readFromParcel(_hidl_request);
                    bindService(serviceData2, clientData);
                    return;
                case 3:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData3 = new vendor.qti.hardware.servicetracker.V1_0.ServiceData();
                    serviceData3.readFromParcel(_hidl_request);
                    vendor.qti.hardware.servicetracker.V1_0.ClientData clientData2 = new vendor.qti.hardware.servicetracker.V1_0.ClientData();
                    clientData2.readFromParcel(_hidl_request);
                    unbindService(serviceData3, clientData2);
                    return;
                case 4:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    vendor.qti.hardware.servicetracker.V1_0.ServiceData serviceData4 = new vendor.qti.hardware.servicetracker.V1_0.ServiceData();
                    serviceData4.readFromParcel(_hidl_request);
                    destroyService(serviceData4);
                    return;
                case 5:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    int pid = _hidl_request.readInt32();
                    killProcess(pid);
                    return;
                case 6:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.lang.String clientName = _hidl_request.readString();
                    getclientInfo(clientName, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getclientInfoCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.1
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getclientInfoCallback
                        public void onValues(int status, vendor.qti.hardware.servicetracker.V1_0.ClientRecord client) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            client.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 7:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.lang.String serviceName = _hidl_request.readString();
                    getserviceInfo(serviceName, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getserviceInfoCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.2
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getserviceInfoCallback
                        public void onValues(int status, vendor.qti.hardware.servicetracker.V1_0.ServiceRecord service) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            service.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 8:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.lang.String serviceName2 = _hidl_request.readString();
                    getServiceConnections(serviceName2, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceConnectionsCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.3
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceConnectionsCallback
                        public void onValues(int status, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceConnection> conn) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            vendor.qti.hardware.servicetracker.V1_0.ServiceConnection.writeVectorToParcel(_hidl_reply, conn);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 9:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.lang.String clientName2 = _hidl_request.readString();
                    getClientConnections(clientName2, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getClientConnectionsCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.4
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getClientConnectionsCallback
                        public void onValues(int status, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ClientConnection> conn) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            vendor.qti.hardware.servicetracker.V1_0.ClientConnection.writeVectorToParcel(_hidl_reply, conn);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 10:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.lang.String processName = _hidl_request.readString();
                    getPid(processName, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.5
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidCallback
                        public void onValues(int status, int pid2) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            _hidl_reply.writeInt32(pid2);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 11:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.util.ArrayList<java.lang.String> serviceList = _hidl_request.readStringVector();
                    getPids(serviceList, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidsCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.6
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getPidsCallback
                        public void onValues(int status, java.util.ArrayList<java.lang.Integer> pidList) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            _hidl_reply.writeInt32Vector(pidList);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 12:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    java.lang.String serviceName3 = _hidl_request.readString();
                    isServiceB(serviceName3, new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.isServiceBCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.7
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.isServiceBCallback
                        public void onValues(int status, boolean serviceB) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            _hidl_reply.writeBool(serviceB);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 13:
                    _hidl_request.enforceInterface(vendor.qti.hardware.servicetracker.V1_0.IServicetracker.kInterfaceName);
                    getServiceBCount(new vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceBCountCallback() { // from class: vendor.qti.hardware.servicetracker.V1_0.IServicetracker.Stub.8
                        @Override // vendor.qti.hardware.servicetracker.V1_0.IServicetracker.getServiceBCountCallback
                        public void onValues(int status, java.util.ArrayList<vendor.qti.hardware.servicetracker.V1_0.ServiceRecord> bServiceList, int count) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(status);
                            vendor.qti.hardware.servicetracker.V1_0.ServiceRecord.writeVectorToParcel(_hidl_reply, bServiceList);
                            _hidl_reply.writeInt32(count);
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
