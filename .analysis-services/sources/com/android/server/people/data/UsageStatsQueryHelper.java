package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class UsageStatsQueryHelper {
    private final com.android.server.people.data.UsageStatsQueryHelper.EventListener mEventListener;
    private long mLastEventTimestamp;
    private final java.util.function.Function<java.lang.String, com.android.server.people.data.PackageData> mPackageDataGetter;
    private final int mUserId;
    private final java.util.Map<android.content.ComponentName, android.app.usage.UsageEvents.Event> mConvoStartEvents = new android.util.ArrayMap();
    private final android.app.usage.UsageStatsManagerInternal mUsageStatsManagerInternal = getUsageStatsManagerInternal();

    interface EventListener {
        void onEvent(com.android.server.people.data.PackageData packageData, com.android.server.people.data.ConversationInfo conversationInfo, com.android.server.people.data.Event event);
    }

    UsageStatsQueryHelper(int userId, java.util.function.Function<java.lang.String, com.android.server.people.data.PackageData> packageDataGetter, com.android.server.people.data.UsageStatsQueryHelper.EventListener eventListener) {
        this.mUserId = userId;
        this.mPackageDataGetter = packageDataGetter;
        this.mEventListener = eventListener;
    }

    boolean querySince(long sinceTime) {
        android.app.usage.UsageEvents usageEvents = this.mUsageStatsManagerInternal.queryEventsForUser(this.mUserId, sinceTime, java.lang.System.currentTimeMillis(), 0);
        if (usageEvents == null) {
            return false;
        }
        boolean hasEvents = false;
        while (usageEvents.hasNextEvent()) {
            android.app.usage.UsageEvents.Event e = new android.app.usage.UsageEvents.Event();
            usageEvents.getNextEvent(e);
            hasEvents = true;
            this.mLastEventTimestamp = java.lang.Math.max(this.mLastEventTimestamp, e.getTimeStamp());
            java.lang.String packageName = e.getPackageName();
            com.android.server.people.data.PackageData packageData = this.mPackageDataGetter.apply(packageName);
            if (packageData != null) {
                switch (e.getEventType()) {
                    case 2:
                    case 23:
                    case 24:
                        onInAppConversationEnded(packageData, e);
                        break;
                    case 8:
                        addEventByShortcutId(packageData, e.getShortcutId(), new com.android.server.people.data.Event(e.getTimeStamp(), 1));
                        break;
                    case 30:
                        onInAppConversationEnded(packageData, e);
                        android.content.LocusId locusId = e.getLocusId() != null ? new android.content.LocusId(e.getLocusId()) : null;
                        if (locusId != null && packageData.getConversationStore().getConversationByLocusId(locusId) != null) {
                            android.content.ComponentName activityName = new android.content.ComponentName(packageName, e.getClassName());
                            this.mConvoStartEvents.put(activityName, e);
                        }
                        break;
                }
            }
        }
        return hasEvents;
    }

    long getLastEventTimestamp() {
        return this.mLastEventTimestamp;
    }

    static java.util.List<android.app.usage.UsageEvents.Event> queryAppMovingToForegroundEvents(int userId, long startTime, long endTime) {
        java.util.List<android.app.usage.UsageEvents.Event> res = new java.util.ArrayList<>();
        android.app.usage.UsageEvents usageEvents = getUsageStatsManagerInternal().queryEventsForUser(userId, startTime, endTime, 10);
        if (usageEvents == null) {
            return res;
        }
        while (usageEvents.hasNextEvent()) {
            android.app.usage.UsageEvents.Event e = new android.app.usage.UsageEvents.Event();
            usageEvents.getNextEvent(e);
            if (e.getEventType() == 1) {
                res.add(e);
            }
        }
        return res;
    }

    static java.util.Map<java.lang.String, com.android.server.people.data.AppUsageStatsData> queryAppUsageStats(int userId, long startTime, long endTime, java.util.Set<java.lang.String> packageNameFilter) {
        java.util.List<android.app.usage.UsageStats> stats = getUsageStatsManagerInternal().queryUsageStatsForUser(userId, 4, startTime, endTime, false);
        java.util.Map<java.lang.String, com.android.server.people.data.AppUsageStatsData> aggregatedStats = new android.util.ArrayMap<>();
        if (stats == null) {
            return aggregatedStats;
        }
        for (android.app.usage.UsageStats stat : stats) {
            java.lang.String packageName = stat.getPackageName();
            if (packageNameFilter.contains(packageName)) {
                com.android.server.people.data.AppUsageStatsData packageStats = aggregatedStats.computeIfAbsent(packageName, new java.util.function.Function() { // from class: com.android.server.people.data.UsageStatsQueryHelper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.people.data.UsageStatsQueryHelper.lambda$queryAppUsageStats$0((java.lang.String) obj);
                    }
                });
                packageStats.incrementChosenCountBy(sumChooserCounts(stat.mChooserCounts));
                packageStats.incrementLaunchCountBy(stat.getAppLaunchCount());
            }
        }
        return aggregatedStats;
    }

    static /* synthetic */ com.android.server.people.data.AppUsageStatsData lambda$queryAppUsageStats$0(java.lang.String key) {
        return new com.android.server.people.data.AppUsageStatsData();
    }

    private static int sumChooserCounts(android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, java.lang.Integer>> chooserCounts) {
        int sum = 0;
        if (chooserCounts == null) {
            return 0;
        }
        int chooserCountsSize = chooserCounts.size();
        for (int i = 0; i < chooserCountsSize; i++) {
            android.util.ArrayMap<java.lang.String, java.lang.Integer> counts = chooserCounts.valueAt(i);
            if (counts != null) {
                int annotationSize = counts.size();
                for (int j = 0; j < annotationSize; j++) {
                    sum += counts.valueAt(j).intValue();
                }
            }
        }
        return sum;
    }

    private void onInAppConversationEnded(com.android.server.people.data.PackageData packageData, android.app.usage.UsageEvents.Event endEvent) {
        android.content.ComponentName activityName = new android.content.ComponentName(endEvent.getPackageName(), endEvent.getClassName());
        android.app.usage.UsageEvents.Event startEvent = this.mConvoStartEvents.remove(activityName);
        if (startEvent == null || startEvent.getTimeStamp() >= endEvent.getTimeStamp()) {
            return;
        }
        long durationMillis = endEvent.getTimeStamp() - startEvent.getTimeStamp();
        com.android.server.people.data.Event event = new com.android.server.people.data.Event.Builder(startEvent.getTimeStamp(), 13).setDurationSeconds((int) (durationMillis / 1000)).build();
        addEventByLocusId(packageData, new android.content.LocusId(startEvent.getLocusId()), event);
    }

    private void addEventByShortcutId(com.android.server.people.data.PackageData packageData, java.lang.String shortcutId, com.android.server.people.data.Event event) {
        com.android.server.people.data.ConversationInfo conversationInfo = packageData.getConversationStore().getConversation(shortcutId);
        if (conversationInfo == null) {
            return;
        }
        com.android.server.people.data.EventHistoryImpl eventHistory = packageData.getEventStore().getOrCreateEventHistory(0, shortcutId);
        eventHistory.addEvent(event);
        this.mEventListener.onEvent(packageData, conversationInfo, event);
    }

    private void addEventByLocusId(com.android.server.people.data.PackageData packageData, android.content.LocusId locusId, com.android.server.people.data.Event event) {
        com.android.server.people.data.ConversationInfo conversationInfo = packageData.getConversationStore().getConversationByLocusId(locusId);
        if (conversationInfo == null) {
            return;
        }
        com.android.server.people.data.EventHistoryImpl eventHistory = packageData.getEventStore().getOrCreateEventHistory(1, locusId.getId());
        eventHistory.addEvent(event);
        this.mEventListener.onEvent(packageData, conversationInfo, event);
    }

    private static android.app.usage.UsageStatsManagerInternal getUsageStatsManagerInternal() {
        return (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
    }
}
