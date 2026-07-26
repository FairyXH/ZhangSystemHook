package com.android.server.people.prediction;

/* JADX INFO: loaded from: classes2.dex */
class ShareTargetPredictor extends com.android.server.people.prediction.AppTargetPredictor {
    private static final java.lang.String REMOTE_APP_PREDICTOR_KEY = "remote_app_predictor";
    private final java.lang.String mChooserActivity;
    private final android.content.IntentFilter mIntentFilter;
    private final android.app.prediction.AppPredictor mRemoteAppPredictor;
    private static final java.lang.String TAG = "ShareTargetPredictor";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    ShareTargetPredictor(android.app.prediction.AppPredictionContext predictionContext, java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> updatePredictionsMethod, com.android.server.people.data.DataManager dataManager, int callingUserId, android.content.Context context) {
        super(predictionContext, updatePredictionsMethod, dataManager, callingUserId);
        this.mIntentFilter = (android.content.IntentFilter) predictionContext.getExtras().getParcelable("intent_filter", android.content.IntentFilter.class);
        if (android.provider.DeviceConfig.getBoolean("systemui", "dark_launch_remote_prediction_service_enabled", false)) {
            predictionContext.getExtras().putBoolean(REMOTE_APP_PREDICTOR_KEY, true);
            this.mRemoteAppPredictor = ((android.app.prediction.AppPredictionManager) context.createContextAsUser(android.os.UserHandle.of(callingUserId), 0).getSystemService(android.app.prediction.AppPredictionManager.class)).createAppPredictionSession(predictionContext);
        } else {
            this.mRemoteAppPredictor = null;
        }
        android.content.ComponentName component = android.content.ComponentName.unflattenFromString(context.getResources().getString(android.R.string.config_customCountryDetector));
        this.mChooserActivity = component != null ? component.getShortClassName() : null;
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    /* JADX INFO: renamed from: reportAppTargetEvent */
    void lambda$onAppTargetEvent$0(android.app.prediction.AppTargetEvent event) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "reportAppTargetEvent");
        }
        if (this.mIntentFilter != null) {
            getDataManager().reportShareTargetEvent(event, this.mIntentFilter);
        }
        if (this.mRemoteAppPredictor != null) {
            this.mRemoteAppPredictor.notifyAppTargetEvent(event);
        }
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    void predictTargets() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "predictTargets");
        }
        if (this.mIntentFilter == null) {
            updatePredictions(java.util.List.of());
            return;
        }
        java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets = getDirectShareTargets();
        com.android.server.people.prediction.SharesheetModelScorer.computeScore(shareTargets, getShareEventType(this.mIntentFilter), java.lang.System.currentTimeMillis());
        java.util.Collections.sort(shareTargets, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.people.prediction.ShareTargetPredictor$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Float.valueOf(((com.android.server.people.prediction.ShareTargetPredictor.ShareTarget) obj).getScore());
            }
        }, java.util.Collections.reverseOrder()).thenComparing(new java.util.function.Function() { // from class: com.android.server.people.prediction.ShareTargetPredictor$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((com.android.server.people.prediction.ShareTargetPredictor.ShareTarget) obj).getAppTarget().getRank());
            }
        }));
        java.util.List<android.app.prediction.AppTarget> res = new java.util.ArrayList<>();
        for (int i = 0; i < java.lang.Math.min(getPredictionContext().getPredictedTargetCount(), shareTargets.size()); i++) {
            res.add(shareTargets.get(i).getAppTarget());
        }
        updatePredictions(res);
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    /* JADX INFO: renamed from: sortTargets */
    void lambda$onSortAppTargets$1(java.util.List<android.app.prediction.AppTarget> targets, java.util.function.Consumer<java.util.List<android.app.prediction.AppTarget>> callback) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "sortTargets");
        }
        if (this.mIntentFilter == null) {
            callback.accept(targets);
            return;
        }
        java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets = getAppShareTargets(targets);
        com.android.server.people.prediction.SharesheetModelScorer.computeScoreForAppShare(shareTargets, getShareEventType(this.mIntentFilter), getPredictionContext().getPredictedTargetCount(), java.lang.System.currentTimeMillis(), getDataManager(), this.mCallingUserId, this.mChooserActivity);
        java.util.Collections.sort(shareTargets, new java.util.Comparator() { // from class: com.android.server.people.prediction.ShareTargetPredictor$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.people.prediction.ShareTargetPredictor.lambda$sortTargets$1((com.android.server.people.prediction.ShareTargetPredictor.ShareTarget) obj, (com.android.server.people.prediction.ShareTargetPredictor.ShareTarget) obj2);
            }
        });
        java.util.List<android.app.prediction.AppTarget> appTargetList = new java.util.ArrayList<>();
        for (com.android.server.people.prediction.ShareTargetPredictor.ShareTarget shareTarget : shareTargets) {
            android.app.prediction.AppTarget appTarget = shareTarget.getAppTarget();
            appTargetList.add(new android.app.prediction.AppTarget.Builder(appTarget.getId(), appTarget.getPackageName(), appTarget.getUser()).setClassName(appTarget.getClassName()).setRank(shareTarget.getScore() > 0.0f ? (int) (shareTarget.getScore() * 1000.0f) : 0).build());
        }
        callback.accept(appTargetList);
    }

    static /* synthetic */ int lambda$sortTargets$1(com.android.server.people.prediction.ShareTargetPredictor.ShareTarget t1, com.android.server.people.prediction.ShareTargetPredictor.ShareTarget t2) {
        return -java.lang.Float.compare(t1.getScore(), t2.getScore());
    }

    @Override // com.android.server.people.prediction.AppTargetPredictor
    void destroy() {
        if (this.mRemoteAppPredictor != null) {
            this.mRemoteAppPredictor.destroy();
        }
    }

    private java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> getDirectShareTargets() {
        java.lang.String shortcutId;
        java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets = new java.util.ArrayList<>();
        java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> shareShortcuts = getDataManager().getShareShortcuts(this.mIntentFilter, this.mCallingUserId);
        for (android.content.pm.ShortcutManager.ShareShortcutInfo shareShortcut : shareShortcuts) {
            android.content.pm.ShortcutInfo shortcutInfo = shareShortcut.getShortcutInfo();
            android.app.prediction.AppTarget appTarget = new android.app.prediction.AppTarget.Builder(new android.app.prediction.AppTargetId(shortcutInfo.getId()), shortcutInfo).setClassName(shareShortcut.getTargetComponent().getClassName()).setRank(shortcutInfo.getRank()).build();
            java.lang.String packageName = shortcutInfo.getPackage();
            int userId = shortcutInfo.getUserId();
            com.android.server.people.data.PackageData packageData = getDataManager().getPackage(packageName, userId);
            com.android.server.people.data.ConversationInfo conversationInfo = null;
            com.android.server.people.data.EventHistory eventHistory = null;
            if (packageData != null && (conversationInfo = packageData.getConversationInfo((shortcutId = shortcutInfo.getId()))) != null) {
                eventHistory = packageData.getEventHistory(shortcutId);
            }
            shareTargets.add(new com.android.server.people.prediction.ShareTargetPredictor.ShareTarget(appTarget, eventHistory, conversationInfo));
        }
        return shareTargets;
    }

    private java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> getAppShareTargets(java.util.List<android.app.prediction.AppTarget> targets) {
        com.android.server.people.data.EventHistory classLevelEventHistory;
        java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets = new java.util.ArrayList<>();
        for (android.app.prediction.AppTarget target : targets) {
            com.android.server.people.data.PackageData packageData = getDataManager().getPackage(target.getPackageName(), target.getUser().getIdentifier());
            if (packageData == null) {
                classLevelEventHistory = null;
            } else {
                classLevelEventHistory = packageData.getClassLevelEventHistory(target.getClassName());
            }
            shareTargets.add(new com.android.server.people.prediction.ShareTargetPredictor.ShareTarget(target, classLevelEventHistory, null));
        }
        return shareTargets;
    }

    private int getShareEventType(android.content.IntentFilter intentFilter) {
        java.lang.String mimeType = intentFilter != null ? intentFilter.getDataType(0) : null;
        return getDataManager().mimeTypeToShareEventType(mimeType);
    }

    static class ShareTarget {
        private final android.app.prediction.AppTarget mAppTarget;
        private final com.android.server.people.data.ConversationInfo mConversationInfo;
        private final com.android.server.people.data.EventHistory mEventHistory;
        private float mScore = 0.0f;

        ShareTarget(android.app.prediction.AppTarget appTarget, com.android.server.people.data.EventHistory eventHistory, com.android.server.people.data.ConversationInfo conversationInfo) {
            this.mAppTarget = appTarget;
            this.mEventHistory = eventHistory;
            this.mConversationInfo = conversationInfo;
        }

        android.app.prediction.AppTarget getAppTarget() {
            return this.mAppTarget;
        }

        com.android.server.people.data.EventHistory getEventHistory() {
            return this.mEventHistory;
        }

        com.android.server.people.data.ConversationInfo getConversationInfo() {
            return this.mConversationInfo;
        }

        float getScore() {
            return this.mScore;
        }

        void setScore(float score) {
            this.mScore = score;
        }
    }
}
