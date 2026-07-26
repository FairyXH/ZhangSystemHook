package android.net;

/* JADX INFO: loaded from: classes.dex */
public interface INetdUnsolicitedEventListener extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$INetdUnsolicitedEventListener".replace('$', '.');
    public static final java.lang.String HASH = "2be6ff6fb01645cdddb3bb60f6de5727e5733267";
    public static final int VERSION = 15;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onInterfaceAdded(java.lang.String str) throws android.os.RemoteException;

    void onInterfaceAddressRemoved(java.lang.String str, java.lang.String str2, int i, int i2) throws android.os.RemoteException;

    void onInterfaceAddressUpdated(java.lang.String str, java.lang.String str2, int i, int i2) throws android.os.RemoteException;

    void onInterfaceChanged(java.lang.String str, boolean z) throws android.os.RemoteException;

    void onInterfaceClassActivityChanged(boolean z, int i, long j, int i2) throws android.os.RemoteException;

    void onInterfaceDnsServerInfo(java.lang.String str, long j, java.lang.String[] strArr) throws android.os.RemoteException;

    void onInterfaceLinkStateChanged(java.lang.String str, boolean z) throws android.os.RemoteException;

    void onInterfaceRemoved(java.lang.String str) throws android.os.RemoteException;

    void onQuotaLimitReached(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void onRouteChanged(boolean z, java.lang.String str, java.lang.String str2, java.lang.String str3) throws android.os.RemoteException;

    void onStrictCleartextDetected(int i, java.lang.String str) throws android.os.RemoteException;

    public static class Default implements android.net.INetdUnsolicitedEventListener {
        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceClassActivityChanged(boolean isActive, int timerLabel, long timestampNs, int uid) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onQuotaLimitReached(java.lang.String alertName, java.lang.String ifName) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceDnsServerInfo(java.lang.String ifName, long lifetimeS, java.lang.String[] servers) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceAddressUpdated(java.lang.String addr, java.lang.String ifName, int flags, int scope) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceAddressRemoved(java.lang.String addr, java.lang.String ifName, int flags, int scope) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceAdded(java.lang.String ifName) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceRemoved(java.lang.String ifName) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceChanged(java.lang.String ifName, boolean up) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onInterfaceLinkStateChanged(java.lang.String ifName, boolean up) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onRouteChanged(boolean updated, java.lang.String route, java.lang.String gateway, java.lang.String ifName) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public void onStrictCleartextDetected(int uid, java.lang.String hex) throws android.os.RemoteException {
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.INetdUnsolicitedEventListener
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.INetdUnsolicitedEventListener {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onInterfaceAdded = 6;
        static final int TRANSACTION_onInterfaceAddressRemoved = 5;
        static final int TRANSACTION_onInterfaceAddressUpdated = 4;
        static final int TRANSACTION_onInterfaceChanged = 8;
        static final int TRANSACTION_onInterfaceClassActivityChanged = 1;
        static final int TRANSACTION_onInterfaceDnsServerInfo = 3;
        static final int TRANSACTION_onInterfaceLinkStateChanged = 9;
        static final int TRANSACTION_onInterfaceRemoved = 7;
        static final int TRANSACTION_onQuotaLimitReached = 2;
        static final int TRANSACTION_onRouteChanged = 10;
        static final int TRANSACTION_onStrictCleartextDetected = 11;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.INetdUnsolicitedEventListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.INetdUnsolicitedEventListener)) {
                return (android.net.INetdUnsolicitedEventListener) iin;
            }
            return new android.net.INetdUnsolicitedEventListener.Stub.Proxy(obj);
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
                    boolean _arg0 = data.readBoolean();
                    int _arg1 = data.readInt();
                    long _arg2 = data.readLong();
                    int _arg3 = data.readInt();
                    onInterfaceClassActivityChanged(_arg0, _arg1, _arg2, _arg3);
                    return true;
                case 2:
                    java.lang.String _arg02 = data.readString();
                    java.lang.String _arg12 = data.readString();
                    onQuotaLimitReached(_arg02, _arg12);
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    long _arg13 = data.readLong();
                    java.lang.String[] _arg22 = data.createStringArray();
                    onInterfaceDnsServerInfo(_arg03, _arg13, _arg22);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    java.lang.String _arg14 = data.readString();
                    int _arg23 = data.readInt();
                    int _arg32 = data.readInt();
                    onInterfaceAddressUpdated(_arg04, _arg14, _arg23, _arg32);
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    java.lang.String _arg15 = data.readString();
                    int _arg24 = data.readInt();
                    int _arg33 = data.readInt();
                    onInterfaceAddressRemoved(_arg05, _arg15, _arg24, _arg33);
                    return true;
                case 6:
                    java.lang.String _arg06 = data.readString();
                    onInterfaceAdded(_arg06);
                    return true;
                case 7:
                    java.lang.String _arg07 = data.readString();
                    onInterfaceRemoved(_arg07);
                    return true;
                case 8:
                    java.lang.String _arg08 = data.readString();
                    boolean _arg16 = data.readBoolean();
                    onInterfaceChanged(_arg08, _arg16);
                    return true;
                case 9:
                    java.lang.String _arg09 = data.readString();
                    boolean _arg17 = data.readBoolean();
                    onInterfaceLinkStateChanged(_arg09, _arg17);
                    return true;
                case 10:
                    boolean _arg010 = data.readBoolean();
                    java.lang.String _arg18 = data.readString();
                    java.lang.String _arg25 = data.readString();
                    java.lang.String _arg34 = data.readString();
                    onRouteChanged(_arg010, _arg18, _arg25, _arg34);
                    return true;
                case 11:
                    int _arg011 = data.readInt();
                    java.lang.String _arg19 = data.readString();
                    onStrictCleartextDetected(_arg011, _arg19);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.INetdUnsolicitedEventListener {
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

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceClassActivityChanged(boolean isActive, int timerLabel, long timestampNs, int uid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(isActive);
                    _data.writeInt(timerLabel);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceClassActivityChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onQuotaLimitReached(java.lang.String alertName, java.lang.String ifName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(alertName);
                    _data.writeString(ifName);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onQuotaLimitReached is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceDnsServerInfo(java.lang.String ifName, long lifetimeS, java.lang.String[] servers) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    _data.writeLong(lifetimeS);
                    _data.writeStringArray(servers);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceDnsServerInfo is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceAddressUpdated(java.lang.String addr, java.lang.String ifName, int flags, int scope) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(addr);
                    _data.writeString(ifName);
                    _data.writeInt(flags);
                    _data.writeInt(scope);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceAddressUpdated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceAddressRemoved(java.lang.String addr, java.lang.String ifName, int flags, int scope) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(addr);
                    _data.writeString(ifName);
                    _data.writeInt(flags);
                    _data.writeInt(scope);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceAddressRemoved is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceAdded(java.lang.String ifName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceAdded is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceRemoved(java.lang.String ifName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceRemoved is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceChanged(java.lang.String ifName, boolean up) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    _data.writeBoolean(up);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onInterfaceLinkStateChanged(java.lang.String ifName, boolean up) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(ifName);
                    _data.writeBoolean(up);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onInterfaceLinkStateChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onRouteChanged(boolean updated, java.lang.String route, java.lang.String gateway, java.lang.String ifName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(updated);
                    _data.writeString(route);
                    _data.writeString(gateway);
                    _data.writeString(ifName);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onRouteChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
            public void onStrictCleartextDetected(int uid, java.lang.String hex) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(hex);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onStrictCleartextDetected is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetdUnsolicitedEventListener
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

            @Override // android.net.INetdUnsolicitedEventListener
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.INetdUnsolicitedEventListener.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
