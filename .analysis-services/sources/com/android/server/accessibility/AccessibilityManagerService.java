package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityManagerService extends android.view.accessibility.IAccessibilityManager.Stub implements com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport, com.android.server.accessibility.AccessibilityUserState.ServiceInfoChangeListener, com.android.server.accessibility.AccessibilityWindowManager.AccessibilityEventSender, com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager, com.android.server.accessibility.SystemActionPerformer.SystemActionsChangedListener, com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack, com.android.server.accessibility.ProxyManager.SystemSupport {
    static final java.lang.String ACTION_LAUNCH_HEARING_DEVICES_DIALOG = "com.android.systemui.action.LAUNCH_HEARING_DEVICES_DIALOG";
    private static final char COMPONENT_NAME_SEPARATOR = ':';
    private static final boolean DEBUG;
    private static final java.lang.String FUNCTION_REGISTER_UI_TEST_AUTOMATION_SERVICE = "registerUiTestAutomationService";
    private static final java.lang.String GET_WINDOW_TOKEN = "getWindowToken";
    public static final int INVALID_SERVICE_ID = -1;
    private static final java.lang.String LOG_TAG = "AccessibilityManagerService";
    public static final int MAGNIFICATION_GESTURE_HANDLER_ID = 0;
    static final java.lang.String METRIC_ID_QS_SHORTCUT_ADD = "accessibility.value_qs_shortcut_add";
    static final java.lang.String METRIC_ID_QS_SHORTCUT_REMOVE = "accessibility.value_qs_shortcut_remove";
    private static final int OWN_PROCESS_ID;
    private static final int POSTPONE_WINDOW_STATE_CHANGED_EVENT_TIMEOUT_MILLIS = 500;
    private static final java.lang.String SET_PIP_ACTION_REPLACEMENT = "setPictureInPictureActionReplacingConnection";
    private static final int WAIT_FOR_USER_STATE_FULLY_INITIALIZED_MILLIS = 3000;
    private static final int WAIT_INPUT_FILTER_INSTALL_TIMEOUT_MS = 1000;
    private static int sIdCounter;
    private final com.android.server.accessibility.AccessibilityManagerService.AccessibilityDisplayListener mA11yDisplayListener;
    private android.util.SparseArray<android.view.SurfaceControl> mA11yOverlayLayers;
    private final com.android.server.accessibility.AccessibilityWindowManager mA11yWindowManager;
    private final com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerService;
    private final com.android.server.accessibility.CaptioningManagerImpl mCaptioningManagerImpl;
    private final android.content.Context mContext;
    private int mCurrentUserId;
    android.view.inputmethod.EditorInfo mEditorInfo;
    private android.app.AlertDialog mEnableTouchExplorationDialog;
    private com.android.server.accessibility.FingerprintGestureDispatcher mFingerprintGestureDispatcher;
    private final com.android.server.accessibility.FlashNotificationsController mFlashNotificationsController;
    private final android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> mGlobalClients;
    private boolean mHasInputFilter;
    private boolean mInitialized;
    private boolean mInputBound;
    private com.android.server.accessibility.AccessibilityInputFilter mInputFilter;
    private boolean mInputFilterInstalled;
    boolean mInputSessionRequested;
    private com.android.server.accessibility.AccessibilityManagerService.InteractionBridge mInteractionBridge;
    private boolean mIsAccessibilityButtonShown;
    private com.android.server.accessibility.KeyEventDispatcher mKeyEventDispatcher;
    private final java.lang.Object mLock;
    private final com.android.server.accessibility.magnification.MagnificationController mMagnificationController;
    private final com.android.server.accessibility.magnification.MagnificationProcessor mMagnificationProcessor;
    private final android.os.Handler mMainHandler;
    private android.util.SparseArray<com.android.server.accessibility.MotionEventInjector> mMotionEventInjectors;
    private final android.content.pm.PackageManager mPackageManager;
    private com.android.internal.content.PackageMonitor mPackageMonitor;
    private final android.os.PowerManager mPowerManager;
    private final com.android.server.accessibility.ProxyManager mProxyManager;
    private int mRealCurrentUserId;
    com.android.internal.inputmethod.IRemoteAccessibilityInputConnection mRemoteInputConnection;
    boolean mRestarting;
    private final com.android.server.accessibility.AccessibilitySecurityPolicy mSecurityPolicy;
    private final java.util.List<com.android.server.accessibility.AccessibilityManagerService.SendWindowStateChangedEventRunnable> mSendWindowStateChangedEventRunnables;
    private com.android.server.accessibility.IAccessibilityManagerServiceExt mServiceExt;
    private final android.text.TextUtils.SimpleStringSplitter mStringColonSplitter;
    private com.android.server.accessibility.SystemActionPerformer mSystemActionPerformer;
    private final java.util.Set<android.content.ComponentName> mTempComponentNameSet;
    private final android.util.IntArray mTempIntArray;
    private android.graphics.Point mTempPoint;
    private final android.graphics.Rect mTempRect;
    private final android.graphics.Rect mTempRect1;
    private final com.android.server.accessibility.AccessibilityTraceManager mTraceManager;
    private final com.android.server.accessibility.UiAutomationManager mUiAutomationManager;
    private final com.android.server.pm.UserManagerInternal mUmi;
    final android.util.SparseArray<com.android.server.accessibility.AccessibilityUserState> mUserStates;
    private final android.util.SparseBooleanArray mVisibleBgUserIds;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerService;

    static {
        DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false) || android.os.SystemProperties.getBoolean("persist.sys.alwayson.enable", false);
        OWN_PROCESS_ID = android.os.Process.myPid();
        sIdCounter = 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accessibility.AccessibilityUserState getCurrentUserStateLocked() {
        return getUserStateLocked(this.mCurrentUserId);
    }

    public void changeMagnificationMode(int displayId, int magnificationMode) {
        synchronized (this.mLock) {
            if (displayId == 0) {
                persistMagnificationModeSettingsLocked(magnificationMode);
            } else {
                com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
                int currentMode = userState.getMagnificationModeLocked(displayId);
                if (magnificationMode != currentMode) {
                    userState.setMagnificationModeLocked(displayId, magnificationMode);
                    updateMagnificationModeChangeSettingsLocked(userState, displayId);
                }
            }
        }
    }

    private static final class LocalServiceImpl extends com.android.server.AccessibilityManagerInternal {
        private final com.android.server.accessibility.AccessibilityManagerService mService;

        LocalServiceImpl(com.android.server.accessibility.AccessibilityManagerService service) {
            this.mService = service;
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void setImeSessionEnabled(android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> sessions, boolean enabled) {
            this.mService.scheduleSetImeSessionEnabled(sessions, enabled);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void unbindInput() {
            this.mService.scheduleUnbindInput();
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void bindInput() {
            this.mService.scheduleBindInput();
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void createImeSession(android.util.ArraySet<java.lang.Integer> ignoreSet) {
            this.mService.scheduleCreateImeSession(ignoreSet);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void startInput(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, android.view.inputmethod.EditorInfo editorInfo, boolean restarting) {
            this.mService.scheduleStartInput(remoteAccessibilityInputConnection, editorInfo, restarting);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public void performSystemAction(int actionId) {
            this.mService.getSystemActionPerformer().performSystemAction(actionId);
        }

        @Override // com.android.server.AccessibilityManagerInternal
        public boolean isTouchExplorationEnabled(int userId) {
            boolean zIsTouchExplorationEnabledLocked;
            synchronized (this.mService.mLock) {
                zIsTouchExplorationEnabledLocked = this.mService.getUserStateLocked(userId).isTouchExplorationEnabledLocked();
            }
            return zIsTouchExplorationEnabledLocked;
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.accessibility.AccessibilityManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mService = new com.android.server.accessibility.AccessibilityManagerService(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            com.android.server.LocalServices.addService(com.android.server.AccessibilityManagerInternal.class, new com.android.server.accessibility.AccessibilityManagerService.LocalServiceImpl(this.mService));
            publishBinderService("accessibility", this.mService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            this.mService.onBootPhase(phase);
        }
    }

    AccessibilityManagerService(android.content.Context context, android.os.Handler handler, android.content.pm.PackageManager packageManager, com.android.server.accessibility.AccessibilitySecurityPolicy securityPolicy, com.android.server.accessibility.SystemActionPerformer systemActionPerformer, com.android.server.accessibility.AccessibilityWindowManager a11yWindowManager, com.android.server.accessibility.AccessibilityManagerService.AccessibilityDisplayListener a11yDisplayListener, com.android.server.accessibility.magnification.MagnificationController magnificationController, com.android.server.accessibility.AccessibilityInputFilter inputFilter, com.android.server.accessibility.ProxyManager proxyManager, android.os.PermissionEnforcer permissionEnforcer) {
        super(permissionEnforcer);
        this.mLock = new java.lang.Object();
        this.mStringColonSplitter = new android.text.TextUtils.SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
        this.mTempRect = new android.graphics.Rect();
        this.mTempRect1 = new android.graphics.Rect();
        this.mTempComponentNameSet = new java.util.HashSet();
        this.mTempIntArray = new android.util.IntArray(0);
        this.mGlobalClients = new android.os.RemoteCallbackList<>();
        this.mUserStates = new android.util.SparseArray<>();
        this.mUiAutomationManager = new com.android.server.accessibility.UiAutomationManager(this.mLock);
        this.mSendWindowStateChangedEventRunnables = new java.util.ArrayList();
        this.mCurrentUserId = 0;
        this.mRealCurrentUserId = -2;
        this.mServiceExt = (com.android.server.accessibility.IAccessibilityManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.accessibility.IAccessibilityManagerServiceExt.class).base(this).create();
        this.mTempPoint = new android.graphics.Point();
        this.mA11yOverlayLayers = new android.util.SparseArray<>();
        this.mContext = context;
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService("power");
        this.mWindowManagerService = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mTraceManager = com.android.server.accessibility.AccessibilityTraceManager.getInstance(this.mWindowManagerService.getAccessibilityController(), this, this.mLock);
        this.mMainHandler = handler;
        this.mActivityTaskManagerService = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mPackageManager = packageManager;
        this.mSecurityPolicy = securityPolicy;
        this.mSystemActionPerformer = systemActionPerformer;
        this.mA11yWindowManager = a11yWindowManager;
        this.mA11yDisplayListener = a11yDisplayListener;
        this.mMagnificationController = magnificationController;
        this.mMagnificationProcessor = new com.android.server.accessibility.magnification.MagnificationProcessor(this.mMagnificationController);
        this.mCaptioningManagerImpl = new com.android.server.accessibility.CaptioningManagerImpl(this.mContext);
        this.mProxyManager = proxyManager;
        if (inputFilter != null) {
            this.mInputFilter = inputFilter;
            this.mHasInputFilter = true;
        }
        this.mFlashNotificationsController = new com.android.server.accessibility.FlashNotificationsController(this.mContext);
        this.mUmi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mVisibleBgUserIds = null;
        init();
    }

    public AccessibilityManagerService(android.content.Context context) {
        super(android.os.PermissionEnforcer.fromContext(context));
        this.mLock = new java.lang.Object();
        this.mStringColonSplitter = new android.text.TextUtils.SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
        this.mTempRect = new android.graphics.Rect();
        this.mTempRect1 = new android.graphics.Rect();
        this.mTempComponentNameSet = new java.util.HashSet();
        this.mTempIntArray = new android.util.IntArray(0);
        this.mGlobalClients = new android.os.RemoteCallbackList<>();
        this.mUserStates = new android.util.SparseArray<>();
        this.mUiAutomationManager = new com.android.server.accessibility.UiAutomationManager(this.mLock);
        this.mSendWindowStateChangedEventRunnables = new java.util.ArrayList();
        this.mCurrentUserId = 0;
        this.mRealCurrentUserId = -2;
        this.mServiceExt = (com.android.server.accessibility.IAccessibilityManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.accessibility.IAccessibilityManagerServiceExt.class).base(this).create();
        this.mTempPoint = new android.graphics.Point();
        this.mA11yOverlayLayers = new android.util.SparseArray<>();
        this.mContext = context;
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mWindowManagerService = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mTraceManager = com.android.server.accessibility.AccessibilityTraceManager.getInstance(this.mWindowManagerService.getAccessibilityController(), this, this.mLock);
        this.mMainHandler = new com.android.server.accessibility.AccessibilityManagerService.MainHandler(this.mContext.getMainLooper());
        this.mActivityTaskManagerService = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mPackageManager = this.mContext.getPackageManager();
        com.android.server.accessibility.PolicyWarningUIController policyWarningUIController = new com.android.server.accessibility.PolicyWarningUIController(this.mMainHandler, context, new com.android.server.accessibility.PolicyWarningUIController.NotificationController(context));
        this.mSecurityPolicy = new com.android.server.accessibility.AccessibilitySecurityPolicy(policyWarningUIController, this.mContext, this, (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class));
        this.mA11yWindowManager = new com.android.server.accessibility.AccessibilityWindowManager(this.mLock, this.mMainHandler, this.mWindowManagerService, this, this.mSecurityPolicy, this, this.mTraceManager);
        this.mA11yDisplayListener = new com.android.server.accessibility.AccessibilityManagerService.AccessibilityDisplayListener(this.mContext, this.mMainHandler);
        this.mMagnificationController = new com.android.server.accessibility.magnification.MagnificationController(this, this.mLock, this.mContext, new com.android.server.accessibility.magnification.MagnificationScaleProvider(this.mContext), java.util.concurrent.Executors.newSingleThreadExecutor());
        this.mMagnificationProcessor = new com.android.server.accessibility.magnification.MagnificationProcessor(this.mMagnificationController);
        this.mCaptioningManagerImpl = new com.android.server.accessibility.CaptioningManagerImpl(this.mContext);
        this.mProxyManager = new com.android.server.accessibility.ProxyManager(this.mLock, this.mA11yWindowManager, this.mContext, this.mMainHandler, this.mUiAutomationManager, this);
        this.mFlashNotificationsController = new com.android.server.accessibility.FlashNotificationsController(this.mContext);
        this.mUmi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        if (android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            this.mVisibleBgUserIds = new android.util.SparseBooleanArray();
            this.mUmi.addUserVisibilityListener(new com.android.server.pm.UserManagerInternal.UserVisibilityListener() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda45
                @Override // com.android.server.pm.UserManagerInternal.UserVisibilityListener
                public final void onUserVisibilityChanged(int i, boolean z) {
                    this.f$0.lambda$new$0(i, z);
                }
            });
        } else {
            this.mVisibleBgUserIds = null;
        }
        init();
    }

    private void init() {
        this.mSecurityPolicy.setAccessibilityWindowManager(this.mA11yWindowManager);
        registerBroadcastReceivers();
        new com.android.server.accessibility.AccessibilityManagerService.AccessibilityContentObserver(this.mMainHandler).register(this.mContext.getContentResolver());
        disableAccessibilityMenuToMigrateIfNeeded();
        this.mServiceExt.init(this.mContext);
    }

    boolean unsafeIsLockHeld() {
        return java.lang.Thread.holdsLock(this.mLock);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isServiceInitializedLocked() {
        return this.mInitialized;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport, com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager
    public int getCurrentUserIdLocked() {
        return this.mCurrentUserId;
    }

    @Override // com.android.server.accessibility.AccessibilitySecurityPolicy.AccessibilityUserManager
    public android.util.SparseBooleanArray getVisibleUserIdsLocked() {
        return this.mVisibleBgUserIds;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public boolean isAccessibilityButtonShown() {
        return this.mIsAccessibilityButtonShown;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public android.util.Pair<float[], android.view.MagnificationSpec> getWindowTransformationMatrixAndMagnificationSpec(int windowId) {
        android.view.WindowInfo windowInfo;
        android.os.IBinder token;
        synchronized (this.mLock) {
            windowInfo = this.mA11yWindowManager.findWindowInfoByIdLocked(windowId);
        }
        if (windowInfo != null) {
            android.view.MagnificationSpec spec = new android.view.MagnificationSpec();
            spec.setTo(windowInfo.mMagnificationSpec);
            return new android.util.Pair<>(windowInfo.mTransformMatrix, spec);
        }
        synchronized (this.mLock) {
            token = this.mA11yWindowManager.getWindowTokenForUserAndWindowIdLocked(this.mCurrentUserId, windowId);
        }
        android.util.Pair<android.graphics.Matrix, android.view.MagnificationSpec> pair = this.mWindowManagerService.getWindowTransformationMatrixAndMagnificationSpec(token);
        float[] outTransformationMatrix = new float[9];
        android.graphics.Matrix tmpMatrix = (android.graphics.Matrix) pair.first;
        android.view.MagnificationSpec spec2 = (android.view.MagnificationSpec) pair.second;
        if (!spec2.isNop()) {
            tmpMatrix.postScale(spec2.scale, spec2.scale);
            tmpMatrix.postTranslate(spec2.offsetX, spec2.offsetY);
        }
        tmpMatrix.getValues(outTransformationMatrix);
        return new android.util.Pair<>(outTransformationMatrix, (android.view.MagnificationSpec) pair.second);
    }

    public android.view.accessibility.IAccessibilityManager.WindowTransformationSpec getWindowTransformationSpec(int windowId) {
        android.view.accessibility.IAccessibilityManager.WindowTransformationSpec windowTransformationSpec = new android.view.accessibility.IAccessibilityManager.WindowTransformationSpec();
        android.util.Pair<float[], android.view.MagnificationSpec> result = getWindowTransformationMatrixAndMagnificationSpec(windowId);
        windowTransformationSpec.transformationMatrix = (float[]) result.first;
        windowTransformationSpec.magnificationSpec = (android.view.MagnificationSpec) result.second;
        return windowTransformationSpec;
    }

    @Override // com.android.server.accessibility.AccessibilityUserState.ServiceInfoChangeListener
    public void onServiceInfoChangedLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        this.mSecurityPolicy.onBoundServicesChangedLocked(userState.mUserId, userState.mBoundServices);
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public com.android.server.accessibility.FingerprintGestureDispatcher getFingerprintGestureDispatcher() {
        return this.mFingerprintGestureDispatcher;
    }

    public void onInputFilterInstalled(boolean installed) {
        synchronized (this.mLock) {
            this.mInputFilterInstalled = installed;
            this.mLock.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBootPhase(int phase) {
        if (phase == 500 && this.mPackageManager.hasSystemFeature("android.software.app_widgets")) {
            this.mSecurityPolicy.setAppWidgetManager((android.appwidget.AppWidgetManagerInternal) com.android.server.LocalServices.getService(android.appwidget.AppWidgetManagerInternal.class));
        }
        if (phase == 600) {
            setNonA11yToolNotificationToMatchSafetyCenter();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNonA11yToolNotificationToMatchSafetyCenter() {
        boolean sendNotification = !((android.safetycenter.SafetyCenterManager) this.mContext.getSystemService(android.safetycenter.SafetyCenterManager.class)).isSafetyCenterEnabled();
        synchronized (this.mLock) {
            this.mSecurityPolicy.setSendingNonA11yToolNotificationLocked(sendNotification);
        }
    }

    java.lang.Object getLock() {
        return this.mLock;
    }

    com.android.server.accessibility.AccessibilityUserState getCurrentUserState() {
        com.android.server.accessibility.AccessibilityUserState currentUserStateLocked;
        synchronized (this.mLock) {
            currentUserStateLocked = getCurrentUserStateLocked();
        }
        return currentUserStateLocked;
    }

    private com.android.server.accessibility.AccessibilityUserState getUserState(int userId) {
        com.android.server.accessibility.AccessibilityUserState userStateLocked;
        synchronized (this.mLock) {
            userStateLocked = getUserStateLocked(userId);
        }
        return userStateLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accessibility.AccessibilityUserState getUserStateLocked(int userId) {
        com.android.server.accessibility.AccessibilityUserState state = this.mUserStates.get(userId);
        if (state == null) {
            com.android.server.accessibility.AccessibilityUserState state2 = new com.android.server.accessibility.AccessibilityUserState(userId, this.mContext, this);
            this.mUserStates.put(userId, state2);
            return state2;
        }
        return state;
    }

    boolean getBindInstantServiceAllowed(int userId) {
        boolean bindInstantServiceAllowedLocked;
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(userId);
            bindInstantServiceAllowedLocked = userState.getBindInstantServiceAllowedLocked();
        }
        return bindInstantServiceAllowedLocked;
    }

    void setBindInstantServiceAllowed(int userId, boolean allowed) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_BIND_INSTANT_SERVICE", "setBindInstantServiceAllowed");
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(userId);
            if (allowed != userState.getBindInstantServiceAllowedLocked()) {
                userState.setBindInstantServiceAllowedLocked(allowed);
                onUserStateChangedLocked(userState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSomePackagesChangedLocked(java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parsedAccessibilityServiceInfos, java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parsedAccessibilityShortcutInfos) {
        com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
        userState.mInstalledServices.clear();
        if (readConfigurationForUserStateLocked(userState, parsedAccessibilityServiceInfos, parsedAccessibilityShortcutInfos)) {
            onUserStateChangedLocked(userState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemovedLocked(final java.lang.String packageName) {
        com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserState();
        java.util.function.Predicate<android.content.ComponentName> filter = new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$onPackageRemovedLocked$1(packageName, (android.content.ComponentName) obj);
            }
        };
        userState.mBindingServices.removeIf(filter);
        userState.mCrashedServices.removeIf(filter);
        java.util.Iterator<android.content.ComponentName> it = userState.mEnabledServices.iterator();
        boolean anyServiceRemoved = false;
        while (it.hasNext()) {
            android.content.ComponentName comp = it.next();
            java.lang.String compPkg = comp.getPackageName();
            if (compPkg.equals(packageName)) {
                it.remove();
                userState.mTouchExplorationGrantedServices.remove(comp);
                anyServiceRemoved = true;
            }
        }
        if (anyServiceRemoved) {
            persistComponentNamesToSettingLocked("enabled_accessibility_services", userState.mEnabledServices, this.mCurrentUserId);
            persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", userState.mTouchExplorationGrantedServices, this.mCurrentUserId);
            onUserStateChangedLocked(userState);
        }
    }

    static /* synthetic */ boolean lambda$onPackageRemovedLocked$1(java.lang.String packageName, android.content.ComponentName component) {
        return component != null && component.getPackageName().equals(packageName);
    }

    boolean onPackagesForceStoppedLocked(java.lang.String[] packages, com.android.server.accessibility.AccessibilityUserState userState) {
        final java.util.Set<java.lang.String> packageSet = new java.util.HashSet<>(java.util.List.of((java.lang.Object[]) packages));
        final java.util.ArrayList<android.content.ComponentName> continuousServices = new java.util.ArrayList<>(userState.mInstalledServices.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda50
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$onPackagesForceStoppedLocked$2((android.accessibilityservice.AccessibilityServiceInfo) obj);
            }
        }).map(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda51()).toList());
        continuousServices.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda52
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$onPackagesForceStoppedLocked$3(packageSet, (android.content.ComponentName) obj);
            }
        });
        boolean enabledServicesChanged = false;
        java.util.Iterator<android.content.ComponentName> it = userState.mEnabledServices.iterator();
        while (it.hasNext()) {
            android.content.ComponentName comp = it.next();
            java.lang.String compPkg = comp.getPackageName();
            if (packageSet.contains(compPkg)) {
                it.remove();
                userState.getBindingServicesLocked().remove(comp);
                userState.getCrashedServicesLocked().remove(comp);
                enabledServicesChanged = true;
            }
        }
        if (enabledServicesChanged) {
            persistComponentNamesToSettingLocked("enabled_accessibility_services", userState.mEnabledServices, userState.mUserId);
        }
        android.util.ArraySet<java.lang.String> shortcutTargetsLocked = userState.getShortcutTargetsLocked(1);
        boolean buttonTargetsChanged = shortcutTargetsLocked.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda53
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return continuousServices.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda73
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj2) {
                        return ((android.content.ComponentName) obj2).flattenToString().equals(str);
                    }
                });
            }
        });
        if (buttonTargetsChanged) {
            userState.updateShortcutTargetsLocked(shortcutTargetsLocked, 1);
            persistColonDelimitedSetToSettingLocked(com.android.internal.accessibility.util.ShortcutUtils.convertToKey(1), userState.mUserId, shortcutTargetsLocked, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda54
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$onPackagesForceStoppedLocked$6((java.lang.String) obj);
                }
            });
        }
        return enabledServicesChanged || buttonTargetsChanged;
    }

    static /* synthetic */ boolean lambda$onPackagesForceStoppedLocked$2(android.accessibilityservice.AccessibilityServiceInfo service) {
        return (service.flags & 256) == 256;
    }

    static /* synthetic */ boolean lambda$onPackagesForceStoppedLocked$3(java.util.Set packageSet, android.content.ComponentName continuousName) {
        return !packageSet.contains(continuousName.getPackageName());
    }

    static /* synthetic */ java.lang.String lambda$onPackagesForceStoppedLocked$6(java.lang.String str) {
        return str;
    }

    com.android.internal.content.PackageMonitor getPackageMonitor() {
        return this.mPackageMonitor;
    }

    void setPackageMonitor(com.android.internal.content.PackageMonitor monitor) {
        this.mPackageMonitor = monitor;
    }

    private void registerBroadcastReceivers() {
        this.mPackageMonitor = new com.android.server.accessibility.AccessibilityManagerService.ManagerPackageMonitor(this);
        this.mServiceExt.hookPackageMonitorRegister(this.mContext, this.mPackageMonitor);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.os.action.SETTING_RESTORED");
        android.os.Handler receiverHandler = com.android.server.accessibility.Flags.managerAvoidReceiverTimeout() ? com.android.internal.os.BackgroundThread.getHandler() : null;
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.accessibility.AccessibilityManagerService.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:37:0x00db  */
            @Override // android.content.BroadcastReceiver
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onReceive(android.content.Context r8, android.content.Intent r9) {
                /*
                    Method dump skipped, instruction units count: 338
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.server.accessibility.AccessibilityManagerService.AnonymousClass1.onReceive(android.content.Context, android.content.Intent):void");
            }
        }, android.os.UserHandle.ALL, intentFilter, null, receiverHandler);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.safetycenter.action.SAFETY_CENTER_ENABLED_CHANGED");
        android.content.BroadcastReceiver receiver = new android.content.BroadcastReceiver() { // from class: com.android.server.accessibility.AccessibilityManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                com.android.server.accessibility.AccessibilityManagerService.this.setNonA11yToolNotificationToMatchSafetyCenter();
            }
        };
        this.mContext.registerReceiverAsUser(receiver, android.os.UserHandle.ALL, filter, null, this.mMainHandler, 2);
        if (!android.companion.virtual.flags.Flags.vdmPublicApis()) {
            android.content.BroadcastReceiver virtualDeviceReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.accessibility.AccessibilityManagerService.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    int deviceId = intent.getIntExtra("android.companion.virtual.extra.VIRTUAL_DEVICE_ID", 0);
                    com.android.server.accessibility.AccessibilityManagerService.this.mProxyManager.clearConnections(deviceId);
                }
            };
            android.content.IntentFilter virtualDeviceFilter = new android.content.IntentFilter("android.companion.virtual.action.VIRTUAL_DEVICE_REMOVED");
            this.mContext.registerReceiver(virtualDeviceReceiver, virtualDeviceFilter, 4);
        }
    }

    private void disableAccessibilityMenuToMigrateIfNeeded() {
        int userId;
        synchronized (this.mLock) {
            userId = this.mCurrentUserId;
        }
        android.content.ComponentName menuToMigrate = com.android.internal.accessibility.util.AccessibilityUtils.getAccessibilityMenuComponentToMigrate(this.mPackageManager, userId);
        if (menuToMigrate != null) {
            android.content.pm.PackageManager userPackageManager = this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager();
            userPackageManager.setComponentEnabledSetting(menuToMigrate, 2, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreLegacyDisplayMagnificationNavBarIfNeededLocked(java.lang.String newSetting, int restoreFromSdkInt) {
        if (restoreFromSdkInt >= 30) {
            return;
        }
        try {
            boolean displayMagnificationNavBarEnabled = java.lang.Integer.parseInt(newSetting) == 1;
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(0);
            android.util.ArraySet arraySet = new android.util.ArraySet();
            readColonDelimitedSettingToSet("accessibility_button_targets", userState.mUserId, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda10
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$7((java.lang.String) obj);
                }
            }, arraySet);
            boolean targetsContainMagnification = arraySet.contains("com.android.server.accessibility.MagnificationController");
            if (targetsContainMagnification == displayMagnificationNavBarEnabled) {
                return;
            }
            if (displayMagnificationNavBarEnabled) {
                arraySet.add("com.android.server.accessibility.MagnificationController");
            } else {
                arraySet.remove("com.android.server.accessibility.MagnificationController");
            }
            persistColonDelimitedSetToSettingLocked("accessibility_button_targets", userState.mUserId, arraySet, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda11
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$8((java.lang.String) obj);
                }
            });
            readAccessibilityButtonTargetsLocked(userState);
            onUserStateChangedLocked(userState);
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.w(LOG_TAG, "number format is incorrect" + e);
        }
    }

    static /* synthetic */ java.lang.String lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$7(java.lang.String str) {
        return str;
    }

    static /* synthetic */ java.lang.String lambda$restoreLegacyDisplayMagnificationNavBarIfNeededLocked$8(java.lang.String str) {
        return str;
    }

    public long addClient(android.view.accessibility.IAccessibilityManagerClient callback, int userId) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.addClient", 4L, "callback=" + callback + ";userId=" + userId);
        }
        this.mServiceExt.addProxyBinder(callback.asBinder(), callback, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
        synchronized (this.mLock) {
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(resolvedUserId);
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            com.android.server.accessibility.AccessibilityManagerService.Client client = new com.android.server.accessibility.AccessibilityManagerService.Client(callback, android.os.Binder.getCallingUid(), userState, deviceId);
            if (this.mSecurityPolicy.isCallerInteractingAcrossUsers(userId)) {
                if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                    if (DEBUG) {
                        android.util.Slog.v(LOG_TAG, "Added global client for proxy-ed pid: " + android.os.Binder.getCallingPid() + " for device id " + deviceId + " with package names " + java.util.Arrays.toString(client.mPackageNames));
                    }
                    return com.android.internal.util.IntPair.of(this.mProxyManager.getStateLocked(deviceId), client.mLastSentRelevantEventTypes);
                }
                this.mGlobalClients.register(callback, client);
                if (DEBUG) {
                    android.util.Slog.i(LOG_TAG, "Added global client for pid:" + android.os.Binder.getCallingPid());
                }
            } else {
                if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                    if (DEBUG) {
                        android.util.Slog.v(LOG_TAG, "Added user client for proxy-ed pid: " + android.os.Binder.getCallingPid() + " for device id " + deviceId + " with package names " + java.util.Arrays.toString(client.mPackageNames));
                    }
                    return com.android.internal.util.IntPair.of(this.mProxyManager.getStateLocked(deviceId), client.mLastSentRelevantEventTypes);
                }
                userState.mUserClients.register(callback, client);
                if (DEBUG) {
                    android.util.Slog.i(LOG_TAG, "Added user client for pid:" + android.os.Binder.getCallingPid() + " and userId:" + this.mCurrentUserId);
                }
            }
            return com.android.internal.util.IntPair.of(resolvedUserId == this.mCurrentUserId ? getClientStateLocked(userState) : 0, client.mLastSentRelevantEventTypes);
        }
    }

    public boolean removeClient(android.view.accessibility.IAccessibilityManagerClient callback, int userId) {
        this.mServiceExt.removeProxyBinder(callback.asBinder(), callback);
        synchronized (this.mLock) {
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(resolvedUserId);
            if (this.mSecurityPolicy.isCallerInteractingAcrossUsers(userId)) {
                boolean unregistered = this.mGlobalClients.unregister(callback);
                if (DEBUG) {
                    android.util.Slog.i(LOG_TAG, "Removed global client for pid:" + android.os.Binder.getCallingPid() + "state: " + unregistered);
                }
                return unregistered;
            }
            boolean unregistered2 = userState.mUserClients.unregister(callback);
            if (DEBUG) {
                android.util.Slog.i(LOG_TAG, "Removed user client for pid:" + android.os.Binder.getCallingPid() + " and userId:" + resolvedUserId + "state: " + unregistered2);
            }
            return unregistered2;
        }
    }

    public void sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent event, int userId) {
        int resolvedUserId;
        android.view.accessibility.AccessibilityWindowInfo pip;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.sendAccessibilityEvent", 4L, "event=" + event + ";userId=" + userId);
        }
        boolean dispatchEvent = false;
        synchronized (this.mLock) {
            if (event.getWindowId() == -3 && (pip = this.mA11yWindowManager.getPictureInPictureWindowLocked()) != null) {
                int pipId = pip.getId();
                event.setWindowId(pipId);
            }
            resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            event.setPackageName(this.mSecurityPolicy.resolveValidReportedPackageLocked(event.getPackageName(), android.os.UserHandle.getCallingAppId(), resolvedUserId, getCallingPid()));
            if (resolvedUserId == this.mCurrentUserId) {
                if (this.mSecurityPolicy.canDispatchAccessibilityEventLocked(this.mCurrentUserId, event)) {
                    this.mA11yWindowManager.updateActiveAndAccessibilityFocusedWindowLocked(this.mCurrentUserId, event.getWindowId(), event.getSourceNodeId(), event.getEventType(), event.getAction());
                    this.mSecurityPolicy.updateEventSourceLocked(event);
                    dispatchEvent = true;
                }
                if (this.mHasInputFilter && this.mInputFilter != null) {
                    this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda4
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.accessibility.AccessibilityManagerService) obj).sendAccessibilityEventToInputFilter((android.view.accessibility.AccessibilityEvent) obj2);
                        }
                    }, this, android.view.accessibility.AccessibilityEvent.obtain(event)));
                }
            }
        }
        if (dispatchEvent) {
            boolean shouldComputeWindows = false;
            int displayId = event.getDisplayId();
            int windowId = event.getWindowId();
            if (windowId != -1 && displayId == -1) {
                displayId = this.mA11yWindowManager.getDisplayIdByUserIdAndWindowId(resolvedUserId, windowId);
                event.setDisplayId(displayId);
            }
            synchronized (this.mLock) {
                if (event.getEventType() == 32 && displayId != -1 && this.mA11yWindowManager.isTrackingWindowsLocked(displayId)) {
                    shouldComputeWindows = true;
                }
            }
            if (shouldComputeWindows) {
                if (this.mTraceManager.isA11yTracingEnabledForTypes(512L)) {
                    this.mTraceManager.logTrace("WindowManagerInternal.computeWindowsForAccessibility", 512L, "display=" + displayId);
                }
                com.android.server.wm.WindowManagerInternal wm = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
                wm.computeWindowsForAccessibility(displayId);
                if (postponeWindowStateEvent(event)) {
                    return;
                }
            }
            synchronized (this.mLock) {
                dispatchAccessibilityEventLocked(event);
            }
        }
        if (OWN_PROCESS_ID != android.os.Binder.getCallingPid()) {
            event.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAccessibilityEventLocked(android.view.accessibility.AccessibilityEvent event) {
        if (this.mProxyManager.isProxyedDisplay(event.getDisplayId())) {
            this.mProxyManager.sendAccessibilityEventLocked(event);
        } else {
            notifyAccessibilityServicesDelayedLocked(event, false);
            notifyAccessibilityServicesDelayedLocked(event, true);
        }
        this.mUiAutomationManager.sendAccessibilityEventLocked(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAccessibilityEventToInputFilter(android.view.accessibility.AccessibilityEvent event) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.notifyAccessibilityEvent(event);
            }
        }
        event.recycle();
    }

    public void registerSystemAction(android.app.RemoteAction action, int actionId) {
        registerSystemAction_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.registerSystemAction", 4L, "action=" + action + ";actionId=" + actionId);
        }
        getSystemActionPerformer().registerSystemAction(actionId, action);
    }

    public void unregisterSystemAction(int actionId) {
        unregisterSystemAction_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.unregisterSystemAction", 4L, "actionId=" + actionId);
        }
        getSystemActionPerformer().unregisterSystemAction(actionId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accessibility.SystemActionPerformer getSystemActionPerformer() {
        if (this.mSystemActionPerformer == null) {
            this.mSystemActionPerformer = new com.android.server.accessibility.SystemActionPerformer(this.mContext, this.mWindowManagerService, null, this, this);
        }
        return this.mSystemActionPerformer;
    }

    public android.content.pm.ParceledListSlice<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int userId) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getInstalledAccessibilityServiceList", 4L, "userId=" + userId);
        }
        synchronized (this.mLock) {
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                return new android.content.pm.ParceledListSlice<>(this.mProxyManager.getInstalledAndEnabledServiceInfosLocked(-1, deviceId));
            }
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            java.util.List<android.accessibilityservice.AccessibilityServiceInfo> serviceInfos = new java.util.ArrayList<>(this.mServiceExt.getAccessibilityServiceAfterCheckCustomizeWhiteList(this.mContext, getUserStateLocked(resolvedUserId).mInstalledServices));
            if (android.os.Binder.getCallingPid() == OWN_PROCESS_ID) {
                return new android.content.pm.ParceledListSlice<>(serviceInfos);
            }
            android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            int callingUid = android.os.Binder.getCallingUid();
            for (int i = serviceInfos.size() - 1; i >= 0; i--) {
                android.accessibilityservice.AccessibilityServiceInfo serviceInfo = serviceInfos.get(i);
                if (pm.filterAppAccess(serviceInfo.getComponentName().getPackageName(), callingUid, resolvedUserId)) {
                    serviceInfos.remove(i);
                }
            }
            return new android.content.pm.ParceledListSlice<>(serviceInfos);
        }
    }

    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType, int userId) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getEnabledAccessibilityServiceList", 4L, "feedbackType=" + feedbackType + ";userId=" + userId);
        }
        synchronized (this.mLock) {
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                return this.mProxyManager.getInstalledAndEnabledServiceInfosLocked(feedbackType, deviceId);
            }
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(resolvedUserId);
            if (this.mUiAutomationManager.suppressingAccessibilityServicesLocked()) {
                return java.util.Collections.emptyList();
            }
            java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = userState.mBoundServices;
            int serviceCount = services.size();
            java.util.List<android.accessibilityservice.AccessibilityServiceInfo> result = new java.util.ArrayList<>(serviceCount);
            for (int i = 0; i < serviceCount; i++) {
                com.android.server.accessibility.AccessibilityServiceConnection service = services.get(i);
                if ((service.mFeedbackType & feedbackType) != 0 || feedbackType == -1) {
                    result.add(service.getServiceInfo());
                }
            }
            return result;
        }
    }

    public void interrupt(int userId) {
        java.util.List<android.accessibilityservice.IAccessibilityServiceClient> interfacesToInterrupt;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.interrupt", 4L, "userId=" + userId);
        }
        synchronized (this.mLock) {
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            if (resolvedUserId != this.mCurrentUserId) {
                return;
            }
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                interfacesToInterrupt = new java.util.ArrayList<>();
                this.mProxyManager.addServiceInterfacesLocked(interfacesToInterrupt, deviceId);
            } else {
                java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = getUserStateLocked(resolvedUserId).mBoundServices;
                java.util.List<android.accessibilityservice.IAccessibilityServiceClient> interfacesToInterrupt2 = new java.util.ArrayList<>(services.size());
                for (int i = 0; i < services.size(); i++) {
                    com.android.server.accessibility.AccessibilityServiceConnection service = services.get(i);
                    android.os.IBinder a11yServiceBinder = service.mService;
                    android.accessibilityservice.IAccessibilityServiceClient a11yServiceInterface = service.mServiceInterface;
                    if (a11yServiceBinder != null && a11yServiceInterface != null) {
                        interfacesToInterrupt2.add(a11yServiceInterface);
                    }
                }
                interfacesToInterrupt = interfacesToInterrupt2;
            }
            int count = interfacesToInterrupt.size();
            for (int i2 = 0; i2 < count; i2++) {
                try {
                    if (this.mTraceManager.isA11yTracingEnabledForTypes(2L)) {
                        this.mTraceManager.logTrace("AccessibilityManagerService.IAccessibilityServiceClient.onInterrupt", 2L);
                    }
                    interfacesToInterrupt.get(i2).onInterrupt();
                } catch (android.os.RemoteException re) {
                    android.util.Slog.e(LOG_TAG, "Error sending interrupt request to " + interfacesToInterrupt.get(i2), re);
                }
            }
        }
    }

    public int addAccessibilityInteractionConnection(android.view.IWindow windowToken, android.os.IBinder leashToken, android.view.accessibility.IAccessibilityInteractionConnection connection, java.lang.String packageName, int userId) throws android.os.RemoteException {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.addAccessibilityInteractionConnection", 4L, "windowToken=" + windowToken + "leashToken=" + leashToken + ";connection=" + connection + "; packageName=" + packageName + ";userId=" + userId);
        }
        return this.mA11yWindowManager.addAccessibilityInteractionConnection(windowToken, leashToken, connection, packageName, userId);
    }

    public void removeAccessibilityInteractionConnection(android.view.IWindow window) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.removeAccessibilityInteractionConnection", 4L, "window=" + window);
        }
        this.mA11yWindowManager.removeAccessibilityInteractionConnection(window);
    }

    public void setPictureInPictureActionReplacingConnection(android.view.accessibility.IAccessibilityInteractionConnection connection) throws android.os.RemoteException {
        setPictureInPictureActionReplacingConnection_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.setPictureInPictureActionReplacingConnection", 4L, "connection=" + connection);
        }
        this.mA11yWindowManager.setPictureInPictureActionReplacingConnection(connection);
    }

    public void registerUiTestAutomationService(android.os.IBinder owner, android.accessibilityservice.IAccessibilityServiceClient serviceClient, android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo, int userId, int flags) throws java.lang.Throwable {
        registerUiTestAutomationService_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.registerUiTestAutomationService", 4L, "owner=" + owner + ";serviceClient=" + serviceClient + ";accessibilityServiceInfo=" + accessibilityServiceInfo + ";flags=" + flags);
        }
        synchronized (this.mLock) {
            try {
                try {
                    changeCurrentUserForTestAutomationIfNeededLocked(userId);
                    com.android.server.accessibility.UiAutomationManager uiAutomationManager = this.mUiAutomationManager;
                    android.content.Context context = this.mContext;
                    int i = sIdCounter;
                    sIdCounter = i + 1;
                    uiAutomationManager.registerUiTestAutomationServiceLocked(owner, serviceClient, context, accessibilityServiceInfo, i, this.mMainHandler, this.mSecurityPolicy, this, getTraceManager(), this.mWindowManagerService, getSystemActionPerformer(), this.mA11yWindowManager, flags);
                    onUserStateChangedLocked(getCurrentUserStateLocked());
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

    public void unregisterUiTestAutomationService(android.accessibilityservice.IAccessibilityServiceClient serviceClient) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.unregisterUiTestAutomationService", 4L, "serviceClient=" + serviceClient);
        }
        synchronized (this.mLock) {
            this.mUiAutomationManager.unregisterUiTestAutomationServiceLocked(serviceClient);
            restoreCurrentUserAfterTestAutomationIfNeededLocked();
        }
    }

    private void changeCurrentUserForTestAutomationIfNeededLocked(int userId) {
        if (this.mVisibleBgUserIds == null) {
            com.android.server.utils.Slogf.d(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(%d): ignoring because device doesn't support visible background users", java.lang.Integer.valueOf(userId));
            return;
        }
        if (!this.mVisibleBgUserIds.get(userId)) {
            com.android.server.utils.Slogf.wtf(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(): cannot change current user to %d as it's not visible (mVisibleUsers=%s)", java.lang.Integer.valueOf(userId), this.mVisibleBgUserIds);
        } else {
            if (this.mCurrentUserId == userId) {
                com.android.server.utils.Slogf.d(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(): NOT changing current user for test automation purposes as it is already %d", java.lang.Integer.valueOf(this.mCurrentUserId));
                return;
            }
            com.android.server.utils.Slogf.i(LOG_TAG, "changeCurrentUserForTestAutomationIfNeededLocked(): changing current user from %d to %d for test automation purposes", java.lang.Integer.valueOf(this.mCurrentUserId), java.lang.Integer.valueOf(userId));
            this.mRealCurrentUserId = this.mCurrentUserId;
            switchUser(userId);
        }
    }

    private void restoreCurrentUserAfterTestAutomationIfNeededLocked() {
        if (this.mVisibleBgUserIds == null) {
            com.android.server.utils.Slogf.d(LOG_TAG, "restoreCurrentUserForTestAutomationIfNeededLocked(): ignoring because device doesn't support visible background users");
            return;
        }
        if (this.mRealCurrentUserId == -2) {
            com.android.server.utils.Slogf.d(LOG_TAG, "restoreCurrentUserForTestAutomationIfNeededLocked(): ignoring because mRealCurrentUserId is already USER_CURRENT");
            return;
        }
        com.android.server.utils.Slogf.i(LOG_TAG, "restoreCurrentUserForTestAutomationIfNeededLocked(): restoring current user to %d after using %d for test automation purposes", java.lang.Integer.valueOf(this.mRealCurrentUserId), java.lang.Integer.valueOf(this.mCurrentUserId));
        int currentUserId = this.mRealCurrentUserId;
        this.mRealCurrentUserId = -2;
        switchUser(currentUserId);
    }

    public android.os.IBinder getWindowToken(int windowId, int userId) {
        getWindowToken_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getWindowToken", 4L, "windowId=" + windowId + ";userId=" + userId);
        }
        synchronized (this.mLock) {
            int resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            if (resolvedUserId != this.mCurrentUserId) {
                return null;
            }
            android.view.accessibility.AccessibilityWindowInfo accessibilityWindowInfo = this.mA11yWindowManager.findA11yWindowInfoByIdLocked(windowId);
            if (accessibilityWindowInfo == null) {
                return null;
            }
            return this.mA11yWindowManager.getWindowTokenForUserAndWindowIdLocked(userId, accessibilityWindowInfo.getId());
        }
    }

    public void notifyAccessibilityButtonClicked(int displayId, java.lang.String targetName) {
        notifyAccessibilityButtonClicked_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.notifyAccessibilityButtonClicked", 4L, "displayId=" + displayId + ";targetName=" + targetName);
        }
        if (targetName == null) {
            synchronized (this.mLock) {
                com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
                targetName = userState.getTargetAssignedToAccessibilityButton();
            }
        }
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda39(), this, java.lang.Integer.valueOf(displayId), 1, targetName));
    }

    public void notifyAccessibilityButtonVisibilityChanged(boolean shown) {
        notifyAccessibilityButtonVisibilityChanged_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.notifyAccessibilityButtonVisibilityChanged", 4L, "shown=" + shown);
        }
        synchronized (this.mLock) {
            notifyAccessibilityButtonVisibilityChangedLocked(shown);
        }
    }

    public void notifyQuickSettingsTilesChanged(int userId, java.util.List<android.content.ComponentName> tileComponentNames) throws java.lang.Throwable {
        notifyQuickSettingsTilesChanged_enforcePermission();
        if (!android.view.accessibility.Flags.a11yQsShortcut()) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(LOG_TAG, android.text.TextUtils.formatSimple("notifyQuickSettingsTilesChanged userId: %d, tileComponentNames: %s", new java.lang.Object[]{java.lang.Integer.valueOf(userId), tileComponentNames}));
        }
        final java.util.Set<android.content.ComponentName> newTileComponentNames = new android.util.ArraySet<>(tileComponentNames);
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(userId);
            java.util.Map<android.content.ComponentName, android.accessibilityservice.AccessibilityServiceInfo> tileServiceToA11yServiceInfo = userState.getTileServiceToA11yServiceInfoMapLocked();
            java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTileService = userState.getA11yFeatureToTileService();
            final android.util.ArraySet<android.content.ComponentName> currentTiles = userState.getA11yQsTilesInQsPanel();
            java.util.Set<android.content.ComponentName> addedTiles = (java.util.Set) newTileComponentNames.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda83
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$notifyQuickSettingsTilesChanged$9(currentTiles, (android.content.ComponentName) obj);
                }
            }).collect(java.util.stream.Collectors.toSet());
            java.util.Set<android.content.ComponentName> removedTiles = (java.util.Set) currentTiles.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda84
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$notifyQuickSettingsTilesChanged$10(newTileComponentNames, (android.content.ComponentName) obj);
                }
            }).collect(java.util.stream.Collectors.toSet());
            if (addedTiles.isEmpty() && removedTiles.isEmpty()) {
                return;
            }
            userState.updateA11yTilesInQsPanelLocked(newTileComponentNames);
            java.util.List<java.lang.String> a11yFeaturesToEnable = new java.util.ArrayList<>();
            java.util.List<java.lang.String> a11yFeaturesToRemove = new java.util.ArrayList<>();
            for (java.util.Map.Entry<android.content.ComponentName, android.content.ComponentName> frameworkFeatureWithTile : com.android.internal.accessibility.common.ShortcutConstants.A11Y_FEATURE_TO_FRAMEWORK_TILE.entrySet()) {
                java.lang.String a11yFeature = frameworkFeatureWithTile.getKey().flattenToString();
                android.content.ComponentName tile = frameworkFeatureWithTile.getValue();
                if (addedTiles.contains(tile)) {
                    a11yFeaturesToEnable.add(a11yFeature);
                } else if (removedTiles.contains(tile)) {
                    a11yFeaturesToRemove.add(a11yFeature);
                }
            }
            for (java.util.Map.Entry<android.content.ComponentName, android.content.ComponentName> a11yFeatureWithTileService : a11yFeatureToTileService.entrySet()) {
                java.lang.String a11yFeature2 = a11yFeatureWithTileService.getKey().flattenToString();
                android.content.ComponentName tileService = a11yFeatureWithTileService.getValue();
                if (addedTiles.contains(tileService)) {
                    android.accessibilityservice.AccessibilityServiceInfo serviceInfo = tileServiceToA11yServiceInfo.getOrDefault(tileService, null);
                    if (serviceInfo != null && isAccessibilityServiceWarningRequired(serviceInfo)) {
                        logMetricForQsShortcutConfiguration(true, 1);
                    } else {
                        a11yFeaturesToEnable.add(a11yFeature2);
                    }
                } else if (removedTiles.contains(tileService)) {
                    a11yFeaturesToRemove.add(a11yFeature2);
                }
            }
            if (!a11yFeaturesToEnable.isEmpty()) {
                enableShortcutForTargets(true, 16, a11yFeaturesToEnable, userId);
            }
            if (!a11yFeaturesToRemove.isEmpty()) {
                enableShortcutForTargets(false, 16, a11yFeaturesToRemove, userId);
            }
        }
    }

    static /* synthetic */ boolean lambda$notifyQuickSettingsTilesChanged$9(android.util.ArraySet currentTiles, android.content.ComponentName tileComponentName) {
        return !currentTiles.contains(tileComponentName);
    }

    static /* synthetic */ boolean lambda$notifyQuickSettingsTilesChanged$10(java.util.Set newTileComponentNames, android.content.ComponentName tileComponentName) {
        return !newTileComponentNames.contains(tileComponentName);
    }

    public boolean onGesture(android.accessibilityservice.AccessibilityGestureEvent gestureEvent) {
        boolean handled;
        synchronized (this.mLock) {
            handled = notifyGestureLocked(gestureEvent, false);
            if (!handled) {
                handled = notifyGestureLocked(gestureEvent, true);
            }
        }
        return handled;
    }

    public boolean sendMotionEventToListeningServices(android.view.MotionEvent event) {
        boolean result = scheduleNotifyMotionEvent(android.view.MotionEvent.obtain(event));
        return result;
    }

    public boolean onTouchStateChanged(int displayId, int state) {
        return scheduleNotifyTouchState(displayId, state);
    }

    @Override // com.android.server.accessibility.SystemActionPerformer.SystemActionsChangedListener
    public void onSystemActionsChanged() {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
            notifySystemActionsChangedLocked(state);
        }
    }

    @Override // com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack
    public void moveNonProxyTopFocusedDisplayToTopIfNeeded() {
        this.mA11yWindowManager.moveNonProxyTopFocusedDisplayToTopIfNeeded();
    }

    @Override // com.android.server.accessibility.SystemActionPerformer.DisplayUpdateCallBack
    public int getLastNonProxyTopFocusedDisplayId() {
        return this.mA11yWindowManager.getLastNonProxyTopFocusedDisplayId();
    }

    void notifySystemActionsChangedLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
            service.notifySystemActionsChangedLocked();
        }
    }

    public boolean notifyKeyEvent(android.view.KeyEvent event, int policyFlags) {
        synchronized (this.mLock) {
            java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> boundServices = getCurrentUserStateLocked().mBoundServices;
            if (boundServices.isEmpty()) {
                return false;
            }
            return getKeyEventDispatcher().notifyKeyEventLocked(event, policyFlags, boundServices);
        }
    }

    public void notifyMagnificationChanged(int displayId, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
        synchronized (this.mLock) {
            notifyClearAccessibilityCacheLocked();
            notifyMagnificationChangedLocked(displayId, region, config);
        }
    }

    void setMotionEventInjectors(android.util.SparseArray<com.android.server.accessibility.MotionEventInjector> motionEventInjectors) {
        synchronized (this.mLock) {
            this.mMotionEventInjectors = motionEventInjectors;
            this.mLock.notifyAll();
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public com.android.server.accessibility.MotionEventInjector getMotionEventInjectorForDisplayLocked(int displayId) {
        long endMillis = android.os.SystemClock.uptimeMillis() + 1000;
        while (this.mMotionEventInjectors == null && android.os.SystemClock.uptimeMillis() < endMillis) {
            try {
                this.mLock.wait(endMillis - android.os.SystemClock.uptimeMillis());
            } catch (java.lang.InterruptedException e) {
            }
        }
        if (this.mMotionEventInjectors == null) {
            android.util.Slog.e(LOG_TAG, "MotionEventInjector installation timed out");
            return null;
        }
        com.android.server.accessibility.MotionEventInjector motionEventInjector = this.mMotionEventInjectors.get(displayId);
        return motionEventInjector;
    }

    public boolean getAccessibilityFocusClickPointInScreen(android.graphics.Point outPoint) {
        return getInteractionBridge().getAccessibilityFocusClickPointInScreenNotLocked(outPoint);
    }

    public boolean performActionOnAccessibilityFocusedItem(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
        return getInteractionBridge().performActionOnAccessibilityFocusedItemNotLocked(action);
    }

    public boolean accessibilityFocusOnlyInActiveWindow() {
        boolean zAccessibilityFocusOnlyInActiveWindowLocked;
        synchronized (this.mLock) {
            zAccessibilityFocusOnlyInActiveWindowLocked = this.mA11yWindowManager.accessibilityFocusOnlyInActiveWindowLocked();
        }
        return zAccessibilityFocusOnlyInActiveWindowLocked;
    }

    boolean getWindowBounds(int windowId, android.graphics.Rect outBounds) {
        android.os.IBinder token;
        synchronized (this.mLock) {
            token = getWindowToken(windowId, this.mCurrentUserId);
        }
        if (this.mTraceManager.isA11yTracingEnabledForTypes(512L)) {
            this.mTraceManager.logTrace("WindowManagerInternal.getWindowFrame", 512L, "token=" + token + ";outBounds=" + outBounds);
        }
        this.mWindowManagerService.getWindowFrame(token, outBounds);
        if (!outBounds.isEmpty()) {
            return true;
        }
        return false;
    }

    public int getActiveWindowId() {
        return this.mA11yWindowManager.getActiveWindowId(this.mCurrentUserId);
    }

    public void onTouchInteractionStart() {
        this.mA11yWindowManager.onTouchInteractionStart();
    }

    public void onTouchInteractionEnd() {
        this.mA11yWindowManager.onTouchInteractionEnd();
    }

    void switchUser(int userId) {
        this.mMagnificationController.updateUserIdIfNeeded(userId);
        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parsedAccessibilityServiceInfos = parseAccessibilityServiceInfos(userId);
        java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parsedAccessibilityShortcutInfos = parseAccessibilityShortcutInfos(userId);
        synchronized (this.mLock) {
            if (this.mCurrentUserId == userId && this.mInitialized) {
                return;
            }
            com.android.server.accessibility.AccessibilityUserState oldUserState = getCurrentUserStateLocked();
            oldUserState.onSwitchToAnotherUserLocked();
            if (oldUserState.mUserClients.getRegisteredCallbackCount() > 0) {
                this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda60
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.accessibility.AccessibilityManagerService) obj).sendStateToClients(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
                    }
                }, this, 0, java.lang.Integer.valueOf(oldUserState.mUserId)));
            }
            android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService("user");
            boolean z = true;
            if (userManager.getUsers().size() <= 1) {
                z = false;
            }
            boolean announceNewUser = z;
            this.mCurrentUserId = userId;
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            readConfigurationForUserStateLocked(userState, parsedAccessibilityServiceInfos, parsedAccessibilityShortcutInfos);
            this.mSecurityPolicy.onSwitchUserLocked(this.mCurrentUserId, userState.mEnabledServices);
            onUserStateChangedLocked(userState);
            migrateAccessibilityButtonSettingsIfNecessaryLocked(userState, null, 0);
            disableAccessibilityMenuToMigrateIfNeeded();
            if (announceNewUser) {
                this.mMainHandler.sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda61
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.accessibility.AccessibilityManagerService) obj).announceNewUserIfNeeded();
                    }
                }, this), 3000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void announceNewUserIfNeeded() {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            if (userState.isHandlingAccessibilityEventsLocked()) {
                android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService("user");
                java.lang.String message = this.mContext.getString(android.R.string.time_picker_increment_minute_button, userManager.getUserInfo(this.mCurrentUserId).name);
                android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(16384);
                event.getText().add(message);
                sendAccessibilityEventLocked(event, this.mCurrentUserId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlockUser(int userId) {
        synchronized (this.mLock) {
            int parentUserId = this.mSecurityPolicy.resolveProfileParentLocked(userId);
            if (parentUserId == this.mCurrentUserId) {
                com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(this.mCurrentUserId);
                onUserStateChangedLocked(userState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeUser(int userId) {
        synchronized (this.mLock) {
            this.mUserStates.remove(userId);
        }
        getMagnificationController().onUserRemoved(userId);
    }

    void restoreEnabledAccessibilityServicesLocked(java.lang.String oldSetting, java.lang.String newSetting, int restoreFromSdkInt) {
        readComponentNamesFromStringLocked(oldSetting, this.mTempComponentNameSet, false);
        readComponentNamesFromStringLocked(newSetting, this.mTempComponentNameSet, true);
        com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(0);
        userState.mEnabledServices.clear();
        userState.mEnabledServices.addAll(this.mTempComponentNameSet);
        persistComponentNamesToSettingLocked("enabled_accessibility_services", userState.mEnabledServices, 0);
        onUserStateChangedLocked(userState);
        migrateAccessibilityButtonSettingsIfNecessaryLocked(userState, null, restoreFromSdkInt);
    }

    void restoreAccessibilityButtonTargetsLocked(java.lang.String oldSetting, java.lang.String newSetting) {
        android.util.ArraySet arraySet = new android.util.ArraySet();
        readColonDelimitedStringToSet(oldSetting, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda68
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityButtonTargetsLocked$11((java.lang.String) obj);
            }
        }, arraySet, false);
        readColonDelimitedStringToSet(newSetting, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda69
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityButtonTargetsLocked$12((java.lang.String) obj);
            }
        }, arraySet, true);
        com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(0);
        userState.mAccessibilityButtonTargets.clear();
        userState.mAccessibilityButtonTargets.addAll((java.util.Collection<? extends java.lang.String>) arraySet);
        persistColonDelimitedSetToSettingLocked("accessibility_button_targets", 0, userState.mAccessibilityButtonTargets, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda70
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityButtonTargetsLocked$13((java.lang.String) obj);
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        onUserStateChangedLocked(userState);
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityButtonTargetsLocked$11(java.lang.String str) {
        return str;
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityButtonTargetsLocked$12(java.lang.String str) {
        return str;
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityButtonTargetsLocked$13(java.lang.String str) {
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreAccessibilityQsTargets(java.lang.String newValue) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(0);
            android.util.ArraySet<java.lang.String> a11yQsTargets = userState.getA11yQsTargets();
            readColonDelimitedStringToSet(newValue, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda63
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityQsTargets$14((java.lang.String) obj);
                }
            }, a11yQsTargets, true);
            userState.updateA11yQsTargetLocked(a11yQsTargets);
            persistColonDelimitedSetToSettingLocked("accessibility_qs_targets", 0, a11yQsTargets, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda64
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityQsTargets$15((java.lang.String) obj);
                }
            });
            scheduleNotifyClientsOfServicesStateChangeLocked(userState);
            onUserStateChangedLocked(userState);
        }
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityQsTargets$14(java.lang.String str) {
        return str;
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityQsTargets$15(java.lang.String str) {
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreAccessibilityShortcutTargetService(java.lang.String oldValue, java.lang.String restoredValue) {
        android.util.ArraySet arraySet = new android.util.ArraySet();
        readColonDelimitedStringToSet(oldValue, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityShortcutTargetService$16((java.lang.String) obj);
            }
        }, arraySet, false);
        java.lang.String defaultService = this.mContext.getString(android.R.string.config_defaultContentCaptureService);
        final android.content.ComponentName defaultServiceComponent = android.text.TextUtils.isEmpty(defaultService) ? null : android.content.ComponentName.unflattenFromString(defaultService);
        boolean shouldClearDefaultService = (defaultServiceComponent == null || stringSetContainsComponentName(arraySet, defaultServiceComponent)) ? false : true;
        readColonDelimitedStringToSet(restoredValue, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityShortcutTargetService$17((java.lang.String) obj);
            }
        }, arraySet, true);
        if (com.android.server.accessibility.Flags.clearDefaultFromA11yShortcutTargetServiceRestore()) {
            if (shouldClearDefaultService && stringSetContainsComponentName(arraySet, defaultServiceComponent)) {
                android.util.Slog.i(LOG_TAG, "Removing default service " + defaultService + " from restore of accessibility_shortcut_target_service");
                arraySet.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda15
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return defaultServiceComponent.equals(android.content.ComponentName.unflattenFromString((java.lang.String) obj));
                    }
                });
            }
            if (arraySet.isEmpty()) {
                return;
            }
        }
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(0);
            java.util.Set<java.lang.String> shortcutTargets = userState.getShortcutTargetsLocked(2);
            shortcutTargets.clear();
            shortcutTargets.addAll(arraySet);
            persistColonDelimitedSetToSettingLocked("accessibility_shortcut_target_service", 0, arraySet, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda16
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$restoreAccessibilityShortcutTargetService$19((java.lang.String) obj);
                }
            });
            scheduleNotifyClientsOfServicesStateChangeLocked(userState);
            onUserStateChangedLocked(userState);
        }
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityShortcutTargetService$16(java.lang.String str) {
        return str;
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityShortcutTargetService$17(java.lang.String str) {
        return str;
    }

    static /* synthetic */ java.lang.String lambda$restoreAccessibilityShortcutTargetService$19(java.lang.String str) {
        return str;
    }

    private boolean stringSetContainsComponentName(java.util.Set<java.lang.String> set, android.content.ComponentName componentName) {
        if (componentName != null) {
            java.util.stream.Stream<R> map = set.stream().map(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda71());
            java.util.Objects.requireNonNull(componentName);
            if (map.anyMatch(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda72(componentName))) {
                return true;
            }
        }
        return false;
    }

    private int getClientStateLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        return userState.getClientStateLocked(this.mUiAutomationManager.canIntrospect(), this.mTraceManager.getTraceStateForAccessibilityManagerClientState());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.accessibility.AccessibilityManagerService.InteractionBridge getInteractionBridge() {
        com.android.server.accessibility.AccessibilityManagerService.InteractionBridge interactionBridge;
        synchronized (this.mLock) {
            if (this.mInteractionBridge == null) {
                this.mInteractionBridge = new com.android.server.accessibility.AccessibilityManagerService.InteractionBridge();
            }
            interactionBridge = this.mInteractionBridge;
        }
        return interactionBridge;
    }

    private boolean notifyGestureLocked(android.accessibilityservice.AccessibilityGestureEvent gestureEvent, boolean isDefault) {
        com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
        for (int i = state.mBoundServices.size() - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityServiceConnection service = state.mBoundServices.get(i);
            if (service.mRequestTouchExplorationMode && service.mIsDefault == isDefault) {
                service.notifyGesture(gestureEvent);
                return true;
            }
        }
        return false;
    }

    private boolean scheduleNotifyMotionEvent(android.view.MotionEvent event) {
        boolean result = false;
        int displayId = event.getDisplayId();
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
            for (int i = state.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = state.mBoundServices.get(i);
                if (service.wantsGenericMotionEvent(event) || (event.isFromSource(4098) && service.isServiceDetectsGesturesEnabled(displayId))) {
                    service.notifyMotionEvent(event);
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean scheduleNotifyTouchState(int displayId, int touchState) {
        boolean result = false;
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
            for (int i = state.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = state.mBoundServices.get(i);
                if (service.isServiceDetectsGesturesEnabled(displayId)) {
                    service.notifyTouchState(displayId, touchState);
                    result = true;
                }
            }
        }
        return result;
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public void notifyClearAccessibilityCacheLocked() {
        com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
        for (int i = state.mBoundServices.size() - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityServiceConnection service = state.mBoundServices.get(i);
            service.notifyClearAccessibilityNodeInfoCache();
        }
        this.mProxyManager.clearCacheLocked();
    }

    private void notifyMagnificationChangedLocked(int displayId, android.graphics.Region region, android.accessibilityservice.MagnificationConfig config) {
        com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
        for (int i = state.mBoundServices.size() - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityServiceConnection service = state.mBoundServices.get(i);
            service.notifyMagnificationChangedLocked(displayId, region, config);
        }
    }

    private void sendAccessibilityButtonToInputFilter(int displayId) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.notifyAccessibilityButtonClicked(displayId);
            }
        }
    }

    private void showAccessibilityTargetsSelection(int displayId, int shortcutType) {
        java.lang.String chooserClassName;
        android.content.Intent intent = new android.content.Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
        if (shortcutType == 2) {
            chooserClassName = com.android.internal.accessibility.dialog.AccessibilityShortcutChooserActivity.class.getName();
        } else {
            chooserClassName = com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity.class.getName();
        }
        intent.setClassName(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, chooserClassName);
        intent.addFlags(268468224);
        intent.setComponent(this.mServiceExt.replaceOplusUiIntent(this.mContext, shortcutType, intent.getComponent()));
        android.os.Bundle bundle = android.app.ActivityOptions.makeBasic().setLaunchDisplayId(displayId).toBundle();
        this.mContext.startActivityAsUser(intent, bundle, android.os.UserHandle.of(this.mCurrentUserId));
    }

    private void launchShortcutTargetActivity(int displayId, android.content.ComponentName name) {
        android.content.Intent intent = new android.content.Intent();
        android.os.Bundle bundle = android.app.ActivityOptions.makeBasic().setLaunchDisplayId(displayId).toBundle();
        intent.setComponent(name);
        intent.addFlags(268435456);
        try {
            this.mContext.startActivityAsUser(intent, bundle, android.os.UserHandle.of(this.mCurrentUserId));
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void launchAccessibilitySubSettings(int displayId, android.content.ComponentName name) {
        android.content.Intent intent = new android.content.Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS");
        android.os.Bundle bundle = android.app.ActivityOptions.makeBasic().setLaunchDisplayId(displayId).toBundle();
        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.AAC_ADIF);
        intent.putExtra("android.intent.extra.COMPONENT_NAME", name.flattenToString());
        try {
            this.mContext.startActivityAsUser(intent, bundle, android.os.UserHandle.of(this.mCurrentUserId));
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void launchHearingDevicesDialog() {
        android.content.Intent intent = new android.content.Intent(ACTION_LAUNCH_HEARING_DEVICES_DIALOG);
        intent.setFlags(268435456);
        intent.setPackage(this.mContext.getString(android.R.string.config_systemUi));
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.SYSTEM);
    }

    private void notifyAccessibilityButtonVisibilityChangedLocked(boolean available) {
        com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
        this.mIsAccessibilityButtonShown = available;
        for (int i = state.mBoundServices.size() - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityServiceConnection clientConnection = state.mBoundServices.get(i);
            if (clientConnection.mRequestAccessibilityButton) {
                clientConnection.notifyAccessibilityButtonAvailabilityChangedLocked(clientConnection.isAccessibilityButtonAvailableLocked(state));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parseAccessibilityServiceInfos(int userId) {
        int flags;
        java.util.List<android.accessibilityservice.AccessibilityServiceInfo> result = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            flags = getUserStateLocked(userId).getBindInstantServiceAllowedLocked() ? 819332 | 8388608 : 819332;
        }
        java.util.List<android.content.pm.ResolveInfo> installedServices = this.mPackageManager.queryIntentServicesAsUser(new android.content.Intent("android.accessibilityservice.AccessibilityService"), flags, userId);
        int count = installedServices.size();
        for (int i = 0; i < count; i++) {
            android.content.pm.ResolveInfo resolveInfo = installedServices.get(i);
            android.content.pm.ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (this.mSecurityPolicy.canRegisterService(serviceInfo)) {
                try {
                    android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo = new android.accessibilityservice.AccessibilityServiceInfo(resolveInfo, this.mContext);
                    if (!accessibilityServiceInfo.isWithinParcelableSize()) {
                        android.util.Slog.e(LOG_TAG, "Skipping service " + accessibilityServiceInfo.getResolveInfo().getComponentInfo() + " because service info size is larger than safe parcelable limits.");
                    } else {
                        result.add(accessibilityServiceInfo);
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException xppe) {
                    android.util.Slog.e(LOG_TAG, "Error while initializing AccessibilityServiceInfo", xppe);
                }
            }
        }
        return result;
    }

    private boolean readInstalledAccessibilityServiceLocked(com.android.server.accessibility.AccessibilityUserState userState, java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parsedAccessibilityServiceInfos) {
        if (parsedAccessibilityServiceInfos == null) {
            android.util.Slog.i(LOG_TAG, "parsedAccessibilityServiceInfos is null, just return");
            return false;
        }
        int count = parsedAccessibilityServiceInfos.size();
        for (int i = 0; i < count; i++) {
            android.accessibilityservice.AccessibilityServiceInfo accessibilityServiceInfo = parsedAccessibilityServiceInfos.get(i);
            if (userState.mCrashedServices.contains(accessibilityServiceInfo.getComponentName())) {
                accessibilityServiceInfo.crashed = true;
            }
        }
        if (parsedAccessibilityServiceInfos.equals(userState.mInstalledServices)) {
            return false;
        }
        userState.mInstalledServices.clear();
        userState.mInstalledServices.addAll(parsedAccessibilityServiceInfos);
        userState.updateTileServiceMapForAccessibilityServiceLocked();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parseAccessibilityShortcutInfos(int userId) {
        return android.view.accessibility.AccessibilityManager.getInstance(this.mContext).getInstalledAccessibilityShortcutListAsUser(this.mContext, userId);
    }

    private boolean readInstalledAccessibilityShortcutLocked(com.android.server.accessibility.AccessibilityUserState userState, java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parsedAccessibilityShortcutInfos) {
        if (!parsedAccessibilityShortcutInfos.equals(userState.mInstalledShortcuts)) {
            userState.mInstalledShortcuts.clear();
            userState.mInstalledShortcuts.addAll(parsedAccessibilityShortcutInfos);
            userState.updateTileServiceMapForAccessibilityActivityLocked();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readEnabledAccessibilityServicesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("enabled_accessibility_services", userState.mUserId, this.mTempComponentNameSet);
        if (!this.mTempComponentNameSet.equals(userState.mEnabledServices)) {
            userState.mEnabledServices.clear();
            userState.mEnabledServices.addAll(this.mTempComponentNameSet);
            this.mTempComponentNameSet.clear();
            return true;
        }
        this.mTempComponentNameSet.clear();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readTouchExplorationGrantedAccessibilityServicesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("touch_exploration_granted_accessibility_services", userState.mUserId, this.mTempComponentNameSet);
        if (!this.mTempComponentNameSet.equals(userState.mTouchExplorationGrantedServices)) {
            userState.mTouchExplorationGrantedServices.clear();
            userState.mTouchExplorationGrantedServices.addAll(this.mTempComponentNameSet);
            this.mTempComponentNameSet.clear();
            return true;
        }
        this.mTempComponentNameSet.clear();
        return false;
    }

    private void notifyAccessibilityServicesDelayedLocked(android.view.accessibility.AccessibilityEvent event, boolean isDefault) {
        try {
            com.android.server.accessibility.AccessibilityUserState state = getCurrentUserStateLocked();
            int count = state.mBoundServices.size();
            for (int i = 0; i < count; i++) {
                com.android.server.accessibility.AccessibilityServiceConnection service = state.mBoundServices.get(i);
                if (service.mIsDefault == isDefault) {
                    service.notifyAccessibilityEvent(event);
                }
            }
        } catch (java.lang.IndexOutOfBoundsException e) {
        }
    }

    private void updateRelevantEventsLocked(final com.android.server.accessibility.AccessibilityUserState userState) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(2L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.updateRelevantEventsLocked", 2L, "userState=" + userState);
        }
        this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateRelevantEventsLocked$21(userState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRelevantEventsLocked$21(final com.android.server.accessibility.AccessibilityUserState userState) {
        broadcastToClients(userState, com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda43
            public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$updateRelevantEventsLocked$20(userState, (com.android.server.accessibility.AccessibilityManagerService.Client) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRelevantEventsLocked$20(com.android.server.accessibility.AccessibilityUserState userState, com.android.server.accessibility.AccessibilityManagerService.Client client) throws android.os.RemoteException {
        synchronized (this.mLock) {
            int relevantEventTypes = computeRelevantEventTypesLocked(userState, client);
            if (!this.mProxyManager.isProxyedDeviceId(client.mDeviceId) && client.mLastSentRelevantEventTypes != relevantEventTypes) {
                client.mLastSentRelevantEventTypes = relevantEventTypes;
                client.mCallback.setRelevantEventTypes(relevantEventTypes);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int computeRelevantEventTypesLocked(com.android.server.accessibility.AccessibilityUserState userState, com.android.server.accessibility.AccessibilityManagerService.Client client) {
        int relevantEventTypes = 0;
        int serviceCount = userState.mBoundServices.size();
        int i = 0;
        while (true) {
            if (i >= serviceCount) {
                break;
            }
            com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
            if (isClientInPackageAllowlist(service.getServiceInfo(), client)) {
                relevantEventTypes = service.getRelevantEventTypes();
            }
            relevantEventTypes |= relevantEventTypes;
            i++;
        }
        return relevantEventTypes | (isClientInPackageAllowlist(this.mUiAutomationManager.getServiceInfo(), client) ? this.mUiAutomationManager.getRelevantEventTypes() : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMagnificationModeChangeSettingsLocked(com.android.server.accessibility.AccessibilityUserState userState, int displayId) {
        if (userState.mUserId != this.mCurrentUserId || fallBackMagnificationModeSettingsLocked(userState, displayId)) {
            return;
        }
        this.mMagnificationController.transitionMagnificationModeLocked(displayId, userState.getMagnificationModeLocked(displayId), new com.android.server.accessibility.magnification.MagnificationController.TransitionCallBack() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda42
            @Override // com.android.server.accessibility.magnification.MagnificationController.TransitionCallBack
            public final void onResult(int i, boolean z) {
                this.f$0.onMagnificationTransitionEndedLocked(i, z);
            }
        });
    }

    void onMagnificationTransitionEndedLocked(int displayId, boolean success) {
        com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
        int previousMode = userState.getMagnificationModeLocked(displayId) ^ 3;
        if (!success && previousMode != 0) {
            userState.setMagnificationModeLocked(displayId, previousMode);
            if (displayId == 0) {
                persistMagnificationModeSettingsLocked(previousMode);
                return;
            }
            return;
        }
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda49
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).notifyRefreshMagnificationModeToInputFilter(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyRefreshMagnificationModeToInputFilter(int displayId) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter) {
                java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
                for (int i = 0; i < displays.size(); i++) {
                    android.view.Display display = displays.get(i);
                    if (display != null && display.getDisplayId() == displayId) {
                        this.mInputFilter.refreshMagnificationMode(display);
                        return;
                    }
                }
            }
        }
    }

    static boolean isClientInPackageAllowlist(android.accessibilityservice.AccessibilityServiceInfo serviceInfo, com.android.server.accessibility.AccessibilityManagerService.Client client) {
        int i = 0;
        if (serviceInfo == null) {
            return false;
        }
        java.lang.String[] clientPackages = client.mPackageNames;
        boolean result = com.android.internal.util.ArrayUtils.isEmpty(serviceInfo.packageNames);
        if (!result && clientPackages != null) {
            int length = clientPackages.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                java.lang.String packageName = clientPackages[i];
                if (!com.android.internal.util.ArrayUtils.contains(serviceInfo.packageNames, packageName)) {
                    i++;
                } else {
                    result = true;
                    break;
                }
            }
        }
        if (!result && DEBUG) {
            android.util.Slog.d(LOG_TAG, "Dropping events: " + java.util.Arrays.toString(clientPackages) + " -> " + serviceInfo.getComponentName().flattenToShortString() + " due to not being in package allowlist " + java.util.Arrays.toString(serviceInfo.packageNames));
        }
        return result;
    }

    private void broadcastToClients(com.android.server.accessibility.AccessibilityUserState userState, java.util.function.Consumer<com.android.server.accessibility.AccessibilityManagerService.Client> clientAction) {
        this.mGlobalClients.broadcastForEachCookie(clientAction);
        userState.mUserClients.broadcastForEachCookie(clientAction);
    }

    void readComponentNamesFromSettingLocked(java.lang.String settingName, int userId, java.util.Set<android.content.ComponentName> outComponentNames) {
        readColonDelimitedSettingToSet(settingName, userId, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda38
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.content.ComponentName.unflattenFromString((java.lang.String) obj);
            }
        }, outComponentNames);
    }

    private void readComponentNamesFromStringLocked(java.lang.String names, java.util.Set<android.content.ComponentName> outComponentNames, boolean doMerge) {
        readColonDelimitedStringToSet(names, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda26
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.content.ComponentName.unflattenFromString((java.lang.String) obj);
            }
        }, outComponentNames, doMerge);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void persistComponentNamesToSettingLocked(java.lang.String settingName, java.util.Set<android.content.ComponentName> componentNames, int userId) {
        persistColonDelimitedSetToSettingLocked(settingName, userId, componentNames, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda20
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.content.ComponentName) obj).flattenToShortString();
            }
        });
    }

    <T> void readColonDelimitedSettingToSet(java.lang.String settingName, int userId, java.util.function.Function<java.lang.String, T> toItem, java.util.Set<T> outSet) {
        java.lang.String settingValue = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), settingName, userId);
        readColonDelimitedStringToSet(settingValue, toItem, outSet, false);
    }

    private <T> void readColonDelimitedStringToSet(java.lang.String names, java.util.function.Function<java.lang.String, T> toItem, java.util.Set<T> outSet, boolean doMerge) {
        T item;
        if (!doMerge) {
            outSet.clear();
        }
        if (!android.text.TextUtils.isEmpty(names)) {
            android.text.TextUtils.SimpleStringSplitter splitter = this.mStringColonSplitter;
            splitter.setString(names);
            while (splitter.hasNext()) {
                java.lang.String str = splitter.next();
                if (!android.text.TextUtils.isEmpty(str) && (item = toItem.apply(str)) != null) {
                    outSet.add(item);
                }
            }
        }
    }

    <T> void persistColonDelimitedSetToSettingLocked(java.lang.String settingName, int userId, java.util.Set<T> set, java.util.function.Function<T, java.lang.String> toString) {
        persistColonDelimitedSetToSettingLocked(settingName, userId, set, toString, null);
    }

    private <T> void persistColonDelimitedSetToSettingLocked(java.lang.String settingName, int userId, java.util.Set<T> set, java.util.function.Function<T, java.lang.String> toString, java.lang.String defaultEmptyString) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        java.util.Iterator<T> it = set.iterator();
        while (it.hasNext()) {
            T item = it.next();
            java.lang.String str = item != null ? toString.apply(item) : null;
            if (!android.text.TextUtils.isEmpty(str)) {
                if (builder.length() > 0) {
                    builder.append(COMPONENT_NAME_SEPARATOR);
                }
                builder.append(str);
            }
        }
        java.lang.String builderValue = builder.toString();
        java.lang.String settingValue = android.text.TextUtils.isEmpty(builderValue) ? defaultEmptyString : builderValue;
        if (android.view.accessibility.Flags.restoreA11yShortcutTargetService()) {
            java.lang.String currentValue = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), settingName, userId);
            if (java.util.Objects.equals(settingValue, currentValue)) {
                return;
            }
        }
        android.util.Slog.d(LOG_TAG, "put settingName=" + settingName + ", settingValue=" + settingValue + ", userId=" + userId);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), settingName, settingValue, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void persistIntToSetting(int userId, java.lang.String settingName, int settingValue) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), settingName, settingValue, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void updateServicesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int i;
        int count;
        java.util.Map<android.content.ComponentName, com.android.server.accessibility.AccessibilityServiceConnection> componentNameToServiceMap;
        com.android.server.accessibility.AccessibilityManagerService accessibilityManagerService;
        com.android.server.accessibility.AccessibilityUserState accessibilityUserState;
        com.android.server.accessibility.AccessibilityServiceConnection service;
        com.android.server.accessibility.AccessibilityManagerService accessibilityManagerService2 = this;
        com.android.server.accessibility.AccessibilityUserState accessibilityUserState2 = userState;
        java.util.Map<android.content.ComponentName, com.android.server.accessibility.AccessibilityServiceConnection> componentNameToServiceMap2 = accessibilityUserState2.mComponentNameToServiceMap;
        boolean isUnlockingOrUnlocked = accessibilityManagerService2.mUmi.isUserUnlockingOrUnlocked(accessibilityUserState2.mUserId);
        accessibilityManagerService2.mTempComponentNameSet.clear();
        int count2 = accessibilityUserState2.mInstalledServices.size();
        int i2 = 0;
        while (i2 < count2) {
            android.accessibilityservice.AccessibilityServiceInfo installedService = accessibilityUserState2.mInstalledServices.get(i2);
            android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(installedService.getId());
            accessibilityManagerService2.mTempComponentNameSet.add(componentName);
            com.android.server.accessibility.AccessibilityServiceConnection service2 = componentNameToServiceMap2.get(componentName);
            if (!isUnlockingOrUnlocked && !installedService.isDirectBootAware()) {
                android.util.Slog.d(LOG_TAG, "Ignoring non-encryption-aware service " + componentName);
                i = i2;
                count = count2;
                componentNameToServiceMap = componentNameToServiceMap2;
                accessibilityManagerService = accessibilityManagerService2;
                accessibilityUserState = accessibilityUserState2;
            } else if (userState.getBindingServicesLocked().contains(componentName)) {
                i = i2;
                count = count2;
                componentNameToServiceMap = componentNameToServiceMap2;
                accessibilityManagerService = accessibilityManagerService2;
                accessibilityUserState = accessibilityUserState2;
            } else if (userState.getCrashedServicesLocked().contains(componentName)) {
                i = i2;
                count = count2;
                componentNameToServiceMap = componentNameToServiceMap2;
                accessibilityManagerService = accessibilityManagerService2;
                accessibilityUserState = accessibilityUserState2;
            } else if (!accessibilityUserState2.mEnabledServices.contains(componentName) || accessibilityManagerService2.mUiAutomationManager.suppressingAccessibilityServicesLocked()) {
                com.android.server.accessibility.AccessibilityServiceConnection service3 = service2;
                i = i2;
                count = count2;
                componentNameToServiceMap = componentNameToServiceMap2;
                accessibilityUserState = accessibilityUserState2;
                if (service3 == null) {
                    accessibilityManagerService = this;
                } else {
                    service3.unbindLocked();
                    accessibilityManagerService = this;
                    accessibilityManagerService.removeShortcutTargetForUnboundServiceLocked(accessibilityUserState, service3);
                }
            } else if (!accessibilityManagerService2.isAccessibilityTargetAllowed(componentName.getPackageName(), installedService.getResolveInfo().serviceInfo.applicationInfo.uid, accessibilityUserState2.mUserId)) {
                android.util.Slog.d(LOG_TAG, "Skipping enabling service disallowed by device admin policy: " + componentName);
                accessibilityManagerService2.disableAccessibilityServiceLocked(componentName, accessibilityUserState2.mUserId);
                i = i2;
                count = count2;
                componentNameToServiceMap = componentNameToServiceMap2;
                accessibilityManagerService = accessibilityManagerService2;
                accessibilityUserState = accessibilityUserState2;
            } else {
                if (service2 == null) {
                    android.content.Context context = accessibilityManagerService2.mContext;
                    int i3 = sIdCounter;
                    sIdCounter = i3 + 1;
                    android.os.Handler handler = accessibilityManagerService2.mMainHandler;
                    java.lang.Object obj = accessibilityManagerService2.mLock;
                    com.android.server.accessibility.AccessibilitySecurityPolicy accessibilitySecurityPolicy = accessibilityManagerService2.mSecurityPolicy;
                    com.android.server.accessibility.AccessibilityTraceManager traceManager = getTraceManager();
                    com.android.server.wm.WindowManagerInternal windowManagerInternal = accessibilityManagerService2.mWindowManagerService;
                    com.android.server.accessibility.SystemActionPerformer systemActionPerformer = getSystemActionPerformer();
                    com.android.server.accessibility.AccessibilityWindowManager accessibilityWindowManager = accessibilityManagerService2.mA11yWindowManager;
                    com.android.server.wm.ActivityTaskManagerInternal activityTaskManagerInternal = accessibilityManagerService2.mActivityTaskManagerService;
                    i = i2;
                    count = count2;
                    componentNameToServiceMap = componentNameToServiceMap2;
                    accessibilityUserState = accessibilityUserState2;
                    service = new com.android.server.accessibility.AccessibilityServiceConnection(userState, context, componentName, installedService, i3, handler, obj, accessibilitySecurityPolicy, this, traceManager, windowManagerInternal, systemActionPerformer, accessibilityWindowManager, activityTaskManagerInternal);
                } else {
                    i = i2;
                    count = count2;
                    componentNameToServiceMap = componentNameToServiceMap2;
                    accessibilityUserState = accessibilityUserState2;
                    if (!accessibilityUserState.mBoundServices.contains(service2)) {
                        service = service2;
                    } else {
                        accessibilityManagerService = this;
                    }
                }
                service.bindLocked();
                accessibilityManagerService = this;
            }
            i2 = i + 1;
            accessibilityUserState2 = accessibilityUserState;
            componentNameToServiceMap2 = componentNameToServiceMap;
            count2 = count;
            accessibilityManagerService2 = accessibilityManagerService;
        }
        final com.android.server.accessibility.AccessibilityManagerService accessibilityManagerService3 = accessibilityManagerService2;
        com.android.server.accessibility.AccessibilityUserState accessibilityUserState3 = accessibilityUserState2;
        int count3 = accessibilityUserState3.mBoundServices.size();
        accessibilityManagerService3.mTempIntArray.clear();
        for (int i4 = 0; i4 < count3; i4++) {
            android.content.pm.ResolveInfo resolveInfo = accessibilityUserState3.mBoundServices.get(i4).mAccessibilityServiceInfo.getResolveInfo();
            if (resolveInfo != null) {
                accessibilityManagerService3.mTempIntArray.add(resolveInfo.serviceInfo.applicationInfo.uid);
            }
        }
        android.media.AudioManagerInternal audioManager = (android.media.AudioManagerInternal) com.android.server.LocalServices.getService(android.media.AudioManagerInternal.class);
        if (audioManager != null) {
            audioManager.setAccessibilityServiceUids(accessibilityManagerService3.mTempIntArray);
        }
        accessibilityManagerService3.mActivityTaskManagerService.setAccessibilityServiceUids(accessibilityManagerService3.mTempIntArray);
        boolean anyServiceRemoved = accessibilityUserState3.mEnabledServices.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda40
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj2) {
                return this.f$0.lambda$updateServicesLocked$25((android.content.ComponentName) obj2);
            }
        }) || accessibilityUserState3.mTouchExplorationGrantedServices.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda41
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj2) {
                return this.f$0.lambda$updateServicesLocked$26((android.content.ComponentName) obj2);
            }
        });
        if (anyServiceRemoved) {
            accessibilityManagerService3.persistComponentNamesToSettingLocked("enabled_accessibility_services", accessibilityUserState3.mEnabledServices, accessibilityUserState3.mUserId);
            accessibilityManagerService3.persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", accessibilityUserState3.mTouchExplorationGrantedServices, accessibilityUserState3.mUserId);
        }
        updateAccessibilityEnabledSettingLocked(userState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateServicesLocked$25(android.content.ComponentName comp) {
        return !this.mTempComponentNameSet.contains(comp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateServicesLocked$26(android.content.ComponentName comp) {
        return !this.mTempComponentNameSet.contains(comp);
    }

    void scheduleUpdateClientsIfNeededLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        scheduleUpdateClientsIfNeededLocked(userState, false);
    }

    void scheduleUpdateClientsIfNeededLocked(com.android.server.accessibility.AccessibilityUserState userState, boolean forceUpdate) {
        int clientState = getClientStateLocked(userState);
        if (userState.getLastSentClientStateLocked() != clientState || forceUpdate) {
            if (this.mGlobalClients.getRegisteredCallbackCount() > 0 || userState.mUserClients.getRegisteredCallbackCount() > 0) {
                userState.setLastSentClientStateLocked(clientState);
                this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda48
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.accessibility.AccessibilityManagerService) obj).sendStateToAllClients(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
                    }
                }, this, java.lang.Integer.valueOf(clientState), java.lang.Integer.valueOf(userState.mUserId)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStateToAllClients(int clientState, int userId) {
        sendStateToClients(clientState, this.mGlobalClients);
        sendStateToClients(clientState, userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStateToClients(int clientState, int userId) {
        sendStateToClients(clientState, getUserState(userId).mUserClients);
    }

    private void sendStateToClients(final int clientState, android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> clients) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(8L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.sendStateToClients", 8L, "clientState=" + clientState);
        }
        clients.broadcastForEachCookie(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda8
            public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$sendStateToClients$27(clientState, obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendStateToClients$27(int clientState, java.lang.Object client) throws android.os.RemoteException {
        com.android.server.accessibility.AccessibilityManagerService.Client managerClient = (com.android.server.accessibility.AccessibilityManagerService.Client) client;
        if (!this.mProxyManager.isProxyedDeviceId(managerClient.mDeviceId)) {
            managerClient.mCallback.setState(clientState);
        }
    }

    private void scheduleNotifyClientsOfServicesStateChangeLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        updateRecommendedUiTimeoutLocked(userState);
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda34
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).sendServicesStateChanged((android.os.RemoteCallbackList) obj2, ((java.lang.Long) obj3).longValue());
            }
        }, this, userState.mUserClients, java.lang.Long.valueOf(getRecommendedTimeoutMillisLocked(userState))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendServicesStateChanged(android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> userClients, long uiTimeout) {
        notifyClientsOfServicesStateChange(this.mGlobalClients, uiTimeout);
        notifyClientsOfServicesStateChange(userClients, uiTimeout);
    }

    private void notifyClientsOfServicesStateChange(android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> clients, final long uiTimeout) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(8L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.notifyClientsOfServicesStateChange", 8L, "uiTimeout=" + uiTimeout);
        }
        clients.broadcastForEachCookie(com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda19
            public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$notifyClientsOfServicesStateChange$28(uiTimeout, obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyClientsOfServicesStateChange$28(long uiTimeout, java.lang.Object client) throws android.os.RemoteException {
        com.android.server.accessibility.AccessibilityManagerService.Client managerClient = (com.android.server.accessibility.AccessibilityManagerService.Client) client;
        if (!this.mProxyManager.isProxyedDeviceId(managerClient.mDeviceId)) {
            managerClient.mCallback.notifyServicesStateChanged(uiTimeout);
        }
    }

    private void scheduleUpdateInputFilter(com.android.server.accessibility.AccessibilityUserState userState) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda76
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).updateInputFilter((com.android.server.accessibility.AccessibilityUserState) obj2);
            }
        }, this, userState));
    }

    private void scheduleUpdateFingerprintGestureHandling(com.android.server.accessibility.AccessibilityUserState userState) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda79
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).updateFingerprintGestureHandling((com.android.server.accessibility.AccessibilityUserState) obj2);
            }
        }, this, userState));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInputFilter(com.android.server.accessibility.AccessibilityUserState userState) {
        if (this.mUiAutomationManager.suppressingAccessibilityServicesLocked()) {
            return;
        }
        boolean setInputFilter = false;
        android.view.IInputFilter iInputFilter = null;
        synchronized (this.mLock) {
            int flags = 0;
            if (userState.isMagnificationSingleFingerTripleTapEnabledLocked()) {
                flags = 0 | 1;
            }
            if (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture() && userState.isMagnificationTwoFingerTripleTapEnabledLocked()) {
                flags |= 4096;
            }
            if (userState.isShortcutMagnificationEnabledLocked()) {
                flags |= 64;
            }
            if (userHasMagnificationServicesLocked(userState)) {
                flags |= 32;
            }
            if (userState.isHandlingAccessibilityEventsLocked() && userState.isTouchExplorationEnabledLocked()) {
                flags |= 2;
                if (userState.isServiceHandlesDoubleTapEnabledLocked()) {
                    flags |= 128;
                }
                if (userState.isMultiFingerGesturesEnabledLocked()) {
                    flags |= 256;
                }
                if (userState.isTwoFingerPassthroughEnabledLocked()) {
                    flags |= 512;
                }
            }
            if (userState.isFilterKeyEventsEnabledLocked()) {
                flags |= 4;
            }
            if (userState.isSendMotionEventsEnabled()) {
                flags |= 1024;
            }
            if (userState.isAutoclickEnabledLocked()) {
                flags |= 8;
            }
            if (userState.isPerformGesturesEnabledLocked()) {
                flags |= 16;
            }
            int combinedGenericMotionEventSources = 0;
            int combinedMotionEventObservedSources = 0;
            for (com.android.server.accessibility.AccessibilityServiceConnection connection : userState.mBoundServices) {
                combinedGenericMotionEventSources |= connection.mGenericMotionEventSources;
                combinedMotionEventObservedSources |= connection.mObservedMotionEventSources;
            }
            if (combinedGenericMotionEventSources != 0) {
                flags |= 2048;
            }
            if (DEBUG) {
                android.util.Slog.d(LOG_TAG, "[updateInputFilter] flags = " + flags + ", mInputFilter=" + this.mInputFilter + ", mHasInputFilter=" + this.mHasInputFilter);
            }
            if (flags != 0) {
                if (!this.mHasInputFilter) {
                    this.mHasInputFilter = true;
                    if (this.mInputFilter == null) {
                        this.mInputFilter = new com.android.server.accessibility.AccessibilityInputFilter(this.mContext, this);
                    }
                    iInputFilter = this.mInputFilter;
                    setInputFilter = true;
                }
                this.mInputFilter.setUserAndEnabledFeatures(userState.mUserId, flags);
                this.mInputFilter.setCombinedGenericMotionEventSources(combinedGenericMotionEventSources);
                this.mInputFilter.setCombinedMotionEventObservedSources(combinedMotionEventObservedSources);
            } else if (this.mHasInputFilter) {
                this.mHasInputFilter = false;
                this.mInputFilter.setUserAndEnabledFeatures(userState.mUserId, 0);
                this.mInputFilter.resetServiceDetectsGestures();
                if (userState.isTouchExplorationEnabledLocked()) {
                    java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
                    for (android.view.Display display : displays) {
                        int displayId = display.getDisplayId();
                        boolean mode = userState.isServiceDetectsGesturesEnabled(displayId);
                        this.mInputFilter.setServiceDetectsGesturesEnabled(displayId, mode);
                    }
                }
                iInputFilter = null;
                setInputFilter = true;
            }
            this.mServiceExt.updateInputFilter(flags);
        }
        if (setInputFilter) {
            if (this.mTraceManager.isA11yTracingEnabledForTypes(4608L)) {
                this.mTraceManager.logTrace("WindowManagerInternal.setInputFilter", 4608L, "inputFilter=" + iInputFilter);
            }
            this.mWindowManagerService.setInputFilter(iInputFilter);
            this.mProxyManager.setAccessibilityInputFilter(iInputFilter);
        }
        synchronized (this.mLock) {
            this.mServiceExt.onUserStateChangedLocked(userState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEnableTouchExplorationDialog(final com.android.server.accessibility.AccessibilityServiceConnection service) {
        synchronized (this.mLock) {
            java.lang.String label = service.getServiceInfo().getResolveInfo().loadLabel(this.mContext.getPackageManager()).toString();
            final com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            if (userState.isTouchExplorationEnabledLocked()) {
                return;
            }
            if (this.mEnableTouchExplorationDialog == null || !this.mEnableTouchExplorationDialog.isShowing()) {
                this.mEnableTouchExplorationDialog = new android.app.AlertDialog.Builder(this.mContext).setIconAttribute(android.R.attr.alertDialogIcon).setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.accessibility.AccessibilityManagerService.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        userState.mTouchExplorationGrantedServices.add(service.mComponentName);
                        com.android.server.accessibility.AccessibilityManagerService.this.persistComponentNamesToSettingLocked("touch_exploration_granted_accessibility_services", userState.mTouchExplorationGrantedServices, userState.mUserId);
                        userState.setTouchExplorationEnabledLocked(true);
                        long identity = android.os.Binder.clearCallingIdentity();
                        try {
                            android.provider.Settings.Secure.putIntForUser(com.android.server.accessibility.AccessibilityManagerService.this.mContext.getContentResolver(), "touch_exploration_enabled", 1, userState.mUserId);
                            android.os.Binder.restoreCallingIdentity(identity);
                            com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                        } catch (java.lang.Throwable th) {
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw th;
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.accessibility.AccessibilityManagerService.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle(android.R.string.error_handwriting_unsupported).setMessage(this.mContext.getString(android.R.string.enable_explore_by_touch_warning_title, label)).create();
                this.mEnableTouchExplorationDialog.getWindow().setType(2003);
                this.mEnableTouchExplorationDialog.getWindow().getAttributes().privateFlags |= 16;
                this.mEnableTouchExplorationDialog.setCanceledOnTouchOutside(true);
                this.mEnableTouchExplorationDialog.show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onUserVisibilityChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0(int userId, boolean visible) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(LOG_TAG, "onUserVisibilityChanged(): %d => %b", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(visible));
        }
        synchronized (this.mLock) {
            if (visible) {
                this.mVisibleBgUserIds.put(userId, visible);
            } else {
                this.mVisibleBgUserIds.delete(userId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStateChangedLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        onUserStateChangedLocked(userState, false);
    }

    private void onUserStateChangedLocked(com.android.server.accessibility.AccessibilityUserState userState, boolean forceUpdate) {
        android.util.Slog.v(LOG_TAG, "onUserStateChangedLocked for user " + userState.mUserId + " with forceUpdate: " + forceUpdate + " mEnabledServices = " + userState.mEnabledServices + " mCrashedServices = " + userState.mCrashedServices);
        this.mInitialized = true;
        updateLegacyCapabilitiesLocked(userState);
        updateServicesLocked(userState);
        updateWindowsForAccessibilityCallbackLocked(userState);
        updateFilterKeyEventsLocked(userState);
        updateTouchExplorationLocked(userState);
        updatePerformGesturesLocked(userState);
        updateMagnificationLocked(userState);
        scheduleUpdateFingerprintGestureHandling(userState);
        scheduleUpdateInputFilter(userState);
        updateRelevantEventsLocked(userState);
        scheduleUpdateClientsIfNeededLocked(userState, forceUpdate);
        updateAccessibilityShortcutKeyTargetsLocked(userState);
        updateAccessibilityButtonTargetsLocked(userState);
        updateAccessibilityQsTargetsLocked(userState);
        updateMagnificationCapabilitiesSettingsChangeLocked(userState);
        updateMagnificationModeChangeSettingsForAllDisplaysLocked(userState);
        updateFocusAppearanceDataLocked(userState);
    }

    private void updateMagnificationModeChangeSettingsForAllDisplaysLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
        for (int i = 0; i < displays.size(); i++) {
            int displayId = displays.get(i).getDisplayId();
            updateMagnificationModeChangeSettingsLocked(userState, displayId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWindowsForAccessibilityCallbackLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean observingWindows = this.mUiAutomationManager.canRetrieveInteractiveWindowsLocked() || this.mProxyManager.canRetrieveInteractiveWindowsLocked();
        java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> boundServices = userState.mBoundServices;
        int boundServiceCount = boundServices.size();
        for (int i = 0; !observingWindows && i < boundServiceCount; i++) {
            com.android.server.accessibility.AccessibilityServiceConnection boundService = boundServices.get(i);
            if (boundService.canRetrieveInteractiveWindowsLocked()) {
                userState.setAccessibilityFocusOnlyInActiveWindow(false);
                observingWindows = true;
            }
        }
        userState.setAccessibilityFocusOnlyInActiveWindow(true);
        java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
        for (int i2 = 0; i2 < displays.size(); i2++) {
            android.view.Display display = displays.get(i2);
            if (display != null) {
                if (observingWindows) {
                    this.mA11yWindowManager.startTrackingWindows(display.getDisplayId(), this.mProxyManager.isProxyedDisplay(display.getDisplayId()));
                } else {
                    this.mA11yWindowManager.stopTrackingWindows(display.getDisplayId());
                }
            }
        }
    }

    private void updateLegacyCapabilitiesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int installedServiceCount = userState.mInstalledServices.size();
        for (int i = 0; i < installedServiceCount; i++) {
            android.accessibilityservice.AccessibilityServiceInfo serviceInfo = userState.mInstalledServices.get(i);
            android.content.pm.ResolveInfo resolveInfo = serviceInfo.getResolveInfo();
            if ((serviceInfo.getCapabilities() & 2) == 0 && resolveInfo.serviceInfo.applicationInfo.targetSdkVersion <= 17) {
                android.content.ComponentName componentName = new android.content.ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                if (userState.mTouchExplorationGrantedServices.contains(componentName)) {
                    serviceInfo.setCapabilities(serviceInfo.getCapabilities() | 2);
                }
            }
        }
    }

    private void updatePerformGesturesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int serviceCount = userState.mBoundServices.size();
        for (int i = 0; i < serviceCount; i++) {
            com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
            if ((service.getCapabilities() & 32) != 0) {
                userState.setPerformGesturesEnabledLocked(true);
                return;
            }
        }
        userState.setPerformGesturesEnabledLocked(false);
    }

    private void updateFilterKeyEventsLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int serviceCount = userState.mBoundServices.size();
        for (int i = 0; i < serviceCount; i++) {
            com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
            if (service.mRequestFilterKeyEvents && (service.getCapabilities() & 8) != 0) {
                userState.setFilterKeyEventsEnabledLocked(true);
                return;
            }
        }
        userState.setFilterKeyEventsEnabledLocked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readConfigurationForUserStateLocked(com.android.server.accessibility.AccessibilityUserState userState, java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parsedAccessibilityServiceInfos, java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parsedAccessibilityShortcutInfos) {
        boolean somethingChanged = readInstalledAccessibilityServiceLocked(userState, parsedAccessibilityServiceInfos);
        return somethingChanged | readInstalledAccessibilityShortcutLocked(userState, parsedAccessibilityShortcutInfos) | readEnabledAccessibilityServicesLocked(userState) | readTouchExplorationGrantedAccessibilityServicesLocked(userState) | readTouchExplorationEnabledSettingLocked(userState) | readHighTextContrastEnabledSettingLocked(userState) | readAudioDescriptionEnabledSettingLocked(userState) | readMagnificationEnabledSettingsLocked(userState) | readAutoclickEnabledSettingLocked(userState) | readAccessibilityShortcutKeySettingLocked(userState) | readAccessibilityQsTargetsLocked(userState) | readAccessibilityButtonTargetsLocked(userState) | readAccessibilityButtonTargetComponentLocked(userState) | readUserRecommendedUiTimeoutSettingsLocked(userState) | readMagnificationModeForDefaultDisplayLocked(userState) | readMagnificationCapabilitiesLocked(userState) | readMagnificationFollowTypingLocked(userState) | readAlwaysOnMagnificationLocked(userState);
    }

    private void updateAccessibilityEnabledSettingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int currentAccessibilityState;
        boolean isA11yEnabled = this.mUiAutomationManager.canIntrospect() || userState.isHandlingAccessibilityEventsLocked();
        long identity = android.os.Binder.clearCallingIdentity();
        int setAccessibilityState = isA11yEnabled ? 1 : 0;
        try {
            try {
                currentAccessibilityState = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_enabled", userState.mUserId);
            } catch (android.provider.Settings.SettingNotFoundException e) {
                android.util.Log.w(LOG_TAG, "currentAccessibilityState is null");
                currentAccessibilityState = setAccessibilityState;
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "accessibility_enabled", setAccessibilityState, userState.mUserId);
            }
            if (currentAccessibilityState != setAccessibilityState) {
                android.util.Slog.i(LOG_TAG, "updateAccessibilityEnabledSettingLocked:" + isA11yEnabled);
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "accessibility_enabled", setAccessibilityState, userState.mUserId);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readTouchExplorationEnabledSettingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean touchExplorationEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "touch_exploration_enabled", 0, userState.mUserId) == 1;
        if (touchExplorationEnabled == userState.isTouchExplorationEnabledLocked()) {
            return false;
        }
        userState.setTouchExplorationEnabledLocked(touchExplorationEnabled);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readMagnificationEnabledSettingsLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean magnificationSingleFingerTripleTapEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_display_magnification_enabled", 0, userState.mUserId) == 1;
        if (magnificationSingleFingerTripleTapEnabled == userState.isMagnificationSingleFingerTripleTapEnabledLocked()) {
            return false;
        }
        userState.setMagnificationSingleFingerTripleTapEnabledLocked(magnificationSingleFingerTripleTapEnabled);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readMagnificationTwoFingerTripleTapSettingsLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean magnificationTwoFingerTripleTapEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_two_finger_triple_tap_enabled", 0, userState.mUserId) == 1;
        if (magnificationTwoFingerTripleTapEnabled == userState.isMagnificationTwoFingerTripleTapEnabledLocked()) {
            return false;
        }
        userState.setMagnificationTwoFingerTripleTapEnabledLocked(magnificationTwoFingerTripleTapEnabled);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readAutoclickEnabledSettingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean autoclickEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_autoclick_enabled", 0, userState.mUserId) == 1;
        if (autoclickEnabled == userState.isAutoclickEnabledLocked()) {
            return false;
        }
        userState.setAutoclickEnabledLocked(autoclickEnabled);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readHighTextContrastEnabledSettingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean highTextContrastEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "high_text_contrast_enabled", 0, userState.mUserId) == 1;
        if (highTextContrastEnabled == userState.isTextHighContrastEnabledLocked()) {
            return false;
        }
        userState.setTextHighContrastEnabledLocked(highTextContrastEnabled);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readAudioDescriptionEnabledSettingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean audioDescriptionByDefaultEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "enabled_accessibility_audio_description_by_default", 0, userState.mUserId) == 1;
        if (audioDescriptionByDefaultEnabled == userState.isAudioDescriptionByDefaultEnabledLocked()) {
            return false;
        }
        userState.setAudioDescriptionByDefaultEnabledLocked(audioDescriptionByDefaultEnabled);
        return true;
    }

    private void updateTouchExplorationLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean touchExplorationEnabled = this.mUiAutomationManager.isTouchExplorationEnabledLocked();
        boolean serviceHandlesDoubleTapEnabled = false;
        boolean requestMultiFingerGestures = false;
        boolean requestTwoFingerPassthrough = false;
        boolean sendMotionEvents = false;
        int serviceCount = userState.mBoundServices.size();
        int i = 0;
        while (true) {
            if (i >= serviceCount) {
                break;
            }
            com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
            if (!canRequestAndRequestsTouchExplorationLocked(service, userState)) {
                i++;
            } else {
                touchExplorationEnabled = true;
                serviceHandlesDoubleTapEnabled = service.isServiceHandlesDoubleTapEnabled();
                requestMultiFingerGestures = service.isMultiFingerGesturesEnabled();
                requestTwoFingerPassthrough = service.isTwoFingerPassthroughEnabled();
                sendMotionEvents = service.isSendMotionEventsEnabled();
                break;
            }
        }
        if (touchExplorationEnabled != userState.isTouchExplorationEnabledLocked()) {
            userState.setTouchExplorationEnabledLocked(touchExplorationEnabled);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "touch_exploration_enabled", touchExplorationEnabled ? 1 : 0, userState.mUserId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
        userState.resetServiceDetectsGestures();
        java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
        for (com.android.server.accessibility.AccessibilityServiceConnection service2 : userState.mBoundServices) {
            for (android.view.Display display : displays) {
                int displayId = display.getDisplayId();
                if (service2.isServiceDetectsGesturesEnabled(displayId)) {
                    userState.setServiceDetectsGesturesEnabled(displayId, true);
                }
            }
        }
        userState.setServiceHandlesDoubleTapLocked(serviceHandlesDoubleTapEnabled);
        userState.setMultiFingerGesturesLocked(requestMultiFingerGestures);
        userState.setTwoFingerPassthroughLocked(requestTwoFingerPassthrough);
        userState.setSendMotionEventsEnabled(sendMotionEvents);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readAccessibilityShortcutKeySettingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        java.lang.String settingValue = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "accessibility_shortcut_target_service", userState.mUserId);
        java.util.Set<java.lang.String> targetsFromSetting = new android.util.ArraySet<>();
        readColonDelimitedStringToSet(settingValue, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda46
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$readAccessibilityShortcutKeySettingLocked$29((java.lang.String) obj);
            }
        }, targetsFromSetting, false);
        if (settingValue == null) {
            java.lang.String defaultService = this.mContext.getString(android.R.string.config_defaultContentCaptureService);
            if (!android.text.TextUtils.isEmpty(defaultService)) {
                targetsFromSetting.add(defaultService);
            }
        }
        java.util.Set<java.lang.String> currentTargets = userState.getShortcutTargetsLocked(2);
        if (targetsFromSetting.equals(currentTargets)) {
            return false;
        }
        currentTargets.clear();
        currentTargets.addAll(targetsFromSetting);
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        return true;
    }

    static /* synthetic */ java.lang.String lambda$readAccessibilityShortcutKeySettingLocked$29(java.lang.String str) {
        return str;
    }

    private boolean readAccessibilityQsTargetsLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        android.util.ArraySet arraySet = new android.util.ArraySet();
        readColonDelimitedSettingToSet("accessibility_qs_targets", userState.mUserId, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda74
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$readAccessibilityQsTargetsLocked$30((java.lang.String) obj);
            }
        }, arraySet);
        java.util.Set<java.lang.String> currentTargets = userState.getShortcutTargetsLocked(16);
        if (arraySet.equals(currentTargets)) {
            return false;
        }
        userState.updateA11yQsTargetLocked(arraySet);
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        return true;
    }

    static /* synthetic */ java.lang.String lambda$readAccessibilityQsTargetsLocked$30(java.lang.String str) {
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readAccessibilityButtonTargetsLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        java.util.Set<java.lang.String> targetsFromSetting = new android.util.ArraySet<>();
        readColonDelimitedSettingToSet("accessibility_button_targets", userState.mUserId, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$readAccessibilityButtonTargetsLocked$31((java.lang.String) obj);
            }
        }, targetsFromSetting);
        java.util.Set<java.lang.String> currentTargets = userState.getShortcutTargetsLocked(1);
        if (targetsFromSetting.equals(currentTargets)) {
            return false;
        }
        currentTargets.clear();
        currentTargets.addAll(targetsFromSetting);
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        return true;
    }

    static /* synthetic */ java.lang.String lambda$readAccessibilityButtonTargetsLocked$31(java.lang.String str) {
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readAccessibilityButtonTargetComponentLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        java.lang.String componentId = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "accessibility_button_target_component", userState.mUserId);
        if (android.text.TextUtils.isEmpty(componentId)) {
            if (userState.getTargetAssignedToAccessibilityButton() == null) {
                return false;
            }
            userState.setTargetAssignedToAccessibilityButton(null);
            return true;
        }
        if (componentId.equals(userState.getTargetAssignedToAccessibilityButton())) {
            return false;
        }
        userState.setTargetAssignedToAccessibilityButton(componentId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readUserRecommendedUiTimeoutSettingsLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int nonInteractiveUiTimeout = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_non_interactive_ui_timeout_ms", 0, userState.mUserId);
        int interactiveUiTimeout = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_interactive_ui_timeout_ms", 0, userState.mUserId);
        this.mProxyManager.updateTimeoutsIfNeeded(nonInteractiveUiTimeout, interactiveUiTimeout);
        if (nonInteractiveUiTimeout == userState.getUserNonInteractiveUiTimeoutLocked() && interactiveUiTimeout == userState.getUserInteractiveUiTimeoutLocked()) {
            return false;
        }
        userState.setUserNonInteractiveUiTimeoutLocked(nonInteractiveUiTimeout);
        userState.setUserInteractiveUiTimeoutLocked(interactiveUiTimeout);
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        return true;
    }

    private void updateAccessibilityShortcutKeyTargetsLocked(final com.android.server.accessibility.AccessibilityUserState userState) {
        android.util.ArraySet<java.lang.String> shortcutTargetsLocked = userState.getShortcutTargetsLocked(2);
        int lastSize = shortcutTargetsLocked.size();
        if (lastSize == 0) {
            return;
        }
        shortcutTargetsLocked.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda77
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateAccessibilityShortcutKeyTargetsLocked$32(userState, (java.lang.String) obj);
            }
        });
        if (lastSize == shortcutTargetsLocked.size()) {
            return;
        }
        persistColonDelimitedSetToSettingLocked("accessibility_shortcut_target_service", userState.mUserId, shortcutTargetsLocked, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda78
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateAccessibilityShortcutKeyTargetsLocked$33((java.lang.String) obj);
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
    }

    static /* synthetic */ boolean lambda$updateAccessibilityShortcutKeyTargetsLocked$32(com.android.server.accessibility.AccessibilityUserState userState, java.lang.String name) {
        return !userState.isShortcutTargetInstalledLocked(name);
    }

    static /* synthetic */ java.lang.String lambda$updateAccessibilityShortcutKeyTargetsLocked$33(java.lang.String str) {
        return str;
    }

    private boolean canRequestAndRequestsTouchExplorationLocked(com.android.server.accessibility.AccessibilityServiceConnection service, com.android.server.accessibility.AccessibilityUserState userState) {
        if (!service.canReceiveEventsLocked() || !service.mRequestTouchExplorationMode) {
            return false;
        }
        if (service.getServiceInfo().getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 17) {
            if (userState.mTouchExplorationGrantedServices.contains(service.mComponentName)) {
                return true;
            }
            if (this.mEnableTouchExplorationDialog == null || !this.mEnableTouchExplorationDialog.isShowing()) {
                this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda80
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.accessibility.AccessibilityManagerService) obj).showEnableTouchExplorationDialog((com.android.server.accessibility.AccessibilityServiceConnection) obj2);
                    }
                }, this, service));
            }
        } else if ((service.getCapabilities() & 2) != 0) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMagnificationLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        if (userState.mUserId != this.mCurrentUserId) {
            return;
        }
        if (this.mUiAutomationManager.suppressingAccessibilityServicesLocked() && this.mMagnificationController.isFullScreenMagnificationControllerInitialized()) {
            getMagnificationController().getFullScreenMagnificationController().unregisterAll();
            return;
        }
        java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
        if (userState.isMagnificationSingleFingerTripleTapEnabledLocked() || ((com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture() && userState.isMagnificationTwoFingerTripleTapEnabledLocked()) || userState.isShortcutMagnificationEnabledLocked())) {
            for (int i = 0; i < displays.size(); i++) {
                android.view.Display display = displays.get(i);
                getMagnificationController().getFullScreenMagnificationController().register(display.getDisplayId());
            }
            return;
        }
        for (int i2 = 0; i2 < displays.size(); i2++) {
            android.view.Display display2 = displays.get(i2);
            int displayId = display2.getDisplayId();
            if (userHasListeningMagnificationServicesLocked(userState, displayId)) {
                getMagnificationController().getFullScreenMagnificationController().register(displayId);
            } else if (this.mMagnificationController.isFullScreenMagnificationControllerInitialized()) {
                getMagnificationController().getFullScreenMagnificationController().unregister(displayId);
            }
        }
    }

    private void updateMagnificationConnectionIfNeeded(com.android.server.accessibility.AccessibilityUserState userState) {
        if (!this.mMagnificationController.supportWindowMagnification()) {
            return;
        }
        boolean shortcutEnabled = userState.isShortcutMagnificationEnabledLocked() || userState.isMagnificationSingleFingerTripleTapEnabledLocked() || (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture() && userState.isMagnificationTwoFingerTripleTapEnabledLocked());
        boolean createConnectionForCurrentCapability = com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder() || userState.getMagnificationCapabilitiesLocked() != 1;
        boolean connect = (shortcutEnabled && createConnectionForCurrentCapability) || userHasMagnificationServicesLocked(userState);
        getMagnificationConnectionManager().requestConnection(connect);
    }

    private boolean userHasMagnificationServicesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = userState.mBoundServices;
        int count = services.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityServiceConnection service = services.get(i);
            if (this.mSecurityPolicy.canControlMagnification(service)) {
                return true;
            }
        }
        return false;
    }

    private boolean userHasListeningMagnificationServicesLocked(com.android.server.accessibility.AccessibilityUserState userState, int displayId) {
        java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = userState.mBoundServices;
        int count = services.size();
        for (int i = 0; i < count; i++) {
            com.android.server.accessibility.AccessibilityServiceConnection service = services.get(i);
            if (this.mSecurityPolicy.canControlMagnification(service) && service.isMagnificationCallbackEnabled(displayId)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFingerprintGestureHandling(com.android.server.accessibility.AccessibilityUserState userState) {
        java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services;
        synchronized (this.mLock) {
            services = userState.mBoundServices;
            if (this.mFingerprintGestureDispatcher == null && this.mPackageManager.hasSystemFeature("android.hardware.fingerprint")) {
                int numServices = services.size();
                int i = 0;
                while (true) {
                    if (i >= numServices) {
                        break;
                    }
                    if (services.get(i).isCapturingFingerprintGestures()) {
                        long identity = android.os.Binder.clearCallingIdentity();
                        try {
                            android.hardware.fingerprint.IFingerprintService service = android.hardware.fingerprint.IFingerprintService.Stub.asInterface(android.os.ServiceManager.getService("fingerprint"));
                            if (service != null) {
                                this.mFingerprintGestureDispatcher = new com.android.server.accessibility.FingerprintGestureDispatcher(service, this.mContext.getResources(), this.mLock);
                                break;
                            }
                        } finally {
                            android.os.Binder.restoreCallingIdentity(identity);
                        }
                    }
                    i++;
                }
            }
        }
        if (this.mFingerprintGestureDispatcher != null) {
            this.mFingerprintGestureDispatcher.updateClientList(services);
        }
    }

    private void updateAccessibilityButtonTargetsLocked(final com.android.server.accessibility.AccessibilityUserState userState) {
        for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
            if (service.mRequestAccessibilityButton) {
                service.notifyAccessibilityButtonAvailabilityChangedLocked(service.isAccessibilityButtonAvailableLocked(userState));
            }
        }
        android.util.ArraySet<java.lang.String> shortcutTargetsLocked = userState.getShortcutTargetsLocked(1);
        int lastSize = shortcutTargetsLocked.size();
        if (lastSize == 0) {
            return;
        }
        shortcutTargetsLocked.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda65
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateAccessibilityButtonTargetsLocked$34(userState, (java.lang.String) obj);
            }
        });
        if (lastSize == shortcutTargetsLocked.size()) {
            return;
        }
        persistColonDelimitedSetToSettingLocked("accessibility_button_targets", userState.mUserId, shortcutTargetsLocked, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda66
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateAccessibilityButtonTargetsLocked$35((java.lang.String) obj);
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
    }

    static /* synthetic */ boolean lambda$updateAccessibilityButtonTargetsLocked$34(com.android.server.accessibility.AccessibilityUserState userState, java.lang.String name) {
        return !userState.isShortcutTargetInstalledLocked(name);
    }

    static /* synthetic */ java.lang.String lambda$updateAccessibilityButtonTargetsLocked$35(java.lang.String str) {
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void migrateAccessibilityButtonSettingsIfNecessaryLocked(final com.android.server.accessibility.AccessibilityUserState userState, final java.lang.String packageName, int restoreFromSdkInt) {
        if (restoreFromSdkInt <= 29) {
            final android.util.ArraySet<java.lang.String> shortcutTargetsLocked = userState.getShortcutTargetsLocked(1);
            int lastSize = shortcutTargetsLocked.size();
            shortcutTargetsLocked.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$36(packageName, userState, (java.lang.String) obj);
                }
            });
            boolean changed = lastSize != shortcutTargetsLocked.size();
            int lastSize2 = shortcutTargetsLocked.size();
            final java.util.Set<java.lang.String> shortcutKeyTargets = userState.getShortcutTargetsLocked(2);
            final java.util.Set<java.lang.String> qsShortcutTargets = userState.getShortcutTargetsLocked(16);
            userState.mEnabledServices.forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.accessibility.AccessibilityManagerService.lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$37(packageName, userState, shortcutTargetsLocked, shortcutKeyTargets, qsShortcutTargets, (android.content.ComponentName) obj);
                }
            });
            if (!(changed | (lastSize2 != shortcutTargetsLocked.size()))) {
                return;
            }
            persistColonDelimitedSetToSettingLocked("accessibility_button_targets", userState.mUserId, shortcutTargetsLocked, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda7
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.AccessibilityManagerService.lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$38((java.lang.String) obj);
                }
            });
            scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        }
    }

    static /* synthetic */ boolean lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$36(java.lang.String packageName, com.android.server.accessibility.AccessibilityUserState userState, java.lang.String name) {
        android.content.ComponentName componentName;
        android.accessibilityservice.AccessibilityServiceInfo serviceInfo;
        if ((packageName != null && name != null && !name.contains(packageName)) || (componentName = android.content.ComponentName.unflattenFromString(name)) == null || (serviceInfo = userState.getInstalledServiceInfoLocked(componentName)) == null) {
            return false;
        }
        if (serviceInfo.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29) {
            android.util.Slog.v(LOG_TAG, "Legacy service " + componentName + " should not in the button");
            return true;
        }
        boolean requestA11yButton = (serviceInfo.flags & 256) != 0;
        if (!requestA11yButton || userState.mEnabledServices.contains(componentName)) {
            return false;
        }
        android.util.Slog.v(LOG_TAG, "Service requesting a11y button and be assigned to the button" + componentName + " should be enabled state");
        return true;
    }

    static /* synthetic */ void lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$37(java.lang.String packageName, com.android.server.accessibility.AccessibilityUserState userState, java.util.Set buttonTargets, java.util.Set shortcutKeyTargets, java.util.Set qsShortcutTargets, android.content.ComponentName componentName) {
        android.accessibilityservice.AccessibilityServiceInfo serviceInfo;
        if ((packageName != null && componentName != null && !packageName.equals(componentName.getPackageName())) || (serviceInfo = userState.getInstalledServiceInfoLocked(componentName)) == null) {
            return;
        }
        boolean requestA11yButton = (serviceInfo.flags & 256) != 0;
        if (serviceInfo.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29 || !requestA11yButton) {
            return;
        }
        java.lang.String serviceName = componentName.flattenToString();
        if (android.text.TextUtils.isEmpty(serviceName) || com.android.server.accessibility.AccessibilityUserState.doesShortcutTargetsStringContain(buttonTargets, serviceName) || com.android.server.accessibility.AccessibilityUserState.doesShortcutTargetsStringContain(shortcutKeyTargets, serviceName) || com.android.server.accessibility.AccessibilityUserState.doesShortcutTargetsStringContain(qsShortcutTargets, serviceName)) {
            return;
        }
        android.util.Slog.v(LOG_TAG, "A enabled service requesting a11y button " + componentName + " should be assign to the button or shortcut.");
        buttonTargets.add(serviceName);
    }

    static /* synthetic */ java.lang.String lambda$migrateAccessibilityButtonSettingsIfNecessaryLocked$38(java.lang.String str) {
        return str;
    }

    private void updateAccessibilityQsTargetsLocked(final com.android.server.accessibility.AccessibilityUserState userState) {
        if (!android.view.accessibility.Flags.a11yQsShortcut()) {
            return;
        }
        android.util.ArraySet<java.lang.String> shortcutTargetsLocked = userState.getShortcutTargetsLocked(16);
        boolean somethingChanged = shortcutTargetsLocked.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda55
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateAccessibilityQsTargetsLocked$39(userState, (java.lang.String) obj);
            }
        });
        java.util.Set<android.content.ComponentName> enabledServices = userState.getEnabledServicesLocked();
        java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTileService = userState.getA11yFeatureToTileService();
        java.util.Set<android.content.ComponentName> currentA11yTilesInQsPanel = userState.getA11yQsTilesInQsPanel();
        for (android.content.ComponentName enabledService : enabledServices) {
            android.content.ComponentName tileService = a11yFeatureToTileService.getOrDefault(enabledService, null);
            if (tileService != null && currentA11yTilesInQsPanel.contains(tileService)) {
                somethingChanged |= shortcutTargetsLocked.add(enabledService.flattenToString());
            }
        }
        if (!somethingChanged) {
            return;
        }
        userState.updateA11yQsTargetLocked(shortcutTargetsLocked);
        persistColonDelimitedSetToSettingLocked("accessibility_qs_targets", userState.mUserId, shortcutTargetsLocked, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda56
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateAccessibilityQsTargetsLocked$40((java.lang.String) obj);
            }
        });
        scheduleNotifyClientsOfServicesStateChangeLocked(userState);
    }

    static /* synthetic */ boolean lambda$updateAccessibilityQsTargetsLocked$39(com.android.server.accessibility.AccessibilityUserState userState, java.lang.String name) {
        return !userState.isShortcutTargetInstalledLocked(name);
    }

    static /* synthetic */ java.lang.String lambda$updateAccessibilityQsTargetsLocked$40(java.lang.String str) {
        return str;
    }

    private void removeShortcutTargetForUnboundServiceLocked(com.android.server.accessibility.AccessibilityUserState userState, com.android.server.accessibility.AccessibilityServiceConnection service) {
        if (!service.mRequestAccessibilityButton || service.getServiceInfo().getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29) {
            return;
        }
        java.util.List<android.util.Pair<java.lang.Integer, java.lang.String>> shortcutTypeAndShortcutSetting = new java.util.ArrayList<>(3);
        shortcutTypeAndShortcutSetting.add(new android.util.Pair<>(2, "accessibility_shortcut_target_service"));
        shortcutTypeAndShortcutSetting.add(new android.util.Pair<>(1, "accessibility_button_targets"));
        if (android.view.accessibility.Flags.a11yQsShortcut()) {
            shortcutTypeAndShortcutSetting.add(new android.util.Pair<>(16, "accessibility_qs_targets"));
        }
        android.content.ComponentName serviceName = service.getComponentName();
        for (android.util.Pair<java.lang.Integer, java.lang.String> shortcutTypePair : shortcutTypeAndShortcutSetting) {
            int shortcutType = ((java.lang.Integer) shortcutTypePair.first).intValue();
            java.lang.String shortcutSettingName = (java.lang.String) shortcutTypePair.second;
            if (userState.removeShortcutTargetLocked(shortcutType, serviceName)) {
                persistColonDelimitedSetToSettingLocked(shortcutSettingName, userState.mUserId, userState.getShortcutTargetsLocked(shortcutType), new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda17
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.accessibility.AccessibilityManagerService.lambda$removeShortcutTargetForUnboundServiceLocked$41((java.lang.String) obj);
                    }
                });
                if (shortcutType == 16) {
                    android.content.ComponentName tileService = userState.getA11yFeatureToTileService().getOrDefault(serviceName, null);
                    com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
                    if (statusBarManagerInternal != null && tileService != null) {
                        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda18
                            @Override // java.util.function.BiConsumer
                            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                                ((com.android.server.statusbar.StatusBarManagerInternal) obj).removeQsTile((android.content.ComponentName) obj2);
                            }
                        }, statusBarManagerInternal, tileService));
                    }
                }
            }
        }
    }

    static /* synthetic */ java.lang.String lambda$removeShortcutTargetForUnboundServiceLocked$41(java.lang.String str) {
        return str;
    }

    private void updateRecommendedUiTimeoutLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int newNonInteractiveUiTimeout = userState.getUserNonInteractiveUiTimeoutLocked();
        int newInteractiveUiTimeout = userState.getUserInteractiveUiTimeoutLocked();
        if (newNonInteractiveUiTimeout == 0 || newInteractiveUiTimeout == 0) {
            int serviceNonInteractiveUiTimeout = 0;
            int serviceInteractiveUiTimeout = 0;
            java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = userState.mBoundServices;
            for (int i = 0; i < services.size(); i++) {
                int timeout = services.get(i).getServiceInfo().getInteractiveUiTimeoutMillis();
                if (serviceInteractiveUiTimeout < timeout) {
                    serviceInteractiveUiTimeout = timeout;
                }
                int timeout2 = services.get(i).getServiceInfo().getNonInteractiveUiTimeoutMillis();
                if (serviceNonInteractiveUiTimeout < timeout2) {
                    serviceNonInteractiveUiTimeout = timeout2;
                }
            }
            if (newNonInteractiveUiTimeout == 0) {
                newNonInteractiveUiTimeout = serviceNonInteractiveUiTimeout;
            }
            if (newInteractiveUiTimeout == 0) {
                newInteractiveUiTimeout = serviceInteractiveUiTimeout;
            }
        }
        userState.setNonInteractiveUiTimeoutLocked(newNonInteractiveUiTimeout);
        userState.setInteractiveUiTimeoutLocked(newInteractiveUiTimeout);
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public com.android.server.accessibility.KeyEventDispatcher getKeyEventDispatcher() {
        if (this.mKeyEventDispatcher == null) {
            this.mKeyEventDispatcher = new com.android.server.accessibility.KeyEventDispatcher(this.mMainHandler, 8, this.mLock, this.mPowerManager);
        }
        return this.mKeyEventDispatcher;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public android.app.PendingIntent getPendingIntentActivity(android.content.Context context, int requestCode, android.content.Intent intent, int flags) {
        return android.app.PendingIntent.getActivity(context, requestCode, intent, flags);
    }

    public void performAccessibilityShortcut(java.lang.String targetName) {
        performAccessibilityShortcut_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.performAccessibilityShortcut", 4L, "targetName=" + targetName);
        }
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda39(), this, 0, 2, targetName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performAccessibilityShortcutInternal(int displayId, int shortcutType, java.lang.String targetName) {
        java.util.List<java.lang.String> shortcutTargets = getAccessibilityShortcutTargetsInternal(shortcutType);
        if (shortcutTargets.isEmpty()) {
            android.util.Slog.d(LOG_TAG, "No target to perform shortcut, shortcutType=" + shortcutType);
            return;
        }
        if (targetName != null && !com.android.server.accessibility.AccessibilityUserState.doesShortcutTargetsStringContain(shortcutTargets, targetName)) {
            android.util.Slog.v(LOG_TAG, "Perform shortcut failed, invalid target name:" + targetName);
            targetName = null;
        }
        if (targetName == null) {
            if (shortcutTargets.size() > 1) {
                showAccessibilityTargetsSelection(displayId, shortcutType);
                return;
            }
            targetName = shortcutTargets.get(0);
        }
        if (targetName.equals("com.android.server.accessibility.MagnificationController")) {
            boolean enabled = !getMagnificationController().getFullScreenMagnificationController().isActivated(displayId);
            com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, com.android.internal.accessibility.AccessibilityShortcutController.MAGNIFICATION_COMPONENT_NAME, shortcutType, enabled);
            sendAccessibilityButtonToInputFilter(displayId);
            return;
        }
        android.content.ComponentName targetComponentName = android.content.ComponentName.unflattenFromString(targetName);
        if (targetComponentName == null) {
            android.util.Slog.d(LOG_TAG, "Perform shortcut failed, invalid target name:" + targetName);
        } else {
            if (performAccessibilityFrameworkFeature(displayId, targetComponentName, shortcutType)) {
                return;
            }
            if (performAccessibilityShortcutTargetActivity(displayId, targetComponentName)) {
                com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, targetComponentName, shortcutType);
            } else {
                performAccessibilityShortcutTargetService(displayId, shortcutType, targetComponentName);
            }
        }
    }

    private boolean performAccessibilityFrameworkFeature(int displayId, android.content.ComponentName assignedTarget, int shortcutType) {
        java.util.Map<android.content.ComponentName, com.android.internal.accessibility.AccessibilityShortcutController.FrameworkFeatureInfo> frameworkFeatureMap = com.android.internal.accessibility.AccessibilityShortcutController.getFrameworkShortcutFeaturesMap();
        if (!frameworkFeatureMap.containsKey(assignedTarget)) {
            return false;
        }
        com.android.internal.accessibility.AccessibilityShortcutController.FrameworkFeatureInfo featureInfo = frameworkFeatureMap.get(assignedTarget);
        android.provider.SettingsStringUtil.SettingStringHelper setting = new android.provider.SettingsStringUtil.SettingStringHelper(this.mContext.getContentResolver(), featureInfo.getSettingKey(), this.mCurrentUserId);
        if (featureInfo instanceof com.android.internal.accessibility.AccessibilityShortcutController.LaunchableFrameworkFeatureInfo) {
            com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, assignedTarget, shortcutType, true);
            launchAccessibilityFrameworkFeature(displayId, assignedTarget);
            return true;
        }
        if (!android.text.TextUtils.equals(featureInfo.getSettingOnValue(), setting.read())) {
            com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, assignedTarget, shortcutType, true);
            setting.write(featureInfo.getSettingOnValue());
        } else {
            com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, assignedTarget, shortcutType, false);
            setting.write(featureInfo.getSettingOffValue());
        }
        return true;
    }

    private boolean performAccessibilityShortcutTargetActivity(int displayId, android.content.ComponentName assignedTarget) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            for (int i = 0; i < userState.mInstalledShortcuts.size(); i++) {
                android.accessibilityservice.AccessibilityShortcutInfo shortcutInfo = userState.mInstalledShortcuts.get(i);
                if (shortcutInfo.getComponentName().equals(assignedTarget)) {
                    launchShortcutTargetActivity(displayId, assignedTarget);
                    return true;
                }
            }
            return false;
        }
    }

    private boolean performAccessibilityShortcutTargetService(int displayId, int shortcutType, android.content.ComponentName assignedTarget) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            android.accessibilityservice.AccessibilityServiceInfo installedServiceInfo = userState.getInstalledServiceInfoLocked(assignedTarget);
            if (installedServiceInfo == null) {
                android.util.Slog.d(LOG_TAG, "Perform shortcut failed, invalid component name:" + assignedTarget);
                return false;
            }
            com.android.server.accessibility.AccessibilityServiceConnection serviceConnection = userState.getServiceConnectionLocked(assignedTarget);
            int targetSdk = installedServiceInfo.getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion;
            boolean requestA11yButton = (installedServiceInfo.flags & 256) != 0;
            if ((targetSdk <= 29 && shortcutType == 2) || (targetSdk > 29 && !requestA11yButton)) {
                if (serviceConnection != null) {
                    com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, assignedTarget, shortcutType, false);
                    disableAccessibilityServiceLocked(assignedTarget, this.mCurrentUserId);
                } else {
                    com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, assignedTarget, shortcutType, true);
                    enableAccessibilityServiceLocked(assignedTarget, this.mCurrentUserId);
                }
                return true;
            }
            if (shortcutType == 2 && targetSdk > 29 && requestA11yButton && !userState.getEnabledServicesLocked().contains(assignedTarget)) {
                enableAccessibilityServiceLocked(assignedTarget, this.mCurrentUserId);
                return true;
            }
            if (serviceConnection != null && userState.mBoundServices.contains(serviceConnection) && serviceConnection.mRequestAccessibilityButton) {
                com.android.internal.accessibility.util.AccessibilityStatsLogUtils.logAccessibilityShortcutActivated(this.mContext, assignedTarget, shortcutType, true);
                serviceConnection.notifyAccessibilityButtonClickedLocked(displayId);
                return true;
            }
            android.util.Slog.d(LOG_TAG, "Perform shortcut failed, service is not ready:" + assignedTarget);
            return false;
        }
    }

    private void launchAccessibilityFrameworkFeature(int displayId, android.content.ComponentName assignedTarget) {
        if (assignedTarget.equals(com.android.internal.accessibility.AccessibilityShortcutController.ACCESSIBILITY_HEARING_AIDS_COMPONENT_NAME)) {
            if (com.android.systemui.Flags.hearingAidsQsTileDialog()) {
                launchHearingDevicesDialog();
            } else {
                launchAccessibilitySubSettings(displayId, com.android.internal.accessibility.AccessibilityShortcutController.ACCESSIBILITY_HEARING_AIDS_COMPONENT_NAME);
            }
        }
    }

    public void enableShortcutsForTargets(boolean enable, int shortcutTypes, java.util.List<java.lang.String> shortcutTargets, int userId) throws java.lang.Throwable {
        enableShortcutsForTargets_enforcePermission();
        for (int shortcutType : com.android.internal.accessibility.common.ShortcutConstants.USER_SHORTCUT_TYPES) {
            if ((shortcutTypes & shortcutType) == shortcutType) {
                enableShortcutForTargets(enable, shortcutType, shortcutTargets, userId);
            }
        }
    }

    private void enableShortcutForTargets(boolean z, int i, java.util.List<java.lang.String> list, int i2) throws java.lang.Throwable {
        java.util.Set<java.lang.String> set;
        java.util.Set<java.lang.String> set2;
        java.lang.String strConvertToKey = com.android.internal.accessibility.util.ShortcutUtils.convertToKey(i);
        if (i == 4 || i == 8) {
            for (java.lang.String str : list) {
                if ("com.android.server.accessibility.MagnificationController".equals(str)) {
                    persistIntToSetting(i2, strConvertToKey, z ? 1 : 0);
                } else {
                    android.util.Slog.w(LOG_TAG, "Triple tap or two-fingers double-tap is not supported for " + str);
                }
            }
            return;
        }
        final java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTileMapInternal = getA11yFeatureToTileMapInternal(i2);
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.accessibility.AccessibilityUserState userStateLocked = getUserStateLocked(i2);
                    java.util.Set shortcutTargetsFromSettings = com.android.internal.accessibility.util.ShortcutUtils.getShortcutTargetsFromSettings(this.mContext, i, i2);
                    android.util.ArraySet arraySet = new android.util.ArraySet(shortcutTargetsFromSettings);
                    if (z) {
                        try {
                            arraySet.addAll(list);
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    } else {
                        arraySet.removeAll(list);
                    }
                    if (i != 16) {
                        set = arraySet;
                    } else {
                        set = (java.util.Set) arraySet.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda30
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return com.android.server.accessibility.AccessibilityManagerService.lambda$enableShortcutForTargets$42(a11yFeatureToTileMapInternal, (java.lang.String) obj);
                            }
                        }).collect(java.util.stream.Collectors.toUnmodifiableSet());
                    }
                    if (shortcutTargetsFromSettings.equals(set)) {
                        return;
                    }
                    java.util.Set<java.lang.String> set3 = set;
                    persistColonDelimitedSetToSettingLocked(strConvertToKey, i2, set, new java.util.function.Function() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda31
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return com.android.server.accessibility.AccessibilityManagerService.lambda$enableShortcutForTargets$43((java.lang.String) obj);
                        }
                    }, "");
                    if (i != 16) {
                        set2 = set3;
                    } else {
                        logMetricForQsShortcutConfiguration(z, java.lang.Math.abs(shortcutTargetsFromSettings.size() - set3.size()));
                        set2 = set3;
                        userStateLocked.updateA11yQsTargetLocked(set2);
                        scheduleNotifyClientsOfServicesStateChangeLocked(userStateLocked);
                        onUserStateChangedLocked(userStateLocked);
                    }
                    long jClearCallingIdentity = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.internal.accessibility.util.ShortcutUtils.updateInvisibleToggleAccessibilityServiceEnableState(this.mContext, new android.util.ArraySet(list), i2);
                        if (i == 16) {
                            this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda32
                                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                                    ((com.android.server.accessibility.AccessibilityManagerService) obj).updateA11yTileServicesInQuickSettingsPanel((java.util.Set) obj2, (java.util.Set) obj3, ((java.lang.Integer) obj4).intValue());
                                }
                            }, this, set2, shortcutTargetsFromSettings, java.lang.Integer.valueOf(i2)));
                        }
                        if (!z) {
                            return;
                        }
                        if (i == 2) {
                            skipVolumeShortcutDialogTimeoutRestriction(i2);
                            if (com.android.server.accessibility.Flags.enableHardwareShortcutDisablesWarning()) {
                                persistIntToSetting(i2, "accessibility_shortcut_dialog_shown", 1);
                                return;
                            }
                            return;
                        }
                        if (i == 1 && list.contains("com.android.server.accessibility.MagnificationController") && android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_floating_menu_size", -1, i2) == -1) {
                            persistIntToSetting(i2, "accessibility_floating_menu_size", 1);
                            return;
                        }
                        return;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(jClearCallingIdentity);
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
            throw th;
        }
    }

    static /* synthetic */ boolean lambda$enableShortcutForTargets$42(java.util.Map featureToTileMap, java.lang.String target) {
        android.content.ComponentName targetComponent = android.content.ComponentName.unflattenFromString(target);
        return featureToTileMap.containsKey(targetComponent);
    }

    static /* synthetic */ java.lang.String lambda$enableShortcutForTargets$43(java.lang.String str) {
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateA11yTileServicesInQuickSettingsPanel(final java.util.Set<java.lang.String> newQsTargets, final java.util.Set<java.lang.String> currentQsTargets, int userId) {
        final com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        if (statusBarManagerInternal == null) {
            return;
        }
        final java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTileMap = getA11yFeatureToTileMapInternal(userId);
        final java.util.Set<java.lang.String> targetWithNoTile = new android.util.ArraySet<>();
        newQsTargets.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateA11yTileServicesInQuickSettingsPanel$44(currentQsTargets, (java.lang.String) obj);
            }
        }).forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda22
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.accessibility.AccessibilityManagerService.lambda$updateA11yTileServicesInQuickSettingsPanel$45(a11yFeatureToTileMap, targetWithNoTile, statusBarManagerInternal, (java.lang.String) obj);
            }
        });
        currentQsTargets.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda23
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.accessibility.AccessibilityManagerService.lambda$updateA11yTileServicesInQuickSettingsPanel$46(newQsTargets, (java.lang.String) obj);
            }
        }).forEach(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.accessibility.AccessibilityManagerService.lambda$updateA11yTileServicesInQuickSettingsPanel$47(a11yFeatureToTileMap, targetWithNoTile, statusBarManagerInternal, (java.lang.String) obj);
            }
        });
        if (!targetWithNoTile.isEmpty()) {
            android.util.Slog.e(LOG_TAG, "Unable to add/remove Tiles for a11y features: " + targetWithNoTile + "as the Tiles aren't provided");
        }
    }

    static /* synthetic */ boolean lambda$updateA11yTileServicesInQuickSettingsPanel$44(java.util.Set currentQsTargets, java.lang.String target) {
        return !currentQsTargets.contains(target);
    }

    static /* synthetic */ void lambda$updateA11yTileServicesInQuickSettingsPanel$45(java.util.Map a11yFeatureToTileMap, java.util.Set targetWithNoTile, com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal, java.lang.String target) {
        android.content.ComponentName targetComponent = android.content.ComponentName.unflattenFromString(target);
        if (targetComponent == null || !a11yFeatureToTileMap.containsKey(targetComponent)) {
            targetWithNoTile.add(target);
        } else {
            if (com.android.internal.accessibility.common.ShortcutConstants.A11Y_FEATURE_TO_FRAMEWORK_TILE.containsKey(targetComponent)) {
                return;
            }
            statusBarManagerInternal.addQsTileToFrontOrEnd((android.content.ComponentName) a11yFeatureToTileMap.get(targetComponent), true);
        }
    }

    static /* synthetic */ boolean lambda$updateA11yTileServicesInQuickSettingsPanel$46(java.util.Set newQsTargets, java.lang.String target) {
        return !newQsTargets.contains(target);
    }

    static /* synthetic */ void lambda$updateA11yTileServicesInQuickSettingsPanel$47(java.util.Map a11yFeatureToTileMap, java.util.Set targetWithNoTile, com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal, java.lang.String target) {
        android.content.ComponentName targetComponent = android.content.ComponentName.unflattenFromString(target);
        if (targetComponent == null || !a11yFeatureToTileMap.containsKey(targetComponent)) {
            targetWithNoTile.add(target);
        } else {
            if (com.android.internal.accessibility.common.ShortcutConstants.A11Y_FEATURE_TO_FRAMEWORK_TILE.containsKey(targetComponent)) {
                return;
            }
            statusBarManagerInternal.removeQsTile((android.content.ComponentName) a11yFeatureToTileMap.get(targetComponent));
        }
    }

    public android.os.Bundle getA11yFeatureToTileMap(int userId) {
        getA11yFeatureToTileMap_enforcePermission();
        android.os.Bundle bundle = new android.os.Bundle();
        java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTile = getA11yFeatureToTileMapInternal(userId);
        for (java.util.Map.Entry<android.content.ComponentName, android.content.ComponentName> entry : a11yFeatureToTile.entrySet()) {
            bundle.putParcelable(entry.getKey().flattenToString(), entry.getValue());
        }
        return bundle;
    }

    private java.util.Map<android.content.ComponentName, android.content.ComponentName> getA11yFeatureToTileMapInternal(int userId) {
        int resolvedUserId;
        java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTileService;
        java.util.Map<android.content.ComponentName, android.content.ComponentName> a11yFeatureToTile = new android.util.ArrayMap<>();
        synchronized (this.mLock) {
            resolvedUserId = this.mSecurityPolicy.resolveCallingUserIdEnforcingPermissionsLocked(userId);
            com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(resolvedUserId);
            a11yFeatureToTileService = userState.getA11yFeatureToTileService();
        }
        boolean shouldFilterAppAccess = android.os.Binder.getCallingPid() != OWN_PROCESS_ID;
        int callingUid = android.os.Binder.getCallingUid();
        android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        for (java.util.Map.Entry<android.content.ComponentName, android.content.ComponentName> entry : a11yFeatureToTileService.entrySet()) {
            if (!shouldFilterAppAccess || !pm.filterAppAccess(entry.getKey().getPackageName(), callingUid, resolvedUserId)) {
                a11yFeatureToTile.put(entry.getKey(), entry.getValue());
            }
        }
        a11yFeatureToTile.putAll(com.android.internal.accessibility.common.ShortcutConstants.A11Y_FEATURE_TO_FRAMEWORK_TILE);
        return a11yFeatureToTile;
    }

    public java.util.List<java.lang.String> getAccessibilityShortcutTargets(int shortcutType) {
        getAccessibilityShortcutTargets_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getAccessibilityShortcutTargets", 4L, "shortcutType=" + shortcutType);
        }
        return getAccessibilityShortcutTargetsInternal(shortcutType);
    }

    private java.util.List<java.lang.String> getAccessibilityShortcutTargetsInternal(int shortcutType) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            java.util.ArrayList<java.lang.String> shortcutTargets = new java.util.ArrayList<>(userState.getShortcutTargetsLocked(shortcutType));
            if (shortcutType != 1) {
                return shortcutTargets;
            }
            for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
                if (service.mRequestAccessibilityButton && service.getServiceInfo().getResolveInfo().serviceInfo.applicationInfo.targetSdkVersion <= 29) {
                    java.lang.String serviceName = service.getComponentName().flattenToString();
                    if (!android.text.TextUtils.isEmpty(serviceName)) {
                        shortcutTargets.add(serviceName);
                    }
                }
            }
            return shortcutTargets;
        }
    }

    private void enableAccessibilityServiceLocked(android.content.ComponentName componentName, int userId) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("enabled_accessibility_services", userId, this.mTempComponentNameSet);
        this.mTempComponentNameSet.add(componentName);
        persistComponentNamesToSettingLocked("enabled_accessibility_services", this.mTempComponentNameSet, userId);
        com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(userId);
        if (userState.mEnabledServices.add(componentName)) {
            onUserStateChangedLocked(userState);
        }
    }

    private void disableAccessibilityServiceLocked(android.content.ComponentName componentName, int userId) {
        this.mTempComponentNameSet.clear();
        readComponentNamesFromSettingLocked("enabled_accessibility_services", userId, this.mTempComponentNameSet);
        this.mTempComponentNameSet.remove(componentName);
        persistComponentNamesToSettingLocked("enabled_accessibility_services", this.mTempComponentNameSet, userId);
        com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(userId);
        if (userState.mEnabledServices.remove(componentName)) {
            onUserStateChangedLocked(userState);
        }
    }

    @Override // com.android.server.accessibility.AccessibilityWindowManager.AccessibilityEventSender
    public void sendAccessibilityEventForCurrentUserLocked(android.view.accessibility.AccessibilityEvent event) {
        if (event.getWindowChanges() == 1) {
            sendPendingWindowStateChangedEventsForAvailableWindowLocked(event.getWindowId());
        }
        sendAccessibilityEventLocked(event, this.mCurrentUserId);
    }

    private void sendAccessibilityEventLocked(android.view.accessibility.AccessibilityEvent event, int userId) {
        event.setEventTime(android.os.SystemClock.uptimeMillis());
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda36
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).sendAccessibilityEvent((android.view.accessibility.AccessibilityEvent) obj2, ((java.lang.Integer) obj3).intValue());
            }
        }, this, event, java.lang.Integer.valueOf(userId)));
    }

    public boolean sendFingerprintGesture(int gestureKeyCode) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(131076L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.sendFingerprintGesture", 131076L, "gestureKeyCode=" + gestureKeyCode);
        }
        synchronized (this.mLock) {
            if (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != 1000) {
                throw new java.lang.SecurityException("Only SYSTEM can call sendFingerprintGesture");
            }
        }
        if (this.mFingerprintGestureDispatcher == null) {
            return false;
        }
        return this.mFingerprintGestureDispatcher.onFingerprintGesture(gestureKeyCode);
    }

    public int getAccessibilityWindowId(android.os.IBinder windowToken) {
        int iFindWindowIdLocked;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getAccessibilityWindowId", 4L, "windowToken=" + windowToken);
        }
        synchronized (this.mLock) {
            if (android.os.UserHandle.getAppId(android.os.Binder.getCallingUid()) != 1000) {
                throw new java.lang.SecurityException("Only SYSTEM can call getAccessibilityWindowId");
            }
            iFindWindowIdLocked = this.mA11yWindowManager.findWindowIdLocked(this.mCurrentUserId, windowToken);
        }
        return iFindWindowIdLocked;
    }

    public long getRecommendedTimeoutMillis() {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getRecommendedTimeoutMillis", 4L);
        }
        synchronized (this.mLock) {
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                return this.mProxyManager.getRecommendedTimeoutMillisLocked(deviceId);
            }
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            return getRecommendedTimeoutMillisLocked(userState);
        }
    }

    private long getRecommendedTimeoutMillisLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        return com.android.internal.util.IntPair.of(userState.getInteractiveUiTimeoutLocked(), userState.getNonInteractiveUiTimeoutLocked());
    }

    public void setMagnificationConnection(android.view.accessibility.IMagnificationConnection connection) throws android.os.RemoteException {
        setMagnificationConnection_enforcePermission();
        if (this.mTraceManager.isA11yTracingEnabledForTypes(132L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.setMagnificationConnection", 132L, "connection=" + connection);
        }
        getMagnificationConnectionManager().setConnection(connection);
        if (com.android.window.flags.Flags.alwaysDrawMagnificationFullscreenBorder() && connection == null && this.mMagnificationController.isFullScreenMagnificationControllerInitialized()) {
            java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
            for (int i = 0; i < displays.size(); i++) {
                android.view.Display display = displays.get(i);
                getMagnificationController().getFullScreenMagnificationController().reset(display.getDisplayId(), false);
            }
        }
    }

    public com.android.server.accessibility.magnification.MagnificationConnectionManager getMagnificationConnectionManager() {
        com.android.server.accessibility.magnification.MagnificationConnectionManager magnificationConnectionManager;
        synchronized (this.mLock) {
            magnificationConnectionManager = this.mMagnificationController.getMagnificationConnectionManager();
        }
        return magnificationConnectionManager;
    }

    com.android.server.accessibility.magnification.MagnificationController getMagnificationController() {
        return this.mMagnificationController;
    }

    public void associateEmbeddedHierarchy(android.os.IBinder host, android.os.IBinder embedded) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.associateEmbeddedHierarchy", 4L, "host=" + host + ";embedded=" + embedded);
        }
        synchronized (this.mLock) {
            this.mA11yWindowManager.associateEmbeddedHierarchyLocked(host, embedded);
        }
    }

    public void disassociateEmbeddedHierarchy(android.os.IBinder token) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.disassociateEmbeddedHierarchy", 4L, "token=" + token);
        }
        synchronized (this.mLock) {
            this.mA11yWindowManager.disassociateEmbeddedHierarchyLocked(token);
        }
    }

    public int getFocusStrokeWidth() {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getFocusStrokeWidth", 4L);
        }
        synchronized (this.mLock) {
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                return this.mProxyManager.getFocusStrokeWidthLocked(deviceId);
            }
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            return userState.getFocusStrokeWidthLocked();
        }
    }

    public int getFocusColor() {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.getFocusColor", 4L);
        }
        synchronized (this.mLock) {
            int deviceId = this.mProxyManager.getFirstDeviceIdForUidLocked(android.os.Binder.getCallingUid());
            if (this.mProxyManager.isProxyedDeviceId(deviceId)) {
                return this.mProxyManager.getFocusColorLocked(deviceId);
            }
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            return userState.getFocusColorLocked();
        }
    }

    public boolean isAudioDescriptionByDefaultEnabled() {
        boolean zIsAudioDescriptionByDefaultEnabledLocked;
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.isAudioDescriptionByDefaultEnabled", 4L);
        }
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            zIsAudioDescriptionByDefaultEnabledLocked = userState.isAudioDescriptionByDefaultEnabledLocked();
        }
        return zIsAudioDescriptionByDefaultEnabledLocked;
    }

    public void setAccessibilityWindowAttributes(int displayId, int windowId, int userId, android.view.accessibility.AccessibilityWindowAttributes attributes) {
        if (this.mTraceManager.isA11yTracingEnabledForTypes(4L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.setAccessibilityWindowAttributes", 4L);
        }
        this.mA11yWindowManager.setAccessibilityWindowAttributes(displayId, windowId, userId, attributes);
    }

    public void setSystemAudioCaptioningEnabled(boolean isEnabled, int userId) {
        setSystemAudioCaptioningEnabled_enforcePermission();
        this.mCaptioningManagerImpl.setSystemAudioCaptioningEnabled(isEnabled, userId);
    }

    public boolean isSystemAudioCaptioningUiEnabled(int userId) {
        return this.mCaptioningManagerImpl.isSystemAudioCaptioningUiEnabled(userId);
    }

    public void setSystemAudioCaptioningUiEnabled(boolean isEnabled, int userId) {
        setSystemAudioCaptioningUiEnabled_enforcePermission();
        this.mCaptioningManagerImpl.setSystemAudioCaptioningUiEnabled(isEnabled, userId);
    }

    public boolean registerProxyForDisplay(android.accessibilityservice.IAccessibilityServiceClient client, int displayId) throws android.os.RemoteException {
        registerProxyForDisplay_enforcePermission();
        this.mSecurityPolicy.checkForAccessibilityPermissionOrRole();
        if (client == null) {
            return false;
        }
        if (displayId < 0) {
            throw new java.lang.IllegalArgumentException("The display id " + displayId + " is invalid.");
        }
        if (!isTrackedDisplay(displayId)) {
            throw new java.lang.IllegalArgumentException("The display " + displayId + " does not exist or is not tracked by accessibility.");
        }
        if (this.mProxyManager.isProxyedDisplay(displayId)) {
            throw new java.lang.IllegalArgumentException("The display " + displayId + " is already being proxy-ed");
        }
        if (!this.mProxyManager.displayBelongsToCaller(android.os.Binder.getCallingUid(), displayId)) {
            throw new java.lang.SecurityException("The display " + displayId + " does not belong to the caller.");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.accessibility.ProxyManager proxyManager = this.mProxyManager;
            int i = sIdCounter;
            sIdCounter = i + 1;
            proxyManager.registerProxy(client, displayId, i, this.mSecurityPolicy, this, getTraceManager(), this.mWindowManagerService);
            synchronized (this.mLock) {
                notifyClearAccessibilityCacheLocked();
            }
            android.os.Binder.restoreCallingIdentity(identity);
            return true;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    public boolean unregisterProxyForDisplay(int displayId) {
        unregisterProxyForDisplay_enforcePermission();
        this.mSecurityPolicy.checkForAccessibilityPermissionOrRole();
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mProxyManager.unregisterProxy(displayId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    boolean isDisplayProxyed(int displayId) {
        return this.mProxyManager.isProxyedDisplay(displayId);
    }

    public boolean startFlashNotificationSequence(java.lang.String opPkg, int reason, android.os.IBinder token) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mFlashNotificationsController.startFlashNotificationSequence(opPkg, reason, token);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean stopFlashNotificationSequence(java.lang.String opPkg) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mFlashNotificationsController.stopFlashNotificationSequence(opPkg);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean startFlashNotificationEvent(java.lang.String opPkg, int reason, java.lang.String reasonPkg) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mFlashNotificationsController.startFlashNotificationEvent(opPkg, reason, reasonPkg);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean isAccessibilityTargetAllowed(java.lang.String packageName, int uid, int userId) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService(android.app.admin.DevicePolicyManager.class);
            java.util.List<java.lang.String> permittedServices = dpm.getPermittedAccessibilityServices(userId);
            boolean allowed = permittedServices == null || permittedServices.contains(packageName);
            if (!allowed) {
                android.util.Slog.d(LOG_TAG, "[isAccessibilityTargetAllowed]False, allowed = " + allowed + " ,packageName = " + packageName);
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            }
            if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.enhancedConfirmationModeApisEnabled() && android.security.Flags.extendEcmToAllSettings()) {
                boolean isRestricted = true ^ ((android.app.ecm.EnhancedConfirmationManager) this.mContext.getSystemService(android.app.ecm.EnhancedConfirmationManager.class)).isRestricted(packageName, "android:bind_accessibility_service");
                if (!isRestricted) {
                    android.util.Slog.d(LOG_TAG, "[isAccessibilityTargetAllowed]False, packageName = " + packageName + " ,uid = " + uid + " userId = " + userId);
                }
                return isRestricted;
            }
            try {
                int mode = ((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class)).noteOpNoThrow(119, uid, packageName);
                boolean ecmEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_enableTimeZoneNotificationsTrackingSupported);
                if (ecmEnabled && mode != 0) {
                    android.util.Slog.d(LOG_TAG, "[isAccessibilityTargetAllowed]False, ecmEnabled = " + ecmEnabled + " ,mode = " + mode + " ,packageName = " + packageName + " ,uid = " + uid);
                }
                return !ecmEnabled || mode == 0 || mode == 3;
            } catch (java.lang.Exception e) {
                android.os.Binder.restoreCallingIdentity(identity);
                return false;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            android.util.Log.e(LOG_TAG, "Exception when retrieving package:" + packageName, e2);
            return false;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean sendRestrictedDialogIntent(java.lang.String packageName, int uid, int userId) {
        if (isAccessibilityTargetAllowed(packageName, uid, userId)) {
            return false;
        }
        com.android.settingslib.RestrictedLockUtils.EnforcedAdmin admin = com.android.server.accessibility.RestrictedLockUtilsInternal.checkIfAccessibilityServiceDisallowed(this.mContext, packageName, userId);
        if (admin != null) {
            com.android.settingslib.RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, admin);
            return true;
        }
        if (com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.enhancedConfirmationModeApisEnabled() && android.security.Flags.extendEcmToAllSettings()) {
            try {
                android.content.Intent settingDialogIntent = ((android.app.ecm.EnhancedConfirmationManager) this.mContext.getSystemService(android.app.ecm.EnhancedConfirmationManager.class)).createRestrictedSettingDialogIntent(packageName, "android:bind_accessibility_service");
                this.mContext.startActivity(settingDialogIntent);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(LOG_TAG, "Exception when retrieving package:" + packageName, e);
            }
        } else {
            com.android.settingslib.RestrictedLockUtils.sendShowRestrictedSettingDialogIntent(this.mContext, packageName, uid);
        }
        return true;
    }

    public boolean isAccessibilityServiceWarningRequired(android.accessibilityservice.AccessibilityServiceInfo info) {
        isAccessibilityServiceWarningRequired_enforcePermission();
        android.content.ComponentName componentName = info.getComponentName();
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            if (userState.getEnabledServicesLocked().contains(componentName)) {
                return false;
            }
            for (int shortcutType : com.android.internal.accessibility.common.ShortcutConstants.USER_SHORTCUT_TYPES) {
                if (getAccessibilityShortcutTargets(shortcutType).contains(componentName.flattenToString())) {
                    return false;
                }
            }
            return (android.view.accessibility.Flags.skipAccessibilityWarningDialogForTrustedServices() && isAccessibilityServicePreinstalledAndTrusted(info)) ? false : true;
        }
    }

    private boolean isAccessibilityServicePreinstalledAndTrusted(android.accessibilityservice.AccessibilityServiceInfo info) {
        android.content.ComponentName componentName = info.getComponentName();
        boolean isPreinstalled = info.getResolveInfo().serviceInfo.applicationInfo.isSystemApp();
        if (isPreinstalled) {
            java.lang.String[] trustedAccessibilityServices = this.mContext.getResources().getStringArray(android.R.array.config_telephonyHardware);
            java.util.stream.Stream map = java.util.Arrays.stream(trustedAccessibilityServices).map(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda71());
            java.util.Objects.requireNonNull(componentName);
            if (map.anyMatch(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda72(componentName))) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, LOG_TAG, pw)) {
            synchronized (this.mLock) {
                pw.println("ACCESSIBILITY MANAGER (dumpsys accessibility)");
                pw.println();
                pw.append("currentUserId=").append((java.lang.CharSequence) java.lang.String.valueOf(this.mCurrentUserId));
                if (this.mRealCurrentUserId != -2 && this.mCurrentUserId != this.mRealCurrentUserId) {
                    pw.append(" (set for UiAutomation purposes; \"real\" current user is ").append((java.lang.CharSequence) java.lang.String.valueOf(this.mRealCurrentUserId)).append(")");
                }
                pw.println();
                if (this.mVisibleBgUserIds != null) {
                    pw.append("visibleBgUserIds=").append((java.lang.CharSequence) this.mVisibleBgUserIds.toString());
                    pw.println();
                }
                pw.append("hasMagnificationConnection=").append((java.lang.CharSequence) java.lang.String.valueOf(getMagnificationConnectionManager().isConnected()));
                pw.println();
                this.mMagnificationProcessor.dump(pw, getValidDisplayList());
                int userCount = this.mUserStates.size();
                for (int i = 0; i < userCount; i++) {
                    this.mUserStates.valueAt(i).dump(fd, pw, args);
                }
                if (this.mUiAutomationManager.isUiAutomationRunningLocked()) {
                    this.mUiAutomationManager.dumpUiAutomationService(fd, pw, args);
                    pw.println();
                }
                this.mA11yWindowManager.dump(fd, pw, args);
                if (this.mHasInputFilter && this.mInputFilter != null) {
                    this.mInputFilter.dump(fd, pw, args);
                }
                pw.println("Global client list info:{");
                this.mGlobalClients.dump(pw, "    Client list ");
                pw.println("    Registered clients:{");
                for (int i2 = 0; i2 < this.mGlobalClients.getRegisteredCallbackCount(); i2++) {
                    com.android.server.accessibility.AccessibilityManagerService.Client client = (com.android.server.accessibility.AccessibilityManagerService.Client) this.mGlobalClients.getRegisteredCallbackCookie(i2);
                    pw.append((java.lang.CharSequence) java.util.Arrays.toString(client.mPackageNames));
                }
                pw.println();
                this.mProxyManager.dump(fd, pw, args);
                this.mA11yDisplayListener.dump(fd, pw, args);
            }
        }
    }

    final class MainHandler extends android.os.Handler {
        public static final int MSG_SEND_KEY_EVENT_TO_INPUT_FILTER = 8;

        public MainHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 8) {
                android.view.KeyEvent event = (android.view.KeyEvent) msg.obj;
                int policyFlags = msg.arg1;
                synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.mHasInputFilter && com.android.server.accessibility.AccessibilityManagerService.this.mInputFilter != null) {
                        com.android.server.accessibility.AccessibilityManagerService.this.mInputFilter.sendInputEvent(event, policyFlags);
                    }
                }
                event.recycle();
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public com.android.server.accessibility.magnification.MagnificationProcessor getMagnificationProcessor() {
        return this.mMagnificationProcessor;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onClientChangeLocked(boolean serviceInfoChanged) {
        onClientChangeLocked(serviceInfoChanged, false);
    }

    public void onClientChangeLocked(boolean serviceInfoChanged, boolean forceUpdate) {
        com.android.server.accessibility.AccessibilityUserState userState = getUserStateLocked(this.mCurrentUserId);
        onUserStateChangedLocked(userState, forceUpdate);
        if (serviceInfoChanged) {
            scheduleNotifyClientsOfServicesStateChangeLocked(userState);
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onProxyChanged(int deviceId) {
        this.mProxyManager.onProxyChanged(deviceId);
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public void removeDeviceIdLocked(int deviceId) {
        resetClientsLocked(deviceId, getCurrentUserStateLocked().mUserClients);
        resetClientsLocked(deviceId, this.mGlobalClients);
        onClientChangeLocked(true, true);
    }

    private void resetClientsLocked(int deviceId, android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> clients) {
        if (clients == null || clients.getRegisteredCallbackCount() == 0) {
            return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < clients.getRegisteredCallbackCount(); i++) {
                com.android.server.accessibility.AccessibilityManagerService.Client appClient = (com.android.server.accessibility.AccessibilityManagerService.Client) clients.getRegisteredCallbackCookie(i);
                if (appClient.mDeviceId == deviceId) {
                    appClient.mDeviceId = 0;
                }
            }
        }
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public void updateWindowsForAccessibilityCallbackLocked() {
        updateWindowsForAccessibilityCallbackLocked(getUserStateLocked(this.mCurrentUserId));
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> getGlobalClientsLocked() {
        return this.mGlobalClients;
    }

    @Override // com.android.server.accessibility.ProxyManager.SystemSupport
    public android.os.RemoteCallbackList<android.view.accessibility.IAccessibilityManagerClient> getCurrentUserClientsLocked() {
        return getCurrentUserState().mUserClients;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.accessibility.AccessibilityShellCommand(this.mContext, this, this.mSystemActionPerformer).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private final class InteractionBridge {
        private final android.content.ComponentName COMPONENT_NAME = new android.content.ComponentName("com.android.server.accessibility", "InteractionBridge");
        private final android.view.accessibility.AccessibilityInteractionClient mClient;
        private final int mConnectionId;
        private final android.view.Display mDefaultDisplay;

        public InteractionBridge() throws java.lang.Throwable {
            com.android.server.accessibility.AccessibilityUserState userState;
            android.accessibilityservice.AccessibilityServiceInfo info = new android.accessibilityservice.AccessibilityServiceInfo();
            info.setCapabilities(1);
            info.flags |= 64;
            info.flags |= 2;
            info.setAccessibilityTool(true);
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                try {
                    userState = com.android.server.accessibility.AccessibilityManagerService.this.getCurrentUserStateLocked();
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
            android.content.Context context = com.android.server.accessibility.AccessibilityManagerService.this.mContext;
            android.content.ComponentName componentName = this.COMPONENT_NAME;
            int i = com.android.server.accessibility.AccessibilityManagerService.sIdCounter;
            com.android.server.accessibility.AccessibilityManagerService.sIdCounter = i + 1;
            com.android.server.accessibility.AccessibilityServiceConnection service = new com.android.server.accessibility.AccessibilityServiceConnection(userState, context, componentName, info, i, com.android.server.accessibility.AccessibilityManagerService.this.mMainHandler, com.android.server.accessibility.AccessibilityManagerService.this.mLock, com.android.server.accessibility.AccessibilityManagerService.this.mSecurityPolicy, com.android.server.accessibility.AccessibilityManagerService.this, com.android.server.accessibility.AccessibilityManagerService.this.getTraceManager(), com.android.server.accessibility.AccessibilityManagerService.this.mWindowManagerService, com.android.server.accessibility.AccessibilityManagerService.this.getSystemActionPerformer(), com.android.server.accessibility.AccessibilityManagerService.this.mA11yWindowManager, com.android.server.accessibility.AccessibilityManagerService.this.mActivityTaskManagerService) { // from class: com.android.server.accessibility.AccessibilityManagerService.InteractionBridge.1
                @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection
                public boolean supportsFlagForNotImportantViews(android.accessibilityservice.AccessibilityServiceInfo info2) {
                    return true;
                }
            };
            this.mConnectionId = service.mId;
            this.mClient = android.view.accessibility.AccessibilityInteractionClient.getInstance(com.android.server.accessibility.AccessibilityManagerService.this.mContext);
            android.view.accessibility.AccessibilityInteractionClient.addConnection(this.mConnectionId, service, false);
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) com.android.server.accessibility.AccessibilityManagerService.this.mContext.getSystemService("display");
            this.mDefaultDisplay = displayManager.getDisplay(0);
        }

        boolean getAccessibilityFocusClickPointInScreen(android.graphics.Point outPoint) {
            return com.android.server.accessibility.AccessibilityManagerService.this.getInteractionBridge().getAccessibilityFocusClickPointInScreenNotLocked(outPoint);
        }

        public boolean performActionOnAccessibilityFocusedItemNotLocked(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction action) {
            android.view.accessibility.AccessibilityNodeInfo focus = com.android.server.accessibility.AccessibilityManagerService.this.mServiceExt.getAccessibilityFocusNotLocked(getAccessibilityFocusNotLocked(), action);
            if (focus == null || !focus.getActionList().contains(action)) {
                return false;
            }
            return focus.performAction(action.getId());
        }

        public boolean getAccessibilityFocusClickPointInScreenNotLocked(android.graphics.Point outPoint) {
            android.view.accessibility.AccessibilityNodeInfo focus = getAccessibilityFocusNotLocked();
            if (focus == null) {
                return false;
            }
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                android.graphics.Rect boundsInScreenBeforeMagnification = com.android.server.accessibility.AccessibilityManagerService.this.mTempRect;
                focus.getBoundsInScreen(boundsInScreenBeforeMagnification);
                android.graphics.Point nodeCenter = new android.graphics.Point(boundsInScreenBeforeMagnification.centerX(), boundsInScreenBeforeMagnification.centerY());
                android.util.Pair<float[], android.view.MagnificationSpec> pair = com.android.server.accessibility.AccessibilityManagerService.this.getWindowTransformationMatrixAndMagnificationSpec(focus.getWindowId());
                android.view.MagnificationSpec spec = null;
                if (pair != null && pair.second != null) {
                    spec = new android.view.MagnificationSpec();
                    spec.setTo((android.view.MagnificationSpec) pair.second);
                }
                if (spec != null && !spec.isNop()) {
                    boundsInScreenBeforeMagnification.offset((int) (-spec.offsetX), (int) (-spec.offsetY));
                    boundsInScreenBeforeMagnification.scale(1.0f / spec.scale);
                }
                android.graphics.Rect windowBounds = com.android.server.accessibility.AccessibilityManagerService.this.mTempRect1;
                android.view.accessibility.AccessibilityWindowInfo window = focus.getWindow();
                if (window != null) {
                    window.getBoundsInScreen(windowBounds);
                }
                if (!boundsInScreenBeforeMagnification.intersect(windowBounds)) {
                    return false;
                }
                android.graphics.Point screenSize = com.android.server.accessibility.AccessibilityManagerService.this.mTempPoint;
                this.mDefaultDisplay.getRealSize(screenSize);
                if (!boundsInScreenBeforeMagnification.intersect(0, 0, screenSize.x, screenSize.y)) {
                    return false;
                }
                outPoint.set(nodeCenter.x, nodeCenter.y);
                return true;
            }
        }

        private android.view.accessibility.AccessibilityNodeInfo getAccessibilityFocusNotLocked() {
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                int focusedWindowId = com.android.server.accessibility.AccessibilityManagerService.this.mA11yWindowManager.getFocusedWindowId(2);
                if (focusedWindowId == -1) {
                    return null;
                }
                return getAccessibilityFocusNotLocked(focusedWindowId);
            }
        }

        private android.view.accessibility.AccessibilityNodeInfo getAccessibilityFocusNotLocked(int windowId) {
            return this.mClient.findFocus(this.mConnectionId, windowId, android.view.accessibility.AccessibilityNodeInfo.ROOT_NODE_ID, 2);
        }
    }

    public java.util.ArrayList<android.view.Display> getValidDisplayList() {
        return this.mA11yDisplayListener.getValidDisplayList();
    }

    private boolean isTrackedDisplay(int displayId) {
        java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
        for (android.view.Display display : displays) {
            if (display.getDisplayId() == displayId) {
                return true;
            }
        }
        return false;
    }

    public class AccessibilityDisplayListener implements android.hardware.display.DisplayManager.DisplayListener {
        private final android.hardware.display.DisplayManager mDisplayManager;
        private final java.util.ArrayList<android.view.Display> mDisplaysList = new java.util.ArrayList<>();
        private int mSystemUiUid;

        AccessibilityDisplayListener(android.content.Context context, android.os.Handler handler) {
            this.mSystemUiUid = 0;
            boolean isMainHandler = handler.getLooper() == android.os.Looper.getMainLooper();
            if (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) {
                com.android.internal.util.Preconditions.checkArgument(isMainHandler, "AccessibilityDisplayListener must use the main handler");
            } else if (!isMainHandler) {
                android.util.Slog.e(com.android.server.accessibility.AccessibilityManagerService.LOG_TAG, "AccessibilityDisplayListener must use the main handler");
            }
            this.mDisplayManager = (android.hardware.display.DisplayManager) context.getSystemService("display");
            this.mDisplayManager.registerDisplayListener(this, handler);
            initializeDisplayList();
            android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            if (pm != null) {
                this.mSystemUiUid = pm.getPackageUid(pm.getSystemUiServiceComponent().getPackageName(), 1048576L, com.android.server.accessibility.AccessibilityManagerService.this.mCurrentUserId);
            }
        }

        public java.util.ArrayList<android.view.Display> getValidDisplayList() {
            java.util.ArrayList<android.view.Display> arrayList;
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                arrayList = this.mDisplaysList;
            }
            return arrayList;
        }

        private void initializeDisplayList() {
            android.view.Display[] displays = this.mDisplayManager.getDisplays();
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                this.mDisplaysList.clear();
                for (android.view.Display display : displays) {
                    if (isValidDisplay(display)) {
                        this.mDisplaysList.add(display);
                    }
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
            java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services;
            boolean isMainThread = android.os.Looper.getMainLooper().isCurrentThread();
            if (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) {
                com.android.internal.util.Preconditions.checkArgument(isMainThread, "onDisplayAdded must be called from the main thread");
            } else if (!isMainThread) {
                android.util.Slog.e(com.android.server.accessibility.AccessibilityManagerService.LOG_TAG, "onDisplayAdded must be called from the main thread");
            }
            android.view.Display display = this.mDisplayManager.getDisplay(displayId);
            if (!isValidDisplay(display)) {
                return;
            }
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                this.mDisplaysList.add(display);
                com.android.server.accessibility.AccessibilityManagerService.this.mA11yOverlayLayers.put(displayId, com.android.server.accessibility.AccessibilityManagerService.this.mWindowManagerService.getA11yOverlayLayer(displayId));
                if (com.android.server.accessibility.AccessibilityManagerService.this.mInputFilter != null) {
                    com.android.server.accessibility.AccessibilityManagerService.this.mInputFilter.onDisplayAdded(display);
                }
                com.android.server.accessibility.AccessibilityUserState userState = com.android.server.accessibility.AccessibilityManagerService.this.getCurrentUserStateLocked();
                services = new java.util.ArrayList<>(userState.mBoundServices);
                com.android.server.accessibility.AccessibilityManagerService.this.updateMagnificationLocked(userState);
                com.android.server.accessibility.AccessibilityManagerService.this.updateWindowsForAccessibilityCallbackLocked(userState);
                com.android.server.accessibility.AccessibilityManagerService.this.notifyClearAccessibilityCacheLocked();
            }
            if (displayId != 0) {
                for (int i = 0; i < services.size(); i++) {
                    com.android.server.accessibility.AccessibilityServiceConnection boundClient = services.get(i);
                    boundClient.addWindowTokenForDisplay(displayId);
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
            boolean isMainThread = android.os.Looper.getMainLooper().isCurrentThread();
            if (android.os.Build.IS_USERDEBUG || android.os.Build.IS_ENG) {
                com.android.internal.util.Preconditions.checkArgument(isMainThread, "onDisplayRemoved must be called from the main thread");
            } else if (!isMainThread) {
                android.util.Slog.e(com.android.server.accessibility.AccessibilityManagerService.LOG_TAG, "onDisplayRemoved must be called from the main thread");
            }
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                if (removeDisplayFromList(displayId)) {
                    com.android.server.accessibility.AccessibilityManagerService.this.mA11yOverlayLayers.remove(displayId);
                    if (com.android.server.accessibility.AccessibilityManagerService.this.mInputFilter != null) {
                        com.android.server.accessibility.AccessibilityManagerService.this.mInputFilter.onDisplayRemoved(displayId);
                    }
                    com.android.server.accessibility.AccessibilityUserState userState = com.android.server.accessibility.AccessibilityManagerService.this.getCurrentUserStateLocked();
                    if (displayId != 0) {
                        java.util.List<com.android.server.accessibility.AccessibilityServiceConnection> services = userState.mBoundServices;
                        for (int i = 0; i < services.size(); i++) {
                            com.android.server.accessibility.AccessibilityServiceConnection boundClient = services.get(i);
                            boundClient.onDisplayRemoved(displayId);
                        }
                    }
                    com.android.server.accessibility.AccessibilityManagerService.this.mMagnificationController.onDisplayRemoved(displayId);
                    com.android.server.accessibility.AccessibilityManagerService.this.mA11yWindowManager.stopTrackingWindows(displayId);
                }
            }
        }

        private boolean removeDisplayFromList(int displayId) {
            for (int i = 0; i < this.mDisplaysList.size(); i++) {
                if (this.mDisplaysList.get(i).getDisplayId() == displayId) {
                    this.mDisplaysList.remove(i);
                    return true;
                }
            }
            return false;
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
        }

        void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            pw.println("Accessibility Display Listener:");
            pw.println("    SystemUI uid: " + this.mSystemUiUid);
            int size = this.mDisplaysList.size();
            pw.printf("    %d valid display%s: ", java.lang.Integer.valueOf(size), size == 1 ? "" : "s");
            for (int i = 0; i < size; i++) {
                pw.print(this.mDisplaysList.get(i).getDisplayId());
                if (i < size - 1) {
                    pw.print(", ");
                }
            }
            pw.println();
        }

        private boolean isValidDisplay(android.view.Display display) {
            if (display == null || display.getType() == 4) {
                return false;
            }
            if (display.getType() == 5 && (display.getFlags() & 4) != 0 && display.getOwnerUid() != this.mSystemUiUid) {
                return false;
            }
            return true;
        }
    }

    class Client {
        final android.view.accessibility.IAccessibilityManagerClient mCallback;
        int mDeviceId;
        int mLastSentRelevantEventTypes;
        final java.lang.String[] mPackageNames;
        int mUid;

        private Client(android.view.accessibility.IAccessibilityManagerClient callback, int clientUid, com.android.server.accessibility.AccessibilityUserState userState, int deviceId) {
            this.mDeviceId = 0;
            this.mCallback = callback;
            this.mPackageNames = com.android.server.accessibility.AccessibilityManagerService.this.mPackageManager.getPackagesForUid(clientUid);
            this.mUid = clientUid;
            this.mDeviceId = deviceId;
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                if (com.android.server.accessibility.AccessibilityManagerService.this.mProxyManager.isProxyedDeviceId(deviceId)) {
                    this.mLastSentRelevantEventTypes = com.android.server.accessibility.AccessibilityManagerService.this.mProxyManager.computeRelevantEventTypesLocked(this);
                } else {
                    this.mLastSentRelevantEventTypes = com.android.server.accessibility.AccessibilityManagerService.this.computeRelevantEventTypesLocked(userState, this);
                }
            }
        }
    }

    private final class AccessibilityContentObserver extends android.database.ContentObserver {
        private final android.net.Uri mAccessibilityButtonComponentIdUri;
        private final android.net.Uri mAccessibilityButtonTargetsUri;
        private final android.net.Uri mAccessibilityShortcutServiceIdUri;
        private final android.net.Uri mAccessibilitySoftKeyboardModeUri;
        private final android.net.Uri mAlwaysOnMagnificationUri;
        private final android.net.Uri mAudioDescriptionByDefaultUri;
        private final android.net.Uri mAutoclickEnabledUri;
        private final android.net.Uri mEnabledAccessibilityServicesUri;
        private final android.net.Uri mHighTextContrastUri;
        private final android.net.Uri mMagnificationCapabilityUri;
        private final android.net.Uri mMagnificationFollowTypingUri;
        private final android.net.Uri mMagnificationModeUri;
        private final android.net.Uri mMagnificationTwoFingerTripleTapEnabledUri;
        private final android.net.Uri mMagnificationmSingleFingerTripleTapEnabledUri;
        private final android.net.Uri mShowImeWithHardKeyboardUri;
        private final android.net.Uri mTouchExplorationEnabledUri;
        private final android.net.Uri mTouchExplorationGrantedAccessibilityServicesUri;
        private final android.net.Uri mUserInteractiveUiTimeoutUri;
        private final android.net.Uri mUserNonInteractiveUiTimeoutUri;

        public AccessibilityContentObserver(android.os.Handler handler) {
            super(handler);
            this.mTouchExplorationEnabledUri = android.provider.Settings.Secure.getUriFor("touch_exploration_enabled");
            this.mMagnificationmSingleFingerTripleTapEnabledUri = android.provider.Settings.Secure.getUriFor("accessibility_display_magnification_enabled");
            this.mMagnificationTwoFingerTripleTapEnabledUri = android.provider.Settings.Secure.getUriFor("accessibility_magnification_two_finger_triple_tap_enabled");
            this.mAutoclickEnabledUri = android.provider.Settings.Secure.getUriFor("accessibility_autoclick_enabled");
            this.mEnabledAccessibilityServicesUri = android.provider.Settings.Secure.getUriFor("enabled_accessibility_services");
            this.mTouchExplorationGrantedAccessibilityServicesUri = android.provider.Settings.Secure.getUriFor("touch_exploration_granted_accessibility_services");
            this.mHighTextContrastUri = android.provider.Settings.Secure.getUriFor("high_text_contrast_enabled");
            this.mAudioDescriptionByDefaultUri = android.provider.Settings.Secure.getUriFor("enabled_accessibility_audio_description_by_default");
            this.mAccessibilitySoftKeyboardModeUri = android.provider.Settings.Secure.getUriFor("accessibility_soft_keyboard_mode");
            this.mShowImeWithHardKeyboardUri = android.provider.Settings.Secure.getUriFor("show_ime_with_hard_keyboard");
            this.mAccessibilityShortcutServiceIdUri = android.provider.Settings.Secure.getUriFor("accessibility_shortcut_target_service");
            this.mAccessibilityButtonComponentIdUri = android.provider.Settings.Secure.getUriFor("accessibility_button_target_component");
            this.mAccessibilityButtonTargetsUri = android.provider.Settings.Secure.getUriFor("accessibility_button_targets");
            this.mUserNonInteractiveUiTimeoutUri = android.provider.Settings.Secure.getUriFor("accessibility_non_interactive_ui_timeout_ms");
            this.mUserInteractiveUiTimeoutUri = android.provider.Settings.Secure.getUriFor("accessibility_interactive_ui_timeout_ms");
            this.mMagnificationModeUri = android.provider.Settings.Secure.getUriFor("accessibility_magnification_mode");
            this.mMagnificationCapabilityUri = android.provider.Settings.Secure.getUriFor("accessibility_magnification_capability");
            this.mMagnificationFollowTypingUri = android.provider.Settings.Secure.getUriFor("accessibility_magnification_follow_typing_enabled");
            this.mAlwaysOnMagnificationUri = android.provider.Settings.Secure.getUriFor("accessibility_magnification_always_on_enabled");
        }

        public void register(android.content.ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.mTouchExplorationEnabledUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationmSingleFingerTripleTapEnabledUri, false, this, -1);
            if (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture()) {
                contentResolver.registerContentObserver(this.mMagnificationTwoFingerTripleTapEnabledUri, false, this, -1);
            }
            contentResolver.registerContentObserver(this.mAutoclickEnabledUri, false, this, -1);
            contentResolver.registerContentObserver(this.mEnabledAccessibilityServicesUri, false, this, -1);
            contentResolver.registerContentObserver(this.mTouchExplorationGrantedAccessibilityServicesUri, false, this, -1);
            contentResolver.registerContentObserver(this.mHighTextContrastUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAudioDescriptionByDefaultUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilitySoftKeyboardModeUri, false, this, -1);
            contentResolver.registerContentObserver(this.mShowImeWithHardKeyboardUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilityShortcutServiceIdUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilityButtonComponentIdUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAccessibilityButtonTargetsUri, false, this, -1);
            contentResolver.registerContentObserver(this.mUserNonInteractiveUiTimeoutUri, false, this, -1);
            contentResolver.registerContentObserver(this.mUserInteractiveUiTimeoutUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationModeUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationCapabilityUri, false, this, -1);
            contentResolver.registerContentObserver(this.mMagnificationFollowTypingUri, false, this, -1);
            contentResolver.registerContentObserver(this.mAlwaysOnMagnificationUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                com.android.server.accessibility.AccessibilityUserState userState = com.android.server.accessibility.AccessibilityManagerService.this.getCurrentUserStateLocked();
                if (this.mTouchExplorationEnabledUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readTouchExplorationEnabledSettingLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mMagnificationmSingleFingerTripleTapEnabledUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readMagnificationEnabledSettingsLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture() && this.mMagnificationTwoFingerTripleTapEnabledUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readMagnificationTwoFingerTripleTapSettingsLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mAutoclickEnabledUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readAutoclickEnabledSettingLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mEnabledAccessibilityServicesUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readEnabledAccessibilityServicesLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.mSecurityPolicy.onEnabledServicesChangedLocked(userState.mUserId, userState.mEnabledServices);
                        userState.removeDisabledServicesFromTemporaryStatesLocked();
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mTouchExplorationGrantedAccessibilityServicesUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readTouchExplorationGrantedAccessibilityServicesLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mHighTextContrastUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readHighTextContrastEnabledSettingLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mAudioDescriptionByDefaultUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readAudioDescriptionEnabledSettingLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mAccessibilitySoftKeyboardModeUri.equals(uri) || this.mShowImeWithHardKeyboardUri.equals(uri)) {
                    userState.reconcileSoftKeyboardModeWithSettingsLocked();
                } else if (this.mAccessibilityShortcutServiceIdUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readAccessibilityShortcutKeySettingLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mAccessibilityButtonComponentIdUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readAccessibilityButtonTargetComponentLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mAccessibilityButtonTargetsUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readAccessibilityButtonTargetsLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.onUserStateChangedLocked(userState);
                    }
                } else if (this.mUserNonInteractiveUiTimeoutUri.equals(uri) || this.mUserInteractiveUiTimeoutUri.equals(uri)) {
                    com.android.server.accessibility.AccessibilityManagerService.this.readUserRecommendedUiTimeoutSettingsLocked(userState);
                } else if (this.mMagnificationModeUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readMagnificationModeForDefaultDisplayLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.updateMagnificationModeChangeSettingsLocked(userState, 0);
                    }
                } else if (this.mMagnificationCapabilityUri.equals(uri)) {
                    if (com.android.server.accessibility.AccessibilityManagerService.this.readMagnificationCapabilitiesLocked(userState)) {
                        com.android.server.accessibility.AccessibilityManagerService.this.updateMagnificationCapabilitiesSettingsChangeLocked(userState);
                    }
                } else if (this.mMagnificationFollowTypingUri.equals(uri)) {
                    com.android.server.accessibility.AccessibilityManagerService.this.readMagnificationFollowTypingLocked(userState);
                } else if (this.mAlwaysOnMagnificationUri.equals(uri)) {
                    com.android.server.accessibility.AccessibilityManagerService.this.readAlwaysOnMagnificationLocked(userState);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMagnificationCapabilitiesSettingsChangeLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        java.util.ArrayList<android.view.Display> displays = getValidDisplayList();
        for (int i = 0; i < displays.size(); i++) {
            int displayId = displays.get(i).getDisplayId();
            if (fallBackMagnificationModeSettingsLocked(userState, displayId)) {
                updateMagnificationModeChangeSettingsLocked(userState, displayId);
            }
        }
        updateMagnificationConnectionIfNeeded(userState);
        if ((!userState.isMagnificationSingleFingerTripleTapEnabledLocked() && ((!com.android.server.accessibility.Flags.enableMagnificationMultipleFingerMultipleTapGesture() || !userState.isMagnificationTwoFingerTripleTapEnabledLocked()) && !userState.isShortcutMagnificationEnabledLocked())) || userState.getMagnificationCapabilitiesLocked() != 3) {
            for (int i2 = 0; i2 < displays.size(); i2++) {
                getMagnificationConnectionManager().removeMagnificationButton(displays.get(i2).getDisplayId());
            }
        }
    }

    private boolean fallBackMagnificationModeSettingsLocked(com.android.server.accessibility.AccessibilityUserState userState, int displayId) {
        if (userState.isValidMagnificationModeLocked(displayId)) {
            return false;
        }
        android.util.Slog.w(LOG_TAG, "displayId " + displayId + ", invalid magnification mode:" + userState.getMagnificationModeLocked(displayId));
        int capabilities = userState.getMagnificationCapabilitiesLocked();
        userState.setMagnificationModeLocked(displayId, capabilities);
        if (displayId == 0) {
            persistMagnificationModeSettingsLocked(capabilities);
            return true;
        }
        return true;
    }

    private void persistMagnificationModeSettingsLocked(final int mode) {
        com.android.internal.os.BackgroundThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$persistMagnificationModeSettingsLocked$48(mode);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$persistMagnificationModeSettingsLocked$48(int mode) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_mode", mode, this.mCurrentUserId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public int getMagnificationMode(int displayId) {
        int magnificationModeLocked;
        synchronized (this.mLock) {
            magnificationModeLocked = getCurrentUserStateLocked().getMagnificationModeLocked(displayId);
        }
        return magnificationModeLocked;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readMagnificationModeForDefaultDisplayLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int magnificationMode = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_mode", 1, userState.mUserId);
        if (magnificationMode == userState.getMagnificationModeLocked(0)) {
            return false;
        }
        userState.setMagnificationModeLocked(0, magnificationMode);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean readMagnificationCapabilitiesLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        int capabilities = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_capability", 1, userState.mUserId);
        if (capabilities != userState.getMagnificationCapabilitiesLocked()) {
            userState.setMagnificationCapabilitiesLocked(capabilities);
            this.mMagnificationController.setMagnificationCapabilities(capabilities);
            return true;
        }
        return false;
    }

    boolean readMagnificationFollowTypingLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean followTypeEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_follow_typing_enabled", 1, userState.mUserId) == 1;
        if (followTypeEnabled == userState.isMagnificationFollowTypingEnabled()) {
            return false;
        }
        userState.setMagnificationFollowTypingEnabled(followTypeEnabled);
        this.mMagnificationController.setMagnificationFollowTypingEnabled(followTypeEnabled);
        return true;
    }

    public void updateAlwaysOnMagnification() {
        synchronized (this.mLock) {
            readAlwaysOnMagnificationLocked(getCurrentUserState());
        }
    }

    boolean readAlwaysOnMagnificationLocked(com.android.server.accessibility.AccessibilityUserState userState) {
        boolean isSettingsAlwaysOnEnabled = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_magnification_always_on_enabled", 1, userState.mUserId) == 1;
        boolean isAlwaysOnFeatureFlagEnabled = this.mMagnificationController.isAlwaysOnMagnificationFeatureFlagEnabled();
        boolean isAlwaysOnEnabled = isAlwaysOnFeatureFlagEnabled && isSettingsAlwaysOnEnabled;
        if (isAlwaysOnEnabled == userState.isAlwaysOnMagnificationEnabled()) {
            return false;
        }
        userState.setAlwaysOnMagnificationEnabled(isAlwaysOnEnabled);
        this.mMagnificationController.setAlwaysOnMagnificationEnabled(isAlwaysOnEnabled);
        return true;
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void setGestureDetectionPassthroughRegion(int displayId, android.graphics.Region region) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda12
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).setGestureDetectionPassthroughRegionInternal(((java.lang.Integer) obj2).intValue(), (android.graphics.Region) obj3);
            }
        }, this, java.lang.Integer.valueOf(displayId), region));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void setTouchExplorationPassthroughRegion(int displayId, android.graphics.Region region) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda9
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).setTouchExplorationPassthroughRegionInternal(((java.lang.Integer) obj2).intValue(), (android.graphics.Region) obj3);
            }
        }, this, java.lang.Integer.valueOf(displayId), region));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTouchExplorationPassthroughRegionInternal(int displayId, android.graphics.Region region) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.setTouchExplorationPassthroughRegion(displayId, region);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setGestureDetectionPassthroughRegionInternal(int displayId, android.graphics.Region region) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.setGestureDetectionPassthroughRegion(displayId, region);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void setServiceDetectsGesturesEnabled(int displayId, boolean mode) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda28
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).setServiceDetectsGesturesInternal(((java.lang.Integer) obj2).intValue(), ((java.lang.Boolean) obj3).booleanValue());
            }
        }, this, java.lang.Integer.valueOf(displayId), java.lang.Boolean.valueOf(mode)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setServiceDetectsGesturesInternal(int displayId, boolean mode) {
        synchronized (this.mLock) {
            getCurrentUserStateLocked().setServiceDetectsGesturesEnabled(displayId, mode);
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.setServiceDetectsGesturesEnabled(displayId, mode);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestTouchExploration(int displayId) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda47
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).requestTouchExplorationInternal(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestTouchExplorationInternal(int displayId) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.requestTouchExploration(displayId);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestDragging(int displayId, int pointerId) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda29
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).requestDraggingInternal(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(pointerId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestDraggingInternal(int displayId, int pointerId) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.requestDragging(displayId, pointerId);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestDelegating(int displayId) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda75
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).requestDelegatingInternal(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestDelegatingInternal(int displayId) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.requestDelegating(displayId);
            }
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onDoubleTap(int displayId) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda59
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).onDoubleTapInternal(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDoubleTapInternal(int displayId) {
        com.android.server.accessibility.AccessibilityInputFilter inputFilter = null;
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                inputFilter = this.mInputFilter;
            }
        }
        if (inputFilter != null) {
            inputFilter.onDoubleTap(displayId);
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void onDoubleTapAndHold(int displayId) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda37
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).onDoubleTapAndHoldInternal(((java.lang.Integer) obj2).intValue());
            }
        }, this, java.lang.Integer.valueOf(displayId)));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void requestImeLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection connection) {
        if (!(connection instanceof com.android.server.accessibility.AccessibilityServiceConnection) || (connection instanceof com.android.server.accessibility.ProxyAccessibilityServiceConnection)) {
            if (DEBUG) {
                android.util.Slog.d(LOG_TAG, "The connection should be a real connection but was " + connection);
            }
        } else {
            com.android.server.accessibility.AccessibilityServiceConnection realConnection = (com.android.server.accessibility.AccessibilityServiceConnection) connection;
            this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda57
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.accessibility.AccessibilityManagerService) obj).createSessionForConnection((com.android.server.accessibility.AccessibilityServiceConnection) obj2);
                }
            }, this, realConnection));
            this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda58
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.accessibility.AccessibilityManagerService) obj).bindAndStartInputForConnection((com.android.server.accessibility.AccessibilityServiceConnection) obj2);
                }
            }, this, realConnection));
        }
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void unbindImeLocked(com.android.server.accessibility.AbstractAccessibilityServiceConnection connection) {
        if (!(connection instanceof com.android.server.accessibility.AccessibilityServiceConnection) || (connection instanceof com.android.server.accessibility.ProxyAccessibilityServiceConnection)) {
            if (DEBUG) {
                android.util.Slog.d(LOG_TAG, "The connection should be a real connection but was " + connection);
            }
        } else {
            com.android.server.accessibility.AccessibilityServiceConnection realConnection = (com.android.server.accessibility.AccessibilityServiceConnection) connection;
            this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda62
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    ((com.android.server.accessibility.AccessibilityManagerService) obj).unbindInputForConnection((com.android.server.accessibility.AccessibilityServiceConnection) obj2);
                }
            }, this, realConnection));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSessionForConnection(com.android.server.accessibility.AccessibilityServiceConnection connection) {
        synchronized (this.mLock) {
            if (this.mInputSessionRequested) {
                connection.createImeSessionLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindAndStartInputForConnection(com.android.server.accessibility.AccessibilityServiceConnection connection) {
        synchronized (this.mLock) {
            if (this.mInputBound) {
                connection.bindInputLocked();
                connection.startInputLocked(this.mRemoteInputConnection, this.mEditorInfo, this.mRestarting);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindInputForConnection(com.android.server.accessibility.AccessibilityServiceConnection connection) {
        com.android.server.inputmethod.InputMethodManagerInternal.get().unbindAccessibilityFromCurrentClient(connection.mId, connection.mUserId);
        synchronized (this.mLock) {
            connection.unbindInputLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDoubleTapAndHoldInternal(int displayId) {
        synchronized (this.mLock) {
            if (this.mHasInputFilter && this.mInputFilter != null) {
                this.mInputFilter.onDoubleTapAndHold(displayId);
            }
        }
    }

    private void updateFocusAppearanceDataLocked(final com.android.server.accessibility.AccessibilityUserState userState) {
        if (userState.mUserId != this.mCurrentUserId) {
            return;
        }
        if (this.mTraceManager.isA11yTracingEnabledForTypes(2L)) {
            this.mTraceManager.logTrace("AccessibilityManagerService.updateFocusAppearanceDataLocked", 2L, "userState=" + userState);
        }
        this.mMainHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateFocusAppearanceDataLocked$50(userState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFocusAppearanceDataLocked$50(final com.android.server.accessibility.AccessibilityUserState userState) {
        broadcastToClients(userState, com.android.internal.util.FunctionalUtils.ignoreRemoteException(new com.android.internal.util.FunctionalUtils.RemoteExceptionIgnoringConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda81
            public final void acceptOrThrow(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$updateFocusAppearanceDataLocked$49(userState, (com.android.server.accessibility.AccessibilityManagerService.Client) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFocusAppearanceDataLocked$49(com.android.server.accessibility.AccessibilityUserState userState, com.android.server.accessibility.AccessibilityManagerService.Client client) throws android.os.RemoteException {
        if (!this.mProxyManager.isProxyedDeviceId(client.mDeviceId)) {
            client.mCallback.setFocusAppearance(userState.getFocusStrokeWidthLocked(), userState.getFocusColorLocked());
        }
    }

    public com.android.server.accessibility.AccessibilityTraceManager getTraceManager() {
        return this.mTraceManager;
    }

    public void scheduleBindInput() {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda27
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).bindInput();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindInput() {
        synchronized (this.mLock) {
            this.mInputBound = true;
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
                if (service.requestImeApis()) {
                    service.bindInputLocked();
                }
            }
        }
    }

    public void scheduleUnbindInput() {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda67
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).unbindInput();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindInput() {
        synchronized (this.mLock) {
            this.mInputBound = false;
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
                if (service.requestImeApis()) {
                    service.unbindInputLocked();
                }
            }
        }
    }

    public void scheduleStartInput(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection connection, android.view.inputmethod.EditorInfo editorInfo, boolean restarting) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda44
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).startInput((com.android.internal.inputmethod.IRemoteAccessibilityInputConnection) obj2, (android.view.inputmethod.EditorInfo) obj3, ((java.lang.Boolean) obj4).booleanValue());
            }
        }, this, connection, editorInfo, java.lang.Boolean.valueOf(restarting)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startInput(com.android.internal.inputmethod.IRemoteAccessibilityInputConnection connection, android.view.inputmethod.EditorInfo editorInfo, boolean restarting) {
        synchronized (this.mLock) {
            this.mRemoteInputConnection = connection;
            this.mEditorInfo = editorInfo;
            this.mRestarting = restarting;
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
                if (service.requestImeApis()) {
                    service.startInputLocked(connection, editorInfo, restarting);
                }
            }
        }
    }

    public void scheduleCreateImeSession(android.util.ArraySet<java.lang.Integer> ignoreSet) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda25
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).createImeSession((android.util.ArraySet) obj2);
            }
        }, this, ignoreSet));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createImeSession(android.util.ArraySet<java.lang.Integer> ignoreSet) {
        synchronized (this.mLock) {
            this.mInputSessionRequested = true;
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
                if (!ignoreSet.contains(java.lang.Integer.valueOf(service.mId)) && service.requestImeApis()) {
                    service.createImeSessionLocked();
                }
            }
        }
    }

    public void scheduleSetImeSessionEnabled(android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> sessions, boolean enabled) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda82
            public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                ((com.android.server.accessibility.AccessibilityManagerService) obj).setImeSessionEnabled((android.util.SparseArray) obj2, ((java.lang.Boolean) obj3).booleanValue());
            }
        }, this, sessions, java.lang.Boolean.valueOf(enabled)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImeSessionEnabled(android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> sessions, boolean enabled) {
        synchronized (this.mLock) {
            com.android.server.accessibility.AccessibilityUserState userState = getCurrentUserStateLocked();
            for (int i = userState.mBoundServices.size() - 1; i >= 0; i--) {
                com.android.server.accessibility.AccessibilityServiceConnection service = userState.mBoundServices.get(i);
                if (sessions.contains(service.mId) && service.requestImeApis()) {
                    service.setImeSessionEnabledLocked(sessions.get(service.mId), enabled);
                }
            }
        }
    }

    public void injectInputEventToInputFilter(android.view.InputEvent event) {
        injectInputEventToInputFilter_enforcePermission();
        synchronized (this.mLock) {
            long endMillis = android.os.SystemClock.uptimeMillis() + 1000;
            while (!this.mInputFilterInstalled && android.os.SystemClock.uptimeMillis() < endMillis) {
                try {
                    this.mLock.wait(endMillis - android.os.SystemClock.uptimeMillis());
                } catch (java.lang.InterruptedException e) {
                }
            }
        }
        if (this.mInputFilterInstalled && this.mInputFilter != null) {
            this.mInputFilter.onInputEvent(event, 1090519040);
        } else {
            android.util.Slog.w(LOG_TAG, "Cannot injectInputEventToInputFilter because the AccessibilityInputFilter is not installed.");
        }
    }

    private final class SendWindowStateChangedEventRunnable implements java.lang.Runnable {
        private final android.view.accessibility.AccessibilityEvent mPendingEvent;
        private final int mWindowId;

        SendWindowStateChangedEventRunnable(android.view.accessibility.AccessibilityEvent event) {
            this.mPendingEvent = event;
            this.mWindowId = event.getWindowId();
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.accessibility.AccessibilityManagerService.this.mLock) {
                android.util.Slog.w(com.android.server.accessibility.AccessibilityManagerService.LOG_TAG, " wait for adding window timeout: " + this.mWindowId);
                sendPendingEventLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendPendingEventLocked() {
            com.android.server.accessibility.AccessibilityManagerService.this.mSendWindowStateChangedEventRunnables.remove(this);
            com.android.server.accessibility.AccessibilityManagerService.this.dispatchAccessibilityEventLocked(this.mPendingEvent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getWindowId() {
            return this.mWindowId;
        }
    }

    public static class ManagerPackageMonitor extends com.android.internal.content.PackageMonitor {
        private final com.android.server.accessibility.AccessibilityManagerService mManagerService;

        public ManagerPackageMonitor(com.android.server.accessibility.AccessibilityManagerService managerService) {
            super(true);
            this.mManagerService = managerService;
        }

        public void onSomePackagesChanged() {
            if (this.mManagerService.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                this.mManagerService.mTraceManager.logTrace("AccessibilityManagerService.PM.onSomePackagesChanged", 32768L);
            }
            int userId = getChangingUserId();
            java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parsedAccessibilityServiceInfos = this.mManagerService.parseAccessibilityServiceInfos(userId);
            java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parsedAccessibilityShortcutInfos = this.mManagerService.parseAccessibilityShortcutInfos(userId);
            synchronized (this.mManagerService.getLock()) {
                if (userId != this.mManagerService.getCurrentUserIdLocked()) {
                    return;
                }
                if (com.android.server.accessibility.Flags.skipPackageChangeBeforeUserSwitch() && !this.mManagerService.isServiceInitializedLocked()) {
                    android.util.Slog.w(com.android.server.accessibility.AccessibilityManagerService.LOG_TAG, "onSomePackagesChanged: service not initialized, skip the callback.");
                } else {
                    this.mManagerService.onSomePackagesChangedLocked(parsedAccessibilityServiceInfos, parsedAccessibilityShortcutInfos);
                }
            }
        }

        public void onPackageUpdateFinished(final java.lang.String packageName, int uid) {
            if (this.mManagerService.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                this.mManagerService.mTraceManager.logTrace("AccessibilityManagerService.PM.onPackageUpdateFinished", 32768L, "packageName=" + packageName + ";uid=" + uid);
            }
            int userId = getChangingUserId();
            java.util.List<android.accessibilityservice.AccessibilityServiceInfo> parsedAccessibilityServiceInfos = this.mManagerService.parseAccessibilityServiceInfos(userId);
            java.util.List<android.accessibilityservice.AccessibilityShortcutInfo> parsedAccessibilityShortcutInfos = this.mManagerService.parseAccessibilityShortcutInfos(userId);
            synchronized (this.mManagerService.getLock()) {
                if (userId != this.mManagerService.getCurrentUserIdLocked()) {
                    return;
                }
                com.android.server.accessibility.AccessibilityUserState userState = this.mManagerService.getUserStateLocked(userId);
                boolean reboundAService = userState.getBindingServicesLocked().removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$ManagerPackageMonitor$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.accessibility.AccessibilityManagerService.ManagerPackageMonitor.lambda$onPackageUpdateFinished$0(packageName, (android.content.ComponentName) obj);
                    }
                }) || userState.mCrashedServices.removeIf(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$ManagerPackageMonitor$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.accessibility.AccessibilityManagerService.ManagerPackageMonitor.lambda$onPackageUpdateFinished$1(packageName, (android.content.ComponentName) obj);
                    }
                });
                userState.mInstalledServices.clear();
                boolean configurationChanged = this.mManagerService.readConfigurationForUserStateLocked(userState, parsedAccessibilityServiceInfos, parsedAccessibilityShortcutInfos);
                if (reboundAService || configurationChanged) {
                    this.mManagerService.onUserStateChangedLocked(userState);
                }
                this.mManagerService.migrateAccessibilityButtonSettingsIfNecessaryLocked(userState, packageName, 0);
            }
        }

        static /* synthetic */ boolean lambda$onPackageUpdateFinished$0(java.lang.String packageName, android.content.ComponentName component) {
            return component != null && component.getPackageName().equals(packageName);
        }

        static /* synthetic */ boolean lambda$onPackageUpdateFinished$1(java.lang.String packageName, android.content.ComponentName component) {
            return component != null && component.getPackageName().equals(packageName);
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            if (this.mManagerService.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                this.mManagerService.mTraceManager.logTrace("AccessibilityManagerService.PM.onPackageRemoved", 32768L, "packageName=" + packageName + ";uid=" + uid);
            }
            synchronized (this.mManagerService.getLock()) {
                int userId = getChangingUserId();
                if (userId != this.mManagerService.getCurrentUserIdLocked()) {
                    return;
                }
                this.mManagerService.onPackageRemovedLocked(packageName);
            }
        }

        public boolean onHandleForceStop(android.content.Intent intent, final java.lang.String[] packages, int uid, boolean doit) {
            if (this.mManagerService.mTraceManager.isA11yTracingEnabledForTypes(32768L)) {
                this.mManagerService.mTraceManager.logTrace("AccessibilityManagerService.PM.onHandleForceStop", 32768L, "intent=" + intent + ";packages=" + java.util.Arrays.toString(packages) + ";uid=" + uid + ";doit=" + doit);
            }
            synchronized (this.mManagerService.getLock()) {
                int userId = getChangingUserId();
                if (userId != this.mManagerService.getCurrentUserIdLocked()) {
                    return false;
                }
                com.android.server.accessibility.AccessibilityUserState userState = this.mManagerService.getUserStateLocked(userId);
                if (com.android.server.accessibility.Flags.managerPackageMonitorLogicFix()) {
                    if (!doit) {
                        return userState.mEnabledServices.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$ManagerPackageMonitor$$ExternalSyntheticLambda3
                            @Override // java.util.function.Predicate
                            public final boolean test(java.lang.Object obj) {
                                return java.util.Arrays.stream(packages).anyMatch(new java.util.function.Predicate() { // from class: com.android.server.accessibility.AccessibilityManagerService$ManagerPackageMonitor$$ExternalSyntheticLambda2
                                    @Override // java.util.function.Predicate
                                    public final boolean test(java.lang.Object obj2) {
                                        return ((java.lang.String) obj2).equals(componentName.getPackageName());
                                    }
                                });
                            }
                        });
                    }
                    if (this.mManagerService.onPackagesForceStoppedLocked(packages, userState)) {
                        this.mManagerService.onUserStateChangedLocked(userState);
                    }
                    return false;
                }
                if (!doit || !this.mManagerService.onPackagesForceStoppedLocked(packages, userState)) {
                    return true;
                }
                this.mManagerService.onUserStateChangedLocked(userState);
                return false;
            }
        }

        public boolean onPackageChanged(java.lang.String packageName, int uid, java.lang.String[] components) {
            return true;
        }
    }

    void sendPendingWindowStateChangedEventsForAvailableWindowLocked(int windowId) {
        int eventSize = this.mSendWindowStateChangedEventRunnables.size();
        for (int i = eventSize - 1; i >= 0; i--) {
            com.android.server.accessibility.AccessibilityManagerService.SendWindowStateChangedEventRunnable runnable = this.mSendWindowStateChangedEventRunnables.get(i);
            if (runnable.getWindowId() == windowId) {
                this.mMainHandler.removeCallbacks(runnable);
                runnable.sendPendingEventLocked();
            }
        }
    }

    private boolean postponeWindowStateEvent(android.view.accessibility.AccessibilityEvent event) {
        synchronized (this.mLock) {
            int resolvedWindowId = this.mA11yWindowManager.resolveParentWindowIdLocked(event.getWindowId());
            if (this.mA11yWindowManager.findWindowInfoByIdLocked(resolvedWindowId) != null) {
                return false;
            }
            com.android.server.accessibility.AccessibilityManagerService.SendWindowStateChangedEventRunnable pendingRunnable = new com.android.server.accessibility.AccessibilityManagerService.SendWindowStateChangedEventRunnable(new android.view.accessibility.AccessibilityEvent(event));
            this.mMainHandler.postDelayed(pendingRunnable, 500L);
            this.mSendWindowStateChangedEventRunnables.add(pendingRunnable);
            return true;
        }
    }

    public void attachAccessibilityOverlayToDisplay(int displayId, android.view.SurfaceControl sc) {
        attachAccessibilityOverlayToDisplay_enforcePermission();
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda35(), this, -1, java.lang.Integer.valueOf(displayId), sc, (java.lang.Object) null));
    }

    @Override // com.android.server.accessibility.AbstractAccessibilityServiceConnection.SystemSupport
    public void attachAccessibilityOverlayToDisplay(int interactionId, int displayId, android.view.SurfaceControl sc, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback) {
        this.mMainHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.accessibility.AccessibilityManagerService$$ExternalSyntheticLambda35(), this, java.lang.Integer.valueOf(interactionId), java.lang.Integer.valueOf(displayId), sc, callback));
    }

    void attachAccessibilityOverlayToDisplayInternal(int interactionId, int displayId, android.view.SurfaceControl sc, android.view.accessibility.IAccessibilityInteractionConnectionCallback callback) {
        int result;
        if (!this.mA11yOverlayLayers.contains(displayId)) {
            this.mA11yOverlayLayers.put(displayId, this.mWindowManagerService.getA11yOverlayLayer(displayId));
        }
        android.view.SurfaceControl parent = this.mA11yOverlayLayers.get(displayId);
        if (parent == null) {
            android.util.Slog.e(LOG_TAG, "Unable to get accessibility overlay SurfaceControl.");
            this.mA11yOverlayLayers.remove(displayId);
            result = 2;
        } else {
            android.view.SurfaceControl.Transaction t = new android.view.SurfaceControl.Transaction();
            t.reparent(sc, parent).setTrustedOverlay(sc, true).apply();
            t.close();
            result = 0;
        }
        if (callback != null) {
            try {
                callback.sendAttachOverlayResult(result, interactionId);
            } catch (android.os.RemoteException re) {
                android.util.Slog.e(LOG_TAG, "Exception while attaching overlay.", re);
            }
        }
    }

    private void skipVolumeShortcutDialogTimeoutRestriction(int userId) {
        persistIntToSetting(userId, "skip_accessibility_shortcut_dialog_timeout_restriction", 1);
    }

    private void logMetricForQsShortcutConfiguration(boolean enable, int numOfFeatures) {
        if (numOfFeatures <= 0) {
            return;
        }
        java.lang.String metricId = enable ? METRIC_ID_QS_SHORTCUT_ADD : METRIC_ID_QS_SHORTCUT_REMOVE;
        com.android.modules.expresslog.Counter.logIncrementWithUid(metricId, android.os.Binder.getCallingUid(), numOfFeatures);
    }
}
