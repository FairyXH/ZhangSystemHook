package vendor.oplus.hardware.charger;

/* JADX INFO: loaded from: classes4.dex */
public interface ICharger extends android.os.IInterface {
    public static final java.lang.String DESCRIPTOR = "vendor$oplus$hardware$charger$ICharger".replace('$', '.');
    public static final java.lang.String HASH = "ede5e600328119dbf1c38269f810a3fdce74029f";
    public static final int VERSION = 6;

    int VolDividerIcWorkModeSet(java.lang.String str) throws android.os.RemoteException;

    int chgExchangeMesgInit() throws android.os.RemoteException;

    int chgExchangeSohMesgInit() throws android.os.RemoteException;

    int getAcType() throws android.os.RemoteException;

    int getBattAuthenticate() throws android.os.RemoteException;

    java.lang.String getBattGaugeInfo() throws android.os.RemoteException;

    int getBattPPSChgIng() throws android.os.RemoteException;

    int getBattPPSChgPower() throws android.os.RemoteException;

    java.lang.String getBattParamNoplug() throws android.os.RemoteException;

    int getBattShortIcOtpStatus() throws android.os.RemoteException;

    int getBattSubCurrent() throws android.os.RemoteException;

    int getBattVoocChgIng() throws android.os.RemoteException;

    int getBatteryVoltageNow() throws android.os.RemoteException;

    java.lang.String getBccCsvData() throws android.os.RemoteException;

    int getBccExpStatus() throws android.os.RemoteException;

    java.lang.String getBmsHeatingRunningStatus() throws android.os.RemoteException;

    int getBmsHeatingStatus() throws android.os.RemoteException;

    java.lang.String getChargerControl() throws android.os.RemoteException;

    int getChargerCoolDown() throws android.os.RemoteException;

    int getChargerCriticalLog() throws android.os.RemoteException;

    int getChargerIdVolt() throws android.os.RemoteException;

    int getChargerLog() throws android.os.RemoteException;

    int getChargingModeInGsmCall() throws android.os.RemoteException;

    java.lang.String getChgConfig(int i, java.lang.String str, int i2) throws android.os.RemoteException;

    java.lang.String getChgOlcConfig() throws android.os.RemoteException;

    int getCpVbatDeviation() throws android.os.RemoteException;

    int getCustomSelectChgMode() throws android.os.RemoteException;

    java.lang.String getDevinfoFastchg() throws android.os.RemoteException;

    int getFastCharge() throws android.os.RemoteException;

    java.lang.String getInterfaceHash() throws android.os.RemoteException;

    int getInterfaceVersion() throws android.os.RemoteException;

    int getParallelChgMosTestResult() throws android.os.RemoteException;

    int getPsyAcOnline() throws android.os.RemoteException;

    int getPsyBatteryCC() throws android.os.RemoteException;

    int getPsyBatteryCurrentNow() throws android.os.RemoteException;

    int getPsyBatteryFcc() throws android.os.RemoteException;

    int getPsyBatteryHmac() throws android.os.RemoteException;

    int getPsyBatteryLevel() throws android.os.RemoteException;

    int getPsyBatteryNotify() throws android.os.RemoteException;

    int getPsyBatteryPchg() throws android.os.RemoteException;

    int getPsyBatteryPchgResetCount() throws android.os.RemoteException;

    int getPsyBatteryRm() throws android.os.RemoteException;

    java.lang.String getPsyBatterySN() throws android.os.RemoteException;

    int getPsyBatteryShortFeature() throws android.os.RemoteException;

    int getPsyBatteryShortStatus() throws android.os.RemoteException;

    java.lang.String getPsyBatteryStatus() throws android.os.RemoteException;

    int getPsyBatteryTemp() throws android.os.RemoteException;

    int getPsyChargeTech() throws android.os.RemoteException;

    int getPsyFastChgType() throws android.os.RemoteException;

    int getPsyInputCurrent() throws android.os.RemoteException;

    int getPsyOtgOnline() throws android.os.RemoteException;

    int getPsyOtgSwitch() throws android.os.RemoteException;

    int getPsyPcPortOnline() throws android.os.RemoteException;

    int getPsyQGVbatDeviation() throws android.os.RemoteException;

    int getPsyTypeOrientation() throws android.os.RemoteException;

    int getPsyUsbOnline() throws android.os.RemoteException;

    int getPsyUsbStatus() throws android.os.RemoteException;

    java.lang.String getPsyWirelessRX() throws android.os.RemoteException;

    java.lang.String getPsyWirelessRxVersion() throws android.os.RemoteException;

    java.lang.String getPsyWirelessTX() throws android.os.RemoteException;

    java.lang.String getPsyWirelessTxVersion() throws android.os.RemoteException;

    int getQgVbatDeviation() throws android.os.RemoteException;

    java.lang.String getQuickModeGain() throws android.os.RemoteException;

    java.lang.String getReserveSocDebug() throws android.os.RemoteException;

    int getSmartChgMode() throws android.os.RemoteException;

    int getUIsohValue() throws android.os.RemoteException;

    java.lang.String getUisohDebugParameterInfo() throws android.os.RemoteException;

    java.lang.String getUsbCurrentEyeDiagram(int i) throws android.os.RemoteException;

    int getUsbInputCurrentNow() throws android.os.RemoteException;

    int getUsbPrimalType() throws android.os.RemoteException;

    int getWiredOtgOnline() throws android.os.RemoteException;

    int getWirelessAdapterPower() throws android.os.RemoteException;

    int getWirelessCapacity() throws android.os.RemoteException;

    int getWirelessChargePumpEn() throws android.os.RemoteException;

    int getWirelessCurrentNow() throws android.os.RemoteException;

    java.lang.String getWirelessDeviated() throws android.os.RemoteException;

    int getWirelessOnline() throws android.os.RemoteException;

    int getWirelessPenPresent() throws android.os.RemoteException;

    int getWirelessPtmcId() throws android.os.RemoteException;

    int getWirelessRXEnable() throws android.os.RemoteException;

    int getWirelessRealType() throws android.os.RemoteException;

    java.lang.String getWirelessTXEnable() throws android.os.RemoteException;

    int getWirelessUserSleepMode() throws android.os.RemoteException;

    int getWirelessVoltageNow() throws android.os.RemoteException;

    java.lang.String healthd_update_ui_soc_decimal() throws android.os.RemoteException;

    int nightstandby(int i) throws android.os.RemoteException;

    int notifyScreenStatus(int i) throws android.os.RemoteException;

    java.lang.String queryChargeInfo() throws android.os.RemoteException;

    java.lang.String queryWlsPencilInfo() throws android.os.RemoteException;

    int setBatteryLogPush(java.lang.String str) throws android.os.RemoteException;

    int setBobStatus(java.lang.String str) throws android.os.RemoteException;

    int setChargeEMMode(java.lang.String str) throws android.os.RemoteException;

    int setChargerControl(java.lang.String str) throws android.os.RemoteException;

    int setChargerCoolDown(java.lang.String str) throws android.os.RemoteException;

    int setChargerCriticalLog(java.lang.String str) throws android.os.RemoteException;

    int setChargerCycle(java.lang.String str) throws android.os.RemoteException;

    int setChargerFactoryModeTest(java.lang.String str) throws android.os.RemoteException;

    int setChargerLog(java.lang.String str) throws android.os.RemoteException;

    int setChargingModeInGsmCall(java.lang.String str) throws android.os.RemoteException;

