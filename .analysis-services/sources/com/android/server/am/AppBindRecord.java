package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppBindRecord {
    final com.android.server.am.ProcessRecord attributedClient;
    final com.android.server.am.ProcessRecord client;
    final android.util.ArraySet<com.android.server.am.ConnectionRecord> connections = new android.util.ArraySet<>();
    final com.android.server.am.IntentBindRecord intent;
    final com.android.server.am.ServiceRecord service;

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "service=" + this.service);
        pw.println(prefix + "client=" + this.client);
        pw.println(prefix + "attributedClient=" + this.attributedClient);
        dumpInIntentBind(pw, prefix);
    }

    void dumpInIntentBind(java.io.PrintWriter pw, java.lang.String prefix) {
        int N = this.connections.size();
        if (N > 0) {
            pw.println(prefix + "Per-process Connections:");
            for (int i = 0; i < N; i++) {
                com.android.server.am.ConnectionRecord c = this.connections.valueAt(i);
                pw.println(prefix + "  " + c);
            }
        }
    }

    AppBindRecord(com.android.server.am.ServiceRecord _service, com.android.server.am.IntentBindRecord _intent, com.android.server.am.ProcessRecord _client, com.android.server.am.ProcessRecord _attributedClient) {
        this.service = _service;
        this.intent = _intent;
        this.client = _client;
        this.attributedClient = _attributedClient;
    }

    public java.lang.String toString() {
        return "AppBindRecord{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + this.service.shortInstanceName + ":" + this.client.processName + "}";
    }

    void logOutIntentBindWithTypeInfo() {
        int N = this.connections.size();
        android.util.Slog.d("AppBindRecord", "size:" + N);
        if (N > 0) {
            android.util.Slog.d("AppBindRecord", "Per-process Connections:");
            for (int i = 0; i < N; i++) {
                java.lang.Object obj = this.connections.valueAt(i);
                if (obj != null) {
                    if (obj instanceof com.android.server.am.ConnectionRecord) {
                        android.util.Slog.d("AppBindRecord", "Connections at: " + i + " = " + ((com.android.server.am.ConnectionRecord) obj));
                    } else {
                        android.util.Slog.d("AppBindRecord", "Connections at: " + i + " is not ConnectionRecord. " + obj);
                    }
                } else {
                    android.util.Slog.d("AppBindRecord", "Connections null at: " + i);
                }
            }
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.service.shortInstanceName);
        proto.write(1138166333442L, this.client.processName);
        int N = this.connections.size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.ConnectionRecord conn = this.connections.valueAt(i);
            proto.write(2237677961219L, java.lang.Integer.toHexString(java.lang.System.identityHashCode(conn)));
        }
        proto.end(token);
    }
}
