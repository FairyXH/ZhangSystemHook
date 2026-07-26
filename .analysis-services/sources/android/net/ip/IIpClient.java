package android.net.ip;

/* JADX INFO: loaded from: classes.dex */
public interface IIpClient extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$net$ip$IIpClient".replace('$', '.');
    public static final java.lang.String HASH = "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
    public static final int HOSTNAME_SETTING_DO_NOT_SEND = 2;
    public static final int HOSTNAME_SETTING_SEND = 1;
    public static final int HOSTNAME_SETTING_UNSET = 0;
    public static final int PROV_IPV4_DHCP = 2;
    public static final int PROV_IPV4_DISABLED = 0;
    public static final int PROV_IPV4_STATIC = 1;
    public static final int PROV_IPV6_DISABLED = 0;
    public static final int PROV_IPV6_LINKLOCAL = 2;
    public static final int PROV_IPV6_SLAAC = 1;
    public static final int VERSION = 21;

    void addKeepalivePacketFilter(int i, android.net.TcpKeepalivePacketDataParcelable tcpKeepalivePacketDataParcelable) throws android.os.RemoteException;

    void addNattKeepalivePacketFilter(int i, android.net.NattKeepalivePacketDataParcelable nattKeepalivePacketDataParcelable) throws android.os.RemoteException;

    void completedPreDhcpAction() throws android.os.RemoteException;

    void confirmConfiguration() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void notifyPreconnectionComplete(boolean z) throws android.os.RemoteException;

    void readPacketFilterComplete(byte[] bArr) throws android.os.RemoteException;

    void removeKeepalivePacketFilter(int i) throws android.os.RemoteException;

    void setHttpProxy(android.net.ProxyInfo proxyInfo) throws android.os.RemoteException;

    void setL2KeyAndGroupHint(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    void setMulticastFilter(boolean z) throws android.os.RemoteException;

    void setTcpBufferSizes(java.lang.String str) throws android.os.RemoteException;

    void shutdown() throws android.os.RemoteException;

    void startProvisioning(android.net.ProvisioningConfigurationParcelable provisioningConfigurationParcelable) throws android.os.RemoteException;

    void stop() throws android.os.RemoteException;

    void updateApfCapabilities(android.net.apf.ApfCapabilities apfCapabilities) throws android.os.RemoteException;

    void updateLayer2Information(android.net.Layer2InformationParcelable layer2InformationParcelable) throws android.os.RemoteException;

    public static class Default implements android.net.ip.IIpClient {
        @Override // android.net.ip.IIpClient
        public void completedPreDhcpAction() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void confirmConfiguration() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void readPacketFilterComplete(byte[] data) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void shutdown() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void startProvisioning(android.net.ProvisioningConfigurationParcelable req) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void stop() throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void setTcpBufferSizes(java.lang.String tcpBufferSizes) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void setHttpProxy(android.net.ProxyInfo proxyInfo) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void setMulticastFilter(boolean enabled) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void addKeepalivePacketFilter(int slot, android.net.TcpKeepalivePacketDataParcelable pkt) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void removeKeepalivePacketFilter(int slot) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void setL2KeyAndGroupHint(java.lang.String l2Key, java.lang.String cluster) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void addNattKeepalivePacketFilter(int slot, android.net.NattKeepalivePacketDataParcelable pkt) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void notifyPreconnectionComplete(boolean success) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void updateLayer2Information(android.net.Layer2InformationParcelable info) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public void updateApfCapabilities(android.net.apf.ApfCapabilities apfCapabilities) throws android.os.RemoteException {
        }

        @Override // android.net.ip.IIpClient
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.net.ip.IIpClient
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.net.ip.IIpClient {
        static final int TRANSACTION_addKeepalivePacketFilter = 10;
        static final int TRANSACTION_addNattKeepalivePacketFilter = 13;
        static final int TRANSACTION_completedPreDhcpAction = 1;
        static final int TRANSACTION_confirmConfiguration = 2;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_notifyPreconnectionComplete = 14;
        static final int TRANSACTION_readPacketFilterComplete = 3;
        static final int TRANSACTION_removeKeepalivePacketFilter = 11;
        static final int TRANSACTION_setHttpProxy = 8;
        static final int TRANSACTION_setL2KeyAndGroupHint = 12;
        static final int TRANSACTION_setMulticastFilter = 9;
        static final int TRANSACTION_setTcpBufferSizes = 7;
        static final int TRANSACTION_shutdown = 4;
        static final int TRANSACTION_startProvisioning = 5;
        static final int TRANSACTION_stop = 6;
        static final int TRANSACTION_updateApfCapabilities = 16;
        static final int TRANSACTION_updateLayer2Information = 15;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static android.net.ip.IIpClient asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.net.ip.IIpClient)) {
                return (android.net.ip.IIpClient) iin;
            }
            return new android.net.ip.IIpClient.Stub.Proxy(obj);
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
                    completedPreDhcpAction();
                    return true;
                case 2:
                    confirmConfiguration();
                    return true;
                case 3:
                    byte[] _arg0 = data.createByteArray();
                    readPacketFilterComplete(_arg0);
                    return true;
                case 4:
                    shutdown();
                    return true;
                case 5:
                    android.net.ProvisioningConfigurationParcelable _arg02 = (android.net.ProvisioningConfigurationParcelable) data.readTypedObject(android.net.ProvisioningConfigurationParcelable.CREATOR);
                    startProvisioning(_arg02);
                    return true;
                case 6:
                    stop();
                    return true;
                case 7:
                    java.lang.String _arg03 = data.readString();
                    setTcpBufferSizes(_arg03);
                    return true;
                case 8:
                    android.net.ProxyInfo _arg04 = (android.net.ProxyInfo) data.readTypedObject(android.net.ProxyInfo.CREATOR);
                    setHttpProxy(_arg04);
                    return true;
                case 9:
                    boolean _arg05 = data.readBoolean();
                    setMulticastFilter(_arg05);
                    return true;
                case 10:
                    int _arg06 = data.readInt();
                    android.net.TcpKeepalivePacketDataParcelable _arg1 = (android.net.TcpKeepalivePacketDataParcelable) data.readTypedObject(android.net.TcpKeepalivePacketDataParcelable.CREATOR);
                    addKeepalivePacketFilter(_arg06, _arg1);
                    return true;
                case 11:
                    int _arg07 = data.readInt();
                    removeKeepalivePacketFilter(_arg07);
                    return true;
                case 12:
                    java.lang.String _arg08 = data.readString();
                    java.lang.String _arg12 = data.readString();
                    setL2KeyAndGroupHint(_arg08, _arg12);
                    return true;
                case 13:
                    int _arg09 = data.readInt();
                    android.net.NattKeepalivePacketDataParcelable _arg13 = (android.net.NattKeepalivePacketDataParcelable) data.readTypedObject(android.net.NattKeepalivePacketDataParcelable.CREATOR);
                    addNattKeepalivePacketFilter(_arg09, _arg13);
                    return true;
                case 14:
                    boolean _arg010 = data.readBoolean();
                    notifyPreconnectionComplete(_arg010);
                    return true;
                case 15:
                    android.net.Layer2InformationParcelable _arg011 = (android.net.Layer2InformationParcelable) data.readTypedObject(android.net.Layer2InformationParcelable.CREATOR);
                    updateLayer2Information(_arg011);
                    return true;
                case 16:
                    android.net.apf.ApfCapabilities _arg012 = (android.net.apf.ApfCapabilities) data.readTypedObject(android.net.apf.ApfCapabilities.CREATOR);
                    updateApfCapabilities(_arg012);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.net.ip.IIpClient {
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

            @Override // android.net.ip.IIpClient
            public void completedPreDhcpAction() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method completedPreDhcpAction is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void confirmConfiguration() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method confirmConfiguration is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void readPacketFilterComplete(byte[] data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeByteArray(data);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method readPacketFilterComplete is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void shutdown() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method shutdown is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void startProvisioning(android.net.ProvisioningConfigurationParcelable req) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(req, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method startProvisioning is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void stop() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method stop is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void setTcpBufferSizes(java.lang.String tcpBufferSizes) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(tcpBufferSizes);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setTcpBufferSizes is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void setHttpProxy(android.net.ProxyInfo proxyInfo) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(proxyInfo, 0);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setHttpProxy is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void setMulticastFilter(boolean enabled) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(enabled);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setMulticastFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void addKeepalivePacketFilter(int slot, android.net.TcpKeepalivePacketDataParcelable pkt) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    _data.writeTypedObject(pkt, 0);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method addKeepalivePacketFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void removeKeepalivePacketFilter(int slot) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method removeKeepalivePacketFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void setL2KeyAndGroupHint(java.lang.String l2Key, java.lang.String cluster) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(l2Key);
                    _data.writeString(cluster);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setL2KeyAndGroupHint is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void addNattKeepalivePacketFilter(int slot, android.net.NattKeepalivePacketDataParcelable pkt) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(slot);
                    _data.writeTypedObject(pkt, 0);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method addNattKeepalivePacketFilter is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void notifyPreconnectionComplete(boolean success) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(success);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyPreconnectionComplete is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void updateLayer2Information(android.net.Layer2InformationParcelable info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method updateLayer2Information is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
            public void updateApfCapabilities(android.net.apf.ApfCapabilities apfCapabilities) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(apfCapabilities, 0);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method updateApfCapabilities is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.net.ip.IIpClient
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

            @Override // android.net.ip.IIpClient
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.net.ip.IIpClient.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
