package android.os;

/* JADX INFO: loaded from: classes.dex */
public interface IDumpstate extends android.os.IInterface {
    public static final int BUGREPORT_FLAG_DEFER_CONSENT = 2;
    public static final int BUGREPORT_FLAG_KEEP_BUGREPORT_ON_RETRIEVAL = 4;
    public static final int BUGREPORT_FLAG_USE_PREDUMPED_UI_DATA = 1;
    public static final int BUGREPORT_MODE_DEFAULT = 6;
    public static final int BUGREPORT_MODE_FULL = 0;
    public static final int BUGREPORT_MODE_INTERACTIVE = 1;
    public static final int BUGREPORT_MODE_ONBOARDING = 7;
    public static final int BUGREPORT_MODE_REMOTE = 2;
    public static final int BUGREPORT_MODE_TELEPHONY = 4;
    public static final int BUGREPORT_MODE_WEAR = 3;
    public static final int BUGREPORT_MODE_WIFI = 5;
    public static final java.lang.String DESCRIPTOR = "android.os.IDumpstate";

    void cancelBugreport(int i, java.lang.String str) throws android.os.RemoteException;

    void preDumpUiData(java.lang.String str) throws android.os.RemoteException;

    void retrieveBugreport(int i, java.lang.String str, int i2, java.io.FileDescriptor fileDescriptor, java.lang.String str2, boolean z, boolean z2, android.os.IDumpstateListener iDumpstateListener) throws android.os.RemoteException;

    void startBugreport(int i, java.lang.String str, java.io.FileDescriptor fileDescriptor, java.io.FileDescriptor fileDescriptor2, int i2, int i3, android.os.IDumpstateListener iDumpstateListener, boolean z, boolean z2) throws android.os.RemoteException;

    public static class Default implements android.os.IDumpstate {
        @Override // android.os.IDumpstate
        public void preDumpUiData(java.lang.String callingPackage) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstate
        public void startBugreport(int callingUid, java.lang.String callingPackage, java.io.FileDescriptor bugreportFd, java.io.FileDescriptor screenshotFd, int bugreportMode, int bugreportFlags, android.os.IDumpstateListener listener, boolean isScreenshotRequested, boolean skipUserConsent) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstate
        public void cancelBugreport(int callingUid, java.lang.String callingPackage) throws android.os.RemoteException {
        }

        @Override // android.os.IDumpstate
        public void retrieveBugreport(int callingUid, java.lang.String callingPackage, int userId, java.io.FileDescriptor bugreportFd, java.lang.String bugreportFile, boolean keepBugreportOnRetrieval, boolean skipUserConsent, android.os.IDumpstateListener listener) throws android.os.RemoteException {
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.os.IDumpstate {
        static final int TRANSACTION_cancelBugreport = 3;
        static final int TRANSACTION_preDumpUiData = 1;
        static final int TRANSACTION_retrieveBugreport = 4;
        static final int TRANSACTION_startBugreport = 2;

        public Stub() {
            attachInterface(this, android.os.IDumpstate.DESCRIPTOR);
        }

        public static android.os.IDumpstate asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(android.os.IDumpstate.DESCRIPTOR);
            if (iin != null && (iin instanceof android.os.IDumpstate)) {
                return (android.os.IDumpstate) iin;
            }
            return new android.os.IDumpstate.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (code >= 1 && code <= 16777215) {
                data.enforceInterface(android.os.IDumpstate.DESCRIPTOR);
            }
            if (code == 1598968902) {
                reply.writeString(android.os.IDumpstate.DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    java.lang.String _arg0 = data.readString();
                    data.enforceNoDataAvail();
                    preDumpUiData(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    java.lang.String _arg1 = data.readString();
                    java.io.FileDescriptor _arg2 = data.readRawFileDescriptor();
                    java.io.FileDescriptor _arg3 = data.readRawFileDescriptor();
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    android.os.IDumpstateListener _arg6 = android.os.IDumpstateListener.Stub.asInterface(data.readStrongBinder());
                    boolean _arg7 = data.readBoolean();
                    boolean _arg8 = data.readBoolean();
                    data.enforceNoDataAvail();
                    startBugreport(_arg02, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                    reply.writeNoException();
                    return true;
                case 3:
                    int _arg03 = data.readInt();
                    java.lang.String _arg12 = data.readString();
                    data.enforceNoDataAvail();
                    cancelBugreport(_arg03, _arg12);
                    reply.writeNoException();
                    return true;
                case 4:
                    int _arg04 = data.readInt();
                    java.lang.String _arg13 = data.readString();
                    int _arg22 = data.readInt();
                    java.io.FileDescriptor _arg32 = data.readRawFileDescriptor();
                    java.lang.String _arg42 = data.readString();
                    boolean _arg52 = data.readBoolean();
                    boolean _arg62 = data.readBoolean();
                    android.os.IDumpstateListener _arg72 = android.os.IDumpstateListener.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    retrieveBugreport(_arg04, _arg13, _arg22, _arg32, _arg42, _arg52, _arg62, _arg72);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.os.IDumpstate {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public android.os.IBinder asBinder() {
                return this.mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return android.os.IDumpstate.DESCRIPTOR;
            }

            @Override // android.os.IDumpstate
            public void preDumpUiData(java.lang.String callingPackage) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IDumpstate.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstate
            public void startBugreport(int callingUid, java.lang.String callingPackage, java.io.FileDescriptor bugreportFd, java.io.FileDescriptor screenshotFd, int bugreportMode, int bugreportFlags, android.os.IDumpstateListener listener, boolean isScreenshotRequested, boolean skipUserConsent) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IDumpstate.DESCRIPTOR);
                    _data.writeInt(callingUid);
                    _data.writeString(callingPackage);
                    _data.writeRawFileDescriptor(bugreportFd);
                    _data.writeRawFileDescriptor(screenshotFd);
                    _data.writeInt(bugreportMode);
                    _data.writeInt(bugreportFlags);
                    _data.writeStrongInterface(listener);
                    _data.writeBoolean(isScreenshotRequested);
                    _data.writeBoolean(skipUserConsent);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstate
            public void cancelBugreport(int callingUid, java.lang.String callingPackage) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IDumpstate.DESCRIPTOR);
                    _data.writeInt(callingUid);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IDumpstate
            public void retrieveBugreport(int callingUid, java.lang.String callingPackage, int userId, java.io.FileDescriptor bugreportFd, java.lang.String bugreportFile, boolean keepBugreportOnRetrieval, boolean skipUserConsent, android.os.IDumpstateListener listener) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(android.os.IDumpstate.DESCRIPTOR);
                    _data.writeInt(callingUid);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    _data.writeRawFileDescriptor(bugreportFd);
                    _data.writeString(bugreportFile);
                    _data.writeBoolean(keepBugreportOnRetrieval);
                    _data.writeBoolean(skipUserConsent);
                    _data.writeStrongInterface(listener);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
