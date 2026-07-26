package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
public class VoiceInteractionManagerService extends com.android.server.SystemService {
    private static final java.lang.String CS_INTENT_FILTER = "com.android.contextualsearch.LAUNCH";
    private static final java.lang.String CS_KEY_FLAG_IS_MANAGED_PROFILE_VISIBLE = "com.android.contextualsearch.is_managed_profile_visible";
    private static final java.lang.String CS_KEY_FLAG_SCREENSHOT = "com.android.contextualsearch.screenshot";
    private static final java.lang.String CS_KEY_FLAG_SECURE_FOUND = "com.android.contextualsearch.flag_secure_found";
    private static final java.lang.String CS_KEY_FLAG_VISIBLE_PACKAGE_NAMES = "com.android.contextualsearch.visible_package_names";
    static final boolean DEBUG = false;
    static final java.lang.String TAG = "VoiceInteractionManager";
    final android.app.ActivityManagerInternal mAmInternal;
    final com.android.server.wm.ActivityTaskManagerInternal mAtmInternal;
    final android.content.Context mContext;
    private com.android.server.voiceinteraction.IEnrolledModelDb mDbHelper;
    final android.app.admin.DevicePolicyManagerInternal mDpmInternal;
    private final com.android.internal.app.IVoiceInteractionSessionListener mLatencyLoggingListener;
    final android.util.ArrayMap<java.lang.Integer, com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.SoundTriggerSession> mLoadedKeyphraseIds;
    public com.android.server.contextualsearch.IOplusCustomizeVoiceInteractionManagerExt mOplusCustomizeVoiceInteractionManagerExt;
    private final com.android.server.voiceinteraction.IEnrolledModelDb mRealDbHelper;
    final android.content.ContentResolver mResolver;
    private final com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub mServiceStub;
    android.content.pm.ShortcutServiceInternal mShortcutServiceInternal;
    com.android.server.SoundTriggerInternal mSoundTriggerInternal;
    final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private com.android.internal.app.IVisualQueryRecognitionStatusListener mVisualQueryRecognitionStatusListener;
    private final android.os.RemoteCallbackList<com.android.internal.app.IVoiceInteractionSessionListener> mVoiceInteractionSessionListeners;
    final com.android.server.wm.WindowManagerInternal mWmInternal;

