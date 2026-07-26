package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class SlicePermissionManager implements com.android.server.slice.DirtyTracker {
    static final int DB_VERSION = 2;
    private static final long PERMISSION_CACHE_PERIOD = 300000;
    private static final java.lang.String SLICE_DIR = "slice";
    private static final java.lang.String TAG = "SlicePermissionManager";
    private static final java.lang.String TAG_LIST = "slice-access-list";
    private static final long WRITE_GRACE_PERIOD = 500;
    private final java.lang.String ATT_VERSION;
    private final android.util.ArrayMap<com.android.server.slice.SlicePermissionManager.PkgUser, com.android.server.slice.SliceClientPermissions> mCachedClients;
    private final android.util.ArrayMap<com.android.server.slice.SlicePermissionManager.PkgUser, com.android.server.slice.SliceProviderPermissions> mCachedProviders;
    private final android.content.Context mContext;
    private final android.util.ArraySet<com.android.server.slice.DirtyTracker.Persistable> mDirty;
    private final android.os.Handler mHandler;
    private final java.io.File mSliceDir;

    SlicePermissionManager(android.content.Context context, android.os.Looper looper, java.io.File sliceDir) {
        this.ATT_VERSION = "version";
        this.mCachedProviders = new android.util.ArrayMap<>();
        this.mCachedClients = new android.util.ArrayMap<>();
        this.mDirty = new android.util.ArraySet<>();
        this.mContext = context;
        this.mHandler = new com.android.server.slice.SlicePermissionManager.H(looper);
        this.mSliceDir = sliceDir;
    }

    public SlicePermissionManager(android.content.Context context, android.os.Looper looper) {
        this(context, looper, new java.io.File(android.os.Environment.getDataDirectory(), "system/slice"));
    }

    public void grantFullAccess(java.lang.String pkg, int userId) {
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, userId);
        com.android.server.slice.SliceClientPermissions client = getClient(pkgUser);
        client.setHasFullAccess(true);
    }

    public void grantSliceAccess(java.lang.String pkg, int userId, java.lang.String providerPkg, int providerUser, android.net.Uri uri) {
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, userId);
        com.android.server.slice.SlicePermissionManager.PkgUser providerPkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(providerPkg, providerUser);
        com.android.server.slice.SliceClientPermissions client = getClient(pkgUser);
        client.grantUri(uri, providerPkgUser);
        com.android.server.slice.SliceProviderPermissions provider = getProvider(providerPkgUser);
        provider.getOrCreateAuthority(android.content.ContentProvider.getUriWithoutUserId(uri).getAuthority()).addPkg(pkgUser);
    }

    public void revokeSliceAccess(java.lang.String pkg, int userId, java.lang.String providerPkg, int providerUser, android.net.Uri uri) {
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, userId);
        com.android.server.slice.SlicePermissionManager.PkgUser providerPkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(providerPkg, providerUser);
        com.android.server.slice.SliceClientPermissions client = getClient(pkgUser);
        client.revokeUri(uri, providerPkgUser);
    }

    public void removePkg(java.lang.String pkg, int userId) {
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, userId);
        com.android.server.slice.SliceProviderPermissions provider = getProvider(pkgUser);
        for (com.android.server.slice.SliceProviderPermissions.SliceAuthority authority : provider.getAuthorities()) {
            for (com.android.server.slice.SlicePermissionManager.PkgUser p : authority.getPkgs()) {
                getClient(p).removeAuthority(authority.getAuthority(), userId);
            }
        }
        com.android.server.slice.SliceClientPermissions client = getClient(pkgUser);
        client.clear();
        this.mHandler.obtainMessage(3, pkgUser).sendToTarget();
    }

    public java.lang.String[] getAllPackagesGranted(java.lang.String pkg) {
        android.util.ArraySet<java.lang.String> ret = new android.util.ArraySet<>();
        for (com.android.server.slice.SliceProviderPermissions.SliceAuthority authority : getProvider(new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, 0)).getAuthorities()) {
            for (com.android.server.slice.SlicePermissionManager.PkgUser pkgUser : authority.getPkgs()) {
                ret.add(pkgUser.mPkg);
            }
        }
        return (java.lang.String[]) ret.toArray(new java.lang.String[ret.size()]);
    }

    public boolean hasFullAccess(java.lang.String pkg, int userId) {
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, userId);
        return getClient(pkgUser).hasFullAccess();
    }

    public boolean hasPermission(java.lang.String pkg, int userId, android.net.Uri uri) {
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(pkg, userId);
        com.android.server.slice.SliceClientPermissions client = getClient(pkgUser);
        int providerUserId = android.content.ContentProvider.getUserIdFromUri(uri, userId);
        return client.hasFullAccess() || client.hasPermission(android.content.ContentProvider.getUriWithoutUserId(uri), providerUserId);
    }

    @Override // com.android.server.slice.DirtyTracker
    public void onPersistableDirty(com.android.server.slice.DirtyTracker.Persistable obj) {
        this.mHandler.removeMessages(2);
        this.mHandler.obtainMessage(1, obj).sendToTarget();
        this.mHandler.sendEmptyMessageDelayed(2, 500L);
    }

    public void writeBackup(org.xmlpull.v1.XmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        synchronized (this) {
            out.startTag(null, TAG_LIST);
            out.attribute(null, "version", java.lang.String.valueOf(2));
            com.android.server.slice.DirtyTracker tracker = new com.android.server.slice.DirtyTracker() { // from class: com.android.server.slice.SlicePermissionManager$$ExternalSyntheticLambda0
                @Override // com.android.server.slice.DirtyTracker
                public final void onPersistableDirty(com.android.server.slice.DirtyTracker.Persistable persistable) {
                    com.android.server.slice.SlicePermissionManager.lambda$writeBackup$0(persistable);
                }
            };
            if (this.mHandler.hasMessages(2)) {
                this.mHandler.removeMessages(2);
                handlePersist();
            }
            for (java.lang.String file : new java.io.File(this.mSliceDir.getAbsolutePath()).list()) {
                com.android.server.slice.SlicePermissionManager.ParserHolder parser = getParser(file);
                com.android.server.slice.DirtyTracker.Persistable p = null;
                while (true) {
                    try {
                        if (parser.parser.getEventType() == 1) {
                            break;
                        }
                        if (parser.parser.getEventType() == 2) {
                            if ("client".equals(parser.parser.getName())) {
                                p = com.android.server.slice.SliceClientPermissions.createFrom(parser.parser, tracker);
                            } else {
                                p = com.android.server.slice.SliceProviderPermissions.createFrom(parser.parser, tracker);
                            }
                        } else {
                            parser.parser.next();
                        }
                    } finally {
                    }
                }
                if (p != null) {
                    p.writeTo(out);
                } else {
                    android.util.Slog.w(TAG, "Invalid or empty slice permissions file: " + file);
                }
                if (parser != null) {
                    parser.close();
                }
            }
            out.endTag(null, TAG_LIST);
        }
    }

    static /* synthetic */ void lambda$writeBackup$0(com.android.server.slice.DirtyTracker.Persistable obj) {
    }

    public void readRestore(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        synchronized (this) {
            while (true) {
                if ((parser.getEventType() == 2 && TAG_LIST.equals(parser.getName())) || parser.getEventType() == 1) {
                    break;
                } else {
                    parser.next();
                }
            }
            int xmlVersion = com.android.internal.util.XmlUtils.readIntAttribute(parser, "version", 0);
            if (xmlVersion < 2) {
                return;
            }
            while (parser.getEventType() != 1) {
                if (parser.getEventType() == 2) {
                    if ("client".equals(parser.getName())) {
                        com.android.server.slice.SliceClientPermissions client = com.android.server.slice.SliceClientPermissions.createFrom(parser, this);
                        synchronized (this.mCachedClients) {
                            this.mCachedClients.put(client.getPkg(), client);
                        }
                        onPersistableDirty(client);
                        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4, client.getPkg()), 300000L);
                    } else if ("provider".equals(parser.getName())) {
                        com.android.server.slice.SliceProviderPermissions provider = com.android.server.slice.SliceProviderPermissions.createFrom(parser, this);
                        synchronized (this.mCachedProviders) {
                            this.mCachedProviders.put(provider.getPkg(), provider);
                        }
                        onPersistableDirty(provider);
                        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(5, provider.getPkg()), 300000L);
                    } else {
                        parser.next();
                    }
                } else {
                    parser.next();
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0069 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.slice.SliceClientPermissions getClient(com.android.server.slice.SlicePermissionManager.PkgUser r7) {
        /*
            r6 = this;
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceClientPermissions> r0 = r6.mCachedClients
            monitor-enter(r0)
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceClientPermissions> r1 = r6.mCachedClients     // Catch: java.lang.Throwable -> L75
            java.lang.Object r1 = r1.get(r7)     // Catch: java.lang.Throwable -> L75
            com.android.server.slice.SliceClientPermissions r1 = (com.android.server.slice.SliceClientPermissions) r1     // Catch: java.lang.Throwable -> L75
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L75
            if (r1 != 0) goto L74
            java.lang.String r0 = com.android.server.slice.SliceClientPermissions.getFileName(r7)     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
            com.android.server.slice.SlicePermissionManager$ParserHolder r0 = r6.getParser(r0)     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
            org.xmlpull.v1.XmlPullParser r2 = com.android.server.slice.SlicePermissionManager.ParserHolder.m8932$$Nest$fgetparser(r0)     // Catch: java.lang.Throwable -> L41
            com.android.server.slice.SliceClientPermissions r2 = com.android.server.slice.SliceClientPermissions.createFrom(r2, r6)     // Catch: java.lang.Throwable -> L41
            r1 = r2
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceClientPermissions> r2 = r6.mCachedClients     // Catch: java.lang.Throwable -> L41
            monitor-enter(r2)     // Catch: java.lang.Throwable -> L41
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceClientPermissions> r3 = r6.mCachedClients     // Catch: java.lang.Throwable -> L3e
            r3.put(r7, r1)     // Catch: java.lang.Throwable -> L3e
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L3e
            android.os.Handler r2 = r6.mHandler     // Catch: java.lang.Throwable -> L41
            android.os.Handler r3 = r6.mHandler     // Catch: java.lang.Throwable -> L41
            r4 = 4
            android.os.Message r3 = r3.obtainMessage(r4, r7)     // Catch: java.lang.Throwable -> L41
            r4 = 300000(0x493e0, double:1.482197E-318)
            r2.sendMessageDelayed(r3, r4)     // Catch: java.lang.Throwable -> L41
            if (r0 == 0) goto L3d
            r0.close()     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
        L3d:
            return r1
        L3e:
            r3 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L3e
            throw r3     // Catch: java.lang.Throwable -> L41
        L41:
            r2 = move-exception
            if (r0 == 0) goto L4c
            r0.close()     // Catch: java.lang.Throwable -> L48
            goto L4c
        L48:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
        L4c:
            throw r2     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
        L4d:
            r0 = move-exception
            java.lang.String r2 = "SlicePermissionManager"
            java.lang.String r3 = "Can't read client"
            android.util.Log.e(r2, r3, r0)
            goto L61
        L56:
            r0 = move-exception
            java.lang.String r2 = "SlicePermissionManager"
            java.lang.String r3 = "Can't read client"
            android.util.Log.e(r2, r3, r0)
            goto L60
        L5f:
            r0 = move-exception
        L60:
        L61:
            com.android.server.slice.SliceClientPermissions r0 = new com.android.server.slice.SliceClientPermissions
            r0.<init>(r7, r6)
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceClientPermissions> r2 = r6.mCachedClients
            monitor-enter(r2)
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceClientPermissions> r1 = r6.mCachedClients     // Catch: java.lang.Throwable -> L71
            r1.put(r7, r0)     // Catch: java.lang.Throwable -> L71
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L71
            r1 = r0
            goto L74
        L71:
            r1 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L71
            throw r1
        L74:
            return r1
        L75:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L75
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.slice.SlicePermissionManager.getClient(com.android.server.slice.SlicePermissionManager$PkgUser):com.android.server.slice.SliceClientPermissions");
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0069 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private com.android.server.slice.SliceProviderPermissions getProvider(com.android.server.slice.SlicePermissionManager.PkgUser r7) {
        /*
            r6 = this;
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceProviderPermissions> r0 = r6.mCachedProviders
            monitor-enter(r0)
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceProviderPermissions> r1 = r6.mCachedProviders     // Catch: java.lang.Throwable -> L75
            java.lang.Object r1 = r1.get(r7)     // Catch: java.lang.Throwable -> L75
            com.android.server.slice.SliceProviderPermissions r1 = (com.android.server.slice.SliceProviderPermissions) r1     // Catch: java.lang.Throwable -> L75
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L75
            if (r1 != 0) goto L74
            java.lang.String r0 = com.android.server.slice.SliceProviderPermissions.getFileName(r7)     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
            com.android.server.slice.SlicePermissionManager$ParserHolder r0 = r6.getParser(r0)     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
            org.xmlpull.v1.XmlPullParser r2 = com.android.server.slice.SlicePermissionManager.ParserHolder.m8932$$Nest$fgetparser(r0)     // Catch: java.lang.Throwable -> L41
            com.android.server.slice.SliceProviderPermissions r2 = com.android.server.slice.SliceProviderPermissions.createFrom(r2, r6)     // Catch: java.lang.Throwable -> L41
            r1 = r2
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceProviderPermissions> r2 = r6.mCachedProviders     // Catch: java.lang.Throwable -> L41
            monitor-enter(r2)     // Catch: java.lang.Throwable -> L41
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceProviderPermissions> r3 = r6.mCachedProviders     // Catch: java.lang.Throwable -> L3e
            r3.put(r7, r1)     // Catch: java.lang.Throwable -> L3e
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L3e
            android.os.Handler r2 = r6.mHandler     // Catch: java.lang.Throwable -> L41
            android.os.Handler r3 = r6.mHandler     // Catch: java.lang.Throwable -> L41
            r4 = 5
            android.os.Message r3 = r3.obtainMessage(r4, r7)     // Catch: java.lang.Throwable -> L41
            r4 = 300000(0x493e0, double:1.482197E-318)
            r2.sendMessageDelayed(r3, r4)     // Catch: java.lang.Throwable -> L41
            if (r0 == 0) goto L3d
            r0.close()     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
        L3d:
            return r1
        L3e:
            r3 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L3e
            throw r3     // Catch: java.lang.Throwable -> L41
        L41:
            r2 = move-exception
            if (r0 == 0) goto L4c
            r0.close()     // Catch: java.lang.Throwable -> L48
            goto L4c
        L48:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
        L4c:
            throw r2     // Catch: org.xmlpull.v1.XmlPullParserException -> L4d java.io.IOException -> L56 java.io.FileNotFoundException -> L5f
        L4d:
            r0 = move-exception
            java.lang.String r2 = "SlicePermissionManager"
            java.lang.String r3 = "Can't read provider"
            android.util.Log.e(r2, r3, r0)
            goto L61
        L56:
            r0 = move-exception
            java.lang.String r2 = "SlicePermissionManager"
            java.lang.String r3 = "Can't read provider"
            android.util.Log.e(r2, r3, r0)
            goto L60
        L5f:
            r0 = move-exception
        L60:
        L61:
            com.android.server.slice.SliceProviderPermissions r0 = new com.android.server.slice.SliceProviderPermissions
            r0.<init>(r7, r6)
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceProviderPermissions> r2 = r6.mCachedProviders
            monitor-enter(r2)
            android.util.ArrayMap<com.android.server.slice.SlicePermissionManager$PkgUser, com.android.server.slice.SliceProviderPermissions> r1 = r6.mCachedProviders     // Catch: java.lang.Throwable -> L71
            r1.put(r7, r0)     // Catch: java.lang.Throwable -> L71
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L71
            r1 = r0
            goto L74
        L71:
            r1 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L71
            throw r1
        L74:
            return r1
        L75:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L75
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.slice.SlicePermissionManager.getProvider(com.android.server.slice.SlicePermissionManager$PkgUser):com.android.server.slice.SliceProviderPermissions");
    }

    private com.android.server.slice.SlicePermissionManager.ParserHolder getParser(java.lang.String fileName) throws org.xmlpull.v1.XmlPullParserException, java.io.FileNotFoundException {
        android.util.AtomicFile file = getFile(fileName);
        com.android.server.slice.SlicePermissionManager.ParserHolder holder = new com.android.server.slice.SlicePermissionManager.ParserHolder();
        holder.input = file.openRead();
        holder.parser = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
        holder.parser.setInput(holder.input, android.util.Xml.Encoding.UTF_8.name());
        return holder;
    }

    private android.util.AtomicFile getFile(java.lang.String fileName) {
        if (!this.mSliceDir.exists()) {
            this.mSliceDir.mkdir();
        }
        return new android.util.AtomicFile(new java.io.File(this.mSliceDir, fileName));
    }

    void handlePersist() {
        synchronized (this) {
            for (com.android.server.slice.DirtyTracker.Persistable persistable : this.mDirty) {
                android.util.AtomicFile file = getFile(persistable.getFileName());
                try {
                    java.io.FileOutputStream stream = file.startWrite();
                    try {
                        org.xmlpull.v1.XmlSerializer out = org.xmlpull.v1.XmlPullParserFactory.newInstance().newSerializer();
                        out.setOutput(stream, android.util.Xml.Encoding.UTF_8.name());
                        persistable.writeTo(out);
                        out.flush();
                        file.finishWrite(stream);
                    } catch (java.io.IOException | java.lang.RuntimeException | org.xmlpull.v1.XmlPullParserException e) {
                        android.util.Slog.w(TAG, "Failed to save access file, restoring backup", e);
                        file.failWrite(stream);
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(TAG, "Failed to save access file", e2);
                    return;
                }
            }
            this.mDirty.clear();
        }
    }

    void addDirtyImmediate(com.android.server.slice.DirtyTracker.Persistable obj) {
        this.mDirty.add(obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemove(com.android.server.slice.SlicePermissionManager.PkgUser pkgUser) {
        getFile(com.android.server.slice.SliceClientPermissions.getFileName(pkgUser)).delete();
        getFile(com.android.server.slice.SliceProviderPermissions.getFileName(pkgUser)).delete();
        synchronized (this.mCachedClients) {
            this.mDirty.remove(this.mCachedClients.remove(pkgUser));
        }
        synchronized (this.mCachedProviders) {
            this.mDirty.remove(this.mCachedProviders.remove(pkgUser));
        }
    }

    private final class H extends android.os.Handler {
        private static final int MSG_ADD_DIRTY = 1;
        private static final int MSG_CLEAR_CLIENT = 4;
        private static final int MSG_CLEAR_PROVIDER = 5;
        private static final int MSG_PERSIST = 2;
        private static final int MSG_REMOVE = 3;

        public H(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.slice.SlicePermissionManager.this.mDirty.add((com.android.server.slice.DirtyTracker.Persistable) msg.obj);
                    return;
                case 2:
                    com.android.server.slice.SlicePermissionManager.this.handlePersist();
                    return;
                case 3:
                    com.android.server.slice.SlicePermissionManager.this.handleRemove((com.android.server.slice.SlicePermissionManager.PkgUser) msg.obj);
                    return;
                case 4:
                    synchronized (com.android.server.slice.SlicePermissionManager.this.mCachedClients) {
                        com.android.server.slice.SlicePermissionManager.this.mCachedClients.remove(msg.obj);
                        break;
                    }
                    return;
                case 5:
                    synchronized (com.android.server.slice.SlicePermissionManager.this.mCachedProviders) {
                        com.android.server.slice.SlicePermissionManager.this.mCachedProviders.remove(msg.obj);
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static class PkgUser {
        private static final java.lang.String FORMAT = "%s@%d";
        private static final java.lang.String SEPARATOR = "@";
        private final java.lang.String mPkg;
        private final int mUserId;

        public PkgUser(java.lang.String pkg, int userId) {
            this.mPkg = pkg;
            this.mUserId = userId;
        }

        public PkgUser(java.lang.String pkgUserStr) throws java.lang.IllegalArgumentException {
            try {
                java.lang.String[] vals = pkgUserStr.split(SEPARATOR, 2);
                this.mPkg = vals[0];
                this.mUserId = java.lang.Integer.parseInt(vals[1]);
            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalArgumentException(e);
            }
        }

        public java.lang.String getPkg() {
            return this.mPkg;
        }

        public int getUserId() {
            return this.mUserId;
        }

        public int hashCode() {
            return this.mPkg.hashCode() + this.mUserId;
        }

        public boolean equals(java.lang.Object obj) {
            if (!getClass().equals(obj != null ? obj.getClass() : null)) {
                return false;
            }
            com.android.server.slice.SlicePermissionManager.PkgUser other = (com.android.server.slice.SlicePermissionManager.PkgUser) obj;
            return java.util.Objects.equals(other.mPkg, this.mPkg) && other.mUserId == this.mUserId;
        }

        public java.lang.String toString() {
            return java.lang.String.format(FORMAT, this.mPkg, java.lang.Integer.valueOf(this.mUserId));
        }
    }

    private class ParserHolder implements java.lang.AutoCloseable {
        private java.io.InputStream input;
        private org.xmlpull.v1.XmlPullParser parser;

        private ParserHolder() {
        }

        @Override // java.lang.AutoCloseable
        public void close() throws java.io.IOException {
            this.input.close();
        }
    }
}
