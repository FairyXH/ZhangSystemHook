package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssLPSocExtImpl implements com.android.server.location.gnss.IGnssLocationProviderSocExt {
    private static final int DEBUG_LEVEL_DEBUG = 4;
    private static final int DEBUG_LEVEL_ERROR = 1;
    private static final int DEBUG_LEVEL_IMPORTANT = 3;
    private static final int DEBUG_LEVEL_NONE = 0;
    private static final int DEBUG_LEVEL_VERBOSE = 5;
    private static final int DEBUG_LEVEL_WARNING = 2;
    private static final int GPS_START_LOG = 2048;
    private static final int GPS_START_LOG_LEVEL_3 = 16384;
    private static final int GPS_START_LOG_LEVEL_4 = 8192;
    private static final int GPS_STOP_LOG = 4096;
    private static final java.lang.String TAG = "GnssLPSocExtImpl";
    private com.android.server.location.gnss.GnssLocationProvider mGnssLocationProvider;
    private boolean mDebug = false;
    private final com.android.server.location.gnss.GnssLPSocExtImpl.DataSubscriptionChangedCB mDataSubscriptionChangedCB = new com.android.server.location.gnss.GnssLPSocExtImpl.DataSubscriptionChangedCB();
    private android.content.Context mContext = null;
    private com.android.server.location.interfaces.IOplusLBSMainClass mOplusLbsClass = com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT;

    public GnssLPSocExtImpl(java.lang.Object gnssLocationProvider) {
        this.mGnssLocationProvider = null;
        this.mGnssLocationProvider = (com.android.server.location.gnss.GnssLocationProvider) gnssLocationProvider;
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void init(android.content.Context context, android.os.Handler handler, com.android.server.location.gnss.NtpNetworkTimeHelper ntpNetworkTimeHelper) {
        this.mContext = context;
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void onReportSvStatus(android.location.GnssStatus gnssStatus) {
        if (this.mDebug) {
            logSvStatus(gnssStatus);
        }
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public int onDeleteAidingData(android.os.Bundle extras, int flag) {
        return flag;
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void onRequestLocation(com.android.server.location.gnss.hal.GnssNative gnssNative) {
    }

    @Override // com.android.server.location.gnss.IGnssLocationProviderSocExt
    public void onGnssLocationProviderInitialize() {
        if (this.mContext == null) {
            android.util.Log.e(TAG, "on loc provider init, context null");
            return;
        }
        this.mOplusLbsClass = (com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext);
        this.mOplusLbsClass.registerLbsConfigListener(new com.android.server.location.interfaces.IOplusConfigListener() { // from class: com.android.server.location.gnss.GnssLPSocExtImpl.1
            @Override // com.android.server.location.interfaces.IOplusConfigListener
            public void onDebugLevelChanged(int level) {
                com.android.server.location.gnss.GnssLPSocExtImpl.this.mDebug = level >= 5;
                android.util.Log.i(com.android.server.location.gnss.GnssLPSocExtImpl.TAG, "onDebugLevelChanged, level: " + level + ", D: " + com.android.server.location.gnss.GnssLPSocExtImpl.this.mDebug);
                int qcDebugFlag = 4096;
                if (level >= 5) {
                    qcDebugFlag = 2048;
                } else if (level >= 4) {
                    qcDebugFlag = 8192;
                } else if (level >= 3) {
                    qcDebugFlag = 16384;
                }
                com.android.server.location.gnss.GnssLPSocExtImpl.this.mGnssLocationProvider.getGnssLocationProviderWrapper().getGnssNative().deleteAidingData(qcDebugFlag);
            }
        });
        android.telephony.TelephonyManager phone = (android.telephony.TelephonyManager) this.mContext.getSystemService(com.android.server.autofill.HintsHelper.AUTOFILL_HINT_PHONE);
        if (phone != null) {
            java.util.concurrent.Executor executor = com.android.server.FgThread.getExecutor();
            phone.registerTelephonyCallback(executor, this.mDataSubscriptionChangedCB);
        }
    }

    private class DataSubscriptionChangedCB extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener {
        private DataSubscriptionChangedCB() {
        }

        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int subId) {
            android.util.Log.d(com.android.server.location.gnss.GnssLPSocExtImpl.TAG, "dds active id " + subId);
            com.android.server.location.gnss.GnssLPSocExtImpl.this.mOplusLbsClass.onActiveDataSubscriptionIdChanged();
        }
    }

    private void logSvStatus(android.location.GnssStatus gnssStatus) {
        if (gnssStatus != null) {
            for (int i = 0; i < gnssStatus.getSatelliteCount(); i++) {
                java.lang.String str = "";
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("svid: ").append(gnssStatus.getSvid(i)).append(" cn0: ").append(gnssStatus.getCn0DbHz(i)).append(" basebandCn0: ").append(gnssStatus.getBasebandCn0DbHz(i)).append(" elev: ").append(gnssStatus.getElevationDegrees(i)).append(" azimuth: ").append(gnssStatus.getAzimuthDegrees(i)).append(" carrier frequency: ").append(gnssStatus.getCarrierFrequencyHz(i)).append(gnssStatus.hasEphemerisData(i) ? " E" : "  ").append(gnssStatus.hasAlmanacData(i) ? " A" : "  ").append(gnssStatus.usedInFix(i) ? "U" : "").append(gnssStatus.hasCarrierFrequencyHz(i) ? "F" : "");
                if (gnssStatus.hasBasebandCn0DbHz(i)) {
                    str = "B";
                }
                android.util.Log.v(TAG, sbAppend.append(str).toString());
            }
        }
    }
}