    public VoiceInteractionManagerService(android.content.Context context) {
        super(context);
        this.mLoadedKeyphraseIds = new android.util.ArrayMap<>();
        this.mVoiceInteractionSessionListeners = new android.os.RemoteCallbackList<>();
        this.mOplusCustomizeVoiceInteractionManagerExt = (com.android.server.contextualsearch.IOplusCustomizeVoiceInteractionManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.contextualsearch.IOplusCustomizeVoiceInteractionManagerExt.class).create();
        this.mLatencyLoggingListener = new com.android.internal.app.IVoiceInteractionSessionListener.Stub() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.3
            public void onVoiceSessionShown() throws android.os.RemoteException {
            }

            public void onVoiceSessionHidden() throws android.os.RemoteException {
            }

            public void onVoiceSessionWindowVisibilityChanged(boolean visible) throws android.os.RemoteException {
                if (visible) {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.stopHotwordTriggerToUiLatencySession(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext);
                }
            }

            public void onSetUiHints(android.os.Bundle args) throws android.os.RemoteException {
            }

            public android.os.IBinder asBinder() {
                return com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub;
            }
        };
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) java.util.Objects.requireNonNull((com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class));
        com.android.server.voiceinteraction.DatabaseHelper databaseHelper = new com.android.server.voiceinteraction.DatabaseHelper(context);
        this.mRealDbHelper = databaseHelper;
        this.mDbHelper = databaseHelper;
        this.mServiceStub = new com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub();
        this.mAmInternal = (android.app.ActivityManagerInternal) java.util.Objects.requireNonNull((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class));
        this.mAtmInternal = (com.android.server.wm.ActivityTaskManagerInternal) java.util.Objects.requireNonNull((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class));
        this.mWmInternal = (com.android.server.wm.WindowManagerInternal) java.util.Objects.requireNonNull((com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class));
        this.mDpmInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        com.android.server.pm.permission.LegacyPermissionManagerInternal permissionManagerInternal = (com.android.server.pm.permission.LegacyPermissionManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.permission.LegacyPermissionManagerInternal.class);
        permissionManagerInternal.setVoiceInteractionPackagesProvider(new com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.1
            @Override // com.android.server.pm.permission.LegacyPermissionManagerInternal.PackagesProvider
            public java.lang.String[] getPackages(int userId) {
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.initForUser(userId);
                android.content.ComponentName interactor = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.getCurInteractor(userId);
                if (interactor != null) {
                    return new java.lang.String[]{interactor.getPackageName()};
                }
                return null;
            }
        });
        this.mOplusCustomizeVoiceInteractionManagerExt.init(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("voiceinteraction", this.mServiceStub);
        publishLocalService(android.service.voice.VoiceInteractionManagerInternal.class, new com.android.server.voiceinteraction.VoiceInteractionManagerService.LocalService());
        this.mAmInternal.setVoiceInteractionManagerProvider(new android.app.ActivityManagerInternal.VoiceInteractionManagerProvider() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.2
            public void notifyActivityDestroyed(android.os.IBinder activityToken) {
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.notifyActivityDestroyed(activityToken);
            }
        });
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (500 == phase) {
            this.mShortcutServiceInternal = (android.content.pm.ShortcutServiceInternal) java.util.Objects.requireNonNull((android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class));
            this.mSoundTriggerInternal = (com.android.server.SoundTriggerInternal) com.android.server.LocalServices.getService(com.android.server.SoundTriggerInternal.class);
        } else if (phase == 600) {
            this.mServiceStub.systemRunning(isSafeMode());
        } else if (phase == 1000) {
            this.mServiceStub.registerVoiceInteractionSessionListener(this.mLatencyLoggingListener);
        }
    }

    @Override // com.android.server.SystemService
    public boolean isUserSupported(com.android.server.SystemService.TargetUser user) {
        return user.isFull();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUserSupported(android.content.pm.UserInfo user) {
        return user.isFull();
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        this.mServiceStub.initForUser(user.getUserIdentifier());
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        this.mServiceStub.initForUser(user.getUserIdentifier());
        this.mServiceStub.switchImplementationIfNeeded(false);
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        this.mServiceStub.switchUser(to.getUserIdentifier());
    }

    class LocalService extends android.service.voice.VoiceInteractionManagerInternal {
        LocalService() {
        }

        public void startLocalVoiceInteraction(android.os.IBinder callingActivity, java.lang.String attributionTag, android.os.Bundle options) {
            com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.startLocalVoiceInteraction(callingActivity, attributionTag, options);
        }

        public boolean supportsLocalVoiceInteraction() {
            return com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.supportsLocalVoiceInteraction();
        }

        public void stopLocalVoiceInteraction(android.os.IBinder callingActivity) {
            com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.stopLocalVoiceInteraction(callingActivity);
        }

        public boolean hasActiveSession(java.lang.String packageName) {
            com.android.server.voiceinteraction.VoiceInteractionSessionConnection session;
            com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl impl = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (impl == null || (session = impl.mActiveSession) == null) {
                return false;
            }
            return android.text.TextUtils.equals(packageName, session.mSessionComponentName.getPackageName());
        }

        public java.lang.String getVoiceInteractorPackageName(android.os.IBinder callingVoiceInteractor) {
            com.android.server.voiceinteraction.VoiceInteractionSessionConnection session;
            com.android.internal.app.IVoiceInteractor voiceInteractor;
            com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl impl = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (impl == null || (session = impl.mActiveSession) == null || (voiceInteractor = session.mInteractor) == null || voiceInteractor.asBinder() != callingVoiceInteractor) {
                return null;
            }
            return session.mSessionComponentName.getPackageName();
        }

        public android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity getHotwordDetectionServiceIdentity() {
            com.android.server.voiceinteraction.HotwordDetectionConnection hotwordDetectionConnection;
            com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl impl = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (impl == null || (hotwordDetectionConnection = impl.mHotwordDetectionConnection) == null) {
                return null;
            }
            return hotwordDetectionConnection.mIdentity;
        }

        public void onPreCreatedUserConversion(int userId) {
            com.android.server.utils.Slogf.d(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "onPreCreatedUserConversion(%d): calling onRoleHoldersChanged() again", java.lang.Integer.valueOf(userId));
            com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.mRoleObserver.onRoleHoldersChanged("android.app.role.ASSISTANT", android.os.UserHandle.of(userId));
        }

        public void startListeningFromWearable(android.os.ParcelFileDescriptor audioStreamFromWearable, android.media.AudioFormat audioFormatFromWearable, android.os.PersistableBundle options, android.content.ComponentName targetVisComponentName, int userId, android.service.voice.VoiceInteractionManagerInternal.WearableHotwordDetectionCallback callback) {
            android.util.Slog.d(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "#startListeningFromWearable");
            com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl impl = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub.mImpl;
            if (impl == null) {
                callback.onError("Unable to start listening from wearable because the service impl is null.");
                return;
            }
            if (targetVisComponentName != null && !targetVisComponentName.equals(impl.mComponent)) {
                callback.onError(android.text.TextUtils.formatSimple("Unable to start listening from wearable because the target VoiceInteractionService %s is different from the current VoiceInteractionService %s", new java.lang.Object[]{targetVisComponentName, impl.mComponent}));
            } else {
                if (userId != impl.mUser) {
                    callback.onError(android.text.TextUtils.formatSimple("Unable to start listening from wearable because the target userId %s is different from the current VoiceInteractionManagerServiceImpl's userId %s", new java.lang.Object[]{java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(impl.mUser)}));
                    return;
                }
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub) {
                    impl.startListeningFromWearableLocked(audioStreamFromWearable, audioFormatFromWearable, options, callback);
                }
            }
        }
    }

    class VoiceInteractionManagerServiceStub extends com.android.internal.app.IVoiceInteractionManagerService.Stub {
        private static final int SHOW_SESSION_START_ID = 0;
        private final boolean IS_HDS_REQUIRED;
        private int mCurUser;
        private boolean mCurUserSupported;
        private final boolean mEnableService;
        volatile com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl mImpl;
        private final com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.RoleObserver mRoleObserver;
        private boolean mSafeMode;
        private boolean mTemporarilyDisabled;
        private int mShowSessionId = 0;
        com.android.internal.content.PackageMonitor mPackageMonitor = new com.android.internal.content.PackageMonitor(true) { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.2
            public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packages, int uid, boolean doit) {
                int userHandle = android.os.UserHandle.getUserId(uid);
                android.content.ComponentName curInteractor = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurInteractor(userHandle);
                android.content.ComponentName curRecognizer = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurRecognizer(userHandle);
                boolean hitInt = false;
                boolean hitRec = false;
                int length = packages.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    java.lang.String pkg = packages[i];
                    if (curInteractor != null && pkg.equals(curInteractor.getPackageName())) {
                        hitInt = true;
                        break;
                    }
                    if (curRecognizer == null || !pkg.equals(curRecognizer.getPackageName())) {
                        i++;
                    } else {
                        hitRec = true;
                        break;
                    }
                }
                if (hitInt && doit) {
                    synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                        android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Force stopping current voice interactor: " + com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurInteractor(userHandle));
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.unloadAllKeyphraseModels();
                        if (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl != null) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl.shutdownLocked();
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setImplLocked(null);
                        }
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.switchImplementationIfNeededLocked(true);
                    }
                } else if (hitRec && doit) {
                    synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                        android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Force stopping current voice recognizer: " + com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurRecognizer(userHandle));
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.initRecognizer(userHandle);
                    }
                }
                return hitInt || hitRec;
            }

            public void onPackageModified(java.lang.String pkgName) {
                if (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser != getChangingUserId() || isPackageAppearing(pkgName) != 0) {
                    return;
                }
                if (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurRecognizer(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser) == null) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.initRecognizer(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser);
                }
                java.lang.String curInteractorStr = android.provider.Settings.Secure.getStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser);
                android.content.ComponentName curInteractor = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurInteractor(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser);
                if (curInteractor == null && !"".equals(curInteractorStr)) {
                    android.service.voice.VoiceInteractionServiceInfo availInteractorInfo = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.findAvailInteractor(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser, pkgName);
                    if (availInteractorInfo != null) {
                        android.content.ComponentName availInteractor = new android.content.ComponentName(availInteractorInfo.getServiceInfo().packageName, availInteractorInfo.getServiceInfo().name);
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurInteractor(availInteractor, com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mCurUser);
                        return;
                    }
                    return;
                }
                if (didSomePackagesChange()) {
                    if (curInteractor != null && pkgName.equals(curInteractor.getPackageName())) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.switchImplementationIfNeeded(true);
                        return;
                    }
                    return;
                }
                if (curInteractor != null && isComponentModified(curInteractor.getClassName())) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.switchImplementationIfNeeded(true);
                }
            }

            public void onSomePackagesChanged() {
                int userHandle = getChangingUserId();
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    android.content.ComponentName curInteractor = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurInteractor(userHandle);
                    android.content.ComponentName curRecognizer = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurRecognizer(userHandle);
                    android.content.ComponentName curAssistant = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.getCurAssistant(userHandle);
                    if (curRecognizer == null && anyPackagesAppearing()) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.initRecognizer(userHandle);
                    }
                    if (curInteractor != null) {
                        if (isPackageDisappearing(curInteractor.getPackageName()) == 3) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurInteractor(null, userHandle);
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurRecognizer(null, userHandle);
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.resetCurAssistant(userHandle);
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.initForUser(userHandle);
                            return;
                        }
                        if (isPackageAppearing(curInteractor.getPackageName()) != 0) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.resetServicesIfNoRecognitionService(curInteractor, userHandle);
                            if (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl != null && curInteractor.getPackageName().equals(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl.mComponent.getPackageName())) {
                                com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.switchImplementationIfNeededLocked(true);
                            }
                        }
                        return;
                    }
                    if (curAssistant != null) {
                        if (isPackageDisappearing(curAssistant.getPackageName()) == 3) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurInteractor(null, userHandle);
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurRecognizer(null, userHandle);
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.resetCurAssistant(userHandle);
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.initForUser(userHandle);
                            return;
                        }
                        if (isPackageAppearing(curAssistant.getPackageName()) != 0) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.resetServicesIfNoRecognitionService(curAssistant, userHandle);
                        }
                    }
                    if (curRecognizer != null) {
                        int change = isPackageDisappearing(curRecognizer.getPackageName());
                        if (change == 3 || change == 2) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurRecognizer(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.findAvailRecognizer(null, userHandle), userHandle);
                        } else if (isPackageModified(curRecognizer.getPackageName())) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.setCurRecognizer(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.findAvailRecognizer(curRecognizer.getPackageName(), userHandle), userHandle);
                        }
                    }
                }
            }
        };

        VoiceInteractionManagerServiceStub() {
            this.IS_HDS_REQUIRED = com.android.server.policy.AppOpsPolicy.isHotwordDetectionServiceRequired(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager());
            this.mEnableService = shouldEnableService(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext);
            this.mRoleObserver = new com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.RoleObserver(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getMainExecutor());
        }

        void handleUserStop(java.lang.String packageName, int userHandle) {
            synchronized (this) {
                android.content.ComponentName curInteractor = getCurInteractor(userHandle);
                if (curInteractor != null && packageName.equals(curInteractor.getPackageName())) {
                    android.util.Slog.d(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "switchImplementation for user stop.");
                    switchImplementationIfNeededLocked(true);
                }
            }
        }

        int getNextShowSessionId() {
            int i;
            synchronized (this) {
                if (this.mShowSessionId == 2147483646) {
                    this.mShowSessionId = 0;
                }
                this.mShowSessionId++;
                i = this.mShowSessionId;
            }
            return i;
        }

        int getShowSessionId() {
            int i;
            synchronized (this) {
                i = this.mShowSessionId;
            }
            return i;
        }

        public com.android.internal.app.IVoiceInteractionSoundTriggerSession createSoundTriggerSessionAsOriginator(android.media.permission.Identity originatorIdentity, android.os.IBinder client, android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties) {
            boolean forHotwordDetectionService;
            java.util.Objects.requireNonNull(originatorIdentity);
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                forHotwordDetectionService = (this.mImpl == null || this.mImpl.mHotwordDetectionConnection == null) ? false : true;
            }
            android.media.permission.SafeCloseable ignored = android.media.permission.PermissionUtil.establishIdentityDirect(originatorIdentity);
            try {
                if (!this.IS_HDS_REQUIRED) {
                    forHotwordDetectionService = true;
                }
                com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.SoundTriggerSession soundTriggerSession = new com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.SoundTriggerSession(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mSoundTriggerInternal.attach(client, moduleProperties, forHotwordDetectionService), originatorIdentity);
                if (ignored != null) {
                    ignored.close();
                }
                return soundTriggerSession;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public java.util.List<android.hardware.soundtrigger.SoundTrigger.ModuleProperties> listModuleProperties(android.media.permission.Identity originatorIdentity) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
            }
            return com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mSoundTriggerInternal.listModuleProperties(originatorIdentity);
        }

        void startLocalVoiceInteraction(final android.os.IBinder token, java.lang.String attributionTag, android.os.Bundle options) {
            if (this.mImpl == null) {
                return;
            }
            final int callingUid = android.os.Binder.getCallingUid();
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.voiceinteraction.HotwordMetricsLogger.cancelHotwordTriggerToUiLatencySession(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext);
                this.mImpl.showSessionLocked(options, 16, attributionTag, new com.android.internal.app.IVoiceInteractionSessionShowCallback.Stub() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.1
                    public void onFailed() {
                    }

                    public void onShown() {
                        synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                            if (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl != null) {
                                com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl.grantImplicitAccessLocked(callingUid, null);
                            }
                        }
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.onLocalVoiceInteractionStarted(token, com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl.mActiveSession.mSession, com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.mImpl.mActiveSession.mInteractor);
                    }
                }, token);
            } finally {
                android.os.Binder.restoreCallingIdentity(caller);
            }
        }

        public void stopLocalVoiceInteraction(android.os.IBinder callingActivity) {
            if (this.mImpl == null) {
                return;
            }
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                this.mImpl.finishLocked(callingActivity, true);
            } finally {
                android.os.Binder.restoreCallingIdentity(caller);
            }
        }

        public boolean supportsLocalVoiceInteraction() {
            if (this.mImpl == null) {
                return false;
            }
            return this.mImpl.supportsLocalVoiceInteraction();
        }

        void notifyActivityDestroyed(final android.os.IBinder activityToken) {
            synchronized (this) {
                if (this.mImpl != null && activityToken != null) {
                    android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda2
                        public final void runOrThrow() throws java.lang.Exception {
                            this.f$0.lambda$notifyActivityDestroyed$0(activityToken);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyActivityDestroyed$0(android.os.IBinder activityToken) throws java.lang.Exception {
            this.mImpl.notifyActivityDestroyedLocked(activityToken);
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            try {
                return super.onTransact(code, data, reply, flags);
            } catch (java.lang.RuntimeException e) {
                if (!(e instanceof java.lang.SecurityException)) {
                    android.util.Slog.wtf(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "VoiceInteractionManagerService Crash", e);
                }
                throw e;
            }
        }

        public void initForUser(int userHandle) {
            com.android.server.utils.TimingsTraceAndSlog t = null;
            initForUserNoTracing(userHandle);
            if (0 != 0) {
                t.traceEnd();
            }
        }

        private void initForUserNoTracing(int userHandle) {
            java.lang.String curInteractorStr = android.provider.Settings.Secure.getStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", userHandle);
            android.content.ComponentName curRecognizer = getCurRecognizer(userHandle);
            android.service.voice.VoiceInteractionServiceInfo curInteractorInfo = null;
            if (curInteractorStr == null && curRecognizer != null && this.mEnableService && (curInteractorInfo = findAvailInteractor(userHandle, curRecognizer.getPackageName())) != null) {
                curRecognizer = null;
            }
            java.lang.String forceInteractorPackage = getForceVoiceInteractionServicePackage(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getResources());
            if (forceInteractorPackage != null && (curInteractorInfo = findAvailInteractor(userHandle, forceInteractorPackage)) != null) {
                curRecognizer = null;
            }
            if (!this.mEnableService && curInteractorStr != null && !android.text.TextUtils.isEmpty(curInteractorStr)) {
                setCurInteractor(null, userHandle);
                curInteractorStr = "";
            }
            if (curRecognizer != null) {
                android.content.pm.IPackageManager pm = android.app.AppGlobals.getPackageManager();
                android.content.pm.ServiceInfo interactorInfo = null;
                android.content.pm.ServiceInfo recognizerInfo = null;
                android.content.ComponentName curInteractor = !android.text.TextUtils.isEmpty(curInteractorStr) ? android.content.ComponentName.unflattenFromString(curInteractorStr) : null;
                try {
                    recognizerInfo = pm.getServiceInfo(curRecognizer, 786560L, userHandle);
                    if (recognizerInfo != null) {
                        com.android.server.voiceinteraction.RecognitionServiceInfo rsi = com.android.server.voiceinteraction.RecognitionServiceInfo.parseInfo(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager(), recognizerInfo);
                        if (!android.text.TextUtils.isEmpty(rsi.getParseError())) {
                            android.util.Log.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Parse error in getAvailableServices: " + rsi.getParseError());
                        }
                        if (!rsi.isSelectableAsDefault()) {
                            recognizerInfo = null;
                        }
                    }
                    if (curInteractor != null) {
                        interactorInfo = pm.getServiceInfo(curInteractor, 786432L, userHandle);
                    }
                } catch (android.os.RemoteException e) {
                }
                if (recognizerInfo != null && (curInteractor == null || interactorInfo != null)) {
                    return;
                }
            }
            if (curInteractorInfo == null && this.mEnableService && !"".equals(curInteractorStr)) {
                curInteractorInfo = findAvailInteractor(userHandle, null);
            }
            if (curInteractorInfo != null) {
                setCurInteractor(new android.content.ComponentName(curInteractorInfo.getServiceInfo().packageName, curInteractorInfo.getServiceInfo().name), userHandle);
            } else {
                setCurInteractor(null, userHandle);
            }
            initRecognizer(userHandle);
        }

        public void initRecognizer(int userHandle) {
            android.content.ComponentName curRecognizer = findAvailRecognizer(null, userHandle);
            if (curRecognizer != null) {
                setCurRecognizer(curRecognizer, userHandle);
            }
        }

        private boolean shouldEnableService(android.content.Context context) {
            if (getForceVoiceInteractionServicePackage(context.getResources()) != null) {
                return true;
            }
            return context.getPackageManager().hasSystemFeature("android.software.voice_recognizers");
        }

        private java.lang.String getForceVoiceInteractionServicePackage(android.content.res.Resources res) {
            java.lang.String interactorPackage = res.getString(android.R.string.config_headlineFontFamilyMedium);
            if (android.text.TextUtils.isEmpty(interactorPackage)) {
                return null;
            }
            return interactorPackage;
        }

        public void systemRunning(boolean safeMode) {
            this.mSafeMode = safeMode;
            this.mPackageMonitor.register(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext, com.android.internal.os.BackgroundThread.getHandler().getLooper(), android.os.UserHandle.ALL, true);
            new com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.SettingsObserver(com.android.server.UiThread.getHandler());
            synchronized (this) {
                setCurrentUserLocked(android.app.ActivityManager.getCurrentUser());
                switchImplementationIfNeededLocked(false);
            }
        }

        private void setCurrentUserLocked(int userHandle) {
            this.mCurUser = userHandle;
            android.content.pm.UserInfo userInfo = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mUserManagerInternal.getUserInfo(this.mCurUser);
            this.mCurUserSupported = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.isUserSupported(userInfo);
        }

        public void switchUser(final int userHandle) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$switchUser$1(userHandle);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$switchUser$1(int userHandle) {
            synchronized (this) {
                setCurrentUserLocked(userHandle);
                switchImplementationIfNeededLocked(false);
            }
        }

        void switchImplementationIfNeeded(boolean force) {
            synchronized (this) {
                switchImplementationIfNeededLocked(force);
            }
        }

        void switchImplementationIfNeededLocked(boolean force) {
            if (!this.mCurUserSupported) {
                if (this.mImpl != null) {
                    this.mImpl.shutdownLocked();
                    setImplLocked(null);
                    return;
                }
                return;
            }
            com.android.server.utils.TimingsTraceAndSlog t = null;
            switchImplementationIfNeededNoTracingLocked(force);
            if (0 != 0) {
                t.traceEnd();
            }
        }

        void switchImplementationIfNeededNoTracingLocked(boolean force) {
            if (!this.mSafeMode) {
                java.lang.String curService = android.provider.Settings.Secure.getStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mResolver, "voice_interaction_service", this.mCurUser);
                android.content.ComponentName serviceComponent = null;
                android.content.pm.ServiceInfo serviceInfo = null;
                if (curService != null && !curService.isEmpty()) {
                    try {
                        serviceComponent = android.content.ComponentName.unflattenFromString(curService);
                        serviceInfo = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 0L, this.mCurUser);
                    } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                        android.util.Slog.wtf(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Bad voice interaction service name " + curService, e);
                        serviceComponent = null;
                        serviceInfo = null;
                    }
                }
                boolean hasComponent = (serviceComponent == null || serviceInfo == null) ? false : true;
                if (com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mUserManagerInternal.isUserUnlockingOrUnlocked(this.mCurUser)) {
                    if (!hasComponent) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mShortcutServiceInternal.setShortcutHostPackage(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, (java.lang.String) null, this.mCurUser);
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.setAllowAppSwitches(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, -1, this.mCurUser);
                    } else {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mShortcutServiceInternal.setShortcutHostPackage(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, serviceComponent.getPackageName(), this.mCurUser);
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.setAllowAppSwitches(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, serviceInfo.applicationInfo.uid, this.mCurUser);
                    }
                }
                if (force || this.mImpl == null || this.mImpl.mUser != this.mCurUser || !this.mImpl.mComponent.equals(serviceComponent)) {
                    unloadAllKeyphraseModels();
                    if (this.mImpl != null) {
                        this.mImpl.shutdownLocked();
                    }
                    if (hasComponent) {
                        setImplLocked(new com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext, com.android.server.UiThread.getHandler(), this, this.mCurUser, serviceComponent));
                        this.mImpl.startLocked();
                    } else {
                        setImplLocked(null);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.util.List<android.content.pm.ResolveInfo> queryInteractorServices(int user, java.lang.String packageName) {
            return com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager().queryIntentServicesAsUser(new android.content.Intent("android.service.voice.VoiceInteractionService").setPackage(packageName), 786560, user);
        }

        android.service.voice.VoiceInteractionServiceInfo findAvailInteractor(int user, java.lang.String packageName) {
            java.util.List<android.content.pm.ResolveInfo> available = queryInteractorServices(user, packageName);
            int numAvailable = available.size();
            if (numAvailable == 0) {
                android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "no available voice interaction services found for user " + user);
                return null;
            }
            android.service.voice.VoiceInteractionServiceInfo foundInfo = null;
            for (int i = 0; i < numAvailable; i++) {
                android.content.pm.ServiceInfo cur = available.get(i).serviceInfo;
                if ((cur.applicationInfo.flags & 1) != 0) {
                    android.service.voice.VoiceInteractionServiceInfo info = new android.service.voice.VoiceInteractionServiceInfo(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager(), cur);
                    if (info.getParseError() != null) {
                        android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Bad interaction service " + cur.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + cur.name + ": " + info.getParseError());
                    } else if (foundInfo == null) {
                        foundInfo = info;
                    } else {
                        android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "More than one voice interaction service, picking first " + new android.content.ComponentName(foundInfo.getServiceInfo().packageName, foundInfo.getServiceInfo().name) + " over " + new android.content.ComponentName(cur.packageName, cur.name));
                    }
                }
            }
            return foundInfo;
        }

        android.content.ComponentName getCurInteractor(int userHandle) {
            java.lang.String curInteractor = android.provider.Settings.Secure.getStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", userHandle);
            if (android.text.TextUtils.isEmpty(curInteractor)) {
                return null;
            }
            return android.content.ComponentName.unflattenFromString(curInteractor);
        }

        void setCurInteractor(android.content.ComponentName comp, int userHandle) {
            android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_interaction_service", comp != null ? comp.flattenToShortString() : "", userHandle);
        }

        android.content.ComponentName findAvailRecognizer(java.lang.String prefPackage, int userHandle) {
            if (prefPackage == null) {
                prefPackage = getDefaultRecognizer();
            }
            java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> available = com.android.server.voiceinteraction.RecognitionServiceInfo.getAvailableServices(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext, userHandle);
            if (available.size() == 0) {
                android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "no available voice recognition services found for user " + userHandle);
                return null;
            }
            java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> nonSelectableAsDefault = removeNonSelectableAsDefault(available);
            if (available.size() == 0) {
                android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "No selectableAsDefault recognition services found for user " + userHandle + ". Falling back to non selectableAsDefault ones.");
                available = nonSelectableAsDefault;
            }
            int numAvailable = available.size();
            if (prefPackage != null) {
                for (int i = 0; i < numAvailable; i++) {
                    android.content.pm.ServiceInfo serviceInfo = available.get(i).getServiceInfo();
                    if (prefPackage.equals(serviceInfo.packageName)) {
                        return new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
                    }
                }
            }
            if (numAvailable > 1) {
                android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "more than one voice recognition service found, picking first");
            }
            android.content.pm.ServiceInfo serviceInfo2 = available.get(0).getServiceInfo();
            return new android.content.ComponentName(serviceInfo2.packageName, serviceInfo2.name);
        }

        private java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> removeNonSelectableAsDefault(java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> services) {
            java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> nonSelectableAsDefault = new java.util.ArrayList<>();
            for (int i = services.size() - 1; i >= 0; i--) {
                if (!services.get(i).isSelectableAsDefault()) {
                    nonSelectableAsDefault.add(services.remove(i));
                }
            }
            return nonSelectableAsDefault;
        }

        public java.lang.String getDefaultRecognizer() {
            java.lang.String recognizer = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getString(android.R.string.config_systemSpeechRecognizer);
            if (android.text.TextUtils.isEmpty(recognizer)) {
                return null;
            }
            return recognizer;
        }

        android.content.ComponentName getCurRecognizer(int userHandle) {
            java.lang.String curRecognizer = android.provider.Settings.Secure.getStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_recognition_service", userHandle);
            if (android.text.TextUtils.isEmpty(curRecognizer)) {
                return null;
            }
            return android.content.ComponentName.unflattenFromString(curRecognizer);
        }

        void setCurRecognizer(android.content.ComponentName comp, int userHandle) {
            android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "voice_recognition_service", comp != null ? comp.flattenToShortString() : "", userHandle);
        }

        android.content.ComponentName getCurAssistant(int userHandle) {
            java.lang.String curAssistant = android.provider.Settings.Secure.getStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "assistant", userHandle);
            if (android.text.TextUtils.isEmpty(curAssistant)) {
                return null;
            }
            return android.content.ComponentName.unflattenFromString(curAssistant);
        }

        void resetCurAssistant(int userHandle) {
            android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver(), "assistant", null, userHandle);
        }

        void forceRestartHotwordDetector() {
            this.mImpl.forceRestartHotwordDetector();
        }

        void setDebugHotwordLogging(boolean logging) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setTemporaryLogging without running voice interaction service");
                } else {
                    this.mImpl.setDebugHotwordLoggingLocked(logging);
                }
            }
        }

        public void showSession(android.os.Bundle args, int flags, java.lang.String attributionTag) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.showSessionLocked(args, flags, attributionTag, null, null);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public boolean deliverNewSession(android.os.IBinder token, android.service.voice.IVoiceInteractionSession session, com.android.internal.app.IVoiceInteractor interactor) {
            boolean zDeliverNewSessionLocked;
            synchronized (this) {
                if (this.mImpl == null) {
                    throw new java.lang.SecurityException("deliverNewSession without running voice interaction service");
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    zDeliverNewSessionLocked = this.mImpl.deliverNewSessionLocked(token, session, interactor);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
            return zDeliverNewSessionLocked;
        }

        /* JADX WARN: Removed duplicated region for block: B:34:0x00a5 A[Catch: all -> 0x00d2, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0022, B:7:0x0028, B:9:0x002f, B:11:0x0035, B:13:0x0044, B:14:0x0047, B:17:0x004a, B:18:0x004e, B:19:0x004f, B:21:0x0064, B:23:0x0082, B:26:0x008d, B:27:0x0096, B:28:0x009d, B:30:0x009f, B:32:0x00a1, B:34:0x00a5, B:35:0x00ad, B:38:0x00b1, B:39:0x00b8, B:41:0x00c7, B:42:0x00ca, B:45:0x00cd, B:46:0x00d1, B:12:0x0040, B:40:0x00bc), top: B:50:0x0001, inners: #1, #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00af  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean showSessionFromSession(android.os.IBinder r11, android.os.Bundle r12, int r13, java.lang.String r14) {
            /*
                Method dump skipped, instruction units count: 213
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.showSessionFromSession(android.os.IBinder, android.os.Bundle, int, java.lang.String):boolean");
        }

        public boolean hideSessionFromSession(android.os.IBinder token) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "hideSessionFromSession without running voice interaction service");
                    return false;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mImpl.hideSessionLocked();
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public int startVoiceActivity(android.os.IBinder token, android.content.Intent intent, java.lang.String resolvedType, java.lang.String attributionTag) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "startVoiceActivity without running voice interaction service");
                    return -96;
                }
                int callingPid = android.os.Binder.getCallingPid();
                int callingUid = android.os.Binder.getCallingUid();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    android.content.pm.ActivityInfo activityInfo = intent.resolveActivityInfo(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager(), 131072);
                    if (activityInfo != null) {
                        int activityUid = activityInfo.applicationInfo.uid;
                        this.mImpl.grantImplicitAccessLocked(activityUid, intent);
                    } else {
                        android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Cannot find ActivityInfo in startVoiceActivity.");
                    }
                    return this.mImpl.startVoiceActivityLocked(attributionTag, callingPid, callingUid, token, intent, resolvedType);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public int startAssistantActivity(android.os.IBinder token, android.content.Intent intent, java.lang.String resolvedType, java.lang.String attributionTag, android.os.Bundle bundle) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "startAssistantActivity without running voice interaction service");
                    return -96;
                }
                int callingPid = android.os.Binder.getCallingPid();
                int callingUid = android.os.Binder.getCallingUid();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mImpl.startAssistantActivityLocked(attributionTag, callingPid, callingUid, token, intent, resolvedType, bundle);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void requestDirectActions(android.os.IBinder token, int taskId, android.os.IBinder assistToken, android.os.RemoteCallback cancellationCallback, android.os.RemoteCallback resultCallback) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "requestDirectActions without running voice interaction service");
                    resultCallback.sendResult((android.os.Bundle) null);
                } else {
                    long caller = android.os.Binder.clearCallingIdentity();
                    try {
                        this.mImpl.requestDirectActionsLocked(token, taskId, assistToken, cancellationCallback, resultCallback);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(caller);
                    }
                }
            }
        }

        public void performDirectAction(android.os.IBinder token, java.lang.String actionId, android.os.Bundle arguments, int taskId, android.os.IBinder assistToken, android.os.RemoteCallback cancellationCallback, android.os.RemoteCallback resultCallback) throws java.lang.Throwable {
            synchronized (this) {
                try {
                    try {
                        if (this.mImpl == null) {
                            android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "performDirectAction without running voice interaction service");
                            resultCallback.sendResult((android.os.Bundle) null);
                        } else {
                            long caller = android.os.Binder.clearCallingIdentity();
                            try {
                                this.mImpl.performDirectActionLocked(token, actionId, arguments, taskId, assistToken, cancellationCallback, resultCallback);
                            } finally {
                                android.os.Binder.restoreCallingIdentity(caller);
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

        public void setKeepAwake(android.os.IBinder token, boolean keepAwake) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setKeepAwake without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.setKeepAwakeLocked(token, keepAwake);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void closeSystemDialogs(android.os.IBinder token) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "closeSystemDialogs without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.closeSystemDialogsLocked(token);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void finish(android.os.IBinder token) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "finish without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.finishLocked(token, false);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void setDisabledShowContext(int flags) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setDisabledShowContext without running voice interaction service");
                    return;
                }
                int callingUid = android.os.Binder.getCallingUid();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.setDisabledShowContextLocked(callingUid, flags);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public int getDisabledShowContext() {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "getDisabledShowContext without running voice interaction service");
                    return 0;
                }
                int callingUid = android.os.Binder.getCallingUid();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mImpl.getDisabledShowContextLocked(callingUid);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public int getUserDisabledShowContext() {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "getUserDisabledShowContext without running voice interaction service");
                    return 0;
                }
                int callingUid = android.os.Binder.getCallingUid();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mImpl.getUserDisabledShowContextLocked(callingUid);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void setDisabled(boolean disabled) {
            super.setDisabled_enforcePermission();
            synchronized (this) {
                if (this.mTemporarilyDisabled == disabled) {
                    return;
                }
                this.mTemporarilyDisabled = disabled;
                if (this.mTemporarilyDisabled) {
                    android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setDisabled(): temporarily disabling and hiding current session");
                    try {
                        hideCurrentSession();
                    } catch (android.os.RemoteException e) {
                        android.util.Log.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Failed to call hideCurrentSession", e);
                    }
                } else {
                    android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setDisabled(): re-enabling");
                }
            }
        }

        public void startListeningVisibleActivityChanged(android.os.IBinder token) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "startListeningVisibleActivityChanged without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.startListeningVisibleActivityChangedLocked(token);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void stopListeningVisibleActivityChanged(android.os.IBinder token) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "stopListeningVisibleActivityChanged without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.stopListeningVisibleActivityChangedLocked(token);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void notifyActivityEventChanged(final android.os.IBinder activityToken, final int type) {
            synchronized (this) {
                if (this.mImpl != null && activityToken != null) {
                    android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda7
                        public final void runOrThrow() throws java.lang.Exception {
                            this.f$0.lambda$notifyActivityEventChanged$2(activityToken, type);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyActivityEventChanged$2(android.os.IBinder activityToken, int type) throws java.lang.Exception {
            this.mImpl.notifyActivityEventChangedLocked(activityToken, type);
        }

        public void updateState(final android.os.PersistableBundle options, final android.os.SharedMemory sharedMemory, final android.os.IBinder token) {
            super.updateState_enforcePermission();
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda3
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$updateState$3(options, sharedMemory, token);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateState$3(android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.os.IBinder token) throws java.lang.Exception {
            this.mImpl.updateStateLocked(options, sharedMemory, token);
        }

        public void initAndVerifyDetector(final android.media.permission.Identity voiceInteractorIdentity, final android.os.PersistableBundle options, final android.os.SharedMemory sharedMemory, final android.os.IBinder token, final com.android.internal.app.IHotwordRecognitionStatusCallback callback, final int detectorType) {
            super.initAndVerifyDetector_enforcePermission();
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                voiceInteractorIdentity.uid = android.os.Binder.getCallingUid();
                voiceInteractorIdentity.pid = android.os.Binder.getCallingPid();
                android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda6
                    public final void runOrThrow() throws java.lang.Exception {
                        this.f$0.lambda$initAndVerifyDetector$4(voiceInteractorIdentity, options, sharedMemory, token, callback, detectorType);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$initAndVerifyDetector$4(android.media.permission.Identity voiceInteractorIdentity, android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int detectorType) throws java.lang.Exception {
            this.mImpl.initAndVerifyDetectorLocked(voiceInteractorIdentity, options, sharedMemory, token, callback, detectorType);
        }

        public void destroyDetector(final android.os.IBinder token) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "destroyDetector without running voice interaction service");
                } else {
                    android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda4
                        public final void runOrThrow() throws java.lang.Exception {
                            this.f$0.lambda$destroyDetector$5(token);
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$destroyDetector$5(android.os.IBinder token) throws java.lang.Exception {
            this.mImpl.destroyDetectorLocked(token);
        }

        public void shutdownHotwordDetectionService() {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "shutdownHotwordDetectionService without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.shutdownHotwordDetectionServiceLocked();
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void subscribeVisualQueryRecognitionStatus(com.android.internal.app.IVisualQueryRecognitionStatusListener listener) {
            super.subscribeVisualQueryRecognitionStatus_enforcePermission();
            synchronized (this) {
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVisualQueryRecognitionStatusListener = listener;
            }
        }

        public void enableVisualQueryDetection(com.android.internal.app.IVisualQueryDetectionAttentionListener listener) {
            super.enableVisualQueryDetection_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "enableVisualQueryDetection without running voice interaction service");
                } else {
                    this.mImpl.setVisualQueryDetectionAttentionListenerLocked(listener);
                }
            }
        }

        public void disableVisualQueryDetection() {
            super.disableVisualQueryDetection_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "disableVisualQueryDetection without running voice interaction service");
                } else {
                    this.mImpl.setVisualQueryDetectionAttentionListenerLocked(null);
                }
            }
        }

        public void startPerceiving(android.service.voice.IVisualQueryDetectionVoiceInteractionCallback callback) throws android.os.RemoteException {
            enforceCallingPermission("android.permission.RECORD_AUDIO");
            enforceCallingPermission("android.permission.CAMERA");
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "startPerceiving without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    boolean success = this.mImpl.startPerceivingLocked(callback);
                    if (success && com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVisualQueryRecognitionStatusListener != null) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVisualQueryRecognitionStatusListener.onStartPerceiving();
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void stopPerceiving() throws android.os.RemoteException {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "stopPerceiving without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    boolean success = this.mImpl.stopPerceivingLocked();
                    if (success && com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVisualQueryRecognitionStatusListener != null) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVisualQueryRecognitionStatusListener.onStopPerceiving();
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void startListeningFromMic(android.media.AudioFormat audioFormat, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) throws android.os.RemoteException {
            enforceCallingPermission("android.permission.RECORD_AUDIO");
            enforceCallingPermission("android.permission.CAPTURE_AUDIO_HOTWORD");
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "startListeningFromMic without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.startListeningFromMicLocked(audioFormat, callback);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void startListeningFromExternalSource(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.os.IBinder token, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) throws android.os.RemoteException {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "startListeningFromExternalSource without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.startListeningFromExternalSourceLocked(audioStream, audioFormat, options, token, callback);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void stopListeningFromMic() throws android.os.RemoteException {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "stopListeningFromMic without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.stopListeningFromMicLocked();
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void triggerHardwareRecognitionEventForTest(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event, com.android.internal.app.IHotwordRecognitionStatusCallback callback) throws android.os.RemoteException {
            enforceCallingPermission("android.permission.RECORD_AUDIO");
            enforceCallingPermission("android.permission.CAPTURE_AUDIO_HOTWORD");
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "triggerHardwareRecognitionEventForTest without running voice interaction service");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.triggerHardwareRecognitionEventForTestLocked(event, callback);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int keyphraseId, java.lang.String bcp47Locale) {
            enforceCallerAllowedToEnrollVoiceModel();
            if (bcp47Locale == null) {
                throw new java.lang.IllegalArgumentException("Illegal argument(s) in getKeyphraseSoundModel");
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(keyphraseId, callingUserId, bcp47Locale);
            } finally {
                android.os.Binder.restoreCallingIdentity(caller);
            }
        }

        public int updateKeyphraseSoundModel(android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel model) {
            enforceCallerAllowedToEnrollVoiceModel();
            if (model == null) {
                throw new java.lang.IllegalArgumentException("Model must not be null");
            }
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                if (com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.updateKeyphraseSoundModel(model)) {
                    synchronized (this) {
                        if (this.mImpl != null && this.mImpl.mService != null) {
                            this.mImpl.notifySoundModelsChangedLocked();
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(caller);
                    return 0;
                }
                android.os.Binder.restoreCallingIdentity(caller);
                return Integer.MIN_VALUE;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(caller);
                throw th;
            }
        }

        public int deleteKeyphraseSoundModel(int keyphraseId, java.lang.String bcp47Locale) {
            int unloadStatus;
            enforceCallerAllowedToEnrollVoiceModel();
            if (bcp47Locale == null) {
                throw new java.lang.IllegalArgumentException("Illegal argument(s) in deleteKeyphraseSoundModel");
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.SoundTriggerSession session = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.get(java.lang.Integer.valueOf(keyphraseId));
                if (session != null && (unloadStatus = session.unloadKeyphraseModel(keyphraseId)) != 0) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Unable to unload keyphrase sound model:" + unloadStatus);
                }
                boolean deleted = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.deleteKeyphraseSoundModel(keyphraseId, callingUserId, bcp47Locale);
                int i = deleted ? 0 : Integer.MIN_VALUE;
                if (deleted) {
                    synchronized (this) {
                        if (this.mImpl != null && this.mImpl.mService != null) {
                            this.mImpl.notifySoundModelsChangedLocked();
                        }
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.remove(java.lang.Integer.valueOf(keyphraseId));
                    }
                }
                android.os.Binder.restoreCallingIdentity(caller);
                return i;
            } catch (java.lang.Throwable th) {
                if (0 != 0) {
                    synchronized (this) {
                        if (this.mImpl != null && this.mImpl.mService != null) {
                            this.mImpl.notifySoundModelsChangedLocked();
                        }
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.remove(java.lang.Integer.valueOf(keyphraseId));
                    }
                }
                android.os.Binder.restoreCallingIdentity(caller);
                throw th;
            }
        }

        public void setModelDatabaseForTestEnabled(boolean enabled, android.os.IBinder token) {
            super.setModelDatabaseForTestEnabled_enforcePermission();
            enforceCallerAllowedToEnrollVoiceModel();
            synchronized (this) {
                if (enabled) {
                    final com.android.server.voiceinteraction.TestModelEnrollmentDatabase db = new com.android.server.voiceinteraction.TestModelEnrollmentDatabase();
                    try {
                        token.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda5
                            @Override // android.os.IBinder.DeathRecipient
                            public final void binderDied() {
                                this.f$0.lambda$setModelDatabaseForTestEnabled$6(db);
                            }
                        }, 0);
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper = db;
                        this.mImpl.notifySoundModelsChangedLocked();
                    } catch (android.os.RemoteException e) {
                    }
                } else if (com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper != com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mRealDbHelper) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mRealDbHelper;
                    this.mImpl.notifySoundModelsChangedLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setModelDatabaseForTestEnabled$6(com.android.server.voiceinteraction.TestModelEnrollmentDatabase db) {
            synchronized (this) {
                if (com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper == db) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mRealDbHelper;
                    this.mImpl.notifySoundModelsChangedLocked();
                }
            }
        }

        public boolean isEnrolledForKeyphrase(int keyphraseId, java.lang.String bcp47Locale) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
            }
            if (bcp47Locale == null) {
                throw new java.lang.IllegalArgumentException("Illegal argument(s) in isEnrolledForKeyphrase");
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel model = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(keyphraseId, callingUserId, bcp47Locale);
                return model != null;
            } finally {
                android.os.Binder.restoreCallingIdentity(caller);
            }
        }

        public android.hardware.soundtrigger.KeyphraseMetadata getEnrolledKeyphraseMetadata(java.lang.String keyphrase, java.lang.String bcp47Locale) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
            }
            if (bcp47Locale == null) {
                throw new java.lang.IllegalArgumentException("Illegal argument(s) in isEnrolledForKeyphrase");
            }
            int callingUserId = android.os.UserHandle.getCallingUserId();
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel model = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(keyphrase, callingUserId, bcp47Locale);
                if (model == null) {
                    return null;
                }
                for (android.hardware.soundtrigger.SoundTrigger.Keyphrase phrase : model.getKeyphrases()) {
                    if (keyphrase.equals(phrase.getText())) {
                        android.util.ArraySet<java.util.Locale> locales = new android.util.ArraySet<>();
                        locales.add(phrase.getLocale());
                        return new android.hardware.soundtrigger.KeyphraseMetadata(phrase.getId(), phrase.getText(), locales, phrase.getRecognitionModes());
                    }
                }
                return null;
            } finally {
                android.os.Binder.restoreCallingIdentity(caller);
            }
        }

        private class SoundTriggerSession extends com.android.internal.app.IVoiceInteractionSoundTriggerSession.Stub {
            final com.android.server.SoundTriggerInternal.Session mSession;
            private com.android.internal.app.IHotwordRecognitionStatusCallback mSessionExternalCallback;
            private android.hardware.soundtrigger.IRecognitionStatusCallback mSessionInternalCallback;
            private final android.media.permission.Identity mVoiceInteractorIdentity;

            SoundTriggerSession(com.android.server.SoundTriggerInternal.Session session, android.media.permission.Identity voiceInteractorIdentity) {
                this.mSession = session;
                this.mVoiceInteractorIdentity = voiceInteractorIdentity;
            }

            public android.hardware.soundtrigger.SoundTrigger.ModuleProperties getDspModuleProperties() {
                android.hardware.soundtrigger.SoundTrigger.ModuleProperties moduleProperties;
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                    long caller = android.os.Binder.clearCallingIdentity();
                    try {
                        moduleProperties = this.mSession.getModuleProperties();
                    } finally {
                        android.os.Binder.restoreCallingIdentity(caller);
                    }
                }
                return moduleProperties;
            }

            public int startRecognition(int keyphraseId, java.lang.String bcp47Locale, com.android.internal.app.IHotwordRecognitionStatusCallback callback, android.hardware.soundtrigger.SoundTrigger.RecognitionConfig recognitionConfig, boolean runInBatterySaverMode) {
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                    if (callback == null || recognitionConfig == null || bcp47Locale == null) {
                        throw new java.lang.IllegalArgumentException("Illegal argument(s) in startRecognition");
                    }
                    if (runInBatterySaverMode) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceCallingPermission("android.permission.SOUND_TRIGGER_RUN_IN_BATTERY_SAVER");
                    }
                }
                int callingUserId = android.os.UserHandle.getCallingUserId();
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.getKeyphraseSoundModel(keyphraseId, callingUserId, bcp47Locale);
                    if (soundModel != null && soundModel.getUuid() != null && soundModel.getKeyphrases() != null) {
                        synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                            com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.put(java.lang.Integer.valueOf(keyphraseId), this);
                            if (this.mSessionExternalCallback == null || this.mSessionInternalCallback == null || callback.asBinder() != this.mSessionExternalCallback.asBinder()) {
                                this.mSessionInternalCallback = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.createSoundTriggerCallbackLocked(callback, this.mVoiceInteractorIdentity);
                                this.mSessionExternalCallback = callback;
                            }
                        }
                        return this.mSession.startRecognition(keyphraseId, soundModel, this.mSessionInternalCallback, recognitionConfig, runInBatterySaverMode);
                    }
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "No matching sound model found in startRecognition");
                    android.os.Binder.restoreCallingIdentity(caller);
                    return Integer.MIN_VALUE;
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }

            public int stopRecognition(int keyphraseId, com.android.internal.app.IHotwordRecognitionStatusCallback callback) {
                android.hardware.soundtrigger.IRecognitionStatusCallback soundTriggerCallback;
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                    if (this.mSessionExternalCallback == null || this.mSessionInternalCallback == null || callback.asBinder() != this.mSessionExternalCallback.asBinder()) {
                        soundTriggerCallback = com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.createSoundTriggerCallbackLocked(callback, this.mVoiceInteractorIdentity);
                        android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "stopRecognition() called with a different callback thanstartRecognition()");
                    } else {
                        soundTriggerCallback = this.mSessionInternalCallback;
                    }
                    this.mSessionExternalCallback = null;
                    this.mSessionInternalCallback = null;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mSession.stopRecognition(keyphraseId, soundTriggerCallback);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }

            public int setParameter(int keyphraseId, int modelParam, int value) {
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mSession.setParameter(keyphraseId, modelParam, value);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }

            public int getParameter(int keyphraseId, int modelParam) {
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mSession.getParameter(keyphraseId, modelParam);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }

            public android.hardware.soundtrigger.SoundTrigger.ModelParamRange queryParameter(int keyphraseId, int modelParam) {
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.enforceIsCurrentVoiceInteractionService();
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mSession.queryParameter(keyphraseId, modelParam);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }

            public void detach() {
                this.mSession.detach();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int unloadKeyphraseModel(int keyphraseId) {
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    return this.mSession.unloadKeyphraseModel(keyphraseId);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void unloadAllKeyphraseModels() {
            for (int i = 0; i < com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.size(); i++) {
                int id = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.keyAt(i).intValue();
                com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.SoundTriggerSession session = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.valueAt(i);
                int status = session.unloadKeyphraseModel(id);
                if (status != 0) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Failed to unload keyphrase " + id + ":" + status);
                }
            }
            com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mLoadedKeyphraseIds.clear();
        }

        public android.content.ComponentName getActiveServiceComponentName() {
            android.content.ComponentName componentName;
            synchronized (this) {
                componentName = this.mImpl != null ? this.mImpl.mComponent : null;
            }
            return componentName;
        }

        public boolean showSessionForActiveService(android.os.Bundle args, int sourceFlags, java.lang.String attributionTag, com.android.internal.app.IVoiceInteractionSessionShowCallback showCallback, android.os.IBinder activityToken) {
            super.showSessionForActiveService_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "showSessionForActiveService without running voice interactionservice");
                    return false;
                }
                if (this.mTemporarilyDisabled) {
                    android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "showSessionForActiveService(): ignored while temporarily disabled");
                    return false;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.cancelHotwordTriggerToUiLatencySession(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext);
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mOplusCustomizeVoiceInteractionManagerExt.beforeShowSession(args);
                    return this.mImpl.showSessionLocked(args, sourceFlags | 1 | 2, attributionTag, showCallback, activityToken);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void hideCurrentSession() throws android.os.RemoteException {
            super.hideCurrentSession_enforcePermission();
            if (this.mImpl == null) {
                return;
            }
            long caller = android.os.Binder.clearCallingIdentity();
            try {
                if (this.mImpl.mActiveSession != null && this.mImpl.mActiveSession.mSession != null) {
                    try {
                        this.mImpl.mActiveSession.mSession.closeSystemDialogs();
                    } catch (android.os.RemoteException e) {
                        android.util.Log.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Failed to call closeSystemDialogs", e);
                    }
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(caller);
            }
        }

        public void launchVoiceAssistFromKeyguard() {
            super.launchVoiceAssistFromKeyguard_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "launchVoiceAssistFromKeyguard without running voice interactionservice");
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mOplusCustomizeVoiceInteractionManagerExt.beforeLaunchVoiceAssistFromKeyguard();
                    this.mImpl.launchVoiceAssistFromKeyguard();
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public boolean isSessionRunning() {
            boolean z;
            super.isSessionRunning_enforcePermission();
            synchronized (this) {
                z = (this.mImpl == null || this.mImpl.mActiveSession == null) ? false : true;
            }
            return z;
        }

        public boolean activeServiceSupportsAssist() {
            boolean z;
            super.activeServiceSupportsAssist_enforcePermission();
            synchronized (this) {
                z = (this.mImpl == null || this.mImpl.mInfo == null || !this.mImpl.mInfo.getSupportsAssist()) ? false : true;
            }
            return z;
        }

        public boolean activeServiceSupportsLaunchFromKeyguard() throws android.os.RemoteException {
            boolean z;
            super.activeServiceSupportsLaunchFromKeyguard_enforcePermission();
            synchronized (this) {
                z = (this.mImpl == null || this.mImpl.mInfo == null || !this.mImpl.mInfo.getSupportsLaunchFromKeyguard()) ? false : true;
            }
            return z;
        }

        public void onLockscreenShown() {
            super.onLockscreenShown_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    if (this.mImpl.mActiveSession != null && this.mImpl.mActiveSession.mSession != null) {
                        try {
                            this.mImpl.mActiveSession.mSession.onLockscreenShown();
                        } catch (android.os.RemoteException e) {
                            android.util.Log.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Failed to call onLockscreenShown", e);
                        }
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void registerVoiceInteractionSessionListener(com.android.internal.app.IVoiceInteractionSessionListener listener) {
            super.registerVoiceInteractionSessionListener_enforcePermission();
            synchronized (this) {
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.register(listener);
            }
        }

        public void getActiveServiceSupportedActions(java.util.List<java.lang.String> voiceActions, com.android.internal.app.IVoiceActionCheckCallback callback) {
            super.getActiveServiceSupportedActions_enforcePermission();
            synchronized (this) {
                if (this.mImpl == null) {
                    try {
                        callback.onComplete((java.util.List) null);
                    } catch (android.os.RemoteException e) {
                    }
                    return;
                }
                long caller = android.os.Binder.clearCallingIdentity();
                try {
                    this.mImpl.getActiveServiceSupportedActions(voiceActions, callback);
                } finally {
                    android.os.Binder.restoreCallingIdentity(caller);
                }
            }
        }

        public void onSessionShown() {
            synchronized (this) {
                int size = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.beginBroadcast();
                for (int i = 0; i < size; i++) {
                    com.android.internal.app.IVoiceInteractionSessionListener listener = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.getBroadcastItem(i);
                    try {
                        listener.onVoiceSessionShown();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Error delivering voice interaction open event.", e);
                    }
                }
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.finishBroadcast();
            }
        }

        public void onSessionHidden() {
            synchronized (this) {
                int size = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.beginBroadcast();
                for (int i = 0; i < size; i++) {
                    com.android.internal.app.IVoiceInteractionSessionListener listener = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.getBroadcastItem(i);
                    try {
                        listener.onVoiceSessionHidden();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Error delivering voice interaction closed event.", e);
                    }
                }
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.finishBroadcast();
            }
        }

        public void setSessionWindowVisible(android.os.IBinder token, final boolean visible) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setSessionWindowVisible called without running voice interaction service");
                    return;
                }
                if (this.mImpl.mActiveSession != null && token == this.mImpl.mActiveSession.mToken) {
                    long caller = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerService$VoiceInteractionManagerServiceStub$$ExternalSyntheticLambda1
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.lambda$setSessionWindowVisible$7(visible, (com.android.internal.app.IVoiceInteractionSessionListener) obj);
                            }
                        });
                        return;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(caller);
                    }
                }
                android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "setSessionWindowVisible does not match active session");
            }
        }

        static /* synthetic */ void lambda$setSessionWindowVisible$7(boolean visible, com.android.internal.app.IVoiceInteractionSessionListener listener) {
            try {
                listener.onVoiceSessionWindowVisibilityChanged(visible);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Error delivering window visibility event to listener.", e);
            }
        }

        public boolean getAccessibilityDetectionEnabled() {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "registerAccessibilityDetectionSettingsListener called without running voice interaction service");
                    return false;
                }
                return this.mImpl.getAccessibilityDetectionEnabled();
            }
        }

        public void registerAccessibilityDetectionSettingsListener(com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener listener) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "registerAccessibilityDetectionSettingsListener called without running voice interaction service");
                } else {
                    this.mImpl.registerAccessibilityDetectionSettingsListenerLocked(listener);
                }
            }
        }

        public void unregisterAccessibilityDetectionSettingsListener(com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener listener) {
            synchronized (this) {
                if (this.mImpl == null) {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "unregisterAccessibilityDetectionSettingsListener called without running voice interaction service");
                } else {
                    this.mImpl.unregisterAccessibilityDetectionSettingsListenerLocked(listener);
                }
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext, com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, pw)) {
                synchronized (this) {
                    pw.println("VOICE INTERACTION MANAGER (dumpsys voiceinteraction)");
                    pw.println("  mEnableService: " + this.mEnableService);
                    pw.println("  mTemporarilyDisabled: " + this.mTemporarilyDisabled);
                    pw.println("  mCurUser: " + this.mCurUser);
                    pw.println("  mCurUserSupported: " + this.mCurUserSupported);
                    pw.println("  mIsHdsRequired: " + this.IS_HDS_REQUIRED);
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.this.dumpSupportedUsers(pw, "  ");
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDbHelper.dump(pw);
                    if (this.mImpl == null) {
                        pw.println("  (No active implementation)");
                    } else {
                        this.mImpl.dumpLocked(fd, pw, args);
                    }
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.voiceinteraction.VoiceInteractionManagerServiceShellCommand(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mServiceStub).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public void setUiHints(android.os.Bundle hints) {
            synchronized (this) {
                enforceIsCurrentVoiceInteractionService();
                int size = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.beginBroadcast();
                for (int i = 0; i < size; i++) {
                    com.android.internal.app.IVoiceInteractionSessionListener listener = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.getBroadcastItem(i);
                    try {
                        listener.onSetUiHints(hints);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "Error delivering UI hints.", e);
                    }
                }
                com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mVoiceInteractionSessionListeners.finishBroadcast();
            }
        }

        private boolean isCallerHoldingPermission(java.lang.String permission) {
            return com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.checkCallingOrSelfPermission(permission) == 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enforceCallingPermission(java.lang.String permission) {
            if (!isCallerHoldingPermission(permission)) {
                throw new java.lang.SecurityException("Caller does not hold the permission " + permission);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enforceIsCurrentVoiceInteractionService() {
            if (!isCallerCurrentVoiceInteractionService()) {
                throw new java.lang.SecurityException("Caller is not the current voice interaction service");
            }
        }

        private void enforceIsCallerPreinstalledAssistant() {
            if (!isCallerPreinstalledAssistant()) {
                throw new java.lang.SecurityException("Caller is not the pre-installed assistant.");
            }
        }

        private void enforceCallerAllowedToEnrollVoiceModel() {
            if (isCallerHoldingPermission("android.permission.KEYPHRASE_ENROLLMENT_APPLICATION")) {
                return;
            }
            enforceCallingPermission("android.permission.MANAGE_VOICE_KEYPHRASES");
            enforceIsCurrentVoiceInteractionService();
        }

        private boolean isCallerCurrentVoiceInteractionService() {
            return this.mImpl != null && this.mImpl.mInfo.getServiceInfo().applicationInfo.uid == android.os.Binder.getCallingUid();
        }

        private boolean isCallerPreinstalledAssistant() {
            return this.mImpl != null && this.mImpl.getApplicationInfo().uid == android.os.Binder.getCallingUid() && (this.mImpl.getApplicationInfo().isSystemApp() || this.mImpl.getApplicationInfo().isUpdatedSystemApp());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setImplLocked(com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl impl) {
            this.mImpl = impl;
            com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.notifyActiveVoiceInteractionServiceChanged(getActiveServiceComponentName());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.hardware.soundtrigger.IRecognitionStatusCallback createSoundTriggerCallbackLocked(com.android.internal.app.IHotwordRecognitionStatusCallback callback, android.media.permission.Identity voiceInteractorIdentity) {
            if (this.mImpl == null) {
                return null;
            }
            return this.mImpl.createSoundTriggerCallbackLocked(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext, callback, voiceInteractorIdentity);
        }

        class RoleObserver implements android.app.role.OnRoleHoldersChangedListener {
            private android.content.pm.PackageManager mPm;
            private android.app.role.RoleManager mRm;

            RoleObserver(java.util.concurrent.Executor executor) {
                this.mPm = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager();
                this.mRm = (android.app.role.RoleManager) com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getSystemService(android.app.role.RoleManager.class);
                this.mRm.addOnRoleHoldersChangedListenerAsUser(executor, this, android.os.UserHandle.ALL);
                if (this.mRm.isRoleAvailable("android.app.role.ASSISTANT")) {
                    android.os.UserHandle currentUser = android.os.UserHandle.of(((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentUserId());
                    onRoleHoldersChanged("android.app.role.ASSISTANT", currentUser);
                }
            }

            public void onRoleHoldersChanged(java.lang.String roleName, android.os.UserHandle user) {
                android.content.pm.UserInfo userInfo;
                if (!roleName.equals("android.app.role.ASSISTANT")) {
                    return;
                }
                java.util.List<java.lang.String> roleHolders = this.mRm.getRoleHoldersAsUser(roleName, user);
                if (roleHolders.isEmpty() && (userInfo = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mUserManagerInternal.getUserInfo(user.getIdentifier())) != null && userInfo.preCreated) {
                    com.android.server.utils.Slogf.d(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "onRoleHoldersChanged(): ignoring pre-created user %s for now, this method will be called again when it's converted to a real user", userInfo.toFullString());
                    return;
                }
                int userId = user.getIdentifier();
                if (roleHolders.isEmpty()) {
                    android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.getContext().getContentResolver(), "assistant", "", userId);
                    android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.getContext().getContentResolver(), "voice_interaction_service", "", userId);
                    return;
                }
                java.lang.String pkg = roleHolders.get(0);
                for (android.content.pm.ResolveInfo resolveInfo : com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.queryInteractorServices(userId, pkg)) {
                    android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                    android.service.voice.VoiceInteractionServiceInfo voiceInteractionServiceInfo = new android.service.voice.VoiceInteractionServiceInfo(this.mPm, serviceInfo);
                    if (voiceInteractionServiceInfo.getSupportsAssist()) {
                        java.lang.String serviceComponentName = serviceInfo.getComponentName().flattenToShortString();
                        if (voiceInteractionServiceInfo.getRecognitionService() == null) {
                            android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "The RecognitionService must be set to avoid boot loop on earlier platform version. Also make sure that this is a valid RecognitionService when running on Android 11 or earlier.");
                            serviceComponentName = "";
                        }
                        android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.getContext().getContentResolver(), "assistant", serviceComponentName, userId);
                        android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.getContext().getContentResolver(), "voice_interaction_service", serviceComponentName, userId);
                        return;
                    }
                }
                java.util.List<android.content.pm.ResolveInfo> activities = this.mPm.queryIntentActivitiesAsUser(new android.content.Intent("android.intent.action.ASSIST").setPackage(pkg), 851968, userId);
                java.util.Iterator<android.content.pm.ResolveInfo> it = activities.iterator();
                if (it.hasNext()) {
                    android.content.pm.ResolveInfo resolveInfo2 = it.next();
                    android.content.pm.ActivityInfo activityInfo = resolveInfo2.activityInfo;
                    android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.getContext().getContentResolver(), "assistant", activityInfo.getComponentName().flattenToShortString(), userId);
                    android.provider.Settings.Secure.putStringForUser(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.getContext().getContentResolver(), "voice_interaction_service", "", userId);
                }
            }
        }

        class SettingsObserver extends android.database.ContentObserver {
            SettingsObserver(android.os.Handler handler) {
                super(handler);
                android.content.ContentResolver resolver = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getContentResolver();
                resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("voice_interaction_service"), false, this, -1);
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this) {
                    com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub.this.switchImplementationIfNeededLocked(false);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resetServicesIfNoRecognitionService(android.content.ComponentName serviceComponent, int userHandle) {
            for (android.content.pm.ResolveInfo resolveInfo : queryInteractorServices(userHandle, serviceComponent.getPackageName())) {
                android.service.voice.VoiceInteractionServiceInfo serviceInfo = new android.service.voice.VoiceInteractionServiceInfo(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager(), resolveInfo.serviceInfo);
                if (serviceInfo.getSupportsAssist() && serviceInfo.getRecognitionService() == null) {
                    android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerService.TAG, "The RecognitionService must be set to avoid boot loop on earlier platform version. Also make sure that this is a valid RecognitionService when running on Android 11 or earlier.");
                    setCurInteractor(null, userHandle);
                    resetCurAssistant(userHandle);
                }
            }
        }

        private android.content.Intent getContextualSearchIntent(android.os.Bundle args) {
            java.lang.String csPkgName = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getResources().getString(android.R.string.config_defaultListenerAccessPackages);
            if (csPkgName.isEmpty()) {
                return null;
            }
            android.content.Intent launchIntent = new android.content.Intent(com.android.server.voiceinteraction.VoiceInteractionManagerService.CS_INTENT_FILTER);
            launchIntent.setPackage(csPkgName);
            android.content.pm.ResolveInfo resolveInfo = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageManager().resolveActivity(launchIntent, 2097152);
            if (resolveInfo == null) {
                return null;
            }
            launchIntent.setComponent(resolveInfo.getComponentInfo().getComponentName());
            launchIntent.addFlags(268795904);
            launchIntent.putExtras(args);
            boolean isAssistDataAllowed = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.isAssistDataAllowed();
            java.util.List<com.android.server.wm.ActivityAssistInfo> records = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.getTopVisibleActivities();
            java.util.ArrayList<java.lang.String> visiblePackageNames = new java.util.ArrayList<>();
            boolean isManagedProfileVisible = false;
            for (com.android.server.wm.ActivityAssistInfo record : records) {
                if (isAssistDataAllowed) {
                    visiblePackageNames.add(record.getComponentName().getPackageName());
                }
                if (com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDpmInternal != null && com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mDpmInternal.isUserOrganizationManaged(record.getUserId())) {
                    isManagedProfileVisible = true;
                }
            }
            android.window.ScreenCapture.ScreenshotHardwareBuffer shb = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mWmInternal.takeAssistScreenshot(java.util.Set.of());
            android.graphics.Bitmap bm = shb != null ? shb.asBitmap() : null;
            if (bm != null) {
                launchIntent.putExtra(com.android.server.voiceinteraction.VoiceInteractionManagerService.CS_KEY_FLAG_SECURE_FOUND, shb.containsSecureLayers());
                if (isAssistDataAllowed) {
                    launchIntent.putExtra(com.android.server.voiceinteraction.VoiceInteractionManagerService.CS_KEY_FLAG_SCREENSHOT, bm.asShared());
                }
            }
            launchIntent.putExtra(com.android.server.voiceinteraction.VoiceInteractionManagerService.CS_KEY_FLAG_IS_MANAGED_PROFILE_VISIBLE, isManagedProfileVisible);
            if (isAssistDataAllowed) {
                launchIntent.putExtra(com.android.server.voiceinteraction.VoiceInteractionManagerService.CS_KEY_FLAG_VISIBLE_PACKAGE_NAMES, visiblePackageNames);
            }
            return launchIntent;
        }

        private boolean startContextualSearch(android.content.Intent launchIntent) {
            android.app.ActivityOptions opts = android.app.ActivityOptions.makeCustomTaskAnimation(com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext, 0, 0, null, null, null);
            opts.setDisableStartingWindow(true);
            int resultCode = com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mAtmInternal.startActivityWithScreenshot(launchIntent, com.android.server.voiceinteraction.VoiceInteractionManagerService.this.mContext.getPackageName(), android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid(), null, opts.toBundle(), android.os.Binder.getCallingUserHandle().getIdentifier());
            return resultCode == 0;
        }
    }
}
