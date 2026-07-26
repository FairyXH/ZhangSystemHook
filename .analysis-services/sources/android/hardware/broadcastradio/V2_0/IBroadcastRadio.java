package android.hardware.broadcastradio.V2_0;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastRadio extends android.hidl.base.V1_0.IBase {
    public static final java.lang.String kInterfaceName = "android.hardware.broadcastradio@2.0::IBroadcastRadio";

    @java.lang.FunctionalInterface
    public interface getAmFmRegionConfigCallback {
        void onValues(int i, android.hardware.broadcastradio.V2_0.AmFmRegionConfig amFmRegionConfig);
    }

    @java.lang.FunctionalInterface
    public interface getDabRegionConfigCallback {
        void onValues(int i, java.util.ArrayList<android.hardware.broadcastradio.V2_0.DabTableEntry> arrayList);
    }

    @java.lang.FunctionalInterface
    public interface openSessionCallback {
        void onValues(int i, android.hardware.broadcastradio.V2_0.ITunerSession iTunerSession);
    }

    @java.lang.FunctionalInterface
    public interface registerAnnouncementListenerCallback {
        void onValues(int i, android.hardware.broadcastradio.V2_0.ICloseHandle iCloseHandle);
    }

    @Override // android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    void getAmFmRegionConfig(boolean z, android.hardware.broadcastradio.V2_0.IBroadcastRadio.getAmFmRegionConfigCallback getamfmregionconfigcallback) throws android.os.RemoteException;

    void getDabRegionConfig(android.hardware.broadcastradio.V2_0.IBroadcastRadio.getDabRegionConfigCallback getdabregionconfigcallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    java.util.ArrayList<java.lang.Byte> getImage(int i) throws android.os.RemoteException;

    android.hardware.broadcastradio.V2_0.Properties getProperties() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    void openSession(android.hardware.broadcastradio.V2_0.ITunerCallback iTunerCallback, android.hardware.broadcastradio.V2_0.IBroadcastRadio.openSessionCallback opensessioncallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    void registerAnnouncementListener(java.util.ArrayList<java.lang.Byte> arrayList, android.hardware.broadcastradio.V2_0.IAnnouncementListener iAnnouncementListener, android.hardware.broadcastradio.V2_0.IBroadcastRadio.registerAnnouncementListenerCallback registerannouncementlistenercallback) throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.broadcastradio.V2_0.IBroadcastRadio asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.broadcastradio.V2_0.IBroadcastRadio)) {
            return (android.hardware.broadcastradio.V2_0.IBroadcastRadio) iface;
        }
        android.hardware.broadcastradio.V2_0.IBroadcastRadio proxy = new android.hardware.broadcastradio.V2_0.IBroadcastRadio.Proxy(binder);
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

    static android.hardware.broadcastradio.V2_0.IBroadcastRadio castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.broadcastradio.V2_0.IBroadcastRadio getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.broadcastradio.V2_0.IBroadcastRadio getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.broadcastradio.V2_0.IBroadcastRadio getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.broadcastradio.V2_0.IBroadcastRadio getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements android.hardware.broadcastradio.V2_0.IBroadcastRadio {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.broadcastradio@2.0::IBroadcastRadio]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio
        public android.hardware.broadcastradio.V2_0.Properties getProperties() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                android.hardware.broadcastradio.V2_0.Properties _hidl_out_properties = new android.hardware.broadcastradio.V2_0.Properties();
                _hidl_out_properties.readFromParcel(_hidl_reply);
                return _hidl_out_properties;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio
        public void getAmFmRegionConfig(boolean full, android.hardware.broadcastradio.V2_0.IBroadcastRadio.getAmFmRegionConfigCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
            _hidl_request.writeBool(full);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                android.hardware.broadcastradio.V2_0.AmFmRegionConfig _hidl_out_config = new android.hardware.broadcastradio.V2_0.AmFmRegionConfig();
                _hidl_out_config.readFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_config);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio
        public void getDabRegionConfig(android.hardware.broadcastradio.V2_0.IBroadcastRadio.getDabRegionConfigCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                java.util.ArrayList<android.hardware.broadcastradio.V2_0.DabTableEntry> _hidl_out_config = android.hardware.broadcastradio.V2_0.DabTableEntry.readVectorFromParcel(_hidl_reply);
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_config);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio
        public void openSession(android.hardware.broadcastradio.V2_0.ITunerCallback callback, android.hardware.broadcastradio.V2_0.IBroadcastRadio.openSessionCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
            _hidl_request.writeStrongBinder(callback == null ? null : callback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                android.hardware.broadcastradio.V2_0.ITunerSession _hidl_out_session = android.hardware.broadcastradio.V2_0.ITunerSession.asInterface(_hidl_reply.readStrongBinder());
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_session);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio
        public java.util.ArrayList<java.lang.Byte> getImage(int id) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
            _hidl_request.writeInt32(id);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                java.util.ArrayList<java.lang.Byte> _hidl_out_image = _hidl_reply.readInt8Vector();
                return _hidl_out_image;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio
        public void registerAnnouncementListener(java.util.ArrayList<java.lang.Byte> enabled, android.hardware.broadcastradio.V2_0.IAnnouncementListener listener, android.hardware.broadcastradio.V2_0.IBroadcastRadio.registerAnnouncementListenerCallback _hidl_cb) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
            _hidl_request.writeInt8Vector(enabled);
            _hidl_request.writeStrongBinder(listener == null ? null : listener.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_result = _hidl_reply.readInt32();
                android.hardware.broadcastradio.V2_0.ICloseHandle _hidl_out_closeHandle = android.hardware.broadcastradio.V2_0.ICloseHandle.asInterface(_hidl_reply.readStrongBinder());
                _hidl_cb.onValues(_hidl_out_result, _hidl_out_closeHandle);
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.broadcastradio.V2_0.IBroadcastRadio {
        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName;
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{68, 1, 124, 66, -26, -12, -40, -53, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION, -16, 126, -79, -38, 4, 84, 10, -104, 115, 106, 51, 106, -62, -116, 126, android.hardware.tv.hdmi.cec.CecLogicalAddress.FREE_USE, -46, -26, -98, 21, -119, -8, -47}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
                    android.hardware.broadcastradio.V2_0.Properties _hidl_out_properties = getProperties();
                    _hidl_reply.writeStatus(0);
                    _hidl_out_properties.writeToParcel(_hidl_reply);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
                    boolean full = _hidl_request.readBool();
                    getAmFmRegionConfig(full, new android.hardware.broadcastradio.V2_0.IBroadcastRadio.getAmFmRegionConfigCallback() { // from class: android.hardware.broadcastradio.V2_0.IBroadcastRadio.Stub.1
                        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.getAmFmRegionConfigCallback
                        public void onValues(int result, android.hardware.broadcastradio.V2_0.AmFmRegionConfig config) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            config.writeToParcel(_hidl_reply);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
                    getDabRegionConfig(new android.hardware.broadcastradio.V2_0.IBroadcastRadio.getDabRegionConfigCallback() { // from class: android.hardware.broadcastradio.V2_0.IBroadcastRadio.Stub.2
                        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.getDabRegionConfigCallback
                        public void onValues(int result, java.util.ArrayList<android.hardware.broadcastradio.V2_0.DabTableEntry> config) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            android.hardware.broadcastradio.V2_0.DabTableEntry.writeVectorToParcel(_hidl_reply, config);
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
                    android.hardware.broadcastradio.V2_0.ITunerCallback callback = android.hardware.broadcastradio.V2_0.ITunerCallback.asInterface(_hidl_request.readStrongBinder());
                    openSession(callback, new android.hardware.broadcastradio.V2_0.IBroadcastRadio.openSessionCallback() { // from class: android.hardware.broadcastradio.V2_0.IBroadcastRadio.Stub.3
                        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.openSessionCallback
                        public void onValues(int result, android.hardware.broadcastradio.V2_0.ITunerSession session) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeStrongBinder(session == null ? null : session.asBinder());
                            _hidl_reply.send();
                        }
                    });
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
                    int id = _hidl_request.readInt32();
                    java.util.ArrayList<java.lang.Byte> _hidl_out_image = getImage(id);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt8Vector(_hidl_out_image);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName);
                    java.util.ArrayList<java.lang.Byte> enabled = _hidl_request.readInt8Vector();
                    android.hardware.broadcastradio.V2_0.IAnnouncementListener listener = android.hardware.broadcastradio.V2_0.IAnnouncementListener.asInterface(_hidl_request.readStrongBinder());
                    registerAnnouncementListener(enabled, listener, new android.hardware.broadcastradio.V2_0.IBroadcastRadio.registerAnnouncementListenerCallback() { // from class: android.hardware.broadcastradio.V2_0.IBroadcastRadio.Stub.4
                        @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.registerAnnouncementListenerCallback
                        public void onValues(int result, android.hardware.broadcastradio.V2_0.ICloseHandle closeHandle) {
                            _hidl_reply.writeStatus(0);
                            _hidl_reply.writeInt32(result);
                            _hidl_reply.writeStrongBinder(closeHandle == null ? null : closeHandle.asBinder());
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
