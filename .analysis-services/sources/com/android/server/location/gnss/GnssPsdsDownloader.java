package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
class GnssPsdsDownloader {
    static final int LONG_TERM_PSDS_SERVER_INDEX = 1;
    private static final long MAXIMUM_CONTENT_LENGTH_BYTES = 1000000;
    private static final int MAX_PSDS_TYPE_INDEX = 3;
    private static final int NORMAL_PSDS_SERVER_INDEX = 2;
    static final long PSDS_INTERVAL = 86400000;
    private static final int REALTIME_PSDS_SERVER_INDEX = 3;
    private final java.lang.String[] mLongTermPsdsServers;
    private int mNextServerIndex;
    private final java.lang.String[] mPsdsServers;
    private static final java.lang.String TAG = "GnssPsdsDownloader";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final int CONNECTION_TIMEOUT_MS = (int) java.util.concurrent.TimeUnit.SECONDS.toMillis(30);
    private static final int READ_TIMEOUT_MS = (int) java.util.concurrent.TimeUnit.SECONDS.toMillis(60);

    GnssPsdsDownloader(java.util.Properties properties) {
        java.lang.String longTermPsdsServer1 = properties.getProperty("LONGTERM_PSDS_SERVER_1");
        java.lang.String longTermPsdsServer2 = properties.getProperty("LONGTERM_PSDS_SERVER_2");
        java.lang.String longTermPsdsServer3 = properties.getProperty("LONGTERM_PSDS_SERVER_3");
        int count = longTermPsdsServer1 != null ? 0 + 1 : 0;
        count = longTermPsdsServer2 != null ? count + 1 : count;
        count = longTermPsdsServer3 != null ? count + 1 : count;
        if (count == 0) {
            android.util.Log.e(TAG, "No Long-Term PSDS servers were specified in the GnssConfiguration");
            this.mLongTermPsdsServers = null;
        } else {
            this.mLongTermPsdsServers = new java.lang.String[count];
            int count2 = 0;
            if (longTermPsdsServer1 != null) {
                this.mLongTermPsdsServers[0] = longTermPsdsServer1;
                count2 = 0 + 1;
            }
            if (longTermPsdsServer2 != null) {
                this.mLongTermPsdsServers[count2] = longTermPsdsServer2;
                count2++;
            }
            if (longTermPsdsServer3 != null) {
                this.mLongTermPsdsServers[count2] = longTermPsdsServer3;
                count2++;
            }
            java.util.Random random = new java.util.Random();
            this.mNextServerIndex = random.nextInt(count2);
        }
        java.lang.String normalPsdsServer = properties.getProperty("NORMAL_PSDS_SERVER");
        java.lang.String realtimePsdsServer = properties.getProperty("REALTIME_PSDS_SERVER");
        this.mPsdsServers = new java.lang.String[4];
        this.mPsdsServers[2] = normalPsdsServer;
        this.mPsdsServers[3] = realtimePsdsServer;
    }

    byte[] downloadPsdsData(int psdsType) {
        byte[] result = null;
        int startIndex = this.mNextServerIndex;
        if (psdsType == 1 && this.mLongTermPsdsServers == null) {
            return null;
        }
        if (psdsType > 1 && psdsType <= 3 && this.mPsdsServers[psdsType] == null) {
            return null;
        }
        if (psdsType == 1) {
            while (result == null) {
                result = doDownloadWithTrafficAccounted(this.mLongTermPsdsServers[this.mNextServerIndex]);
                this.mNextServerIndex++;
                if (this.mNextServerIndex == this.mLongTermPsdsServers.length) {
                    this.mNextServerIndex = 0;
                }
                if (this.mNextServerIndex == startIndex) {
                    return result;
                }
            }
            return result;
        }
        if (psdsType <= 1 || psdsType > 3) {
            return null;
        }
        byte[] result2 = doDownloadWithTrafficAccounted(this.mPsdsServers[psdsType]);
        return result2;
    }

    private byte[] doDownloadWithTrafficAccounted(java.lang.String url) {
        int oldTag = android.net.TrafficStats.getAndSetThreadStatsTag(-188);
        try {
            byte[] result = doDownload(url);
            return result;
        } finally {
            android.net.TrafficStats.setThreadStatsTag(oldTag);
        }
    }

    private byte[] doDownload(java.lang.String url) {
        if (DEBUG) {
            android.util.Log.d(TAG, "Downloading PSDS data from " + url);
        }
        java.net.HttpURLConnection connection = null;
        try {
            try {
                java.net.HttpURLConnection connection2 = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
                connection2.setRequestProperty("Accept", "*/*, application/vnd.wap.mms-message, application/vnd.wap.sic");
                connection2.setRequestProperty("x-wap-profile", "http://www.openmobilealliance.org/tech/profiles/UAPROF/ccppschema-20021212#");
                connection2.setConnectTimeout(CONNECTION_TIMEOUT_MS);
                connection2.setReadTimeout(READ_TIMEOUT_MS);
                connection2.connect();
                int statusCode = connection2.getResponseCode();
                if (statusCode != 200) {
                    if (DEBUG) {
                        android.util.Log.d(TAG, "HTTP error downloading gnss PSDS: " + statusCode);
                    }
                    if (connection2 != null) {
                        connection2.disconnect();
                    }
                    return null;
                }
                java.io.InputStream in = connection2.getInputStream();
                try {
                    java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    do {
                        int count = in.read(buffer);
                        if (count == -1) {
                            byte[] byteArray = bytes.toByteArray();
                            if (in != null) {
                                in.close();
                            }
                            if (connection2 != null) {
                                connection2.disconnect();
                            }
                            return byteArray;
                        }
                        bytes.write(buffer, 0, count);
                    } while (bytes.size() <= MAXIMUM_CONTENT_LENGTH_BYTES);
                    if (DEBUG) {
                        android.util.Log.d(TAG, "PSDS file too large");
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (connection2 != null) {
                        connection2.disconnect();
                    }
                    return null;
                } catch (java.lang.Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.IOException ioe) {
                if (DEBUG) {
                    android.util.Log.d(TAG, "Error downloading gnss PSDS: ", ioe);
                }
                if (0 != 0) {
                    connection.disconnect();
                }
                return null;
            }
        } catch (java.lang.Throwable th3) {
            if (0 != 0) {
                connection.disconnect();
            }
            throw th3;
        }
    }
}
