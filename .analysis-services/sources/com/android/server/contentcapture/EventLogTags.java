package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
public class EventLogTags {
    public static final int CC_CONNECT_STATE_CHANGED = 53200;
    public static final int CC_CURRENT_ALLOWLIST = 53202;
    public static final int CC_SET_ALLOWLIST = 53201;
    public static final int CC_UPDATE_OPTIONS = 53203;

    private EventLogTags() {
    }

    public static void writeCcConnectStateChanged(int user, int type, int packageCount) {
        android.util.EventLog.writeEvent(CC_CONNECT_STATE_CHANGED, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(type), java.lang.Integer.valueOf(packageCount));
    }

    public static void writeCcSetAllowlist(int user, int packageCount, int activityCount) {
        android.util.EventLog.writeEvent(CC_SET_ALLOWLIST, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(packageCount), java.lang.Integer.valueOf(activityCount));
    }

    public static void writeCcCurrentAllowlist(int user, int count) {
        android.util.EventLog.writeEvent(CC_CURRENT_ALLOWLIST, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(count));
    }

    public static void writeCcUpdateOptions(int user, int count) {
        android.util.EventLog.writeEvent(CC_UPDATE_OPTIONS, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(count));
    }
}
