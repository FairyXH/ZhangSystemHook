package android.net;

/* JADX INFO: loaded from: classes.dex */
public interface INetworkMonitor extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$INetworkMonitor".replace('$', '.');
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int NETWORK_TEST_RESULT_INVALID = 1;
    public static final int NETWORK_TEST_RESULT_PARTIAL_CONNECTIVITY = 2;
    public static final int NETWORK_TEST_RESULT_VALID = 0;
    public static final int NETWORK_VALIDATION_PROBE_DNS = 4;
    public static final int NETWORK_VALIDATION_PROBE_FALLBACK = 32;
    public static final int NETWORK_VALIDATION_PROBE_HTTP = 8;
    public static final int NETWORK_VALIDATION_PROBE_HTTPS = 16;
    public static final int NETWORK_VALIDATION_PROBE_PRIVDNS = 64;
    public static final int NETWORK_VALIDATION_RESULT_PARTIAL = 2;
    public static final int NETWORK_VALIDATION_RESULT_SKIPPED = 4;
    public static final int NETWORK_VALIDATION_RESULT_VALID = 1;
    public static final int VERSION = 21;

    void forceReevaluation(int i) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void launchCaptivePortalApp() throws android.os.RemoteException;

    void notifyCaptivePortalAppFinished(int i) throws android.os.RemoteException;

    void notifyDnsResponse(int i) throws android.os.RemoteException;

    void notifyLinkPropertiesChanged(android.net.LinkProperties linkProperties) throws android.os.RemoteException;

    void notifyNetworkCapabilitiesChanged(android.net.NetworkCapabilities networkCapabilities) throws android.os.RemoteException;

    void notifyNetworkConnected(android.net.LinkProperties linkProperties, android.net.NetworkCapabilities networkCapabilities) throws android.os.RemoteException;

    void notifyNetworkConnectedParcel(android.net.networkstack.aidl.NetworkMonitorParameters networkMonitorParameters) throws android.os.RemoteException;

    void notifyNetworkDisconnected() throws android.os.RemoteException;

    void notifyPrivateDnsChanged(android.net.PrivateDnsConfigParcel privateDnsConfigParcel) throws android.os.RemoteException;

    void setAcceptPartialConnectivity() throws android.os.RemoteException;

    void start() throws android.os.RemoteException;

    public static class Default implements android.net.INetworkMonitor {
        @Override // android.net.INetworkMonitor
        public void start() throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void launchCaptivePortalApp() throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyCaptivePortalAppFinished(int response) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void setAcceptPartialConnectivity() throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void forceReevaluation(int uid) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyPrivateDnsChanged(android.net.PrivateDnsConfigParcel config) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyDnsResponse(int returnCode) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyNetworkConnected(android.net.LinkProperties lp, android.net.NetworkCapabilities nc) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyNetworkDisconnected() throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyLinkPropertiesChanged(android.net.LinkProperties lp) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyNetworkCapabilitiesChanged(android.net.NetworkCapabilities nc) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public void notifyNetworkConnectedParcel(android.net.networkstack.aidl.NetworkMonitorParameters params) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitor
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.INetworkMonitor
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.INetworkMonitor {
        static final int TRANSACTION_forceReevaluation = 5;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_launchCaptivePortalApp = 2;
        static final int TRANSACTION_notifyCaptivePortalAppFinished = 3;
        static final int TRANSACTION_notifyDnsResponse = 7;
        static final int TRANSACTION_notifyLinkPropertiesChanged = 10;
        static final int TRANSACTION_notifyNetworkCapabilitiesChanged = 11;
        static final int TRANSACTION_notifyNetworkConnected = 8;
        static final int TRANSACTION_notifyNetworkConnectedParcel = 12;
        static final int TRANSACTION_notifyNetworkDisconnected = 9;
        static final int TRANSACTION_notifyPrivateDnsChanged = 6;
        static final int TRANSACTION_setAcceptPartialConnectivity = 4;
        static final int TRANSACTION_start = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.INetworkMonitor asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.INetworkMonitor)) {
                return (android.net.INetworkMonitor) iin;
            }
            return new android.net.INetworkMonitor.Stub.Proxy(obj);
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
                    start();
                    return true;
                case 2:
                    launchCaptivePortalApp();
                    return true;
                case 3:
                    int _arg0 = data.readInt();
                    notifyCaptivePortalAppFinished(_arg0);
                    return true;
                case 4:
                    setAcceptPartialConnectivity();
                    return true;
                case 5:
                    int _arg02 = data.readInt();
                    forceReevaluation(_arg02);
                    return true;
                case 6:
                    android.net.PrivateDnsConfigParcel _arg03 = (android.net.PrivateDnsConfigParcel) data.readTypedObject(android.net.PrivateDnsConfigParcel.CREATOR);
                    notifyPrivateDnsChanged(_arg03);
                    return true;
                case 7:
                    int _arg04 = data.readInt();
                    notifyDnsResponse(_arg04);
                    return true;
                case 8:
                    android.net.LinkProperties _arg05 = (android.net.LinkProperties) data.readTypedObject(android.net.LinkProperties.CREATOR);
                    android.net.NetworkCapabilities _arg1 = (android.net.NetworkCapabilities) data.readTypedObject(android.net.NetworkCapabilities.CREATOR);
                    notifyNetworkConnected(_arg05, _arg1);
                    return true;
                case 9:
                    notifyNetworkDisconnected();
                    return true;
                case 10:
                    android.net.LinkProperties _arg06 = (android.net.LinkProperties) data.readTypedObject(android.net.LinkProperties.CREATOR);
                    notifyLinkPropertiesChanged(_arg06);
                    return true;
                case 11:
                    android.net.NetworkCapabilities _arg07 = (android.net.NetworkCapabilities) data.readTypedObject(android.net.NetworkCapabilities.CREATOR);
                    notifyNetworkCapabilitiesChanged(_arg07);
                    return true;
                case 12:
                    android.net.networkstack.aidl.NetworkMonitorParameters _arg08 = (android.net.networkstack.aidl.NetworkMonitorParameters) data.readTypedObject(android.net.networkstack.aidl.NetworkMonitorParameters.CREATOR);
                    notifyNetworkConnectedParcel(_arg08);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.INetworkMonitor {
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

            @Override // android.net.INetworkMonitor
            public void start() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method start is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void launchCaptivePortalApp() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method launchCaptivePortalApp is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyCaptivePortalAppFinished(int response) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(response);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyCaptivePortalAppFinished is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void setAcceptPartialConnectivity() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setAcceptPartialConnectivity is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void forceReevaluation(int uid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method forceReevaluation is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyPrivateDnsChanged(android.net.PrivateDnsConfigParcel config) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(config, 0);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyPrivateDnsChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyDnsResponse(int returnCode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(returnCode);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyDnsResponse is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyNetworkConnected(android.net.LinkProperties lp, android.net.NetworkCapabilities nc) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(lp, 0);
                    _data.writeTypedObject(nc, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyNetworkConnected is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyNetworkDisconnected() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyNetworkDisconnected is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyLinkPropertiesChanged(android.net.LinkProperties lp) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(lp, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyLinkPropertiesChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyNetworkCapabilitiesChanged(android.net.NetworkCapabilities nc) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(nc, 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyNetworkCapabilitiesChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
            public void notifyNetworkConnectedParcel(android.net.networkstack.aidl.NetworkMonitorParameters params) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(params, 0);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyNetworkConnectedParcel is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitor
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

            @Override // android.net.INetworkMonitor
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.INetworkMonitor.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
