package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class OomAdjusterModernImpl extends com.android.server.am.OomAdjuster {
    static final int ADJ_SLOT_BACKUP_APP = 10;
    static final int ADJ_SLOT_CACHED_APP = 16;
    static final int ADJ_SLOT_FOREGROUND_APP = 4;
    static final int ADJ_SLOT_HEAVY_WEIGHT_APP = 11;
    static final int ADJ_SLOT_HOME_APP = 13;
    static final int ADJ_SLOT_INVALID = -1;
    static final int ADJ_SLOT_NATIVE = 0;
    static final int ADJ_SLOT_PERCEPTIBLE_APP = 7;
    static final int ADJ_SLOT_PERCEPTIBLE_LOW_APP = 9;
    static final int ADJ_SLOT_PERCEPTIBLE_MEDIUM_APP = 8;
    static final int ADJ_SLOT_PERCEPTIBLE_RECENT_FOREGROUND_APP = 5;
    static final int ADJ_SLOT_PERSISTENT_PROC = 2;
    static final int ADJ_SLOT_PERSISTENT_SERVICE = 3;
    static final int ADJ_SLOT_PREVIOUS_APP = 14;
    static final int ADJ_SLOT_SERVICE = 12;
    static final int ADJ_SLOT_SERVICE_B = 15;
    static final int ADJ_SLOT_SYSTEM = 1;
    static final int ADJ_SLOT_UNKNOWN = 17;
    static final int ADJ_SLOT_VISIBLE_APP = 6;
    static final java.lang.String TAG = "OomAdjusterModernImpl";
    private final com.android.server.am.OomAdjusterModernImpl.ComputeConnectionIgnoringReachableClientsConsumer mComputeConnectionIgnoringReachableClientsConsumer;
    private final com.android.server.am.OomAdjusterModernImpl.ComputeConnectionsConsumer mComputeConnectionsConsumer;
    private final com.android.server.am.OomAdjusterModernImpl.ComputeHostConsumer mComputeHostConsumer;
    private final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes mProcessRecordAdjNodes;
    private final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes mProcessRecordProcStateNodes;
    private final com.android.server.am.OomAdjusterModernImpl.ReachableCollectingConsumer mReachableCollectingConsumer;
    private final com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs mTmpOomAdjusterArgs;
    static final int[] ADJ_SLOT_VALUES = {-1000, com.android.server.am.ProcessList.SYSTEM_ADJ, com.android.server.am.ProcessList.PERSISTENT_PROC_ADJ, com.android.server.am.ProcessList.PERSISTENT_SERVICE_ADJ, 0, 50, 100, 200, com.android.server.am.ProcessList.PERCEPTIBLE_MEDIUM_APP_ADJ, 250, 300, 400, 500, 600, com.android.server.am.ProcessList.PREVIOUS_APP_ADJ, 800, 900, 1001};
    static final int[] PROC_STATE_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, -1};

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface AdjSlot {
    }

    public interface Connection {
        boolean canAffectCapabilities();

        void computeHostOomAdjLSP(com.android.server.am.OomAdjuster oomAdjuster, com.android.server.am.ProcessRecord processRecord, com.android.server.am.ProcessRecord processRecord2, long j, com.android.server.am.ProcessRecord processRecord3, boolean z, int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int adjToSlot(int adj) {
        if (adj >= ADJ_SLOT_VALUES[0] && adj <= ADJ_SLOT_VALUES[ADJ_SLOT_VALUES.length - 1]) {
            int index = java.util.Arrays.binarySearch(ADJ_SLOT_VALUES, adj);
            if (index >= 0) {
                return index;
            }
            return (-(index + 1)) - 1;
        }
        return ADJ_SLOT_VALUES.length - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int processStateToSlot(int state) {
        if (state >= 0 && state <= 19) {
            return state;
        }
        return PROC_STATE_SLOTS.length - 1;
    }

    static class ProcessRecordNode {
        static final int NODE_TYPE_ADJ = 1;
        static final int NODE_TYPE_PROC_STATE = 0;
        static final int NUM_NODE_TYPE = 2;
        final com.android.server.am.ProcessRecord mApp;
        com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode mNext;
        com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode mPrev;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface NodeType {
        }

        ProcessRecordNode(com.android.server.am.ProcessRecord app) {
            this.mApp = app;
        }

        void unlink() {
            if (this.mPrev != null) {
                this.mPrev.mNext = this.mNext;
            }
            if (this.mNext != null) {
                this.mNext.mPrev = this.mPrev;
            }
            this.mNext = null;
            this.mPrev = null;
        }

        boolean isLinked() {
            return (this.mPrev == null || this.mNext == null) ? false : true;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("ProcessRecordNode{");
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
            sb.append(' ');
            sb.append(this.mApp);
            sb.append(' ');
            sb.append(this.mApp != null ? this.mApp.mState.getCurProcState() : -1);
            sb.append(' ');
            sb.append(this.mApp != null ? this.mApp.mState.getCurAdj() : 1001);
            sb.append(' ');
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mPrev)));
            sb.append(' ');
            sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mNext)));
            sb.append('}');
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ProcessRecordNodes {
        private int mFirstPopulatedSlot = 0;
        private final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode[] mLastNode;
        private final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.LinkedProcessRecordList[] mProcessRecordNodes;
        private final java.util.function.ToIntFunction<com.android.server.am.ProcessRecord> mSlotFunction;
        private final int mType;

        ProcessRecordNodes(int type, int size) {
            java.util.function.ToIntFunction<com.android.server.am.ProcessRecord> valueFunction;
            this.mType = type;
            switch (this.mType) {
                case 0:
                    valueFunction = new java.util.function.ToIntFunction() { // from class: com.android.server.am.OomAdjusterModernImpl$ProcessRecordNodes$$ExternalSyntheticLambda0
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(java.lang.Object obj) {
                            return ((com.android.server.am.ProcessRecord) obj).mState.getCurProcState();
                        }
                    };
                    this.mSlotFunction = new java.util.function.ToIntFunction() { // from class: com.android.server.am.OomAdjusterModernImpl$ProcessRecordNodes$$ExternalSyntheticLambda1
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(java.lang.Object obj) {
                            return com.android.server.am.OomAdjusterModernImpl.processStateToSlot(((com.android.server.am.ProcessRecord) obj).mState.getCurProcState());
                        }
                    };
                    break;
                case 1:
                    valueFunction = new java.util.function.ToIntFunction() { // from class: com.android.server.am.OomAdjusterModernImpl$ProcessRecordNodes$$ExternalSyntheticLambda2
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(java.lang.Object obj) {
                            return ((com.android.server.am.ProcessRecord) obj).mState.getCurRawAdj();
                        }
                    };
                    this.mSlotFunction = new java.util.function.ToIntFunction() { // from class: com.android.server.am.OomAdjusterModernImpl$ProcessRecordNodes$$ExternalSyntheticLambda3
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(java.lang.Object obj) {
                            return com.android.server.am.OomAdjusterModernImpl.adjToSlot(((com.android.server.am.ProcessRecord) obj).mState.getCurRawAdj());
                        }
                    };
                    break;
                default:
                    valueFunction = new java.util.function.ToIntFunction() { // from class: com.android.server.am.OomAdjusterModernImpl$ProcessRecordNodes$$ExternalSyntheticLambda4
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(java.lang.Object obj) {
                            return com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.lambda$new$4((com.android.server.am.ProcessRecord) obj);
                        }
                    };
                    this.mSlotFunction = new java.util.function.ToIntFunction() { // from class: com.android.server.am.OomAdjusterModernImpl$ProcessRecordNodes$$ExternalSyntheticLambda5
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(java.lang.Object obj) {
                            return com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.lambda$new$5((com.android.server.am.ProcessRecord) obj);
                        }
                    };
                    break;
            }
            this.mProcessRecordNodes = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.LinkedProcessRecordList[size];
            for (int i = 0; i < size; i++) {
                this.mProcessRecordNodes[i] = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.LinkedProcessRecordList(valueFunction);
            }
            this.mLastNode = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode[size];
            resetLastNodes();
        }

        static /* synthetic */ int lambda$new$4(com.android.server.am.ProcessRecord proc) {
            return 0;
        }

        static /* synthetic */ int lambda$new$5(com.android.server.am.ProcessRecord proc) {
            return 0;
        }

        int size() {
            return this.mProcessRecordNodes.length;
        }

        void reset() {
            for (int i = 0; i < this.mProcessRecordNodes.length; i++) {
                this.mProcessRecordNodes[i].reset();
                setLastNodeToHead(i);
            }
        }

        void resetLastNodes() {
            if (com.android.server.am.Flags.simplifyProcessTraversal()) {
                reset();
                return;
            }
            for (int i = 0; i < this.mProcessRecordNodes.length; i++) {
                this.mLastNode[i] = this.mProcessRecordNodes[i].getLastNodeBeforeTail();
            }
        }

        void setLastNodeToHead(int slot) {
            this.mLastNode[slot] = this.mProcessRecordNodes[slot].HEAD;
        }

        void forEachNewNode(int slot, java.util.function.Consumer<com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs> callback) {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = this.mLastNode[slot].mNext;
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode tail = this.mProcessRecordNodes[slot].TAIL;
            while (node != tail) {
                com.android.server.am.OomAdjusterModernImpl.this.mTmpOomAdjusterArgs.mApp = node.mApp;
                if (node.mApp == null) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append("Iterating null process during OomAdjuster traversal!!!\n");
                    sb.append("Type:");
                    switch (this.mType) {
                        case 0:
                            sb.append("NODE_TYPE_PROC_STATE");
                            break;
                        case 1:
                            sb.append("NODE_TYPE_ADJ");
                            break;
                        default:
                            sb.append("UNKNOWN");
                            break;
                    }
                    sb.append(", Slot:");
                    sb.append(slot);
                    sb.append("\nLAST:");
                    com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode last = this.mLastNode[slot];
                    if (last.mApp == null) {
                        sb.append("null");
                    } else {
                        sb.append(last);
                        sb.append("\nSetProcState:");
                        sb.append(last.mApp.getSetProcState());
                        sb.append(", CurProcState:");
                        sb.append(last.mApp.mState.getCurProcState());
                        sb.append(", SetAdj:");
                        sb.append(last.mApp.getSetAdj());
                        sb.append(", CurRawAdj:");
                        sb.append(last.mApp.mState.getCurRawAdj());
                    }
                    android.util.Slog.wtfStack(com.android.server.am.OomAdjusterModernImpl.TAG, sb.toString());
                }
                com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode next = node.mNext;
                callback.accept(com.android.server.am.OomAdjusterModernImpl.this.mTmpOomAdjusterArgs);
                node = (next != tail || node.mNext == null || node.mNext.mNext == null) ? next : node.mNext;
            }
        }

        com.android.server.am.ProcessRecord poll() {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = null;
            int size = this.mProcessRecordNodes.length;
            while (node == null && this.mFirstPopulatedSlot < size) {
                node = this.mProcessRecordNodes[this.mFirstPopulatedSlot].poll();
                if (node == null) {
                    this.mFirstPopulatedSlot++;
                }
            }
            if (node == null) {
                return null;
            }
            return node.mApp;
        }

        void offer(com.android.server.am.ProcessRecord proc) {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = proc.mLinkedNodes[this.mType];
            int newSlot = this.mSlotFunction.applyAsInt(proc);
            if (newSlot < this.mFirstPopulatedSlot) {
                this.mFirstPopulatedSlot = newSlot;
            }
            node.unlink();
            this.mProcessRecordNodes[newSlot].offer(node);
        }

        int getNumberOfSlots() {
            return this.mProcessRecordNodes.length;
        }

        void moveAppTo(com.android.server.am.ProcessRecord app, int prevSlot, int newSlot) {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = app.mLinkedNodes[this.mType];
            if (prevSlot != -1 && this.mLastNode[prevSlot] == node) {
                this.mLastNode[prevSlot] = node.mPrev;
            }
            append(node, newSlot);
        }

        void moveAllNodesTo(int fromSlot, int toSlot) {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.LinkedProcessRecordList fromList = this.mProcessRecordNodes[fromSlot];
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.LinkedProcessRecordList toList = this.mProcessRecordNodes[toSlot];
            if (fromSlot != toSlot && fromList.HEAD.mNext != fromList.TAIL) {
                fromList.moveTo(toList);
                this.mLastNode[fromSlot] = fromList.getLastNodeBeforeTail();
            }
        }

        void moveAppToTail(com.android.server.am.ProcessRecord app) {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = app.mLinkedNodes[this.mType];
            switch (this.mType) {
                case 0:
                    int slot = com.android.server.am.OomAdjusterModernImpl.processStateToSlot(app.mState.getCurProcState());
                    if (this.mLastNode[slot] == node) {
                        this.mLastNode[slot] = node.mPrev;
                    }
                    this.mProcessRecordNodes[slot].moveNodeToTail(node);
                    break;
                case 1:
                    int slot2 = com.android.server.am.OomAdjusterModernImpl.adjToSlot(app.mState.getCurRawAdj());
                    if (this.mLastNode[slot2] == node) {
                        this.mLastNode[slot2] = node.mPrev;
                    }
                    this.mProcessRecordNodes[slot2].moveNodeToTail(node);
                    break;
            }
        }

        void reset(int slot) {
            this.mProcessRecordNodes[slot].reset();
        }

        void unlink(com.android.server.am.ProcessRecord app) {
            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = app.mLinkedNodes[this.mType];
            int slot = getCurrentSlot(app);
            if (slot != -1 && this.mLastNode[slot] == node) {
                this.mLastNode[slot] = node.mPrev;
            }
            node.unlink();
        }

        void append(com.android.server.am.ProcessRecord app) {
            append(app, getCurrentSlot(app));
        }

        void append(com.android.server.am.ProcessRecord app, int targetSlot) {
            append(app.mLinkedNodes[this.mType], targetSlot);
        }

        void append(com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node, int targetSlot) {
            node.unlink();
            this.mProcessRecordNodes[targetSlot].append(node);
        }

        private int getCurrentSlot(com.android.server.am.ProcessRecord app) {
            switch (this.mType) {
                case 0:
                    return com.android.server.am.OomAdjusterModernImpl.processStateToSlot(app.mState.getCurProcState());
                case 1:
                    return com.android.server.am.OomAdjusterModernImpl.adjToSlot(app.mState.getCurRawAdj());
                default:
                    return -1;
            }
        }

        java.lang.String toString(int slot, int logUid) {
            return "lastNode=" + this.mLastNode[slot] + " " + this.mProcessRecordNodes[slot].toString(logUid);
        }

        private static class LinkedProcessRecordList {
            final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode HEAD = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode(null);
            final com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode TAIL = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode(null);
            final java.util.function.ToIntFunction<com.android.server.am.ProcessRecord> mValueFunction;

            LinkedProcessRecordList(java.util.function.ToIntFunction<com.android.server.am.ProcessRecord> valueFunction) {
                this.HEAD.mNext = this.TAIL;
                this.TAIL.mPrev = this.HEAD;
                this.mValueFunction = valueFunction;
            }

            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode poll() {
                com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode next = this.HEAD.mNext;
                if (next == this.TAIL) {
                    return null;
                }
                next.unlink();
                return next;
            }

            void offer(com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node) {
                int newValue = this.mValueFunction.applyAsInt(node.mApp);
                com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode curNode = this.TAIL.mPrev;
                while (curNode != this.HEAD && this.mValueFunction.applyAsInt(curNode.mApp) > newValue) {
                    curNode = curNode.mPrev;
                }
                node.mPrev = curNode;
                node.mNext = curNode.mNext;
                curNode.mNext.mPrev = node;
                curNode.mNext = node;
            }

            void append(com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node) {
                node.mNext = this.TAIL;
                node.mPrev = this.TAIL.mPrev;
                this.TAIL.mPrev.mNext = node;
                this.TAIL.mPrev = node;
            }

            void moveTo(com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes.LinkedProcessRecordList toList) {
                if (this.HEAD.mNext != this.TAIL) {
                    toList.TAIL.mPrev.mNext = this.HEAD.mNext;
                    this.HEAD.mNext.mPrev = toList.TAIL.mPrev;
                    toList.TAIL.mPrev = this.TAIL.mPrev;
                    this.TAIL.mPrev.mNext = toList.TAIL;
                    this.HEAD.mNext = this.TAIL;
                    this.TAIL.mPrev = this.HEAD;
                }
            }

            void moveNodeToTail(com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node) {
                node.unlink();
                append(node);
            }

            com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode getLastNodeBeforeTail() {
                return this.TAIL.mPrev;
            }

            void reset() {
                if (this.HEAD.mNext != this.TAIL) {
                    com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode processRecordNode = this.HEAD.mNext;
                    this.TAIL.mPrev.mNext = null;
                    processRecordNode.mPrev = null;
                }
                this.HEAD.mNext = this.TAIL;
                this.TAIL.mPrev = this.HEAD;
            }

            java.lang.String toString(int logUid) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append("LinkedProcessRecordList{");
                sb.append(this.HEAD);
                sb.append(' ');
                sb.append(this.TAIL);
                sb.append('[');
                for (com.android.server.am.OomAdjusterModernImpl.ProcessRecordNode node = this.HEAD.mNext; node != this.TAIL; node = node.mNext) {
                    if (node.mApp != null && node.mApp.uid == logUid) {
                        sb.append(node);
                        sb.append(',');
                    }
                }
                sb.append(']');
                sb.append('}');
                return sb.toString();
            }
        }
    }

    private class OomAdjusterArgs {
        com.android.server.am.ProcessRecord mApp;
        int mCachedAdj;
        boolean mFullUpdate;
        long mNow;
        int mOomAdjReason;
        com.android.server.am.ProcessRecord mTopApp;
        com.android.server.am.ActiveUids mUids;

        private OomAdjusterArgs() {
        }

        void update(com.android.server.am.ProcessRecord topApp, long now, int cachedAdj, int oomAdjReason, com.android.server.am.ActiveUids uids, boolean fullUpdate) {
            this.mTopApp = topApp;
            this.mNow = now;
            this.mCachedAdj = cachedAdj;
            this.mOomAdjReason = oomAdjReason;
            this.mUids = uids;
            this.mFullUpdate = fullUpdate;
        }
    }

    private static class ReachableCollectingConsumer implements java.util.function.BiConsumer<com.android.server.am.OomAdjusterModernImpl.Connection, com.android.server.am.ProcessRecord> {
        java.util.ArrayList<com.android.server.am.ProcessRecord> mReachables;

        private ReachableCollectingConsumer() {
            this.mReachables = null;
        }

        public void init(java.util.ArrayList<com.android.server.am.ProcessRecord> reachables) {
            this.mReachables = reachables;
        }

        @Override // java.util.function.BiConsumer
        public void accept(com.android.server.am.OomAdjusterModernImpl.Connection unused, com.android.server.am.ProcessRecord host) {
            if (host.mState.isReachable()) {
                return;
            }
            host.mState.setReachable(true);
            this.mReachables.add(host);
        }
    }

    private class ComputeConnectionIgnoringReachableClientsConsumer implements java.util.function.BiConsumer<com.android.server.am.OomAdjusterModernImpl.Connection, com.android.server.am.ProcessRecord> {
        public boolean hasReachableClient;
        private com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs mArgs;

        private ComputeConnectionIgnoringReachableClientsConsumer() {
            this.mArgs = null;
            this.hasReachableClient = false;
        }

        public void init(com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs args) {
            this.mArgs = args;
            this.hasReachableClient = false;
        }

        @Override // java.util.function.BiConsumer
        public void accept(com.android.server.am.OomAdjusterModernImpl.Connection conn, com.android.server.am.ProcessRecord client) {
            com.android.server.am.ProcessRecord host = this.mArgs.mApp;
            com.android.server.am.ProcessRecord topApp = this.mArgs.mTopApp;
            long now = this.mArgs.mNow;
            int oomAdjReason = this.mArgs.mOomAdjReason;
            if (client.mState.isReachable()) {
                this.hasReachableClient = true;
            } else {
                if (com.android.server.am.OomAdjusterModernImpl.unimportantConnectionLSP(conn, host, client)) {
                    return;
                }
                conn.computeHostOomAdjLSP(com.android.server.am.OomAdjusterModernImpl.this, host, client, now, topApp, false, oomAdjReason, 1001);
            }
        }
    }

    private class ComputeHostConsumer implements java.util.function.BiConsumer<com.android.server.am.OomAdjusterModernImpl.Connection, com.android.server.am.ProcessRecord> {
        public com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs args;

        private ComputeHostConsumer() {
            this.args = null;
        }

        @Override // java.util.function.BiConsumer
        public void accept(com.android.server.am.OomAdjusterModernImpl.Connection conn, com.android.server.am.ProcessRecord host) {
            com.android.server.am.ProcessRecord client = this.args.mApp;
            int cachedAdj = this.args.mCachedAdj;
            com.android.server.am.ProcessRecord topApp = this.args.mTopApp;
            long now = this.args.mNow;
            int oomAdjReason = this.args.mOomAdjReason;
            boolean z = this.args.mFullUpdate;
            int prevProcState = host.mState.getCurProcState();
            int prevAdj = host.mState.getCurRawAdj();
            if (com.android.server.am.OomAdjusterModernImpl.unimportantConnectionLSP(conn, host, client)) {
                return;
            }
            conn.computeHostOomAdjLSP(com.android.server.am.OomAdjusterModernImpl.this, host, client, now, topApp, z, oomAdjReason, cachedAdj);
            com.android.server.am.OomAdjusterModernImpl.this.updateProcStateSlotIfNecessary(host, prevProcState);
            com.android.server.am.OomAdjusterModernImpl.this.updateAdjSlotIfNecessary(host, prevAdj);
        }
    }

    private class ComputeConnectionsConsumer implements java.util.function.Consumer<com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs> {
        private ComputeConnectionsConsumer() {
        }

        @Override // java.util.function.Consumer
        public void accept(com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs args) {
            com.android.server.am.UidRecord uidRec;
            com.android.server.am.ProcessRecord app = args.mApp;
            com.android.server.am.ActiveUids uids = args.mUids;
            app.mState.setCompletedAdjSeq(com.android.server.am.OomAdjusterModernImpl.this.mAdjSeq);
            if (uids != null && (uidRec = app.getUidRecord()) != null) {
                uids.put(uidRec.getUid(), uidRec);
            }
            com.android.server.am.OomAdjusterModernImpl.this.mComputeHostConsumer.args = args;
            com.android.server.am.OomAdjusterModernImpl.forEachConnectionLSP(app, com.android.server.am.OomAdjusterModernImpl.this.mComputeHostConsumer);
        }
    }

    OomAdjusterModernImpl(com.android.server.am.ActivityManagerService service, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids) {
        this(service, processList, activeUids, createAdjusterThread());
    }

    /* JADX WARN: Multi-variable type inference failed */
    OomAdjusterModernImpl(com.android.server.am.ActivityManagerService activityManagerService, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids, com.android.server.ServiceThread serviceThread) {
        super(activityManagerService, processList, activeUids, serviceThread);
        this.mReachableCollectingConsumer = new com.android.server.am.OomAdjusterModernImpl.ReachableCollectingConsumer();
        this.mComputeConnectionIgnoringReachableClientsConsumer = new com.android.server.am.OomAdjusterModernImpl.ComputeConnectionIgnoringReachableClientsConsumer();
        this.mComputeHostConsumer = new com.android.server.am.OomAdjusterModernImpl.ComputeHostConsumer();
        this.mComputeConnectionsConsumer = new com.android.server.am.OomAdjusterModernImpl.ComputeConnectionsConsumer();
        this.mProcessRecordProcStateNodes = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes(0, PROC_STATE_SLOTS.length);
        this.mProcessRecordAdjNodes = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes(1, ADJ_SLOT_VALUES.length);
        this.mTmpOomAdjusterArgs = new com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs();
    }

    /* JADX WARN: Multi-variable type inference failed */
    OomAdjusterModernImpl(com.android.server.am.ActivityManagerService activityManagerService, com.android.server.am.ProcessList processList, com.android.server.am.ActiveUids activeUids, com.android.server.am.OomAdjuster.Injector injector) {
        super(activityManagerService, processList, activeUids, injector);
        this.mReachableCollectingConsumer = new com.android.server.am.OomAdjusterModernImpl.ReachableCollectingConsumer();
        this.mComputeConnectionIgnoringReachableClientsConsumer = new com.android.server.am.OomAdjusterModernImpl.ComputeConnectionIgnoringReachableClientsConsumer();
        this.mComputeHostConsumer = new com.android.server.am.OomAdjusterModernImpl.ComputeHostConsumer();
        this.mComputeConnectionsConsumer = new com.android.server.am.OomAdjusterModernImpl.ComputeConnectionsConsumer();
        this.mProcessRecordProcStateNodes = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes(0, PROC_STATE_SLOTS.length);
        this.mProcessRecordAdjNodes = new com.android.server.am.OomAdjusterModernImpl.ProcessRecordNodes(1, ADJ_SLOT_VALUES.length);
        this.mTmpOomAdjusterArgs = new com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs();
    }

    void linkProcessRecordToList(com.android.server.am.ProcessRecord app) {
        this.mProcessRecordProcStateNodes.append(app);
        this.mProcessRecordAdjNodes.append(app);
    }

    void unlinkProcessRecordFromList(com.android.server.am.ProcessRecord app) {
        this.mProcessRecordProcStateNodes.unlink(app);
        this.mProcessRecordAdjNodes.unlink(app);
    }

    @Override // com.android.server.am.OomAdjuster
    void resetInternal() {
        this.mProcessRecordProcStateNodes.reset();
        this.mProcessRecordAdjNodes.reset();
    }

    @Override // com.android.server.am.OomAdjuster
    void onProcessEndLocked(com.android.server.am.ProcessRecord app) {
        if (app.mLinkedNodes[0] != null && app.mLinkedNodes[0].isLinked()) {
            unlinkProcessRecordFromList(app);
        }
    }

    @Override // com.android.server.am.OomAdjuster
    void onProcessStateChanged(com.android.server.am.ProcessRecord app, int prevProcState) {
        updateProcStateSlotIfNecessary(app, prevProcState);
    }

    @Override // com.android.server.am.OomAdjuster
    void onProcessOomAdjChanged(com.android.server.am.ProcessRecord app, int prevAdj) {
        updateAdjSlotIfNecessary(app, prevAdj);
    }

    @Override // com.android.server.am.OomAdjuster
    protected int getInitialAdj(com.android.server.am.ProcessRecord app) {
        return 1001;
    }

    @Override // com.android.server.am.OomAdjuster
    protected int getInitialProcState(com.android.server.am.ProcessRecord app) {
        return -1;
    }

    @Override // com.android.server.am.OomAdjuster
    protected int getInitialCapability(com.android.server.am.ProcessRecord app) {
        return 0;
    }

    @Override // com.android.server.am.OomAdjuster
    protected boolean getInitialIsCurBoundByNonBgRestrictedApp(com.android.server.am.ProcessRecord app) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAdjSlotIfNecessary(com.android.server.am.ProcessRecord app, int prevRawAdj) {
        if (app.mState.getCurRawAdj() != prevRawAdj) {
            if (com.android.server.am.Flags.simplifyProcessTraversal()) {
                this.mProcessRecordAdjNodes.offer(app);
                return;
            }
            int slot = adjToSlot(app.mState.getCurRawAdj());
            int prevSlot = adjToSlot(prevRawAdj);
            if (slot != prevSlot && slot != -1) {
                this.mProcessRecordAdjNodes.moveAppTo(app, prevSlot, slot);
            }
        }
    }

    private void updateAdjSlot(com.android.server.am.ProcessRecord app, int prevRawAdj) {
        if (com.android.server.am.Flags.simplifyProcessTraversal()) {
            this.mProcessRecordAdjNodes.offer(app);
            return;
        }
        int slot = adjToSlot(app.mState.getCurRawAdj());
        int prevSlot = adjToSlot(prevRawAdj);
        this.mProcessRecordAdjNodes.moveAppTo(app, prevSlot, slot);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProcStateSlotIfNecessary(com.android.server.am.ProcessRecord app, int prevProcState) {
        if (app.mState.getCurProcState() != prevProcState) {
            if (com.android.server.am.Flags.simplifyProcessTraversal()) {
                this.mProcessRecordProcStateNodes.offer(app);
                return;
            }
            int slot = processStateToSlot(app.mState.getCurProcState());
            int prevSlot = processStateToSlot(prevProcState);
            if (slot != prevSlot) {
                this.mProcessRecordProcStateNodes.moveAppTo(app, prevSlot, slot);
            }
        }
    }

    private void updateProcStateSlot(com.android.server.am.ProcessRecord app, int prevProcState) {
        if (com.android.server.am.Flags.simplifyProcessTraversal()) {
            this.mProcessRecordProcStateNodes.offer(app);
            return;
        }
        int slot = processStateToSlot(app.mState.getCurProcState());
        int prevSlot = processStateToSlot(prevProcState);
        this.mProcessRecordProcStateNodes.moveAppTo(app, prevSlot, slot);
    }

    @Override // com.android.server.am.OomAdjuster
    protected void performUpdateOomAdjLSP(int oomAdjReason) {
        this.mService.getTopApp();
        this.mProcessStateCurTop = this.mService.mAtmInternal.getTopProcessState();
        this.mPendingProcessSet.clear();
        com.android.server.am.AppProfiler appProfiler = this.mService.mAppProfiler;
        this.mService.mAppProfiler.mHasHomeProcess = false;
        appProfiler.mHasPreviousProcess = false;
        this.mLastReason = oomAdjReason;
        android.os.Trace.traceBegin(64L, oomAdjReasonToString(oomAdjReason));
        fullUpdateLSP(oomAdjReason);
        android.os.Trace.traceEnd(64L);
    }

    @Override // com.android.server.am.OomAdjuster
    protected boolean performUpdateOomAdjLSP(com.android.server.am.ProcessRecord app, int oomAdjReason) {
        this.mPendingProcessSet.add(app);
        performUpdateOomAdjPendingTargetsLocked(oomAdjReason);
        return true;
    }

    @Override // com.android.server.am.OomAdjuster
    protected void performUpdateOomAdjPendingTargetsLocked(int oomAdjReason) {
        this.mLastReason = oomAdjReason;
        this.mProcessStateCurTop = enqueuePendingTopAppIfNecessaryLSP();
        android.os.Trace.traceBegin(64L, oomAdjReasonToString(oomAdjReason));
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                partialUpdateLSP(oomAdjReason, this.mPendingProcessSet);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
        this.mPendingProcessSet.clear();
        android.os.Trace.traceEnd(64L);
    }

    private void fullUpdateLSP(int oomAdjReason) {
        com.android.server.am.ProcessRecord topApp = this.mService.getTopApp();
        long now = this.mInjector.getUptimeMillis();
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        long oldTime = now - this.mConstants.mMaxEmptyTimeMillis;
        this.mAdjSeq++;
        this.mNewNumServiceProcs = 0;
        this.mNewNumAServiceProcs = 0;
        this.mProcessRecordProcStateNodes.reset();
        this.mProcessRecordAdjNodes.reset();
        java.util.ArrayList<com.android.server.am.ProcessRecord> lru = this.mProcessList.getLruProcessesLOSP();
        int i = lru.size() - 1;
        while (i >= 0) {
            com.android.server.am.ProcessRecord app = lru.get(i);
            int prevProcState = app.mState.getCurProcState();
            int prevAdj = app.mState.getCurRawAdj();
            app.mState.resetCachedInfo();
            com.android.server.am.UidRecord uidRec = app.getUidRecord();
            if (uidRec != null) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                    android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Starting update of " + uidRec);
                }
                uidRec.reset();
            }
            int i2 = i;
            java.util.ArrayList<com.android.server.am.ProcessRecord> lru2 = lru;
            computeOomAdjLSP(app, 1001, topApp, true, now, false, false, oomAdjReason, false);
            if (!com.android.server.am.Flags.simplifyProcessTraversal()) {
                updateProcStateSlot(app, prevProcState);
                updateAdjSlot(app, prevAdj);
            } else {
                this.mProcessRecordProcStateNodes.offer(app);
            }
            i = i2 - 1;
            lru = lru2;
        }
        this.mProcessRecordAdjNodes.resetLastNodes();
        this.mTmpOomAdjusterArgs.update(topApp, now, 1001, oomAdjReason, null, true);
        computeConnectionsLSP();
        assignCachedAdjIfNecessary(this.mProcessList.getLruProcessesLOSP());
        postUpdateOomAdjInnerLSP(oomAdjReason, this.mActiveUids, now, nowElapsed, oldTime, true);
    }

    private void computeConnectionsLSP() {
        if (com.android.server.am.Flags.simplifyProcessTraversal()) {
            com.android.server.am.ProcessRecord proc = this.mProcessRecordProcStateNodes.poll();
            while (proc != null) {
                this.mTmpOomAdjusterArgs.mApp = proc;
                this.mComputeConnectionsConsumer.accept(this.mTmpOomAdjusterArgs);
                proc = this.mProcessRecordProcStateNodes.poll();
            }
            com.android.server.am.ProcessRecord proc2 = this.mProcessRecordAdjNodes.poll();
            while (proc2 != null) {
                this.mTmpOomAdjusterArgs.mApp = proc2;
                this.mComputeConnectionsConsumer.accept(this.mTmpOomAdjusterArgs);
                proc2 = this.mProcessRecordAdjNodes.poll();
            }
            return;
        }
        int end = this.mProcessRecordProcStateNodes.size() - 1;
        for (int i = 0; i < end; i++) {
            this.mProcessRecordProcStateNodes.forEachNewNode(i, this.mComputeConnectionsConsumer);
        }
        int end2 = this.mProcessRecordAdjNodes.size() - 1;
        for (int i2 = 0; i2 < end2; i2++) {
            this.mProcessRecordAdjNodes.forEachNewNode(i2, this.mComputeConnectionsConsumer);
        }
    }

    private void partialUpdateLSP(int oomAdjReason, android.util.ArraySet<com.android.server.am.ProcessRecord> targets) {
        com.android.server.am.ProcessRecord topApp = this.mService.getTopApp();
        long now = this.mInjector.getUptimeMillis();
        long nowElapsed = this.mInjector.getElapsedRealtimeMillis();
        long oldTime = now - this.mConstants.mMaxEmptyTimeMillis;
        com.android.server.am.ActiveUids activeUids = this.mTmpUidRecords;
        activeUids.clear();
        this.mTmpOomAdjusterArgs.update(topApp, now, 1001, oomAdjReason, activeUids, false);
        this.mAdjSeq++;
        java.util.ArrayList<com.android.server.am.ProcessRecord> reachables = this.mTmpProcessList;
        reachables.clear();
        int size = targets.size();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ProcessRecord target = (com.android.server.am.ProcessRecord) targets.valueAtUnchecked(i);
            target.mState.resetCachedInfo();
            target.mState.setReachable(true);
            reachables.add(target);
        }
        collectAndMarkReachableProcessesLSP(reachables);
        this.mProcessRecordProcStateNodes.resetLastNodes();
        initReachableStatesLSP(reachables, targets.size(), this.mTmpOomAdjusterArgs);
        this.mProcessRecordAdjNodes.resetLastNodes();
        computeConnectionsLSP();
        int size2 = reachables.size();
        boolean unassignedAdj = false;
        for (int i2 = 0; i2 < size2; i2++) {
            com.android.server.am.ProcessStateRecord state = reachables.get(i2).mState;
            state.setReachable(false);
            state.setCompletedAdjSeq(this.mAdjSeq);
            if (state.getCurAdj() >= 1001) {
                unassignedAdj = true;
            }
        }
        if (unassignedAdj) {
            assignCachedAdjIfNecessary(this.mProcessList.getLruProcessesLOSP());
        }
        int size3 = activeUids.size();
        for (int i3 = 0; i3 < size3; i3++) {
            com.android.server.am.UidRecord ur = activeUids.valueAt(i3);
            ur.reset();
            for (int j = ur.getNumOfProcs() - 1; j >= 0; j--) {
                com.android.server.am.ProcessRecord proc = ur.getProcessRecordByIndex(j);
                updateAppUidRecIfNecessaryLSP(proc);
            }
        }
        postUpdateOomAdjInnerLSP(oomAdjReason, activeUids, now, nowElapsed, oldTime, false);
    }

    private void collectAndMarkReachableProcessesLSP(java.util.ArrayList<com.android.server.am.ProcessRecord> reachables) {
        this.mReachableCollectingConsumer.init(reachables);
        for (int i = 0; i < reachables.size(); i++) {
            com.android.server.am.ProcessRecord pr = reachables.get(i);
            forEachConnectionLSP(pr, this.mReachableCollectingConsumer);
        }
    }

    private void initReachableStatesLSP(java.util.ArrayList<com.android.server.am.ProcessRecord> reachables, int targetCount, com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs args) {
        int i = 0;
        boolean initReachables = !com.android.server.am.Flags.skipUnimportantConnections();
        while (i < targetCount && !initReachables) {
            com.android.server.am.ProcessRecord target = reachables.get(i);
            int prevProcState = target.mState.getCurProcState();
            int prevAdj = target.mState.getCurRawAdj();
            int prevCapability = target.mState.getCurCapability();
            boolean prevShouldNotFreeze = target.mOptRecord.shouldNotFreeze();
            args.mApp = target;
            initReachables = initReachables | computeOomAdjIgnoringReachablesLSP(args) | selfImportanceLoweredLSP(target, prevProcState, prevAdj, prevCapability, prevShouldNotFreeze);
            updateProcStateSlot(target, prevProcState);
            updateAdjSlot(target, prevAdj);
            i++;
        }
        if (!initReachables) {
            return;
        }
        int size = reachables.size();
        while (i < size) {
            com.android.server.am.ProcessRecord reachable = reachables.get(i);
            int prevProcState2 = reachable.mState.getCurProcState();
            int prevAdj2 = reachable.mState.getCurRawAdj();
            args.mApp = reachable;
            computeOomAdjIgnoringReachablesLSP(args);
            if (com.android.server.am.Flags.simplifyProcessTraversal()) {
                this.mProcessRecordProcStateNodes.offer(reachable);
            } else {
                updateProcStateSlot(reachable, prevProcState2);
                updateAdjSlot(reachable, prevAdj2);
            }
            i++;
        }
    }

    private boolean computeOomAdjIgnoringReachablesLSP(com.android.server.am.OomAdjusterModernImpl.OomAdjusterArgs args) {
        com.android.server.am.ProcessRecord app = args.mApp;
        com.android.server.am.ProcessRecord topApp = args.mTopApp;
        long now = args.mNow;
        int oomAdjReason = args.mOomAdjReason;
        computeOomAdjLSP(app, 1001, topApp, false, now, false, false, oomAdjReason, false);
        this.mComputeConnectionIgnoringReachableClientsConsumer.init(args);
        forEachClientConnectionLSP(app, this.mComputeConnectionIgnoringReachableClientsConsumer);
        return this.mComputeConnectionIgnoringReachableClientsConsumer.hasReachableClient;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void forEachConnectionLSP(com.android.server.am.ProcessRecord app, java.util.function.BiConsumer<com.android.server.am.OomAdjusterModernImpl.Connection, com.android.server.am.ProcessRecord> connectionConsumer) {
        com.android.server.am.ProcessServiceRecord psr = app.mServices;
        int i = psr.numberOfConnections();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            com.android.server.am.ConnectionRecord cr = psr.getConnectionAt(i);
            com.android.server.am.ProcessRecord service = cr.hasFlag(2) ? cr.binding.service.isolationHostProc : cr.binding.service.app;
            if (service != null && service != app && ((service.mState.getMaxAdj() < -900 || service.mState.getMaxAdj() >= 0) && ((service.mState.getCurAdj() > 0 || service.mState.getCurrentSchedulingGroup() <= 0 || service.mState.getCurProcState() > 2) && (!service.isSdkSandbox || cr.binding.attributedClient == null)))) {
                connectionConsumer.accept(cr, service);
            }
        }
        int i2 = psr.numberOfSdkSandboxConnections();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            com.android.server.am.ConnectionRecord cr2 = psr.getSdkSandboxConnectionAt(i3);
            com.android.server.am.ProcessRecord service2 = cr2.binding.service.app;
            if (service2 != null && service2 != app && ((service2.mState.getMaxAdj() < -900 || service2.mState.getMaxAdj() >= 0) && (service2.mState.getCurAdj() > 0 || service2.mState.getCurrentSchedulingGroup() <= 0 || service2.mState.getCurProcState() > 2))) {
                connectionConsumer.accept(cr2, service2);
            }
        }
        com.android.server.am.ProcessProviderRecord ppr = app.mProviders;
        for (int i4 = ppr.numberOfProviderConnections() - 1; i4 >= 0; i4--) {
            com.android.server.am.ContentProviderConnection cpc = ppr.getProviderConnectionAt(i4);
            com.android.server.am.ProcessRecord provider = cpc.provider.proc;
            if (provider != null && provider != app && ((provider.mState.getMaxAdj() < -900 || provider.mState.getMaxAdj() >= 0) && (provider.mState.getCurAdj() > 0 || provider.mState.getCurrentSchedulingGroup() <= 0 || provider.mState.getCurProcState() > 2))) {
                connectionConsumer.accept(cpc, provider);
            }
        }
    }

    private static void forEachClientConnectionLSP(com.android.server.am.ProcessRecord app, java.util.function.BiConsumer<com.android.server.am.OomAdjusterModernImpl.Connection, com.android.server.am.ProcessRecord> connectionConsumer) {
        com.android.server.am.ProcessRecord client;
        com.android.server.am.ProcessServiceRecord psr = app.mServices;
        for (int i = psr.numberOfRunningServices() - 1; i >= 0; i--) {
            com.android.server.am.ServiceRecord s = psr.getRunningServiceAt(i);
            android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.am.ConnectionRecord>> serviceConnections = s.getConnections();
            for (int j = serviceConnections.size() - 1; j >= 0; j--) {
                java.util.ArrayList<com.android.server.am.ConnectionRecord> clist = serviceConnections.valueAt(j);
                for (int k = clist.size() - 1; k >= 0; k--) {
                    com.android.server.am.ConnectionRecord cr = clist.get(k);
                    if (app.isSdkSandbox && cr.binding.attributedClient != null) {
                        client = cr.binding.attributedClient;
                    } else {
                        client = cr.binding.client;
                    }
                    if (client != null && client != app) {
                        connectionConsumer.accept(cr, client);
                    }
                }
            }
        }
        com.android.server.am.ProcessProviderRecord ppr = app.mProviders;
        for (int i2 = ppr.numberOfProviders() - 1; i2 >= 0; i2--) {
            com.android.server.am.ContentProviderRecord cpr = ppr.getProviderAt(i2);
            for (int j2 = cpr.connections.size() - 1; j2 >= 0; j2--) {
                com.android.server.am.ContentProviderConnection conn = cpr.connections.get(j2);
                connectionConsumer.accept(conn, conn.client);
            }
        }
    }

    private static boolean selfImportanceLoweredLSP(com.android.server.am.ProcessRecord app, int prevProcState, int prevAdj, int prevCapability, boolean prevShouldNotFreeze) {
        if (app.mState.getCurProcState() <= prevProcState && app.mState.getCurRawAdj() <= prevAdj && (app.mState.getCurCapability() & prevCapability) == prevCapability) {
            return !app.mOptRecord.shouldNotFreeze() && prevShouldNotFreeze;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean unimportantConnectionLSP(com.android.server.am.OomAdjusterModernImpl.Connection conn, com.android.server.am.ProcessRecord host, com.android.server.am.ProcessRecord client) {
        if (!com.android.server.am.Flags.skipUnimportantConnections() || host.mState.getCurProcState() > client.mState.getCurProcState() || host.mState.getCurRawAdj() > client.mState.getCurRawAdj()) {
            return false;
        }
        int serviceCapability = host.mState.getCurCapability();
        int clientCapability = client.mState.getCurCapability();
        if ((serviceCapability & clientCapability) == clientCapability || !(((clientCapability & 16) == 16 && (serviceCapability & 16) == 0) || conn.canAffectCapabilities())) {
            return host.mOptRecord.shouldNotFreeze() || !client.mOptRecord.shouldNotFreeze();
        }
        return false;
    }
}
