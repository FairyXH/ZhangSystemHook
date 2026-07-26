package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ReceiverList extends java.util.ArrayList<com.android.server.am.BroadcastFilter> implements android.os.IBinder.DeathRecipient {
    public final com.android.server.am.ProcessRecord app;
    com.android.server.am.BroadcastRecord curBroadcast = null;
    boolean linkedToDeath = false;
    final com.android.server.am.ActivityManagerService owner;
    public final int pid;
    public final android.content.IIntentReceiver receiver;
    java.lang.String stringName;
    public final int uid;
    public final int userId;

    ReceiverList(com.android.server.am.ActivityManagerService _owner, com.android.server.am.ProcessRecord _app, int _pid, int _uid, int _userId, android.content.IIntentReceiver _receiver) {
        this.owner = _owner;
        this.receiver = _receiver;
        this.app = _app;
        this.pid = _pid;
        this.uid = _uid;
        this.userId = _userId;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(java.lang.Object o) {
        return this == o;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        return java.lang.System.identityHashCode(this);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        this.linkedToDeath = false;
        this.owner.unregisterReceiver(this.receiver);
    }

    public boolean containsFilter(android.content.IntentFilter filter) {
        int N = size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.BroadcastFilter f = get(i);
            if (android.content.IntentFilter.filterEquals(f, filter)) {
                return true;
            }
        }
        return false;
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (this.app != null) {
            this.app.dumpDebug(proto, 1146756268033L);
            proto.write(1120986464265L, this.app.mReceivers.numberOfReceivers());
        }
        proto.write(1120986464258L, this.pid);
        proto.write(1120986464259L, this.uid);
        proto.write(1120986464260L, this.userId);
        if (this.curBroadcast != null) {
            this.curBroadcast.dumpDebug(proto, 1146756268037L);
        }
        proto.write(1133871366150L, this.linkedToDeath);
        int N = size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.BroadcastFilter bf = get(i);
            bf.dumpDebug(proto, 2246267895815L);
        }
        int i2 = java.lang.System.identityHashCode(this);
        proto.write(1138166333448L, java.lang.Integer.toHexString(i2));
        proto.end(token);
    }

    void dumpLocal(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("app=");
        pw.print(this.app != null ? this.app.toShortString() : null);
        pw.print(" pid=");
        pw.print(this.pid);
        pw.print(" uid=");
        pw.print(this.uid);
        pw.print(" user=");
        pw.print(this.userId);
        if (this.app != null) {
            pw.print(" #receivers=");
            pw.print(this.app.mReceivers.numberOfReceivers());
        }
        pw.println();
        if (this.curBroadcast != null || this.linkedToDeath) {
            pw.print(prefix);
            pw.print("curBroadcast=");
            pw.print(this.curBroadcast);
            pw.print(" linkedToDeath=");
            pw.println(this.linkedToDeath);
        }
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        android.util.Printer pr = new android.util.PrintWriterPrinter(pw);
        dumpLocal(pw, prefix);
        java.lang.String p2 = prefix + "  ";
        int N = size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.BroadcastFilter bf = get(i);
            pw.print(prefix);
            pw.print("Filter #");
            pw.print(i);
            pw.print(": BroadcastFilter{");
            pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(bf)));
            pw.println('}');
            bf.dumpInReceiverList(pw, pr, p2);
        }
    }

    @Override // java.util.AbstractCollection
    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ReceiverList{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.pid);
        sb.append(' ');
        sb.append(this.app != null ? this.app.processName : "(unknown name)");
        sb.append('/');
        sb.append(this.uid);
        sb.append("/u");
        sb.append(this.userId);
        sb.append(this.receiver.asBinder() instanceof android.os.Binder ? " local:" : " remote:");
        if (this.receiver.asBinder() instanceof android.os.Binder) {
            sb.append("" + this.receiver);
            sb.append(",");
        }
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.receiver.asBinder())));
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }
}
