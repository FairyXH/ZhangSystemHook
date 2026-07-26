package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public class InputMethodManagerService implements com.android.server.inputmethod.IInputMethodManagerImpl.Callback, com.android.server.inputmethod.ZeroJankProxy.Callback, android.os.Handler.Callback {
    static final int FALLBACK_DISPLAY_ID = 0;
    private static final java.lang.String HANDLER_THREAD_NAME = "android.imms";
    private static final int INVALID_SUBTYPE_HASHCODE = -1;
    private static final int MSG_DISPATCH_ON_INPUT_METHOD_LIST_UPDATED = 5010;
    private static final int MSG_FINISH_HANDWRITING = 1110;
    private static final int MSG_HARD_KEYBOARD_SWITCH_CHANGED = 4000;
    private static final int MSG_HIDE_ALL_INPUT_METHODS = 1035;
    private static final int MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE = 7000;
    private static final int MSG_PREPARE_HANDWRITING_DELEGATION = 1130;
    private static final int MSG_REMOVE_HANDWRITING_WINDOW = 1120;
    private static final int MSG_REMOVE_IME_SURFACE = 1060;
    private static final int MSG_REMOVE_IME_SURFACE_FROM_WINDOW = 1061;
    private static final int MSG_RESET_HANDWRITING = 1090;
    private static final int MSG_SET_INTERACTIVE = 3030;
    private static final int MSG_SHOW_IM_SUBTYPE_PICKER = 1;
    private static final int MSG_START_HANDWRITING = 1100;
    private static final int MSG_SYSTEM_UNLOCK_USER = 5000;
    private static final int MSG_UPDATE_IME_WINDOW_STATUS = 1070;
    private static final int NOT_A_SUBTYPE_ID = -1;
    private static final java.lang.String PACKAGE_MONITOR_THREAD_NAME = "android.imms2";
    public static final java.lang.String PROTO_ARG = "--proto";
    static final java.lang.String TAG = "InputMethodManagerService";
    private static final java.lang.String TAG_TRY_SUPPRESSING_IME_SWITCHER = "TrySuppressingImeSwitcher";
    private final android.app.ActivityManagerInternal mActivityManagerInternal;
    private android.media.AudioManagerInternal mAudioManagerInternal;
    int mBackDisposition;
    boolean mBoundToAccessibility;
    boolean mBoundToMethod;
    private final com.android.server.inputmethod.ClientController mClientController;
    final android.content.Context mContext;
    private com.android.server.inputmethod.ClientState mCurClient;
    android.view.inputmethod.EditorInfo mCurEditorInfo;
    android.window.ImeOnBackInvokedDispatcher mCurImeDispatcher;
    com.android.internal.inputmethod.IRemoteInputConnection mCurInputConnection;
    com.android.internal.inputmethod.IRemoteAccessibilityInputConnection mCurRemoteAccessibilityInputConnection;
    private android.view.inputmethod.ImeTracker.Token mCurStatsToken;
    private int mCurrentUserId;
    private final com.android.internal.inputmethod.ImeTracing.ServiceDumper mDumper;
    android.util.SparseArray<com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState> mEnabledAccessibilitySessions;
    com.android.server.inputmethod.InputMethodManagerService.SessionState mEnabledSession;
    private final boolean mExperimentalConcurrentMultiUserModeEnabled;
    private final java.util.WeakHashMap<android.os.IBinder, java.lang.Boolean> mFocusedWindowPerceptible;
    private final android.os.Handler mHandler;
    private com.android.server.inputmethod.HardwareKeyboardShortcutController mHardwareKeyboardShortcutController;
    private final com.android.server.inputmethod.HandwritingModeController mHwController;
    com.android.server.inputmethod.ImeBindingState mImeBindingState;
    private com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper mImeDrawsImeNavBarRes;
    java.util.concurrent.Future<?> mImeDrawsImeNavBarResLazyInitFuture;
    final com.android.server.inputmethod.ImePlatformCompatUtils mImePlatformCompatUtils;
    private final java.util.WeakHashMap<android.os.IBinder, android.os.IBinder> mImeTargetWindowMap;
    private final com.android.server.inputmethod.ImeTrackerService mImeTrackerService;
    int mImeWindowVis;
    private com.android.server.inputmethod.InputMethodManagerService.InputMethodManagerServiceWrapper mImmsWrapper;
    boolean mInFullscreenMode;
    final com.android.server.input.InputManagerInternal mInputManagerInternal;
    final com.android.server.inputmethod.InputMethodDeviceConfigs mInputMethodDeviceConfigs;
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.inputmethod.InputMethodManagerInternal.InputMethodListListener> mInputMethodListListeners;
    private final android.os.Handler mIoHandler;
    boolean mIsInteractive;
    android.os.IBinder mLastImeTargetWindow;
    private final com.android.server.inputmethod.InputMethodMenuController mMenuController;
    private final com.android.server.inputmethod.InputMethodManagerService.MyPackageMonitor mMyPackageMonitor;
    private final java.lang.String[] mNonPreemptibleInputMethods;
    final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final boolean mPreventImeStartupUnlessTextEditor;
    private final com.android.server.utils.PriorityDump.PriorityDumper mPriorityDumper;
    final android.content.res.Resources mRes;
    final com.android.server.inputmethod.InputMethodManagerService.SettingsObserver mSettingsObserver;
    private boolean mShowOngoingImeSwitcherForPhones;
    private final java.lang.String mSlotIme;
    private final com.android.server.inputmethod.SoftInputShowHideHistory mSoftInputShowHideHistory;
    private final com.android.server.inputmethod.StartInputHistory mStartInputHistory;
    private com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal;
    private android.util.IntArray mStylusIds;
    private com.android.server.inputmethod.InputMethodSubtypeSwitchingController mSwitchingController;
    boolean mSystemReady;
    private com.android.server.inputmethod.UserDataRepository mUserDataRepository;
    private final com.android.server.pm.UserManagerInternal mUserManagerInternal;
    private com.android.server.inputmethod.InputMethodManagerService.UserSwitchHandlerTask mUserSwitchHandlerTask;
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal mVdmInternal;
    private final android.util.SparseArray<java.lang.String> mVirtualDeviceMethodMap;
    private final com.android.server.inputmethod.DefaultImeVisibilityApplier mVisibilityApplier;
    private final com.android.server.inputmethod.ImeVisibilityStateComputer mVisibilityStateComputer;
    final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private static final java.lang.Integer VIRTUAL_STYLUS_ID_FOR_TEST = 999999;
    static boolean DEBUG = false;
    private static final com.android.server.inputmethod.IInputMethodManagerServiceExt.IStaticExt sImmsStaticExt = (com.android.server.inputmethod.IInputMethodManagerServiceExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.inputmethod.IInputMethodManagerServiceExt.IStaticExt.class).create();

    @java.lang.FunctionalInterface
    interface ImeDisplayValidator {
        int getDisplayImePolicy(int i);
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface MultiUserUnawareField {
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface SharedByAllUsersField {
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ShellCommandResult {
        public static final int FAILURE = -1;
        public static final int SUCCESS = 0;
    }

    static boolean shouldEnableExperimentalConcurrentMultiUserMode(android.content.Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.type.automotive") && android.os.UserManager.isVisibleBackgroundUsersEnabled() && context.getResources().getBoolean(android.R.bool.config_perDisplayFocusEnabled) && android.view.inputmethod.Flags.concurrentInputMethods();
    }

    static class SessionState {
        android.view.InputChannel mChannel;
        final com.android.server.inputmethod.ClientState mClient;
        final com.android.server.inputmethod.IInputMethodInvoker mMethod;
        com.android.internal.inputmethod.IInputMethodSession mSession;

        public java.lang.String toString() {
            return "SessionState{uid=" + this.mClient.mUid + " pid=" + this.mClient.mPid + " method=" + java.lang.Integer.toHexString(com.android.server.inputmethod.IInputMethodInvoker.getBinderIdentityHashCode(this.mMethod)) + " session=" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mSession)) + " channel=" + this.mChannel + "}";
        }

        SessionState(com.android.server.inputmethod.ClientState client, com.android.server.inputmethod.IInputMethodInvoker method, com.android.internal.inputmethod.IInputMethodSession session, android.view.InputChannel channel) {
            this.mClient = client;
            this.mMethod = method;
            this.mSession = session;
            this.mChannel = channel;
        }
    }

    static class AccessibilitySessionState {
        final com.android.server.inputmethod.ClientState mClient;
        final int mId;
        public com.android.internal.inputmethod.IAccessibilityInputMethodSession mSession;

        public java.lang.String toString() {
            return "AccessibilitySessionState{uid=" + this.mClient.mUid + " pid=" + this.mClient.mPid + " id=" + java.lang.Integer.toHexString(this.mId) + " session=" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this.mSession)) + "}";
        }

        AccessibilitySessionState(com.android.server.inputmethod.ClientState client, int id, com.android.internal.inputmethod.IAccessibilityInputMethodSession session) {
            this.mClient = client;
            this.mId = id;
            this.mSession = session;
        }
    }

    com.android.server.inputmethod.UserDataRepository.UserData getUserData(int userId) {
        return this.mUserDataRepository.getOrCreate(userId);
    }

    com.android.server.inputmethod.InputMethodBindingController getInputMethodBindingController(int userId) {
        return getUserData(userId).mBindingController;
    }

    java.lang.String getSelectedMethodIdLocked() {
        return getInputMethodBindingController(this.mCurrentUserId).getSelectedMethodId();
    }

    android.view.inputmethod.InputMethodInfo queryInputMethodForCurrentUserLocked(java.lang.String imeId) {
        return com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId).getMethodMap().get(imeId);
    }

    android.os.IBinder getCurTokenLocked() {
        return getInputMethodBindingController(this.mCurrentUserId).getCurToken();
    }

    int getCurTokenDisplayIdLocked() {
        return getInputMethodBindingController(this.mCurrentUserId).getCurTokenDisplayId();
    }

    com.android.server.inputmethod.IInputMethodInvoker getCurMethodLocked() {
        return getInputMethodBindingController(this.mCurrentUserId).getCurMethod();
    }

    class SettingsObserver extends android.database.ContentObserver {
        java.lang.String mLastEnabled;
        boolean mRegistered;
        int mUserId;

        SettingsObserver(android.os.Handler handler) {
            super(handler);
            this.mRegistered = false;
            this.mLastEnabled = "";
        }

        public void registerContentObserverLocked(int userId) {
            if (this.mRegistered && this.mUserId == userId) {
                return;
            }
            android.content.ContentResolver resolver = com.android.server.inputmethod.InputMethodManagerService.this.mContext.getContentResolver();
            if (this.mRegistered) {
                com.android.server.inputmethod.InputMethodManagerService.this.mContext.getContentResolver().unregisterContentObserver(this);
                this.mRegistered = false;
            }
            if (this.mUserId != userId) {
                this.mLastEnabled = "";
                this.mUserId = userId;
            }
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("default_input_method"), false, this, userId);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("enabled_input_methods"), false, this, userId);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("selected_input_method_subtype"), false, this, userId);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("show_ime_with_hard_keyboard"), false, this, userId);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("accessibility_soft_keyboard_mode"), false, this, userId);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("stylus_handwriting_enabled"), false, this);
            com.android.server.inputmethod.InputMethodManagerService.this.mImmsWrapper.getExtImpl().onServerRegisterContentObserver(this, userId);
            this.mRegistered = true;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            android.net.Uri showImeUri = android.provider.Settings.Secure.getUriFor("show_ime_with_hard_keyboard");
            android.net.Uri accessibilityRequestingNoImeUri = android.provider.Settings.Secure.getUriFor("accessibility_soft_keyboard_mode");
            android.net.Uri stylusHandwritingEnabledUri = android.provider.Settings.Secure.getUriFor("stylus_handwriting_enabled");
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (com.android.server.inputmethod.InputMethodManagerService.this.getWrapper().getExtImpl().onServerSettingsObserverChanged(this, this.mUserId, selfChange, uri)) {
                    return;
                }
                if (showImeUri.equals(uri)) {
                    com.android.server.inputmethod.InputMethodManagerService.this.mMenuController.updateKeyboardFromSettingsLocked();
                } else if (accessibilityRequestingNoImeUri.equals(uri)) {
                    int accessibilitySoftKeyboardSetting = android.provider.Settings.Secure.getIntForUser(com.android.server.inputmethod.InputMethodManagerService.this.mContext.getContentResolver(), "accessibility_soft_keyboard_mode", 0, this.mUserId);
                    com.android.server.inputmethod.InputMethodManagerService.this.mVisibilityStateComputer.getImePolicy().setA11yRequestNoSoftKeyboard(accessibilitySoftKeyboardSetting);
                    if (com.android.server.inputmethod.InputMethodManagerService.this.mVisibilityStateComputer.getImePolicy().isA11yRequestNoSoftKeyboard()) {
                        com.android.server.inputmethod.InputMethodManagerService.this.hideCurrentInputLocked(com.android.server.inputmethod.InputMethodManagerService.this.mImeBindingState.mFocusedWindow, 0, 16);
                    } else if (com.android.server.inputmethod.InputMethodManagerService.this.isShowRequestedForCurrentWindow()) {
                        com.android.server.inputmethod.InputMethodManagerService.this.showCurrentInputLocked(com.android.server.inputmethod.InputMethodManagerService.this.mImeBindingState.mFocusedWindow, 1, 9);
                    }
                } else if (stylusHandwritingEnabledUri.equals(uri)) {
                    android.view.inputmethod.InputMethodManager.invalidateLocalStylusHandwritingAvailabilityCaches();
                    android.view.inputmethod.InputMethodManager.invalidateLocalConnectionlessStylusHandwritingAvailabilityCaches();
                } else {
                    boolean enabledChanged = false;
                    java.lang.String newEnabled = com.android.server.inputmethod.InputMethodSettingsRepository.get(com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId).getEnabledInputMethodsStr();
                    if (!this.mLastEnabled.equals(newEnabled)) {
                        this.mLastEnabled = newEnabled;
                        enabledChanged = true;
                    }
                    com.android.server.inputmethod.InputMethodManagerService.this.updateInputMethodsFromSettingsLocked(enabledChanged);
                }
            }
        }

        public java.lang.String toString() {
            return "SettingsObserver{mUserId=" + this.mUserId + " mRegistered=" + this.mRegistered + " mLastEnabled=" + this.mLastEnabled + "}";
        }
    }

    private final class ImmsBroadcastReceiverForAllUsers extends android.content.BroadcastReceiver {
        private ImmsBroadcastReceiverForAllUsers() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            java.lang.String action = intent.getAction();
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action)) {
                android.content.BroadcastReceiver.PendingResult pendingResult = getPendingResult();
                if (pendingResult == null) {
                    return;
                }
                int senderUserId = pendingResult.getSendingUserId();
                if (senderUserId != -1) {
                    synchronized (com.android.server.inputmethod.ImfLock.class) {
                        if (senderUserId != com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId) {
                            return;
                        }
                    }
                }
                com.android.server.inputmethod.InputMethodManagerService.this.mMenuController.hideInputMethodMenu();
                return;
            }
            android.util.Slog.w(com.android.server.inputmethod.InputMethodManagerService.TAG, "Unexpected intent " + intent);
        }
    }

    void onActionLocaleChanged(android.os.LocaleList prevLocales, android.os.LocaleList newLocales) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onActionLocaleChanged prev=" + prevLocales + " new=" + newLocales);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (this.mSystemReady) {
                for (int userId : this.mUserManagerInternal.getUserIds()) {
                    com.android.server.inputmethod.InputMethodSettings settings = queryInputMethodServicesInternal(this.mContext, userId, com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId), 0);
                    com.android.server.inputmethod.InputMethodSettingsRepository.put(userId, settings);
                }
                postInputMethodSettingUpdatedLocked(true);
                resetDefaultImeLocked(this.mContext);
                updateFromSettingsLocked(true);
            }
        }
    }

    final class MyPackageMonitor extends com.android.internal.content.PackageMonitor {
        private java.util.ArrayList<java.lang.String> mDataClearedPackages;

        private MyPackageMonitor() {
            super(true);
            this.mDataClearedPackages = new java.util.ArrayList<>();
        }

        private boolean isChangingPackagesOfCurrentUserLocked() {
            int userId = getChangingUserId();
            boolean retval = userId == com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId;
            if (com.android.server.inputmethod.InputMethodManagerService.DEBUG && !retval) {
                android.util.Slog.d(com.android.server.inputmethod.InputMethodManagerService.TAG, "--- ignore this call back from a background user: " + userId);
            }
            return retval;
        }

        public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packages, int uid, boolean doit) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (!isChangingPackagesOfCurrentUserLocked()) {
                    return false;
                }
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId);
                java.lang.String curInputMethodId = settings.getSelectedInputMethod();
                java.util.List<android.view.inputmethod.InputMethodInfo> methodList = settings.getMethodList();
                int numImes = methodList.size();
                if (curInputMethodId != null) {
                    for (int i = 0; i < numImes; i++) {
                        android.view.inputmethod.InputMethodInfo imi = methodList.get(i);
                        if (imi.getId().equals(curInputMethodId)) {
                            for (java.lang.String pkg : packages) {
                                if (imi.getPackageName().equals(pkg)) {
                                    if (!doit) {
                                        return true;
                                    }
                                    com.android.server.inputmethod.InputMethodManagerService.this.resetSelectedInputMethodAndSubtypeLocked("");
                                    com.android.server.inputmethod.InputMethodManagerService.this.chooseNewDefaultIMELocked();
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }
        }

        public void onBeginPackageChanges() {
            clearPackageChangeState();
        }

        public void onPackageDataCleared(java.lang.String packageName, int uid) {
            this.mDataClearedPackages.add(packageName);
        }

        public void onFinishPackageChanges() throws java.lang.Throwable {
            onFinishPackageChangesInternal();
            clearPackageChangeState();
        }

        private void clearPackageChangeState() {
            this.mDataClearedPackages.clear();
        }

        private void onFinishPackageChangesInternal() throws java.lang.Throwable {
            android.content.pm.PackageManager userAwarePackageManager;
            java.util.List<android.view.inputmethod.InputMethodInfo> methodList;
            int numImes;
            int userId = getChangingUserId();
            com.android.server.inputmethod.InputMethodMap newMethodMapWithoutAdditionalSubtypes = com.android.server.inputmethod.InputMethodManagerService.queryInputMethodServicesInternal(com.android.server.inputmethod.InputMethodManagerService.this.mContext, userId, com.android.server.inputmethod.AdditionalSubtypeMap.EMPTY_MAP, 0).getMethodMap();
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                try {
                    try {
                        boolean isCurrentUser = userId == com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId;
                        com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap = com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId);
                        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
                        java.lang.String curInputMethodId = settings.getSelectedInputMethod();
                        java.util.List<android.view.inputmethod.InputMethodInfo> methodList2 = settings.getMethodList();
                        java.util.ArrayList<java.lang.String> imesToClearAdditionalSubtypes = new java.util.ArrayList<>();
                        int numImes2 = methodList2.size();
                        android.view.inputmethod.InputMethodInfo curIm = null;
                        int i = 0;
                        while (i < numImes2) {
                            try {
                                android.view.inputmethod.InputMethodInfo imi = methodList2.get(i);
                                java.lang.String imiId = imi.getId();
                                if (imiId.equals(curInputMethodId)) {
                                    curIm = imi;
                                }
                                if (this.mDataClearedPackages.contains(imi.getPackageName())) {
                                    imesToClearAdditionalSubtypes.add(imiId);
                                }
                                int change = isPackageDisappearing(imi.getPackageName());
                                if (change == 3) {
                                    methodList = methodList2;
                                    numImes = numImes2;
                                    android.util.Slog.i(com.android.server.inputmethod.InputMethodManagerService.TAG, "Input method uninstalled, disabling: " + imi.getComponent());
                                    if (isCurrentUser) {
                                        com.android.server.inputmethod.InputMethodManagerService.this.setInputMethodEnabledLocked(imi.getId(), false);
                                    } else {
                                        settings.buildAndPutEnabledInputMethodsStrRemovingId(new java.lang.StringBuilder(), settings.getEnabledInputMethodsAndSubtypeList(), imi.getId());
                                    }
                                } else {
                                    methodList = methodList2;
                                    numImes = numImes2;
                                    if (change == 1) {
                                        android.util.Slog.i(com.android.server.inputmethod.InputMethodManagerService.TAG, "Input method reinstalling, clearing additional subtypes: " + imi.getComponent());
                                        imesToClearAdditionalSubtypes.add(imiId);
                                    }
                                }
                                i++;
                                methodList2 = methodList;
                                numImes2 = numImes;
                            } catch (java.lang.Throwable th) {
                                th = th;
                            }
                        }
                        com.android.server.inputmethod.AdditionalSubtypeMap newAdditionalSubtypeMap = additionalSubtypeMap.cloneWithRemoveOrSelf(imesToClearAdditionalSubtypes);
                        boolean additionalSubtypeChanged = newAdditionalSubtypeMap != additionalSubtypeMap;
                        if (additionalSubtypeChanged) {
                            com.android.server.inputmethod.AdditionalSubtypeMapRepository.putAndSave(userId, newAdditionalSubtypeMap, settings.getMethodMap());
                        }
                        com.android.server.inputmethod.InputMethodMap newMethodMap = newMethodMapWithoutAdditionalSubtypes.applyAdditionalSubtypes(newAdditionalSubtypeMap);
                        if (com.android.server.inputmethod.InputMethodMap.areSame(settings.getMethodMap(), newMethodMap)) {
                            return;
                        }
                        com.android.server.inputmethod.InputMethodSettings newSettings = com.android.server.inputmethod.InputMethodSettings.create(newMethodMap, userId);
                        com.android.server.inputmethod.InputMethodSettingsRepository.put(userId, newSettings);
                        if (isCurrentUser) {
                            com.android.server.inputmethod.InputMethodManagerService.this.postInputMethodSettingUpdatedLocked(false);
                            boolean changed = false;
                            if (curIm != null) {
                                int change2 = isPackageDisappearing(curIm.getPackageName());
                                if (change2 == 2 || change2 == 3) {
                                    android.content.pm.PackageManager userAwarePackageManager2 = com.android.server.inputmethod.InputMethodManagerService.getPackageManagerForUser(com.android.server.inputmethod.InputMethodManagerService.this.mContext, userId);
                                    android.content.pm.ServiceInfo si = null;
                                    try {
                                        try {
                                            userAwarePackageManager = userAwarePackageManager2;
                                            try {
                                                si = userAwarePackageManager.getServiceInfo(curIm.getComponent(), android.content.pm.PackageManager.ComponentInfoFlags.of(0L));
                                            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                                            }
                                        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                                            userAwarePackageManager = userAwarePackageManager2;
                                        }
                                    } catch (android.content.pm.PackageManager.NameNotFoundException e3) {
                                        userAwarePackageManager = userAwarePackageManager2;
                                    }
                                    if (si == null) {
                                        android.util.Slog.i(com.android.server.inputmethod.InputMethodManagerService.TAG, "Current input method removed: " + curInputMethodId);
                                        com.android.server.inputmethod.InputMethodManagerService.this.updateSystemUiLocked(0, com.android.server.inputmethod.InputMethodManagerService.this.mBackDisposition);
                                        if (!com.android.server.inputmethod.InputMethodManagerService.this.chooseNewDefaultIMELocked()) {
                                            changed = true;
                                            curIm = null;
                                            android.util.Slog.i(com.android.server.inputmethod.InputMethodManagerService.TAG, "Unsetting current input method");
                                            com.android.server.inputmethod.InputMethodManagerService.this.resetSelectedInputMethodAndSubtypeLocked("");
                                        }
                                    }
                                }
                            }
                            if (curIm == null) {
                                changed = com.android.server.inputmethod.InputMethodManagerService.this.chooseNewDefaultIMELocked();
                            } else if (!changed && isPackageModified(curIm.getPackageName())) {
                                changed = true;
                            }
                            if (changed) {
                                com.android.server.inputmethod.InputMethodManagerService.this.updateFromSettingsLocked(false);
                            }
                            com.android.server.inputmethod.InputMethodManagerService.this.mImmsWrapper.getExtImpl().onFinishPackageChanges(userId);
                            return;
                        }
                        return;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
                throw th;
            }
        }
    }

    private static final class UserSwitchHandlerTask implements java.lang.Runnable {
        com.android.server.inputmethod.IInputMethodClientInvoker mClientToBeReset;
        final com.android.server.inputmethod.InputMethodManagerService mService;
        final int mToUserId;

        UserSwitchHandlerTask(com.android.server.inputmethod.InputMethodManagerService service, int toUserId, com.android.server.inputmethod.IInputMethodClientInvoker clientToBeReset) {
            this.mService = service;
            this.mToUserId = toUserId;
            this.mClientToBeReset = clientToBeReset;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (this.mService.mUserSwitchHandlerTask != this) {
                    return;
                }
                this.mService.switchUserOnHandlerLocked(this.mService.mUserSwitchHandlerTask.mToUserId, this.mClientToBeReset);
                this.mService.mUserSwitchHandlerTask = null;
            }
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private final com.android.server.inputmethod.InputMethodManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
            this.mService = ((com.android.server.inputmethod.IInputMethodManagerServiceExt.ILifecycleExt) system.ext.loader.core.ExtLoader.type(com.android.server.inputmethod.IInputMethodManagerServiceExt.ILifecycleExt.class).base(this).create()).initInputMethodManagerService(context);
        }

        public Lifecycle(android.content.Context context, com.android.server.inputmethod.InputMethodManagerService inputMethodManagerService) {
            super(context);
            this.mService = inputMethodManagerService;
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            com.android.server.inputmethod.IInputMethodManagerImpl.Callback service;
            this.mService.publishLocalService();
            if (android.view.inputmethod.Flags.useZeroJankProxy()) {
                android.os.Handler handler = this.mService.mHandler;
                java.util.Objects.requireNonNull(handler);
                service = new com.android.server.inputmethod.ZeroJankProxy(new com.android.server.devicepolicy.DevicePolicyManagerService$$ExternalSyntheticLambda216(handler), this.mService);
            } else {
                service = this.mService;
            }
            com.android.internal.view.IInputMethodManager.Stub stubCreate = com.android.server.inputmethod.IInputMethodManagerImpl.create(service);
            this.mService.mImmsWrapper.getExtImpl().setBinderService(stubCreate);
            publishBinderService("input_method", stubCreate, false, 21);
            if (android.view.inputmethod.Flags.refactorInsetsController()) {
                this.mService.registerImeRequestedChangedListener();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (this.mService.mExperimentalConcurrentMultiUserModeEnabled) {
                    return;
                }
                this.mService.scheduleSwitchUserTaskLocked(to.getUserIdentifier(), null);
            }
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                this.mService.systemRunning();
            }
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            com.android.server.inputmethod.SecureSettingsWrapper.onUserUnlocking(user.getUserIdentifier());
            this.mService.mHandler.obtainMessage(5000, user.getUserIdentifier(), 0).sendToTarget();
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) {
            int userId = user.getUserIdentifier();
            com.android.server.inputmethod.SecureSettingsWrapper.onUserStarting(userId);
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                this.mService.getUserData(userId);
                if (this.mService.mExperimentalConcurrentMultiUserModeEnabled && this.mService.mCurrentUserId != userId && this.mService.mSystemReady) {
                    this.mService.experimentalInitializeVisibleBackgroundUserLocked(userId);
                }
            }
        }
    }

    void onUnlockUser(int userId) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "onUnlockUser: userId=" + userId + " curUserId=" + this.mCurrentUserId);
            }
            if (this.mSystemReady) {
                com.android.server.inputmethod.InputMethodSettings newSettings = queryInputMethodServicesInternal(this.mContext, userId, com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId), 0);
                com.android.server.inputmethod.InputMethodSettingsRepository.put(userId, newSettings);
                if (this.mCurrentUserId == userId) {
                    postInputMethodSettingUpdatedLocked(false);
                    updateInputMethodsFromSettingsLocked(true);
                } else if (this.mExperimentalConcurrentMultiUserModeEnabled) {
                    experimentalInitializeVisibleBackgroundUserLocked(userId);
                }
            }
        }
    }

    void scheduleSwitchUserTaskLocked(int userId, com.android.server.inputmethod.IInputMethodClientInvoker clientToBeReset) {
        if (this.mUserSwitchHandlerTask != null) {
            if (this.mUserSwitchHandlerTask.mToUserId == userId) {
                this.mUserSwitchHandlerTask.mClientToBeReset = clientToBeReset;
                return;
            }
            this.mHandler.removeCallbacks(this.mUserSwitchHandlerTask);
        }
        hideCurrentInputLocked(this.mImeBindingState.mFocusedWindow, 0, 10);
        com.android.server.inputmethod.InputMethodManagerService.UserSwitchHandlerTask task = new com.android.server.inputmethod.InputMethodManagerService.UserSwitchHandlerTask(this, userId, clientToBeReset);
        this.mUserSwitchHandlerTask = task;
        this.mHandler.post(task);
    }

    public InputMethodManagerService(android.content.Context context, boolean experimentalConcurrentMultiUserModeEnabled) {
        this(context, experimentalConcurrentMultiUserModeEnabled, null, null, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    InputMethodManagerService(android.content.Context context, boolean z, com.android.server.ServiceThread serviceThread, com.android.server.ServiceThread serviceThread2, java.util.function.IntFunction<com.android.server.inputmethod.InputMethodBindingController> intFunction) {
        final com.android.server.ServiceThread serviceThread3;
        com.android.server.ServiceThread serviceThread4;
        java.lang.Object[] objArr = 0;
        this.mAudioManagerInternal = null;
        this.mVdmInternal = null;
        this.mVirtualDeviceMethodMap = new android.util.SparseArray<>();
        this.mDumper = new com.android.internal.inputmethod.ImeTracing.ServiceDumper() { // from class: com.android.server.inputmethod.InputMethodManagerService.1
            public void dumpToProto(android.util.proto.ProtoOutputStream proto, byte[] icProto) {
                com.android.server.inputmethod.InputMethodManagerService.this.dumpDebug(proto, 1146756268035L);
            }
        };
        this.mFocusedWindowPerceptible = new java.util.WeakHashMap<>();
        this.mEnabledAccessibilitySessions = new android.util.SparseArray<>();
        this.mIsInteractive = true;
        this.mBackDisposition = 0;
        this.mMyPackageMonitor = new com.android.server.inputmethod.InputMethodManagerService.MyPackageMonitor();
        this.mInputMethodListListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mImeTargetWindowMap = new java.util.WeakHashMap<>();
        this.mStartInputHistory = new com.android.server.inputmethod.StartInputHistory();
        this.mSoftInputShowHideHistory = new com.android.server.inputmethod.SoftInputShowHideHistory();
        this.mPriorityDumper = new com.android.server.utils.PriorityDump.PriorityDumper() { // from class: com.android.server.inputmethod.InputMethodManagerService.5
            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
                if (asProto) {
                    dumpAsProtoNoCheck(fd);
                } else {
                    com.android.server.inputmethod.InputMethodManagerService.this.dumpAsStringNoCheck(fd, pw, args, true);
                }
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpHigh(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
                dumpNormal(fd, pw, args, asProto);
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dumpNormal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
                if (asProto) {
                    dumpAsProtoNoCheck(fd);
                } else {
                    com.android.server.inputmethod.InputMethodManagerService.this.dumpAsStringNoCheck(fd, pw, args, false);
                }
            }

            @Override // com.android.server.utils.PriorityDump.PriorityDumper
            public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
                dumpNormal(fd, pw, args, asProto);
            }

            private void dumpAsProtoNoCheck(java.io.FileDescriptor fd) {
                android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
                long timeOffsetNs = java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(java.lang.System.currentTimeMillis()) - android.os.SystemClock.elapsedRealtimeNanos();
                proto.write(1125281431553L, 4990904633914117449L);
                proto.write(1125281431555L, timeOffsetNs);
                long token = proto.start(2246267895810L);
                proto.write(1125281431553L, android.os.SystemClock.elapsedRealtimeNanos());
                proto.write(1138166333442L, "InputMethodManagerService.mPriorityDumper#dumpAsProtoNoCheck");
                com.android.server.inputmethod.InputMethodManagerService.this.dumpDebug(proto, 1146756268035L);
                proto.end(token);
                proto.flush();
            }
        };
        this.mImmsWrapper = new com.android.server.inputmethod.InputMethodManagerService.InputMethodManagerServiceWrapper();
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mExperimentalConcurrentMultiUserModeEnabled = z;
            this.mContext = context;
            this.mRes = context.getResources();
            com.android.server.inputmethod.SecureSettingsWrapper.onStart(this.mContext);
            if (serviceThread != null) {
                serviceThread3 = serviceThread;
            } else {
                serviceThread3 = new com.android.server.ServiceThread(HANDLER_THREAD_NAME, -2, true);
            }
            serviceThread3.start();
            this.mHandler = android.os.Handler.createAsync(serviceThread3.getLooper(), this);
            if (serviceThread2 != null) {
                serviceThread4 = serviceThread2;
            } else {
                serviceThread4 = new com.android.server.ServiceThread(PACKAGE_MONITOR_THREAD_NAME, -2, true);
            }
            com.android.server.ServiceThread serviceThread5 = serviceThread4;
            serviceThread5.start();
            this.mIoHandler = android.os.Handler.createAsync(serviceThread5.getLooper());
            if (HANDLER_THREAD_NAME.equals(serviceThread3.getName())) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$new$0(serviceThread3);
                    }
                });
            }
            com.android.server.inputmethod.SystemLocaleWrapper.onStart(context, new com.android.server.inputmethod.SystemLocaleWrapper.Callback() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda3
                @Override // com.android.server.inputmethod.SystemLocaleWrapper.Callback
                public final void onLocaleChanged(android.os.LocaleList localeList, android.os.LocaleList localeList2) {
                    this.f$0.onActionLocaleChanged(localeList, localeList2);
                }
            }, this.mHandler);
            this.mImeTrackerService = new com.android.server.inputmethod.ImeTrackerService(serviceThread != null ? serviceThread.getLooper() : android.os.Looper.getMainLooper());
            this.mSettingsObserver = new com.android.server.inputmethod.InputMethodManagerService.SettingsObserver(this.mHandler);
            this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
            this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
            this.mInputManagerInternal = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
            this.mImePlatformCompatUtils = new com.android.server.inputmethod.ImePlatformCompatUtils();
            this.mInputMethodDeviceConfigs = new com.android.server.inputmethod.InputMethodDeviceConfigs();
            this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            this.mSlotIme = this.mContext.getString(android.R.string.shortcut_restore_unknown_issue);
            this.mShowOngoingImeSwitcherForPhones = false;
            com.android.server.inputmethod.InputMethodSettingsRepository.initialize(this.mHandler, this.mContext);
            com.android.server.inputmethod.AdditionalSubtypeMapRepository.initialize(this.mHandler, this.mContext);
            this.mCurrentUserId = this.mActivityManagerInternal.getCurrentUserId();
            this.mUserDataRepository = new com.android.server.inputmethod.UserDataRepository(this.mHandler, this.mUserManagerInternal, intFunction != null ? intFunction : new java.util.function.IntFunction() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda4
                @Override // java.util.function.IntFunction
                public final java.lang.Object apply(int i) {
                    return this.f$0.lambda$new$1(i);
                }
            });
            for (int i : this.mUserManagerInternal.getUserIds()) {
                getUserData(i);
            }
            com.android.server.inputmethod.InputMethodSettings inputMethodSettings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
            this.mSwitchingController = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.createInstanceLocked(context, inputMethodSettings.getMethodMap(), inputMethodSettings.getUserId());
            this.mHardwareKeyboardShortcutController = new com.android.server.inputmethod.HardwareKeyboardShortcutController(inputMethodSettings.getMethodMap(), inputMethodSettings.getUserId());
            this.mMenuController = new com.android.server.inputmethod.InputMethodMenuController(this);
            this.mVisibilityStateComputer = new com.android.server.inputmethod.ImeVisibilityStateComputer(this);
            this.mVisibilityApplier = new com.android.server.inputmethod.DefaultImeVisibilityApplier(this);
            this.mClientController = new com.android.server.inputmethod.ClientController(this.mPackageManagerInternal);
            this.mClientController.addClientControllerCallback(new com.android.server.inputmethod.ClientController.ClientControllerCallback() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda5
                @Override // com.android.server.inputmethod.ClientController.ClientControllerCallback
                public final void onClientRemoved(com.android.server.inputmethod.ClientState clientState) throws java.lang.Throwable {
                    this.f$0.lambda$new$2(clientState);
                }
            });
            this.mImeBindingState = com.android.server.inputmethod.ImeBindingState.newEmptyState();
            this.mPreventImeStartupUnlessTextEditor = this.mRes.getBoolean(android.R.bool.config_preventImeStartupUnlessTextEditor);
            this.mNonPreemptibleInputMethods = this.mRes.getStringArray(android.R.array.config_network_type_tcp_buffers);
            this.mHwController = new com.android.server.inputmethod.HandwritingModeController(this.mContext, serviceThread3.getLooper(), new com.android.server.inputmethod.InputMethodManagerService.InkWindowInitializer(), new java.lang.Runnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$new$3();
                }
            });
            registerDeviceListenerAndCheckStylusSupport();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(com.android.server.ServiceThread thread) {
        getWrapper().getExtImpl().initServiceUx(thread.getName(), thread.getThreadId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.inputmethod.InputMethodBindingController lambda$new$1(int userId) {
        return new com.android.server.inputmethod.InputMethodBindingController(userId, this);
    }

    int getCurrentImeUserIdLocked() {
        return this.mCurrentUserId;
    }

    private final class InkWindowInitializer implements java.lang.Runnable {
        private InkWindowInitializer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.IInputMethodInvoker curMethod = com.android.server.inputmethod.InputMethodManagerService.this.getCurMethodLocked();
                if (curMethod != null) {
                    curMethod.initInkWindow();
                }
            }
        }
    }

    private void onUpdateEditorToolType(int toolType) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
            if (curMethod != null) {
                curMethod.updateEditorToolType(toolType);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: discardHandwritingDelegationText, reason: merged with bridge method [inline-methods] */
    public void lambda$new$3() {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
            if (curMethod != null) {
                curMethod.discardHandwritingDelegationText();
            }
        }
    }

    private void resetDefaultImeLocked(android.content.Context context) {
        java.lang.String selectedMethodId = getSelectedMethodIdLocked();
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
        if (selectedMethodId != null && settings.getMethodMap().containsKey(selectedMethodId) && !settings.getMethodMap().get(selectedMethodId).isSystem()) {
            return;
        }
        java.util.List<android.view.inputmethod.InputMethodInfo> suitableImes = com.android.server.inputmethod.InputMethodInfoUtils.getDefaultEnabledImes(context, settings.getEnabledInputMethodList());
        if (suitableImes.isEmpty()) {
            android.util.Slog.i(TAG, "No default found");
            return;
        }
        android.view.inputmethod.InputMethodInfo defIm = suitableImes.get(0);
        if (DEBUG) {
            android.util.Slog.i(TAG, "Default found, using " + defIm.getId());
        }
        setSelectedInputMethodAndSubtypeLocked(defIm, -1, false);
    }

    private void maybeInitImeNavbarConfigLocked(int targetUserId) {
        android.content.Context userContext;
        int profileParentUserId = this.mUserManagerInternal.getProfileParentId(targetUserId);
        if (this.mImeDrawsImeNavBarRes != null && this.mImeDrawsImeNavBarRes.getUserId() != profileParentUserId) {
            this.mImeDrawsImeNavBarRes.close();
            this.mImeDrawsImeNavBarRes = null;
        }
        if (this.mImeDrawsImeNavBarRes == null) {
            if (this.mContext.getUserId() == profileParentUserId) {
                userContext = this.mContext;
            } else {
                android.content.Context userContext2 = this.mContext;
                userContext = userContext2.createContextAsUser(android.os.UserHandle.of(profileParentUserId), 0);
            }
            this.mImeDrawsImeNavBarRes = com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper.create(userContext, android.R.bool.config_fusedLocationOverlayUnstableFallback, this.mHandler, new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$maybeInitImeNavbarConfigLocked$4((com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeInitImeNavbarConfigLocked$4(com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper resource) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (resource == this.mImeDrawsImeNavBarRes) {
                sendOnNavButtonFlagsChangedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.content.pm.PackageManager getPackageManagerForUser(android.content.Context context, int userId) {
        if (context.getUserId() == userId) {
            return context.getPackageManager();
        }
        return context.createContextAsUser(android.os.UserHandle.of(userId), 0).getPackageManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchUserOnHandlerLocked(int newUserId, com.android.server.inputmethod.IInputMethodClientInvoker clientToBeReset) throws java.lang.Throwable {
        com.android.server.inputmethod.ClientState cs;
        if (DEBUG) {
            android.util.Slog.d(TAG, "Switching user stage 1/3. newUserId=" + newUserId + " currentUserId=" + this.mCurrentUserId);
        }
        onUnbindCurrentMethodByReset();
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        bindingController.unbindCurrentMethod();
        unbindCurrentClientLocked(6);
        maybeInitImeNavbarConfigLocked(newUserId);
        this.mSettingsObserver.registerContentObserverLocked(newUserId);
        this.mCurrentUserId = newUserId;
        java.lang.String defaultImiId = com.android.server.inputmethod.SecureSettingsWrapper.getString("default_input_method", null, newUserId);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Switching user stage 2/3. newUserId=" + newUserId + " defaultImiId=" + defaultImiId);
        }
        boolean initialUserSwitch = android.text.TextUtils.isEmpty(defaultImiId);
        com.android.server.inputmethod.InputMethodSettings newSettings = com.android.server.inputmethod.InputMethodSettingsRepository.get(newUserId);
        postInputMethodSettingUpdatedLocked(initialUserSwitch);
        if (android.text.TextUtils.isEmpty(newSettings.getSelectedInputMethod())) {
            resetDefaultImeLocked(this.mContext);
        }
        updateFromSettingsLocked(true);
        if (initialUserSwitch) {
            com.android.server.inputmethod.InputMethodUtils.setNonSelectedSystemImesDisabledUntilUsed(getPackageManagerForUser(this.mContext, newUserId), newSettings.getEnabledInputMethodList());
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Switching user stage 3/3. newUserId=" + newUserId + " selectedIme=" + newSettings.getSelectedInputMethod());
        }
        if (!this.mIsInteractive || clientToBeReset == null || (cs = this.mClientController.getClient(clientToBeReset.asBinder())) == null) {
            return;
        }
        cs.mClient.scheduleStartInputIfNecessary(this.mInFullscreenMode);
    }

    public void systemRunning() {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "--- systemReady");
            }
            if (!this.mSystemReady) {
                this.mSystemReady = true;
                final int currentUserId = this.mCurrentUserId;
                this.mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
                hideStatusBarIconLocked();
                updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
                this.mShowOngoingImeSwitcherForPhones = this.mRes.getBoolean(android.R.bool.config_wimaxEnabled);
                if (this.mShowOngoingImeSwitcherForPhones) {
                    this.mWindowManagerInternal.setOnHardKeyboardStatusChangeListener(new com.android.server.wm.WindowManagerInternal.OnHardKeyboardStatusChangeListener() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda7
                        @Override // com.android.server.wm.WindowManagerInternal.OnHardKeyboardStatusChangeListener
                        public final void onHardKeyboardStatusChange(boolean z) {
                            this.f$0.lambda$systemRunning$5(z);
                        }
                    });
                }
                this.mImeDrawsImeNavBarResLazyInitFuture = com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$systemRunning$6(currentUserId);
                    }
                }, "Lazily initialize IMMS#mImeDrawsImeNavBarRes");
                this.mMyPackageMonitor.register(this.mContext, android.os.UserHandle.ALL, this.mIoHandler);
                this.mSettingsObserver.registerContentObserverLocked(currentUserId);
                android.content.IntentFilter broadcastFilterForAllUsers = new android.content.IntentFilter();
                broadcastFilterForAllUsers.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
                this.mContext.registerReceiverAsUser(new com.android.server.inputmethod.InputMethodManagerService.ImmsBroadcastReceiverForAllUsers(), android.os.UserHandle.ALL, broadcastFilterForAllUsers, null, null, 2);
                java.lang.String defaultImiId = com.android.server.inputmethod.SecureSettingsWrapper.getString("default_input_method", null, currentUserId);
                boolean imeSelectedOnBoot = !android.text.TextUtils.isEmpty(defaultImiId);
                com.android.server.inputmethod.InputMethodSettings newSettings = queryInputMethodServicesInternal(this.mContext, currentUserId, com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(currentUserId), 0);
                com.android.server.inputmethod.InputMethodSettingsRepository.put(currentUserId, newSettings);
                postInputMethodSettingUpdatedLocked(!imeSelectedOnBoot);
                updateFromSettingsLocked(true);
                com.android.server.inputmethod.InputMethodUtils.setNonSelectedSystemImesDisabledUntilUsed(getPackageManagerForUser(this.mContext, currentUserId), newSettings.getEnabledInputMethodList());
                com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        com.android.server.inputmethod.AdditionalSubtypeMapRepository.startWriterThread();
                    }
                }, "Start AdditionalSubtypeMapRepository's writer thread");
                if (this.mExperimentalConcurrentMultiUserModeEnabled) {
                    for (int userId : this.mUserManagerInternal.getUserIds()) {
                        if (userId != this.mCurrentUserId) {
                            experimentalInitializeVisibleBackgroundUserLocked(userId);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemRunning$5(boolean z) {
        this.mHandler.obtainMessage(MSG_HARD_KEYBOARD_SWITCH_CHANGED, z ? 1 : 0, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemRunning$6(int currentUserId) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mImeDrawsImeNavBarResLazyInitFuture = null;
            if (currentUserId != this.mCurrentUserId) {
                return;
            }
            maybeInitImeNavbarConfigLocked(currentUserId);
        }
    }

    void registerImeRequestedChangedListener() {
        this.mWindowManagerInternal.setOnImeRequestedChangedListener(new com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda14
            @Override // com.android.server.wm.WindowManagerInternal.OnImeRequestedChangedListener
            public final void onImeRequestedChanged(android.os.IBinder iBinder, boolean z) {
                this.f$0.lambda$registerImeRequestedChangedListener$7(iBinder, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerImeRequestedChangedListener$7(android.os.IBinder windowToken, boolean imeVisible) {
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            if (imeVisible) {
                showSoftInputInternal(windowToken);
            } else {
                hideSoftInputInternal(windowToken);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean calledWithValidTokenLocked(android.os.IBinder token) {
        if (token == null) {
            throw new java.security.InvalidParameterException("token must not be null.");
        }
        if (token != getCurTokenLocked()) {
            android.util.Slog.e(TAG, "Ignoring " + android.os.Debug.getCaller() + " due to an invalid token. uid:" + android.os.Binder.getCallingUid() + " token:" + token);
            return false;
        }
        return true;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public android.view.inputmethod.InputMethodInfo getCurrentInputMethodInfoAsUser(int userId) {
        android.view.inputmethod.InputMethodInfo inputMethodInfoQueryDefaultInputMethodForUserIdLocked;
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            inputMethodInfoQueryDefaultInputMethodForUserIdLocked = queryDefaultInputMethodForUserIdLocked(userId);
        }
        return inputMethodInfoQueryDefaultInputMethodForUserIdLocked;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.InputMethodInfoSafeList getInputMethodList(int userId, int directBootAwareness) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            int[] resolvedUserIds = com.android.server.inputmethod.InputMethodUtils.resolveUserId(userId, this.mCurrentUserId, null);
            if (resolvedUserIds.length != 1) {
                return com.android.internal.inputmethod.InputMethodInfoSafeList.empty();
            }
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.internal.inputmethod.InputMethodInfoSafeList.create(getInputMethodListLocked(resolvedUserIds[0], directBootAwareness, callingUid));
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.InputMethodInfoSafeList getEnabledInputMethodList(int userId) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            int[] resolvedUserIds = com.android.server.inputmethod.InputMethodUtils.resolveUserId(userId, this.mCurrentUserId, null);
            if (resolvedUserIds.length != 1) {
                return com.android.internal.inputmethod.InputMethodInfoSafeList.empty();
            }
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.internal.inputmethod.InputMethodInfoSafeList.create(getEnabledInputMethodListLocked(resolvedUserIds[0], callingUid));
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListLegacy(int userId, int directBootAwareness) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            int[] resolvedUserIds = com.android.server.inputmethod.InputMethodUtils.resolveUserId(userId, this.mCurrentUserId, null);
            if (resolvedUserIds.length != 1) {
                return java.util.Collections.emptyList();
            }
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return getInputMethodListLocked(resolvedUserIds[0], directBootAwareness, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListLegacy(int userId) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            int[] resolvedUserIds = com.android.server.inputmethod.InputMethodUtils.resolveUserId(userId, this.mCurrentUserId, null);
            if (resolvedUserIds.length != 1) {
                return java.util.Collections.emptyList();
            }
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                return getEnabledInputMethodListLocked(resolvedUserIds[0], callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean isStylusHandwritingAvailableAsUser(int userId, boolean connectionless) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            boolean z = false;
            if (!isStylusHandwritingEnabled(this.mContext, userId)) {
                return false;
            }
            if (userId == this.mCurrentUserId) {
                com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
                if (bindingController.supportsStylusHandwriting() && (!connectionless || bindingController.supportsConnectionlessStylusHandwriting())) {
                    z = true;
                }
                return z;
            }
            com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
            android.view.inputmethod.InputMethodInfo imi = settings.getMethodMap().get(settings.getSelectedInputMethod());
            if (imi != null && imi.supportsStylusHandwriting() && (!connectionless || imi.supportsConnectionlessStylusHandwriting())) {
                z = true;
            }
            return z;
        }
    }

    private boolean isStylusHandwritingEnabled(android.content.Context context, int userId) {
        int profileParentUserId = this.mUserManagerInternal.getProfileParentId(userId);
        return android.provider.Settings.Secure.getIntForUser(context.getContentResolver(), "stylus_handwriting_enabled", 1, profileParentUserId) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListLocked(final int userId, int directBootAwareness, final int callingUid) {
        final com.android.server.inputmethod.InputMethodSettings settings;
        if (directBootAwareness == 0) {
            settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        } else {
            com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap = com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId);
            settings = queryInputMethodServicesInternal(this.mContext, userId, additionalSubtypeMap, directBootAwareness);
        }
        java.util.ArrayList<android.view.inputmethod.InputMethodInfo> methodList = new java.util.ArrayList<>(settings.getMethodList());
        methodList.removeIf(new java.util.function.Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getInputMethodListLocked$8(callingUid, userId, settings, (android.view.inputmethod.InputMethodInfo) obj);
            }
        });
        return methodList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getInputMethodListLocked$8(int callingUid, int userId, com.android.server.inputmethod.InputMethodSettings settings, android.view.inputmethod.InputMethodInfo imi) {
        return !canCallerAccessInputMethod(imi.getPackageName(), callingUid, userId, settings);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListLocked(final int userId, final int callingUid) {
        final com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        java.util.ArrayList<android.view.inputmethod.InputMethodInfo> methodList = settings.getEnabledInputMethodList();
        methodList.removeIf(new java.util.function.Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda21
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$getEnabledInputMethodListLocked$9(callingUid, userId, settings, (android.view.inputmethod.InputMethodInfo) obj);
            }
        });
        return methodList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getEnabledInputMethodListLocked$9(int callingUid, int userId, com.android.server.inputmethod.InputMethodSettings settings, android.view.inputmethod.InputMethodInfo imi) {
        return !canCallerAccessInputMethod(imi.getPackageName(), callingUid, userId, settings);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeList(java.lang.String imiId, boolean allowsImplicitlyEnabledSubtypes, int userId) {
        java.util.List<android.view.inputmethod.InputMethodSubtype> enabledInputMethodSubtypeListLocked;
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            int callingUid = android.os.Binder.getCallingUid();
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                enabledInputMethodSubtypeListLocked = getEnabledInputMethodSubtypeListLocked(imiId, allowsImplicitlyEnabledSubtypes, userId, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
        return enabledInputMethodSubtypeListLocked;
    }

    private java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(java.lang.String imiId, boolean allowsImplicitlyEnabledSubtypes, int userId, int callingUid) {
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        android.view.inputmethod.InputMethodInfo imi = settings.getMethodMap().get(imiId);
        if (imi == null) {
            return java.util.Collections.emptyList();
        }
        if (!canCallerAccessInputMethod(imi.getPackageName(), callingUid, userId, settings)) {
            return java.util.Collections.emptyList();
        }
        return settings.getEnabledInputMethodSubtypeList(imi, allowsImplicitlyEnabledSubtypes);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void addClient(com.android.internal.inputmethod.IInputMethodClient client, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, int selfReportedDisplayId) {
        int callerUid = android.os.Binder.getCallingUid();
        int callerPid = android.os.Binder.getCallingPid();
        com.android.server.inputmethod.IInputMethodClientInvoker clientInvoker = com.android.server.inputmethod.IInputMethodClientInvoker.create(client, this.mHandler);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mClientController.addClient(clientInvoker, inputConnection, selfReportedDisplayId, callerUid, callerPid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onClientRemoved, reason: merged with bridge method [inline-methods] */
    public void lambda$new$2(com.android.server.inputmethod.ClientState client) throws java.lang.Throwable {
        clearClientSessionLocked(client);
        clearClientSessionForAccessibilityLocked(client);
        if (this.mCurClient == client) {
            hideCurrentInputLocked(this.mImeBindingState.mFocusedWindow, 0, 22);
            if (this.mBoundToMethod) {
                this.mBoundToMethod = false;
                com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
                if (curMethod != null) {
                    curMethod.unbindInput();
                    com.android.server.AccessibilityManagerInternal.get().unbindInput();
                }
            }
            this.mBoundToAccessibility = false;
            this.mCurClient = null;
            if (this.mImeBindingState.mFocusedWindowClient == client) {
                this.mImeBindingState = com.android.server.inputmethod.ImeBindingState.newEmptyState();
            }
        }
    }

    @Override // com.android.server.inputmethod.ZeroJankProxy.Callback
    public com.android.server.inputmethod.ClientState getClientStateLocked(com.android.internal.inputmethod.IInputMethodClient client) {
        return this.mClientController.getClient(client.asBinder());
    }

    void unbindCurrentClientLocked(int unbindClientReason) {
        if (this.mCurClient != null) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "unbindCurrentInputLocked: client=" + this.mCurClient.mClient.asBinder());
            }
            if (this.mBoundToMethod) {
                this.mBoundToMethod = false;
                com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
                if (curMethod != null) {
                    curMethod.unbindInput();
                }
            }
            this.mBoundToAccessibility = false;
            this.mCurClient.mClient.setActive(false, false);
            com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
            this.mCurClient.mClient.onUnbindMethod(bindingController.getSequenceNumber(), unbindClientReason);
            this.mCurClient.mSessionRequested = false;
            this.mCurClient.mSessionRequestedForAccessibility = false;
            this.mCurClient = null;
            android.view.inputmethod.ImeTracker.forLogging().onFailed(this.mCurStatsToken, 8);
            this.mCurStatsToken = null;
            this.mMenuController.hideInputMethodMenuLocked();
        }
    }

    void onUnbindCurrentMethodByReset() {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState winState = this.mVisibilityStateComputer.getWindowStateOrNull(this.mImeBindingState.mFocusedWindow);
        if (winState != null && !winState.isRequestedImeVisible() && !this.mVisibilityStateComputer.isInputShown()) {
            android.view.inputmethod.ImeTracker.Token statsToken = createStatsTokenForFocusedClient(false, 50);
            this.mVisibilityApplier.applyImeVisibility(this.mImeBindingState.mFocusedWindow, statsToken, 0, this.mCurrentUserId);
        }
    }

    boolean hasAttachedClient() {
        return this.mCurClient != null;
    }

    void setAttachedClientForTesting(com.android.server.inputmethod.ClientState cs) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mCurClient = cs;
        }
    }

    void clearInputShownLocked() {
        this.mVisibilityStateComputer.setInputShown(false);
    }

    @Override // com.android.server.inputmethod.ZeroJankProxy.Callback
    public boolean isInputShownLocked() {
        return this.mVisibilityStateComputer.isInputShown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isShowRequestedForCurrentWindow() {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = this.mVisibilityStateComputer.getWindowStateOrNull(this.mImeBindingState.mFocusedWindow);
        return state != null && state.isRequestedImeVisible();
    }

    com.android.internal.inputmethod.InputBindResult attachNewInputLocked(int startInputReason, boolean initial) {
        android.os.Binder startInputToken;
        com.android.server.inputmethod.InputMethodManagerService.SessionState session;
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        if (!this.mBoundToMethod) {
            bindingController.getCurMethod().bindInput(this.mCurClient.mBinding);
            this.mBoundToMethod = true;
        }
        boolean restarting = !initial;
        android.os.Binder startInputToken2 = new android.os.Binder();
        com.android.server.inputmethod.StartInputInfo info = new com.android.server.inputmethod.StartInputInfo(this.mCurrentUserId, bindingController.getCurToken(), bindingController.getCurTokenDisplayId(), bindingController.getCurId(), startInputReason, restarting, android.os.UserHandle.getUserId(this.mCurClient.mUid), this.mCurClient.mSelfReportedDisplayId, this.mImeBindingState.mFocusedWindow, this.mCurEditorInfo, this.mImeBindingState.mFocusedWindowSoftInputMode, bindingController.getSequenceNumber());
        this.mImeTargetWindowMap.put(startInputToken2, this.mImeBindingState.mFocusedWindow);
        this.mStartInputHistory.addEntry(info);
        if (userId != android.os.UserHandle.getUserId(this.mCurClient.mUid)) {
            startInputToken = startInputToken2;
        } else {
            startInputToken = startInputToken2;
            this.mPackageManagerInternal.grantImplicitAccess(userId, null, android.os.UserHandle.getAppId(bindingController.getCurMethodUid()), this.mCurClient.mUid, true);
        }
        int navButtonFlags = getInputMethodNavButtonFlagsLocked();
        com.android.server.inputmethod.InputMethodManagerService.SessionState session2 = this.mCurClient.mCurSession;
        setEnabledSessionLocked(session2);
        this.mImmsWrapper.getExtImpl().setAsyncBinderUxFlag(true);
        session2.mMethod.startInput(startInputToken, this.mCurInputConnection, this.mCurEditorInfo, restarting, navButtonFlags, this.mCurImeDispatcher);
        this.mImmsWrapper.getExtImpl().setAsyncBinderUxFlag(false);
        com.android.internal.inputmethod.IInputMethod.StartInputParams params = new com.android.internal.inputmethod.IInputMethod.StartInputParams();
        params.startInputToken = startInputToken;
        params.remoteInputConnection = this.mCurInputConnection;
        params.editorInfo = this.mCurEditorInfo;
        params.restarting = restarting;
        params.navigationBarFlags = navButtonFlags;
        params.imeDispatcher = null;
        this.mImmsWrapper.getExtImpl().startInputToSynergy(params);
        if (android.view.inputmethod.Flags.refactorInsetsController()) {
            if (!isShowRequestedForCurrentWindow() || this.mImeBindingState == null || this.mImeBindingState.mFocusedWindow == null) {
                session = session2;
            } else {
                showSoftInputInternal(this.mImeBindingState.mFocusedWindow);
                session = session2;
            }
        } else if (isShowRequestedForCurrentWindow()) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Attach new input asks to show input");
            }
            android.view.inputmethod.ImeTracker.Token statsToken = this.mCurStatsToken != null ? this.mCurStatsToken : createStatsTokenForFocusedClient(true, 2);
            this.mCurStatsToken = null;
            session = session2;
            showCurrentInputLocked(this.mImeBindingState.mFocusedWindow, statsToken, this.mVisibilityStateComputer.getShowFlags(), 0, null, 2);
        } else {
            session = session2;
        }
        java.lang.String curId = bindingController.getCurId();
        android.view.inputmethod.InputMethodInfo curInputMethodInfo = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId).getMethodMap().get(curId);
        boolean suppressesSpellChecker = curInputMethodInfo != null && curInputMethodInfo.suppressesSpellChecker();
        android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> accessibilityInputMethodSessions = createAccessibilityInputMethodSessions(this.mCurClient.mAccessibilitySessions);
        if (bindingController.supportsStylusHandwriting() && hasSupportedStylusLocked()) {
            this.mHwController.setInkWindowInitializer(new com.android.server.inputmethod.InputMethodManagerService.InkWindowInitializer());
        }
        return new com.android.internal.inputmethod.InputBindResult(0, session.mSession, accessibilityInputMethodSessions, session.mChannel != null ? session.mChannel.dup() : null, curId, bindingController.getSequenceNumber(), suppressesSpellChecker);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachNewAccessibilityLocked(int startInputReason, boolean initial) {
        if (!this.mBoundToAccessibility) {
            com.android.server.AccessibilityManagerInternal.get().bindInput();
            this.mBoundToAccessibility = true;
        }
        if (startInputReason != 11) {
            setEnabledSessionForAccessibilityLocked(this.mCurClient.mAccessibilitySessions);
            com.android.server.AccessibilityManagerInternal.get().startInput(this.mCurRemoteAccessibilityInputConnection, this.mCurEditorInfo, !initial);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> createAccessibilityInputMethodSessions(android.util.SparseArray<com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState> accessibilitySessions) {
        android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> accessibilityInputMethodSessions = new android.util.SparseArray<>();
        if (accessibilitySessions != null) {
            for (int i = 0; i < accessibilitySessions.size(); i++) {
                accessibilityInputMethodSessions.append(accessibilitySessions.keyAt(i), accessibilitySessions.valueAt(i).mSession);
            }
        }
        return accessibilityInputMethodSessions;
    }

    private com.android.internal.inputmethod.InputBindResult startInputUncheckedLocked(com.android.server.inputmethod.ClientState cs, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, android.view.inputmethod.EditorInfo editorInfo, int startInputFlags, int startInputReason, int unverifiedTargetSdkVersion, android.window.ImeOnBackInvokedDispatcher imeDispatcher, com.android.server.inputmethod.InputMethodBindingController bindingController) throws java.lang.Throwable {
        android.os.LocaleList hintsFromVirtualDevice;
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState winState = this.mVisibilityStateComputer.getWindowStateOrNull(this.mImeBindingState.mFocusedWindow);
        if (winState == null) {
            return com.android.internal.inputmethod.InputBindResult.NOT_IME_TARGET_WINDOW;
        }
        int csDisplayId = cs.mSelfReportedDisplayId;
        bindingController.setDisplayIdToShowIme(this.mVisibilityStateComputer.computeImeDisplayId(winState, csDisplayId));
        java.lang.String selectedMethodId = bindingController.getSelectedMethodId();
        java.lang.String deviceMethodId = computeCurrentDeviceMethodIdLocked(bindingController.mUserId, selectedMethodId);
        if (deviceMethodId == null) {
            this.mVisibilityStateComputer.getImePolicy().setImeHiddenByDisplayPolicy(true);
        } else if (!java.util.Objects.equals(deviceMethodId, selectedMethodId)) {
            setInputMethodLocked(deviceMethodId, -1, bindingController.getDeviceIdToShowIme());
            selectedMethodId = deviceMethodId;
        }
        if (this.mVisibilityStateComputer.getImePolicy().isImeHiddenByDisplayPolicy()) {
            hideCurrentInputLocked(this.mImeBindingState.mFocusedWindow, 0, 27);
            return com.android.internal.inputmethod.InputBindResult.NO_IME;
        }
        if (selectedMethodId == null) {
            return com.android.internal.inputmethod.InputBindResult.NO_IME;
        }
        if (this.mCurClient != cs) {
            prepareClientSwitchLocked(cs);
            this.mImmsWrapper.getExtImpl().onClientStateSwitch(cs);
        }
        boolean connectionWasActive = this.mCurInputConnection != null;
        bindingController.advanceSequenceNumber();
        this.mCurClient = cs;
        this.mCurInputConnection = inputConnection;
        this.mCurRemoteAccessibilityInputConnection = remoteAccessibilityInputConnection;
        this.mCurImeDispatcher = imeDispatcher;
        if (this.mVdmInternal == null) {
            this.mVdmInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        }
        if (this.mVdmInternal != null && editorInfo.hintLocales == null && (hintsFromVirtualDevice = this.mVdmInternal.getPreferredLocaleListForUid(cs.mUid)) != null) {
            editorInfo.hintLocales = hintsFromVirtualDevice;
        }
        this.mCurEditorInfo = editorInfo;
        boolean connectionIsActive = this.mCurInputConnection != null;
        if (connectionIsActive != connectionWasActive) {
            this.mInputManagerInternal.notifyInputMethodConnectionActive(connectionIsActive);
        }
        if (shouldPreventImeStartupLocked(selectedMethodId, startInputFlags, unverifiedTargetSdkVersion)) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Avoiding IME startup and unbinding current input method.");
            }
            bindingController.invalidateAutofillSession();
            bindingController.unbindCurrentMethod();
            return com.android.internal.inputmethod.InputBindResult.NO_EDITOR;
        }
        java.lang.String curId = bindingController.getCurId();
        int displayIdToShowIme = bindingController.getDisplayIdToShowIme();
        if (curId != null && curId.equals(bindingController.getSelectedMethodId()) && displayIdToShowIme == getCurTokenDisplayIdLocked()) {
            if (cs.mCurSession != null) {
                cs.mSessionRequestedForAccessibility = false;
                requestClientSessionForAccessibilityLocked(cs);
                attachNewAccessibilityLocked(startInputReason, (startInputFlags & 4) != 0);
                return attachNewInputLocked(startInputReason, (startInputFlags & 4) != 0);
            }
            com.android.internal.inputmethod.InputBindResult bindResult = tryReuseConnectionLocked(bindingController, cs);
            if (bindResult != null) {
                return bindResult;
            }
        }
        bindingController.unbindCurrentMethod();
        return bindingController.bindCurrentMethod();
    }

    private java.lang.String computeCurrentDeviceMethodIdLocked(int userId, java.lang.String currentMethodId) {
        if (this.mVdmInternal == null) {
            this.mVdmInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        }
        if (this.mVdmInternal == null || !android.companion.virtual.flags.Flags.vdmCustomIme()) {
            return currentMethodId;
        }
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        int oldDeviceId = bindingController.getDeviceIdToShowIme();
        int displayIdToShowIme = bindingController.getDisplayIdToShowIme();
        int newDeviceId = this.mVdmInternal.getDeviceIdForDisplayId(displayIdToShowIme);
        bindingController.setDeviceIdToShowIme(newDeviceId);
        if (newDeviceId == 0) {
            if (oldDeviceId == 0) {
                return currentMethodId;
            }
            java.lang.String defaultDeviceMethodId = settings.getSelectedDefaultDeviceInputMethod();
            if (DEBUG) {
                android.util.Slog.v(TAG, "Restoring default device input method: " + defaultDeviceMethodId);
            }
            settings.putSelectedDefaultDeviceInputMethod(null);
            return defaultDeviceMethodId;
        }
        java.lang.String deviceMethodId = this.mVirtualDeviceMethodMap.get(newDeviceId, currentMethodId);
        if (java.util.Objects.equals(deviceMethodId, currentMethodId)) {
            return currentMethodId;
        }
        if (!settings.getMethodMap().containsKey(deviceMethodId)) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Disabling IME on virtual device with id " + newDeviceId + " because its custom input method is not available: " + deviceMethodId);
            }
            return null;
        }
        if (oldDeviceId == 0) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Storing default device input method " + currentMethodId);
            }
            settings.putSelectedDefaultDeviceInputMethod(currentMethodId);
        }
        if (DEBUG) {
            android.util.Slog.v(TAG, "Switching current input method from " + currentMethodId + " to device-specific one " + deviceMethodId + " because the current display " + displayIdToShowIme + " belongs to device with id " + newDeviceId);
        }
        return deviceMethodId;
    }

    private boolean shouldPreventImeStartupLocked(java.lang.String selectedMethodId, int startInputFlags, int unverifiedTargetSdkVersion) {
        android.view.inputmethod.InputMethodInfo imi;
        return (!this.mPreventImeStartupUnlessTextEditor || isShowRequestedForCurrentWindow() || com.android.server.inputmethod.InputMethodUtils.isSoftInputModeStateVisibleAllowed(unverifiedTargetSdkVersion, startInputFlags) || (imi = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId).getMethodMap().get(selectedMethodId)) == null || com.android.internal.util.ArrayUtils.contains(this.mNonPreemptibleInputMethods, imi.getPackageName())) ? false : true;
    }

    private void prepareClientSwitchLocked(com.android.server.inputmethod.ClientState cs) {
        unbindCurrentClientLocked(1);
        if (this.mIsInteractive) {
            cs.mClient.setActive(true, false);
        }
    }

    private com.android.internal.inputmethod.InputBindResult tryReuseConnectionLocked(com.android.server.inputmethod.InputMethodBindingController bindingController, com.android.server.inputmethod.ClientState cs) {
        if (bindingController.hasMainConnection()) {
            if (getCurMethodLocked() != null) {
                if (!android.view.inputmethod.Flags.useZeroJankProxy()) {
                    requestClientSessionLocked(cs);
                    requestClientSessionForAccessibilityLocked(cs);
                }
                return new com.android.internal.inputmethod.InputBindResult(1, (com.android.internal.inputmethod.IInputMethodSession) null, (android.util.SparseArray) null, (android.view.InputChannel) null, bindingController.getCurId(), bindingController.getSequenceNumber(), false);
            }
            long lastBindTime = bindingController.getLastBindTime();
            long bindingDuration = android.os.SystemClock.uptimeMillis() - lastBindTime;
            if (bindingDuration < com.android.server.inputmethod.InputMethodBindingController.TIME_TO_RECONNECT) {
                return new com.android.internal.inputmethod.InputBindResult(2, (com.android.internal.inputmethod.IInputMethodSession) null, (android.util.SparseArray) null, (android.view.InputChannel) null, bindingController.getCurId(), bindingController.getSequenceNumber(), false);
            }
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.IMF_FORCE_RECONNECT_IME, getSelectedMethodIdLocked(), java.lang.Long.valueOf(bindingDuration), 0);
            return null;
        }
        return null;
    }

    static int computeImeDisplayIdForTarget(int displayId, com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator checker) {
        if (displayId == 0 || displayId == -1) {
            return 0;
        }
        int result = checker.getDisplayImePolicy(displayId);
        if (result == 0) {
            return displayId;
        }
        return result == 2 ? -1 : 0;
    }

    void initializeImeLocked(com.android.server.inputmethod.IInputMethodInvoker inputMethod, android.os.IBinder token) {
        if (DEBUG) {
            android.util.Slog.v(TAG, "Sending attach of token: " + token + " for display: " + getCurTokenDisplayIdLocked());
        }
        inputMethod.initializeInternal(token, new com.android.server.inputmethod.InputMethodManagerService.InputMethodPrivilegedOperationsImpl(this, token), getInputMethodNavButtonFlagsLocked());
        this.mImmsWrapper.getExtImpl().onImeInitialized(getCurTokenDisplayIdLocked());
    }

    void scheduleResetStylusHandwriting() {
        this.mHandler.obtainMessage(1090).sendToTarget();
    }

    void schedulePrepareStylusHandwritingDelegation(int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName) {
        this.mHandler.obtainMessage(MSG_PREPARE_HANDWRITING_DELEGATION, userId, 0, new android.util.Pair(delegatePackageName, delegatorPackageName)).sendToTarget();
    }

    void scheduleRemoveStylusHandwritingWindow() {
        this.mHandler.obtainMessage(MSG_REMOVE_HANDWRITING_WINDOW).sendToTarget();
    }

    void scheduleNotifyImeUidToAudioService(int uid) {
        this.mHandler.removeMessages(MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE);
        this.mHandler.obtainMessage(MSG_NOTIFY_IME_UID_TO_AUDIO_SERVICE, uid, 0).sendToTarget();
    }

    void onSessionCreated(com.android.server.inputmethod.IInputMethodInvoker method, com.android.internal.inputmethod.IInputMethodSession session, android.view.InputChannel channel) {
        android.os.Trace.traceBegin(32L, "IMMS.onSessionCreated");
        try {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (this.mUserSwitchHandlerTask != null) {
                    channel.dispose();
                    return;
                }
                com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
                if (curMethod == null || method == null || curMethod.asBinder() != method.asBinder() || this.mCurClient == null) {
                    channel.dispose();
                    return;
                }
                clearClientSessionLocked(this.mCurClient);
                this.mCurClient.mCurSession = new com.android.server.inputmethod.InputMethodManagerService.SessionState(this.mCurClient, method, session, channel);
                com.android.internal.inputmethod.InputBindResult res = attachNewInputLocked(10, true);
                attachNewAccessibilityLocked(10, true);
                if (res.method != null) {
                    this.mCurClient.mClient.onBindMethod(res);
                }
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    void resetSystemUiLocked() throws java.lang.Throwable {
        this.mImeWindowVis = 0;
        this.mBackDisposition = 0;
        updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
    }

    void resetCurrentMethodAndClientLocked(int unbindClientReason) {
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        bindingController.setSelectedMethodId(null);
        onUnbindCurrentMethodByReset();
        bindingController.unbindCurrentMethod();
        unbindCurrentClientLocked(unbindClientReason);
    }

    void reRequestCurrentClientSessionLocked() throws java.lang.Throwable {
        if (this.mCurClient != null) {
            clearClientSessionLocked(this.mCurClient);
            clearClientSessionForAccessibilityLocked(this.mCurClient);
            requestClientSessionLocked(this.mCurClient);
            requestClientSessionForAccessibilityLocked(this.mCurClient);
        }
    }

    void requestClientSessionLocked(com.android.server.inputmethod.ClientState cs) {
        if (!cs.mSessionRequested) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Creating new session for client " + cs);
            }
            android.view.InputChannel[] channels = android.view.InputChannel.openInputChannelPair(cs.toString());
            final android.view.InputChannel serverChannel = channels[0];
            android.view.InputChannel clientChannel = channels[1];
            cs.mSessionRequested = true;
            final com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
            com.android.internal.inputmethod.IInputMethodSessionCallback.Stub callback = new com.android.internal.inputmethod.IInputMethodSessionCallback.Stub() { // from class: com.android.server.inputmethod.InputMethodManagerService.2
                public void sessionCreated(com.android.internal.inputmethod.IInputMethodSession session) {
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.inputmethod.InputMethodManagerService.this.onSessionCreated(curMethod, session, serverChannel);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(ident);
                    }
                }
            };
            try {
                curMethod.createSession(clientChannel, callback);
            } finally {
                if (clientChannel != null) {
                    clientChannel.dispose();
                }
            }
        }
    }

    void requestClientSessionForAccessibilityLocked(com.android.server.inputmethod.ClientState cs) {
        if (!cs.mSessionRequestedForAccessibility) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Creating new accessibility sessions for client " + cs);
            }
            cs.mSessionRequestedForAccessibility = true;
            android.util.ArraySet<java.lang.Integer> ignoreSet = new android.util.ArraySet<>();
            for (int i = 0; i < cs.mAccessibilitySessions.size(); i++) {
                ignoreSet.add(java.lang.Integer.valueOf(cs.mAccessibilitySessions.keyAt(i)));
            }
            com.android.server.AccessibilityManagerInternal.get().createImeSession(ignoreSet);
        }
    }

    void clearClientSessionLocked(com.android.server.inputmethod.ClientState cs) throws java.lang.Throwable {
        finishSessionLocked(cs.mCurSession);
        cs.mCurSession = null;
        cs.mSessionRequested = false;
    }

    void clearClientSessionForAccessibilityLocked(com.android.server.inputmethod.ClientState cs) {
        for (int i = 0; i < cs.mAccessibilitySessions.size(); i++) {
            finishSessionForAccessibilityLocked(cs.mAccessibilitySessions.valueAt(i));
        }
        cs.mAccessibilitySessions.clear();
        cs.mSessionRequestedForAccessibility = false;
    }

    void clearClientSessionForAccessibilityLocked(com.android.server.inputmethod.ClientState cs, int id) {
        com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState session = cs.mAccessibilitySessions.get(id);
        if (session != null) {
            finishSessionForAccessibilityLocked(session);
            cs.mAccessibilitySessions.remove(id);
        }
    }

    private void finishSessionLocked(com.android.server.inputmethod.InputMethodManagerService.SessionState sessionState) throws java.lang.Throwable {
        if (sessionState != null) {
            if (sessionState.mSession != null) {
                try {
                    sessionState.mSession.finishSession();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Session failed to close due to remote exception", e);
                    updateSystemUiLocked(0, this.mBackDisposition);
                }
                sessionState.mSession = null;
            }
            if (sessionState.mChannel != null) {
                sessionState.mChannel.dispose();
                sessionState.mChannel = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishSessionForAccessibilityLocked(com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState sessionState) {
        if (sessionState != null && sessionState.mSession != null) {
            try {
                sessionState.mSession.finishSession();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Session failed to close due to remote exception", e);
            }
            sessionState.mSession = null;
        }
    }

    void clearClientSessionsLocked() throws java.lang.Throwable {
        if (getCurMethodLocked() != null) {
            java.util.function.Consumer<com.android.server.inputmethod.ClientState> clearClientSession = new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda18
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) throws java.lang.Throwable {
                    this.f$0.lambda$clearClientSessionsLocked$10((com.android.server.inputmethod.ClientState) obj);
                }
            };
            this.mClientController.forAllClients(clearClientSession);
            finishSessionLocked(this.mEnabledSession);
            for (int i = 0; i < this.mEnabledAccessibilitySessions.size(); i++) {
                finishSessionForAccessibilityLocked(this.mEnabledAccessibilitySessions.valueAt(i));
            }
            this.mEnabledSession = null;
            this.mEnabledAccessibilitySessions.clear();
            scheduleNotifyImeUidToAudioService(-1);
        }
        hideStatusBarIconLocked();
        this.mInFullscreenMode = false;
        this.mWindowManagerInternal.setDismissImeOnBackKeyPressed(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearClientSessionsLocked$10(com.android.server.inputmethod.ClientState c) throws java.lang.Throwable {
        clearClientSessionLocked(c);
        clearClientSessionForAccessibilityLocked(c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatusIcon(android.os.IBinder token, java.lang.String packageName, int iconId) {
        android.content.pm.ApplicationInfo applicationInfo;
        java.lang.CharSequence applicationLabel;
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (calledWithValidTokenLocked(token)) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (iconId == 0) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "hide the small icon for the input method");
                        }
                        hideStatusBarIconLocked();
                    } else if (packageName != null) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "show a small icon for the input method");
                        }
                        android.content.pm.PackageManager userAwarePackageManager = getPackageManagerForUser(this.mContext, this.mCurrentUserId);
                        try {
                            android.content.pm.ApplicationInfo applicationInfo2 = userAwarePackageManager.getApplicationInfo(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L));
                            applicationInfo = applicationInfo2;
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            applicationInfo = null;
                        }
                        java.lang.String string = null;
                        if (applicationInfo != null) {
                            applicationLabel = userAwarePackageManager.getApplicationLabel(applicationInfo);
                        } else {
                            applicationLabel = null;
                        }
                        java.lang.CharSequence contentDescription = applicationLabel;
                        if (this.mStatusBarManagerInternal != null) {
                            com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal = this.mStatusBarManagerInternal;
                            java.lang.String str = this.mSlotIme;
                            if (contentDescription != null) {
                                string = contentDescription.toString();
                            }
                            statusBarManagerInternal.setIcon(str, packageName, iconId, 0, string);
                            this.mStatusBarManagerInternal.setIconVisibility(this.mSlotIme, true);
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(ident);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            }
        }
    }

    private void hideStatusBarIconLocked() {
        if (this.mStatusBarManagerInternal != null) {
            this.mStatusBarManagerInternal.setIconVisibility(this.mSlotIme, false);
        }
    }

    private int getInputMethodNavButtonFlagsLocked() {
        if (this.mImeDrawsImeNavBarResLazyInitFuture != null) {
            com.android.internal.util.ConcurrentUtils.waitForFutureNoInterrupt(this.mImeDrawsImeNavBarResLazyInitFuture, "Waiting for the lazy init of mImeDrawsImeNavBarRes");
        }
        int tokenDisplayId = getCurTokenDisplayIdLocked();
        boolean hasNavigationBar = this.mWindowManagerInternal.hasNavigationBar(tokenDisplayId != -1 ? tokenDisplayId : 0);
        boolean canImeDrawsImeNavBar = this.mImeDrawsImeNavBarRes != null && this.mImeDrawsImeNavBarRes.get() && hasNavigationBar;
        boolean shouldShowImeSwitcherWhenImeIsShown = shouldShowImeSwitcherLocked(3, this.mCurrentUserId);
        return (shouldShowImeSwitcherWhenImeIsShown ? 2 : 0) | (canImeDrawsImeNavBar ? 1 : 0);
    }

    private boolean shouldShowImeSwitcherLocked(int visibility, int userId) {
        if (this.mImmsWrapper.getExtImpl().shouldHideImeSwitcher() || !this.mShowOngoingImeSwitcherForPhones || this.mMenuController.getSwitchingDialogLocked() != null) {
            return false;
        }
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        if (!java.util.Objects.equals(bindingController.getCurId(), bindingController.getSelectedMethodId())) {
            return false;
        }
        if ((this.mWindowManagerInternal.isKeyguardShowingAndNotOccluded() && this.mWindowManagerInternal.isKeyguardSecure(userId)) || (visibility & 1) == 0 || (visibility & 4) != 0) {
            return false;
        }
        if (this.mWindowManagerInternal.isHardKeyboardAvailable()) {
            return true;
        }
        if ((visibility & 2) == 0) {
            return false;
        }
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        java.util.List<android.view.inputmethod.InputMethodInfo> imes = settings.getEnabledInputMethodListWithFilter(new java.util.function.Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.view.inputmethod.InputMethodInfo) obj).shouldShowInInputMethodPicker();
            }
        });
        int numImes = imes.size();
        if (numImes > 2) {
            return true;
        }
        if (numImes < 1) {
            return false;
        }
        int nonAuxCount = 0;
        int auxCount = 0;
        android.view.inputmethod.InputMethodSubtype nonAuxSubtype = null;
        android.view.inputmethod.InputMethodSubtype auxSubtype = null;
        for (int i = 0; i < numImes; i++) {
            android.view.inputmethod.InputMethodInfo imi = imes.get(i);
            java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes = settings.getEnabledInputMethodSubtypeList(imi, true);
            int subtypeCount = subtypes.size();
            if (subtypeCount == 0) {
                nonAuxCount++;
            } else {
                for (int j = 0; j < subtypeCount; j++) {
                    android.view.inputmethod.InputMethodSubtype subtype = subtypes.get(j);
                    if (!subtype.isAuxiliary()) {
                        nonAuxCount++;
                        nonAuxSubtype = subtype;
                    } else {
                        auxCount++;
                        auxSubtype = subtype;
                    }
                }
            }
        }
        if (nonAuxCount > 1 || auxCount > 1) {
            return true;
        }
        if (nonAuxCount == 1 && auxCount == 1) {
            return nonAuxSubtype == null || auxSubtype == null || !((nonAuxSubtype.getLocale().equals(auxSubtype.getLocale()) || auxSubtype.overridesImplicitlyEnabledSubtype() || nonAuxSubtype.overridesImplicitlyEnabledSubtype()) && nonAuxSubtype.containsExtraValueKey(TAG_TRY_SUPPRESSING_IME_SWITCHER));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setImeWindowStatus(android.os.IBinder token, int vis, int backDisposition) {
        boolean dismissImeOnBackKeyPressed;
        int topFocusedDisplayId = this.mWindowManagerInternal.getTopFocusedDisplayId();
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (calledWithValidTokenLocked(token)) {
                int tokenDisplayId = getCurTokenDisplayIdLocked();
                if (tokenDisplayId == topFocusedDisplayId || tokenDisplayId == 0) {
                    this.mImeWindowVis = vis;
                    this.mBackDisposition = backDisposition;
                    updateSystemUiLocked(vis, backDisposition);
                    switch (backDisposition) {
                        case 1:
                            dismissImeOnBackKeyPressed = false;
                            break;
                        case 2:
                            dismissImeOnBackKeyPressed = true;
                            break;
                        default:
                            dismissImeOnBackKeyPressed = (vis & 2) != 0;
                            break;
                    }
                    this.mWindowManagerInternal.setDismissImeOnBackKeyPressed(dismissImeOnBackKeyPressed);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportStartInput(android.os.IBinder token, android.os.IBinder startInputToken) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (calledWithValidTokenLocked(token)) {
                android.os.IBinder targetWindow = this.mImeTargetWindowMap.get(startInputToken);
                if (targetWindow != null) {
                    this.mWindowManagerInternal.updateInputMethodTargetWindow(token, targetWindow);
                }
                this.mLastImeTargetWindow = targetWindow;
                getWrapper().getExtImpl().showInputMethodPickerIfNeeded();
            }
        }
    }

    private void updateImeWindowStatus(boolean disableImeIcon) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (disableImeIcon) {
                updateSystemUiLocked(0, this.mBackDisposition);
            } else {
                updateSystemUiLocked();
            }
        }
    }

    void updateSystemUiLocked() throws java.lang.Throwable {
        updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemUiLocked(int vis, int backDisposition) throws java.lang.Throwable {
        updateSystemUiLocked(vis, backDisposition, this.mCurrentUserId);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void updateSystemUiLocked(int r19, int r20, int r21) throws java.lang.Throwable {
        /*
            r18 = this;
            r1 = r18
            r2 = r19
            r3 = r21
            com.android.server.inputmethod.InputMethodBindingController r4 = r1.getInputMethodBindingController(r3)
            android.os.IBinder r11 = r4.getCurToken()
            if (r11 != 0) goto L11
            return
        L11:
            int r12 = r4.getCurTokenDisplayId()
            boolean r0 = com.android.server.inputmethod.InputMethodManagerService.DEBUG
            if (r0 == 0) goto L53
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "IME window vis: "
            java.lang.StringBuilder r0 = r0.append(r5)
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r5 = " active: "
            java.lang.StringBuilder r0 = r0.append(r5)
            r5 = r2 & 1
            java.lang.StringBuilder r0 = r0.append(r5)
            java.lang.String r5 = " inv: "
            java.lang.StringBuilder r0 = r0.append(r5)
            r5 = r2 & 4
            java.lang.StringBuilder r0 = r0.append(r5)
            java.lang.String r5 = " displayId: "
            java.lang.StringBuilder r0 = r0.append(r5)
            java.lang.StringBuilder r0 = r0.append(r12)
            java.lang.String r0 = r0.toString()
            java.lang.String r5 = "InputMethodManagerService"
            android.util.Slog.d(r5, r0)
        L53:
            com.android.server.inputmethod.ImeBindingState r0 = r1.mImeBindingState
            r5 = 0
            if (r0 == 0) goto L5d
            com.android.server.inputmethod.ImeBindingState r0 = r1.mImeBindingState
            android.os.IBinder r0 = r0.mFocusedWindow
            goto L5e
        L5d:
            r0 = r5
        L5e:
            r13 = r0
            if (r13 == 0) goto L6b
            java.util.WeakHashMap<android.os.IBinder, java.lang.Boolean> r0 = r1.mFocusedWindowPerceptible
            java.lang.Object r0 = r0.get(r13)
            r5 = r0
            java.lang.Boolean r5 = (java.lang.Boolean) r5
            goto L6c
        L6b:
        L6c:
            r14 = r5
            long r15 = android.os.Binder.clearCallingIdentity()
            if (r14 == 0) goto L87
            boolean r0 = r14.booleanValue()     // Catch: java.lang.Throwable -> L83
            if (r0 != 0) goto L87
            r0 = r2 & 2
            if (r0 == 0) goto L8a
            r0 = r2 & (-3)
            r0 = r0 | 8
            r2 = r0
            goto L8a
        L83:
            r0 = move-exception
            r17 = r20
            goto Lc3
        L87:
            r0 = r2 & (-9)
            r2 = r0
        L8a:
            java.lang.String r0 = r4.getCurId()     // Catch: java.lang.Throwable -> Lc0
            com.android.server.inputmethod.InputMethodMenuController r5 = r1.mMenuController     // Catch: java.lang.Throwable -> Lc0
            android.app.AlertDialog r5 = r5.getSwitchingDialogLocked()     // Catch: java.lang.Throwable -> Lc0
            if (r5 != 0) goto La4
            java.lang.String r5 = r4.getSelectedMethodId()     // Catch: java.lang.Throwable -> Lc0
            boolean r5 = java.util.Objects.equals(r0, r5)     // Catch: java.lang.Throwable -> Lc0
            if (r5 != 0) goto La1
            goto La4
        La1:
            r17 = r20
            goto La7
        La4:
            r5 = 3
            r17 = r5
        La7:
            boolean r10 = r1.shouldShowImeSwitcherLocked(r2, r3)     // Catch: java.lang.Throwable -> Lbe
            com.android.server.statusbar.StatusBarManagerInternal r5 = r1.mStatusBarManagerInternal     // Catch: java.lang.Throwable -> Lbe
            if (r5 == 0) goto Lb9
            com.android.server.statusbar.StatusBarManagerInternal r5 = r1.mStatusBarManagerInternal     // Catch: java.lang.Throwable -> Lbe
            r6 = r12
            r7 = r11
            r8 = r2
            r9 = r17
            r5.setImeWindowStatus(r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> Lbe
        Lb9:
            android.os.Binder.restoreCallingIdentity(r15)
            return
        Lbe:
            r0 = move-exception
            goto Lc3
        Lc0:
            r0 = move-exception
            r17 = r20
        Lc3:
            android.os.Binder.restoreCallingIdentity(r15)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.updateSystemUiLocked(int, int, int):void");
    }

    void updateFromSettingsLocked(boolean enabledMayChange) throws java.lang.Throwable {
        updateInputMethodsFromSettingsLocked(enabledMayChange);
        this.mMenuController.updateKeyboardFromSettingsLocked();
    }

    void experimentalInitializeVisibleBackgroundUserLocked(int userId) {
        android.view.inputmethod.InputMethodInfo imi;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        java.lang.String enabledImeIdsStr = settings.getEnabledInputMethodsStr();
        for (android.view.inputmethod.InputMethodInfo imi2 : settings.getMethodList()) {
            if (!imi2.isSystem()) {
                return;
            } else {
                enabledImeIdsStr = com.android.server.inputmethod.InputMethodUtils.concatEnabledImeIds(enabledImeIdsStr, imi2.getId());
            }
        }
        if (!android.text.TextUtils.equals(settings.getEnabledInputMethodsStr(), enabledImeIdsStr)) {
            settings.putEnabledInputMethodsStr(enabledImeIdsStr);
        }
        java.lang.String id = settings.getSelectedInputMethod();
        if (!android.text.TextUtils.isEmpty(id) || (imi = com.android.server.inputmethod.InputMethodInfoUtils.getMostApplicableDefaultIME(settings.getEnabledInputMethodList())) == null) {
            return;
        }
        java.lang.String id2 = imi.getId();
        settings.putSelectedInputMethod(id2);
    }

    void updateInputMethodsFromSettingsLocked(boolean enabledMayChange) throws java.lang.Throwable {
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        if (enabledMayChange) {
            android.content.pm.PackageManager userAwarePackageManager = getPackageManagerForUser(this.mContext, userId);
            java.util.List<android.view.inputmethod.InputMethodInfo> enabled = settings.getEnabledInputMethodList();
            for (int i = 0; i < enabled.size(); i++) {
                android.view.inputmethod.InputMethodInfo imm = enabled.get(i);
                android.content.pm.ApplicationInfo ai = null;
                try {
                    ai = userAwarePackageManager.getApplicationInfo(imm.getPackageName(), android.content.pm.PackageManager.ApplicationInfoFlags.of(32768L));
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
                if (ai != null && ai.enabledSetting == 4) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Update state(" + imm.getId() + "): DISABLED_UNTIL_USED -> DEFAULT");
                    }
                    userAwarePackageManager.setApplicationEnabledSetting(imm.getPackageName(), 0, 1);
                }
            }
        }
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        if (bindingController.getDeviceIdToShowIme() == 0) {
            java.lang.String ime = com.android.server.inputmethod.SecureSettingsWrapper.getString("default_input_method", null, userId);
            java.lang.String defaultDeviceIme = com.android.server.inputmethod.SecureSettingsWrapper.getString("default_device_input_method", null, userId);
            if (defaultDeviceIme != null && !java.util.Objects.equals(ime, defaultDeviceIme)) {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Current input method " + ime + " differs from the stored default device input method for user " + userId + " - restoring " + defaultDeviceIme);
                }
                com.android.server.inputmethod.SecureSettingsWrapper.putString("default_input_method", defaultDeviceIme, userId);
                com.android.server.inputmethod.SecureSettingsWrapper.putString("default_device_input_method", null, userId);
            }
        }
        java.lang.String id = settings.getSelectedInputMethod();
        if (android.text.TextUtils.isEmpty(id) && chooseNewDefaultIMELocked()) {
            id = settings.getSelectedInputMethod();
        }
        if (!android.text.TextUtils.isEmpty(id)) {
            try {
                setInputMethodLocked(id, settings.getSelectedInputMethodSubtypeId(id));
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.w(TAG, "Unknown input method from prefs: " + id, e2);
                resetCurrentMethodAndClientLocked(5);
            }
        } else {
            resetCurrentMethodAndClientLocked(4);
        }
        if (userId == this.mSwitchingController.getUserId()) {
            this.mSwitchingController.resetCircularListLocked(settings.getMethodMap());
        } else {
            this.mSwitchingController = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.createInstanceLocked(this.mContext, settings.getMethodMap(), userId);
        }
        if (userId == this.mHardwareKeyboardShortcutController.getUserId()) {
            this.mHardwareKeyboardShortcutController.reset(settings.getMethodMap());
        } else {
            this.mHardwareKeyboardShortcutController = new com.android.server.inputmethod.HardwareKeyboardShortcutController(settings.getMethodMap(), userId);
        }
        sendOnNavButtonFlagsChangedLocked();
    }

    private void notifyInputMethodSubtypeChangedLocked(int userId, android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
        android.view.inputmethod.InputMethodSubtype normalizedSubtype;
        com.android.internal.inputmethod.InputMethodSubtypeHandle newSubtypeHandle = null;
        if (subtype == null || !subtype.isSuitableForPhysicalKeyboardLayoutMapping()) {
            normalizedSubtype = null;
        } else {
            normalizedSubtype = subtype;
        }
        if (normalizedSubtype != null) {
            newSubtypeHandle = com.android.internal.inputmethod.InputMethodSubtypeHandle.of(imi, normalizedSubtype);
        }
        this.mInputManagerInternal.onInputMethodSubtypeChangedForKeyboardLayoutMapping(userId, newSubtypeHandle, normalizedSubtype);
    }

    void setInputMethodLocked(java.lang.String id, int subtypeId) throws java.lang.Throwable {
        setInputMethodLocked(id, subtypeId, 0);
    }

    void setInputMethodLocked(java.lang.String id, int subtypeId, int deviceId) throws java.lang.Throwable {
        android.view.inputmethod.InputMethodSubtype newSubtype;
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        android.view.inputmethod.InputMethodInfo info = settings.getMethodMap().get(id);
        if (info == null) {
            throw getExceptionForUnknownImeId(id);
        }
        if (this.mImmsWrapper.getExtImpl().setInputMethodLocked(info, subtypeId)) {
            return;
        }
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        if (id.equals(bindingController.getSelectedMethodId())) {
            int subtypeCount = info.getSubtypeCount();
            if (subtypeCount <= 0) {
                notifyInputMethodSubtypeChangedLocked(userId, info, null);
                return;
            }
            android.view.inputmethod.InputMethodSubtype oldSubtype = bindingController.getCurrentSubtype();
            if (subtypeId >= 0 && subtypeId < subtypeCount) {
                newSubtype = info.getSubtypeAt(subtypeId);
            } else {
                subtypeId = -1;
                newSubtype = getCurrentInputMethodSubtypeLocked();
                if (newSubtype != null) {
                    int i = 0;
                    while (true) {
                        if (i >= subtypeCount) {
                            break;
                        }
                        if (!java.util.Objects.equals(newSubtype, info.getSubtypeAt(i))) {
                            i++;
                        } else {
                            subtypeId = i;
                            break;
                        }
                    }
                }
            }
            if (!java.util.Objects.equals(newSubtype, oldSubtype)) {
                setSelectedInputMethodAndSubtypeLocked(info, subtypeId, true);
                com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
                if (curMethod != null) {
                    updateSystemUiLocked(this.mImeWindowVis, this.mBackDisposition);
                    curMethod.changeInputMethodSubtype(newSubtype);
                    return;
                }
                return;
            }
            return;
        }
        if (bindingController.getDeviceIdToShowIme() != 0 && deviceId == 0) {
            settings.putSelectedDefaultDeviceInputMethod(id);
            return;
        }
        com.android.server.inputmethod.IInputMethodInvoker curMethod2 = getCurMethodLocked();
        if (curMethod2 != null) {
            curMethod2.removeStylusHandwritingWindow();
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            setSelectedInputMethodAndSubtypeLocked(info, subtypeId, false);
            bindingController.setSelectedMethodId(id);
            this.mImmsWrapper.getExtImpl().unfreezeInputMethodPackage(info);
            if (this.mActivityManagerInternal.isSystemReady()) {
                android.content.Intent intent = new android.content.Intent("android.intent.action.INPUT_METHOD_CHANGED");
                intent.addFlags(536870912);
                intent.putExtra("input_method_id", id);
                this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.CURRENT);
            }
            unbindCurrentClientLocked(2);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean showSoftInput(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int lastClickToolType, android.os.ResultReceiver resultReceiver, int reason, boolean async) {
        boolean z;
        android.os.Trace.traceBegin(32L, "IMMS.showSoftInput");
        int uid = android.os.Binder.getCallingUid();
        com.android.internal.inputmethod.ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#showSoftInput", this.mDumper);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            try {
                if (!canInteractWithImeLocked(uid, client, "showSoftInput", statsToken)) {
                    android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 3);
                    android.os.Trace.traceEnd(32L);
                    return false;
                }
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    getWrapper().getExtImpl().setServiceUxEnabled(true, "showSoftInput");
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Client requesting input be shown");
                    }
                    if (android.view.inputmethod.Flags.refactorInsetsController()) {
                        boolean wasVisible = isInputShownLocked();
                        if (this.mImeBindingState == null || this.mImeBindingState.mFocusedWindowClient == null || this.mImeBindingState.mFocusedWindowClient.mClient == null) {
                            android.os.Binder.restoreCallingIdentity(ident);
                            android.os.Trace.traceEnd(32L);
                            getWrapper().getExtImpl().setServiceUxEnabled(false, "showSoftInput");
                            return false;
                        }
                        this.mImeBindingState.mFocusedWindowClient.mClient.setImeVisibility(true);
                        if (resultReceiver != null) {
                            resultReceiver.send(wasVisible ? 0 : 2, null);
                        }
                        android.os.Binder.restoreCallingIdentity(ident);
                        android.os.Trace.traceEnd(32L);
                        getWrapper().getExtImpl().setServiceUxEnabled(false, "showSoftInput");
                        return true;
                    }
                    try {
                        boolean zShowCurrentInputLocked = showCurrentInputLocked(windowToken, statsToken, flags, lastClickToolType, resultReceiver, reason);
                        android.os.Binder.restoreCallingIdentity(ident);
                        android.os.Trace.traceEnd(32L);
                        getWrapper().getExtImpl().setServiceUxEnabled(false, "showSoftInput");
                        return zShowCurrentInputLocked;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        z = false;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    z = false;
                }
                android.os.Binder.restoreCallingIdentity(ident);
                android.os.Trace.traceEnd(32L);
                getWrapper().getExtImpl().setServiceUxEnabled(z, "showSoftInput");
                throw th;
            } catch (java.lang.Throwable th3) {
                throw th3;
            }
        }
    }

    boolean showSoftInputInternal(android.os.IBinder windowToken) {
        boolean zShowCurrentInputLocked;
        android.os.Trace.traceBegin(32L, "IMMS.showSoftInputInternal");
        com.android.internal.inputmethod.ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#showSoftInput", this.mDumper);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Client requesting input be shown");
                }
                zShowCurrentInputLocked = showCurrentInputLocked(windowToken, null, 0, 0, null, 1);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
                android.os.Trace.traceEnd(32L);
            }
        }
        return zShowCurrentInputLocked;
    }

    boolean hideSoftInputInternal(android.os.IBinder windowToken) {
        boolean zHideCurrentInputLocked;
        android.os.Trace.traceBegin(32L, "IMMS.hideSoftInputInternal");
        com.android.internal.inputmethod.ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#hideSoftInput", this.mDumper);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Client requesting input be hidden");
                }
                zHideCurrentInputLocked = hideCurrentInputLocked(windowToken, null, 0, null, 4);
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
                android.os.Trace.traceEnd(32L);
            }
        }
        return zHideCurrentInputLocked;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient client) throws java.lang.Throwable {
        startStylusHandwriting(client, false);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startConnectionlessStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient client, final int userId, android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo, final java.lang.String delegatePackageName, final java.lang.String delegatorPackageName, final com.android.internal.inputmethod.IConnectionlessHandwritingCallback callback) throws java.lang.Throwable {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            try {
                com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
                if (!bindingController.supportsConnectionlessStylusHandwriting()) {
                    android.util.Slog.w(TAG, "Connectionless stylus handwriting mode unsupported by IME.");
                    try {
                        callback.onError(1);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(TAG, "Failed to report CONNECTIONLESS_HANDWRITING_ERROR_UNSUPPORTED", e);
                        e.rethrowAsRuntimeException();
                    }
                    return;
                }
                com.android.internal.inputmethod.IConnectionlessHandwritingCallback immsCallback = callback;
                boolean isForDelegation = (delegatePackageName == null || delegatorPackageName == null) ? false : true;
                if (isForDelegation) {
                    synchronized (com.android.server.inputmethod.ImfLock.class) {
                        try {
                            try {
                                if (!this.mClientController.verifyClientAndPackageMatch(client, delegatorPackageName)) {
                                    android.util.Slog.w(TAG, "startConnectionlessStylusHandwriting() fail");
                                    try {
                                        callback.onError(2);
                                    } catch (android.os.RemoteException e2) {
                                        android.util.Slog.e(TAG, "Failed to report CONNECTIONLESS_HANDWRITING_ERROR_OTHER", e2);
                                        e2.rethrowAsRuntimeException();
                                    }
                                    throw new java.lang.IllegalArgumentException("Delegator doesn't match UID");
                                }
                                immsCallback = new com.android.internal.inputmethod.IConnectionlessHandwritingCallback.Stub() { // from class: com.android.server.inputmethod.InputMethodManagerService.3
                                    public void onResult(java.lang.CharSequence text) throws android.os.RemoteException {
                                        synchronized (com.android.server.inputmethod.ImfLock.class) {
                                            com.android.server.inputmethod.InputMethodManagerService.this.mHwController.prepareStylusHandwritingDelegation(userId, delegatePackageName, delegatorPackageName, true);
                                        }
                                        callback.onResult(text);
                                    }

                                    public void onError(int errorCode) throws android.os.RemoteException {
                                        callback.onError(errorCode);
                                    }
                                };
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
                if (!startStylusHandwriting(client, false, immsCallback, cursorAnchorInfo, isForDelegation)) {
                    try {
                        callback.onError(2);
                    } catch (android.os.RemoteException e3) {
                        android.util.Slog.e(TAG, "Failed to report CONNECTIONLESS_HANDWRITING_ERROR_OTHER", e3);
                        e3.rethrowAsRuntimeException();
                    }
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                }
            }
        }
    }

    private void startStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient client, boolean acceptingDelegation) throws java.lang.Throwable {
        startStylusHandwriting(client, acceptingDelegation, null, null, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002f A[Catch: all -> 0x00f8, DONT_GENERATE, TRY_LEAVE, TryCatch #2 {all -> 0x00f8, blocks: (B:9:0x0028, B:11:0x002f, B:14:0x0034, B:16:0x003a, B:17:0x0041, B:20:0x0046, B:24:0x005e, B:25:0x0061, B:31:0x007a, B:32:0x007d, B:38:0x0092, B:39:0x0095, B:45:0x00aa, B:46:0x00ad), top: B:90:0x0028 }] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0034 A[Catch: all -> 0x00f8, TRY_ENTER, TryCatch #2 {all -> 0x00f8, blocks: (B:9:0x0028, B:11:0x002f, B:14:0x0034, B:16:0x003a, B:17:0x0041, B:20:0x0046, B:24:0x005e, B:25:0x0061, B:31:0x007a, B:32:0x007d, B:38:0x0092, B:39:0x0095, B:45:0x00aa, B:46:0x00ad), top: B:90:0x0028 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean startStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient r16, boolean r17, com.android.internal.inputmethod.IConnectionlessHandwritingCallback r18, android.view.inputmethod.CursorAnchorInfo r19, boolean r20) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 278
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.startStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient, boolean, com.android.internal.inputmethod.IConnectionlessHandwritingCallback, android.view.inputmethod.CursorAnchorInfo, boolean):boolean");
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void prepareStylusHandwritingDelegation(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName) {
        if (!isStylusHandwritingEnabled(this.mContext, userId)) {
            android.util.Slog.w(TAG, "Can not prepare stylus handwriting delegation. Stylus handwriting pref is disabled for user: " + userId);
            return;
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (!this.mClientController.verifyClientAndPackageMatch(client, delegatorPackageName)) {
                android.util.Slog.w(TAG, "prepareStylusHandwritingDelegation() fail");
                throw new java.lang.IllegalArgumentException("Delegator doesn't match Uid");
            }
        }
        schedulePrepareStylusHandwritingDelegation(userId, delegatePackageName, delegatorPackageName);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void acceptStylusHandwritingDelegationAsync(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags, com.android.internal.inputmethod.IBooleanListener callback) {
        boolean result = acceptStylusHandwritingDelegation(client, userId, delegatePackageName, delegatorPackageName, flags);
        try {
            callback.onResult(result);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to report result=" + result, e);
            e.rethrowAsRuntimeException();
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean acceptStylusHandwritingDelegation(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags) {
        if (!isStylusHandwritingEnabled(this.mContext, userId)) {
            android.util.Slog.w(TAG, "Can not accept stylus handwriting delegation. Stylus handwriting pref is disabled for user: " + userId);
            return false;
        }
        if (!verifyDelegator(client, delegatePackageName, delegatorPackageName, flags)) {
            return false;
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (this.mHwController.isDelegationUsingConnectionlessFlow()) {
                com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
                if (curMethod == null) {
                    return false;
                }
                curMethod.commitHandwritingDelegationTextIfAvailable();
                this.mHwController.clearPendingHandwritingDelegation();
            } else {
                startStylusHandwriting(client, true);
            }
            return true;
        }
    }

    private boolean verifyDelegator(com.android.internal.inputmethod.IInputMethodClient client, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (!this.mClientController.verifyClientAndPackageMatch(client, delegatePackageName)) {
                android.util.Slog.w(TAG, "Delegate package does not belong to the same user. Ignoring startStylusHandwriting");
                return false;
            }
            boolean homeDelegatorAllowed = (flags & 1) != 0;
            if (!delegatorPackageName.equals(this.mHwController.getDelegatorPackageName()) && (!homeDelegatorAllowed || !this.mHwController.isDelegatorFromDefaultHomePackage())) {
                android.util.Slog.w(TAG, "Delegator package does not match. Ignoring startStylusHandwriting");
                return false;
            }
            if (delegatePackageName.equals(this.mHwController.getDelegatePackageName())) {
                return true;
            }
            android.util.Slog.w(TAG, "Delegate package does not match. Ignoring startStylusHandwriting");
            return false;
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void reportPerceptibleAsync(final android.os.IBinder windowToken, final boolean perceptible) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda23
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$reportPerceptibleAsync$11(windowToken, perceptible);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportPerceptibleAsync$11(android.os.IBinder windowToken, boolean perceptible) throws java.lang.Exception {
        java.util.Objects.requireNonNull(windowToken, "windowToken must not be null");
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            java.lang.Boolean windowPerceptible = this.mFocusedWindowPerceptible.get(windowToken);
            if (this.mImeBindingState.mFocusedWindow == windowToken && (windowPerceptible == null || windowPerceptible.booleanValue() != perceptible)) {
                this.mFocusedWindowPerceptible.put(windowToken, windowPerceptible);
                updateSystemUiLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean showCurrentInputLocked(android.os.IBinder windowToken, int flags, int reason) {
        android.view.inputmethod.ImeTracker.Token statsToken = createStatsTokenForFocusedClient(true, reason);
        return showCurrentInputLocked(windowToken, statsToken, flags, 0, null, reason);
    }

    boolean showCurrentInputLocked(android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int lastClickToolType, android.os.ResultReceiver resultReceiver, int reason) {
        boolean readyToDispatchToIme;
        if (this.mImmsWrapper.getExtImpl().shouldInterceptImeForZoom(windowToken) || !this.mVisibilityStateComputer.onImeShowFlags(statsToken, flags)) {
            return false;
        }
        if (!this.mSystemReady) {
            android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 5);
            return false;
        }
        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 5);
        if (this.mImmsWrapper.getExtImpl().shouldIgnoreShowBySynergy(getSelectedMethodIdLocked())) {
            return false;
        }
        this.mVisibilityStateComputer.requestImeVisibility(windowToken, true);
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        bindingController.setCurrentMethodVisible();
        com.android.server.inputmethod.IInputMethodInvoker curMethod = getCurMethodLocked();
        android.view.inputmethod.ImeTracker.forLogging().onCancelled(this.mCurStatsToken, 8);
        if (android.view.inputmethod.Flags.deferShowSoftInputUntilSessionCreation()) {
            readyToDispatchToIme = (curMethod == null || this.mCurClient == null || this.mCurClient.mCurSession == null) ? false : true;
        } else {
            readyToDispatchToIme = curMethod != null;
        }
        if (readyToDispatchToIme) {
            android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 9);
            this.mImmsWrapper.getExtImpl().updateOsenseAction();
            this.mCurStatsToken = null;
            if (android.view.inputmethod.Flags.useHandwritingListenerForTooltype()) {
                maybeReportToolType();
            } else if (lastClickToolType != 0) {
                onUpdateEditorToolType(lastClickToolType);
            }
            this.mVisibilityApplier.performShowIme(windowToken, statsToken, this.mVisibilityStateComputer.getShowFlagsForInputMethodServiceOnly(), resultReceiver, reason);
            this.mVisibilityStateComputer.setInputShown(true);
            return true;
        }
        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 8);
        this.mCurStatsToken = statsToken;
        return false;
    }

    private void maybeReportToolType() {
        android.view.InputDevice device;
        int toolType;
        int lastDeviceId = this.mInputManagerInternal.getLastUsedInputDeviceId();
        android.hardware.input.InputManager im = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        if (im == null || (device = im.getInputDevice(lastDeviceId)) == null) {
            return;
        }
        if (isStylusDevice(device)) {
            toolType = 2;
        } else if (isFingerDevice(device)) {
            toolType = 1;
        } else {
            toolType = 0;
        }
        onUpdateEditorToolType(toolType);
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean hideSoftInput(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, android.os.ResultReceiver resultReceiver, int reason, boolean async) {
        long j;
        int uid = android.os.Binder.getCallingUid();
        com.android.internal.inputmethod.ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#hideSoftInput", this.mDumper);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            try {
                int i = 3;
                if (!canInteractWithImeLocked(uid, client, "hideSoftInput", statsToken)) {
                    if (isInputShownLocked()) {
                        android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 3);
                    } else {
                        android.view.inputmethod.ImeTracker.forLogging().onCancelled(statsToken, 3);
                    }
                    return false;
                }
                com.android.server.inputmethod.ClientState cs = this.mClientController.getClient(client.asBinder());
                if (cs.mSelfReportedDisplayId != getCurTokenDisplayIdLocked()) {
                    if (DEBUG) {
                        android.util.Slog.w(TAG, "Ignoring hideSoftInput of displayId " + cs.mSelfReportedDisplayId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + getCurTokenDisplayIdLocked());
                    }
                    return false;
                }
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    getWrapper().getExtImpl().setServiceUxEnabled(true, "hideSoftInput");
                    android.os.Trace.traceBegin(32L, "IMMS.hideSoftInput");
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Client requesting input be hidden");
                    }
                    if (android.view.inputmethod.Flags.refactorInsetsController()) {
                        if (this.mImeBindingState == null || this.mImeBindingState.mFocusedWindowClient == null || this.mImeBindingState.mFocusedWindowClient.mClient == null) {
                            android.os.Binder.restoreCallingIdentity(ident);
                            android.os.Trace.traceEnd(32L);
                            getWrapper().getExtImpl().setServiceUxEnabled(false, "hideSoftInput");
                            return false;
                        }
                        boolean wasVisible = isInputShownLocked();
                        this.mImeBindingState.mFocusedWindowClient.mClient.setImeVisibility(false);
                        if (resultReceiver != null) {
                            if (!wasVisible) {
                                i = 1;
                            }
                            resultReceiver.send(i, null);
                        }
                        android.os.Binder.restoreCallingIdentity(ident);
                        android.os.Trace.traceEnd(32L);
                        getWrapper().getExtImpl().setServiceUxEnabled(false, "hideSoftInput");
                        return true;
                    }
                    j = 32;
                    try {
                        boolean zHideCurrentInputLocked = hideCurrentInputLocked(windowToken, statsToken, flags, resultReceiver, reason);
                        android.os.Binder.restoreCallingIdentity(ident);
                        android.os.Trace.traceEnd(32L);
                        getWrapper().getExtImpl().setServiceUxEnabled(false, "hideSoftInput");
                        return zHideCurrentInputLocked;
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    j = 32;
                }
                android.os.Binder.restoreCallingIdentity(ident);
                android.os.Trace.traceEnd(j);
                getWrapper().getExtImpl().setServiceUxEnabled(false, "hideSoftInput");
                throw th;
            } catch (java.lang.Throwable th3) {
                throw th3;
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void hideSoftInputFromServerForTest() {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            hideCurrentInputLocked(this.mImeBindingState.mFocusedWindow, 0, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hideCurrentInputLocked(android.os.IBinder windowToken, int flags, int reason) {
        android.view.inputmethod.ImeTracker.Token statsToken = createStatsTokenForFocusedClient(false, reason);
        return hideCurrentInputLocked(windowToken, statsToken, flags, null, reason);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean hideCurrentInputLocked(android.os.IBinder r7, android.view.inputmethod.ImeTracker.Token r8, int r9, android.os.ResultReceiver r10, int r11) {
        /*
            r6 = this;
            com.android.server.inputmethod.ImeVisibilityStateComputer r0 = r6.mVisibilityStateComputer
            boolean r0 = r0.canHideIme(r8, r9)
            r1 = 0
            if (r0 != 0) goto La
            return r1
        La:
            com.android.server.inputmethod.IInputMethodInvoker r0 = r6.getCurMethodLocked()
            if (r0 == 0) goto L2b
            boolean r2 = r6.isInputShownLocked()
            r3 = 1
            if (r2 != 0) goto L2a
            int r2 = r6.mImeWindowVis
            r2 = r2 & r3
            if (r2 != 0) goto L2a
            com.android.server.inputmethod.InputMethodManagerService$InputMethodManagerServiceWrapper r2 = r6.mImmsWrapper
            com.android.server.inputmethod.IInputMethodManagerServiceExt r2 = r2.getExtImpl()
            com.android.server.inputmethod.ImeVisibilityStateComputer r4 = r6.mVisibilityStateComputer
            boolean r2 = r2.shouldForceHideSoftInput(r4, r11)
            if (r2 == 0) goto L2b
        L2a:
            goto L2c
        L2b:
            r3 = r1
        L2c:
            r2 = r3
            com.android.server.inputmethod.ImeVisibilityStateComputer r3 = r6.mVisibilityStateComputer
            r3.requestImeVisibility(r7, r1)
            r1 = 10
            if (r2 == 0) goto L43
            android.view.inputmethod.ImeTracker r3 = android.view.inputmethod.ImeTracker.forLogging()
            r3.onProgress(r8, r1)
            com.android.server.inputmethod.DefaultImeVisibilityApplier r1 = r6.mVisibilityApplier
            r1.performHideIme(r7, r8, r10, r11)
            goto L4a
        L43:
            android.view.inputmethod.ImeTracker r3 = android.view.inputmethod.ImeTracker.forLogging()
            r3.onCancelled(r8, r1)
        L4a:
            int r1 = r6.mCurrentUserId
            com.android.server.inputmethod.InputMethodBindingController r1 = r6.getInputMethodBindingController(r1)
            r1.setCurrentMethodNotVisible()
            com.android.server.inputmethod.ImeVisibilityStateComputer r3 = r6.mVisibilityStateComputer
            r3.clearImeShowFlags()
            android.view.inputmethod.ImeTracker r3 = android.view.inputmethod.ImeTracker.forLogging()
            android.view.inputmethod.ImeTracker$Token r4 = r6.mCurStatsToken
            r5 = 8
            r3.onCancelled(r4, r5)
            r3 = 0
            r6.mCurStatsToken = r3
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.hideCurrentInputLocked(android.os.IBinder, android.view.inputmethod.ImeTracker$Token, int, android.os.ResultReceiver, int):boolean");
    }

    private boolean isImeClientFocused(android.os.IBinder windowToken, com.android.server.inputmethod.ClientState cs) {
        int imeClientFocus = this.mWindowManagerInternal.hasInputMethodClientFocus(windowToken, cs.mUid, cs.mPid, cs.mSelfReportedDisplayId);
        return imeClientFocus == 0;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startInputOrWindowGainedFocusAsync(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, int userId, android.window.ImeOnBackInvokedDispatcher imeDispatcher, int startInputSeq, boolean useAsyncShowHideMethod) {
    }

    /* JADX WARN: Not initialized variable reg: 22, insn: 0x0394: MOVE (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r22 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('userId' int)]), block:B:142:0x0394 */
    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.InputBindResult startInputOrWindowGainedFocus(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, int userId, android.window.ImeOnBackInvokedDispatcher imeDispatcher) throws java.lang.Throwable {
        long j;
        int userId2;
        int userId3;
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
            if (editorInfo == null || editorInfo.targetInputMethodUser == null || editorInfo.targetInputMethodUser.getIdentifier() != userId) {
                throw new java.security.InvalidParameterException("EditorInfo#targetInputMethodUser must also be specified for cross-user startInputOrWindowGainedFocus()");
            }
        }
        if (windowToken == null) {
            android.util.Slog.e(TAG, "windowToken cannot be null.");
            return com.android.internal.inputmethod.InputBindResult.NULL;
        }
        if (!this.mUserManagerInternal.isUserRunning(userId)) {
            android.util.Slog.w(TAG, "User #" + userId + " is not running.");
            return com.android.internal.inputmethod.InputBindResult.INVALID_USER;
        }
        try {
            getWrapper().getExtImpl().setServiceUxEnabled(true, "startInputOrWindowGainedFocus");
            android.os.Trace.traceBegin(32L, "IMMS.startInputOrWindowGainedFocus");
            com.android.internal.inputmethod.ImeTracing.getInstance().triggerManagerServiceDump("InputMethodManagerService#startInputOrWindowGainedFocus", this.mDumper);
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                try {
                    if (this.mImmsWrapper.getExtImpl().isMultiAppUserId(userId)) {
                        try {
                            userId2 = this.mCurrentUserId;
                        } catch (java.lang.Throwable th) {
                            th = th;
                            userId2 = userId;
                            j = 32;
                        }
                    } else {
                        userId2 = userId;
                    }
                    try {
                        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId2);
                        try {
                            if (!this.mSystemReady) {
                                com.android.internal.inputmethod.InputBindResult inputBindResult = new com.android.internal.inputmethod.InputBindResult(8, (com.android.internal.inputmethod.IInputMethodSession) null, (android.util.SparseArray) null, (android.view.InputChannel) null, getSelectedMethodIdLocked(), bindingController.getSequenceNumber(), false);
                                android.os.Trace.traceEnd(32L);
                                getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                return inputBindResult;
                            }
                            com.android.server.inputmethod.ClientState cs = this.mClientController.getClient(client.asBinder());
                            try {
                                if (cs == null) {
                                    throw new java.lang.IllegalArgumentException("Unknown client " + client.asBinder());
                                }
                                long ident = android.os.Binder.clearCallingIdentity();
                                try {
                                    if (!this.mExperimentalConcurrentMultiUserModeEnabled) {
                                        try {
                                            if (this.mUserSwitchHandlerTask != null) {
                                                int nextUserId = this.mUserSwitchHandlerTask.mToUserId;
                                                if (userId2 == nextUserId) {
                                                    scheduleSwitchUserTaskLocked(userId2, cs.mClient);
                                                    com.android.internal.inputmethod.InputBindResult inputBindResult2 = com.android.internal.inputmethod.InputBindResult.USER_SWITCHING;
                                                    android.os.Binder.restoreCallingIdentity(ident);
                                                    android.os.Trace.traceEnd(32L);
                                                    getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                    return inputBindResult2;
                                                }
                                                int[] profileIdsWithDisabled = this.mUserManagerInternal.getProfileIds(this.mCurrentUserId, false);
                                                for (int profileId : profileIdsWithDisabled) {
                                                    if (profileId == userId2) {
                                                        scheduleSwitchUserTaskLocked(userId2, cs.mClient);
                                                        com.android.internal.inputmethod.InputBindResult inputBindResult3 = com.android.internal.inputmethod.InputBindResult.USER_SWITCHING;
                                                        android.os.Binder.restoreCallingIdentity(ident);
                                                        android.os.Trace.traceEnd(32L);
                                                        getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                        return inputBindResult3;
                                                    }
                                                }
                                                com.android.internal.inputmethod.InputBindResult inputBindResult4 = com.android.internal.inputmethod.InputBindResult.INVALID_USER;
                                                android.os.Binder.restoreCallingIdentity(ident);
                                                android.os.Trace.traceEnd(32L);
                                                getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                return inputBindResult4;
                                            }
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            android.os.Binder.restoreCallingIdentity(ident);
                                            throw th;
                                        }
                                    }
                                    int imeClientFocus = this.mWindowManagerInternal.hasInputMethodClientFocus(windowToken, cs.mUid, cs.mPid, cs.mSelfReportedDisplayId);
                                    switch (imeClientFocus) {
                                        case -3:
                                            com.android.internal.inputmethod.InputBindResult inputBindResult5 = com.android.internal.inputmethod.InputBindResult.INVALID_DISPLAY_ID;
                                            android.os.Binder.restoreCallingIdentity(ident);
                                            android.os.Trace.traceEnd(32L);
                                            getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                            return inputBindResult5;
                                        case -2:
                                            android.util.Slog.e(TAG, "startInputOrWindowGainedFocusInternal: display ID mismatch.");
                                            com.android.internal.inputmethod.InputBindResult inputBindResult6 = com.android.internal.inputmethod.InputBindResult.DISPLAY_ID_MISMATCH;
                                            android.os.Binder.restoreCallingIdentity(ident);
                                            android.os.Trace.traceEnd(32L);
                                            getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                            return inputBindResult6;
                                        case -1:
                                            if (DEBUG) {
                                                android.util.Slog.w(TAG, "Focus gain on non-focused client " + cs.mClient + " (uid=" + cs.mUid + " pid=" + cs.mPid + ")");
                                            }
                                            com.android.internal.inputmethod.InputBindResult inputBindResult7 = com.android.internal.inputmethod.InputBindResult.NOT_IME_TARGET_WINDOW;
                                            android.os.Binder.restoreCallingIdentity(ident);
                                            android.os.Trace.traceEnd(32L);
                                            getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                            return inputBindResult7;
                                        default:
                                            boolean shouldClearFlag = this.mImePlatformCompatUtils.shouldClearShowForcedFlag(cs.mUid);
                                            boolean showForced = this.mVisibilityStateComputer.mShowForced;
                                            if (this.mImeBindingState.mFocusedWindow != windowToken && showForced && shouldClearFlag) {
                                                this.mVisibilityStateComputer.mShowForced = false;
                                            }
                                            if (!this.mExperimentalConcurrentMultiUserModeEnabled && userId2 != this.mCurrentUserId) {
                                                if (com.android.internal.util.ArrayUtils.contains(this.mUserManagerInternal.getProfileIds(this.mCurrentUserId, false), userId2)) {
                                                    scheduleSwitchUserTaskLocked(userId2, cs.mClient);
                                                    com.android.internal.inputmethod.InputBindResult inputBindResult8 = com.android.internal.inputmethod.InputBindResult.USER_SWITCHING;
                                                    android.os.Binder.restoreCallingIdentity(ident);
                                                    android.os.Trace.traceEnd(32L);
                                                    getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                    return inputBindResult8;
                                                }
                                                android.util.Slog.w(TAG, "A background user is requesting window. Hiding IME.");
                                                android.util.Slog.w(TAG, "If you need to impersonate a foreground user/profile from a background user, use EditorInfo.targetInputMethodUser with INTERACT_ACROSS_USERS_FULL permission.");
                                                hideCurrentInputLocked(this.mImeBindingState.mFocusedWindow, 0, 11);
                                                com.android.internal.inputmethod.InputBindResult inputBindResult9 = com.android.internal.inputmethod.InputBindResult.INVALID_USER;
                                                android.os.Binder.restoreCallingIdentity(ident);
                                                android.os.Trace.traceEnd(32L);
                                                getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                return inputBindResult9;
                                            }
                                            if (editorInfo != null && !com.android.server.inputmethod.InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, cs.mUid, editorInfo.packageName)) {
                                                android.util.Slog.e(TAG, "Rejecting this client as it reported an invalid package name. uid=" + cs.mUid + " package=" + editorInfo.packageName);
                                                com.android.internal.inputmethod.InputBindResult inputBindResult10 = com.android.internal.inputmethod.InputBindResult.INVALID_PACKAGE_NAME;
                                                android.os.Binder.restoreCallingIdentity(ident);
                                                android.os.Trace.traceEnd(32L);
                                                getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                return inputBindResult10;
                                            }
                                            j = 32;
                                            try {
                                                com.android.internal.inputmethod.InputBindResult result = startInputOrWindowGainedFocusInternalLocked(startInputReason, client, windowToken, startInputFlags, softInputMode, windowFlags, editorInfo, inputConnection, remoteAccessibilityInputConnection, unverifiedTargetSdkVersion, bindingController, imeDispatcher, cs);
                                                android.os.Binder.restoreCallingIdentity(ident);
                                                if (result != null) {
                                                    android.os.Trace.traceEnd(32L);
                                                    getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                    return result;
                                                }
                                                try {
                                                    android.util.Slog.wtf(TAG, "InputBindResult is @NonNull. startInputReason=" + com.android.internal.inputmethod.InputMethodDebug.startInputReasonToString(startInputReason) + " windowFlags=#" + java.lang.Integer.toHexString(windowFlags) + " editorInfo=" + editorInfo);
                                                    com.android.internal.inputmethod.InputBindResult inputBindResult11 = com.android.internal.inputmethod.InputBindResult.NULL;
                                                    android.os.Trace.traceEnd(32L);
                                                    getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                    return inputBindResult11;
                                                } catch (java.lang.Throwable th3) {
                                                    th = th3;
                                                    android.os.Trace.traceEnd(j);
                                                    getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                                    throw th;
                                                }
                                            } catch (java.lang.Throwable th4) {
                                                th = th4;
                                                android.os.Binder.restoreCallingIdentity(ident);
                                                throw th;
                                            }
                                    }
                                } catch (java.lang.Throwable th5) {
                                    th = th5;
                                }
                            } catch (java.lang.Throwable th6) {
                                th = th6;
                                userId2 = userId3;
                                while (true) {
                                    try {
                                        try {
                                            throw th;
                                        } catch (java.lang.Throwable th7) {
                                            th = th7;
                                            android.os.Trace.traceEnd(j);
                                            getWrapper().getExtImpl().setServiceUxEnabled(false, "startInputOrWindowGainedFocus");
                                            throw th;
                                        }
                                    } catch (java.lang.Throwable th8) {
                                        th = th8;
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th9) {
                            th = th9;
                            j = 32;
                        }
                    } catch (java.lang.Throwable th10) {
                        th = th10;
                        j = 32;
                    }
                } catch (java.lang.Throwable th11) {
                    th = th11;
                    j = 32;
                    userId2 = userId;
                }
                while (true) {
                    throw th;
                }
            }
        } catch (java.lang.Throwable th12) {
            th = th12;
            j = 32;
        }
    }

    private com.android.internal.inputmethod.InputBindResult startInputOrWindowGainedFocusInternalLocked(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputContext, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, com.android.server.inputmethod.InputMethodBindingController bindingController, android.window.ImeOnBackInvokedDispatcher imeDispatcher, com.android.server.inputmethod.ClientState cs) throws java.lang.Throwable {
        boolean isTextEditor;
        boolean sameWindowFocused;
        if (DEBUG) {
            android.util.Slog.v(TAG, "startInputOrWindowGainedFocusInternalLocked: reason=" + com.android.internal.inputmethod.InputMethodDebug.startInputReasonToString(startInputReason) + " client=" + client.asBinder() + " inputContext=" + inputContext + " editorInfo=" + editorInfo + " startInputFlags=" + com.android.internal.inputmethod.InputMethodDebug.startInputFlagsToString(startInputFlags) + " softInputMode=" + com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(softInputMode) + " windowFlags=#" + java.lang.Integer.toHexString(windowFlags) + " unverifiedTargetSdkVersion=" + unverifiedTargetSdkVersion + " bindingController=" + bindingController + " imeDispatcher=" + imeDispatcher + " cs=" + cs);
        }
        boolean sameWindowFocused2 = this.mImeBindingState.mFocusedWindow == windowToken;
        boolean isTextEditor2 = (startInputFlags & 2) != 0;
        boolean startInputByWinGainedFocus = (startInputFlags & 8) != 0;
        int toolType = editorInfo != null ? editorInfo.getInitialToolType() : 0;
        if (!sameWindowFocused2) {
            isTextEditor = isTextEditor2;
            sameWindowFocused = sameWindowFocused2;
            if (this.mImmsWrapper.getExtImpl().shouldIgnoreStartInput(this.mContext, startInputFlags, editorInfo, cs.mSelfReportedDisplayId, getCurTokenDisplayIdLocked(), isInputShownLocked())) {
                return com.android.internal.inputmethod.InputBindResult.INVALID_DISPLAY_ID;
            }
        } else {
            isTextEditor = isTextEditor2;
            sameWindowFocused = sameWindowFocused2;
        }
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState windowState = new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState(softInputMode, windowFlags, !sameWindowFocused, isTextEditor, startInputByWinGainedFocus, toolType);
        this.mVisibilityStateComputer.setWindowState(windowToken, windowState);
        boolean isTextEditor3 = isTextEditor;
        this.mImmsWrapper.getExtImpl().notifyImeAttributeChanged(isTextEditor3, editorInfo, sameWindowFocused, cs.mSelfReportedDisplayId);
        if (!sameWindowFocused || !isTextEditor3) {
            this.mImeBindingState = new com.android.server.inputmethod.ImeBindingState(bindingController.mUserId, windowToken, softInputMode, cs, editorInfo);
            this.mFocusedWindowPerceptible.put(windowToken, true);
            boolean didStart = false;
            com.android.internal.inputmethod.InputBindResult res = null;
            com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult imeVisRes = this.mVisibilityStateComputer.computeState(windowState, com.android.server.inputmethod.InputMethodUtils.isSoftInputModeStateVisibleAllowed(unverifiedTargetSdkVersion, startInputFlags));
            if (imeVisRes != null) {
                boolean isShow = false;
                switch (imeVisRes.getReason()) {
                    case 6:
                    case 7:
                    case 8:
                    case 23:
                        if (editorInfo == null) {
                            isShow = true;
                        } else {
                            res = startInputUncheckedLocked(cs, inputContext, remoteAccessibilityInputConnection, editorInfo, startInputFlags, startInputReason, unverifiedTargetSdkVersion, imeDispatcher, bindingController);
                            didStart = true;
                            isShow = true;
                        }
                        break;
                }
                android.view.inputmethod.ImeTracker.Token statsToken = createStatsTokenForFocusedClient(isShow, imeVisRes.getReason());
                this.mVisibilityApplier.applyImeVisibility(this.mImeBindingState.mFocusedWindow, statsToken, imeVisRes.getState(), imeVisRes.getReason(), bindingController.mUserId);
                if (imeVisRes.getReason() == 12 && cs.mSelfReportedDisplayId != getCurTokenDisplayIdLocked()) {
                    bindingController.unbindCurrentMethod();
                }
            }
            if (!didStart) {
                if (editorInfo != null) {
                    return startInputUncheckedLocked(cs, inputContext, remoteAccessibilityInputConnection, editorInfo, startInputFlags, startInputReason, unverifiedTargetSdkVersion, imeDispatcher, bindingController);
                }
                return com.android.internal.inputmethod.InputBindResult.NULL_EDITOR_INFO;
            }
            return res;
        }
        if (DEBUG) {
            android.util.Slog.w(TAG, "Window already focused, ignoring focus gain of: " + client + " editorInfo=" + editorInfo + ", token = " + windowToken + ", startInputReason=" + com.android.internal.inputmethod.InputMethodDebug.startInputReasonToString(startInputReason));
        }
        return editorInfo != null ? startInputUncheckedLocked(cs, inputContext, remoteAccessibilityInputConnection, editorInfo, startInputFlags, startInputReason, unverifiedTargetSdkVersion, imeDispatcher, bindingController) : new com.android.internal.inputmethod.InputBindResult(4, (com.android.internal.inputmethod.IInputMethodSession) null, (android.util.SparseArray) null, (android.view.InputChannel) null, (java.lang.String) null, -1, false);
    }

    private boolean canInteractWithImeLocked(int uid, com.android.internal.inputmethod.IInputMethodClient client, java.lang.String methodName, android.view.inputmethod.ImeTracker.Token statsToken) {
        if (this.mCurClient == null || client == null || this.mCurClient.mClient.asBinder() != client.asBinder()) {
            com.android.server.inputmethod.ClientState cs = this.mClientController.getClient(client.asBinder());
            if (cs != null) {
                android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 2);
                if (!isImeClientFocused(this.mImeBindingState.mFocusedWindow, cs)) {
                    android.util.Slog.w(TAG, java.lang.String.format("Ignoring %s of uid %d : %s", methodName, java.lang.Integer.valueOf(uid), client));
                    return false;
                }
            } else {
                android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 2);
                throw new java.lang.IllegalArgumentException("unknown client " + client.asBinder());
            }
        }
        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 3);
        return true;
    }

    private boolean canShowInputMethodPickerLocked(com.android.internal.inputmethod.IInputMethodClient client) {
        android.content.Intent curIntent;
        int uid = android.os.Binder.getCallingUid();
        if (this.mImeBindingState.mFocusedWindowClient == null || client == null || this.mImeBindingState.mFocusedWindowClient.mClient.asBinder() != client.asBinder()) {
            return this.mCurrentUserId == android.os.UserHandle.getUserId(uid) && (curIntent = getInputMethodBindingController(this.mCurrentUserId).getCurIntent()) != null && com.android.server.inputmethod.InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, uid, curIntent.getComponent().getPackageName());
        }
        return true;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void showInputMethodPickerFromClient(com.android.internal.inputmethod.IInputMethodClient client, int auxiliarySubtypeMode) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (!canShowInputMethodPickerLocked(client)) {
                android.util.Slog.w(TAG, "Ignoring showInputMethodPickerFromClient of uid " + android.os.Binder.getCallingUid() + ": " + client);
            } else {
                int displayId = this.mCurClient != null ? this.mCurClient.mSelfReportedDisplayId : 0;
                this.mHandler.obtainMessage(1, auxiliarySubtypeMode, displayId).sendToTarget();
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void showInputMethodPickerFromSystem(int auxiliarySubtypeMode, int displayId) {
        if (this.mImmsWrapper.getExtImpl().shouldInterceptInputMethodPicker(auxiliarySubtypeMode, displayId)) {
            return;
        }
        this.mHandler.obtainMessage(1, auxiliarySubtypeMode, displayId).sendToTarget();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean isInputMethodPickerShownForTest() {
        boolean zIsisInputMethodPickerShownForTestLocked;
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            zIsisInputMethodPickerShownForTestLocked = this.mMenuController.isisInputMethodPickerShownForTestLocked();
        }
        return zIsisInputMethodPickerShownForTestLocked;
    }

    private static java.lang.IllegalArgumentException getExceptionForUnknownImeId(java.lang.String imeId) {
        return new java.lang.IllegalArgumentException("Unknown id: " + imeId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInputMethod(android.os.IBinder token, java.lang.String id) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (calledWithValidTokenLocked(token)) {
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
                android.view.inputmethod.InputMethodInfo imi = settings.getMethodMap().get(id);
                if (imi == null || !canCallerAccessInputMethod(imi.getPackageName(), callingUid, userId, settings)) {
                    throw getExceptionForUnknownImeId(id);
                }
                setInputMethodWithSubtypeIdLocked(token, id, -1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setInputMethodAndSubtype(android.os.IBinder token, java.lang.String id, android.view.inputmethod.InputMethodSubtype subtype) {
        int callingUid = android.os.Binder.getCallingUid();
        int userId = android.os.UserHandle.getUserId(callingUid);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (calledWithValidTokenLocked(token)) {
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
                android.view.inputmethod.InputMethodInfo imi = settings.getMethodMap().get(id);
                if (imi == null || !canCallerAccessInputMethod(imi.getPackageName(), callingUid, userId, settings)) {
                    throw getExceptionForUnknownImeId(id);
                }
                if (subtype != null) {
                    setInputMethodWithSubtypeIdLocked(token, id, com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(imi, subtype.hashCode()));
                } else {
                    setInputMethod(token, id);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean switchToPreviousInputMethod(android.os.IBinder token) throws java.lang.Throwable {
        android.view.inputmethod.InputMethodInfo lastImi;
        java.util.List<android.view.inputmethod.InputMethodInfo> enabled;
        java.lang.String locale;
        int userId;
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            try {
                try {
                    if (!calledWithValidTokenLocked(token)) {
                        return false;
                    }
                    int userId2 = this.mCurrentUserId;
                    com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId2);
                    com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId2);
                    android.util.Pair<java.lang.String, java.lang.String> lastIme = settings.getLastInputMethodAndSubtype();
                    if (lastIme != null) {
                        lastImi = settings.getMethodMap().get((java.lang.String) lastIme.first);
                    } else {
                        lastImi = null;
                    }
                    android.view.inputmethod.InputMethodSubtype currentSubtype = bindingController.getCurrentSubtype();
                    java.lang.String targetLastImiId = null;
                    int subtypeId = -1;
                    if (lastIme != null && lastImi != null) {
                        boolean imiIdIsSame = lastImi.getId().equals(bindingController.getSelectedMethodId());
                        int lastSubtypeHash = java.lang.Integer.parseInt((java.lang.String) lastIme.second);
                        int currentSubtypeHash = currentSubtype == null ? -1 : currentSubtype.hashCode();
                        if (!imiIdIsSame || lastSubtypeHash != currentSubtypeHash) {
                            targetLastImiId = (java.lang.String) lastIme.first;
                            subtypeId = com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(lastImi, lastSubtypeHash);
                        }
                    }
                    boolean imiIdIsSame2 = android.text.TextUtils.isEmpty(targetLastImiId);
                    if (imiIdIsSame2 && !com.android.server.inputmethod.InputMethodUtils.canAddToLastInputMethod(currentSubtype) && (enabled = settings.getEnabledInputMethodList()) != null) {
                        int enabledCount = enabled.size();
                        if (currentSubtype != null && !android.text.TextUtils.isEmpty(currentSubtype.getLocale())) {
                            locale = currentSubtype.getLocale();
                        } else {
                            locale = com.android.server.inputmethod.SystemLocaleWrapper.get(userId2).get(0).toString();
                        }
                        int i = 0;
                        while (i < enabledCount) {
                            android.view.inputmethod.InputMethodInfo imi = enabled.get(i);
                            if (imi.getSubtypeCount() <= 0 || !imi.isSystem()) {
                                userId = userId2;
                            } else {
                                userId = userId2;
                                android.view.inputmethod.InputMethodSubtype keyboardSubtype = com.android.server.inputmethod.SubtypeUtils.findLastResortApplicableSubtype(com.android.server.inputmethod.SubtypeUtils.getSubtypes(imi), "keyboard", locale, true);
                                if (keyboardSubtype != null) {
                                    targetLastImiId = imi.getId();
                                    subtypeId = com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(imi, keyboardSubtype.hashCode());
                                    if (keyboardSubtype.getLocale().equals(locale)) {
                                        break;
                                    }
                                }
                            }
                            i++;
                            userId2 = userId;
                        }
                    }
                    if (android.text.TextUtils.isEmpty(targetLastImiId)) {
                        return false;
                    }
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Switch to: " + lastImi.getId() + ", " + ((java.lang.String) lastIme.second) + ", from: " + getSelectedMethodIdLocked() + ", " + subtypeId);
                    }
                    setInputMethodWithSubtypeIdLocked(token, targetLastImiId, subtypeId);
                    return true;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean switchToNextInputMethod(android.os.IBinder token, boolean onlyCurrentIme) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (!calledWithValidTokenLocked(token)) {
                return false;
            }
            return switchToNextInputMethodLocked(token, onlyCurrentIme);
        }
    }

    private boolean switchToNextInputMethodLocked(android.os.IBinder token, boolean onlyCurrentIme) {
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        android.view.inputmethod.InputMethodInfo currentImi = bindingController.getSelectedMethod();
        com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem nextSubtype = this.mSwitchingController.getNextInputMethodLocked(onlyCurrentIme, currentImi, bindingController.getCurrentSubtype());
        if (nextSubtype == null) {
            return false;
        }
        setInputMethodWithSubtypeIdLocked(token, nextSubtype.mImi.getId(), nextSubtype.mSubtypeId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldOfferSwitchingToNextInputMethod(android.os.IBinder token) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (!calledWithValidTokenLocked(token)) {
                return false;
            }
            int userId = this.mCurrentUserId;
            com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
            android.view.inputmethod.InputMethodInfo currentImi = bindingController.getSelectedMethod();
            com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem nextSubtype = this.mSwitchingController.getNextInputMethodLocked(false, currentImi, bindingController.getCurrentSubtype());
            return nextSubtype != null;
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public android.view.inputmethod.InputMethodSubtype getLastInputMethodSubtype(int userId) {
        android.view.inputmethod.InputMethodSubtype lastInputMethodSubtype;
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            lastInputMethodSubtype = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId).getLastInputMethodSubtype();
        }
        return lastInputMethodSubtype;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void setAdditionalInputMethodSubtypes(java.lang.String imiId, android.view.inputmethod.InputMethodSubtype[] subtypes, int userId) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (android.text.TextUtils.isEmpty(imiId) || subtypes == null) {
            return;
        }
        java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> toBeAdded = new java.util.ArrayList<>();
        for (android.view.inputmethod.InputMethodSubtype subtype : subtypes) {
            if (!toBeAdded.contains(subtype)) {
                toBeAdded.add(subtype);
            } else {
                android.util.Slog.w(TAG, "Duplicated subtype definition found: " + subtype.getLocale() + ", " + subtype.getMode());
            }
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (this.mSystemReady) {
                com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap = com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId);
                boolean isCurrentUser = this.mCurrentUserId == userId;
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
                com.android.server.inputmethod.AdditionalSubtypeMap newAdditionalSubtypeMap = settings.getNewAdditionalSubtypeMap(imiId, toBeAdded, additionalSubtypeMap, this.mPackageManagerInternal, callingUid);
                if (additionalSubtypeMap != newAdditionalSubtypeMap) {
                    com.android.server.inputmethod.AdditionalSubtypeMapRepository.putAndSave(userId, newAdditionalSubtypeMap, settings.getMethodMap());
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.inputmethod.InputMethodSettings newSettings = queryInputMethodServicesInternal(this.mContext, userId, com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId), 0);
                        com.android.server.inputmethod.InputMethodSettingsRepository.put(userId, newSettings);
                        if (isCurrentUser) {
                            postInputMethodSettingUpdatedLocked(false);
                        }
                        android.os.Binder.restoreCallingIdentity(ident);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                }
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void setExplicitlyEnabledInputMethodSubtypes(java.lang.String imeId, int[] subtypeHashCodes, int userId) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        int callingUid = android.os.Binder.getCallingUid();
        android.content.ComponentName imeComponentName = imeId != null ? android.content.ComponentName.unflattenFromString(imeId) : null;
        if (imeComponentName == null || !com.android.server.inputmethod.InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, callingUid, imeComponentName.getPackageName())) {
            throw new java.lang.SecurityException("Calling UID=" + callingUid + " does not belong to imeId=" + imeId);
        }
        java.util.Objects.requireNonNull(subtypeHashCodes, "subtypeHashCodes must not be null");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                boolean currentUser = this.mCurrentUserId == userId;
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
                if (!settings.setEnabledInputMethodSubtypes(imeId, subtypeHashCodes)) {
                    return;
                }
                if (currentUser) {
                    if (this.mSettingsObserver != null) {
                        this.mSettingsObserver.mLastEnabled = settings.getEnabledInputMethodsStr();
                    }
                    updateInputMethodsFromSettingsLocked(false);
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    @java.lang.Deprecated
    public int getInputMethodWindowVisibleHeight(final com.android.internal.inputmethod.IInputMethodClient client) {
        final int callingUid = android.os.Binder.getCallingUid();
        return ((java.lang.Integer) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda22
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getInputMethodWindowVisibleHeight$12(callingUid, client);
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getInputMethodWindowVisibleHeight$12(int callingUid, com.android.internal.inputmethod.IInputMethodClient client) throws java.lang.Exception {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (!canInteractWithImeLocked(callingUid, client, "getInputMethodWindowVisibleHeight", null)) {
                return 0;
            }
            int curTokenDisplayId = getCurTokenDisplayIdLocked();
            return java.lang.Integer.valueOf(this.mWindowManagerInternal.getInputMethodWindowVisibleHeight(curTokenDisplayId));
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void removeImeSurface() {
        this.mHandler.obtainMessage(MSG_REMOVE_IME_SURFACE).sendToTarget();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void removeImeSurfaceFromWindowAsync(android.os.IBinder windowToken) {
        this.mHandler.obtainMessage(MSG_REMOVE_IME_SURFACE_FROM_WINDOW, windowToken).sendToTarget();
    }

    private void registerDeviceListenerAndCheckStylusSupport() {
        final android.hardware.input.InputManager im = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        android.util.IntArray stylusIds = getStylusInputDeviceIds(im);
        if (stylusIds.size() > 0) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                this.mStylusIds = new android.util.IntArray();
                this.mStylusIds.addAll(stylusIds);
            }
        }
        im.registerInputDeviceListener(new android.hardware.input.InputManager.InputDeviceListener() { // from class: com.android.server.inputmethod.InputMethodManagerService.4
            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceAdded(int deviceId) {
                android.view.InputDevice device = im.getInputDevice(deviceId);
                if (device != null && com.android.server.inputmethod.InputMethodManagerService.isStylusDevice(device)) {
                    add(deviceId);
                }
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceRemoved(int deviceId) {
                remove(deviceId);
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceChanged(int deviceId) {
                android.view.InputDevice device = im.getInputDevice(deviceId);
                if (device == null) {
                    return;
                }
                if (com.android.server.inputmethod.InputMethodManagerService.isStylusDevice(device)) {
                    add(deviceId);
                } else {
                    remove(deviceId);
                }
            }

            private void add(int deviceId) {
                synchronized (com.android.server.inputmethod.ImfLock.class) {
                    com.android.server.inputmethod.InputMethodManagerService.this.addStylusDeviceIdLocked(deviceId);
                }
            }

            private void remove(int deviceId) {
                synchronized (com.android.server.inputmethod.ImfLock.class) {
                    com.android.server.inputmethod.InputMethodManagerService.this.removeStylusDeviceIdLocked(deviceId);
                }
            }
        }, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addStylusDeviceIdLocked(int deviceId) {
        if (this.mStylusIds == null) {
            this.mStylusIds = new android.util.IntArray();
        } else if (this.mStylusIds.indexOf(deviceId) != -1) {
            return;
        }
        android.util.Slog.d(TAG, "New Stylus deviceId" + deviceId + " added.");
        this.mStylusIds.add(deviceId);
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        if (!this.mHwController.getCurrentRequestId().isPresent() && bindingController.supportsStylusHandwriting()) {
            scheduleResetStylusHandwriting();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeStylusDeviceIdLocked(int deviceId) {
        if (this.mStylusIds == null || this.mStylusIds.size() == 0) {
            return;
        }
        int index = this.mStylusIds.indexOf(deviceId);
        if (index != -1) {
            this.mStylusIds.remove(index);
            android.util.Slog.d(TAG, "Stylus deviceId: " + deviceId + " removed.");
        }
        if (this.mStylusIds.size() == 0) {
            this.mHwController.reset();
            scheduleRemoveStylusHandwritingWindow();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isStylusDevice(android.view.InputDevice inputDevice) {
        return inputDevice.supportsSource(16386) || inputDevice.supportsSource(49154);
    }

    private static boolean isFingerDevice(android.view.InputDevice inputDevice) {
        return inputDevice.supportsSource(4098);
    }

    private boolean hasSupportedStylusLocked() {
        return (this.mStylusIds == null || this.mStylusIds.size() == 0) ? false : true;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void addVirtualStylusIdForTestSession(com.android.internal.inputmethod.IInputMethodClient client) {
        int uid = android.os.Binder.getCallingUid();
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (canInteractWithImeLocked(uid, client, "addVirtualStylusIdForTestSession", null)) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Adding virtual stylus id for session");
                    }
                    addStylusDeviceIdLocked(VIRTUAL_STYLUS_ID_FOR_TEST.intValue());
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void setStylusWindowIdleTimeoutForTest(com.android.internal.inputmethod.IInputMethodClient client, long timeout) {
        int uid = android.os.Binder.getCallingUid();
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (canInteractWithImeLocked(uid, client, "setStylusWindowIdleTimeoutForTest", null)) {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Setting stylus window idle timeout");
                    }
                    getCurMethodLocked().setStylusWindowIdleTimeoutForTest(timeout);
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        }
    }

    private void removeVirtualStylusIdForTestSessionLocked() {
        removeStylusDeviceIdLocked(VIRTUAL_STYLUS_ID_FOR_TEST.intValue());
    }

    private static android.util.IntArray getStylusInputDeviceIds(android.hardware.input.InputManager im) {
        android.util.IntArray stylusIds = new android.util.IntArray();
        for (int id : im.getInputDeviceIds()) {
            android.view.InputDevice device = im.getInputDevice(id);
            if (device != null && device.isEnabled() && isStylusDevice(device)) {
                stylusIds.add(id);
            }
        }
        return stylusIds;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startProtoDump(byte[] protoDump, int source, java.lang.String where) {
        if (protoDump == null && source != 2) {
            return;
        }
        com.android.internal.inputmethod.ImeTracing tracingInstance = com.android.internal.inputmethod.ImeTracing.getInstance();
        if (!tracingInstance.isAvailable() || !tracingInstance.isEnabled()) {
            return;
        }
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream();
        switch (source) {
            case 0:
                long client_token = proto.start(2246267895810L);
                proto.write(1125281431553L, android.os.SystemClock.elapsedRealtimeNanos());
                proto.write(1138166333442L, where);
                proto.write(1146756268035L, protoDump);
                proto.end(client_token);
                break;
            case 1:
                long service_token = proto.start(2246267895810L);
                proto.write(1125281431553L, android.os.SystemClock.elapsedRealtimeNanos());
                proto.write(1138166333442L, where);
                proto.write(1146756268035L, protoDump);
                proto.end(service_token);
                break;
            case 2:
                long managerservice_token = proto.start(2246267895810L);
                proto.write(1125281431553L, android.os.SystemClock.elapsedRealtimeNanos());
                proto.write(1138166333442L, where);
                dumpDebug(proto, 1146756268035L);
                proto.end(managerservice_token);
                break;
            default:
                return;
        }
        tracingInstance.addToBuffer(proto, source);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean isImeTraceEnabled() {
        return com.android.internal.inputmethod.ImeTracing.getInstance().isEnabled();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startImeTrace() {
        com.android.internal.inputmethod.ImeTracing.getInstance().startTrace((java.io.PrintWriter) null);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mClientController.forAllClients(new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda17
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.inputmethod.ClientState) obj).mClient.setImeTraceEnabled(true);
                }
            });
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void stopImeTrace() {
        com.android.internal.inputmethod.ImeTracing.getInstance().stopTrace((java.io.PrintWriter) null);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mClientController.forAllClients(new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.inputmethod.ClientState) obj).mClient.setImeTraceEnabled(false);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
            long token = proto.start(fieldId);
            proto.write(1138166333441L, getSelectedMethodIdLocked());
            proto.write(1120986464258L, bindingController.getSequenceNumber());
            proto.write(1138166333443L, java.util.Objects.toString(this.mCurClient));
            this.mImeBindingState.dumpDebug(proto, this.mWindowManagerInternal);
            proto.write(1138166333445L, this.mWindowManagerInternal.getWindowName(this.mLastImeTargetWindow));
            proto.write(1138166333446L, com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(this.mImeBindingState.mFocusedWindowSoftInputMode));
            if (this.mCurEditorInfo != null) {
                this.mCurEditorInfo.dumpDebug(proto, 1146756268039L);
            }
            proto.write(1138166333448L, bindingController.getCurId());
            this.mVisibilityStateComputer.dumpDebug(proto, fieldId);
            proto.write(1133871366157L, this.mInFullscreenMode);
            proto.write(1138166333454L, java.util.Objects.toString(getCurTokenLocked()));
            proto.write(1120986464271L, getCurTokenDisplayIdLocked());
            proto.write(1133871366160L, this.mSystemReady);
            proto.write(1133871366162L, bindingController.hasMainConnection());
            proto.write(1133871366163L, this.mBoundToMethod);
            proto.write(1133871366164L, this.mIsInteractive);
            proto.write(1120986464277L, this.mBackDisposition);
            proto.write(1120986464278L, this.mImeWindowVis);
            proto.write(1133871366167L, this.mMenuController.getShowImeWithHardKeyboard());
            proto.end(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUserAction(android.os.IBinder token) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Got the notification of a user action.");
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (getCurTokenLocked() != token) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Ignoring the user action notification from IMEs that are no longer active.");
                }
                return;
            }
            int userId = this.mCurrentUserId;
            if (userId != this.mSwitchingController.getUserId()) {
                return;
            }
            android.view.inputmethod.InputMethodInfo imi = getInputMethodBindingController(userId).getSelectedMethod();
            if (imi != null) {
                this.mSwitchingController.onUserActionLocked(imi, getInputMethodBindingController(userId).getCurrentSubtype());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyImeVisibility(android.os.IBinder token, android.os.IBinder windowToken, boolean setVisible, android.view.inputmethod.ImeTracker.Token statsToken) {
        try {
            android.os.Trace.traceBegin(32L, "IMMS.applyImeVisibility");
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (!calledWithValidTokenLocked(token)) {
                    android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 47);
                } else {
                    if (this.mImmsWrapper.getExtImpl().onApplyImeVisibility(setVisible)) {
                        return;
                    }
                    android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 47);
                    android.os.IBinder requestToken = this.mVisibilityStateComputer.getWindowTokenFrom(windowToken);
                    this.mVisibilityApplier.applyImeVisibility(requestToken, statsToken, setVisible ? 1 : 0, this.mCurrentUserId);
                }
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetStylusHandwriting(int requestId) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            java.util.OptionalInt curRequest = this.mHwController.getCurrentRequestId();
            if (!curRequest.isPresent() || curRequest.getAsInt() != requestId) {
                android.util.Slog.w(TAG, "IME requested to finish handwriting with a mismatched requestId: " + requestId);
            }
            removeVirtualStylusIdForTestSessionLocked();
            scheduleResetStylusHandwriting();
        }
    }

    private void setInputMethodWithSubtypeIdLocked(android.os.IBinder token, final java.lang.String id, int subtypeId) {
        if (token == null) {
            if (this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_SECURE_SETTINGS") != 0) {
                throw new java.lang.SecurityException("Using null token requires permission android.permission.WRITE_SECURE_SETTINGS");
            }
        } else {
            if (getCurTokenLocked() != token) {
                android.util.Slog.w(TAG, "Ignoring setInputMethod of uid " + android.os.Binder.getCallingUid() + " token: " + token);
                return;
            }
            com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
            if (settings.getMethodMap().get(id) != null && settings.getEnabledInputMethodListWithFilter(new java.util.function.Predicate() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda12
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((android.view.inputmethod.InputMethodInfo) obj).getId().equals(id);
                }
            }).isEmpty()) {
                throw new java.lang.IllegalStateException("Requested IME is not enabled: " + id);
            }
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            setInputMethodLocked(id, subtypeId);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void onShowHideSoftInputRequested(boolean show, android.os.IBinder requestImeToken, int reason, android.view.inputmethod.ImeTracker.Token statsToken) {
        android.os.IBinder requestToken = this.mVisibilityStateComputer.getWindowTokenFrom(requestImeToken);
        com.android.server.wm.WindowManagerInternal.ImeTargetInfo info = this.mWindowManagerInternal.onToggleImeRequested(show, this.mImeBindingState.mFocusedWindow, requestToken, getCurTokenDisplayIdLocked());
        this.mSoftInputShowHideHistory.addEntry(new com.android.server.inputmethod.SoftInputShowHideHistory.Entry(this.mImeBindingState.mFocusedWindowClient, this.mImeBindingState.mFocusedWindowEditorInfo, info.focusedWindowName, this.mImeBindingState.mFocusedWindowSoftInputMode, reason, this.mInFullscreenMode, info.requestWindowName, info.imeControlTargetName, info.imeLayerTargetName, info.imeSurfaceParentName));
        if (statsToken != null) {
            this.mImeTrackerService.onImmsUpdate(statsToken, info.requestWindowName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideMySoftInput(android.os.IBinder token, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int reason) {
        try {
            android.os.Trace.traceBegin(32L, "IMMS.hideMySoftInput");
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (!calledWithValidTokenLocked(token)) {
                    android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 47);
                    return;
                }
                android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 47);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (android.view.inputmethod.Flags.refactorInsetsController()) {
                        this.mCurClient.mClient.setImeVisibility(false);
                        if (this.mImeBindingState != null && this.mImeBindingState.mFocusedWindowClient != null && this.mImeBindingState.mFocusedWindowClient.mClient != null) {
                            this.mImeBindingState.mFocusedWindowClient.mClient.setImeVisibility(false);
                        }
                    } else {
                        hideCurrentInputLocked(this.mLastImeTargetWindow, statsToken, flags, null, reason);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMySoftInput(android.os.IBinder token, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int reason) {
        try {
            android.os.Trace.traceBegin(32L, "IMMS.showMySoftInput");
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (!calledWithValidTokenLocked(token)) {
                    android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 47);
                    return;
                }
                android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 47);
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    if (android.view.inputmethod.Flags.refactorInsetsController()) {
                        this.mCurClient.mClient.setImeVisibility(false);
                        if (this.mImeBindingState != null && this.mImeBindingState.mFocusedWindowClient != null && this.mImeBindingState.mFocusedWindowClient.mClient != null) {
                            this.mImeBindingState.mFocusedWindowClient.mClient.setImeVisibility(true);
                        }
                    } else {
                        showCurrentInputLocked(this.mLastImeTargetWindow, statsToken, flags, 0, null, reason);
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            }
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    com.android.server.inputmethod.ImeVisibilityApplier getVisibilityApplier() {
        com.android.server.inputmethod.DefaultImeVisibilityApplier defaultImeVisibilityApplier;
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            defaultImeVisibilityApplier = this.mVisibilityApplier;
        }
        return defaultImeVisibilityApplier;
    }

    void onApplyImeVisibilityFromComputer(android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult result) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mVisibilityApplier.applyImeVisibility(windowToken, statsToken, result.getState(), result.getReason(), this.mCurrentUserId);
        }
    }

    void setEnabledSessionLocked(com.android.server.inputmethod.InputMethodManagerService.SessionState session) {
        if (this.mEnabledSession != session) {
            if (this.mEnabledSession != null && this.mEnabledSession.mSession != null) {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Disabling: " + this.mEnabledSession);
                }
                this.mEnabledSession.mMethod.setSessionEnabled(this.mEnabledSession.mSession, false);
            }
            this.mEnabledSession = session;
            if (this.mEnabledSession != null && this.mEnabledSession.mSession != null) {
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Enabling: " + this.mEnabledSession);
                }
                this.mEnabledSession.mMethod.setSessionEnabled(this.mEnabledSession.mSession, true);
            }
        }
    }

    void setEnabledSessionForAccessibilityLocked(android.util.SparseArray<com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState> accessibilitySessions) {
        com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState sessionState;
        com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState sessionState2;
        android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> disabledSessions = new android.util.SparseArray<>();
        for (int i = 0; i < this.mEnabledAccessibilitySessions.size(); i++) {
            if (!accessibilitySessions.contains(this.mEnabledAccessibilitySessions.keyAt(i)) && (sessionState2 = this.mEnabledAccessibilitySessions.valueAt(i)) != null) {
                disabledSessions.append(this.mEnabledAccessibilitySessions.keyAt(i), sessionState2.mSession);
            }
        }
        int i2 = disabledSessions.size();
        if (i2 > 0) {
            com.android.server.AccessibilityManagerInternal.get().setImeSessionEnabled(disabledSessions, false);
        }
        android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> enabledSessions = new android.util.SparseArray<>();
        for (int i3 = 0; i3 < accessibilitySessions.size(); i3++) {
            if (!this.mEnabledAccessibilitySessions.contains(accessibilitySessions.keyAt(i3)) && (sessionState = accessibilitySessions.valueAt(i3)) != null) {
                enabledSessions.append(accessibilitySessions.keyAt(i3), sessionState.mSession);
            }
        }
        int i4 = enabledSessions.size();
        if (i4 > 0) {
            com.android.server.AccessibilityManagerInternal.get().setImeSessionEnabled(enabledSessions, true);
        }
        this.mEnabledAccessibilitySessions = accessibilitySessions;
    }

    /* JADX WARN: Removed duplicated region for block: B:175:0x0226  */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean handleMessage(android.os.Message r19) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 730
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.handleMessage(android.os.Message):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStylusHandwritingReady(int requestId, int pid) {
        this.mHandler.obtainMessage(MSG_START_HANDWRITING, requestId, pid).sendToTarget();
    }

    private void handleSetInteractive(boolean interactive) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mIsInteractive = interactive;
            updateSystemUiLocked(interactive ? this.mImeWindowVis : 0, this.mBackDisposition);
            if (this.mCurClient != null && this.mCurClient.mClient != null) {
                com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
                if (this.mImePlatformCompatUtils.shouldUseSetInteractiveProtocol(bindingController.getCurMethodUid())) {
                    com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult imeVisRes = this.mVisibilityStateComputer.onInteractiveChanged(this.mImeBindingState.mFocusedWindow, interactive);
                    if (imeVisRes != null) {
                        this.mVisibilityApplier.applyImeVisibility(this.mImeBindingState.mFocusedWindow, null, imeVisRes.getState(), imeVisRes.getReason(), this.mCurrentUserId);
                    }
                    this.mCurClient.mClient.setInteractive(this.mIsInteractive, this.mInFullscreenMode);
                } else {
                    this.mCurClient.mClient.setActive(this.mIsInteractive, this.mInFullscreenMode);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean chooseNewDefaultIMELocked() {
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
        android.view.inputmethod.InputMethodInfo imi1 = com.android.server.inputmethod.InputMethodInfoUtils.getMostApplicableDefaultIME(settings.getEnabledInputMethodList());
        android.view.inputmethod.InputMethodInfo imi2 = this.mImmsWrapper.getExtImpl().getDefaultInputMethodByConfig(settings.getUserId());
        android.view.inputmethod.InputMethodInfo imi = imi2 != null ? imi2 : imi1;
        if (imi != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "New default IME was selected: " + imi.getId());
            }
            resetSelectedInputMethodAndSubtypeLocked(imi.getId());
            return true;
        }
        return false;
    }

    static com.android.server.inputmethod.InputMethodSettings queryInputMethodServicesInternal(android.content.Context context, int userId, com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap, int directBootAwareness) {
        android.content.Context userAwareContext;
        int directBootAwarenessFlags;
        if (context.getUserId() == userId) {
            userAwareContext = context;
        } else {
            userAwareContext = context.createContextAsUser(android.os.UserHandle.of(userId), 0);
        }
        switch (directBootAwareness) {
            case 0:
                directBootAwarenessFlags = 268435456;
                break;
            case 1:
                directBootAwarenessFlags = com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED;
                break;
            default:
                directBootAwarenessFlags = 268435456;
                android.util.Slog.e(TAG, "Unknown directBootAwareness=" + directBootAwareness + ". Falling back to DirectBootAwareness.AUTO");
                break;
        }
        int flags = 32896 | directBootAwarenessFlags | 1073741824;
        java.util.List<android.content.pm.ResolveInfo> services = userAwareContext.getPackageManager().queryIntentServices(new android.content.Intent("android.view.InputMethod"), android.content.pm.PackageManager.ResolveInfoFlags.of(flags));
        java.util.List<java.lang.String> enabledInputMethodList = com.android.server.inputmethod.InputMethodUtils.getEnabledInputMethodIdsForFiltering(context, userId);
        com.android.server.inputmethod.InputMethodMap methodMap = filterInputMethodServices(additionalSubtypeMap, enabledInputMethodList, userAwareContext, services);
        return com.android.server.inputmethod.InputMethodSettings.create(methodMap, userId);
    }

    static com.android.server.inputmethod.InputMethodMap filterInputMethodServices(com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap, java.util.List<java.lang.String> enabledInputMethodList, android.content.Context userAwareContext, java.util.List<android.content.pm.ResolveInfo> services) {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> imiPackageCount = new android.util.ArrayMap<>();
        android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodInfo> methodMap = new android.util.ArrayMap<>(services.size());
        for (int i = 0; i < services.size(); i++) {
            android.content.pm.ResolveInfo ri = services.get(i);
            android.content.pm.ServiceInfo si = ri.serviceInfo;
            java.lang.String imeId = android.view.inputmethod.InputMethodInfo.computeId(ri);
            if (!"android.permission.BIND_INPUT_METHOD".equals(si.permission)) {
                android.util.Slog.w(TAG, "Skipping input method " + imeId + ": it does not require the permission android.permission.BIND_INPUT_METHOD");
            } else if (!sImmsStaticExt.shouldHideInputMethodService(si.packageName, imeId)) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Checking " + imeId);
                }
                try {
                    android.view.inputmethod.InputMethodInfo imi = new android.view.inputmethod.InputMethodInfo(userAwareContext, ri, additionalSubtypeMap.get(imeId));
                    if (!imi.isVrOnly()) {
                        java.lang.String packageName = si.packageName;
                        if (si.applicationInfo.isSystemApp() || enabledInputMethodList.contains(imi.getId()) || imiPackageCount.getOrDefault(packageName, 0).intValue() < 20) {
                            imiPackageCount.put(packageName, java.lang.Integer.valueOf(imiPackageCount.getOrDefault(packageName, 0).intValue() + 1));
                            methodMap.put(imi.getId(), imi);
                            if (DEBUG) {
                                android.util.Slog.d(TAG, "Found an input method " + imi);
                            }
                        } else if (DEBUG) {
                            android.util.Slog.d(TAG, "Found an input method, but ignored due threshold: " + imi);
                        }
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.wtf(TAG, "Unable to load input method " + imeId, e);
                }
            }
        }
        return com.android.server.inputmethod.InputMethodMap.of(methodMap);
    }

    void postInputMethodSettingUpdatedLocked(boolean resetDefaultEnabledIme) throws java.lang.Throwable {
        if (DEBUG) {
            android.util.Slog.d(TAG, "--- re-buildInputMethodList reset = " + resetDefaultEnabledIme + " \n ------ caller=" + android.os.Debug.getCallers(10));
        }
        if (!this.mSystemReady) {
            android.util.Slog.e(TAG, "buildInputMethodListLocked is not allowed until system is ready");
            return;
        }
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        this.mImmsWrapper.getExtImpl().configInputMethodAfterQuery();
        boolean reenableMinimumNonAuxSystemImes = false;
        if (!resetDefaultEnabledIme) {
            boolean enabledImeFound = false;
            boolean enabledNonAuxImeFound = false;
            java.util.List<android.view.inputmethod.InputMethodInfo> enabledImes = settings.getEnabledInputMethodList();
            int numImes = enabledImes.size();
            int i = 0;
            while (true) {
                if (i >= numImes) {
                    break;
                }
                android.view.inputmethod.InputMethodInfo imi = enabledImes.get(i);
                if (settings.getMethodMap().containsKey(imi.getId())) {
                    enabledImeFound = true;
                    if (!imi.isAuxiliaryIme()) {
                        enabledNonAuxImeFound = true;
                        break;
                    }
                }
                i++;
            }
            if (!enabledImeFound) {
                if (DEBUG) {
                    android.util.Slog.i(TAG, "All the enabled IMEs are gone. Reset default enabled IMEs.");
                }
                resetDefaultEnabledIme = true;
                resetSelectedInputMethodAndSubtypeLocked("");
            } else if (!enabledNonAuxImeFound) {
                if (DEBUG) {
                    android.util.Slog.i(TAG, "All the enabled non-Aux IMEs are gone. Do partial reset.");
                }
                reenableMinimumNonAuxSystemImes = true;
            }
        }
        if (resetDefaultEnabledIme || reenableMinimumNonAuxSystemImes) {
            java.util.ArrayList<android.view.inputmethod.InputMethodInfo> defaultEnabledIme = com.android.server.inputmethod.InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, settings.getMethodList(), reenableMinimumNonAuxSystemImes);
            this.mImmsWrapper.getExtImpl().updateDefaultEnabledImes(defaultEnabledIme);
            int numImes2 = defaultEnabledIme.size();
            for (int i2 = 0; i2 < numImes2; i2++) {
                android.view.inputmethod.InputMethodInfo imi2 = defaultEnabledIme.get(i2);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "--- enable ime = " + imi2);
                }
                setInputMethodEnabledLocked(imi2.getId(), true);
            }
        }
        java.lang.String defaultImiId = settings.getSelectedInputMethod();
        if (!android.text.TextUtils.isEmpty(defaultImiId)) {
            if (!settings.getMethodMap().containsKey(defaultImiId)) {
                android.util.Slog.w(TAG, "Default IME is uninstalled. Choose new default IME.");
                if (chooseNewDefaultIMELocked()) {
                    updateInputMethodsFromSettingsLocked(true);
                }
            } else {
                setInputMethodEnabledLocked(defaultImiId, true);
            }
        }
        updateDefaultVoiceImeIfNeededLocked();
        if (userId == this.mSwitchingController.getUserId()) {
            this.mSwitchingController.resetCircularListLocked(settings.getMethodMap());
        } else {
            this.mSwitchingController = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.createInstanceLocked(this.mContext, settings.getMethodMap(), this.mCurrentUserId);
        }
        if (userId == this.mHardwareKeyboardShortcutController.getUserId()) {
            this.mHardwareKeyboardShortcutController.reset(settings.getMethodMap());
        } else {
            this.mHardwareKeyboardShortcutController = new com.android.server.inputmethod.HardwareKeyboardShortcutController(settings.getMethodMap(), userId);
        }
        sendOnNavButtonFlagsChangedLocked();
        java.util.List<android.view.inputmethod.InputMethodInfo> inputMethodList = settings.getMethodList();
        this.mHandler.obtainMessage(MSG_DISPATCH_ON_INPUT_METHOD_LIST_UPDATED, userId, 0, inputMethodList).sendToTarget();
    }

    void sendOnNavButtonFlagsChangedLocked() {
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        com.android.server.inputmethod.IInputMethodInvoker curMethod = bindingController.getCurMethod();
        if (curMethod == null) {
            return;
        }
        curMethod.onNavButtonFlagsChanged(getInputMethodNavButtonFlagsLocked());
    }

    private void updateDefaultVoiceImeIfNeededLocked() {
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
        java.lang.String systemSpeechRecognizer = this.mContext.getString(android.R.string.config_systemSpeechRecognizer);
        java.lang.String currentDefaultVoiceImeId = settings.getDefaultVoiceInputMethod();
        android.view.inputmethod.InputMethodInfo newSystemVoiceIme = com.android.server.inputmethod.InputMethodInfoUtils.chooseSystemVoiceIme(settings.getMethodMap(), systemSpeechRecognizer, currentDefaultVoiceImeId);
        if (newSystemVoiceIme == null) {
            if (DEBUG) {
                android.util.Slog.i(TAG, "Found no valid default Voice IME. If the user is still locked, this may be expected.");
            }
            if (!android.text.TextUtils.isEmpty(currentDefaultVoiceImeId)) {
                settings.putDefaultVoiceInputMethod("");
                return;
            }
            return;
        }
        if (android.text.TextUtils.equals(currentDefaultVoiceImeId, newSystemVoiceIme.getId())) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "Enabling the default Voice IME:" + newSystemVoiceIme);
        }
        setInputMethodEnabledLocked(newSystemVoiceIme.getId(), true);
        settings.putDefaultVoiceInputMethod(newSystemVoiceIme.getId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setInputMethodEnabledLocked(java.lang.String id, boolean enabled) {
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        if (enabled) {
            java.lang.String enabledImeIdsStr = settings.getEnabledInputMethodsStr();
            java.lang.String newEnabledImeIdsStr = com.android.server.inputmethod.InputMethodUtils.concatEnabledImeIds(enabledImeIdsStr, id);
            if (android.text.TextUtils.equals(enabledImeIdsStr, newEnabledImeIdsStr)) {
                return true;
            }
            settings.putEnabledInputMethodsStr(newEnabledImeIdsStr);
            return false;
        }
        java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> enabledInputMethodsList = settings.getEnabledInputMethodsAndSubtypeList();
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        if (!settings.buildAndPutEnabledInputMethodsStrRemovingId(builder, enabledInputMethodsList, id)) {
            return false;
        }
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        if (bindingController.getDeviceIdToShowIme() == 0) {
            java.lang.String selId = settings.getSelectedInputMethod();
            if (id.equals(selId) && !chooseNewDefaultIMELocked()) {
                android.util.Slog.i(TAG, "Can't find new IME, unsetting the current input method.");
                resetSelectedInputMethodAndSubtypeLocked("");
            }
        } else {
            java.lang.String selId2 = settings.getSelectedDefaultDeviceInputMethod();
            if (id.equals(selId2)) {
                android.view.inputmethod.InputMethodInfo newDefaultIme = com.android.server.inputmethod.InputMethodInfoUtils.getMostApplicableDefaultIME(settings.getEnabledInputMethodList());
                settings.putSelectedDefaultDeviceInputMethod(newDefaultIme == null ? null : newDefaultIme.getId());
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectedInputMethodAndSubtypeLocked(android.view.inputmethod.InputMethodInfo imi, int subtypeId, boolean setSubtypeOnly) {
        int newSubtypeHashcode;
        android.view.inputmethod.InputMethodSubtype subtype;
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        settings.saveCurrentInputMethodAndSubtypeToHistory(getSelectedMethodIdLocked(), bindingController.getCurrentSubtype());
        if (imi == null || subtypeId < 0) {
            newSubtypeHashcode = -1;
            subtype = null;
        } else if (subtypeId < imi.getSubtypeCount()) {
            subtype = imi.getSubtypeAt(subtypeId);
            newSubtypeHashcode = subtype.hashCode();
        } else {
            newSubtypeHashcode = -1;
            subtype = getCurrentInputMethodSubtypeLocked();
        }
        settings.putSelectedSubtype(newSubtypeHashcode);
        bindingController.setCurrentSubtype(subtype);
        notifyInputMethodSubtypeChangedLocked(settings.getUserId(), imi, subtype);
        if (!setSubtypeOnly) {
            settings.putSelectedInputMethod(imi != null ? imi.getId() : "");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSelectedInputMethodAndSubtypeLocked(java.lang.String newDefaultIme) {
        java.lang.String subtypeHashCode;
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
        bindingController.setDisplayIdToShowIme(-1);
        bindingController.setDeviceIdToShowIme(0);
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
        settings.putSelectedDefaultDeviceInputMethod(null);
        android.view.inputmethod.InputMethodInfo imi = settings.getMethodMap().get(newDefaultIme);
        int lastSubtypeId = -1;
        if (imi != null && !android.text.TextUtils.isEmpty(newDefaultIme) && (subtypeHashCode = settings.getLastSubtypeForInputMethod(newDefaultIme)) != null) {
            try {
                lastSubtypeId = com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(imi, java.lang.Integer.parseInt(subtypeHashCode));
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.w(TAG, "HashCode for subtype looks broken: " + subtypeHashCode, e);
            }
        }
        setSelectedInputMethodAndSubtypeLocked(imi, lastSubtypeId, false);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtype(int userId) {
        if (android.os.UserHandle.getCallingUserId() != userId) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", null);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (this.mCurrentUserId == userId) {
                return getCurrentInputMethodSubtypeLocked();
            }
            return com.android.server.inputmethod.InputMethodSettingsRepository.get(userId).getCurrentInputMethodSubtypeForNonCurrentUsers();
        }
    }

    android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtypeLocked() {
        int userId = this.mCurrentUserId;
        java.lang.String selectedMethodId = getInputMethodBindingController(userId).getSelectedMethodId();
        if (selectedMethodId == null) {
            return null;
        }
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        android.view.inputmethod.InputMethodInfo imi = settings.getMethodMap().get(selectedMethodId);
        if (imi == null || imi.getSubtypeCount() == 0) {
            return null;
        }
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        android.view.inputmethod.InputMethodSubtype subtype = com.android.server.inputmethod.SubtypeUtils.getCurrentInputMethodSubtype(imi, settings, bindingController.getCurrentSubtype());
        bindingController.setCurrentSubtype(subtype);
        return subtype;
    }

    private android.view.inputmethod.InputMethodInfo queryDefaultInputMethodForUserIdLocked(int userId) {
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        return settings.getMethodMap().get(settings.getSelectedInputMethod());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean switchToInputMethodLocked(java.lang.String imeId, int userId) throws java.lang.Throwable {
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        if (userId == this.mCurrentUserId) {
            if (!settings.getMethodMap().containsKey(imeId) || !settings.getEnabledInputMethodList().contains(settings.getMethodMap().get(imeId))) {
                return false;
            }
            setInputMethodLocked(imeId, -1);
            return true;
        }
        if (!settings.getMethodMap().containsKey(imeId) || !settings.getEnabledInputMethodList().contains(settings.getMethodMap().get(imeId))) {
            return false;
        }
        settings.putSelectedInputMethod(imeId);
        settings.putSelectedSubtype(-1);
        return true;
    }

    private boolean canCallerAccessInputMethod(java.lang.String targetPkgName, int callingUid, int userId, com.android.server.inputmethod.InputMethodSettings settings) {
        java.lang.String methodId = settings.getSelectedInputMethod();
        android.content.ComponentName selectedInputMethod = methodId != null ? com.android.server.inputmethod.InputMethodUtils.convertIdToComponentName(methodId) : null;
        if (selectedInputMethod != null && selectedInputMethod.getPackageName().equals(targetPkgName)) {
            return true;
        }
        if (this.mImmsWrapper.getExtImpl().isInputMethodAccessible(targetPkgName)) {
            boolean canAccess = true ^ this.mPackageManagerInternal.filterAppAccess(targetPkgName, callingUid, userId);
            if (DEBUG && !canAccess) {
                android.util.Slog.d(TAG, "Input method " + targetPkgName + " is not visible to the caller " + callingUid);
            }
            return canAccess;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchKeyboardLayoutLocked(int direction) throws java.lang.Throwable {
        android.view.inputmethod.InputMethodInfo nextImi;
        int userId = this.mCurrentUserId;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        android.view.inputmethod.InputMethodInfo currentImi = settings.getMethodMap().get(getSelectedMethodIdLocked());
        if (currentImi == null) {
            return;
        }
        com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
        com.android.internal.inputmethod.InputMethodSubtypeHandle currentSubtypeHandle = com.android.internal.inputmethod.InputMethodSubtypeHandle.of(currentImi, bindingController.getCurrentSubtype());
        com.android.internal.inputmethod.InputMethodSubtypeHandle nextSubtypeHandle = this.mHardwareKeyboardShortcutController.onSubtypeSwitch(currentSubtypeHandle, direction > 0);
        if (nextSubtypeHandle == null || (nextImi = settings.getMethodMap().get(nextSubtypeHandle.getImeId())) == null) {
            return;
        }
        int subtypeCount = nextImi.getSubtypeCount();
        if (subtypeCount == 0) {
            if (nextSubtypeHandle.equals(com.android.internal.inputmethod.InputMethodSubtypeHandle.of(nextImi, (android.view.inputmethod.InputMethodSubtype) null))) {
                setInputMethodLocked(nextImi.getId(), -1);
            }
        } else {
            for (int i = 0; i < subtypeCount; i++) {
                if (nextSubtypeHandle.equals(com.android.internal.inputmethod.InputMethodSubtypeHandle.of(nextImi, nextImi.getSubtypeAt(i)))) {
                    setInputMethodLocked(nextImi.getId(), i);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishLocalService() {
        com.android.server.LocalServices.addService(com.android.server.inputmethod.InputMethodManagerInternal.class, new com.android.server.inputmethod.InputMethodManagerService.LocalServiceImpl());
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class LocalServiceImpl extends com.android.server.inputmethod.InputMethodManagerInternal {
        private LocalServiceImpl() {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void setInteractive(boolean z) {
            com.android.server.inputmethod.InputMethodManagerService.sImmsStaticExt.logMethodCallers(com.android.server.inputmethod.InputMethodManagerService.TAG, "setInteractive = " + z);
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.obtainMessage(com.android.server.inputmethod.InputMethodManagerService.MSG_SET_INTERACTIVE, z ? 1 : 0, 0).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void hideAllInputMethods(int reason, int originatingDisplayId) {
            com.android.server.inputmethod.InputMethodManagerService.sImmsStaticExt.logMethodCallers(com.android.server.inputmethod.InputMethodManagerService.TAG, "hideAllInputMethods");
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.removeMessages(1035);
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.obtainMessage(1035, java.lang.Integer.valueOf(reason)).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListAsUser(int userId) {
            java.util.List<android.view.inputmethod.InputMethodInfo> inputMethodListLocked;
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                inputMethodListLocked = com.android.server.inputmethod.InputMethodManagerService.this.getInputMethodListLocked(userId, 0, 1000);
            }
            return inputMethodListLocked;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListAsUser(int userId) {
            java.util.List<android.view.inputmethod.InputMethodInfo> enabledInputMethodListLocked;
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                enabledInputMethodListLocked = com.android.server.inputmethod.InputMethodManagerService.this.getEnabledInputMethodListLocked(userId, 1000);
            }
            return enabledInputMethodListLocked;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onCreateInlineSuggestionsRequest(int userId, com.android.internal.inputmethod.InlineSuggestionsRequestInfo requestInfo, com.android.internal.inputmethod.InlineSuggestionsRequestCallback cb) {
            boolean touchExplorationEnabled = com.android.server.AccessibilityManagerInternal.get().isTouchExplorationEnabled(userId);
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.InputMethodManagerService.this.getInputMethodBindingController(userId).onCreateInlineSuggestionsRequest(requestInfo, cb, touchExplorationEnabled);
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean switchToInputMethod(java.lang.String imeId, int userId) {
            boolean zSwitchToInputMethodLocked;
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                zSwitchToInputMethodLocked = com.android.server.inputmethod.InputMethodManagerService.this.switchToInputMethodLocked(imeId, userId);
            }
            return zSwitchToInputMethodLocked;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean setInputMethodEnabled(java.lang.String imeId, boolean enabled, int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
                if (!settings.getMethodMap().containsKey(imeId)) {
                    return false;
                }
                if (userId == com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId) {
                    com.android.server.inputmethod.InputMethodManagerService.this.setInputMethodEnabledLocked(imeId, enabled);
                    return true;
                }
                if (enabled) {
                    java.lang.String enabledImeIdsStr = settings.getEnabledInputMethodsStr();
                    java.lang.String newEnabledImeIdsStr = com.android.server.inputmethod.InputMethodUtils.concatEnabledImeIds(enabledImeIdsStr, imeId);
                    if (!android.text.TextUtils.equals(enabledImeIdsStr, newEnabledImeIdsStr)) {
                        settings.putEnabledInputMethodsStr(newEnabledImeIdsStr);
                    }
                } else {
                    settings.buildAndPutEnabledInputMethodsStrRemovingId(new java.lang.StringBuilder(), settings.getEnabledInputMethodsAndSubtypeList(), imeId);
                }
                return true;
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void setVirtualDeviceInputMethodForAllUsers(int deviceId, java.lang.String imeId) {
            com.android.internal.util.Preconditions.checkArgument(deviceId != 0, android.text.TextUtils.formatSimple("DeviceId %d is not a virtual device id.", new java.lang.Object[]{java.lang.Integer.valueOf(deviceId)}));
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (imeId == null) {
                    com.android.server.inputmethod.InputMethodManagerService.this.mVirtualDeviceMethodMap.remove(deviceId);
                } else {
                    if (com.android.server.inputmethod.InputMethodManagerService.this.mVirtualDeviceMethodMap.contains(deviceId)) {
                        throw new java.lang.IllegalArgumentException("Virtual device " + deviceId + " already has a custom input method component");
                    }
                    com.android.server.inputmethod.InputMethodManagerService.this.mVirtualDeviceMethodMap.put(deviceId, imeId);
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void registerInputMethodListListener(com.android.server.inputmethod.InputMethodManagerInternal.InputMethodListListener listener) {
            com.android.server.inputmethod.InputMethodManagerService.this.mInputMethodListListeners.addIfAbsent(listener);
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean transferTouchFocusToImeWindow(android.os.IBinder sourceInputToken, int displayId, int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (displayId != com.android.server.inputmethod.InputMethodManagerService.this.getCurTokenDisplayIdLocked()) {
                    return false;
                }
                android.os.IBinder curHostInputToken = com.android.server.inputmethod.InputMethodManagerService.this.getInputMethodBindingController(userId).getCurHostInputToken();
                if (curHostInputToken == null) {
                    return false;
                }
                return com.android.server.inputmethod.InputMethodManagerService.this.mInputManagerInternal.transferTouchGesture(sourceInputToken, curHostInputToken);
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void reportImeControl(android.os.IBinder windowToken) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (com.android.server.inputmethod.InputMethodManagerService.this.mImeBindingState.mFocusedWindow != windowToken) {
                    com.android.server.inputmethod.InputMethodManagerService.this.mFocusedWindowPerceptible.put(windowToken, true);
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onImeParentChanged(int displayId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (com.android.server.inputmethod.InputMethodManagerService.this.mLastImeTargetWindow != com.android.server.inputmethod.InputMethodManagerService.this.mImeBindingState.mFocusedWindow) {
                    com.android.server.inputmethod.InputMethodManagerService.this.mMenuController.hideInputMethodMenuLocked();
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void removeImeSurface(int displayId) {
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.obtainMessage(com.android.server.inputmethod.InputMethodManagerService.MSG_REMOVE_IME_SURFACE).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void updateImeWindowStatus(boolean z, int i) {
            com.android.server.inputmethod.InputMethodManagerService.sImmsStaticExt.logMethodCallers(com.android.server.inputmethod.InputMethodManagerService.TAG, "updateImeWindowStatus = " + z);
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.obtainMessage(1070, z ? 1 : 0, 0).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onSessionForAccessibilityCreated(int accessibilityConnectionId, com.android.internal.inputmethod.IAccessibilityInputMethodSession session, int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.InputMethodBindingController bindingController = com.android.server.inputmethod.InputMethodManagerService.this.getInputMethodBindingController(com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId);
                if (com.android.server.inputmethod.InputMethodManagerService.this.mCurClient != null) {
                    com.android.server.inputmethod.InputMethodManagerService.this.clearClientSessionForAccessibilityLocked(com.android.server.inputmethod.InputMethodManagerService.this.mCurClient, accessibilityConnectionId);
                    com.android.server.inputmethod.InputMethodManagerService.this.mCurClient.mAccessibilitySessions.put(accessibilityConnectionId, new com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState(com.android.server.inputmethod.InputMethodManagerService.this.mCurClient, accessibilityConnectionId, session));
                    com.android.server.inputmethod.InputMethodManagerService.this.attachNewAccessibilityLocked(11, true);
                    com.android.server.inputmethod.InputMethodManagerService.SessionState sessionState = com.android.server.inputmethod.InputMethodManagerService.this.mCurClient.mCurSession;
                    com.android.internal.inputmethod.IInputMethodSession imeSession = sessionState == null ? null : sessionState.mSession;
                    android.util.SparseArray<com.android.internal.inputmethod.IAccessibilityInputMethodSession> accessibilityInputMethodSessions = com.android.server.inputmethod.InputMethodManagerService.this.createAccessibilityInputMethodSessions(com.android.server.inputmethod.InputMethodManagerService.this.mCurClient.mAccessibilitySessions);
                    com.android.internal.inputmethod.InputBindResult res = new com.android.internal.inputmethod.InputBindResult(16, imeSession, accessibilityInputMethodSessions, (android.view.InputChannel) null, bindingController.getCurId(), bindingController.getSequenceNumber(), false);
                    com.android.server.inputmethod.InputMethodManagerService.this.mCurClient.mClient.onBindAccessibilityService(res, accessibilityConnectionId);
                }
            }
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void unbindAccessibilityFromCurrentClient(final int accessibilityConnectionId, int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.InputMethodBindingController bindingController = com.android.server.inputmethod.InputMethodManagerService.this.getInputMethodBindingController(com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId);
                if (com.android.server.inputmethod.InputMethodManagerService.this.mCurClient != null) {
                    if (com.android.server.inputmethod.InputMethodManagerService.DEBUG) {
                        android.util.Slog.v(com.android.server.inputmethod.InputMethodManagerService.TAG, "unbindAccessibilityFromCurrentClientLocked: client=" + com.android.server.inputmethod.InputMethodManagerService.this.mCurClient.mClient.asBinder());
                    }
                    com.android.server.inputmethod.InputMethodManagerService.this.mCurClient.mClient.onUnbindAccessibilityService(bindingController.getSequenceNumber(), accessibilityConnectionId);
                }
                if (com.android.server.inputmethod.InputMethodManagerService.this.getCurMethodLocked() != null) {
                    java.util.function.Consumer<com.android.server.inputmethod.ClientState> clearClientSession = new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$LocalServiceImpl$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$unbindAccessibilityFromCurrentClient$0(accessibilityConnectionId, (com.android.server.inputmethod.ClientState) obj);
                        }
                    };
                    com.android.server.inputmethod.InputMethodManagerService.this.mClientController.forAllClients(clearClientSession);
                    com.android.server.inputmethod.InputMethodManagerService.AccessibilitySessionState session = com.android.server.inputmethod.InputMethodManagerService.this.mEnabledAccessibilitySessions.get(accessibilityConnectionId);
                    if (session != null) {
                        com.android.server.inputmethod.InputMethodManagerService.this.finishSessionForAccessibilityLocked(session);
                        com.android.server.inputmethod.InputMethodManagerService.this.mEnabledAccessibilitySessions.remove(accessibilityConnectionId);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$unbindAccessibilityFromCurrentClient$0(int accessibilityConnectionId, com.android.server.inputmethod.ClientState c) {
            com.android.server.inputmethod.InputMethodManagerService.this.clearClientSessionForAccessibilityLocked(c, accessibilityConnectionId);
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void maybeFinishStylusHandwriting() {
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.removeMessages(com.android.server.inputmethod.InputMethodManagerService.MSG_FINISH_HANDWRITING);
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.obtainMessage(com.android.server.inputmethod.InputMethodManagerService.MSG_FINISH_HANDWRITING).sendToTarget();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onSwitchKeyboardLayoutShortcut(int direction, int displayId, android.os.IBinder targetWindowToken) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.InputMethodManagerService.this.switchKeyboardLayoutLocked(direction);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.internal.inputmethod.IInputContentUriToken createInputContentUriToken(android.os.IBinder token, android.net.Uri contentUri, java.lang.String packageName) {
        if (token == null) {
            throw new java.lang.NullPointerException("token");
        }
        if (packageName == null) {
            throw new java.lang.NullPointerException(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME);
        }
        if (contentUri == null) {
            throw new java.lang.NullPointerException("contentUri");
        }
        java.lang.String contentUriScheme = contentUri.getScheme();
        if (!com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(contentUriScheme)) {
            throw new java.security.InvalidParameterException("contentUri must have content scheme");
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            int uid = android.os.Binder.getCallingUid();
            int imeUserId = android.os.UserHandle.getUserId(uid);
            if (imeUserId != this.mCurrentUserId) {
                android.util.Slog.i(TAG, "Ignoring createInputContentUriToken due to user ID mismatch. imeUserId=" + imeUserId + " mCurrentUserId=" + this.mCurrentUserId);
                return null;
            }
            com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(imeUserId);
            if (bindingController.getSelectedMethodId() == null) {
                return null;
            }
            if (bindingController.getCurToken() != token) {
                android.util.Slog.e(TAG, "Ignoring createInputContentUriToken mCurToken=" + bindingController.getCurToken() + " token=" + token);
                return null;
            }
            java.lang.String curPackageName = this.mCurEditorInfo != null ? this.mCurEditorInfo.packageName : null;
            if (!android.text.TextUtils.equals(curPackageName, packageName)) {
                android.util.Slog.e(TAG, "Ignoring createInputContentUriToken mCurEditorInfo.packageName=" + curPackageName + " packageName=" + packageName);
                return null;
            }
            int appUserId = android.os.UserHandle.getUserId(this.mCurClient.mUid);
            int contentUriOwnerUserId = android.content.ContentProvider.getUserIdFromUri(contentUri, imeUserId);
            android.net.Uri contentUriWithoutUserId = android.content.ContentProvider.getUriWithoutUserId(contentUri);
            return new com.android.server.inputmethod.InputContentUriTokenHandler(contentUriWithoutUserId, uid, packageName, contentUriOwnerUserId, appUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportFullscreenMode(android.os.IBinder token, boolean fullscreen) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (calledWithValidTokenLocked(token)) {
                if (this.mCurClient != null && this.mCurClient.mClient != null) {
                    this.mInFullscreenMode = fullscreen;
                    this.mCurClient.mClient.reportFullscreenMode(fullscreen);
                }
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            this.mImmsWrapper.getExtImpl().configDebug(fd, args);
            com.android.server.utils.PriorityDump.dump(this.mPriorityDumper, fd, pw, args);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpAsStringNoCheck(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean isCritical) {
        com.android.server.inputmethod.ClientState client;
        com.android.server.inputmethod.IInputMethodInvoker method;
        final android.util.Printer p = new android.util.PrintWriterPrinter(pw);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mCurrentUserId);
            p.println("Current Input Method Manager state:");
            java.util.List<android.view.inputmethod.InputMethodInfo> methodList = settings.getMethodList();
            int numImes = methodList.size();
            p.println("  Input Methods:");
            for (int i = 0; i < numImes; i++) {
                android.view.inputmethod.InputMethodInfo info = methodList.get(i);
                p.println("  InputMethod #" + i + ":");
                info.dump(p, "    ");
            }
            p.println("  ClientStates:");
            java.util.function.Consumer<com.android.server.inputmethod.ClientState> clientControllerDump = new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda19
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.inputmethod.InputMethodManagerService.lambda$dumpAsStringNoCheck$17(p, (com.android.server.inputmethod.ClientState) obj);
                }
            };
            this.mClientController.forAllClients(clientControllerDump);
            com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(this.mCurrentUserId);
            p.println("  mCurrentUserId=" + this.mCurrentUserId);
            p.println("  mCurMethodId=" + getSelectedMethodIdLocked());
            client = this.mCurClient;
            p.println("  mCurClient=" + client + " mCurSeq=" + bindingController.getSequenceNumber());
            p.println("  mFocusedWindowPerceptible=" + this.mFocusedWindowPerceptible);
            this.mImeBindingState.dump("  ", p);
            p.println("  mCurId=" + bindingController.getCurId() + " mHaveConnection=" + bindingController.hasMainConnection() + " mBoundToMethod=" + this.mBoundToMethod + " mVisibleBound=" + bindingController.isVisibleBound());
            p.println("  mUserDataRepository=");
            java.util.function.Consumer<com.android.server.inputmethod.UserDataRepository.UserData> userDataDump = new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda20
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.inputmethod.InputMethodManagerService.lambda$dumpAsStringNoCheck$18(p, (com.android.server.inputmethod.UserDataRepository.UserData) obj);
                }
            };
            this.mUserDataRepository.forAllUserData(userDataDump);
            p.println("  mCurToken=" + getCurTokenLocked());
            p.println("  mCurTokenDisplayId=" + getCurTokenDisplayIdLocked());
            p.println("  mCurHostInputToken=" + bindingController.getCurHostInputToken());
            p.println("  mCurIntent=" + bindingController.getCurIntent());
            method = getCurMethodLocked();
            p.println("  mCurMethod=" + getCurMethodLocked());
            p.println("  mEnabledSession=" + this.mEnabledSession);
            this.mVisibilityStateComputer.dump(pw, "  ");
            p.println("  mInFullscreenMode=" + this.mInFullscreenMode);
            p.println("  mSystemReady=" + this.mSystemReady + " mInteractive=" + this.mIsInteractive);
            p.println("  mExperimentalConcurrentMultiUserModeEnabled=" + this.mExperimentalConcurrentMultiUserModeEnabled);
            p.println("  ENABLE_HIDE_IME_CAPTION_BAR=true");
            p.println("  mSettingsObserver=" + this.mSettingsObserver);
            p.println("  mStylusIds=" + (this.mStylusIds != null ? java.util.Arrays.toString(this.mStylusIds.toArray()) : ""));
            p.println("  mSwitchingController:");
            this.mSwitchingController.dump(p, "    ");
            p.println("  mStartInputHistory:");
            this.mStartInputHistory.dump(pw, "    ");
            p.println("  mSoftInputShowHideHistory:");
            this.mSoftInputShowHideHistory.dump(pw, "    ");
            p.println("  mImeTrackerService#History:");
            this.mImeTrackerService.dump(pw, "    ");
        }
        if (isCritical) {
            return;
        }
        p.println(" ");
        if (client != null) {
            pw.flush();
            try {
                com.android.internal.os.TransferPipe.dumpAsync(client.mClient.asBinder(), fd, args);
            } catch (android.os.RemoteException | java.io.IOException e) {
                p.println("Failed to dump input method client: " + e);
            }
        } else {
            p.println("No input method client.");
        }
        if (this.mImeBindingState.mFocusedWindowClient != null && client != this.mImeBindingState.mFocusedWindowClient) {
            p.println(" ");
            p.println("Warning: Current input method client doesn't match the last focused. window.");
            p.println("Dumping input method client in the last focused window just in case.");
            p.println(" ");
            pw.flush();
            try {
                com.android.internal.os.TransferPipe.dumpAsync(this.mImeBindingState.mFocusedWindowClient.mClient.asBinder(), fd, args);
            } catch (android.os.RemoteException | java.io.IOException e2) {
                p.println("Failed to dump input method client in focused window: " + e2);
            }
        }
        p.println(" ");
        if (method != null) {
            pw.flush();
            try {
                com.android.internal.os.TransferPipe.dumpAsync(method.asBinder(), fd, args);
                return;
            } catch (android.os.RemoteException | java.io.IOException e3) {
                p.println("Failed to dump input method service: " + e3);
                return;
            }
        }
        p.println("No input method service.");
    }

    static /* synthetic */ void lambda$dumpAsStringNoCheck$17(android.util.Printer p, com.android.server.inputmethod.ClientState c) {
        p.println("  " + c + ":");
        p.println("    client=" + c.mClient);
        p.println("    fallbackInputConnection=" + c.mFallbackInputConnection);
        p.println("    sessionRequested=" + c.mSessionRequested);
        p.println("    sessionRequestedForAccessibility=" + c.mSessionRequestedForAccessibility);
        p.println("    curSession=" + c.mCurSession);
        p.println("    selfReportedDisplayId=" + c.mSelfReportedDisplayId);
        p.println("    uid=" + c.mUid);
        p.println("    pid=" + c.mPid);
    }

    static /* synthetic */ void lambda$dumpAsStringNoCheck$18(android.util.Printer p, com.android.server.inputmethod.UserDataRepository.UserData u) {
        p.println("    mUserId=" + u.mUserId);
        p.println("      hasMainConnection=" + u.mBindingController.hasMainConnection());
        p.println("      isVisibleBound=" + u.mBindingController.isVisibleBound());
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver, android.os.Binder self) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != 2000) {
            if (resultReceiver != null) {
                resultReceiver.send(-1, null);
            }
            java.lang.String errorMsg = "InputMethodManagerService does not support shell commands from non-shell users. callingUid=" + callingUid + " args=" + java.util.Arrays.toString(args);
            if (android.os.Process.isCoreUid(callingUid)) {
                android.util.Slog.e(TAG, errorMsg);
                return;
            }
            throw new java.lang.SecurityException(errorMsg);
        }
        new com.android.server.inputmethod.InputMethodManagerService.ShellCommandImpl(this).exec(self, in, out, err, args, callback, resultReceiver);
    }

    private static final class ShellCommandImpl extends android.os.ShellCommand {
        final com.android.server.inputmethod.InputMethodManagerService mService;

        ShellCommandImpl(com.android.server.inputmethod.InputMethodManagerService service) {
            this.mService = service;
        }

        public int onCommand(java.lang.String cmd) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return onCommandWithSystemIdentity(cmd);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private int onCommandWithSystemIdentity(java.lang.String cmd) {
            byte b;
            byte b2;
            java.lang.String strEmptyIfNull = android.text.TextUtils.emptyIfNull(cmd);
            switch (strEmptyIfNull.hashCode()) {
                case -1067396926:
                    b = !strEmptyIfNull.equals("tracing") ? (byte) -1 : (byte) 0;
                    break;
                case 104385:
                    b = !strEmptyIfNull.equals("ime") ? (byte) -1 : (byte) 1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    return this.mService.handleShellCommandTraceInputMethod(this);
                case 1:
                    java.lang.String imeCommand = android.text.TextUtils.emptyIfNull(getNextArg());
                    switch (imeCommand.hashCode()) {
                        case -1298848381:
                            b2 = !imeCommand.equals(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE) ? (byte) -1 : (byte) 4;
                            break;
                        case -1067396926:
                            b2 = !imeCommand.equals("tracing") ? (byte) -1 : (byte) 8;
                            break;
                        case 0:
                            b2 = !imeCommand.equals("") ? (byte) -1 : (byte) 0;
                            break;
                        case 1499:
                            b2 = !imeCommand.equals("-h") ? (byte) -1 : (byte) 1;
                            break;
                        case 113762:
                            b2 = !imeCommand.equals("set") ? (byte) -1 : (byte) 6;
                            break;
                        case 3198785:
                            b2 = !imeCommand.equals("help") ? (byte) -1 : (byte) 2;
                            break;
                        case 3322014:
                            b2 = !imeCommand.equals("list") ? (byte) -1 : (byte) 3;
                            break;
                        case 108404047:
                            b2 = !imeCommand.equals("reset") ? (byte) -1 : (byte) 7;
                            break;
                        case 1671308008:
                            b2 = !imeCommand.equals("disable") ? (byte) -1 : (byte) 5;
                            break;
                        default:
                            b2 = -1;
                            break;
                    }
                    switch (b2) {
                        case 0:
                        case 1:
                        case 2:
                            return onImeCommandHelp();
                        case 3:
                            return this.mService.handleShellCommandListInputMethods(this);
                        case 4:
                            return this.mService.handleShellCommandEnableDisableInputMethod(this, true);
                        case 5:
                            return this.mService.handleShellCommandEnableDisableInputMethod(this, false);
                        case 6:
                            return this.mService.handleShellCommandSetInputMethod(this);
                        case 7:
                            return this.mService.handleShellCommandResetInputMethod(this);
                        case 8:
                            return this.mService.handleShellCommandTraceInputMethod(this);
                        default:
                            getOutPrintWriter().println("Unknown command: " + imeCommand);
                            return -1;
                    }
                default:
                    return handleDefaultCommands(cmd);
            }
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            try {
                pw.println("InputMethodManagerService commands:");
                pw.println("  help");
                pw.println("    Prints this help text.");
                pw.println("  dump [options]");
                pw.println("    Synonym of dumpsys.");
                pw.println("  ime <command> [options]");
                pw.println("    Manipulate IMEs.  Run \"ime help\" for details.");
                pw.println("  tracing <command>");
                pw.println("    start: Start tracing.");
                pw.println("    stop : Stop tracing.");
                pw.println("    help : Show help.");
                if (pw != null) {
                    pw.close();
                }
            } catch (java.lang.Throwable th) {
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        private int onImeCommandHelp() {
            android.util.IndentingPrintWriter pw = new android.util.IndentingPrintWriter(getOutPrintWriter(), "  ", 100);
            try {
                pw.println("ime <command>:");
                pw.increaseIndent();
                pw.println("list [-a] [-s]");
                pw.increaseIndent();
                pw.println("prints all enabled input methods.");
                pw.increaseIndent();
                pw.println("-a: see all input methods");
                pw.println("-s: only a single summary line of each");
                pw.decreaseIndent();
                pw.decreaseIndent();
                pw.println("enable [--user <USER_ID>] <ID>");
                pw.increaseIndent();
                pw.println("allows the given input method ID to be used.");
                pw.increaseIndent();
                pw.print("--user <USER_ID>: Specify which user to enable.");
                pw.println(" Assumes the current user if not specified.");
                pw.decreaseIndent();
                pw.decreaseIndent();
                pw.println("disable [--user <USER_ID>] <ID>");
                pw.increaseIndent();
                pw.println("disallows the given input method ID to be used.");
                pw.increaseIndent();
                pw.print("--user <USER_ID>: Specify which user to disable.");
                pw.println(" Assumes the current user if not specified.");
                pw.decreaseIndent();
                pw.decreaseIndent();
                pw.println("set [--user <USER_ID>] <ID>");
                pw.increaseIndent();
                pw.println("switches to the given input method ID.");
                pw.increaseIndent();
                pw.print("--user <USER_ID>: Specify which user to enable.");
                pw.println(" Assumes the current user if not specified.");
                pw.decreaseIndent();
                pw.decreaseIndent();
                pw.println("reset [--user <USER_ID>]");
                pw.increaseIndent();
                pw.println("reset currently selected/enabled IMEs to the default ones as if the device is initially booted with the current locale.");
                pw.increaseIndent();
                pw.print("--user <USER_ID>: Specify which user to reset.");
                pw.println(" Assumes the current user if not specified.");
                pw.decreaseIndent();
                pw.decreaseIndent();
                pw.decreaseIndent();
                pw.close();
                return 0;
            } catch (java.lang.Throwable th) {
                try {
                    pw.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int handleShellCommandListInputMethods(android.os.ShellCommand r17) {
        /*
            Method dump skipped, instruction units count: 252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.handleShellCommandListInputMethods(android.os.ShellCommand):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0077 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int handleShellCommandEnableDisableInputMethod(android.os.ShellCommand r19, boolean r20) throws java.lang.Throwable {
        /*
            r18 = this;
            r7 = r18
            int r8 = handleOptionsForCommandsThatOnlyHaveUserOption(r19)
            java.lang.String r9 = r19.getNextArgRequired()
            r1 = 0
            java.io.PrintWriter r10 = r19.getOutPrintWriter()
            java.io.PrintWriter r0 = r19.getErrPrintWriter()     // Catch: java.lang.Throwable -> L73
            r11 = r0
            java.lang.Class<com.android.server.inputmethod.ImfLock> r12 = com.android.server.inputmethod.ImfLock.class
            monitor-enter(r12)     // Catch: java.lang.Throwable -> L65
            int r0 = r7.mCurrentUserId     // Catch: java.lang.Throwable -> L62
            java.io.PrintWriter r2 = r19.getErrPrintWriter()     // Catch: java.lang.Throwable -> L62
            int[] r0 = com.android.server.inputmethod.InputMethodUtils.resolveUserId(r8, r0, r2)     // Catch: java.lang.Throwable -> L62
            int r13 = r0.length     // Catch: java.lang.Throwable -> L62
            r14 = 0
            r15 = r1
            r6 = r14
        L25:
            if (r6 >= r13) goto L4b
            r1 = r0[r6]     // Catch: java.lang.Throwable -> L5f
            r5 = r1
            r4 = r19
            boolean r1 = r7.userHasDebugPriv(r5, r4)     // Catch: java.lang.Throwable -> L5f
            if (r1 != 0) goto L35
            r17 = r6
            goto L48
        L35:
            r1 = r18
            r2 = r5
            r3 = r9
            r4 = r20
            r16 = r5
            r5 = r10
            r17 = r6
            r6 = r11
            boolean r1 = r1.handleShellCommandEnableDisableInputMethodInternalLocked(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L5f
            r1 = r1 ^ 1
            r15 = r15 | r1
        L48:
            int r6 = r17 + 1
            goto L25
        L4b:
            monitor-exit(r12)     // Catch: java.lang.Throwable -> L5f
            if (r11 == 0) goto L56
            r11.close()     // Catch: java.lang.Throwable -> L52
            goto L56
        L52:
            r0 = move-exception
            r2 = r0
            r1 = r15
            goto L75
        L56:
            if (r10 == 0) goto L5b
            r10.close()
        L5b:
            if (r15 == 0) goto L5e
            r14 = -1
        L5e:
            return r14
        L5f:
            r0 = move-exception
            r1 = r15
            goto L63
        L62:
            r0 = move-exception
        L63:
            monitor-exit(r12)     // Catch: java.lang.Throwable -> L62
            throw r0     // Catch: java.lang.Throwable -> L65
        L65:
            r0 = move-exception
            r2 = r0
            if (r11 == 0) goto L72
            r11.close()     // Catch: java.lang.Throwable -> L6d
            goto L72
        L6d:
            r0 = move-exception
            r3 = r0
            r2.addSuppressed(r3)     // Catch: java.lang.Throwable -> L73
        L72:
            throw r2     // Catch: java.lang.Throwable -> L73
        L73:
            r0 = move-exception
            r2 = r0
        L75:
            if (r10 == 0) goto L80
            r10.close()     // Catch: java.lang.Throwable -> L7b
            goto L80
        L7b:
            r0 = move-exception
            r3 = r0
            r2.addSuppressed(r3)
        L80:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.handleShellCommandEnableDisableInputMethod(android.os.ShellCommand, boolean):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int handleOptionsForCommandsThatOnlyHaveUserOption(android.os.ShellCommand r2) {
        /*
        L1:
            java.lang.String r0 = r2.getNextOption()
            if (r0 != 0) goto La
        L8:
            r0 = -2
            return r0
        La:
            int r1 = r0.hashCode()
            switch(r1) {
                case 1512: goto L1c;
                case 1333469547: goto L12;
                default: goto L11;
            }
        L11:
            goto L26
        L12:
            java.lang.String r1 = "--user"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L11
            r1 = 1
            goto L27
        L1c:
            java.lang.String r1 = "-u"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L11
            r1 = 0
            goto L27
        L26:
            r1 = -1
        L27:
            switch(r1) {
                case 0: goto L2b;
                case 1: goto L2b;
                default: goto L2a;
            }
        L2a:
            goto L1
        L2b:
            java.lang.String r1 = r2.getNextArgRequired()
            int r1 = android.os.UserHandle.parseUserArg(r1)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.handleOptionsForCommandsThatOnlyHaveUserOption(android.os.ShellCommand):int");
    }

    private boolean handleShellCommandEnableDisableInputMethodInternalLocked(int userId, java.lang.String imeId, boolean enabled, java.io.PrintWriter out, java.io.PrintWriter error) {
        boolean failedToEnableUnknownIme = false;
        boolean previouslyEnabled = false;
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
        if (userId == this.mCurrentUserId) {
            if (enabled && !settings.getMethodMap().containsKey(imeId)) {
                failedToEnableUnknownIme = true;
            } else {
                previouslyEnabled = setInputMethodEnabledLocked(imeId, enabled);
            }
        } else if (enabled) {
            if (!settings.getMethodMap().containsKey(imeId)) {
                failedToEnableUnknownIme = true;
            } else {
                java.lang.String enabledImeIdsStr = settings.getEnabledInputMethodsStr();
                java.lang.String newEnabledImeIdsStr = com.android.server.inputmethod.InputMethodUtils.concatEnabledImeIds(enabledImeIdsStr, imeId);
                previouslyEnabled = android.text.TextUtils.equals(enabledImeIdsStr, newEnabledImeIdsStr);
                if (!previouslyEnabled) {
                    settings.putEnabledInputMethodsStr(newEnabledImeIdsStr);
                }
            }
        } else {
            previouslyEnabled = settings.buildAndPutEnabledInputMethodsStrRemovingId(new java.lang.StringBuilder(), settings.getEnabledInputMethodsAndSubtypeList(), imeId);
        }
        if (failedToEnableUnknownIme) {
            error.print("Unknown input method ");
            error.print(imeId);
            error.println(" cannot be enabled for user #" + userId);
            android.util.Slog.e(TAG, "\"ime enable " + imeId + "\" for user #" + userId + " failed due to its unrecognized IME ID.");
            return false;
        }
        out.print("Input method ");
        out.print(imeId);
        out.print(": ");
        out.print(enabled == previouslyEnabled ? "already " : "now ");
        out.print(enabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
        out.print(" for user #");
        out.println(userId);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleShellCommandSetInputMethod(android.os.ShellCommand shellCommand) throws java.lang.Throwable {
        int userIdToBeResolved = handleOptionsForCommandsThatOnlyHaveUserOption(shellCommand);
        java.lang.String imeId = shellCommand.getNextArgRequired();
        boolean hasFailed = false;
        java.io.PrintWriter out = shellCommand.getOutPrintWriter();
        try {
            java.io.PrintWriter error = shellCommand.getErrPrintWriter();
            try {
                try {
                    synchronized (com.android.server.inputmethod.ImfLock.class) {
                        try {
                            int[] userIds = com.android.server.inputmethod.InputMethodUtils.resolveUserId(userIdToBeResolved, this.mCurrentUserId, shellCommand.getErrPrintWriter());
                            for (int userId : userIds) {
                                try {
                                    if (userHasDebugPriv(userId, shellCommand)) {
                                        boolean failedToSelectUnknownIme = !switchToInputMethodLocked(imeId, userId);
                                        if (failedToSelectUnknownIme) {
                                            error.print("Unknown input method ");
                                            error.print(imeId);
                                            error.print(" cannot be selected for user #");
                                            error.println(userId);
                                            android.util.Slog.e(TAG, "\"ime set " + imeId + "\" for user #" + userId + " failed due to its unrecognized IME ID.");
                                        } else {
                                            out.print("Input method ");
                                            out.print(imeId);
                                            out.print(" selected for user #");
                                            out.println(userId);
                                        }
                                        hasFailed |= failedToSelectUnknownIme;
                                    }
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    try {
                                        throw th;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                        java.lang.Throwable th3 = th;
                                        if (error == null) {
                                            throw th3;
                                        }
                                        try {
                                            error.close();
                                            throw th3;
                                        } catch (java.lang.Throwable th4) {
                                            th3.addSuppressed(th4);
                                            throw th3;
                                        }
                                    }
                                }
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    }
                    if (error != null) {
                        error.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    return hasFailed ? -1 : 0;
                } catch (java.lang.Throwable th6) {
                    th = th6;
                }
            } catch (java.lang.Throwable th7) {
                th = th7;
                java.lang.Throwable th8 = th;
                if (out == null) {
                    throw th8;
                }
                try {
                    out.close();
                    throw th8;
                } catch (java.lang.Throwable th9) {
                    th8.addSuppressed(th9);
                    throw th8;
                }
            }
        } catch (java.lang.Throwable th10) {
            th = th10;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v14 */
    /* JADX WARN: Type inference failed for: r6v2, types: [boolean, int] */
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
    public int handleShellCommandResetInputMethod(android.os.ShellCommand shellCommand) throws java.lang.Throwable {
        int[] userIds;
        java.util.List<android.view.inputmethod.InputMethodInfo> nextEnabledImes;
        java.lang.String nextIme;
        int userIdToBeResolved = handleOptionsForCommandsThatOnlyHaveUserOption(shellCommand);
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            try {
                try {
                    final java.io.PrintWriter out = shellCommand.getOutPrintWriter();
                    try {
                        int[] userIds2 = com.android.server.inputmethod.InputMethodUtils.resolveUserId(userIdToBeResolved, this.mCurrentUserId, shellCommand.getErrPrintWriter());
                        int length = userIds2.length;
                        ?? r6 = 0;
                        int i = 0;
                        while (i < length) {
                            int userId = userIds2[i];
                            try {
                                if (!userHasDebugPriv(userId, shellCommand)) {
                                    userIds = userIds2;
                                } else {
                                    android.content.pm.UserInfo userInfo = this.mUserManagerInternal.getUserInfo(userId);
                                    if (userInfo != null && "android.os.usertype.system.HEADLESS".equals(userInfo.userType)) {
                                        userIds = userIds2;
                                    } else {
                                        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettingsRepository.get(userId);
                                        if (userId == this.mCurrentUserId) {
                                            if (!android.view.inputmethod.Flags.refactorInsetsController()) {
                                                hideCurrentInputLocked(this.mImeBindingState.mFocusedWindow, r6, 15);
                                            } else if (this.mImeBindingState != null && this.mImeBindingState.mFocusedWindowClient != null && this.mImeBindingState.mFocusedWindowClient.mClient != null) {
                                                this.mImeBindingState.mFocusedWindowClient.mClient.setImeVisibility(r6);
                                            }
                                            com.android.server.inputmethod.InputMethodBindingController bindingController = getInputMethodBindingController(userId);
                                            bindingController.unbindCurrentMethod();
                                            java.util.ArrayList<android.view.inputmethod.InputMethodInfo> toDisable = settings.getEnabledInputMethodList();
                                            java.util.ArrayList<android.view.inputmethod.InputMethodInfo> defaultEnabled = com.android.server.inputmethod.InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, settings.getMethodList());
                                            toDisable.removeAll(defaultEnabled);
                                            java.util.Iterator<android.view.inputmethod.InputMethodInfo> it = toDisable.iterator();
                                            while (it.hasNext()) {
                                                setInputMethodEnabledLocked(it.next().getId(), false);
                                                userIds2 = userIds2;
                                            }
                                            userIds = userIds2;
                                            for (java.util.Iterator<android.view.inputmethod.InputMethodInfo> it2 = defaultEnabled.iterator(); it2.hasNext(); it2 = it2) {
                                                setInputMethodEnabledLocked(it2.next().getId(), true);
                                            }
                                            if (!chooseNewDefaultIMELocked()) {
                                                resetSelectedInputMethodAndSubtypeLocked(null);
                                            }
                                            updateInputMethodsFromSettingsLocked(true);
                                            com.android.server.inputmethod.InputMethodUtils.setNonSelectedSystemImesDisabledUntilUsed(getPackageManagerForUser(this.mContext, settings.getUserId()), settings.getEnabledInputMethodList());
                                            nextIme = settings.getSelectedInputMethod();
                                            nextEnabledImes = settings.getEnabledInputMethodList();
                                        } else {
                                            userIds = userIds2;
                                            nextEnabledImes = com.android.server.inputmethod.InputMethodInfoUtils.getDefaultEnabledImes(this.mContext, settings.getMethodList());
                                            android.view.inputmethod.InputMethodInfo info = this.mImmsWrapper.getExtImpl().getDefaultInputMethodByConfig(userId);
                                            java.lang.String nextIme2 = info != null ? info.getId() : com.android.server.inputmethod.InputMethodInfoUtils.getMostApplicableDefaultIME(nextEnabledImes).getId();
                                            java.lang.String[] nextEnabledImeIds = new java.lang.String[nextEnabledImes.size()];
                                            for (int i2 = 0; i2 < nextEnabledImeIds.length; i2++) {
                                                nextEnabledImeIds[i2] = nextEnabledImes.get(i2).getId();
                                            }
                                            settings.putEnabledInputMethodsStr(com.android.server.inputmethod.InputMethodUtils.concatEnabledImeIds("", nextEnabledImeIds));
                                            settings.putSelectedInputMethod(nextIme2);
                                            settings.putSelectedDefaultDeviceInputMethod(null);
                                            settings.putSelectedSubtype(-1);
                                            nextIme = nextIme2;
                                        }
                                        out.println("Reset current and enabled IMEs for user #" + userId);
                                        out.println("  Selected: " + nextIme);
                                        nextEnabledImes.forEach(new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodManagerService$$ExternalSyntheticLambda13
                                            @Override // java.util.function.Consumer
                                            public final void accept(java.lang.Object obj) {
                                                out.println("   Enabled: " + ((android.view.inputmethod.InputMethodInfo) obj).getId());
                                            }
                                        });
                                    }
                                }
                                i++;
                                userIds2 = userIds;
                                r6 = 0;
                            } catch (java.lang.Throwable th) {
                                th = th;
                                java.lang.Throwable th2 = th;
                                if (out == null) {
                                    throw th2;
                                }
                                try {
                                    out.close();
                                    throw th2;
                                } catch (java.lang.Throwable th3) {
                                    th2.addSuppressed(th3);
                                    throw th2;
                                }
                                throw th;
                            }
                        }
                        if (out != null) {
                            out.close();
                        }
                        return 0;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                } catch (java.lang.Throwable th5) {
                    th = th5;
                    throw th;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:5:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int handleShellCommandTraceInputMethod(android.os.ShellCommand r7) {
        /*
            r6 = this;
            java.lang.String r0 = r7.getNextArgRequired()
            java.io.PrintWriter r1 = r7.getOutPrintWriter()
            int r2 = r0.hashCode()     // Catch: java.lang.Throwable -> La1
            r3 = -1
            r4 = 0
            switch(r2) {
                case -390772652: goto L28;
                case 3540994: goto L1d;
                case 109757538: goto L12;
                default: goto L11;
            }     // Catch: java.lang.Throwable -> La1
        L11:
            goto L33
        L12:
            java.lang.String r2 = "start"
            boolean r2 = r0.equals(r2)     // Catch: java.lang.Throwable -> La1
            if (r2 == 0) goto L11
            r2 = r4
            goto L34
        L1d:
            java.lang.String r2 = "stop"
            boolean r2 = r0.equals(r2)     // Catch: java.lang.Throwable -> La1
            if (r2 == 0) goto L11
            r2 = 1
            goto L34
        L28:
            java.lang.String r2 = "save-for-bugreport"
            boolean r2 = r0.equals(r2)     // Catch: java.lang.Throwable -> La1
            if (r2 == 0) goto L11
            r2 = 2
            goto L34
        L33:
            r2 = r3
        L34:
            switch(r2) {
                case 0: goto L50;
                case 1: goto L48;
                case 2: goto L3a;
                default: goto L37;
            }     // Catch: java.lang.Throwable -> La1
        L37:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La1
            goto L77
        L3a:
            com.android.internal.inputmethod.ImeTracing r2 = com.android.internal.inputmethod.ImeTracing.getInstance()     // Catch: java.lang.Throwable -> La1
            r2.saveForBugreport(r1)     // Catch: java.lang.Throwable -> La1
            if (r1 == 0) goto L47
            r1.close()
        L47:
            return r4
        L48:
            com.android.internal.inputmethod.ImeTracing r2 = com.android.internal.inputmethod.ImeTracing.getInstance()     // Catch: java.lang.Throwable -> La1
            r2.stopTrace(r1)     // Catch: java.lang.Throwable -> La1
            goto L58
        L50:
            com.android.internal.inputmethod.ImeTracing r2 = com.android.internal.inputmethod.ImeTracing.getInstance()     // Catch: java.lang.Throwable -> La1
            r2.startTrace(r1)     // Catch: java.lang.Throwable -> La1
        L58:
            if (r1 == 0) goto L5d
            r1.close()
        L5d:
            com.android.internal.inputmethod.ImeTracing r1 = com.android.internal.inputmethod.ImeTracing.getInstance()
            boolean r2 = r1.isEnabled()
            java.lang.Class<com.android.server.inputmethod.ImfLock> r3 = com.android.server.inputmethod.ImfLock.class
            monitor-enter(r3)
            com.android.server.inputmethod.ClientController r1 = r6.mClientController     // Catch: java.lang.Throwable -> L74
            com.android.server.inputmethod.InputMethodManagerService$6 r5 = new com.android.server.inputmethod.InputMethodManagerService$6     // Catch: java.lang.Throwable -> L74
            r5.<init>()     // Catch: java.lang.Throwable -> L74
            r1.forAllClients(r5)     // Catch: java.lang.Throwable -> L74
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L74
            return r4
        L74:
            r1 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L74
            throw r1
        L77:
            r2.<init>()     // Catch: java.lang.Throwable -> La1
            java.lang.String r4 = "Unknown command: "
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch: java.lang.Throwable -> La1
            java.lang.StringBuilder r2 = r2.append(r0)     // Catch: java.lang.Throwable -> La1
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> La1
            r1.println(r2)     // Catch: java.lang.Throwable -> La1
            java.lang.String r2 = "Input method trace options:"
            r1.println(r2)     // Catch: java.lang.Throwable -> La1
            java.lang.String r2 = "  start: Start tracing"
            r1.println(r2)     // Catch: java.lang.Throwable -> La1
            java.lang.String r2 = "  stop: Stop tracing"
            r1.println(r2)     // Catch: java.lang.Throwable -> La1
            if (r1 == 0) goto La0
            r1.close()
        La0:
            return r3
        La1:
            r2 = move-exception
            if (r1 == 0) goto Lac
            r1.close()     // Catch: java.lang.Throwable -> La8
            goto Lac
        La8:
            r3 = move-exception
            r2.addSuppressed(r3)
        Lac:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.InputMethodManagerService.handleShellCommandTraceInputMethod(android.os.ShellCommand):int");
    }

    private boolean userHasDebugPriv(int userId, android.os.ShellCommand shellCommand) {
        if (this.mUserManagerInternal.hasUserRestriction("no_debugging_features", userId)) {
            shellCommand.getErrPrintWriter().println("User #" + userId + " is restricted with DISALLOW_DEBUGGING_FEATURES.");
            return false;
        }
        return true;
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.IImeTracker getImeTrackerService() {
        return this.mImeTrackerService;
    }

    private android.view.inputmethod.ImeTracker.Token createStatsTokenForFocusedClient(boolean show, int reason) {
        int uid;
        java.lang.String packageName;
        if (this.mImeBindingState.mFocusedWindowClient != null) {
            uid = this.mImeBindingState.mFocusedWindowClient.mUid;
        } else {
            uid = -1;
        }
        if (this.mImeBindingState.mFocusedWindowEditorInfo != null) {
            packageName = this.mImeBindingState.mFocusedWindowEditorInfo.packageName;
        } else {
            packageName = "uid(" + uid + ")";
        }
        return android.view.inputmethod.ImeTracker.forLogging().onStart(packageName, uid, show ? 1 : 2, 6, reason, false);
    }

    private static final class InputMethodPrivilegedOperationsImpl extends com.android.internal.inputmethod.IInputMethodPrivilegedOperations.Stub {
        private final com.android.server.inputmethod.InputMethodManagerService mImms;
        private final android.os.IBinder mToken;

        InputMethodPrivilegedOperationsImpl(com.android.server.inputmethod.InputMethodManagerService imms, android.os.IBinder token) {
            this.mImms = imms;
            this.mToken = token;
        }

        public void setImeWindowStatusAsync(int vis, int backDisposition) {
            this.mImms.setImeWindowStatus(this.mToken, vis, backDisposition);
        }

        public void reportStartInputAsync(android.os.IBinder startInputToken) {
            this.mImms.reportStartInput(this.mToken, startInputToken);
        }

        public void setHandwritingSurfaceNotTouchable(boolean notTouchable) {
            this.mImms.mHwController.setNotTouchable(notTouchable);
        }

        public void createInputContentUriToken(android.net.Uri contentUri, java.lang.String packageName, com.android.internal.infra.AndroidFuture future) {
            try {
                future.complete(this.mImms.createInputContentUriToken(this.mToken, contentUri, packageName).asBinder());
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void reportFullscreenModeAsync(boolean fullscreen) {
            this.mImms.reportFullscreenMode(this.mToken, fullscreen);
        }

        public void setInputMethod(java.lang.String id, com.android.internal.infra.AndroidFuture future) {
            try {
                this.mImms.setInputMethod(this.mToken, id);
                future.complete((java.lang.Object) null);
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void setInputMethodAndSubtype(java.lang.String id, android.view.inputmethod.InputMethodSubtype subtype, com.android.internal.infra.AndroidFuture future) {
            try {
                this.mImms.setInputMethodAndSubtype(this.mToken, id, subtype);
                future.complete((java.lang.Object) null);
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void hideMySoftInput(android.view.inputmethod.ImeTracker.Token statsToken, int flags, int reason, com.android.internal.infra.AndroidFuture future) {
            try {
                this.mImms.hideMySoftInput(this.mToken, statsToken, flags, reason);
                future.complete((java.lang.Object) null);
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void showMySoftInput(android.view.inputmethod.ImeTracker.Token statsToken, int flags, int reason, com.android.internal.infra.AndroidFuture future) {
            try {
                this.mImms.showMySoftInput(this.mToken, statsToken, flags, reason);
                future.complete((java.lang.Object) null);
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void updateStatusIconAsync(java.lang.String packageName, int iconId) {
            this.mImms.updateStatusIcon(this.mToken, packageName, iconId);
        }

        public void switchToPreviousInputMethod(com.android.internal.infra.AndroidFuture future) {
            try {
                future.complete(java.lang.Boolean.valueOf(this.mImms.switchToPreviousInputMethod(this.mToken)));
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void switchToNextInputMethod(boolean onlyCurrentIme, com.android.internal.infra.AndroidFuture future) {
            try {
                future.complete(java.lang.Boolean.valueOf(this.mImms.switchToNextInputMethod(this.mToken, onlyCurrentIme)));
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void shouldOfferSwitchingToNextInputMethod(com.android.internal.infra.AndroidFuture future) {
            try {
                future.complete(java.lang.Boolean.valueOf(this.mImms.shouldOfferSwitchingToNextInputMethod(this.mToken)));
            } catch (java.lang.Throwable e) {
                future.completeExceptionally(e);
            }
        }

        public void notifyUserActionAsync() {
            this.mImms.notifyUserAction(this.mToken);
        }

        public void applyImeVisibilityAsync(android.os.IBinder windowToken, boolean setVisible, android.view.inputmethod.ImeTracker.Token statsToken) {
            com.android.server.inputmethod.InputMethodManagerService.sImmsStaticExt.logDebugIme(com.android.server.inputmethod.InputMethodManagerService.TAG, "applyImeVisibilityAsync: setVisible = " + setVisible);
            this.mImms.applyImeVisibility(this.mToken, windowToken, setVisible, statsToken);
        }

        public void onStylusHandwritingReady(int requestId, int pid) {
            this.mImms.onStylusHandwritingReady(requestId, pid);
        }

        public void resetStylusHandwriting(int requestId) {
            this.mImms.resetStylusHandwriting(requestId);
        }

        public void switchKeyboardLayoutAsync(int direction) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                if (this.mImms.calledWithValidTokenLocked(this.mToken)) {
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        this.mImms.switchKeyboardLayoutLocked(direction);
                    } finally {
                        android.os.Binder.restoreCallingIdentity(ident);
                    }
                }
            }
        }
    }

    public static com.android.server.inputmethod.IInputMethodManagerServiceExt.IStaticExt getStaticExtImpl() {
        return sImmsStaticExt;
    }

    public com.android.server.inputmethod.IInputMethodManagerServiceWrapper getWrapper() {
        return this.mImmsWrapper;
    }

    private class InputMethodManagerServiceWrapper implements com.android.server.inputmethod.IInputMethodManagerServiceWrapper {
        private com.android.server.inputmethod.IInputMethodManagerServiceExt mImmsExt;

        private InputMethodManagerServiceWrapper() {
            this.mImmsExt = (com.android.server.inputmethod.IInputMethodManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.inputmethod.IInputMethodManagerServiceExt.class).base(com.android.server.inputmethod.InputMethodManagerService.this).create();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public com.android.server.inputmethod.IInputMethodManagerServiceExt getExtImpl() {
            return this.mImmsExt;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public com.android.server.inputmethod.ClientState getCurClient() {
            return com.android.server.inputmethod.InputMethodManagerService.this.mCurClient;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public android.os.Handler getHandler() {
            return com.android.server.inputmethod.InputMethodManagerService.this.mHandler;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void setSelectedMethodIdLocked(java.lang.String selectedMethodId) {
            if (android.text.TextUtils.isEmpty(selectedMethodId)) {
                selectedMethodId = null;
            }
            getBindingController().setSelectedMethodId(selectedMethodId);
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public boolean isShowRequested() {
            return com.android.server.inputmethod.InputMethodManagerService.this.isShowRequestedForCurrentWindow();
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public com.android.server.inputmethod.InputMethodBindingController getBindingController() {
            return com.android.server.inputmethod.InputMethodManagerService.this.mUserDataRepository.getOrCreate(com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId).mBindingController;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public com.android.server.inputmethod.InputMethodMenuController getInputMethodMenuController() {
            return com.android.server.inputmethod.InputMethodManagerService.this.mMenuController;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public com.android.server.inputmethod.InputMethodSettings getSettings() {
            return com.android.server.inputmethod.InputMethodSettingsRepository.get(com.android.server.inputmethod.InputMethodManagerService.this.mCurrentUserId);
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public com.android.server.inputmethod.ClientController getClientController() {
            return com.android.server.inputmethod.InputMethodManagerService.this.mClientController;
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void setSelectedInputMethodAndSubtypeLocked(android.view.inputmethod.InputMethodInfo imi, int subtypeId, boolean setSubtypeOnly) {
            com.android.server.inputmethod.InputMethodManagerService.this.setSelectedInputMethodAndSubtypeLocked(imi, subtypeId, setSubtypeOnly);
        }

        @Override // com.android.server.inputmethod.IInputMethodManagerServiceWrapper
        public void showInputMethodPickerFromDelay(int auxiliarySubtypeMode, int displayId) {
            com.android.server.inputmethod.InputMethodManagerService.this.mHandler.obtainMessage(1, auxiliarySubtypeMode, displayId).sendToTarget();
        }
    }
}
