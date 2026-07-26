package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseAppStateTimeSlotEventsTracker<T extends com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy, U extends com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents> extends com.android.server.am.BaseAppStateEventsTracker<T, U> {
    static final boolean DEBUG_APP_STATE_TIME_SLOT_EVENT_TRACKER = false;
    static final java.lang.String TAG = "BaseAppStateTimeSlotEventsTracker";
    private com.android.server.am.BaseAppStateTimeSlotEventsTracker.H mHandler;
    private final android.util.ArrayMap<U, java.lang.Integer> mTmpPkgs;

    BaseAppStateTimeSlotEventsTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<T>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mTmpPkgs = new android.util.ArrayMap<>();
        this.mHandler = new com.android.server.am.BaseAppStateTimeSlotEventsTracker.H(this);
    }

    void onNewEvent(java.lang.String packageName, int uid) {
        this.mHandler.obtainMessage(0, uid, 0, packageName).sendToTarget();
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
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:20:0x005d
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    void handleNewEvent(java.lang.String r11, int r12) throws java.lang.Throwable {
        /*
            r10 = this;
            com.android.server.am.BaseAppStateTracker$Injector<T extends com.android.server.am.BaseAppStatePolicy> r0 = r10.mInjector
            com.android.server.am.BaseAppStatePolicy r0 = r0.getPolicy()
            com.android.server.am.BaseAppStateTimeSlotEventsTracker$BaseAppStateTimeSlotEventsPolicy r0 = (com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy) r0
            int r0 = r0.shouldExempt(r11, r12)
            r1 = -1
            if (r0 == r1) goto L10
            return
        L10:
            long r0 = android.os.SystemClock.elapsedRealtime()
            r2 = 0
            java.lang.Object r3 = r10.mLock
            monitor-enter(r3)
            com.android.server.am.UidProcessMap<U extends com.android.server.am.BaseAppStateEvents> r4 = r10.mPkgEvents     // Catch: java.lang.Throwable -> L5d
            java.lang.Object r4 = r4.get(r12, r11)     // Catch: java.lang.Throwable -> L5d
            com.android.server.am.BaseAppStateTimeSlotEventsTracker$SimpleAppStateTimeslotEvents r4 = (com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents) r4     // Catch: java.lang.Throwable -> L5d
            if (r4 != 0) goto L2e
            com.android.server.am.BaseAppStateEvents r5 = r10.createAppStateEvents(r12, r11)     // Catch: java.lang.Throwable -> L5d
            com.android.server.am.BaseAppStateTimeSlotEventsTracker$SimpleAppStateTimeslotEvents r5 = (com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents) r5     // Catch: java.lang.Throwable -> L5d
            r4 = r5
            com.android.server.am.UidProcessMap<U extends com.android.server.am.BaseAppStateEvents> r5 = r10.mPkgEvents     // Catch: java.lang.Throwable -> L5d
            r5.put(r12, r11, r4)     // Catch: java.lang.Throwable -> L5d
        L2e:
            r5 = 0
            r4.addEvent(r0, r5)     // Catch: java.lang.Throwable -> L5d
            int r6 = r4.getTotalEvents(r0, r5)     // Catch: java.lang.Throwable -> L5d
            r8 = r6
            com.android.server.am.BaseAppStateTracker$Injector<T extends com.android.server.am.BaseAppStatePolicy> r6 = r10.mInjector     // Catch: java.lang.Throwable -> L5d
            com.android.server.am.BaseAppStatePolicy r6 = r6.getPolicy()     // Catch: java.lang.Throwable -> L5d
            com.android.server.am.BaseAppStateTimeSlotEventsTracker$BaseAppStateTimeSlotEventsPolicy r6 = (com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy) r6     // Catch: java.lang.Throwable -> L5d
            int r6 = r6.getNumOfEventsThreshold()     // Catch: java.lang.Throwable -> L5d
            if (r8 < r6) goto L46
            r5 = 1
        L46:
            r9 = r5
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L5a
            if (r9 == 0) goto L59
            com.android.server.am.BaseAppStateTracker$Injector<T extends com.android.server.am.BaseAppStatePolicy> r2 = r10.mInjector
            com.android.server.am.BaseAppStatePolicy r2 = r2.getPolicy()
            com.android.server.am.BaseAppStateTimeSlotEventsTracker$BaseAppStateTimeSlotEventsPolicy r2 = (com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy) r2
            r3 = r11
            r4 = r12
            r5 = r8
            r6 = r0
            r2.onExcessiveEvents(r3, r4, r5, r6)
        L59:
            return
        L5a:
            r4 = move-exception
            r2 = r9
            goto L5e
        L5d:
            r4 = move-exception
        L5e:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L5d
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.BaseAppStateTimeSlotEventsTracker.handleNewEvent(java.lang.String, int):void");
    }

    void onMonitorEnabled(boolean enabled) {
        if (!enabled) {
            synchronized (this.mLock) {
                this.mPkgEvents.clear();
            }
        }
    }

    void onNumOfEventsThresholdChanged(int i) {
        long jElapsedRealtime = android.os.SystemClock.elapsedRealtime();
        synchronized (this.mLock) {
            android.util.SparseArray map = this.mPkgEvents.getMap();
            for (int size = map.size() - 1; size >= 0; size--) {
                android.util.ArrayMap arrayMap = (android.util.ArrayMap) map.valueAt(size);
                for (int size2 = arrayMap.size() - 1; size2 >= 0; size2--) {
                    com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents simpleAppStateTimeslotEvents = (com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents) arrayMap.valueAt(size2);
                    int totalEvents = simpleAppStateTimeslotEvents.getTotalEvents(jElapsedRealtime, 0);
                    if (totalEvents >= i) {
                        this.mTmpPkgs.put(simpleAppStateTimeslotEvents, java.lang.Integer.valueOf(totalEvents));
                    }
                }
            }
        }
        for (int size3 = this.mTmpPkgs.size() - 1; size3 >= 0; size3--) {
            U uKeyAt = this.mTmpPkgs.keyAt(size3);
            ((com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy) this.mInjector.getPolicy()).onExcessiveEvents(uKeyAt.mPackageName, uKeyAt.mUid, this.mTmpPkgs.valueAt(size3).intValue(), jElapsedRealtime);
        }
        this.mTmpPkgs.clear();
    }

    int getTotalEventsLocked(int uid, long now) {
        U events = getUidEventsLocked(uid);
        if (events == null) {
            return 0;
        }
        return events.getTotalEvents(now, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimEvents() {
        long now = android.os.SystemClock.elapsedRealtime();
        trim(java.lang.Math.max(0L, now - ((com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy) this.mInjector.getPolicy()).getMaxTrackingDuration()));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUserInteractionStarted(java.lang.String packageName, int uid) {
        ((com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy) this.mInjector.getPolicy()).onUserInteractionStarted(packageName, uid);
    }

    static class H extends android.os.Handler {
        static final int MSG_NEW_EVENT = 0;
        final com.android.server.am.BaseAppStateTimeSlotEventsTracker mTracker;

        H(com.android.server.am.BaseAppStateTimeSlotEventsTracker tracker) {
            super(tracker.mBgHandler.getLooper());
            this.mTracker = tracker;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 0:
                    this.mTracker.handleNewEvent((java.lang.String) msg.obj, msg.arg1);
                    break;
            }
        }
    }

    static class BaseAppStateTimeSlotEventsPolicy<E extends com.android.server.am.BaseAppStateTimeSlotEventsTracker> extends com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy<E> {
        final int mDefaultNumOfEventsThreshold;
        private final com.android.internal.app.ProcessMap<java.lang.Long> mExcessiveEventPkgs;
        final java.lang.String mKeyNumOfEventsThreshold;
        private final java.lang.Object mLock;
        volatile int mNumOfEventsThreshold;
        long mTimeSlotSize;

        BaseAppStateTimeSlotEventsPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, E tracker, java.lang.String keyTrackerEnabled, boolean defaultTrackerEnabled, java.lang.String keyMaxTrackingDuration, long defaultMaxTrackingDuration, java.lang.String keyNumOfEventsThreshold, int defaultNumOfEventsThreshold) {
            super(injector, tracker, keyTrackerEnabled, defaultTrackerEnabled, keyMaxTrackingDuration, defaultMaxTrackingDuration);
            this.mExcessiveEventPkgs = new com.android.internal.app.ProcessMap<>();
            this.mTimeSlotSize = 900000L;
            this.mKeyNumOfEventsThreshold = keyNumOfEventsThreshold;
            this.mDefaultNumOfEventsThreshold = defaultNumOfEventsThreshold;
            this.mLock = tracker.mLock;
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            super.onSystemReady();
            updateNumOfEventsThreshold();
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        public void onPropertiesChanged(java.lang.String name) {
            if (this.mKeyNumOfEventsThreshold.equals(name)) {
                updateNumOfEventsThreshold();
            } else {
                super.onPropertiesChanged(name);
            }
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean enabled) {
            ((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).onMonitorEnabled(enabled);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        public void onMaxTrackingDurationChanged(long maxDuration) {
            android.os.Handler handler = ((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).mBgHandler;
            final com.android.server.am.BaseAppStateTimeSlotEventsTracker baseAppStateTimeSlotEventsTracker = (com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker;
            java.util.Objects.requireNonNull(baseAppStateTimeSlotEventsTracker);
            handler.post(new java.lang.Runnable() { // from class: com.android.server.am.BaseAppStateTimeSlotEventsTracker$BaseAppStateTimeSlotEventsPolicy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    baseAppStateTimeSlotEventsTracker.trimEvents();
                }
            });
        }

        private void updateNumOfEventsThreshold() {
            int threshold = android.provider.DeviceConfig.getInt("activity_manager", this.mKeyNumOfEventsThreshold, this.mDefaultNumOfEventsThreshold);
            if (threshold != this.mNumOfEventsThreshold) {
                this.mNumOfEventsThreshold = threshold;
                ((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).onNumOfEventsThresholdChanged(threshold);
            }
        }

        int getNumOfEventsThreshold() {
            return this.mNumOfEventsThreshold;
        }

        long getTimeSlotSize() {
            return this.mTimeSlotSize;
        }

        void setTimeSlotSize(long size) {
            this.mTimeSlotSize = size;
        }

        java.lang.String getEventName() {
            return "event";
        }

        void onExcessiveEvents(java.lang.String packageName, int uid, int numOfEvents, long now) {
            boolean notifyController = false;
            synchronized (this.mLock) {
                java.lang.Long ts = (java.lang.Long) this.mExcessiveEventPkgs.get(packageName, uid);
                if (ts == null) {
                    this.mExcessiveEventPkgs.put(packageName, uid, java.lang.Long.valueOf(now));
                    notifyController = true;
                }
            }
            if (notifyController) {
                ((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).mAppRestrictionController.refreshAppRestrictionLevelForUid(uid, 1536, 2, true);
            }
        }

        int shouldExempt(java.lang.String packageName, int uid) {
            if (((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).isUidOnTop(uid)) {
                return 12;
            }
            if (((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).mAppRestrictionController.hasForegroundServices(packageName, uid)) {
                return 14;
            }
            int reason = shouldExemptUid(uid);
            if (reason == -1) {
                return -1;
            }
            return reason;
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public int getProposedRestrictionLevel(java.lang.String packageName, int uid, int maxLevel) {
            int level;
            synchronized (this.mLock) {
                if (this.mExcessiveEventPkgs.get(packageName, uid) == null || !((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).mAppRestrictionController.isAutoRestrictAbusiveAppEnabled()) {
                    level = 30;
                } else {
                    level = 40;
                }
                return maxLevel > 40 ? level : maxLevel == 40 ? 30 : 0;
            }
        }

        void onUserInteractionStarted(java.lang.String packageName, int uid) {
            synchronized (this.mLock) {
                boolean z = this.mExcessiveEventPkgs.remove(packageName, uid) != null;
            }
            ((com.android.server.am.BaseAppStateTimeSlotEventsTracker) this.mTracker).mAppRestrictionController.refreshAppRestrictionLevelForUid(uid, 768, 3, true);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            super.dump(pw, prefix);
            if (isEnabled()) {
                pw.print(prefix);
                pw.print(this.mKeyNumOfEventsThreshold);
                pw.print('=');
                pw.println(this.mNumOfEventsThreshold);
            }
            pw.print(prefix);
            pw.print("event_time_slot_size=");
            pw.println(getTimeSlotSize());
        }
    }

    static class SimpleAppStateTimeslotEvents extends com.android.server.am.BaseAppStateTimeSlotEvents {
        static final int DEFAULT_INDEX = 0;
        static final long DEFAULT_TIME_SLOT_SIZE = 900000;
        static final long DEFAULT_TIME_SLOT_SIZE_DEBUG = 60000;

        SimpleAppStateTimeslotEvents(int uid, java.lang.String packageName, long timeslotSize, java.lang.String tag, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(uid, packageName, 1, timeslotSize, tag, maxTrackingDurationConfig);
        }

        SimpleAppStateTimeslotEvents(com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents other) {
            super(other);
        }

        @Override // com.android.server.am.BaseAppStateEvents
        java.lang.String formatEventTypeLabel(int index) {
            return "";
        }

        @Override // com.android.server.am.BaseAppStateEvents
        java.lang.String formatEventSummary(long now, int index) {
            if (this.mEvents[0] == null || this.mEvents[0].size() == 0) {
                return "(none)";
            }
            int total = getTotalEvents(now, 0);
            return "total=" + total + ", latest=" + getTotalEventsSince(this.mCurSlotStartTime[0], now, 0) + "(slot=" + android.util.TimeUtils.formatTime(this.mCurSlotStartTime[0], now) + ")";
        }
    }
}
