package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppBatteryExemptionTracker extends com.android.server.am.BaseAppStateDurationsTracker<com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy, com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates> implements com.android.server.am.BaseAppStateEvents.Factory<com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates>, com.android.server.am.BaseAppStateTracker.StateListener {
    private static final boolean DEBUG_BACKGROUND_BATTERY_EXEMPTION_TRACKER = false;
    static final java.lang.String DEFAULT_NAME = "";
    private static final java.lang.String TAG = "ActivityManager";
    private com.android.server.am.UidProcessMap<java.lang.Integer> mUidPackageStates;

    AppBatteryExemptionTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller) {
        this(context, controller, null, null);
    }

    AppBatteryExemptionTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mUidPackageStates = new com.android.server.am.UidProcessMap<>();
        this.mInjector.setPolicy(new com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy(this.mInjector, this));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    int getType() {
        return 2;
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() {
        super.onSystemReady();
        this.mAppRestrictionController.forEachTracker(new java.util.function.Consumer() { // from class: com.android.server.am.AppBatteryExemptionTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$onSystemReady$0((com.android.server.am.BaseAppStateTracker) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0(com.android.server.am.BaseAppStateTracker tracker) {
        tracker.registerStateListener(this);
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates createAppStateEvents(int uid, java.lang.String packageName) {
        return new com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates(uid, "ActivityManager", (com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates createAppStateEvents(com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates other) {
        return new com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates(other);
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
    @Override // com.android.server.am.BaseAppStateTracker.StateListener
    public void onStateChange(int uid, java.lang.String packageName, boolean start, long now, int stateType) {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> pkgsStates;
        int indexOfPkg;
        int i;
        com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates pkg;
        if (!((com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy) this.mInjector.getPolicy()).isEnabled()) {
            return;
        }
        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage batteryUsage = this.mAppRestrictionController.getUidBatteryUsage(uid);
        int stateTypeIndex = stateTypeToIndex(stateType);
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, java.lang.Integer>> map = this.mUidPackageStates.getMap();
            android.util.ArrayMap<java.lang.String, java.lang.Integer> pkgsStates2 = map.get(uid);
            if (pkgsStates2 != null) {
                pkgsStates = pkgsStates2;
            } else {
                android.util.ArrayMap<java.lang.String, java.lang.Integer> pkgsStates3 = new android.util.ArrayMap<>();
                map.put(uid, pkgsStates3);
                pkgsStates = pkgsStates3;
            }
            int states = 0;
            int indexOfPkg2 = pkgsStates.indexOfKey(packageName);
            if (indexOfPkg2 >= 0) {
                states = pkgsStates.valueAt(indexOfPkg2).intValue();
                indexOfPkg = indexOfPkg2;
            } else {
                pkgsStates.put(packageName, 0);
                indexOfPkg = pkgsStates.indexOfKey(packageName);
            }
            int indexOfPkg3 = 0;
            if (start) {
                boolean alreadyStarted = false;
                int i2 = pkgsStates.size() - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    }
                    int s = pkgsStates.valueAt(i2).intValue();
                    if ((s & stateType) == 0) {
                        i2--;
                    } else {
                        alreadyStarted = true;
                        break;
                    }
                }
                int i3 = states | stateType;
                pkgsStates.setValueAt(indexOfPkg, java.lang.Integer.valueOf(i3));
                if (!alreadyStarted) {
                    indexOfPkg3 = 1;
                }
                i = indexOfPkg3;
            } else {
                int states2 = states & (~stateType);
                pkgsStates.setValueAt(indexOfPkg, java.lang.Integer.valueOf(states2));
                boolean allStopped = true;
                int i4 = pkgsStates.size() - 1;
                while (true) {
                    if (i4 < 0) {
                        break;
                    }
                    int s2 = pkgsStates.valueAt(i4).intValue();
                    if ((s2 & stateType) == 0) {
                        i4--;
                    } else {
                        allStopped = false;
                        break;
                    }
                }
                if (allStopped) {
                    indexOfPkg3 = 1;
                }
                if (states2 == 0) {
                    pkgsStates.removeAt(indexOfPkg);
                    if (pkgsStates.size() == 0) {
                        map.remove(uid);
                    }
                }
                i = indexOfPkg3;
            }
            if (i != 0) {
                com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates pkg2 = (com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates) this.mPkgEvents.get(uid, "");
                if (pkg2 == null) {
                    com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates pkg3 = createAppStateEvents(uid, "");
                    this.mPkgEvents.put(uid, "", pkg3);
                    pkg = pkg3;
                } else {
                    pkg = pkg2;
                }
                pkg.addEvent(start, now, batteryUsage, stateTypeIndex);
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateDurationsTracker, com.android.server.am.BaseAppStateEventsTracker
    void reset() {
        super.reset();
        synchronized (this.mLock) {
            this.mUidPackageStates.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTrackerEnabled(boolean enabled) {
        if (!enabled) {
            synchronized (this.mLock) {
                this.mPkgEvents.clear();
                this.mUidPackageStates.clear();
            }
        }
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
    com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getUidBatteryExemptedUsageSince(int uid, long since, long now, int types) {
        if (!((com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy) this.mInjector.getPolicy()).isEnabled()) {
            return com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE;
        }
        synchronized (this.mLock) {
            com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates pkg = (com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates) this.mPkgEvents.get(uid, "");
            if (pkg == null) {
                return com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE;
            }
            android.util.Pair<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> result = pkg.getBatteryUsageSince(since, now, types);
            if (!((com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage) result.second).isEmpty()) {
                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage batteryUsage = this.mAppRestrictionController.getUidBatteryUsage(uid);
                return ((com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage) result.first).mutate().add(batteryUsage).subtract((com.android.server.am.AppBatteryTracker.BatteryUsage) result.second).unmutate();
            }
            return (com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage) result.first;
        }
    }

    static final class UidBatteryStates extends com.android.server.am.BaseAppStateDurations<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> {
        UidBatteryStates(int uid, java.lang.String tag, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(uid, "", 5, tag, maxTrackingDurationConfig);
        }

        UidBatteryStates(com.android.server.am.AppBatteryExemptionTracker.UidBatteryStates other) {
            super(other);
        }

        void addEvent(boolean start, long now, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage batteryUsage, int eventType) {
            if (start) {
                addEvent(start, new com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery(start, now, batteryUsage, null), eventType);
                return;
            }
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery last = getLastEvent(eventType);
            if (last == null || !last.isStart()) {
                return;
            }
            addEvent(start, new com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery(start, now, batteryUsage.mutate().subtract(last.getBatteryUsage()).unmutate(), last), eventType);
        }

        com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery getLastEvent(int eventType) {
            if (this.mEvents[eventType] != null) {
                return (com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery) this.mEvents[eventType].peekLast();
            }
            return null;
        }

        private android.util.Pair<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> getBatteryUsageSince(long since, long now, java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> events) {
            if (events == null || events.size() == 0) {
                return android.util.Pair.create(com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE, com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE);
            }
            com.android.server.am.AppBatteryTracker.BatteryUsage batteryUsage = new com.android.server.am.AppBatteryTracker.BatteryUsage();
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery lastEvent = null;
            for (com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery event : events) {
                lastEvent = event;
                if (event.getTimestamp() >= since && !event.isStart()) {
                    batteryUsage.add(event.getBatteryUsage(since, java.lang.Math.min(now, event.getTimestamp())));
                    if (now <= event.getTimestamp()) {
                        break;
                    }
                }
            }
            return android.util.Pair.create(batteryUsage.unmutate(), lastEvent.isStart() ? lastEvent.getBatteryUsage() : com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        android.util.Pair<com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage> getBatteryUsageSince(long since, long now, int types) {
            java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> result = new java.util.LinkedList<>();
            for (int i = 0; i < this.mEvents.length; i++) {
                if ((com.android.server.am.BaseAppStateTracker.stateIndexToType(i) & types) != 0) {
                    result = add(result, this.mEvents[i]);
                }
            }
            return getBatteryUsageSince(since, now, result);
        }

        @Override // com.android.server.am.BaseAppStateDurations, com.android.server.am.BaseAppStateTimeEvents, com.android.server.am.BaseAppStateEvents
        java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> add(java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> durations, java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> otherDurations) {
            java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> dest;
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery earliest;
            java.util.Iterator<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> itr;
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery l;
            boolean actl;
            boolean actr;
            long recentActTs;
            java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> dest2;
            long timestamp;
            if (otherDurations == null || otherDurations.size() == 0) {
                return durations;
            }
            if (durations == null || durations.size() == 0) {
                return (java.util.LinkedList) otherDurations.clone();
            }
            java.util.Iterator<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> itl = durations.iterator();
            java.util.Iterator<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> itr2 = otherDurations.iterator();
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery l2 = itl.next();
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery r = itr2.next();
            java.util.LinkedList<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> dest3 = new java.util.LinkedList<>();
            boolean actr2 = false;
            boolean actr3 = false;
            boolean overlapping = false;
            com.android.server.am.AppBatteryTracker.BatteryUsage batteryUsage = new com.android.server.am.AppBatteryTracker.BatteryUsage();
            long recentActTs2 = 0;
            long overlappingDuration = 0;
            long lts = l2.getTimestamp();
            long rts = r.getTimestamp();
            while (true) {
                long timestamp2 = Long.MAX_VALUE;
                if (lts != Long.MAX_VALUE || rts != Long.MAX_VALUE) {
                    boolean actCur = actr2 || actr3;
                    if (lts == rts) {
                        earliest = l2;
                        if (actr2) {
                            dest = dest3;
                            batteryUsage.add(l2.getBatteryUsage());
                        } else {
                            dest = dest3;
                        }
                        if (actr3) {
                            batteryUsage.add(r.getBatteryUsage());
                        }
                        overlappingDuration += (overlapping && (actr2 || actr3)) ? lts - recentActTs2 : 0L;
                        boolean actl2 = !actr2;
                        boolean actr4 = !actr3;
                        boolean actr5 = itl.hasNext();
                        if (actr5) {
                            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery next = itl.next();
                            l2 = next;
                            timestamp = next.getTimestamp();
                        } else {
                            timestamp = Long.MAX_VALUE;
                        }
                        lts = timestamp;
                        if (itr2.hasNext()) {
                            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery next2 = itr2.next();
                            r = next2;
                            timestamp2 = next2.getTimestamp();
                        }
                        rts = timestamp2;
                        actr3 = actr4;
                        actr2 = actl2;
                    } else {
                        dest = dest3;
                        if (lts < rts) {
                            earliest = l2;
                            if (actr2) {
                                batteryUsage.add(l2.getBatteryUsage());
                            }
                            overlappingDuration += (overlapping && actr2) ? lts - recentActTs2 : 0L;
                            boolean actl3 = !actr2;
                            boolean actl4 = itl.hasNext();
                            if (actl4) {
                                com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery next3 = itl.next();
                                l2 = next3;
                                timestamp2 = next3.getTimestamp();
                            }
                            lts = timestamp2;
                            actr2 = actl3;
                        } else {
                            earliest = r;
                            if (actr3) {
                                batteryUsage.add(r.getBatteryUsage());
                            }
                            overlappingDuration += (overlapping && actr3) ? rts - recentActTs2 : 0L;
                            boolean actr6 = !actr3;
                            boolean actr7 = itr2.hasNext();
                            if (actr7) {
                                com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery next4 = itr2.next();
                                r = next4;
                                timestamp2 = next4.getTimestamp();
                            }
                            rts = timestamp2;
                            actr3 = actr6;
                        }
                    }
                    overlapping = actr2 && actr3;
                    if (actr2 || actr3) {
                        recentActTs2 = earliest.getTimestamp();
                    }
                    java.util.Iterator<com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery> itl2 = itl;
                    if (actCur != (actr2 || actr3)) {
                        com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery event = (com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery) earliest.clone();
                        if (actCur) {
                            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery lastEvent = dest.peekLast();
                            long startTs = lastEvent.getTimestamp();
                            itr = itr2;
                            l = l2;
                            long duration = event.getTimestamp() - startTs;
                            actl = actr2;
                            actr = actr3;
                            long durationWithOverlapping = duration + overlappingDuration;
                            if (durationWithOverlapping != 0) {
                                recentActTs = recentActTs2;
                                batteryUsage.scale((duration * 1.0d) / durationWithOverlapping);
                                event.update(lastEvent, new com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage(batteryUsage));
                            } else {
                                recentActTs = recentActTs2;
                                event.update(lastEvent, com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE);
                            }
                            batteryUsage.setTo(com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE);
                            overlappingDuration = 0;
                        } else {
                            itr = itr2;
                            l = l2;
                            actl = actr2;
                            actr = actr3;
                            recentActTs = recentActTs2;
                        }
                        dest2 = dest;
                        dest2.add(event);
                    } else {
                        itr = itr2;
                        l = l2;
                        actl = actr2;
                        actr = actr3;
                        recentActTs = recentActTs2;
                        dest2 = dest;
                    }
                    dest3 = dest2;
                    itl = itl2;
                    itr2 = itr;
                    l2 = l;
                    actr2 = actl;
                    recentActTs2 = recentActTs;
                    actr3 = actr;
                } else {
                    return dest3;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimDurations() {
        long now = android.os.SystemClock.elapsedRealtime();
        trim(java.lang.Math.max(0L, now - ((com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy) this.mInjector.getPolicy()).getMaxTrackingDuration()));
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        ((com.android.server.am.AppBatteryExemptionTracker.AppBatteryExemptionPolicy) this.mInjector.getPolicy()).dump(pw, prefix);
    }

    static final class UidStateEventWithBattery extends com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent {
        private com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage mBatteryUsage;
        private boolean mIsStart;
        private com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery mPeer;

        UidStateEventWithBattery(boolean isStart, long now, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage batteryUsage, com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery peer) {
            super(now);
            this.mIsStart = isStart;
            this.mBatteryUsage = batteryUsage;
            this.mPeer = peer;
            if (peer != null) {
                peer.mPeer = this;
            }
        }

        UidStateEventWithBattery(com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery other) {
            super(other);
            this.mIsStart = other.mIsStart;
            this.mBatteryUsage = other.mBatteryUsage;
        }

        @Override // com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent
        void trimTo(long timestamp) {
            if (!this.mIsStart || timestamp < this.mTimestamp) {
                return;
            }
            if (this.mPeer != null) {
                com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage batteryUsage = this.mPeer.getBatteryUsage();
                this.mPeer.mBatteryUsage = this.mPeer.getBatteryUsage(timestamp, this.mPeer.mTimestamp);
                this.mBatteryUsage = this.mBatteryUsage.mutate().add(batteryUsage).subtract(this.mPeer.mBatteryUsage).unmutate();
            }
            this.mTimestamp = timestamp;
        }

        void update(com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery peer, com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage batteryUsage) {
            this.mPeer = peer;
            peer.mPeer = this;
            this.mBatteryUsage = batteryUsage;
        }

        boolean isStart() {
            return this.mIsStart;
        }

        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getBatteryUsage(long start, long end) {
            if (this.mIsStart || start >= this.mTimestamp || end <= start) {
                return com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE;
            }
            long start2 = java.lang.Math.max(start, this.mPeer.mTimestamp);
            long end2 = java.lang.Math.min(end, this.mTimestamp);
            long totalDur = this.mTimestamp - this.mPeer.mTimestamp;
            long inputDur = end2 - start2;
            return totalDur != 0 ? totalDur == inputDur ? this.mBatteryUsage : this.mBatteryUsage.mutate().scale((inputDur * 1.0d) / totalDur).unmutate() : com.android.server.am.AppBatteryTracker.BATTERY_USAGE_NONE;
        }

        com.android.server.am.AppBatteryTracker.ImmutableBatteryUsage getBatteryUsage() {
            return this.mBatteryUsage;
        }

        @Override // com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent
        public java.lang.Object clone() {
            return new com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery(this);
        }

        @Override // com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent
        public boolean equals(java.lang.Object other) {
            if (other == null || other.getClass() != com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery.class) {
                return false;
            }
            com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery otherEvent = (com.android.server.am.AppBatteryExemptionTracker.UidStateEventWithBattery) other;
            return otherEvent.mIsStart == this.mIsStart && otherEvent.mTimestamp == this.mTimestamp && this.mBatteryUsage.equals(otherEvent.mBatteryUsage);
        }

        public java.lang.String toString() {
            return "UidStateEventWithBattery(" + this.mIsStart + ", " + this.mTimestamp + ", " + this.mBatteryUsage + ")";
        }

        @Override // com.android.server.am.BaseAppStateTimeEvents.BaseTimeEvent
        public int hashCode() {
            return (((java.lang.Boolean.hashCode(this.mIsStart) * 31) + java.lang.Long.hashCode(this.mTimestamp)) * 31) + this.mBatteryUsage.hashCode();
        }
    }

    static final class AppBatteryExemptionPolicy extends com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy<com.android.server.am.AppBatteryExemptionTracker> {
        static final boolean DEFAULT_BG_BATTERY_EXEMPTION_ENABLED = false;
        static final java.lang.String KEY_BG_BATTERY_EXEMPTION_ENABLED = "bg_battery_exemption_enabled";

        AppBatteryExemptionPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, com.android.server.am.AppBatteryExemptionTracker tracker) {
            super(injector, tracker, KEY_BG_BATTERY_EXEMPTION_ENABLED, false, "bg_current_drain_window", tracker.mContext.getResources().getInteger(android.R.integer.config_batterySaver_full_soundTriggerMode));
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        public void onMaxTrackingDurationChanged(long maxDuration) {
            android.os.Handler handler = ((com.android.server.am.AppBatteryExemptionTracker) this.mTracker).mBgHandler;
            final com.android.server.am.AppBatteryExemptionTracker appBatteryExemptionTracker = (com.android.server.am.AppBatteryExemptionTracker) this.mTracker;
            java.util.Objects.requireNonNull(appBatteryExemptionTracker);
            handler.post(new java.lang.Runnable() { // from class: com.android.server.am.AppBatteryExemptionTracker$AppBatteryExemptionPolicy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    appBatteryExemptionTracker.trimDurations();
                }
            });
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean enabled) {
            ((com.android.server.am.AppBatteryExemptionTracker) this.mTracker).onTrackerEnabled(enabled);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("APP BATTERY EXEMPTION TRACKER POLICY SETTINGS:");
            super.dump(pw, "  " + prefix);
        }
    }
}