    int setChgConfig(int i, java.lang.String str, int i2) throws android.os.RemoteException;

    int setChgOlcConfig(java.lang.String str) throws android.os.RemoteException;

    int setChgRusConfig(java.lang.String str) throws android.os.RemoteException;

    int setChgStatusToBcc(int i) throws android.os.RemoteException;

    int setCustomSelectChgMode(int i, boolean z) throws android.os.RemoteException;

    int setFastchgFwUpdate(java.lang.String str) throws android.os.RemoteException;

    int setPsyMmiChgEn(java.lang.String str) throws android.os.RemoteException;

    int setPsyOtgSwitch(java.lang.String str) throws android.os.RemoteException;

    int setPsySlowChgEn(java.lang.String str) throws android.os.RemoteException;

    int setReserveSocDebug(java.lang.String str) throws android.os.RemoteException;

    int setShipMode(java.lang.String str) throws android.os.RemoteException;

    int setSmartChgMode(java.lang.String str) throws android.os.RemoteException;

    int setSmartCoolDown(int i, int i2, java.lang.String str) throws android.os.RemoteException;

    int setSuperEnduranceCount(java.lang.String str) throws android.os.RemoteException;

    int setSuperEnduranceStatus(java.lang.String str) throws android.os.RemoteException;

    int setTbattPwrOff(java.lang.String str) throws android.os.RemoteException;

    int setUisohDebugInfo(java.lang.String str) throws android.os.RemoteException;

    int setUsbEyeDiagram(int i, java.lang.String str, boolean z) throws android.os.RemoteException;

    int setUsbPrimalType(java.lang.String str) throws android.os.RemoteException;

    int setWirelessChargePumpEn(java.lang.String str) throws android.os.RemoteException;

    int setWirelessFtmMode(java.lang.String str) throws android.os.RemoteException;

    int setWirelessIconDelay(java.lang.String str) throws android.os.RemoteException;

    int setWirelessIdtAdcTest(java.lang.String str) throws android.os.RemoteException;

    int setWirelessPenSoc(java.lang.String str) throws android.os.RemoteException;

    int setWirelessRXEnable(java.lang.String str) throws android.os.RemoteException;

    int setWirelessTXEnable(java.lang.String str) throws android.os.RemoteException;

    int setWirelessUserSleepMode(java.lang.String str) throws android.os.RemoteException;

    int setWlsThirdPartitionInfo(java.lang.String str) throws android.os.RemoteException;

    vendor.oplus.hardware.charger.testKitFeatureTestResult testKitFeatureTest(int i) throws android.os.RemoteException;

    java.lang.String testKitGetFeatureList() throws android.os.RemoteException;

    java.lang.String testKitGetFeatureName(int i) throws android.os.RemoteException;

    int testKitGetFeatureNum() throws android.os.RemoteException;

    int updateUiSohToPartion() throws android.os.RemoteException;

