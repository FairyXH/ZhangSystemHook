package android.net;

/* JADX INFO: loaded from: classes.dex */
public class NetworkFactory {
    public static final int CMD_CANCEL_REQUEST = 2;
    public static final int CMD_REQUEST_NETWORK = 1;
    static final boolean DBG = true;
    static final boolean VDBG = false;
    private final java.lang.String LOG_TAG;
    final android.net.NetworkFactoryShim mImpl;
    private int mRefCount = 0;

    public NetworkFactory(android.os.Looper looper, android.content.Context context, java.lang.String logTag, android.net.NetworkCapabilities filter) {
        this.LOG_TAG = logTag;
        if (com.android.modules.utils.build.SdkLevel.isAtLeastS()) {
            this.mImpl = new android.net.NetworkFactoryImpl(this, looper, context, filter);
        } else {
            this.mImpl = new android.net.NetworkFactoryLegacyImpl(this, looper, context, filter);
        }
    }

    public android.os.Message obtainMessage(int what, int arg1, int arg2, java.lang.Object obj) {
        return this.mImpl.obtainMessage(what, arg1, arg2, obj);
    }

    public final android.os.Looper getLooper() {
        return this.mImpl.getLooper();
    }

    public void register() {
        this.mImpl.register(this.LOG_TAG);
    }

    public void registerIgnoringScore() {
        this.mImpl.registerIgnoringScore(this.LOG_TAG);
    }

    public void terminate() {
        this.mImpl.terminate();
    }

    protected final void reevaluateAllRequests() {
        this.mImpl.reevaluateAllRequests();
    }

    public boolean acceptRequest(android.net.NetworkRequest request) {
        return true;
    }

    protected void releaseRequestAsUnfulfillableByAnyFactory(android.net.NetworkRequest r) {
        this.mImpl.releaseRequestAsUnfulfillableByAnyFactory(r);
    }

    protected void startNetwork() {
    }

    protected void stopNetwork() {
    }

    protected void needNetworkFor(android.net.NetworkRequest networkRequest) {
        int i = this.mRefCount + 1;
        this.mRefCount = i;
        if (i == 1) {
            startNetwork();
        }
    }

    protected void releaseNetworkFor(android.net.NetworkRequest networkRequest) {
        int i = this.mRefCount - 1;
        this.mRefCount = i;
        if (i == 0) {
            stopNetwork();
        }
    }

    @java.lang.Deprecated
    public void setScoreFilter(int score) {
        this.mImpl.setScoreFilter(score);
    }

    public void setScoreFilter(android.net.NetworkScore score) {
        this.mImpl.setScoreFilter(score);
    }

    public void setCapabilityFilter(android.net.NetworkCapabilities netCap) {
        this.mImpl.setCapabilityFilter(netCap);
    }

    protected int getRequestCount() {
        return this.mImpl.getRequestCount();
    }

    public int getSerialNumber() {
        return this.mImpl.getSerialNumber();
    }

    public android.net.NetworkProvider getProvider() {
        return this.mImpl.getProvider();
    }

    protected void log(java.lang.String s) {
        android.util.Log.d(this.LOG_TAG, s);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        this.mImpl.dump(fd, writer, args);
    }

    public java.lang.String toString() {
        return "{" + this.LOG_TAG + " " + this.mImpl.toString() + "}";
    }
}
