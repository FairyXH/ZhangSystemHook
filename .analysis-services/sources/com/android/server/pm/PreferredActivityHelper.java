package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class PreferredActivityHelper {
    private static final java.lang.String TAG_DEFAULT_APPS = "da";
    private static final java.lang.String TAG_PREFERRED_BACKUP = "pa";
    private final com.android.server.pm.BroadcastHelper mBroadcastHelper;
    private final com.android.server.pm.PackageManagerService mPm;

    /* JADX INFO: Access modifiers changed from: private */
    interface BlobXmlRestorer {
        void apply(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser, int i) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;
    }

    PreferredActivityHelper(com.android.server.pm.PackageManagerService pm, com.android.server.pm.BroadcastHelper broadcastHelper) {
        this.mPm = pm;
        this.mBroadcastHelper = broadcastHelper;
    }

    private android.content.pm.ResolveInfo findPreferredActivityNotLocked(com.android.server.pm.Computer snapshot, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<android.content.pm.ResolveInfo> query, boolean always, boolean removeMatches, boolean debug, int userId) {
        return findPreferredActivityNotLocked(snapshot, intent, resolvedType, flags, query, always, removeMatches, debug, userId, android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) >= 10000);
    }

    public android.content.pm.ResolveInfo findPreferredActivityNotLocked(com.android.server.pm.Computer snapshot, android.content.Intent intent, java.lang.String resolvedType, long flags, java.util.List<android.content.pm.ResolveInfo> query, boolean always, boolean removeMatches, boolean debug, int userId, boolean queryMayBeFiltered) {
        if (java.lang.Thread.holdsLock(this.mPm.mLock) || this.mPm.mPackageManagerServiceExt.isHoldingLockInFindPreferredActivityNotLocked()) {
            android.util.Slog.wtf("PackageManager", "Calling thread " + java.lang.Thread.currentThread().getName() + " is holding mLock", new java.lang.Throwable());
        }
        if (!this.mPm.mUserManager.exists(userId)) {
            return null;
        }
        com.android.server.pm.PackageManagerService.FindPreferredActivityBodyResult body = snapshot.findPreferredActivityInternal(intent, resolvedType, flags, query, always, removeMatches, debug, userId, queryMayBeFiltered);
        if (body.mChanged) {
            if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                android.util.Slog.v("PackageManager", "Preferred activity bookkeeping changed; writing restrictions");
            }
            this.mPm.scheduleWritePackageRestrictions(userId);
        }
        if ((com.android.server.pm.PackageManagerService.DEBUG_PREFERRED || debug) && body.mPreferredResolveInfo == null) {
            android.util.Slog.v("PackageManager", "No preferred activity to return");
        }
        return body.mPreferredResolveInfo;
    }

    public void clearPackagePreferredActivities(java.lang.String packageName, int userId) {
        android.util.SparseBooleanArray changedUsers = new android.util.SparseBooleanArray();
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPm.clearPackagePreferredActivitiesLPw(packageName, changedUsers, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (changedUsers.size() > 0) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), changedUsers);
            this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
            this.mPm.scheduleWritePackageRestrictions(userId);
        }
    }

    public boolean updateDefaultHomeNotLocked(com.android.server.pm.Computer snapshot, final int userId) {
        if (java.lang.Thread.holdsLock(this.mPm.mLock) || this.mPm.mPackageManagerServiceExt.isHoldingLockInUpdateDefaultHomeNotLocked()) {
            android.util.Slog.wtf("PackageManager", "Calling thread " + java.lang.Thread.currentThread().getName() + " is holding mLock", new java.lang.Throwable());
        }
        if (!this.mPm.isSystemReady()) {
            return false;
        }
        android.content.Intent intent = snapshot.getHomeIntent();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = snapshot.queryIntentActivitiesInternal(intent, null, 786432L, userId);
        android.content.pm.ResolveInfo preferredResolveInfo = findPreferredActivityNotLocked(snapshot, intent, null, 0L, resolveInfos, true, false, false, userId);
        java.lang.String packageName = (preferredResolveInfo == null || preferredResolveInfo.activityInfo == null) ? null : preferredResolveInfo.activityInfo.packageName;
        java.lang.String currentPackageName = this.mPm.getActiveLauncherPackageName(userId);
        if (android.text.TextUtils.equals(currentPackageName, packageName)) {
            return false;
        }
        java.lang.String[] callingPackages = snapshot.getPackagesForUid(android.os.Binder.getCallingUid());
        if ((callingPackages == null || !com.android.internal.util.ArrayUtils.contains(callingPackages, this.mPm.mRequiredPermissionControllerPackage)) && packageName != null) {
            return this.mPm.setActiveLauncherPackage(packageName, userId, new java.util.function.Consumer() { // from class: com.android.server.pm.PreferredActivityHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$updateDefaultHomeNotLocked$0(userId, (java.lang.Boolean) obj);
                }
            });
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDefaultHomeNotLocked$0(int userId, java.lang.Boolean successful) {
        if (successful.booleanValue()) {
            this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
        }
    }

    public void addPreferredActivity(com.android.server.pm.Computer snapshot, com.android.server.pm.WatchedIntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, boolean always, int userId, java.lang.String opname, boolean removeExisting) {
        if (this.mPm.mPackageManagerServiceExt.interceptAddPreferredActivity(filter.getIntentFilter(), match, set, activity, userId, removeExisting)) {
            return;
        }
        int callingUid = android.os.Binder.getCallingUid();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, false, "add preferred activity");
        if (this.mPm.mContext.checkCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS") != 0) {
            if (snapshot.getUidTargetSdkVersion(callingUid) < 8) {
                android.util.Slog.w("PackageManager", "Ignoring addPreferredActivity() from uid " + callingUid);
                return;
            }
            this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
        }
        if (filter.countActions() == 0) {
            android.util.Slog.w("PackageManager", "Cannot set a preferred activity with no filter actions");
            return;
        }
        this.mPm.mPackageManagerServiceExt.beforeAddInAddPreferredActivityInternal(activity, filter, userId);
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            android.util.Slog.i("PackageManager", opname + " activity " + activity.flattenToShortString() + " for user " + userId + ":");
            filter.dump(new android.util.LogPrinter(4, "PackageManager"), "  ");
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                try {
                    com.android.server.pm.PreferredIntentResolver pir = this.mPm.mSettings.editPreferredActivitiesLPw(userId);
                    java.util.ArrayList<com.android.server.pm.PreferredActivity> existing = pir.findFilters(filter);
                    if (removeExisting && existing != null) {
                        try {
                            com.android.server.pm.Settings.removeFilters(pir, filter, existing);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        }
                    }
                    pir.addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) this.mPm.snapshotComputer(), new com.android.server.pm.PreferredActivity(filter, match, set, activity, always));
                    this.mPm.scheduleWritePackageRestrictions(userId);
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    if (!isHomeFilter(filter) || !updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), userId)) {
                        this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public void replacePreferredActivity(com.android.server.pm.Computer snapshot, com.android.server.pm.WatchedIntentFilter filter, int match, android.content.ComponentName[] set, android.content.ComponentName activity, int userId) {
        if (this.mPm.mPackageManagerServiceExt.interceptReplacePreferredActivity(filter.getIntentFilter())) {
            return;
        }
        if (filter.countActions() != 1) {
            throw new java.lang.IllegalArgumentException("replacePreferredActivity expects filter to have only 1 action.");
        }
        if (filter.countDataAuthorities() != 0 || filter.countDataPaths() != 0 || filter.countDataSchemes() > 1 || filter.countDataTypes() != 0) {
            throw new java.lang.IllegalArgumentException("replacePreferredActivity expects filter to have no data authorities, paths, or types; and at most one scheme.");
        }
        int callingUid = android.os.Binder.getCallingUid();
        snapshot.enforceCrossUserPermission(callingUid, userId, true, false, "replace preferred activity");
        if (this.mPm.mContext.checkCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS") != 0) {
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    if (this.mPm.snapshotComputer().getUidTargetSdkVersion(callingUid) < 8) {
                        android.util.Slog.w("PackageManager", "Ignoring replacePreferredActivity() from uid " + android.os.Binder.getCallingUid());
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        return;
                    } else {
                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
                    }
                } finally {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                }
            }
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock2) {
            try {
                try {
                    com.android.server.pm.PreferredIntentResolver pir = this.mPm.mSettings.getPreferredActivities(userId);
                    if (pir != null) {
                        try {
                            java.util.ArrayList<com.android.server.pm.PreferredActivity> existing = pir.findFilters(filter);
                            if (existing != null && existing.size() == 1) {
                                com.android.server.pm.PreferredActivity cur = existing.get(0);
                                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                    android.util.Slog.i("PackageManager", "Checking replace of preferred:");
                                    filter.dump(new android.util.LogPrinter(4, "PackageManager"), "  ");
                                    if (cur.mPref.mAlways) {
                                        android.util.Slog.i("PackageManager", "  -- CUR: mMatch=" + cur.mPref.mMatch);
                                        android.util.Slog.i("PackageManager", "  -- CUR: mSet=" + java.util.Arrays.toString(cur.mPref.mSetComponents));
                                        android.util.Slog.i("PackageManager", "  -- CUR: mComponent=" + cur.mPref.mShortComponent);
                                        android.util.Slog.i("PackageManager", "  -- NEW: mMatch=" + (match & 268369920));
                                        android.util.Slog.i("PackageManager", "  -- CUR: mSet=" + java.util.Arrays.toString(set));
                                        android.util.Slog.i("PackageManager", "  -- CUR: mComponent=" + activity.flattenToShortString());
                                    } else {
                                        android.util.Slog.i("PackageManager", "  -- CUR; not mAlways!");
                                    }
                                }
                                if (cur.mPref.mAlways) {
                                    try {
                                        if (cur.mPref.mComponent.equals(activity) && cur.mPref.mMatch == (match & 268369920)) {
                                            if (cur.mPref.sameSet(set)) {
                                                if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
                                                    android.util.Slog.i("PackageManager", "Replacing with same preferred activity " + cur.mPref.mShortComponent + " for user " + userId + ":");
                                                    filter.dump(new android.util.LogPrinter(4, "PackageManager"), "  ");
                                                }
                                                return;
                                            }
                                        }
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                                        throw th;
                                    }
                                }
                            }
                            if (existing != null) {
                                com.android.server.pm.Settings.removeFilters(pir, filter, existing);
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    addPreferredActivity(this.mPm.snapshotComputer(), filter, match, set, activity, true, userId, "Replacing preferred", false);
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    public void clearPackagePreferredActivities(com.android.server.pm.Computer snapshot, java.lang.String packageName) {
        int callingUid = android.os.Binder.getCallingUid();
        if (snapshot.getInstantAppPackageName(callingUid) != null) {
            return;
        }
        com.android.server.pm.pkg.PackageStateInternal packageState = snapshot.getPackageStateInternal(packageName);
        if ((packageState == null || !snapshot.isCallerSameApp(packageName, callingUid)) && this.mPm.mContext.checkCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS") != 0) {
            if (snapshot.getUidTargetSdkVersion(callingUid) < 8) {
                android.util.Slog.w("PackageManager", "Ignoring clearPackagePreferredActivities() from uid " + callingUid);
                return;
            }
            this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
        }
        if (packageState != null && snapshot.shouldFilterApplication(packageState, callingUid, android.os.UserHandle.getUserId(callingUid))) {
            return;
        }
        int callingUserId = android.os.UserHandle.getCallingUserId();
        clearPackagePreferredActivities(packageName, callingUserId);
    }

    void updateDefaultHomeNotLocked(com.android.server.pm.Computer snapshot, android.util.SparseBooleanArray userIds) {
        if (java.lang.Thread.holdsLock(this.mPm.mLock) || this.mPm.mPackageManagerServiceExt.isHoldingLockInUpdateDefaultHomeNotLockedMulti()) {
            android.util.Slog.wtf("PackageManager", "Calling thread " + java.lang.Thread.currentThread().getName() + " is holding mLock", new java.lang.Throwable());
        }
        for (int i = userIds.size() - 1; i >= 0; i--) {
            int userId = userIds.keyAt(i);
            updateDefaultHomeNotLocked(snapshot, userId);
        }
    }

    public void setHomeActivity(com.android.server.pm.Computer snapshot, android.content.ComponentName comp, int userId) {
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return;
        }
        java.util.ArrayList<android.content.pm.ResolveInfo> homeActivities = new java.util.ArrayList<>();
        snapshot.getHomeActivitiesAsUser(homeActivities, userId);
        boolean found = false;
        int size = homeActivities.size();
        android.content.ComponentName[] set = new android.content.ComponentName[size];
        for (int i = 0; i < size; i++) {
            android.content.pm.ResolveInfo candidate = homeActivities.get(i);
            android.content.pm.ActivityInfo info = candidate.activityInfo;
            android.content.ComponentName activityName = new android.content.ComponentName(info.packageName, info.name);
            set[i] = activityName;
            if (!found && activityName.equals(comp)) {
                found = true;
            }
        }
        if (!found) {
            throw new java.lang.IllegalArgumentException("Component " + comp + " cannot be home on user " + userId);
        }
        replacePreferredActivity(snapshot, getHomeFilter(), 1048576, set, comp, userId);
    }

    private com.android.server.pm.WatchedIntentFilter getHomeFilter() {
        com.android.server.pm.WatchedIntentFilter filter = new com.android.server.pm.WatchedIntentFilter("android.intent.action.MAIN");
        filter.addCategory("android.intent.category.HOME");
        filter.addCategory("android.intent.category.DEFAULT");
        return filter;
    }

    public void addPersistentPreferredActivity(com.android.server.pm.WatchedIntentFilter filter, android.content.ComponentName activity, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new java.lang.SecurityException("addPersistentPreferredActivity can only be run by the system");
        }
        if (!filter.checkDataPathAndSchemeSpecificParts()) {
            android.util.EventLog.writeEvent(1397638484, "246749702", java.lang.Integer.valueOf(callingUid));
            throw new java.lang.IllegalArgumentException("Invalid intent data paths or scheme specific parts in the filter.");
        }
        if (filter.countActions() == 0) {
            android.util.Slog.w("PackageManager", "Cannot set a preferred activity with no filter actions");
            return;
        }
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            android.util.Slog.i("PackageManager", "Adding persistent preferred activity " + activity + " for user " + userId + ":");
            filter.dump(new android.util.LogPrinter(4, "PackageManager"), "  ");
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPm.mSettings.editPersistentPreferredActivitiesLPw(userId).addFilter((com.android.server.pm.snapshot.PackageDataSnapshot) this.mPm.snapshotComputer(), new com.android.server.pm.PersistentPreferredActivity(filter, activity, true));
                this.mPm.scheduleWritePackageRestrictions(userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (isHomeFilter(filter)) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), userId);
        }
        this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
    }

    public void clearPackagePersistentPreferredActivities(java.lang.String packageName, int userId) {
        boolean changed;
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new java.lang.SecurityException("clearPackagePersistentPreferredActivities can only be run by the system");
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                changed = this.mPm.mSettings.clearPackagePersistentPreferredActivities(packageName, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (changed) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), userId);
            this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
            this.mPm.scheduleWritePackageRestrictions(userId);
        }
    }

    public void clearPersistentPreferredActivity(android.content.IntentFilter filter, int userId) {
        boolean changed;
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new java.lang.SecurityException("clearPersistentPreferredActivity can only be run by the system");
        }
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                changed = this.mPm.mSettings.clearPersistentPreferredActivity(filter, userId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        if (changed) {
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), userId);
            this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
            this.mPm.scheduleWritePackageRestrictions(userId);
        }
    }

    private boolean isHomeFilter(com.android.server.pm.WatchedIntentFilter filter) {
        return filter.hasAction("android.intent.action.MAIN") && filter.hasCategory("android.intent.category.HOME") && filter.hasCategory("android.intent.category.DEFAULT");
    }

    private void restoreFromXml(com.android.modules.utils.TypedXmlPullParser parser, int userId, java.lang.String expectedStartTag, com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer functor) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                android.util.Slog.e("PackageManager", "Didn't find start tag during restore");
            }
        } else if (!expectedStartTag.equals(parser.getName())) {
            if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                android.util.Slog.e("PackageManager", "Found unexpected tag " + parser.getName());
            }
        } else {
            while (parser.next() == 4) {
            }
            functor.apply(parser, userId);
        }
    }

    public byte[] getPreferredActivityBackup(int userId) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the system may call getPreferredActivityBackup()");
        }
        java.io.ByteArrayOutputStream dataStream = new java.io.ByteArrayOutputStream();
        try {
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.newFastSerializer();
            serializer.setOutput(dataStream, java.nio.charset.StandardCharsets.UTF_8.name());
            serializer.startDocument((java.lang.String) null, true);
            serializer.startTag((java.lang.String) null, TAG_PREFERRED_BACKUP);
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    this.mPm.mSettings.writePreferredActivitiesLPr(serializer, userId, true);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            serializer.endTag((java.lang.String) null, TAG_PREFERRED_BACKUP);
            serializer.endDocument();
            serializer.flush();
            return dataStream.toByteArray();
        } catch (java.lang.Exception e) {
            if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                android.util.Slog.e("PackageManager", "Unable to write preferred activities for backup", e);
            }
            return null;
        }
    }

    public void restorePreferredActivities(byte[] backup, int userId) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the system may call restorePreferredActivities()");
        }
        try {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
            parser.setInput(new java.io.ByteArrayInputStream(backup), java.nio.charset.StandardCharsets.UTF_8.name());
            restoreFromXml(parser, userId, TAG_PREFERRED_BACKUP, new com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer() { // from class: com.android.server.pm.PreferredActivityHelper$$ExternalSyntheticLambda2
                @Override // com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer
                public final void apply(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser, int i) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
                    this.f$0.lambda$restorePreferredActivities$1(typedXmlPullParser, i);
                }
            });
        } catch (java.lang.Exception e) {
            if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                android.util.Slog.e("PackageManager", "Exception restoring preferred activities: " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restorePreferredActivities$1(com.android.modules.utils.TypedXmlPullParser readParser, int readUserId) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
        com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
        synchronized (packageManagerTracedLock) {
            try {
                this.mPm.mSettings.readPreferredActivitiesLPw(readParser, readUserId);
            } catch (java.lang.Throwable th) {
                com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                throw th;
            }
        }
        com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), readUserId);
    }

    public byte[] getDefaultAppsBackup(int userId) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the system may call getDefaultAppsBackup()");
        }
        java.io.ByteArrayOutputStream dataStream = new java.io.ByteArrayOutputStream();
        try {
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.newFastSerializer();
            serializer.setOutput(dataStream, java.nio.charset.StandardCharsets.UTF_8.name());
            serializer.startDocument((java.lang.String) null, true);
            serializer.startTag((java.lang.String) null, TAG_DEFAULT_APPS);
            java.lang.String defaultBrowser = this.mPm.getDefaultBrowser(userId);
            com.android.server.pm.Settings.writeDefaultApps(serializer, defaultBrowser);
            serializer.endTag((java.lang.String) null, TAG_DEFAULT_APPS);
            serializer.endDocument();
            serializer.flush();
            return dataStream.toByteArray();
        } catch (java.lang.Exception e) {
            if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                android.util.Slog.e("PackageManager", "Unable to write default apps for backup", e);
            }
            return null;
        }
    }

    public void restoreDefaultApps(byte[] backup, int userId) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Only the system may call restoreDefaultApps()");
        }
        try {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
            parser.setInput(new java.io.ByteArrayInputStream(backup), java.nio.charset.StandardCharsets.UTF_8.name());
            restoreFromXml(parser, userId, TAG_DEFAULT_APPS, new com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer() { // from class: com.android.server.pm.PreferredActivityHelper$$ExternalSyntheticLambda1
                @Override // com.android.server.pm.PreferredActivityHelper.BlobXmlRestorer
                public final void apply(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser, int i) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
                    this.f$0.lambda$restoreDefaultApps$2(typedXmlPullParser, i);
                }
            });
        } catch (java.lang.Exception e) {
            if (com.android.server.pm.PackageManagerService.DEBUG_BACKUP) {
                android.util.Slog.e("PackageManager", "Exception restoring default apps: " + e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreDefaultApps$2(com.android.modules.utils.TypedXmlPullParser parser1, int userId1) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String defaultBrowser = com.android.server.pm.Settings.readDefaultApps(parser1);
        if (defaultBrowser != null) {
            com.android.server.pm.pkg.PackageStateInternal packageState = this.mPm.snapshotComputer().getPackageStateInternal(defaultBrowser);
            if (packageState != null && packageState.getUserStateOrDefault(userId1).isInstalled()) {
                this.mPm.setDefaultBrowser(defaultBrowser, userId1);
                return;
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    this.mPm.mSettings.setPendingDefaultBrowserLPw(defaultBrowser, userId1);
                } catch (java.lang.Throwable th) {
                    com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
                    throw th;
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
        }
    }

    public void resetApplicationPreferences(int userId) {
        this.mPm.mContext.enforceCallingOrSelfPermission("android.permission.SET_PREFERRED_APPLICATIONS", null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.util.SparseBooleanArray changedUsers = new android.util.SparseBooleanArray();
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock) {
                try {
                    this.mPm.clearPackagePreferredActivitiesLPw(null, changedUsers, userId);
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            if (changedUsers.size() > 0) {
                this.mBroadcastHelper.sendPreferredActivityChangedBroadcast(userId);
            }
            com.android.server.pm.PackageManagerTracedLock packageManagerTracedLock2 = this.mPm.mLock;
            com.android.server.pm.PackageManagerService.boostPriorityForPackageManagerTracedLockedSection();
            synchronized (packageManagerTracedLock2) {
                try {
                    this.mPm.mSettings.applyDefaultPreferredAppsLPw(userId);
                    this.mPm.mDomainVerificationManager.clearUser(userId);
                    this.mPm.mPermissionManager.resetRuntimePermissionsForUser(userId);
                } finally {
                }
            }
            com.android.server.pm.PackageManagerService.resetPriorityAfterPackageManagerTracedLockedSection();
            updateDefaultHomeNotLocked(this.mPm.snapshotComputer(), userId);
            resetNetworkPolicies(userId);
            this.mPm.scheduleWritePackageRestrictions(userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void resetNetworkPolicies(int userId) {
        ((com.android.server.net.NetworkPolicyManagerInternal) this.mPm.mInjector.getLocalService(com.android.server.net.NetworkPolicyManagerInternal.class)).resetUserState(userId);
    }

    public int getPreferredActivities(com.android.server.pm.Computer snapshot, java.util.List<android.content.IntentFilter> outFilters, java.util.List<android.content.ComponentName> outActivities, java.lang.String packageName) {
        java.util.List<com.android.server.pm.WatchedIntentFilter> temp = com.android.server.pm.WatchedIntentFilter.toWatchedIntentFilterList(outFilters);
        int result = getPreferredActivitiesInternal(snapshot, temp, outActivities, packageName);
        outFilters.clear();
        for (int i = 0; i < temp.size(); i++) {
            outFilters.add(temp.get(i).getIntentFilter());
        }
        return result;
    }

    private int getPreferredActivitiesInternal(com.android.server.pm.Computer snapshot, java.util.List<com.android.server.pm.WatchedIntentFilter> outFilters, java.util.List<android.content.ComponentName> outActivities, java.lang.String packageName) {
        int userId;
        com.android.server.pm.PreferredIntentResolver pir;
        int callingUid = android.os.Binder.getCallingUid();
        if (snapshot.getInstantAppPackageName(callingUid) == null && (pir = snapshot.getPreferredActivities((userId = android.os.UserHandle.getCallingUserId()))) != null) {
            java.util.Iterator<F> itFilterIterator = pir.filterIterator();
            while (itFilterIterator.hasNext()) {
                com.android.server.pm.PreferredActivity pa = (com.android.server.pm.PreferredActivity) itFilterIterator.next();
                if (pa != null) {
                    java.lang.String prefPackageName = pa.mPref.mComponent.getPackageName();
                    if (packageName == null || (prefPackageName.equals(packageName) && pa.mPref.mAlways)) {
                        if (!snapshot.shouldFilterApplication(snapshot.getPackageStateInternal(prefPackageName), callingUid, userId)) {
                            if (outFilters != null) {
                                outFilters.add(new com.android.server.pm.WatchedIntentFilter(pa.getIntentFilter()));
                            }
                            if (outActivities != null) {
                                outActivities.add(pa.mPref.mComponent);
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public android.content.pm.ResolveInfo findPersistentPreferredActivity(com.android.server.pm.Computer snapshot, android.content.Intent intent, int userId) {
        if (!android.os.UserHandle.isSameApp(android.os.Binder.getCallingUid(), 1000)) {
            throw new java.lang.SecurityException("findPersistentPreferredActivity can only be run by the system");
        }
        if (!this.mPm.mUserManager.exists(userId)) {
            return null;
        }
        int callingUid = android.os.Binder.getCallingUid();
        android.content.Intent intent2 = com.android.server.pm.PackageManagerServiceUtils.updateIntentForResolve(intent);
        java.lang.String resolvedType = intent2.resolveTypeIfNeeded(this.mPm.mContext.getContentResolver());
        long flags = snapshot.updateFlagsForResolve(0L, userId, callingUid, false, snapshot.isImplicitImageCaptureIntentAndNotSetByDpc(intent2, userId, resolvedType, 0L));
        java.util.List<android.content.pm.ResolveInfo> query = snapshot.queryIntentActivitiesInternal(intent2, resolvedType, flags, userId);
        return snapshot.findPersistentPreferredActivity(intent2, resolvedType, flags, query, false, userId);
    }

    public void setLastChosenActivity(com.android.server.pm.Computer snapshot, android.content.Intent intent, java.lang.String resolvedType, int flags, com.android.server.pm.WatchedIntentFilter filter, int match, android.content.ComponentName activity) {
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return;
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            android.util.Log.v("PackageManager", "setLastChosenActivity intent=" + intent + " resolvedType=" + resolvedType + " flags=" + flags + " filter=" + filter + " match=" + match + " activity=" + activity);
            filter.dump(new android.util.PrintStreamPrinter(java.lang.System.out), "    ");
        }
        intent.setComponent(null);
        java.util.List<android.content.pm.ResolveInfo> query = snapshot.queryIntentActivitiesInternal(intent, resolvedType, flags, userId);
        findPreferredActivityNotLocked(snapshot, intent, resolvedType, flags, query, false, true, false, userId);
        addPreferredActivity(snapshot, filter, match, null, activity, false, userId, "Setting last chosen", false);
    }

    public android.content.pm.ResolveInfo getLastChosenActivity(com.android.server.pm.Computer snapshot, android.content.Intent intent, java.lang.String resolvedType, int flags) {
        if (snapshot.getInstantAppPackageName(android.os.Binder.getCallingUid()) != null) {
            return null;
        }
        int userId = android.os.UserHandle.getCallingUserId();
        if (com.android.server.pm.PackageManagerService.DEBUG_PREFERRED) {
            android.util.Log.v("PackageManager", "Querying last chosen activity for " + intent);
        }
        java.util.List<android.content.pm.ResolveInfo> query = snapshot.queryIntentActivitiesInternal(intent, resolvedType, flags, userId);
        return findPreferredActivityNotLocked(snapshot, intent, resolvedType, flags, query, false, false, false, userId);
    }
}
