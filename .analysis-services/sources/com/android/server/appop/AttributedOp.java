package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
final class AttributedOp {
    private android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> mAccessEvents;
    private final com.android.server.appop.AppOpsService mAppOpsService;
    public com.android.server.appop.IAppOpsServiceExt mAppOpsServiceExt = (com.android.server.appop.IAppOpsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.appop.IAppOpsServiceExt.class).create();
    android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> mInProgressEvents;
    android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> mPausedInProgressEvents;
    private android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> mRejectEvents;
    public final com.android.server.appop.AppOpsService.Op parent;
    public final java.lang.String persistentDeviceId;
    public final java.lang.String tag;

    AttributedOp(com.android.server.appop.AppOpsService appOpsService, java.lang.String tag, java.lang.String persistentDeviceId, com.android.server.appop.AppOpsService.Op parent) {
        this.mAppOpsService = appOpsService;
        this.tag = tag;
        this.persistentDeviceId = persistentDeviceId;
        this.parent = parent;
    }

    public void accessed(int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, java.lang.String proxyDeviceId, int uidState, int flags) {
        long accessTime = java.lang.System.currentTimeMillis();
        accessed(accessTime, -1L, proxyUid, proxyPackageName, proxyAttributionTag, proxyDeviceId, uidState, flags);
        this.mAppOpsService.mHistoricalRegistry.incrementOpAccessedCount(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, uidState, flags, accessTime, 0, -1);
        this.mAppOpsServiceExt.notifyPermissionRecordAsUser(this.parent.packageName, this.parent.op, 0, uidState, this.parent.uid);
    }

    public void accessed(long noteTime, long duration, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, java.lang.String proxyDeviceId, int uidState, int flags) {
        long key = android.app.AppOpsManager.makeKey(uidState, flags);
        if (this.mAccessEvents == null) {
            this.mAccessEvents = new android.util.LongSparseArray<>(1);
        }
        android.app.AppOpsManager.OpEventProxyInfo proxyInfo = null;
        if (proxyUid != -1) {
            proxyInfo = this.mAppOpsService.mOpEventProxyInfoPool.acquire(proxyUid, proxyPackageName, proxyAttributionTag, proxyDeviceId);
        }
        android.app.AppOpsManager.NoteOpEvent existingEvent = this.mAccessEvents.get(key);
        if (existingEvent != null) {
            existingEvent.reinit(noteTime, duration, proxyInfo, this.mAppOpsService.mOpEventProxyInfoPool);
        } else {
            this.mAccessEvents.put(key, new android.app.AppOpsManager.NoteOpEvent(noteTime, duration, proxyInfo));
        }
    }

    public void rejected(int uidState, int flags) {
        rejected(java.lang.System.currentTimeMillis(), uidState, flags);
        this.mAppOpsService.mHistoricalRegistry.incrementOpRejected(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, uidState, flags);
        this.mAppOpsServiceExt.notifyPermissionRecordAsUser(this.parent.packageName, this.parent.op, 2, uidState, this.parent.uid);
    }

    public void rejected(long noteTime, int uidState, int flags) {
        long key = android.app.AppOpsManager.makeKey(uidState, flags);
        if (this.mRejectEvents == null) {
            this.mRejectEvents = new android.util.LongSparseArray<>(1);
        }
        android.app.AppOpsManager.NoteOpEvent existingEvent = this.mRejectEvents.get(key);
        if (existingEvent != null) {
            existingEvent.reinit(noteTime, -1L, (android.app.AppOpsManager.OpEventProxyInfo) null, this.mAppOpsService.mOpEventProxyInfoPool);
        } else {
            this.mRejectEvents.put(key, new android.app.AppOpsManager.NoteOpEvent(noteTime, -1L, (android.app.AppOpsManager.OpEventProxyInfo) null));
        }
    }

    public void started(android.os.IBinder clientId, int virtualDeviceId, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, java.lang.String proxyDeviceId, int uidState, int flags, int attributionFlags, int attributionChainId) throws android.os.RemoteException {
        startedOrPaused(clientId, virtualDeviceId, proxyUid, proxyPackageName, proxyAttributionTag, proxyDeviceId, uidState, flags, attributionFlags, attributionChainId, false, true);
    }

