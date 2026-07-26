package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class OomConnection {
    private static final java.lang.String TAG = "OomConnection";
    private final com.android.server.am.OomConnection.OomConnectionThread mOomConnectionThread = new com.android.server.am.OomConnection.OomConnectionThread();
    private final com.android.server.am.OomConnection.OomConnectionListener mOomListener;

    public interface OomConnectionListener {
        void handleOomEvent(android.os.OomKillRecord[] oomKillRecordArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native android.os.OomKillRecord[] waitOom();

    public OomConnection(com.android.server.am.OomConnection.OomConnectionListener listener) {
        this.mOomListener = listener;
        this.mOomConnectionThread.start();
    }

    private final class OomConnectionThread extends java.lang.Thread {
        private OomConnectionThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    android.os.OomKillRecord[] oom_kills = com.android.server.am.OomConnection.waitOom();
                    com.android.server.am.OomConnection.this.mOomListener.handleOomEvent(oom_kills);
                } catch (java.lang.RuntimeException e) {
                    android.util.Slog.e(com.android.server.am.OomConnection.TAG, "failed waiting for OOM events: " + e);
                    return;
                }
            }
        }
    }
}
