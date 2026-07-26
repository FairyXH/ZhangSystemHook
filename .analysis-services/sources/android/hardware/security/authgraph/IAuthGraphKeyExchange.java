package android.hardware.security.authgraph;

/* JADX INFO: loaded from: classes.dex */
public interface IAuthGraphKeyExchange extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$security$authgraph$IAuthGraphKeyExchange".replace('$', '.');
    public static final java.lang.String HASH = "3758824e7b75acdb1ca66620fb8a8aec0ec6dfcc";
    public static final int VERSION = 1;

    android.hardware.security.authgraph.Arc[] authenticationComplete(android.hardware.security.authgraph.SessionIdSignature sessionIdSignature, android.hardware.security.authgraph.Arc[] arcArr) throws android.os.RemoteException;

    android.hardware.security.authgraph.SessionInitiationInfo create() throws android.os.RemoteException;

    android.hardware.security.authgraph.SessionInfo finish(android.hardware.security.authgraph.PubKey pubKey, android.hardware.security.authgraph.Identity identity, android.hardware.security.authgraph.SessionIdSignature sessionIdSignature, byte[] bArr, int i, android.hardware.security.authgraph.Key key) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    android.hardware.security.authgraph.KeInitResult init(android.hardware.security.authgraph.PubKey pubKey, android.hardware.security.authgraph.Identity identity, byte[] bArr, int i) throws android.os.RemoteException;

    public static class Default implements android.hardware.security.authgraph.IAuthGraphKeyExchange {
        @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
        public android.hardware.security.authgraph.SessionInitiationInfo create() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
        public android.hardware.security.authgraph.KeInitResult init(android.hardware.security.authgraph.PubKey peerPubKey, android.hardware.security.authgraph.Identity peerId, byte[] peerNonce, int peerVersion) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
        public android.hardware.security.authgraph.SessionInfo finish(android.hardware.security.authgraph.PubKey peerPubKey, android.hardware.security.authgraph.Identity peerId, android.hardware.security.authgraph.SessionIdSignature peerSignature, byte[] peerNonce, int peerVersion, android.hardware.security.authgraph.Key ownKey) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
        public android.hardware.security.authgraph.Arc[] authenticationComplete(android.hardware.security.authgraph.SessionIdSignature peerSignature, android.hardware.security.authgraph.Arc[] sharedKeys) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.security.authgraph.IAuthGraphKeyExchange {
        static final int TRANSACTION_authenticationComplete = 4;
        static final int TRANSACTION_create = 1;
        static final int TRANSACTION_finish = 3;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_init = 2;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.security.authgraph.IAuthGraphKeyExchange asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.security.authgraph.IAuthGraphKeyExchange)) {
                return (android.hardware.security.authgraph.IAuthGraphKeyExchange) iin;
            }
            return new android.hardware.security.authgraph.IAuthGraphKeyExchange.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "create";
                case 2:
                    return "init";
                case 3:
                    return "finish";
                case 4:
                    return "authenticationComplete";
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case 16777215:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        public java.lang.String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
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
                    android.hardware.security.authgraph.SessionInitiationInfo _result = create();
                    reply.writeNoException();
                    reply.writeTypedObject(_result, 1);
                    return true;
                case 2:
                    android.hardware.security.authgraph.PubKey _arg0 = (android.hardware.security.authgraph.PubKey) data.readTypedObject(android.hardware.security.authgraph.PubKey.CREATOR);
                    android.hardware.security.authgraph.Identity _arg1 = (android.hardware.security.authgraph.Identity) data.readTypedObject(android.hardware.security.authgraph.Identity.CREATOR);
                    byte[] _arg2 = data.createByteArray();
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    android.hardware.security.authgraph.KeInitResult _result2 = init(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeTypedObject(_result2, 1);
                    return true;
                case 3:
                    android.hardware.security.authgraph.PubKey _arg02 = (android.hardware.security.authgraph.PubKey) data.readTypedObject(android.hardware.security.authgraph.PubKey.CREATOR);
                    android.hardware.security.authgraph.Identity _arg12 = (android.hardware.security.authgraph.Identity) data.readTypedObject(android.hardware.security.authgraph.Identity.CREATOR);
                    android.hardware.security.authgraph.SessionIdSignature _arg22 = (android.hardware.security.authgraph.SessionIdSignature) data.readTypedObject(android.hardware.security.authgraph.SessionIdSignature.CREATOR);
                    byte[] _arg32 = data.createByteArray();
                    int _arg4 = data.readInt();
                    android.hardware.security.authgraph.Key _arg5 = (android.hardware.security.authgraph.Key) data.readTypedObject(android.hardware.security.authgraph.Key.CREATOR);
                    data.enforceNoDataAvail();
                    android.hardware.security.authgraph.SessionInfo _result3 = finish(_arg02, _arg12, _arg22, _arg32, _arg4, _arg5);
                    reply.writeNoException();
                    reply.writeTypedObject(_result3, 1);
                    return true;
                case 4:
                    android.hardware.security.authgraph.SessionIdSignature _arg03 = (android.hardware.security.authgraph.SessionIdSignature) data.readTypedObject(android.hardware.security.authgraph.SessionIdSignature.CREATOR);
                    android.hardware.security.authgraph.Arc[] _arg13 = (android.hardware.security.authgraph.Arc[]) data.createFixedArray(android.hardware.security.authgraph.Arc[].class, android.hardware.security.authgraph.Arc.CREATOR, 2);
                    data.enforceNoDataAvail();
                    android.hardware.security.authgraph.Arc[] _result4 = authenticationComplete(_arg03, _arg13);
                    reply.writeNoException();
                    reply.writeFixedArray(_result4, 1, 2);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.security.authgraph.IAuthGraphKeyExchange {
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

            @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
            public android.hardware.security.authgraph.SessionInitiationInfo create() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method create is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.security.authgraph.SessionInitiationInfo _result = (android.hardware.security.authgraph.SessionInitiationInfo) _reply.readTypedObject(android.hardware.security.authgraph.SessionInitiationInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
            public android.hardware.security.authgraph.KeInitResult init(android.hardware.security.authgraph.PubKey peerPubKey, android.hardware.security.authgraph.Identity peerId, byte[] peerNonce, int peerVersion) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(peerPubKey, 0);
                    _data.writeTypedObject(peerId, 0);
                    _data.writeByteArray(peerNonce);
                    _data.writeInt(peerVersion);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method init is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.security.authgraph.KeInitResult _result = (android.hardware.security.authgraph.KeInitResult) _reply.readTypedObject(android.hardware.security.authgraph.KeInitResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
            public android.hardware.security.authgraph.SessionInfo finish(android.hardware.security.authgraph.PubKey peerPubKey, android.hardware.security.authgraph.Identity peerId, android.hardware.security.authgraph.SessionIdSignature peerSignature, byte[] peerNonce, int peerVersion, android.hardware.security.authgraph.Key ownKey) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(peerPubKey, 0);
                    _data.writeTypedObject(peerId, 0);
                    _data.writeTypedObject(peerSignature, 0);
                    _data.writeByteArray(peerNonce);
                    _data.writeInt(peerVersion);
                    _data.writeTypedObject(ownKey, 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method finish is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.security.authgraph.SessionInfo _result = (android.hardware.security.authgraph.SessionInfo) _reply.readTypedObject(android.hardware.security.authgraph.SessionInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
            public android.hardware.security.authgraph.Arc[] authenticationComplete(android.hardware.security.authgraph.SessionIdSignature peerSignature, android.hardware.security.authgraph.Arc[] sharedKeys) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(peerSignature, 0);
                    _data.writeFixedArray(sharedKeys, 0, 2);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method authenticationComplete is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.security.authgraph.Arc[] _result = (android.hardware.security.authgraph.Arc[]) _reply.createFixedArray(android.hardware.security.authgraph.Arc[].class, android.hardware.security.authgraph.Arc.CREATOR, 2);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
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

            @Override // android.hardware.security.authgraph.IAuthGraphKeyExchange
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.security.authgraph.IAuthGraphKeyExchange.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }
    }
}
