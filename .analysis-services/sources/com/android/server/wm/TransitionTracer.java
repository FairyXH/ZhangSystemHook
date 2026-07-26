package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
interface TransitionTracer {
    boolean isTracing();

    void logAbortedTransition(com.android.server.wm.Transition transition);

    void logFinishedTransition(com.android.server.wm.Transition transition);

    void logRemovingStartingWindow(com.android.server.wm.StartingData startingData);

    void logSentTransition(com.android.server.wm.Transition transition, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> arrayList);

    void saveForBugreport(java.io.PrintWriter printWriter);

    void startTrace(java.io.PrintWriter printWriter);

    void stopTrace(java.io.PrintWriter printWriter);
}
