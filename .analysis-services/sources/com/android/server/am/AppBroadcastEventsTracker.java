package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppBroadcastEventsTracker extends com.android.server.am.BaseAppStateTimeSlotEventsTracker<com.android.server.am.AppBroadcastEventsTracker.AppBroadcastEventsPolicy, com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents> implements android.app.ActivityManagerInternal.BroadcastEventListener {
    static final boolean DEBUG_APP_STATE_BROADCAST_EVENT_TRACKER = false;
    static final java.lang.String TAG = "ActivityManager";

    AppBroadcastEventsTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller) {
        this(context, controller, null, null);
    }

    AppBroadcastEventsTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<com.android.server.am.AppBroadcastEventsTracker.AppBroadcastEventsPolicy>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mInjector.setPolicy(new com.android.server.am.AppBroadcastEventsTracker.AppBroadcastEventsPolicy(this.mInjector, this));
    }

    public void onSendingBroadcast(java.lang.String packageName, int uid) {
        if (((com.android.server.am.AppBroadcastEventsTracker.AppBroadcastEventsPolicy) this.mInjector.getPolicy()).isEnabled()) {
            onNewEvent(packageName, uid);
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    int getType() {
        return 6;
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onSystemReady() {
        super.onSystemReady();
        this.mInjector.getActivityManagerInternal().addBroadcastEventListener(this);
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents createAppStateEvents(int uid, java.lang.String packageName) {
        return new com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents(uid, packageName, ((com.android.server.am.AppBroadcastEventsTracker.AppBroadcastEventsPolicy) this.mInjector.getPolicy()).getTimeSlotSize(), "ActivityManager", (com.android.server.am.BaseAppStateEvents.MaxTrackingDurationConfig) this.mInjector.getPolicy());
    }

    @Override // com.android.server.am.BaseAppStateEvents.Factory
    public com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents createAppStateEvents(com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents other) {
        return new com.android.server.am.BaseAppStateTimeSlotEventsTracker.SimpleAppStateTimeslotEvents(other);
    }

    @Override // com.android.server.am.BaseAppStateTracker
    byte[] getTrackerInfoForStatsd(int uid) {
        long now = android.os.SystemClock.elapsedRealtime();
        int numOfBroadcasts = getTotalEventsLocked(uid, now);
        if (numOfBroadcasts == 0) {
            return null;
        }
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        proto.write(1120986464257L, numOfBroadcasts);
        proto.flush();
        return proto.getBytes();
    }

    @Override // com.android.server.am.BaseAppStateEventsTracker, com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.println("APP BROADCAST EVENT TRACKER:");
        super.dump(pw, "  " + prefix);
    }

    static final class AppBroadcastEventsPolicy extends com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy<com.android.server.am.AppBroadcastEventsTracker> {
        static final boolean DEFAULT_BG_BROADCAST_MONITOR_ENABLED = true;
        static final long DEFAULT_BG_BROADCAST_WINDOW = 86400000;
        static final int DEFAULT_BG_EX_BROADCAST_THRESHOLD = 10000;
        static final java.lang.String KEY_BG_BROADCAST_MONITOR_ENABLED = "bg_broadcast_monitor_enabled";
        static final java.lang.String KEY_BG_BROADCAST_WINDOW = "bg_broadcast_window";
        static final java.lang.String KEY_BG_EX_BROADCAST_THRESHOLD = "bg_ex_broadcast_threshold";

        AppBroadcastEventsPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, com.android.server.am.AppBroadcastEventsTracker tracker) {
            super(injector, tracker, KEY_BG_BROADCAST_MONITOR_ENABLED, true, KEY_BG_BROADCAST_WINDOW, 86400000L, KEY_BG_EX_BROADCAST_THRESHOLD, 10000);
        }

        @Override // com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy
        java.lang.String getEventName() {
            return "broadcast";
        }

        @Override // com.android.server.am.BaseAppStateTimeSlotEventsTracker.BaseAppStateTimeSlotEventsPolicy, com.android.server.am.BaseAppStateEventsTracker.BaseAppStateEventsPolicy, com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("APP BROADCAST EVENT TRACKER POLICY SETTINGS:");
            super.dump(pw, "  " + prefix);
        }
    }
}
