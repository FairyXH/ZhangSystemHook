package android.net;

/* JADX INFO: loaded from: classes.dex */
interface NetworkFactoryShim {
    void dump(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr);

    android.os.Looper getLooper();

    android.net.NetworkProvider getProvider();

    int getRequestCount();

    int getSerialNumber();

    android.os.Message obtainMessage(int i, int i2, int i3, java.lang.Object obj);

    void reevaluateAllRequests();

    void register(java.lang.String str);

    void releaseRequestAsUnfulfillableByAnyFactory(android.net.NetworkRequest networkRequest);

    void setCapabilityFilter(android.net.NetworkCapabilities networkCapabilities);

    void setScoreFilter(int i);

    void setScoreFilter(android.net.NetworkScore networkScore);

    void terminate();

    default void registerIgnoringScore(java.lang.String logTag) {
        throw new java.lang.UnsupportedOperationException();
    }
}
