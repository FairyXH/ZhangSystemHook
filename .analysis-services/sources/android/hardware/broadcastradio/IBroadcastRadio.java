package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface IBroadcastRadio extends android.os.IInterface {
    public static final int ANTENNA_STATE_CHANGE_TIMEOUT_MS = 100;
    public static final java.lang.String DESCRIPTOR = "android$hardware$broadcastradio$IBroadcastRadio".replace('$', '.');
    public static final java.lang.String HASH = "bff68a8bc8b7cc191ab62bee10f7df8e79494467";
    public static final int INVALID_IMAGE = 0;
    public static final int LIST_COMPLETE_TIMEOUT_MS = 300000;
    public static final int TUNER_TIMEOUT_MS = 30000;
    public static final int VERSION = 2;

    void cancel() throws android.os.RemoteException;

    android.hardware.broadcastradio.AmFmRegionConfig getAmFmRegionConfig(boolean z) throws android.os.RemoteException;

    android.hardware.broadcastradio.DabTableEntry[] getDabRegionConfig() throws android.os.RemoteException;

    byte[] getImage(int i) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    android.hardware.broadcastradio.VendorKeyValue[] getParameters(java.lang.String[] strArr) throws android.os.RemoteException;

    android.hardware.broadcastradio.Properties getProperties() throws android.os.RemoteException;

    boolean isConfigFlagSet(int i) throws android.os.RemoteException;

    android.hardware.broadcastradio.ICloseHandle registerAnnouncementListener(android.hardware.broadcastradio.IAnnouncementListener iAnnouncementListener, byte[] bArr) throws android.os.RemoteException;

    void seek(boolean z, boolean z2) throws android.os.RemoteException;

    void setConfigFlag(int i, boolean z) throws android.os.RemoteException;

    android.hardware.broadcastradio.VendorKeyValue[] setParameters(android.hardware.broadcastradio.VendorKeyValue[] vendorKeyValueArr) throws android.os.RemoteException;

    void setTunerCallback(android.hardware.broadcastradio.ITunerCallback iTunerCallback) throws android.os.RemoteException;

    void startProgramListUpdates(android.hardware.broadcastradio.ProgramFilter programFilter) throws android.os.RemoteException;

    void step(boolean z) throws android.os.RemoteException;

    void stopProgramListUpdates() throws android.os.RemoteException;

    void tune(android.hardware.broadcastradio.ProgramSelector programSelector) throws android.os.RemoteException;

    void unsetTunerCallback() throws android.os.RemoteException;

    public static class Default implements android.hardware.broadcastradio.IBroadcastRadio {
        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public android.hardware.broadcastradio.Properties getProperties() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public android.hardware.broadcastradio.AmFmRegionConfig getAmFmRegionConfig(boolean full) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public android.hardware.broadcastradio.DabTableEntry[] getDabRegionConfig() throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void setTunerCallback(android.hardware.broadcastradio.ITunerCallback callback) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void unsetTunerCallback() throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void tune(android.hardware.broadcastradio.ProgramSelector program) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void seek(boolean directionUp, boolean skipSubChannel) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void step(boolean directionUp) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void cancel() throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void startProgramListUpdates(android.hardware.broadcastradio.ProgramFilter filter) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void stopProgramListUpdates() throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public boolean isConfigFlagSet(int flag) throws android.os.RemoteException {
            return false;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public void setConfigFlag(int flag, boolean value) throws android.os.RemoteException {
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public android.hardware.broadcastradio.VendorKeyValue[] setParameters(android.hardware.broadcastradio.VendorKeyValue[] parameters) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public android.hardware.broadcastradio.VendorKeyValue[] getParameters(java.lang.String[] keys) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public byte[] getImage(int id) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public android.hardware.broadcastradio.ICloseHandle registerAnnouncementListener(android.hardware.broadcastradio.IAnnouncementListener listener, byte[] enabled) throws android.os.RemoteException {
            return null;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // android.hardware.broadcastradio.IBroadcastRadio
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements android.hardware.broadcastradio.IBroadcastRadio {
        static final int TRANSACTION_cancel = 9;
        static final int TRANSACTION_getAmFmRegionConfig = 2;
        static final int TRANSACTION_getDabRegionConfig = 3;
        static final int TRANSACTION_getImage = 16;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getParameters = 15;
        static final int TRANSACTION_getProperties = 1;
        static final int TRANSACTION_isConfigFlagSet = 12;
        static final int TRANSACTION_registerAnnouncementListener = 17;
        static final int TRANSACTION_seek = 7;
        static final int TRANSACTION_setConfigFlag = 13;
        static final int TRANSACTION_setParameters = 14;
        static final int TRANSACTION_setTunerCallback = 4;
        static final int TRANSACTION_startProgramListUpdates = 10;
        static final int TRANSACTION_step = 8;
        static final int TRANSACTION_stopProgramListUpdates = 11;
        static final int TRANSACTION_tune = 6;
        static final int TRANSACTION_unsetTunerCallback = 5;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static android.hardware.broadcastradio.IBroadcastRadio asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof android.hardware.broadcastradio.IBroadcastRadio)) {
                return (android.hardware.broadcastradio.IBroadcastRadio) iin;
            }
            return new android.hardware.broadcastradio.IBroadcastRadio.Stub.Proxy(obj);
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
                    android.hardware.broadcastradio.Properties _result = getProperties();
                    reply.writeNoException();
                    reply.writeTypedObject(_result, 1);
                    return true;
                case 2:
                    boolean _arg0 = data.readBoolean();
                    data.enforceNoDataAvail();
                    android.hardware.broadcastradio.AmFmRegionConfig _result2 = getAmFmRegionConfig(_arg0);
                    reply.writeNoException();
                    reply.writeTypedObject(_result2, 1);
                    return true;
                case 3:
                    android.hardware.broadcastradio.DabTableEntry[] _result3 = getDabRegionConfig();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                case 4:
                    android.hardware.broadcastradio.ITunerCallback _arg02 = android.hardware.broadcastradio.ITunerCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    setTunerCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 5:
                    unsetTunerCallback();
                    reply.writeNoException();
                    return true;
                case 6:
                    android.hardware.broadcastradio.ProgramSelector _arg03 = (android.hardware.broadcastradio.ProgramSelector) data.readTypedObject(android.hardware.broadcastradio.ProgramSelector.CREATOR);
                    data.enforceNoDataAvail();
                    tune(_arg03);
                    reply.writeNoException();
                    return true;
                case 7:
                    boolean _arg04 = data.readBoolean();
                    boolean _arg1 = data.readBoolean();
                    data.enforceNoDataAvail();
                    seek(_arg04, _arg1);
                    reply.writeNoException();
                    return true;
                case 8:
                    boolean _arg05 = data.readBoolean();
                    data.enforceNoDataAvail();
                    step(_arg05);
                    reply.writeNoException();
                    return true;
                case 9:
                    cancel();
                    reply.writeNoException();
                    return true;
                case 10:
                    android.hardware.broadcastradio.ProgramFilter _arg06 = (android.hardware.broadcastradio.ProgramFilter) data.readTypedObject(android.hardware.broadcastradio.ProgramFilter.CREATOR);
                    data.enforceNoDataAvail();
                    startProgramListUpdates(_arg06);
                    reply.writeNoException();
                    return true;
                case 11:
                    stopProgramListUpdates();
                    reply.writeNoException();
                    return true;
                case 12:
                    int _arg07 = data.readInt();
                    data.enforceNoDataAvail();
                    boolean _result4 = isConfigFlagSet(_arg07);
                    reply.writeNoException();
                    reply.writeBoolean(_result4);
                    return true;
                case 13:
                    int _arg08 = data.readInt();
                    boolean _arg12 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setConfigFlag(_arg08, _arg12);
                    reply.writeNoException();
                    return true;
                case 14:
                    android.hardware.broadcastradio.VendorKeyValue[] _arg09 = (android.hardware.broadcastradio.VendorKeyValue[]) data.createTypedArray(android.hardware.broadcastradio.VendorKeyValue.CREATOR);
                    data.enforceNoDataAvail();
                    android.hardware.broadcastradio.VendorKeyValue[] _result5 = setParameters(_arg09);
                    reply.writeNoException();
                    reply.writeTypedArray(_result5, 1);
                    return true;
                case 15:
                    java.lang.String[] _arg010 = data.createStringArray();
                    data.enforceNoDataAvail();
                    android.hardware.broadcastradio.VendorKeyValue[] _result6 = getParameters(_arg010);
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                case 16:
                    int _arg011 = data.readInt();
                    data.enforceNoDataAvail();
                    byte[] _result7 = getImage(_arg011);
                    reply.writeNoException();
                    reply.writeByteArray(_result7);
                    return true;
                case 17:
                    android.hardware.broadcastradio.IAnnouncementListener _arg012 = android.hardware.broadcastradio.IAnnouncementListener.Stub.asInterface(data.readStrongBinder());
                    byte[] _arg13 = data.createByteArray();
                    data.enforceNoDataAvail();
                    android.hardware.broadcastradio.ICloseHandle _result8 = registerAnnouncementListener(_arg012, _arg13);
                    reply.writeNoException();
                    reply.writeStrongInterface(_result8);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements android.hardware.broadcastradio.IBroadcastRadio {
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

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public android.hardware.broadcastradio.Properties getProperties() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getProperties is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.broadcastradio.Properties _result = (android.hardware.broadcastradio.Properties) _reply.readTypedObject(android.hardware.broadcastradio.Properties.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public android.hardware.broadcastradio.AmFmRegionConfig getAmFmRegionConfig(boolean full) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(full);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getAmFmRegionConfig is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.broadcastradio.AmFmRegionConfig _result = (android.hardware.broadcastradio.AmFmRegionConfig) _reply.readTypedObject(android.hardware.broadcastradio.AmFmRegionConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public android.hardware.broadcastradio.DabTableEntry[] getDabRegionConfig() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getDabRegionConfig is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.broadcastradio.DabTableEntry[] _result = (android.hardware.broadcastradio.DabTableEntry[]) _reply.createTypedArray(android.hardware.broadcastradio.DabTableEntry.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void setTunerCallback(android.hardware.broadcastradio.ITunerCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setTunerCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void unsetTunerCallback() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method unsetTunerCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void tune(android.hardware.broadcastradio.ProgramSelector program) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(program, 0);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method tune is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void seek(boolean directionUp, boolean skipSubChannel) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(directionUp);
                    _data.writeBoolean(skipSubChannel);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method seek is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void step(boolean directionUp) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(directionUp);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method step is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void cancel() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method cancel is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void startProgramListUpdates(android.hardware.broadcastradio.ProgramFilter filter) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(filter, 0);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method startProgramListUpdates is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void stopProgramListUpdates() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method stopProgramListUpdates is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public boolean isConfigFlagSet(int flag) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(flag);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method isConfigFlagSet is unimplemented.");
                    }
                    _reply.readException();
                    boolean _result = _reply.readBoolean();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public void setConfigFlag(int flag, boolean value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(flag);
                    _data.writeBoolean(value);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setConfigFlag is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public android.hardware.broadcastradio.VendorKeyValue[] setParameters(android.hardware.broadcastradio.VendorKeyValue[] parameters) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedArray(parameters, 0);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setParameters is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.broadcastradio.VendorKeyValue[] _result = (android.hardware.broadcastradio.VendorKeyValue[]) _reply.createTypedArray(android.hardware.broadcastradio.VendorKeyValue.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public android.hardware.broadcastradio.VendorKeyValue[] getParameters(java.lang.String[] keys) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStringArray(keys);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getParameters is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.broadcastradio.VendorKeyValue[] _result = (android.hardware.broadcastradio.VendorKeyValue[]) _reply.createTypedArray(android.hardware.broadcastradio.VendorKeyValue.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public byte[] getImage(int id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(id);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getImage is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public android.hardware.broadcastradio.ICloseHandle registerAnnouncementListener(android.hardware.broadcastradio.IAnnouncementListener listener, byte[] enabled) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongInterface(listener);
                    _data.writeByteArray(enabled);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerAnnouncementListener is unimplemented.");
                    }
                    _reply.readException();
                    android.hardware.broadcastradio.ICloseHandle _result = android.hardware.broadcastradio.ICloseHandle.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.broadcastradio.IBroadcastRadio
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

            @Override // android.hardware.broadcastradio.IBroadcastRadio
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(android.hardware.broadcastradio.IBroadcastRadio.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
