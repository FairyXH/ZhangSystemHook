package android.hardware.biometrics.fingerprint.V2_3;

/* JADX INFO: loaded from: classes.dex */
public interface IBiometricsFingerprint extends android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint {
    public static final java.lang.String kInterfaceName = "android.hardware.biometrics.fingerprint@2.3::IBiometricsFingerprint";

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    android.os.IHwBinder asBinder();

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    void debug(android.os.NativeHandle nativeHandle, java.util.ArrayList<java.lang.String> arrayList) throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    android.hidl.base.V1_0.DebugInfo getDebugInfo() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    java.util.ArrayList<byte[]> getHashChain() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    java.util.ArrayList<java.lang.String> interfaceChain() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    java.lang.String interfaceDescriptor() throws android.os.RemoteException;

    boolean isUdfps(int i) throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    boolean linkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient, long j) throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws android.os.RemoteException;

    void onFingerDown(int i, int i2, float f, float f2) throws android.os.RemoteException;

    void onFingerUp() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    void ping() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws android.os.RemoteException;

    @Override // android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
    boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient deathRecipient) throws android.os.RemoteException;

    static android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint asInterface(android.os.IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        android.os.IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint)) {
            return (android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint) iface;
        }
        android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint proxy = new android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.Proxy(binder);
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

    static android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint castFrom(android.os.IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint getService(java.lang.String serviceName, boolean retry) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint getService(boolean retry) throws android.os.RemoteException {
        return getService("default", retry);
    }

    @java.lang.Deprecated
    static android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint getService(java.lang.String serviceName) throws android.os.RemoteException {
        return asInterface(android.os.HwBinder.getService(kInterfaceName, serviceName));
    }

    @java.lang.Deprecated
    static android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint getService() throws android.os.RemoteException {
        return getService("default");
    }

    public static final class Proxy implements android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint {
        private android.os.IHwBinder mRemote;

        public Proxy(android.os.IHwBinder remote) {
            this.mRemote = (android.os.IHwBinder) java.util.Objects.requireNonNull(remote);
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this.mRemote;
        }

        public java.lang.String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (android.os.RemoteException e) {
                return "[class or subclass of android.hardware.biometrics.fingerprint@2.3::IBiometricsFingerprint]@Proxy";
            }
        }

        public final boolean equals(java.lang.Object other) {
            return android.os.HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public long setNotify(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback clientCallback) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            _hidl_request.writeStrongBinder(clientCallback == null ? null : clientCallback.asBinder());
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                long _hidl_out_deviceId = _hidl_reply.readInt64();
                return _hidl_out_deviceId;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public long preEnroll() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                long _hidl_out_authChallenge = _hidl_reply.readInt64();
                return _hidl_out_authChallenge;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int enroll(byte[] hat, int gid, int timeoutSec) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            android.os.HwBlob _hidl_blob = new android.os.HwBlob(69);
            if (hat == null || hat.length != 69) {
                throw new java.lang.IllegalArgumentException("Array element is not of the expected length");
            }
            _hidl_blob.putInt8Array(0L, hat);
            _hidl_request.writeBuffer(_hidl_blob);
            _hidl_request.writeInt32(gid);
            _hidl_request.writeInt32(timeoutSec);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(3, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int postEnroll() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(4, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public long getAuthenticatorId() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(5, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                long _hidl_out_AuthenticatorId = _hidl_reply.readInt64();
                return _hidl_out_AuthenticatorId;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int cancel() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(6, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int enumerate() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(7, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int remove(int gid, int fid) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            _hidl_request.writeInt32(gid);
            _hidl_request.writeInt32(fid);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(8, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int setActiveGroup(int gid, java.lang.String storePath) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            _hidl_request.writeInt32(gid);
            _hidl_request.writeString(storePath);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(9, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint
        public int authenticate(long operationId, int gid) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
            _hidl_request.writeInt64(operationId);
            _hidl_request.writeInt32(gid);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(10, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                int _hidl_out_debugErrno = _hidl_reply.readInt32();
                return _hidl_out_debugErrno;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint
        public boolean isUdfps(int sensorId) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName);
            _hidl_request.writeInt32(sensorId);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(11, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                boolean _hidl_out_isUdfps = _hidl_reply.readBool();
                return _hidl_out_isUdfps;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint
        public void onFingerDown(int x, int y, float minor, float major) throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName);
            _hidl_request.writeInt32(x);
            _hidl_request.writeInt32(y);
            _hidl_request.writeFloat(minor);
            _hidl_request.writeFloat(major);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(12, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint
        public void onFingerUp() throws android.os.RemoteException {
            android.os.HwParcel _hidl_request = new android.os.HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName);
            android.os.HwParcel _hidl_reply = new android.os.HwParcel();
            try {
                this.mRemote.transact(13, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) throws android.os.RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
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

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) throws android.os.RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    public static abstract class Stub extends android.os.HwBinder implements android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint {
        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public android.os.IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<java.lang.String> interfaceChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint.kInterfaceName, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName, android.hidl.base.V1_0.IBase.kInterfaceName));
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public void debug(android.os.NativeHandle fd, java.util.ArrayList<java.lang.String> options) {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final java.lang.String interfaceDescriptor() {
            return android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final java.util.ArrayList<byte[]> getHashChain() {
            return new java.util.ArrayList<>(java.util.Arrays.asList(new byte[]{122, 120, -23, -106, 59, -20, 11, 7, 30, 125, 70, -110, -116, 97, 0, -30, 23, 66, 112, -119, 45, 63, 21, -95, -22, -83, 7, 73, -105, -83, -14, 121}, new byte[]{20, 15, -113, 98, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CAPABILITY, 12, -49, -100, -46, com.android.server.usb.descriptors.UsbASFormat.EXT_FORMAT_TYPE_II, -82, 54, -123, -96, -12, -17, 10, -97, -105, 29, 119, -33, -68, 115, 80, -52, -76, -32, 76, -14, -107, -20}, new byte[]{31, -67, -63, -8, 82, -8, -67, 46, 74, 108, 92, -77, 10, -62, -73, -122, 104, -55, -115, -50, 17, -118, 97, 118, 45, 64, 52, -82, -123, -97, 67, -40}, new byte[]{-20, 127, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_PHYSICAL, -17, 5, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -13, -51, 105, 87, 19, -109, com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final boolean linkToDeath(android.os.IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final android.hidl.base.V1_0.DebugInfo getDebugInfo() {
            android.hidl.base.V1_0.DebugInfo info = new android.hidl.base.V1_0.DebugInfo();
            info.pid = android.os.HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            android.os.HwBinder.enableInstrumentation();
        }

        @Override // android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_2.IBiometricsFingerprint, android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint, android.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(android.os.IHwBinder.DeathRecipient recipient) {
            return true;
        }

        public android.os.IHwInterface queryLocalInterface(java.lang.String descriptor) {
            if (android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName.equals(descriptor)) {
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
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback clientCallback = android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprintClientCallback.asInterface(_hidl_request.readStrongBinder());
                    long _hidl_out_deviceId = setNotify(clientCallback);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt64(_hidl_out_deviceId);
                    _hidl_reply.send();
                    return;
                case 2:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    long _hidl_out_authChallenge = preEnroll();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt64(_hidl_out_authChallenge);
                    _hidl_reply.send();
                    return;
                case 3:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    byte[] hat = new byte[69];
                    _hidl_request.readBuffer(69L).copyToInt8Array(0L, hat, 69);
                    int gid = _hidl_request.readInt32();
                    int timeoutSec = _hidl_request.readInt32();
                    int _hidl_out_debugErrno = enroll(hat, gid, timeoutSec);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno);
                    _hidl_reply.send();
                    return;
                case 4:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    int _hidl_out_debugErrno2 = postEnroll();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno2);
                    _hidl_reply.send();
                    return;
                case 5:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    long _hidl_out_AuthenticatorId = getAuthenticatorId();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt64(_hidl_out_AuthenticatorId);
                    _hidl_reply.send();
                    return;
                case 6:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    int _hidl_out_debugErrno3 = cancel();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno3);
                    _hidl_reply.send();
                    return;
                case 7:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    int _hidl_out_debugErrno4 = enumerate();
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno4);
                    _hidl_reply.send();
                    return;
                case 8:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    int gid2 = _hidl_request.readInt32();
                    int fid = _hidl_request.readInt32();
                    int _hidl_out_debugErrno5 = remove(gid2, fid);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno5);
                    _hidl_reply.send();
                    return;
                case 9:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    int gid3 = _hidl_request.readInt32();
                    java.lang.String storePath = _hidl_request.readString();
                    int _hidl_out_debugErrno6 = setActiveGroup(gid3, storePath);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno6);
                    _hidl_reply.send();
                    return;
                case 10:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_1.IBiometricsFingerprint.kInterfaceName);
                    long operationId = _hidl_request.readInt64();
                    int gid4 = _hidl_request.readInt32();
                    int _hidl_out_debugErrno7 = authenticate(operationId, gid4);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeInt32(_hidl_out_debugErrno7);
                    _hidl_reply.send();
                    return;
                case 11:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName);
                    int sensorId = _hidl_request.readInt32();
                    boolean _hidl_out_isUdfps = isUdfps(sensorId);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.writeBool(_hidl_out_isUdfps);
                    _hidl_reply.send();
                    return;
                case 12:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName);
                    int x = _hidl_request.readInt32();
                    int y = _hidl_request.readInt32();
                    float minor = _hidl_request.readFloat();
                    float major = _hidl_request.readFloat();
                    onFingerDown(x, y, minor, major);
                    _hidl_reply.writeStatus(0);
                    _hidl_reply.send();
                    return;
                case 13:
                    _hidl_request.enforceInterface(android.hardware.biometrics.fingerprint.V2_3.IBiometricsFingerprint.kInterfaceName);
                    onFingerUp();
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
