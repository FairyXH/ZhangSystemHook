package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class SliceManagerService extends android.app.slice.ISliceManager.Stub {
    private static final java.lang.String TAG = "SliceManagerService";
    private final android.app.AppOpsManager mAppOps;
    private final android.app.usage.UsageStatsManagerInternal mAppUsageStats;
    private final com.android.internal.app.AssistUtils mAssistUtils;
    private final android.util.SparseArray<com.android.server.slice.SliceManagerService.PackageMatchingCache> mAssistantLookup;
    private java.lang.String mCachedDefaultHome;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final android.util.SparseArray<com.android.server.slice.SliceManagerService.PackageMatchingCache> mHomeLookup;
    private final java.lang.Object mLock;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final com.android.server.slice.SlicePermissionManager mPermissions;
    private final android.util.ArrayMap<android.net.Uri, com.android.server.slice.PinnedSliceState> mPinnedSlicesByUri;
    private final android.content.BroadcastReceiver mReceiver;
    private com.android.server.slice.SliceManagerService.RoleObserver mRoleObserver;

    public SliceManagerService(android.content.Context context) {
        this(context, createHandler().getLooper());
    }

    SliceManagerService(android.content.Context context, android.os.Looper looper) {
        this.mLock = new java.lang.Object();
        this.mPinnedSlicesByUri = new android.util.ArrayMap<>();
        this.mAssistantLookup = new android.util.SparseArray<>();
        this.mHomeLookup = new android.util.SparseArray<>();
        this.mCachedDefaultHome = null;
        this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.slice.SliceManagerService.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:22:0x0069  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r7, android.content.Intent r8) {
                /*
                    r6 = this;
                    java.lang.String r0 = "android.intent.extra.user_handle"
                    r1 = -10000(0xffffffffffffd8f0, float:NaN)
                    int r0 = r8.getIntExtra(r0, r1)
                    java.lang.String r2 = "SliceManagerService"
                    if (r0 != r1) goto L23
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    java.lang.String r3 = "Intent broadcast does not contain user handle: "
                    java.lang.StringBuilder r1 = r1.append(r3)
                    java.lang.StringBuilder r1 = r1.append(r8)
                    java.lang.String r1 = r1.toString()
                    android.util.Slog.w(r2, r1)
                    return
                L23:
                    android.net.Uri r1 = r8.getData()
                    if (r1 == 0) goto L2e
                    java.lang.String r3 = r1.getSchemeSpecificPart()
                    goto L2f
                L2e:
                    r3 = 0
                L2f:
                    if (r3 != 0) goto L48
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder
                    r4.<init>()
                    java.lang.String r5 = "Intent broadcast does not contain package name: "
                    java.lang.StringBuilder r4 = r4.append(r5)
                    java.lang.StringBuilder r4 = r4.append(r8)
                    java.lang.String r4 = r4.toString()
                    android.util.Slog.w(r2, r4)
                    return
                L48:
                    java.lang.String r2 = r8.getAction()
                    int r4 = r2.hashCode()
                    r5 = 0
                    switch(r4) {
                        case 267468725: goto L5f;
                        case 525384130: goto L55;
                        default: goto L54;
                    }
                L54:
                    goto L69
                L55:
                    java.lang.String r4 = "android.intent.action.PACKAGE_REMOVED"
                    boolean r2 = r2.equals(r4)
                    if (r2 == 0) goto L54
                    r2 = r5
                    goto L6a
                L5f:
                    java.lang.String r4 = "android.intent.action.PACKAGE_DATA_CLEARED"
                    boolean r2 = r2.equals(r4)
                    if (r2 == 0) goto L54
                    r2 = 1
                    goto L6a
                L69:
                    r2 = -1
                L6a:
                    switch(r2) {
                        case 0: goto L78;
                        case 1: goto L6e;
                        default: goto L6d;
                    }
                L6d:
                    goto L8a
                L6e:
                    com.android.server.slice.SliceManagerService r2 = com.android.server.slice.SliceManagerService.this
                    com.android.server.slice.SlicePermissionManager r2 = com.android.server.slice.SliceManagerService.m8923$$Nest$fgetmPermissions(r2)
                    r2.removePkg(r3, r0)
                    goto L8a
                L78:
                    java.lang.String r2 = "android.intent.extra.REPLACING"
                    boolean r2 = r8.getBooleanExtra(r2, r5)
                    if (r2 != 0) goto L8a
                    com.android.server.slice.SliceManagerService r4 = com.android.server.slice.SliceManagerService.this
                    com.android.server.slice.SlicePermissionManager r4 = com.android.server.slice.SliceManagerService.m8923$$Nest$fgetmPermissions(r4)
                    r4.removePkg(r3, r0)
                L8a:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.slice.SliceManagerService.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        };
        this.mContext = context;
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) java.util.Objects.requireNonNull((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class));
        this.mAppOps = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mAssistUtils = new com.android.internal.app.AssistUtils(context);
        this.mHandler = new android.os.Handler(looper);
        this.mAppUsageStats = (android.app.usage.UsageStatsManagerInternal) com.android.server.LocalServices.getService(android.app.usage.UsageStatsManagerInternal.class);
        this.mPermissions = new com.android.server.slice.SlicePermissionManager(this.mContext, looper);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mRoleObserver = new com.android.server.slice.SliceManagerService.RoleObserver();
        this.mContext.registerReceiverAsUser(this.mReceiver, android.os.UserHandle.ALL, filter, null, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void systemReady() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnlockUser(int userId) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopUser(final int userId) {
        synchronized (this.mLock) {
            this.mPinnedSlicesByUri.values().removeIf(new java.util.function.Predicate() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.slice.SliceManagerService.lambda$onStopUser$0(userId, (com.android.server.slice.PinnedSliceState) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$onStopUser$0(int userId, com.android.server.slice.PinnedSliceState s) {
        return android.content.ContentProvider.getUserIdFromUri(s.getUri()) == userId;
    }

    public android.net.Uri[] getPinnedSlices(java.lang.String pkg) {
        verifyCaller(pkg);
        int callingUser = android.os.Binder.getCallingUserHandle().getIdentifier();
        java.util.ArrayList<android.net.Uri> ret = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (com.android.server.slice.PinnedSliceState state : this.mPinnedSlicesByUri.values()) {
                if (java.util.Objects.equals(pkg, state.getPkg())) {
                    android.net.Uri uri = state.getUri();
                    int userId = android.content.ContentProvider.getUserIdFromUri(uri, callingUser);
                    if (userId == callingUser) {
                        ret.add(android.content.ContentProvider.getUriWithoutUserId(uri));
                    }
                }
            }
        }
        return (android.net.Uri[]) ret.toArray(new android.net.Uri[ret.size()]);
    }

    public void pinSlice(final java.lang.String pkg, android.net.Uri uri, android.app.slice.SliceSpec[] specs, android.os.IBinder token) throws android.os.RemoteException {
        verifyCaller(pkg);
        enforceAccess(pkg, uri);
        final int user = android.os.Binder.getCallingUserHandle().getIdentifier();
        android.net.Uri uri2 = android.content.ContentProvider.maybeAddUserId(uri, user);
        final java.lang.String slicePkg = getProviderPkg(uri2, user);
        getOrCreatePinnedSlice(uri2, slicePkg).pin(pkg, specs, token);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$pinSlice$1(slicePkg, pkg, user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinSlice$1(java.lang.String slicePkg, java.lang.String pkg, int user) {
        if (slicePkg != null && !java.util.Objects.equals(pkg, slicePkg)) {
            this.mAppUsageStats.reportEvent(slicePkg, user, (isAssistant(pkg, user) || isDefaultHomeApp(pkg, user)) ? 13 : 14);
        }
    }

    public void unpinSlice(java.lang.String pkg, android.net.Uri uri, android.os.IBinder token) throws android.os.RemoteException {
        verifyCaller(pkg);
        enforceAccess(pkg, uri);
        android.net.Uri uri2 = android.content.ContentProvider.maybeAddUserId(uri, android.os.Binder.getCallingUserHandle().getIdentifier());
        try {
            com.android.server.slice.PinnedSliceState slice = getPinnedSlice(uri2);
            if (slice != null && slice.unpin(pkg, token)) {
                removePinnedSlice(uri2);
            }
        } catch (java.lang.IllegalStateException exception) {
            android.util.Slog.w(TAG, exception.getMessage());
        }
    }

    public boolean hasSliceAccess(java.lang.String pkg) throws android.os.RemoteException {
        verifyCaller(pkg);
        return hasFullSliceAccess(pkg, android.os.Binder.getCallingUserHandle().getIdentifier());
    }

    public android.app.slice.SliceSpec[] getPinnedSpecs(android.net.Uri uri, java.lang.String pkg) throws android.os.RemoteException {
        verifyCaller(pkg);
        enforceAccess(pkg, uri);
        return getPinnedSlice(android.content.ContentProvider.maybeAddUserId(uri, android.os.Binder.getCallingUserHandle().getIdentifier())).getSpecs();
    }

    public void grantSlicePermission(java.lang.String pkg, java.lang.String toPkg, android.net.Uri uri) throws android.os.RemoteException {
        verifyCaller(pkg);
        int user = android.os.Binder.getCallingUserHandle().getIdentifier();
        enforceOwner(pkg, uri, user);
        this.mPermissions.grantSliceAccess(toPkg, user, pkg, user, uri);
    }

    public void revokeSlicePermission(java.lang.String pkg, java.lang.String toPkg, android.net.Uri uri) throws android.os.RemoteException {
        verifyCaller(pkg);
        int user = android.os.Binder.getCallingUserHandle().getIdentifier();
        enforceOwner(pkg, uri, user);
        this.mPermissions.revokeSliceAccess(toPkg, user, pkg, user, uri);
    }

    public int checkSlicePermission(android.net.Uri uri, java.lang.String callingPkg, int pid, int uid, java.lang.String[] autoGrantPermissions) {
        return checkSlicePermissionInternal(uri, callingPkg, null, pid, uid, autoGrantPermissions);
    }

    private int checkSlicePermissionInternal(android.net.Uri uri, java.lang.String callingPkg, java.lang.String pkg, int pid, int uid, java.lang.String[] autoGrantPermissions) {
        int userId = android.os.UserHandle.getUserId(uid);
        if (pkg == null) {
            java.lang.String[] packagesForUid = this.mContext.getPackageManager().getPackagesForUid(uid);
            int length = packagesForUid.length;
            int i = 0;
            while (i < length) {
                java.lang.String p = packagesForUid[i];
                int i2 = i;
                int i3 = length;
                java.lang.String[] strArr = packagesForUid;
                if (checkSlicePermissionInternal(uri, callingPkg, p, pid, uid, autoGrantPermissions) == 0) {
                    return 0;
                }
                i = i2 + 1;
                length = i3;
                packagesForUid = strArr;
            }
            return -1;
        }
        if (hasFullSliceAccess(pkg, userId) || this.mPermissions.hasPermission(pkg, userId, uri)) {
            return 0;
        }
        if (autoGrantPermissions != null && callingPkg != null) {
            enforceOwner(callingPkg, uri, userId);
            verifyCaller(callingPkg);
            for (java.lang.String perm : autoGrantPermissions) {
                if (this.mContext.checkPermission(perm, pid, uid) == 0) {
                    int providerUser = android.content.ContentProvider.getUserIdFromUri(uri, userId);
                    java.lang.String providerPkg = getProviderPkg(uri, providerUser);
                    this.mPermissions.grantSliceAccess(pkg, userId, providerPkg, providerUser, uri);
                    return 0;
                }
            }
        }
        return -1;
    }

    public void grantPermissionFromUser(android.net.Uri uri, java.lang.String pkg, java.lang.String callingPkg, boolean allSlices) {
        verifyCaller(callingPkg);
        getContext().enforceCallingOrSelfPermission("android.permission.MANAGE_SLICE_PERMISSIONS", "Slice granting requires MANAGE_SLICE_PERMISSIONS");
        int userId = android.os.Binder.getCallingUserHandle().getIdentifier();
        if (allSlices) {
            this.mPermissions.grantFullAccess(pkg, userId);
        } else {
            android.net.Uri grantUri = uri.buildUpon().path("").build();
            int providerUser = android.content.ContentProvider.getUserIdFromUri(grantUri, userId);
            java.lang.String providerPkg = getProviderPkg(grantUri, providerUser);
            this.mPermissions.grantSliceAccess(pkg, userId, providerPkg, providerUser, grantUri);
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.getContentResolver().notifyChange(uri, null);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public byte[] getBackupPayload(int user) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Caller must be system");
        }
        if (user != 0) {
            android.util.Slog.w(TAG, "getBackupPayload: cannot backup policy for user " + user);
            return null;
        }
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try {
            org.xmlpull.v1.XmlSerializer out = org.xmlpull.v1.XmlPullParserFactory.newInstance().newSerializer();
            out.setOutput(baos, android.util.Xml.Encoding.UTF_8.name());
            this.mPermissions.writeBackup(out);
            out.flush();
            return baos.toByteArray();
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.w(TAG, "getBackupPayload: error writing payload for user " + user, e);
            return null;
        }
    }

    public void applyRestore(byte[] payload, int user) {
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.SecurityException("Caller must be system");
        }
        if (payload == null) {
            android.util.Slog.w(TAG, "applyRestore: no payload to restore for user " + user);
            return;
        }
        if (user != 0) {
            android.util.Slog.w(TAG, "applyRestore: cannot restore policy for user " + user);
            return;
        }
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(payload);
        try {
            org.xmlpull.v1.XmlPullParser parser = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(bais, android.util.Xml.Encoding.UTF_8.name());
            this.mPermissions.readRestore(parser);
        } catch (java.io.IOException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.w(TAG, "applyRestore: error reading payload", e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.slice.SliceShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private void enforceOwner(java.lang.String pkg, android.net.Uri uri, int user) {
        if (!java.util.Objects.equals(getProviderPkg(uri, user), pkg) || pkg == null) {
            throw new java.lang.SecurityException("Caller must own " + uri);
        }
    }

    protected void removePinnedSlice(android.net.Uri uri) {
        synchronized (this.mLock) {
            this.mPinnedSlicesByUri.remove(uri).destroy();
        }
    }

    private com.android.server.slice.PinnedSliceState getPinnedSlice(android.net.Uri uri) {
        com.android.server.slice.PinnedSliceState manager;
        synchronized (this.mLock) {
            manager = this.mPinnedSlicesByUri.get(uri);
            if (manager == null) {
                throw new java.lang.IllegalStateException(java.lang.String.format("Slice %s not pinned", uri.toString()));
            }
        }
        return manager;
    }

    private com.android.server.slice.PinnedSliceState getOrCreatePinnedSlice(android.net.Uri uri, java.lang.String pkg) {
        com.android.server.slice.PinnedSliceState manager;
        synchronized (this.mLock) {
            manager = this.mPinnedSlicesByUri.get(uri);
            if (manager == null) {
                manager = createPinnedSlice(uri, pkg);
                this.mPinnedSlicesByUri.put(uri, manager);
            }
        }
        return manager;
    }

    protected com.android.server.slice.PinnedSliceState createPinnedSlice(android.net.Uri uri, java.lang.String pkg) {
        return new com.android.server.slice.PinnedSliceState(this, uri, pkg);
    }

    public java.lang.Object getLock() {
        return this.mLock;
    }

    public android.content.Context getContext() {
        return this.mContext;
    }

    public android.os.Handler getHandler() {
        return this.mHandler;
    }

    protected int checkAccess(java.lang.String pkg, android.net.Uri uri, int uid, int pid) {
        return checkSlicePermissionInternal(uri, null, pkg, pid, uid, null);
    }

    private java.lang.String getProviderPkg(android.net.Uri uri, int user) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String providerName = android.content.ContentProvider.getUriWithoutUserId(uri).getAuthority();
            android.content.pm.ProviderInfo provider = this.mContext.getPackageManager().resolveContentProviderAsUser(providerName, 0, android.content.ContentProvider.getUserIdFromUri(uri, user));
            return provider == null ? null : provider.packageName;
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void enforceCrossUser(java.lang.String pkg, android.net.Uri uri) {
        int user = android.os.Binder.getCallingUserHandle().getIdentifier();
        if (android.content.ContentProvider.getUserIdFromUri(uri, user) != user) {
            getContext().enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "Slice interaction across users requires INTERACT_ACROSS_USERS_FULL");
        }
    }

    private void enforceAccess(java.lang.String pkg, android.net.Uri uri) throws android.os.RemoteException {
        if (checkAccess(pkg, uri, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid()) != 0) {
            int userId = android.content.ContentProvider.getUserIdFromUri(uri, android.os.Binder.getCallingUserHandle().getIdentifier());
            if (!java.util.Objects.equals(pkg, getProviderPkg(uri, userId))) {
                throw new java.lang.SecurityException("Access to slice " + uri + " is required");
            }
        }
        enforceCrossUser(pkg, uri);
    }

    private void verifyCaller(java.lang.String pkg) {
        this.mAppOps.checkPackage(android.os.Binder.getCallingUid(), pkg);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean hasFullSliceAccess(java.lang.String r4, int r5) {
        /*
            r3 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            boolean r2 = r3.isDefaultHomeApp(r4, r5)     // Catch: java.lang.Throwable -> L1f
            if (r2 != 0) goto L19
            boolean r2 = r3.isAssistant(r4, r5)     // Catch: java.lang.Throwable -> L1f
            if (r2 != 0) goto L19
            boolean r2 = r3.isGrantedFullAccess(r4, r5)     // Catch: java.lang.Throwable -> L1f
            if (r2 == 0) goto L17
            goto L19
        L17:
            r2 = 0
            goto L1a
        L19:
            r2 = 1
        L1a:
            android.os.Binder.restoreCallingIdentity(r0)
            return r2
        L1f:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.slice.SliceManagerService.hasFullSliceAccess(java.lang.String, int):boolean");
    }

    private boolean isAssistant(java.lang.String pkg, int userId) {
        return getAssistantMatcher(userId).matches(pkg);
    }

    private boolean isDefaultHomeApp(java.lang.String pkg, int userId) {
        return getHomeMatcher(userId).matches(pkg);
    }

    private com.android.server.slice.SliceManagerService.PackageMatchingCache getAssistantMatcher(final int userId) {
        com.android.server.slice.SliceManagerService.PackageMatchingCache matcher = this.mAssistantLookup.get(userId);
        if (matcher == null) {
            com.android.server.slice.SliceManagerService.PackageMatchingCache matcher2 = new com.android.server.slice.SliceManagerService.PackageMatchingCache(new java.util.function.Supplier() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getAssistantMatcher$2(userId);
                }
            });
            this.mAssistantLookup.put(userId, matcher2);
            return matcher2;
        }
        return matcher;
    }

    private com.android.server.slice.SliceManagerService.PackageMatchingCache getHomeMatcher(final int userId) {
        com.android.server.slice.SliceManagerService.PackageMatchingCache matcher = this.mHomeLookup.get(userId);
        if (matcher == null) {
            com.android.server.slice.SliceManagerService.PackageMatchingCache matcher2 = new com.android.server.slice.SliceManagerService.PackageMatchingCache(new java.util.function.Supplier() { // from class: com.android.server.slice.SliceManagerService$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getHomeMatcher$3(userId);
                }
            });
            this.mHomeLookup.put(userId, matcher2);
            return matcher2;
        }
        return matcher;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: getAssistant, reason: merged with bridge method [inline-methods] */
    public java.lang.String lambda$getAssistantMatcher$2(int userId) {
        android.content.ComponentName cn = this.mAssistUtils.getAssistComponentForUser(userId);
        if (cn == null) {
            return null;
        }
        return cn.getPackageName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: getDefaultHome, reason: merged with bridge method [inline-methods] */
    public java.lang.String lambda$getHomeMatcher$3(int userId) {
        if (this.mCachedDefaultHome != null) {
            return this.mCachedDefaultHome;
        }
        long token = android.os.Binder.clearCallingIdentity();
        try {
            java.util.List<android.content.pm.ResolveInfo> allHomeCandidates = new java.util.ArrayList<>();
            android.content.ComponentName defaultLauncher = this.mPackageManagerInternal.getHomeActivitiesAsUser(allHomeCandidates, userId);
            android.content.ComponentName detected = defaultLauncher;
            this.mCachedDefaultHome = detected != null ? detected.getPackageName() : null;
            if (detected == null) {
                int size = allHomeCandidates.size();
                int lastPriority = Integer.MIN_VALUE;
                for (int i = 0; i < size; i++) {
                    android.content.pm.ResolveInfo ri = allHomeCandidates.get(i);
                    if (ri.activityInfo.applicationInfo.isSystemApp() && ri.priority >= lastPriority) {
                        detected = ri.activityInfo.getComponentName();
                        lastPriority = ri.priority;
                    }
                }
            }
            java.lang.String ret = detected != null ? detected.getPackageName() : null;
            return ret;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public void invalidateCachedDefaultHome() {
        this.mCachedDefaultHome = null;
    }

    class RoleObserver implements android.app.role.OnRoleHoldersChangedListener {
        private final java.util.concurrent.Executor mExecutor;
        private android.app.role.RoleManager mRm;

        RoleObserver() {
            this.mExecutor = com.android.server.slice.SliceManagerService.this.mContext.getMainExecutor();
            register();
        }

        public void register() {
            this.mRm = (android.app.role.RoleManager) com.android.server.slice.SliceManagerService.this.mContext.getSystemService(android.app.role.RoleManager.class);
            if (this.mRm != null) {
                this.mRm.addOnRoleHoldersChangedListenerAsUser(this.mExecutor, this, android.os.UserHandle.ALL);
                com.android.server.slice.SliceManagerService.this.invalidateCachedDefaultHome();
            }
        }

        public void onRoleHoldersChanged(java.lang.String roleName, android.os.UserHandle user) {
            if ("android.app.role.HOME".equals(roleName)) {
                com.android.server.slice.SliceManagerService.this.invalidateCachedDefaultHome();
            }
        }
    }

    private boolean isGrantedFullAccess(java.lang.String pkg, int userId) {
        return this.mPermissions.hasFullAccess(pkg, userId);
    }

    private static com.android.server.ServiceThread createHandler() {
        com.android.server.ServiceThread handlerThread = new com.android.server.ServiceThread(TAG, 10, true);
        handlerThread.start();
        return handlerThread;
    }

    public java.lang.String[] getAllPackagesGranted(java.lang.String authority) {
        java.lang.String pkg = getProviderPkg(new android.net.Uri.Builder().scheme(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT).authority(authority).build(), 0);
        return pkg == null ? new java.lang.String[0] : this.mPermissions.getAllPackagesGranted(pkg);
    }

    static class PackageMatchingCache {
        private java.lang.String mCurrentPkg;
        private final java.util.function.Supplier<java.lang.String> mPkgSource;

        public PackageMatchingCache(java.util.function.Supplier<java.lang.String> pkgSource) {
            this.mPkgSource = pkgSource;
        }

        public boolean matches(java.lang.String pkgCandidate) {
            if (pkgCandidate == null) {
                return false;
            }
            if (java.util.Objects.equals(pkgCandidate, this.mCurrentPkg)) {
                return true;
            }
            this.mCurrentPkg = this.mPkgSource.get();
            return java.util.Objects.equals(pkgCandidate, this.mCurrentPkg);
        }
    }

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.slice.SliceManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.slice.SliceManagerService(getContext());
            publishBinderService("slice", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                this.mService.systemReady();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            this.mService.onUnlockUser(user.getUserIdentifier());
        }

        @Override // com.android.server.SystemService
        public void onUserStopping(com.android.server.SystemService.TargetUser user) {
            this.mService.onStopUser(user.getUserIdentifier());
        }
    }

    private class SliceGrant {
        private final java.lang.String mPkg;
        private final android.net.Uri mUri;
        private final int mUserId;

        public SliceGrant(android.net.Uri uri, java.lang.String pkg, int userId) {
            this.mUri = uri;
            this.mPkg = pkg;
            this.mUserId = userId;
        }

        public int hashCode() {
            return this.mUri.hashCode() + this.mPkg.hashCode();
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.slice.SliceManagerService.SliceGrant)) {
                return false;
            }
            com.android.server.slice.SliceManagerService.SliceGrant other = (com.android.server.slice.SliceManagerService.SliceGrant) obj;
            return java.util.Objects.equals(other.mUri, this.mUri) && java.util.Objects.equals(other.mPkg, this.mPkg) && other.mUserId == this.mUserId;
        }
    }
}
