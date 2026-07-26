package com.android.server.alarm;

/* JADX INFO: loaded from: classes.dex */
public class Alarm {
    public static final int ADJUSTMENT_POLICY_INDEX = 4;
    public static final int APP_STANDBY_POLICY_INDEX = 1;
    public static final int BATTERY_SAVER_POLICY_INDEX = 3;
    public static final int DEVICE_IDLE_POLICY_INDEX = 2;
    static final int EXACT_ALLOW_REASON_ALLOW_LIST = 1;
    static final int EXACT_ALLOW_REASON_COMPAT = 2;
    static final int EXACT_ALLOW_REASON_LISTENER = 4;
    static final int EXACT_ALLOW_REASON_NOT_APPLICABLE = -1;
    static final int EXACT_ALLOW_REASON_PERMISSION = 0;
    static final int EXACT_ALLOW_REASON_POLICY_PERMISSION = 3;
    static final int EXACT_ALLOW_REASON_PRIORITIZED = 5;
    public static final int NUM_POLICIES = 5;
    public static final int REQUESTER_POLICY_INDEX = 0;
    public final android.app.AlarmManager.AlarmClockInfo alarmClock;
    public int count;
    public final int creatorUid;
    public int exactAllowReason;
    public final int flags;
    public final android.app.IAlarmListener listener;
    public final java.lang.String listenerTag;
    public android.os.Bundle mIdleOptions;
    private long mMaxWhenElapsed;
    private long[] mPolicyWhenElapsed;
    public boolean mUsingReserveQuota;
    private long mWhenElapsed;
    public final android.app.PendingIntent operation;
    public final long origWhen;
    public final java.lang.String packageName;
    public int priorityClass;
    public final long repeatInterval;
    public final java.lang.String sourcePackage;
    public java.lang.String statsTag;
    public int type;
    public final int uid;
    public final boolean wakeup;
    public final long windowLength;
    public final android.os.WorkSource workSource;
    private com.android.server.alarm.Alarm.AlarmWrapper mAlarmWrapper = new com.android.server.alarm.Alarm.AlarmWrapper();
    private com.android.server.alarm.IAlarmExt mAlarmExt = (com.android.server.alarm.IAlarmExt) system.ext.loader.core.ExtLoader.type(com.android.server.alarm.IAlarmExt.class).base(this).create();

    Alarm(int type, long when, long requestedWhenElapsed, long windowLength, long interval, android.app.PendingIntent op, android.app.IAlarmListener rec, java.lang.String listenerTag, android.os.WorkSource ws, int flags, android.app.AlarmManager.AlarmClockInfo info, int uid, java.lang.String pkgName, android.os.Bundle idleOptions, int exactAllowReason) {
        this.type = type;
        this.origWhen = when;
        this.wakeup = type == 2 || type == 0;
        this.mPolicyWhenElapsed = new long[5];
        this.mPolicyWhenElapsed[0] = requestedWhenElapsed;
        this.mWhenElapsed = requestedWhenElapsed;
        this.windowLength = windowLength;
        this.mMaxWhenElapsed = com.android.server.alarm.AlarmManagerService.addClampPositive(requestedWhenElapsed, windowLength);
        this.repeatInterval = interval;
        this.operation = op;
        this.listener = rec;
        this.listenerTag = listenerTag;
        this.statsTag = makeTag(op, listenerTag, type);
        this.workSource = ws;
        this.flags = flags;
        this.alarmClock = info;
        this.uid = uid;
        this.packageName = pkgName;
        this.mIdleOptions = idleOptions;
        this.exactAllowReason = exactAllowReason;
        this.sourcePackage = this.operation != null ? this.operation.getCreatorPackage() : this.packageName;
        this.creatorUid = this.operation != null ? this.operation.getCreatorUid() : this.uid;
        this.mUsingReserveQuota = false;
        this.priorityClass = 2;
    }

    public static java.lang.String makeTag(android.app.PendingIntent pi, java.lang.String tag, int type) {
        java.lang.String alarmString = (type == 2 || type == 0) ? "*walarm*:" : "*alarm*:";
        return pi != null ? pi.getTag(alarmString) : alarmString + tag;
    }

