package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public final class ImeTrackerService extends com.android.internal.inputmethod.IImeTracker.Stub {
    private static final java.lang.String TAG = "ImeTracker";
    private static final long TIMEOUT_MS = 10000;
    private final android.os.Handler mHandler;
    private final com.android.server.inputmethod.ImeTrackerService.History mHistory = new com.android.server.inputmethod.ImeTrackerService.History();
    private final java.lang.Object mLock = new java.lang.Object();

    ImeTrackerService(android.os.Looper looper) {
        this.mHandler = new android.os.Handler(looper, null, true);
    }

    public android.view.inputmethod.ImeTracker.Token onStart(java.lang.String tag, int uid, int type, int origin, int reason, boolean fromUser) {
        android.os.Binder binder = new android.os.Binder();
        final android.view.inputmethod.ImeTracker.Token token = new android.view.inputmethod.ImeTracker.Token(binder, tag);
        com.android.server.inputmethod.ImeTrackerService.History.Entry entry = new com.android.server.inputmethod.ImeTrackerService.History.Entry(tag, uid, type, 1, origin, reason, fromUser);
        synchronized (this.mLock) {
            this.mHistory.addEntry(binder, entry);
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.inputmethod.ImeTrackerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStart$0(token);
                }
            }, 10000L);
        }
        return token;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(android.view.inputmethod.ImeTracker.Token token) {
        synchronized (this.mLock) {
            this.mHistory.setFinished(token, 5, 0);
        }
    }

    public void onProgress(android.os.IBinder binder, int phase) {
        synchronized (this.mLock) {
            com.android.server.inputmethod.ImeTrackerService.History.Entry entry = this.mHistory.getEntry(binder);
            if (entry == null) {
                return;
            }
            entry.mPhase = phase;
        }
    }

    public void onFailed(android.view.inputmethod.ImeTracker.Token statsToken, int phase) {
        synchronized (this.mLock) {
            this.mHistory.setFinished(statsToken, 3, phase);
        }
    }

    public void onCancelled(android.view.inputmethod.ImeTracker.Token statsToken, int phase) {
        synchronized (this.mLock) {
            this.mHistory.setFinished(statsToken, 2, phase);
        }
    }

    public void onShown(android.view.inputmethod.ImeTracker.Token statsToken) {
        synchronized (this.mLock) {
            this.mHistory.setFinished(statsToken, 4, 0);
        }
    }

    public void onHidden(android.view.inputmethod.ImeTracker.Token statsToken) {
        synchronized (this.mLock) {
            this.mHistory.setFinished(statsToken, 4, 0);
        }
    }

    public void onDispatched(android.view.inputmethod.ImeTracker.Token statsToken) {
        synchronized (this.mLock) {
            this.mHistory.setFinished(statsToken, 4, 0);
        }
    }

    public void onImmsUpdate(android.view.inputmethod.ImeTracker.Token statsToken, java.lang.String requestWindowName) {
        synchronized (this.mLock) {
            com.android.server.inputmethod.ImeTrackerService.History.Entry entry = this.mHistory.getEntry(statsToken.getBinder());
            if (entry == null) {
                return;
            }
            entry.mRequestWindowName = requestWindowName;
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this.mLock) {
            this.mHistory.dump(pw, prefix);
        }
    }

    public boolean hasPendingImeVisibilityRequests() {
        boolean z;
        super.hasPendingImeVisibilityRequests_enforcePermission();
        synchronized (this.mLock) {
            z = !this.mHistory.mLiveEntries.isEmpty();
        }
        return z;
    }

    public void finishTrackingPendingImeVisibilityRequests(com.android.internal.infra.AndroidFuture completionSignal) {
        super.finishTrackingPendingImeVisibilityRequests_enforcePermission();
        try {
            synchronized (this.mLock) {
                this.mHistory.mLiveEntries.clear();
            }
            completionSignal.complete((java.lang.Object) null);
        } catch (java.lang.Throwable e) {
            completionSignal.completeExceptionally(e);
        }
    }

    private static final class History {
        private static final int CAPACITY = 100;
        private static final java.util.concurrent.atomic.AtomicInteger sSequenceNumber = new java.util.concurrent.atomic.AtomicInteger(0);
        private final java.util.ArrayDeque<com.android.server.inputmethod.ImeTrackerService.History.Entry> mEntries;
        private final java.util.WeakHashMap<android.os.IBinder, com.android.server.inputmethod.ImeTrackerService.History.Entry> mLiveEntries;

        private History() {
            this.mEntries = new java.util.ArrayDeque<>(100);
            this.mLiveEntries = new java.util.WeakHashMap<>();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addEntry(android.os.IBinder binder, com.android.server.inputmethod.ImeTrackerService.History.Entry entry) {
            this.mLiveEntries.put(binder, entry);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.inputmethod.ImeTrackerService.History.Entry getEntry(android.os.IBinder binder) {
            return this.mLiveEntries.get(binder);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setFinished(android.view.inputmethod.ImeTracker.Token statsToken, int status, int phase) {
            com.android.server.inputmethod.ImeTrackerService.History.Entry entry = this.mLiveEntries.remove(statsToken.getBinder());
            if (entry == null) {
                if (status != 5) {
                    android.util.Log.i(com.android.server.inputmethod.ImeTrackerService.TAG, statsToken.getTag() + ": setFinished on previously finished token at " + android.view.inputmethod.ImeTracker.Debug.phaseToString(phase) + " with " + android.view.inputmethod.ImeTracker.Debug.statusToString(status));
                    return;
                }
                return;
            }
            entry.mDuration = java.lang.System.currentTimeMillis() - entry.mStartTime;
            entry.mStatus = status;
            if (phase != 0) {
                entry.mPhase = phase;
            }
            if (status == 5) {
                android.util.Log.i(com.android.server.inputmethod.ImeTrackerService.TAG, statsToken.getTag() + ": setFinished at " + android.view.inputmethod.ImeTracker.Debug.phaseToString(entry.mPhase) + " with " + android.view.inputmethod.ImeTracker.Debug.statusToString(status));
            }
            while (this.mEntries.size() >= 100) {
                this.mEntries.remove();
            }
            this.mEntries.offer(entry);
            com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.IME_REQUEST_FINISHED, entry.mUid, entry.mDuration, entry.mType, entry.mStatus, entry.mReason, entry.mOrigin, entry.mPhase, entry.mFromUser);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US).withZone(java.time.ZoneId.systemDefault());
            pw.print(prefix);
            pw.println("mLiveEntries: " + this.mLiveEntries.size() + " elements");
            for (com.android.server.inputmethod.ImeTrackerService.History.Entry entry : this.mLiveEntries.values()) {
                dumpEntry(entry, pw, prefix + "  ", formatter);
            }
            pw.print(prefix);
            pw.println("mEntries: " + this.mEntries.size() + " elements");
            for (com.android.server.inputmethod.ImeTrackerService.History.Entry entry2 : this.mEntries) {
                dumpEntry(entry2, pw, prefix + "  ", formatter);
            }
        }

        private void dumpEntry(com.android.server.inputmethod.ImeTrackerService.History.Entry entry, java.io.PrintWriter pw, java.lang.String prefix, java.time.format.DateTimeFormatter formatter) {
            pw.print(prefix);
            pw.print("#" + entry.mSequenceNumber);
            pw.print(" " + android.view.inputmethod.ImeTracker.Debug.typeToString(entry.mType));
            pw.print(" - " + android.view.inputmethod.ImeTracker.Debug.statusToString(entry.mStatus));
            pw.print(" - " + entry.mTag);
            pw.println(" (" + entry.mDuration + "ms):");
            pw.print(prefix);
            pw.print("  startTime=" + formatter.format(java.time.Instant.ofEpochMilli(entry.mStartTime)));
            pw.println(" " + android.view.inputmethod.ImeTracker.Debug.originToString(entry.mOrigin));
            pw.print(prefix);
            pw.print("  reason=" + com.android.internal.inputmethod.InputMethodDebug.softInputDisplayReasonToString(entry.mReason));
            pw.println(" " + android.view.inputmethod.ImeTracker.Debug.phaseToString(entry.mPhase));
            pw.print(prefix);
            pw.println("  requestWindowName=" + entry.mRequestWindowName);
        }

        private static final class Entry {
            private long mDuration;
            private final boolean mFromUser;
            private final int mOrigin;
            private int mPhase;
            private final int mReason;
            private java.lang.String mRequestWindowName;
            private final int mSequenceNumber;
            private final long mStartTime;
            private int mStatus;
            private final java.lang.String mTag;
            private final int mType;
            private final int mUid;

            private Entry(java.lang.String tag, int uid, int type, int status, int origin, int reason, boolean fromUser) {
                this.mSequenceNumber = com.android.server.inputmethod.ImeTrackerService.History.sSequenceNumber.getAndIncrement();
                this.mStartTime = java.lang.System.currentTimeMillis();
                this.mDuration = 0L;
                this.mPhase = 0;
                this.mRequestWindowName = "not set";
                this.mTag = tag;
                this.mUid = uid;
                this.mType = type;
                this.mStatus = status;
                this.mOrigin = origin;
                this.mReason = reason;
                this.mFromUser = fromUser;
            }
        }
    }
}
