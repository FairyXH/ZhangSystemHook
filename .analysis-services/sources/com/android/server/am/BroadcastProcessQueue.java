package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class BroadcastProcessQueue {
    static final int REASON_BLOCKED = 4;
    static final int REASON_CACHED = 1;
    static final int REASON_CACHED_INFINITE_DEFER = 8;
    static final int REASON_CONTAINS_ALARM = 12;
    static final int REASON_CONTAINS_FOREGROUND = 10;
    static final int REASON_CONTAINS_INSTRUMENTED = 16;
    static final int REASON_CONTAINS_INTERACTIVE = 14;
    static final int REASON_CONTAINS_MANIFEST = 17;
    static final int REASON_CONTAINS_ORDERED = 11;
    static final int REASON_CONTAINS_PRIORITIZED = 13;
    static final int REASON_CONTAINS_RESULT_TO = 15;
    static final int REASON_CORE_UID = 19;
    static final int REASON_EMPTY = 0;
    static final int REASON_FORCE_DELAYED = 7;
    static final int REASON_FOREGROUND = 18;
    static final int REASON_INSTRUMENTED = 5;
    static final int REASON_MAX_PENDING = 3;
    static final int REASON_NORMAL = 2;
    static final int REASON_PERSISTENT = 6;
    static final int REASON_TOP_PROCESS = 20;
    static final boolean VERBOSE = false;
    public static com.android.server.am.IBroadcastProcessQueueExt.IStaticExt mStaticExt = (com.android.server.am.IBroadcastProcessQueueExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBroadcastProcessQueueExt.IStaticExt.class).create();
    com.android.server.am.ProcessRecord app;
    final com.android.server.am.BroadcastConstants constants;
    long lastCpuDelayTime;
    int lastProcessState;
    private com.android.server.am.BroadcastRecord mActive;
    private int mActiveAssumedDeliveryCountSinceIdle;
    private int mActiveCountConsecutiveNormal;
    private int mActiveCountConsecutiveUrgent;
    private int mActiveCountSinceIdle;
    private boolean mActiveFirstLaunch;
    private int mActiveIndex;
    private boolean mActiveReEnqueued;
    private boolean mActiveViaColdStart;
    private boolean mActiveWasStopped;
    private java.lang.String mCachedToShortString;
    private java.lang.String mCachedToString;
    private int mCountAlarm;
    private int mCountDeferred;
    private int mCountEnqueued;
    private int mCountForeground;
    private int mCountForegroundDeferred;
    private int mCountInstrumented;
    private int mCountInteractive;
    private int mCountManifest;
    private int mCountOrdered;
    private int mCountPrioritizeEarliestRequests;
    private int mCountPrioritized;
    private int mCountPrioritizedDeferred;
    private int mCountResultTo;
    private long mForcedDelayedDurationMs;
    private boolean mLastDeferredStates;
    private boolean mProcessFreezable;
    private boolean mProcessInstrumented;
    private boolean mProcessPersistent;
    private boolean mRunnableAtInvalidated;
    private boolean mTimeoutScheduled;
    private boolean mUidForeground;
    final java.lang.String processName;
    com.android.server.am.BroadcastProcessQueue processNameNext;
    com.android.server.am.BroadcastProcessQueue runnableAtNext;
    com.android.server.am.BroadcastProcessQueue runnableAtPrev;
    boolean runningOomAdjusted;
    java.lang.String runningTraceTrackName;
    final int uid;
    private final java.util.ArrayDeque<com.android.internal.os.SomeArgs> mPending = new java.util.ArrayDeque<>();
    private final java.util.ArrayDeque<com.android.internal.os.SomeArgs> mPendingUrgent = new java.util.ArrayDeque<>(4);
    private final java.util.ArrayDeque<com.android.internal.os.SomeArgs> mPendingOffload = new java.util.ArrayDeque<>(4);
    private long mRunnableAt = Long.MAX_VALUE;
    private int mRunnableAtReason = 0;
    private final java.util.ArrayList<com.android.server.am.BroadcastRecord> mOutgoingBroadcasts = new java.util.ArrayList<>();
    private com.android.server.am.IBroadcastProcessQueueExt mBroadcastProcessQueueExt = (com.android.server.am.IBroadcastProcessQueueExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBroadcastProcessQueueExt.class).base(this).create();
    private final com.android.server.am.IBroadcastProcessQueueWrapper mWrapper = new com.android.server.am.BroadcastProcessQueue.BroadcastProcessQueueWrapper();

    @java.lang.FunctionalInterface
    public interface BroadcastConsumer {
        void accept(com.android.server.am.BroadcastRecord broadcastRecord, int i);
    }

    @java.lang.FunctionalInterface
    public interface BroadcastPredicate {
        boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i);
    }

    @java.lang.FunctionalInterface
    public interface BroadcastRecordConsumer {
        void accept(com.android.server.am.BroadcastRecord broadcastRecord);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Reason {
    }

    public BroadcastProcessQueue(com.android.server.am.BroadcastConstants constants, java.lang.String processName, int uid) {
        this.constants = (com.android.server.am.BroadcastConstants) java.util.Objects.requireNonNull(constants);
        this.processName = (java.lang.String) java.util.Objects.requireNonNull(processName);
        this.uid = uid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.ArrayDeque<com.android.internal.os.SomeArgs> getQueueForBroadcast(com.android.server.am.BroadcastRecord record) {
        if (record.isUrgent()) {
            return this.mPendingUrgent;
        }
        if (record.isOffload()) {
            return this.mPendingOffload;
        }
        return this.mPending;
    }

    public void enqueueOutgoingBroadcast(com.android.server.am.BroadcastRecord record) {
        this.mOutgoingBroadcasts.add(record);
    }

    public int getOutgoingBroadcastCount() {
        return this.mOutgoingBroadcasts.size();
    }

    public void enqueueOutgoingBroadcasts(com.android.server.am.BroadcastProcessQueue.BroadcastRecordConsumer consumer) {
        for (int i = 0; i < this.mOutgoingBroadcasts.size(); i++) {
            consumer.accept(this.mOutgoingBroadcasts.get(i));
        }
        this.mOutgoingBroadcasts.clear();
    }

    public void clearOutgoingBroadcasts() {
        this.mOutgoingBroadcasts.clear();
    }

    public com.android.server.am.BroadcastRecord enqueueOrReplaceBroadcast(com.android.server.am.BroadcastRecord record, int recordIndex, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer deferredStatesApplyConsumer) {
        com.android.server.am.BroadcastRecord replacedBroadcastRecord;
        if (record.isReplacePending() && record.getDeliveryGroupPolicy() == 0 && (replacedBroadcastRecord = replaceBroadcast(record, recordIndex)) != null) {
            if (this.mLastDeferredStates && shouldBeDeferred() && record.getDeliveryState(recordIndex) == 0) {
                deferredStatesApplyConsumer.accept(record, recordIndex);
            }
            return replacedBroadcastRecord;
        }
        com.android.internal.os.SomeArgs newBroadcastArgs = com.android.internal.os.SomeArgs.obtain();
        newBroadcastArgs.arg1 = record;
        newBroadcastArgs.argi1 = recordIndex;
        getQueueForBroadcast(record).addLast(newBroadcastArgs);
        onBroadcastEnqueued(record, recordIndex);
        if (this.mLastDeferredStates && shouldBeDeferred() && record.getDeliveryState(recordIndex) == 0) {
            deferredStatesApplyConsumer.accept(record, recordIndex);
            return null;
        }
        return null;
    }

    public void reEnqueueActiveBroadcast() {
        if (!isActive()) {
            return;
        }
        com.android.server.am.BroadcastRecord record = getActive();
        int recordIndex = getActiveIndex();
        com.android.internal.os.SomeArgs broadcastArgs = com.android.internal.os.SomeArgs.obtain();
        broadcastArgs.arg1 = record;
        broadcastArgs.argi1 = recordIndex;
        broadcastArgs.argi2 = 1;
        getQueueForBroadcast(record).addFirst(broadcastArgs);
        onBroadcastEnqueued(record, recordIndex);
    }

    private com.android.server.am.BroadcastRecord replaceBroadcast(com.android.server.am.BroadcastRecord record, int recordIndex) {
        java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue = getQueueForBroadcast(record);
        return replaceBroadcastInQueue(queue, record, recordIndex);
    }

    private com.android.server.am.BroadcastRecord replaceBroadcastInQueue(java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue, com.android.server.am.BroadcastRecord record, int recordIndex) {
        java.util.Iterator<com.android.internal.os.SomeArgs> it = queue.descendingIterator();
        java.lang.Object receiver = record.receivers.get(recordIndex);
        while (it.hasNext()) {
            com.android.internal.os.SomeArgs args = it.next();
            com.android.server.am.BroadcastRecord testRecord = (com.android.server.am.BroadcastRecord) args.arg1;
            int testRecordIndex = args.argi1;
            java.lang.Object testReceiver = testRecord.receivers.get(testRecordIndex);
            if (record != testRecord) {
                if (record.callingUid == testRecord.callingUid && record.userId == testRecord.userId && record.intent.filterEquals(testRecord.intent) && com.android.server.am.BroadcastRecord.isReceiverEquals(receiver, testReceiver) && record.initialSticky == testRecord.initialSticky && testRecord.allReceiversPending() && record.isMatchingRecord(testRecord)) {
                    args.arg1 = record;
                    args.argi1 = recordIndex;
                    record.copyEnqueueTimeFrom(testRecord);
                    onBroadcastDequeued(testRecord, testRecordIndex);
                    onBroadcastEnqueued(record, recordIndex);
                    return testRecord;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public boolean forEachMatchingBroadcast(com.android.server.am.BroadcastProcessQueue.BroadcastPredicate predicate, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer consumer, boolean andRemove) {
        boolean didSomething = false | forEachMatchingBroadcastInQueue(this.mPending, predicate, consumer, andRemove);
        return didSomething | forEachMatchingBroadcastInQueue(this.mPendingUrgent, predicate, consumer, andRemove) | forEachMatchingBroadcastInQueue(this.mPendingOffload, predicate, consumer, andRemove);
    }

    private boolean forEachMatchingBroadcastInQueue(java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue, com.android.server.am.BroadcastProcessQueue.BroadcastPredicate predicate, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer consumer, boolean andRemove) {
        boolean didSomething = false;
        java.util.Iterator<com.android.internal.os.SomeArgs> it = queue.iterator();
        while (it.hasNext()) {
            com.android.internal.os.SomeArgs args = it.next();
            com.android.server.am.BroadcastRecord record = (com.android.server.am.BroadcastRecord) args.arg1;
            int recordIndex = args.argi1;
            if (predicate.test(record, recordIndex)) {
                consumer.accept(record, recordIndex);
                if (andRemove) {
                    args.recycle();
                    it.remove();
                    onBroadcastDequeued(record, recordIndex);
                } else {
                    invalidateRunnableAt();
                }
                didSomething = true;
            }
        }
        return didSomething;
    }

    public boolean setProcessAndUidState(com.android.server.am.ProcessRecord app, boolean uidForeground, boolean processFreezable) {
        this.app = app;
        this.mCachedToString = null;
        this.mCachedToShortString = null;
        if (app != null) {
            boolean didSomething = false | setUidForeground(uidForeground);
            return didSomething | setProcessFreezable(processFreezable) | setProcessInstrumented(app.getActiveInstrumentation() != null) | setProcessPersistent(app.isPersistent());
        }
        boolean didSomething2 = false | setUidForeground(false);
        return didSomething2 | setProcessFreezable(false) | setProcessInstrumented(false) | setProcessPersistent(false);
    }

    private boolean setUidForeground(boolean uidForeground) {
        if (this.mUidForeground != uidForeground) {
            this.mUidForeground = uidForeground;
            invalidateRunnableAt();
            return true;
        }
        return false;
    }

    private boolean setProcessFreezable(boolean freezable) {
        if (this.mProcessFreezable != freezable) {
            this.mProcessFreezable = freezable;
            invalidateRunnableAt();
            return true;
        }
        return false;
    }

    private boolean setProcessInstrumented(boolean instrumented) {
        if (this.mProcessInstrumented != instrumented) {
            this.mProcessInstrumented = instrumented;
            invalidateRunnableAt();
            return true;
        }
        return false;
    }

    private boolean setProcessPersistent(boolean persistent) {
        if (this.mProcessPersistent != persistent) {
            this.mProcessPersistent = persistent;
            invalidateRunnableAt();
            return true;
        }
        return false;
    }

    public boolean isProcessWarm() {
        return (this.app == null || this.app.getOnewayThread() == null || this.app.isKilled()) ? false : true;
    }

    public int getPreferredSchedulingGroupLocked() {
        if (!isActive()) {
            return Integer.MIN_VALUE;
        }
        if (this.mCountForeground > this.mCountForegroundDeferred) {
            return 2;
        }
        return (this.mActive == null || !this.mActive.isForeground()) ? 0 : 2;
    }

    public int getActiveCountSinceIdle() {
        return this.mActiveCountSinceIdle;
    }

    public int getActiveAssumedDeliveryCountSinceIdle() {
        return this.mActiveAssumedDeliveryCountSinceIdle;
    }

    public void setActiveViaColdStart(boolean activeViaColdStart) {
        this.mActiveViaColdStart = activeViaColdStart;
    }

    public void setActiveWasStopped(boolean activeWasStopped) {
        this.mActiveWasStopped = activeWasStopped;
    }

    public void setActiveFirstLaunch(boolean activeFirstLaunch) {
        this.mActiveFirstLaunch = activeFirstLaunch;
    }

    public boolean getActiveViaColdStart() {
        return this.mActiveViaColdStart;
    }

    public boolean getActiveWasStopped() {
        return this.mActiveWasStopped;
    }

    public boolean getActiveFirstLaunch() {
        return this.mActiveFirstLaunch;
    }

    public java.lang.String getPackageName() {
        if (this.app == null) {
            return null;
        }
        return this.app.getApplicationInfo().packageName;
    }

    public void makeActiveNextPending() {
        com.android.internal.os.SomeArgs someArgsRemoveNextBroadcast = removeNextBroadcast();
        this.mActive = (com.android.server.am.BroadcastRecord) someArgsRemoveNextBroadcast.arg1;
        this.mActiveIndex = someArgsRemoveNextBroadcast.argi1;
        this.mActiveReEnqueued = someArgsRemoveNextBroadcast.argi2 == 1;
        this.mActiveCountSinceIdle++;
        this.mActiveAssumedDeliveryCountSinceIdle += this.mActive.isAssumedDelivered(this.mActiveIndex) ? 1 : 0;
        this.mActiveViaColdStart = false;
        this.mActiveWasStopped = false;
        someArgsRemoveNextBroadcast.recycle();
        onBroadcastDequeued(this.mActive, this.mActiveIndex);
    }

    public void makeActiveIdle() {
        this.mActive = null;
        this.mActiveIndex = 0;
        this.mActiveReEnqueued = false;
        this.mActiveCountSinceIdle = 0;
        this.mActiveAssumedDeliveryCountSinceIdle = 0;
        this.mActiveViaColdStart = false;
        invalidateRunnableAt();
    }

    public boolean wasActiveBroadcastReEnqueued() {
        if (!com.android.server.am.Flags.avoidRepeatedBcastReEnqueues()) {
            return false;
        }
        return this.mActiveReEnqueued;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBroadcastEnqueued(com.android.server.am.BroadcastRecord record, int recordIndex) {
        this.mCountEnqueued++;
        if (record.deferUntilActive) {
            this.mCountDeferred++;
        }
        if (record.isForeground()) {
            if (record.deferUntilActive) {
                this.mCountForegroundDeferred++;
            }
            this.mCountForeground++;
        }
        if (record.ordered) {
            this.mCountOrdered++;
        }
        if (record.alarm) {
            this.mCountAlarm++;
        }
        if (record.prioritized) {
            if (record.deferUntilActive) {
                this.mCountPrioritizedDeferred++;
            }
            this.mCountPrioritized++;
        }
        if (record.interactive) {
            this.mCountInteractive++;
        }
        if (record.resultTo != null) {
            this.mCountResultTo++;
        }
        if (record.callerInstrumented) {
            this.mCountInstrumented++;
        }
        if (record.receivers.get(recordIndex) instanceof android.content.pm.ResolveInfo) {
            this.mCountManifest++;
        }
        invalidateRunnableAt();
    }

    private void onBroadcastDequeued(com.android.server.am.BroadcastRecord record, int recordIndex) {
        this.mCountEnqueued--;
        if (record.deferUntilActive) {
            this.mCountDeferred--;
        }
        if (record.isForeground()) {
            if (record.deferUntilActive) {
                this.mCountForegroundDeferred--;
            }
            this.mCountForeground--;
        }
        if (record.ordered) {
            this.mCountOrdered--;
        }
        if (record.alarm) {
            this.mCountAlarm--;
        }
        if (record.prioritized) {
            if (record.deferUntilActive) {
                this.mCountPrioritizedDeferred--;
            }
            this.mCountPrioritized--;
        }
        if (record.interactive) {
            this.mCountInteractive--;
        }
        if (record.resultTo != null) {
            this.mCountResultTo--;
        }
        if (record.callerInstrumented) {
            this.mCountInstrumented--;
        }
        if (record.receivers.get(recordIndex) instanceof android.content.pm.ResolveInfo) {
            this.mCountManifest--;
        }
        invalidateRunnableAt();
    }

    public void traceProcessStartingBegin() {
        android.os.Trace.asyncTraceForTrackBegin(64L, this.runningTraceTrackName, toShortString() + " starting", hashCode());
    }

    public void traceProcessRunningBegin() {
        android.os.Trace.asyncTraceForTrackBegin(64L, this.runningTraceTrackName, toShortString() + " running", hashCode());
    }

    public void traceProcessEnd() {
        android.os.Trace.asyncTraceForTrackEnd(64L, this.runningTraceTrackName, hashCode());
    }

    public void traceActiveBegin() {
        android.os.Trace.asyncTraceForTrackBegin(64L, this.runningTraceTrackName, this.mActive.toShortString() + " scheduled", hashCode());
    }

    public void traceActiveEnd() {
        android.os.Trace.asyncTraceForTrackEnd(64L, this.runningTraceTrackName, hashCode());
    }

    public com.android.server.am.BroadcastRecord getActive() {
        return (com.android.server.am.BroadcastRecord) java.util.Objects.requireNonNull(this.mActive);
    }

    public int getActiveIndex() {
        java.util.Objects.requireNonNull(this.mActive);
        return this.mActiveIndex;
    }

    public boolean isOutgoingEmpty() {
        return this.mOutgoingBroadcasts.isEmpty();
    }

    public boolean isEmpty() {
        return this.mPending.isEmpty() && this.mPendingUrgent.isEmpty() && this.mPendingOffload.isEmpty();
    }

    public boolean isActive() {
        return this.mActive != null;
    }

    boolean forceDelayBroadcastDelivery(long delayedDurationMs) {
        if (this.mForcedDelayedDurationMs != delayedDurationMs) {
            this.mForcedDelayedDurationMs = delayedDurationMs;
            invalidateRunnableAt();
            return true;
        }
        return false;
    }

    private com.android.internal.os.SomeArgs removeNextBroadcast() {
        java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue = queueForNextBroadcast();
        if (queue == this.mPendingUrgent) {
            this.mActiveCountConsecutiveUrgent++;
        } else if (queue == this.mPending) {
            this.mActiveCountConsecutiveUrgent = 0;
            this.mActiveCountConsecutiveNormal++;
        } else if (queue == this.mPendingOffload) {
            this.mActiveCountConsecutiveUrgent = 0;
            this.mActiveCountConsecutiveNormal = 0;
        }
        if (isQueueEmpty(queue)) {
            return null;
        }
        return queue.removeFirst();
    }

    java.util.ArrayDeque<com.android.internal.os.SomeArgs> queueForNextBroadcast() {
        java.util.ArrayDeque<com.android.internal.os.SomeArgs> nextNormal = queueForNextBroadcast(this.mPending, this.mPendingOffload, this.mActiveCountConsecutiveNormal, this.constants.MAX_CONSECUTIVE_NORMAL_DISPATCHES);
        java.util.ArrayDeque<com.android.internal.os.SomeArgs> nextBroadcastQueue = queueForNextBroadcast(this.mPendingUrgent, nextNormal, this.mActiveCountConsecutiveUrgent, this.constants.MAX_CONSECUTIVE_URGENT_DISPATCHES);
        return nextBroadcastQueue;
    }

    private java.util.ArrayDeque<com.android.internal.os.SomeArgs> queueForNextBroadcast(java.util.ArrayDeque<com.android.internal.os.SomeArgs> highPriorityQueue, java.util.ArrayDeque<com.android.internal.os.SomeArgs> lowPriorityQueue, int consecutiveHighPriorityCount, int maxHighPriorityDispatchLimit) {
        if (isQueueEmpty(highPriorityQueue)) {
            return lowPriorityQueue;
        }
        if (isQueueEmpty(lowPriorityQueue)) {
            return highPriorityQueue;
        }
        com.android.internal.os.SomeArgs nextLPArgs = lowPriorityQueue.peekFirst();
        com.android.server.am.BroadcastRecord nextLPRecord = (com.android.server.am.BroadcastRecord) nextLPArgs.arg1;
        int nextLPRecordIndex = nextLPArgs.argi1;
        com.android.server.am.BroadcastRecord nextHPRecord = (com.android.server.am.BroadcastRecord) highPriorityQueue.peekFirst().arg1;
        boolean isLPQueueEligible = false;
        boolean shouldConsiderLPQueue = this.mCountPrioritizeEarliestRequests > 0 || consecutiveHighPriorityCount >= maxHighPriorityDispatchLimit;
        if (shouldConsiderLPQueue && nextLPRecord.enqueueTime <= nextHPRecord.enqueueTime && !nextLPRecord.isBlocked(nextLPRecordIndex)) {
            isLPQueueEligible = true;
        }
        return isLPQueueEligible ? lowPriorityQueue : highPriorityQueue;
    }

    private static boolean isQueueEmpty(java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue) {
        return queue == null || queue.isEmpty();
    }

    boolean addPrioritizeEarliestRequest() {
        if (this.mCountPrioritizeEarliestRequests == 0) {
            this.mCountPrioritizeEarliestRequests++;
            invalidateRunnableAt();
            return true;
        }
        this.mCountPrioritizeEarliestRequests++;
        return false;
    }

    boolean removePrioritizeEarliestRequest() {
        this.mCountPrioritizeEarliestRequests--;
        if (this.mCountPrioritizeEarliestRequests == 0) {
            invalidateRunnableAt();
            return true;
        }
        if (this.mCountPrioritizeEarliestRequests >= 0) {
            return false;
        }
        this.mCountPrioritizeEarliestRequests = 0;
        return false;
    }

    com.android.internal.os.SomeArgs peekNextBroadcast() {
        java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue = queueForNextBroadcast();
        if (isQueueEmpty(queue)) {
            return null;
        }
        return queue.peekFirst();
    }

    com.android.server.am.BroadcastRecord peekNextBroadcastRecord() {
        java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue = queueForNextBroadcast();
        if (isQueueEmpty(queue)) {
            return null;
        }
        return (com.android.server.am.BroadcastRecord) queue.peekFirst().arg1;
    }

    public boolean isPendingManifest() {
        return this.mCountManifest > 0;
    }

    public boolean isPendingOrdered() {
        return this.mCountOrdered > 0;
    }

    public boolean isPendingResultTo() {
        return this.mCountResultTo > 0;
    }

    public boolean isPendingUrgent() {
        com.android.server.am.BroadcastRecord next = peekNextBroadcastRecord();
        if (next != null) {
            return next.isUrgent();
        }
        return false;
    }

    public boolean isIdle() {
        return (!isActive() && isEmpty()) || isDeferredUntilActive();
    }

    public boolean isBeyondBarrierLocked(long barrierTime) {
        com.android.internal.os.SomeArgs next = this.mPending.peekFirst();
        com.android.internal.os.SomeArgs nextUrgent = this.mPendingUrgent.peekFirst();
        com.android.internal.os.SomeArgs nextOffload = this.mPendingOffload.peekFirst();
        boolean activeBeyond = this.mActive == null || this.mActive.enqueueTime > barrierTime;
        boolean nextBeyond = next == null || ((com.android.server.am.BroadcastRecord) next.arg1).enqueueTime > barrierTime;
        boolean nextUrgentBeyond = nextUrgent == null || ((com.android.server.am.BroadcastRecord) nextUrgent.arg1).enqueueTime > barrierTime;
        boolean nextOffloadBeyond = nextOffload == null || ((com.android.server.am.BroadcastRecord) nextOffload.arg1).enqueueTime > barrierTime;
        return (activeBeyond && nextBeyond && nextUrgentBeyond && nextOffloadBeyond) || isDeferredUntilActive();
    }

    public boolean isDispatched(android.content.Intent intent) {
        boolean activeDispatched = this.mActive == null || !intent.filterEquals(this.mActive.intent);
        boolean dispatched = isDispatchedInQueue(this.mPending, intent);
        boolean urgentDispatched = isDispatchedInQueue(this.mPendingUrgent, intent);
        boolean offloadDispatched = isDispatchedInQueue(this.mPendingOffload, intent);
        return (activeDispatched && dispatched && urgentDispatched && offloadDispatched) || isDeferredUntilActive();
    }

    private boolean isDispatchedInQueue(java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue, android.content.Intent intent) {
        com.android.internal.os.SomeArgs args;
        java.util.Iterator<com.android.internal.os.SomeArgs> it = queue.iterator();
        while (it.hasNext() && (args = it.next()) != null) {
            com.android.server.am.BroadcastRecord record = (com.android.server.am.BroadcastRecord) args.arg1;
            if (intent.filterEquals(record.intent)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRunnable() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAt != Long.MAX_VALUE;
    }

    public boolean isDeferredUntilActive() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAtReason == 8;
    }

    public boolean hasDeferredBroadcasts() {
        return this.mCountDeferred > 0;
    }

    public long getRunnableAt() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAt;
    }

    public int getRunnableAtReason() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAtReason;
    }

    public void invalidateRunnableAt() {
        this.mRunnableAtInvalidated = true;
    }

    static java.lang.String reasonToString(int reason) {
        java.lang.String reasonExtend = mStaticExt.reasonToStringExtend(reason);
        if (reasonExtend != null) {
            return reasonExtend;
        }
        switch (reason) {
            case 0:
                return "EMPTY";
            case 1:
                return "CACHED";
            case 2:
                return com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL;
            case 3:
                return "MAX_PENDING";
            case 4:
                return "BLOCKED";
            case 5:
                return "INSTRUMENTED";
            case 6:
                return "PERSISTENT";
            case 7:
                return "FORCE_DELAYED";
            case 8:
                return "INFINITE_DEFER";
            case 9:
            default:
                return java.lang.Integer.toString(reason);
            case 10:
                return "CONTAINS_FOREGROUND";
            case 11:
                return "CONTAINS_ORDERED";
            case 12:
                return "CONTAINS_ALARM";
            case 13:
                return "CONTAINS_PRIORITIZED";
            case 14:
                return "CONTAINS_INTERACTIVE";
            case 15:
                return "CONTAINS_RESULT_TO";
            case 16:
                return "CONTAINS_INSTRUMENTED";
            case 17:
                return "CONTAINS_MANIFEST";
            case 18:
                return "FOREGROUND";
            case 19:
                return "CORE_UID";
            case 20:
                return "TOP_PROCESS";
        }
    }

    void updateRunnableAt() {
        if (this.mRunnableAtInvalidated) {
            this.mRunnableAtInvalidated = false;
            int oldRunnableAtReason = this.mRunnableAtReason;
            com.android.internal.os.SomeArgs next = peekNextBroadcast();
            if (next != null) {
                com.android.server.am.BroadcastRecord r = (com.android.server.am.BroadcastRecord) next.arg1;
                int index = next.argi1;
                long runnableAt = r.enqueueTime;
                if (r.isBlocked(index)) {
                    this.mRunnableAt = Long.MAX_VALUE;
                    this.mRunnableAtReason = 4;
                    return;
                }
                if (this.mForcedDelayedDurationMs > 0) {
                    this.mRunnableAt = this.mForcedDelayedDurationMs + runnableAt;
                    this.mRunnableAtReason = 7;
                } else if (this.mCountForeground > this.mCountForegroundDeferred) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + runnableAt;
                    this.mRunnableAtReason = 10;
                } else if (this.mCountInteractive > 0) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + runnableAt;
                    this.mRunnableAtReason = 14;
                } else if (this.mCountInstrumented > 0) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + runnableAt;
                    this.mRunnableAtReason = 16;
                } else if (this.mProcessInstrumented) {
                    this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + runnableAt;
                    this.mRunnableAtReason = 5;
                } else if (this.mUidForeground) {
                    this.mRunnableAt = this.constants.DELAY_FOREGROUND_PROC_MILLIS + runnableAt;
                    this.mRunnableAtReason = 18;
                } else if (this.app != null && this.app.getSetProcState() == 2) {
                    this.mRunnableAt = this.constants.DELAY_FOREGROUND_PROC_MILLIS + runnableAt;
                    this.mRunnableAtReason = 20;
                } else if (this.mProcessPersistent) {
                    this.mRunnableAt = this.constants.DELAY_PERSISTENT_PROC_MILLIS + runnableAt;
                    this.mRunnableAtReason = 6;
                } else if (this.mCountOrdered > 0) {
                    this.mRunnableAt = runnableAt;
                    this.mRunnableAtReason = 11;
                } else if (this.mCountAlarm > 0) {
                    this.mRunnableAt = runnableAt;
                    this.mRunnableAtReason = 12;
                } else if (this.mCountPrioritized > this.mCountPrioritizedDeferred) {
                    this.mRunnableAt = runnableAt;
                    this.mRunnableAtReason = 13;
                } else if (this.mCountManifest > 0) {
                    this.mRunnableAt = runnableAt;
                    this.mRunnableAtReason = 17;
                } else if (this.mProcessFreezable) {
                    if (r.deferUntilActive) {
                        if (this.mCountDeferred == this.mCountEnqueued) {
                            this.mRunnableAt = Long.MAX_VALUE;
                            this.mRunnableAtReason = 8;
                        } else if (r.isForeground()) {
                            this.mRunnableAt = this.constants.DELAY_URGENT_MILLIS + runnableAt;
                            this.mRunnableAtReason = 10;
                        } else if (r.prioritized) {
                            this.mRunnableAt = runnableAt;
                            this.mRunnableAtReason = 13;
                        } else if (r.resultTo != null) {
                            this.mRunnableAt = runnableAt;
                            this.mRunnableAtReason = 15;
                        } else {
                            this.mRunnableAt = this.constants.DELAY_CACHED_MILLIS + runnableAt;
                            this.mRunnableAtReason = 1;
                        }
                    } else {
                        this.mRunnableAt = this.constants.DELAY_CACHED_MILLIS + runnableAt;
                        this.mRunnableAtReason = 1;
                    }
                } else if (this.mCountResultTo > 0) {
                    this.mRunnableAt = runnableAt;
                    this.mRunnableAtReason = 15;
                } else if (android.os.UserHandle.isCore(this.uid)) {
                    this.mRunnableAt = runnableAt;
                    this.mRunnableAtReason = 19;
                } else {
                    this.mRunnableAt = this.constants.DELAY_NORMAL_MILLIS + runnableAt;
                    this.mRunnableAtReason = 2;
                }
                long customRunnableAt = this.mBroadcastProcessQueueExt.getCustomizedRunnableAt(runnableAt);
                if (this.mRunnableAt > customRunnableAt) {
                    this.mRunnableAt = customRunnableAt;
                    this.mRunnableAtReason = 101;
                }
                if (this.mPending.size() + this.mPendingUrgent.size() + this.mPendingOffload.size() >= this.constants.MAX_PENDING_BROADCASTS) {
                    this.mRunnableAt = java.lang.Math.min(this.mRunnableAt, runnableAt);
                    this.mRunnableAtReason = 3;
                }
            } else {
                this.mRunnableAt = Long.MAX_VALUE;
                this.mRunnableAtReason = 0;
            }
            this.mBroadcastProcessQueueExt.updateRunnableAtEnd(oldRunnableAtReason, this.mRunnableAtReason);
        }
    }

    void updateDeferredStates(com.android.server.am.BroadcastProcessQueue.BroadcastConsumer applyConsumer, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer clearConsumer) {
        boolean wantDeferredStates = shouldBeDeferred();
        if (this.mLastDeferredStates != wantDeferredStates) {
            this.mLastDeferredStates = wantDeferredStates;
            if (wantDeferredStates) {
                forEachMatchingBroadcast(new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastProcessQueue$$ExternalSyntheticLambda1
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                        return com.android.server.am.BroadcastProcessQueue.lambda$updateDeferredStates$0(broadcastRecord, i);
                    }
                }, applyConsumer, false);
            } else {
                forEachMatchingBroadcast(new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastProcessQueue$$ExternalSyntheticLambda2
                    @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                    public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                        return com.android.server.am.BroadcastProcessQueue.lambda$updateDeferredStates$1(broadcastRecord, i);
                    }
                }, clearConsumer, false);
            }
        }
    }

    static /* synthetic */ boolean lambda$updateDeferredStates$0(com.android.server.am.BroadcastRecord r, int i) {
        return r.getDeliveryState(i) == 0;
    }

    static /* synthetic */ boolean lambda$updateDeferredStates$1(com.android.server.am.BroadcastRecord r, int i) {
        return r.getDeliveryState(i) == 6;
    }

    void clearDeferredStates(com.android.server.am.BroadcastProcessQueue.BroadcastConsumer clearConsumer) {
        if (this.mLastDeferredStates) {
            this.mLastDeferredStates = false;
            forEachMatchingBroadcast(new com.android.server.am.BroadcastProcessQueue.BroadcastPredicate() { // from class: com.android.server.am.BroadcastProcessQueue$$ExternalSyntheticLambda0
                @Override // com.android.server.am.BroadcastProcessQueue.BroadcastPredicate
                public final boolean test(com.android.server.am.BroadcastRecord broadcastRecord, int i) {
                    return com.android.server.am.BroadcastProcessQueue.lambda$clearDeferredStates$2(broadcastRecord, i);
                }
            }, clearConsumer, false);
        }
    }

    static /* synthetic */ boolean lambda$clearDeferredStates$2(com.android.server.am.BroadcastRecord r, int i) {
        return r.getDeliveryState(i) == 6;
    }

    boolean shouldBeDeferred() {
        if (this.mRunnableAtInvalidated) {
            updateRunnableAt();
        }
        return this.mRunnableAtReason == 1 || this.mRunnableAtReason == 8;
    }

    public void assertHealthLocked() {
        if (!isActive()) {
            com.android.internal.util.Preconditions.checkState(!this.mRunnableAtInvalidated, "mRunnableAtInvalidated");
        }
        assertHealthLocked(this.mPending);
        assertHealthLocked(this.mPendingUrgent);
        assertHealthLocked(this.mPendingOffload);
    }

    private void assertHealthLocked(java.util.ArrayDeque<com.android.internal.os.SomeArgs> queue) {
        java.util.ArrayList<java.lang.String> brs = this.mBroadcastProcessQueueExt.beginAssertHealthLocked();
        if (queue.isEmpty()) {
            return;
        }
        java.util.Iterator<com.android.internal.os.SomeArgs> it = queue.descendingIterator();
        while (it.hasNext()) {
            com.android.internal.os.SomeArgs args = it.next();
            com.android.server.am.BroadcastRecord record = (com.android.server.am.BroadcastRecord) args.arg1;
            int recordIndex = args.argi1;
            if (!com.android.server.am.BroadcastRecord.isDeliveryStateTerminal(record.getDeliveryState(recordIndex)) && !record.isDeferUntilActive()) {
                long waitingTime = android.os.SystemClock.uptimeMillis() - record.enqueueTime;
                this.mBroadcastProcessQueueExt.assertHealthLocked(record, waitingTime, brs);
                com.android.internal.util.Preconditions.checkState(waitingTime < 600000, "waitingTime");
            }
        }
    }

    static com.android.server.am.BroadcastProcessQueue insertIntoRunnableList(com.android.server.am.BroadcastProcessQueue head, com.android.server.am.BroadcastProcessQueue item) {
        if (head == null) {
            return item;
        }
        long itemRunnableAt = item.getRunnableAt();
        com.android.server.am.BroadcastProcessQueue test = head;
        com.android.server.am.BroadcastProcessQueue tail = null;
        while (test != null) {
            if (test.getRunnableAt() > itemRunnableAt) {
                item.runnableAtNext = test;
                item.runnableAtPrev = test.runnableAtPrev;
                if (item.runnableAtNext != null) {
                    item.runnableAtNext.runnableAtPrev = item;
                }
                if (item.runnableAtPrev != null) {
                    item.runnableAtPrev.runnableAtNext = item;
                }
                return test == head ? item : head;
            }
            tail = test;
            test = test.runnableAtNext;
        }
        item.runnableAtPrev = tail;
        item.runnableAtPrev.runnableAtNext = item;
        return head;
    }

    static com.android.server.am.BroadcastProcessQueue removeFromRunnableList(com.android.server.am.BroadcastProcessQueue head, com.android.server.am.BroadcastProcessQueue item) {
        if (head == item) {
            head = item.runnableAtNext;
        }
        if (item.runnableAtNext != null) {
            item.runnableAtNext.runnableAtPrev = item.runnableAtPrev;
        }
        if (item.runnableAtPrev != null) {
            item.runnableAtPrev.runnableAtNext = item.runnableAtNext;
        }
        item.runnableAtNext = null;
        item.runnableAtPrev = null;
        return head;
    }

    void setTimeoutScheduled(boolean timeoutScheduled) {
        this.mTimeoutScheduled = timeoutScheduled;
    }

    boolean timeoutScheduled() {
        return this.mTimeoutScheduled;
    }

    public java.lang.String toString() {
        if (this.mCachedToString == null) {
            this.mCachedToString = "BroadcastProcessQueue{" + toShortString() + "}";
        }
        return this.mCachedToString;
    }

    public java.lang.String toShortString() {
        if (this.mCachedToShortString == null) {
            this.mCachedToShortString = java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + (this.app != null ? java.lang.Integer.valueOf(this.app.getPid()) : "?") + ":" + this.processName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.UserHandle.formatUid(this.uid);
        }
        return this.mCachedToShortString;
    }

    public java.lang.String describeStateLocked() {
        return describeStateLocked(android.os.SystemClock.uptimeMillis());
    }

    public java.lang.String describeStateLocked(long now) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (isRunnable()) {
            sb.append("runnable at ");
            android.util.TimeUtils.formatDuration(getRunnableAt(), now, sb);
        } else {
            sb.append("not runnable");
        }
        sb.append(" because ");
        sb.append(reasonToString(this.mRunnableAtReason));
        return sb.toString();
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpLocked(long now, android.util.IndentingPrintWriter pw) {
        if (this.mActive == null && isEmpty() && isOutgoingEmpty()) {
            return;
        }
        pw.print(toShortString());
        pw.print(" ");
        pw.print(describeStateLocked(now));
        pw.println();
        pw.increaseIndent();
        dumpProcessState(pw);
        dumpBroadcastCounts(pw);
        if (!this.mOutgoingBroadcasts.isEmpty()) {
            for (int i = 0; i < this.mOutgoingBroadcasts.size(); i++) {
                dumpOutgoingRecord(now, pw, this.mOutgoingBroadcasts.get(i));
            }
        }
        if (this.mActive != null) {
            dumpRecord("ACTIVE", now, pw, this.mActive, this.mActiveIndex);
        }
        for (com.android.internal.os.SomeArgs args : this.mPendingUrgent) {
            com.android.server.am.BroadcastRecord r = (com.android.server.am.BroadcastRecord) args.arg1;
            dumpRecord("URGENT", now, pw, r, args.argi1);
        }
        for (com.android.internal.os.SomeArgs args2 : this.mPending) {
            com.android.server.am.BroadcastRecord r2 = (com.android.server.am.BroadcastRecord) args2.arg1;
            dumpRecord(null, now, pw, r2, args2.argi1);
        }
        for (com.android.internal.os.SomeArgs args3 : this.mPendingOffload) {
            com.android.server.am.BroadcastRecord r3 = (com.android.server.am.BroadcastRecord) args3.arg1;
            dumpRecord("OFFLOAD", now, pw, r3, args3.argi1);
        }
        pw.decreaseIndent();
        pw.println();
    }

    @dalvik.annotation.optimization.NeverCompile
    private void dumpProcessState(android.util.IndentingPrintWriter pw) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (this.mUidForeground) {
            sb.append("FG");
        }
        if (this.mProcessFreezable) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("FRZ");
        }
        if (this.mProcessInstrumented) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("INSTR");
        }
        if (this.mProcessPersistent) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append("PER");
        }
        if (sb.length() > 0) {
            pw.print("state:");
            pw.println(sb);
        }
        if (this.runningOomAdjusted) {
            pw.print("runningOomAdjusted:");
            pw.println(this.runningOomAdjusted);
        }
        if (this.mActiveReEnqueued) {
            pw.print("activeReEnqueued:");
            pw.println(this.mActiveReEnqueued);
        }
    }

    @dalvik.annotation.optimization.NeverCompile
    private void dumpBroadcastCounts(android.util.IndentingPrintWriter pw) {
        pw.print("e:");
        pw.print(this.mCountEnqueued);
        pw.print(" d:");
        pw.print(this.mCountDeferred);
        pw.print(" f:");
        pw.print(this.mCountForeground);
        pw.print(" fd:");
        pw.print(this.mCountForegroundDeferred);
        pw.print(" o:");
        pw.print(this.mCountOrdered);
        pw.print(" a:");
        pw.print(this.mCountAlarm);
        pw.print(" p:");
        pw.print(this.mCountPrioritized);
        pw.print(" pd:");
        pw.print(this.mCountPrioritizedDeferred);
        pw.print(" int:");
        pw.print(this.mCountInteractive);
        pw.print(" rt:");
        pw.print(this.mCountResultTo);
        pw.print(" ins:");
        pw.print(this.mCountInstrumented);
        pw.print(" m:");
        pw.print(this.mCountManifest);
        pw.print(" csi:");
        pw.print(this.mActiveCountSinceIdle);
        pw.print(" adcsi:");
        pw.print(this.mActiveAssumedDeliveryCountSinceIdle);
        pw.print(" ccu:");
        pw.print(this.mActiveCountConsecutiveUrgent);
        pw.print(" ccn:");
        pw.print(this.mActiveCountConsecutiveNormal);
        pw.println();
    }

    @dalvik.annotation.optimization.NeverCompile
    private void dumpOutgoingRecord(long now, android.util.IndentingPrintWriter pw, com.android.server.am.BroadcastRecord record) {
        pw.print("OUTGOING ");
        android.util.TimeUtils.formatDuration(record.enqueueTime, now, pw);
        pw.print(' ');
        pw.println(record.toShortString());
    }

    @dalvik.annotation.optimization.NeverCompile
    private void dumpRecord(java.lang.String flavor, long now, android.util.IndentingPrintWriter pw, com.android.server.am.BroadcastRecord record, int recordIndex) {
        android.util.TimeUtils.formatDuration(record.enqueueTime, now, pw);
        pw.print(' ');
        pw.println(record.toString());
        pw.print("    ");
        int deliveryState = record.delivery[recordIndex];
        pw.print(com.android.server.am.BroadcastRecord.deliveryStateToString(deliveryState));
        if (deliveryState == 4) {
            pw.print(" at ");
            android.util.TimeUtils.formatDuration(record.scheduledTime[recordIndex], now, pw);
        }
        if (flavor != null) {
            pw.print(' ');
            pw.print(flavor);
        }
        java.lang.Object receiver = record.receivers.get(recordIndex);
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            com.android.server.am.BroadcastFilter filter = (com.android.server.am.BroadcastFilter) receiver;
            pw.print(" for registered ");
            pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(filter)));
        } else {
            android.content.pm.ResolveInfo info = (android.content.pm.ResolveInfo) receiver;
            pw.print(" for manifest ");
            pw.print(info.activityInfo.name);
        }
        pw.println();
        int blockedUntilBeyondCount = record.blockedUntilBeyondCount[recordIndex];
        if (blockedUntilBeyondCount != -1) {
            pw.print("    blocked until ");
            pw.print(blockedUntilBeyondCount);
            pw.print(", currently at ");
            pw.print(record.beyondCount);
            pw.print(" of ");
            pw.print(record.receivers.size());
            pw.print(", recordIndex: ");
            pw.println(recordIndex);
        }
    }

    public com.android.server.am.IBroadcastProcessQueueWrapper getWrapper() {
        return this.mWrapper;
    }

    private class BroadcastProcessQueueWrapper implements com.android.server.am.IBroadcastProcessQueueWrapper {
        private BroadcastProcessQueueWrapper() {
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public com.android.server.am.IBroadcastProcessQueueExt getExtImpl() {
            return com.android.server.am.BroadcastProcessQueue.this.mBroadcastProcessQueueExt;
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public void enqueueBroadcast(com.android.server.am.BroadcastRecord record, int recordIndex, boolean addFirst, com.android.server.am.BroadcastProcessQueue.BroadcastConsumer deferredStatesApplyConsumer) {
            com.android.internal.os.SomeArgs newBroadcastArgs = com.android.internal.os.SomeArgs.obtain();
            newBroadcastArgs.arg1 = record;
            newBroadcastArgs.argi1 = recordIndex;
            if (addFirst) {
                com.android.server.am.BroadcastProcessQueue.this.getQueueForBroadcast(record).addFirst(newBroadcastArgs);
            } else {
                com.android.server.am.BroadcastProcessQueue.this.getQueueForBroadcast(record).addLast(newBroadcastArgs);
            }
            com.android.server.am.BroadcastProcessQueue.this.onBroadcastEnqueued(record, recordIndex);
            if (com.android.server.am.BroadcastProcessQueue.this.mLastDeferredStates && com.android.server.am.BroadcastProcessQueue.this.shouldBeDeferred() && record.getDeliveryState(recordIndex) == 0) {
                deferredStatesApplyConsumer.accept(record, recordIndex);
            }
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public long getRunnableAtWithoutRefresh() {
            return com.android.server.am.BroadcastProcessQueue.this.mRunnableAt;
        }

        @Override // com.android.server.am.IBroadcastProcessQueueWrapper
        public int getRunnableAtReasonWithoutRefresh() {
            return com.android.server.am.BroadcastProcessQueue.this.mRunnableAtReason;
        }
    }
}
