package vendor.oplus.hardware.ormsHalService;

/* JADX INFO: loaded from: classes4.dex */
public interface IOrmsAidlHalService extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$ormsHalService$IOrmsAidlHalService".replace('$', '.');
    public static final java.lang.String HASH = "45b9fba87d14d35c461848d6bb6cc13324f947c3";
    public static final int VERSION = 1;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    void ormsBoostAcquire(int i, int[] iArr) throws android.os.RemoteException;

    void ormsBoostRelease(int i) throws android.os.RemoteException;

    void ormsEnableCpuBouncing(java.lang.String str) throws android.os.RemoteException;

    java.lang.String ormsReadDdrAvailFreq(int i) throws android.os.RemoteException;

    java.lang.String ormsReadFile(java.lang.String str) throws android.os.RemoteException;

    java.lang.String ormsReadGpuFreq(int i) throws android.os.RemoteException;

    void ormsWriteAboveHispeedDelay(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteBgCpuUclampMin(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteBusyDownThres(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteBusyUpThres(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCameraTracingEvents(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCoreCtlEnable(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuBouncing(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuCoreNum(int i, int i2, int i3) throws android.os.RemoteException;

    void ormsWriteCpuCpuDdrBwMin(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuCpuDdrLatMin(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuDdrLatfloorMax(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuDdrLatfloorMax2(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuDdrLatfloorMin(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuDdrLatfloorMin2(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuL3LatMax(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuLlccLatMax(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteCpuOnline(int i, int i2) throws android.os.RemoteException;

    void ormsWriteFgCpuUclampMin(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteForceStep(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteFpsgo(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteHwmonHystOpt(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteInputBoostEnabled(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteInputBoostFreq(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteLlccDdrLatMax(int i, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteLowPowerMode(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteMemlatL3Opt(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteNodeCommon(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    void ormsWritePreferSilverEnabled(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteRulerEnable(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteSchedAsymcapBoost(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteSchedtuneColocate(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteSchedtunePreferIdle(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteSleepDisabled(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteSlideBoost(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteTargetLoads(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    void ormsWriteTopCpuUclampMin(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteTouchBoost(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteTracingSetEvent(java.lang.String str) throws android.os.RemoteException;

    void ormsWriteUclampLatencySensitive(java.lang.String str) throws android.os.RemoteException;

    void ormsWritehalUfsPlusCtrl(java.lang.String str, java.lang.String str2) throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService {
        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsBoostAcquire(int pl_handle, int[] boostsList) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsBoostRelease(int pl_handle) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsEnableCpuBouncing(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public java.lang.String ormsReadDdrAvailFreq(int cluster) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public java.lang.String ormsReadFile(java.lang.String path) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public java.lang.String ormsReadGpuFreq(int pos) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteAboveHispeedDelay(int setOrRelease, int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteBgCpuUclampMin(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteBusyDownThres(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteBusyUpThres(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCameraTracingEvents(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCoreCtlEnable(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuBouncing(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuCoreNum(int cluster, int min, int max) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMax2(int clusterOffset, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMin(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuDdrLatfloorMin2(int clusterOffset, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuL3LatMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuLlccLatMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuOnline(int checkMask, int onlineMask) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteFgCpuUclampMin(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteFpsgo(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteHwmonHystOpt(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteInputBoostEnabled(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteInputBoostFreq(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteLlccDdrLatMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteLowPowerMode(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteMemlatL3Opt(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWritePreferSilverEnabled(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteRulerEnable(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSchedtuneColocate(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSchedtunePreferIdle(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSleepDisabled(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSlideBoost(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTargetLoads(int setOrRelease, int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTopCpuUclampMin(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTouchBoost(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteTracingSetEvent(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteUclampLatencySensitive(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWritehalUfsPlusCtrl(java.lang.String stingValue1, java.lang.String stingValue2) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteSchedAsymcapBoost(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuCpuDdrLatMin(int cluster, java.lang.String stingValue2) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteCpuCpuDdrBwMin(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteNodeCommon(int id, int cluster, java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public void ormsWriteForceStep(java.lang.String stingValue) throws android.os.RemoteException {
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService {
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_ormsBoostAcquire = 1;
        static final int TRANSACTION_ormsBoostRelease = 2;
        static final int TRANSACTION_ormsEnableCpuBouncing = 3;
        static final int TRANSACTION_ormsReadDdrAvailFreq = 4;
        static final int TRANSACTION_ormsReadFile = 5;
        static final int TRANSACTION_ormsReadGpuFreq = 6;
        static final int TRANSACTION_ormsWriteAboveHispeedDelay = 7;
        static final int TRANSACTION_ormsWriteBgCpuUclampMin = 8;
        static final int TRANSACTION_ormsWriteBusyDownThres = 9;
        static final int TRANSACTION_ormsWriteBusyUpThres = 10;
        static final int TRANSACTION_ormsWriteCameraTracingEvents = 11;
        static final int TRANSACTION_ormsWriteCoreCtlEnable = 12;
        static final int TRANSACTION_ormsWriteCpuBouncing = 13;
        static final int TRANSACTION_ormsWriteCpuCoreNum = 14;
        static final int TRANSACTION_ormsWriteCpuCpuDdrBwMin = 44;
        static final int TRANSACTION_ormsWriteCpuCpuDdrLatMin = 43;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMax = 15;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMax2 = 16;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMin = 17;
        static final int TRANSACTION_ormsWriteCpuDdrLatfloorMin2 = 18;
        static final int TRANSACTION_ormsWriteCpuL3LatMax = 19;
        static final int TRANSACTION_ormsWriteCpuLlccLatMax = 20;
        static final int TRANSACTION_ormsWriteCpuOnline = 21;
        static final int TRANSACTION_ormsWriteFgCpuUclampMin = 22;
        static final int TRANSACTION_ormsWriteForceStep = 46;
        static final int TRANSACTION_ormsWriteFpsgo = 23;
        static final int TRANSACTION_ormsWriteHwmonHystOpt = 24;
        static final int TRANSACTION_ormsWriteInputBoostEnabled = 25;
        static final int TRANSACTION_ormsWriteInputBoostFreq = 26;
        static final int TRANSACTION_ormsWriteLlccDdrLatMax = 27;
        static final int TRANSACTION_ormsWriteLowPowerMode = 28;
        static final int TRANSACTION_ormsWriteMemlatL3Opt = 29;
        static final int TRANSACTION_ormsWriteNodeCommon = 45;
        static final int TRANSACTION_ormsWritePreferSilverEnabled = 30;
        static final int TRANSACTION_ormsWriteRulerEnable = 31;
        static final int TRANSACTION_ormsWriteSchedAsymcapBoost = 42;
        static final int TRANSACTION_ormsWriteSchedtuneColocate = 32;
        static final int TRANSACTION_ormsWriteSchedtunePreferIdle = 33;
        static final int TRANSACTION_ormsWriteSleepDisabled = 34;
        static final int TRANSACTION_ormsWriteSlideBoost = 35;
        static final int TRANSACTION_ormsWriteTargetLoads = 36;
        static final int TRANSACTION_ormsWriteTopCpuUclampMin = 37;
        static final int TRANSACTION_ormsWriteTouchBoost = 38;
        static final int TRANSACTION_ormsWriteTracingSetEvent = 39;
        static final int TRANSACTION_ormsWriteUclampLatencySensitive = 40;
        static final int TRANSACTION_ormsWritehalUfsPlusCtrl = 41;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService)) {
                return (vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService) iin;
            }
            return new vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "ormsBoostAcquire";
                case 2:
                    return "ormsBoostRelease";
                case 3:
                    return "ormsEnableCpuBouncing";
                case 4:
                    return "ormsReadDdrAvailFreq";
                case 5:
                    return "ormsReadFile";
                case 6:
                    return "ormsReadGpuFreq";
                case 7:
                    return "ormsWriteAboveHispeedDelay";
                case 8:
                    return "ormsWriteBgCpuUclampMin";
                case 9:
                    return "ormsWriteBusyDownThres";
                case 10:
                    return "ormsWriteBusyUpThres";
                case 11:
                    return "ormsWriteCameraTracingEvents";
                case 12:
                    return "ormsWriteCoreCtlEnable";
                case 13:
                    return "ormsWriteCpuBouncing";
                case 14:
                    return "ormsWriteCpuCoreNum";
                case 15:
                    return "ormsWriteCpuDdrLatfloorMax";
                case 16:
                    return "ormsWriteCpuDdrLatfloorMax2";
                case 17:
                    return "ormsWriteCpuDdrLatfloorMin";
                case 18:
                    return "ormsWriteCpuDdrLatfloorMin2";
                case 19:
                    return "ormsWriteCpuL3LatMax";
                case 20:
                    return "ormsWriteCpuLlccLatMax";
                case 21:
                    return "ormsWriteCpuOnline";
                case 22:
                    return "ormsWriteFgCpuUclampMin";
                case 23:
                    return "ormsWriteFpsgo";
                case 24:
                    return "ormsWriteHwmonHystOpt";
                case 25:
                    return "ormsWriteInputBoostEnabled";
                case 26:
                    return "ormsWriteInputBoostFreq";
                case 27:
                    return "ormsWriteLlccDdrLatMax";
                case 28:
                    return "ormsWriteLowPowerMode";
                case 29:
                    return "ormsWriteMemlatL3Opt";
                case 30:
                    return "ormsWritePreferSilverEnabled";
                case 31:
                    return "ormsWriteRulerEnable";
                case 32:
                    return "ormsWriteSchedtuneColocate";
                case 33:
                    return "ormsWriteSchedtunePreferIdle";
                case 34:
                    return "ormsWriteSleepDisabled";
                case 35:
                    return "ormsWriteSlideBoost";
                case 36:
                    return "ormsWriteTargetLoads";
                case 37:
                    return "ormsWriteTopCpuUclampMin";
                case 38:
                    return "ormsWriteTouchBoost";
                case 39:
                    return "ormsWriteTracingSetEvent";
                case 40:
                    return "ormsWriteUclampLatencySensitive";
                case 41:
                    return "ormsWritehalUfsPlusCtrl";
                case 42:
                    return "ormsWriteSchedAsymcapBoost";
                case 43:
                    return "ormsWriteCpuCpuDdrLatMin";
                case 44:
                    return "ormsWriteCpuCpuDdrBwMin";
                case 45:
                    return "ormsWriteNodeCommon";
                case 46:
                    return "ormsWriteForceStep";
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
                    int _arg0 = data.readInt();
                    int[] _arg1 = data.createIntArray();
                    data.enforceNoDataAvail();
                    ormsBoostAcquire(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 2:
                    int _arg02 = data.readInt();
                    data.enforceNoDataAvail();
                    ormsBoostRelease(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    java.lang.String _arg03 = data.readString();
                    data.enforceNoDataAvail();
                    ormsEnableCpuBouncing(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    int _arg04 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result = ormsReadDdrAvailFreq(_arg04);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 5:
                    java.lang.String _arg05 = data.readString();
                    data.enforceNoDataAvail();
                    java.lang.String _result2 = ormsReadFile(_arg05);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 6:
                    int _arg06 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result3 = ormsReadGpuFreq(_arg06);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 7:
                    int _arg07 = data.readInt();
                    int _arg12 = data.readInt();
                    java.lang.String _arg2 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteAboveHispeedDelay(_arg07, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 8:
                    java.lang.String _arg08 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteBgCpuUclampMin(_arg08);
                    reply.writeNoException();
                    return true;
                case 9:
                    java.lang.String _arg09 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteBusyDownThres(_arg09);
                    reply.writeNoException();
                    return true;
                case 10:
                    java.lang.String _arg010 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteBusyUpThres(_arg010);
                    reply.writeNoException();
                    return true;
                case 11:
                    java.lang.String _arg011 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCameraTracingEvents(_arg011);
                    reply.writeNoException();
                    return true;
                case 12:
                    java.lang.String _arg012 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCoreCtlEnable(_arg012);
                    reply.writeNoException();
                    return true;
                case 13:
                    java.lang.String _arg013 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuBouncing(_arg013);
                    reply.writeNoException();
                    return true;
                case 14:
                    int _arg014 = data.readInt();
                    int _arg13 = data.readInt();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    ormsWriteCpuCoreNum(_arg014, _arg13, _arg22);
                    reply.writeNoException();
                    return true;
                case 15:
                    int _arg015 = data.readInt();
                    java.lang.String _arg14 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuDdrLatfloorMax(_arg015, _arg14);
                    reply.writeNoException();
                    return true;
                case 16:
                    int _arg016 = data.readInt();
                    java.lang.String _arg15 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuDdrLatfloorMax2(_arg016, _arg15);
                    reply.writeNoException();
                    return true;
                case 17:
                    int _arg017 = data.readInt();
                    java.lang.String _arg16 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuDdrLatfloorMin(_arg017, _arg16);
                    reply.writeNoException();
                    return true;
                case 18:
                    int _arg018 = data.readInt();
                    java.lang.String _arg17 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuDdrLatfloorMin2(_arg018, _arg17);
                    reply.writeNoException();
                    return true;
                case 19:
                    int _arg019 = data.readInt();
                    java.lang.String _arg18 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuL3LatMax(_arg019, _arg18);
                    reply.writeNoException();
                    return true;
                case 20:
                    int _arg020 = data.readInt();
                    java.lang.String _arg19 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuLlccLatMax(_arg020, _arg19);
                    reply.writeNoException();
                    return true;
                case 21:
                    int _arg021 = data.readInt();
                    int _arg110 = data.readInt();
                    data.enforceNoDataAvail();
                    ormsWriteCpuOnline(_arg021, _arg110);
                    reply.writeNoException();
                    return true;
                case 22:
                    java.lang.String _arg022 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteFgCpuUclampMin(_arg022);
                    reply.writeNoException();
                    return true;
                case 23:
                    java.lang.String _arg023 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteFpsgo(_arg023);
                    reply.writeNoException();
                    return true;
                case 24:
                    java.lang.String _arg024 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteHwmonHystOpt(_arg024);
                    reply.writeNoException();
                    return true;
                case 25:
                    java.lang.String _arg025 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteInputBoostEnabled(_arg025);
                    reply.writeNoException();
                    return true;
                case 26:
                    java.lang.String _arg026 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteInputBoostFreq(_arg026);
                    reply.writeNoException();
                    return true;
                case 27:
                    int _arg027 = data.readInt();
                    java.lang.String _arg111 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteLlccDdrLatMax(_arg027, _arg111);
                    reply.writeNoException();
                    return true;
                case 28:
                    java.lang.String _arg028 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteLowPowerMode(_arg028);
                    reply.writeNoException();
                    return true;
                case 29:
                    java.lang.String _arg029 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteMemlatL3Opt(_arg029);
                    reply.writeNoException();
                    return true;
                case 30:
                    java.lang.String _arg030 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWritePreferSilverEnabled(_arg030);
                    reply.writeNoException();
                    return true;
                case 31:
                    java.lang.String _arg031 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteRulerEnable(_arg031);
                    reply.writeNoException();
                    return true;
                case 32:
                    java.lang.String _arg032 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteSchedtuneColocate(_arg032);
                    reply.writeNoException();
                    return true;
                case 33:
                    java.lang.String _arg033 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteSchedtunePreferIdle(_arg033);
                    reply.writeNoException();
                    return true;
                case 34:
                    java.lang.String _arg034 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteSleepDisabled(_arg034);
                    reply.writeNoException();
                    return true;
                case 35:
                    java.lang.String _arg035 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteSlideBoost(_arg035);
                    reply.writeNoException();
                    return true;
                case 36:
                    int _arg036 = data.readInt();
                    int _arg112 = data.readInt();
                    java.lang.String _arg23 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteTargetLoads(_arg036, _arg112, _arg23);
                    reply.writeNoException();
                    return true;
                case 37:
                    java.lang.String _arg037 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteTopCpuUclampMin(_arg037);
                    reply.writeNoException();
                    return true;
                case 38:
                    java.lang.String _arg038 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteTouchBoost(_arg038);
                    reply.writeNoException();
                    return true;
                case 39:
                    java.lang.String _arg039 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteTracingSetEvent(_arg039);
                    reply.writeNoException();
                    return true;
                case 40:
                    java.lang.String _arg040 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteUclampLatencySensitive(_arg040);
                    reply.writeNoException();
                    return true;
                case 41:
                    java.lang.String _arg041 = data.readString();
                    java.lang.String _arg113 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWritehalUfsPlusCtrl(_arg041, _arg113);
                    reply.writeNoException();
                    return true;
                case 42:
                    java.lang.String _arg042 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteSchedAsymcapBoost(_arg042);
                    reply.writeNoException();
                    return true;
                case 43:
                    int _arg043 = data.readInt();
                    java.lang.String _arg114 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuCpuDdrLatMin(_arg043, _arg114);
                    reply.writeNoException();
                    return true;
                case 44:
                    java.lang.String _arg044 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteCpuCpuDdrBwMin(_arg044);
                    reply.writeNoException();
                    return true;
                case 45:
                    int _arg045 = data.readInt();
                    int _arg115 = data.readInt();
                    java.lang.String _arg24 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteNodeCommon(_arg045, _arg115, _arg24);
                    reply.writeNoException();
                    return true;
                case 46:
                    java.lang.String _arg046 = data.readString();
                    data.enforceNoDataAvail();
                    ormsWriteForceStep(_arg046);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService {
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

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsBoostAcquire(int pl_handle, int[] boostsList) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(pl_handle);
                    _data.writeIntArray(boostsList);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsBoostAcquire is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsBoostRelease(int pl_handle) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(pl_handle);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsBoostRelease is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsEnableCpuBouncing(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsEnableCpuBouncing is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public java.lang.String ormsReadDdrAvailFreq(int cluster) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsReadDdrAvailFreq is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public java.lang.String ormsReadFile(java.lang.String path) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsReadFile is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public java.lang.String ormsReadGpuFreq(int pos) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(pos);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsReadGpuFreq is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteAboveHispeedDelay(int setOrRelease, int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(setOrRelease);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteAboveHispeedDelay is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteBgCpuUclampMin(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteBgCpuUclampMin is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteBusyDownThres(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteBusyDownThres is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteBusyUpThres(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteBusyUpThres is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCameraTracingEvents(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCameraTracingEvents is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCoreCtlEnable(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCoreCtlEnable is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuBouncing(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuBouncing is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuCoreNum(int cluster, int min, int max) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeInt(min);
                    _data.writeInt(max);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuCoreNum is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuDdrLatfloorMax is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMax2(int clusterOffset, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(clusterOffset);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuDdrLatfloorMax2 is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMin(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuDdrLatfloorMin is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuDdrLatfloorMin2(int clusterOffset, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(clusterOffset);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuDdrLatfloorMin2 is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuL3LatMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuL3LatMax is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuLlccLatMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuLlccLatMax is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuOnline(int checkMask, int onlineMask) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(checkMask);
                    _data.writeInt(onlineMask);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuOnline is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteFgCpuUclampMin(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteFgCpuUclampMin is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteFpsgo(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteFpsgo is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteHwmonHystOpt(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteHwmonHystOpt is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteInputBoostEnabled(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteInputBoostEnabled is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteInputBoostFreq(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteInputBoostFreq is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteLlccDdrLatMax(int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteLlccDdrLatMax is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteLowPowerMode(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteLowPowerMode is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteMemlatL3Opt(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteMemlatL3Opt is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWritePreferSilverEnabled(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWritePreferSilverEnabled is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteRulerEnable(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteRulerEnable is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSchedtuneColocate(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteSchedtuneColocate is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSchedtunePreferIdle(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteSchedtunePreferIdle is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSleepDisabled(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteSleepDisabled is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSlideBoost(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteSlideBoost is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTargetLoads(int setOrRelease, int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(setOrRelease);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteTargetLoads is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTopCpuUclampMin(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteTopCpuUclampMin is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTouchBoost(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteTouchBoost is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteTracingSetEvent(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteTracingSetEvent is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteUclampLatencySensitive(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteUclampLatencySensitive is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWritehalUfsPlusCtrl(java.lang.String stingValue1, java.lang.String stingValue2) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue1);
                    _data.writeString(stingValue2);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWritehalUfsPlusCtrl is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteSchedAsymcapBoost(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteSchedAsymcapBoost is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuCpuDdrLatMin(int cluster, java.lang.String stingValue2) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue2);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuCpuDdrLatMin is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteCpuCpuDdrBwMin(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteCpuCpuDdrBwMin is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteNodeCommon(int id, int cluster, java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(id);
                    _data.writeInt(cluster);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteNodeCommon is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public void ormsWriteForceStep(java.lang.String stingValue) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(stingValue);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method ormsWriteForceStep is unimplemented.");
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
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

            @Override // vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.ormsHalService.IOrmsAidlHalService.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
