package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public interface ITunerSession extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "android.hardware.broadcastradio@2.0::ITunerSession";

    @java.lang.FunctionalInterface
    public interface isConfigFlagSetCallback {
        void onValues(int i, boolean z);
    }

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    void cancel() throws android.os.RemoteException;

    void close() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> getParameters(java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    void isConfigFlagSet(int i, android.hardware.broadcastradio.V2_0.ITunerSession.isConfigFlagSetCallback isconfigflagsetcallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    int scan(boolean z, boolean z2) throws android.os.RemoteException;

    int setConfigFlag(int i, boolean z) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> setParameters(java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> arrayList) throws android.os.RemoteException;

    int startProgramListUpdates(android.hardware.broadcastradio.V2_0.ProgramFilter programFilter) throws android.os.RemoteException;

    int step(boolean z) throws android.os.RemoteException;

    void stopProgramListUpdates() throws android.os.RemoteException;

    int tune(android.hardware.broadcastradio.V2_0.ProgramSelector programSelector) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.broadcastradio.V2_0.ITunerSession asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.broadcastradio.V2_0.ITunerSession)) {
            return (android.hardware.broadcastradio.V2_0.ITunerSession) iface;
        }
        android.hardware.broadcastradio.V2_0.ITunerSession proxy = new android.hardware.broadcastradio.V2_0.ITunerSession.Proxy(binder);
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

    static android.hardware.broadcastradio.V2_0.ITunerSession castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.broadcastradio.V2_0.ITunerSession getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.broadcastradio.V2_0.ITunerSession getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.broadcastradio.V2_0.ITunerSession getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.broadcastradio.V2_0.ITunerSession getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements android.hardware.broadcastradio.V2_0.ITunerSession {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.broadcastradio@2.0::ITunerSession]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public int tune(android.hardware.broadcastradio.V2_0.ProgramSelector program) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            program.writeToParcel(_hidl_request);
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public int scan(boolean directionUp, boolean skipSubChannel) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            _hidl_request.writeBool(directionUp);
            _hidl_request.writeBool(skipSubChannel);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public int step(boolean directionUp) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            _hidl_request.writeBool(directionUp);
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public void cancel() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public int startProgramListUpdates(android.hardware.broadcastradio.V2_0.ProgramFilter filter) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            filter.writeToParcel(_hidl_request);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public void stopProgramListUpdates() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public void isConfigFlagSet(int flag, android.hardware.broadcastradio.V2_0.ITunerSession.isConfigFlagSetCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            _hidl_request.writeInt32(flag);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                boolean _hidl_out_value = _hidl_reply.readBool();
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_value);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public int setConfigFlag(int flag, boolean value) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            _hidl_request.writeInt32(flag);
            _hidl_request.writeBool(value);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                return _hidl_out_result;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> setParameters(java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> parameters) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            android.hardware.broadcastradio.V2_0.VendorKeyValue.writeVectorToParcel(_hidl_request, parameters);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> _hidl_out_results = android.hardware.broadcastradio.V2_0.VendorKeyValue.readVectorFromParcel(_hidl_reply);
                return _hidl_out_results;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> getParameters(java.util.ArrayList<java.lang.String> keys) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            _hidl_request.writeStringVector(keys);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> _hidl_out_parameters = android.hardware.broadcastradio.V2_0.VendorKeyValue.readVectorFromParcel(_hidl_reply);
                return _hidl_out_parameters;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession
        public void close() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.broadcastradio.V2_0.ITunerSession {
        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName;
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{-41, 4, 100, -59, 23, -92, -95, -75, 22, 119, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, -124, 55, 117, -87, 127, 69, 81, 2, -111, -98, -108, 91, 4, -9, 23, -71, -38, 57, 12, 15, 57}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerSession, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    android.hardware.broadcastradio.V2_0.ProgramSelector program = new android.hardware.broadcastradio.V2_0.ProgramSelector();
                    program.readFromParcel(_hidl_request);
                    int _hidl_out_result = tune(program);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    boolean directionUp = _hidl_request.readBool();
                    boolean skipSubChannel = _hidl_request.readBool();
                    int _hidl_out_result2 = scan(directionUp, skipSubChannel);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result2);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    boolean directionUp2 = _hidl_request.readBool();
                    int _hidl_out_result3 = step(directionUp2);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result3);
                    _hidl_reply.send();
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    cancel();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    android.hardware.broadcastradio.V2_0.ProgramFilter filter = new android.hardware.broadcastradio.V2_0.ProgramFilter();
                    filter.readFromParcel(_hidl_request);
                    int _hidl_out_result4 = startProgramListUpdates(filter);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result4);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    stopProgramListUpdates();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 7:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    int flag = _hidl_request.readInt32();
                    isConfigFlagSet(flag, new android.hardware.broadcastradio.V2_0.ITunerSession.isConfigFlagSetCallback() { // from class: android.hardware.broadcastradio.V2_0.ITunerSession.Stub.1
                        @Override // android.hardware.broadcastradio.V2_0.ITunerSession.isConfigFlagSetCallback
                        public void onValues(int result, boolean value) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeBool(value);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 8:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    int flag2 = _hidl_request.readInt32();
                    boolean value = _hidl_request.readBool();
                    int _hidl_out_result5 = setConfigFlag(flag2, value);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_result5);
                    _hidl_reply.send();
                    return;
                case 9:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> parameters = android.hardware.broadcastradio.V2_0.VendorKeyValue.readVectorFromParcel(_hidl_request);
                    java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> _hidl_out_results = setParameters(parameters);
                    _hidl_reply.writeStatus(0);
                    android.hardware.broadcastradio.V2_0.VendorKeyValue.writeVectorToParcel(_hidl_reply, _hidl_out_results);
                    _hidl_reply.send();
                    return;
                case 10:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    java.util.ArrayList<java.lang.String> keys = _hidl_request.readStringVector();
                    java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> _hidl_out_parameters = getParameters(keys);
                    _hidl_reply.writeStatus(0);
                    android.hardware.broadcastradio.V2_0.VendorKeyValue.writeVectorToParcel(_hidl_reply, _hidl_out_parameters);
                    _hidl_reply.send();
                    return;
                case 11:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.ITunerSession.kInterfaceName);
                    close();
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
