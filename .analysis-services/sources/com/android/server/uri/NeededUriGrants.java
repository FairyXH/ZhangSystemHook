package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
public class NeededUriGrants {
    final int flags;
    final java.lang.String targetPkg;
    final int targetUid;
    final android.util.ArraySet<com.android.server.uri.GrantUri> uris = new android.util.ArraySet<>();

    public NeededUriGrants(java.lang.String targetPkg, int targetUid, int flags) {
        this.targetPkg = targetPkg;
        this.targetUid = targetUid;
        this.flags = flags;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.targetPkg);
        proto.write(1120986464258L, this.targetUid);
        proto.write(1120986464259L, this.flags);
        int N = this.uris.size();
        for (int i = 0; i < N; i++) {
            this.uris.valueAt(i).dumpDebug(proto, 2246267895812L);
        }
        proto.end(token);
    }
}
