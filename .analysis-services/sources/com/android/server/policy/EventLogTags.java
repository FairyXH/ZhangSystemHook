package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class EventLogTags {
    public static final int INTERCEPT_POWER = 70001;
    public static final int SCREEN_TOGGLED = 70000;

    private EventLogTags() {
    }

    public static void writeScreenToggled(int screenState) {
        android.util.EventLog.writeEvent(SCREEN_TOGGLED, screenState);
    }

    public static void writeInterceptPower(java.lang.String action, int mpowerkeyhandled, int mpowerkeypresscounter) {
        android.util.EventLog.writeEvent(INTERCEPT_POWER, action, java.lang.Integer.valueOf(mpowerkeyhandled), java.lang.Integer.valueOf(mpowerkeypresscounter));
    }
}
