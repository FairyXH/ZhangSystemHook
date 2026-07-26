package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
interface SuspendBlocker {
    void acquire();

    void acquire(java.lang.String str);

    void dumpDebug(android.util.proto.ProtoOutputStream protoOutputStream, long j);

    void release();

    void release(java.lang.String str);
}
