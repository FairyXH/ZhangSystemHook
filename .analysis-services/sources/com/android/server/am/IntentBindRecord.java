package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class IntentBindRecord {
    android.os.IBinder binder;
    boolean doRebind;
    boolean hasBound;
    final android.content.Intent.FilterComparison intent;
    boolean received;
    boolean requested;
    final com.android.server.am.ServiceRecord service;
    java.lang.String stringName;
    final android.util.ArrayMap<com.android.server.am.ProcessRecord, com.android.server.am.AppBindRecord> apps = new android.util.ArrayMap<>();
    private final com.android.server.am.IntentBindRecord.IntentBindRecordWrapper mWrapper = new com.android.server.am.IntentBindRecord.IntentBindRecordWrapper();
    private final com.android.server.am.IIntentBindRecordExt mIntentBindRecordExt = (com.android.server.am.IIntentBindRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IIntentBindRecordExt.class).base(this).create();

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("service=");
        pw.println(this.service);
        dumpInService(pw, prefix);
    }

    void dumpInService(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("intent={");
        pw.print(this.intent.getIntent().toShortString(false, true, false, false));
        pw.println('}');
        pw.print(prefix);
        pw.print("binder=");
        pw.println(this.binder);
        pw.print(prefix);
        pw.print("requested=");
        pw.print(this.requested);
        pw.print(" received=");
        pw.print(this.received);
        pw.print(" hasBound=");
        pw.print(this.hasBound);
        pw.print(" doRebind=");
        pw.println(this.doRebind);
        for (int i = 0; i < this.apps.size(); i++) {
            com.android.server.am.AppBindRecord a = this.apps.valueAt(i);
            pw.print(prefix);
            pw.print("* Client AppBindRecord{");
            pw.print(java.lang.Integer.toHexString(java.lang.System.identityHashCode(a)));
            pw.print(' ');
            pw.print(a.client);
            pw.println('}');
            a.dumpInIntentBind(pw, prefix + "  ");
        }
    }

    IntentBindRecord(com.android.server.am.ServiceRecord _service, android.content.Intent.FilterComparison _intent) {
        this.service = _service;
        this.intent = _intent;
    }

    long collectFlags() {
        long flags = 0;
        for (int i = this.apps.size() - 1; i >= 0; i--) {
            android.util.ArraySet<com.android.server.am.ConnectionRecord> connections = this.apps.valueAt(i).connections;
            for (int j = connections.size() - 1; j >= 0; j--) {
                flags |= connections.valueAt(j).getFlags();
            }
        }
        return flags;
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("IntentBindRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        if ((collectFlags() & 1) != 0) {
            sb.append("CR ");
        }
        sb.append(this.service.shortInstanceName);
        sb.append(':');
        if (this.intent != null) {
            this.intent.getIntent().toShortString(sb, false, false, false, false);
        }
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (this.intent != null) {
            this.intent.getIntent().dumpDebug(proto, 1146756268033L, false, true, false, false);
        }
        if (this.binder != null) {
            proto.write(1138166333442L, this.binder.toString());
        }
        proto.write(1133871366147L, (collectFlags() & 1) != 0);
        proto.write(1133871366148L, this.requested);
        proto.write(1133871366149L, this.received);
        proto.write(1133871366150L, this.hasBound);
        proto.write(1133871366151L, this.doRebind);
        int N = this.apps.size();
        for (int i = 0; i < N; i++) {
            com.android.server.am.AppBindRecord a = this.apps.valueAt(i);
            if (a != null) {
                a.dumpDebug(proto, 2246267895816L);
            }
        }
        proto.end(token);
    }

    public com.android.server.am.IntentBindRecord.IntentBindRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    public class IntentBindRecordWrapper implements com.android.server.am.IIntentBindRecordWrapper {
        public IntentBindRecordWrapper() {
        }

        @Override // com.android.server.am.IIntentBindRecordWrapper
        public com.android.server.am.IIntentBindRecordExt getExtImpl() {
            return com.android.server.am.IntentBindRecord.this.mIntentBindRecordExt;
        }
    }
}
