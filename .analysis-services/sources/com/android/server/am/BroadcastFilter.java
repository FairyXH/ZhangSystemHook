package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class BroadcastFilter extends android.content.IntentFilter {
    public final boolean exported;
    final java.lang.String featureId;
    final boolean instantApp;
    final int owningUid;
    final int owningUserId;
    final java.lang.String packageName;
    final java.lang.String receiverId;
    final com.android.server.am.ReceiverList receiverList;
    final java.lang.String requiredPermission;
    final boolean visibleToInstantApp;

    BroadcastFilter(android.content.IntentFilter _filter, com.android.server.am.ReceiverList _receiverList, java.lang.String _packageName, java.lang.String _featureId, java.lang.String _receiverId, java.lang.String _requiredPermission, int _owningUid, int _userId, boolean _instantApp, boolean _visibleToInstantApp, boolean _exported) {
        super(_filter);
        this.receiverList = _receiverList;
        this.packageName = _packageName;
        this.featureId = _featureId;
        this.receiverId = _receiverId;
        this.requiredPermission = _requiredPermission;
        this.owningUid = _owningUid;
        this.owningUserId = _userId;
        this.instantApp = _instantApp;
        this.visibleToInstantApp = _visibleToInstantApp;
        this.exported = _exported;
    }

    public java.lang.String getReceiverClassName() {
        int index;
        if (this.receiverId != null && (index = this.receiverId.lastIndexOf(64)) > 0) {
            return this.receiverId.substring(0, index);
        }
        return null;
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        super.dumpDebug(proto, 1146756268033L);
        if (this.requiredPermission != null) {
            proto.write(1138166333442L, this.requiredPermission);
        }
        proto.write(1138166333443L, java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        proto.write(1120986464260L, this.owningUserId);
        proto.end(token);
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        dumpInReceiverList(pw, new android.util.PrintWriterPrinter(pw), prefix);
        this.receiverList.dumpLocal(pw, prefix);
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpBrief(java.io.PrintWriter pw, java.lang.String prefix) {
        dumpBroadcastFilterState(pw, prefix);
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpInReceiverList(java.io.PrintWriter pw, android.util.Printer pr, java.lang.String prefix) {
        super.dump(pr, prefix);
        dumpBroadcastFilterState(pw, prefix);
    }

    @dalvik.annotation.optimization.NeverCompile
    void dumpBroadcastFilterState(java.io.PrintWriter pw, java.lang.String prefix) {
        if (this.requiredPermission != null) {
            pw.print(prefix);
            pw.print("requiredPermission=");
            pw.println(this.requiredPermission);
        }
    }

    public java.lang.String toString() {
        return "BroadcastFilter{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + ' ' + this.owningUid + "/u" + this.owningUserId + ' ' + this.receiverList + '}';
    }
}