    private void startedOrPaused(android.os.IBinder clientId, int virtualDeviceId, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, java.lang.String proxyDeviceId, int uidState, int flags, int attributionFlags, int attributionChainId, boolean triggeredByUidStateChange, boolean isStarted) throws android.os.RemoteException {
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> arrayMap;
        com.android.server.appop.AttributedOp attributedOp;
        com.android.server.appop.AttributedOp.InProgressStartOpEvent event;
        if (!triggeredByUidStateChange && !this.parent.isRunning() && isStarted) {
            this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, virtualDeviceId, true, attributionFlags, attributionChainId);
        }
        if (isStarted && this.mInProgressEvents == null) {
            this.mInProgressEvents = new android.util.ArrayMap<>(1);
        } else if (!isStarted && this.mPausedInProgressEvents == null) {
            this.mPausedInProgressEvents = new android.util.ArrayMap<>(1);
        }
        if (!isStarted) {
            arrayMap = this.mPausedInProgressEvents;
        } else {
            arrayMap = this.mInProgressEvents;
        }
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> events = arrayMap;
        long startTime = java.lang.System.currentTimeMillis();
        com.android.server.appop.AttributedOp.InProgressStartOpEvent event2 = events.get(clientId);
        if (event2 == null) {
            com.android.server.appop.AttributedOp.InProgressStartOpEvent event3 = this.mAppOpsService.mInProgressStartOpEventPool.acquire(startTime, android.os.SystemClock.elapsedRealtime(), clientId, this.tag, virtualDeviceId, com.android.internal.util.function.pooled.PooledLambda.obtainRunnable(new java.util.function.BiConsumer() { // from class: com.android.server.appop.AttributedOp$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.appop.AppOpsService.onClientDeath((com.android.server.appop.AttributedOp) obj, (android.os.IBinder) obj2);
                }
            }, this, clientId), proxyUid, proxyPackageName, proxyAttributionTag, proxyDeviceId, uidState, flags, attributionFlags, attributionChainId);
            events.put(clientId, event3);
            event = event3;
            attributedOp = this;
        } else {
            if (uidState != event2.getUidState()) {
                attributedOp = this;
                attributedOp.onUidStateChanged(uidState);
            } else {
                attributedOp = this;
            }
            event = event2;
        }
        event.mNumUnfinishedStarts++;
        if (isStarted) {
            attributedOp.mAppOpsService.mHistoricalRegistry.incrementOpAccessedCount(attributedOp.parent.op, attributedOp.parent.uid, attributedOp.parent.packageName, attributedOp.tag, uidState, flags, startTime, attributionFlags, attributionChainId);
            attributedOp.mAppOpsServiceExt.notifyPermissionRecordAsUser(attributedOp.parent.packageName, attributedOp.parent.op, 1, uidState, attributedOp.parent.uid);
        }
    }

    public void doForAllInProgressStartOpEvents(java.util.function.Consumer<com.android.server.appop.AttributedOp.InProgressStartOpEvent> action) {
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> events = isPaused() ? this.mPausedInProgressEvents : this.mInProgressEvents;
        if (events == null) {
            return;
        }
        int numStartedOps = events.size();
        android.util.ArraySet<android.os.IBinder> keys = new android.util.ArraySet<>(events.keySet());
        for (int i = 0; i < numStartedOps; i++) {
            action.accept(events.get(keys.valueAt(i)));
        }
    }

    public void finished(android.os.IBinder clientId) {
        finished(clientId, false);
    }

    private void finished(android.os.IBinder clientId, boolean triggeredByUidStateChange) {
        finishOrPause(clientId, triggeredByUidStateChange, false);
    }

    private void finishOrPause(android.os.IBinder clientId, boolean triggeredByUidStateChange, boolean isPausing) {
        int indexOfToken = isRunning() ? this.mInProgressEvents.indexOfKey(clientId) : -1;
        if (indexOfToken < 0) {
            finishPossiblyPaused(clientId, isPausing);
            return;
        }
        com.android.server.appop.AttributedOp.InProgressStartOpEvent event = this.mInProgressEvents.valueAt(indexOfToken);
        if (!isPausing) {
            event.mNumUnfinishedStarts--;
        }
        if (event.mNumUnfinishedStarts == 0 || isPausing) {
            if (!isPausing) {
                event.finish();
                this.mInProgressEvents.removeAt(indexOfToken);
            }
            if (this.mAccessEvents == null) {
                this.mAccessEvents = new android.util.LongSparseArray<>(1);
            }
            android.app.AppOpsManager.OpEventProxyInfo proxyCopy = event.getProxy() != null ? new android.app.AppOpsManager.OpEventProxyInfo(event.getProxy()) : null;
            long accessDurationMillis = android.os.SystemClock.elapsedRealtime() - event.getStartElapsedTime();
            android.app.AppOpsManager.NoteOpEvent finishedEvent = new android.app.AppOpsManager.NoteOpEvent(event.getStartTime(), accessDurationMillis, proxyCopy);
            this.mAccessEvents.put(android.app.AppOpsManager.makeKey(event.getUidState(), event.getFlags()), finishedEvent);
            this.mAppOpsService.mHistoricalRegistry.increaseOpAccessDuration(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getUidState(), event.getFlags(), finishedEvent.getNoteTime(), finishedEvent.getDuration(), event.getAttributionFlags(), event.getAttributionChainId());
            if (!isPausing) {
                this.mAppOpsService.mInProgressStartOpEventPool.release(event);
                if (this.mInProgressEvents.isEmpty()) {
                    this.mInProgressEvents = null;
                    if (!triggeredByUidStateChange && !this.parent.isRunning()) {
                        this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), false, event.getAttributionFlags(), event.getAttributionChainId());
                    }
                }
            }
        }
    }

    private void finishPossiblyPaused(android.os.IBinder clientId, boolean isPausing) {
        if (!isPaused()) {
            android.util.Slog.wtf("AppOps", "No ops running or paused");
            return;
        }
        int indexOfToken = this.mPausedInProgressEvents.indexOfKey(clientId);
        if (indexOfToken < 0) {
            android.util.Slog.wtf("AppOps", "No op running or paused for the client");
            return;
        }
        if (isPausing) {
            return;
        }
        com.android.server.appop.AttributedOp.InProgressStartOpEvent event = this.mPausedInProgressEvents.valueAt(indexOfToken);
        event.mNumUnfinishedStarts--;
        if (event.mNumUnfinishedStarts == 0) {
            this.mPausedInProgressEvents.removeAt(indexOfToken);
            this.mAppOpsService.mInProgressStartOpEventPool.release(event);
            if (this.mPausedInProgressEvents.isEmpty()) {
                this.mPausedInProgressEvents = null;
            }
        }
    }

    public void createPaused(android.os.IBinder clientId, int virtualDeviceId, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, java.lang.String proxyDeviceId, int uidState, int flags, int attributionFlags, int attributionChainId) throws android.os.RemoteException {
        startedOrPaused(clientId, virtualDeviceId, proxyUid, proxyPackageName, proxyAttributionTag, proxyDeviceId, uidState, flags, attributionFlags, attributionChainId, false, false);
    }

    public void pause() {
        if (!isRunning()) {
            return;
        }
        if (this.mPausedInProgressEvents == null) {
            this.mPausedInProgressEvents = new android.util.ArrayMap<>(1);
        }
        for (int i = 0; i < this.mInProgressEvents.size(); i++) {
            com.android.server.appop.AttributedOp.InProgressStartOpEvent event = this.mInProgressEvents.valueAt(i);
            this.mPausedInProgressEvents.put(event.getClientId(), event);
            finishOrPause(event.getClientId(), false, true);
            this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), false, event.getAttributionFlags(), event.getAttributionChainId());
        }
        this.mInProgressEvents = null;
    }

    public void resume() {
        if (!isPaused()) {
            return;
        }
        if (this.mInProgressEvents == null) {
            this.mInProgressEvents = new android.util.ArrayMap<>(this.mPausedInProgressEvents.size());
        }
        boolean shouldSendActive = !this.mPausedInProgressEvents.isEmpty() && this.mInProgressEvents.isEmpty();
        long startTime = java.lang.System.currentTimeMillis();
        for (int i = 0; i < this.mPausedInProgressEvents.size(); i++) {
            com.android.server.appop.AttributedOp.InProgressStartOpEvent event = this.mPausedInProgressEvents.valueAt(i);
            this.mInProgressEvents.put(event.getClientId(), event);
            event.setStartElapsedTime(android.os.SystemClock.elapsedRealtime());
            event.setStartTime(startTime);
            this.mAppOpsService.mHistoricalRegistry.incrementOpAccessedCount(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getUidState(), event.getFlags(), startTime, event.getAttributionFlags(), event.getAttributionChainId());
            if (shouldSendActive) {
                this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), true, event.getAttributionFlags(), event.getAttributionChainId());
            }
            this.mAppOpsService.scheduleOpStartedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), event.getFlags(), 0, 2, event.getAttributionFlags(), event.getAttributionChainId());
        }
        this.mPausedInProgressEvents = null;
    }

    void onClientDeath(android.os.IBinder clientId) {
        synchronized (this.mAppOpsService) {
            if (isPaused() || isRunning()) {
                android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> events = isPaused() ? this.mPausedInProgressEvents : this.mInProgressEvents;
                com.android.server.appop.AttributedOp.InProgressStartOpEvent deadEvent = events.get(clientId);
                if (deadEvent != null) {
                    deadEvent.mNumUnfinishedStarts = 1;
                }
                finished(clientId);
            }
        }
    }

    public void onUidStateChanged(int newState) {
        int i;
        java.util.List<android.os.IBinder> binders;
        int numInProgressEvents;
        com.android.server.appop.AttributedOp.InProgressStartOpEvent event;
        int i2;
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> events;
        java.util.List<android.os.IBinder> binders2;
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> arrayMap;
        if (!isPaused() && !isRunning()) {
            return;
        }
        boolean isRunning = isRunning();
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> events2 = isRunning ? this.mInProgressEvents : this.mPausedInProgressEvents;
        int numInProgressEvents2 = events2.size();
        java.util.List<android.os.IBinder> binders3 = new java.util.ArrayList<>(events2.keySet());
        android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> events3 = events2;
        int i3 = 0;
        while (i3 < numInProgressEvents2) {
            com.android.server.appop.AttributedOp.InProgressStartOpEvent event2 = events3.get(binders3.get(i3));
            if (event2 == null || event2.getUidState() == newState) {
                i = i3;
                binders = binders3;
                numInProgressEvents = numInProgressEvents2;
                events3 = events3;
            } else {
                int eventAttributionFlags = event2.getAttributionFlags();
                int eventAttributionChainId = event2.getAttributionChainId();
                try {
                    int numPreviousUnfinishedStarts = event2.mNumUnfinishedStarts;
                    event2.mNumUnfinishedStarts = 1;
                    android.app.AppOpsManager.OpEventProxyInfo proxy = event2.getProxy();
                    finished(event2.getClientId(), true);
                    if (proxy != null) {
                        try {
                            event = event2;
                            i2 = i3;
                            events = events3;
                            binders2 = binders3;
                            numInProgressEvents = numInProgressEvents2;
                            try {
                                startedOrPaused(event2.getClientId(), event2.getVirtualDeviceId(), proxy.getUid(), proxy.getPackageName(), proxy.getAttributionTag(), proxy.getDeviceId(), newState, event2.getFlags(), event2.getAttributionFlags(), event2.getAttributionChainId(), true, isRunning);
                            } catch (android.os.RemoteException e) {
                                events3 = events;
                                i = i2;
                                binders = binders2;
                                this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), false, eventAttributionFlags, eventAttributionChainId);
                                i3 = i + 1;
                                binders3 = binders;
                                numInProgressEvents2 = numInProgressEvents;
                            }
                        } catch (android.os.RemoteException e2) {
                            event = event2;
                            numInProgressEvents = numInProgressEvents2;
                            i = i3;
                            binders = binders3;
                        }
                    } else {
                        event = event2;
                        i2 = i3;
                        events = events3;
                        binders2 = binders3;
                        numInProgressEvents = numInProgressEvents2;
                        startedOrPaused(event.getClientId(), event.getVirtualDeviceId(), -1, null, null, null, newState, event.getFlags(), event.getAttributionFlags(), event.getAttributionChainId(), true, isRunning);
                    }
                    if (isRunning) {
                        arrayMap = this.mInProgressEvents;
                    } else {
                        try {
                            arrayMap = this.mPausedInProgressEvents;
                        } catch (android.os.RemoteException e3) {
                            i = i2;
                            binders = binders2;
                            events3 = events;
                            this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), false, eventAttributionFlags, eventAttributionChainId);
                            i3 = i + 1;
                            binders3 = binders;
                            numInProgressEvents2 = numInProgressEvents;
                        }
                    }
                    events3 = arrayMap;
                    i = i2;
                    binders = binders2;
                    try {
                        com.android.server.appop.AttributedOp.InProgressStartOpEvent newEvent = events3.get(binders.get(i));
                        if (newEvent != null) {
                            newEvent.mNumUnfinishedStarts += numPreviousUnfinishedStarts - 1;
                        }
                    } catch (android.os.RemoteException e4) {
                        this.mAppOpsService.scheduleOpActiveChangedIfNeededLocked(this.parent.op, this.parent.uid, this.parent.packageName, this.tag, event.getVirtualDeviceId(), false, eventAttributionFlags, eventAttributionChainId);
                    }
                } catch (android.os.RemoteException e5) {
                    event = event2;
                    i = i3;
                    binders = binders3;
                    numInProgressEvents = numInProgressEvents2;
                }
            }
            i3 = i + 1;
            binders3 = binders;
            numInProgressEvents2 = numInProgressEvents;
        }
    }

    private android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> add(android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> a, android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        int numEventsToAdd = b.size();
        for (int i = 0; i < numEventsToAdd; i++) {
            long keyOfEventToAdd = b.keyAt(i);
            android.app.AppOpsManager.NoteOpEvent bEvent = b.valueAt(i);
            android.app.AppOpsManager.NoteOpEvent aEvent = a.get(keyOfEventToAdd);
            if (aEvent == null || bEvent.getNoteTime() > aEvent.getNoteTime()) {
                a.put(keyOfEventToAdd, bEvent);
            }
        }
        return a;
    }

    public void add(com.android.server.appop.AttributedOp opToAdd) {
        if (opToAdd.isRunning() || opToAdd.isPaused()) {
            android.util.ArrayMap<android.os.IBinder, com.android.server.appop.AttributedOp.InProgressStartOpEvent> ignoredEvents = opToAdd.isRunning() ? opToAdd.mInProgressEvents : opToAdd.mPausedInProgressEvents;
            android.util.Slog.w("AppOps", "Ignoring " + ignoredEvents.size() + " app-ops, running: " + opToAdd.isRunning());
            int numInProgressEvents = ignoredEvents.size();
            for (int i = 0; i < numInProgressEvents; i++) {
                com.android.server.appop.AttributedOp.InProgressStartOpEvent event = ignoredEvents.valueAt(i);
                event.finish();
                this.mAppOpsService.mInProgressStartOpEventPool.release(event);
            }
        }
        this.mAccessEvents = add(this.mAccessEvents, opToAdd.mAccessEvents);
        this.mRejectEvents = add(this.mRejectEvents, opToAdd.mRejectEvents);
    }

    public boolean isRunning() {
        return (this.mInProgressEvents == null || this.mInProgressEvents.isEmpty()) ? false : true;
    }

    public boolean isPaused() {
        return (this.mPausedInProgressEvents == null || this.mPausedInProgressEvents.isEmpty()) ? false : true;
    }

    boolean hasAnyTime() {
        return (this.mAccessEvents != null && this.mAccessEvents.size() > 0) || (this.mRejectEvents != null && this.mRejectEvents.size() > 0);
    }

    private android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> deepClone(android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> original) {
        if (original == null) {
            return original;
        }
        int size = original.size();
        android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> clone = new android.util.LongSparseArray<>(size);
        for (int i = 0; i < size; i++) {
            clone.put(original.keyAt(i), new android.app.AppOpsManager.NoteOpEvent(original.valueAt(i)));
        }
        return clone;
    }

    android.app.AppOpsManager.AttributedOpEntry createAttributedOpEntryLocked() {
        android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> accessEvents = deepClone(this.mAccessEvents);
        if (isRunning()) {
            long now = android.os.SystemClock.elapsedRealtime();
            int numInProgressEvents = this.mInProgressEvents.size();
            if (accessEvents == null) {
                accessEvents = new android.util.LongSparseArray<>(numInProgressEvents);
            }
            int i = 0;
            while (i < numInProgressEvents) {
                com.android.server.appop.AttributedOp.InProgressStartOpEvent event = this.mInProgressEvents.valueAt(i);
                accessEvents.append(android.app.AppOpsManager.makeKey(event.getUidState(), event.getFlags()), new android.app.AppOpsManager.NoteOpEvent(event.getStartTime(), java.lang.Math.max(now - event.getStartElapsedTime(), 0L), event.getProxy()));
                i++;
                now = now;
            }
        }
        android.util.LongSparseArray<android.app.AppOpsManager.NoteOpEvent> rejectEvents = deepClone(this.mRejectEvents);
        return new android.app.AppOpsManager.AttributedOpEntry(this.parent.op, isRunning(), accessEvents, rejectEvents);
    }

    static final class InProgressStartOpEvent implements android.os.IBinder.DeathRecipient {
        private int mAttributionChainId;
        private int mAttributionFlags;
        private java.lang.String mAttributionTag;
        private android.os.IBinder mClientId;
        private int mFlags;
        int mNumUnfinishedStarts;
        private java.lang.Runnable mOnDeath;
        private android.app.AppOpsManager.OpEventProxyInfo mProxy;
        private long mStartElapsedTime;
        private long mStartTime;
        private int mUidState;
        private int mVirtualDeviceId;

        InProgressStartOpEvent(long startTime, long startElapsedTime, android.os.IBinder clientId, int virtualDeviceId, java.lang.String attributionTag, java.lang.Runnable onDeath, int uidState, android.app.AppOpsManager.OpEventProxyInfo proxy, int flags, int attributionFlags, int attributionChainId) throws android.os.RemoteException {
            this.mStartTime = startTime;
            this.mStartElapsedTime = startElapsedTime;
            this.mClientId = clientId;
            this.mVirtualDeviceId = virtualDeviceId;
            this.mAttributionTag = attributionTag;
            this.mOnDeath = onDeath;
            this.mUidState = uidState;
            this.mProxy = proxy;
            this.mFlags = flags;
            this.mAttributionFlags = attributionFlags;
            this.mAttributionChainId = attributionChainId;
            clientId.linkToDeath(this, 0);
        }

        public void finish() {
            try {
                this.mClientId.unlinkToDeath(this, 0);
            } catch (java.util.NoSuchElementException e) {
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mOnDeath.run();
        }

        public void reinit(long startTime, long startElapsedTime, android.os.IBinder clientId, java.lang.String attributionTag, int virtualDeviceId, java.lang.Runnable onDeath, int uidState, int flags, android.app.AppOpsManager.OpEventProxyInfo proxy, int attributionFlags, int attributionChainId, android.util.Pools.Pool<android.app.AppOpsManager.OpEventProxyInfo> proxyPool) throws android.os.RemoteException {
            this.mStartTime = startTime;
            this.mStartElapsedTime = startElapsedTime;
            this.mClientId = clientId;
            this.mAttributionTag = attributionTag;
            this.mOnDeath = onDeath;
            this.mVirtualDeviceId = virtualDeviceId;
            this.mUidState = uidState;
            this.mFlags = flags;
            if (this.mProxy != null) {
                proxyPool.release(this.mProxy);
            }
            this.mProxy = proxy;
            this.mAttributionFlags = attributionFlags;
            this.mAttributionChainId = attributionChainId;
            clientId.linkToDeath(this, 0);
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public long getStartElapsedTime() {
            return this.mStartElapsedTime;
        }

        public android.os.IBinder getClientId() {
            return this.mClientId;
        }

        public int getUidState() {
            return this.mUidState;
        }

        public android.app.AppOpsManager.OpEventProxyInfo getProxy() {
            return this.mProxy;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public int getAttributionFlags() {
            return this.mAttributionFlags;
        }

        public int getAttributionChainId() {
            return this.mAttributionChainId;
        }

        public int getVirtualDeviceId() {
            return this.mVirtualDeviceId;
        }

        public void setStartTime(long startTime) {
            this.mStartTime = startTime;
        }

        public void setStartElapsedTime(long startElapsedTime) {
            this.mStartElapsedTime = startElapsedTime;
        }
    }

    static class InProgressStartOpEventPool extends android.util.Pools.SimplePool<com.android.server.appop.AttributedOp.InProgressStartOpEvent> {
        private com.android.server.appop.AttributedOp.OpEventProxyInfoPool mOpEventProxyInfoPool;

        InProgressStartOpEventPool(com.android.server.appop.AttributedOp.OpEventProxyInfoPool opEventProxyInfoPool, int maxUnusedPooledObjects) {
            super(maxUnusedPooledObjects);
            this.mOpEventProxyInfoPool = opEventProxyInfoPool;
        }

        com.android.server.appop.AttributedOp.InProgressStartOpEvent acquire(long startTime, long elapsedTime, android.os.IBinder clientId, java.lang.String attributionTag, int virtualDeviceId, java.lang.Runnable onDeath, int proxyUid, java.lang.String proxyPackageName, java.lang.String proxyAttributionTag, java.lang.String proxyDeviceId, int uidState, int flags, int attributionFlags, int attributionChainId) throws android.os.RemoteException {
            android.app.AppOpsManager.OpEventProxyInfo proxyInfo;
            com.android.server.appop.AttributedOp.InProgressStartOpEvent recycled = (com.android.server.appop.AttributedOp.InProgressStartOpEvent) acquire();
            if (proxyUid == -1) {
                proxyInfo = null;
            } else {
                android.app.AppOpsManager.OpEventProxyInfo proxyInfo2 = this.mOpEventProxyInfoPool.acquire(proxyUid, proxyPackageName, proxyAttributionTag, proxyDeviceId);
                proxyInfo = proxyInfo2;
            }
            if (recycled != null) {
                recycled.reinit(startTime, elapsedTime, clientId, attributionTag, virtualDeviceId, onDeath, uidState, flags, proxyInfo, attributionFlags, attributionChainId, this.mOpEventProxyInfoPool);
                return recycled;
            }
            return new com.android.server.appop.AttributedOp.InProgressStartOpEvent(startTime, elapsedTime, clientId, virtualDeviceId, attributionTag, onDeath, uidState, proxyInfo, flags, attributionFlags, attributionChainId);
        }
    }

    static class OpEventProxyInfoPool extends android.util.Pools.SimplePool<android.app.AppOpsManager.OpEventProxyInfo> {
        OpEventProxyInfoPool(int maxUnusedPooledObjects) {
            super(maxUnusedPooledObjects);
        }

        android.app.AppOpsManager.OpEventProxyInfo acquire(int uid, java.lang.String packageName, java.lang.String attributionTag, java.lang.String deviceId) {
            android.app.AppOpsManager.OpEventProxyInfo recycled = (android.app.AppOpsManager.OpEventProxyInfo) acquire();
            if (recycled != null) {
                recycled.reinit(uid, packageName, attributionTag, deviceId);
                return recycled;
            }
            return new android.app.AppOpsManager.OpEventProxyInfo(uid, packageName, attributionTag, deviceId);
        }
    }
}
