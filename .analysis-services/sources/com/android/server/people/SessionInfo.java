package com.android.server.people;

/* JADX INFO: loaded from: classes2.dex */
class SessionInfo {
    private static final java.lang.String TAG = "SessionInfo";
    private final com.android.server.people.prediction.AppTargetPredictor mAppTargetPredictor;
    private final android.os.RemoteCallbackList<android.app.prediction.IPredictionCallback> mCallbacks = new android.os.RemoteCallbackList<>();

    SessionInfo(android.app.prediction.AppPredictionContext predictionContext, com.android.server.people.data.DataManager dataManager, int callingUserId, android.content.Context context) {
        this.mAppTargetPredictor = com.android.server.people.prediction.AppTargetPredictor.create(predictionContext, new java.util.function.Consumer() { // from class: com.android.server.people.SessionInfo$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.updatePredictions((java.util.List) obj);
            }
        }, dataManager, callingUserId, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addCallback(android.app.prediction.IPredictionCallback callback) {
        this.mCallbacks.register(callback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeCallback(android.app.prediction.IPredictionCallback callback) {
        this.mCallbacks.unregister(callback);
    }

    com.android.server.people.prediction.AppTargetPredictor getPredictor() {
        return this.mAppTargetPredictor;
    }

    void onDestroy() {
        this.mCallbacks.kill();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePredictions(java.util.List<android.app.prediction.AppTarget> targets) {
        int callbackCount = this.mCallbacks.beginBroadcast();
        for (int i = 0; i < callbackCount; i++) {
            try {
                this.mCallbacks.getBroadcastItem(i).onResult(new android.content.pm.ParceledListSlice(targets));
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to calling callback" + e);
            }
        }
        this.mCallbacks.finishBroadcast();
    }
}
