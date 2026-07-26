package com.android.server.clipboard;

/* JADX INFO: loaded from: classes.dex */
public class ClipboardService extends com.android.server.SystemService {
    public static final long DEFAULT_CLIPBOARD_TIMEOUT_MILLIS = 3600000;
    private static final int DEFAULT_MAX_CLASSIFICATION_LENGTH = 400;
    public static final java.lang.String PROPERTY_AUTO_CLEAR_ENABLED = "auto_clear_enabled";
    public static final java.lang.String PROPERTY_AUTO_CLEAR_TIMEOUT = "auto_clear_timeout";
    private static final java.lang.String PROPERTY_MAX_CLASSIFICATION_LENGTH = "max_classification_length";
    private static final java.lang.String TAG = "ClipboardService";
    private com.android.server.clipboard.ClipboardService.ClipboardServiceWrapper csWrapper;
    private boolean mAllowVirtualDeviceSilos;
    private final android.app.ActivityManagerInternal mAmInternal;
    private final android.app.AppOpsManager mAppOps;
    private final android.view.autofill.AutofillManagerInternal mAutofillInternal;
    private final java.util.function.Consumer<android.content.ClipData> mClipboardMonitor;
    public com.android.server.clipboard.IClipboardServiceExt mClipboardServiceExt;
    private final android.util.SparseArrayMap<java.lang.Integer, com.android.server.clipboard.ClipboardService.Clipboard> mClipboards;
    private final com.android.server.contentcapture.ContentCaptureManagerInternal mContentCaptureInternal;
    private final java.lang.Object mLock;
    private int mMaxClassificationLength;
    private final android.os.IBinder mPermissionOwner;
    private final android.content.pm.PackageManager mPm;
    private boolean mShowAccessNotifications;
    private final android.app.IUriGrantsManager mUgm;
    private final com.android.server.uri.UriGrantsManagerInternal mUgmInternal;
    private final android.os.IUserManager mUm;
    private final android.companion.virtual.VirtualDeviceManager mVdm;
    private final com.android.server.companion.virtual.VirtualDeviceManagerInternal mVdmInternal;
    private android.companion.virtual.VirtualDeviceManager.VirtualDeviceListener mVirtualDeviceListener;
    private android.content.BroadcastReceiver mVirtualDeviceRemovedReceiver;
    private final com.android.server.wm.WindowManagerInternal mWm;
    private final android.os.Handler mWorkerHandler;

