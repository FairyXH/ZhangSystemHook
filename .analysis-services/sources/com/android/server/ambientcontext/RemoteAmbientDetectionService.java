package com.android.server.ambientcontext;

/* JADX INFO: loaded from: classes.dex */
interface RemoteAmbientDetectionService {
    void dump(java.lang.String str, java.io.PrintWriter printWriter);

    void queryServiceStatus(int[] iArr, java.lang.String str, android.os.RemoteCallback remoteCallback);

    void startDetection(android.app.ambientcontext.AmbientContextEventRequest ambientContextEventRequest, java.lang.String str, android.os.RemoteCallback remoteCallback, android.os.RemoteCallback remoteCallback2);

    void stopDetection(java.lang.String str);

    void unbind();
}
