package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class BLASTSyncEngine {
    public static final int METHOD_BLAST = 1;
    public static final int METHOD_NONE = 0;
    public static final int METHOD_UNDEFINED = -1;
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String TAG = "BLASTSyncEngine";
    private final java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> mActiveSyncs;
    private com.android.server.wm.IBLASTSyncEngineExt mBLASTSyncEngineExt;
    private final android.os.Handler mHandler;
    private int mNextSyncId;
    private final java.util.ArrayList<java.lang.Runnable> mOnIdleListeners;
    private final java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.PendingSyncSet> mPendingSyncSets;
    private final java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> mTmpFinishQueue;
    private final java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> mTmpFringe;
    private final com.android.server.wm.WindowManagerService mWm;

    interface TransactionReadyListener {
        void onTransactionReady(int i, android.view.SurfaceControl.Transaction transaction);

        default void onPreReady(int mSyncId) {
        }

        default void onTransactionCommitTimeout() {
        }

        default void onReadyTimeout() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class PendingSyncSet {
        private java.lang.Runnable mApplySync;
        private java.lang.Runnable mStartSync;

        private PendingSyncSet() {
        }
    }

    class SyncGroup {
        private static final java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> NO_DEPENDENCIES = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> mDependencies;
        boolean mIgnoreIndirectMembers;
        final com.android.server.wm.BLASTSyncEngine.TransactionReadyListener mListener;
        final java.lang.Runnable mOnTimeout;
        private android.view.SurfaceControl.Transaction mOrphanTransaction;
        boolean mReady;
        final android.util.ArraySet<com.android.server.wm.WindowContainer> mRootMembers;
        final int mSyncId;
        int mSyncMethod;
        final java.lang.String mSyncName;
        private java.lang.String mTraceName;

        private SyncGroup(com.android.server.wm.BLASTSyncEngine.TransactionReadyListener listener, int id, java.lang.String name) {
            this.mSyncMethod = 1;
            this.mReady = false;
            this.mRootMembers = new android.util.ArraySet<>();
            this.mOrphanTransaction = null;
            this.mIgnoreIndirectMembers = false;
            this.mDependencies = NO_DEPENDENCIES;
            this.mSyncId = id;
            this.mSyncName = name;
            this.mListener = listener;
            this.mOnTimeout = new java.lang.Runnable() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$0();
                }
            };
            if (android.os.Trace.isTagEnabled(32L)) {
                this.mTraceName = name + "SyncGroupReady";
                android.os.Trace.asyncTraceBegin(32L, this.mTraceName, id);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            android.util.Slog.w(com.android.server.wm.BLASTSyncEngine.TAG, "Sync group " + this.mSyncId + " timeout");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.BLASTSyncEngine.this.mWm.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    onTimeout();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        android.view.SurfaceControl.Transaction getOrphanTransaction() {
            if (this.mOrphanTransaction == null) {
                this.mOrphanTransaction = com.android.server.wm.BLASTSyncEngine.this.mWm.mTransactionFactory.get();
            }
            return this.mOrphanTransaction;
        }

        boolean isIgnoring(com.android.server.wm.WindowContainer wc) {
            return this.mIgnoreIndirectMembers && wc.asWindowState() == null && wc.mSyncGroup != this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean tryFinish() {
            if (!this.mReady) {
                return false;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                long protoLogParam0 = this.mSyncId;
                java.lang.String protoLogParam1 = java.lang.String.valueOf(this.mRootMembers);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 495867940519492701L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
            }
            if (!this.mDependencies.isEmpty()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                    long protoLogParam02 = this.mSyncId;
                    java.lang.String protoLogParam12 = java.lang.String.valueOf(this.mDependencies);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 8452501904614439940L, 1, null, java.lang.Long.valueOf(protoLogParam02), protoLogParam12);
                }
                return false;
            }
            if (com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.tryFinishAheadIfNeed(this.mSyncId, this, this.mRootMembers)) {
                finishNow();
                return true;
            }
            for (int i = this.mRootMembers.size() - 1; i >= 0; i--) {
                com.android.server.wm.WindowContainer wc = this.mRootMembers.valueAt(i);
                if (!wc.isSyncFinished(this)) {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                        long protoLogParam03 = this.mSyncId;
                        java.lang.String protoLogParam13 = java.lang.String.valueOf(wc);
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 616739530932040800L, 1, null, java.lang.Long.valueOf(protoLogParam03), protoLogParam13);
                    }
                    if (!com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.shouldSkipSyncFinishCheck(this.mSyncId, wc)) {
                        return false;
                    }
                } else if (com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.onSurfacePlacement(this.mSyncId, wc, this.mRootMembers)) {
                    return false;
                }
            }
            if (com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.shouldSkipFinishNowForQuickLaunch(this.mSyncId, this.mRootMembers)) {
                return false;
            }
            finishNow();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finishNow() {
            if (this.mTraceName != null) {
                android.os.Trace.asyncTraceEnd(32L, this.mTraceName, this.mSyncId);
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                long protoLogParam0 = this.mSyncId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 6649777898123506907L, 1, null, java.lang.Long.valueOf(protoLogParam0));
            }
            android.view.SurfaceControl.Transaction merged = com.android.server.wm.BLASTSyncEngine.this.mWm.mTransactionFactory.get();
            if (this.mOrphanTransaction != null) {
                merged.merge(this.mOrphanTransaction);
            }
            java.util.Iterator<com.android.server.wm.WindowContainer> it = this.mRootMembers.iterator();
            while (it.hasNext()) {
                it.next().finishSync(merged, this, false);
            }
            android.util.ArraySet<com.android.server.wm.WindowContainer> wcAwaitingCommit = new android.util.ArraySet<>();
            for (com.android.server.wm.WindowContainer wc : this.mRootMembers) {
                wc.waitForSyncTransactionCommit(wcAwaitingCommit);
                com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.showStartingSurface(wc.asActivityRecord(), merged);
            }
            int i = this.mSyncId;
            long mergedTxId = merged.getId();
            java.lang.String syncName = this.mSyncName;
            final com.android.server.wm.BLASTSyncEngine.SyncGroup.C1CommitCallback callback = new com.android.server.wm.BLASTSyncEngine.SyncGroup.C1CommitCallback(wcAwaitingCommit, syncName, mergedTxId, merged);
            merged.addTransactionCommittedListener(new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new android.view.SurfaceControl.TransactionCommittedListener() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda0
                @Override // android.view.SurfaceControl.TransactionCommittedListener
                public final void onTransactionCommitted() {
                    callback.onCommitted(new android.view.SurfaceControl.Transaction());
                }
            });
            com.android.server.wm.BLASTSyncEngine.this.mHandler.postDelayed(callback, 5000L);
            android.os.Trace.traceBegin(32L, "onTransactionReady");
            this.mListener.onTransactionReady(this.mSyncId, merged);
            android.os.Trace.traceEnd(32L);
            com.android.server.wm.BLASTSyncEngine.this.mActiveSyncs.remove(this);
            com.android.server.wm.BLASTSyncEngine.this.mHandler.removeCallbacks(this.mOnTimeout);
            if (com.android.server.wm.BLASTSyncEngine.this.mActiveSyncs.size() == 0 && !com.android.server.wm.BLASTSyncEngine.this.mPendingSyncSets.isEmpty()) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 4174320302463990554L, 0, null, null);
                }
                final com.android.server.wm.BLASTSyncEngine.PendingSyncSet pt = (com.android.server.wm.BLASTSyncEngine.PendingSyncSet) com.android.server.wm.BLASTSyncEngine.this.mPendingSyncSets.remove(0);
                pt.mStartSync.run();
                if (com.android.server.wm.BLASTSyncEngine.this.mActiveSyncs.size() != 0) {
                    com.android.server.wm.BLASTSyncEngine.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$finishNow$2(pt);
                        }
                    });
                } else {
                    throw new java.lang.IllegalStateException("Pending Sync Set didn't start a sync.");
                }
            }
            for (int i2 = com.android.server.wm.BLASTSyncEngine.this.mOnIdleListeners.size() - 1; i2 >= 0 && com.android.server.wm.BLASTSyncEngine.this.mActiveSyncs.size() <= 0; i2--) {
                ((java.lang.Runnable) com.android.server.wm.BLASTSyncEngine.this.mOnIdleListeners.get(i2)).run();
            }
        }

        /* JADX INFO: renamed from: com.android.server.wm.BLASTSyncEngine$SyncGroup$1CommitCallback, reason: invalid class name */
        class C1CommitCallback implements java.lang.Runnable {
            final int syncId;
            final /* synthetic */ android.view.SurfaceControl.Transaction val$merged;
            final /* synthetic */ long val$mergedTxId;
            final /* synthetic */ java.lang.String val$syncName;
            final /* synthetic */ android.util.ArraySet val$wcAwaitingCommit;
            boolean ran = false;
            boolean timeout = false;

            C1CommitCallback(android.util.ArraySet arraySet, java.lang.String str, long j, android.view.SurfaceControl.Transaction transaction) {
                this.val$wcAwaitingCommit = arraySet;
                this.val$syncName = str;
                this.val$mergedTxId = j;
                this.val$merged = transaction;
                this.syncId = com.android.server.wm.BLASTSyncEngine.SyncGroup.this.mSyncId;
            }

            public void onCommitted(android.view.SurfaceControl.Transaction t) {
                com.android.server.wm.BLASTSyncEngine.this.mHandler.removeCallbacks(this);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.BLASTSyncEngine.this.mWm.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "onCommitted syncId: " + this.syncId + ", timeout: " + this.timeout + "ran: " + this.ran);
                        if (this.ran) {
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        this.ran = true;
                        com.android.server.wm.BLASTSyncEngine.this.mWm.mAtmService.mTaskSupervisor.getWrapper().getExtImpl().markTransitionCommit(com.android.server.wm.BLASTSyncEngine.SyncGroup.this.mSyncId);
                        for (com.android.server.wm.WindowContainer wc : this.val$wcAwaitingCommit) {
                            wc.onSyncTransactionCommitted(t);
                        }
                        t.apply();
                        this.val$wcAwaitingCommit.clear();
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                android.os.Trace.traceBegin(32L, "onTransactionCommitTimeout");
                this.timeout = true;
                android.util.Slog.e(com.android.server.wm.BLASTSyncEngine.TAG, "WM sent Transaction (#" + this.syncId + ", " + this.val$syncName + ", tx=" + this.val$mergedTxId + ") to organizer, but never received commit callback. Application ANR likely to follow.");
                android.os.Trace.traceEnd(32L);
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.BLASTSyncEngine.this.mWm.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.syncTransitionCommitTimeout(this.val$wcAwaitingCommit, com.android.server.wm.BLASTSyncEngine.SyncGroup.this.mSyncId, this.val$merged);
                        com.android.server.wm.BLASTSyncEngine.SyncGroup.this.mListener.onTransactionCommitTimeout();
                        onCommitted(this.val$merged.mNativeObject != 0 ? this.val$merged : com.android.server.wm.BLASTSyncEngine.this.mWm.mTransactionFactory.get());
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$finishNow$2(com.android.server.wm.BLASTSyncEngine.PendingSyncSet pt) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.BLASTSyncEngine.this.mWm.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    pt.mApplySync.run();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setReady(boolean ready) {
            if (this.mReady == ready) {
                return false;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                long protoLogParam0 = this.mSyncId;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, 6310906192788668020L, 13, null, java.lang.Long.valueOf(protoLogParam0), java.lang.Boolean.valueOf(ready));
            }
            this.mReady = ready;
            if (com.android.server.wm.BLASTSyncEngine.PANIC_DEBUG) {
                android.util.Slog.e(com.android.server.wm.BLASTSyncEngine.TAG, "setReady " + ready + " debug " + android.os.Debug.getCallers(10));
            }
            if (ready) {
                com.android.server.wm.BLASTSyncEngine.this.mWm.mWindowPlacerLocked.requestTraversal();
            }
            return true;
        }

        public void setReadyForStartingSurface() {
            if (this.mReady) {
                return;
            }
            this.mListener.onPreReady(this.mSyncId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToSync(com.android.server.wm.WindowContainer wc) {
            if (this.mRootMembers.contains(wc)) {
                return;
            }
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
                long protoLogParam0 = this.mSyncId;
                java.lang.String protoLogParam1 = java.lang.String.valueOf(wc);
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -476337038362199951L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1);
            }
            com.android.server.wm.BLASTSyncEngine.SyncGroup dependency = wc.getSyncGroup();
            if (dependency != null && dependency != this && !dependency.isIgnoring(wc)) {
                android.util.Slog.w(com.android.server.wm.BLASTSyncEngine.TAG, "SyncGroup " + this.mSyncId + " conflicts with " + dependency.mSyncId + ": Making " + this.mSyncId + " depend on " + dependency.mSyncId + ", wc=" + wc);
                if (com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.skipAddToSync(wc, this, dependency)) {
                    return;
                }
                if (!this.mDependencies.contains(dependency)) {
                    if (dependency.dependsOn(this)) {
                        android.util.Slog.w(com.android.server.wm.BLASTSyncEngine.TAG, " Detected dependency cycle between " + this.mSyncId + " and " + dependency.mSyncId + ": Moving " + wc + " to " + this.mSyncId);
                        if (wc.mSyncGroup == null) {
                            wc.setSyncGroup(this);
                        } else {
                            wc.mSyncGroup.mRootMembers.remove(wc);
                            this.mRootMembers.add(wc);
                            wc.mSyncGroup = this;
                        }
                    } else {
                        if (this.mDependencies == NO_DEPENDENCIES) {
                            this.mDependencies = new java.util.ArrayList<>();
                        }
                        this.mDependencies.add(dependency);
                    }
                }
            } else {
                this.mRootMembers.add(wc);
                wc.setSyncGroup(this);
            }
            wc.prepareSync();
            if (this.mReady) {
                com.android.server.wm.BLASTSyncEngine.this.mWm.mWindowPlacerLocked.requestTraversal();
            }
            if (!this.mReady && (wc instanceof com.android.server.wm.ActivityRecord) && com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.shouldSetReady((com.android.server.wm.ActivityRecord) wc)) {
                android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "set mReady true as " + wc + " isShowStartingSurface");
                this.mListener.onPreReady(this.mSyncId);
            }
        }

        private boolean dependsOn(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
            if (this.mDependencies.isEmpty()) {
                return false;
            }
            java.util.ArrayList<com.android.server.wm.BLASTSyncEngine.SyncGroup> fringe = com.android.server.wm.BLASTSyncEngine.this.mTmpFringe;
            fringe.clear();
            fringe.add(this);
            for (int head = 0; head < fringe.size(); head++) {
                com.android.server.wm.BLASTSyncEngine.SyncGroup next = fringe.get(head);
                if (next == group) {
                    fringe.clear();
                    return true;
                }
                for (int i = 0; i < next.mDependencies.size(); i++) {
                    if (!fringe.contains(next.mDependencies.get(i))) {
                        fringe.add(next.mDependencies.get(i));
                    }
                }
            }
            fringe.clear();
            return false;
        }

        void onCancelSync(com.android.server.wm.WindowContainer wc) {
            this.mRootMembers.remove(wc);
        }

        private void onTimeout() {
            if (com.android.server.wm.BLASTSyncEngine.this.mActiveSyncs.contains(this)) {
                boolean allFinished = true;
                int i = this.mRootMembers.size();
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    }
                    com.android.server.wm.WindowContainer<?> wc = this.mRootMembers.valueAt(i);
                    if (wc != null && !wc.isSyncFinished(this)) {
                        allFinished = false;
                        android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "Unfinished container: " + wc);
                        com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.logOutUnfinishedcontainerInfo(this, wc);
                        wc.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda3
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                com.android.server.wm.BLASTSyncEngine.SyncGroup.lambda$onTimeout$4((com.android.server.wm.ActivityRecord) obj);
                            }
                        });
                    }
                }
                for (int i2 = this.mDependencies.size() - 1; i2 >= 0; i2--) {
                    allFinished = false;
                    android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "Unfinished dependency: " + this.mDependencies.get(i2).mSyncId);
                }
                if (allFinished && !this.mReady) {
                    android.util.Slog.w(com.android.server.wm.BLASTSyncEngine.TAG, "Sync group " + this.mSyncId + " timed-out because not ready. If you see this, please file a bug.");
                    this.mListener.onReadyTimeout();
                }
                com.android.server.wm.BLASTSyncEngine.this.mBLASTSyncEngineExt.onTimeout(com.android.server.wm.BLASTSyncEngine.this.mWm, this);
                finishNow();
                com.android.server.wm.BLASTSyncEngine.this.removeFromDependencies(this);
            }
        }

        static /* synthetic */ void lambda$onTimeout$4(com.android.server.wm.ActivityRecord a) {
            if (a.isVisibleRequested()) {
                if (a.isRelaunching()) {
                    android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "  " + a + " is relaunching");
                }
                a.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.BLASTSyncEngine$SyncGroup$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wm.WindowState windowState = (com.android.server.wm.WindowState) obj;
                        android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "  " + windowState + " " + windowState.mWinAnimator.drawStateToString());
                    }
                }, true);
            } else if (a.mDisplayContent != null && !a.mDisplayContent.mUnknownAppVisibilityController.allResolved()) {
                android.util.Slog.i(com.android.server.wm.BLASTSyncEngine.TAG, "  UnknownAppVisibility: " + a.mDisplayContent.mUnknownAppVisibilityController.getDebugMessage());
            }
        }
    }

    BLASTSyncEngine(com.android.server.wm.WindowManagerService wms) {
        this(wms, wms.mH);
    }

    BLASTSyncEngine(com.android.server.wm.WindowManagerService wms, android.os.Handler mainHandler) {
        this.mBLASTSyncEngineExt = (com.android.server.wm.IBLASTSyncEngineExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IBLASTSyncEngineExt.class).base(this).create();
        this.mNextSyncId = 0;
        this.mActiveSyncs = new java.util.ArrayList<>();
        this.mPendingSyncSets = new java.util.ArrayList<>();
        this.mOnIdleListeners = new java.util.ArrayList<>();
        this.mTmpFinishQueue = new java.util.ArrayList<>();
        this.mTmpFringe = new java.util.ArrayList<>();
        this.mWm = wms;
        this.mHandler = mainHandler;
    }

    com.android.server.wm.BLASTSyncEngine.SyncGroup prepareSyncSet(com.android.server.wm.BLASTSyncEngine.TransactionReadyListener listener, java.lang.String name) {
        int i = this.mNextSyncId;
        this.mNextSyncId = i + 1;
        return new com.android.server.wm.BLASTSyncEngine.SyncGroup(listener, i, name);
    }

    int startSyncSet(com.android.server.wm.BLASTSyncEngine.TransactionReadyListener listener, long timeoutMs, java.lang.String name, boolean parallel) {
        com.android.server.wm.BLASTSyncEngine.SyncGroup s = prepareSyncSet(listener, name);
        startSyncSet(s, this.mBLASTSyncEngineExt.adjustSyncTimeout(timeoutMs, s.mSyncId, this.mWm), parallel);
        return s.mSyncId;
    }

    void startSyncSet(com.android.server.wm.BLASTSyncEngine.SyncGroup s) {
        startSyncSet(s, 5000L, false);
    }

    void startSyncSet(com.android.server.wm.BLASTSyncEngine.SyncGroup s, long timeoutMs, boolean parallel) {
        boolean alreadyRunning = this.mActiveSyncs.size() > 0;
        if (!parallel && alreadyRunning) {
            android.util.Slog.e(TAG, "SyncGroup " + s.mSyncId + ": Started when there is other active SyncGroup");
        }
        this.mActiveSyncs.add(s);
        s.mIgnoreIndirectMembers = parallel;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_SYNC_ENGINE_enabled[1]) {
            long protoLogParam0 = s.mSyncId;
            java.lang.String protoLogParam1 = java.lang.String.valueOf((parallel && alreadyRunning) ? "(in parallel) " : "");
            java.lang.String protoLogParam2 = java.lang.String.valueOf(s.mListener);
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_SYNC_ENGINE, -2978812352001196863L, 1, null, java.lang.Long.valueOf(protoLogParam0), protoLogParam1, protoLogParam2);
        }
        scheduleTimeout(s, timeoutMs);
    }

    com.android.server.wm.BLASTSyncEngine.SyncGroup getSyncSet(int id) {
        for (int i = 0; i < this.mActiveSyncs.size(); i++) {
            if (this.mActiveSyncs.get(i).mSyncId == id) {
                return this.mActiveSyncs.get(i);
            }
        }
        return null;
    }

    boolean hasActiveSync() {
        return this.mActiveSyncs.size() != 0;
    }

    void scheduleTimeout(com.android.server.wm.BLASTSyncEngine.SyncGroup s, long timeoutMs) {
        this.mHandler.postDelayed(s.mOnTimeout, timeoutMs);
    }

    void addToSyncSet(int id, com.android.server.wm.WindowContainer wc) {
        getSyncGroup(id).addToSync(wc);
    }

    void setSyncMethod(int id, int method) {
        com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncGroup(id);
        if (!syncGroup.mRootMembers.isEmpty()) {
            throw new java.lang.IllegalStateException("Not allow to change sync method after adding group member, id=" + id);
        }
        syncGroup.mSyncMethod = method;
    }

    boolean setReady(int id, boolean ready) {
        return getSyncGroup(id).setReady(ready);
    }

    void setReady(int id) {
        setReady(id, true);
    }

    boolean isReady(int id) {
        return getSyncGroup(id).mReady;
    }

    void abort(int id) {
        com.android.server.wm.BLASTSyncEngine.SyncGroup group = getSyncGroup(id);
        group.finishNow();
        removeFromDependencies(group);
    }

    private com.android.server.wm.BLASTSyncEngine.SyncGroup getSyncGroup(int id) {
        com.android.server.wm.BLASTSyncEngine.SyncGroup syncGroup = getSyncSet(id);
        if (syncGroup == null) {
            throw new java.lang.IllegalStateException("SyncGroup is not started yet id=" + id);
        }
        return syncGroup;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromDependencies(com.android.server.wm.BLASTSyncEngine.SyncGroup group) {
        boolean anyChange = false;
        for (int i = 0; i < this.mActiveSyncs.size(); i++) {
            com.android.server.wm.BLASTSyncEngine.SyncGroup active = this.mActiveSyncs.get(i);
            if (active.mDependencies.remove(group) && active.mDependencies.isEmpty()) {
                anyChange = true;
            }
        }
        if (anyChange) {
            this.mWm.mWindowPlacerLocked.requestTraversal();
        }
    }

    void onSurfacePlacement() {
        if (this.mActiveSyncs.isEmpty()) {
            return;
        }
        this.mTmpFinishQueue.addAll(this.mActiveSyncs);
        int visitBounds = ((this.mActiveSyncs.size() + 1) * this.mActiveSyncs.size()) / 2;
        while (!this.mTmpFinishQueue.isEmpty()) {
            if (visitBounds <= 0) {
                android.util.Slog.e(TAG, "Trying to finish more syncs than theoretically possible. This should never happen. Most likely a dependency cycle wasn't detected.");
            }
            visitBounds--;
            com.android.server.wm.BLASTSyncEngine.SyncGroup group = this.mTmpFinishQueue.remove(0);
            int grpIdx = this.mActiveSyncs.indexOf(group);
            if (grpIdx >= 0 && group.tryFinish()) {
                int insertAt = 0;
                for (int i = 0; i < this.mActiveSyncs.size(); i++) {
                    com.android.server.wm.BLASTSyncEngine.SyncGroup active = this.mActiveSyncs.get(i);
                    if (active.mDependencies.remove(group) && i < grpIdx && active.mDependencies.isEmpty()) {
                        this.mTmpFinishQueue.add(insertAt, this.mActiveSyncs.get(i));
                        insertAt++;
                    }
                }
            }
        }
    }

    void tryFinishForTest(int syncId) {
        getSyncSet(syncId).tryFinish();
    }

    void queueSyncSet(java.lang.Runnable startSync, java.lang.Runnable applySync) {
        com.android.server.wm.BLASTSyncEngine.PendingSyncSet pt = new com.android.server.wm.BLASTSyncEngine.PendingSyncSet();
        pt.mStartSync = startSync;
        pt.mApplySync = applySync;
        this.mPendingSyncSets.add(pt);
    }

    boolean hasPendingSyncSets() {
        return !this.mPendingSyncSets.isEmpty();
    }

    void addOnIdleListener(java.lang.Runnable onIdleListener) {
        this.mOnIdleListeners.add(onIdleListener);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean dumpAll) {
        this.mBLASTSyncEngineExt.dump(pw, prefix, this, this.mActiveSyncs);
    }
}
