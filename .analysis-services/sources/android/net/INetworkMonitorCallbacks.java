package android.net;

/* JADX INFO: loaded from: classes.dex */
public interface INetworkMonitorCallbacks extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$INetworkMonitorCallbacks".replace('$', '.');
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int VERSION = 21;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void hideProvisioningNotification() throws android.os.RemoteException;

    void notifyCaptivePortalDataChanged(android.net.CaptivePortalData captivePortalData) throws android.os.RemoteException;

    void notifyDataStallSuspected(android.net.DataStallReportParcelable dataStallReportParcelable) throws android.os.RemoteException;

    void notifyNetworkTested(int i, java.lang.String str) throws android.os.RemoteException;

    void notifyNetworkTestedWithExtras(android.net.NetworkTestResultParcelable networkTestResultParcelable) throws android.os.RemoteException;

    void notifyPrivateDnsConfigResolved(android.net.PrivateDnsConfigParcel privateDnsConfigParcel) throws android.os.RemoteException;

    void notifyProbeStatusChanged(int i, int i2) throws android.os.RemoteException;

    void onNetworkMonitorCreated(android.net.INetworkMonitor iNetworkMonitor) throws android.os.RemoteException;

    void showProvisioningNotification(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    public static class Default implements android.net.INetworkMonitorCallbacks {
        @Override // android.net.INetworkMonitorCallbacks
        public void onNetworkMonitorCreated(android.net.INetworkMonitor networkMonitor) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void notifyNetworkTested(int testResult, java.lang.String redirectUrl) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void notifyPrivateDnsConfigResolved(android.net.PrivateDnsConfigParcel config) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void showProvisioningNotification(java.lang.String action, java.lang.String packageName) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void hideProvisioningNotification() throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void notifyProbeStatusChanged(int probesCompleted, int probesSucceeded) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void notifyNetworkTestedWithExtras(android.net.NetworkTestResultParcelable result) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void notifyDataStallSuspected(android.net.DataStallReportParcelable report) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public void notifyCaptivePortalDataChanged(android.net.CaptivePortalData data) throws android.os.RemoteException {
        }

        @Override // android.net.INetworkMonitorCallbacks
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.INetworkMonitorCallbacks
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.INetworkMonitorCallbacks {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_hideProvisioningNotification = 5;
        static final int TRANSACTION_notifyCaptivePortalDataChanged = 9;
        static final int TRANSACTION_notifyDataStallSuspected = 8;
        static final int TRANSACTION_notifyNetworkTested = 2;
        static final int TRANSACTION_notifyNetworkTestedWithExtras = 7;
        static final int TRANSACTION_notifyPrivateDnsConfigResolved = 3;
        static final int TRANSACTION_notifyProbeStatusChanged = 6;
        static final int TRANSACTION_onNetworkMonitorCreated = 1;
        static final int TRANSACTION_showProvisioningNotification = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.INetworkMonitorCallbacks asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.INetworkMonitorCallbacks)) {
                return (android.net.INetworkMonitorCallbacks) iin;
            }
            return new android.net.INetworkMonitorCallbacks.Stub.Proxy(obj);
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
                    android.net.INetworkMonitor _arg0 = android.net.INetworkMonitor.Stub.asInterface(data.readStrongBinder());
                    onNetworkMonitorCreated(_arg0);
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    java.lang.String _arg1 = data.readString();
                    notifyNetworkTested(_arg02, _arg1);
                    return true;
                case 3:
                    android.net.PrivateDnsConfigParcel _arg03 = (android.net.PrivateDnsConfigParcel) data.readTypedObject(android.net.PrivateDnsConfigParcel.CREATOR);
                    notifyPrivateDnsConfigResolved(_arg03);
                    return true;
                case 4:
                    java.lang.String _arg04 = data.readString();
                    java.lang.String _arg12 = data.readString();
                    showProvisioningNotification(_arg04, _arg12);
                    return true;
                case 5:
                    hideProvisioningNotification();
                    return true;
                case 6:
                    int _arg05 = data.readInt();
                    int _arg13 = data.readInt();
                    notifyProbeStatusChanged(_arg05, _arg13);
                    return true;
                case 7:
                    android.net.NetworkTestResultParcelable _arg06 = (android.net.NetworkTestResultParcelable) data.readTypedObject(android.net.NetworkTestResultParcelable.CREATOR);
                    notifyNetworkTestedWithExtras(_arg06);
                    return true;
                case 8:
                    android.net.DataStallReportParcelable _arg07 = (android.net.DataStallReportParcelable) data.readTypedObject(android.net.DataStallReportParcelable.CREATOR);
                    notifyDataStallSuspected(_arg07);
                    return true;
                case 9:
                    android.net.CaptivePortalData _arg08 = (android.net.CaptivePortalData) data.readTypedObject(android.net.CaptivePortalData.CREATOR);
                    notifyCaptivePortalDataChanged(_arg08);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.INetworkMonitorCallbacks {
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

            @Override // android.net.INetworkMonitorCallbacks
            public void onNetworkMonitorCreated(android.net.INetworkMonitor networkMonitor) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(networkMonitor);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onNetworkMonitorCreated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void notifyNetworkTested(int testResult, java.lang.String redirectUrl) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(testResult);
                    _data.writeString(redirectUrl);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyNetworkTested is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void notifyPrivateDnsConfigResolved(android.net.PrivateDnsConfigParcel config) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(config, 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyPrivateDnsConfigResolved is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void showProvisioningNotification(java.lang.String action, java.lang.String packageName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method showProvisioningNotification is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void hideProvisioningNotification() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method hideProvisioningNotification is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void notifyProbeStatusChanged(int probesCompleted, int probesSucceeded) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(probesCompleted);
                    _data.writeInt(probesSucceeded);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyProbeStatusChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void notifyNetworkTestedWithExtras(android.net.NetworkTestResultParcelable result) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(result, 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyNetworkTestedWithExtras is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void notifyDataStallSuspected(android.net.DataStallReportParcelable report) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(report, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyDataStallSuspected is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
            public void notifyCaptivePortalDataChanged(android.net.CaptivePortalData data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(data, 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyCaptivePortalDataChanged is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkMonitorCallbacks
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

            @Override // android.net.INetworkMonitorCallbacks
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.INetworkMonitorCallbacks.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
