package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseAppStateDurationsTracker<T extends com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, U extends com.android.server.am.BaseAppStateDurations> extends com.android.server.am.BaseAppStateEventsTracker<T, U> {
    static final boolean DEBUG_BASE_APP_STATE_DURATION_TRACKER = false;
    final android.util.SparseArray<com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations> mUidStateDurations;

    BaseAppStateDurationsTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<T>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mUidStateDurations = new android.util.SparseArray<>();
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void onUidProcStateChanged(int uid, int procState) {
        synchronized (this.mLock) {
            if (this.mPkgEvents.getMap().indexOfKey(uid) < 0) {
                return;
            }
            onUidProcStateChangedUncheckedLocked(uid, procState);
            com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations uidStateDurations = this.mUidStateDurations.get(uid);
            if (uidStateDurations == null) {
                uidStateDurations = new com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations(uid, (com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
                this.mUidStateDurations.put(uid, uidStateDurations);
            }
            uidStateDurations.addEvent(procState < 4, android.os.SystemClock.elapsedRealtime());
        }
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void onUidGone(int uid) {
        onUidProcStateChanged(uid, 20);
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    void trimLocked(long earliest) {
        super.trimLocked(earliest);
        for (int i = this.mUidStateDurations.size() - 1; i >= 0; i--) {
            com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations u = this.mUidStateDurations.valueAt(i);
            u.trim(earliest);
            if (u.isEmpty()) {
                this.mUidStateDurations.removeAt(i);
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    void onUntrackingUidLocked(int uid) {
        this.mUidStateDurations.remove(uid);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    long getTotalDurations(java.lang.String packageName, int uid, long now, int index, boolean bgOnly) {
        com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations uidDurations;
        synchronized (this.mLock) {
            com.android.server.am.BaseAppStateDurations baseAppStateDurations = (com.android.server.am.BaseAppStateDurations) this.mPkgEvents.get(uid, packageName);
            if (baseAppStateDurations == null) {
                return 0L;
            }
            if (bgOnly && (uidDurations = this.mUidStateDurations.get(uid)) != null && !uidDurations.isEmpty()) {
                U res = createAppStateEvents(baseAppStateDurations);
                res.subtract(uidDurations, index, 0);
                return res.getTotalDurations(now, index);
            }
            return baseAppStateDurations.getTotalDurations(now, index);
        }
    }

    long getTotalDurations(java.lang.String packageName, int uid, long now, int index) {
        return getTotalDurations(packageName, uid, now, index, true);
    }

    long getTotalDurations(java.lang.String packageName, int uid, long now) {
        return getTotalDurations(packageName, uid, now, 0);
    }

    long getTotalDurations(int uid, long now, int index, boolean bgOnly) {
        com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations uidDurations;
        synchronized (this.mLock) {
            U durations = getUidEventsLocked(uid);
            if (durations == null) {
                return 0L;
            }
            if (bgOnly && (uidDurations = this.mUidStateDurations.get(uid)) != null && !uidDurations.isEmpty()) {
                durations.subtract(uidDurations, index, 0);
            }
            return durations.getTotalDurations(now, index);
        }
    }

    long getTotalDurations(int uid, long now, int index) {
        return getTotalDurations(uid, now, index, true);
    }

    long getTotalDurations(int uid, long now) {
        return getTotalDurations(uid, now, 0);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    long getTotalDurationsSince(java.lang.String packageName, int uid, long since, long now, int index, boolean bgOnly) throws java.lang.Throwable {
        com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations uidDurations;
        synchronized (this.mLock) {
            try {
                try {
                    try {
                        com.android.server.am.BaseAppStateDurations baseAppStateDurations = (com.android.server.am.BaseAppStateDurations) this.mPkgEvents.get(uid, packageName);
                        if (baseAppStateDurations == null) {
                            return 0L;
                        }
                        if (bgOnly && (uidDurations = this.mUidStateDurations.get(uid)) != null && !uidDurations.isEmpty()) {
                            U res = createAppStateEvents(baseAppStateDurations);
                            res.subtract(uidDurations, index, 0);
                            return res.getTotalDurationsSince(since, now, index);
                        }
                        return baseAppStateDurations.getTotalDurationsSince(since, now, index);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    long getTotalDurationsSince(java.lang.String packageName, int uid, long since, long now, int index) {
        return getTotalDurationsSince(packageName, uid, since, now, index, true);
    }

    long getTotalDurationsSince(java.lang.String packageName, int uid, long since, long now) {
        return getTotalDurationsSince(packageName, uid, since, now, 0);
    }

    long getTotalDurationsSince(int uid, long since, long now, int index, boolean bgOnly) {
        com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations uidDurations;
        synchronized (this.mLock) {
            U durations = getUidEventsLocked(uid);
            if (durations == null) {
                return 0L;
            }
            if (bgOnly && (uidDurations = this.mUidStateDurations.get(uid)) != null && !uidDurations.isEmpty()) {
                durations.subtract(uidDurations, index, 0);
            }
            return durations.getTotalDurationsSince(since, now, index);
        }
    }

    long getTotalDurationsSince(int uid, long since, long now, int index) {
        return getTotalDurationsSince(uid, since, now, index, true);
    }

    long getTotalDurationsSince(int uid, long since, long now) {
        return getTotalDurationsSince(uid, since, now, 0);
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker
    void reset() {
        super.reset();
        synchronized (this.mLock) {
            this.mUidStateDurations.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.am.BaseAppStateEventsTracker
    public void dumpEventLocked(java.io.PrintWriter pw, java.lang.String prefix, U events, long now) {
        com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations uidDurations = this.mUidStateDurations.get(events.mUid);
        pw.print("  " + prefix);
        pw.println("(bg only)");
        if (uidDurations == null || uidDurations.isEmpty()) {
            events.dump(pw, "    " + prefix, now);
            return;
        }
        U bgEvents = createAppStateEvents(events);
        bgEvents.subtract(uidDurations, 0);
        bgEvents.dump(pw, "    " + prefix, now);
        pw.print("  " + prefix);
        pw.println("(fg + bg)");
        events.dump(pw, "    " + prefix, now);
    }

    static class SimplePackageDurations extends com.android.server.am.BaseAppStateDurations<com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent> {
        static final int DEFAULT_INDEX = 0;

        /* JADX WARN: Multi-variable type inference failed */
        SimplePackageDurations(int uid, java.lang.String packageName, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(uid, packageName, 1, com.android.server.am.IActivityManagerServiceExt.TAG, maxTrackingDurationConfig);
            this.mEvents[0] = new java.util.LinkedList();
        }

        SimplePackageDurations(com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations other) {
            super(other);
        }

        void addEvent(boolean active, long now) {
            addEvent(active, new com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent(now), 0);
        }

        long getTotalDurations(long now) {
            return getTotalDurations(now, 0);
        }

        long getTotalDurationsSince(long since, long now) {
            return getTotalDurationsSince(since, now, 0);
        }

        boolean isActive() {
            return isActive(0);
        }

        @Override // com.android.server.am.BaseAppStateEvents
        java.lang.String formatEventTypeLabel(int index) {
            return "";
        }
    }

    static class UidStateDurations extends com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations {
        UidStateDurations(int uid, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(uid, "", maxTrackingDurationConfig);
        }

        UidStateDurations(com.android.server.am.BaseAppStateDurationsTracker.UidStateDurations other) {
            super(other);
        }
    }
}
