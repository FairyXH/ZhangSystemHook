package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class BroadcastQueueModernImpl extends com.android.server.am.BroadcastQueue {
    private static final int MSG_BG_ACTIVITY_START_TIMEOUT = 3;
    private static final int MSG_CHECK_HEALTH = 4;
    private static final int MSG_CHECK_PENDING_COLD_START_VALIDITY = 5;
    private static final int MSG_DELIVERY_TIMEOUT = 2;
    private static final int MSG_DELIVERY_TIMEOUT_SOFT = 8;
    private static final int MSG_PROCESS_FREEZABLE_CHANGED = 6;
    private static final int MSG_UID_STATE_CHANGED = 7;
    private static final int MSG_UPDATE_RUNNING_LIST = 1;
    private final com.android.server.am.BroadcastQueueModernImpl.BroadcastAnrTimer mAnrTimer;
    private final com.android.server.am.BroadcastConstants mBgConstants;
    final com.android.server.am.BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerDeferApply;
    final com.android.server.am.BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerDeferClear;
    private final com.android.server.am.BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerSkip;
    private final com.android.server.am.BroadcastProcessQueue.BroadcastConsumer mBroadcastConsumerSkipAndCanceled;
    private com.android.server.am.IBroadcastQueueModernImplExt mBroadcastQueueModernImplExt;
    final com.android.server.am.BroadcastProcessQueue.BroadcastRecordConsumer mBroadcastRecordConsumerEnqueue;
    private boolean mCheckPendingColdStartQueued;
    private final com.android.server.am.BroadcastConstants mConstants;
    private final com.android.server.am.BroadcastConstants mFgConstants;
    private long mLastTestFailureTime;
    private final android.os.Handler.Callback mLocalCallback;
    private final android.os.Handler mLocalHandler;
    private final java.util.concurrent.atomic.AtomicReference<android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean>> mMatchingRecordsCache;
    private final android.util.SparseArray<com.android.server.am.BroadcastProcessQueue> mProcessQueues;
    private final java.util.concurrent.atomic.AtomicReference<android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean>> mRecordsLookupCache;
    private final java.util.concurrent.atomic.AtomicReference<android.util.ArraySet<com.android.server.am.BroadcastRecord>> mReplacedBroadcastsCache;
    private com.android.server.am.BroadcastProcessQueue mRunnableHead;
    private final com.android.server.am.BroadcastProcessQueue[] mRunning;
    private com.android.server.am.BroadcastProcessQueue mRunningColdStart;
    private final android.util.SparseBooleanArray mUidForeground;
    private final java.util.ArrayList<android.util.Pair<java.util.function.BooleanSupplier, java.util.concurrent.CountDownLatch>> mWaitingFor;
    private com.android.server.am.BroadcastQueueModernImpl.BroadcastQueueWrapper mWrapper;
    private static final java.util.function.Predicate<com.android.server.am.BroadcastProcessQueue> QUEUE_PREDICATE_ANY = new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda6
        @Override // java.util.function.Predicate
        public final boolean test(java.lang.Object obj) {
            return com.android.server.am.BroadcastQueueModernImpl.lambda$static$8((com.android.server.am.BroadcastProcessQueue) obj);
        }
    };
    private static final com.android.server.am.BroadcastProcessQueue.BroadcastPredicate BROADCAST_PREDICATE_ANY = new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda7
        @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
        public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
            return com.android.server.am.BroadcastQueueModernImpl.lambda$static$9(broadcastRecord, i);
        }
    };

    BroadcastQueueModernImpl(com.android.server.am.ActivityManagerService service, android.os.Handler handler, com.android.server.am.BroadcastConstants fgConstants, com.android.server.am.BroadcastConstants bgConstants) {
        this(service, handler, fgConstants, bgConstants, new com.android.server.am.BroadcastSkipPolicy(service), new com.android.server.am.BroadcastHistory(fgConstants));
    }

    BroadcastQueueModernImpl(com.android.server.am.ActivityManagerService service, android.os.Handler handler, com.android.server.am.BroadcastConstants fgConstants, com.android.server.am.BroadcastConstants bgConstants, com.android.server.am.BroadcastSkipPolicy skipPolicy, com.android.server.am.BroadcastHistory history) {
        super(service, handler, "modern", skipPolicy, history);
        this.mProcessQueues = new android.util.SparseArray<>();
        this.mRunnableHead = null;
        this.mWaitingFor = new java.util.ArrayList<>();
        this.mReplacedBroadcastsCache = new java.util.concurrent.atomic.AtomicReference<>();
        this.mRecordsLookupCache = new java.util.concurrent.atomic.AtomicReference<>();
        this.mMatchingRecordsCache = new java.util.concurrent.atomic.AtomicReference<>();
        this.mUidForeground = new android.util.SparseBooleanArray();
        this.mLocalCallback = new android.os.Handler.Callback() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda15
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.lambda$new$0(message);
            }
        };
        this.mBroadcastConsumerSkip = new com.android.server.am.BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda16
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                this.f$0.lambda$new$10(broadcastRecord, i);
            }
        };
        this.mBroadcastConsumerSkipAndCanceled = new com.android.server.am.BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda17
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                this.f$0.lambda$new$11(broadcastRecord, i);
            }
        };
        this.mBroadcastConsumerDeferApply = new com.android.server.am.BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda18
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                this.f$0.lambda$new$12(broadcastRecord, i);
            }
        };
        this.mBroadcastConsumerDeferClear = new com.android.server.am.BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda19
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
            public final void accept(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                this.f$0.lambda$new$13(broadcastRecord, i);
            }
        };
        this.mBroadcastRecordConsumerEnqueue = new com.android.server.am.BroadcastProcessQueue.BroadcastRecordConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda20
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastRecordConsumer
            public final void accept(com.android.server.am.BroadcastRecord broadcastRecord) {
                this.f$0.enqueueBroadcastLocked(broadcastRecord);
            }
        };
        this.mWrapper = new com.android.server.am.BroadcastQueueModernImpl.BroadcastQueueWrapper();
        this.mBroadcastQueueModernImplExt = (com.android.server.am.IBroadcastQueueModernImplExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBroadcastQueueModernImplExt.class).base(this).create();
        this.mConstants = (com.android.server.am.BroadcastConstants) java.util.Objects.requireNonNull(fgConstants);
        this.mFgConstants = (com.android.server.am.BroadcastConstants) java.util.Objects.requireNonNull(fgConstants);
        this.mBgConstants = (com.android.server.am.BroadcastConstants) java.util.Objects.requireNonNull(bgConstants);
        this.mLocalHandler = new android.os.Handler(handler.getLooper(), this.mLocalCallback);
        this.mRunning = new com.android.server.am.BroadcastProcessQueue[this.mConstants.getMaxRunningQueues()];
        this.mAnrTimer = new com.android.server.am.BroadcastQueueModernImpl.BroadcastAnrTimer(this.mLocalHandler);
        this.mBroadcastQueueModernImplExt.initArgs(service, handler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enqueueUpdateRunningList() {
        this.mLocalHandler.removeMessages(1);
        this.mLocalHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                updateRunningList();
                return true;
            case 2:
                deliveryTimeout((com.android.server.am.BroadcastProcessQueue) msg.obj);
                return true;
            case 3:
                com.android.server.am.ActivityManagerService activityManagerService = this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                        com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) args.arg1;
                        com.android.server.am.BroadcastRecord r = (com.android.server.am.BroadcastRecord) args.arg2;
                        args.recycle();
                        app.removeBackgroundStartPrivileges(r);
                    } finally {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                    break;
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            case 4:
                checkHealth();
                return true;
            case 5:
                com.android.server.am.ActivityManagerService activityManagerService2 = this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService2) {
                    try {
                        this.mCheckPendingColdStartQueued = false;
                        checkPendingColdStartValidityLocked();
                    } finally {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                    break;
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            case 6:
                handleProcessFreezableChanged((com.android.server.am.ProcessRecord) msg.obj);
                return true;
            case 7:
                int uid = ((java.lang.Integer) msg.obj).intValue();
                int procState = msg.arg1;
                com.android.server.am.ActivityManagerService activityManagerService3 = this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService3) {
                    try {
                        if (procState == 2) {
                            this.mUidForeground.put(uid, true);
                        } else {
                            this.mUidForeground.delete(uid);
                        }
                        refreshProcessQueuesLocked(uid);
                    } finally {
                    }
                    break;
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            case 8:
                com.android.server.am.ActivityManagerService activityManagerService4 = this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService4) {
                    try {
                        deliveryTimeoutSoftLocked((com.android.server.am.BroadcastProcessQueue) msg.obj, msg.arg1);
                    } finally {
                    }
                    break;
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return true;
            default:
                return false;
        }
    }

    private int getRunningSize() {
        int size = 0;
        for (int i = 0; i < this.mRunning.length; i++) {
            if (this.mRunning[i] != null) {
                size++;
            }
        }
        return size;
    }

    private int getRunningUrgentCount() {
        int count = 0;
        for (int i = 0; i < this.mRunning.length; i++) {
            if (this.mRunning[i] != null && this.mRunning[i].getActive().isUrgent()) {
                count++;
            }
        }
        return count;
    }

    private int getRunningIndexOf(com.android.server.am.BroadcastProcessQueue test) {
        for (int i = 0; i < this.mRunning.length; i++) {
            if (this.mRunning[i] == test) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRunnableList(com.android.server.am.BroadcastProcessQueue queue) {
        if (getRunningIndexOf(queue) >= 0) {
            return;
        }
        queue.updateDeferredStates(this.mBroadcastConsumerDeferApply, this.mBroadcastConsumerDeferClear);
        queue.updateRunnableAt();
        boolean wantQueue = queue.isRunnable();
        boolean inQueue = (queue != this.mRunnableHead && queue.runnableAtPrev == null && queue.runnableAtNext == null) ? false : true;
        if (wantQueue) {
            if (inQueue) {
                boolean prevLower = queue.runnableAtPrev == null || queue.runnableAtPrev.getRunnableAt() <= queue.getRunnableAt();
                boolean nextHigher = queue.runnableAtNext == null || queue.runnableAtNext.getRunnableAt() >= queue.getRunnableAt();
                if (!prevLower || !nextHigher) {
                    this.mRunnableHead = com.android.server.am.BroadcastProcessQueue.removeFromRunnableList(this.mRunnableHead, queue);
                    this.mRunnableHead = com.android.server.am.BroadcastProcessQueue.insertIntoRunnableList(this.mRunnableHead, queue);
                }
            } else {
                this.mRunnableHead = com.android.server.am.BroadcastProcessQueue.insertIntoRunnableList(this.mRunnableHead, queue);
            }
        } else if (inQueue) {
            this.mRunnableHead = com.android.server.am.BroadcastProcessQueue.removeFromRunnableList(this.mRunnableHead, queue);
        }
        if (queue.isEmpty() && queue.isOutgoingEmpty() && !queue.isActive() && !queue.isProcessWarm()) {
            removeProcessQueue(queue.processName, queue.uid);
        }
    }

    private void updateRunningList() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                updateRunningListLocked();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x010d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updateRunningListLocked() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 300
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.BroadcastQueueModernImpl.updateRunningListLocked():void");
    }

    private boolean isPendingColdStartValid() {
        if (this.mRunningColdStart.app.getPid() > 0) {
            return !this.mRunningColdStart.app.isKilled();
        }
        return this.mRunningColdStart.app.isPendingStart();
    }

    private void clearInvalidPendingColdStart() {
        logw("Clearing invalid pending cold start: " + this.mRunningColdStart);
        if (this.mRunningColdStart.wasActiveBroadcastReEnqueued()) {
            finishReceiverActiveLocked(this.mRunningColdStart, 5, "invalid start with re-enqueued broadcast");
        } else {
            this.mRunningColdStart.reEnqueueActiveBroadcast();
        }
        demoteFromRunningLocked(this.mRunningColdStart);
        clearRunningColdStart();
        enqueueUpdateRunningList();
    }

    private void checkPendingColdStartValidityLocked() {
        if (this.mRunningColdStart == null) {
            return;
        }
        if (isPendingColdStartValid()) {
            if (!this.mCheckPendingColdStartQueued) {
                this.mLocalHandler.sendEmptyMessageDelayed(5, this.mConstants.PENDING_COLD_START_CHECK_INTERVAL_MILLIS);
                this.mCheckPendingColdStartQueued = true;
                return;
            }
            return;
        }
        clearInvalidPendingColdStart();
    }

    private void finishOrReEnqueueActiveBroadcast(com.android.server.am.BroadcastProcessQueue queue) {
        checkState(queue.isActive(), "isActive");
        if (queue.wasActiveBroadcastReEnqueued()) {
            finishReceiverActiveLocked(queue, 5, "re-enqueued broadcast delivery failed");
            return;
        }
        com.android.server.am.BroadcastRecord record = queue.getActive();
        int index = queue.getActiveIndex();
        setDeliveryState(queue, queue.app, record, index, record.receivers.get(index), 0, "reEnqueueActiveBroadcast");
        queue.reEnqueueActiveBroadcast();
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean onApplicationAttachedLocked(com.android.server.am.ProcessRecord app) throws com.android.server.am.BroadcastRetryException {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            logv("Process " + app + " is attached");
        }
        com.android.server.am.BroadcastProcessQueue queue = getProcessQueue(app);
        if (queue != null) {
            setQueueProcess(queue, app);
            queue.clearOutgoingBroadcasts();
        }
        if (this.mRunningColdStart == null || this.mRunningColdStart != queue) {
            return false;
        }
        this.mRunningColdStart = null;
        notifyStartedRunning(queue);
        this.mService.updateOomAdjPendingTargetsLocked(3);
        queue.traceProcessEnd();
        queue.traceProcessRunningBegin();
        try {
            if (scheduleReceiverWarmLocked(queue)) {
                demoteFromRunningLocked(queue);
            }
            enqueueUpdateRunningList();
            return true;
        } catch (com.android.server.am.BroadcastRetryException e) {
            finishOrReEnqueueActiveBroadcast(queue);
            demoteFromRunningLocked(queue);
            throw e;
        }
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationTimeoutLocked(com.android.server.am.ProcessRecord app) {
        onApplicationCleanupLocked(app);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationProblemLocked(com.android.server.am.ProcessRecord app) {
        onApplicationCleanupLocked(app);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onApplicationCleanupLocked(com.android.server.am.ProcessRecord app) {
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            logv("Process " + app + " is cleaned up");
        }
        com.android.server.am.BroadcastProcessQueue queue = getProcessQueue(app);
        if (this.mRunningColdStart != null && this.mRunningColdStart == queue && this.mRunningColdStart.app == app) {
            clearRunningColdStart();
        }
        if (queue != null && queue.app == app) {
            setQueueProcess(queue, null);
            if (queue.isActive()) {
                finishReceiverActiveLocked(queue, 5, "onApplicationCleanupLocked");
                demoteFromRunningLocked(queue);
            }
            queue.clearOutgoingBroadcasts();
            boolean didSomething = queue.forEachMatchingBroadcast(new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda8
                @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                    return com.android.server.am.BroadcastQueueModernImpl.lambda$onApplicationCleanupLocked$1(broadcastRecord, i);
                }
            }, this.mBroadcastConsumerSkip, true);
            if (didSomething || queue.isEmpty()) {
                updateRunnableList(queue);
                enqueueUpdateRunningList();
            }
        }
    }

    static /* synthetic */ boolean lambda$onApplicationCleanupLocked$1(com.android.server.am.BroadcastRecord r, int i) {
        return r.receivers.get(i) instanceof com.android.server.am.BroadcastFilter;
    }

    private void clearRunningColdStart() {
        this.mRunningColdStart.traceProcessEnd();
        this.mRunningColdStart = null;
        enqueueUpdateRunningList();
    }

    @Override // com.android.server.am.BroadcastQueue
    public void onProcessFreezableChangedLocked(com.android.server.am.ProcessRecord app) {
        this.mLocalHandler.removeMessages(6, app);
        this.mLocalHandler.obtainMessage(6, app).sendToTarget();
    }

    @Override // com.android.server.am.BroadcastQueue
    public int getPreferredSchedulingGroupLocked(com.android.server.am.ProcessRecord app) {
        com.android.server.am.BroadcastProcessQueue queue = getProcessQueue(app);
        if (queue != null && getRunningIndexOf(queue) >= 0) {
            return queue.getPreferredSchedulingGroupLocked();
        }
        return Integer.MIN_VALUE;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void enqueueBroadcastLocked(com.android.server.am.BroadcastRecord r) {
        android.util.ArraySet<com.android.server.am.BroadcastRecord> replacedBroadcasts;
        android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> matchingBroadcasts;
        if (com.android.server.am.Flags.deferOutgoingBroadcasts() && isProcessFreezable(r.callerApp)) {
            com.android.server.am.BroadcastProcessQueue queue = getOrCreateProcessQueue(r.callerApp.processName, r.callerApp.uid);
            if (queue.getOutgoingBroadcastCount() >= this.mConstants.MAX_FROZEN_OUTGOING_BROADCASTS) {
                r.callerApp.killLocked("Too many outgoing broadcasts in cached state", 13, 32, true);
                return;
            }
            queue.enqueueOutgoingBroadcast(r);
            this.mHistory.onBroadcastFrozenLocked(r);
            this.mService.mOomAdjuster.mCachedAppOptimizer.freezeAppAsyncImmediateLSP(r.callerApp);
            return;
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            logv("Enqueuing " + r + " for " + r.receivers.size() + " receivers");
        }
        int cookie = traceBegin("enqueueBroadcast");
        r.applySingletonPolicy(this.mService);
        applyDeliveryGroupPolicy(r);
        r.enqueueTime = android.os.SystemClock.uptimeMillis();
        r.enqueueRealTime = android.os.SystemClock.elapsedRealtime();
        r.enqueueClockTime = java.lang.System.currentTimeMillis();
        this.mHistory.onBroadcastEnqueuedLocked(r);
        android.util.ArraySet<com.android.server.am.BroadcastRecord> replacedBroadcasts2 = this.mReplacedBroadcastsCache.getAndSet(null);
        if (replacedBroadcasts2 != null) {
            replacedBroadcasts = replacedBroadcasts2;
        } else {
            replacedBroadcasts = new android.util.ArraySet<>();
        }
        android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> matchingBroadcasts2 = this.mMatchingRecordsCache.getAndSet(null);
        if (matchingBroadcasts2 != null) {
            matchingBroadcasts = matchingBroadcasts2;
        } else {
            matchingBroadcasts = new android.util.ArrayMap<>();
        }
        r.setMatchingRecordsCache(matchingBroadcasts);
        boolean enqueuedBroadcast = false;
        for (int i = 0; i < r.receivers.size(); i++) {
            java.lang.Object receiver = r.receivers.get(i);
            com.android.server.am.BroadcastProcessQueue queue2 = getOrCreateProcessQueue(com.android.server.am.BroadcastRecord.getReceiverProcessName(receiver), com.android.server.am.BroadcastRecord.getReceiverUid(receiver));
            java.lang.String skipReason = this.mSkipPolicy.shouldSkipMessage(r, receiver);
            if (skipReason != null) {
                setDeliveryState(null, null, r, i, receiver, 2, "skipped by policy at enqueue: " + skipReason);
            } else {
                com.android.server.am.BroadcastRecord replacedBroadcast = queue2.enqueueOrReplaceBroadcast(r, i, this.mBroadcastConsumerDeferApply);
                this.mBroadcastQueueModernImplExt.handleEnqueuedBroadcastOption(r, 2, i);
                if (replacedBroadcast != null) {
                    replacedBroadcasts.add(replacedBroadcast);
                }
                updateRunnableList(queue2);
                enqueueUpdateRunningList();
                enqueuedBroadcast = true;
            }
        }
        skipAndCancelReplacedBroadcasts(replacedBroadcasts);
        replacedBroadcasts.clear();
        this.mReplacedBroadcastsCache.compareAndSet(null, replacedBroadcasts);
        matchingBroadcasts.clear();
        r.clearMatchingRecordsCache();
        this.mMatchingRecordsCache.compareAndSet(null, matchingBroadcasts);
        if (r.receivers.isEmpty() || !enqueuedBroadcast) {
            scheduleResultTo(r);
            notifyFinishBroadcast(r);
        }
        traceEnd(cookie);
    }

    private void skipAndCancelReplacedBroadcasts(android.util.ArraySet<com.android.server.am.BroadcastRecord> replacedBroadcasts) {
        for (int i = 0; i < replacedBroadcasts.size(); i++) {
            com.android.server.am.BroadcastRecord r = replacedBroadcasts.valueAt(i);
            for (int rcvrIdx = 0; rcvrIdx < r.receivers.size(); rcvrIdx++) {
                if (!com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(r.getDeliveryState(rcvrIdx))) {
                    this.mBroadcastConsumerSkipAndCanceled.accept(r, rcvrIdx);
                }
            }
        }
    }

    private void applyDeliveryGroupPolicy(final com.android.server.am.BroadcastRecord r) {
        com.android.server.am.BroadcastProcessQueue.BroadcastConsumer broadcastConsumer;
        final android.os.BundleMerger extrasMerger;
        if (this.mService.shouldIgnoreDeliveryGroupPolicy(r.intent.getAction())) {
            return;
        }
        int policy = r.getDeliveryGroupPolicy();
        switch (policy) {
            case 0:
                return;
            case 1:
                broadcastConsumer = this.mBroadcastConsumerSkipAndCanceled;
                break;
            case 2:
                if (r.receivers.size() > 1 || (extrasMerger = r.options.getDeliveryGroupExtrasMerger()) == null) {
                    return;
                }
                broadcastConsumer = new com.android.server.am.BroadcastProcessQueue.BroadcastConsumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda21
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastConsumer
                    public final void accept(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                        this.f$0.lambda$applyDeliveryGroupPolicy$2(r, extrasMerger, broadcastRecord, i);
                    }
                };
                break;
                break;
            default:
                logw("Unknown delivery group policy: " + policy);
                return;
        }
        final android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> recordsLookupCache = getRecordsLookupCache();
        forEachMatchingBroadcast(QUEUE_PREDICATE_ANY, new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda22
            @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
            public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                return this.f$0.lambda$applyDeliveryGroupPolicy$3(r, recordsLookupCache, broadcastRecord, i);
            }
        }, broadcastConsumer, true);
        this.mBroadcastQueueModernImplExt.handleEnqueuedBroadcastOption(r, 1, -1);
        recordsLookupCache.clear();
        this.mRecordsLookupCache.compareAndSet(null, recordsLookupCache);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyDeliveryGroupPolicy$2(com.android.server.am.BroadcastRecord r, android.os.BundleMerger extrasMerger, com.android.server.am.BroadcastRecord record, int recordIndex) {
        r.intent.mergeExtras(record.intent, extrasMerger);
        this.mBroadcastConsumerSkipAndCanceled.accept(record, recordIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyDeliveryGroupPolicy$3(com.android.server.am.BroadcastRecord r, android.util.ArrayMap recordsLookupCache, com.android.server.am.BroadcastRecord testRecord, int testIndex) {
        if (com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(testRecord.getDeliveryState(testIndex)) || r.callingUid != testRecord.callingUid || r.userId != testRecord.userId || !r.matchesDeliveryGroup(testRecord)) {
            return false;
        }
        if (testRecord.ordered || testRecord.prioritized) {
            return containsAllReceivers(r, testRecord, recordsLookupCache);
        }
        if (testRecord.resultTo != null) {
            if (testRecord.getDeliveryState(testIndex) == 6) {
                return r.containsReceiver(testRecord.receivers.get(testIndex));
            }
            return containsAllReceivers(r, testRecord, recordsLookupCache);
        }
        return r.containsReceiver(testRecord.receivers.get(testIndex));
    }

    private android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> getRecordsLookupCache() {
        android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> recordsLookupCache = this.mRecordsLookupCache.getAndSet(null);
        if (recordsLookupCache == null) {
            return new android.util.ArrayMap<>();
        }
        return recordsLookupCache;
    }

    private boolean containsAllReceivers(com.android.server.am.BroadcastRecord record, com.android.server.am.BroadcastRecord testRecord, android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> recordsLookupCache) {
        int idx = recordsLookupCache.indexOfKey(testRecord);
        if (idx > 0) {
            return recordsLookupCache.valueAt(idx).booleanValue();
        }
        boolean containsAll = record.containsAllReceivers(testRecord.receivers);
        recordsLookupCache.put(testRecord, java.lang.Boolean.valueOf(containsAll));
        return containsAll;
    }

    private boolean scheduleReceiverColdLocked(com.android.server.am.BroadcastProcessQueue queue) {
        int zygotePolicyFlags;
        checkState(queue.isActive(), "isActive");
        queue.setActiveViaColdStart(true);
        com.android.server.am.BroadcastRecord r = queue.getActive();
        int index = queue.getActiveIndex();
        java.lang.Object receiver = r.receivers.get(index);
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            this.mRunningColdStart = null;
            finishReceiverActiveLocked(queue, 2, "BroadcastFilter for cold app");
            return true;
        }
        java.lang.String skipReason = shouldSkipReceiver(queue, r, index);
        if (skipReason == null) {
            java.lang.String oplusSkipReason = this.mBroadcastQueueModernImplExt.skipScheduleReceiverColdLocked(this, queue, r, (android.content.pm.ResolveInfo) receiver);
            if (oplusSkipReason != null) {
                this.mRunningColdStart = null;
                finishReceiverActiveLocked(queue, 2, oplusSkipReason);
                return true;
            }
            android.content.pm.ApplicationInfo info = ((android.content.pm.ResolveInfo) receiver).activityInfo.applicationInfo;
            android.content.ComponentName component = ((android.content.pm.ResolveInfo) receiver).activityInfo.getComponentName();
            if ((info.flags & 2097152) != 0) {
                queue.setActiveWasStopped(true);
            }
            int intentFlags = r.intent.getFlags() | 4;
            boolean firstLaunch = !this.mService.wasPackageEverLaunched(info.packageName, r.userId);
            queue.setActiveFirstLaunch(firstLaunch);
            com.android.server.am.HostingRecord hostingRecord = new com.android.server.am.HostingRecord("broadcast", component, r.intent.getAction(), r.getHostingRecordTriggerType());
            hostingRecord.getWrapper().getExtImpl().setCallerUid(r.callingUid);
            hostingRecord.getWrapper().getExtImpl().setAction(r.intent.getAction());
            boolean isActivityCapable = r.options != null && r.options.getTemporaryAppAllowlistDuration() > 0;
            if (isActivityCapable) {
                zygotePolicyFlags = 1;
            } else {
                zygotePolicyFlags = 0;
            }
            boolean allowWhileBooting = (r.intent.getFlags() & 33554432) != 0;
            long startTimeNs = android.os.SystemClock.uptimeNanos();
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
                logv("Scheduling " + r + " to cold " + queue);
            }
            queue.app = this.mService.startProcessLocked(queue.processName, info, true, intentFlags, hostingRecord, zygotePolicyFlags, allowWhileBooting, false);
            if (queue.app == null) {
                this.mRunningColdStart = null;
                finishReceiverActiveLocked(queue, 5, "startProcessLocked failed");
                return true;
            }
            this.mBroadcastQueueModernImplExt.hookScheduleReceiverColdAfterStartProc(r, (android.content.pm.ResolveInfo) receiver);
            this.mService.mProcessList.getAppStartInfoTracker().handleProcessBroadcastStart(startTimeNs, queue.app, r.getReceiverIntent(receiver), r.alarm);
            return false;
        }
        this.mRunningColdStart = null;
        finishReceiverActiveLocked(queue, 2, skipReason);
        return true;
    }

    private boolean scheduleReceiverWarmLocked(com.android.server.am.BroadcastProcessQueue queue) throws java.lang.Throwable {
        checkState(queue.isActive(), "isActive");
        int cookie = traceBegin("scheduleReceiverWarmLocked");
        while (queue.isActive()) {
            com.android.server.am.BroadcastRecord r = queue.getActive();
            int index = queue.getActiveIndex();
            if (r.terminalCount == 0) {
                r.dispatchTime = android.os.SystemClock.uptimeMillis();
                r.dispatchRealTime = android.os.SystemClock.elapsedRealtime();
                r.dispatchClockTime = java.lang.System.currentTimeMillis();
            }
            java.lang.String skipReason = shouldSkipReceiver(queue, r, index);
            if (skipReason == null) {
                skipReason = this.mBroadcastQueueModernImplExt.skipScheduleReceiverWarmLocked(this, r, r.receivers.get(index));
            }
            if (skipReason == null) {
                boolean isBlockingDispatch = dispatchReceivers(queue, r, index);
                if (isBlockingDispatch) {
                    traceEnd(cookie);
                    return false;
                }
            } else {
                finishReceiverActiveLocked(queue, 2, skipReason);
            }
            if (shouldRetire(queue)) {
                break;
            }
            queue.makeActiveNextPending();
        }
        traceEnd(cookie);
        return true;
    }

    private java.lang.String shouldSkipReceiver(com.android.server.am.BroadcastProcessQueue queue, com.android.server.am.BroadcastRecord r, int index) {
        int oldDeliveryState = getDeliveryState(r, index);
        com.android.server.am.ProcessRecord app = queue.app;
        java.lang.Object receiver = r.receivers.get(index);
        if (com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(oldDeliveryState)) {
            return "already terminal state";
        }
        if (app != null && app.isInFullBackup()) {
            return "isInFullBackup";
        }
        java.lang.String skipReason = this.mSkipPolicy.shouldSkipMessage(r, receiver);
        if (skipReason != null) {
            return skipReason;
        }
        android.content.Intent receiverIntent = r.getReceiverIntent(receiver);
        if (receiverIntent == null) {
            return "getReceiverIntent";
        }
        if ((receiver instanceof com.android.server.am.BroadcastFilter) && ((com.android.server.am.BroadcastFilter) receiver).receiverList.pid != app.getPid()) {
            return "BroadcastFilter for mismatched PID";
        }
        if (this.mBroadcastQueueModernImplExt.shouldSkipReceiver(r, receiver)) {
            return "Skipping delivery to " + queue.uid + " register due to frozen state";
        }
        if (this.mBroadcastQueueModernImplExt.skipReceiverForOsense(r, receiver, queue.isProcessWarm())) {
            return "Skipping delivery to " + queue.uid + " register due to osense cpnproxy";
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.server.am.ProcessRecord] */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private boolean dispatchReceivers(com.android.server.am.BroadcastProcessQueue queue, com.android.server.am.BroadcastRecord r, int index) throws java.lang.Throwable {
        boolean z;
        long timeout;
        long j;
        com.android.server.am.ProcessRecord processRecord = queue.app;
        java.lang.Object receiver = r.receivers.get(index);
        boolean assumeDelivered = r.isAssumedDelivered(index);
        if (this.mService.mProcessesReady && !r.timeoutExempt && !assumeDelivered) {
            queue.setTimeoutScheduled(true);
            if (r.isForeground()) {
                j = this.mFgConstants.TIMEOUT;
            } else {
                j = this.mBgConstants.TIMEOUT;
            }
            int softTimeoutMillis = (int) j;
            startDeliveryTimeoutLocked(queue, softTimeoutMillis);
        } else {
            queue.setTimeoutScheduled(false);
        }
        if (r.mBackgroundStartPrivileges.allowsAny()) {
            processRecord.addOrUpdateBackgroundStartPrivileges(r, r.mBackgroundStartPrivileges);
            if (r.isForeground()) {
                timeout = this.mFgConstants.ALLOW_BG_ACTIVITY_START_TIMEOUT;
            } else {
                timeout = this.mBgConstants.ALLOW_BG_ACTIVITY_START_TIMEOUT;
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = processRecord;
            args.arg2 = r;
            this.mLocalHandler.sendMessageDelayed(android.os.Message.obtain(this.mLocalHandler, 3, args), timeout);
        }
        if (r.options != null && r.options.getTemporaryAppAllowlistDuration() > 0) {
            if (r.options.getTemporaryAppAllowlistType() == 4) {
                this.mService.mOomAdjuster.mCachedAppOptimizer.unfreezeTemporarily(processRecord, 3, r.options.getTemporaryAppAllowlistDuration());
            } else {
                if (!this.mBroadcastQueueModernImplExt.shouldIgnoreTempWhitelistChange(queue.uid, com.android.server.am.BroadcastRecord.getReceiverPackageName(receiver), true, java.util.Arrays.binarySearch(this.mService.mDeviceIdleAllowlist, android.os.UserHandle.getAppId(queue.uid)) >= 0)) {
                    this.mService.tempAllowlistUidLocked(queue.uid, r.options.getTemporaryAppAllowlistDuration(), r.options.getTemporaryAppAllowlistReasonCode(), r.toShortString(), r.options.getTemporaryAppAllowlistType(), r.callingUid);
                }
            }
        }
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST) {
            logv("Scheduling " + r + " to warm " + processRecord);
        }
        boolean z2 = processRecord;
        setDeliveryState(queue, z2, r, index, receiver, 4, "scheduleReceiverWarmLocked");
        android.content.Intent receiverIntent = r.getReceiverIntent(receiver);
        android.app.IApplicationThread thread = processRecord.getOnewayThread();
        if (thread == null) {
            finishReceiverActiveLocked(queue, 5, "missing IApplicationThread");
            return false;
        }
        try {
            if (r.shareIdentity) {
                this.mService.mPackageManagerInt.grantImplicitAccess(r.userId, r.intent, android.os.UserHandle.getAppId(processRecord.uid), r.callingUid, true);
            }
            queue.lastProcessState = processRecord.mState.getCurProcState();
            try {
                if (receiver instanceof com.android.server.am.BroadcastFilter) {
                    notifyScheduleRegisteredReceiver(processRecord, r, (com.android.server.am.BroadcastFilter) receiver);
                    try {
                        z = false;
                        try {
                            thread.scheduleRegisteredReceiver(((com.android.server.am.BroadcastFilter) receiver).receiverList.receiver, receiverIntent, r.resultCode, r.resultData, r.resultExtras, r.ordered, r.initialSticky, assumeDelivered, r.userId, processRecord.mState.getReportedProcState(), r.shareIdentity ? r.callingUid : -1, r.shareIdentity ? r.callerPackage : null);
                            this.mBroadcastQueueModernImplExt.broadcastStatistic(queue.app, r, receiver, this.mService.getUidStateLocked(queue.app.uid));
                            queue.getWrapper().getExtImpl().dispatchReceiverFinish(r, index);
                            if (assumeDelivered) {
                                finishReceiverActiveLocked(queue, 1, "assuming delivered");
                                return false;
                            }
                            return true;
                        } catch (android.os.RemoteException e) {
                            e = e;
                        }
                    } catch (android.os.RemoteException e2) {
                        e = e2;
                        z2 = 1;
                        z = false;
                    }
                } else {
                    notifyScheduleReceiver(processRecord, r, (android.content.pm.ResolveInfo) receiver);
                    thread.scheduleReceiver(receiverIntent, ((android.content.pm.ResolveInfo) receiver).activityInfo, (android.content.res.CompatibilityInfo) null, r.resultCode, r.resultData, r.resultExtras, r.ordered, assumeDelivered, r.userId, processRecord.mState.getReportedProcState(), r.shareIdentity ? r.callingUid : -1, r.shareIdentity ? r.callerPackage : null);
                    this.mBroadcastQueueModernImplExt.broadcastStatistic(queue.app, r, receiver, this.mService.getUidStateLocked(queue.app.uid));
                    queue.getWrapper().getExtImpl().dispatchReceiverFinish(r, index);
                    return true;
                }
            } catch (android.os.RemoteException e3) {
                e = e3;
            }
        } catch (android.os.RemoteException e4) {
            e = e4;
            z = false;
        }
        z2 = 1;
        java.lang.String msg = "Failed to schedule " + r + " to " + receiver + " via " + processRecord + ": " + e;
        logw(msg);
        processRecord.killLocked("Can't deliver broadcast", 13, 26, z2);
        if (!(receiver instanceof android.content.pm.ResolveInfo)) {
            finishReceiverActiveLocked(queue, 5, "remote app");
            return z;
        }
        cancelDeliveryTimeoutLocked(queue);
        throw new com.android.server.am.BroadcastRetryException(e);
    }

    private void scheduleResultTo(com.android.server.am.BroadcastRecord r) {
        if (r.resultTo == null) {
            return;
        }
        com.android.server.am.ProcessRecord app = r.resultToApp;
        android.app.IApplicationThread thread = app != null ? app.getOnewayThread() : null;
        if (thread != null) {
            this.mService.mOomAdjuster.unfreezeTemporarily(app, 2);
            if (r.shareIdentity && app.uid != r.callingUid) {
                this.mService.mPackageManagerInt.grantImplicitAccess(r.userId, r.intent, android.os.UserHandle.getAppId(app.uid), r.callingUid, true);
            }
            try {
                thread.scheduleRegisteredReceiver(r.resultTo, r.intent, r.resultCode, r.resultData, r.resultExtras, false, r.initialSticky, true, r.userId, app.mState.getReportedProcState(), r.shareIdentity ? r.callingUid : -1, r.shareIdentity ? r.callerPackage : null);
            } catch (android.os.RemoteException e) {
                java.lang.String msg = "Failed to schedule result of " + r + " via " + app + ": " + e;
                logw(msg);
                app.killLocked("Can't deliver broadcast", 13, 26, true);
            }
        }
        r.resultTo = null;
    }

    private void startDeliveryTimeoutLocked(com.android.server.am.BroadcastProcessQueue queue, int softTimeoutMillis) {
        if (this.mAnrTimer.serviceEnabled()) {
            this.mAnrTimer.start(queue, softTimeoutMillis);
        } else {
            queue.lastCpuDelayTime = queue.app.getCpuDelayTime();
            this.mLocalHandler.sendMessageDelayed(android.os.Message.obtain(this.mLocalHandler, 8, softTimeoutMillis, 0, queue), softTimeoutMillis);
        }
    }

    private void cancelDeliveryTimeoutLocked(com.android.server.am.BroadcastProcessQueue queue) {
        this.mAnrTimer.cancel(queue);
        if (!this.mAnrTimer.serviceEnabled()) {
            this.mLocalHandler.removeMessages(8, queue);
        }
    }

    private void deliveryTimeoutSoftLocked(com.android.server.am.BroadcastProcessQueue queue, int softTimeoutMillis) {
        if (queue.app != null) {
            long cpuDelayTime = queue.app.getCpuDelayTime() - queue.lastCpuDelayTime;
            long hardTimeoutMillis = android.util.MathUtils.constrain(cpuDelayTime, 0L, softTimeoutMillis);
            this.mAnrTimer.start(queue, hardTimeoutMillis);
            return;
        }
        deliveryTimeoutLocked(queue);
    }

    private void deliveryTimeout(com.android.server.am.BroadcastProcessQueue queue) {
        int cookie = traceBegin("deliveryTimeout");
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                deliveryTimeoutLocked(queue);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        traceEnd(cookie);
    }

    private void deliveryTimeoutLocked(com.android.server.am.BroadcastProcessQueue queue) {
        finishReceiverActiveLocked(queue, 3, "deliveryTimeoutLocked");
        demoteFromRunningLocked(queue);
    }

    private class BroadcastAnrTimer extends com.android.server.utils.AnrTimer<com.android.server.am.BroadcastProcessQueue> {
        BroadcastAnrTimer(android.os.Handler handler) {
            super((android.os.Handler) java.util.Objects.requireNonNull(handler), 2, "BROADCAST_TIMEOUT", new com.android.server.utils.AnrTimer.Args().extend(true).freeze(true));
        }

        @Override // com.android.server.utils.AnrTimer
        public int getPid(com.android.server.am.BroadcastProcessQueue queue) {
            if (queue.app != null) {
                return queue.app.getPid();
            }
            return 0;
        }

        @Override // com.android.server.utils.AnrTimer
        public int getUid(com.android.server.am.BroadcastProcessQueue queue) {
            if (queue.app != null) {
                return queue.app.uid;
            }
            return 0;
        }
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean finishReceiverLocked(com.android.server.am.ProcessRecord app, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, boolean resultAbort, boolean waitForServices) {
        boolean z;
        com.android.server.am.BroadcastProcessQueue queue = getProcessQueue(app);
        if (queue == null || !queue.isActive()) {
            logw("Ignoring finishReceiverLocked; no active broadcast for " + queue);
            return false;
        }
        com.android.server.am.BroadcastRecord r = queue.getActive();
        int index = queue.getActiveIndex();
        if (r.ordered) {
            r.resultCode = resultCode;
            r.resultData = resultData;
            r.resultExtras = resultExtras;
            if (!r.isNoAbort()) {
                r.resultAbort = resultAbort;
            }
        }
        boolean z2 = true;
        finishReceiverActiveLocked(queue, 1, "remote app");
        if (!r.resultAbort) {
            z = true;
        } else {
            int i = index + 1;
            while (i < r.receivers.size()) {
                setDeliveryState(null, null, r, i, r.receivers.get(i), 2, "resultAbort");
                i++;
                z2 = z2;
            }
            z = z2;
        }
        if (shouldRetire(queue)) {
            demoteFromRunningLocked(queue);
            return z;
        }
        queue.makeActiveNextPending();
        try {
            if (!scheduleReceiverWarmLocked(queue)) {
                return false;
            }
            demoteFromRunningLocked(queue);
            return z;
        } catch (com.android.server.am.BroadcastRetryException e) {
            finishOrReEnqueueActiveBroadcast(queue);
            demoteFromRunningLocked(queue);
            return z;
        }
    }

    private boolean shouldRetire(com.android.server.am.BroadcastProcessQueue queue) {
        boolean shouldRetire;
        if (android.os.UserHandle.isCore(queue.uid)) {
            int nonBlockingDeliveryCount = queue.getActiveAssumedDeliveryCountSinceIdle();
            int blockingDeliveryCount = queue.getActiveCountSinceIdle() - queue.getActiveAssumedDeliveryCountSinceIdle();
            shouldRetire = blockingDeliveryCount >= this.mConstants.MAX_CORE_RUNNING_BLOCKING_BROADCASTS || nonBlockingDeliveryCount >= this.mConstants.MAX_CORE_RUNNING_NON_BLOCKING_BROADCASTS;
        } else {
            shouldRetire = queue.getActiveCountSinceIdle() >= this.mConstants.MAX_RUNNING_ACTIVE_BROADCASTS;
        }
        return (queue.isRunnable() && queue.isProcessWarm() && !shouldRetire) ? false : true;
    }

    private void finishReceiverActiveLocked(com.android.server.am.BroadcastProcessQueue queue, int deliveryState, java.lang.String reason) {
        if (!queue.isActive()) {
            logw("Ignoring finishReceiverActiveLocked; no active broadcast for " + queue);
            return;
        }
        int cookie = traceBegin("finishReceiver");
        com.android.server.am.ProcessRecord app = queue.app;
        com.android.server.am.BroadcastRecord r = queue.getActive();
        int index = queue.getActiveIndex();
        java.lang.Object receiver = r.receivers.get(index);
        setDeliveryState(queue, app, r, index, receiver, deliveryState, reason);
        if (deliveryState == 3) {
            r.anrCount++;
            this.mBroadcastQueueModernImplExt.handleBroadcastTimeout(r, app, index);
            if (app != null && !app.isDebugging() && !this.mBroadcastQueueModernImplExt.ignoreAnr(app, r)) {
                java.lang.AutoCloseable timer = this.mAnrTimer.accept(queue);
                java.lang.String packageName = com.android.server.am.BroadcastRecord.getReceiverPackageName(receiver);
                java.lang.String className = com.android.server.am.BroadcastRecord.getReceiverClassName(receiver);
                com.android.internal.os.TimeoutRecord tr = com.android.internal.os.TimeoutRecord.forBroadcastReceiver(r.intent, packageName, className).setExpiredTimer(timer);
                this.mService.appNotResponding(queue.app, tr);
            } else {
                this.mAnrTimer.discard(queue);
            }
        } else if (queue.timeoutScheduled()) {
            cancelDeliveryTimeoutLocked(queue);
        }
        checkAndRemoveWaitingFor();
        traceEnd(cookie);
    }

    private void promoteToRunningLocked(com.android.server.am.BroadcastProcessQueue queue) {
        int queueIndex = getRunningIndexOf(null);
        this.mRunning[queueIndex] = queue;
        this.mRunnableHead = com.android.server.am.BroadcastProcessQueue.removeFromRunnableList(this.mRunnableHead, queue);
        queue.runningTraceTrackName = "BroadcastQueue.mRunning[" + queueIndex + "]";
        queue.runningOomAdjusted = queue.isPendingManifest() || queue.isPendingOrdered() || queue.isPendingResultTo();
        boolean processWarm = queue.isProcessWarm();
        if (processWarm) {
            notifyStartedRunning(queue);
        }
        queue.makeActiveNextPending();
        if (processWarm) {
            queue.traceProcessRunningBegin();
        } else {
            queue.traceProcessStartingBegin();
        }
    }

    private void demoteFromRunningLocked(com.android.server.am.BroadcastProcessQueue queue) {
        if (!queue.isActive()) {
            logw("Ignoring demoteFromRunning; no active broadcast for " + queue);
            return;
        }
        int cookie = traceBegin("demoteFromRunning");
        queue.makeActiveIdle();
        queue.traceProcessEnd();
        int queueIndex = getRunningIndexOf(queue);
        this.mRunning[queueIndex] = null;
        updateRunnableList(queue);
        enqueueUpdateRunningList();
        notifyStoppedRunning(queue);
        traceEnd(cookie);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDeliveryState(com.android.server.am.BroadcastProcessQueue queue, com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r, int index, java.lang.Object receiver, int newDeliveryState, java.lang.String reason) {
        int cookie = traceBegin("setDeliveryState");
        int oldDeliveryState = getDeliveryState(r, index);
        boolean beyondCountChanged = r.setDeliveryState(index, newDeliveryState, reason);
        if (queue != null) {
            if (newDeliveryState == 4) {
                queue.traceActiveBegin();
                r.getWrapper().setDeliveryState(index, queue.getWrapper().getRunnableAtWithoutRefresh(), queue.getWrapper().getRunnableAtReasonWithoutRefresh());
            } else if (oldDeliveryState == 4 && com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(newDeliveryState)) {
                queue.traceActiveEnd();
            }
        }
        if (!com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(oldDeliveryState) && com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(newDeliveryState)) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_BROADCAST && newDeliveryState != 1) {
                logw("Delivery state of " + r + " to " + receiver + " via " + app + " changed from " + com.android.server.am.BroadcastRecord.deliveryStateToString(oldDeliveryState) + " to " + com.android.server.am.BroadcastRecord.deliveryStateToString(newDeliveryState) + " because " + reason);
            }
            notifyFinishReceiver(queue, app, r, index, receiver);
        }
        if (beyondCountChanged) {
            if (r.beyondCount == r.receivers.size()) {
                scheduleResultTo(r);
            }
            if (r.ordered || r.prioritized) {
                for (int i = 0; i < r.receivers.size(); i++) {
                    if (!com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(getDeliveryState(r, i)) || i == index) {
                        java.lang.Object otherReceiver = r.receivers.get(i);
                        com.android.server.am.BroadcastProcessQueue otherQueue = getProcessQueue(com.android.server.am.BroadcastRecord.getReceiverProcessName(otherReceiver), com.android.server.am.BroadcastRecord.getReceiverUid(otherReceiver));
                        if (otherQueue != null) {
                            otherQueue.invalidateRunnableAt();
                            updateRunnableList(otherQueue);
                        }
                    }
                }
                enqueueUpdateRunningList();
            }
        }
        traceEnd(cookie);
    }

    private int getDeliveryState(com.android.server.am.BroadcastRecord r, int index) {
        return r.getDeliveryState(index);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean cleanupDisabledPackageReceiversLocked(final java.lang.String packageName, final java.util.Set<java.lang.String> filterByClasses, final int userId) {
        java.util.function.Predicate<com.android.server.am.BroadcastProcessQueue> queuePredicate;
        com.android.server.am.BroadcastProcessQueue.BroadcastPredicate broadcastPredicate;
        if (packageName != null) {
            final int uid = this.mService.mPackageManagerInt.getPackageUid(packageName, 8192L, userId);
            queuePredicate = new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.am.BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$4(uid, (com.android.server.am.BroadcastProcessQueue) obj);
                }
            };
            if (filterByClasses != null) {
                broadcastPredicate = new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda3
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                        return com.android.server.am.BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$5(packageName, filterByClasses, broadcastRecord, i);
                    }
                };
            } else {
                broadcastPredicate = new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda4
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                        return com.android.server.am.BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$6(packageName, broadcastRecord, i);
                    }
                };
            }
        } else {
            queuePredicate = new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.am.BroadcastQueueModernImpl.lambda$cleanupDisabledPackageReceiversLocked$7(userId, (com.android.server.am.BroadcastProcessQueue) obj);
                }
            };
            broadcastPredicate = BROADCAST_PREDICATE_ANY;
            cleanupUserStateLocked(this.mUidForeground, userId);
        }
        this.mBroadcastQueueModernImplExt.cleanupDisabledPackageReceiversLocked(packageName, filterByClasses, userId);
        return forEachMatchingBroadcast(queuePredicate, broadcastPredicate, this.mBroadcastConsumerSkip, true);
    }

    static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$4(int uid, com.android.server.am.BroadcastProcessQueue q) {
        return q.uid == uid;
    }

    static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$5(java.lang.String packageName, java.util.Set filterByClasses, com.android.server.am.BroadcastRecord r, int i) {
        java.lang.Object receiver = r.receivers.get(i);
        if (!(receiver instanceof android.content.pm.ResolveInfo)) {
            return false;
        }
        android.content.pm.ActivityInfo info = ((android.content.pm.ResolveInfo) receiver).activityInfo;
        return packageName.equals(info.packageName) && filterByClasses.contains(info.name);
    }

    static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$6(java.lang.String packageName, com.android.server.am.BroadcastRecord r, int i) {
        java.lang.Object receiver = r.receivers.get(i);
        return packageName.equals(com.android.server.am.BroadcastRecord.getReceiverPackageName(receiver));
    }

    static /* synthetic */ boolean lambda$cleanupDisabledPackageReceiversLocked$7(int userId, com.android.server.am.BroadcastProcessQueue q) {
        return android.os.UserHandle.getUserId(q.uid) == userId;
    }

    private void cleanupUserStateLocked(android.util.SparseBooleanArray uidState, int userId) {
        for (int i = uidState.size() - 1; i >= 0; i--) {
            int uid = uidState.keyAt(i);
            if (android.os.UserHandle.getUserId(uid) == userId) {
                uidState.removeAt(i);
            }
        }
    }

    static /* synthetic */ boolean lambda$static$8(com.android.server.am.BroadcastProcessQueue q) {
        return true;
    }

    static /* synthetic */ boolean lambda$static$9(com.android.server.am.BroadcastRecord r, int i) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10(com.android.server.am.BroadcastRecord r, int i) {
        setDeliveryState(null, null, r, i, r.receivers.get(i), 2, "mBroadcastConsumerSkip");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11(com.android.server.am.BroadcastRecord r, int i) {
        setDeliveryState(null, null, r, i, r.receivers.get(i), 2, "mBroadcastConsumerSkipAndCanceled");
        r.resultCode = 0;
        r.resultData = null;
        r.resultExtras = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12(com.android.server.am.BroadcastRecord r, int i) {
        setDeliveryState(null, null, r, i, r.receivers.get(i), 6, "mBroadcastConsumerDeferApply");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$13(com.android.server.am.BroadcastRecord r, int i) {
        setDeliveryState(null, null, r, i, r.receivers.get(i), 0, "mBroadcastConsumerDeferClear");
    }

    private boolean testAllProcessQueues(java.util.function.Predicate<com.android.server.am.BroadcastProcessQueue> test, java.lang.String label, java.io.PrintWriter pw) {
        for (int i = 0; i < this.mProcessQueues.size(); i++) {
            for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.valueAt(i); leaf != null; leaf = leaf.processNameNext) {
                if (!test.test(leaf)) {
                    long now = android.os.SystemClock.uptimeMillis();
                    if (now > this.mLastTestFailureTime + 1000) {
                        this.mLastTestFailureTime = now;
                        pw.println("Test " + label + " failed due to " + leaf.toShortString() + " " + leaf.describeStateLocked());
                        pw.flush();
                        return false;
                    }
                    return false;
                }
            }
        }
        pw.println("Test " + label + " passed");
        pw.flush();
        return true;
    }

    private boolean forEachMatchingBroadcast(java.util.function.Predicate<com.android.server.am.BroadcastProcessQueue> queuePredicate, com.android.server.am.BroadcastProcessQueue.BroadcastPredicate broadcastPredicate, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer broadcastConsumer, boolean andRemove) {
        boolean didSomething = false;
        for (int i = this.mProcessQueues.size() - 1; i >= 0; i--) {
            for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.valueAt(i); leaf != null; leaf = leaf.processNameNext) {
                if (queuePredicate.test(leaf) && leaf.forEachMatchingBroadcast(broadcastPredicate, broadcastConsumer, andRemove)) {
                    updateRunnableList(leaf);
                    didSomething = true;
                }
            }
        }
        if (didSomething) {
            enqueueUpdateRunningList();
        }
        return didSomething;
    }

    private boolean forEachMatchingQueue(java.util.function.Predicate<com.android.server.am.BroadcastProcessQueue> queuePredicate, java.util.function.Consumer<com.android.server.am.BroadcastProcessQueue> queueConsumer) {
        boolean didSomething = false;
        for (int i = this.mProcessQueues.size() - 1; i >= 0; i--) {
            for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.valueAt(i); leaf != null; leaf = leaf.processNameNext) {
                if (queuePredicate.test(leaf)) {
                    queueConsumer.accept(leaf);
                    updateRunnableList(leaf);
                    didSomething = true;
                }
            }
        }
        if (didSomething) {
            enqueueUpdateRunningList();
        }
        return didSomething;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void start(android.content.ContentResolver resolver) {
        this.mFgConstants.startObserving(this.mHandler, resolver);
        this.mBgConstants.startObserving(this.mHandler, resolver);
        this.mService.registerUidObserver(new android.app.UidObserver() { // from class: com.android.server.am.BroadcastQueueModernImpl.1
            public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
                com.android.server.am.BroadcastQueueModernImpl.this.mLocalHandler.removeMessages(7, java.lang.Integer.valueOf(uid));
                com.android.server.am.BroadcastQueueModernImpl.this.mLocalHandler.obtainMessage(7, procState, 0, java.lang.Integer.valueOf(uid)).sendToTarget();
            }
        }, 1, 2, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
        this.mLocalHandler.sendEmptyMessage(4);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean isIdleLocked() {
        return lambda$waitForIdle$17(com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO);
    }

    /* JADX INFO: renamed from: isIdleLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForIdle$17(java.io.PrintWriter pw) {
        return testAllProcessQueues(new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.BroadcastProcessQueue) obj).isIdle();
            }
        }, "idle", pw);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean isBeyondBarrierLocked(long barrierTime) {
        return lambda$waitForBarrier$19(barrierTime, com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO);
    }

    /* JADX INFO: renamed from: isBeyondBarrierLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForBarrier$19(final long barrierTime, java.io.PrintWriter pw) {
        return testAllProcessQueues(new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.BroadcastProcessQueue) obj).isBeyondBarrierLocked(barrierTime);
            }
        }, "barrier", pw);
    }

    @Override // com.android.server.am.BroadcastQueue
    public boolean isDispatchedLocked(android.content.Intent intent) {
        return lambda$waitForDispatched$21(intent, com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO);
    }

    /* JADX INFO: renamed from: isDispatchedLocked, reason: merged with bridge method [inline-methods] */
    public boolean lambda$waitForDispatched$21(final android.content.Intent intent, java.io.PrintWriter pw) {
        return testAllProcessQueues(new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.am.BroadcastProcessQueue) obj).isDispatched(intent);
            }
        }, "dispatch of " + intent, pw);
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForIdle(final java.io.PrintWriter pw) {
        waitFor(new java.util.function.BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$waitForIdle$17(pw);
            }
        });
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForBarrier(final java.io.PrintWriter pw) {
        final long now = android.os.SystemClock.uptimeMillis();
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                forEachMatchingQueue(QUEUE_PREDICATE_ANY, new java.util.function.Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda23
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.am.BroadcastProcessQueue) obj).addPrioritizeEarliestRequest();
                    }
                });
            } finally {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        try {
            waitFor(new java.util.function.BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda24
                @Override // java.util.function.BooleanSupplier
                public final boolean getAsBoolean() {
                    return this.f$0.lambda$waitForBarrier$19(now, pw);
                }
            });
            com.android.server.am.ActivityManagerService activityManagerService2 = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService2) {
                try {
                    forEachMatchingQueue(QUEUE_PREDICATE_ANY, new java.util.function.Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda25
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.am.BroadcastProcessQueue) obj).removePrioritizeEarliestRequest();
                        }
                    });
                } finally {
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        } catch (java.lang.Throwable th) {
            com.android.server.am.ActivityManagerService activityManagerService3 = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService3) {
                try {
                    forEachMatchingQueue(QUEUE_PREDICATE_ANY, new java.util.function.Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda25
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.am.BroadcastProcessQueue) obj).removePrioritizeEarliestRequest();
                        }
                    });
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                } finally {
                }
            }
        }
    }

    @Override // com.android.server.am.BroadcastQueue
    public void waitForDispatched(final android.content.Intent intent, final java.io.PrintWriter pw) {
        waitFor(new java.util.function.BooleanSupplier() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda10
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return this.f$0.lambda$waitForDispatched$21(intent, pw);
            }
        });
    }

    private void waitFor(java.util.function.BooleanSupplier condition) {
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                this.mWaitingFor.add(android.util.Pair.create(condition, latch));
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        enqueueUpdateRunningList();
        try {
            latch.await();
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private void checkAndRemoveWaitingFor() {
        if (!this.mWaitingFor.isEmpty()) {
            this.mWaitingFor.removeIf(new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.am.BroadcastQueueModernImpl.lambda$checkAndRemoveWaitingFor$22((android.util.Pair) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$checkAndRemoveWaitingFor$22(android.util.Pair pair) {
        if (((java.util.function.BooleanSupplier) pair.first).getAsBoolean()) {
            ((java.util.concurrent.CountDownLatch) pair.second).countDown();
            return true;
        }
        return false;
    }

    @Override // com.android.server.am.BroadcastQueue
    public void forceDelayBroadcastDelivery(final java.lang.String targetPackage, final long delayedDurationMs) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                forEachMatchingQueue(new java.util.function.Predicate() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda12
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return targetPackage.equals(((com.android.server.am.BroadcastProcessQueue) obj).getPackageName());
                    }
                }, new java.util.function.Consumer() { // from class: com.android.server.am.BroadcastQueueModernImpl$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.am.BroadcastProcessQueue) obj).forceDelayBroadcastDelivery(delayedDurationMs);
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    @Override // com.android.server.am.BroadcastQueue
    public java.lang.String describeStateLocked() {
        return getRunningSize() + " running";
    }

    @Override // com.android.server.am.BroadcastQueue
    public void backgroundServicesFinishedLocked(int userId) {
    }

    private void checkHealth() {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                checkHealthLocked();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void checkHealthLocked() {
        try {
            assertHealthLocked();
            this.mLocalHandler.sendEmptyMessageDelayed(4, 60000L);
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf(com.android.server.am.BroadcastQueue.TAG, e);
            dumpToDropBoxLocked(e.toString());
        }
    }

    void assertHealthLocked() {
        com.android.server.am.BroadcastProcessQueue prev = null;
        com.android.server.am.BroadcastProcessQueue next = this.mRunnableHead;
        while (true) {
            if (next == null) {
                break;
            }
            checkState(next.runnableAtPrev == prev, "runnableAtPrev");
            checkState(next.isRunnable(), "isRunnable " + next);
            if (prev != null) {
                checkState(next.getRunnableAt() >= prev.getRunnableAt(), "getRunnableAt " + next + " vs " + prev);
            }
            prev = next;
            next = next.runnableAtNext;
        }
        for (com.android.server.am.BroadcastProcessQueue queue : this.mRunning) {
            if (queue != null) {
                checkState(queue.isActive(), "isActive " + queue);
            }
        }
        if (this.mRunningColdStart != null) {
            checkState(getRunningIndexOf(this.mRunningColdStart) >= 0, "isOrphaned " + this.mRunningColdStart);
        }
        this.mBroadcastQueueModernImplExt.beginAssertHealthLocked();
        for (int i = 0; i < this.mProcessQueues.size(); i++) {
            for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.valueAt(i); leaf != null; leaf = leaf.processNameNext) {
                leaf.assertHealthLocked();
                this.mBroadcastQueueModernImplExt.assertHealthLocked(leaf);
            }
        }
        this.mBroadcastQueueModernImplExt.endAssertHealthLocked(this.mProcessQueues, this.mLocalHandler);
    }

    private void updateWarmProcess(com.android.server.am.BroadcastProcessQueue queue) {
        if (!queue.isProcessWarm()) {
            com.android.server.am.ProcessRecord app = this.mService.getProcessRecordLocked(queue.processName, queue.uid);
            queue.setProcessAndUidState(app, this.mUidForeground.get(queue.uid, false), isProcessFreezable(app));
        }
    }

    private void setQueueProcess(com.android.server.am.BroadcastProcessQueue queue, com.android.server.am.ProcessRecord app) {
        if (queue.setProcessAndUidState(app, this.mUidForeground.get(queue.uid, false), isProcessFreezable(app))) {
            updateRunnableList(queue);
        }
    }

    boolean isProcessFreezable(com.android.server.am.ProcessRecord app) {
        boolean z;
        if (app == null) {
            return false;
        }
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                z = app.mOptRecord.isPendingFreeze() || app.mOptRecord.isFrozen();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        return z;
    }

    private void refreshProcessQueuesLocked(int uid) {
        for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.get(uid); leaf != null; leaf = leaf.processNameNext) {
            setQueueProcess(leaf, leaf.app);
        }
        enqueueUpdateRunningList();
    }

    private void handleProcessFreezableChanged(com.android.server.am.ProcessRecord app) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.BroadcastProcessQueue queue = getProcessQueue(app.processName, app.uid);
                if (queue != null && queue.app != null && queue.app.getPid() == app.getPid()) {
                    if (!isProcessFreezable(app)) {
                        queue.enqueueOutgoingBroadcasts(this.mBroadcastRecordConsumerEnqueue);
                    }
                    refreshProcessQueueLocked(queue);
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    private void refreshProcessQueueLocked(com.android.server.am.BroadcastProcessQueue queue) {
        setQueueProcess(queue, queue.app);
        enqueueUpdateRunningList();
    }

    private void notifyStartedRunning(com.android.server.am.BroadcastProcessQueue queue) {
        if (queue.app != null) {
            queue.app.mReceivers.incrementCurReceivers();
            if (this.mService.mInternal.getRestrictionLevel(queue.uid) < 40) {
                this.mService.updateLruProcessLocked(queue.app, false, null);
            }
            this.mService.mOomAdjuster.unfreezeTemporarily(queue.app, 3);
            if (queue.runningOomAdjusted) {
                queue.app.mState.forceProcessStateUpTo(11);
                this.mService.lambda$appDiedLocked$2(queue.app);
            }
        }
    }

    private void notifyStoppedRunning(com.android.server.am.BroadcastProcessQueue queue) {
        if (queue.app != null) {
            queue.app.mReceivers.decrementCurReceivers();
            if (queue.runningOomAdjusted) {
                this.mService.lambda$appDiedLocked$2(queue.app);
            }
        }
    }

    private void notifyScheduleRegisteredReceiver(com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r, com.android.server.am.BroadcastFilter receiver) {
        reportUsageStatsBroadcastDispatched(app, r);
    }

    private void notifyScheduleReceiver(com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r, android.content.pm.ResolveInfo receiver) {
        reportUsageStatsBroadcastDispatched(app, r);
        java.lang.String receiverPackageName = receiver.activityInfo.packageName;
        app.addPackage(receiverPackageName, receiver.activityInfo.applicationInfo.longVersionCode, this.mService.mProcessStats);
        boolean targetedBroadcast = r.intent.getComponent() != null;
        boolean targetedSelf = java.util.Objects.equals(r.callerPackage, receiverPackageName);
        if (targetedBroadcast && !targetedSelf) {
            this.mService.mUsageStatsService.reportEvent(receiverPackageName, r.userId, 31);
        }
        this.mService.notifyPackageUse(receiverPackageName, 3);
        this.mService.mPackageManagerInt.notifyComponentUsed(receiverPackageName, r.userId, r.callerPackage, r.toString());
    }

    private void reportUsageStatsBroadcastDispatched(com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r) {
        java.lang.String targetPackage;
        long idForResponseEvent = r.options != null ? r.options.getIdForResponseEvent() : 0L;
        if (idForResponseEvent <= 0) {
            return;
        }
        if (r.intent.getPackage() != null) {
            targetPackage = r.intent.getPackage();
        } else if (r.intent.getComponent() != null) {
            targetPackage = r.intent.getComponent().getPackageName();
        } else {
            targetPackage = null;
        }
        if (targetPackage == null) {
            return;
        }
        this.mService.mUsageStatsService.reportBroadcastDispatched(r.callingUid, targetPackage, android.os.UserHandle.of(r.userId), idForResponseEvent, android.os.SystemClock.elapsedRealtime(), this.mService.getUidStateLocked(app.uid));
    }

    private void notifyFinishReceiver(com.android.server.am.BroadcastProcessQueue queue, com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r, int index, java.lang.Object receiver) {
        if (r.wasDeliveryAttempted(index)) {
            logBroadcastDeliveryEventReported(queue, app, r, index, receiver);
        }
        boolean recordFinished = r.terminalCount == r.receivers.size();
        if (recordFinished) {
            notifyFinishBroadcast(r);
        }
    }

    private void logBroadcastDeliveryEventReported(com.android.server.am.BroadcastProcessQueue queue, com.android.server.am.ProcessRecord app, com.android.server.am.BroadcastRecord r, int index, java.lang.Object receiver) {
        int receiverType;
        int type;
        int receiverProcessState;
        int packageState;
        int uid = com.android.server.am.BroadcastRecord.getReceiverUid(receiver);
        int senderUid = r.callingUid == -1 ? 1000 : r.callingUid;
        java.lang.String actionName = r.intent.getAction();
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            receiverType = 1;
        } else {
            receiverType = 2;
        }
        if (queue == null) {
            type = 0;
            receiverProcessState = -1;
        } else if (queue.getActiveViaColdStart()) {
            type = 3;
            receiverProcessState = 20;
        } else {
            type = 1;
            receiverProcessState = queue.lastProcessState;
        }
        long dispatchDelay = r.scheduledTime[index] - r.enqueueTime;
        long finishDelay = r.terminalTime[index] - r.scheduledTime[index];
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES) {
            android.util.Slog.d(com.android.server.am.BroadcastQueue.TAG, "Logging broadcast for " + (app != null ? app.info.packageName : "<null>") + ", stopped=" + queue.getActiveWasStopped() + ", firstLaunch=" + queue.getActiveFirstLaunch());
        }
        if (queue != null) {
            if (queue.getActiveWasStopped()) {
                packageState = 2;
            } else {
                packageState = 1;
            }
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BROADCAST_DELIVERY_EVENT_REPORTED, uid, senderUid, actionName, receiverType, type, dispatchDelay, 0L, finishDelay, packageState, app != null ? app.info.packageName : null, r.callerPackage, r.calculateTypeForLogging(), r.getDeliveryGroupPolicy(), r.intent.getFlags(), com.android.server.am.BroadcastRecord.getReceiverPriority(receiver), r.callerProcState, receiverProcessState, queue.getActiveFirstLaunch(), 0L);
            queue.setActiveFirstLaunch(false);
            queue.setActiveWasStopped(false);
        }
    }

    private void notifyFinishBroadcast(com.android.server.am.BroadcastRecord r) {
        this.mService.notifyBroadcastFinishedLocked(r);
        r.finishTime = android.os.SystemClock.uptimeMillis();
        r.nextReceiver = r.receivers.size();
        this.mHistory.onBroadcastFinishedLocked(r);
        logBootCompletedBroadcastCompletionLatencyIfPossible(r);
        if (r.intent.getComponent() == null && r.intent.getPackage() == null && (r.intent.getFlags() & 1073741824) == 0) {
            int manifestCount = 0;
            int manifestSkipCount = 0;
            for (int i = 0; i < r.receivers.size(); i++) {
                if (r.receivers.get(i) instanceof android.content.pm.ResolveInfo) {
                    manifestCount++;
                    if (r.delivery[i] == 2) {
                        manifestSkipCount++;
                    }
                }
            }
            long dispatchTime = android.os.SystemClock.uptimeMillis() - r.enqueueTime;
            this.mService.addBroadcastStatLocked(r.intent.getAction(), r.callerPackage, manifestCount, manifestSkipCount, dispatchTime);
        }
    }

    com.android.server.am.BroadcastProcessQueue getOrCreateProcessQueue(com.android.server.am.ProcessRecord app) {
        return getOrCreateProcessQueue(app.processName, app.info.uid);
    }

    com.android.server.am.BroadcastProcessQueue getOrCreateProcessQueue(java.lang.String processName, int uid) {
        com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.get(uid);
        while (leaf != null) {
            if (java.util.Objects.equals(leaf.processName, processName)) {
                return leaf;
            }
            if (leaf.processNameNext == null) {
                break;
            }
            leaf = leaf.processNameNext;
        }
        com.android.server.am.BroadcastProcessQueue created = new com.android.server.am.BroadcastProcessQueue(this.mConstants, processName, uid);
        setQueueProcess(created, this.mService.getProcessRecordLocked(processName, uid));
        if (leaf == null) {
            this.mProcessQueues.put(uid, created);
        } else {
            leaf.processNameNext = created;
        }
        this.mBroadcastQueueModernImplExt.hookCreateProcessQueue(created);
        return created;
    }

    com.android.server.am.BroadcastProcessQueue getProcessQueue(com.android.server.am.ProcessRecord app) {
        return getProcessQueue(app.processName, app.info.uid);
    }

    com.android.server.am.BroadcastProcessQueue getProcessQueue(java.lang.String processName, int uid) {
        for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.get(uid); leaf != null; leaf = leaf.processNameNext) {
            if (java.util.Objects.equals(leaf.processName, processName)) {
                return leaf;
            }
        }
        return null;
    }

    com.android.server.am.BroadcastProcessQueue removeProcessQueue(com.android.server.am.ProcessRecord app) {
        return removeProcessQueue(app.processName, app.info.uid);
    }

    com.android.server.am.BroadcastProcessQueue removeProcessQueue(java.lang.String processName, int uid) {
        com.android.server.am.BroadcastProcessQueue prev = null;
        for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.get(uid); leaf != null; leaf = leaf.processNameNext) {
            if (java.util.Objects.equals(leaf.processName, processName)) {
                if (prev != null) {
                    prev.processNameNext = leaf.processNameNext;
                } else if (leaf.processNameNext != null) {
                    this.mProcessQueues.put(uid, leaf.processNameNext);
                } else {
                    this.mProcessQueues.remove(uid);
                }
                return leaf;
            }
            prev = leaf;
        }
        return null;
    }

    private void logBootCompletedBroadcastCompletionLatencyIfPossible(com.android.server.am.BroadcastRecord r) {
        android.content.pm.UserInfo userInfo;
        int userType;
        int numReceivers = r.receivers != null ? r.receivers.size() : 0;
        if (r.nextReceiver < numReceivers) {
            return;
        }
        java.lang.String action = r.intent.getAction();
        int event = 0;
        if ("android.intent.action.LOCKED_BOOT_COMPLETED".equals(action)) {
            event = 1;
        } else if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            event = 2;
        }
        if (event != 0) {
            int dispatchLatency = (int) (r.dispatchTime - r.enqueueTime);
            int completeLatency = (int) (android.os.SystemClock.uptimeMillis() - r.enqueueTime);
            int dispatchRealLatency = (int) (r.dispatchRealTime - r.enqueueRealTime);
            int completeRealLatency = (int) (android.os.SystemClock.elapsedRealtime() - r.enqueueRealTime);
            com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            if (umInternal != null) {
                userInfo = umInternal.getUserInfo(r.userId);
            } else {
                userInfo = null;
            }
            android.content.pm.UserInfo userInfo2 = userInfo;
            if (userInfo2 == null) {
                userType = 0;
            } else {
                int userType2 = com.android.server.pm.UserJourneyLogger.getUserTypeForStatsd(userInfo2.userType);
                userType = userType2;
            }
            android.util.Slog.i(com.android.server.am.BroadcastQueue.TAG, "BOOT_COMPLETED_BROADCAST_COMPLETION_LATENCY_REPORTED action:" + action + " dispatchLatency:" + dispatchLatency + " completeLatency:" + completeLatency + " dispatchRealLatency:" + dispatchRealLatency + " completeRealLatency:" + completeRealLatency + " receiversSize:" + numReceivers + " userId:" + r.userId + " userType:" + (userInfo2 != null ? userInfo2.userType : null));
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BOOT_COMPLETED_BROADCAST_COMPLETION_LATENCY_REPORTED, event, dispatchLatency, completeLatency, dispatchRealLatency, completeRealLatency, r.userId, userType);
        }
    }

    @Override // com.android.server.am.BroadcastQueue
    @dalvik.annotation.optimization.NeverCompile
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.mQueueName);
        this.mHistory.dumpDebug(proto);
        proto.end(token);
    }

    @Override // com.android.server.am.BroadcastQueue
    @dalvik.annotation.optimization.NeverCompile
    public boolean dumpLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpConstants, boolean dumpHistory, boolean dumpAll, java.lang.String dumpPackage, boolean needSep) {
        long now = android.os.SystemClock.uptimeMillis();
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
        ipw.increaseIndent();
        ipw.println();
        ipw.println("📋 Per-process queues:");
        ipw.increaseIndent();
        for (int i = 0; i < this.mProcessQueues.size(); i++) {
            for (com.android.server.am.BroadcastProcessQueue leaf = this.mProcessQueues.valueAt(i); leaf != null; leaf = leaf.processNameNext) {
                leaf.dumpLocked(now, ipw);
            }
        }
        ipw.decreaseIndent();
        ipw.println();
        ipw.println("🧍 Runnable:");
        ipw.increaseIndent();
        if (this.mRunnableHead == null) {
            ipw.println("(none)");
        } else {
            for (com.android.server.am.BroadcastProcessQueue queue = this.mRunnableHead; queue != null; queue = queue.runnableAtNext) {
                android.util.TimeUtils.formatDuration(queue.getRunnableAt(), now, ipw);
                ipw.print(' ');
                ipw.print(com.android.server.am.BroadcastProcessQueue.reasonToString(queue.getRunnableAtReason()));
                ipw.print(' ');
                ipw.print(queue.toShortString());
                ipw.println();
            }
        }
        ipw.decreaseIndent();
        ipw.println();
        ipw.println("🏃 Running:");
        ipw.increaseIndent();
        for (com.android.server.am.BroadcastProcessQueue queue2 : this.mRunning) {
            if (queue2 != null && queue2 == this.mRunningColdStart) {
                ipw.print("🥶 ");
            } else {
                ipw.print("\u3000 ");
            }
            if (queue2 != null) {
                ipw.println(queue2.toShortString());
            } else {
                ipw.println("(none)");
            }
        }
        ipw.decreaseIndent();
        ipw.println();
        ipw.println("Broadcasts with ignored delivery group policies:");
        ipw.increaseIndent();
        this.mService.dumpDeliveryGroupPolicyIgnoredActions(ipw);
        ipw.decreaseIndent();
        ipw.println();
        ipw.println("Foreground UIDs:");
        ipw.increaseIndent();
        ipw.println(this.mUidForeground);
        ipw.decreaseIndent();
        ipw.println();
        if (dumpConstants) {
            this.mConstants.dump(ipw);
        }
        if (!dumpHistory) {
            return needSep;
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return this.mHistory.dumpLocked(ipw, dumpPackage, this.mQueueName, sdf, dumpAll, needSep);
    }

    public com.android.server.am.IBroadcastQueueWrapper getWrapper() {
        return this.mWrapper;
    }

    private class BroadcastQueueWrapper implements com.android.server.am.IBroadcastQueueWrapper {
        private BroadcastQueueWrapper() {
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public com.android.server.am.IBroadcastQueueModernImplExt getModernExtImpl() {
            return com.android.server.am.BroadcastQueueModernImpl.this.mBroadcastQueueModernImplExt;
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public android.util.SparseArray<com.android.server.am.BroadcastProcessQueue> getProcessQueues() {
            return com.android.server.am.BroadcastQueueModernImpl.this.mProcessQueues;
        }

        @Override // com.android.server.am.IBroadcastQueueWrapper
        public void enqueueBroadcastLocked(java.util.ArrayList<com.android.server.am.BroadcastRecord> pendingBroadcasts, boolean wouldBeSkipped, boolean addFirst) {
            java.util.HashSet<com.android.server.am.BroadcastProcessQueue> enqueuedBroadcastQueue = new java.util.HashSet<>();
            for (com.android.server.am.BroadcastRecord r : pendingBroadcasts) {
                int cookie = com.android.server.am.BroadcastQueue.traceBegin("enqueueBroadcast");
                r.enqueueTime = android.os.SystemClock.uptimeMillis();
                r.enqueueRealTime = android.os.SystemClock.elapsedRealtime();
                r.enqueueClockTime = java.lang.System.currentTimeMillis();
                for (int i = 0; i < r.receivers.size(); i++) {
                    java.lang.Object receiver = r.receivers.get(i);
                    com.android.server.am.BroadcastProcessQueue queue = com.android.server.am.BroadcastQueueModernImpl.this.getOrCreateProcessQueue(com.android.server.am.BroadcastRecord.getReceiverProcessName(receiver), com.android.server.am.BroadcastRecord.getReceiverUid(receiver));
                    queue.getWrapper().enqueueBroadcast(r, i, addFirst, com.android.server.am.BroadcastQueueModernImpl.this.mBroadcastConsumerDeferApply);
                    if (r.isDeferUntilActive() && queue.isDeferredUntilActive()) {
                        com.android.server.am.BroadcastQueueModernImpl.this.setDeliveryState(queue, null, r, i, receiver, 6, "deferred at enqueue time");
                    }
                    enqueuedBroadcastQueue.add(queue);
                }
                com.android.server.am.BroadcastQueue.traceEnd(cookie);
            }
            java.util.Iterator<com.android.server.am.BroadcastProcessQueue> it = enqueuedBroadcastQueue.iterator();
            while (it.hasNext()) {
                com.android.server.am.BroadcastQueueModernImpl.this.updateRunnableList(it.next());
                com.android.server.am.BroadcastQueueModernImpl.this.enqueueUpdateRunningList();
            }
        }
    }
}
