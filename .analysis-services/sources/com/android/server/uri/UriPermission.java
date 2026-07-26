package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
final class UriPermission {
    static final long INVALID_TIME = Long.MIN_VALUE;
    public static final int STRENGTH_GLOBAL = 2;
    public static final int STRENGTH_NONE = 0;
    public static final int STRENGTH_OWNED = 1;
    public static final int STRENGTH_PERSISTABLE = 3;
    private static final java.lang.String TAG = "UriPermission";
    private android.util.ArraySet<com.android.server.uri.UriPermissionOwner> mReadOwners;
    private android.util.ArraySet<com.android.server.uri.UriPermissionOwner> mWriteOwners;
    final java.lang.String sourcePkg;
    private java.lang.String stringName;
    final java.lang.String targetPkg;
    final int targetUid;
    final int targetUserId;
    final com.android.server.uri.GrantUri uri;
    int modeFlags = 0;
    int ownedModeFlags = 0;
    int globalModeFlags = 0;
    int persistableModeFlags = 0;
    int persistedModeFlags = 0;
    long persistedCreateTime = Long.MIN_VALUE;

    UriPermission(java.lang.String sourcePkg, java.lang.String targetPkg, int targetUid, com.android.server.uri.GrantUri uri) {
        this.targetUserId = android.os.UserHandle.getUserId(targetUid);
        this.sourcePkg = sourcePkg;
        this.targetPkg = targetPkg;
        this.targetUid = targetUid;
        this.uri = uri;
    }

    private void updateModeFlags() {
        int oldModeFlags = this.modeFlags;
        this.modeFlags = this.ownedModeFlags | this.globalModeFlags | this.persistedModeFlags;
        if (android.util.Log.isLoggable(TAG, 2) && this.modeFlags != oldModeFlags) {
            android.util.Slog.d(TAG, "Permission for " + this.targetPkg + " to " + this.uri + " is changing from 0x" + java.lang.Integer.toHexString(oldModeFlags) + " to 0x" + java.lang.Integer.toHexString(this.modeFlags) + " via calling UID " + android.os.Binder.getCallingUid() + " PID " + android.os.Binder.getCallingPid(), new java.lang.Throwable());
        }
    }

    void initPersistedModes(int modeFlags, long createdTime) {
        int modeFlags2 = modeFlags & 3;
        this.persistableModeFlags = modeFlags2;
        this.persistedModeFlags = modeFlags2;
        this.persistedCreateTime = createdTime;
        updateModeFlags();
    }

    boolean grantModes(int modeFlags, com.android.server.uri.UriPermissionOwner owner) {
        boolean persistable = (modeFlags & 64) != 0;
        int modeFlags2 = modeFlags & 3;
        if (persistable) {
            this.persistableModeFlags |= modeFlags2;
        }
        if (owner == null) {
            this.globalModeFlags |= modeFlags2;
        } else {
            if ((modeFlags2 & 1) != 0) {
                addReadOwner(owner);
            }
            if ((modeFlags2 & 2) != 0) {
                addWriteOwner(owner);
            }
        }
        updateModeFlags();
        return false;
    }

    boolean takePersistableModes(int modeFlags) {
        int modeFlags2 = modeFlags & 3;
        if ((this.persistableModeFlags & modeFlags2) != modeFlags2) {
            android.util.Slog.w(TAG, "Requested flags 0x" + java.lang.Integer.toHexString(modeFlags2) + ", but only 0x" + java.lang.Integer.toHexString(this.persistableModeFlags) + " are allowed");
            return false;
        }
        int before = this.persistedModeFlags;
        this.persistedModeFlags |= this.persistableModeFlags & modeFlags2;
        if (this.persistedModeFlags != 0) {
            this.persistedCreateTime = java.lang.System.currentTimeMillis();
        }
        updateModeFlags();
        return this.persistedModeFlags != before;
    }

    boolean releasePersistableModes(int modeFlags) {
        int before = this.persistedModeFlags;
        this.persistedModeFlags &= ~(modeFlags & 3);
        if (this.persistedModeFlags == 0) {
            this.persistedCreateTime = Long.MIN_VALUE;
        }
        updateModeFlags();
        return this.persistedModeFlags != before;
    }

    boolean revokeModes(int modeFlags, boolean includingOwners) {
        boolean persistable = (modeFlags & 64) != 0;
        int modeFlags2 = modeFlags & 3;
        int before = this.persistedModeFlags;
        if ((modeFlags2 & 1) != 0) {
            if (persistable) {
                this.persistableModeFlags &= -2;
                this.persistedModeFlags &= -2;
            }
            this.globalModeFlags &= -2;
            if (this.mReadOwners != null && includingOwners) {
                this.ownedModeFlags &= -2;
                for (com.android.server.uri.UriPermissionOwner r : this.mReadOwners) {
                    if (r != null) {
                        r.removeReadPermission(this);
                    }
                }
                this.mReadOwners = null;
            }
        }
        if ((modeFlags2 & 2) != 0) {
            if (persistable) {
                this.persistableModeFlags &= -3;
                this.persistedModeFlags &= -3;
            }
            this.globalModeFlags &= -3;
            if (this.mWriteOwners != null && includingOwners) {
                this.ownedModeFlags &= -3;
                for (com.android.server.uri.UriPermissionOwner r2 : this.mWriteOwners) {
                    if (r2 != null) {
                        r2.removeWritePermission(this);
                    }
                }
                this.mWriteOwners = null;
            }
        }
        if (this.persistedModeFlags == 0) {
            this.persistedCreateTime = Long.MIN_VALUE;
        }
        updateModeFlags();
        return this.persistedModeFlags != before;
    }

