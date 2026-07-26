package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
public class UriPermissionOwner {
    private android.os.Binder externalToken;
    private final java.lang.Object mOwner;
    private android.util.ArraySet<com.android.server.uri.UriPermission> mReadPerms;
    private final com.android.server.uri.UriGrantsManagerInternal mService;
    private android.util.ArraySet<com.android.server.uri.UriPermission> mWritePerms;

    class ExternalToken extends android.os.Binder {
        ExternalToken() {
        }

        com.android.server.uri.UriPermissionOwner getOwner() {
            return com.android.server.uri.UriPermissionOwner.this;
        }
    }

    public UriPermissionOwner(com.android.server.uri.UriGrantsManagerInternal service, java.lang.Object owner) {
        this.mService = service;
        this.mOwner = owner;
    }

    public android.os.Binder getExternalToken() {
        if (this.externalToken == null) {
            this.externalToken = new com.android.server.uri.UriPermissionOwner.ExternalToken();
        }
        return this.externalToken;
    }

    static com.android.server.uri.UriPermissionOwner fromExternalToken(android.os.IBinder token) {
        if (token instanceof com.android.server.uri.UriPermissionOwner.ExternalToken) {
            return ((com.android.server.uri.UriPermissionOwner.ExternalToken) token).getOwner();
        }
        return null;
    }

    public void removeUriPermissions() {
        removeUriPermissions(3);
    }

    void removeUriPermissions(int mode) {
        removeUriPermission(null, mode);
    }

    void removeUriPermission(com.android.server.uri.GrantUri grantUri, int mode) {
        removeUriPermission(grantUri, mode, null, -1);
    }

    void removeUriPermission(com.android.server.uri.GrantUri grantUri, int mode, java.lang.String targetPgk, int targetUserId) {
        java.util.List<com.android.server.uri.UriPermission> permissionsToRemove = new java.util.ArrayList<>();
        synchronized (this) {
            if ((mode & 1) != 0) {
                try {
                    if (this.mReadPerms != null) {
                        java.util.Iterator<com.android.server.uri.UriPermission> it = this.mReadPerms.iterator();
                        while (it.hasNext()) {
                            com.android.server.uri.UriPermission perm = it.next();
                            if (grantUri == null || grantUri.equals(perm.uri)) {
                                if (targetPgk == null || targetPgk.equals(perm.targetPkg)) {
                                    if (targetUserId == -1 || targetUserId == perm.targetUserId) {
                                        permissionsToRemove.add(perm);
                                        perm.removeReadOwner(this);
                                        it.remove();
                                    }
                                }
                            }
                        }
                        if (this.mReadPerms.isEmpty()) {
                            this.mReadPerms = null;
                        }
                    }
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            if ((mode & 2) != 0 && this.mWritePerms != null) {
                java.util.Iterator<com.android.server.uri.UriPermission> it2 = this.mWritePerms.iterator();
                while (it2.hasNext()) {
                    com.android.server.uri.UriPermission perm2 = it2.next();
                    if (grantUri == null || grantUri.equals(perm2.uri)) {
                        if (targetPgk == null || targetPgk.equals(perm2.targetPkg)) {
                            if (targetUserId == -1 || targetUserId == perm2.targetUserId) {
                                permissionsToRemove.add(perm2);
                                perm2.removeWriteOwner(this);
                                it2.remove();
                            }
                        }
                    }
                }
                if (this.mWritePerms.isEmpty()) {
                    this.mWritePerms = null;
                }
            }
        }
        int permissionsToRemoveSize = permissionsToRemove.size();
        for (int i = 0; i < permissionsToRemoveSize; i++) {
            this.mService.removeUriPermissionIfNeeded(permissionsToRemove.get(i));
        }
    }

    public void addReadPermission(com.android.server.uri.UriPermission perm) {
        synchronized (this) {
            if (this.mReadPerms == null) {
                this.mReadPerms = com.google.android.collect.Sets.newArraySet();
            }
            this.mReadPerms.add(perm);
        }
    }

    public void addWritePermission(com.android.server.uri.UriPermission perm) {
        synchronized (this) {
            if (this.mWritePerms == null) {
                this.mWritePerms = com.google.android.collect.Sets.newArraySet();
            }
            this.mWritePerms.add(perm);
        }
    }

    public void removeReadPermission(com.android.server.uri.UriPermission perm) {
        synchronized (this) {
            if (this.mReadPerms != null) {
                this.mReadPerms.remove(perm);
                if (this.mReadPerms.isEmpty()) {
                    this.mReadPerms = null;
                }
            }
        }
    }

    public void removeWritePermission(com.android.server.uri.UriPermission perm) {
        synchronized (this) {
            if (this.mWritePerms != null) {
                this.mWritePerms.remove(perm);
                if (this.mWritePerms.isEmpty()) {
                    this.mWritePerms = null;
                }
            }
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this) {
            if (this.mReadPerms != null) {
                pw.print(prefix);
                pw.print("readUriPermissions=");
                pw.println(this.mReadPerms);
            }
            if (this.mWritePerms != null) {
                pw.print(prefix);
                pw.print("writeUriPermissions=");
                pw.println(this.mWritePerms);
            }
        }
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.mOwner.toString());
        synchronized (this) {
            if (this.mReadPerms != null) {
                for (com.android.server.uri.UriPermission p : this.mReadPerms) {
                    p.uri.dumpDebug(proto, 2246267895810L);
                }
            }
            if (this.mWritePerms != null) {
                for (com.android.server.uri.UriPermission p2 : this.mWritePerms) {
                    p2.uri.dumpDebug(proto, 2246267895811L);
                }
            }
        }
        proto.end(token);
    }

    public java.lang.String toString() {
        return this.mOwner.toString();
    }
}
