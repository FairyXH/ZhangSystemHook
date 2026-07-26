package android.net.resolv.aidl;

/* JADX INFO: loaded from: classes.dex */
public interface IDnsResolverUnsolicitedEventListener extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$resolv$aidl$IDnsResolverUnsolicitedEventListener".replace('$', '.');
    public static final int DNS_HEALTH_RESULT_OK = 0;
    public static final int DNS_HEALTH_RESULT_TIMEOUT = 255;
    public static final java.lang.String HASH = "e6ef3246f1613151e9196c283abe55a544514c21";
    public static final int PREFIX_OPERATION_ADDED = 1;
    public static final int PREFIX_OPERATION_REMOVED = 2;
    public static final int VALIDATION_RESULT_FAILURE = 2;
    public static final int VALIDATION_RESULT_SUCCESS = 1;
    public static final int VERSION = 8;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onDnsHealthEvent(android.net.resolv.aidl.DnsHealthEventParcel dnsHealthEventParcel) throws android.os.RemoteException;

    void onNat64PrefixEvent(android.net.resolv.aidl.Nat64PrefixEventParcel nat64PrefixEventParcel) throws android.os.RemoteException;

    void onPrivateDnsValidationEvent(android.net.resolv.aidl.PrivateDnsValidationEventParcel privateDnsValidationEventParcel) throws android.os.RemoteException;

    public static class Default implements android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener {
        @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
        public void onDnsHealthEvent(android.net.resolv.aidl.DnsHealthEventParcel dnsHealthEvent) throws android.os.RemoteException {
        }

        @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
        public void onNat64PrefixEvent(android.net.resolv.aidl.Nat64PrefixEventParcel nat64PrefixEvent) throws android.os.RemoteException {
        }

        @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
        public void onPrivateDnsValidationEvent(android.net.resolv.aidl.PrivateDnsValidationEventParcel privateDnsValidationEvent) throws android.os.RemoteException {
        }

        @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onDnsHealthEvent = 1;
        static final int TRANSACTION_onNat64PrefixEvent = 2;
        static final int TRANSACTION_onPrivateDnsValidationEvent = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener)) {
                return (android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener) iin;
            }
            return new android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener.Stub.Proxy(obj);
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
                    android.net.resolv.aidl.DnsHealthEventParcel _arg0 = (android.net.resolv.aidl.DnsHealthEventParcel) data.readTypedObject(android.net.resolv.aidl.DnsHealthEventParcel.CREATOR);
                    onDnsHealthEvent(_arg0);
                    return true;
                case 2:
                    android.net.resolv.aidl.Nat64PrefixEventParcel _arg02 = (android.net.resolv.aidl.Nat64PrefixEventParcel) data.readTypedObject(android.net.resolv.aidl.Nat64PrefixEventParcel.CREATOR);
                    onNat64PrefixEvent(_arg02);
                    return true;
                case 3:
                    android.net.resolv.aidl.PrivateDnsValidationEventParcel _arg03 = (android.net.resolv.aidl.PrivateDnsValidationEventParcel) data.readTypedObject(android.net.resolv.aidl.PrivateDnsValidationEventParcel.CREATOR);
                    onPrivateDnsValidationEvent(_arg03);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener {
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

            @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
            public void onDnsHealthEvent(android.net.resolv.aidl.DnsHealthEventParcel dnsHealthEvent) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(dnsHealthEvent, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onDnsHealthEvent is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
            public void onNat64PrefixEvent(android.net.resolv.aidl.Nat64PrefixEventParcel nat64PrefixEvent) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(nat64PrefixEvent, 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onNat64PrefixEvent is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
            public void onPrivateDnsValidationEvent(android.net.resolv.aidl.PrivateDnsValidationEventParcel privateDnsValidationEvent) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(privateDnsValidationEvent, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onPrivateDnsValidationEvent is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
            public int getInterfaceVersion() throws android.os.RemoteException {
                if (this.mCachedVersion == -1) {
                    android.os.Parcel data = android.os.Parcel.obtain();
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

            @Override // android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