    public static class Default implements vendor.oplus.hardware.charger.ICharger {
        @Override // vendor.oplus.hardware.charger.ICharger
        public int VolDividerIcWorkModeSet(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int chgExchangeMesgInit() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int chgExchangeSohMesgInit() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getAcType() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattAuthenticate() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattPPSChgIng() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattPPSChgPower() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getBattParamNoplug() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattShortIcOtpStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattSubCurrent() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBattVoocChgIng() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBatteryVoltageNow() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getBccCsvData() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBccExpStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getBmsHeatingRunningStatus() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getBmsHeatingStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getChargerControl() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerCoolDown() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerCriticalLog() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerIdVolt() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargerLog() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getCustomSelectChgMode() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getDevinfoFastchg() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getFastCharge() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getParallelChgMosTestResult() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyAcOnline() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryCC() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryCurrentNow() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryFcc() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryHmac() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryLevel() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryNotify() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryPchg() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryPchgResetCount() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryRm() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryShortFeature() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryShortStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getPsyBatteryStatus() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyBatteryTemp() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyChargeTech() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyFastChgType() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyInputCurrent() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyOtgOnline() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyOtgSwitch() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyPcPortOnline() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyQGVbatDeviation() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyTypeOrientation() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyUsbOnline() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getPsyUsbStatus() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getPsyWirelessRX() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getPsyWirelessRxVersion() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getPsyWirelessTX() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getPsyWirelessTxVersion() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getQgVbatDeviation() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getQuickModeGain() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getReserveSocDebug() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getSmartChgMode() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getUIsohValue() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getUisohDebugParameterInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getUsbInputCurrentNow() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getUsbPrimalType() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWiredOtgOnline() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessAdapterPower() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessCapacity() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessChargePumpEn() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessCurrentNow() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getWirelessDeviated() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessOnline() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessPenPresent() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessPtmcId() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessRXEnable() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessRealType() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getWirelessTXEnable() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessUserSleepMode() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getWirelessVoltageNow() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String healthd_update_ui_soc_decimal() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int nightstandby(int status) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int notifyScreenStatus(int status) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String queryChargeInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargeEMMode(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerControl(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerCoolDown(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerCriticalLog(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerCycle(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerFactoryModeTest(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargerLog(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgStatusToBcc(int status) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setCustomSelectChgMode(int mode, boolean enable) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setFastchgFwUpdate(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setPsyMmiChgEn(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setPsyOtgSwitch(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setReserveSocDebug(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setShipMode(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSmartChgMode(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSmartCoolDown(int coolDown, int normalCoolDown, java.lang.String pkgName) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setTbattPwrOff(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setUisohDebugInfo(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setUsbPrimalType(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessChargePumpEn(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessFtmMode(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessIconDelay(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessIdtAdcTest(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessPenSoc(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessRXEnable(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessTXEnable(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWirelessUserSleepMode(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setWlsThirdPartitionInfo(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public vendor.oplus.hardware.charger.testKitFeatureTestResult testKitFeatureTest(int index) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String testKitGetFeatureList() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String testKitGetFeatureName(int index) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int testKitGetFeatureNum() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int updateUiSohToPartion() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String queryWlsPencilInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getChgOlcConfig() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgOlcConfig(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSuperEnduranceStatus(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setSuperEnduranceCount(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setBobStatus(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setPsySlowChgEn(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getCpVbatDeviation() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setBatteryLogPush(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getChargingModeInGsmCall() throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChargingModeInGsmCall(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgRusConfig(java.lang.String data) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getPsyBatterySN() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getBattGaugeInfo() throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setChgConfig(int flag, java.lang.String extra, int callname) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getChgConfig(int flag, java.lang.String extra, int callname) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int setUsbEyeDiagram(int model, java.lang.String eyeDiagram, boolean isDefaultEyeDiagram) throws android.os.RemoteException {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getUsbCurrentEyeDiagram(int model) throws android.os.RemoteException {
            return null;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public int getInterfaceVersion() {
            return 0;
        }

        @Override // vendor.oplus.hardware.charger.ICharger
        public java.lang.String getInterfaceHash() {
            return "";
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends android.os.Binder implements vendor.oplus.hardware.charger.ICharger {
        static final int TRANSACTION_VolDividerIcWorkModeSet = 1;
        static final int TRANSACTION_chgExchangeMesgInit = 2;
        static final int TRANSACTION_chgExchangeSohMesgInit = 3;
        static final int TRANSACTION_getAcType = 4;
        static final int TRANSACTION_getBattAuthenticate = 5;
        static final int TRANSACTION_getBattGaugeInfo = 126;
        static final int TRANSACTION_getBattPPSChgIng = 6;
        static final int TRANSACTION_getBattPPSChgPower = 7;
        static final int TRANSACTION_getBattParamNoplug = 8;
        static final int TRANSACTION_getBattShortIcOtpStatus = 9;
        static final int TRANSACTION_getBattSubCurrent = 10;
        static final int TRANSACTION_getBattVoocChgIng = 11;
        static final int TRANSACTION_getBatteryVoltageNow = 12;
        static final int TRANSACTION_getBccCsvData = 13;
        static final int TRANSACTION_getBccExpStatus = 14;
        static final int TRANSACTION_getBmsHeatingRunningStatus = 15;
        static final int TRANSACTION_getBmsHeatingStatus = 16;
        static final int TRANSACTION_getChargerControl = 17;
        static final int TRANSACTION_getChargerCoolDown = 18;
        static final int TRANSACTION_getChargerCriticalLog = 19;
        static final int TRANSACTION_getChargerIdVolt = 20;
        static final int TRANSACTION_getChargerLog = 21;
        static final int TRANSACTION_getChargingModeInGsmCall = 122;
        static final int TRANSACTION_getChgConfig = 128;
        static final int TRANSACTION_getChgOlcConfig = 114;
        static final int TRANSACTION_getCpVbatDeviation = 120;
        static final int TRANSACTION_getCustomSelectChgMode = 22;
        static final int TRANSACTION_getDevinfoFastchg = 23;
        static final int TRANSACTION_getFastCharge = 24;
        static final int TRANSACTION_getInterfaceHash = 16777214;
        static final int TRANSACTION_getInterfaceVersion = 16777215;
        static final int TRANSACTION_getParallelChgMosTestResult = 25;
        static final int TRANSACTION_getPsyAcOnline = 26;
        static final int TRANSACTION_getPsyBatteryCC = 27;
        static final int TRANSACTION_getPsyBatteryCurrentNow = 28;
        static final int TRANSACTION_getPsyBatteryFcc = 29;
        static final int TRANSACTION_getPsyBatteryHmac = 30;
        static final int TRANSACTION_getPsyBatteryLevel = 31;
        static final int TRANSACTION_getPsyBatteryNotify = 32;
        static final int TRANSACTION_getPsyBatteryPchg = 33;
        static final int TRANSACTION_getPsyBatteryPchgResetCount = 34;
        static final int TRANSACTION_getPsyBatteryRm = 35;
        static final int TRANSACTION_getPsyBatterySN = 125;
        static final int TRANSACTION_getPsyBatteryShortFeature = 36;
        static final int TRANSACTION_getPsyBatteryShortStatus = 37;
        static final int TRANSACTION_getPsyBatteryStatus = 38;
        static final int TRANSACTION_getPsyBatteryTemp = 39;
        static final int TRANSACTION_getPsyChargeTech = 40;
        static final int TRANSACTION_getPsyFastChgType = 41;
        static final int TRANSACTION_getPsyInputCurrent = 42;
        static final int TRANSACTION_getPsyOtgOnline = 43;
        static final int TRANSACTION_getPsyOtgSwitch = 44;
        static final int TRANSACTION_getPsyPcPortOnline = 45;
        static final int TRANSACTION_getPsyQGVbatDeviation = 46;
        static final int TRANSACTION_getPsyTypeOrientation = 47;
        static final int TRANSACTION_getPsyUsbOnline = 48;
        static final int TRANSACTION_getPsyUsbStatus = 49;
        static final int TRANSACTION_getPsyWirelessRX = 50;
        static final int TRANSACTION_getPsyWirelessRxVersion = 51;
        static final int TRANSACTION_getPsyWirelessTX = 52;
        static final int TRANSACTION_getPsyWirelessTxVersion = 53;
        static final int TRANSACTION_getQgVbatDeviation = 54;
        static final int TRANSACTION_getQuickModeGain = 55;
        static final int TRANSACTION_getReserveSocDebug = 56;
        static final int TRANSACTION_getSmartChgMode = 57;
        static final int TRANSACTION_getUIsohValue = 58;
        static final int TRANSACTION_getUisohDebugParameterInfo = 59;
        static final int TRANSACTION_getUsbCurrentEyeDiagram = 130;
        static final int TRANSACTION_getUsbInputCurrentNow = 60;
        static final int TRANSACTION_getUsbPrimalType = 61;
        static final int TRANSACTION_getWiredOtgOnline = 62;
        static final int TRANSACTION_getWirelessAdapterPower = 63;
        static final int TRANSACTION_getWirelessCapacity = 64;
        static final int TRANSACTION_getWirelessChargePumpEn = 65;
        static final int TRANSACTION_getWirelessCurrentNow = 66;
        static final int TRANSACTION_getWirelessDeviated = 67;
        static final int TRANSACTION_getWirelessOnline = 68;
        static final int TRANSACTION_getWirelessPenPresent = 69;
        static final int TRANSACTION_getWirelessPtmcId = 70;
        static final int TRANSACTION_getWirelessRXEnable = 71;
        static final int TRANSACTION_getWirelessRealType = 72;
        static final int TRANSACTION_getWirelessTXEnable = 73;
        static final int TRANSACTION_getWirelessUserSleepMode = 74;
        static final int TRANSACTION_getWirelessVoltageNow = 75;
        static final int TRANSACTION_healthd_update_ui_soc_decimal = 76;
        static final int TRANSACTION_nightstandby = 77;
        static final int TRANSACTION_notifyScreenStatus = 78;
        static final int TRANSACTION_queryChargeInfo = 79;
        static final int TRANSACTION_queryWlsPencilInfo = 113;
        static final int TRANSACTION_setBatteryLogPush = 121;
        static final int TRANSACTION_setBobStatus = 118;
        static final int TRANSACTION_setChargeEMMode = 80;
        static final int TRANSACTION_setChargerControl = 81;
        static final int TRANSACTION_setChargerCoolDown = 82;
        static final int TRANSACTION_setChargerCriticalLog = 83;
        static final int TRANSACTION_setChargerCycle = 84;
        static final int TRANSACTION_setChargerFactoryModeTest = 85;
        static final int TRANSACTION_setChargerLog = 86;
        static final int TRANSACTION_setChargingModeInGsmCall = 123;
        static final int TRANSACTION_setChgConfig = 127;
        static final int TRANSACTION_setChgOlcConfig = 115;
        static final int TRANSACTION_setChgRusConfig = 124;
        static final int TRANSACTION_setChgStatusToBcc = 87;
        static final int TRANSACTION_setCustomSelectChgMode = 88;
        static final int TRANSACTION_setFastchgFwUpdate = 89;
        static final int TRANSACTION_setPsyMmiChgEn = 90;
        static final int TRANSACTION_setPsyOtgSwitch = 91;
        static final int TRANSACTION_setPsySlowChgEn = 119;
        static final int TRANSACTION_setReserveSocDebug = 92;
        static final int TRANSACTION_setShipMode = 93;
        static final int TRANSACTION_setSmartChgMode = 94;
        static final int TRANSACTION_setSmartCoolDown = 95;
        static final int TRANSACTION_setSuperEnduranceCount = 117;
        static final int TRANSACTION_setSuperEnduranceStatus = 116;
        static final int TRANSACTION_setTbattPwrOff = 96;
        static final int TRANSACTION_setUisohDebugInfo = 97;
        static final int TRANSACTION_setUsbEyeDiagram = 129;
        static final int TRANSACTION_setUsbPrimalType = 98;
        static final int TRANSACTION_setWirelessChargePumpEn = 99;
        static final int TRANSACTION_setWirelessFtmMode = 100;
        static final int TRANSACTION_setWirelessIconDelay = 101;
        static final int TRANSACTION_setWirelessIdtAdcTest = 102;
        static final int TRANSACTION_setWirelessPenSoc = 103;
        static final int TRANSACTION_setWirelessRXEnable = 104;
        static final int TRANSACTION_setWirelessTXEnable = 105;
        static final int TRANSACTION_setWirelessUserSleepMode = 106;
        static final int TRANSACTION_setWlsThirdPartitionInfo = 107;
        static final int TRANSACTION_testKitFeatureTest = 108;
        static final int TRANSACTION_testKitGetFeatureList = 109;
        static final int TRANSACTION_testKitGetFeatureName = 110;
        static final int TRANSACTION_testKitGetFeatureNum = 111;
        static final int TRANSACTION_updateUiSohToPartion = 112;

        public Stub() {
            markVintfStability();
            attachInterface(this, DESCRIPTOR);
        }

        public static vendor.oplus.hardware.charger.ICharger asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof vendor.oplus.hardware.charger.ICharger)) {
                return (vendor.oplus.hardware.charger.ICharger) iin;
            }
            return new vendor.oplus.hardware.charger.ICharger.Stub.Proxy(obj);
        }

        @Override // android.os.IInterface
        public android.os.IBinder asBinder() {
            return this;
        }

        public static java.lang.String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "VolDividerIcWorkModeSet";
                case 2:
                    return "chgExchangeMesgInit";
                case 3:
                    return "chgExchangeSohMesgInit";
                case 4:
                    return "getAcType";
                case 5:
                    return "getBattAuthenticate";
                case 6:
                    return "getBattPPSChgIng";
                case 7:
                    return "getBattPPSChgPower";
                case 8:
                    return "getBattParamNoplug";
                case 9:
                    return "getBattShortIcOtpStatus";
                case 10:
                    return "getBattSubCurrent";
                case 11:
                    return "getBattVoocChgIng";
                case 12:
                    return "getBatteryVoltageNow";
                case 13:
                    return "getBccCsvData";
                case 14:
                    return "getBccExpStatus";
                case 15:
                    return "getBmsHeatingRunningStatus";
                case 16:
                    return "getBmsHeatingStatus";
                case 17:
                    return "getChargerControl";
                case 18:
                    return "getChargerCoolDown";
                case 19:
                    return "getChargerCriticalLog";
                case 20:
                    return "getChargerIdVolt";
                case 21:
                    return "getChargerLog";
                case 22:
                    return "getCustomSelectChgMode";
                case 23:
                    return "getDevinfoFastchg";
                case 24:
                    return "getFastCharge";
                case 25:
                    return "getParallelChgMosTestResult";
                case 26:
                    return "getPsyAcOnline";
                case 27:
                    return "getPsyBatteryCC";
                case 28:
                    return "getPsyBatteryCurrentNow";
                case 29:
                    return "getPsyBatteryFcc";
                case 30:
                    return "getPsyBatteryHmac";
                case 31:
                    return "getPsyBatteryLevel";
                case 32:
                    return "getPsyBatteryNotify";
                case 33:
                    return "getPsyBatteryPchg";
                case 34:
                    return "getPsyBatteryPchgResetCount";
                case 35:
                    return "getPsyBatteryRm";
                case 36:
                    return "getPsyBatteryShortFeature";
                case 37:
                    return "getPsyBatteryShortStatus";
                case 38:
                    return "getPsyBatteryStatus";
                case 39:
                    return "getPsyBatteryTemp";
                case 40:
                    return "getPsyChargeTech";
                case 41:
                    return "getPsyFastChgType";
                case 42:
                    return "getPsyInputCurrent";
                case 43:
                    return "getPsyOtgOnline";
                case 44:
                    return "getPsyOtgSwitch";
                case 45:
                    return "getPsyPcPortOnline";
                case 46:
                    return "getPsyQGVbatDeviation";
                case 47:
                    return "getPsyTypeOrientation";
                case 48:
                    return "getPsyUsbOnline";
                case 49:
                    return "getPsyUsbStatus";
                case 50:
                    return "getPsyWirelessRX";
                case 51:
                    return "getPsyWirelessRxVersion";
                case 52:
                    return "getPsyWirelessTX";
                case 53:
                    return "getPsyWirelessTxVersion";
                case 54:
                    return "getQgVbatDeviation";
                case 55:
                    return "getQuickModeGain";
                case 56:
                    return "getReserveSocDebug";
                case 57:
                    return "getSmartChgMode";
                case 58:
                    return "getUIsohValue";
                case 59:
                    return "getUisohDebugParameterInfo";
                case 60:
                    return "getUsbInputCurrentNow";
                case 61:
                    return "getUsbPrimalType";
                case 62:
                    return "getWiredOtgOnline";
                case 63:
                    return "getWirelessAdapterPower";
                case 64:
                    return "getWirelessCapacity";
                case 65:
                    return "getWirelessChargePumpEn";
                case 66:
                    return "getWirelessCurrentNow";
                case 67:
                    return "getWirelessDeviated";
                case 68:
                    return "getWirelessOnline";
                case 69:
                    return "getWirelessPenPresent";
                case 70:
                    return "getWirelessPtmcId";
                case 71:
                    return "getWirelessRXEnable";
                case 72:
                    return "getWirelessRealType";
                case 73:
                    return "getWirelessTXEnable";
                case 74:
                    return "getWirelessUserSleepMode";
                case 75:
                    return "getWirelessVoltageNow";
                case 76:
                    return "healthd_update_ui_soc_decimal";
                case 77:
                    return "nightstandby";
                case 78:
                    return "notifyScreenStatus";
                case 79:
                    return "queryChargeInfo";
                case 80:
                    return "setChargeEMMode";
                case 81:
                    return "setChargerControl";
                case 82:
                    return "setChargerCoolDown";
                case 83:
                    return "setChargerCriticalLog";
                case 84:
                    return "setChargerCycle";
                case 85:
                    return "setChargerFactoryModeTest";
                case 86:
                    return "setChargerLog";
                case 87:
                    return "setChgStatusToBcc";
                case 88:
                    return "setCustomSelectChgMode";
                case 89:
                    return "setFastchgFwUpdate";
                case 90:
                    return "setPsyMmiChgEn";
                case 91:
                    return "setPsyOtgSwitch";
                case 92:
                    return "setReserveSocDebug";
                case 93:
                    return "setShipMode";
                case 94:
                    return "setSmartChgMode";
                case 95:
                    return "setSmartCoolDown";
                case 96:
                    return "setTbattPwrOff";
                case 97:
                    return "setUisohDebugInfo";
                case 98:
                    return "setUsbPrimalType";
                case 99:
                    return "setWirelessChargePumpEn";
                case 100:
                    return "setWirelessFtmMode";
                case 101:
                    return "setWirelessIconDelay";
                case 102:
                    return "setWirelessIdtAdcTest";
                case 103:
                    return "setWirelessPenSoc";
                case 104:
                    return "setWirelessRXEnable";
                case 105:
                    return "setWirelessTXEnable";
                case 106:
                    return "setWirelessUserSleepMode";
                case 107:
                    return "setWlsThirdPartitionInfo";
                case 108:
                    return "testKitFeatureTest";
                case 109:
                    return "testKitGetFeatureList";
                case 110:
                    return "testKitGetFeatureName";
                case 111:
                    return "testKitGetFeatureNum";
                case 112:
                    return "updateUiSohToPartion";
                case 113:
                    return "queryWlsPencilInfo";
                case 114:
                    return "getChgOlcConfig";
                case 115:
                    return "setChgOlcConfig";
                case 116:
                    return "setSuperEnduranceStatus";
                case 117:
                    return "setSuperEnduranceCount";
                case 118:
                    return "setBobStatus";
                case 119:
                    return "setPsySlowChgEn";
                case 120:
                    return "getCpVbatDeviation";
                case 121:
                    return "setBatteryLogPush";
                case 122:
                    return "getChargingModeInGsmCall";
                case 123:
                    return "setChargingModeInGsmCall";
                case 124:
                    return "setChgRusConfig";
                case 125:
                    return "getPsyBatterySN";
                case 126:
                    return "getBattGaugeInfo";
                case 127:
                    return "setChgConfig";
                case 128:
                    return "getChgConfig";
                case 129:
                    return "setUsbEyeDiagram";
                case 130:
                    return "getUsbCurrentEyeDiagram";
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
                    java.lang.String _arg0 = data.readString();
                    data.enforceNoDataAvail();
                    int _result = VolDividerIcWorkModeSet(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    int _result2 = chgExchangeMesgInit();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    int _result3 = chgExchangeSohMesgInit();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    int _result4 = getAcType();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    int _result5 = getBattAuthenticate();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    int _result6 = getBattPPSChgIng();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 7:
                    int _result7 = getBattPPSChgPower();
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 8:
                    java.lang.String _result8 = getBattParamNoplug();
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 9:
                    int _result9 = getBattShortIcOtpStatus();
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 10:
                    int _result10 = getBattSubCurrent();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 11:
                    int _result11 = getBattVoocChgIng();
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 12:
                    int _result12 = getBatteryVoltageNow();
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 13:
                    java.lang.String _result13 = getBccCsvData();
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 14:
                    int _result14 = getBccExpStatus();
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 15:
                    java.lang.String _result15 = getBmsHeatingRunningStatus();
                    reply.writeNoException();
                    reply.writeString(_result15);
                    return true;
                case 16:
                    int _result16 = getBmsHeatingStatus();
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 17:
                    java.lang.String _result17 = getChargerControl();
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 18:
                    int _result18 = getChargerCoolDown();
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 19:
                    int _result19 = getChargerCriticalLog();
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 20:
                    int _result20 = getChargerIdVolt();
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 21:
                    int _result21 = getChargerLog();
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 22:
                    int _result22 = getCustomSelectChgMode();
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 23:
                    java.lang.String _result23 = getDevinfoFastchg();
                    reply.writeNoException();
                    reply.writeString(_result23);
                    return true;
                case 24:
                    int _result24 = getFastCharge();
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 25:
                    int _result25 = getParallelChgMosTestResult();
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 26:
                    int _result26 = getPsyAcOnline();
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 27:
                    int _result27 = getPsyBatteryCC();
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 28:
                    int _result28 = getPsyBatteryCurrentNow();
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 29:
                    int _result29 = getPsyBatteryFcc();
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 30:
                    int _result30 = getPsyBatteryHmac();
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 31:
                    int _result31 = getPsyBatteryLevel();
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 32:
                    int _result32 = getPsyBatteryNotify();
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case 33:
                    int _result33 = getPsyBatteryPchg();
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 34:
                    int _result34 = getPsyBatteryPchgResetCount();
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 35:
                    int _result35 = getPsyBatteryRm();
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 36:
                    int _result36 = getPsyBatteryShortFeature();
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 37:
                    int _result37 = getPsyBatteryShortStatus();
                    reply.writeNoException();
                    reply.writeInt(_result37);
                    return true;
                case 38:
                    java.lang.String _result38 = getPsyBatteryStatus();
                    reply.writeNoException();
                    reply.writeString(_result38);
                    return true;
                case 39:
                    int _result39 = getPsyBatteryTemp();
                    reply.writeNoException();
                    reply.writeInt(_result39);
                    return true;
                case 40:
                    int _result40 = getPsyChargeTech();
                    reply.writeNoException();
                    reply.writeInt(_result40);
                    return true;
                case 41:
                    int _result41 = getPsyFastChgType();
                    reply.writeNoException();
                    reply.writeInt(_result41);
                    return true;
                case 42:
                    int _result42 = getPsyInputCurrent();
                    reply.writeNoException();
                    reply.writeInt(_result42);
                    return true;
                case 43:
                    int _result43 = getPsyOtgOnline();
                    reply.writeNoException();
                    reply.writeInt(_result43);
                    return true;
                case 44:
                    int _result44 = getPsyOtgSwitch();
                    reply.writeNoException();
                    reply.writeInt(_result44);
                    return true;
                case 45:
                    int _result45 = getPsyPcPortOnline();
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 46:
                    int _result46 = getPsyQGVbatDeviation();
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 47:
                    int _result47 = getPsyTypeOrientation();
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    return true;
                case 48:
                    int _result48 = getPsyUsbOnline();
                    reply.writeNoException();
                    reply.writeInt(_result48);
                    return true;
                case 49:
                    int _result49 = getPsyUsbStatus();
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 50:
                    java.lang.String _result50 = getPsyWirelessRX();
                    reply.writeNoException();
                    reply.writeString(_result50);
                    return true;
                case 51:
                    java.lang.String _result51 = getPsyWirelessRxVersion();
                    reply.writeNoException();
                    reply.writeString(_result51);
                    return true;
                case 52:
                    java.lang.String _result52 = getPsyWirelessTX();
                    reply.writeNoException();
                    reply.writeString(_result52);
                    return true;
                case 53:
                    java.lang.String _result53 = getPsyWirelessTxVersion();
                    reply.writeNoException();
                    reply.writeString(_result53);
                    return true;
                case 54:
                    int _result54 = getQgVbatDeviation();
                    reply.writeNoException();
                    reply.writeInt(_result54);
                    return true;
                case 55:
                    java.lang.String _result55 = getQuickModeGain();
                    reply.writeNoException();
                    reply.writeString(_result55);
                    return true;
                case 56:
                    java.lang.String _result56 = getReserveSocDebug();
                    reply.writeNoException();
                    reply.writeString(_result56);
                    return true;
                case 57:
                    int _result57 = getSmartChgMode();
                    reply.writeNoException();
                    reply.writeInt(_result57);
                    return true;
                case 58:
                    int _result58 = getUIsohValue();
                    reply.writeNoException();
                    reply.writeInt(_result58);
                    return true;
                case 59:
                    java.lang.String _result59 = getUisohDebugParameterInfo();
                    reply.writeNoException();
                    reply.writeString(_result59);
                    return true;
                case 60:
                    int _result60 = getUsbInputCurrentNow();
                    reply.writeNoException();
                    reply.writeInt(_result60);
                    return true;
                case 61:
                    int _result61 = getUsbPrimalType();
                    reply.writeNoException();
                    reply.writeInt(_result61);
                    return true;
                case 62:
                    int _result62 = getWiredOtgOnline();
                    reply.writeNoException();
                    reply.writeInt(_result62);
                    return true;
                case 63:
                    int _result63 = getWirelessAdapterPower();
                    reply.writeNoException();
                    reply.writeInt(_result63);
                    return true;
                case 64:
                    int _result64 = getWirelessCapacity();
                    reply.writeNoException();
                    reply.writeInt(_result64);
                    return true;
                case 65:
                    int _result65 = getWirelessChargePumpEn();
                    reply.writeNoException();
                    reply.writeInt(_result65);
                    return true;
                case 66:
                    int _result66 = getWirelessCurrentNow();
                    reply.writeNoException();
                    reply.writeInt(_result66);
                    return true;
                case 67:
                    java.lang.String _result67 = getWirelessDeviated();
                    reply.writeNoException();
                    reply.writeString(_result67);
                    return true;
                case 68:
                    int _result68 = getWirelessOnline();
                    reply.writeNoException();
                    reply.writeInt(_result68);
                    return true;
                case 69:
                    int _result69 = getWirelessPenPresent();
                    reply.writeNoException();
                    reply.writeInt(_result69);
                    return true;
                case 70:
                    int _result70 = getWirelessPtmcId();
                    reply.writeNoException();
                    reply.writeInt(_result70);
                    return true;
                case 71:
                    int _result71 = getWirelessRXEnable();
                    reply.writeNoException();
                    reply.writeInt(_result71);
                    return true;
                case 72:
                    int _result72 = getWirelessRealType();
                    reply.writeNoException();
                    reply.writeInt(_result72);
                    return true;
                case 73:
                    java.lang.String _result73 = getWirelessTXEnable();
                    reply.writeNoException();
                    reply.writeString(_result73);
                    return true;
                case 74:
                    int _result74 = getWirelessUserSleepMode();
                    reply.writeNoException();
                    reply.writeInt(_result74);
                    return true;
                case 75:
                    int _result75 = getWirelessVoltageNow();
                    reply.writeNoException();
                    reply.writeInt(_result75);
                    return true;
                case 76:
                    java.lang.String _result76 = healthd_update_ui_soc_decimal();
                    reply.writeNoException();
                    reply.writeString(_result76);
                    return true;
                case 77:
                    int _arg02 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result77 = nightstandby(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result77);
                    return true;
                case 78:
                    int _arg03 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result78 = notifyScreenStatus(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result78);
                    return true;
                case 79:
                    java.lang.String _result79 = queryChargeInfo();
                    reply.writeNoException();
                    reply.writeString(_result79);
                    return true;
                case 80:
                    java.lang.String _arg04 = data.readString();
                    data.enforceNoDataAvail();
                    int _result80 = setChargeEMMode(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result80);
                    return true;
                case 81:
                    java.lang.String _arg05 = data.readString();
                    data.enforceNoDataAvail();
                    int _result81 = setChargerControl(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result81);
                    return true;
                case 82:
                    java.lang.String _arg06 = data.readString();
                    data.enforceNoDataAvail();
                    int _result82 = setChargerCoolDown(_arg06);
                    reply.writeNoException();
                    reply.writeInt(_result82);
                    return true;
                case 83:
                    java.lang.String _arg07 = data.readString();
                    data.enforceNoDataAvail();
                    int _result83 = setChargerCriticalLog(_arg07);
                    reply.writeNoException();
                    reply.writeInt(_result83);
                    return true;
                case 84:
                    java.lang.String _arg08 = data.readString();
                    data.enforceNoDataAvail();
                    int _result84 = setChargerCycle(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result84);
                    return true;
                case 85:
                    java.lang.String _arg09 = data.readString();
                    data.enforceNoDataAvail();
                    int _result85 = setChargerFactoryModeTest(_arg09);
                    reply.writeNoException();
                    reply.writeInt(_result85);
                    return true;
                case 86:
                    java.lang.String _arg010 = data.readString();
                    data.enforceNoDataAvail();
                    int _result86 = setChargerLog(_arg010);
                    reply.writeNoException();
                    reply.writeInt(_result86);
                    return true;
                case 87:
                    int _arg011 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result87 = setChgStatusToBcc(_arg011);
                    reply.writeNoException();
                    reply.writeInt(_result87);
                    return true;
                case 88:
                    int _arg012 = data.readInt();
                    boolean _arg1 = data.readBoolean();
                    data.enforceNoDataAvail();
                    int _result88 = setCustomSelectChgMode(_arg012, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result88);
                    return true;
                case 89:
                    java.lang.String _arg013 = data.readString();
                    data.enforceNoDataAvail();
                    int _result89 = setFastchgFwUpdate(_arg013);
                    reply.writeNoException();
                    reply.writeInt(_result89);
                    return true;
                case 90:
                    java.lang.String _arg014 = data.readString();
                    data.enforceNoDataAvail();
                    int _result90 = setPsyMmiChgEn(_arg014);
                    reply.writeNoException();
                    reply.writeInt(_result90);
                    return true;
                case 91:
                    java.lang.String _arg015 = data.readString();
                    data.enforceNoDataAvail();
                    int _result91 = setPsyOtgSwitch(_arg015);
                    reply.writeNoException();
                    reply.writeInt(_result91);
                    return true;
                case 92:
                    java.lang.String _arg016 = data.readString();
                    data.enforceNoDataAvail();
                    int _result92 = setReserveSocDebug(_arg016);
                    reply.writeNoException();
                    reply.writeInt(_result92);
                    return true;
                case 93:
                    java.lang.String _arg017 = data.readString();
                    data.enforceNoDataAvail();
                    int _result93 = setShipMode(_arg017);
                    reply.writeNoException();
                    reply.writeInt(_result93);
                    return true;
                case 94:
                    java.lang.String _arg018 = data.readString();
                    data.enforceNoDataAvail();
                    int _result94 = setSmartChgMode(_arg018);
                    reply.writeNoException();
                    reply.writeInt(_result94);
                    return true;
                case 95:
                    int _arg019 = data.readInt();
                    int _arg12 = data.readInt();
                    java.lang.String _arg2 = data.readString();
                    data.enforceNoDataAvail();
                    int _result95 = setSmartCoolDown(_arg019, _arg12, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result95);
                    return true;
                case 96:
                    java.lang.String _arg020 = data.readString();
                    data.enforceNoDataAvail();
                    int _result96 = setTbattPwrOff(_arg020);
                    reply.writeNoException();
                    reply.writeInt(_result96);
                    return true;
                case 97:
                    java.lang.String _arg021 = data.readString();
                    data.enforceNoDataAvail();
                    int _result97 = setUisohDebugInfo(_arg021);
                    reply.writeNoException();
                    reply.writeInt(_result97);
                    return true;
                case 98:
                    java.lang.String _arg022 = data.readString();
                    data.enforceNoDataAvail();
                    int _result98 = setUsbPrimalType(_arg022);
                    reply.writeNoException();
                    reply.writeInt(_result98);
                    return true;
                case 99:
                    java.lang.String _arg023 = data.readString();
                    data.enforceNoDataAvail();
                    int _result99 = setWirelessChargePumpEn(_arg023);
                    reply.writeNoException();
                    reply.writeInt(_result99);
                    return true;
                case 100:
                    java.lang.String _arg024 = data.readString();
                    data.enforceNoDataAvail();
                    int _result100 = setWirelessFtmMode(_arg024);
                    reply.writeNoException();
                    reply.writeInt(_result100);
                    return true;
                case 101:
                    java.lang.String _arg025 = data.readString();
                    data.enforceNoDataAvail();
                    int _result101 = setWirelessIconDelay(_arg025);
                    reply.writeNoException();
                    reply.writeInt(_result101);
                    return true;
                case 102:
                    java.lang.String _arg026 = data.readString();
                    data.enforceNoDataAvail();
                    int _result102 = setWirelessIdtAdcTest(_arg026);
                    reply.writeNoException();
                    reply.writeInt(_result102);
                    return true;
                case 103:
                    java.lang.String _arg027 = data.readString();
                    data.enforceNoDataAvail();
                    int _result103 = setWirelessPenSoc(_arg027);
                    reply.writeNoException();
                    reply.writeInt(_result103);
                    return true;
                case 104:
                    java.lang.String _arg028 = data.readString();
                    data.enforceNoDataAvail();
                    int _result104 = setWirelessRXEnable(_arg028);
                    reply.writeNoException();
                    reply.writeInt(_result104);
                    return true;
                case 105:
                    java.lang.String _arg029 = data.readString();
                    data.enforceNoDataAvail();
                    int _result105 = setWirelessTXEnable(_arg029);
                    reply.writeNoException();
                    reply.writeInt(_result105);
                    return true;
                case 106:
                    java.lang.String _arg030 = data.readString();
                    data.enforceNoDataAvail();
                    int _result106 = setWirelessUserSleepMode(_arg030);
                    reply.writeNoException();
                    reply.writeInt(_result106);
                    return true;
                case 107:
                    java.lang.String _arg031 = data.readString();
                    data.enforceNoDataAvail();
                    int _result107 = setWlsThirdPartitionInfo(_arg031);
                    reply.writeNoException();
                    reply.writeInt(_result107);
                    return true;
                case 108:
                    int _arg032 = data.readInt();
                    data.enforceNoDataAvail();
                    vendor.oplus.hardware.charger.testKitFeatureTestResult _result108 = testKitFeatureTest(_arg032);
                    reply.writeNoException();
                    reply.writeTypedObject(_result108, 1);
                    return true;
                case 109:
                    java.lang.String _result109 = testKitGetFeatureList();
                    reply.writeNoException();
                    reply.writeString(_result109);
                    return true;
                case 110:
                    int _arg033 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result110 = testKitGetFeatureName(_arg033);
                    reply.writeNoException();
                    reply.writeString(_result110);
                    return true;
                case 111:
                    int _result111 = testKitGetFeatureNum();
                    reply.writeNoException();
                    reply.writeInt(_result111);
                    return true;
                case 112:
                    int _result112 = updateUiSohToPartion();
                    reply.writeNoException();
                    reply.writeInt(_result112);
                    return true;
                case 113:
                    java.lang.String _result113 = queryWlsPencilInfo();
                    reply.writeNoException();
                    reply.writeString(_result113);
                    return true;
                case 114:
                    java.lang.String _result114 = getChgOlcConfig();
                    reply.writeNoException();
                    reply.writeString(_result114);
                    return true;
                case 115:
                    java.lang.String _arg034 = data.readString();
                    data.enforceNoDataAvail();
                    int _result115 = setChgOlcConfig(_arg034);
                    reply.writeNoException();
                    reply.writeInt(_result115);
                    return true;
                case 116:
                    java.lang.String _arg035 = data.readString();
                    data.enforceNoDataAvail();
                    int _result116 = setSuperEnduranceStatus(_arg035);
                    reply.writeNoException();
                    reply.writeInt(_result116);
                    return true;
                case 117:
                    java.lang.String _arg036 = data.readString();
                    data.enforceNoDataAvail();
                    int _result117 = setSuperEnduranceCount(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result117);
                    return true;
                case 118:
                    java.lang.String _arg037 = data.readString();
                    data.enforceNoDataAvail();
                    int _result118 = setBobStatus(_arg037);
                    reply.writeNoException();
                    reply.writeInt(_result118);
                    return true;
                case 119:
                    java.lang.String _arg038 = data.readString();
                    data.enforceNoDataAvail();
                    int _result119 = setPsySlowChgEn(_arg038);
                    reply.writeNoException();
                    reply.writeInt(_result119);
                    return true;
                case 120:
                    int _result120 = getCpVbatDeviation();
                    reply.writeNoException();
                    reply.writeInt(_result120);
                    return true;
                case 121:
                    java.lang.String _arg039 = data.readString();
                    data.enforceNoDataAvail();
                    int _result121 = setBatteryLogPush(_arg039);
                    reply.writeNoException();
                    reply.writeInt(_result121);
                    return true;
                case 122:
                    int _result122 = getChargingModeInGsmCall();
                    reply.writeNoException();
                    reply.writeInt(_result122);
                    return true;
                case 123:
                    java.lang.String _arg040 = data.readString();
                    data.enforceNoDataAvail();
                    int _result123 = setChargingModeInGsmCall(_arg040);
                    reply.writeNoException();
                    reply.writeInt(_result123);
                    return true;
                case 124:
                    java.lang.String _arg041 = data.readString();
                    data.enforceNoDataAvail();
                    int _result124 = setChgRusConfig(_arg041);
                    reply.writeNoException();
                    reply.writeInt(_result124);
                    return true;
                case 125:
                    java.lang.String _result125 = getPsyBatterySN();
                    reply.writeNoException();
                    reply.writeString(_result125);
                    return true;
                case 126:
                    java.lang.String _result126 = getBattGaugeInfo();
                    reply.writeNoException();
                    reply.writeString(_result126);
                    return true;
                case 127:
                    int _arg042 = data.readInt();
                    java.lang.String _arg13 = data.readString();
                    int _arg22 = data.readInt();
                    data.enforceNoDataAvail();
                    int _result127 = setChgConfig(_arg042, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result127);
                    return true;
                case 128:
                    int _arg043 = data.readInt();
                    java.lang.String _arg14 = data.readString();
                    int _arg23 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result128 = getChgConfig(_arg043, _arg14, _arg23);
                    reply.writeNoException();
                    reply.writeString(_result128);
                    return true;
                case 129:
                    int _arg044 = data.readInt();
                    java.lang.String _arg15 = data.readString();
                    boolean _arg24 = data.readBoolean();
                    data.enforceNoDataAvail();
                    int _result129 = setUsbEyeDiagram(_arg044, _arg15, _arg24);
                    reply.writeNoException();
                    reply.writeInt(_result129);
                    return true;
                case 130:
                    int _arg045 = data.readInt();
                    data.enforceNoDataAvail();
                    java.lang.String _result130 = getUsbCurrentEyeDiagram(_arg045);
                    reply.writeNoException();
                    reply.writeString(_result130);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements vendor.oplus.hardware.charger.ICharger {
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

            @Override // vendor.oplus.hardware.charger.ICharger
            public int VolDividerIcWorkModeSet(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method VolDividerIcWorkModeSet is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int chgExchangeMesgInit() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method chgExchangeMesgInit is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int chgExchangeSohMesgInit() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method chgExchangeSohMesgInit is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getAcType() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getAcType is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattAuthenticate() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattAuthenticate is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattPPSChgIng() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattPPSChgIng is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattPPSChgPower() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattPPSChgPower is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getBattParamNoplug() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattParamNoplug is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattShortIcOtpStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattShortIcOtpStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattSubCurrent() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattSubCurrent is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBattVoocChgIng() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattVoocChgIng is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBatteryVoltageNow() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBatteryVoltageNow is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getBccCsvData() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBccCsvData is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBccExpStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBccExpStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getBmsHeatingRunningStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBmsHeatingRunningStatus is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getBmsHeatingStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBmsHeatingStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getChargerControl() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargerControl is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerCoolDown() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargerCoolDown is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerCriticalLog() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargerCriticalLog is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerIdVolt() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargerIdVolt is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargerLog() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargerLog is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getCustomSelectChgMode() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCustomSelectChgMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getDevinfoFastchg() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getDevinfoFastchg is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getFastCharge() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getFastCharge is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getParallelChgMosTestResult() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getParallelChgMosTestResult is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyAcOnline() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyAcOnline is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryCC() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryCC is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryCurrentNow() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryCurrentNow is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryFcc() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryFcc is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryHmac() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryHmac is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryLevel() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryLevel is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryNotify() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryNotify is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryPchg() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryPchg is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryPchgResetCount() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryPchgResetCount is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryRm() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryRm is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryShortFeature() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryShortFeature is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryShortStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryShortStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getPsyBatteryStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryStatus is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyBatteryTemp() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatteryTemp is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyChargeTech() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyChargeTech is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyFastChgType() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyFastChgType is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyInputCurrent() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyInputCurrent is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyOtgOnline() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyOtgOnline is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyOtgSwitch() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyOtgSwitch is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyPcPortOnline() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyPcPortOnline is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyQGVbatDeviation() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyQGVbatDeviation is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyTypeOrientation() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyTypeOrientation is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyUsbOnline() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyUsbOnline is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getPsyUsbStatus() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyUsbStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getPsyWirelessRX() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyWirelessRX is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getPsyWirelessRxVersion() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyWirelessRxVersion is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getPsyWirelessTX() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyWirelessTX is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getPsyWirelessTxVersion() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyWirelessTxVersion is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getQgVbatDeviation() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getQgVbatDeviation is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getQuickModeGain() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getQuickModeGain is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getReserveSocDebug() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getReserveSocDebug is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getSmartChgMode() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getSmartChgMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getUIsohValue() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getUIsohValue is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getUisohDebugParameterInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getUisohDebugParameterInfo is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getUsbInputCurrentNow() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getUsbInputCurrentNow is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getUsbPrimalType() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getUsbPrimalType is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWiredOtgOnline() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWiredOtgOnline is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessAdapterPower() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessAdapterPower is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessCapacity() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessCapacity is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessChargePumpEn() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessChargePumpEn is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessCurrentNow() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessCurrentNow is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getWirelessDeviated() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessDeviated is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessOnline() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessOnline is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessPenPresent() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessPenPresent is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessPtmcId() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessPtmcId is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessRXEnable() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessRXEnable is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessRealType() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessRealType is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getWirelessTXEnable() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessTXEnable is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessUserSleepMode() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessUserSleepMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getWirelessVoltageNow() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getWirelessVoltageNow is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String healthd_update_ui_soc_decimal() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method healthd_update_ui_soc_decimal is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int nightstandby(int status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method nightstandby is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int notifyScreenStatus(int status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method notifyScreenStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String queryChargeInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method queryChargeInfo is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargeEMMode(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargeEMMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerControl(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargerControl is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerCoolDown(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargerCoolDown is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerCriticalLog(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargerCriticalLog is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerCycle(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargerCycle is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerFactoryModeTest(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargerFactoryModeTest is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargerLog(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargerLog is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgStatusToBcc(int status) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChgStatusToBcc is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setCustomSelectChgMode(int mode, boolean enable) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeBoolean(enable);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setCustomSelectChgMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setFastchgFwUpdate(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setFastchgFwUpdate is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setPsyMmiChgEn(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setPsyMmiChgEn is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setPsyOtgSwitch(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setPsyOtgSwitch is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setReserveSocDebug(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setReserveSocDebug is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setShipMode(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setShipMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSmartChgMode(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setSmartChgMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSmartCoolDown(int coolDown, int normalCoolDown, java.lang.String pkgName) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(coolDown);
                    _data.writeInt(normalCoolDown);
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setSmartCoolDown is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setTbattPwrOff(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setTbattPwrOff is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setUisohDebugInfo(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setUisohDebugInfo is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setUsbPrimalType(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setUsbPrimalType is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessChargePumpEn(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessChargePumpEn is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessFtmMode(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessFtmMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessIconDelay(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessIconDelay is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessIdtAdcTest(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessIdtAdcTest is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessPenSoc(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessPenSoc is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessRXEnable(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessRXEnable is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessTXEnable(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessTXEnable is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWirelessUserSleepMode(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWirelessUserSleepMode is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setWlsThirdPartitionInfo(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setWlsThirdPartitionInfo is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public vendor.oplus.hardware.charger.testKitFeatureTestResult testKitFeatureTest(int index) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(index);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method testKitFeatureTest is unimplemented.");
                    }
                    _reply.readException();
                    vendor.oplus.hardware.charger.testKitFeatureTestResult _result = (vendor.oplus.hardware.charger.testKitFeatureTestResult) _reply.readTypedObject(vendor.oplus.hardware.charger.testKitFeatureTestResult.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String testKitGetFeatureList() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method testKitGetFeatureList is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String testKitGetFeatureName(int index) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(index);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method testKitGetFeatureName is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int testKitGetFeatureNum() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method testKitGetFeatureNum is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int updateUiSohToPartion() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method updateUiSohToPartion is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String queryWlsPencilInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method queryWlsPencilInfo is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getChgOlcConfig() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChgOlcConfig is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgOlcConfig(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChgOlcConfig is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSuperEnduranceStatus(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setSuperEnduranceStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setSuperEnduranceCount(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setSuperEnduranceCount is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setBobStatus(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setBobStatus is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setPsySlowChgEn(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setPsySlowChgEn is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getCpVbatDeviation() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getCpVbatDeviation is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setBatteryLogPush(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setBatteryLogPush is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int getChargingModeInGsmCall() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChargingModeInGsmCall is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChargingModeInGsmCall(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChargingModeInGsmCall is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgRusConfig(java.lang.String data) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(data);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChgRusConfig is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getPsyBatterySN() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getPsyBatterySN is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getBattGaugeInfo() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getBattGaugeInfo is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setChgConfig(int flag, java.lang.String extra, int callname) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(flag);
                    _data.writeString(extra);
                    _data.writeInt(callname);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setChgConfig is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getChgConfig(int flag, java.lang.String extra, int callname) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(flag);
                    _data.writeString(extra);
                    _data.writeInt(callname);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getChgConfig is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public int setUsbEyeDiagram(int model, java.lang.String eyeDiagram, boolean isDefaultEyeDiagram) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(model);
                    _data.writeString(eyeDiagram);
                    _data.writeBoolean(isDefaultEyeDiagram);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method setUsbEyeDiagram is unimplemented.");
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
            public java.lang.String getUsbCurrentEyeDiagram(int model) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain(asBinder());
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(model);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status) {
                        throw new android.os.RemoteException("Method getUsbCurrentEyeDiagram is unimplemented.");
                    }
                    _reply.readException();
                    java.lang.String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // vendor.oplus.hardware.charger.ICharger
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

            @Override // vendor.oplus.hardware.charger.ICharger
            public synchronized java.lang.String getInterfaceHash() throws android.os.RemoteException {
                if ("-1".equals(this.mCachedHash)) {
                    android.os.Parcel data = android.os.Parcel.obtain(asBinder());
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    try {
                        data.writeInterfaceToken(DESCRIPTOR);
                        this.mRemote.transact(vendor.oplus.hardware.charger.ICharger.Stub.TRANSACTION_getInterfaceHash, data, reply, 0);
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
