package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppMediaSessionTracker extends com.android.server.am.BaseAppStateDurationsTracker<com.android.server.am.AppMediaSessionTracker.AppMediaSessionPolicy, com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations> {
    static final boolean DEBUG_MEDIA_SESSION_TRACKER = false;
    static final java.lang.String TAG = "ActivityManager";
    private final android.os.HandlerExecutor mHandlerExecutor;
    private final android.media.session.MediaSessionManager.OnActiveSessionsChangedListener mSessionsChangedListener;
    private final com.android.internal.app.ProcessMap<java.lang.Boolean> mTmpMediaControllers;

    AppMediaSessionTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller) {
        this(context, controller, null, null);
    }

    AppMediaSessionTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<com.android.server.am.AppMediaSessionTracker.AppMediaSessionPolicy>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mSessionsChangedListener = new android.media.session.MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.android.server.am.AppMediaSessionTracker$$ExternalSyntheticLambda0
            @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
            public final void onActiveSessionsChanged(java.util.List list) {
                this.f$0.handleMediaSessionChanged(list);
            }
        };
        this.mTmpMediaControllers = new com.android.internal.app.ProcessMap<>();
        this.mHandlerExecutor = new android.os.HandlerExecutor(this.mBgHandler);
        this.mInjector.setPolicy(new com.android.server.am.AppMediaSessionTracker.AppMediaSessionPolicy(this.mInjector, this));
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations createAppStateEvents(int uid, java.lang.String packageName) {
        return new com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations(uid, packageName, (com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations createAppStateEvents(com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations other) {
        return new com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations(other);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBgMediaSessionMonitorEnabled(boolean enabled) {
        if (enabled) {
            this.mInjector.getMediaSessionManager().addOnActiveSessionsChangedListener(null, android.os.UserHandle.ALL, this.mHandlerExecutor, this.mSessionsChangedListener);
        } else {
            this.mInjector.getMediaSessionManager().removeOnActiveSessionsChangedListener(this.mSessionsChangedListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
    public void handleMediaSessionChanged(java.util.List<android.media.session.MediaController> controllers) {
        com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations pkg;
        int uid;
        if (controllers != null) {
            synchronized (this.mLock) {
                long now = android.os.SystemClock.elapsedRealtime();
                for (android.media.session.MediaController controller : controllers) {
                    java.lang.String packageName = controller.getPackageName();
                    int uid2 = controller.getSessionToken().getUid();
                    com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations pkg2 = (com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations) this.mPkgEvents.get(uid2, packageName);
                    if (pkg2 != null) {
                        pkg = pkg2;
                    } else {
                        com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations pkg3 = createAppStateEvents(uid2, packageName);
                        this.mPkgEvents.put(uid2, packageName, pkg3);
                        pkg = pkg3;
                    }
                    if (pkg.isActive()) {
                        uid = uid2;
                    } else {
                        pkg.addEvent(true, now);
                        uid = uid2;
                        notifyListenersOnStateChange(pkg.mUid, pkg.mPackageName, true, now, 1);
                    }
                    this.mTmpMediaControllers.put(packageName, uid, java.lang.Boolean.TRUE);
                }
                android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations>> map = this.mPkgEvents.getMap();
                for (int i = map.size() - 1; i >= 0; i--) {
                    android.util.ArrayMap<java.lang.String, com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations> val = map.valueAt(i);
                    for (int j = val.size() - 1; j >= 0; j--) {
                        com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations pkg4 = val.valueAt(j);
                        if (pkg4.isActive() && this.mTmpMediaControllers.get(pkg4.mPackageName, pkg4.mUid) == null) {
                            pkg4.addEvent(false, now);
                            notifyListenersOnStateChange(pkg4.mUid, pkg4.mPackageName, false, now, 1);
                        }
                    }
                }
            }
            this.mTmpMediaControllers.clear();
            return;
        }
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations>> map2 = this.mPkgEvents.getMap();
            long now2 = android.os.SystemClock.elapsedRealtime();
            for (int i2 = map2.size() - 1; i2 >= 0; i2--) {
                android.util.ArrayMap<java.lang.String, com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations> val2 = map2.valueAt(i2);
                for (int j2 = val2.size() - 1; j2 >= 0; j2--) {
                    com.android.server.am.BaseAppStateDurationsTracker.SimplePackageDurations pkg5 = val2.valueAt(j2);
                    if (pkg5.isActive()) {
                        pkg5.addEvent(false, now2);
                        notifyListenersOnStateChange(pkg5.mUid, pkg5.mPackageName, false, now2, 1);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimDurations() {
        long now = android.os.SystemClock.elapsedRealtime();
        trim(java.lang.Math.max(0L, now - ((com.android.server.am.AppMediaSessionTracker.AppMediaSessionPolicy) this.mInjector.getPolicy()).getMaxTrackingDuration()));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    int getType() {
        return 4;
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println("APP MEDIA SESSION TRACKER:");
        super.dump(pw, "  " + prefix);
    }

    static final class AppMediaSessionPolicy extends com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy<com.android.server.am.AppMediaSessionTracker> {
        static final boolean DEFAULT_BG_MEDIA_SESSION_MONITOR_ENABLED = true;
        static final long DEFAULT_BG_MEDIA_SESSION_MONITOR_MAX_TRACKING_DURATION = 345600000;
        static final java.lang.String KEY_BG_MEADIA_SESSION_MONITOR_ENABLED = "bg_media_session_monitor_enabled";
        static final java.lang.String KEY_BG_MEDIA_SESSION_MONITOR_MAX_TRACKING_DURATION = "bg_media_session_monitor_max_tracking_duration";

        AppMediaSessionPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, com.android.server.am.AppMediaSessionTracker tracker) {
            super(injector, tracker, KEY_BG_MEADIA_SESSION_MONITOR_ENABLED, true, KEY_BG_MEDIA_SESSION_MONITOR_MAX_TRACKING_DURATION, DEFAULT_BG_MEDIA_SESSION_MONITOR_MAX_TRACKING_DURATION);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean enabled) {
            ((com.android.server.am.AppMediaSessionTracker) this.mTracker).onBgMediaSessionMonitorEnabled(enabled);
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        public void onMaxTrackingDurationChanged(long maxDuration) {
            android.os.Handler handler = ((com.android.server.am.AppMediaSessionTracker) this.mTracker).mBgHandler;
            final com.android.server.am.AppMediaSessionTracker appMediaSessionTracker = (com.android.server.am.AppMediaSessionTracker) this.mTracker;
            java.util.Objects.requireNonNull(appMediaSessionTracker);
            handler.post(new java.lang.Runnable() { // from class: com.android.server.am.AppMediaSessionTracker$AppMediaSessionPolicy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    appMediaSessionTracker.trimDurations();
                }
            });
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy
        java.lang.String getExemptionReasonString(java.lang.String packageName, int uid, int reason) {
            return "n/a";
        }

        @Override // com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("APP MEDIA SESSION TRACKER POLICY SETTINGS:");
            super.dump(pw, "  " + prefix);
        }
    }
}
