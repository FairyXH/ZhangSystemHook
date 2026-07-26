package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public class GnssMetrics {
    private static final int CONVERT_MILLI_TO_MICRO = 1000;
    private static final int DEFAULT_TIME_BETWEEN_FIXES_MILLISECS = 1000;
    private static final double L5_CARRIER_FREQ_RANGE_HIGH_HZ = 1.189E9d;
    private static final double L5_CARRIER_FREQ_RANGE_LOW_HZ = 1.164E9d;
    private static final java.lang.String TAG = "GnssMetrics";
    private static final int VENDOR_SPECIFIC_POWER_MODES_SIZE = 10;
    private boolean[] mConstellationTypes;
    private final com.android.server.location.gnss.hal.GnssNative mGnssNative;
    com.android.server.location.gnss.GnssMetrics.GnssPowerMetrics mGnssPowerMetrics;
    long mL5SvStatusReports;
    long mL5SvStatusReportsUsedInFix;
    com.android.server.location.gnss.GnssMetrics.Statistics mL5TopFourAverageCn0DbmHzReportsStatistics;
    com.android.server.location.gnss.GnssMetrics.Statistics mLocationFailureReportsStatistics;
    private long mLogStartInElapsedRealtimeMs;
    private int mNumL5SvStatus;
    private int mNumL5SvStatusUsedInFix;
    private int mNumSvStatus;
    private int mNumSvStatusUsedInFix;
    com.android.server.location.gnss.GnssMetrics.Statistics mPositionAccuracyMetersReportsStatistics;
    private final android.app.StatsManager mStatsManager;
    long mSvStatusReports;
    long mSvStatusReportsUsedInFix;
    com.android.server.location.gnss.GnssMetrics.Statistics mTimeToFirstFixMilliSReportsStatistics;
    com.android.server.location.gnss.GnssMetrics.Statistics mTopFourAverageCn0DbmHzReportsStatistics;
    private final com.android.server.location.gnss.GnssMetrics.Statistics mLocationFailureStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
    private final com.android.server.location.gnss.GnssMetrics.Statistics mTimeToFirstFixSecStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
    private final com.android.server.location.gnss.GnssMetrics.Statistics mPositionAccuracyMeterStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
    private final com.android.server.location.gnss.GnssMetrics.Statistics mTopFourAverageCn0Statistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
    private final com.android.server.location.gnss.GnssMetrics.Statistics mTopFourAverageCn0StatisticsL5 = new com.android.server.location.gnss.GnssMetrics.Statistics();

    public GnssMetrics(android.content.Context context, com.android.internal.app.IBatteryStats stats, com.android.server.location.gnss.hal.GnssNative gnssNative) {
        this.mGnssNative = gnssNative;
        this.mGnssPowerMetrics = new com.android.server.location.gnss.GnssMetrics.GnssPowerMetrics(stats);
        reset();
        this.mLocationFailureReportsStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
        this.mTimeToFirstFixMilliSReportsStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
        this.mPositionAccuracyMetersReportsStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
        this.mTopFourAverageCn0DbmHzReportsStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
        this.mL5TopFourAverageCn0DbmHzReportsStatistics = new com.android.server.location.gnss.GnssMetrics.Statistics();
        this.mStatsManager = (android.app.StatsManager) context.getSystemService("stats");
        registerGnssStats();
    }

    public void logReceivedLocationStatus(boolean isSuccessful) {
        if (!isSuccessful) {
            this.mLocationFailureStatistics.addItem(1.0d);
            this.mLocationFailureReportsStatistics.addItem(1.0d);
        } else {
            this.mLocationFailureStatistics.addItem(0.0d);
            this.mLocationFailureReportsStatistics.addItem(0.0d);
        }
    }

    public void logMissedReports(int desiredTimeBetweenFixesMilliSeconds, int actualTimeBetweenFixesMilliSeconds) {
        int numReportMissed = (actualTimeBetweenFixesMilliSeconds / java.lang.Math.max(1000, desiredTimeBetweenFixesMilliSeconds)) - 1;
        if (numReportMissed > 0) {
            for (int i = 0; i < numReportMissed; i++) {
                this.mLocationFailureStatistics.addItem(1.0d);
                this.mLocationFailureReportsStatistics.addItem(1.0d);
            }
        }
    }

    public void logTimeToFirstFixMilliSecs(int timeToFirstFixMilliSeconds) {
        this.mTimeToFirstFixSecStatistics.addItem(((double) timeToFirstFixMilliSeconds) / 1000.0d);
        this.mTimeToFirstFixMilliSReportsStatistics.addItem(timeToFirstFixMilliSeconds);
    }

    public void logPositionAccuracyMeters(float positionAccuracyMeters) {
        this.mPositionAccuracyMeterStatistics.addItem(positionAccuracyMeters);
        this.mPositionAccuracyMetersReportsStatistics.addItem(positionAccuracyMeters);
    }

    public void logCn0(android.location.GnssStatus gnssStatus) {
        logCn0L5(gnssStatus);
        if (gnssStatus.getSatelliteCount() == 0) {
            this.mGnssPowerMetrics.reportSignalQuality(null);
            return;
        }
        float[] cn0DbHzs = new float[gnssStatus.getSatelliteCount()];
        for (int i = 0; i < gnssStatus.getSatelliteCount(); i++) {
            cn0DbHzs[i] = gnssStatus.getCn0DbHz(i);
        }
        java.util.Arrays.sort(cn0DbHzs);
        this.mGnssPowerMetrics.reportSignalQuality(cn0DbHzs);
        if (cn0DbHzs.length >= 4 && cn0DbHzs[cn0DbHzs.length - 4] > 0.0d) {
            double top4AvgCn0 = 0.0d;
            for (int i2 = cn0DbHzs.length - 4; i2 < cn0DbHzs.length; i2++) {
                top4AvgCn0 += (double) cn0DbHzs[i2];
            }
            double top4AvgCn02 = top4AvgCn0 / 4.0d;
            this.mTopFourAverageCn0Statistics.addItem(top4AvgCn02);
            this.mTopFourAverageCn0DbmHzReportsStatistics.addItem(1000.0d * top4AvgCn02);
        }
    }

    private static boolean isL5Sv(float carrierFreq) {
        return ((double) carrierFreq) >= L5_CARRIER_FREQ_RANGE_LOW_HZ && ((double) carrierFreq) <= L5_CARRIER_FREQ_RANGE_HIGH_HZ;
    }

    public void logSvStatus(android.location.GnssStatus status) {
        for (int i = 0; i < status.getSatelliteCount(); i++) {
            if (status.hasCarrierFrequencyHz(i)) {
                this.mNumSvStatus++;
                this.mSvStatusReports++;
                boolean isL5 = isL5Sv(status.getCarrierFrequencyHz(i));
                if (isL5) {
                    this.mNumL5SvStatus++;
                    this.mL5SvStatusReports++;
                }
                if (status.usedInFix(i)) {
                    this.mNumSvStatusUsedInFix++;
                    this.mSvStatusReportsUsedInFix++;
                    if (isL5) {
                        this.mNumL5SvStatusUsedInFix++;
                        this.mL5SvStatusReportsUsedInFix++;
                    }
                }
            }
        }
    }

    private void logCn0L5(android.location.GnssStatus gnssStatus) {
        if (gnssStatus.getSatelliteCount() == 0) {
            return;
        }
        java.util.ArrayList<java.lang.Float> l5Cn0DbHzs = new java.util.ArrayList<>(gnssStatus.getSatelliteCount());
        for (int i = 0; i < gnssStatus.getSatelliteCount(); i++) {
            if (isL5Sv(gnssStatus.getCarrierFrequencyHz(i))) {
                l5Cn0DbHzs.add(java.lang.Float.valueOf(gnssStatus.getCn0DbHz(i)));
            }
        }
        int i2 = l5Cn0DbHzs.size();
        if (i2 < 4) {
            return;
        }
        java.util.Collections.sort(l5Cn0DbHzs);
        if (l5Cn0DbHzs.get(l5Cn0DbHzs.size() - 4).floatValue() > 0.0d) {
            double top4AvgCn0 = 0.0d;
            for (int i3 = l5Cn0DbHzs.size() - 4; i3 < l5Cn0DbHzs.size(); i3++) {
                top4AvgCn0 += (double) l5Cn0DbHzs.get(i3).floatValue();
            }
            double top4AvgCn02 = top4AvgCn0 / 4.0d;
            this.mTopFourAverageCn0StatisticsL5.addItem(top4AvgCn02);
            this.mL5TopFourAverageCn0DbmHzReportsStatistics.addItem(1000.0d * top4AvgCn02);
        }
    }

    public void logConstellationType(int constellationType) {
        if (constellationType >= this.mConstellationTypes.length) {
            android.util.Log.e(TAG, "Constellation type " + constellationType + " is not valid.");
        } else {
            this.mConstellationTypes[constellationType] = true;
        }
    }

    public java.lang.String dumpGnssMetricsAsProtoString() {
        com.android.internal.location.nano.GnssLogsProto.GnssLog msg = new com.android.internal.location.nano.GnssLogsProto.GnssLog();
        if (this.mLocationFailureStatistics.getCount() > 0) {
            msg.numLocationReportProcessed = this.mLocationFailureStatistics.getCount();
            msg.percentageLocationFailure = (int) (this.mLocationFailureStatistics.getMean() * 100.0d);
        }
        if (this.mTimeToFirstFixSecStatistics.getCount() > 0) {
            msg.numTimeToFirstFixProcessed = this.mTimeToFirstFixSecStatistics.getCount();
            msg.meanTimeToFirstFixSecs = (int) this.mTimeToFirstFixSecStatistics.getMean();
            msg.standardDeviationTimeToFirstFixSecs = (int) this.mTimeToFirstFixSecStatistics.getStandardDeviation();
        }
        if (this.mPositionAccuracyMeterStatistics.getCount() > 0) {
            msg.numPositionAccuracyProcessed = this.mPositionAccuracyMeterStatistics.getCount();
            msg.meanPositionAccuracyMeters = (int) this.mPositionAccuracyMeterStatistics.getMean();
            msg.standardDeviationPositionAccuracyMeters = (int) this.mPositionAccuracyMeterStatistics.getStandardDeviation();
        }
        if (this.mTopFourAverageCn0Statistics.getCount() > 0) {
            msg.numTopFourAverageCn0Processed = this.mTopFourAverageCn0Statistics.getCount();
            msg.meanTopFourAverageCn0DbHz = this.mTopFourAverageCn0Statistics.getMean();
            msg.standardDeviationTopFourAverageCn0DbHz = this.mTopFourAverageCn0Statistics.getStandardDeviation();
        }
        if (this.mNumSvStatus > 0) {
            msg.numSvStatusProcessed = this.mNumSvStatus;
        }
        if (this.mNumL5SvStatus > 0) {
            msg.numL5SvStatusProcessed = this.mNumL5SvStatus;
        }
        if (this.mNumSvStatusUsedInFix > 0) {
            msg.numSvStatusUsedInFix = this.mNumSvStatusUsedInFix;
        }
        if (this.mNumL5SvStatusUsedInFix > 0) {
            msg.numL5SvStatusUsedInFix = this.mNumL5SvStatusUsedInFix;
        }
        if (this.mTopFourAverageCn0StatisticsL5.getCount() > 0) {
            msg.numL5TopFourAverageCn0Processed = this.mTopFourAverageCn0StatisticsL5.getCount();
            msg.meanL5TopFourAverageCn0DbHz = this.mTopFourAverageCn0StatisticsL5.getMean();
            msg.standardDeviationL5TopFourAverageCn0DbHz = this.mTopFourAverageCn0StatisticsL5.getStandardDeviation();
        }
        msg.powerMetrics = this.mGnssPowerMetrics.buildProto();
        msg.hardwareRevision = android.os.SystemProperties.get("ro.boot.revision", "");
        java.lang.String s = android.util.Base64.encodeToString(com.android.internal.location.nano.GnssLogsProto.GnssLog.toByteArray(msg), 0);
        reset();
        return s;
    }

    public java.lang.String dumpGnssMetricsAsText() {
        java.lang.StringBuilder s = new java.lang.StringBuilder();
        s.append("GNSS_KPI_START").append('\n');
        s.append("  KPI logging start time: ");
        android.util.TimeUtils.formatDuration(this.mLogStartInElapsedRealtimeMs, s);
        s.append("\n");
        s.append("  KPI logging end time: ");
        android.util.TimeUtils.formatDuration(android.os.SystemClock.elapsedRealtime(), s);
        s.append("\n");
        s.append("  Number of location reports: ").append(this.mLocationFailureStatistics.getCount()).append("\n");
        if (this.mLocationFailureStatistics.getCount() > 0) {
            s.append("  Percentage location failure: ").append(this.mLocationFailureStatistics.getMean() * 100.0d).append("\n");
        }
        s.append("  Number of TTFF reports: ").append(this.mTimeToFirstFixSecStatistics.getCount()).append("\n");
        if (this.mTimeToFirstFixSecStatistics.getCount() > 0) {
            s.append("  TTFF mean (sec): ").append(this.mTimeToFirstFixSecStatistics.getMean()).append("\n");
            s.append("  TTFF standard deviation (sec): ").append(this.mTimeToFirstFixSecStatistics.getStandardDeviation()).append("\n");
        }
        s.append("  Number of position accuracy reports: ").append(this.mPositionAccuracyMeterStatistics.getCount()).append("\n");
        if (this.mPositionAccuracyMeterStatistics.getCount() > 0) {
            s.append("  Position accuracy mean (m): ").append(this.mPositionAccuracyMeterStatistics.getMean()).append("\n");
            s.append("  Position accuracy standard deviation (m): ").append(this.mPositionAccuracyMeterStatistics.getStandardDeviation()).append("\n");
        }
        s.append("  Number of CN0 reports: ").append(this.mTopFourAverageCn0Statistics.getCount()).append("\n");
        if (this.mTopFourAverageCn0Statistics.getCount() > 0) {
            s.append("  Top 4 Avg CN0 mean (dB-Hz): ").append(this.mTopFourAverageCn0Statistics.getMean()).append("\n");
            s.append("  Top 4 Avg CN0 standard deviation (dB-Hz): ").append(this.mTopFourAverageCn0Statistics.getStandardDeviation()).append("\n");
        }
        s.append("  Total number of sv status messages processed: ").append(this.mNumSvStatus).append("\n");
        s.append("  Total number of L5 sv status messages processed: ").append(this.mNumL5SvStatus).append("\n");
        s.append("  Total number of sv status messages processed, where sv is used in fix: ").append(this.mNumSvStatusUsedInFix).append("\n");
        s.append("  Total number of L5 sv status messages processed, where sv is used in fix: ").append(this.mNumL5SvStatusUsedInFix).append("\n");
        s.append("  Number of L5 CN0 reports: ").append(this.mTopFourAverageCn0StatisticsL5.getCount()).append("\n");
        if (this.mTopFourAverageCn0StatisticsL5.getCount() > 0) {
            s.append("  L5 Top 4 Avg CN0 mean (dB-Hz): ").append(this.mTopFourAverageCn0StatisticsL5.getMean()).append("\n");
            s.append("  L5 Top 4 Avg CN0 standard deviation (dB-Hz): ").append(this.mTopFourAverageCn0StatisticsL5.getStandardDeviation()).append("\n");
        }
        s.append("  Used-in-fix constellation types: ");
        for (int i = 0; i < this.mConstellationTypes.length; i++) {
            if (this.mConstellationTypes[i]) {
                s.append(android.location.GnssStatus.constellationTypeToString(i)).append(" ");
            }
        }
        s.append("\n");
        s.append("GNSS_KPI_END").append("\n");
        android.os.connectivity.GpsBatteryStats stats = this.mGnssPowerMetrics.getGpsBatteryStats();
        if (stats != null) {
            s.append("Power Metrics").append("\n");
            s.append("  Time on battery (min): ").append(stats.getLoggingDurationMs() / 60000.0d).append("\n");
            long[] t = stats.getTimeInGpsSignalQualityLevel();
            if (t != null && t.length == 2) {
                s.append("  Amount of time (while on battery) Top 4 Avg CN0 > 20.0 dB-Hz (min): ").append(t[1] / 60000.0d).append("\n");
                s.append("  Amount of time (while on battery) Top 4 Avg CN0 <= 20.0 dB-Hz (min): ").append(t[0] / 60000.0d).append("\n");
            }
            s.append("  Energy consumed while on battery (mAh): ").append(stats.getEnergyConsumedMaMs() / 3600000.0d).append("\n");
        }
        s.append("Hardware Version: ").append(android.os.SystemProperties.get("ro.boot.revision", "")).append("\n");
        return s.toString();
    }

    private void reset() {
        this.mLogStartInElapsedRealtimeMs = android.os.SystemClock.elapsedRealtime();
        this.mLocationFailureStatistics.reset();
        this.mTimeToFirstFixSecStatistics.reset();
        this.mPositionAccuracyMeterStatistics.reset();
        this.mTopFourAverageCn0Statistics.reset();
        resetConstellationTypes();
        this.mTopFourAverageCn0StatisticsL5.reset();
        this.mNumSvStatus = 0;
        this.mNumL5SvStatus = 0;
        this.mNumSvStatusUsedInFix = 0;
        this.mNumL5SvStatusUsedInFix = 0;
    }

    public void resetConstellationTypes() {
        this.mConstellationTypes = new boolean[8];
    }

    private static class Statistics {
        private int mCount;
        private long mLongSum;
        private double mSum;
        private double mSumSquare;

        Statistics() {
        }

        public synchronized void reset() {
            this.mCount = 0;
            this.mSum = 0.0d;
            this.mSumSquare = 0.0d;
            this.mLongSum = 0L;
        }

        public synchronized void addItem(double item) {
            this.mCount++;
            this.mSum += item;
            this.mSumSquare += item * item;
            this.mLongSum = (long) (this.mLongSum + item);
        }

        public synchronized int getCount() {
            return this.mCount;
        }

        public synchronized double getMean() {
            return this.mSum / ((double) this.mCount);
        }

        public synchronized double getStandardDeviation() {
            double m = this.mSum / ((double) this.mCount);
            double m2 = m * m;
            double v = this.mSumSquare / ((double) this.mCount);
            if (v <= m2) {
                return 0.0d;
            }
            return java.lang.Math.sqrt(v - m2);
        }

        public synchronized long getLongSum() {
            return this.mLongSum;
        }
    }

    private class GnssPowerMetrics {
        public static final double POOR_TOP_FOUR_AVG_CN0_THRESHOLD_DB_HZ = 20.0d;
        private static final double REPORTING_THRESHOLD_DB_HZ = 1.0d;
        private final com.android.internal.app.IBatteryStats mBatteryStats;
        private double mLastAverageCn0 = -100.0d;
        private int mLastSignalLevel = -1;

        GnssPowerMetrics(com.android.internal.app.IBatteryStats stats) {
            this.mBatteryStats = stats;
        }

        public com.android.internal.location.nano.GnssLogsProto.PowerMetrics buildProto() {
            com.android.internal.location.nano.GnssLogsProto.PowerMetrics p = new com.android.internal.location.nano.GnssLogsProto.PowerMetrics();
            android.os.connectivity.GpsBatteryStats stats = com.android.server.location.gnss.GnssMetrics.this.mGnssPowerMetrics.getGpsBatteryStats();
            if (stats != null) {
                p.loggingDurationMs = stats.getLoggingDurationMs();
                p.energyConsumedMah = stats.getEnergyConsumedMaMs() / 3600000.0d;
                long[] t = stats.getTimeInGpsSignalQualityLevel();
                p.timeInSignalQualityLevelMs = new long[t.length];
                java.lang.System.arraycopy(t, 0, p.timeInSignalQualityLevelMs, 0, t.length);
            }
            return p;
        }

        public android.os.connectivity.GpsBatteryStats getGpsBatteryStats() {
            try {
                return this.mBatteryStats.getGpsBatteryStats();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.location.gnss.GnssMetrics.TAG, e);
                return null;
            }
        }

        public void reportSignalQuality(float[] sortedCn0DbHzs) {
            double avgCn0 = 0.0d;
            if (sortedCn0DbHzs != null && sortedCn0DbHzs.length > 0) {
                for (int i = java.lang.Math.max(0, sortedCn0DbHzs.length - 4); i < sortedCn0DbHzs.length; i++) {
                    avgCn0 += (double) sortedCn0DbHzs[i];
                }
                int i2 = sortedCn0DbHzs.length;
                avgCn0 /= (double) java.lang.Math.min(i2, 4);
            }
            if (java.lang.Math.abs(avgCn0 - this.mLastAverageCn0) < REPORTING_THRESHOLD_DB_HZ) {
                return;
            }
            int signalLevel = getSignalLevel(avgCn0);
            if (signalLevel != this.mLastSignalLevel) {
                com.android.internal.util.FrameworkStatsLog.write(69, signalLevel);
                this.mLastSignalLevel = signalLevel;
            }
            try {
                this.mBatteryStats.noteGpsSignalQuality(signalLevel);
                this.mLastAverageCn0 = avgCn0;
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.location.gnss.GnssMetrics.TAG, e);
            }
        }

        private int getSignalLevel(double cn0) {
            if (cn0 > 20.0d) {
                return 1;
            }
            return 0;
        }
    }

    private void registerGnssStats() {
        com.android.server.location.gnss.GnssMetrics.StatsPullAtomCallbackImpl pullAtomCallback = new com.android.server.location.gnss.GnssMetrics.StatsPullAtomCallbackImpl();
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.GNSS_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, pullAtomCallback);
        this.mStatsManager.setPullAtomCallback(com.android.internal.util.FrameworkStatsLog.GNSS_POWER_STATS, (android.app.StatsManager.PullAtomMetadata) null, com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, pullAtomCallback);
    }

    private class StatsPullAtomCallbackImpl implements android.app.StatsManager.StatsPullAtomCallback {
        StatsPullAtomCallbackImpl() {
        }

        public int onPullAtom(int atomTag, java.util.List<android.util.StatsEvent> data) {
            switch (atomTag) {
                case com.android.internal.util.FrameworkStatsLog.GNSS_STATS /* 10074 */:
                    return com.android.server.location.gnss.GnssMetrics.this.pullGnssStats(atomTag, data);
                case com.android.internal.util.FrameworkStatsLog.GNSS_POWER_STATS /* 10101 */:
                    return com.android.server.location.gnss.GnssMetrics.this.pullGnssPowerStats(atomTag, data);
                default:
                    throw new java.lang.UnsupportedOperationException("Unknown tagId = " + atomTag);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullGnssStats(int atomTag, java.util.List<android.util.StatsEvent> data) {
        data.add(com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, this.mLocationFailureReportsStatistics.getCount(), this.mLocationFailureReportsStatistics.getLongSum(), this.mTimeToFirstFixMilliSReportsStatistics.getCount(), this.mTimeToFirstFixMilliSReportsStatistics.getLongSum(), this.mPositionAccuracyMetersReportsStatistics.getCount(), this.mPositionAccuracyMetersReportsStatistics.getLongSum(), this.mTopFourAverageCn0DbmHzReportsStatistics.getCount(), this.mTopFourAverageCn0DbmHzReportsStatistics.getLongSum(), this.mL5TopFourAverageCn0DbmHzReportsStatistics.getCount(), this.mL5TopFourAverageCn0DbmHzReportsStatistics.getLongSum(), this.mSvStatusReports, this.mSvStatusReportsUsedInFix, this.mL5SvStatusReports, this.mL5SvStatusReportsUsedInFix));
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int pullGnssPowerStats(int atomTag, java.util.List<android.util.StatsEvent> data) {
        com.android.server.location.gnss.GnssPowerStats powerStats = this.mGnssNative.requestPowerStatsBlocking();
        if (powerStats == null) {
            return 1;
        }
        data.add(createPowerStatsEvent(atomTag, powerStats));
        return 0;
    }

    private static android.util.StatsEvent createPowerStatsEvent(int atomTag, com.android.server.location.gnss.GnssPowerStats powerStats) {
        double[] otherModesEnergyMilliJoule = new double[10];
        double[] tempGnssPowerStatsOtherModes = powerStats.getOtherModesEnergyMilliJoule();
        java.lang.System.arraycopy(tempGnssPowerStatsOtherModes, 0, otherModesEnergyMilliJoule, 0, java.lang.Math.min(tempGnssPowerStatsOtherModes.length, 10));
        return com.android.internal.util.FrameworkStatsLog.buildStatsEvent(atomTag, (long) powerStats.getElapsedRealtimeUncertaintyNanos(), (long) (powerStats.getTotalEnergyMilliJoule() * 1000.0d), (long) (powerStats.getSinglebandTrackingModeEnergyMilliJoule() * 1000.0d), (long) (powerStats.getMultibandTrackingModeEnergyMilliJoule() * 1000.0d), (long) (powerStats.getSinglebandAcquisitionModeEnergyMilliJoule() * 1000.0d), (long) (powerStats.getMultibandAcquisitionModeEnergyMilliJoule() * 1000.0d), (long) (otherModesEnergyMilliJoule[0] * 1000.0d), (long) (otherModesEnergyMilliJoule[1] * 1000.0d), (long) (otherModesEnergyMilliJoule[2] * 1000.0d), (long) (otherModesEnergyMilliJoule[3] * 1000.0d), (long) (otherModesEnergyMilliJoule[4] * 1000.0d), (long) (otherModesEnergyMilliJoule[5] * 1000.0d), (long) (otherModesEnergyMilliJoule[6] * 1000.0d), (long) (otherModesEnergyMilliJoule[7] * 1000.0d), (long) (otherModesEnergyMilliJoule[8] * 1000.0d), (long) (otherModesEnergyMilliJoule[9] * 1000.0d));
    }
}
