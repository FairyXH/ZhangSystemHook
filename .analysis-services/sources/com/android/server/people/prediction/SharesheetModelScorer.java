package com.android.server.people.prediction;

/* JADX INFO: loaded from: classes2.dex */
class SharesheetModelScorer {
    private static final boolean DEBUG = false;
    static final float FOREGROUND_APP_WEIGHT = 0.0f;
    private static final float FREQUENTLY_USED_APP_SCORE_INITIAL_DECAY = 0.3f;
    private static final float RECENCY_INITIAL_BASE_SCORE = 0.4f;
    private static final float RECENCY_SCORE_INITIAL_DECAY = 0.05f;
    private static final float RECENCY_SCORE_SUBSEQUENT_DECAY = 0.02f;
    private static final java.lang.String TAG = "SharesheetModelScorer";
    private static final float USAGE_STATS_CHOOSER_SCORE_INITIAL_DECAY = 0.9f;
    private static final java.lang.Integer RECENCY_SCORE_COUNT = 6;
    private static final long ONE_MONTH_WINDOW = java.util.concurrent.TimeUnit.DAYS.toMillis(30);
    private static final long FOREGROUND_APP_PROMO_TIME_WINDOW = java.util.concurrent.TimeUnit.MINUTES.toMillis(10);

    private SharesheetModelScorer() {
    }

