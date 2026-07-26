package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public final class ContentProviderConnection extends android.os.Binder implements com.android.server.am.OomAdjusterModernImpl.Connection {
    public com.android.internal.app.procstats.AssociationState.SourceState association;
    public final com.android.server.am.ProcessRecord client;
    public final java.lang.String clientPackage;
    public boolean dead;
    final int mExpectedUserId;
    private int mNumStableIncs;
    private int mNumUnstableIncs;
    private java.lang.Object mProcStatsLock;
    private int mStableCount;
    private int mUnstableCount;
    public final com.android.server.am.ContentProviderRecord provider;
    public boolean waiting;
    private final java.lang.Object mLock = new java.lang.Object();
    public final long createTime = android.os.SystemClock.elapsedRealtime();

    public ContentProviderConnection(com.android.server.am.ContentProviderRecord _provider, com.android.server.am.ProcessRecord _client, java.lang.String _clientPackage, int _expectedUserId) {
        this.provider = _provider;
        this.client = _client;
        this.clientPackage = _clientPackage;
        this.mExpectedUserId = _expectedUserId;
    }

    @Override // com.android.server.am.OomAdjusterModernImpl.Connection
    public void computeHostOomAdjLSP(com.android.server.am.OomAdjuster oomAdjuster, com.android.server.am.ProcessRecord host, com.android.server.am.ProcessRecord client, long now, com.android.server.am.ProcessRecord topApp, boolean doingAll, int oomAdjReason, int cachedAdj) {
        oomAdjuster.computeProviderHostOomAdjLSP(this, host, client, now, topApp, doingAll, false, false, oomAdjReason, 1001, false, false);
    }

    @Override // com.android.server.am.OomAdjusterModernImpl.Connection
    public boolean canAffectCapabilities() {
        return false;
    }

    public void startAssociationIfNeeded() {
        if (this.association == null && this.provider.proc != null) {
            if (this.provider.appInfo.uid != this.client.uid || !this.provider.info.processName.equals(this.client.processName)) {
                com.android.internal.app.procstats.ProcessStats.ProcessStateHolder holder = this.provider.proc.getPkgList().get(this.provider.name.getPackageName());
                if (holder == null) {
                    android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "No package in referenced provider " + this.provider.name.toShortString() + ": proc=" + this.provider.proc);
                    return;
                }
                if (holder.pkg == null) {
                    android.util.Slog.wtf(com.android.server.am.IActivityManagerServiceExt.TAG, "Inactive holder in referenced provider " + this.provider.name.toShortString() + ": proc=" + this.provider.proc);
                    return;
                }
                this.mProcStatsLock = this.provider.proc.mService.mProcessStats.mLock;
                synchronized (this.mProcStatsLock) {
                    this.association = holder.pkg.getAssociationStateLocked(holder.state, this.provider.name.getClassName()).startSource(this.client.uid, this.client.processName, this.clientPackage);
                }
            }
        }
    }

    public void trackProcState(int procState, int seq) {
        if (this.association != null) {
            synchronized (this.mProcStatsLock) {
                this.association.trackProcState(procState, seq, android.os.SystemClock.uptimeMillis());
            }
        }
    }

    public void stopAssociation() {
        if (this.association != null) {
            synchronized (this.mProcStatsLock) {
                this.association.stop();
            }
            this.association = null;
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ContentProviderConnection{");
        toShortString(sb);
        sb.append('}');
        return sb.toString();
    }

    public java.lang.String toShortString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        toShortString(sb);
        return sb.toString();
    }

    public java.lang.String toClientString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        toClientString(sb);
        return sb.toString();
    }

    public void toShortString(java.lang.StringBuilder sb) {
        sb.append(this.provider.toShortString());
        sb.append("->");
        toClientString(sb);
    }

    public void toClientString(java.lang.StringBuilder sb) {
        sb.append(this.client.toShortString());
        synchronized (this.mLock) {
            sb.append(" s");
            sb.append(this.mStableCount);
            sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            sb.append(this.mNumStableIncs);
            sb.append(" u");
            sb.append(this.mUnstableCount);
            sb.append(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            sb.append(this.mNumUnstableIncs);
        }
        if (this.waiting) {
            sb.append(" WAITING");
        }
        if (this.dead) {
            sb.append(" DEAD");
        }
        long nowReal = android.os.SystemClock.elapsedRealtime();
        sb.append(" ");
        android.util.TimeUtils.formatDuration(nowReal - this.createTime, sb);
    }

    public void initializeCount(boolean stable) {
        synchronized (this.mLock) {
            if (stable) {
                this.mStableCount = 1;
                this.mNumStableIncs = 1;
                this.mUnstableCount = 0;
                this.mNumUnstableIncs = 0;
            } else {
                this.mStableCount = 0;
                this.mNumStableIncs = 0;
                this.mUnstableCount = 1;
                this.mNumUnstableIncs = 1;
            }
        }
    }

    public int incrementCount(boolean stable) {
        int i;
        synchronized (this.mLock) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                com.android.server.am.ContentProviderRecord cpr = this.provider;
                android.util.Slog.v(com.android.server.am.IActivityManagerServiceExt.TAG, "Adding provider requested by " + this.client.processName + " from process " + cpr.info.processName + ": " + cpr.name.flattenToShortString() + " scnt=" + this.mStableCount + " uscnt=" + this.mUnstableCount);
            }
            if (stable) {
                this.mStableCount++;
                this.mNumStableIncs++;
            } else {
                this.mUnstableCount++;
                this.mNumUnstableIncs++;
            }
            i = this.mStableCount + this.mUnstableCount;
        }
        return i;
    }

    public int decrementCount(boolean stable) {
        int i;
        synchronized (this.mLock) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROVIDER) {
                com.android.server.am.ContentProviderRecord cpr = this.provider;
                android.util.Slog.v(com.android.server.am.IActivityManagerServiceExt.TAG, "Removing provider requested by " + this.client.processName + " from process " + cpr.info.processName + ": " + cpr.name.flattenToShortString() + " scnt=" + this.mStableCount + " uscnt=" + this.mUnstableCount);
            }
            if (stable) {
                this.mStableCount--;
            } else {
                this.mUnstableCount--;
            }
            i = this.mStableCount + this.mUnstableCount;
        }
        return i;
    }

    public void adjustCounts(int stableIncrement, int unstableIncrement) {
        synchronized (this.mLock) {
            if (stableIncrement > 0) {
                this.mNumStableIncs += stableIncrement;
            }
            int stable = this.mStableCount + stableIncrement;
            if (stable < 0) {
                throw new java.lang.IllegalStateException("stableCount < 0: " + stable);
            }
            if (unstableIncrement > 0) {
                this.mNumUnstableIncs += unstableIncrement;
            }
            int unstable = this.mUnstableCount + unstableIncrement;
            if (unstable < 0) {
                throw new java.lang.IllegalStateException("unstableCount < 0: " + unstable);
            }
            if (stable + unstable <= 0) {
                throw new java.lang.IllegalStateException("ref counts can't go to zero here: stable=" + stable + " unstable=" + unstable);
            }
            this.mStableCount = stable;
            this.mUnstableCount = unstable;
        }
    }

    public int stableCount() {
        int i;
        synchronized (this.mLock) {
            i = this.mStableCount;
        }
        return i;
    }

    public int unstableCount() {
        int i;
        synchronized (this.mLock) {
            i = this.mUnstableCount;
        }
        return i;
    }

    int totalRefCount() {
        int i;
        synchronized (this.mLock) {
            i = this.mStableCount + this.mUnstableCount;
        }
        return i;
    }
}
