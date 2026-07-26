package android.hardware.biometrics.fingerprint.V2_2;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricsFingerprintClientCallback extends android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback {
    public static final java.lang.String kInterfaceName = "android.hardware.biometrics.fingerprint@2.2::IBiometricsFingerprintClientCallback";

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    void onAcquired_2_2(long j, int i, int i2) throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback)) {
            return (android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback) iface;
        }
        android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback proxy = new android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.Proxy(binder);
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

    static android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.biometrics.fingerprint@2.2::IBiometricsFingerprintClientCallback]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(fingerId);
            _hidl_request.writeInt32(groupId);
            _hidl_request.writeInt32(remaining);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(acquiredInfo);
            _hidl_request.writeInt32(vendorCode);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onAuthenticated(long deviceId, int fingerId, int groupId, java.util.ArrayList<java.lang.Byte> token) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(fingerId);
            _hidl_request.writeInt32(groupId);
            _hidl_request.writeInt8Vector(token);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onError(long deviceId, int error, int vendorCode) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(error);
            _hidl_request.writeInt32(vendorCode);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onRemoved(long deviceId, int fingerId, int groupId, int remaining) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(fingerId);
            _hidl_request.writeInt32(groupId);
            _hidl_request.writeInt32(remaining);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback
        public void onEnumerate(long deviceId, int fingerId, int groupId, int remaining) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(fingerId);
            _hidl_request.writeInt32(groupId);
            _hidl_request.writeInt32(remaining);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback
        public void onAcquired_2_2(long deviceId, int acquiredInfo, int vendorCode) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.kInterfaceName);
            _hidl_request.writeInt64(deviceId);
            _hidl_request.writeInt32(acquiredInfo);
            _hidl_request.writeInt32(vendorCode);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback {
        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.kInterfaceName, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.kInterfaceName;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_II, -54, -39, -97, 95, -21, 46, -87, -68, -44, 87, -112, 85, -19, -12, -81, -113, -21, -97, -58, 2, -90, -28, com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_II, 125, -35, 114, 125, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_ENDPOINT, 77, 73, -111}, new byte[]{-86, -69, 92, 60, 88, 85, -110, -41, 30, -27, 123, 119, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_HUB, -116, 20, -103, 61, 119, -111, 77, -34, -86, 100, -78, -59, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CAPABILITY, -102, 96, 43, 2, -22, 71}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId = _hidl_request.readInt64();
                    int fingerId = _hidl_request.readInt32();
                    int groupId = _hidl_request.readInt32();
                    int remaining = _hidl_request.readInt32();
                    onEnrollResult(deviceId, fingerId, groupId, remaining);
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId2 = _hidl_request.readInt64();
                    int acquiredInfo = _hidl_request.readInt32();
                    int vendorCode = _hidl_request.readInt32();
                    onAcquired(deviceId2, acquiredInfo, vendorCode);
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId3 = _hidl_request.readInt64();
                    int fingerId2 = _hidl_request.readInt32();
                    int groupId2 = _hidl_request.readInt32();
                    java.util.ArrayList<java.lang.Byte> token = _hidl_request.readInt8Vector();
                    onAuthenticated(deviceId3, fingerId2, groupId2, token);
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId4 = _hidl_request.readInt64();
                    int error = _hidl_request.readInt32();
                    int vendorCode2 = _hidl_request.readInt32();
                    onError(deviceId4, error, vendorCode2);
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId5 = _hidl_request.readInt64();
                    int fingerId3 = _hidl_request.readInt32();
                    int groupId3 = _hidl_request.readInt32();
                    int remaining2 = _hidl_request.readInt32();
                    onRemoved(deviceId5, fingerId3, groupId3, remaining2);
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId6 = _hidl_request.readInt64();
                    int fingerId4 = _hidl_request.readInt32();
                    int groupId4 = _hidl_request.readInt32();
                    int remaining3 = _hidl_request.readInt32();
                    onEnumerate(deviceId6, fingerId4, groupId4, remaining3);
                    return;
                case 7:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprintClientCallback.kInterfaceName);
                    long deviceId7 = _hidl_request.readInt64();
                    int acquiredInfo2 = _hidl_request.readInt32();
                    int vendorCode3 = _hidl_request.readInt32();
                    onAcquired_2_2(deviceId7, acquiredInfo2, vendorCode3);
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
