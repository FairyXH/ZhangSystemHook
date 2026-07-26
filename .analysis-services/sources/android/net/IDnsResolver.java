package android.net;

/* JADX INFO: loaded from: classes.dex */
public interface IDnsResolver extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$IDnsResolver".replace('$', '.');
    public static final int DNS_RESOLVER_LOG_DEBUG = 1;
    public static final int DNS_RESOLVER_LOG_ERROR = 4;
    public static final int DNS_RESOLVER_LOG_INFO = 2;
    public static final int DNS_RESOLVER_LOG_VERBOSE = 0;
    public static final int DNS_RESOLVER_LOG_WARNING = 3;
    public static final java.lang.String HASH = "e6ef3246f1613151e9196c283abe55a544514c21";
    public static final int RESOLVER_PARAMS_BASE_TIMEOUT_MSEC = 4;
    public static final int RESOLVER_PARAMS_COUNT = 6;
    public static final int RESOLVER_PARAMS_MAX_SAMPLES = 3;
    public static final int RESOLVER_PARAMS_MIN_SAMPLES = 2;
    public static final int RESOLVER_PARAMS_RETRY_COUNT = 5;
    public static final int RESOLVER_PARAMS_SAMPLE_VALIDITY = 0;
    public static final int RESOLVER_PARAMS_SUCCESS_THRESHOLD = 1;
    public static final int RESOLVER_STATS_COUNT = 7;
    public static final int RESOLVER_STATS_ERRORS = 1;
    public static final int RESOLVER_STATS_INTERNAL_ERRORS = 3;
    public static final int RESOLVER_STATS_LAST_SAMPLE_TIME = 5;
    public static final int RESOLVER_STATS_RTT_AVG = 4;
    public static final int RESOLVER_STATS_SUCCESSES = 0;
    public static final int RESOLVER_STATS_TIMEOUTS = 2;
    public static final int RESOLVER_STATS_USABLE = 6;
    public static final int TC_MODE_DEFAULT = 0;
    public static final int TC_MODE_UDP_TCP = 1;
    public static final int TRANSPORT_BLUETOOTH = 2;
    public static final int TRANSPORT_CELLULAR = 0;
    public static final int TRANSPORT_ETHERNET = 3;
    public static final int TRANSPORT_LOWPAN = 6;
    public static final int TRANSPORT_TEST = 7;
    public static final int TRANSPORT_UNKNOWN = -1;
    public static final int TRANSPORT_USB = 8;
    public static final int TRANSPORT_VPN = 4;
    public static final int TRANSPORT_WIFI = 1;
    public static final int TRANSPORT_WIFI_AWARE = 5;
    public static final int VERSION = 8;

    void createNetworkCache(int i) throws android.os.RemoteException;

    void destroyNetworkCache(int i) throws android.os.RemoteException;

    void flushNetworkCache(int i) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    java.lang.String getPrefix64(int i) throws android.os.RemoteException;

    void getResolverInfo(int i, java.lang.String[] strArr, java.lang.String[] strArr2, java.lang.String[] strArr3, int[] iArr, int[] iArr2, int[] iArr3) throws android.os.RemoteException;

    boolean isAlive() throws android.os.RemoteException;

    void registerEventListener(android.net.metrics.INetdEventListener iNetdEventListener) throws android.os.RemoteException;

    void registerUnsolicitedEventListener(android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener iDnsResolverUnsolicitedEventListener) throws android.os.RemoteException;

    void setLogSeverity(int i) throws android.os.RemoteException;

    void setPrefix64(int i, java.lang.String str) throws android.os.RemoteException;

    void setResolverConfiguration(android.net.ResolverParamsParcel resolverParamsParcel) throws android.os.RemoteException;

    void startPrefix64Discovery(int i) throws android.os.RemoteException;

    void stopPrefix64Discovery(int i) throws android.os.RemoteException;

    public static class Default implements android.net.IDnsResolver {
        @Override // android.net.IDnsResolver
        public boolean isAlive() throws android.os.RemoteException {
            return false;
        }

        @Override // android.net.IDnsResolver
        public void registerEventListener(android.net.metrics.INetdEventListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void setResolverConfiguration(android.net.ResolverParamsParcel resolverParams) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void getResolverInfo(int netId, java.lang.String[] servers, java.lang.String[] domains, java.lang.String[] tlsServers, int[] params, int[] stats, int[] wait_for_pending_req_timeout_count) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void startPrefix64Discovery(int netId) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void stopPrefix64Discovery(int netId) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public java.lang.String getPrefix64(int netId) throws android.os.RemoteException {
            return null;
        }

        @Override // android.net.IDnsResolver
        public void createNetworkCache(int netId) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void destroyNetworkCache(int netId) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void setLogSeverity(int logSeverity) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void flushNetworkCache(int netId) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void setPrefix64(int netId, java.lang.String prefix) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public void registerUnsolicitedEventListener(android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener listener) throws android.os.RemoteException {
        }

        @Override // android.net.IDnsResolver
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.IDnsResolver
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.IDnsResolver {
        static final int TRANSACTION_createNetworkCache = 8;
        static final int TRANSACTION_destroyNetworkCache = 9;
        static final int TRANSACTION_flushNetworkCache = 11;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getPrefix64 = 7;
        static final int TRANSACTION_getResolverInfo = 4;
        static final int TRANSACTION_isAlive = 1;
        static final int TRANSACTION_registerEventListener = 2;
        static final int TRANSACTION_registerUnsolicitedEventListener = 13;
        static final int TRANSACTION_setLogSeverity = 10;
        static final int TRANSACTION_setPrefix64 = 12;
        static final int TRANSACTION_setResolverConfiguration = 3;
        static final int TRANSACTION_startPrefix64Discovery = 5;
        static final int TRANSACTION_stopPrefix64Discovery = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.IDnsResolver asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.IDnsResolver)) {
                return (android.net.IDnsResolver) iin;
            }
            return new android.net.IDnsResolver.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String[] _arg1;
            java.lang.String[] _arg2;
            java.lang.String[] _arg3;
            int[] _arg4;
            int[] _arg5;
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
                    boolean _result = isAlive();
                    reply.writeNoException();
                    reply.writeBoolean(_result);
                    break;
                case 2:
                    android.net.metrics.INetdEventListener _arg0 = android.net.metrics.INetdEventListener.Stub.asInterface(data.readStrongBinder());
                    registerEventListener(_arg0);
                    reply.writeNoException();
                    break;
                case 3:
                    android.net.ResolverParamsParcel _arg02 = (android.net.ResolverParamsParcel) data.readTypedObject(android.net.ResolverParamsParcel.CREATOR);
                    setResolverConfiguration(_arg02);
                    reply.writeNoException();
                    break;
                case 4:
                    int _arg03 = data.readInt();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        java.lang.String[] _arg12 = new java.lang.String[_arg1_length];
                        _arg1 = _arg12;
                    }
                    int _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        java.lang.String[] _arg22 = new java.lang.String[_arg2_length];
                        _arg2 = _arg22;
                    }
                    int _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        java.lang.String[] _arg32 = new java.lang.String[_arg3_length];
                        _arg3 = _arg32;
                    }
                    int _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        int[] _arg42 = new int[_arg4_length];
                        _arg4 = _arg42;
                    }
                    int _arg5_length = data.readInt();
                    if (_arg5_length < 0) {
                        _arg5 = null;
                    } else {
                        _arg5 = new int[_arg5_length];
                    }
                    int _arg6_length = data.readInt();
                    int[] _arg6 = _arg6_length < 0 ? null : new int[_arg6_length];
                    int[] _arg43 = _arg4;
                    java.lang.String[] _arg33 = _arg3;
                    java.lang.String[] _arg34 = _arg2;
                    getResolverInfo(_arg03, _arg1, _arg34, _arg33, _arg43, _arg5, _arg6);
                    reply.writeNoException();
                    reply.writeStringArray(_arg1);
                    reply.writeStringArray(_arg2);
                    reply.writeStringArray(_arg33);
                    reply.writeIntArray(_arg43);
                    reply.writeIntArray(_arg5);
                    reply.writeIntArray(_arg6);
                    break;
                case 5:
                    int _arg04 = data.readInt();
                    startPrefix64Discovery(_arg04);
                    reply.writeNoException();
                    break;
                case 6:
                    int _arg05 = data.readInt();
                    stopPrefix64Discovery(_arg05);
                    reply.writeNoException();
                    break;
                case 7:
                    int _arg06 = data.readInt();
                    java.lang.String _result2 = getPrefix64(_arg06);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    break;
                case 8:
                    int _arg07 = data.readInt();
                    createNetworkCache(_arg07);
                    reply.writeNoException();
                    break;
                case 9:
                    int _arg08 = data.readInt();
                    destroyNetworkCache(_arg08);
                    reply.writeNoException();
                    break;
                case 10:
                    int _arg09 = data.readInt();
                    setLogSeverity(_arg09);
                    reply.writeNoException();
                    break;
                case 11:
                    int _arg010 = data.readInt();
                    flushNetworkCache(_arg010);
                    reply.writeNoException();
                    break;
                case 12:
                    int _arg011 = data.readInt();
                    java.lang.String _arg13 = data.readString();
                    setPrefix64(_arg011, _arg13);
                    reply.writeNoException();
                    break;
                case 13:
                    android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener _arg012 = android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener.Stub.asInterface(data.readStrongBinder());
                    registerUnsolicitedEventListener(_arg012);
                    reply.writeNoException();
                    break;
            }
            return true;
        }

        private static class Proxy implements android.net.IDnsResolver {
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

            @Override // android.net.IDnsResolver
            public boolean isAlive() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isAlive is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void registerEventListener(android.net.metrics.INetdEventListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerEventListener is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void setResolverConfiguration(android.net.ResolverParamsParcel resolverParams) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(resolverParams, 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setResolverConfiguration is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void getResolverInfo(int netId, java.lang.String[] servers, java.lang.String[] domains, java.lang.String[] tlsServers, int[] params, int[] stats, int[] wait_for_pending_req_timeout_count) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeInt(servers.length);
                    _data.writeInt(domains.length);
                    _data.writeInt(tlsServers.length);
                    _data.writeInt(params.length);
                    _data.writeInt(stats.length);
                    _data.writeInt(wait_for_pending_req_timeout_count.length);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getResolverInfo is unimplemented.");
                    }
                    _reply.readException();
                    _reply.readStringArray(servers);
                    _reply.readStringArray(domains);
                    _reply.readStringArray(tlsServers);
                    _reply.readIntArray(params);
                    _reply.readIntArray(stats);
                    _reply.readIntArray(wait_for_pending_req_timeout_count);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void startPrefix64Discovery(int netId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method startPrefix64Discovery is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void stopPrefix64Discovery(int netId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method stopPrefix64Discovery is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public java.lang.String getPrefix64(int netId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPrefix64 is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void createNetworkCache(int netId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method createNetworkCache is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void destroyNetworkCache(int netId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method destroyNetworkCache is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void setLogSeverity(int logSeverity) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(logSeverity);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLogSeverity is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void flushNetworkCache(int netId) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method flushNetworkCache is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void setPrefix64(int netId, java.lang.String prefix) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(netId);
                    _data.writeString(prefix);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setPrefix64 is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
            public void registerUnsolicitedEventListener(android.net.resolv.aidl.IDnsResolverUnsolicitedEventListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerUnsolicitedEventListener is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IDnsResolver
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

            @Override // android.net.IDnsResolver
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.IDnsResolver.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
