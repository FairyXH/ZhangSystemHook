package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ContentProviderRecord implements android.content.ComponentName.WithComponentName {
    static final int MAX_RETRY_COUNT = 3;
    final android.content.pm.ApplicationInfo appInfo;
    int externalProcessNoHandleCount;
    android.util.ArrayMap<android.os.IBinder, com.android.server.am.ContentProviderRecord.ExternalProcessHandle> externalProcessTokenToHandle;
    public final android.content.pm.ProviderInfo info;
    com.android.server.am.ProcessRecord launchingApp;
    int mRestartCount;
    final android.content.ComponentName name;
    public boolean noReleaseNeeded;
    com.android.server.am.ProcessRecord proc;
    public android.content.IContentProvider provider;
    final com.android.server.am.ActivityManagerService service;
    java.lang.String shortStringName;
    final boolean singleton;
    java.lang.String stringName;
    final int uid;
    final java.util.ArrayList<com.android.server.am.ContentProviderConnection> connections = new java.util.ArrayList<>();
    public com.android.server.am.IContentProviderRecordExt mContentProviderRecordExt = (com.android.server.am.IContentProviderRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IContentProviderRecordExt.class).create();

    public ContentProviderRecord(com.android.server.am.ActivityManagerService _service, android.content.pm.ProviderInfo _info, android.content.pm.ApplicationInfo ai, android.content.ComponentName _name, boolean _singleton) {
        this.service = _service;
        this.info = _info;
        this.uid = ai.uid;
        this.appInfo = ai;
        this.name = _name;
        this.singleton = _singleton;
        this.noReleaseNeeded = (this.uid == 0 || this.uid == 1000) && (_name == null || !"com.android.settings".equals(_name.getPackageName()));
        if (_name != null && this.noReleaseNeeded) {
            this.noReleaseNeeded = !this.mContentProviderRecordExt.isNeedRelease(_name);
        }
    }

    public ContentProviderRecord(com.android.server.am.ContentProviderRecord cpr) {
        this.service = cpr.service;
        this.info = cpr.info;
        this.uid = cpr.uid;
        this.appInfo = cpr.appInfo;
        this.name = cpr.name;
        this.singleton = cpr.singleton;
        this.noReleaseNeeded = cpr.noReleaseNeeded;
    }

    public android.app.ContentProviderHolder newHolder(com.android.server.am.ContentProviderConnection conn, boolean local) {
        android.app.ContentProviderHolder holder = new android.app.ContentProviderHolder(this.info);
        holder.provider = this.provider;
        holder.noReleaseNeeded = this.noReleaseNeeded;
        holder.connection = conn;
        holder.mLocal = local;
        return holder;
    }

    public void setProcess(com.android.server.am.ProcessRecord proc) {
        this.proc = proc;
        for (int iconn = this.connections.size() - 1; iconn >= 0; iconn--) {
            com.android.server.am.ContentProviderConnection conn = this.connections.get(iconn);
            if (proc != null) {
                conn.startAssociationIfNeeded();
            } else {
                conn.stopAssociation();
            }
        }
        if (this.externalProcessTokenToHandle != null) {
            for (int iext = this.externalProcessTokenToHandle.size() - 1; iext >= 0; iext--) {
                com.android.server.am.ContentProviderRecord.ExternalProcessHandle handle = this.externalProcessTokenToHandle.valueAt(iext);
                if (proc != null) {
                    handle.startAssociationIfNeeded(this);
                } else {
                    handle.stopAssociation();
                }
            }
        }
    }

    public boolean canRunHere(com.android.server.am.ProcessRecord app) {
        return (this.info.multiprocess || this.info.processName.equals(app.processName)) && this.uid == app.info.uid;
    }

    public void addExternalProcessHandleLocked(android.os.IBinder token, int callingUid, java.lang.String callingTag) {
        if (token == null) {
            this.externalProcessNoHandleCount++;
            return;
        }
        if (this.externalProcessTokenToHandle == null) {
            this.externalProcessTokenToHandle = new android.util.ArrayMap<>();
        }
        com.android.server.am.ContentProviderRecord.ExternalProcessHandle handle = this.externalProcessTokenToHandle.get(token);
        if (handle == null) {
            handle = new com.android.server.am.ContentProviderRecord.ExternalProcessHandle(token, callingUid, callingTag);
            this.externalProcessTokenToHandle.put(token, handle);
            handle.startAssociationIfNeeded(this);
        }
        handle.mAcquisitionCount++;
    }

    public boolean removeExternalProcessHandleLocked(android.os.IBinder token) {
        com.android.server.am.ContentProviderRecord.ExternalProcessHandle handle;
        if (hasExternalProcessHandles()) {
            boolean hasHandle = false;
            if (this.externalProcessTokenToHandle != null && (handle = this.externalProcessTokenToHandle.get(token)) != null) {
                hasHandle = true;
                handle.mAcquisitionCount--;
                if (handle.mAcquisitionCount == 0) {
                    removeExternalProcessHandleInternalLocked(token);
                    return true;
                }
            }
            if (!hasHandle) {
                this.externalProcessNoHandleCount--;
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeExternalProcessHandleInternalLocked(android.os.IBinder token) {
        com.android.server.am.ContentProviderRecord.ExternalProcessHandle handle = this.externalProcessTokenToHandle.get(token);
        handle.unlinkFromOwnDeathLocked();
        handle.stopAssociation();
        this.externalProcessTokenToHandle.remove(token);
        if (this.externalProcessTokenToHandle.size() == 0) {
            this.externalProcessTokenToHandle = null;
        }
    }

    public boolean hasExternalProcessHandles() {
        return this.externalProcessTokenToHandle != null || this.externalProcessNoHandleCount > 0;
    }

    public boolean hasConnectionOrHandle() {
        return !this.connections.isEmpty() || hasExternalProcessHandles();
    }

    void onProviderPublishStatusLocked(boolean status) {
        int numOfConns = this.connections.size();
        for (int i = 0; i < numOfConns; i++) {
            try {
                com.android.server.am.ContentProviderConnection conn = this.connections.get(i);
                if (conn.waiting && conn.client != null) {
                    com.android.server.am.ProcessRecord client = conn.client;
                    if (!status) {
                        if (this.launchingApp == null) {
                            android.util.Slog.w(com.android.server.am.IActivityManagerServiceExt.TAG, "Unable to launch app " + this.appInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.appInfo.uid + " for provider " + this.info.authority + ": launching app became null");
                            com.android.server.am.EventLogTags.writeAmProviderLostProcess(android.os.UserHandle.getUserId(this.appInfo.uid), this.appInfo.packageName, this.appInfo.uid, this.info.authority);
                        } else {
                            this.mContentProviderRecordExt.hookProviderTimeout(this.service, this.launchingApp, this.appInfo);
                            android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "Timeout waiting for provider " + this.appInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.appInfo.uid + " for provider " + this.info.authority + " caller=" + client);
                        }
                    }
                    android.app.IApplicationThread thread = client.getThread();
                    if (thread != null) {
                        try {
                            thread.notifyContentProviderPublishStatus(newHolder(status ? conn : null, false), this.info.authority, conn.mExpectedUserId, status);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                }
                conn.waiting = false;
            } catch (java.lang.IndexOutOfBoundsException e2) {
                android.util.Slog.e(com.android.server.am.IActivityManagerServiceExt.TAG, "cpr.connections.get cause IndexOutOfBoundsException, hosting process is " + this.proc + ", launching proc is " + this.launchingApp + ", provider is " + this.info);
            }
        }
        this.mContentProviderRecordExt.settleWaitTime(status);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix, boolean full) {
        if (full) {
            pw.print(prefix);
            pw.print("package=");
            pw.print(this.info.applicationInfo.packageName);
            pw.print(" process=");
            pw.println(this.info.processName);
        }
        pw.print(prefix);
        pw.print("proc=");
        pw.println(this.proc);
        if (this.launchingApp != null) {
            pw.print(prefix);
            pw.print("launchingApp=");
            pw.println(this.launchingApp);
        }
        if (full) {
            pw.print(prefix);
            pw.print("uid=");
            pw.print(this.uid);
            pw.print(" provider=");
            pw.println(this.provider);
        }
        if (this.singleton) {
            pw.print(prefix);
            pw.print("singleton=");
            pw.println(this.singleton);
        }
        pw.print(prefix);
        pw.print("authority=");
        pw.println(this.info.authority);
        if (full && (this.info.isSyncable || this.info.multiprocess || this.info.initOrder != 0)) {
            pw.print(prefix);
            pw.print("isSyncable=");
            pw.print(this.info.isSyncable);
            pw.print(" multiprocess=");
            pw.print(this.info.multiprocess);
            pw.print(" initOrder=");
            pw.println(this.info.initOrder);
        }
        if (full) {
            if (hasExternalProcessHandles()) {
                pw.print(prefix);
                pw.print("externals:");
                if (this.externalProcessTokenToHandle != null) {
                    pw.print(" w/token=");
                    pw.print(this.externalProcessTokenToHandle.size());
                }
                if (this.externalProcessNoHandleCount > 0) {
                    pw.print(" notoken=");
                    pw.print(this.externalProcessNoHandleCount);
                }
                pw.println();
            }
        } else if (this.connections.size() > 0 || this.externalProcessNoHandleCount > 0) {
            pw.print(prefix);
            pw.print(this.connections.size());
            pw.print(" connections, ");
            pw.print(this.externalProcessNoHandleCount);
            pw.println(" external handles");
        }
        if (this.connections.size() > 0) {
            if (full) {
                pw.print(prefix);
                pw.println("Connections:");
            }
            for (int i = 0; i < this.connections.size(); i++) {
                com.android.server.am.ContentProviderConnection conn = this.connections.get(i);
                pw.print(prefix);
                pw.print("  -> ");
                pw.println(conn.toClientString());
                if (conn.provider != this) {
                    pw.print(prefix);
                    pw.print("    *** WRONG PROVIDER: ");
                    pw.println(conn.provider);
                }
            }
        }
        this.mContentProviderRecordExt.handleExtendDump(pw, prefix);
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ContentProviderRecord{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" u");
        sb.append(android.os.UserHandle.getUserId(this.uid));
        sb.append(' ');
        sb.append(this.name.flattenToShortString());
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }

    public java.lang.String toShortString() {
        if (this.shortStringName != null) {
            return this.shortStringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append('/');
        sb.append(this.name.flattenToShortString());
        java.lang.String string = sb.toString();
        this.shortStringName = string;
        return string;
    }

    private class ExternalProcessHandle implements android.os.IBinder.DeathRecipient {
        private static final java.lang.String LOG_TAG = "ExternalProcessHanldle";
        int mAcquisitionCount;
        com.android.internal.app.procstats.AssociationState.SourceState mAssociation;
        final java.lang.String mOwningProcessName;
        final int mOwningUid;
        private java.lang.Object mProcStatsLock;
        final android.os.IBinder mToken;

        public ExternalProcessHandle(android.os.IBinder token, int owningUid, java.lang.String owningProcessName) {
            this.mToken = token;
            this.mOwningUid = owningUid;
            this.mOwningProcessName = owningProcessName;
            try {
                token.linkToDeath(this, 0);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Couldn't register for death for token: " + this.mToken, re);
            }
        }

        public void unlinkFromOwnDeathLocked() {
            this.mToken.unlinkToDeath(this, 0);
        }

        public void startAssociationIfNeeded(com.android.server.am.ContentProviderRecord provider) {
            if (this.mAssociation == null && provider.proc != null) {
                if (provider.appInfo.uid != this.mOwningUid || !provider.info.processName.equals(this.mOwningProcessName)) {
                    com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder = provider.proc.getPkgList().get(provider.name.getPackageName());
                    if (holder == null) {
                        android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "No package in referenced provider " + provider.name.toShortString() + ": proc=" + provider.proc);
                        return;
                    }
                    if (holder.pkg == null) {
                        android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "Inactive holder in referenced provider " + provider.name.toShortString() + ": proc=" + provider.proc);
                        return;
                    }
                    this.mProcStatsLock = provider.proc.mService.mProcessStats.mLock;
                    synchronized (this.mProcStatsLock) {
                        this.mAssociation = holder.pkg.getAssociationStateLocked(holder.state, provider.name.getClassName()).startSource(this.mOwningUid, this.mOwningProcessName, (java.lang.String) null);
                    }
                }
            }
        }

        public void stopAssociation() {
            if (this.mAssociation != null) {
                synchronized (this.mProcStatsLock) {
                    this.mAssociation.stop();
                }
                this.mAssociation = null;
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.am.ActivityManagerService activityManagerService = com.android.server.am.ContentProviderRecord.this.service;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService) {
                try {
                    if (com.android.server.am.ContentProviderRecord.this.hasExternalProcessHandles() && com.android.server.am.ContentProviderRecord.this.externalProcessTokenToHandle.get(this.mToken) != null) {
                        com.android.server.am.ContentProviderRecord.this.removeExternalProcessHandleInternalLocked(this.mToken);
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    public android.content.ComponentName getComponentName() {
        return this.name;
    }
}
