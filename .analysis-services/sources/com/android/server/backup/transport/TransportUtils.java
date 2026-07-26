package com.android.server.backup.transport;

/* JADX INFO: loaded from: classes.dex */
public class TransportUtils {
    private static final java.lang.String TAG = "TransportUtils";

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface Priority {
        public static final int DEBUG = 3;
        public static final int ERROR = 6;
        public static final int INFO = 4;
        public static final int VERBOSE = 2;
        public static final int WARN = 5;
        public static final int WTF = -1;
    }

    public static com.android.internal.backup.IBackupTransport checkTransportNotNull(com.android.internal.backup.IBackupTransport transport) throws com.android.server.backup.transport.TransportNotAvailableException {
        if (transport == null) {
            log(6, TAG, "Transport not available");
            throw new com.android.server.backup.transport.TransportNotAvailableException();
        }
        return transport;
    }

    static void log(int priority, java.lang.String tag, java.lang.String message) {
        if (priority == -1) {
            android.util.Slog.wtf(tag, message);
        } else if (android.util.Log.isLoggable(tag, priority)) {
            android.util.Slog.println(priority, tag, message);
        }
    }

    static java.lang.String formatMessage(java.lang.String prefix, java.lang.String caller, java.lang.String message) {
        java.lang.StringBuilder string = new java.lang.StringBuilder();
        if (prefix != null) {
            string.append(prefix).append(" ");
        }
        if (caller != null) {
            string.append("[").append(caller).append("] ");
        }
        return string.append(message).toString();
    }

    private TransportUtils() {
    }
}
