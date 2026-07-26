package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class ThreadPriorityBooster {
    private static final boolean ENABLE_LOCK_GUARD = false;
    private static final int PRIORITY_NOT_ADJUSTED = Integer.MAX_VALUE;
    private volatile int mBoostToPriority;
    private final int mLockGuardIndex;
    public com.android.server.IThreadPriorityBoosterExt mThreadPriorityBoosterExt = (com.android.server.IThreadPriorityBoosterExt) system.ext.loader.core.ExtLoader.type(com.android.server.IThreadPriorityBoosterExt.class).base(this).create();
    private final java.lang.ThreadLocal<com.android.server.ThreadPriorityBooster.PriorityState> mThreadState = new java.lang.ThreadLocal<com.android.server.ThreadPriorityBooster.PriorityState>() { // from class: com.android.server.ThreadPriorityBooster.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public com.android.server.ThreadPriorityBooster.PriorityState initialValue() {
            return new com.android.server.ThreadPriorityBooster.PriorityState();
        }
    };

    public ThreadPriorityBooster(int boostToPriority, int lockGuardIndex) {
        this.mBoostToPriority = boostToPriority;
        this.mLockGuardIndex = lockGuardIndex;
    }

    public void boost() {
        int prevPriority;
        com.android.server.ThreadPriorityBooster.PriorityState state = this.mThreadState.get();
        if (state.regionCounter == 0 && !this.mThreadPriorityBoosterExt.setLockOwnerThreadBoost(state.tid, true) && (prevPriority = android.os.Process.getThreadPriority(state.tid)) > this.mBoostToPriority) {
            android.os.Process.setThreadPriority(state.tid, this.mBoostToPriority);
            state.prevPriority = prevPriority;
        }
        state.regionCounter++;
    }

    public void reset() {
        com.android.server.ThreadPriorityBooster.PriorityState state = this.mThreadState.get();
        state.regionCounter--;
        if ((state.regionCounter != 0 || !this.mThreadPriorityBoosterExt.setLockOwnerThreadBoost(state.tid, false)) && state.regionCounter == 0 && state.prevPriority != Integer.MAX_VALUE) {
            android.os.Process.setThreadPriority(state.tid, state.prevPriority);
            state.prevPriority = Integer.MAX_VALUE;
        }
    }

    protected void setBoostToPriority(int priority) {
        this.mBoostToPriority = priority;
        com.android.server.ThreadPriorityBooster.PriorityState state = this.mThreadState.get();
        if (state.regionCounter != 0) {
            int prevPriority = android.os.Process.getThreadPriority(state.tid);
            if (prevPriority != priority) {
                android.os.Process.setThreadPriority(state.tid, priority);
            }
        }
    }

    private static class PriorityState {
        int prevPriority;
        int regionCounter;
        final int tid;

        private PriorityState() {
            this.tid = android.os.Process.myTid();
            this.prevPriority = Integer.MAX_VALUE;
        }
    }
}
