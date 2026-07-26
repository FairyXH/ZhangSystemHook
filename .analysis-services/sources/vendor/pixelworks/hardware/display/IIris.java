package vendor.pixelworks.hardware.display;

/* JADX INFO: loaded from: classes4.dex */
public interface IIris extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$pixelworks$hardware$display$IIris".replace('$', '.');
    public static final java.lang.String HASH = "02c8c5526cbde39f502b3bf8cccaf196c81de25f";
    public static final int VERSION = 1;

    void buildLayerStack(long j, vendor.pixelworks.hardware.display.LayerStack layerStack) throws android.os.RemoteException;

    void changeLayerType(long j, long j2) throws android.os.RemoteException;

    int commitLayerStack(long j, int i) throws android.os.RemoteException;

    void configureIrisHdrMode(int i) throws android.os.RemoteException;

    int configureIrisMaxcll(int i) throws android.os.RemoteException;

    void createLayer(long j, long j2) throws android.os.RemoteException;

    void destroyLayer(long j, long j2) throws android.os.RemoteException;

    void enableSecondaryDisplay(boolean z) throws android.os.RemoteException;

    vendor.pixelworks.hardware.display.IrisFixedConfig getCurrentConfig(long j) throws android.os.RemoteException;

    java.lang.String getDumpString(long j) throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    vendor.pixelworks.hardware.display.LutData getLayerToneMappingLut(long j, int i) throws android.os.RemoteException;

    int getOsdStatus(int i) throws android.os.RemoteException;

    int handleDisplayEvent(long j, int i, int i2) throws android.os.RemoteException;

    void initialize(vendor.pixelworks.hardware.display.DisplayConfigVariableInfo displayConfigVariableInfo) throws android.os.RemoteException;

    java.lang.String irisConfigureBatch(int i, java.lang.String str) throws android.os.RemoteException;

    int irisConfigureBuffer(int i, long j, android.os.ParcelFileDescriptor parcelFileDescriptor, int i2) throws android.os.RemoteException;

    int[] irisConfigureGet(int i, int[] iArr) throws android.os.RemoteException;

    int irisConfigureSet(int i, int[] iArr) throws android.os.RemoteException;

    byte[] panelReadWrite(boolean z, int i, int i2, boolean z2, byte[] bArr, int i3) throws android.os.RemoteException;

    int present(long j) throws android.os.RemoteException;

    int presentDisplay(long j) throws android.os.RemoteException;

    void registerCallback(long j, vendor.pixelworks.hardware.display.IIrisCallback iIrisCallback) throws android.os.RemoteException;

    void registerSoftIrisClient(long j, vendor.pixelworks.hardware.display.ISoftIrisClient iSoftIrisClient) throws android.os.RemoteException;

    void reportDualChannelStatus(int i) throws android.os.RemoteException;

    void setActiveConfig(long j, vendor.pixelworks.hardware.display.DisplayConfigVariableInfo displayConfigVariableInfo) throws android.os.RemoteException;

    void setClientTarget(long j, int i) throws android.os.RemoteException;

    int setColorModeWithRenderIntent(long j, int i, int i2) throws android.os.RemoteException;

    int setColorTransform(float[] fArr) throws android.os.RemoteException;

    void setDisplayConnected(long j, boolean z) throws android.os.RemoteException;

    void setLayerBuffer(long j, long j2, vendor.pixelworks.hardware.display.BufferInfo bufferInfo) throws android.os.RemoteException;

    void setLayerCompositionType(long j, long j2, int i) throws android.os.RemoteException;

    void setLayerDisplayFrame(long j, long j2, vendor.pixelworks.hardware.display.HwcRect hwcRect) throws android.os.RemoteException;

    void setLayerProperty(long j, int i, long j2) throws android.os.RemoteException;

    void setLayerSetEmpty(long j, boolean z) throws android.os.RemoteException;

    void setLayerSourceCrop(long j, long j2, vendor.pixelworks.hardware.display.HwcRect hwcRect) throws android.os.RemoteException;

    void setLayerTransform(long j, long j2, int i) throws android.os.RemoteException;

    void setLayerZOrder(long j, long j2, int i) throws android.os.RemoteException;

    int setOsdAutoRefresh(int i) throws android.os.RemoteException;

    int setPowerMode(long j, int i, boolean z, boolean z2) throws android.os.RemoteException;

    int[] updateDisplayBrightness(long j, int i, int[] iArr) throws android.os.RemoteException;

    public static class Default implements vendor.pixelworks.hardware.display.IIris {
        @Override // vendor.pixelworks.hardware.display.IIris
        public void buildLayerStack(long display, vendor.pixelworks.hardware.display.LayerStack layerStack) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void changeLayerType(long id, long newDisplay) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int commitLayerStack(long display, int compType) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int configureIrisMaxcll(int hdr_maxcll) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void createLayer(long display, long id) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void destroyLayer(long display, long id) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void enableSecondaryDisplay(boolean enable) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public vendor.pixelworks.hardware.display.IrisFixedConfig getCurrentConfig(long display) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public java.lang.String getDumpString(long display) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public vendor.pixelworks.hardware.display.LutData getLayerToneMappingLut(long display, int type) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int getOsdStatus(int type) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int handleDisplayEvent(long display, int event, int mode) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void initialize(vendor.pixelworks.hardware.display.DisplayConfigVariableInfo info) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public java.lang.String irisConfigureBatch(int type, java.lang.String json) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int irisConfigureBuffer(int type, long display, android.os.ParcelFileDescriptor buffer, int size) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int[] irisConfigureGet(int type, int[] values) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int irisConfigureSet(int type, int[] values) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public byte[] panelReadWrite(boolean highSpeed, int dtype, int vc, boolean last, byte[] tx, int rxLen) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int present(long display) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int presentDisplay(long display) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void registerCallback(long cookie, vendor.pixelworks.hardware.display.IIrisCallback callback) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void registerSoftIrisClient(long cookie, vendor.pixelworks.hardware.display.ISoftIrisClient client) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void reportDualChannelStatus(int status) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setActiveConfig(long display, vendor.pixelworks.hardware.display.DisplayConfigVariableInfo info) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setClientTarget(long display, int acquireFence) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setColorModeWithRenderIntent(long display, int mode, int intent) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setDisplayConnected(long display, boolean connnected) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerBuffer(long display, long id, vendor.pixelworks.hardware.display.BufferInfo bufferInfo) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerCompositionType(long display, long id, int type) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerDisplayFrame(long display, long id, vendor.pixelworks.hardware.display.HwcRect frame) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerProperty(long display, int prop, long id) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerSetEmpty(long display, boolean empty) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerSourceCrop(long display, long id, vendor.pixelworks.hardware.display.HwcRect crop) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerTransform(long display, long id, int transform) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void setLayerZOrder(long display, long id, int z) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setOsdAutoRefresh(int value) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setPowerMode(long display, int mode, boolean isAfter, boolean fromEvent) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int[] updateDisplayBrightness(long display, int syncMethod, int[] values) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int setColorTransform(float[] matrix) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public void configureIrisHdrMode(int mode) throws android.os.RemoteException {
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.pixelworks.hardware.display.IIris
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.pixelworks.hardware.display.IIris {
        static final int TRANSACTION_buildLayerStack = 1;
        static final int TRANSACTION_changeLayerType = 2;
        static final int TRANSACTION_commitLayerStack = 3;
        static final int TRANSACTION_configureIrisHdrMode = 40;
        static final int TRANSACTION_configureIrisMaxcll = 4;
        static final int TRANSACTION_createLayer = 5;
        static final int TRANSACTION_destroyLayer = 6;
        static final int TRANSACTION_enableSecondaryDisplay = 7;
        static final int TRANSACTION_getCurrentConfig = 8;
        static final int TRANSACTION_getDumpString = 9;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getLayerToneMappingLut = 10;
        static final int TRANSACTION_getOsdStatus = 11;
        static final int TRANSACTION_handleDisplayEvent = 12;
        static final int TRANSACTION_initialize = 13;
        static final int TRANSACTION_irisConfigureBatch = 14;
        static final int TRANSACTION_irisConfigureBuffer = 15;
        static final int TRANSACTION_irisConfigureGet = 16;
        static final int TRANSACTION_irisConfigureSet = 17;
        static final int TRANSACTION_panelReadWrite = 18;
        static final int TRANSACTION_present = 19;
        static final int TRANSACTION_presentDisplay = 20;
        static final int TRANSACTION_registerCallback = 21;
        static final int TRANSACTION_registerSoftIrisClient = 22;
        static final int TRANSACTION_reportDualChannelStatus = 23;
        static final int TRANSACTION_setActiveConfig = 24;
        static final int TRANSACTION_setClientTarget = 25;
        static final int TRANSACTION_setColorModeWithRenderIntent = 26;
        static final int TRANSACTION_setColorTransform = 39;
        static final int TRANSACTION_setDisplayConnected = 27;
        static final int TRANSACTION_setLayerBuffer = 28;
        static final int TRANSACTION_setLayerCompositionType = 29;
        static final int TRANSACTION_setLayerDisplayFrame = 30;
        static final int TRANSACTION_setLayerProperty = 31;
        static final int TRANSACTION_setLayerSetEmpty = 32;
        static final int TRANSACTION_setLayerSourceCrop = 33;
        static final int TRANSACTION_setLayerTransform = 34;
        static final int TRANSACTION_setLayerZOrder = 35;
        static final int TRANSACTION_setOsdAutoRefresh = 36;
        static final int TRANSACTION_setPowerMode = 37;
        static final int TRANSACTION_updateDisplayBrightness = 38;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.pixelworks.hardware.display.IIris asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.pixelworks.hardware.display.IIris)) {
                return (vendor.pixelworks.hardware.display.IIris) iin;
            }
            return new vendor.pixelworks.hardware.display.IIris.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "buildLayerStack";
                case 2:
                    return "changeLayerType";
                case 3:
                    return "commitLayerStack";
                case 4:
                    return "configureIrisMaxcll";
                case 5:
                    return "createLayer";
                case 6:
                    return "destroyLayer";
                case 7:
                    return "enableSecondaryDisplay";
                case 8:
                    return "getCurrentConfig";
                case 9:
                    return "getDumpString";
                case 10:
                    return "getLayerToneMappingLut";
                case 11:
                    return "getOsdStatus";
                case 12:
                    return "handleDisplayEvent";
                case 13:
                    return "initialize";
                case 14:
                    return "irisConfigureBatch";
                case 15:
                    return "irisConfigureBuffer";
                case 16:
                    return "irisConfigureGet";
                case 17:
                    return "irisConfigureSet";
                case 18:
                    return "panelReadWrite";
                case 19:
                    return "present";
                case 20:
                    return "presentDisplay";
                case 21:
                    return "registerCallback";
                case 22:
                    return "registerSoftIrisClient";
                case 23:
                    return "reportDualChannelStatus";
                case 24:
                    return "setActiveConfig";
                case 25:
                    return "setClientTarget";
                case 26:
                    return "setColorModeWithRenderIntent";
                case 27:
                    return "setDisplayConnected";
                case 28:
                    return "setLayerBuffer";
                case 29:
                    return "setLayerCompositionType";
                case 30:
                    return "setLayerDisplayFrame";
                case 31:
                    return "setLayerProperty";
                case 32:
                    return "setLayerSetEmpty";
                case 33:
                    return "setLayerSourceCrop";
                case 34:
                    return "setLayerTransform";
                case 35:
                    return "setLayerZOrder";
                case 36:
                    return "setOsdAutoRefresh";
                case 37:
                    return "setPowerMode";
                case 38:
                    return "updateDisplayBrightness";
                case 39:
                    return "setColorTransform";
                case 40:
                    return "configureIrisHdrMode";
                case TRANSACTION_getInterfaceHash /* 16777214 */:
                    return "getInterfaceHash";
                case 16777215:
                    return "getInterfaceVersion";
                default:
                    return null;
            }
        }

        public java.lang.String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
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
                    long _arg0 = data.readLong();
                    vendor.pixelworks.hardware.display.LayerStack _arg1 = (vendor.pixelworks.hardware.display.LayerStack) data.readTypedObject(vendor.pixelworks.hardware.display.LayerStack.CREATOR);
                    data.enforceNoDataAvail();
                    buildLayerStack(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    long _arg02 = data.readLong();
                    long _arg12 = data.readLong();
                    data.enforceNoDataAvail();
                    changeLayerType(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    long _arg03 = data.readLong();
                    int _arg13 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result = commitLayerStack(_arg03, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 4:
                    int _arg04 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result2 = configureIrisMaxcll(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    long _arg05 = data.readLong();
                    long _arg14 = data.readLong();
                    data.enforceNoDataAvail();
                    createLayer(_arg05, _arg14);
                    reply.writeNoException();
                    return true;
                case 6:
                    long _arg06 = data.readLong();
                    long _arg15 = data.readLong();
                    data.enforceNoDataAvail();
                    destroyLayer(_arg06, _arg15);
                    reply.writeNoException();
                    return true;
                case 7:
                    boolean _arg07 = data.readBoolean();
                    data.enforceNoDataAvail();
                    enableSecondaryDisplay(_arg07);
                    reply.writeNoException();
                    return true;
                case 8:
                    long _arg08 = data.readLong();
                    data.enforceNoDataAvail();
                    vendor.pixelworks.hardware.display.IrisFixedConfig _result3 = getCurrentConfig(_arg08);
                    reply.writeNoException();
                    reply.writeTypedObject(_result3, 1);
                    return true;
                case 9:
                    long _arg09 = data.readLong();
                    data.enforceNoDataAvail();
                    java.lang.String _result4 = getDumpString(_arg09);
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 10:
                    long _arg010 = data.readLong();
                    int _arg16 = data.readInt();
                    data.enforceNoDataAvail();
                    vendor.pixelworks.hardware.display.LutData _result5 = getLayerToneMappingLut(_arg010, _arg16);
                    reply.writeNoException();
                    reply.writeTypedObject(_result5, 1);
                    return true;
                case 11:
                    int _arg011 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result6 = getOsdStatus(_arg011);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 12:
                    long _arg012 = data.readLong();
                    int _arg17 = data.readInt();
                    int _arg2 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result7 = handleDisplayEvent(_arg012, _arg17, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 13:
                    vendor.pixelworks.hardware.display.DisplayConfigVariableInfo _arg013 = (vendor.pixelworks.hardware.display.DisplayConfigVariableInfo) data.readTypedObject(vendor.pixelworks.hardware.display.DisplayConfigVariableInfo.CREATOR);
                    data.enforceNoDataAvail();
                    initialize(_arg013);
                    reply.writeNoException();
                    return true;
                case 14:
                    int _arg014 = data.readInt();
                    java.lang.String _arg18 = data.readString();
                    data.enforceNoDataAvail();
                    java.lang.String _result8 = irisConfigureBatch(_arg014, _arg18);
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 15:
                    int _arg015 = data.readInt();
                    long _arg19 = data.readLong();
                    android.os.ParcelFileDescriptor _arg22 = (android.os.ParcelFileDescriptor) data.readTypedObject(android.os.ParcelFileDescriptor.CREATOR);
                    int _arg3 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result9 = irisConfigureBuffer(_arg015, _arg19, _arg22, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 16:
                    int _arg016 = data.readInt();
                    int[] _arg110 = data.createIntArray();
                    data.enforceNoDataAvail();
                    int[] _result10 = irisConfigureGet(_arg016, _arg110);
                    reply.writeNoException();
                    reply.writeIntArray(_result10);
                    return true;
                case 17:
                    int _arg017 = data.readInt();
                    int[] _arg111 = data.createIntArray();
                    data.enforceNoDataAvail();
                    int _result11 = irisConfigureSet(_arg017, _arg111);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 18:
                    boolean _arg018 = data.readBoolean();
                    int _arg112 = data.readInt();
                    int _arg23 = data.readInt();
                    boolean _arg32 = data.readBoolean();
                    byte[] _arg4 = data.createByteArray();
                    int _arg5 = data.readInt();
                    data.enforceNoDataAvail();
                    byte[] _result12 = panelReadWrite(_arg018, _arg112, _arg23, _arg32, _arg4, _arg5);
                    reply.writeNoException();
                    reply.writeByteArray(_result12);
                    return true;
                case 19:
                    long _arg019 = data.readLong();
                    data.enforceNoDataAvail();
                    int _result13 = present(_arg019);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 20:
                    long _arg020 = data.readLong();
                    data.enforceNoDataAvail();
                    int _result14 = presentDisplay(_arg020);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 21:
                    long _arg021 = data.readLong();
                    vendor.pixelworks.hardware.display.IIrisCallback _arg113 = vendor.pixelworks.hardware.display.IIrisCallback.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    registerCallback(_arg021, _arg113);
                    reply.writeNoException();
                    return true;
                case 22:
                    long _arg022 = data.readLong();
                    vendor.pixelworks.hardware.display.ISoftIrisClient _arg114 = vendor.pixelworks.hardware.display.ISoftIrisClient.Stub.asInterface(data.readStrongBinder());
                    data.enforceNoDataAvail();
                    registerSoftIrisClient(_arg022, _arg114);
                    reply.writeNoException();
                    return true;
                case 23:
                    int _arg023 = data.readInt();
                    data.enforceNoDataAvail();
                    reportDualChannelStatus(_arg023);
                    reply.writeNoException();
                    return true;
                case 24:
                    long _arg024 = data.readLong();
                    vendor.pixelworks.hardware.display.DisplayConfigVariableInfo _arg115 = (vendor.pixelworks.hardware.display.DisplayConfigVariableInfo) data.readTypedObject(vendor.pixelworks.hardware.display.DisplayConfigVariableInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setActiveConfig(_arg024, _arg115);
                    reply.writeNoException();
                    return true;
                case 25:
                    long _arg025 = data.readLong();
                    int _arg116 = data.readInt();
                    data.enforceNoDataAvail();
                    setClientTarget(_arg025, _arg116);
                    reply.writeNoException();
                    return true;
                case 26:
                    long _arg026 = data.readLong();
                    int _arg117 = data.readInt();
                    int _arg24 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result15 = setColorModeWithRenderIntent(_arg026, _arg117, _arg24);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 27:
                    long _arg027 = data.readLong();
                    boolean _arg118 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setDisplayConnected(_arg027, _arg118);
                    reply.writeNoException();
                    return true;
                case 28:
                    long _arg028 = data.readLong();
                    long _arg119 = data.readLong();
                    vendor.pixelworks.hardware.display.BufferInfo _arg25 = (vendor.pixelworks.hardware.display.BufferInfo) data.readTypedObject(vendor.pixelworks.hardware.display.BufferInfo.CREATOR);
                    data.enforceNoDataAvail();
                    setLayerBuffer(_arg028, _arg119, _arg25);
                    reply.writeNoException();
                    return true;
                case 29:
                    long _arg029 = data.readLong();
                    long _arg120 = data.readLong();
                    int _arg26 = data.readInt();
                    data.enforceNoDataAvail();
                    setLayerCompositionType(_arg029, _arg120, _arg26);
                    reply.writeNoException();
                    return true;
                case 30:
                    long _arg030 = data.readLong();
                    long _arg121 = data.readLong();
                    vendor.pixelworks.hardware.display.HwcRect _arg27 = (vendor.pixelworks.hardware.display.HwcRect) data.readTypedObject(vendor.pixelworks.hardware.display.HwcRect.CREATOR);
                    data.enforceNoDataAvail();
                    setLayerDisplayFrame(_arg030, _arg121, _arg27);
                    reply.writeNoException();
                    return true;
                case 31:
                    long _arg031 = data.readLong();
                    int _arg122 = data.readInt();
                    long _arg28 = data.readLong();
                    data.enforceNoDataAvail();
                    setLayerProperty(_arg031, _arg122, _arg28);
                    reply.writeNoException();
                    return true;
                case 32:
                    long _arg032 = data.readLong();
                    boolean _arg123 = data.readBoolean();
                    data.enforceNoDataAvail();
                    setLayerSetEmpty(_arg032, _arg123);
                    reply.writeNoException();
                    return true;
                case 33:
                    long _arg033 = data.readLong();
                    long _arg124 = data.readLong();
                    vendor.pixelworks.hardware.display.HwcRect _arg29 = (vendor.pixelworks.hardware.display.HwcRect) data.readTypedObject(vendor.pixelworks.hardware.display.HwcRect.CREATOR);
                    data.enforceNoDataAvail();
                    setLayerSourceCrop(_arg033, _arg124, _arg29);
                    reply.writeNoException();
                    return true;
                case 34:
                    long _arg034 = data.readLong();
                    long _arg125 = data.readLong();
                    int _arg210 = data.readInt();
                    data.enforceNoDataAvail();
                    setLayerTransform(_arg034, _arg125, _arg210);
                    reply.writeNoException();
                    return true;
                case 35:
                    long _arg035 = data.readLong();
                    long _arg126 = data.readLong();
                    int _arg211 = data.readInt();
                    data.enforceNoDataAvail();
                    setLayerZOrder(_arg035, _arg126, _arg211);
                    reply.writeNoException();
                    return true;
                case 36:
                    int _arg036 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result16 = setOsdAutoRefresh(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 37:
                    long _arg037 = data.readLong();
                    int _arg127 = data.readInt();
                    boolean _arg212 = data.readBoolean();
                    boolean _arg33 = data.readBoolean();
                    data.enforceNoDataAvail();
                    int _result17 = setPowerMode(_arg037, _arg127, _arg212, _arg33);
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 38:
                    long _arg038 = data.readLong();
                    int _arg128 = data.readInt();
                    int[] _arg213 = data.createIntArray();
                    data.enforceNoDataAvail();
                    int[] _result18 = updateDisplayBrightness(_arg038, _arg128, _arg213);
                    reply.writeNoException();
                    reply.writeIntArray(_result18);
                    return true;
                case 39:
                    float[] _arg039 = data.createFloatArray();
                    data.enforceNoDataAvail();
                    int _result19 = setColorTransform(_arg039);
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 40:
                    int _arg040 = data.readInt();
                    data.enforceNoDataAvail();
                    configureIrisHdrMode(_arg040);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.pixelworks.hardware.display.IIris {
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

            @Override // vendor.pixelworks.hardware.display.IIris
            public void buildLayerStack(long display, vendor.pixelworks.hardware.display.LayerStack layerStack) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeTypedObject(layerStack, 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method buildLayerStack is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void changeLayerType(long id, long newDisplay) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(id);
                    _data.writeLong(newDisplay);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method changeLayerType is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int commitLayerStack(long display, int compType) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(compType);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method commitLayerStack is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int configureIrisMaxcll(int hdr_maxcll) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(hdr_maxcll);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method configureIrisMaxcll is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void createLayer(long display, long id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method createLayer is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void destroyLayer(long display, long id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method destroyLayer is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void enableSecondaryDisplay(boolean enable) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(enable);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method enableSecondaryDisplay is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public vendor.pixelworks.hardware.display.IrisFixedConfig getCurrentConfig(long display) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCurrentConfig is unimplemented.");
                    }
                    _reply.readException();
                    vendor.pixelworks.hardware.display.IrisFixedConfig _result = (vendor.pixelworks.hardware.display.IrisFixedConfig) _reply.readTypedObject(vendor.pixelworks.hardware.display.IrisFixedConfig.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public java.lang.String getDumpString(long display) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getDumpString is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public vendor.pixelworks.hardware.display.LutData getLayerToneMappingLut(long display, int type) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getLayerToneMappingLut is unimplemented.");
                    }
                    _reply.readException();
                    vendor.pixelworks.hardware.display.LutData _result = (vendor.pixelworks.hardware.display.LutData) _reply.readTypedObject(vendor.pixelworks.hardware.display.LutData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int getOsdStatus(int type) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getOsdStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int handleDisplayEvent(long display, int event, int mode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(event);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method handleDisplayEvent is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void initialize(vendor.pixelworks.hardware.display.DisplayConfigVariableInfo info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method initialize is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public java.lang.String irisConfigureBatch(int type, java.lang.String json) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(json);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method irisConfigureBatch is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int irisConfigureBuffer(int type, long display, android.os.ParcelFileDescriptor buffer, int size) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeLong(display);
                    _data.writeTypedObject(buffer, 0);
                    _data.writeInt(size);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method irisConfigureBuffer is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int[] irisConfigureGet(int type, int[] values) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method irisConfigureGet is unimplemented.");
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int irisConfigureSet(int type, int[] values) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method irisConfigureSet is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public byte[] panelReadWrite(boolean highSpeed, int dtype, int vc, boolean last, byte[] tx, int rxLen) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeBoolean(highSpeed);
                    _data.writeInt(dtype);
                    _data.writeInt(vc);
                    _data.writeBoolean(last);
                    _data.writeByteArray(tx);
                    _data.writeInt(rxLen);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method panelReadWrite is unimplemented.");
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int present(long display) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method present is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int presentDisplay(long display) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method presentDisplay is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void registerCallback(long cookie, vendor.pixelworks.hardware.display.IIrisCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(cookie);
                    _data.writeStrongInterface(callback);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerCallback is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void registerSoftIrisClient(long cookie, vendor.pixelworks.hardware.display.ISoftIrisClient client) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(cookie);
                    _data.writeStrongInterface(client);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method registerSoftIrisClient is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void reportDualChannelStatus(int status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method reportDualChannelStatus is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setActiveConfig(long display, vendor.pixelworks.hardware.display.DisplayConfigVariableInfo info) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeTypedObject(info, 0);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setActiveConfig is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setClientTarget(long display, int acquireFence) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(acquireFence);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setClientTarget is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setColorModeWithRenderIntent(long display, int mode, int intent) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(mode);
                    _data.writeInt(intent);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setColorModeWithRenderIntent is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setDisplayConnected(long display, boolean connnected) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeBoolean(connnected);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setDisplayConnected is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerBuffer(long display, long id, vendor.pixelworks.hardware.display.BufferInfo bufferInfo) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    _data.writeTypedObject(bufferInfo, 0);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerBuffer is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerCompositionType(long display, long id, int type) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerCompositionType is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerDisplayFrame(long display, long id, vendor.pixelworks.hardware.display.HwcRect frame) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    _data.writeTypedObject(frame, 0);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerDisplayFrame is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerProperty(long display, int prop, long id) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(prop);
                    _data.writeLong(id);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerProperty is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerSetEmpty(long display, boolean empty) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeBoolean(empty);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerSetEmpty is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerSourceCrop(long display, long id, vendor.pixelworks.hardware.display.HwcRect crop) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    _data.writeTypedObject(crop, 0);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerSourceCrop is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerTransform(long display, long id, int transform) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    _data.writeInt(transform);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerTransform is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void setLayerZOrder(long display, long id, int z) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeLong(id);
                    _data.writeInt(z);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setLayerZOrder is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setOsdAutoRefresh(int value) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(value);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setOsdAutoRefresh is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setPowerMode(long display, int mode, boolean isAfter, boolean fromEvent) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(mode);
                    _data.writeBoolean(isAfter);
                    _data.writeBoolean(fromEvent);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setPowerMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int[] updateDisplayBrightness(long display, int syncMethod, int[] values) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeLong(display);
                    _data.writeInt(syncMethod);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method updateDisplayBrightness is unimplemented.");
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public int setColorTransform(float[] matrix) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeFloatArray(matrix);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setColorTransform is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
            public void configureIrisHdrMode(int mode) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(mode);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method configureIrisHdrMode is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.pixelworks.hardware.display.IIris
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

            @Override // vendor.pixelworks.hardware.display.IIris
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.pixelworks.hardware.display.IIris.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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

        public int getMaxTransactionId() {
            return TRANSACTION_getInterfaceHash;
        }
    }
}
