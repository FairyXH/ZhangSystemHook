package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
interface ICaptureStateNotifier {

    public interface Listener {
        void onCaptureStateChange(boolean z);
    }

    boolean registerListener(com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener listener);

    void unregisterListener(com.android.server.soundtrigger_middleware.ICaptureStateNotifier.Listener listener);
}
