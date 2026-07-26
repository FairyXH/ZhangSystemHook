package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class PackageData {
    private final com.android.server.people.data.ConversationStore mConversationStore;
    private final com.android.server.people.data.EventStore mEventStore;
    private final java.util.function.Predicate<java.lang.String> mIsDefaultDialerPredicate;
    private final java.util.function.Predicate<java.lang.String> mIsDefaultSmsAppPredicate;
    private final java.io.File mPackageDataDir;
    private final java.lang.String mPackageName;
    private final int mUserId;

    PackageData(java.lang.String packageName, int userId, java.util.function.Predicate<java.lang.String> isDefaultDialerPredicate, java.util.function.Predicate<java.lang.String> isDefaultSmsAppPredicate, java.util.concurrent.ScheduledExecutorService scheduledExecutorService, java.io.File perUserPeopleDataDir) {
        this.mPackageName = packageName;
        this.mUserId = userId;
        this.mPackageDataDir = new java.io.File(perUserPeopleDataDir, this.mPackageName);
        this.mPackageDataDir.mkdirs();
        this.mConversationStore = new com.android.server.people.data.ConversationStore(this.mPackageDataDir, scheduledExecutorService);
        this.mEventStore = new com.android.server.people.data.EventStore(this.mPackageDataDir, scheduledExecutorService);
        this.mIsDefaultDialerPredicate = isDefaultDialerPredicate;
        this.mIsDefaultSmsAppPredicate = isDefaultSmsAppPredicate;
    }

    static java.util.Map<java.lang.String, com.android.server.people.data.PackageData> packagesDataFromDisk(int userId, java.util.function.Predicate<java.lang.String> isDefaultDialerPredicate, java.util.function.Predicate<java.lang.String> isDefaultSmsAppPredicate, java.util.concurrent.ScheduledExecutorService scheduledExecutorService, java.io.File perUserPeopleDataDir) {
        java.util.Map<java.lang.String, com.android.server.people.data.PackageData> results = new android.util.ArrayMap<>();
        java.io.File[] packageDirs = perUserPeopleDataDir.listFiles(new com.android.server.cpu.CpuInfoReader$$ExternalSyntheticLambda2());
        if (packageDirs == null) {
            return results;
        }
        for (java.io.File packageDir : packageDirs) {
            com.android.server.people.data.PackageData packageData = new com.android.server.people.data.PackageData(packageDir.getName(), userId, isDefaultDialerPredicate, isDefaultSmsAppPredicate, scheduledExecutorService, perUserPeopleDataDir);
            packageData.loadFromDisk();
            results.put(packageDir.getName(), packageData);
        }
        return results;
    }

    private void loadFromDisk() {
        this.mConversationStore.loadConversationsFromDisk();
        this.mEventStore.loadFromDisk();
    }

    void saveToDisk() {
        this.mConversationStore.saveConversationsToDisk();
        this.mEventStore.saveToDisk();
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public void forAllConversations(java.util.function.Consumer<com.android.server.people.data.ConversationInfo> consumer) {
        this.mConversationStore.forAllConversations(consumer);
    }

    public com.android.server.people.data.ConversationInfo getConversationInfo(java.lang.String shortcutId) {
        return getConversationStore().getConversation(shortcutId);
    }

    public com.android.server.people.data.EventHistory getEventHistory(java.lang.String shortcutId) {
        com.android.server.people.data.EventHistory smsEventHistory;
        com.android.server.people.data.EventHistory callEventHistory;
        com.android.server.people.data.EventHistory locusEventHistory;
        com.android.server.people.data.AggregateEventHistoryImpl result = new com.android.server.people.data.AggregateEventHistoryImpl();
        com.android.server.people.data.ConversationInfo conversationInfo = this.mConversationStore.getConversation(shortcutId);
        if (conversationInfo == null) {
            return result;
        }
        com.android.server.people.data.EventHistory shortcutEventHistory = getEventStore().getEventHistory(0, shortcutId);
        if (shortcutEventHistory != null) {
            result.addEventHistory(shortcutEventHistory);
        }
        android.content.LocusId locusId = conversationInfo.getLocusId();
        if (locusId != null && (locusEventHistory = getEventStore().getEventHistory(1, locusId.getId())) != null) {
            result.addEventHistory(locusEventHistory);
        }
        java.lang.String phoneNumber = conversationInfo.getContactPhoneNumber();
        if (android.text.TextUtils.isEmpty(phoneNumber)) {
            return result;
        }
        if (isDefaultDialer() && (callEventHistory = getEventStore().getEventHistory(2, phoneNumber)) != null) {
            result.addEventHistory(callEventHistory);
        }
        if (isDefaultSmsApp() && (smsEventHistory = getEventStore().getEventHistory(3, phoneNumber)) != null) {
            result.addEventHistory(smsEventHistory);
        }
        return result;
    }

    public com.android.server.people.data.EventHistory getClassLevelEventHistory(java.lang.String className) {
        com.android.server.people.data.EventHistory eventHistory = getEventStore().getEventHistory(4, className);
        return eventHistory != null ? eventHistory : new com.android.server.people.data.AggregateEventHistoryImpl();
    }

    public boolean isDefaultDialer() {
        return this.mIsDefaultDialerPredicate.test(this.mPackageName);
    }

    public boolean isDefaultSmsApp() {
        return this.mIsDefaultSmsAppPredicate.test(this.mPackageName);
    }

    com.android.server.people.data.ConversationStore getConversationStore() {
        return this.mConversationStore;
    }

    com.android.server.people.data.EventStore getEventStore() {
        return this.mEventStore;
    }

    void deleteDataForConversation(java.lang.String shortcutId) {
        com.android.server.people.data.ConversationInfo conversationInfo = this.mConversationStore.deleteConversation(shortcutId);
        if (conversationInfo == null) {
            return;
        }
        this.mEventStore.deleteEventHistory(0, shortcutId);
        if (conversationInfo.getLocusId() != null) {
            this.mEventStore.deleteEventHistory(1, conversationInfo.getLocusId().getId());
        }
        java.lang.String phoneNumber = conversationInfo.getContactPhoneNumber();
        if (!android.text.TextUtils.isEmpty(phoneNumber)) {
            if (isDefaultDialer()) {
                this.mEventStore.deleteEventHistory(2, phoneNumber);
            }
            if (isDefaultSmsApp()) {
                this.mEventStore.deleteEventHistory(3, phoneNumber);
            }
        }
    }

    void pruneOrphanEvents() {
        this.mEventStore.pruneOrphanEventHistories(0, new java.util.function.Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$pruneOrphanEvents$0((java.lang.String) obj);
            }
        });
        this.mEventStore.pruneOrphanEventHistories(1, new java.util.function.Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$pruneOrphanEvents$1((java.lang.String) obj);
            }
        });
        if (isDefaultDialer()) {
            this.mEventStore.pruneOrphanEventHistories(2, new java.util.function.Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$pruneOrphanEvents$2((java.lang.String) obj);
                }
            });
        }
        if (isDefaultSmsApp()) {
            this.mEventStore.pruneOrphanEventHistories(3, new java.util.function.Predicate() { // from class: com.android.server.people.data.PackageData$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$pruneOrphanEvents$3((java.lang.String) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$0(java.lang.String key) {
        return this.mConversationStore.getConversation(key) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$1(java.lang.String key) {
        return this.mConversationStore.getConversationByLocusId(new android.content.LocusId(key)) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$2(java.lang.String key) {
        return this.mConversationStore.getConversationByPhoneNumber(key) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$pruneOrphanEvents$3(java.lang.String key) {
        return this.mConversationStore.getConversationByPhoneNumber(key) != null;
    }

    void onDestroy() {
        this.mEventStore.onDestroy();
        this.mConversationStore.onDestroy();
        android.os.FileUtils.deleteContentsAndDir(this.mPackageDataDir);
    }
}
