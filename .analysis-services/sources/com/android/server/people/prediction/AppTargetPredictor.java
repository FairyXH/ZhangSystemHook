package com.android.server.people.prediction;

/* JADX INFO: loaded from: classes2.dex */
public class AppTargetPredictor {
    private static final java.lang.String UI_SURFACE_SHARE = "share";
    private final java.util.concurrent.ExecutorService mCallbackExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();
    final int mCallingUserId;
    private final com.android.server.people.data.DataManager mDataManager;
    private final android.app.prediction.AppPredictionContext mPredictionContext;
    private final java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> mUpdatePredictionsMethod;

    public static com.android.server.people.prediction.AppTargetPredictor create(android.app.prediction.AppPredictionContext predictionContext, java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> updatePredictionsMethod, com.android.server.people.data.DataManager dataManager, int callingUserId, android.content.Context context) {
        if (UI_SURFACE_SHARE.equals(predictionContext.getUiSurface())) {
            return new com.android.server.people.prediction.ShareTargetPredictor(predictionContext, updatePredictionsMethod, dataManager, callingUserId, context);
        }
        return new com.android.server.people.prediction.AppTargetPredictor(predictionContext, updatePredictionsMethod, dataManager, callingUserId);
    }

    AppTargetPredictor(android.app.prediction.AppPredictionContext predictionContext, java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> updatePredictionsMethod, com.android.server.people.data.DataManager dataManager, int callingUserId) {
        this.mPredictionContext = predictionContext;
        this.mUpdatePredictionsMethod = updatePredictionsMethod;
        this.mDataManager = dataManager;
        this.mCallingUserId = callingUserId;
    }

    public void onAppTargetEvent(final android.app.prediction.AppTargetEvent event) {
        this.mCallbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.people.prediction.AppTargetPredictor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAppTargetEvent$0(event);
            }
        });
    }

    public void onLaunchLocationShown(java.lang.String launchLocation, java.util.List<android.app.prediction.AppTargetId> targetIds) {
    }

    public void onSortAppTargets(final java.util.List<android.app.prediction.AppTarget> targets, final java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> callback) {
        this.mCallbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.people.prediction.AppTargetPredictor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSortAppTargets$1(targets, callback);
            }
        });
    }

    public void onRequestPredictionUpdate() {
        this.mCallbackExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.people.prediction.AppTargetPredictor$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.predictTargets();
            }
        });
    }

    public java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> getUpdatePredictionsMethod() {
        return this.mUpdatePredictionsMethod;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: reportAppTargetEvent, reason: merged with bridge method [inline-methods] */
    public void lambda$onAppTargetEvent$0(android.app.prediction.AppTargetEvent event) {
    }

    void predictTargets() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: sortTargets, reason: merged with bridge method [inline-methods] */
    public void lambda$onSortAppTargets$1(java.util.List<android.app.prediction.AppTarget> targets, java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> callback) {
        callback.accept(targets);
    }

    void destroy() {
    }

    android.app.prediction.AppPredictionContext getPredictionContext() {
        return this.mPredictionContext;
    }

    com.android.server.people.data.DataManager getDataManager() {
        return this.mDataManager;
    }

    void updatePredictions(java.util.List<android.app.prediction.AppTarget> targets) {
        this.mUpdatePredictionsMethod.accept(targets);
    }
}
