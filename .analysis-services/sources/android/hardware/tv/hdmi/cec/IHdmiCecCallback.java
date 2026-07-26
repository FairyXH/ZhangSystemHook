package android.hardware.tv.hdmi.cec;

/* JADX INFO: loaded from: classes.dex */
public interface IHdmiCecCallback extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$tv$hdmi$cec$IHdmiCecCallback".replace('$', '.');
    public static final java.lang.String HASH = "cd956e3a0c2e6ade71693c85e9f0aeffa221ea26";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onCecMessage(android.hardware.tv.hdmi.cec.CecMessage cecMessage) throws android.os.RemoteException;

    public static class Default implements android.hardware.tv.hdmi.cec.IHdmiCecCallback {
        @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
        public void onCecMessage(android.hardware.tv.hdmi.cec.CecMessage message) throws android.os.RemoteException {
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.tv.hdmi.cec.IHdmiCecCallback {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onCecMessage = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.tv.hdmi.cec.IHdmiCecCallback asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.tv.hdmi.cec.IHdmiCecCallback)) {
                return (android.hardware.tv.hdmi.cec.IHdmiCecCallback) iin;
            }
            return new android.hardware.tv.hdmi.cec.IHdmiCecCallback.Stub.Proxy(obj);
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
                    android.hardware.tv.hdmi.cec.CecMessage _arg0 = (android.hardware.tv.hdmi.cec.CecMessage) data.readTypedObject(android.hardware.tv.hdmi.cec.CecMessage.CREATOR);
                    data.enforceNoDataAvail();
                    onCecMessage(_arg0);
                    break;
            }
            return true;
        }

        private static class Proxy implements android.hardware.tv.hdmi.cec.IHdmiCecCallback {
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

            @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
            public void onCecMessage(android.hardware.tv.hdmi.cec.CecMessage message) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(message, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onCecMessage is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
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

            @Override // android.hardware.tv.hdmi.cec.IHdmiCecCallback
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.tv.hdmi.cec.IHdmiCecCallback.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
