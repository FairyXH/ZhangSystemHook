package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ActiveUids {
    private final android.util.SparseArray<com.android.server.am.UidRecord> mActiveUids = new android.util.SparseArray<>();
    private final boolean mPostChangesToAtm;
    private final com.android.server.am.ActivityManagerGlobalLock mProcLock;
    private final com.android.server.am.ActivityManagerService mService;

    ActiveUids(com.android.server.am.ActivityManagerService service, boolean postChangesToAtm) {
        this.mService = service;
        this.mProcLock = service != null ? service.mProcLock : null;
        this.mPostChangesToAtm = postChangesToAtm;
    }

    void put(int uid, com.android.server.am.UidRecord value) {
        this.mActiveUids.put(uid, value);
        if (this.mPostChangesToAtm) {
            this.mService.mAtmInternal.onUidActive(uid, value.getCurProcState());
        }
    }

    void remove(int uid) {
        this.mActiveUids.remove(uid);
        if (this.mPostChangesToAtm) {
            this.mService.mAtmInternal.onUidInactive(uid);
        }
    }

    void clear() {
        this.mActiveUids.clear();
    }

    com.android.server.am.UidRecord get(int uid) {
        return this.mActiveUids.get(uid);
    }

    int size() {
        return this.mActiveUids.size();
    }

    com.android.server.am.UidRecord valueAt(int index) {
        return this.mActiveUids.valueAt(index);
    }

    int keyAt(int index) {
        return this.mActiveUids.keyAt(index);
    }

    int indexOfKey(int uid) {
        return this.mActiveUids.indexOfKey(uid);
    }

    boolean dump(final java.io.PrintWriter pw, java.lang.String dumpPackage, int dumpAppId, java.lang.String header, boolean needSep) {
        boolean printed = false;
        for (int i = 0; i < this.mActiveUids.size(); i++) {
            com.android.server.am.UidRecord uidRec = this.mActiveUids.valueAt(i);
            if (dumpPackage == null || android.os.UserHandle.getAppId(uidRec.getUid()) == dumpAppId) {
                if (!printed) {
                    printed = true;
                    if (needSep) {
                        pw.println();
                    }
                    pw.print("  ");
                    pw.println(header);
                }
                pw.print("    UID ");
                android.os.UserHandle.formatUid(pw, uidRec.getUid());
                pw.print(": ");
                pw.println(uidRec);
                pw.print("      curProcState=");
                pw.print(uidRec.getCurProcState());
                pw.print(" curCapability=");
                android.app.ActivityManager.printCapabilitiesFull(pw, uidRec.getCurCapability());
                pw.println();
                uidRec.forEachProcess(new java.util.function.Consumer() { // from class: com.android.server.am.ActiveUids$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.am.ActiveUids.lambda$dump$0(pw, (com.android.server.am.ProcessRecord) obj);
                    }
                });
            }
        }
        return printed;
    }

    static /* synthetic */ void lambda$dump$0(java.io.PrintWriter pw, com.android.server.am.ProcessRecord app) {
        pw.print("      proc=");
        pw.println(app);
    }

    void dumpProto(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage, int dumpAppId, long fieldId) {
        for (int i = 0; i < this.mActiveUids.size(); i++) {
            com.android.server.am.UidRecord uidRec = this.mActiveUids.valueAt(i);
            if (dumpPackage == null || android.os.UserHandle.getAppId(uidRec.getUid()) == dumpAppId) {
                uidRec.dumpDebug(proto, fieldId);
            }
        }
    }
}