    public ClipboardService(android.content.Context context) {
        super(context);
        this.mClipboards = new android.util.SparseArrayMap<>();
        this.mShowAccessNotifications = true;
        this.mAllowVirtualDeviceSilos = true;
        this.mMaxClassificationLength = 400;
        this.mLock = new java.lang.Object();
        this.mClipboardServiceExt = (com.android.server.clipboard.IClipboardServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.clipboard.IClipboardServiceExt.class).create();
        this.csWrapper = new com.android.server.clipboard.ClipboardService.ClipboardServiceWrapper();
        this.mAmInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mUgm = android.app.UriGrantsManager.getService();
        this.mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
        this.mWm = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mVdmInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        this.mVdm = this.mVdmInternal == null ? null : (android.companion.virtual.VirtualDeviceManager) getContext().getSystemService(android.companion.virtual.VirtualDeviceManager.class);
        this.mPm = getContext().getPackageManager();
        this.mUm = android.os.ServiceManager.getService("user");
        this.mAppOps = (android.app.AppOpsManager) getContext().getSystemService("appops");
        this.mContentCaptureInternal = (com.android.server.contentcapture.ContentCaptureManagerInternal) com.android.server.LocalServices.getService(com.android.server.contentcapture.ContentCaptureManagerInternal.class);
        this.mAutofillInternal = (android.view.autofill.AutofillManagerInternal) com.android.server.LocalServices.getService(android.view.autofill.AutofillManagerInternal.class);
        android.os.IBinder permOwner = this.mUgmInternal.newUriPermissionOwner("clipboard");
        this.mPermissionOwner = permOwner;
        if (android.os.Build.IS_EMULATOR) {
            this.mClipboardMonitor = new com.android.server.clipboard.EmulatorClipboardMonitor(new java.util.function.Consumer() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$new$0((android.content.ClipData) obj);
                }
            });
        } else if (android.os.Build.IS_ARC) {
            this.mClipboardMonitor = new com.android.server.clipboard.ArcClipboardMonitor(new java.util.function.BiConsumer() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda4
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$new$1((android.content.ClipData) obj, (java.lang.Integer) obj2);
                }
            });
        } else {
            this.mClipboardMonitor = new java.util.function.Consumer() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.clipboard.ClipboardService.lambda$new$2((android.content.ClipData) obj);
                }
            };
        }
        updateConfig();
        android.provider.DeviceConfig.addOnPropertiesChangedListener("clipboard", getContext().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda6
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$new$3(properties);
            }
        });
        android.os.HandlerThread workerThread = new android.os.HandlerThread(TAG);
        workerThread.start();
        this.mWorkerHandler = workerThread.getThreadHandler();
        this.mClipboardServiceExt.hookServiceReady(context, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.content.ClipData clip) {
        synchronized (this.mLock) {
            com.android.server.clipboard.ClipboardService.Clipboard clipboard = getClipboardLocked(0, 0);
            if (clipboard != null) {
                setPrimaryClipInternalLocked(clipboard, clip, 1000, (java.lang.String) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(android.content.ClipData clip, java.lang.Integer uid) {
        setPrimaryClipInternal(clip, uid.intValue());
    }

    static /* synthetic */ void lambda$new$2(android.content.ClipData clip) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(android.provider.DeviceConfig.Properties properties) {
        updateConfig();
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // com.android.server.SystemService
    public void onStart() {
        android.content.IClipboard.Stub clipboardImpl = new com.android.server.clipboard.ClipboardService.ClipboardImpl();
        this.mClipboardServiceExt.hookServiceStart(clipboardImpl);
        publishBinderService("clipboard", clipboardImpl);
        if (!android.companion.virtual.flags.Flags.vdmPublicApis() && this.mVdmInternal != null) {
            registerVirtualDeviceBroadcastReceiver();
        } else if (android.companion.virtual.flags.Flags.vdmPublicApis() && this.mVdm != null) {
            registerVirtualDeviceListener();
        }
    }

    private void registerVirtualDeviceBroadcastReceiver() {
        if (this.mVirtualDeviceRemovedReceiver != null) {
            return;
        }
        this.mVirtualDeviceRemovedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.clipboard.ClipboardService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if (!intent.getAction().equals("android.companion.virtual.action.VIRTUAL_DEVICE_REMOVED")) {
                    return;
                }
                int removedDeviceId = intent.getIntExtra("android.companion.virtual.extra.VIRTUAL_DEVICE_ID", -1);
                synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                    for (int i = com.android.server.clipboard.ClipboardService.this.mClipboards.numMaps() - 1; i >= 0; i--) {
                        com.android.server.clipboard.ClipboardService.this.mClipboards.delete(com.android.server.clipboard.ClipboardService.this.mClipboards.keyAt(i), java.lang.Integer.valueOf(removedDeviceId));
                    }
                }
            }
        };
        android.content.IntentFilter filter = new android.content.IntentFilter("android.companion.virtual.action.VIRTUAL_DEVICE_REMOVED");
        getContext().registerReceiver(this.mVirtualDeviceRemovedReceiver, filter, 4);
    }

    private void registerVirtualDeviceListener() {
        if (this.mVirtualDeviceListener != null) {
            return;
        }
        this.mVirtualDeviceListener = new android.companion.virtual.VirtualDeviceManager.VirtualDeviceListener() { // from class: com.android.server.clipboard.ClipboardService.2
            public void onVirtualDeviceClosed(int deviceId) {
                synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                    for (int i = com.android.server.clipboard.ClipboardService.this.mClipboards.numMaps() - 1; i >= 0; i--) {
                        com.android.server.clipboard.ClipboardService.this.mClipboards.delete(com.android.server.clipboard.ClipboardService.this.mClipboards.keyAt(i), java.lang.Integer.valueOf(deviceId));
                    }
                }
            }
        };
        this.mVdm.registerVirtualDeviceListener(getContext().getMainExecutor(), this.mVirtualDeviceListener);
    }

    @Override // com.android.server.SystemService
    public void onUserStopped(com.android.server.SystemService.TargetUser user) {
        synchronized (this.mLock) {
            this.mClipboards.delete(user.getUserIdentifier());
        }
    }

    private void updateConfig() {
        synchronized (this.mLock) {
            this.mShowAccessNotifications = android.provider.DeviceConfig.getBoolean("clipboard", "show_access_notifications", true);
            this.mAllowVirtualDeviceSilos = android.provider.DeviceConfig.getBoolean("clipboard", "allow_virtualdevice_silos", true);
            this.mMaxClassificationLength = android.provider.DeviceConfig.getInt("clipboard", PROPERTY_MAX_CLASSIFICATION_LENGTH, 400);
        }
    }

    private class ListenerInfo {
        final java.lang.String mAttributionTag;
        final java.lang.String mPackageName;
        final int mUid;

        ListenerInfo(int uid, java.lang.String packageName, java.lang.String attributionTag) {
            this.mUid = uid;
            this.mPackageName = packageName;
            this.mAttributionTag = attributionTag;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Clipboard {
        public final int deviceId;
        java.lang.String mPrimaryClipPackage;
        android.view.textclassifier.TextClassifier mTextClassifier;
        android.content.ClipData primaryClip;
        public final int userId;
        final android.os.RemoteCallbackList<android.content.IOnPrimaryClipChangedListener> primaryClipListeners = new android.os.RemoteCallbackList<>();
        int primaryClipUid = 9999;
        final android.util.SparseBooleanArray mNotifiedUids = new android.util.SparseBooleanArray();
        final android.util.SparseBooleanArray mNotifiedTextClassifierUids = new android.util.SparseBooleanArray();
        final java.util.HashSet<java.lang.String> activePermissionOwners = new java.util.HashSet<>();

        Clipboard(int userId, int deviceId) {
            this.userId = userId;
            this.deviceId = deviceId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInternalSysWindowAppWithWindowFocus(java.lang.String callingPackage) {
        if (this.mPm.checkPermission("android.permission.INTERNAL_SYSTEM_WINDOW", callingPackage) == 0 && this.mWm.isUidFocused(android.os.Binder.getCallingUid())) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getIntendingUserId(java.lang.String packageName, int userId) {
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (!android.os.UserManager.supportsMultipleUsers() || callingUserId == userId) {
            return callingUserId;
        }
        if (android.text.TextUtils.equals(packageName, "com.android.systemui") && userId == 999) {
            return callingUserId;
        }
        int intendingUserId = this.mAmInternal.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 2, "checkClipboardServiceCallingUser", packageName);
        return intendingUserId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getIntendingUid(java.lang.String packageName, int userId) {
        return android.os.UserHandle.getUid(getIntendingUserId(packageName, userId), android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getIntendingDeviceId(int requestedDeviceId, int uid) {
        int clipboardDeviceId;
        if (this.mVdmInternal == null) {
            return 0;
        }
        android.util.ArraySet<java.lang.Integer> virtualDeviceIds = this.mVdmInternal.getDeviceIdsForUid(uid);
        synchronized (this.mLock) {
            if (!this.mAllowVirtualDeviceSilos && (!virtualDeviceIds.isEmpty() || requestedDeviceId != 0)) {
                return -1;
            }
            boolean allDevicesHaveDefaultClipboard = true;
            java.util.Iterator<java.lang.Integer> it = virtualDeviceIds.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                int deviceId = it.next().intValue();
                if (!deviceUsesDefaultClipboard(deviceId)) {
                    allDevicesHaveDefaultClipboard = false;
                    break;
                }
            }
            if (requestedDeviceId == 0) {
                return allDevicesHaveDefaultClipboard ? 0 : -1;
            }
            if (deviceUsesDefaultClipboard(requestedDeviceId)) {
                clipboardDeviceId = 0;
            } else {
                clipboardDeviceId = requestedDeviceId;
            }
            if (this.mVdmInternal.getDeviceOwnerUid(requestedDeviceId) == uid || virtualDeviceIds.contains(java.lang.Integer.valueOf(requestedDeviceId)) || (clipboardDeviceId == 0 && allDevicesHaveDefaultClipboard)) {
                return clipboardDeviceId;
            }
            int fallbackDeviceId = virtualDeviceIds.valueAt(0).intValue();
            if (deviceUsesDefaultClipboard(fallbackDeviceId)) {
                return 0;
            }
            return fallbackDeviceId;
        }
    }

    private boolean deviceUsesDefaultClipboard(int deviceId) {
        return deviceId == 0 || this.mVdm == null || this.mVdm.getDevicePolicy(deviceId, 4) == 1;
    }

    private class ClipboardImpl extends android.content.IClipboard.Stub {
        private final android.os.Handler mClipboardClearHandler;

        private ClipboardImpl() {
            this.mClipboardClearHandler = new com.android.server.clipboard.ClipboardService.ClipboardImpl.ClipboardClearHandler(com.android.server.clipboard.ClipboardService.this.mWorkerHandler.getLooper());
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            try {
                return super.onTransact(code, data, reply, flags);
            } catch (java.lang.RuntimeException e) {
                if (!(e instanceof java.lang.SecurityException)) {
                    android.util.Slog.wtf("clipboard", "Exception: ", e);
                }
                throw e;
            }
        }

        public void setPrimaryClip(android.content.ClipData clip, java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) throws java.lang.Throwable {
            checkAndSetPrimaryClip(clip, callingPackage, attributionTag, userId, deviceId, callingPackage);
        }

        public void setPrimaryClipAsPackage(android.content.ClipData clip, java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId, java.lang.String sourcePackage) throws java.lang.Throwable {
            setPrimaryClipAsPackage_enforcePermission();
            checkAndSetPrimaryClip(clip, callingPackage, attributionTag, userId, deviceId, sourcePackage);
        }

        public boolean areClipboardAccessNotificationsEnabledForUser(int userId) {
            int result = com.android.server.clipboard.ClipboardService.this.getContext().checkCallingOrSelfPermission("android.permission.MANAGE_CLIPBOARD_ACCESS_NOTIFICATION");
            if (result != 0) {
                throw new java.lang.SecurityException("areClipboardAccessNotificationsEnable requires permission MANAGE_CLIPBOARD_ACCESS_NOTIFICATION");
            }
            long callingId = android.os.Binder.clearCallingIdentity();
            try {
                return android.provider.Settings.Secure.getIntForUser(com.android.server.clipboard.ClipboardService.this.getContext().getContentResolver(), "clipboard_show_access_notifications", getDefaultClipboardAccessNotificationsSetting(), userId) != 0;
            } finally {
                android.os.Binder.restoreCallingIdentity(callingId);
            }
        }

        public void setClipboardAccessNotificationsEnabledForUser(boolean enable, int userId) {
            int result = com.android.server.clipboard.ClipboardService.this.getContext().checkCallingOrSelfPermission("android.permission.MANAGE_CLIPBOARD_ACCESS_NOTIFICATION");
            if (result != 0) {
                throw new java.lang.SecurityException("areClipboardAccessNotificationsEnable requires permission MANAGE_CLIPBOARD_ACCESS_NOTIFICATION");
            }
            long callingId = android.os.Binder.clearCallingIdentity();
            try {
                android.content.ContentResolver resolver = com.android.server.clipboard.ClipboardService.this.getContext().createContextAsUser(android.os.UserHandle.of(userId), 0).getContentResolver();
                android.provider.Settings.Secure.putInt(resolver, "clipboard_show_access_notifications", enable ? 1 : 0);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingId);
            }
        }

        private int getDefaultClipboardAccessNotificationsSetting() {
            return android.provider.DeviceConfig.getBoolean("clipboard", "show_access_notifications", true) ? 1 : 0;
        }

        private void checkAndSetPrimaryClip(android.content.ClipData clip, java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId, java.lang.String sourcePackage) throws java.lang.Throwable {
            if (clip == null || clip.getItemCount() <= 0) {
                throw new java.lang.IllegalArgumentException("No items");
            }
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(30, callingPackage, attributionTag, intendingUid, intendingUserId, intendingDeviceId)) {
                return;
            }
            com.android.server.clipboard.ClipboardService.this.checkDataOwner(clip, intendingUid);
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                try {
                    try {
                        scheduleAutoClear(userId, intendingUid, intendingDeviceId);
                        com.android.server.clipboard.ClipboardService.this.setPrimaryClipInternalLocked(clip, intendingUid, intendingDeviceId, sourcePackage);
                        boolean focus = com.android.server.clipboard.ClipboardService.this.isDefaultDeviceAndUidFocused(intendingDeviceId, intendingUid) || com.android.server.clipboard.ClipboardService.this.isVirtualDeviceAndUidFocused(intendingDeviceId, intendingUid) || com.android.server.clipboard.ClipboardService.this.isInternalSysWindowAppWithWindowFocus(callingPackage);
                        com.android.server.clipboard.ClipboardService.this.mClipboardServiceExt.onCommonSetPrimaryClipLocked(com.android.server.clipboard.ClipboardService.this.getContext(), focus, clip);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        private void scheduleAutoClear(int userId, int intendingUid, int intendingDeviceId) {
            long oldIdentity = android.os.Binder.clearCallingIdentity();
            try {
                if (android.provider.DeviceConfig.getBoolean("clipboard", com.android.server.clipboard.ClipboardService.PROPERTY_AUTO_CLEAR_ENABLED, true)) {
                    android.util.Pair<java.lang.Integer, java.lang.Integer> userIdDeviceId = new android.util.Pair<>(java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(intendingDeviceId));
                    this.mClipboardClearHandler.removeEqualMessages(101, userIdDeviceId);
                    android.os.Message clearMessage = android.os.Message.obtain(this.mClipboardClearHandler, 101, userId, intendingUid, userIdDeviceId);
                    this.mClipboardClearHandler.sendMessageDelayed(clearMessage, getTimeoutForAutoClear());
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(oldIdentity);
            }
        }

        private long getTimeoutForAutoClear() {
            return android.provider.DeviceConfig.getLong("clipboard", com.android.server.clipboard.ClipboardService.PROPERTY_AUTO_CLEAR_TIMEOUT, 3600000L);
        }

        public void clearPrimaryClip(java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(30, callingPackage, attributionTag, intendingUid, intendingUserId, intendingDeviceId)) {
                return;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                this.mClipboardClearHandler.removeEqualMessages(101, new android.util.Pair(java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(deviceId)));
                com.android.server.clipboard.ClipboardService.this.setPrimaryClipInternalLocked((android.content.ClipData) null, intendingUid, intendingDeviceId, callingPackage);
            }
        }

        public android.content.ClipData getPrimaryClip(java.lang.String pkg, java.lang.String attributionTag, int userId, int deviceId) {
            java.lang.String pkg2 = com.android.server.clipboard.ClipboardService.this.mClipboardServiceExt.getOriginPkgName(pkg);
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(pkg2, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(29, pkg2, attributionTag, intendingUid, intendingUserId, intendingDeviceId) || com.android.server.clipboard.ClipboardService.this.isDeviceLocked(intendingUserId, deviceId)) {
                return null;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                try {
                    try {
                        try {
                            com.android.server.clipboard.ClipboardService.this.addActiveOwnerLocked(intendingUid, intendingDeviceId, pkg2);
                            com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                            try {
                                if (clipboard == null) {
                                    return null;
                                }
                                com.android.server.clipboard.ClipboardService.this.mClipboardServiceExt.onCommonGetPrimaryClipLocked(com.android.server.clipboard.ClipboardService.this.getContext(), clipboard.primaryClip, pkg);
                                com.android.server.clipboard.ClipboardService.this.notifyTextClassifierLocked(clipboard, pkg2, intendingUid);
                                if (clipboard.primaryClip != null) {
                                    scheduleAutoClear(userId, intendingUid, intendingDeviceId);
                                }
                                try {
                                    android.content.ClipData clip = com.android.server.clipboard.ClipboardService.this.mClipboardServiceExt.showClassificationNotificationLocked(clipboard.primaryClip, pkg, intendingUid, clipboard.primaryClipUid, com.android.server.clipboard.ClipboardService.this.mAppOps, com.android.server.clipboard.ClipboardService.this.mContentCaptureInternal, com.android.server.clipboard.ClipboardService.this.mAutofillInternal, intendingUserId);
                                    if (clip != null) {
                                        try {
                                            return clip;
                                        } catch (java.lang.Throwable th) {
                                            e = th;
                                        }
                                    } else {
                                        try {
                                            com.android.server.clipboard.ClipboardService.this.showAccessNotificationLocked(pkg2, intendingUid, intendingUserId, clipboard);
                                            try {
                                                return com.android.server.clipboard.ClipboardService.this.mClipboardServiceExt.hookGetPrimaryClipResult(com.android.server.clipboard.ClipboardService.this.getContext(), clipboard.primaryClip, com.android.server.clipboard.ClipboardService.this.mAppOps, pkg2, intendingUid, intendingUserId, intendingDeviceId, clipboard.primaryClipUid);
                                            } catch (java.lang.Throwable th2) {
                                                e = th2;
                                            }
                                        } catch (java.lang.Throwable th3) {
                                            e = th3;
                                        }
                                    }
                                } catch (java.lang.Throwable th4) {
                                    e = th4;
                                }
                            } catch (java.lang.Throwable th5) {
                                e = th5;
                            }
                        } catch (java.lang.Throwable th6) {
                            e = th6;
                        }
                    } catch (java.lang.SecurityException e) {
                        try {
                            android.util.Slog.i(com.android.server.clipboard.ClipboardService.TAG, "Could not grant permission to primary clip. Clearing clipboard. pkg=" + pkg2);
                            com.android.server.clipboard.ClipboardService.this.setPrimaryClipInternalLocked((android.content.ClipData) null, intendingUid, intendingDeviceId, pkg2);
                            return null;
                        } catch (java.lang.Throwable th7) {
                            e = th7;
                        }
                    }
                } catch (java.lang.Throwable th8) {
                    e = th8;
                }
                throw e;
            }
        }

        public android.content.ClipDescription getPrimaryClipDescription(java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            android.content.ClipDescription description = null;
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(29, callingPackage, attributionTag, intendingUid, intendingUserId, intendingDeviceId, false) || com.android.server.clipboard.ClipboardService.this.isDeviceLocked(intendingUserId, deviceId)) {
                return null;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                if (clipboard != null && clipboard.primaryClip != null) {
                    description = clipboard.primaryClip.getDescription();
                }
            }
            return description;
        }

        public boolean hasPrimaryClip(java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            boolean z = false;
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(29, callingPackage, attributionTag, intendingUid, intendingUserId, intendingDeviceId, false) || com.android.server.clipboard.ClipboardService.this.isDeviceLocked(intendingUserId, deviceId)) {
                return false;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                if (clipboard != null && clipboard.primaryClip != null) {
                    z = true;
                }
            }
            return z;
        }

        public void addPrimaryClipChangedListener(android.content.IOnPrimaryClipChangedListener listener, java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            if (intendingDeviceId == -1) {
                android.util.Slog.i(com.android.server.clipboard.ClipboardService.TAG, "addPrimaryClipChangedListener invalid deviceId for userId:" + userId + " uid:" + intendingUid + " callingPackage:" + callingPackage + " requestedDeviceId:" + deviceId);
                return;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                if (clipboard == null) {
                    return;
                }
                clipboard.primaryClipListeners.register(listener, com.android.server.clipboard.ClipboardService.this.new ListenerInfo(intendingUid, callingPackage, attributionTag));
            }
        }

        public void removePrimaryClipChangedListener(android.content.IOnPrimaryClipChangedListener listener, java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = com.android.server.clipboard.ClipboardService.this.getIntendingUserId(callingPackage, userId);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            if (intendingDeviceId == -1) {
                android.util.Slog.i(com.android.server.clipboard.ClipboardService.TAG, "removePrimaryClipChangedListener invalid deviceId for userId:" + userId + " uid:" + intendingUid + " callingPackage:" + callingPackage);
                return;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                if (clipboard != null) {
                    clipboard.primaryClipListeners.unregister(listener);
                }
            }
        }

        public boolean hasClipboardText(java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            boolean z = false;
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(29, callingPackage, attributionTag, intendingUid, intendingUserId, intendingDeviceId, false) || com.android.server.clipboard.ClipboardService.this.isDeviceLocked(intendingUserId, deviceId)) {
                return false;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                if (clipboard == null || clipboard.primaryClip == null) {
                    return false;
                }
                java.lang.CharSequence text = clipboard.primaryClip.getItemAt(0).getText();
                if (text != null && text.length() > 0) {
                    z = true;
                }
                return z;
            }
        }

        public java.lang.String getPrimaryClipSource(java.lang.String callingPackage, java.lang.String attributionTag, int userId, int deviceId) {
            getPrimaryClipSource_enforcePermission();
            int intendingUid = com.android.server.clipboard.ClipboardService.this.getIntendingUid(callingPackage, userId);
            int intendingUserId = android.os.UserHandle.getUserId(intendingUid);
            int intendingDeviceId = com.android.server.clipboard.ClipboardService.this.getIntendingDeviceId(deviceId, intendingUid);
            if (!com.android.server.clipboard.ClipboardService.this.clipboardAccessAllowed(29, callingPackage, attributionTag, intendingUid, intendingUserId, intendingDeviceId, false) || com.android.server.clipboard.ClipboardService.this.isDeviceLocked(intendingUserId, deviceId)) {
                return null;
            }
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(intendingUserId, intendingDeviceId);
                if (clipboard == null || clipboard.primaryClip == null) {
                    return null;
                }
                return clipboard.mPrimaryClipPackage;
            }
        }

        private class ClipboardClearHandler extends android.os.Handler {
            public static final int MSG_CLEAR = 101;

            ClipboardClearHandler(android.os.Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 101:
                        int userId = msg.arg1;
                        int intendingUid = msg.arg2;
                        int intendingDeviceId = ((java.lang.Integer) ((android.util.Pair) msg.obj).second).intValue();
                        synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                            com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(userId, intendingDeviceId);
                            if (clipboard != null && clipboard.primaryClip != null) {
                                com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.CLIPBOARD_CLEARED, 1);
                                com.android.server.clipboard.ClipboardService.this.setPrimaryClipInternalLocked((android.content.ClipData) null, intendingUid, intendingDeviceId, (java.lang.String) null);
                            }
                            break;
                        }
                        return;
                    default:
                        android.util.Slog.wtf(com.android.server.clipboard.ClipboardService.TAG, "ClipboardClearHandler received unknown message " + msg.what);
                        return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.clipboard.ClipboardService.Clipboard getClipboardLocked(int userId, int deviceId) {
        com.android.server.clipboard.ClipboardService.Clipboard clipboard = (com.android.server.clipboard.ClipboardService.Clipboard) this.mClipboards.get(userId, java.lang.Integer.valueOf(deviceId));
        if (clipboard == null) {
            try {
                if (!this.mUm.isUserRunning(userId)) {
                    android.util.Slog.w(TAG, "getClipboardLocked called with not running userId " + userId);
                    return null;
                }
                if (deviceId != 0 && !this.mVdm.isValidVirtualDeviceId(deviceId)) {
                    android.util.Slog.w(TAG, "getClipboardLocked called with invalid (possibly released) deviceId " + deviceId);
                    return null;
                }
                com.android.server.clipboard.ClipboardService.Clipboard clipboard2 = new com.android.server.clipboard.ClipboardService.Clipboard(userId, deviceId);
                this.mClipboards.add(userId, java.lang.Integer.valueOf(deviceId), clipboard2);
                return clipboard2;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException calling UserManager: " + e);
                return null;
            }
        }
        return clipboard;
    }

    java.util.List<android.content.pm.UserInfo> getRelatedProfiles(int userId) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            try {
                java.util.List<android.content.pm.UserInfo> related = this.mUm.getProfiles(userId, true);
                return related;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Remote Exception calling UserManager: " + e);
                android.os.Binder.restoreCallingIdentity(origId);
                return null;
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    private boolean hasRestriction(java.lang.String restriction, int userId) {
        try {
            return this.mUm.hasUserRestriction(restriction, userId);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Remote Exception calling UserManager.getUserRestrictions: ", e);
            return true;
        }
    }

    void setPrimaryClipInternal(android.content.ClipData clip, int uid) {
        synchronized (this.mLock) {
            setPrimaryClipInternalLocked(clip, uid, 0, (java.lang.String) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPrimaryClipInternalLocked(android.content.ClipData clip, int uid, int deviceId, java.lang.String sourcePackage) {
        int size;
        com.android.server.clipboard.ClipboardService.Clipboard relatedClipboard;
        if (deviceId == 0) {
            this.mClipboardMonitor.accept(clip);
        }
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.clipboard.ClipboardService.Clipboard clipboard = getClipboardLocked(userId, deviceId);
        if (clipboard == null) {
            return;
        }
        setPrimaryClipInternalLocked(clipboard, clip, uid, sourcePackage);
        java.util.List<android.content.pm.UserInfo> related = getRelatedProfiles(userId);
        if (related != null && (size = related.size()) > 1) {
            boolean canCopy = !hasRestriction("no_cross_profile_copy_paste", userId);
            if (!canCopy) {
                clip = null;
            } else if (clip != null) {
                clip = new android.content.ClipData(clip);
                for (int i = clip.getItemCount() - 1; i >= 0; i--) {
                    clip.setItemAt(i, new android.content.ClipData.Item(clip.getItemAt(i)));
                }
                clip.fixUrisLight(userId);
            }
            for (int i2 = 0; i2 < size; i2++) {
                int id = related.get(i2).id;
                if (id != userId) {
                    boolean canCopyIntoProfile = !hasRestriction("no_sharing_into_profile", id);
                    if (canCopyIntoProfile && (relatedClipboard = getClipboardLocked(id, deviceId)) != null) {
                        setPrimaryClipInternalNoClassifyLocked(relatedClipboard, clip, uid, sourcePackage);
                    }
                }
            }
        }
    }

    void setPrimaryClipInternal(com.android.server.clipboard.ClipboardService.Clipboard clipboard, android.content.ClipData clip, int uid) {
        synchronized (this.mLock) {
            setPrimaryClipInternalLocked(clipboard, clip, uid, (java.lang.String) null);
        }
    }

    private void setPrimaryClipInternalLocked(com.android.server.clipboard.ClipboardService.Clipboard clipboard, android.content.ClipData clip, int uid, java.lang.String sourcePackage) {
        int userId = android.os.UserHandle.getUserId(uid);
        if (clip != null) {
            startClassificationLocked(clip, userId, clipboard.deviceId);
        }
        setPrimaryClipInternalNoClassifyLocked(clipboard, clip, uid, sourcePackage);
        this.mClipboardServiceExt.startAIClassificationLocked(this.mWorkerHandler.getLooper(), clip, sourcePackage, userId, clipboard.deviceId);
    }

    private void setPrimaryClipInternalNoClassifyLocked(com.android.server.clipboard.ClipboardService.Clipboard clipboard, android.content.ClipData clip, int uid, java.lang.String sourcePackage) {
        android.content.ClipDescription description;
        revokeUris(clipboard);
        clipboard.activePermissionOwners.clear();
        if (clip == null && clipboard.primaryClip == null) {
            return;
        }
        clipboard.primaryClip = clip;
        clipboard.mNotifiedUids.clear();
        clipboard.mNotifiedTextClassifierUids.clear();
        if (clip != null) {
            clipboard.primaryClipUid = uid;
            clipboard.mPrimaryClipPackage = sourcePackage;
            clipboard.primaryClip.mSensitiveNotifiedUids.clear();
        } else {
            clipboard.primaryClipUid = 9999;
            clipboard.mPrimaryClipPackage = null;
        }
        if (clip != null && (description = clip.getDescription()) != null) {
            description.setTimestamp(java.lang.System.currentTimeMillis());
        }
        sendClipChangedBroadcast(clipboard);
    }

    private void sendClipChangedBroadcast(com.android.server.clipboard.ClipboardService.Clipboard clipboard) {
        long ident = android.os.Binder.clearCallingIdentity();
        int n = clipboard.primaryClipListeners.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                com.android.server.clipboard.ClipboardService.ListenerInfo li = (com.android.server.clipboard.ClipboardService.ListenerInfo) clipboard.primaryClipListeners.getBroadcastCookie(i);
                if (clipboardAccessAllowed(29, li.mPackageName, li.mAttributionTag, li.mUid, android.os.UserHandle.getUserId(li.mUid), clipboard.deviceId)) {
                    clipboard.primaryClipListeners.getBroadcastItem(i).dispatchPrimaryClipChanged();
                }
            } catch (android.os.RemoteException | java.lang.SecurityException e) {
            } catch (java.lang.Throwable th) {
                clipboard.primaryClipListeners.finishBroadcast();
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }
        clipboard.primaryClipListeners.finishBroadcast();
        android.os.Binder.restoreCallingIdentity(ident);
    }

    private void startClassificationLocked(final android.content.ClipData clip, final int userId, final int deviceId) {
        final java.lang.CharSequence text = clip.getItemCount() == 0 ? null : clip.getItemAt(0).getText();
        if (android.text.TextUtils.isEmpty(text) || text.length() > this.mMaxClassificationLength) {
            clip.getDescription().setClassificationStatus(2);
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            final android.view.textclassifier.TextClassifier classifier = createTextClassificationManagerAsUser(userId).createTextClassificationSession(new android.view.textclassifier.TextClassificationContext.Builder(getContext().getPackageName(), "clipboard").build());
            android.os.Binder.restoreCallingIdentity(ident);
            if (text.length() > classifier.getMaxGenerateLinksTextLength()) {
                clip.getDescription().setClassificationStatus(2);
            } else {
                this.mWorkerHandler.post(new java.lang.Runnable() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() throws java.lang.Throwable {
                        this.f$0.lambda$startClassificationLocked$4(text, clip, classifier, userId, deviceId);
                    }
                });
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: doClassification, reason: merged with bridge method [inline-methods] */
    public void lambda$startClassificationLocked$4(java.lang.CharSequence text, android.content.ClipData clip, android.view.textclassifier.TextClassifier classifier, int userId, int deviceId) throws java.lang.Throwable {
        com.android.server.clipboard.ClipboardService.Clipboard clipboard;
        android.view.textclassifier.TextLinks.Request request = new android.view.textclassifier.TextLinks.Request.Builder(text).build();
        android.view.textclassifier.TextLinks links = classifier.generateLinks(request);
        android.util.ArrayMap<java.lang.String, java.lang.Float> confidences = new android.util.ArrayMap<>();
        for (android.view.textclassifier.TextLinks.TextLink link : links.getLinks()) {
            for (int i = 0; i < link.getEntityCount(); i++) {
                java.lang.String entity = link.getEntity(i);
                float conf = link.getConfidenceScore(entity);
                if (conf > confidences.getOrDefault(entity, java.lang.Float.valueOf(0.0f)).floatValue()) {
                    confidences.put(entity, java.lang.Float.valueOf(conf));
                }
            }
        }
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.clipboard.ClipboardService.Clipboard clipboard2 = getClipboardLocked(userId, deviceId);
                    if (clipboard2 == null) {
                        return;
                    }
                    if (clipboard2.primaryClip == clip) {
                        applyClassificationAndSendBroadcastLocked(clipboard2, confidences, links, classifier);
                        java.util.List<android.content.pm.UserInfo> related = getRelatedProfiles(userId);
                        if (related != null) {
                            int size = related.size();
                            int i2 = 0;
                            while (i2 < size) {
                                int id = related.get(i2).id;
                                if (id == userId) {
                                    clipboard = clipboard2;
                                } else {
                                    boolean canCopyIntoProfile = !hasRestriction("no_sharing_into_profile", id);
                                    if (!canCopyIntoProfile) {
                                        clipboard = clipboard2;
                                    } else {
                                        com.android.server.clipboard.ClipboardService.Clipboard relatedClipboard = getClipboardLocked(id, deviceId);
                                        clipboard = clipboard2;
                                        if (relatedClipboard != null && hasTextLocked(relatedClipboard, text)) {
                                            applyClassificationAndSendBroadcastLocked(relatedClipboard, confidences, links, classifier);
                                        }
                                    }
                                }
                                i2++;
                                clipboard2 = clipboard;
                            }
                        }
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private void applyClassificationAndSendBroadcastLocked(com.android.server.clipboard.ClipboardService.Clipboard clipboard, android.util.ArrayMap<java.lang.String, java.lang.Float> confidences, android.view.textclassifier.TextLinks links, android.view.textclassifier.TextClassifier classifier) {
        clipboard.mTextClassifier = classifier;
        clipboard.primaryClip.getDescription().setConfidenceScores(confidences);
        if (!links.getLinks().isEmpty()) {
            clipboard.primaryClip.getItemAt(0).setTextLinks(links);
        }
        sendClipChangedBroadcast(clipboard);
    }

    private boolean hasTextLocked(com.android.server.clipboard.ClipboardService.Clipboard clipboard, java.lang.CharSequence text) {
        return clipboard.primaryClip != null && clipboard.primaryClip.getItemCount() > 0 && text.equals(clipboard.primaryClip.getItemAt(0).getText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean isDeviceLocked(int r5, int r6) {
        /*
            r4 = this;
            long r0 = android.os.Binder.clearCallingIdentity()
            android.content.Context r2 = r4.getContext()     // Catch: java.lang.Throwable -> L1f
            java.lang.Class<android.app.KeyguardManager> r3 = android.app.KeyguardManager.class
            java.lang.Object r2 = r2.getSystemService(r3)     // Catch: java.lang.Throwable -> L1f
            android.app.KeyguardManager r2 = (android.app.KeyguardManager) r2     // Catch: java.lang.Throwable -> L1f
            if (r2 == 0) goto L1a
            boolean r3 = r2.isDeviceLocked(r5, r6)     // Catch: java.lang.Throwable -> L1f
            if (r3 == 0) goto L1a
            r3 = 1
            goto L1b
        L1a:
            r3 = 0
        L1b:
            android.os.Binder.restoreCallingIdentity(r0)
            return r3
        L1f:
            r2 = move-exception
            android.os.Binder.restoreCallingIdentity(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.clipboard.ClipboardService.isDeviceLocked(int, int):boolean");
    }

    private void checkUriOwner(android.net.Uri uri, int sourceUid) {
        if (uri == null || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mUgmInternal.checkGrantUriPermission(sourceUid, null, android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(sourceUid)));
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void checkItemOwner(android.content.ClipData.Item item, int uid) {
        if (item.getUri() != null) {
            checkUriOwner(item.getUri(), uid);
        }
        android.content.Intent intent = item.getIntent();
        if (intent != null && intent.getData() != null) {
            checkUriOwner(intent.getData(), uid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkDataOwner(android.content.ClipData data, int uid) {
        int N = data.getItemCount();
        for (int i = 0; i < N; i++) {
            checkItemOwner(data.getItemAt(i), uid);
        }
    }

    private void grantUriPermission(android.net.Uri uri, int sourceUid, java.lang.String targetPkg, int targetUserId) {
        if (uri == null || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mUgm.grantUriPermissionFromOwner(this.mPermissionOwner, sourceUid, targetPkg, android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(sourceUid)), targetUserId);
        } catch (android.os.RemoteException e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(ident);
    }

    private void grantItemPermission(android.content.ClipData.Item item, int sourceUid, java.lang.String targetPkg, int targetUserId) {
        if (item.getUri() != null) {
            grantUriPermission(item.getUri(), sourceUid, targetPkg, targetUserId);
        }
        android.content.Intent intent = item.getIntent();
        if (intent != null && intent.getData() != null) {
            grantUriPermission(intent.getData(), sourceUid, targetPkg, targetUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveOwnerLocked(int uid, int deviceId, java.lang.String pkg) {
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        int targetUserHandle = android.os.UserHandle.getCallingUserId();
        long oldIdentity = android.os.Binder.clearCallingIdentity();
        try {
            if (!pm.isSameApp(pkg, 0L, uid, targetUserHandle)) {
                throw new java.lang.SecurityException("Calling uid " + uid + " does not own package " + pkg);
            }
            android.os.Binder.restoreCallingIdentity(oldIdentity);
            com.android.server.clipboard.ClipboardService.Clipboard clipboard = getClipboardLocked(android.os.UserHandle.getUserId(uid), deviceId);
            if (clipboard != null && clipboard.primaryClip != null && !clipboard.activePermissionOwners.contains(pkg)) {
                int N = clipboard.primaryClip.getItemCount();
                for (int i = 0; i < N; i++) {
                    grantItemPermission(clipboard.primaryClip.getItemAt(i), clipboard.primaryClipUid, pkg, android.os.UserHandle.getUserId(uid));
                }
                clipboard.activePermissionOwners.add(pkg);
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(oldIdentity);
            throw th;
        }
    }

    private void revokeUriPermission(android.net.Uri uri, int sourceUid) {
        if (uri == null || !com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return;
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mUgmInternal.revokeUriPermissionFromOwner(this.mPermissionOwner, android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(sourceUid)));
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void revokeItemPermission(android.content.ClipData.Item item, int sourceUid) {
        if (item.getUri() != null) {
            revokeUriPermission(item.getUri(), sourceUid);
        }
        android.content.Intent intent = item.getIntent();
        if (intent != null && intent.getData() != null) {
            revokeUriPermission(intent.getData(), sourceUid);
        }
    }

    private void revokeUris(com.android.server.clipboard.ClipboardService.Clipboard clipboard) {
        if (clipboard.primaryClip == null) {
            return;
        }
        int N = clipboard.primaryClip.getItemCount();
        for (int i = 0; i < N; i++) {
            revokeItemPermission(clipboard.primaryClip.getItemAt(i), clipboard.primaryClipUid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clipboardAccessAllowed(int op, java.lang.String callingPackage, java.lang.String attributionTag, int uid, int userId, int intendingDeviceId) {
        return clipboardAccessAllowed(op, callingPackage, attributionTag, uid, userId, intendingDeviceId, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean clipboardAccessAllowed(int op, java.lang.String callingPackage, java.lang.String attributionTag, int uid, int userId, int intendingDeviceId, boolean shouldNoteOp) {
        boolean allowed;
        boolean allowed2;
        int appOpsResult;
        this.mAppOps.checkPackage(uid, callingPackage);
        if (intendingDeviceId == -1) {
            android.util.Slog.w(TAG, "Clipboard access denied to " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + callingPackage + " due to invalid device id");
            return false;
        }
        if (this.mClipboardServiceExt.isPrivilegedPackage(callingPackage, getContext())) {
            android.util.Slog.d(TAG, "op = " + op + " clipboardAccessAllowed return true for privileged package " + callingPackage);
            return true;
        }
        if (this.mPm.checkPermission("android.permission.READ_CLIPBOARD_IN_BACKGROUND", callingPackage) == 0) {
            allowed = true;
        } else {
            allowed = isDefaultIme(userId, callingPackage);
        }
        switch (op) {
            case 29:
                if (!allowed) {
                    allowed = ((isDefaultDeviceAndUidFocused(intendingDeviceId, uid) || isVirtualDeviceAndUidFocused(intendingDeviceId, uid)) && !this.mClipboardServiceExt.isActivityPreloadingPkg(callingPackage)) || isInternalSysWindowAppWithWindowFocus(callingPackage);
                }
                if (!allowed && this.mContentCaptureInternal != null) {
                    allowed = this.mContentCaptureInternal.isContentCaptureServiceForUser(uid, userId);
                }
                if (!allowed && this.mAutofillInternal != null) {
                    allowed = this.mAutofillInternal.isAugmentedAutofillServiceForUser(uid, userId);
                }
                if (!allowed && intendingDeviceId != 0) {
                    boolean allowed3 = this.mVdmInternal != null && this.mVdmInternal.getDeviceOwnerUid(intendingDeviceId) == uid;
                    allowed2 = allowed3;
                } else {
                    allowed2 = allowed;
                }
                break;
            case 30:
                allowed2 = true;
                break;
            default:
                throw new java.lang.IllegalArgumentException("Unknown clipboard appop " + op);
        }
        if (!allowed2) {
            android.util.Slog.e(TAG, "op = " + op + "Denying clipboard access to " + callingPackage + ", application is not in focus nor is it a system service for user " + userId);
            return false;
        }
        if (shouldNoteOp) {
            appOpsResult = this.mAppOps.noteOp(op, uid, callingPackage, attributionTag, (java.lang.String) null);
        } else {
            appOpsResult = this.mAppOps.checkOp(op, uid, callingPackage);
        }
        boolean result = appOpsResult == 0 || this.mClipboardServiceExt.hookClipboardAccessAllowedResult(appOpsResult);
        android.util.Slog.d(TAG, "clipboardAccessAllowed: op=" + op + " result=" + result + " callingPackage=" + callingPackage);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDefaultDeviceAndUidFocused(int intendingDeviceId, int uid) {
        return intendingDeviceId == 0 && this.mWm.isUidFocused(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVirtualDeviceAndUidFocused(int intendingDeviceId, int uid) {
        if (intendingDeviceId == 0 || this.mVdm == null) {
            return false;
        }
        int topFocusedDisplayId = this.mWm.getTopFocusedDisplayId();
        int focusedDeviceId = this.mVdm.getDeviceIdForDisplayId(topFocusedDisplayId);
        return focusedDeviceId == intendingDeviceId && this.mWm.isUidFocused(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDefaultIme(int userId, java.lang.String packageName) {
        android.content.ComponentName imeComponent;
        java.lang.String defaultIme = android.provider.Settings.Secure.getStringForUser(getContext().getContentResolver(), "default_input_method", userId);
        if (android.text.TextUtils.isEmpty(defaultIme) || (imeComponent = android.content.ComponentName.unflattenFromString(defaultIme)) == null) {
            return false;
        }
        java.lang.String imePkg = imeComponent.getPackageName();
        return imePkg.equals(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAccessNotificationLocked(final java.lang.String callingPackage, int uid, final int userId, com.android.server.clipboard.ClipboardService.Clipboard clipboard) {
        if (clipboard.primaryClip == null || this.mClipboardServiceExt.isPrivilegedPackage(callingPackage, getContext()) || !this.mClipboardServiceExt.hookShowAccessNotification(getContext(), callingPackage, uid, clipboard.primaryClipUid, this.mAppOps)) {
            return;
        }
        if (android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "clipboard_show_access_notifications", (this.mShowAccessNotifications && this.mClipboardServiceExt.getOplusShowAccessNotifications()) ? 1 : 0, userId) == 0 || android.os.UserHandle.isSameApp(uid, clipboard.primaryClipUid) || isDefaultIme(userId, callingPackage)) {
            return;
        }
        if (this.mContentCaptureInternal != null && this.mContentCaptureInternal.isContentCaptureServiceForUser(uid, userId)) {
            return;
        }
        if ((this.mAutofillInternal != null && this.mAutofillInternal.isAugmentedAutofillServiceForUser(uid, userId)) || this.mPm.checkPermission("android.permission.SUPPRESS_CLIPBOARD_ACCESS_NOTIFICATION", callingPackage) == 0) {
            return;
        }
        if ((clipboard.deviceId != 0 && this.mVdmInternal.getDeviceOwnerUid(clipboard.deviceId) == uid) || clipboard.mNotifiedUids.get(uid)) {
            return;
        }
        final android.util.ArraySet<android.content.Context> toastContexts = getToastContexts(clipboard);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$showAccessNotificationLocked$5(callingPackage, userId, toastContexts);
            }
        });
        clipboard.mNotifiedUids.put(uid, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAccessNotificationLocked$5(java.lang.String callingPackage, int userId, android.util.ArraySet toastContexts) throws java.lang.Exception {
        android.widget.Toast toastToShow;
        try {
            java.lang.CharSequence callingAppLabel = this.mPm.getApplicationLabel(this.mPm.getApplicationInfoAsUser(callingPackage, 0, userId));
            java.lang.String message = getContext().getString(android.R.string.number_picker_increment_scroll_mode, callingAppLabel);
            android.util.Slog.i(TAG, message);
            for (int i = 0; i < toastContexts.size(); i++) {
                android.content.Context toastContext = (android.content.Context) toastContexts.valueAt(i);
                if (android.util.SafetyProtectionUtils.shouldShowSafetyProtectionResources(getContext())) {
                    android.graphics.drawable.Drawable safetyProtectionIcon = getContext().getDrawable(android.R.drawable.ic_safety_protection);
                    toastToShow = android.widget.Toast.makeCustomToastWithIcon(toastContext, com.android.server.UiThread.get().getLooper(), message, 1, safetyProtectionIcon);
                } else {
                    toastToShow = android.widget.Toast.makeText(toastContext, com.android.server.UiThread.get().getLooper(), message, 1);
                }
                toastToShow.show();
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    private android.util.ArraySet<android.content.Context> getToastContexts(com.android.server.clipboard.ClipboardService.Clipboard clipboard) throws java.lang.IllegalStateException {
        android.view.Display display;
        android.util.ArraySet<android.content.Context> contexts = new android.util.ArraySet<>();
        if (clipboard.deviceId != 0) {
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) getContext().getSystemService(android.hardware.display.DisplayManager.class);
            int topFocusedDisplayId = this.mWm.getTopFocusedDisplayId();
            android.util.ArraySet<java.lang.Integer> displayIds = this.mVdmInternal.getDisplayIdsForDevice(clipboard.deviceId);
            if (displayIds.contains(java.lang.Integer.valueOf(topFocusedDisplayId)) && (display = displayManager.getDisplay(topFocusedDisplayId)) != null) {
                contexts.add(getContext().createDisplayContext(display));
                return contexts;
            }
            for (int i = 0; i < displayIds.size(); i++) {
                android.view.Display display2 = displayManager.getDisplay(displayIds.valueAt(i).intValue());
                if (display2 != null) {
                    contexts.add(getContext().createDisplayContext(display2));
                }
            }
            if (!contexts.isEmpty()) {
                return contexts;
            }
            android.util.Slog.e(TAG, "getToastContexts Couldn't find any VirtualDisplays for VirtualDevice " + clipboard.deviceId);
        }
        contexts.add(getContext());
        return contexts;
    }

    private static boolean isText(android.content.ClipData data) {
        if (data.getItemCount() > 1) {
            return false;
        }
        android.content.ClipData.Item item = data.getItemAt(0);
        return !android.text.TextUtils.isEmpty(item.getText()) && item.getUri() == null && item.getIntent() == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTextClassifierLocked(final com.android.server.clipboard.ClipboardService.Clipboard clipboard, final java.lang.String callingPackage, int callingUid) {
        final android.view.textclassifier.TextClassifier textClassifier;
        if (clipboard.primaryClip == null) {
            return;
        }
        android.content.ClipData.Item item = clipboard.primaryClip.getItemAt(0);
        if (item == null || !isText(clipboard.primaryClip) || (textClassifier = clipboard.mTextClassifier) == null || !this.mWm.isUidFocused(callingUid) || clipboard.mNotifiedTextClassifierUids.get(callingUid)) {
            return;
        }
        clipboard.mNotifiedTextClassifierUids.put(callingUid, true);
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.clipboard.ClipboardService$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                com.android.server.clipboard.ClipboardService.lambda$notifyTextClassifierLocked$6(callingPackage, clipboard, textClassifier);
            }
        });
    }

    static /* synthetic */ void lambda$notifyTextClassifierLocked$6(java.lang.String callingPackage, com.android.server.clipboard.ClipboardService.Clipboard clipboard, android.view.textclassifier.TextClassifier textClassifier) throws java.lang.Exception {
        android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent pasteEvent = ((android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent.Builder) ((android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent.Builder) new android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent.Builder(22).setEventContext(new android.view.textclassifier.TextClassificationContext.Builder(callingPackage, "clipboard").build())).setExtras(android.os.Bundle.forPair("source_package", clipboard.mPrimaryClipPackage))).build();
        textClassifier.onTextClassifierEvent(pasteEvent);
    }

    private android.view.textclassifier.TextClassificationManager createTextClassificationManagerAsUser(int userId) {
        android.content.Context context = getContext().createContextAsUser(android.os.UserHandle.of(userId), 0);
        return (android.view.textclassifier.TextClassificationManager) context.getSystemService(android.view.textclassifier.TextClassificationManager.class);
    }

    public com.android.server.clipboard.ClipboardService.ClipboardServiceWrapper getWrapper() {
        return this.csWrapper;
    }

    public class ClipboardServiceWrapper {
        public ClipboardServiceWrapper() {
        }

        public void updateNotifiedUids(int uid, int userId, int deviceId) {
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clip = (com.android.server.clipboard.ClipboardService.Clipboard) com.android.server.clipboard.ClipboardService.this.mClipboards.get(userId, java.lang.Integer.valueOf(deviceId));
                if (clip == null) {
                    return;
                }
                if (clip.mNotifiedUids.get(uid)) {
                    clip.mNotifiedUids.put(uid, false);
                }
            }
        }

        public android.content.ClipData getCurrentClipData(int userId, int deviceId) {
            android.content.ClipData clipData;
            synchronized (com.android.server.clipboard.ClipboardService.this.mLock) {
                com.android.server.clipboard.ClipboardService.Clipboard clipboard = com.android.server.clipboard.ClipboardService.this.getClipboardLocked(userId, deviceId);
                clipData = clipboard != null ? clipboard.primaryClip : null;
            }
            return clipData;
        }

        public boolean isDefaultImeInner(int userId, java.lang.String packageName) {
            return com.android.server.clipboard.ClipboardService.this.isDefaultIme(userId, packageName);
        }
    }
}
