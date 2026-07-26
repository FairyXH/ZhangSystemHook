package android.hardware.security.keymint;

/* JADX INFO: loaded from: classes.dex */
public interface IRemotelyProvisionedComponent extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$security$keymint$IRemotelyProvisionedComponent".replace('$', '.');
    public static final java.lang.String HASH = "7d14edbfab5c490efa407ba55fa80614bb48ae8e";
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_INVALID_EEK = 5;
    public static final int STATUS_INVALID_MAC = 2;
    public static final int STATUS_PRODUCTION_KEY_IN_TEST_REQUEST = 3;
    public static final int STATUS_REMOVED = 6;
    public static final int STATUS_TEST_KEY_IN_PRODUCTION_REQUEST = 4;
    public static final int VERSION = 3;

    byte[] generateCertificateRequest(boolean z, android.hardware.security.keymint.MacedPublicKey[] macedPublicKeyArr, byte[] bArr, byte[] bArr2, android.hardware.security.keymint.DeviceInfo deviceInfo, android.hardware.security.keymint.ProtectedData protectedData) throws android.os.RemoteException;

    byte[] generateCertificateRequestV2(android.hardware.security.keymint.MacedPublicKey[] macedPublicKeyArr, byte[] bArr) throws android.os.RemoteException;

    byte[] generateEcdsaP256KeyPair(boolean z, android.hardware.security.keymint.MacedPublicKey macedPublicKey) throws android.os.RemoteException;

    android.hardware.security.keymint.RpcHardwareInfo getHardwareInfo() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    public static class Default implements android.hardware.security.keymint.IRemotelyProvisionedComponent {
        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public android.hardware.security.keymint.RpcHardwareInfo getHardwareInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public byte[] generateEcdsaP256KeyPair(boolean testMode, android.hardware.security.keymint.MacedPublicKey macedPublicKey) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public byte[] generateCertificateRequest(boolean testMode, android.hardware.security.keymint.MacedPublicKey[] keysToSign, byte[] endpointEncryptionCertChain, byte[] challenge, android.hardware.security.keymint.DeviceInfo deviceInfo, android.hardware.security.keymint.ProtectedData protectedData) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public byte[] generateCertificateRequestV2(android.hardware.security.keymint.MacedPublicKey[] keysToSign, byte[] challenge) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.security.keymint.IRemotelyProvisionedComponent {
        static final int TRANSACTION_generateCertificateRequest = 3;
        static final int TRANSACTION_generateCertificateRequestV2 = 4;
        static final int TRANSACTION_generateEcdsaP256KeyPair = 2;
        static final int TRANSACTION_getHardwareInfo = 1;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.security.keymint.IRemotelyProvisionedComponent asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.security.keymint.IRemotelyProvisionedComponent)) {
                return (android.hardware.security.keymint.IRemotelyProvisionedComponent) iin;
            }
            return new android.hardware.security.keymint.IRemotelyProvisionedComponent.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(descriptor);
            }
            if (code == 1598968902) {
                reply.writeString(descriptor);
                return true;
            }
            if (code == 16777215) {
                reply.writeNoException();
                reply.writeInt(getInterfaceVersion());
                return true;
            }
            if (code == TRANSACTION_getInterfaceHash) {
                reply.writeNoException();
                reply.writeString(getInterfaceHash());
                return true;
            }
            switch (code) {
                case 1:
                    android.hardware.security.keymint.RpcHardwareInfo _result = getHardwareInfo();
                    reply.writeNoException();
                    reply.writeTypedObject(_result, 1);
                    return true;
                case 2:
                    boolean _arg0 = data.readBoolean();
                    android.hardware.security.keymint.MacedPublicKey _arg1 = new android.hardware.security.keymint.MacedPublicKey();
                    data.enforceNoDataAvail();
                    byte[] _result2 = generateEcdsaP256KeyPair(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeByteArray(_result2);
                    reply.writeTypedObject(_arg1, 1);
                    return true;
                case 3:
                    boolean _arg02 = data.readBoolean();
                    android.hardware.security.keymint.MacedPublicKey[] _arg12 = (android.hardware.security.keymint.MacedPublicKey[]) data.createTypedArray(android.hardware.security.keymint.MacedPublicKey.CREATOR);
                    byte[] _arg2 = data.createByteArray();
                    byte[] _arg3 = data.createByteArray();
                    android.hardware.security.keymint.DeviceInfo _arg4 = new android.hardware.security.keymint.DeviceInfo();
                    android.hardware.security.keymint.ProtectedData _arg5 = new android.hardware.security.keymint.ProtectedData();
                    data.enforceNoDataAvail();
                    byte[] _result3 = generateCertificateRequest(_arg02, _arg12, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    reply.writeTypedObject(_arg4, 1);
                    reply.writeTypedObject(_arg5, 1);
                    return true;
                case 4:
                    android.hardware.security.keymint.MacedPublicKey[] _arg03 = (android.hardware.security.keymint.MacedPublicKey[]) data.createTypedArray(android.hardware.security.keymint.MacedPublicKey.CREATOR);
                    byte[] _arg13 = data.createByteArray();
                    data.enforceNoDataAvail();
                    byte[] _result4 = generateCertificateRequestV2(_arg03, _arg13);
                    reply.writeNoException();
                    reply.writeByteArray(_result4);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.security.keymint.IRemotelyProvisionedComponent {
            private android.os.IBinder mRemote;
            private int mCachedVersion = -1;
            private java.lang.String mCachedHash = "-1";

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public android.hardware.security.keymint.RpcHardwareInfo getHardwareInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getHardwareInfo is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.security.keymint.RpcHardwareInfo _result = (android.hardware.security.keymint.RpcHardwareInfo) _reply.readTypedObject(android.hardware.security.keymint.RpcHardwareInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public byte[] generateEcdsaP256KeyPair(boolean testMode, android.hardware.security.keymint.MacedPublicKey macedPublicKey) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(testMode);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method generateEcdsaP256KeyPair is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    if (_reply.readInt() != 0) {
                        macedPublicKey.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public byte[] generateCertificateRequest(boolean testMode, android.hardware.security.keymint.MacedPublicKey[] keysToSign, byte[] endpointEncryptionCertChain, byte[] challenge, android.hardware.security.keymint.DeviceInfo deviceInfo, android.hardware.security.keymint.ProtectedData protectedData) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(testMode);
                    _data.writeTypedArray(keysToSign, 0);
                    _data.writeByteArray(endpointEncryptionCertChain);
                    _data.writeByteArray(challenge);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method generateCertificateRequest is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    if (_reply.readInt() != 0) {
                        deviceInfo.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        protectedData.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public byte[] generateCertificateRequestV2(android.hardware.security.keymint.MacedPublicKey[] keysToSign, byte[] challenge) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(keysToSign, 0);
                    _data.writeByteArray(challenge);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method generateCertificateRequestV2 is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public int getInterfaceVersion() throws android.os.RemoteException {
                if (this.mCachedVersion == -1) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(16777215, data, reply, 0);
                        reply.readException();
                        this.mCachedVersion = reply.readInt();
                    } finally {
                        reply.recycle();
                        data.recycle();
                    }
                }
                return this.mCachedVersion;
            }

            @Override // android.hardware.security.keymint.IRemotelyProvisionedComponent
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.security.keymint.IRemotelyProvisionedComponent.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
                        reply.readException();
                        this.mCachedHash = reply.readString();
                        reply.recycle();
                        data.recycle();
                    } catch (java.lang.Throwable th) {
                        reply.recycle();
                        data.recycle();
                        throw th;
                    }
                }
                return this.mCachedHash;
            }
        }
    }
}
