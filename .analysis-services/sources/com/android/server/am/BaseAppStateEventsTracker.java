package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
abstract class BaseAppStateEventsTracker<T extends com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, U extends com.android.server.am.BaseAppStateEvents> extends com.android.server.am.BaseAppStateTracker<T> implements com.android.server.am.BaseAppStateEvents.Factory<U> {
    static final boolean DEBUG_BASE_APP_STATE_EVENTS_TRACKER = false;
    final com.android.server.am.UidProcessMap<U> mPkgEvents;
    final android.util.ArraySet<java.lang.Integer> mTopUids;

    BaseAppStateEventsTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<T>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mPkgEvents = new com.android.server.am.UidProcessMap<>();
        this.mTopUids = new android.util.ArraySet<>();
    }

    void reset() {
        synchronized (this.mLock) {
            this.mPkgEvents.clear();
            this.mTopUids.clear();
        }
    }

    U getUidEventsLocked(int uid) {
        U events = null;
        android.util.ArrayMap<java.lang.String, U> map = this.mPkgEvents.getMap().get(uid);
        if (map == null) {
            return null;
        }
        for (int i = map.size() - 1; i >= 0; i--) {
            U event = map.valueAt(i);
            if (event != null) {
                if (events == null) {
                    events = createAppStateEvents(uid, event.mPackageName);
                }
                events.add(event);
            }
        }
        return events;
    }

    void trim(long earliest) {
        synchronized (this.mLock) {
            trimLocked(earliest);
        }
    }

    void trimLocked(long earliest) {
        android.util.SparseArray<android.util.ArrayMap<java.lang.String, U>> map = this.mPkgEvents.getMap();
        for (int i = map.size() - 1; i >= 0; i--) {
            android.util.ArrayMap<java.lang.String, U> val = map.valueAt(i);
            for (int j = val.size() - 1; j >= 0; j--) {
                U v = val.valueAt(j);
                v.trim(earliest);
                if (v.isEmpty()) {
                    val.removeAt(j);
                }
            }
            int j2 = val.size();
            if (j2 == 0) {
                map.removeAt(i);
            }
        }
    }

    boolean isUidOnTop(int uid) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mTopUids.contains(java.lang.Integer.valueOf(uid));
        }
        return zContains;
    }

    void onUntrackingUidLocked(int uid) {
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUidProcStateChanged(int uid, int procState) {
        synchronized (this.mLock) {
            if (this.mPkgEvents.getMap().indexOfKey(uid) < 0) {
                return;
            }
            onUidProcStateChangedUncheckedLocked(uid, procState);
        }
    }

    void onUidProcStateChangedUncheckedLocked(int uid, int procState) {
        if (procState < 4) {
            this.mTopUids.add(java.lang.Integer.valueOf(uid));
        } else {
            this.mTopUids.remove(java.lang.Integer.valueOf(uid));
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUidGone(int uid) {
        synchronized (this.mLock) {
            this.mTopUids.remove(java.lang.Integer.valueOf(uid));
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUidRemoved(int uid) {
        synchronized (this.mLock) {
            this.mPkgEvents.getMap().remove(uid);
            onUntrackingUidLocked(uid);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onUserRemoved(int userId) {
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, U>> map = this.mPkgEvents.getMap();
            for (int i = map.size() - 1; i >= 0; i--) {
                int uid = map.keyAt(i);
                if (android.os.UserHandle.getUserId(uid) == userId) {
                    map.removeAt(i);
                    onUntrackingUidLocked(uid);
                }
            }
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) throws java.lang.Throwable {
        java.lang.Object obj;
        com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy baseAppStateEventsPolicy = (com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy) this.mInjector.getPolicy();
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                long now = android.os.SystemClock.elapsedRealtime();
                android.util.SparseArray<android.util.ArrayMap<java.lang.String, U>> map = this.mPkgEvents.getMap();
                int i = map.size() - 1;
                while (i >= 0) {
                    try {
                        int uid = map.keyAt(i);
                        android.util.ArrayMap<java.lang.String, U> val = map.valueAt(i);
                        int j = val.size() - 1;
                        while (j >= 0) {
                            java.lang.String packageName = val.keyAt(j);
                            U events = val.valueAt(j);
                            dumpEventHeaderLocked(pw, prefix, packageName, uid, events, baseAppStateEventsPolicy);
                            int j2 = j;
                            android.util.ArrayMap<java.lang.String, U> val2 = val;
                            int i2 = i;
                            obj = obj2;
                            try {
                                dumpEventLocked(pw, prefix, events, now);
                                j = j2 - 1;
                                val = val2;
                                i = i2;
                                obj2 = obj;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                while (true) {
                                    try {
                                        throw th;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                }
                            }
                        }
                        i--;
                    } catch (java.lang.Throwable th3) {
                        th = th3;
                        obj = obj2;
                        while (true) {
                            throw th;
                        }
                    }
                }
                obj = obj2;
                dumpOthers(pw, prefix);
                baseAppStateEventsPolicy.dump(pw, prefix);
            } catch (java.lang.Throwable th4) {
                th = th4;
                obj = obj2;
            }
        }
    }

    void dumpOthers(java.io.PrintWriter pw, java.lang.String prefix) {
    }

    void dumpEventHeaderLocked(java.io.PrintWriter pw, java.lang.String prefix, java.lang.String packageName, int uid, U events, T policy) {
        pw.print(prefix);
        pw.print("* ");
        pw.print(packageName);
        pw.print('/');
        pw.print(android.os.UserHandle.formatUid(uid));
        pw.print(" exemption=");
        pw.println(policy.getExemptionReasonString(packageName, uid, events.mExemptReason));
    }

    void dumpEventLocked(java.io.PrintWriter pw, java.lang.String prefix, U events, long now) {
        events.dump(pw, "  " + prefix, now);
    }

    static abstract class BaseAppStateEventsPolicy<V extends com.android.server.am.BaseAppStateEventsTracker> extends com.android.server.am.BaseAppStatePolicy<V> implements com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig {
        final long mDefaultMaxTrackingDuration;
        final java.lang.String mKeyMaxTrackingDuration;
        volatile long mMaxTrackingDuration;

        public abstract void onMaxTrackingDurationChanged(long j);

        BaseAppStateEventsPolicy(com.android.server.am.BaseAppStateTracker.Injector<?> injector, V tracker, java.lang.String keyTrackerEnabled, boolean defaultTrackerEnabled, java.lang.String keyMaxTrackingDuration, long defaultMaxTrackingDuration) {
            super(injector, tracker, keyTrackerEnabled, defaultTrackerEnabled);
            this.mKeyMaxTrackingDuration = keyMaxTrackingDuration;
            this.mDefaultMaxTrackingDuration = defaultMaxTrackingDuration;
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onPropertiesChanged(java.lang.String name) {
            if (this.mKeyMaxTrackingDuration.equals(name)) {
                updateMaxTrackingDuration();
            } else {
                super.onPropertiesChanged(name);
            }
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            super.onSystemReady();
            updateMaxTrackingDuration();
        }

        void updateMaxTrackingDuration() {
            long max = android.provider.DeviceConfig.getLong("activity_manager", this.mKeyMaxTrackingDuration, this.mDefaultMaxTrackingDuration);
            if (max != this.mMaxTrackingDuration) {
                this.mMaxTrackingDuration = max;
                onMaxTrackingDurationChanged(max);
            }
        }

        @Override // com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig
        public long getMaxTrackingDuration() {
            return this.mMaxTrackingDuration;
        }

        java.lang.String getExemptionReasonString(java.lang.String packageName, int uid, int reason) {
            return android.os.PowerExemptionManager.reasonCodeToString(reason);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            super.dump(pw, prefix);
            if (isEnabled()) {
                pw.print(prefix);
                pw.print(this.mKeyMaxTrackingDuration);
                pw.print('=');
                pw.println(this.mMaxTrackingDuration);
            }
        }
    }

    static class SimplePackageEvents extends com.android.server.am.BaseAppStateTimeEvents {
        static final int DEFAULT_INDEX = 0;

        /* JADX WARN: Multi-variable type inference failed */
        SimplePackageEvents(int uid, java.lang.String packageName, com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig maxTrackingDurationConfig) {
            super(uid, packageName, 1, com.android.server.am.IActivityManagerServiceExt.TAG, maxTrackingDurationConfig);
            this.mEvents[0] = new java.util.LinkedList();
        }

        long getTotalEvents(long now) {
            return getTotalEvents(now, 0);
        }

        long getTotalEventsSince(long since, long now) {
            return getTotalEventsSince(since, now, 0);
        }

        @Override // com.android.server.am.BaseAppStateEvents
        java.lang.String formatEventTypeLabel(int index) {
            return "";
        }
    }
}
