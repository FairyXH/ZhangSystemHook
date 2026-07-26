package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ProviderMap {
    private static final boolean DBG = false;
    private static final java.lang.String TAG = "ProviderMap";
    private final com.android.server.am.ActivityManagerService mAm;
    private final java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord> mSingletonByName = new java.util.HashMap<>();
    private final java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> mSingletonByClass = new java.util.HashMap<>();
    private final android.util.SparseArray<java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord>> mProvidersByNamePerUser = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord>> mProvidersByClassPerUser = new android.util.SparseArray<>();

    ProviderMap(com.android.server.am.ActivityManagerService am) {
        this.mAm = am;
    }

    com.android.server.am.ContentProviderRecord getProviderByName(java.lang.String name) {
        return getProviderByName(name, -1);
    }

    com.android.server.am.ContentProviderRecord getProviderByName(java.lang.String name, int userId) {
        com.android.server.am.ContentProviderRecord record = this.mSingletonByName.get(name);
        if (record != null) {
            return record;
        }
        return getProvidersByName(userId).get(name);
    }

    com.android.server.am.ContentProviderRecord getProviderByClass(android.content.ComponentName name) {
        return getProviderByClass(name, -1);
    }

    com.android.server.am.ContentProviderRecord getProviderByClass(android.content.ComponentName name, int userId) {
        com.android.server.am.ContentProviderRecord record = this.mSingletonByClass.get(name);
        if (record != null) {
            return record;
        }
        return getProvidersByClass(userId).get(name);
    }

    void putProviderByName(java.lang.String name, com.android.server.am.ContentProviderRecord record) {
        if (record.singleton) {
            this.mSingletonByName.put(name, record);
        } else {
            int userId = android.os.UserHandle.getUserId(record.appInfo.uid);
            getProvidersByName(userId).put(name, record);
        }
    }

    void putProviderByClass(android.content.ComponentName name, com.android.server.am.ContentProviderRecord record) {
        if (record.singleton) {
            this.mSingletonByClass.put(name, record);
        } else {
            int userId = android.os.UserHandle.getUserId(record.appInfo.uid);
            getProvidersByClass(userId).put(name, record);
        }
    }

    void removeProviderByName(java.lang.String name, int userId) {
        if (this.mSingletonByName.containsKey(name)) {
            this.mSingletonByName.remove(name);
            return;
        }
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Bad user " + userId);
        }
        java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord> map = getProvidersByName(userId);
        map.remove(name);
        if (map.size() == 0) {
            this.mProvidersByNamePerUser.remove(userId);
        }
    }

    void removeProviderByClass(android.content.ComponentName name, int userId) {
        if (this.mSingletonByClass.containsKey(name)) {
            this.mSingletonByClass.remove(name);
            return;
        }
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Bad user " + userId);
        }
        java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> map = getProvidersByClass(userId);
        map.remove(name);
        if (map.size() == 0) {
            this.mProvidersByClassPerUser.remove(userId);
        }
    }

    private java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord> getProvidersByName(int userId) {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Bad user " + userId);
        }
        java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord> map = this.mProvidersByNamePerUser.get(userId);
        if (map == null) {
            java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord> newMap = new java.util.HashMap<>();
            this.mProvidersByNamePerUser.put(userId, newMap);
            return newMap;
        }
        return map;
    }

    java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> getProvidersByClass(int userId) {
        if (userId < 0) {
            throw new java.lang.IllegalArgumentException("Bad user " + userId);
        }
        java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> map = this.mProvidersByClassPerUser.get(userId);
        if (map == null) {
            java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> newMap = new java.util.HashMap<>();
            this.mProvidersByClassPerUser.put(userId, newMap);
            return newMap;
        }
        return map;
    }

    private boolean collectPackageProvidersLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, boolean doit, boolean evenPersistent, java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> providers, java.util.ArrayList<com.android.server.am.ContentProviderRecord> result) {
        boolean didSomething = false;
        for (com.android.server.am.ContentProviderRecord provider : providers.values()) {
            boolean sameComponent = packageName == null || (provider.info.packageName.equals(packageName) && (filterByClasses == null || filterByClasses.contains(provider.name.getClassName())));
            if (sameComponent && (provider.proc == null || evenPersistent || !provider.proc.isPersistent())) {
                if (!doit) {
                    return true;
                }
                didSomething = true;
                result.add(provider);
            }
        }
        return didSomething;
    }

    boolean collectPackageProvidersLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, boolean doit, boolean evenPersistent, int userId, java.util.ArrayList<com.android.server.am.ContentProviderRecord> result) {
        boolean didSomething = (userId == -1 || userId == 0) ? collectPackageProvidersLocked(packageName, filterByClasses, doit, evenPersistent, this.mSingletonByClass, result) : false;
        if (!doit && didSomething) {
            return true;
        }
        if (userId == -1) {
            boolean didSomething2 = didSomething;
            for (int i = 0; i < this.mProvidersByClassPerUser.size(); i++) {
                if (collectPackageProvidersLocked(packageName, filterByClasses, doit, evenPersistent, this.mProvidersByClassPerUser.valueAt(i), result)) {
                    if (!doit) {
                        return true;
                    }
                    didSomething2 = true;
                }
            }
            return didSomething2;
        }
        java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> items = getProvidersByClass(userId);
        if (items != null) {
            return didSomething | collectPackageProvidersLocked(packageName, filterByClasses, doit, evenPersistent, items, result);
        }
        return didSomething;
    }

    private boolean dumpProvidersByClassLocked(java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage, java.lang.String header, boolean needSep, java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> map) {
        boolean written = false;
        for (java.util.Map.Entry<android.content.ComponentName, com.android.server.am.ContentProviderRecord> e : map.entrySet()) {
            com.android.server.am.ContentProviderRecord r = e.getValue();
            if (dumpPackage == null || dumpPackage.equals(r.appInfo.packageName)) {
                if (needSep) {
                    pw.println("");
                    needSep = false;
                }
                if (header != null) {
                    pw.println(header);
                    header = null;
                }
                written = true;
                pw.print("  * ");
                pw.println(r);
                r.dump(pw, "    ", dumpAll);
            }
        }
        return written;
    }

    private boolean dumpProvidersByNameLocked(java.io.PrintWriter pw, java.lang.String dumpPackage, java.lang.String header, boolean needSep, java.util.HashMap<java.lang.String, com.android.server.am.ContentProviderRecord> map) {
        boolean written = false;
        for (java.util.Map.Entry<java.lang.String, com.android.server.am.ContentProviderRecord> e : map.entrySet()) {
            com.android.server.am.ContentProviderRecord r = e.getValue();
            if (dumpPackage == null || dumpPackage.equals(r.appInfo.packageName)) {
                if (needSep) {
                    pw.println("");
                    needSep = false;
                }
                if (header != null) {
                    pw.println(header);
                    header = null;
                }
                written = true;
                pw.print("  ");
                pw.print(e.getKey());
                pw.print(": ");
                pw.println(r.toShortString());
            }
        }
        return written;
    }

    boolean dumpProvidersLocked(java.io.PrintWriter pw, boolean dumpAll, java.lang.String dumpPackage) {
        boolean needSep = this.mSingletonByClass.size() > 0 ? false | dumpProvidersByClassLocked(pw, dumpAll, dumpPackage, "  Published single-user content providers (by class):", false, this.mSingletonByClass) : false;
        boolean needSep2 = needSep;
        for (int i = 0; i < this.mProvidersByClassPerUser.size(); i++) {
            java.util.HashMap<android.content.ComponentName, com.android.server.am.ContentProviderRecord> map = this.mProvidersByClassPerUser.valueAt(i);
            needSep2 |= dumpProvidersByClassLocked(pw, dumpAll, dumpPackage, "  Published user " + this.mProvidersByClassPerUser.keyAt(i) + " content providers (by class):", needSep2, map);
        }
        if (dumpAll) {
            needSep2 = dumpProvidersByNameLocked(pw, dumpPackage, "  Single-user authority to provider mappings:", needSep2, this.mSingletonByName) | needSep2;
            for (int i2 = 0; i2 < this.mProvidersByNamePerUser.size(); i2++) {
                needSep2 |= dumpProvidersByNameLocked(pw, dumpPackage, "  User " + this.mProvidersByNamePerUser.keyAt(i2) + " authority to provider mappings:", needSep2, this.mProvidersByNamePerUser.valueAt(i2));
            }
        }
        return needSep2;
    }

    private java.util.ArrayList<com.android.server.am.ContentProviderRecord> getProvidersForName(java.lang.String name) {
        java.util.ArrayList<com.android.server.am.ContentProviderRecord> allProviders = new java.util.ArrayList<>();
        java.util.ArrayList<com.android.server.am.ContentProviderRecord> ret = new java.util.ArrayList<>();
        java.util.function.Predicate<com.android.server.am.ContentProviderRecord> filter = com.android.internal.util.DumpUtils.filterRecord(name);
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                allProviders.addAll(this.mSingletonByClass.values());
                for (int i = 0; i < this.mProvidersByClassPerUser.size(); i++) {
                    allProviders.addAll(this.mProvidersByClassPerUser.valueAt(i).values());
                }
                com.android.internal.util.CollectionUtils.addIf(allProviders, ret, filter);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        ret.sort(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.am.ProviderMap$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.am.ContentProviderRecord) obj).getComponentName();
            }
        }));
        return ret;
    }

    protected boolean dumpProvider(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String name, java.lang.String[] args, int opti, boolean dumpAll) throws java.lang.Throwable {
        try {
            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
            try {
                java.util.ArrayList<com.android.server.am.ContentProviderRecord> providers = getProvidersForName(name);
                if (providers.size() > 0) {
                    boolean needSep = false;
                    int i = 0;
                    while (i < providers.size()) {
                        if (needSep) {
                            pw.println();
                        }
                        dumpProvider("", fd, pw, providers.get(i), args, dumpAll);
                        i++;
                        needSep = true;
                    }
                    this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
                    return true;
                }
                this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
                return false;
            } catch (java.lang.Throwable th) {
                th = th;
                this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
                throw th;
            }
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
    }

    private void dumpProvider(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter pw, com.android.server.am.ContentProviderRecord r, java.lang.String[] args, boolean dumpAll) {
        android.app.IApplicationThread thread = r.proc != null ? r.proc.getThread() : null;
        for (java.lang.String s : args) {
            if (!dumpAll && s.contains("--proto")) {
                if (thread != null) {
                    dumpToTransferPipe(null, fd, pw, r, thread, args);
                    return;
                }
                return;
            }
        }
        java.lang.String innerPrefix = prefix + "  ";
        com.android.server.am.ActivityManagerService activityManagerService = this.mAm;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                pw.print(prefix);
                pw.print("PROVIDER ");
                pw.print(r);
                pw.print(" pid=");
                if (r.proc != null) {
                    pw.println(r.proc.getPid());
                } else {
                    pw.println("(not running)");
                }
                if (dumpAll) {
                    r.dump(pw, innerPrefix, true);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        if (thread != null) {
            pw.println("    Client:");
            pw.flush();
            dumpToTransferPipe("      ", fd, pw, r, thread, args);
        }
    }

    protected boolean dumpProviderProto(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String name, java.lang.String[] args) {
        android.app.IApplicationThread thread;
        java.lang.String[] newArgs = (java.lang.String[]) java.util.Arrays.copyOf(args, args.length + 1);
        newArgs[args.length] = "--proto";
        java.util.ArrayList<com.android.server.am.ContentProviderRecord> providers = getProvidersForName(name);
        if (providers.size() <= 0) {
            return false;
        }
        for (int i = 0; i < providers.size(); i++) {
            com.android.server.am.ContentProviderRecord r = providers.get(i);
            if (r.proc != null && (thread = r.proc.getThread()) != null) {
                dumpToTransferPipe(null, fd, pw, r, thread, newArgs);
                return true;
            }
        }
        return false;
    }

    private void dumpToTransferPipe(java.lang.String prefix, java.io.FileDescriptor fd, java.io.PrintWriter pw, com.android.server.am.ContentProviderRecord r, android.app.IApplicationThread thread, java.lang.String[] args) {
        try {
            com.android.internal.os.TransferPipe tp = new com.android.internal.os.TransferPipe();
            try {
                thread.dumpProvider(tp.getWriteFd(), r.provider.asBinder(), args);
                tp.setBufferPrefix(prefix);
                tp.go(fd, 2000L);
                tp.kill();
            } catch (java.lang.Throwable th) {
                tp.kill();
                throw th;
            }
        } catch (android.os.RemoteException e) {
            pw.println("      Got a RemoteException while dumping the service");
        } catch (java.io.IOException ex) {
            pw.println("      Failure while dumping the provider: " + ex);
        }
    }
}
