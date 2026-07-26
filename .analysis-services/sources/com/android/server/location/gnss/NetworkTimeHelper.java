package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
abstract class NetworkTimeHelper {
    static final boolean USE_TIME_DETECTOR_IMPL = true;

    interface InjectTimeCallback {
        void injectTime(long j, long j2, int i);
    }

    abstract void demandUtcTimeInjection();

    abstract void dump(java.io.PrintWriter printWriter);

    abstract void onNetworkAvailable();

    abstract void setPeriodicTimeInjectionMode(boolean z);

    NetworkTimeHelper() {
    }

    static com.android.server.location.gnss.NetworkTimeHelper create(android.content.Context context, android.os.Looper looper, com.android.server.location.gnss.NetworkTimeHelper.InjectTimeCallback injectTimeCallback) {
        com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.Environment environment = new com.android.server.location.gnss.TimeDetectorNetworkTimeHelper.EnvironmentImpl(looper);
        return new com.android.server.location.gnss.TimeDetectorNetworkTimeHelper(environment, injectTimeCallback);
    }
}
