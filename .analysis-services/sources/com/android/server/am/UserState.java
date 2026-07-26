package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class UserState {
    public static final int STATE_BOOTING = 0;
    public static final int STATE_NONE = -1;
    public static final int STATE_RUNNING_LOCKED = 1;
    public static final int STATE_RUNNING_UNLOCKED = 3;
    public static final int STATE_RUNNING_UNLOCKING = 2;
    public static final int STATE_SHUTDOWN = 5;
    public static final int STATE_STOPPING = 4;
    private static final java.lang.String TAG = "ActivityManager";
    public final android.os.UserHandle mHandle;
    public final com.android.internal.util.ProgressReporter mUnlockProgress;
    public boolean switching;
    public final java.util.ArrayList<android.app.IStopUserCallback> mStopCallbacks = new java.util.ArrayList<>();
    public final java.util.ArrayList<com.android.server.am.UserState.KeyEvictedCallback> mKeyEvictedCallbacks = new java.util.ArrayList<>();
    public int state = 0;
    public int lastState = 0;
    final android.util.ArrayMap<java.lang.String, java.lang.Long> mProviderLastReportedFg = new android.util.ArrayMap<>();

    public interface KeyEvictedCallback {
        void keyEvicted(int i);
    }

    public UserState(android.os.UserHandle handle) {
        this.mHandle = handle;
        this.mUnlockProgress = new com.android.internal.util.ProgressReporter(handle.getIdentifier());
    }

    public boolean setState(int oldState, int newState) {
        if (this.state == oldState) {
            setState(newState);
            return true;
        }
        android.util.Slog.w("ActivityManager", "Expected user " + this.mHandle.getIdentifier() + " in state " + stateToString(oldState) + " but was in state " + stateToString(this.state));
        return false;
    }

    public void setState(int newState) {
        if (newState == this.state) {
            return;
        }
        int userId = this.mHandle.getIdentifier();
        if (this.state != 0) {
            android.os.Trace.asyncTraceEnd(64L, stateToString(this.state) + " " + userId, userId);
        }
        if (newState != 5) {
            android.os.Trace.asyncTraceBegin(64L, stateToString(newState) + " " + userId, userId);
        }
        android.util.Slog.i("ActivityManager", "User " + userId + " state changed from " + stateToString(this.state) + " to " + stateToString(newState));
        com.android.server.am.EventLogTags.writeAmUserStateChanged(userId, newState);
        this.lastState = this.state;
        this.state = newState;
    }

    public static java.lang.String stateToString(int state) {
        switch (state) {
            case 0:
                return "BOOTING";
            case 1:
                return "RUNNING_LOCKED";
            case 2:
                return "RUNNING_UNLOCKING";
            case 3:
                return "RUNNING_UNLOCKED";
            case 4:
                return "STOPPING";
            case 5:
                return "SHUTDOWN";
            default:
                return java.lang.Integer.toString(state);
        }
    }

    public static int stateToProtoEnum(int state) {
        switch (state) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return state;
        }
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("state=");
        pw.print(stateToString(this.state));
        if (this.switching) {
            pw.print(" SWITCHING");
        }
        pw.println();
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1159641169921L, stateToProtoEnum(this.state));
        proto.write(1133871366146L, this.switching);
        proto.end(token);
    }

    public java.lang.String toString() {
        return "[UserState: id=" + this.mHandle.getIdentifier() + ", state=" + stateToString(this.state) + ", lastState=" + stateToString(this.lastState) + ", switching=" + this.switching + "]";
    }
}
