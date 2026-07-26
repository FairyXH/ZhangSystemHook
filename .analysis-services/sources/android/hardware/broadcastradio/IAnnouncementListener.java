package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface IAnnouncementListener extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "android$hardware$broadcastradio$IAnnouncementListener".replace('$', '.');
    public static final java.lang.String HASH = "bff68a8bc8b7cc191ab62bee10f7df8e79494467";
    public static final int VERSION = 2;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void onListUpdated(android.hardware.broadcastradio.Announcement[] announcementArr) throws android.os.RemoteException;

    public static class Default implements android.hardware.broadcastradio.IAnnouncementListener {
        @Override // android.hardware.broadcastradio.IAnnouncementListener
        public void onListUpdated(android.hardware.broadcastradio.Announcement[] announcements) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IAnnouncementListener
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.broadcastradio.IAnnouncementListener
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.broadcastradio.IAnnouncementListener {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_onListUpdated = 1;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.broadcastradio.IAnnouncementListener asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.broadcastradio.IAnnouncementListener)) {
                return (android.hardware.broadcastradio.IAnnouncementListener) iin;
            }
            return new android.hardware.broadcastradio.IAnnouncementListener.Stub.Proxy(obj);
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
                    android.hardware.broadcastradio.Announcement[] _arg0 = (android.hardware.broadcastradio.Announcement[]) data.createTypedArray(android.hardware.broadcastradio.Announcement.CREATOR);
                    data.enforceNoDataAvail();
                    onListUpdated(_arg0);
                    break;
            }
            return true;
        }

        private static class Proxy implements android.hardware.broadcastradio.IAnnouncementListener {
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

            @Override // android.hardware.broadcastradio.IAnnouncementListener
            public void onListUpdated(android.hardware.broadcastradio.Announcement[] announcements) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(announcements, 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status) {
                        throw new android.os.RemoteException("Method onListUpdated is unimplemented.");
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IAnnouncementListener
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

            @Override // android.hardware.broadcastradio.IAnnouncementListener
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.broadcastradio.IAnnouncementListener.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
