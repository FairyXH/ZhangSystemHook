package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class UidObserverController {
    private static final int SLOW_UID_OBSERVER_THRESHOLD_MS = 20;
    private static final boolean VALIDATE_UID_STATES = true;
    private final android.os.Handler mHandler;
    private int mUidChangeDispatchCount;
    private final java.lang.Object mLock = new java.lang.Object();
    final android.os.RemoteCallbackList<android.app.IUidObserver> mUidObservers = new android.os.RemoteCallbackList<>();
    private final java.util.ArrayList<com.android.server.am.UidObserverController.ChangeRecord> mPendingUidChanges = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.am.UidObserverController.ChangeRecord> mAvailUidChanges = new java.util.ArrayList<>();
    private com.android.server.am.UidObserverController.ChangeRecord[] mActiveUidChanges = new com.android.server.am.UidObserverController.ChangeRecord[5];
    private final java.lang.Runnable mDispatchRunnable = new java.lang.Runnable() { // from class: com.android.server.am.UidObserverController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.dispatchUidsChanged();
        }
    };
    private final com.android.server.am.ActiveUids mValidateUids = new com.android.server.am.ActiveUids(null, false);

    UidObserverController(android.os.Handler handler) {
        this.mHandler = handler;
    }

    android.os.IBinder register(android.app.IUidObserver observer, int which, int cutpoint, java.lang.String callingPackage, int callingUid, int[] uids) {
        android.os.IBinder token = new android.os.Binder("UidObserver-" + callingPackage + "-" + java.util.UUID.randomUUID().toString());
        synchronized (this.mLock) {
            try {
                try {
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                try {
                    this.mUidObservers.register(observer, new com.android.server.am.UidObserverController.UidObserverRegistration(callingUid, callingPackage, which, cutpoint, android.app.ActivityManager.checkUidPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingUid) == 0, uids, token));
                    return token;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    void unregister(android.app.IUidObserver observer) {
        synchronized (this.mLock) {
            this.mUidObservers.unregister(observer);
        }
    }

    final void addUidToObserver(android.os.IBinder observerToken, int uid) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 80, uid, 0, observerToken);
        this.mHandler.sendMessage(msg);
    }

    public final void addUidToObserverImpl(android.os.IBinder observerToken, int uid) {
        int i = this.mUidObservers.beginBroadcast();
        while (true) {
            int i2 = i - 1;
            if (i <= 0) {
                break;
            }
            com.android.server.am.UidObserverController.UidObserverRegistration reg = (com.android.server.am.UidObserverController.UidObserverRegistration) this.mUidObservers.getBroadcastCookie(i2);
            if (reg.getToken().equals(observerToken)) {
                reg.addUid(uid);
                break;
            } else {
                if (i2 == 0) {
                    android.util.Slog.e(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Unable to find UidObserver by token");
                }
                i = i2;
            }
        }
        this.mUidObservers.finishBroadcast();
    }

    final void removeUidFromObserver(android.os.IBinder observerToken, int uid) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 81, uid, 0, observerToken);
        this.mHandler.sendMessage(msg);
    }

    public final void removeUidFromObserverImpl(android.os.IBinder observerToken, int uid) {
        int i = this.mUidObservers.beginBroadcast();
        while (true) {
            int i2 = i - 1;
            if (i <= 0) {
                break;
            }
            com.android.server.am.UidObserverController.UidObserverRegistration reg = (com.android.server.am.UidObserverController.UidObserverRegistration) this.mUidObservers.getBroadcastCookie(i2);
            if (reg.getToken().equals(observerToken)) {
                reg.removeUid(uid);
                break;
            } else {
                if (i2 == 0) {
                    android.util.Slog.e(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Unable to find UidObserver by token");
                }
                i = i2;
            }
        }
        this.mUidObservers.finishBroadcast();
    }

    int enqueueUidChange(com.android.server.am.UidObserverController.ChangeRecord currentRecord, int uid, int change, int procState, int procAdj, long procStateSeq, int capability, boolean ephemeral) {
        int i;
        synchronized (this.mLock) {
            if (this.mPendingUidChanges.size() == 0) {
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                    android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "*** Enqueueing dispatch uid changed!");
                }
                this.mHandler.post(this.mDispatchRunnable);
            }
            com.android.server.am.UidObserverController.ChangeRecord changeRecord = currentRecord != null ? currentRecord : getOrCreateChangeRecordLocked();
            if (!changeRecord.isPending) {
                changeRecord.isPending = true;
                this.mPendingUidChanges.add(changeRecord);
            } else {
                change = mergeWithPendingChange(change, changeRecord.change);
            }
            changeRecord.uid = uid;
            changeRecord.change = change;
            changeRecord.procState = procState;
            changeRecord.procAdj = procAdj;
            changeRecord.procStateSeq = procStateSeq;
            changeRecord.capability = capability;
            changeRecord.ephemeral = ephemeral;
            i = changeRecord.change;
        }
        return i;
    }

    java.util.ArrayList<com.android.server.am.UidObserverController.ChangeRecord> getPendingUidChangesForTest() {
        return this.mPendingUidChanges;
    }

    com.android.server.am.ActiveUids getValidateUidsForTest() {
        return this.mValidateUids;
    }

    java.lang.Runnable getDispatchRunnableForTest() {
        return this.mDispatchRunnable;
    }

    static int mergeWithPendingChange(int currentChange, int pendingChange) {
        if ((currentChange & 6) == 0) {
            currentChange |= pendingChange & 6;
        }
        if ((currentChange & 24) == 0) {
            currentChange |= pendingChange & 24;
        }
        if ((currentChange & 1) != 0) {
            currentChange &= -13;
        }
        if ((pendingChange & 32) != 0) {
            currentChange |= 32;
        }
        if ((pendingChange & Integer.MIN_VALUE) != 0) {
            currentChange |= Integer.MIN_VALUE;
        }
        if ((pendingChange & 64) != 0) {
            return currentChange | 64;
        }
        return currentChange;
    }

    private com.android.server.am.UidObserverController.ChangeRecord getOrCreateChangeRecordLocked() {
        com.android.server.am.UidObserverController.ChangeRecord changeRecord;
        int size = this.mAvailUidChanges.size();
        if (size > 0) {
            changeRecord = this.mAvailUidChanges.remove(size - 1);
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Retrieving available item: " + changeRecord);
            }
        } else {
            changeRecord = new com.android.server.am.UidObserverController.ChangeRecord();
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "Allocating new item: " + changeRecord);
            }
        }
        return changeRecord;
    }

    void dispatchUidsChanged() {
        int numUidChanges;
        synchronized (this.mLock) {
            numUidChanges = this.mPendingUidChanges.size();
            if (this.mActiveUidChanges.length < numUidChanges) {
                this.mActiveUidChanges = new com.android.server.am.UidObserverController.ChangeRecord[numUidChanges];
            }
            for (int i = 0; i < numUidChanges; i++) {
                com.android.server.am.UidObserverController.ChangeRecord changeRecord = this.mPendingUidChanges.get(i);
                this.mActiveUidChanges[i] = getOrCreateChangeRecordLocked();
                changeRecord.copyTo(this.mActiveUidChanges[i]);
                changeRecord.isPending = false;
            }
            this.mPendingUidChanges.clear();
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "*** Delivering " + numUidChanges + " uid changes");
            }
            this.mUidChangeDispatchCount += numUidChanges;
        }
        int i2 = this.mUidObservers.beginBroadcast();
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                break;
            }
            dispatchUidsChangedForObserver((android.app.IUidObserver) this.mUidObservers.getBroadcastItem(i3), (com.android.server.am.UidObserverController.UidObserverRegistration) this.mUidObservers.getBroadcastCookie(i3), numUidChanges);
            i2 = i3;
        }
        this.mUidObservers.finishBroadcast();
        if (this.mUidObservers.getRegisteredCallbackCount() > 0) {
            for (int j = 0; j < numUidChanges; j++) {
                com.android.server.am.UidObserverController.ChangeRecord item = this.mActiveUidChanges[j];
                if ((item.change & 1) != 0) {
                    this.mValidateUids.remove(item.uid);
                } else {
                    com.android.server.am.UidRecord validateUid = this.mValidateUids.get(item.uid);
                    if (validateUid == null) {
                        validateUid = new com.android.server.am.UidRecord(item.uid, null);
                        this.mValidateUids.put(item.uid, validateUid);
                    }
                    if ((item.change & 2) != 0) {
                        validateUid.setIdle(true);
                    } else if ((item.change & 4) != 0) {
                        validateUid.setIdle(false);
                    }
                    validateUid.setSetProcState(item.procState);
                    validateUid.setCurProcState(item.procState);
                    validateUid.setSetCapability(item.capability);
                    validateUid.setCurCapability(item.capability);
                }
            }
        }
        synchronized (this.mLock) {
            for (int j2 = 0; j2 < numUidChanges; j2++) {
                com.android.server.am.UidObserverController.ChangeRecord changeRecord2 = this.mActiveUidChanges[j2];
                changeRecord2.isPending = false;
                this.mAvailUidChanges.add(changeRecord2);
            }
        }
    }

    private void dispatchUidsChangedForObserver(android.app.IUidObserver observer, com.android.server.am.UidObserverController.UidObserverRegistration reg, int changesSize) {
        boolean doReport;
        java.lang.String str;
        int i;
        java.lang.String str2 = ": ";
        if (observer == null) {
            return;
        }
        int j = 0;
        while (j < changesSize) {
            try {
                com.android.server.am.UidObserverController.ChangeRecord item = this.mActiveUidChanges[j];
                long start = android.os.SystemClock.uptimeMillis();
                int change = item.change;
                if (!reg.isWatchingUid(item.uid)) {
                    str = str2;
                } else if (android.os.UserHandle.getUserId(item.uid) != android.os.UserHandle.getUserId(reg.mUid) && !reg.mCanInteractAcrossUsers) {
                    str = str2;
                } else if (change == Integer.MIN_VALUE && (reg.mWhich & 1) == 0) {
                    str = str2;
                } else if (change == 64 && (reg.mWhich & 64) == 0) {
                    str = str2;
                } else {
                    if ((change & 2) != 0) {
                        if ((reg.mWhich & 4) != 0) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "UID idle uid=" + item.uid);
                            }
                            observer.onUidIdle(item.uid, item.ephemeral);
                        }
                    } else if ((change & 4) != 0 && (reg.mWhich & 8) != 0) {
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                            android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "UID active uid=" + item.uid);
                        }
                        observer.onUidActive(item.uid);
                    }
                    if ((reg.mWhich & 16) != 0) {
                        if ((change & 8) != 0) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "UID cached uid=" + item.uid);
                            }
                            observer.onUidCachedChanged(item.uid, true);
                        } else if ((change & 16) != 0) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "UID active uid=" + item.uid);
                            }
                            observer.onUidCachedChanged(item.uid, false);
                        }
                    }
                    if ((change & 1) != 0) {
                        if ((reg.mWhich & 2) != 0) {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "UID gone uid=" + item.uid);
                            }
                            observer.onUidGone(item.uid, item.ephemeral);
                        }
                        if (reg.mLastProcStates == null) {
                            str = str2;
                            i = 20;
                        } else {
                            reg.mLastProcStates.delete(item.uid);
                            str = str2;
                            i = 20;
                        }
                    } else {
                        boolean doReport2 = false;
                        if ((reg.mWhich & 1) != 0) {
                            doReport2 = true;
                            if (reg.mCutpoint >= 0) {
                                int lastState = reg.mLastProcStates.get(item.uid, -1);
                                if (lastState != -1) {
                                    boolean lastAboveCut = lastState <= reg.mCutpoint;
                                    boolean newAboveCut = item.procState <= reg.mCutpoint;
                                    doReport2 = lastAboveCut != newAboveCut;
                                } else {
                                    doReport2 = item.procState != 20;
                                }
                            }
                        }
                        if ((reg.mWhich & 32) == 0) {
                            doReport = doReport2;
                        } else {
                            doReport = doReport2 | ((change & 32) != 0);
                        }
                        if (!doReport) {
                            str = str2;
                            i = 20;
                        } else {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_UID_OBSERVERS) {
                                android.util.Slog.i(com.android.server.am.ActivityManagerService.TAG_UID_OBSERVERS, "UID CHANGED uid=" + item.uid + str2 + item.procState + str2 + item.capability);
                            }
                            if (reg.mLastProcStates != null) {
                                reg.mLastProcStates.put(item.uid, item.procState);
                            }
                            str = str2;
                            i = 20;
                            observer.onUidStateChanged(item.uid, item.procState, item.procStateSeq, item.capability);
                        }
                        if ((reg.mWhich & 64) != 0 && (change & 64) != 0) {
                            observer.onUidProcAdjChanged(item.uid, item.procAdj);
                        }
                    }
                    int duration = (int) (android.os.SystemClock.uptimeMillis() - start);
                    if (reg.mMaxDispatchTime < duration) {
                        reg.mMaxDispatchTime = duration;
                    }
                    if (duration >= i) {
                        reg.mSlowDispatchCount++;
                    }
                }
                j++;
                str2 = str;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
    }

    com.android.server.am.UidRecord getValidateUidRecord(int uid) {
        return this.mValidateUids.get(uid);
    }

    void dump(java.io.PrintWriter pw, java.lang.String dumpPackage) {
        synchronized (this.mLock) {
            int count = this.mUidObservers.getRegisteredCallbackCount();
            boolean printed = false;
            for (int i = 0; i < count; i++) {
                com.android.server.am.UidObserverController.UidObserverRegistration reg = (com.android.server.am.UidObserverController.UidObserverRegistration) this.mUidObservers.getRegisteredCallbackCookie(i);
                if (dumpPackage == null || dumpPackage.equals(reg.mPkg)) {
                    if (!printed) {
                        pw.println("  mUidObservers:");
                        printed = true;
                    }
                    reg.dump(pw, (android.app.IUidObserver) this.mUidObservers.getRegisteredCallbackItem(i));
                }
            }
            if (dumpPackage == null) {
                pw.println();
                pw.print("  mUidChangeDispatchCount=");
                pw.print(this.mUidChangeDispatchCount);
                pw.println();
                pw.println("  Slow UID dispatches:");
                for (int i2 = 0; i2 < count; i2++) {
                    com.android.server.am.UidObserverController.UidObserverRegistration reg2 = (com.android.server.am.UidObserverController.UidObserverRegistration) this.mUidObservers.getRegisteredCallbackCookie(i2);
                    pw.print("    ");
                    pw.print(this.mUidObservers.getRegisteredCallbackItem(i2).getClass().getTypeName());
                    pw.print(": ");
                    pw.print(reg2.mSlowDispatchCount);
                    pw.print(" / Max ");
                    pw.print(reg2.mMaxDispatchTime);
                    pw.println("ms");
                }
            }
        }
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage) {
        synchronized (this.mLock) {
            int count = this.mUidObservers.getRegisteredCallbackCount();
            for (int i = 0; i < count; i++) {
                com.android.server.am.UidObserverController.UidObserverRegistration reg = (com.android.server.am.UidObserverController.UidObserverRegistration) this.mUidObservers.getRegisteredCallbackCookie(i);
                if (dumpPackage == null || dumpPackage.equals(reg.mPkg)) {
                    reg.dumpDebug(proto, 2246267895831L);
                }
            }
        }
    }

    boolean dumpValidateUids(java.io.PrintWriter pw, java.lang.String dumpPackage, int dumpAppId, java.lang.String header, boolean needSep) {
        return this.mValidateUids.dump(pw, dumpPackage, dumpAppId, header, needSep);
    }

    void dumpValidateUidsProto(android.util.proto.ProtoOutputStream proto, java.lang.String dumpPackage, int dumpAppId, long fieldId) {
        this.mValidateUids.dumpProto(proto, dumpPackage, dumpAppId, fieldId);
    }

    static final class ChangeRecord {
        public int capability;
        public int change;
        public boolean ephemeral;
        public boolean isPending;
        public int procAdj;
        public int procState;
        public long procStateSeq;
        public int uid;

        ChangeRecord() {
        }

        void copyTo(com.android.server.am.UidObserverController.ChangeRecord changeRecord) {
            changeRecord.isPending = this.isPending;
            changeRecord.uid = this.uid;
            changeRecord.change = this.change;
            changeRecord.procState = this.procState;
            changeRecord.procAdj = this.procAdj;
            changeRecord.capability = this.capability;
            changeRecord.ephemeral = this.ephemeral;
            changeRecord.procStateSeq = this.procStateSeq;
        }
    }

    private static final class UidObserverRegistration {
        private static final int[] ORIG_ENUMS = {4, 8, 2, 1, 32, 64};
        private static final int[] PROTO_ENUMS = {3, 4, 2, 1, 6, 7};
        private final boolean mCanInteractAcrossUsers;
        private final int mCutpoint;
        final android.util.SparseIntArray mLastProcStates;
        int mMaxDispatchTime;
        private final java.lang.String mPkg;
        int mSlowDispatchCount;
        private final android.os.IBinder mToken;
        private final int mUid;
        private int[] mUids;
        private final int mWhich;

        UidObserverRegistration(int uid, java.lang.String pkg, int which, int cutpoint, boolean canInteractAcrossUsers, int[] uids, android.os.IBinder token) {
            this.mUid = uid;
            this.mPkg = pkg;
            this.mWhich = which;
            this.mCutpoint = cutpoint;
            this.mCanInteractAcrossUsers = canInteractAcrossUsers;
            if (uids != null) {
                this.mUids = (int[]) uids.clone();
                java.util.Arrays.sort(this.mUids);
            } else {
                this.mUids = null;
            }
            this.mToken = token;
            this.mLastProcStates = cutpoint >= 0 ? new android.util.SparseIntArray() : null;
        }

        boolean isWatchingUid(int uid) {
            return this.mUids == null || java.util.Arrays.binarySearch(this.mUids, uid) >= 0;
        }

        void addUid(int uid) {
            if (this.mUids == null) {
                return;
            }
            int[] temp = this.mUids;
            this.mUids = new int[temp.length + 1];
            boolean inserted = false;
            for (int i = 0; i < temp.length; i++) {
                if (!inserted) {
                    if (temp[i] < uid) {
                        this.mUids[i] = temp[i];
                    } else if (temp[i] == uid) {
                        this.mUids = temp;
                        return;
                    } else {
                        this.mUids[i] = uid;
                        this.mUids[i + 1] = temp[i];
                        inserted = true;
                    }
                } else {
                    this.mUids[i + 1] = temp[i];
                }
            }
            if (!inserted) {
                this.mUids[temp.length] = uid;
            }
        }

        void removeUid(int uid) {
            if (this.mUids == null || this.mUids.length == 0) {
                return;
            }
            int[] temp = this.mUids;
            this.mUids = new int[temp.length - 1];
            boolean removed = false;
            for (int i = 0; i < temp.length; i++) {
                if (!removed) {
                    if (temp[i] == uid) {
                        removed = true;
                    } else {
                        if (i == temp.length - 1) {
                            this.mUids = temp;
                            return;
                        }
                        this.mUids[i] = temp[i];
                    }
                } else {
                    this.mUids[i - 1] = temp[i];
                }
            }
        }

        android.os.IBinder getToken() {
            return this.mToken;
        }

        void dump(java.io.PrintWriter pw, android.app.IUidObserver observer) {
            pw.print("    ");
            android.os.UserHandle.formatUid(pw, this.mUid);
            pw.print(" ");
            pw.print(this.mPkg);
            pw.print(" ");
            pw.print(observer.getClass().getTypeName());
            pw.print(":");
            if ((this.mWhich & 4) != 0) {
                pw.print(" IDLE");
            }
            if ((this.mWhich & 8) != 0) {
                pw.print(" ACT");
            }
            if ((this.mWhich & 2) != 0) {
                pw.print(" GONE");
            }
            if ((this.mWhich & 32) != 0) {
                pw.print(" CAP");
            }
            if ((this.mWhich & 1) != 0) {
                pw.print(" STATE");
                pw.print(" (cut=");
                pw.print(this.mCutpoint);
                pw.print(")");
            }
            pw.println();
            if (this.mLastProcStates != null) {
                int size = this.mLastProcStates.size();
                for (int j = 0; j < size; j++) {
                    pw.print("      Last ");
                    android.os.UserHandle.formatUid(pw, this.mLastProcStates.keyAt(j));
                    pw.print(": ");
                    pw.println(this.mLastProcStates.valueAt(j));
                }
            }
        }

        void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.mUid);
            proto.write(1138166333442L, this.mPkg);
            android.util.proto.ProtoUtils.writeBitWiseFlagsToProtoEnum(proto, 2259152797699L, this.mWhich, ORIG_ENUMS, PROTO_ENUMS);
            proto.write(1120986464260L, this.mCutpoint);
            if (this.mLastProcStates != null) {
                int size = this.mLastProcStates.size();
                for (int i = 0; i < size; i++) {
                    long pToken = proto.start(2246267895813L);
                    proto.write(1120986464257L, this.mLastProcStates.keyAt(i));
                    proto.write(1120986464258L, this.mLastProcStates.valueAt(i));
                    proto.end(pToken);
                }
            }
            proto.end(token);
        }
    }
}
