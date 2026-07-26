package android.net.util;

/* JADX INFO: loaded from: classes.dex */
public class NetdService {
    private static final long BASE_TIMEOUT_MS = 100;
    private static final long MAX_TIMEOUT_MS = 1000;
    private static final java.lang.String TAG = android.net.util.NetdService.class.getSimpleName();

    public interface NetdCommand {
        void run(android.net.INetd iNetd) throws android.os.RemoteException;
    }

    public static android.net.INetd getInstance() {
        android.net.INetd netdInstance = android.net.INetd.Stub.asInterface(android.os.ServiceManager.getService("netd"));
        if (netdInstance == null) {
            android.util.Log.w(TAG, "WARNING: returning null INetd instance.");
        }
        return netdInstance;
    }

    public static android.net.INetd get(long maxTimeoutMs) {
        long stop;
        if (maxTimeoutMs == 0) {
            return getInstance();
        }
        if (maxTimeoutMs > 0) {
            stop = android.os.SystemClock.elapsedRealtime() + maxTimeoutMs;
        } else {
            stop = Long.MAX_VALUE;
        }
        long timeoutMs = 0;
        while (true) {
            android.net.INetd netdInstance = getInstance();
            if (netdInstance != null) {
                return netdInstance;
            }
            long remaining = stop - android.os.SystemClock.elapsedRealtime();
            if (remaining > 0) {
                timeoutMs = java.lang.Math.min(java.lang.Math.min(BASE_TIMEOUT_MS + timeoutMs, 1000L), remaining);
                try {
                    java.lang.Thread.sleep(timeoutMs);
                } catch (java.lang.InterruptedException e) {
                }
            } else {
                return null;
            }
        }
    }

    public static android.net.INetd get() {
        return get(-1L);
    }

    public static void run(android.net.util.NetdService.NetdCommand cmd) {
        while (true) {
            try {
                cmd.run(get());
                return;
            } catch (android.os.RemoteException re) {
                android.util.Log.e(TAG, "error communicating with netd: " + re);
            }
        }
    }
}