    public boolean matches(android.app.PendingIntent pi, android.app.IAlarmListener rec) {
        if (this.operation != null) {
            return this.operation.equals(pi);
        }
        return rec != null && this.listener.asBinder().equals(rec.asBinder());
    }

    public boolean matches(java.lang.String packageName) {
        return packageName.equals(this.sourcePackage);
    }

    long getPolicyElapsed(int policyIndex) {
        return this.mPolicyWhenElapsed[policyIndex];
    }

    public long getRequestedElapsed() {
        return this.mPolicyWhenElapsed[0];
    }

    public long getWhenElapsed() {
        return this.mWhenElapsed;
    }

    public long getMaxWhenElapsed() {
        return this.mMaxWhenElapsed;
    }

    public boolean setPolicyElapsed(int policyIndex, long policyElapsed) {
        this.mPolicyWhenElapsed[policyIndex] = policyElapsed;
        return updateWhenElapsed();
    }

    private boolean updateWhenElapsed() {
        long oldWhenElapsed = this.mWhenElapsed;
        this.mWhenElapsed = 0L;
        long oldMaxWhenElapsed = this.mMaxWhenElapsed;
        if (this.mPolicyWhenElapsed[4] != 0) {
            long alignedElapsed = this.mPolicyWhenElapsed[4];
            this.mWhenElapsed = alignedElapsed;
            this.mMaxWhenElapsed = alignedElapsed;
            return (oldWhenElapsed == alignedElapsed && oldMaxWhenElapsed == alignedElapsed) ? false : true;
        }
        for (int i = 0; i < 5; i++) {
            this.mWhenElapsed = java.lang.Math.max(this.mWhenElapsed, this.mPolicyWhenElapsed[i]);
        }
        long maxRequestedElapsed = com.android.server.alarm.AlarmManagerService.addClampPositive(this.mPolicyWhenElapsed[0], this.windowLength);
        this.mMaxWhenElapsed = java.lang.Math.max(maxRequestedElapsed, this.mWhenElapsed);
        return (oldWhenElapsed == this.mWhenElapsed && oldMaxWhenElapsed == this.mMaxWhenElapsed) ? false : true;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Alarm{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" type ");
        sb.append(this.type);
        sb.append(" origWhen ");
        sb.append(this.origWhen);
        sb.append(" whenElapsed ");
        sb.append(getWhenElapsed());
        sb.append(" ");
        sb.append(this.sourcePackage);
        sb.append('}');
        this.mAlarmExt.alarmToStringExtend(sb, getWhenElapsed(), this.windowLength, this.mMaxWhenElapsed, this.repeatInterval, this.operation, this.listenerTag, this.flags, this.uid);
        return sb.toString();
    }

    static java.lang.String policyIndexToString(int index) {
        switch (index) {
            case 0:
                return "requester";
            case 1:
                return "app_standby";
            case 2:
                return "device_idle";
            case 3:
                return "battery_saver";
            case 4:
                return "adjustment";
            default:
                return "--unknown(" + index + ")--";
        }
    }

    private static java.lang.String exactReasonToString(int reason) {
        switch (reason) {
            case -1:
                return "N/A";
            case 0:
                return com.android.server.permission.access.PermissionUri.SCHEME;
            case 1:
                return "allow-listed";
            case 2:
                return "compat";
            case 3:
                return "policy_permission";
            case 4:
                return "listener";
            case 5:
                return "prioritized";
            default:
                return "--unknown--";
        }
    }

    public static java.lang.String typeToString(int type) {
        switch (type) {
            case 0:
                return "RTC_WAKEUP";
            case 1:
                return "RTC";
            case 2:
                return "ELAPSED_WAKEUP";
            case 3:
                return "ELAPSED";
            default:
                return "--unknown--";
        }
    }