    static void computeScore(java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets, int shareEventType, long now) {
        java.lang.Float avgFreq;
        double frequencyScore;
        if (shareTargets.isEmpty()) {
            return;
        }
        float totalFreqScore = FOREGROUND_APP_WEIGHT;
        int freqScoreCount = 0;
        float totalMimeFreqScore = FOREGROUND_APP_WEIGHT;
        int mimeFreqScoreCount = 0;
        java.util.PriorityQueue<android.util.Pair<com.android.server.people.prediction.SharesheetModelScorer.ShareTargetRankingScore, android.util.Range<java.lang.Long>>> recencyMinHeap = new java.util.PriorityQueue<>(RECENCY_SCORE_COUNT.intValue(), java.util.Comparator.comparingLong(new java.util.function.ToLongFunction() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda3
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(java.lang.Object obj) {
                return ((java.lang.Long) ((android.util.Range) ((android.util.Pair) obj).second).getUpper()).longValue();
            }
        }));
        java.util.List<com.android.server.people.prediction.SharesheetModelScorer.ShareTargetRankingScore> scoreList = new java.util.ArrayList<>(shareTargets.size());
        for (com.android.server.people.prediction.ShareTargetPredictor.ShareTarget target : shareTargets) {
            com.android.server.people.prediction.SharesheetModelScorer.ShareTargetRankingScore shareTargetScore = new com.android.server.people.prediction.SharesheetModelScorer.ShareTargetRankingScore();
            scoreList.add(shareTargetScore);
            if (target.getEventHistory() != null) {
                java.util.List<android.util.Range<java.lang.Long>> timeSlots = target.getEventHistory().getEventIndex(com.android.server.people.data.Event.SHARE_EVENT_TYPES).getActiveTimeSlots();
                if (!timeSlots.isEmpty()) {
                    for (android.util.Range<java.lang.Long> timeSlot : timeSlots) {
                        shareTargetScore.incrementFrequencyScore(getFreqDecayedOnElapsedTime(now - ((java.lang.Long) timeSlot.getLower()).longValue()));
                    }
                    totalFreqScore += shareTargetScore.getFrequencyScore();
                    freqScoreCount++;
                }
                java.util.List<android.util.Range<java.lang.Long>> timeSlotsOfSameType = target.getEventHistory().getEventIndex(shareEventType).getActiveTimeSlots();
                if (!timeSlotsOfSameType.isEmpty()) {
                    for (android.util.Range<java.lang.Long> timeSlot2 : timeSlotsOfSameType) {
                        shareTargetScore.incrementMimeFrequencyScore(getFreqDecayedOnElapsedTime(now - ((java.lang.Long) timeSlot2.getLower()).longValue()));
                    }
                    totalMimeFreqScore += shareTargetScore.getMimeFrequencyScore();
                    mimeFreqScoreCount++;
                }
                android.util.Range<java.lang.Long> mostRecentTimeSlot = target.getEventHistory().getEventIndex(com.android.server.people.data.Event.SHARE_EVENT_TYPES).getMostRecentActiveTimeSlot();
                if (mostRecentTimeSlot != null && (recencyMinHeap.size() < RECENCY_SCORE_COUNT.intValue() || ((java.lang.Long) mostRecentTimeSlot.getUpper()).longValue() > ((java.lang.Long) ((android.util.Range) recencyMinHeap.peek().second).getUpper()).longValue())) {
                    if (recencyMinHeap.size() == RECENCY_SCORE_COUNT.intValue()) {
                        recencyMinHeap.poll();
                    }
                    recencyMinHeap.offer(new android.util.Pair<>(shareTargetScore, mostRecentTimeSlot));
                }
            }
        }
        while (!recencyMinHeap.isEmpty()) {
            float recencyScore = RECENCY_INITIAL_BASE_SCORE;
            if (recencyMinHeap.size() > 1) {
                recencyScore = 0.35f - ((recencyMinHeap.size() - 2) * RECENCY_SCORE_SUBSEQUENT_DECAY);
            }
            ((com.android.server.people.prediction.SharesheetModelScorer.ShareTargetRankingScore) recencyMinHeap.poll().first).setRecencyScore(recencyScore);
        }
        float f = FOREGROUND_APP_WEIGHT;
        java.lang.Float avgFreq2 = java.lang.Float.valueOf(freqScoreCount != 0 ? totalFreqScore / freqScoreCount : 0.0f);
        java.lang.Float avgMimeFreq = java.lang.Float.valueOf(mimeFreqScoreCount != 0 ? totalMimeFreqScore / mimeFreqScoreCount : 0.0f);
        int i = 0;
        while (i < scoreList.size()) {
            com.android.server.people.prediction.ShareTargetPredictor.ShareTarget target2 = shareTargets.get(i);
            com.android.server.people.prediction.SharesheetModelScorer.ShareTargetRankingScore targetScore = scoreList.get(i);
            double mimeFrequencyScore = 0.0d;
            if (avgFreq2.equals(java.lang.Float.valueOf(f))) {
                avgFreq = avgFreq2;
                frequencyScore = 0.0d;
            } else {
                avgFreq = avgFreq2;
                frequencyScore = targetScore.getFrequencyScore() / avgFreq2.floatValue();
            }
            targetScore.setFrequencyScore(normalizeFreqScore(frequencyScore));
            f = FOREGROUND_APP_WEIGHT;
            if (!avgMimeFreq.equals(java.lang.Float.valueOf(FOREGROUND_APP_WEIGHT))) {
                mimeFrequencyScore = targetScore.getMimeFrequencyScore() / avgMimeFreq.floatValue();
            }
            targetScore.setMimeFrequencyScore(normalizeMimeFreqScore(mimeFrequencyScore));
            targetScore.setTotalScore(probOR(probOR(targetScore.getRecencyScore(), targetScore.getFrequencyScore()), targetScore.getMimeFrequencyScore()));
            target2.setScore(targetScore.getTotalScore());
            i++;
            avgFreq2 = avgFreq;
        }
    }

    static void computeScoreForAppShare(java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets, int shareEventType, int targetsLimit, long now, com.android.server.people.data.DataManager dataManager, int callingUserId, java.lang.String chooserActivity) {
        computeScore(shareTargets, shareEventType, now);
        postProcess(shareTargets, targetsLimit, dataManager, callingUserId, chooserActivity);
    }

    private static void postProcess(java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> shareTargets, int targetsLimit, com.android.server.people.data.DataManager dataManager, int callingUserId, java.lang.String chooserActivity) {
        java.util.Map<java.lang.String, java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget>> shareTargetMap = new android.util.ArrayMap<>();
        for (com.android.server.people.prediction.ShareTargetPredictor.ShareTarget shareTarget : shareTargets) {
            java.lang.String packageName = shareTarget.getAppTarget().getPackageName();
            shareTargetMap.computeIfAbsent(packageName, new java.util.function.Function() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.people.prediction.SharesheetModelScorer.lambda$postProcess$1((java.lang.String) obj);
                }
            });
            java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> targetsList = shareTargetMap.get(packageName);
            int index = 0;
            while (index < targetsList.size() && shareTarget.getScore() <= targetsList.get(index).getScore()) {
                index++;
            }
            targetsList.add(index, shareTarget);
        }
        promoteForegroundApp(shareTargetMap, dataManager, callingUserId, chooserActivity);
        promoteMostChosenAndFrequentlyUsedApps(shareTargetMap, targetsLimit, dataManager, callingUserId);
    }

    static /* synthetic */ java.util.List lambda$postProcess$1(java.lang.String key) {
        return new java.util.ArrayList();
    }

    private static void promoteMostChosenAndFrequentlyUsedApps(java.util.Map<java.lang.String, java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget>> shareTargetMap, int targetsLimit, com.android.server.people.data.DataManager dataManager, int callingUserId) {
        int validPredictionNum = 0;
        float minValidScore = 1.0f;
        for (java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget> targets : shareTargetMap.values()) {
            for (com.android.server.people.prediction.ShareTargetPredictor.ShareTarget target : targets) {
                if (target.getScore() > FOREGROUND_APP_WEIGHT) {
                    validPredictionNum++;
                    minValidScore = java.lang.Math.min(target.getScore(), minValidScore);
                }
            }
        }
        if (validPredictionNum >= targetsLimit) {
            return;
        }
        long now = java.lang.System.currentTimeMillis();
        java.util.Map<java.lang.String, com.android.server.people.data.AppUsageStatsData> appStatsMap = dataManager.queryAppUsageStats(callingUserId, now - ONE_MONTH_WINDOW, now, shareTargetMap.keySet());
        float minValidScore2 = promoteApp(shareTargetMap, appStatsMap, new java.util.function.Function() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((com.android.server.people.data.AppUsageStatsData) obj).getChosenCount());
            }
        }, USAGE_STATS_CHOOSER_SCORE_INITIAL_DECAY * minValidScore, minValidScore);
        promoteApp(shareTargetMap, appStatsMap, new java.util.function.Function() { // from class: com.android.server.people.prediction.SharesheetModelScorer$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((com.android.server.people.data.AppUsageStatsData) obj).getLaunchCount());
            }
        }, FREQUENTLY_USED_APP_SCORE_INITIAL_DECAY * minValidScore2, minValidScore2);
    }

    private static float promoteApp(java.util.Map<java.lang.String, java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget>> shareTargetMap, java.util.Map<java.lang.String, com.android.server.people.data.AppUsageStatsData> appStatsMap, java.util.function.Function<com.android.server.people.data.AppUsageStatsData, java.lang.Integer> countFunc, float baseScore, float minValidScore) {
        int maxCount = 0;
        for (com.android.server.people.data.AppUsageStatsData data : appStatsMap.values()) {
            maxCount = java.lang.Math.max(maxCount, countFunc.apply(data).intValue());
        }
        if (maxCount > 0) {
            for (java.util.Map.Entry<java.lang.String, com.android.server.people.data.AppUsageStatsData> entry : appStatsMap.entrySet()) {
                if (shareTargetMap.containsKey(entry.getKey())) {
                    com.android.server.people.prediction.ShareTargetPredictor.ShareTarget target = shareTargetMap.get(entry.getKey()).get(0);
                    if (target.getScore() <= FOREGROUND_APP_WEIGHT) {
                        float curScore = (countFunc.apply(entry.getValue()).intValue() * baseScore) / maxCount;
                        target.setScore(curScore);
                        if (curScore > FOREGROUND_APP_WEIGHT) {
                            minValidScore = java.lang.Math.min(minValidScore, curScore);
                        }
                    }
                }
            }
        }
        return minValidScore;
    }

    private static void promoteForegroundApp(java.util.Map<java.lang.String, java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget>> shareTargetMap, com.android.server.people.data.DataManager dataManager, int callingUserId, java.lang.String chooserActivity) {
        java.lang.String sharingForegroundApp = findSharingForegroundApp(shareTargetMap, dataManager, callingUserId, chooserActivity);
        if (sharingForegroundApp != null) {
            com.android.server.people.prediction.ShareTargetPredictor.ShareTarget target = shareTargetMap.get(sharingForegroundApp).get(0);
            target.setScore(probOR(target.getScore(), FOREGROUND_APP_WEIGHT));
        }
    }

    private static java.lang.String findSharingForegroundApp(java.util.Map<java.lang.String, java.util.List<com.android.server.people.prediction.ShareTargetPredictor.ShareTarget>> shareTargetMap, com.android.server.people.data.DataManager dataManager, int callingUserId, java.lang.String chooserActivity) {
        long now = java.lang.System.currentTimeMillis();
        java.util.List<android.app.usage.UsageEvents.Event> events = dataManager.queryAppMovingToForegroundEvents(callingUserId, now - FOREGROUND_APP_PROMO_TIME_WINDOW, now);
        java.lang.String sourceApp = null;
        for (int i = events.size() - 1; i >= 0; i--) {
            java.lang.String className = events.get(i).getClassName();
            java.lang.String packageName = events.get(i).getPackageName();
            if (packageName != null && (className == null || chooserActivity == null || !className.contains(chooserActivity))) {
                if (sourceApp == null) {
                    sourceApp = packageName;
                } else if (!packageName.equals(sourceApp) && shareTargetMap.containsKey(packageName)) {
                    return packageName;
                }
            }
        }
        return null;
    }

    private static float probOR(float a, float b) {
        return 1.0f - ((1.0f - a) * (1.0f - b));
    }

    private static float getFreqDecayedOnElapsedTime(long elapsedTimeMillis) {
        java.time.Duration duration = java.time.Duration.ofMillis(elapsedTimeMillis);
        if (duration.compareTo(java.time.Duration.ofDays(1L)) <= 0) {
            return 1.0f;
        }
        if (duration.compareTo(java.time.Duration.ofDays(3L)) <= 0) {
            return USAGE_STATS_CHOOSER_SCORE_INITIAL_DECAY;
        }
        if (duration.compareTo(java.time.Duration.ofDays(7L)) <= 0) {
            return 0.8f;
        }
        if (duration.compareTo(java.time.Duration.ofDays(14L)) <= 0) {
            return 0.7f;
        }
        return 0.6f;
    }

    private static float normalizeFreqScore(double freqRatio) {
        if (freqRatio >= 2.5d) {
            return 0.2f;
        }
        if (freqRatio >= 1.5d) {
            return 0.15f;
        }
        if (freqRatio >= 1.0d) {
            return 0.1f;
        }
        if (freqRatio >= 0.75d) {
            return RECENCY_SCORE_INITIAL_DECAY;
        }
        return FOREGROUND_APP_WEIGHT;
    }

    private static float normalizeMimeFreqScore(double freqRatio) {
        if (freqRatio >= 2.0d) {
            return 0.2f;
        }
        if (freqRatio >= 1.2d) {
            return 0.15f;
        }
        if (freqRatio > 0.0d) {
            return 0.1f;
        }
        return FOREGROUND_APP_WEIGHT;
    }

    private static class ShareTargetRankingScore {
        private float mFrequencyScore;
        private float mMimeFrequencyScore;
        private float mRecencyScore;
        private float mTotalScore;

        private ShareTargetRankingScore() {
            this.mRecencyScore = com.android.server.people.prediction.SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
            this.mFrequencyScore = com.android.server.people.prediction.SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
            this.mMimeFrequencyScore = com.android.server.people.prediction.SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
            this.mTotalScore = com.android.server.people.prediction.SharesheetModelScorer.FOREGROUND_APP_WEIGHT;
        }

        float getTotalScore() {
            return this.mTotalScore;
        }

        void setTotalScore(float totalScore) {
            this.mTotalScore = totalScore;
        }

        float getRecencyScore() {
            return this.mRecencyScore;
        }

        void setRecencyScore(float recencyScore) {
            this.mRecencyScore = recencyScore;
        }

        float getFrequencyScore() {
            return this.mFrequencyScore;
        }

        void setFrequencyScore(float frequencyScore) {
            this.mFrequencyScore = frequencyScore;
        }

        void incrementFrequencyScore(float incremental) {
            this.mFrequencyScore += incremental;
        }

        float getMimeFrequencyScore() {
            return this.mMimeFrequencyScore;
        }

        void setMimeFrequencyScore(float mimeFrequencyScore) {
            this.mMimeFrequencyScore = mimeFrequencyScore;
        }

        void incrementMimeFrequencyScore(float incremental) {
            this.mMimeFrequencyScore += incremental;
        }
    }
}
