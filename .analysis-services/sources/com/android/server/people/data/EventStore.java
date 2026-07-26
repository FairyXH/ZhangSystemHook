package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class EventStore {
    static final int CATEGORY_CALL = 2;
    static final int CATEGORY_CLASS_BASED = 4;
    static final int CATEGORY_LOCUS_ID_BASED = 1;
    static final int CATEGORY_SHORTCUT_BASED = 0;
    static final int CATEGORY_SMS = 3;
    private final java.util.List<java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl>> mEventHistoryMaps = new java.util.ArrayList();
    private final java.util.List<java.io.File> mEventsCategoryDirs = new java.util.ArrayList();
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface EventCategory {
    }

    EventStore(java.io.File packageDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        this.mEventHistoryMaps.add(0, new android.util.ArrayMap());
        this.mEventHistoryMaps.add(1, new android.util.ArrayMap());
        this.mEventHistoryMaps.add(2, new android.util.ArrayMap());
        this.mEventHistoryMaps.add(3, new android.util.ArrayMap());
        this.mEventHistoryMaps.add(4, new android.util.ArrayMap());
        java.io.File eventDir = new java.io.File(packageDir, "event");
        this.mEventsCategoryDirs.add(0, new java.io.File(eventDir, "shortcut"));
        this.mEventsCategoryDirs.add(1, new java.io.File(eventDir, "locus"));
        this.mEventsCategoryDirs.add(2, new java.io.File(eventDir, "call"));
        this.mEventsCategoryDirs.add(3, new java.io.File(eventDir, com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_SMS));
        this.mEventsCategoryDirs.add(4, new java.io.File(eventDir, "class"));
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    synchronized void loadFromDisk() {
        for (int category = 0; category < this.mEventsCategoryDirs.size(); category++) {
            java.io.File categoryDir = this.mEventsCategoryDirs.get(category);
            java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> existingEventHistoriesImpl = com.android.server.people.data.EventHistoryImpl.eventHistoriesImplFromDisk(categoryDir, this.mScheduledExecutorService);
            this.mEventHistoryMaps.get(category).putAll(existingEventHistoriesImpl);
        }
    }

    synchronized void saveToDisk() {
        for (java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> map : this.mEventHistoryMaps) {
            for (com.android.server.people.data.EventHistoryImpl eventHistory : map.values()) {
                eventHistory.saveToDisk();
            }
        }
    }

    synchronized com.android.server.people.data.EventHistory getEventHistory(int category, java.lang.String key) {
        return this.mEventHistoryMaps.get(category).get(key);
    }

    synchronized com.android.server.people.data.EventHistoryImpl getOrCreateEventHistory(final int category, final java.lang.String key) {
        return this.mEventHistoryMaps.get(category).computeIfAbsent(key, new java.util.function.Function() { // from class: com.android.server.people.data.EventStore$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$getOrCreateEventHistory$0(category, key, (java.lang.String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.people.data.EventHistoryImpl lambda$getOrCreateEventHistory$0(int category, java.lang.String key, java.lang.String k) {
        return new com.android.server.people.data.EventHistoryImpl(new java.io.File(this.mEventsCategoryDirs.get(category), android.net.Uri.encode(key)), this.mScheduledExecutorService);
    }

    synchronized void deleteEventHistory(int category, java.lang.String key) {
        com.android.server.people.data.EventHistoryImpl eventHistory = this.mEventHistoryMaps.get(category).remove(key);
        if (eventHistory != null) {
            eventHistory.onDestroy();
        }
    }

    synchronized void deleteEventHistories(int category) {
        for (com.android.server.people.data.EventHistoryImpl eventHistory : this.mEventHistoryMaps.get(category).values()) {
            eventHistory.onDestroy();
        }
        this.mEventHistoryMaps.get(category).clear();
    }

    synchronized void pruneOldEvents() {
        for (java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> map : this.mEventHistoryMaps) {
            for (com.android.server.people.data.EventHistoryImpl eventHistory : map.values()) {
                eventHistory.pruneOldEvents();
            }
        }
    }

    synchronized void pruneOrphanEventHistories(int category, java.util.function.Predicate<java.lang.String> keyChecker) {
        java.util.Set<java.lang.String> keys = this.mEventHistoryMaps.get(category).keySet();
        java.util.List<java.lang.String> keysToDelete = new java.util.ArrayList<>();
        for (java.lang.String key : keys) {
            if (!keyChecker.test(key)) {
                keysToDelete.add(key);
            }
        }
        java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> eventHistoryMap = this.mEventHistoryMaps.get(category);
        java.util.Iterator<java.lang.String> it = keysToDelete.iterator();
        while (it.hasNext()) {
            com.android.server.people.data.EventHistoryImpl eventHistory = eventHistoryMap.remove(it.next());
            if (eventHistory != null) {
                eventHistory.onDestroy();
            }
        }
    }

    synchronized void onDestroy() {
        for (java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> map : this.mEventHistoryMaps) {
            for (com.android.server.people.data.EventHistoryImpl eventHistory : map.values()) {
                eventHistory.onDestroy();
            }
        }
    }
}