    public void dump(android.util.IndentingPrintWriter ipw, long nowELAPSED, java.text.SimpleDateFormat sdf) {
        boolean z = true;
        if (this.type != 1 && this.type != 0) {
            z = false;
        }
        boolean isRtc = z;
        ipw.print("tag=");
        ipw.println(this.statsTag);
        ipw.print("type=");
        ipw.print(typeToString(this.type));
        ipw.print(" origWhen=");
        if (isRtc) {
            ipw.print(sdf.format(new java.util.Date(this.origWhen)));
        } else {
            android.util.TimeUtils.formatDuration(this.origWhen, nowELAPSED, ipw);
        }
        ipw.print(" window=");
        android.util.TimeUtils.formatDuration(this.windowLength, ipw);
        if (this.exactAllowReason != -1) {
            ipw.print(" exactAllowReason=");
            ipw.print(exactReasonToString(this.exactAllowReason));
        }
        ipw.print(" repeatInterval=");
        ipw.print(this.repeatInterval);
        ipw.print(" count=");
        ipw.print(this.count);
        ipw.print(" flags=0x");
        ipw.println(java.lang.Integer.toHexString(this.flags));
        ipw.print("policyWhenElapsed:");
        for (int i = 0; i < 5; i++) {
            ipw.print(" " + policyIndexToString(i) + "=");
            android.util.TimeUtils.formatDuration(this.mPolicyWhenElapsed[i], nowELAPSED, ipw);
        }
        ipw.println();
        ipw.print("whenElapsed=");
        android.util.TimeUtils.formatDuration(getWhenElapsed(), nowELAPSED, ipw);
        ipw.print(" maxWhenElapsed=");
        android.util.TimeUtils.formatDuration(this.mMaxWhenElapsed, nowELAPSED, ipw);
        if (this.mUsingReserveQuota) {
            ipw.print(" usingReserveQuota=true");
        }
        ipw.println();
        if (this.alarmClock != null) {
            ipw.println("Alarm clock:");
            ipw.print("  triggerTime=");
            ipw.println(sdf.format(new java.util.Date(this.alarmClock.getTriggerTime())));
            ipw.print("  showIntent=");
            ipw.println(this.alarmClock.getShowIntent());
        }
        if (this.operation != null) {
            ipw.print("operation=");
            ipw.println(this.operation);
        }
        if (this.listener != null) {
            ipw.print("listener=");
            ipw.println(this.listener.asBinder());
        }
        if (this.mIdleOptions != null) {
            ipw.print("idle-options=");
            ipw.println(this.mIdleOptions.toString());
        }
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, long nowElapsed) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.statsTag);
        proto.write(1159641169922L, this.type);
        proto.write(1112396529667L, getWhenElapsed() - nowElapsed);
        proto.write(1112396529668L, this.windowLength);
        proto.write(1112396529669L, this.repeatInterval);
        proto.write(1120986464262L, this.count);
        proto.write(1120986464263L, this.flags);
        if (this.alarmClock != null) {
            this.alarmClock.dumpDebug(proto, 1146756268040L);
        }
        if (this.operation != null) {
            this.operation.dumpDebug(proto, 1146756268041L);
        }
        if (this.listener != null) {
            proto.write(1138166333450L, this.listener.asBinder().toString());
        }
        proto.end(token);
    }

    static class Snapshot {
        final long[] mPolicyWhenElapsed;
        final java.lang.String mTag;
        final int mType;

        Snapshot(com.android.server.alarm.Alarm a) {
            this.mType = a.type;
            this.mTag = a.statsTag;
            this.mPolicyWhenElapsed = java.util.Arrays.copyOf(a.mPolicyWhenElapsed, 5);
        }

        void dump(android.util.IndentingPrintWriter pw, long nowElapsed) {
            pw.print("type", com.android.server.alarm.Alarm.typeToString(this.mType));
            pw.print("tag", this.mTag);
            pw.println();
            pw.print("policyWhenElapsed:");
            for (int i = 0; i < 5; i++) {
                pw.print(" " + com.android.server.alarm.Alarm.policyIndexToString(i) + "=");
                android.util.TimeUtils.formatDuration(this.mPolicyWhenElapsed[i], nowElapsed, pw);
            }
            pw.println();
        }
    }

    public com.android.server.alarm.IAlarmWrapper getWrapper() {
        return this.mAlarmWrapper;
    }

    private class AlarmWrapper implements com.android.server.alarm.IAlarmWrapper {
        private AlarmWrapper() {
        }

        @Override // com.android.server.alarm.IAlarmWrapper
        public com.android.server.alarm.IAlarmExt getExt() {
            return com.android.server.alarm.Alarm.this.mAlarmExt;
        }
    }
}
