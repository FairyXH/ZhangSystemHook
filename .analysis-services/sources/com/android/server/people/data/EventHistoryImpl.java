package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class EventHistoryImpl implements com.android.server.people.data.EventHistory {
    private static final java.lang.String EVENTS_DIR = "events";
    private static final java.lang.String INDEXES_DIR = "indexes";
    private static final long MAX_EVENTS_AGE = 14400000;
    private static final long PRUNE_OLD_EVENTS_DELAY = 900000;
    private final android.util.SparseArray<com.android.server.people.data.EventIndex> mEventIndexArray;
    private final com.android.server.people.data.EventHistoryImpl.EventIndexesProtoDiskReadWriter mEventIndexesProtoDiskReadWriter;
    private final com.android.server.people.data.EventHistoryImpl.EventsProtoDiskReadWriter mEventsProtoDiskReadWriter;
    private final com.android.server.people.data.EventHistoryImpl.Injector mInjector;
    private long mLastPruneTime;
    private final com.android.server.people.data.EventList mRecentEvents;
    private final java.io.File mRootDir;
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService;

    EventHistoryImpl(java.io.File rootDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        this(new com.android.server.people.data.EventHistoryImpl.Injector(), rootDir, scheduledExecutorService);
    }

    EventHistoryImpl(com.android.server.people.data.EventHistoryImpl.Injector injector, java.io.File rootDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        this.mEventIndexArray = new android.util.SparseArray<>();
        this.mRecentEvents = new com.android.server.people.data.EventList();
        this.mInjector = injector;
        this.mScheduledExecutorService = scheduledExecutorService;
        this.mLastPruneTime = injector.currentTimeMillis();
        this.mRootDir = rootDir;
        java.io.File eventsDir = new java.io.File(this.mRootDir, EVENTS_DIR);
        this.mEventsProtoDiskReadWriter = new com.android.server.people.data.EventHistoryImpl.EventsProtoDiskReadWriter(eventsDir, this.mScheduledExecutorService);
        java.io.File indexesDir = new java.io.File(this.mRootDir, INDEXES_DIR);
        this.mEventIndexesProtoDiskReadWriter = new com.android.server.people.data.EventHistoryImpl.EventIndexesProtoDiskReadWriter(indexesDir, scheduledExecutorService);
    }

    static java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> eventHistoriesImplFromDisk(java.io.File categoryDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        return eventHistoriesImplFromDisk(new com.android.server.people.data.EventHistoryImpl.Injector(), categoryDir, scheduledExecutorService);
    }

    static java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> eventHistoriesImplFromDisk(com.android.server.people.data.EventHistoryImpl.Injector injector, java.io.File categoryDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        java.util.Map<java.lang.String, com.android.server.people.data.EventHistoryImpl> results = new android.util.ArrayMap<>();
        java.io.File[] keyDirs = categoryDir.listFiles(new com.android.server.cpu.CpuInfoReader$$ExternalSyntheticLambda2());
        if (keyDirs == null) {
            return results;
        }
        for (java.io.File keyDir : keyDirs) {
            java.io.File[] dirContents = keyDir.listFiles(new java.io.FilenameFilter() { // from class: com.android.server.people.data.EventHistoryImpl$$ExternalSyntheticLambda1
                @Override // java.io.FilenameFilter
                public final boolean accept(java.io.File file, java.lang.String str) {
                    return com.android.server.people.data.EventHistoryImpl.lambda$eventHistoriesImplFromDisk$0(file, str);
                }
            });
            if (dirContents != null && dirContents.length == 2) {
                com.android.server.people.data.EventHistoryImpl eventHistory = new com.android.server.people.data.EventHistoryImpl(injector, keyDir, scheduledExecutorService);
                eventHistory.loadFromDisk();
                results.put(android.net.Uri.decode(keyDir.getName()), eventHistory);
            }
        }
        return results;
    }

    static /* synthetic */ boolean lambda$eventHistoriesImplFromDisk$0(java.io.File dir, java.lang.String name) {
        return EVENTS_DIR.equals(name) || INDEXES_DIR.equals(name);
    }

    synchronized void loadFromDisk() {
        this.mScheduledExecutorService.execute(new java.lang.Runnable() { // from class: com.android.server.people.data.EventHistoryImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$loadFromDisk$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFromDisk$1() {
        synchronized (this) {
            com.android.server.people.data.EventList diskEvents = this.mEventsProtoDiskReadWriter.loadRecentEventsFromDisk();
            if (diskEvents != null) {
                diskEvents.removeOldEvents(this.mInjector.currentTimeMillis() - 14400000);
                this.mRecentEvents.addAll(diskEvents.getAllEvents());
            }
            android.util.SparseArray<com.android.server.people.data.EventIndex> diskIndexes = this.mEventIndexesProtoDiskReadWriter.loadIndexesFromDisk();
            if (diskIndexes != null) {
                for (int i = 0; i < diskIndexes.size(); i++) {
                    this.mEventIndexArray.put(diskIndexes.keyAt(i), diskIndexes.valueAt(i));
                }
            }
        }
    }

    synchronized void saveToDisk() {
        this.mEventsProtoDiskReadWriter.saveEventsImmediately(this.mRecentEvents);
        this.mEventIndexesProtoDiskReadWriter.saveIndexesImmediately(this.mEventIndexArray);
    }

    @Override // com.android.server.people.data.EventHistory
    public synchronized com.android.server.people.data.EventIndex getEventIndex(int eventType) {
        com.android.server.people.data.EventIndex eventIndex;
        eventIndex = this.mEventIndexArray.get(eventType);
        return eventIndex != null ? new com.android.server.people.data.EventIndex(eventIndex) : this.mInjector.createEventIndex();
    }

    @Override // com.android.server.people.data.EventHistory
    public synchronized com.android.server.people.data.EventIndex getEventIndex(java.util.Set<java.lang.Integer> eventTypes) {
        com.android.server.people.data.EventIndex combined;
        combined = this.mInjector.createEventIndex();
        java.util.Iterator<java.lang.Integer> it = eventTypes.iterator();
        while (it.hasNext()) {
            int eventType = it.next().intValue();
            com.android.server.people.data.EventIndex eventIndex = this.mEventIndexArray.get(eventType);
            if (eventIndex != null) {
                combined = com.android.server.people.data.EventIndex.combine(combined, eventIndex);
            }
        }
        return combined;
    }

    @Override // com.android.server.people.data.EventHistory
    public synchronized java.util.List<com.android.server.people.data.Event> queryEvents(java.util.Set<java.lang.Integer> eventTypes, long startTime, long endTime) {
        return this.mRecentEvents.queryEvents(eventTypes, startTime, endTime);
    }

    synchronized void addEvent(com.android.server.people.data.Event event) {
        pruneOldEvents();
        addEventInMemory(event);
        this.mEventsProtoDiskReadWriter.scheduleEventsSave(this.mRecentEvents);
        this.mEventIndexesProtoDiskReadWriter.scheduleIndexesSave(this.mEventIndexArray);
    }

    synchronized void onDestroy() {
        this.mEventIndexArray.clear();
        this.mRecentEvents.clear();
        this.mEventsProtoDiskReadWriter.deleteRecentEventsFile();
        this.mEventIndexesProtoDiskReadWriter.deleteIndexesFile();
        android.os.FileUtils.deleteContentsAndDir(this.mRootDir);
    }

    synchronized void pruneOldEvents() {
        long currentTime = this.mInjector.currentTimeMillis();
        if (currentTime - this.mLastPruneTime > PRUNE_OLD_EVENTS_DELAY) {
            this.mRecentEvents.removeOldEvents(currentTime - 14400000);
            this.mLastPruneTime = currentTime;
        }
    }

    private synchronized void addEventInMemory(com.android.server.people.data.Event event) {
        com.android.server.people.data.EventIndex eventIndex = this.mEventIndexArray.get(event.getType());
        if (eventIndex == null) {
            eventIndex = this.mInjector.createEventIndex();
            this.mEventIndexArray.put(event.getType(), eventIndex);
        }
        eventIndex.addEvent(event.getTimestamp());
        this.mRecentEvents.add(event);
    }

    static class Injector {
        Injector() {
        }

        com.android.server.people.data.EventIndex createEventIndex() {
            return new com.android.server.people.data.EventIndex();
        }

        long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class EventsProtoDiskReadWriter extends com.android.server.people.data.AbstractProtoDiskReadWriter<com.android.server.people.data.EventList> {
        private static final java.lang.String RECENT_FILE = "recent";
        private static final java.lang.String TAG = com.android.server.people.data.EventHistoryImpl.EventsProtoDiskReadWriter.class.getSimpleName();

        EventsProtoDiskReadWriter(java.io.File rootDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
            super(rootDir, scheduledExecutorService);
            rootDir.mkdirs();
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter<com.android.server.people.data.EventList> protoStreamWriter() {
            return new com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter() { // from class: com.android.server.people.data.EventHistoryImpl$EventsProtoDiskReadWriter$$ExternalSyntheticLambda0
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter
                public final void write(android.util.proto.ProtoOutputStream protoOutputStream, java.lang.Object obj) {
                    com.android.server.people.data.EventHistoryImpl.EventsProtoDiskReadWriter.lambda$protoStreamWriter$0(protoOutputStream, (com.android.server.people.data.EventList) obj);
                }
            };
        }

        static /* synthetic */ void lambda$protoStreamWriter$0(android.util.proto.ProtoOutputStream protoOutputStream, com.android.server.people.data.EventList data) {
            for (com.android.server.people.data.Event event : data.getAllEvents()) {
                long token = protoOutputStream.start(2246267895809L);
                event.writeToProto(protoOutputStream);
                protoOutputStream.end(token);
            }
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader<com.android.server.people.data.EventList> protoStreamReader() {
            return new com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader() { // from class: com.android.server.people.data.EventHistoryImpl$EventsProtoDiskReadWriter$$ExternalSyntheticLambda1
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader
                public final java.lang.Object read(android.util.proto.ProtoInputStream protoInputStream) {
                    return com.android.server.people.data.EventHistoryImpl.EventsProtoDiskReadWriter.lambda$protoStreamReader$1(protoInputStream);
                }
            };
        }

        static /* synthetic */ com.android.server.people.data.EventList lambda$protoStreamReader$1(android.util.proto.ProtoInputStream protoInputStream) {
            java.util.List<com.android.server.people.data.Event> results = com.google.android.collect.Lists.newArrayList();
            while (protoInputStream.nextField() != -1) {
                try {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long token = protoInputStream.start(2246267895809L);
                        com.android.server.people.data.Event event = com.android.server.people.data.Event.readFromProto(protoInputStream);
                        protoInputStream.end(token);
                        results.add(event);
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "Failed to read protobuf input stream.", e);
                }
            }
            com.android.server.people.data.EventList eventList = new com.android.server.people.data.EventList();
            eventList.addAll(results);
            return eventList;
        }

        void scheduleEventsSave(com.android.server.people.data.EventList recentEvents) {
            scheduleSave(RECENT_FILE, recentEvents);
        }

        void saveEventsImmediately(com.android.server.people.data.EventList recentEvents) {
            saveImmediately(RECENT_FILE, recentEvents);
        }

        com.android.server.people.data.EventList loadRecentEventsFromDisk() {
            return read(RECENT_FILE);
        }

        void deleteRecentEventsFile() {
            delete(RECENT_FILE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class EventIndexesProtoDiskReadWriter extends com.android.server.people.data.AbstractProtoDiskReadWriter<android.util.SparseArray<com.android.server.people.data.EventIndex>> {
        private static final java.lang.String INDEXES_FILE = "index";
        private static final java.lang.String TAG = com.android.server.people.data.EventHistoryImpl.EventIndexesProtoDiskReadWriter.class.getSimpleName();

        EventIndexesProtoDiskReadWriter(java.io.File rootDir, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
            super(rootDir, scheduledExecutorService);
            rootDir.mkdirs();
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter<android.util.SparseArray<com.android.server.people.data.EventIndex>> protoStreamWriter() {
            return new com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter() { // from class: com.android.server.people.data.EventHistoryImpl$EventIndexesProtoDiskReadWriter$$ExternalSyntheticLambda1
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamWriter
                public final void write(android.util.proto.ProtoOutputStream protoOutputStream, java.lang.Object obj) {
                    com.android.server.people.data.EventHistoryImpl.EventIndexesProtoDiskReadWriter.lambda$protoStreamWriter$0(protoOutputStream, (android.util.SparseArray) obj);
                }
            };
        }

        static /* synthetic */ void lambda$protoStreamWriter$0(android.util.proto.ProtoOutputStream protoOutputStream, android.util.SparseArray data) {
            for (int i = 0; i < data.size(); i++) {
                int eventType = data.keyAt(i);
                com.android.server.people.data.EventIndex index = (com.android.server.people.data.EventIndex) data.valueAt(i);
                long token = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1120986464257L, eventType);
                long indexToken = protoOutputStream.start(1146756268034L);
                index.writeToProto(protoOutputStream);
                protoOutputStream.end(indexToken);
                protoOutputStream.end(token);
            }
        }

        @Override // com.android.server.people.data.AbstractProtoDiskReadWriter
        com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader<android.util.SparseArray<com.android.server.people.data.EventIndex>> protoStreamReader() {
            return new com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader() { // from class: com.android.server.people.data.EventHistoryImpl$EventIndexesProtoDiskReadWriter$$ExternalSyntheticLambda0
                @Override // com.android.server.people.data.AbstractProtoDiskReadWriter.ProtoStreamReader
                public final java.lang.Object read(android.util.proto.ProtoInputStream protoInputStream) {
                    return com.android.server.people.data.EventHistoryImpl.EventIndexesProtoDiskReadWriter.lambda$protoStreamReader$1(protoInputStream);
                }
            };
        }

        static /* synthetic */ android.util.SparseArray lambda$protoStreamReader$1(android.util.proto.ProtoInputStream protoInputStream) {
            android.util.SparseArray<com.android.server.people.data.EventIndex> results = new android.util.SparseArray<>();
            while (protoInputStream.nextField() != -1) {
                try {
                    if (protoInputStream.getFieldNumber() == 1) {
                        long token = protoInputStream.start(2246267895809L);
                        int eventType = 0;
                        com.android.server.people.data.EventIndex index = com.android.server.people.data.EventIndex.EMPTY;
                        while (protoInputStream.nextField() != -1) {
                            switch (protoInputStream.getFieldNumber()) {
                                case 1:
                                    eventType = protoInputStream.readInt(1120986464257L);
                                    break;
                                case 2:
                                    long indexToken = protoInputStream.start(1146756268034L);
                                    index = com.android.server.people.data.EventIndex.readFromProto(protoInputStream);
                                    protoInputStream.end(indexToken);
                                    break;
                                default:
                                    android.util.Slog.w(TAG, "Could not read undefined field: " + protoInputStream.getFieldNumber());
                                    break;
                            }
                        }
                        results.append(eventType, index);
                        protoInputStream.end(token);
                    }
                } catch (java.io.IOException e) {
                    android.util.Slog.e(TAG, "Failed to read protobuf input stream.", e);
                }
            }
            return results;
        }

        void scheduleIndexesSave(android.util.SparseArray<com.android.server.people.data.EventIndex> indexes) {
            scheduleSave("index", indexes);
        }

        void saveIndexesImmediately(android.util.SparseArray<com.android.server.people.data.EventIndex> indexes) {
            saveImmediately("index", indexes);
        }

        android.util.SparseArray<com.android.server.people.data.EventIndex> loadIndexesFromDisk() {
            return read("index");
        }

        void deleteIndexesFile() {
            delete("index");
        }
    }
}