    public int getStrength(int modeFlags) {
        int modeFlags2 = modeFlags & 3;
        if ((this.persistableModeFlags & modeFlags2) == modeFlags2) {
            return 3;
        }
        if ((this.globalModeFlags & modeFlags2) == modeFlags2) {
            return 2;
        }
        if ((this.ownedModeFlags & modeFlags2) == modeFlags2) {
            return 1;
        }
        return 0;
    }

    private void addReadOwner(com.android.server.uri.UriPermissionOwner owner) {
        if (this.mReadOwners == null) {
            this.mReadOwners = com.google.android.collect.Sets.newArraySet();
            this.ownedModeFlags |= 1;
            updateModeFlags();
        }
        if (this.mReadOwners.add(owner)) {
            owner.addReadPermission(this);
        }
    }

    void removeReadOwner(com.android.server.uri.UriPermissionOwner owner) {
        if (this.mReadOwners == null || !this.mReadOwners.remove(owner)) {
            android.util.Slog.wtf(TAG, "Unknown read owner " + owner + " in " + this);
        } else if (this.mReadOwners.size() == 0) {
            this.mReadOwners = null;
            this.ownedModeFlags &= -2;
            updateModeFlags();
        }
    }

    private void addWriteOwner(com.android.server.uri.UriPermissionOwner owner) {
        if (this.mWriteOwners == null) {
            this.mWriteOwners = com.google.android.collect.Sets.newArraySet();
            this.ownedModeFlags |= 2;
            updateModeFlags();
        }
        if (this.mWriteOwners.add(owner)) {
            owner.addWritePermission(this);
        }
    }

    void removeWriteOwner(com.android.server.uri.UriPermissionOwner owner) {
        if (this.mWriteOwners == null || !this.mWriteOwners.remove(owner)) {
            android.util.Slog.wtf(TAG, "Unknown write owner " + owner + " in " + this);
        } else if (this.mWriteOwners.size() == 0) {
            this.mWriteOwners = null;
            this.ownedModeFlags &= -3;
            updateModeFlags();
        }
    }

    public java.lang.String toString() {
        if (this.stringName != null) {
            return this.stringName;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("UriPermission{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.uri);
        sb.append('}');
        java.lang.String string = sb.toString();
        this.stringName = string;
        return string;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("targetUserId=" + this.targetUserId);
        pw.print(" sourcePkg=" + this.sourcePkg);
        pw.println(" targetPkg=" + this.targetPkg);
        pw.print(prefix);
        pw.print("mode=0x" + java.lang.Integer.toHexString(this.modeFlags));
        pw.print(" owned=0x" + java.lang.Integer.toHexString(this.ownedModeFlags));
        pw.print(" global=0x" + java.lang.Integer.toHexString(this.globalModeFlags));
        pw.print(" persistable=0x" + java.lang.Integer.toHexString(this.persistableModeFlags));
        pw.print(" persisted=0x" + java.lang.Integer.toHexString(this.persistedModeFlags));
        if (this.persistedCreateTime != Long.MIN_VALUE) {
            pw.print(" persistedCreate=" + this.persistedCreateTime);
        }
        pw.println();
        if (this.mReadOwners != null) {
            pw.print(prefix);
            pw.println("readOwners:");
            for (com.android.server.uri.UriPermissionOwner owner : this.mReadOwners) {
                pw.print(prefix);
                pw.println("  * " + owner);
            }
        }
        if (this.mWriteOwners != null) {
            pw.print(prefix);
            pw.println("writeOwners:");
            for (com.android.server.uri.UriPermissionOwner owner2 : this.mWriteOwners) {
                pw.print(prefix);
                pw.println("  * " + owner2);
            }
        }
    }

    public static class PersistedTimeComparator implements java.util.Comparator<com.android.server.uri.UriPermission> {
        @Override // java.util.Comparator
        public int compare(com.android.server.uri.UriPermission lhs, com.android.server.uri.UriPermission rhs) {
            return java.lang.Long.compare(lhs.persistedCreateTime, rhs.persistedCreateTime);
        }
    }

    public static class Snapshot {
        final long persistedCreateTime;
        final int persistedModeFlags;
        final java.lang.String sourcePkg;
        final java.lang.String targetPkg;
        final int targetUserId;
        final com.android.server.uri.GrantUri uri;

        private Snapshot(com.android.server.uri.UriPermission perm) {
            this.targetUserId = perm.targetUserId;
            this.sourcePkg = perm.sourcePkg;
            this.targetPkg = perm.targetPkg;
            this.uri = perm.uri;
            this.persistedModeFlags = perm.persistedModeFlags;
            this.persistedCreateTime = perm.persistedCreateTime;
        }
    }

    public com.android.server.uri.UriPermission.Snapshot snapshot() {
        return new com.android.server.uri.UriPermission.Snapshot();
    }

    public android.content.UriPermission buildPersistedPublicApiObject() {
        return new android.content.UriPermission(this.uri.uri, this.persistedModeFlags, this.persistedCreateTime);
    }

    public android.app.GrantedUriPermission buildGrantedUriPermission() {
        return new android.app.GrantedUriPermission(this.uri.uri, this.targetPkg);
    }
}
