package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ConnectionRecord implements com.android.server.am.OomAdjusterModernImpl.Connection {
    private static final int[] BIND_ORIG_ENUMS = {1, 2, 4, 8388608, 8, 16, 32, 64, 128, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 256, 4096, 512};
    private static final int[] BIND_PROTO_ENUMS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18};
    final com.android.server.wm.ActivityServiceConnectionsHolder<com.android.server.am.ConnectionRecord> activity;
    final android.content.ComponentName aliasComponent;
    public com.android.internal.app.procstats.AssociationState.SourceState association;
    final com.android.server.am.AppBindRecord binding;
    final android.app.PendingIntent clientIntent;
    final int clientLabel;
    final java.lang.String clientPackageName;
    final java.lang.String clientProcessName;
    final int clientUid;
    final android.app.IServiceConnection conn;
    private final long flags;
    private java.lang.Object mProcStatsLock;
    boolean serviceDead;
    java.lang.String stringName;

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "binding=" + this.binding);
        if (this.activity != null) {
            this.activity.dump(pw, prefix);
        }
        pw.println(prefix + "conn=" + this.conn.asBinder() + " flags=0x" + java.lang.Long.toHexString(this.flags));
    }

    ConnectionRecord(com.android.server.am.AppBindRecord _binding, com.android.server.wm.ActivityServiceConnectionsHolder<com.android.server.am.ConnectionRecord> _activity, android.app.IServiceConnection _conn, long _flags, int _clientLabel, android.app.PendingIntent _clientIntent, int _clientUid, java.lang.String _clientProcessName, java.lang.String _clientPackageName, android.content.ComponentName _aliasComponent) {
        this.binding = _binding;
        this.activity = _activity;
        this.conn = _conn;
        this.flags = _flags;
        this.clientLabel = _clientLabel;
        this.clientIntent = _clientIntent;
        this.clientUid = _clientUid;
        this.clientProcessName = _clientProcessName;
        this.clientPackageName = _clientPackageName;
        this.aliasComponent = _aliasComponent;
    }

    @Override // com.android.server.am.OomAdjusterModernImpl.Connection
    public void computeHostOomAdjLSP(com.android.server.am.OomAdjuster oomAdjuster, com.android.server.am.ProcessRecord host, com.android.server.am.ProcessRecord client, long now, com.android.server.am.ProcessRecord topApp, boolean doingAll, int oomAdjReason, int cachedAdj) {
        oomAdjuster.computeServiceHostOomAdjLSP(this, host, client, now, topApp, doingAll, false, false, oomAdjReason, 1001, false, false);
    }

    @Override // com.android.server.am.OomAdjusterModernImpl.Connection
    public boolean canAffectCapabilities() {
        return hasFlag(4294971392L);
    }

    public long getFlags() {
        return this.flags;
    }

    public boolean hasFlag(int flag) {
        return (this.flags & java.lang.Integer.toUnsignedLong(flag)) != 0;
    }

    public boolean hasFlag(long flag) {
        return (this.flags & flag) != 0;
    }

    public boolean notHasFlag(int flag) {
        return !hasFlag(flag);
    }

    public boolean notHasFlag(long flag) {
        return !hasFlag(flag);
    }

    public void startAssociationIfNeeded() {
        if (this.association == null && this.binding.service.app != null) {
            if (this.binding.service.appInfo.uid != this.clientUid || !this.binding.service.processName.equals(this.clientProcessName)) {
                com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder = this.binding.service.app.getPkgList().get(this.binding.service.instanceName.getPackageName());
                if (holder == null) {
                    android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "No package in referenced service " + this.binding.service.shortInstanceName + ": proc=" + this.binding.service.app);
                    return;
                }
                if (holder.pkg == null) {
                    android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "Inactive holder in referenced service " + this.binding.service.shortInstanceName + ": proc=" + this.binding.service.app);
                    return;
                }
                this.mProcStatsLock = this.binding.service.app.mService.mProcessStats.mLock;
                synchronized (this.mProcStatsLock) {
                    this.association = holder.pkg.getAssociationStateLocked(holder.state, this.binding.service.instanceName.getClassName()).startSource(this.clientUid, this.clientProcessName, this.clientPackageName);
                }
            }
        }
    }

    public void trackProcState(int procState, int seq) {
        if (this.association != null) {
            synchronized (this.mProcStatsLock) {
                this.association.trackProcState(procState, seq, android.os.SystemClock.uptimeMillis());
            }
        }
    }

    public void stopAssociation() {
        if (this.association != null) {
            synchronized (this.mProcStatsLock) {
                this.association.stop();
            }
            this.association = null;
        }
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ConnectionRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" u");
        sb.append(this.binding.client.userId);
        sb.append(' ');
        if (hasFlag(1)) {
            sb.append("CR ");
        }
        if (hasFlag(2)) {
            sb.append("DBG ");
        }
        if (hasFlag(4)) {
            sb.append("!FG ");
        }
        if (hasFlag(8388608)) {
            sb.append("IMPB ");
        }
        if (hasFlag(8)) {
            sb.append("ABCLT ");
        }
        if (hasFlag(16)) {
            sb.append("OOM ");
        }
        if (hasFlag(32)) {
            sb.append("WPRI ");
        }
        if (hasFlag(64)) {
            sb.append("IMP ");
        }
        if (hasFlag(128)) {
            sb.append("WACT ");
        }
        if (hasFlag(33554432)) {
            sb.append("FGSA ");
        }
        if (hasFlag(67108864)) {
            sb.append("FGS ");
        }
        if (hasFlag(134217728)) {
            sb.append("LACT ");
        }
        if (hasFlag(524288)) {
            sb.append("SLTA ");
        }
        if (hasFlag(268435456)) {
            sb.append("VFGS ");
        }
        if (hasFlag(536870912)) {
            sb.append("UI ");
        }
        if (hasFlag(1073741824)) {
            sb.append("!VIS ");
        }
        if (hasFlag(256)) {
            sb.append("!PRCP ");
        }
        if (hasFlag(512)) {
            sb.append("BALF ");
        }
        if (hasFlag(4096)) {
            sb.append("CAPS ");
        }
        if (this.serviceDead) {
            sb.append("DEAD ");
        }
        sb.append(this.binding.service.shortInstanceName);
        sb.append(":@");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.conn.asBinder())));
        sb.append(" flags=0x" + java.lang.Long.toHexString(this.flags));
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        if (this.binding == null) {
            return;
        }
        long token = proto.start(fieldId);
        proto.write(1138166333441L, java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        if (this.binding.client != null) {
            proto.write(1120986464258L, this.binding.client.userId);
        }
        android.util.proto.ProtoUtils.writeBitWiseFlagsToProtoEnum(proto, 2259152797699L, this.flags, BIND_ORIG_ENUMS, BIND_PROTO_ENUMS);
        if (this.serviceDead) {
            proto.write(2259152797699L, 15);
        }
        if (this.binding.service != null) {
            proto.write(1138166333444L, this.binding.service.shortInstanceName);
        }
        proto.end(token);
    }
}
