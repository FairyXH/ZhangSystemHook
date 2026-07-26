package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class AppPermissionTracker extends com.android.server.am.BaseAppStateTracker<com.android.server.am.AppPermissionTracker.AppPermissionPolicy> implements android.content.pm.PackageManager.OnPermissionsChangedListener {
    static final boolean DEBUG_PERMISSION_TRACKER = false;
    static final java.lang.String TAG = "ActivityManager";
    private final android.util.SparseArray<com.android.server.am.AppPermissionTracker.MyAppOpsCallback> mAppOpsCallbacks;
    private final com.android.server.am.AppPermissionTracker.MyHandler mHandler;
    private volatile boolean mLockedBootCompleted;
    private android.util.SparseArray<android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState>> mUidGrantedPermissionsInMonitor;

    AppPermissionTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller) {
        this(context, controller, null, null);
    }

    AppPermissionTracker(android.content.Context context, com.android.server.am.AppRestrictionController controller, java.lang.reflect.Constructor<? extends com.android.server.am.BaseAppStateTracker.Injector<com.android.server.am.AppPermissionTracker.AppPermissionPolicy>> injector, java.lang.Object outerContext) {
        super(context, controller, injector, outerContext);
        this.mAppOpsCallbacks = new android.util.SparseArray<>();
        this.mUidGrantedPermissionsInMonitor = new android.util.SparseArray<>();
        this.mLockedBootCompleted = false;
        this.mHandler = new com.android.server.am.AppPermissionTracker.MyHandler(this);
        this.mInjector.setPolicy(new com.android.server.am.AppPermissionTracker.AppPermissionPolicy(this.mInjector, this));
    }

    @Override // com.android.server.am.BaseAppStateTracker
    int getType() {
        return 5;
    }

    public void onPermissionsChanged(int uid) {
        this.mHandler.obtainMessage(2, uid, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppOpsInit() {
        java.util.ArrayList<java.lang.Integer> ops = new java.util.ArrayList<>();
        android.util.Pair<java.lang.String, java.lang.Integer>[] permissions = ((com.android.server.am.AppPermissionTracker.AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        for (android.util.Pair<java.lang.String, java.lang.Integer> pair : permissions) {
            if (((java.lang.Integer) pair.second).intValue() != -1) {
                ops.add((java.lang.Integer) pair.second);
            }
        }
        int i = ops.size();
        startWatchingMode((java.lang.Integer[]) ops.toArray(new java.lang.Integer[i]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:? -> B:35:0x0104). Please report as a decompilation issue!!! */
    public void handlePermissionsInit() throws java.lang.Throwable {
        int i;
        java.lang.Object obj;
        android.content.pm.ApplicationInfo ai;
        com.android.server.am.AppPermissionTracker.UidGrantedPermissionState state;
        java.util.List<android.content.pm.ApplicationInfo> apps;
        int size;
        int userId;
        int[] allUsers = this.mInjector.getUserManagerInternal().getUserIds();
        android.content.pm.PackageManagerInternal pmi = this.mInjector.getPackageManagerInternal();
        this.mInjector.getPermissionManagerServiceInternal();
        android.util.Pair<java.lang.String, java.lang.Integer>[] permissions = ((com.android.server.am.AppPermissionTracker.AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        android.util.SparseArray<android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState>> uidPerms = this.mUidGrantedPermissionsInMonitor;
        int length = allUsers.length;
        int i2 = 0;
        while (i2 < length) {
            int userId2 = allUsers[i2];
            java.util.List<android.content.pm.ApplicationInfo> apps2 = pmi.getInstalledApplications(0L, userId2, 1000);
            if (apps2 == null) {
                i = length;
            } else {
                long now = android.os.SystemClock.elapsedRealtime();
                int size2 = apps2.size();
                int i3 = 0;
                while (i3 < size2) {
                    android.content.pm.ApplicationInfo ai2 = apps2.get(i3);
                    int length2 = permissions.length;
                    int i4 = 0;
                    while (i4 < length2) {
                        android.util.Pair<java.lang.String, java.lang.Integer> permission = permissions[i4];
                        int i5 = length;
                        int i6 = i4;
                        int i7 = length2;
                        int i8 = i3;
                        com.android.server.am.AppPermissionTracker.UidGrantedPermissionState state2 = new com.android.server.am.AppPermissionTracker.UidGrantedPermissionState(ai2.uid, (java.lang.String) permission.first, ((java.lang.Integer) permission.second).intValue());
                        if (!state2.isGranted()) {
                            ai = ai2;
                            apps = apps2;
                            size = size2;
                            userId = userId2;
                        } else {
                            java.lang.Object obj2 = this.mLock;
                            synchronized (obj2) {
                                try {
                                    android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState> grantedPermissions = uidPerms.get(ai2.uid);
                                    if (grantedPermissions != null) {
                                        obj = obj2;
                                        ai = ai2;
                                        state = state2;
                                        apps = apps2;
                                        size = size2;
                                        userId = userId2;
                                    } else {
                                        try {
                                            grantedPermissions = new android.util.ArraySet<>();
                                            uidPerms.put(ai2.uid, grantedPermissions);
                                            obj = obj2;
                                            ai = ai2;
                                            state = state2;
                                            apps = apps2;
                                            size = size2;
                                            userId = userId2;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            obj = obj2;
                                        }
                                        try {
                                            notifyListenersOnStateChange(ai2.uid, "", true, now, 16);
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            throw th;
                                        }
                                    }
                                    try {
                                        grantedPermissions.add(state);
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    obj = obj2;
                                }
                            }
                        }
                        i4 = i6 + 1;
                        length = i5;
                        length2 = i7;
                        i3 = i8;
                        apps2 = apps;
                        size2 = size;
                        ai2 = ai;
                        userId2 = userId;
                    }
                    i3++;
                }
                i = length;
            }
            i2++;
            length = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppOpsDestroy() {
        stopWatchingMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePermissionsDestroy() {
        synchronized (this.mLock) {
            android.util.SparseArray<android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState>> uidPerms = this.mUidGrantedPermissionsInMonitor;
            long now = android.os.SystemClock.elapsedRealtime();
            int size = uidPerms.size();
            for (int i = 0; i < size; i++) {
                int uid = uidPerms.keyAt(i);
                android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState> grantedPermissions = uidPerms.valueAt(i);
                if (grantedPermissions.size() > 0) {
                    notifyListenersOnStateChange(uid, "", false, now, 16);
                }
            }
            uidPerms.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOpChanged(int op, int uid, java.lang.String packageName) {
        android.util.Pair<java.lang.String, java.lang.Integer>[] permissions = ((com.android.server.am.AppPermissionTracker.AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        if (permissions != null && permissions.length > 0) {
            for (android.util.Pair<java.lang.String, java.lang.Integer> pair : permissions) {
                if (((java.lang.Integer) pair.second).intValue() == op) {
                    com.android.server.am.AppPermissionTracker.UidGrantedPermissionState state = new com.android.server.am.AppPermissionTracker.UidGrantedPermissionState(uid, (java.lang.String) pair.first, op);
                    synchronized (this.mLock) {
                        handlePermissionsChangedLocked(uid, new com.android.server.am.AppPermissionTracker.UidGrantedPermissionState[]{state});
                    }
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePermissionsChanged(int uid) {
        android.util.Pair<java.lang.String, java.lang.Integer>[] permissions = ((com.android.server.am.AppPermissionTracker.AppPermissionPolicy) this.mInjector.getPolicy()).getBgPermissionsInMonitor();
        if (permissions != null && permissions.length > 0) {
            this.mInjector.getPermissionManagerServiceInternal();
            com.android.server.am.AppPermissionTracker.UidGrantedPermissionState[] states = new com.android.server.am.AppPermissionTracker.UidGrantedPermissionState[permissions.length];
            for (int i = 0; i < permissions.length; i++) {
                android.util.Pair<java.lang.String, java.lang.Integer> pair = permissions[i];
                states[i] = new com.android.server.am.AppPermissionTracker.UidGrantedPermissionState(uid, (java.lang.String) pair.first, ((java.lang.Integer) pair.second).intValue());
            }
            synchronized (this.mLock) {
                handlePermissionsChangedLocked(uid, states);
            }
        }
    }

    private void handlePermissionsChangedLocked(int uid, com.android.server.am.AppPermissionTracker.UidGrantedPermissionState[] states) {
        android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState> grantedPermissions;
        boolean changed;
        int index = this.mUidGrantedPermissionsInMonitor.indexOfKey(uid);
        android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState> grantedPermissions2 = index >= 0 ? this.mUidGrantedPermissionsInMonitor.valueAt(index) : null;
        long now = android.os.SystemClock.elapsedRealtime();
        int i = 0;
        while (i < states.length) {
            boolean granted = states[i].isGranted();
            boolean changed2 = false;
            if (granted) {
                if (grantedPermissions2 == null) {
                    grantedPermissions2 = new android.util.ArraySet<>();
                    this.mUidGrantedPermissionsInMonitor.put(uid, grantedPermissions2);
                    changed2 = true;
                }
                grantedPermissions2.add(states[i]);
                grantedPermissions = grantedPermissions2;
                changed = changed2;
            } else if (grantedPermissions2 != null && !grantedPermissions2.isEmpty() && grantedPermissions2.remove(states[i]) && grantedPermissions2.isEmpty()) {
                this.mUidGrantedPermissionsInMonitor.removeAt(index);
                grantedPermissions = grantedPermissions2;
                changed = true;
            } else {
                grantedPermissions = grantedPermissions2;
                changed = false;
            }
            if (changed) {
                notifyListenersOnStateChange(uid, "", granted, now, 16);
            }
            i++;
            grantedPermissions2 = grantedPermissions;
        }
    }

    private class UidGrantedPermissionState {
        final int mAppOp;
        private boolean mAppOpAllowed;
        final java.lang.String mPermission;
        private boolean mPermissionGranted;
        final int mUid;

        UidGrantedPermissionState(int uid, java.lang.String permission, int appOp) {
            this.mUid = uid;
            this.mPermission = permission;
            this.mAppOp = appOp;
            updatePermissionState();
            updateAppOps();
        }

        /* JADX WARN: Type inference fix 'apply assigned field type' failed
        java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
        	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
        	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
        	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        void updatePermissionState() {
            if (android.text.TextUtils.isEmpty(this.mPermission)) {
                this.mPermissionGranted = true;
            } else {
                this.mPermissionGranted = com.android.server.am.AppPermissionTracker.this.mInjector.checkPermission(this.mPermission, -1, this.mUid) == 0;
            }
        }

        void updateAppOps() {
            if (this.mAppOp == -1) {
                this.mAppOpAllowed = true;
                return;
            }
            java.lang.String[] packages = com.android.server.am.AppPermissionTracker.this.mInjector.getPackageManager().getPackagesForUid(this.mUid);
            if (packages != null) {
                com.android.internal.app.IAppOpsService appOpsService = com.android.server.am.AppPermissionTracker.this.mInjector.getIAppOpsService();
                for (java.lang.String pkg : packages) {
                    try {
                        int mode = appOpsService.checkOperation(this.mAppOp, this.mUid, pkg);
                        if (mode == 0) {
                            this.mAppOpAllowed = true;
                            return;
                        }
                        continue;
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
            this.mAppOpAllowed = false;
        }

        boolean isGranted() {
            return this.mPermissionGranted && this.mAppOpAllowed;
        }

        public boolean equals(java.lang.Object other) {
            if (other == null || !(other instanceof com.android.server.am.AppPermissionTracker.UidGrantedPermissionState)) {
                return false;
            }
            com.android.server.am.AppPermissionTracker.UidGrantedPermissionState otherState = (com.android.server.am.AppPermissionTracker.UidGrantedPermissionState) other;
            return this.mUid == otherState.mUid && this.mAppOp == otherState.mAppOp && java.util.Objects.equals(this.mPermission, otherState.mPermission);
        }

        public int hashCode() {
            return (((java.lang.Integer.hashCode(this.mUid) * 31) + java.lang.Integer.hashCode(this.mAppOp)) * 31) + (this.mPermission == null ? 0 : this.mPermission.hashCode());
        }

        public java.lang.String toString() {
            java.lang.String s = "UidGrantedPermissionState{" + java.lang.System.identityHashCode(this) + " " + android.os.UserHandle.formatUid(this.mUid) + ": ";
            boolean emptyPermissionName = android.text.TextUtils.isEmpty(this.mPermission);
            if (!emptyPermissionName) {
                s = s + this.mPermission + "=" + this.mPermissionGranted;
            }
            if (this.mAppOp != -1) {
                if (!emptyPermissionName) {
                    s = s + ",";
                }
                s = s + android.app.AppOpsManager.opToPublicName(this.mAppOp) + "=" + this.mAppOpAllowed;
            }
            return s + "}";
        }
    }

    private void startWatchingMode(java.lang.Integer[] ops) {
        synchronized (this.mAppOpsCallbacks) {
            stopWatchingMode();
            com.android.internal.app.IAppOpsService appOpsService = this.mInjector.getIAppOpsService();
            try {
                for (java.lang.Integer num : ops) {
                    int op = num.intValue();
                    com.android.server.am.AppPermissionTracker.MyAppOpsCallback cb = new com.android.server.am.AppPermissionTracker.MyAppOpsCallback();
                    this.mAppOpsCallbacks.put(op, cb);
                    appOpsService.startWatchingModeWithFlags(op, (java.lang.String) null, 1, cb);
                }
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void stopWatchingMode() {
        synchronized (this.mAppOpsCallbacks) {
            com.android.internal.app.IAppOpsService appOpsService = this.mInjector.getIAppOpsService();
            for (int i = this.mAppOpsCallbacks.size() - 1; i >= 0; i--) {
                try {
                    appOpsService.stopWatchingMode(this.mAppOpsCallbacks.valueAt(i));
                } catch (android.os.RemoteException e) {
                }
            }
            this.mAppOpsCallbacks.clear();
        }
    }

    private class MyAppOpsCallback extends com.android.internal.app.IAppOpsCallback.Stub {
        private MyAppOpsCallback() {
        }

        public void opChanged(int op, int uid, java.lang.String packageName, java.lang.String persistentDeviceId) {
            com.android.server.am.AppPermissionTracker.this.mHandler.obtainMessage(3, op, uid, packageName).sendToTarget();
        }
    }

    private static class MyHandler extends android.os.Handler {
        static final int MSG_APPOPS_CHANGED = 3;
        static final int MSG_PERMISSIONS_CHANGED = 2;
        static final int MSG_PERMISSIONS_DESTROY = 1;
        static final int MSG_PERMISSIONS_INIT = 0;
        private com.android.server.am.AppPermissionTracker mTracker;

        MyHandler(com.android.server.am.AppPermissionTracker tracker) {
            super(tracker.mBgHandler.getLooper());
            this.mTracker = tracker;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws java.lang.Throwable {
            switch (msg.what) {
                case 0:
                    this.mTracker.handleAppOpsInit();
                    this.mTracker.handlePermissionsInit();
                    break;
                case 1:
                    this.mTracker.handlePermissionsDestroy();
                    this.mTracker.handleAppOpsDestroy();
                    break;
                case 2:
                    this.mTracker.handlePermissionsChanged(msg.arg1);
                    break;
                case 3:
                    this.mTracker.handleOpChanged(msg.arg1, msg.arg2, (java.lang.String) msg.obj);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPermissionTrackerEnabled(boolean enabled) {
        if (!this.mLockedBootCompleted) {
            return;
        }
        android.permission.PermissionManager pm = this.mInjector.getPermissionManager();
        if (enabled) {
            pm.addOnPermissionsChangeListener(this);
            this.mHandler.obtainMessage(0).sendToTarget();
        } else {
            pm.removeOnPermissionsChangeListener(this);
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    @Override // com.android.server.am.BaseAppStateTracker
    void onLockedBootCompleted() {
        this.mLockedBootCompleted = true;
        onPermissionTrackerEnabled(((com.android.server.am.AppPermissionTracker.AppPermissionPolicy) this.mInjector.getPolicy()).isEnabled());
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:? -> B:37:0x010f). Please report as a decompilation issue!!! */
    @Override // com.android.server.am.BaseAppStateTracker
    void dump(java.io.PrintWriter pw, java.lang.String prefix) throws java.lang.Throwable {
        int i;
        android.util.Pair<java.lang.String, java.lang.Integer>[] pairArr;
        android.util.Pair<java.lang.String, java.lang.Integer>[] pairArr2;
        com.android.server.am.AppPermissionTracker appPermissionTracker = this;
        pw.print(prefix);
        pw.println("APP PERMISSIONS TRACKER:");
        android.util.Pair<java.lang.String, java.lang.Integer>[] permissions = ((com.android.server.am.AppPermissionTracker.AppPermissionPolicy) appPermissionTracker.mInjector.getPolicy()).getBgPermissionsInMonitor();
        java.lang.String prefixMore = "  " + prefix;
        java.lang.String prefixMoreMore = "  " + prefixMore;
        int length = permissions.length;
        int i2 = 0;
        while (i2 < length) {
            android.util.Pair<java.lang.String, java.lang.Integer> permission = permissions[i2];
            pw.print(prefixMore);
            boolean emptyPermissionName = android.text.TextUtils.isEmpty((java.lang.CharSequence) permission.first);
            if (!emptyPermissionName) {
                pw.print((java.lang.String) permission.first);
            }
            if (((java.lang.Integer) permission.second).intValue() != -1) {
                if (!emptyPermissionName) {
                    pw.print('+');
                }
                pw.print(android.app.AppOpsManager.opToPublicName(((java.lang.Integer) permission.second).intValue()));
            }
            pw.println(':');
            synchronized (appPermissionTracker.mLock) {
                try {
                    android.util.SparseArray<android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState>> uidPerms = appPermissionTracker.mUidGrantedPermissionsInMonitor;
                    pw.print(prefixMoreMore);
                    pw.print('[');
                    boolean needDelimiter = false;
                    int i3 = 0;
                    int size = uidPerms.size();
                    while (i3 < size) {
                        android.util.ArraySet<com.android.server.am.AppPermissionTracker.UidGrantedPermissionState> uidPerm = uidPerms.valueAt(i3);
                        int i4 = length;
                        int j = uidPerm.size() - 1;
                        while (true) {
                            if (j < 0) {
                                pairArr2 = permissions;
                                break;
                            }
                            com.android.server.am.AppPermissionTracker.UidGrantedPermissionState state = uidPerm.valueAt(j);
                            pairArr2 = permissions;
                            try {
                                if (state.mAppOp != ((java.lang.Integer) permission.second).intValue() || !android.text.TextUtils.equals(state.mPermission, (java.lang.CharSequence) permission.first)) {
                                    j--;
                                    permissions = pairArr2;
                                } else {
                                    if (needDelimiter) {
                                        pw.print(',');
                                    }
                                    pw.print(android.os.UserHandle.formatUid(state.mUid));
                                    needDelimiter = true;
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        i3++;
                        permissions = pairArr2;
                        length = i4;
                    }
                    i = length;
                    pairArr = permissions;
                    pw.println(']');
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
            i2++;
            appPermissionTracker = this;
            permissions = pairArr;
            length = i;
        }
        super.dump(pw, prefix);
    }

    static final class AppPermissionPolicy extends com.android.server.am.BaseAppStatePolicy<com.android.server.am.AppPermissionTracker> {
        static final java.lang.String[] DEFAULT_BG_PERMISSIONS_IN_MONITOR = {"android.permission.ACCESS_FINE_LOCATION", "android:fine_location", "android.permission.CAMERA", "android:camera", "android.permission.RECORD_AUDIO", "android:record_audio"};
        static final boolean DEFAULT_BG_PERMISSION_MONITOR_ENABLED = true;
        static final java.lang.String KEY_BG_PERMISSIONS_IN_MONITOR = "bg_permission_in_monitor";
        static final java.lang.String KEY_BG_PERMISSION_MONITOR_ENABLED = "bg_permission_monitor_enabled";
        volatile android.util.Pair[] mBgPermissionsInMonitor;

        AppPermissionPolicy(com.android.server.am.BaseAppStateTracker.Injector injector, com.android.server.am.AppPermissionTracker tracker) {
            super(injector, tracker, KEY_BG_PERMISSION_MONITOR_ENABLED, true);
            this.mBgPermissionsInMonitor = parsePermissionConfig(DEFAULT_BG_PERMISSIONS_IN_MONITOR);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onSystemReady() {
            super.onSystemReady();
            updateBgPermissionsInMonitor();
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onPropertiesChanged(java.lang.String name) {
            byte b;
            switch (name.hashCode()) {
                case -1888141258:
                    if (name.equals(KEY_BG_PERMISSIONS_IN_MONITOR)) {
                        b = 0;
                        break;
                    }
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    updateBgPermissionsInMonitor();
                    break;
                default:
                    super.onPropertiesChanged(name);
                    break;
            }
        }

        android.util.Pair[] getBgPermissionsInMonitor() {
            return this.mBgPermissionsInMonitor;
        }

        private android.util.Pair[] parsePermissionConfig(java.lang.String[] perms) {
            android.util.Pair[] result = new android.util.Pair[perms.length / 2];
            int i = 0;
            int j = 0;
            while (i < perms.length) {
                try {
                    result[j] = android.util.Pair.create(android.text.TextUtils.isEmpty(perms[i]) ? null : perms[i], java.lang.Integer.valueOf(android.text.TextUtils.isEmpty(perms[i + 1]) ? -1 : android.app.AppOpsManager.strOpToOp(perms[i + 1])));
                } catch (java.lang.Exception e) {
                }
                i += 2;
                j++;
            }
            return result;
        }

        private void updateBgPermissionsInMonitor() {
            java.lang.String config = android.provider.DeviceConfig.getString("activity_manager", KEY_BG_PERMISSIONS_IN_MONITOR, (java.lang.String) null);
            android.util.Pair[] newPermsInMonitor = parsePermissionConfig(config != null ? config.split(",") : DEFAULT_BG_PERMISSIONS_IN_MONITOR);
            if (!java.util.Arrays.equals(this.mBgPermissionsInMonitor, newPermsInMonitor)) {
                this.mBgPermissionsInMonitor = newPermsInMonitor;
                if (isEnabled()) {
                    onTrackerEnabled(false);
                    onTrackerEnabled(true);
                }
            }
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        public void onTrackerEnabled(boolean enabled) {
            ((com.android.server.am.AppPermissionTracker) this.mTracker).onPermissionTrackerEnabled(enabled);
        }

        @Override // com.android.server.am.BaseAppStatePolicy
        void dump(java.io.PrintWriter pw, java.lang.String prefix) {
            pw.print(prefix);
            pw.println("APP PERMISSION TRACKER POLICY SETTINGS:");
            java.lang.String prefix2 = "  " + prefix;
            super.dump(pw, prefix2);
            pw.print(prefix2);
            pw.print(KEY_BG_PERMISSIONS_IN_MONITOR);
            pw.print('=');
            pw.print('[');
            for (int i = 0; i < this.mBgPermissionsInMonitor.length; i++) {
                if (i > 0) {
                    pw.print(',');
                }
                android.util.Pair<java.lang.String, java.lang.Integer> pair = this.mBgPermissionsInMonitor[i];
                if (pair.first != null) {
                    pw.print((java.lang.String) pair.first);
                }
                pw.print(',');
                if (((java.lang.Integer) pair.second).intValue() != -1) {
                    pw.print(android.app.AppOpsManager.opToPublicName(((java.lang.Integer) pair.second).intValue()));
                }
            }
            pw.println(']');
        }
    }
}
