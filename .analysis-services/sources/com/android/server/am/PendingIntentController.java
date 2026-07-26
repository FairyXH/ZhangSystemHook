package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class PendingIntentController {
    private static final int RECENT_N = 10;
    private static final java.lang.String TAG = "ActivityManager";
    private static final java.lang.String TAG_MU = "ActivityManager_MU";
    android.app.ActivityManagerInternal mAmInternal;
    private final com.android.server.am.ActivityManagerConstants mConstants;
    final android.os.Handler mH;
    final com.android.server.am.UserController mUserController;
    final java.lang.Object mLock = new java.lang.Object();
    final java.util.HashMap<com.android.server.am.PendingIntentRecord.Key, java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> mIntentSenderRecords = new java.util.HashMap<>();
    private final android.util.SparseIntArray mIntentsPerUid = new android.util.SparseIntArray();
    private final android.util.SparseArray<com.android.internal.util.RingBuffer<java.lang.String>> mRecentIntentsPerUid = new android.util.SparseArray<>();
    com.android.server.am.IPendingIntentControllerExt mPendingIntentControllerExt = (com.android.server.am.IPendingIntentControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IPendingIntentControllerExt.class).create();
    final com.android.server.wm.ActivityTaskManagerInternal mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);

    PendingIntentController(android.os.Looper looper, com.android.server.am.UserController userController, com.android.server.am.ActivityManagerConstants constants) {
        this.mH = new android.os.Handler(looper);
        this.mUserController = userController;
        this.mConstants = constants;
    }

    void onActivityManagerInternalAdded() {
        synchronized (this.mLock) {
            this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008a A[Catch: all -> 0x0029, TryCatch #4 {all -> 0x0029, blocks: (B:6:0x000f, B:12:0x0037, B:14:0x003a, B:20:0x0052, B:22:0x0058, B:24:0x008a, B:25:0x008e, B:26:0x0096, B:28:0x0099, B:30:0x009f, B:32:0x00c3, B:33:0x00c7, B:34:0x00cf), top: B:104:0x000f }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x008e A[Catch: all -> 0x0029, TryCatch #4 {all -> 0x0029, blocks: (B:6:0x000f, B:12:0x0037, B:14:0x003a, B:20:0x0052, B:22:0x0058, B:24:0x008a, B:25:0x008e, B:26:0x0096, B:28:0x0099, B:30:0x009f, B:32:0x00c3, B:33:0x00c7, B:34:0x00cf), top: B:104:0x000f }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00c3 A[Catch: all -> 0x0029, TryCatch #4 {all -> 0x0029, blocks: (B:6:0x000f, B:12:0x0037, B:14:0x003a, B:20:0x0052, B:22:0x0058, B:24:0x008a, B:25:0x008e, B:26:0x0096, B:28:0x0099, B:30:0x009f, B:32:0x00c3, B:33:0x00c7, B:34:0x00cf), top: B:104:0x000f }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c7 A[Catch: all -> 0x0029, TryCatch #4 {all -> 0x0029, blocks: (B:6:0x000f, B:12:0x0037, B:14:0x003a, B:20:0x0052, B:22:0x0058, B:24:0x008a, B:25:0x008e, B:26:0x0096, B:28:0x0099, B:30:0x009f, B:32:0x00c3, B:33:0x00c7, B:34:0x00cf), top: B:104:0x000f }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0155 A[Catch: all -> 0x01c3, TryCatch #0 {all -> 0x01c3, blocks: (B:57:0x0145, B:59:0x0155, B:64:0x0163, B:66:0x0169, B:68:0x016f, B:70:0x0175, B:72:0x017a), top: B:97:0x0145 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01ac  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01b0 A[Catch: all -> 0x01d8, TryCatch #3 {all -> 0x01d8, blocks: (B:74:0x018a, B:77:0x019a, B:93:0x01d6, B:75:0x018d, B:79:0x019c, B:82:0x01b0, B:84:0x01b2, B:85:0x01c1), top: B:103:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01b2 A[Catch: all -> 0x01d8, TryCatch #3 {all -> 0x01d8, blocks: (B:74:0x018a, B:77:0x019a, B:93:0x01d6, B:75:0x018d, B:79:0x019c, B:82:0x01b0, B:84:0x01b2, B:85:0x01c1), top: B:103:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x00f8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.android.server.am.PendingIntentRecord getIntentSender(int r25, java.lang.String r26, java.lang.String r27, int r28, int r29, android.os.IBinder r30, java.lang.String r31, int r32, android.content.Intent[] r33, java.lang.String[] r34, int r35, android.os.Bundle r36) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 474
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.PendingIntentController.getIntentSender(int, java.lang.String, java.lang.String, int, int, android.os.IBinder, java.lang.String, int, android.content.Intent[], java.lang.String[], int, android.os.Bundle):com.android.server.am.PendingIntentRecord");
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0069 A[Catch: all -> 0x0090, TryCatch #0 {, blocks: (B:4:0x0004, B:6:0x000c, B:9:0x000f, B:10:0x0019, B:12:0x001f, B:14:0x0027, B:15:0x002b, B:17:0x0033, B:19:0x0039, B:36:0x0066, B:39:0x0069, B:41:0x0079, B:22:0x0040, B:27:0x004c, B:30:0x0053, B:32:0x0059, B:43:0x008e), top: B:48:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0066 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean removePendingIntentsForPackage(java.lang.String r9, int r10, int r11, boolean r12, int r13) {
        /*
            r8 = this;
            r0 = 0
            java.lang.Object r1 = r8.mLock
            monitor-enter(r1)
            java.util.HashMap<com.android.server.am.PendingIntentRecord$Key, java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> r2 = r8.mIntentSenderRecords     // Catch: java.lang.Throwable -> L90
            int r2 = r2.size()     // Catch: java.lang.Throwable -> L90
            if (r2 > 0) goto Lf
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L90
            r1 = 0
            return r1
        Lf:
            java.util.HashMap<com.android.server.am.PendingIntentRecord$Key, java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> r2 = r8.mIntentSenderRecords     // Catch: java.lang.Throwable -> L90
            java.util.Collection r2 = r2.values()     // Catch: java.lang.Throwable -> L90
            java.util.Iterator r2 = r2.iterator()     // Catch: java.lang.Throwable -> L90
        L19:
            boolean r3 = r2.hasNext()     // Catch: java.lang.Throwable -> L90
            if (r3 == 0) goto L8e
            java.lang.Object r3 = r2.next()     // Catch: java.lang.Throwable -> L90
            java.lang.ref.WeakReference r3 = (java.lang.ref.WeakReference) r3     // Catch: java.lang.Throwable -> L90
            if (r3 != 0) goto L2b
            r2.remove()     // Catch: java.lang.Throwable -> L90
            goto L19
        L2b:
            java.lang.Object r4 = r3.get()     // Catch: java.lang.Throwable -> L90
            com.android.server.am.PendingIntentRecord r4 = (com.android.server.am.PendingIntentRecord) r4     // Catch: java.lang.Throwable -> L90
            if (r4 != 0) goto L37
            r2.remove()     // Catch: java.lang.Throwable -> L90
            goto L19
        L37:
            if (r9 != 0) goto L40
            com.android.server.am.PendingIntentRecord$Key r5 = r4.key     // Catch: java.lang.Throwable -> L90
            int r5 = r5.userId     // Catch: java.lang.Throwable -> L90
            if (r5 == r10) goto L64
            goto L19
        L40:
            int r5 = r4.uid     // Catch: java.lang.Throwable -> L90
            int r5 = android.os.UserHandle.getAppId(r5)     // Catch: java.lang.Throwable -> L90
            if (r5 == r11) goto L49
            goto L19
        L49:
            r5 = -1
            if (r10 == r5) goto L53
            com.android.server.am.PendingIntentRecord$Key r5 = r4.key     // Catch: java.lang.Throwable -> L90
            int r5 = r5.userId     // Catch: java.lang.Throwable -> L90
            if (r5 == r10) goto L53
            goto L19
        L53:
            com.android.server.am.PendingIntentRecord$Key r5 = r4.key     // Catch: java.lang.Throwable -> L90
            java.lang.String r5 = r5.packageName     // Catch: java.lang.Throwable -> L90
            if (r5 == 0) goto L19
            com.android.server.am.PendingIntentRecord$Key r5 = r4.key     // Catch: java.lang.Throwable -> L90
            java.lang.String r5 = r5.packageName     // Catch: java.lang.Throwable -> L90
            boolean r5 = r5.equals(r9)     // Catch: java.lang.Throwable -> L90
            if (r5 != 0) goto L64
            goto L19
        L64:
            if (r12 != 0) goto L69
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L90
            r1 = 1
            return r1
        L69:
            r0 = 1
            r2.remove()     // Catch: java.lang.Throwable -> L90
            r8.makeIntentSenderCanceled(r4, r13)     // Catch: java.lang.Throwable -> L90
            r8.decrementUidStatLocked(r4)     // Catch: java.lang.Throwable -> L90
            com.android.server.am.PendingIntentRecord$Key r5 = r4.key     // Catch: java.lang.Throwable -> L90
            android.os.IBinder r5 = r5.activity     // Catch: java.lang.Throwable -> L90
            if (r5 == 0) goto L8d
            com.android.server.am.PendingIntentController$$ExternalSyntheticLambda0 r5 = new com.android.server.am.PendingIntentController$$ExternalSyntheticLambda0     // Catch: java.lang.Throwable -> L90
            r5.<init>()     // Catch: java.lang.Throwable -> L90
            com.android.server.am.PendingIntentRecord$Key r6 = r4.key     // Catch: java.lang.Throwable -> L90
            android.os.IBinder r6 = r6.activity     // Catch: java.lang.Throwable -> L90
            java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> r7 = r4.ref     // Catch: java.lang.Throwable -> L90
            android.os.Message r5 = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(r5, r8, r6, r7)     // Catch: java.lang.Throwable -> L90
            android.os.Handler r6 = r8.mH     // Catch: java.lang.Throwable -> L90
            r6.sendMessage(r5)     // Catch: java.lang.Throwable -> L90
        L8d:
            goto L19
        L8e:
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L90
            return r0
        L90:
            r2 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L90
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.PendingIntentController.removePendingIntentsForPackage(java.lang.String, int, int, boolean, int):boolean");
    }

    public void cancelIntentSender(android.content.IIntentSender sender) {
        if (!(sender instanceof com.android.server.am.PendingIntentRecord)) {
            return;
        }
        synchronized (this.mLock) {
            com.android.server.am.PendingIntentRecord rec = (com.android.server.am.PendingIntentRecord) sender;
            try {
                int uid = android.app.AppGlobals.getPackageManager().getPackageUid(rec.key.packageName, 268435456L, android.os.UserHandle.getCallingUserId());
                if (!android.os.UserHandle.isSameApp(uid, android.os.Binder.getCallingUid())) {
                    java.lang.String msg = "Permission Denial: cancelIntentSender() from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " is not allowed to cancel package " + rec.key.packageName;
                    android.util.Slog.w("ActivityManager", msg);
                    throw new java.lang.SecurityException(msg);
                }
                cancelIntentSender(rec, true, 8);
            } catch (android.os.RemoteException e) {
                throw new java.lang.SecurityException(e);
            }
        }
    }

    public void cancelIntentSender(com.android.server.am.PendingIntentRecord rec, boolean cleanActivity, int cancelReason) {
        synchronized (this.mLock) {
            makeIntentSenderCanceled(rec, cancelReason);
            this.mIntentSenderRecords.remove(rec.key);
            decrementUidStatLocked(rec);
            if (cleanActivity && rec.key.activity != null) {
                android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.am.PendingIntentController$$ExternalSyntheticLambda0(), this, rec.key.activity, rec.ref);
                this.mH.sendMessage(m);
            }
        }
    }

    boolean registerIntentSenderCancelListener(android.content.IIntentSender sender, com.android.internal.os.IResultReceiver receiver) {
        if (!(sender instanceof com.android.server.am.PendingIntentRecord)) {
            android.util.Slog.w("ActivityManager", "registerIntentSenderCancelListener called on non-PendingIntentRecord");
            return true;
        }
        synchronized (this.mLock) {
            com.android.server.am.PendingIntentRecord pendingIntent = (com.android.server.am.PendingIntentRecord) sender;
            boolean isCancelled = pendingIntent.canceled;
            if (isCancelled) {
                return false;
            }
            pendingIntent.registerCancelListenerLocked(receiver);
            return true;
        }
    }

    void unregisterIntentSenderCancelListener(android.content.IIntentSender sender, com.android.internal.os.IResultReceiver receiver) {
        if (!(sender instanceof com.android.server.am.PendingIntentRecord)) {
            return;
        }
        synchronized (this.mLock) {
            ((com.android.server.am.PendingIntentRecord) sender).unregisterCancelListenerLocked(receiver);
        }
    }

    void setPendingIntentAllowlistDuration(android.content.IIntentSender target, android.os.IBinder allowlistToken, long duration, int type, int reasonCode, java.lang.String reason) {
        if (!(target instanceof com.android.server.am.PendingIntentRecord)) {
            android.util.Slog.w("ActivityManager", "markAsSentFromNotification(): not a PendingIntentRecord: " + target);
            return;
        }
        synchronized (this.mLock) {
            ((com.android.server.am.PendingIntentRecord) target).setAllowlistDurationLocked(allowlistToken, duration, type, reasonCode, reason);
        }
    }

    int getPendingIntentFlags(android.content.IIntentSender target) {
        int i;
        if (!(target instanceof com.android.server.am.PendingIntentRecord)) {
            android.util.Slog.w("ActivityManager", "markAsSentFromNotification(): not a PendingIntentRecord: " + target);
            return 0;
        }
        synchronized (this.mLock) {
            i = ((com.android.server.am.PendingIntentRecord) target).key.flags;
        }
        return i;
    }

    private void makeIntentSenderCanceled(com.android.server.am.PendingIntentRecord rec, int cancelReason) {
        rec.canceled = true;
        rec.cancelReason = cancelReason;
        android.os.RemoteCallbackList<com.android.internal.os.IResultReceiver> callbacks = rec.detachCancelListenersLocked();
        if (callbacks != null) {
            android.os.Message m = com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.am.PendingIntentController$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.am.PendingIntentController) obj).handlePendingIntentCancelled((android.os.RemoteCallbackList) obj2);
                }
            }, this, callbacks);
            this.mH.sendMessage(m);
        }
        com.android.server.AlarmManagerInternal ami = (com.android.server.AlarmManagerInternal) com.android.server.LocalServices.getService(com.android.server.AlarmManagerInternal.class);
        ami.remove(new android.app.PendingIntent(rec));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePendingIntentCancelled(android.os.RemoteCallbackList<com.android.internal.os.IResultReceiver> callbacks) {
        int N = callbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                callbacks.getBroadcastItem(i).send(0, (android.os.Bundle) null);
            } catch (android.os.RemoteException e) {
            }
        }
        callbacks.finishBroadcast();
        callbacks.kill();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearPendingResultForActivity(android.os.IBinder activityToken, java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> pir) {
        this.mAtmInternal.clearPendingResultForActivity(activityToken, pir);
    }

    void dumpPendingIntents(java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage) {
        synchronized (this.mLock) {
            boolean printed = false;
            pw.println("ACTIVITY MANAGER PENDING INTENTS (dumpsys activity intents)");
            if (this.mIntentSenderRecords.size() > 0) {
                android.util.ArrayMap<java.lang.String, java.util.ArrayList<com.android.server.am.PendingIntentRecord>> byPackage = new android.util.ArrayMap<>();
                java.util.ArrayList<java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> weakRefs = new java.util.ArrayList<>();
                java.util.Iterator<java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord>> it = this.mIntentSenderRecords.values().iterator();
                while (it.hasNext()) {
                    java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> ref = it.next();
                    com.android.server.am.PendingIntentRecord rec = ref != null ? ref.get() : null;
                    if (rec == null) {
                        weakRefs.add(ref);
                    } else if (dumpPackage == null || dumpPackage.equals(rec.key.packageName)) {
                        java.util.ArrayList<com.android.server.am.PendingIntentRecord> list = byPackage.get(rec.key.packageName);
                        if (list == null) {
                            list = new java.util.ArrayList<>();
                            byPackage.put(rec.key.packageName, list);
                        }
                        list.add(rec);
                    }
                }
                for (int i = 0; i < byPackage.size(); i++) {
                    java.util.ArrayList<com.android.server.am.PendingIntentRecord> intents = byPackage.valueAt(i);
                    printed = true;
                    pw.print("  * ");
                    pw.print(byPackage.keyAt(i));
                    pw.print(": ");
                    pw.print(intents.size());
                    pw.println(" items");
                    for (int j = 0; j < intents.size(); j++) {
                        pw.print("    #");
                        pw.print(j);
                        pw.print(": ");
                        pw.println(intents.get(j));
                        if (dumpAll) {
                            intents.get(j).dump(pw, "      ");
                        }
                    }
                }
                int i2 = weakRefs.size();
                if (i2 > 0) {
                    printed = true;
                    pw.println("  * WEAK REFS:");
                    for (int i3 = 0; i3 < weakRefs.size(); i3++) {
                        pw.print("    #");
                        pw.print(i3);
                        pw.print(": ");
                        pw.println(weakRefs.get(i3));
                    }
                }
            }
            int sizeOfIntentsPerUid = this.mIntentsPerUid.size();
            if (sizeOfIntentsPerUid > 0) {
                for (int i4 = 0; i4 < sizeOfIntentsPerUid; i4++) {
                    pw.print("  * UID: ");
                    pw.print(this.mIntentsPerUid.keyAt(i4));
                    pw.print(" total: ");
                    pw.println(this.mIntentsPerUid.valueAt(i4));
                }
            }
            if (!printed) {
                pw.println("  (nothing)");
            }
        }
    }

    public java.util.List<android.app.PendingIntentStats> dumpPendingIntentStatsForStatsd() {
        java.util.List<android.app.PendingIntentStats> pendingIntentStats = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            if (this.mIntentSenderRecords.size() > 0) {
                android.util.SparseIntArray countsByUid = new android.util.SparseIntArray();
                android.util.SparseIntArray bundleSizesByUid = new android.util.SparseIntArray();
                for (java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> reference : this.mIntentSenderRecords.values()) {
                    if (reference != null && reference.get() != null) {
                        com.android.server.am.PendingIntentRecord record = reference.get();
                        int index = countsByUid.indexOfKey(record.uid);
                        if (index < 0) {
                            countsByUid.put(record.uid, 1);
                            bundleSizesByUid.put(record.uid, record.key.requestIntent.getExtrasTotalSize());
                        } else {
                            countsByUid.put(record.uid, countsByUid.valueAt(index) + 1);
                            bundleSizesByUid.put(record.uid, bundleSizesByUid.valueAt(index) + record.key.requestIntent.getExtrasTotalSize());
                        }
                    }
                }
                int size = countsByUid.size();
                for (int i = 0; i < size; i++) {
                    pendingIntentStats.add(new android.app.PendingIntentStats(countsByUid.keyAt(i), countsByUid.valueAt(i), bundleSizesByUid.valueAt(i) / 1024));
                }
            }
        }
        return pendingIntentStats;
    }

    void incrementUidStatLocked(com.android.server.am.PendingIntentRecord pir) {
        int uid = pir.uid;
        int idx = this.mIntentsPerUid.indexOfKey(uid);
        int newCount = 1;
        if (idx < 0) {
            this.mIntentsPerUid.put(uid, 1);
        } else {
            newCount = this.mIntentsPerUid.valueAt(idx) + 1;
            this.mIntentsPerUid.setValueAt(idx, newCount);
        }
        this.mPendingIntentControllerExt.addPendingIntentUid(uid, pir.key.packageName, pir.key.userId);
        int lowBound = (this.mConstants.PENDINGINTENT_WARNING_THRESHOLD - 10) + 1;
        com.android.internal.util.RingBuffer<java.lang.String> recentHistory = null;
        if (newCount == lowBound) {
            recentHistory = new com.android.internal.util.RingBuffer<>(java.lang.String.class, 10);
            this.mRecentIntentsPerUid.put(uid, recentHistory);
        } else if (newCount > lowBound && newCount <= this.mConstants.PENDINGINTENT_WARNING_THRESHOLD) {
            recentHistory = this.mRecentIntentsPerUid.get(uid);
        }
        this.mPendingIntentControllerExt.recyclePendingIntentsIfNeed(newCount, pir, this, this.mIntentsPerUid);
        if (recentHistory == null) {
            return;
        }
        recentHistory.append(pir.key.toString());
        if (newCount == this.mConstants.PENDINGINTENT_WARNING_THRESHOLD) {
            android.util.Slog.wtf("ActivityManager", "Too many PendingIntent created for uid " + uid + ", recent 10: " + java.util.Arrays.toString(recentHistory.toArray()));
            this.mRecentIntentsPerUid.remove(uid);
        }
    }

    void decrementUidStatLocked(com.android.server.am.PendingIntentRecord pir) {
        int uid = pir.uid;
        int idx = this.mIntentsPerUid.indexOfKey(uid);
        if (idx >= 0) {
            int newCount = this.mIntentsPerUid.valueAt(idx) - 1;
            if (newCount == this.mConstants.PENDINGINTENT_WARNING_THRESHOLD - 10) {
                this.mRecentIntentsPerUid.delete(uid);
            }
            if (newCount == 0) {
                this.mIntentsPerUid.removeAt(idx);
                this.mPendingIntentControllerExt.removePendingIntentUid(uid);
            } else {
                this.mIntentsPerUid.setValueAt(idx, newCount);
                this.mPendingIntentControllerExt.deletePendingIntentUid(uid, pir.key.packageName, pir.key.userId);
            }
        }
    }
}
