package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class PhoneWindowManager implements com.android.server.policy.WindowManagerPolicy {
    private static final int ACQUIRE_WAKE_TIME = 200000;
    private static final java.lang.String ACTION_VOICE_ASSIST_RETAIL = "android.intent.action.VOICE_ASSIST_RETAIL";
    private static final int BRIGHTNESS_STEPS = 10;
    private static final long BUGREPORT_TV_GESTURE_TIMEOUT_MILLIS = 1000;
    static final int DOUBLE_PRESS_PRIMARY_NOTHING = 0;
    static final int DOUBLE_PRESS_PRIMARY_SWITCH_RECENT_APP = 1;
    static final int DOUBLE_TAP_HOME_NOTHING = 0;
    static final int DOUBLE_TAP_HOME_PIP_MENU = 2;
    static final int DOUBLE_TAP_HOME_RECENT_SYSTEM_UI = 1;
    static final boolean ENABLE_DESK_DOCK_HOME_CAPTURE = false;
    static final boolean ENABLE_VR_HEADSET_HOME_CAPTURE = true;
    private static final float KEYGUARD_SCREENSHOT_CHORD_DELAY_MULTIPLIER = 2.5f;
    static final int LAST_LONG_PRESS_HOME_BEHAVIOR = 3;
    static final int LAST_SETTINGS_KEY_BEHAVIOR = 2;
    static final int LONG_PRESS_BACK_GO_TO_VOICE_ASSIST = 1;
    static final int LONG_PRESS_BACK_NOTHING = 0;
    static final int LONG_PRESS_HOME_ALL_APPS = 1;
    static final int LONG_PRESS_HOME_ASSIST = 2;
    static final int LONG_PRESS_HOME_NOTHING = 0;
    static final int LONG_PRESS_HOME_NOTIFICATION_PANEL = 3;
    static final int LONG_PRESS_POWER_ASSISTANT = 5;
    static final int LONG_PRESS_POWER_GLOBAL_ACTIONS = 1;
    static final int LONG_PRESS_POWER_GO_TO_VOICE_ASSIST = 4;
    static final int LONG_PRESS_POWER_NOTHING = 0;
    static final int LONG_PRESS_POWER_SHUT_OFF = 2;
    static final int LONG_PRESS_POWER_SHUT_OFF_NO_CONFIRM = 3;
    static final int LONG_PRESS_PRIMARY_LAUNCH_VOICE_ASSISTANT = 1;
    static final int LONG_PRESS_PRIMARY_NOTHING = 0;
    private static final int MSG_ACCESSIBILITY_SHORTCUT = 17;
    private static final int MSG_ACCESSIBILITY_TV = 19;
    private static final int MSG_BUGREPORT_TV = 18;
    private static final int MSG_DISPATCH_BACK_KEY_TO_AUTOFILL = 20;
    private static final int MSG_DISPATCH_MEDIA_KEY_REPEAT_WITH_WAKE_LOCK = 4;
    private static final int MSG_DISPATCH_MEDIA_KEY_WITH_WAKE_LOCK = 3;
    private static final int MSG_DISPATCH_SHOW_GLOBAL_ACTIONS = 10;
    private static final int MSG_DISPATCH_SHOW_RECENTS = 9;
    private static final int MSG_HANDLE_ALL_APPS = 22;
    private static final int MSG_HIDE_BOOT_MESSAGE = 11;
    private static final int MSG_KEYGUARD_DRAWN_COMPLETE = 5;
    private static final int MSG_KEYGUARD_DRAWN_TIMEOUT = 6;
    private static final int MSG_LAUNCH_ASSIST = 23;
    private static final int MSG_LAUNCH_VOICE_ASSIST_WITH_WAKE_LOCK = 12;
    private static final int MSG_LOG_KEYBOARD_SYSTEM_EVENT = 26;
    private static final int MSG_RINGER_TOGGLE_CHORD = 24;
    private static final int MSG_SCREENSHOT_CHORD = 16;
    private static final int MSG_SET_DEFERRED_KEY_ACTIONS_EXECUTABLE = 27;
    private static final int MSG_SHOW_PICTURE_IN_PICTURE_MENU = 15;
    private static final int MSG_SWITCH_KEYBOARD_LAYOUT = 25;
    private static final int MSG_SYSTEM_KEY_PRESS = 21;
    private static final int MSG_WINDOW_MANAGER_DRAWN_COMPLETE = 7;
    static final int MULTI_PRESS_POWER_BRIGHTNESS_BOOST = 2;
    static final int MULTI_PRESS_POWER_LAUNCH_TARGET_ACTIVITY = 3;
    static final int MULTI_PRESS_POWER_NOTHING = 0;
    static final int MULTI_PRESS_POWER_THEATER_MODE = 1;
    static final int PENDING_KEY_NULL = -1;
    private static final int POWER_BUTTON_SUPPRESSION_DELAY_DEFAULT_MILLIS = 800;
    static final int POWER_VOLUME_UP_BEHAVIOR_GLOBAL_ACTIONS = 2;
    static final int POWER_VOLUME_UP_BEHAVIOR_MUTE = 1;
    static final int POWER_VOLUME_UP_BEHAVIOR_NOTHING = 0;
    static final int SEARCH_KEY_BEHAVIOR_DEFAULT_SEARCH = 0;
    static final int SEARCH_KEY_BEHAVIOR_TARGET_ACTIVITY = 1;
    static final int SETTINGS_KEY_BEHAVIOR_NOTHING = 2;
    static final int SETTINGS_KEY_BEHAVIOR_NOTIFICATION_PANEL = 1;
    static final int SETTINGS_KEY_BEHAVIOR_SETTINGS_ACTIVITY = 0;
    static final int SHORT_PRESS_POWER_CLOSE_IME_OR_GO_HOME = 5;
    static final int SHORT_PRESS_POWER_DREAM_OR_SLEEP = 7;
    static final int SHORT_PRESS_POWER_GO_HOME = 4;
    static final int SHORT_PRESS_POWER_GO_TO_SLEEP = 1;
    static final int SHORT_PRESS_POWER_LOCK_OR_SLEEP = 6;
    static final int SHORT_PRESS_POWER_NOTHING = 0;
    static final int SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP = 2;
    static final int SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP_AND_GO_HOME = 3;
    static final int SHORT_PRESS_PRIMARY_LAUNCH_ALL_APPS = 1;
    static final int SHORT_PRESS_PRIMARY_LAUNCH_TARGET_ACTIVITY = 2;
    static final int SHORT_PRESS_PRIMARY_NOTHING = 0;
    static final int SHORT_PRESS_SLEEP_GO_TO_SLEEP = 0;
    static final int SHORT_PRESS_SLEEP_GO_TO_SLEEP_AND_GO_HOME = 1;
    static final int SHORT_PRESS_WINDOW_NOTHING = 0;
    static final int SHORT_PRESS_WINDOW_PICTURE_IN_PICTURE = 1;
    public static final java.lang.String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    public static final java.lang.String SYSTEM_DIALOG_REASON_GESTURE_NAV = "gestureNav";
    public static final java.lang.String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    public static final java.lang.String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    public static final java.lang.String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final java.lang.String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    public static final java.lang.String SYSTEM_DIALOG_REASON_SCREENSHOT = "screenshot";
    static final java.lang.String TAG = "WindowManager";
    public static final int TOAST_WINDOW_ANIM_BUFFER = 600;
    public static final int TOAST_WINDOW_TIMEOUT = 4100;
    public static final java.lang.String TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD = "waitForAllWindowsDrawn";
    static final int TRIPLE_PRESS_PRIMARY_NOTHING = 0;
    static final int TRIPLE_PRESS_PRIMARY_TOGGLE_ACCESSIBILITY = 1;
    static final int VERY_LONG_PRESS_POWER_GLOBAL_ACTIONS = 1;
    static final int VERY_LONG_PRESS_POWER_NOTHING = 0;
    static final int WAITING_FOR_DRAWN_TIMEOUT = 1000;
    android.view.accessibility.AccessibilityManager mAccessibilityManager;
    com.android.server.AccessibilityManagerInternal mAccessibilityManagerInternal;
    private com.android.internal.accessibility.AccessibilityShortcutController mAccessibilityShortcutController;
    android.app.ActivityManagerInternal mActivityManagerInternal;
    android.app.IActivityManager mActivityManagerService;
    com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    boolean mAllowStartActivityForLongPressOnPowerDuringSetup;
    android.app.AppOpsManager mAppOpsManager;
    android.media.AudioManagerInternal mAudioManagerInternal;
    android.view.autofill.AutofillManagerInternal mAutofillManagerInternal;
    volatile boolean mBackKeyHandled;
    private android.app.ActivityManager.RecentTaskInfo mBackgroundRecentTaskInfoOnStemPrimarySingleKeyUp;
    volatile boolean mBootAnimationDismissable;
    boolean mBootMessageNeedsHiding;
    android.os.PowerManager.WakeLock mBroadcastWakeLock;
    com.android.server.policy.BurnInProtectionHelper mBurnInProtectionHelper;
    private com.android.server.policy.PhoneWindowManager.ButtonOverridePermissionChecker mButtonOverridePermissionChecker;
    volatile boolean mCameraGestureTriggered;
    volatile boolean mCameraGestureTriggeredDuringGoingToSleep;
    android.content.Intent mCarDockIntent;
    android.content.Context mContext;
    private int mCurrentUserId;
    android.view.Display mDefaultDisplay;
    com.android.server.wm.DisplayPolicy mDefaultDisplayPolicy;
    com.android.server.wm.DisplayRotation mDefaultDisplayRotation;
    android.content.Intent mDeskDockIntent;
    volatile boolean mDeviceGoingToSleep;
    private volatile boolean mDismissImeOnBackKeyPressed;
    private com.android.server.policy.DisplayFoldController mDisplayFoldController;
    android.hardware.display.DisplayManager mDisplayManager;
    android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    int mDoublePressOnPowerBehavior;
    private int mDoublePressOnStemPrimaryBehavior;
    int mDoubleTapOnHomeBehavior;
    android.service.dreams.DreamManagerInternal mDreamManagerInternal;
    volatile boolean mEndCallKeyHandled;
    int mEndcallBehavior;
    private android.app.ActivityTaskManager.RootTaskInfo mFocusedTaskInfoOnStemPrimarySingleKeyUp;
    private com.android.server.GestureLauncherService mGestureLauncherService;
    private com.android.server.policy.GlobalActions mGlobalActions;
    private java.util.function.Supplier<com.android.server.policy.GlobalActions> mGlobalActionsFactory;
    private com.android.server.policy.GlobalKeyManager mGlobalKeyManager;
    private boolean mGoToSleepOnButtonPressTheaterMode;
    private boolean mHandleVolumeKeysInWM;
    public android.os.Handler mHandler;
    private com.android.server.vibrator.HapticFeedbackVibrationProvider mHapticFeedbackVibrationProvider;
    private boolean mHasFeatureAuto;
    private boolean mHasFeatureHdmiCec;
    private boolean mHasFeatureLeanback;
    private boolean mHasFeatureWatch;
    boolean mHaveBuiltInKeyboard;
    boolean mHavePendingMediaKeyRepeatWithWakeLock;
    com.android.server.policy.PhoneWindowManager.HdmiControl mHdmiControl;
    android.content.Intent mHomeIntent;
    int mIncallBackBehavior;
    int mIncallPowerBehavior;
    android.hardware.input.InputManager mInputManager;
    com.android.server.input.InputManagerInternal mInputManagerInternal;
    volatile boolean mIsGoingToSleepDefaultDisplay;
    private com.android.server.policy.KeyCombinationManager mKeyCombinationManager;
    private boolean mKeyguardBound;
    com.android.server.policy.keyguard.KeyguardServiceDelegate mKeyguardDelegate;
    private boolean mKeyguardDrawnOnce;
    private boolean mKeyguardOccludedChanged;
    boolean mKidsModeEnabled;
    int mLidKeyboardAccessibility;
    int mLidNavigationAccessibility;
    private boolean mLockAfterDreamingTransitionFinished;
    com.android.internal.widget.LockPatternUtils mLockPatternUtils;
    int mLockScreenTimeout;
    boolean mLockScreenTimerActive;
    com.android.internal.logging.MetricsLogger mLogger;
    int mLongPressOnBackBehavior;
    int mLongPressOnHomeBehavior;
    long mLongPressOnPowerAssistantTimeoutMs;
    int mLongPressOnPowerBehavior;
    private int mLongPressOnStemPrimaryBehavior;
    com.android.server.policy.ModifierShortcutManager mModifierShortcutManager;
    android.content.pm.PackageManager mPackageManager;
    boolean mPendingCapsLockToggle;
    private boolean mPendingKeyguardOccluded;
    boolean mPendingMetaAction;
    volatile boolean mPictureInPictureVisible;
    android.content.ComponentName mPowerDoublePressTargetActivity;
    volatile boolean mPowerKeyHandled;
    android.os.PowerManager.WakeLock mPowerKeyWakeLock;
    android.os.PowerManager mPowerManager;
    android.os.PowerManagerInternal mPowerManagerInternal;
    int mPowerVolUpBehavior;
    boolean mPreloadedRecentApps;
    android.content.ComponentName mPrimaryShortPressTargetActivity;
    int mRecentAppsHeldModifiers;
    volatile boolean mRecentsVisible;
    volatile boolean mRequestedOrSleepingDefaultDisplay;
    boolean mSafeMode;
    int mSearchKeyBehavior;
    android.content.ComponentName mSearchKeyTargetActivity;
    android.hardware.SensorPrivacyManager mSensorPrivacyManager;
    int mSettingsKeyBehavior;
    com.android.server.policy.PhoneWindowManager.SettingsObserver mSettingsObserver;
    int mShortPressOnPowerBehavior;
    int mShortPressOnSleepBehavior;
    private int mShortPressOnStemPrimaryBehavior;
    int mShortPressOnWindowBehavior;
    private boolean mShouldEarlyShortPressOnPower;
    boolean mShouldEarlyShortPressOnStemPrimary;
    com.android.server.policy.SideFpsEventHandler mSideFpsEventHandler;
    boolean mSilenceRingerOnSleepKey;
    private com.android.server.policy.SingleKeyGestureDetector mSingleKeyGestureDetector;
    com.android.server.statusbar.StatusBarManagerInternal mStatusBarManagerInternal;
    com.android.internal.statusbar.IStatusBarService mStatusBarService;
    private boolean mSupportLongPressPowerWhenNonInteractive;
    private boolean mSupportShortPressPowerWhenDefaultDisplayOn;
    boolean mSystemBooted;
    boolean mSystemNavigationKeysEnabled;
    boolean mSystemReady;
    private com.android.server.policy.TalkbackShortcutController mTalkbackShortcutController;
    int mTriplePressOnPowerBehavior;
    private int mTriplePressOnStemPrimaryBehavior;
    int mUiMode;
    android.app.IUiModeManager mUiModeManager;
    boolean mUseTvRouting;
    com.android.server.pm.UserManagerInternal mUserManagerInternal;
    int mVeryLongPressOnPowerBehavior;
    android.os.Vibrator mVibrator;
    android.content.Intent mVrHeadsetHomeIntent;
    volatile com.android.server.vr.VrManagerInternal mVrManagerInternal;
    boolean mWakeGestureEnabledSetting;
    com.android.server.policy.PhoneWindowManager.MyWakeGestureListener mWakeGestureListener;
    boolean mWakeOnAssistKeyPress;
    boolean mWakeOnBackKeyPress;
    boolean mWakeOnDpadKeyPress;
    long mWakeUpToLastStateTimeout;
    private com.android.server.wallpaper.WallpaperManagerInternal mWallpaperManagerInternal;
    com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;
    com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private com.android.server.policy.WindowWakeUpPolicy mWindowWakeUpPolicy;
    static boolean localLOGV = false;
    static boolean DEBUG_INPUT = false;
    static boolean DEBUG_KEYGUARD = false;
    static boolean DEBUG_WAKEUP = false;
    private static boolean LTW_DISABLE = android.os.SystemProperties.getBoolean("persist.sys.ltw.disable", false);
    private static final int[] WINDOW_TYPES_WHERE_HOME_DOESNT_WORK = {2003, 2010};
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.policy.WindowManagerPolicy.ScreenOnListener> mScreenOnListeners = new android.util.SparseArray<>();
    final java.lang.Object mServiceAcquireLock = new java.lang.Object();
    boolean mEnableBugReportKeyboardShortcut = false;
    private boolean mEnableCarDockHomeCapture = true;
    final com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener mKeyguardDrawnCallback = new com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener() { // from class: com.android.server.policy.PhoneWindowManager.1
        @Override // com.android.server.policy.keyguard.KeyguardServiceDelegate.DrawnListener
        public void onDrawn() {
            android.util.Slog.d(com.android.server.policy.PhoneWindowManager.TAG, "mKeyguardDelegate.ShowListener.onDrawn.");
            com.android.server.policy.PhoneWindowManager.this.mHandler.sendEmptyMessage(5);
        }
    };
    volatile boolean mNavBarVirtualKeyHapticFeedbackEnabled = true;
    volatile int mPendingWakeKey = -1;
    int mCameraLensCoverState = -1;
    boolean mStylusButtonsEnabled = true;
    boolean mHasSoftInput = false;
    private java.util.HashSet<java.lang.Integer> mAllowLockscreenWhenOnDisplays = new java.util.HashSet<>();
    int mRingerToggleChord = 0;
    private final android.util.SparseArray<java.util.Set<java.lang.Integer>> mConsumedKeysForDevice = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.view.KeyCharacterMap.FallbackAction> mFallbackActions = new android.util.SparseArray<>();
    private final com.android.internal.policy.LogDecelerateInterpolator mLogDecelerateInterpolator = new com.android.internal.policy.LogDecelerateInterpolator(100, 0);
    private final com.android.server.policy.DeferredKeyActionExecutor mDeferredKeyActionExecutor = new com.android.server.policy.DeferredKeyActionExecutor();
    private volatile int mTopFocusedDisplayId = -1;
    private int mPowerButtonSuppressionDelayMillis = 800;
    private boolean mLockNowPending = false;
    private int mKeyguardDrawnTimeout = 1000;
    private boolean mAllowSetKeyData = false;
    private android.os.UEventObserver mHDMIObserver = new android.os.UEventObserver() { // from class: com.android.server.policy.PhoneWindowManager.2
        public void onUEvent(android.os.UEventObserver.UEvent event) {
            com.android.server.policy.PhoneWindowManager.this.mDefaultDisplayPolicy.setHdmiPlugged("1".equals(event.get("SWITCH_STATE")));
        }
    };
    final android.service.vr.IPersistentVrStateCallbacks mPersistentVrModeListener = new android.service.vr.IPersistentVrStateCallbacks.Stub() { // from class: com.android.server.policy.PhoneWindowManager.3
        public void onPersistentVrStateChanged(boolean enabled) {
            com.android.server.policy.PhoneWindowManager.this.mDefaultDisplayPolicy.setPersistentVrModeEnabled(enabled);
        }
    };
    public com.android.server.policy.IPhoneWindowManagerSocExt mPhoneWindowManagerSocExt = (com.android.server.policy.IPhoneWindowManagerSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.IPhoneWindowManagerSocExt.class).base(this).create();
    private final java.lang.Runnable mEndCallLongPress = new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager.4
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.policy.PhoneWindowManager.this.mEndCallKeyHandled = true;
            com.android.server.policy.PhoneWindowManager.this.performHapticFeedback(0, false, "End Call - Long Press - Show Global Actions");
            com.android.server.policy.PhoneWindowManager.this.showGlobalActionsInternal();
        }
    };
    private final android.util.SparseArray<com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler> mDisplayHomeButtonHandlers = new android.util.SparseArray<>();
    android.content.BroadcastReceiver mDockReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.policy.PhoneWindowManager.13
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.DOCK_EVENT".equals(intent.getAction())) {
                com.android.server.policy.PhoneWindowManager.this.mDefaultDisplayPolicy.setDockMode(intent.getIntExtra("android.intent.extra.DOCK_STATE", 0));
            } else {
                try {
                    android.app.IUiModeManager uiModeService = android.app.IUiModeManager.Stub.asInterface(android.os.ServiceManager.getService("uimode"));
                    com.android.server.policy.PhoneWindowManager.this.mUiMode = uiModeService.getCurrentModeType();
                } catch (android.os.RemoteException e) {
                }
            }
            com.android.server.policy.PhoneWindowManager.this.updateRotation(true);
            com.android.server.policy.PhoneWindowManager.this.mDefaultDisplayRotation.updateOrientationListener();
        }
    };
    android.content.BroadcastReceiver mMultiuserReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.policy.PhoneWindowManager.14
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                com.android.server.policy.PhoneWindowManager.this.mSettingsObserver.onChange(false);
                com.android.server.policy.PhoneWindowManager.this.mDefaultDisplayRotation.onUserSwitch();
                com.android.server.policy.PhoneWindowManager.this.mWindowManagerFuncs.onUserSwitched();
            }
        }
    };
    android.app.ProgressDialog mBootMsgDialog = null;
    final com.android.server.policy.PhoneWindowManager.ScreenLockTimeout mScreenLockTimeout = new com.android.server.policy.PhoneWindowManager.ScreenLockTimeout();
    private com.android.server.policy.IPhoneWindowManagerWrapper mWrapper = new com.android.server.policy.PhoneWindowManager.PhoneWindowManagerWrapper();
    protected com.android.server.policy.IPhoneWindowManagerExt mPhoneWindowManagerExt = (com.android.server.policy.IPhoneWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.IPhoneWindowManagerExt.class).base(this).create();
    protected android.os.ITheiaManagerExt mTheiaManagerExt = (android.os.ITheiaManagerExt) system.ext.loader.core.ExtLoader.type(android.os.ITheiaManagerExt.class).create();

    private class PolicyHandler extends android.os.Handler {
        private PolicyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 3:
                    com.android.server.policy.PhoneWindowManager.this.dispatchMediaKeyWithWakeLock((android.view.KeyEvent) msg.obj);
                    break;
                case 4:
                    com.android.server.policy.PhoneWindowManager.this.dispatchMediaKeyRepeatWithWakeLock((android.view.KeyEvent) msg.obj);
                    break;
                case 5:
                    android.util.Slog.w(com.android.server.policy.PhoneWindowManager.TAG, "Setting mKeyguardDrawComplete");
                    com.android.server.policy.PhoneWindowManager.this.finishKeyguardDrawn();
                    break;
                case 6:
                    android.util.Slog.w(com.android.server.policy.PhoneWindowManager.TAG, "Keyguard drawn timeout. Setting mKeyguardDrawComplete");
                    com.android.server.policy.PhoneWindowManager.this.finishKeyguardDrawn();
                    break;
                case 7:
                    int displayId = msg.arg1;
                    android.util.Slog.w(com.android.server.policy.PhoneWindowManager.TAG, "All windows drawn on display " + displayId);
                    android.os.Trace.asyncTraceEnd(32L, com.android.server.policy.PhoneWindowManager.TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD, displayId);
                    com.android.server.policy.PhoneWindowManager.this.finishWindowsDrawn(displayId);
                    break;
                case 9:
                    com.android.server.policy.PhoneWindowManager.this.showRecentApps(false);
                    break;
                case 10:
                    com.android.server.policy.PhoneWindowManager.this.showGlobalActionsInternal();
                    break;
                case 11:
                    com.android.server.policy.PhoneWindowManager.this.handleHideBootMessage();
                    break;
                case 12:
                    com.android.server.policy.PhoneWindowManager.this.launchVoiceAssistWithWakeLock();
                    break;
                case 15:
                    com.android.server.policy.PhoneWindowManager.this.showPictureInPictureMenuInternal();
                    break;
                case 16:
                    com.android.server.policy.PhoneWindowManager.this.handleScreenShot(msg.arg1);
                    break;
                case 17:
                    com.android.server.policy.PhoneWindowManager.this.accessibilityShortcutActivated();
                    break;
                case 18:
                    com.android.server.policy.PhoneWindowManager.this.requestBugreportForTv();
                    break;
                case 19:
                    if (com.android.server.policy.PhoneWindowManager.this.mAccessibilityShortcutController.isAccessibilityShortcutAvailable(false)) {
                        com.android.server.policy.PhoneWindowManager.this.accessibilityShortcutActivated();
                    }
                    break;
                case 20:
                    com.android.server.policy.PhoneWindowManager.this.mAutofillManagerInternal.onBackKeyPressed();
                    break;
                case 21:
                    android.view.KeyEvent event = (android.view.KeyEvent) msg.obj;
                    com.android.server.policy.PhoneWindowManager.this.sendSystemKeyToStatusBar(event);
                    event.recycle();
                    break;
                case 22:
                    com.android.server.policy.PhoneWindowManager.this.launchAllAppsAction();
                    break;
                case 23:
                    int deviceId = msg.arg1;
                    java.lang.Long eventTime = (java.lang.Long) msg.obj;
                    com.android.server.policy.PhoneWindowManager.this.launchAssistAction(null, deviceId, eventTime.longValue(), 7, 0);
                    break;
                case 24:
                    if (!com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().interceptRingerChordGesture()) {
                        com.android.server.policy.PhoneWindowManager.this.handleRingerChordGesture();
                        break;
                    }
                    break;
                case 25:
                    com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject object = (com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject) msg.obj;
                    com.android.server.policy.PhoneWindowManager.this.handleSwitchKeyboardLayout(object.keyEvent, object.direction, object.focusedToken);
                    break;
                case 26:
                    com.android.server.policy.PhoneWindowManager.this.handleKeyboardSystemEvent(com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.from(msg.arg1), (android.view.KeyEvent) msg.obj);
                    break;
                case 27:
                    int keyCode = msg.arg1;
                    long downTime = ((java.lang.Long) msg.obj).longValue();
                    com.android.server.policy.PhoneWindowManager.this.mDeferredKeyActionExecutor.setActionsExecutable(keyCode, downTime);
                    break;
            }
        }
    }

    class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        void observe() {
            android.content.ContentResolver resolver = com.android.server.policy.PhoneWindowManager.this.mContext.getContentResolver();
            resolver.registerContentObserver(android.provider.Settings.System.getUriFor("end_button_behavior"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("incall_power_button_behavior"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("incall_back_button_behavior"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("wake_gesture_enabled"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_off_timeout"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("default_input_method"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("volume_hush_gesture"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("system_navigation_keys_enabled"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_short_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_double_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_triple_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_long_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_long_press_duration_ms"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_very_long_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("stem_primary_button_short_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("stem_primary_button_double_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("stem_primary_button_triple_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("stem_primary_button_long_press"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("key_chord_power_volume_up"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("power_button_suppression_delay_after_gesture_wake"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("stylus_buttons_enabled"), false, this, -1);
            resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("nav_bar_kids_mode"), false, this, -1);
            com.android.server.policy.PhoneWindowManager.this.updateSettings();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            com.android.server.policy.PhoneWindowManager.this.updateSettings();
        }
    }

    class MyWakeGestureListener extends com.android.server.policy.WakeGestureListener {
        MyWakeGestureListener(android.content.Context context, android.os.Handler handler) {
            super(context, handler);
        }

        @Override // com.android.server.policy.WakeGestureListener
        public void onWakeUp() {
            synchronized (com.android.server.policy.PhoneWindowManager.this.mLock) {
                if (com.android.server.policy.PhoneWindowManager.this.shouldEnableWakeGestureLp()) {
                    com.android.server.policy.PhoneWindowManager.this.performHapticFeedback(1, false, "Wake Up");
                    com.android.server.policy.PhoneWindowManager.this.mWindowWakeUpPolicy.wakeUpFromWakeGesture();
                }
            }
        }
    }

    private static final class SwitchKeyboardLayoutMessageObject extends java.lang.Record {
        private final int direction;
        private final android.os.IBinder focusedToken;
        private final android.view.KeyEvent keyEvent;

        private SwitchKeyboardLayoutMessageObject(android.view.KeyEvent keyEvent, android.os.IBinder focusedToken, int direction) {
            this.keyEvent = keyEvent;
            this.focusedToken = focusedToken;
            this.direction = direction;
        }

        public int direction() {
            return this.direction;
        }

        @Override // java.lang.Record
        public final boolean equals(java.lang.Object o) {
            return (boolean) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "equals", java.lang.invoke.MethodType.methodType(java.lang.Boolean.TYPE, com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject.class, java.lang.Object.class), com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject.class, "keyEvent;focusedToken;direction", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->keyEvent:Landroid/view/KeyEvent;", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->focusedToken:Landroid/os/IBinder;", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->direction:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        public android.os.IBinder focusedToken() {
            return this.focusedToken;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "hashCode", java.lang.invoke.MethodType.methodType(java.lang.Integer.TYPE, com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject.class), com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject.class, "keyEvent;focusedToken;direction", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->keyEvent:Landroid/view/KeyEvent;", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->focusedToken:Landroid/os/IBinder;", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->direction:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        public android.view.KeyEvent keyEvent() {
            return this.keyEvent;
        }

        @Override // java.lang.Record
        public final java.lang.String toString() {
            return (java.lang.String) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "toString", java.lang.invoke.MethodType.methodType(java.lang.String.class, com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject.class), com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject.class, "keyEvent;focusedToken;direction", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->keyEvent:Landroid/view/KeyEvent;", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->focusedToken:Landroid/os/IBinder;", "FIELD:Lcom/android/server/policy/PhoneWindowManager$SwitchKeyboardLayoutMessageObject;->direction:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRingerChordGesture() {
        if (this.mRingerToggleChord == 0) {
            return;
        }
        getAudioManagerInternal();
        this.mAudioManagerInternal.silenceRingerModeInternal("volume_hush");
        android.provider.Settings.Secure.putInt(this.mContext.getContentResolver(), "hush_gesture_used", 1);
        this.mLogger.action(1440, this.mRingerToggleChord);
    }

    com.android.internal.statusbar.IStatusBarService getStatusBarService() {
        com.android.internal.statusbar.IStatusBarService iStatusBarService;
        synchronized (this.mServiceAcquireLock) {
            if (this.mStatusBarService == null) {
                this.mStatusBarService = com.android.internal.statusbar.IStatusBarService.Stub.asInterface(android.os.ServiceManager.getService("statusbar"));
            }
            iStatusBarService = this.mStatusBarService;
        }
        return iStatusBarService;
    }

    com.android.server.statusbar.StatusBarManagerInternal getStatusBarManagerInternal() {
        com.android.server.statusbar.StatusBarManagerInternal statusBarManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mStatusBarManagerInternal == null) {
                this.mStatusBarManagerInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
            }
            statusBarManagerInternal = this.mStatusBarManagerInternal;
        }
        return statusBarManagerInternal;
    }

    android.media.AudioManagerInternal getAudioManagerInternal() {
        android.media.AudioManagerInternal audioManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mAudioManagerInternal == null) {
                this.mAudioManagerInternal = (android.media.AudioManagerInternal) com.android.server.LocalServices.getService(android.media.AudioManagerInternal.class);
            }
            audioManagerInternal = this.mAudioManagerInternal;
        }
        return audioManagerInternal;
    }

    com.android.server.AccessibilityManagerInternal getAccessibilityManagerInternal() {
        com.android.server.AccessibilityManagerInternal accessibilityManagerInternal;
        synchronized (this.mServiceAcquireLock) {
            if (this.mAccessibilityManagerInternal == null) {
                this.mAccessibilityManagerInternal = (com.android.server.AccessibilityManagerInternal) com.android.server.LocalServices.getService(com.android.server.AccessibilityManagerInternal.class);
            }
            accessibilityManagerInternal = this.mAccessibilityManagerInternal;
        }
        return accessibilityManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean backKeyPress() {
        android.telecom.TelecomManager telecomManager;
        this.mLogger.count("key_back_press", 1);
        boolean handled = this.mBackKeyHandled;
        if (this.mHasFeatureWatch && (telecomManager = getTelecommService()) != null) {
            if (telecomManager.isRinging()) {
                telecomManager.silenceRinger();
                return false;
            }
            if ((1 & this.mIncallBackBehavior) != 0 && telecomManager.isInCall()) {
                return telecomManager.endCall();
            }
        }
        if (this.mAutofillManagerInternal != null) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(20));
        }
        return handled;
    }

    private void interceptPowerKeyDown(android.view.KeyEvent event, boolean interactive) {
        if (!this.mPowerKeyWakeLock.isHeld()) {
            this.mPowerKeyWakeLock.acquire(200000L);
            this.mPhoneWindowManagerExt.hookForInputLogV("interceptPowerKeyDown mPowerKeyWakeLock acquired");
        }
        this.mWindowManagerFuncs.onPowerKeyDown(interactive);
        this.mPhoneWindowManagerExt.onPwkPressed();
        getTelecommService();
        boolean hungUp = this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyForTelephone(event, interactive);
        this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyForAlarm();
        boolean handledByPowerManager = this.mPowerManagerInternal.interceptPowerKeyDown(event);
        sendSystemKeyToStatusBarAsync(event);
        boolean z = false;
        this.mPowerKeyHandled = this.mPowerKeyHandled || hungUp || handledByPowerManager || this.mKeyCombinationManager.isPowerKeyIntercepted();
        this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyDown(event, interactive);
        if (!interactive && !this.mPhoneWindowManagerExt.interceptPowerKeyDown()) {
            z = true;
        }
        boolean shouldWakeUp = z;
        if (!this.mPowerKeyHandled) {
            if (!interactive && shouldWakeUp) {
                wakeUpFromWakeKey(event);
            }
        } else {
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("interceptPowerKeyDown keyhandled", true, true);
            if (this.mSingleKeyGestureDetector.isKeyIntercepted(26)) {
                android.util.Slog.d(TAG, "Skip power key gesture for other policy has handled it.");
                this.mSingleKeyGestureDetector.reset();
            }
        }
        this.mPhoneWindowManagerExt.startHwShutdownDectect();
    }

    private void interceptPowerKeyUp(android.view.KeyEvent event, boolean canceled) {
        sendSystemKeyToStatusBarAsync(event);
        boolean handled = canceled || this.mPowerKeyHandled || this.mPhoneWindowManagerExt.getInputExtension().getSpeechLongPressHandle();
        this.mPhoneWindowManagerExt.getInputExtension().interceptPowerKeyUp(handled);
        this.mPhoneWindowManagerExt.clearHwShutdownDectect();
        this.mPhoneWindowManagerExt.onPwkReleased();
        this.mPhoneWindowManagerExt.interceptPowerKeyUp(handled, this.mWindowManagerFuncs);
        if (!handled && (event.getFlags() & 128) == 0) {
            android.os.Handler handler = this.mHandler;
            final com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs windowManagerFuncs = this.mWindowManagerFuncs;
            java.util.Objects.requireNonNull(windowManagerFuncs);
            handler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    windowManagerFuncs.triggerAnimationFailsafe();
                }
            });
        }
        finishPowerKeyPress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishPowerKeyPress() {
        this.mPowerKeyHandled = false;
        if (this.mPowerKeyWakeLock.isHeld()) {
            this.mPowerKeyWakeLock.release();
            this.mPhoneWindowManagerExt.hookForInputLogV("interceptPowerKeyDown mPowerKeyWakeLock released");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void powerPress(final long eventTime, int count, int displayId) {
        if (count == 1) {
            this.mSideFpsEventHandler.notifyPowerPressed();
        }
        if (this.mDefaultDisplayPolicy.isScreenOnEarly() && !this.mDefaultDisplayPolicy.isScreenOnFully()) {
            android.util.Slog.i(TAG, "Suppressed redundant power key press while already in the process of turning the screen on.");
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("Screen turning on ignore powerpress", false, true);
        }
        boolean interactive = this.mPhoneWindowManagerExt.isDisplaysOnLocked(this.mDefaultDisplay);
        android.util.Slog.d(TAG, "powerPress: eventTime=" + eventTime + " interactive=" + interactive + " count=" + count + " mShortPressOnPowerBehavior=" + this.mShortPressOnPowerBehavior);
        if (count == 2) {
            powerMultiPressAction(eventTime, interactive, this.mDoublePressOnPowerBehavior);
            return;
        }
        if (count == 3) {
            powerMultiPressAction(eventTime, interactive, this.mTriplePressOnPowerBehavior);
            return;
        }
        if (count > 3 && count <= getMaxMultiPressPowerCount()) {
            android.util.Slog.d(TAG, "No behavior defined for power press count " + count);
            return;
        }
        if (count == 1 && shouldHandleShortPressPowerAction(interactive, eventTime)) {
            if (!LTW_DISABLE && this.mPhoneWindowManagerExt.getBlackScreenWindowManagerPowerKeyState()) {
                android.util.Slog.d(TAG, "intercept power key down event success!");
                return;
            }
            switch (this.mShortPressOnPowerBehavior) {
                case 1:
                    sleepDefaultDisplayFromPowerButton(eventTime, 0);
                    break;
                case 2:
                    sleepDefaultDisplayFromPowerButton(eventTime, 1);
                    break;
                case 3:
                    if (sleepDefaultDisplayFromPowerButton(eventTime, 1)) {
                        launchHomeFromHotKey(0);
                    }
                    break;
                case 4:
                    shortPressPowerGoHome();
                    break;
                case 5:
                    if (this.mDismissImeOnBackKeyPressed) {
                        com.android.server.inputmethod.InputMethodManagerInternal.get().hideAllInputMethods(17, displayId);
                    } else {
                        shortPressPowerGoHome();
                    }
                    break;
                case 6:
                    if (this.mKeyguardDelegate == null || !this.mKeyguardDelegate.hasKeyguard() || !this.mKeyguardDelegate.isSecure(this.mCurrentUserId) || keyguardOn()) {
                        sleepDefaultDisplayFromPowerButton(eventTime, 0);
                    } else {
                        lockNow(null);
                    }
                    break;
                case 7:
                    attemptToDreamFromShortPowerButtonPress(true, new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$powerPress$0(eventTime);
                        }
                    });
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$powerPress$0(long eventTime) {
        sleepDefaultDisplayFromPowerButton(eventTime, 0);
    }

    private boolean shouldHandleShortPressPowerAction(boolean interactive, long eventTime) {
        if (this.mSupportShortPressPowerWhenDefaultDisplayOn) {
            boolean defaultDisplayOn = android.view.Display.isOnState(this.mDefaultDisplay.getState());
            boolean beganFromDefaultDisplayOn = this.mSingleKeyGestureDetector.beganFromDefaultDisplayOn();
            if (defaultDisplayOn && beganFromDefaultDisplayOn) {
                return true;
            }
            android.util.Slog.v(TAG, "Ignoring short press of power button because the default display is not on. defaultDisplayOn=" + defaultDisplayOn + ", beganFromDefaultDisplayOn=" + beganFromDefaultDisplayOn);
            return false;
        }
        boolean beganFromNonInteractive = this.mSingleKeyGestureDetector.beganFromNonInteractive();
        if (!interactive || beganFromNonInteractive) {
            android.util.Slog.v(TAG, "Ignoring short press of power button because the device is not interactive. interactive=" + interactive + ", beganFromNonInteractive=" + beganFromNonInteractive);
            return false;
        }
        if (!this.mSideFpsEventHandler.shouldConsumeSinglePress(eventTime)) {
            return true;
        }
        android.util.Slog.i(TAG, "Suppressing power key because the user is interacting with the fingerprint sensor");
        return false;
    }

    private void attemptToDreamFromShortPowerButtonPress(boolean isScreenOn, java.lang.Runnable noDreamAction) {
        if (this.mShortPressOnPowerBehavior != 7) {
            noDreamAction.run();
            return;
        }
        android.service.dreams.DreamManagerInternal dreamManagerInternal = getDreamManagerInternal();
        if (dreamManagerInternal == null || !dreamManagerInternal.canStartDreaming(isScreenOn)) {
            android.util.Slog.d(TAG, "Can't start dreaming when attempting to dream from short power press (isScreenOn=" + isScreenOn + ")");
            noDreamAction.run();
        } else {
            synchronized (this.mLock) {
                this.mLockAfterDreamingTransitionFinished = this.mLockPatternUtils.getPowerButtonInstantlyLocks(this.mCurrentUserId);
            }
            dreamManagerInternal.requestDream();
        }
    }

    private boolean sleepDefaultDisplayFromPowerButton(long eventTime, int flags) {
        android.os.PowerManager.WakeData lastWakeUp = this.mPowerManagerInternal.getLastWakeup();
        if (lastWakeUp != null && (lastWakeUp.wakeReason == 4 || lastWakeUp.wakeReason == 16 || lastWakeUp.wakeReason == 17)) {
            long now = android.os.SystemClock.uptimeMillis();
            if (this.mPowerButtonSuppressionDelayMillis > 0 && now < lastWakeUp.wakeTime + ((long) this.mPowerButtonSuppressionDelayMillis)) {
                android.util.Slog.i(TAG, "Sleep from power button suppressed. Time since gesture: " + (now - lastWakeUp.wakeTime) + "ms");
                this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("Ignored goToSleepFromPowerButton for gesture wakeup", false, true);
                return false;
            }
        }
        if (this.mPhoneWindowManagerExt.isSleepByPowerButtonDisabled() && isScreenOn()) {
            android.util.Slog.i(TAG, "Sleep from power button suppressed due to customize disabled!");
            return false;
        }
        this.mPhoneWindowManagerExt.notePowerkeyProcessStagePoint("POWERKEY_goToSleepFromPowerButton");
        sleepDefaultDisplay(eventTime, 4, flags);
        return true;
    }

    private void sleepDefaultDisplay(long eventTime, int reason, int flags) {
        this.mRequestedOrSleepingDefaultDisplay = true;
        this.mPhoneWindowManagerExt.keyEventSpendTimeEventLog(eventTime);
        this.mPowerManager.goToSleep(eventTime, reason, flags);
    }

    private void shortPressPowerGoHome() {
        launchHomeFromHotKey(0, true, false);
        if (isKeyguardShowingAndNotOccluded()) {
            this.mKeyguardDelegate.onShortPowerPressedGoHome();
        }
    }

    private void powerMultiPressAction(long eventTime, boolean interactive, int behavior) {
        switch (behavior) {
            case 1:
                if (!isUserSetupComplete()) {
                    android.util.Slog.i(TAG, "Ignoring toggling theater mode - device not setup.");
                    break;
                } else if (isTheaterModeEnabled()) {
                    android.util.Slog.i(TAG, "Toggling theater mode off.");
                    android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "theater_mode_on", 0);
                    if (!interactive) {
                        wakeUpFromWakeKey(eventTime, 26, false);
                    }
                    break;
                } else {
                    android.util.Slog.i(TAG, "Toggling theater mode on.");
                    android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "theater_mode_on", 1);
                    if (this.mGoToSleepOnButtonPressTheaterMode && interactive) {
                        sleepDefaultDisplay(eventTime, 4, 0);
                        break;
                    }
                }
                break;
            case 2:
                android.util.Slog.i(TAG, "Starting brightness boost.");
                if (!interactive) {
                    wakeUpFromWakeKey(eventTime, 26, false);
                }
                this.mPowerManager.boostScreenBrightness(eventTime);
                break;
            case 3:
                launchTargetActivityOnMultiPressPower();
                break;
        }
    }

    private void launchTargetActivityOnMultiPressPower() {
        if (DEBUG_INPUT) {
            android.util.Slog.d(TAG, "Executing the double press power action.");
        }
        if (this.mPowerDoublePressTargetActivity != null) {
            android.content.Intent intent = new android.content.Intent();
            intent.setComponent(this.mPowerDoublePressTargetActivity);
            boolean z = false;
            android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveActivity(intent, 0);
            if (resolveInfo != null) {
                if (this.mKeyguardDelegate != null && this.mKeyguardDelegate.isShowing()) {
                    z = true;
                }
                boolean keyguardActive = z;
                intent.addFlags(270532608);
                if (!keyguardActive) {
                    startActivityAsUser(intent, android.os.UserHandle.CURRENT_OR_SELF);
                    return;
                } else {
                    this.mKeyguardDelegate.dismissKeyguardToLaunch(intent);
                    return;
                }
            }
            android.util.Slog.e(TAG, "Could not resolve activity with : " + this.mPowerDoublePressTargetActivity.flattenToString() + " name.");
        }
    }

    private int getLidBehavior() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "lid_behavior", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMaxMultiPressPowerCount() {
        if (this.mHasFeatureWatch && com.android.server.GestureLauncherService.isEmergencyGestureSettingEnabled(this.mContext, android.app.ActivityManager.getCurrentUser())) {
            return 5;
        }
        if (this.mTriplePressOnPowerBehavior != 0) {
            return 3;
        }
        if (this.mDoublePressOnPowerBehavior != 0) {
            return 2;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void powerLongPress(long eventTime) {
        int behavior = getResolvedLongPressOnPowerBehavior();
        android.util.Slog.d(TAG, "powerLongPress: eventTime=" + eventTime + " behavior =" + behavior + " mLongPressOnPowerBehavior=" + this.mLongPressOnPowerBehavior);
        switch (behavior) {
            case 1:
                this.mPowerKeyHandled = true;
                if (!this.mPhoneWindowManagerExt.getInputExtension().interceptLongPowerPress()) {
                    showGlobalActions();
                }
                break;
            case 2:
            case 3:
                this.mPowerKeyHandled = true;
                if (!android.app.ActivityManager.isUserAMonkey()) {
                    performHapticFeedback(10003, false, "Power - Long Press - Shut Off");
                    sendCloseSystemWindows(SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS);
                    this.mWindowManagerFuncs.shutdown(behavior == 2);
                }
                break;
            case 4:
                this.mPowerKeyHandled = true;
                performHapticFeedback(10003, false, "Power - Long Press - Go To Voice Assist");
                launchVoiceAssist(this.mAllowStartActivityForLongPressOnPowerDuringSetup);
                break;
            case 5:
                this.mPowerKeyHandled = true;
                performHapticFeedback(10002, false, "Power - Long Press - Go To Assistant");
                launchAssistAction(null, -2, eventTime, 6, 1);
                break;
        }
        if (this.mPowerKeyHandled) {
            this.mPhoneWindowManagerExt.notePowerkeyProcessEvent("powerLongPress handled behavior is " + behavior, true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void powerVeryLongPress() {
        android.util.Slog.v(TAG, " powerVeryLongPress b=" + this.mVeryLongPressOnPowerBehavior);
        switch (this.mVeryLongPressOnPowerBehavior) {
            case 1:
                this.mPowerKeyHandled = true;
                performHapticFeedback(10003, false, "Power - Very Long Press - Show Global Actions");
                showGlobalActions();
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void backLongPress() {
        this.mBackKeyHandled = true;
        switch (this.mLongPressOnBackBehavior) {
            case 1:
                launchVoiceAssist(false);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void accessibilityShortcutActivated() {
        this.mAccessibilityShortcutController.performAccessibilityShortcut();
    }

    private void sleepPress() {
        if (this.mShortPressOnSleepBehavior == 1) {
            launchHomeFromHotKey(0, false, true);
        }
    }

    private void sleepRelease(long eventTime) {
        android.telecom.TelecomManager telecomManager;
        if (this.mSilenceRingerOnSleepKey && (telecomManager = getTelecommService()) != null && telecomManager.isRinging()) {
            telecomManager.silenceRinger();
            android.util.Slog.i(TAG, "sleepRelease() silence ringer");
        } else {
            switch (this.mShortPressOnSleepBehavior) {
                case 0:
                case 1:
                    android.util.Slog.i(TAG, "sleepRelease() calling goToSleep(GO_TO_SLEEP_REASON_SLEEP_BUTTON)");
                    sleepDefaultDisplay(eventTime, 6, 0);
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getResolvedLongPressOnPowerBehavior() {
        if (android.os.FactoryTest.isLongPressOnPowerOffEnabled()) {
            return 3;
        }
        if (this.mLongPressOnPowerBehavior == 5 && !isDeviceProvisioned()) {
            return 1;
        }
        if (this.mLongPressOnPowerBehavior == 4 && !isLongPressToAssistantEnabled(this.mContext)) {
            return 0;
        }
        return this.mLongPressOnPowerBehavior;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stemPrimaryPress(int count) {
        android.util.Slog.d(TAG, "stemPrimaryPress: " + count);
        if (count == 3) {
            stemPrimaryTriplePressAction(this.mTriplePressOnStemPrimaryBehavior);
        } else if (count == 2) {
            stemPrimaryDoublePressAction(this.mDoublePressOnStemPrimaryBehavior);
        } else if (count == 1) {
            stemPrimarySinglePressAction(this.mShortPressOnStemPrimaryBehavior);
        }
    }

    private void stemPrimarySinglePressAction(int behavior) {
        android.util.Slog.d(TAG, "stemPrimarySinglePressAction: behavior=" + behavior);
        if (behavior == 0) {
        }
        boolean keyguardActive = this.mKeyguardDelegate != null && this.mKeyguardDelegate.isShowing();
        if (keyguardActive) {
            this.mKeyguardDelegate.onSystemKeyPressed(264);
            android.util.Slog.d(TAG, "stemPrimarySinglePressAction: skip due to keyguard");
            return;
        }
        switch (behavior) {
            case 1:
                android.content.Intent allAppsIntent = new android.content.Intent("android.intent.action.ALL_APPS");
                allAppsIntent.addFlags(270532608);
                startActivityAsUser(allAppsIntent, android.os.UserHandle.CURRENT_OR_SELF);
                break;
            case 2:
                if (this.mPrimaryShortPressTargetActivity != null) {
                    android.content.Intent targetActivityIntent = new android.content.Intent();
                    targetActivityIntent.setComponent(this.mPrimaryShortPressTargetActivity);
                    android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveActivity(targetActivityIntent, 0);
                    if (resolveInfo != null) {
                        targetActivityIntent.addFlags(270548992);
                        startActivityAsUser(targetActivityIntent, android.os.UserHandle.CURRENT_OR_SELF);
                    } else {
                        android.util.Slog.wtf(TAG, "Could not resolve activity with : " + this.mPrimaryShortPressTargetActivity.flattenToString() + " name.");
                    }
                } else {
                    android.util.Slog.wtf(TAG, "mPrimaryShortPressTargetActivity must not be null and correctly specified");
                }
                break;
        }
    }

    private void stemPrimaryDoublePressAction(int behavior) {
        boolean keyguardActive;
        android.util.Slog.d(TAG, "stemPrimaryDoublePressAction: " + behavior);
        switch (behavior) {
            case 1:
                if (this.mKeyguardDelegate == null) {
                    keyguardActive = false;
                } else {
                    keyguardActive = this.mKeyguardDelegate.isShowing();
                }
                if (!keyguardActive) {
                    performStemPrimaryDoublePressSwitchToRecentTask();
                }
                break;
        }
    }

    private void stemPrimaryTriplePressAction(int behavior) {
        android.util.Slog.d(TAG, "stemPrimaryTriplePressAction: " + behavior);
        switch (behavior) {
            case 1:
                this.mTalkbackShortcutController.toggleTalkback(this.mCurrentUserId);
                if (this.mTalkbackShortcutController.isTalkBackShortcutGestureEnabled()) {
                    performHapticFeedback(16, false, "Stem primary - Triple Press - Toggle Accessibility");
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stemPrimaryLongPress(long eventTime) {
        android.util.Slog.d(TAG, "stemPrimaryLongPress: " + this.mLongPressOnStemPrimaryBehavior);
        switch (this.mLongPressOnStemPrimaryBehavior) {
            case 1:
                launchAssistAction(null, -2, eventTime, 0, 1);
                break;
        }
    }

    void performStemPrimaryDoublePressSwitchToRecentTask() {
        android.app.ActivityManager.RecentTaskInfo targetTask = this.mBackgroundRecentTaskInfoOnStemPrimarySingleKeyUp;
        if (targetTask == null) {
            if (DEBUG_INPUT) {
                android.util.Slog.w(TAG, "No recent task available! Show wallpaper.");
            }
            goHome();
        } else {
            if (DEBUG_INPUT) {
                android.util.Slog.d(TAG, "Starting task from recents. id=" + targetTask.id + ", persistentId=" + targetTask.persistentId + ", topActivity=" + targetTask.topActivity + ", baseIntent=" + targetTask.baseIntent);
            }
            try {
                this.mActivityManagerService.startActivityFromRecents(targetTask.persistentId, (android.os.Bundle) null);
            } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
                android.util.Slog.e(TAG, "Failed to start task " + targetTask.persistentId + " from recents", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMaxMultiPressStemPrimaryCount() {
        switch (this.mTriplePressOnStemPrimaryBehavior) {
            case 1:
                if (this.mTalkbackShortcutController.isTalkBackShortcutGestureEnabled()) {
                    return 3;
                }
                break;
        }
        if (this.mDoublePressOnStemPrimaryBehavior != 0) {
            return 2;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasLongPressOnPowerBehavior() {
        return getResolvedLongPressOnPowerBehavior() != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasVeryLongPressOnPowerBehavior() {
        return this.mVeryLongPressOnPowerBehavior != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasLongPressOnBackBehavior() {
        return this.mLongPressOnBackBehavior != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasLongPressOnStemPrimaryBehavior() {
        return this.mLongPressOnStemPrimaryBehavior != 0;
    }

    private boolean hasStemPrimaryBehavior() {
        int defaultShortPressOnStemPrimaryBehavior = this.mContext.getResources().getInteger(android.R.integer.config_satellite_nb_iot_inactivity_timeout_millis);
        int defaultLongPressOnStemPrimaryBehavior = this.mContext.getResources().getInteger(android.R.integer.config_keyguardDrawnTimeout);
        return (getMaxMultiPressStemPrimaryCount() <= 1 && defaultLongPressOnStemPrimaryBehavior == 0 && defaultShortPressOnStemPrimaryBehavior == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptScreenshotChord(int source, long pressDelay) {
        this.mHandler.removeMessages(16);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(16, source, 0), pressDelay);
        this.mPhoneWindowManagerExt.interceptScreenshotChord();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptAccessibilityShortcutChord() {
        this.mHandler.removeMessages(17);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(17), getAccessibilityShortcutTimeout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptRingerToggleChord() {
        this.mHandler.removeMessages(24);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(24), getRingerToggleChordDelay());
    }

    private long getAccessibilityShortcutTimeout() {
        android.view.ViewConfiguration config = android.view.ViewConfiguration.get(this.mContext);
        boolean hasDialogShown = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "accessibility_shortcut_dialog_shown", 0, this.mCurrentUserId) != 0;
        boolean skipTimeoutRestriction = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "skip_accessibility_shortcut_dialog_timeout_restriction", 0, this.mCurrentUserId) != 0;
        if (hasDialogShown || skipTimeoutRestriction) {
            return config.getAccessibilityShortcutKeyTimeoutAfterConfirmation();
        }
        return config.getAccessibilityShortcutKeyTimeout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getScreenshotChordLongPressDelay() {
        if (this.mPhoneWindowManagerExt.isCustomize()) {
            return this.mPhoneWindowManagerExt.hookScreenshotChordLongPressDelay();
        }
        long delayMs = android.provider.DeviceConfig.getLong("systemui", "screenshot_keychord_delay", android.view.ViewConfiguration.get(this.mContext).getScreenshotChordKeyTimeout());
        if (this.mKeyguardDelegate.isShowing()) {
            return (long) (delayMs * KEYGUARD_SCREENSHOT_CHORD_DELAY_MULTIPLIER);
        }
        return delayMs;
    }

    private long getRingerToggleChordDelay() {
        return android.view.ViewConfiguration.getTapTimeout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingScreenshotChordAction() {
        this.mHandler.removeMessages(16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingAccessibilityShortcutAction() {
        this.mHandler.removeMessages(17);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPendingRingerToggleChordAction() {
        this.mHandler.removeMessages(24);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenShot(int source) {
        this.mDefaultDisplayPolicy.takeScreenshot(1, source);
        this.mPhoneWindowManagerExt.getTpInfo("ctl.start", "gettpinfo");
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showGlobalActions() {
        this.mHandler.removeMessages(10);
        this.mHandler.sendEmptyMessage(10);
    }

    void showGlobalActionsInternal() {
        if (this.mGlobalActions == null) {
            this.mGlobalActions = this.mGlobalActionsFactory.get();
        }
        boolean keyguardShowing = isKeyguardShowingAndNotOccluded();
        this.mGlobalActions.showDialog(keyguardShowing, isDeviceProvisioned());
        this.mPowerManager.userActivity(android.os.SystemClock.uptimeMillis(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelGlobalActionsAction() {
        this.mHandler.removeMessages(10);
    }

    boolean isDeviceProvisioned() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isUserSetupComplete() {
        boolean isSetupComplete = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, -2) != 0;
        if (this.mHasFeatureLeanback) {
            return isSetupComplete & isTvUserSetupComplete();
        }
        if (this.mHasFeatureAuto) {
            return isSetupComplete & isAutoUserSetupComplete();
        }
        return isSetupComplete;
    }

    private boolean isAutoUserSetupComplete() {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "android.car.SETUP_WIZARD_IN_PROGRESS", 0, -2) == 0;
    }

    private boolean isTvUserSetupComplete() {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "tv_user_setup_complete", 0, -2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShortPressOnHome(android.view.KeyEvent event) {
        logKeyboardSystemsEvent(event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.HOME);
        com.android.server.policy.PhoneWindowManager.HdmiControl hdmiControl = getHdmiControl();
        if (hdmiControl != null) {
            hdmiControl.turnOnTv();
        }
        android.service.dreams.DreamManagerInternal dreamManagerInternal = getDreamManagerInternal();
        if (dreamManagerInternal != null && dreamManagerInternal.isDreaming()) {
            this.mDreamManagerInternal.stopDream(false, "short press on home");
            android.util.Log.i(TAG, "Handle short press on home, when there is a dream running.");
        } else {
            launchHomeFromHotKey(event.getDisplayId());
        }
    }

    private com.android.server.policy.PhoneWindowManager.HdmiControl getHdmiControl() {
        if (this.mHdmiControl == null) {
            if (!this.mHasFeatureHdmiCec) {
                return null;
            }
            android.hardware.hdmi.HdmiControlManager manager = (android.hardware.hdmi.HdmiControlManager) this.mContext.getSystemService("hdmi_control");
            android.hardware.hdmi.HdmiPlaybackClient client = null;
            if (manager != null) {
                client = manager.getPlaybackClient();
            }
            this.mHdmiControl = new com.android.server.policy.PhoneWindowManager.HdmiControl(client);
        }
        return this.mHdmiControl;
    }

    private static class HdmiControl {
        private final android.hardware.hdmi.HdmiPlaybackClient mClient;

        private HdmiControl(android.hardware.hdmi.HdmiPlaybackClient client) {
            this.mClient = client;
        }

        public void turnOnTv() {
            if (this.mClient == null) {
                return;
            }
            this.mClient.oneTouchPlay(new android.hardware.hdmi.HdmiPlaybackClient.OneTouchPlayCallback() { // from class: com.android.server.policy.PhoneWindowManager.HdmiControl.1
                public void onComplete(int result) {
                    if (result != 0) {
                        android.util.Log.w(com.android.server.policy.PhoneWindowManager.TAG, "One touch play failed: " + result);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchAllAppsAction() {
        android.content.Intent intent = new android.content.Intent("android.intent.action.ALL_APPS");
        if (this.mHasFeatureLeanback) {
            android.content.Intent intentLauncher = new android.content.Intent("android.intent.action.MAIN");
            intentLauncher.addCategory("android.intent.category.HOME");
            android.content.pm.ResolveInfo resolveInfo = this.mPackageManager.resolveActivityAsUser(intentLauncher, 1048576, this.mCurrentUserId);
            if (resolveInfo != null) {
                intent.setPackage(resolveInfo.activityInfo.packageName);
            }
        }
        startActivityAsUser(intent, android.os.UserHandle.CURRENT);
    }

    private void launchAllAppsViaA11y() {
        com.android.server.AccessibilityManagerInternal accessibilityManager = getAccessibilityManagerInternal();
        if (accessibilityManager != null) {
            accessibilityManager.performSystemAction(14);
        }
        dismissKeyboardShortcutsMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleNotificationPanel() {
        com.android.internal.statusbar.IStatusBarService statusBarService = getStatusBarService();
        if (isUserSetupComplete() && statusBarService != null) {
            try {
                statusBarService.togglePanel();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void showSystemSettings() {
        startActivityAsUser(new android.content.Intent("android.settings.SETTINGS"), android.os.UserHandle.CURRENT_OR_SELF);
    }

    private void showPictureInPictureMenu(android.view.KeyEvent event) {
        if (DEBUG_INPUT) {
            android.util.Log.d(TAG, "showPictureInPictureMenu event=" + event);
        }
        this.mHandler.removeMessages(15);
        android.os.Message msg = this.mHandler.obtainMessage(15);
        msg.setAsynchronous(true);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPictureInPictureMenuInternal() {
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.showPictureInPictureMenu();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DisplayHomeButtonHandler {
        private final int mDisplayId;
        private boolean mHomeConsumed;
        private final java.lang.Runnable mHomeDoubleTapTimeoutRunnable = new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler.this.mPendingHomeKeyEvent != null) {
                    com.android.server.policy.PhoneWindowManager.this.handleShortPressOnHome(com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler.this.mPendingHomeKeyEvent);
                    com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler.this.mPendingHomeKeyEvent = null;
                }
            }
        };
        private boolean mHomePressed;
        private android.view.KeyEvent mPendingHomeKeyEvent;

        DisplayHomeButtonHandler(int displayId) {
            this.mDisplayId = displayId;
        }

        boolean handleHomeButton(android.os.IBinder focusedToken, final android.view.KeyEvent event) {
            boolean keyguardOn = com.android.server.policy.PhoneWindowManager.this.keyguardOn();
            int repeatCount = event.getRepeatCount();
            boolean down = event.getAction() == 0;
            boolean canceled = event.isCanceled();
            if (com.android.server.policy.PhoneWindowManager.DEBUG_INPUT) {
                android.util.Log.d(com.android.server.policy.PhoneWindowManager.TAG, java.lang.String.format("handleHomeButton in display#%d mHomePressed = %b", java.lang.Integer.valueOf(this.mDisplayId), java.lang.Boolean.valueOf(this.mHomePressed)));
            }
            com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.onRecentClicked();
            if (!down) {
                if (this.mDisplayId == 0) {
                    com.android.server.policy.PhoneWindowManager.this.cancelPreloadRecentApps();
                }
                this.mHomePressed = false;
                if (this.mHomeConsumed) {
                    this.mHomeConsumed = false;
                    return true;
                }
                if (canceled) {
                    android.util.Log.i(com.android.server.policy.PhoneWindowManager.TAG, "Ignoring HOME; event canceled.");
                    return true;
                }
                if (com.android.server.policy.PhoneWindowManager.this.mDoubleTapOnHomeBehavior != 0 && (com.android.server.policy.PhoneWindowManager.this.mDoubleTapOnHomeBehavior != 2 || com.android.server.policy.PhoneWindowManager.this.mPictureInPictureVisible)) {
                    com.android.server.policy.PhoneWindowManager.this.mHandler.removeCallbacks(this.mHomeDoubleTapTimeoutRunnable);
                    this.mPendingHomeKeyEvent = event;
                    com.android.server.policy.PhoneWindowManager.this.mHandler.postDelayed(this.mHomeDoubleTapTimeoutRunnable, android.view.ViewConfiguration.getDoubleTapTimeout());
                    return true;
                }
                com.android.server.policy.PhoneWindowManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$DisplayHomeButtonHandler$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleHomeButton$0(event);
                    }
                });
                return true;
            }
            com.android.internal.policy.KeyInterceptionInfo info = com.android.server.policy.PhoneWindowManager.this.mWindowManagerInternal.getKeyInterceptionInfoFromToken(focusedToken);
            if (info != null) {
                if (info.layoutParamsType == 2009 || (info.layoutParamsType == 2040 && com.android.server.policy.PhoneWindowManager.this.isKeyguardShowing())) {
                    return false;
                }
                for (int t : com.android.server.policy.PhoneWindowManager.WINDOW_TYPES_WHERE_HOME_DOESNT_WORK) {
                    if (info.layoutParamsType == t) {
                        return true;
                    }
                }
            }
            if (repeatCount == 0) {
                this.mHomePressed = true;
                if (this.mPendingHomeKeyEvent != null) {
                    this.mPendingHomeKeyEvent = null;
                    com.android.server.policy.PhoneWindowManager.this.mHandler.removeCallbacks(this.mHomeDoubleTapTimeoutRunnable);
                    com.android.server.policy.PhoneWindowManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$DisplayHomeButtonHandler$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$handleHomeButton$1(event);
                        }
                    });
                } else if (com.android.server.policy.PhoneWindowManager.this.mDoubleTapOnHomeBehavior == 1 && this.mDisplayId == 0) {
                    com.android.server.policy.PhoneWindowManager.this.preloadRecentApps();
                }
            } else if ((event.getFlags() & 128) != 0 && !keyguardOn) {
                com.android.server.policy.PhoneWindowManager.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$DisplayHomeButtonHandler$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$handleHomeButton$2(event);
                    }
                });
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleHomeButton$0(android.view.KeyEvent event) {
            com.android.server.policy.PhoneWindowManager.this.handleShortPressOnHome(event);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: handleDoubleTapOnHome, reason: merged with bridge method [inline-methods] */
        public void lambda$handleHomeButton$1(android.view.KeyEvent event) {
            if (this.mHomeConsumed) {
            }
            switch (com.android.server.policy.PhoneWindowManager.this.mDoubleTapOnHomeBehavior) {
                case 1:
                    com.android.server.policy.PhoneWindowManager.this.logKeyboardSystemsEvent(event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.APP_SWITCH);
                    this.mHomeConsumed = true;
                    com.android.server.policy.PhoneWindowManager.this.toggleRecentApps();
                    break;
                case 2:
                    this.mHomeConsumed = true;
                    com.android.server.policy.PhoneWindowManager.this.showPictureInPictureMenuInternal();
                    break;
                default:
                    android.util.Log.w(com.android.server.policy.PhoneWindowManager.TAG, "No action or undefined behavior for double tap home: " + com.android.server.policy.PhoneWindowManager.this.mDoubleTapOnHomeBehavior);
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: handleLongPressOnHome, reason: merged with bridge method [inline-methods] */
        public void lambda$handleHomeButton$2(android.view.KeyEvent event) {
            if (this.mHomeConsumed || com.android.server.policy.PhoneWindowManager.this.mLongPressOnHomeBehavior == 0 || com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().interceptLongHomePress()) {
                return;
            }
            this.mHomeConsumed = true;
            switch (com.android.server.policy.PhoneWindowManager.this.mLongPressOnHomeBehavior) {
                case 1:
                    com.android.server.policy.PhoneWindowManager.this.logKeyboardSystemsEvent(event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.ALL_APPS);
                    com.android.server.policy.PhoneWindowManager.this.launchAllAppsAction();
                    break;
                case 2:
                    com.android.server.policy.PhoneWindowManager.this.logKeyboardSystemsEvent(event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.LAUNCH_ASSISTANT);
                    com.android.server.policy.PhoneWindowManager.this.launchAssistAction(null, event.getDeviceId(), event.getEventTime(), 5, 1);
                    break;
                case 3:
                    com.android.server.policy.PhoneWindowManager.this.logKeyboardSystemsEvent(event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.TOGGLE_NOTIFICATION_PANEL);
                    com.android.server.policy.PhoneWindowManager.this.toggleNotificationPanel();
                    break;
                default:
                    android.util.Log.w(com.android.server.policy.PhoneWindowManager.TAG, "Undefined long press on home behavior: " + com.android.server.policy.PhoneWindowManager.this.mLongPressOnHomeBehavior);
                    break;
            }
        }

        public java.lang.String toString() {
            return java.lang.String.format("mDisplayId = %d, mHomePressed = %b", java.lang.Integer.valueOf(this.mDisplayId), java.lang.Boolean.valueOf(this.mHomePressed));
        }
    }

    private boolean isRoundWindow() {
        return this.mContext.getResources().getConfiguration().isScreenRound();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setDefaultDisplay(com.android.server.policy.WindowManagerPolicy.DisplayContentInfo displayContentInfo) {
        this.mDefaultDisplay = displayContentInfo.getDisplay();
        this.mDefaultDisplayRotation = displayContentInfo.getDisplayRotation();
        this.mDefaultDisplayPolicy = this.mDefaultDisplayRotation.getDisplayPolicy();
        this.mPhoneWindowManagerSocExt.hookSetDefaultDisplay(this.mDefaultDisplayPolicy);
    }

    static class Injector {
        private final android.content.Context mContext;
        private final com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs mWindowManagerFuncs;

        Injector(android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs funcs) {
            this.mContext = context;
            this.mWindowManagerFuncs = funcs;
        }

        android.content.Context getContext() {
            return this.mContext;
        }

        com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs getWindowManagerFuncs() {
            return this.mWindowManagerFuncs;
        }

        android.os.Looper getLooper() {
            return android.os.Looper.myLooper();
        }

        com.android.internal.accessibility.AccessibilityShortcutController getAccessibilityShortcutController(android.content.Context context, android.os.Handler handler, int initialUserId) {
            return new com.android.internal.accessibility.AccessibilityShortcutController(context, handler, initialUserId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.policy.GlobalActions lambda$getGlobalActionsFactory$0() {
            return new com.android.server.policy.GlobalActions(this.mContext, this.mWindowManagerFuncs);
        }

        java.util.function.Supplier<com.android.server.policy.GlobalActions> getGlobalActionsFactory() {
            return new java.util.function.Supplier() { // from class: com.android.server.policy.PhoneWindowManager$Injector$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getGlobalActionsFactory$0();
                }
            };
        }

        com.android.server.policy.keyguard.KeyguardServiceDelegate getKeyguardServiceDelegate() {
            return new com.android.server.policy.keyguard.KeyguardServiceDelegate(this.mContext, new com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback() { // from class: com.android.server.policy.PhoneWindowManager.Injector.1
                @Override // com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback
                public void onTrustedChanged() {
                    com.android.server.policy.PhoneWindowManager.Injector.this.mWindowManagerFuncs.notifyKeyguardTrustedChanged();
                }

                @Override // com.android.server.policy.keyguard.KeyguardStateMonitor.StateCallback
                public void onShowingChanged() {
                    com.android.server.policy.PhoneWindowManager.Injector.this.mWindowManagerFuncs.onKeyguardShowingAndNotOccludedChanged();
                }
            });
        }

        android.app.IActivityManager getActivityManagerService() {
            return android.app.ActivityManager.getService();
        }

        com.android.server.policy.PhoneWindowManager.ButtonOverridePermissionChecker getButtonOverridePermissionChecker() {
            return new com.android.server.policy.PhoneWindowManager.ButtonOverridePermissionChecker();
        }

        com.android.server.policy.TalkbackShortcutController getTalkbackShortcutController() {
            return new com.android.server.policy.TalkbackShortcutController(this.mContext);
        }

        com.android.server.policy.WindowWakeUpPolicy getWindowWakeUpPolicy() {
            return new com.android.server.policy.WindowWakeUpPolicy(this.mContext);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setSecondDefaultDisplay(com.android.server.policy.WindowManagerPolicy.DisplayContentInfo displayContentInfo) {
        this.mPhoneWindowManagerExt.setSecondDefaultDisplay(displayContentInfo);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setDisplayEnable(boolean isDefaultDisplay, boolean enable) {
        this.mPhoneWindowManagerExt.setDisplayEnable(isDefaultDisplay, enable);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean getDisplayEnable(boolean isDefaultDisplay) {
        return this.mPhoneWindowManagerExt.getDisplayEnable(isDefaultDisplay);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void init(android.content.Context context, com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs funcs) {
        init(new com.android.server.policy.PhoneWindowManager.Injector(context, funcs));
    }

    void init(com.android.server.policy.PhoneWindowManager.Injector injector) {
        int minHorizontal;
        int minHorizontal2;
        int maxHorizontal;
        int minVertical;
        int maxVertical;
        this.mContext = injector.getContext();
        this.mWindowManagerFuncs = injector.getWindowManagerFuncs();
        this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        this.mActivityManagerService = injector.getActivityManagerService();
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mInputManager = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        this.mInputManagerInternal = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
        this.mDreamManagerInternal = (android.service.dreams.DreamManagerInternal) com.android.server.LocalServices.getService(android.service.dreams.DreamManagerInternal.class);
        this.mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        this.mSensorPrivacyManager = (android.hardware.SensorPrivacyManager) this.mContext.getSystemService(android.hardware.SensorPrivacyManager.class);
        this.mDisplayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        this.mUserManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.mPackageManager = this.mContext.getPackageManager();
        this.mHasFeatureWatch = this.mPackageManager.hasSystemFeature("android.hardware.type.watch");
        this.mHasFeatureLeanback = this.mPackageManager.hasSystemFeature("android.software.leanback");
        this.mHasFeatureAuto = this.mPackageManager.hasSystemFeature("android.hardware.type.automotive");
        this.mHasFeatureHdmiCec = this.mPackageManager.hasSystemFeature("android.hardware.hdmi.cec");
        this.mAccessibilityShortcutController = injector.getAccessibilityShortcutController(this.mContext, new android.os.Handler(), this.mCurrentUserId);
        this.mGlobalActionsFactory = injector.getGlobalActionsFactory();
        this.mLockPatternUtils = new com.android.internal.widget.LockPatternUtils(this.mContext);
        this.mLogger = new com.android.internal.logging.MetricsLogger();
        android.content.res.Resources res = this.mContext.getResources();
        this.mWakeOnDpadKeyPress = res.getBoolean(android.R.bool.config_useAttentionLight);
        this.mWakeOnAssistKeyPress = res.getBoolean(android.R.bool.config_use16BitTaskSnapshotPixelFormat);
        this.mWakeOnBackKeyPress = res.getBoolean(android.R.bool.config_useAssistantVolume);
        boolean burnInProtectionEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_enableAppWidgetService);
        boolean burnInProtectionDevMode = android.os.SystemProperties.getBoolean("persist.debug.force_burn_in", false);
        if (burnInProtectionEnabled || burnInProtectionDevMode) {
            if (burnInProtectionDevMode) {
                minHorizontal = -8;
                minHorizontal2 = 8;
                maxHorizontal = -8;
                minVertical = -4;
                maxVertical = isRoundWindow() ? 6 : -1;
            } else {
                android.content.res.Resources resources = this.mContext.getResources();
                int minHorizontal3 = resources.getInteger(android.R.integer.config_brightness_ramp_rate_slow);
                int maxHorizontal2 = resources.getInteger(android.R.integer.config_bluetooth_rx_cur_ma);
                int minVertical2 = resources.getInteger(android.R.integer.config_burnInProtectionMaxHorizontalOffset);
                int maxVertical2 = resources.getInteger(android.R.integer.config_brightness_ramp_rate_fast);
                int integer = resources.getInteger(android.R.integer.config_bluetooth_tx_cur_ma);
                minHorizontal = minHorizontal3;
                minHorizontal2 = maxHorizontal2;
                maxHorizontal = minVertical2;
                minVertical = maxVertical2;
                maxVertical = integer;
            }
            this.mBurnInProtectionHelper = new com.android.server.policy.BurnInProtectionHelper(this.mContext, minHorizontal, minHorizontal2, maxHorizontal, minVertical, maxVertical);
        }
        this.mHandler = new com.android.server.policy.PhoneWindowManager.PolicyHandler(injector.getLooper());
        this.mWakeGestureListener = new com.android.server.policy.PhoneWindowManager.MyWakeGestureListener(this.mContext, this.mHandler);
        this.mSettingsObserver = new com.android.server.policy.PhoneWindowManager.SettingsObserver(this.mHandler);
        this.mSettingsObserver.observe();
        this.mModifierShortcutManager = new com.android.server.policy.ModifierShortcutManager(this.mContext, this.mHandler);
        this.mUiMode = this.mContext.getResources().getInteger(android.R.integer.config_defaultPeakRefreshRate);
        this.mHomeIntent = new android.content.Intent("android.intent.action.MAIN", (android.net.Uri) null);
        this.mHomeIntent.addCategory("android.intent.category.HOME");
        this.mHomeIntent.addFlags(270532608);
        this.mEnableCarDockHomeCapture = this.mContext.getResources().getBoolean(android.R.bool.config_enableAutoPowerModes);
        this.mCarDockIntent = new android.content.Intent("android.intent.action.MAIN", (android.net.Uri) null);
        this.mCarDockIntent.addCategory("android.intent.category.CAR_DOCK");
        this.mCarDockIntent.addFlags(270532608);
        this.mDeskDockIntent = new android.content.Intent("android.intent.action.MAIN", (android.net.Uri) null);
        this.mDeskDockIntent.addCategory("android.intent.category.DESK_DOCK");
        this.mDeskDockIntent.addFlags(270532608);
        this.mVrHeadsetHomeIntent = new android.content.Intent("android.intent.action.MAIN", (android.net.Uri) null);
        this.mVrHeadsetHomeIntent.addCategory("android.intent.category.VR_HOME");
        this.mVrHeadsetHomeIntent.addFlags(270532608);
        this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService("power");
        this.mBroadcastWakeLock = this.mPowerManager.newWakeLock(1, "PhoneWindowManager.mBroadcastWakeLock");
        this.mPowerKeyWakeLock = this.mPowerManager.newWakeLock(1, "PhoneWindowManager.mPowerKeyWakeLock");
        this.mEnableBugReportKeyboardShortcut = "1".equals(android.os.SystemProperties.get("ro.debuggable"));
        this.mLidKeyboardAccessibility = this.mContext.getResources().getInteger(android.R.integer.config_jobSchedulerBackgroundJobsDelay);
        this.mLidNavigationAccessibility = this.mContext.getResources().getInteger(android.R.integer.config_jobSchedulerIdleWindowSlop);
        this.mGoToSleepOnButtonPressTheaterMode = this.mContext.getResources().getBoolean(android.R.bool.config_enhanced_iwlan_handover_check);
        this.mSupportLongPressPowerWhenNonInteractive = this.mContext.getResources().getBoolean(android.R.bool.config_showUserSwitcherByDefault);
        this.mSupportShortPressPowerWhenDefaultDisplayOn = this.mContext.getResources().getBoolean(android.R.bool.config_silenceSensorAvailable);
        this.mLongPressOnBackBehavior = this.mContext.getResources().getInteger(android.R.integer.config_jumpTapTimeoutMillis);
        this.mLongPressOnPowerBehavior = this.mContext.getResources().getInteger(android.R.integer.config_keyChordPowerVolumeUp);
        this.mLongPressOnPowerAssistantTimeoutMs = this.mContext.getResources().getInteger(android.R.integer.config_keyboardBacklightTimeoutMs);
        this.mVeryLongPressOnPowerBehavior = this.mContext.getResources().getInteger(android.R.integer.config_showOperatorNameDefault);
        this.mPowerDoublePressTargetActivity = android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_extensionFallbackPackageName));
        this.mPrimaryShortPressTargetActivity = android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_recentsComponentName));
        this.mShortPressOnSleepBehavior = this.mContext.getResources().getInteger(android.R.integer.config_satellite_modem_image_switching_duration_millis);
        this.mSilenceRingerOnSleepKey = this.mContext.getResources().getBoolean(android.R.bool.config_safe_media_volume_enabled);
        this.mAllowStartActivityForLongPressOnPowerDuringSetup = this.mContext.getResources().getBoolean(android.R.bool.config_allowPriorityVibrationsInLowPowerMode);
        this.mUseTvRouting = android.media.AudioSystem.getPlatformType(this.mContext) == 2;
        this.mHandleVolumeKeysInWM = this.mContext.getResources().getBoolean(android.R.bool.config_faceAuthSupportsSelfIllumination);
        this.mWakeUpToLastStateTimeout = this.mContext.getResources().getInteger(android.R.integer.config_stableDeviceDisplayHeight);
        this.mSearchKeyBehavior = this.mContext.getResources().getInteger(android.R.integer.config_satellite_demo_mode_nb_iot_inactivity_timeout_millis);
        this.mSearchKeyTargetActivity = android.content.ComponentName.unflattenFromString(this.mContext.getResources().getString(android.R.string.config_secondaryDisplayShape));
        readConfigurationDependentBehaviors();
        this.mDisplayFoldController = com.android.server.policy.DisplayFoldController.create(this.mContext, 0);
        this.mAccessibilityManager = (android.view.accessibility.AccessibilityManager) this.mContext.getSystemService(android.view.accessibility.AccessibilityManager.class);
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(android.app.UiModeManager.ACTION_ENTER_CAR_MODE);
        filter.addAction(android.app.UiModeManager.ACTION_EXIT_CAR_MODE);
        filter.addAction(android.app.UiModeManager.ACTION_ENTER_DESK_MODE);
        filter.addAction(android.app.UiModeManager.ACTION_EXIT_DESK_MODE);
        filter.addAction("android.intent.action.DOCK_EVENT");
        android.content.Intent intent = this.mContext.registerReceiver(this.mDockReceiver, filter);
        if (intent != null) {
            this.mDefaultDisplayPolicy.setDockMode(intent.getIntExtra("android.intent.extra.DOCK_STATE", 0));
        }
        this.mContext.registerReceiver(this.mMultiuserReceiver, new android.content.IntentFilter("android.intent.action.USER_SWITCHED"));
        this.mVibrator = (android.os.Vibrator) this.mContext.getSystemService("vibrator");
        this.mHapticFeedbackVibrationProvider = new com.android.server.vibrator.HapticFeedbackVibrationProvider(this.mContext.getResources(), this.mVibrator);
        this.mGlobalKeyManager = new com.android.server.policy.GlobalKeyManager(this.mContext);
        initializeHdmiState();
        if (!this.mPowerManager.isInteractive()) {
            startedGoingToSleep(0, 2);
            finishedGoingToSleep(0, 2);
        }
        this.mWindowManagerInternal.registerAppTransitionListener(new com.android.server.wm.WindowManagerInternal.AppTransitionListener() { // from class: com.android.server.policy.PhoneWindowManager.5
            @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
            public int onAppTransitionStartingLocked(long statusBarAnimationStartTime, long statusBarAnimationDuration) {
                return com.android.server.policy.PhoneWindowManager.this.handleTransitionForKeyguardLw(false, false);
            }

            @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
            public void onAppTransitionCancelledLocked(boolean keyguardGoingAwayCancelled) {
                com.android.server.policy.PhoneWindowManager.this.handleTransitionForKeyguardLw(keyguardGoingAwayCancelled, true);
                synchronized (com.android.server.policy.PhoneWindowManager.this.mLock) {
                    com.android.server.policy.PhoneWindowManager.this.mLockAfterDreamingTransitionFinished = false;
                }
            }

            @Override // com.android.server.wm.WindowManagerInternal.AppTransitionListener
            public void onAppTransitionFinishedLocked(android.os.IBinder token) {
                synchronized (com.android.server.policy.PhoneWindowManager.this.mLock) {
                    android.service.dreams.DreamManagerInternal dreamManagerInternal = com.android.server.policy.PhoneWindowManager.this.getDreamManagerInternal();
                    if (dreamManagerInternal != null && dreamManagerInternal.isDreaming() && com.android.server.policy.PhoneWindowManager.this.mLockAfterDreamingTransitionFinished) {
                        com.android.server.policy.PhoneWindowManager.this.lockNow(null);
                    }
                    com.android.server.policy.PhoneWindowManager.this.mLockAfterDreamingTransitionFinished = false;
                }
            }
        });
        this.mKeyguardDrawnTimeout = this.mContext.getResources().getInteger(android.R.integer.config_fixedRefreshRateInHighZone);
        this.mKeyguardDelegate = injector.getKeyguardServiceDelegate();
        this.mTalkbackShortcutController = injector.getTalkbackShortcutController();
        this.mWindowWakeUpPolicy = injector.getWindowWakeUpPolicy();
        initKeyCombinationRules();
        initSingleKeyGestureRules(injector.getLooper());
        this.mButtonOverridePermissionChecker = injector.getButtonOverridePermissionChecker();
        this.mPhoneWindowManagerExt.hookForInit();
        this.mSideFpsEventHandler = new com.android.server.policy.SideFpsEventHandler(this.mContext, this.mHandler, this.mPowerManager);
        this.mAllowSetKeyData = allowShowBfs();
    }

    private void initKeyCombinationRules() {
        this.mKeyCombinationManager = new com.android.server.policy.KeyCombinationManager(this.mHandler);
        boolean screenshotChordEnabled = this.mContext.getResources().getBoolean(android.R.bool.config_enableNetworkLocationOverlay);
        android.util.Slog.d(TAG, "initKeyCombinationRules screenshotChordEnabled:" + screenshotChordEnabled);
        int i = 25;
        int i2 = 26;
        if (screenshotChordEnabled) {
            this.mKeyCombinationManager.addRule(new com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule(i, i2) { // from class: com.android.server.policy.PhoneWindowManager.6
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                void execute() {
                    boolean isUnderWaterCameraStatus = com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.isUnderWaterCameraStatus();
                    if (!isUnderWaterCameraStatus) {
                        com.android.server.policy.PhoneWindowManager.this.mPowerKeyHandled = true;
                        com.android.server.policy.PhoneWindowManager.this.interceptScreenshotChord(1, com.android.server.policy.PhoneWindowManager.this.getScreenshotChordLongPressDelay());
                        com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.onScreenShotKeyPressedOnTheiaMonitor();
                        com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.sendBroadcastForCombinationKeyGrabSystrace();
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void cancel() {
                    com.android.server.policy.PhoneWindowManager.this.cancelPendingScreenshotChordAction();
                }
            });
            if (this.mHasFeatureWatch) {
                this.mKeyCombinationManager.addRule(new com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule(i2, 264) { // from class: com.android.server.policy.PhoneWindowManager.7
                    @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                    void execute() {
                        com.android.server.policy.PhoneWindowManager.this.mPowerKeyHandled = true;
                        com.android.server.policy.PhoneWindowManager.this.interceptScreenshotChord(1, com.android.server.policy.PhoneWindowManager.this.getScreenshotChordLongPressDelay());
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                    public void cancel() {
                        com.android.server.policy.PhoneWindowManager.this.cancelPendingScreenshotChordAction();
                    }
                });
            }
        }
        int i3 = 24;
        this.mKeyCombinationManager.addRule(new com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule(i, i3) { // from class: com.android.server.policy.PhoneWindowManager.8
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            boolean preCondition() {
                return com.android.server.policy.PhoneWindowManager.this.mAccessibilityShortcutController.isAccessibilityShortcutAvailable(com.android.server.policy.PhoneWindowManager.this.isKeyguardLocked());
            }

            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            void execute() {
                com.android.server.policy.PhoneWindowManager.this.interceptAccessibilityShortcutChord();
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            public void cancel() {
                com.android.server.policy.PhoneWindowManager.this.cancelPendingAccessibilityShortcutAction();
            }
        });
        this.mKeyCombinationManager.addRule(new com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule(i3, i2) { // from class: com.android.server.policy.PhoneWindowManager.9
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            boolean preCondition() {
                switch (com.android.server.policy.PhoneWindowManager.this.mPowerVolUpBehavior) {
                    case 1:
                        if (com.android.server.policy.PhoneWindowManager.this.mRingerToggleChord != 0) {
                        }
                        break;
                }
                return true;
            }

            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            void execute() {
                switch (com.android.server.policy.PhoneWindowManager.this.mPowerVolUpBehavior) {
                    case 1:
                        com.android.server.policy.PhoneWindowManager.this.interceptRingerToggleChord();
                        com.android.server.policy.PhoneWindowManager.this.mPowerKeyHandled = true;
                        break;
                    case 2:
                        com.android.server.policy.PhoneWindowManager.this.performHapticFeedback(10003, false, "Power + Volume Up - Global Actions");
                        com.android.server.policy.PhoneWindowManager.this.showGlobalActions();
                        com.android.server.policy.PhoneWindowManager.this.mPowerKeyHandled = true;
                        break;
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
            public void cancel() {
                switch (com.android.server.policy.PhoneWindowManager.this.mPowerVolUpBehavior) {
                    case 1:
                        com.android.server.policy.PhoneWindowManager.this.cancelPendingRingerToggleChordAction();
                        break;
                    case 2:
                        com.android.server.policy.PhoneWindowManager.this.cancelGlobalActionsAction();
                        break;
                }
            }
        });
        if (this.mHasFeatureLeanback) {
            int i4 = 4;
            this.mKeyCombinationManager.addRule(new com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule(i4, 20) { // from class: com.android.server.policy.PhoneWindowManager.10
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                void execute() {
                    com.android.server.policy.PhoneWindowManager.this.mBackKeyHandled = true;
                    com.android.server.policy.PhoneWindowManager.this.interceptAccessibilityGestureTv();
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void cancel() {
                    com.android.server.policy.PhoneWindowManager.this.cancelAccessibilityGestureTv();
                }

                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                long getKeyInterceptDelayMs() {
                    return 0L;
                }
            });
            this.mKeyCombinationManager.addRule(new com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule(23, i4) { // from class: com.android.server.policy.PhoneWindowManager.11
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                void execute() {
                    com.android.server.policy.PhoneWindowManager.this.mBackKeyHandled = true;
                    com.android.server.policy.PhoneWindowManager.this.interceptBugreportGestureTv();
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                public void cancel() {
                    com.android.server.policy.PhoneWindowManager.this.cancelBugreportGestureTv();
                }

                @Override // com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule
                long getKeyInterceptDelayMs() {
                    return 0L;
                }
            });
        }
    }

    public class PowerKeyRule extends com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule {
        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        public /* bridge */ /* synthetic */ boolean equals(java.lang.Object obj) {
            return super.equals(obj);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        public /* bridge */ /* synthetic */ java.lang.String toString() {
            return super.toString();
        }

        PowerKeyRule() {
            super(26);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportLongPress() {
            return com.android.server.policy.PhoneWindowManager.this.hasLongPressOnPowerBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportVeryLongPress() {
            return com.android.server.policy.PhoneWindowManager.this.hasVeryLongPressOnPowerBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        int getMaxMultiPressCount() {
            return com.android.server.policy.PhoneWindowManager.this.getMaxMultiPressPowerCount();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onPress(long downTime, int displayId) {
            if (com.android.server.policy.PhoneWindowManager.this.mShouldEarlyShortPressOnPower) {
                return;
            }
            com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().powerPress(downTime, com.android.server.policy.PhoneWindowManager.this.mSingleKeyGestureDetector.beganFromNonInteractive(), 1, displayId);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        long getLongPressTimeoutMs() {
            if (com.android.server.policy.PhoneWindowManager.this.getResolvedLongPressOnPowerBehavior() == 5) {
                return com.android.server.policy.PhoneWindowManager.this.mLongPressOnPowerAssistantTimeoutMs;
            }
            return super.getLongPressTimeoutMs();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onLongPress(long eventTime) {
            if (com.android.server.policy.PhoneWindowManager.this.mSingleKeyGestureDetector.beganFromNonInteractive() && !com.android.server.policy.PhoneWindowManager.this.mSupportLongPressPowerWhenNonInteractive) {
                android.util.Slog.v(com.android.server.policy.PhoneWindowManager.TAG, "Not support long press power when device is not interactive.");
            } else {
                com.android.server.policy.PhoneWindowManager.this.powerLongPress(eventTime);
            }
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onVeryLongPress(long eventTime) {
            com.android.server.policy.PhoneWindowManager.this.mActivityManagerInternal.prepareForPossibleShutdown();
            com.android.server.policy.PhoneWindowManager.this.powerVeryLongPress();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onMultiPress(long downTime, int count, int displayId) {
            com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt.getInputExtension().powerPress(downTime, com.android.server.policy.PhoneWindowManager.this.mSingleKeyGestureDetector.beganFromNonInteractive(), count, displayId);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onKeyUp(long eventTime, int count, int displayId) {
            if (com.android.server.policy.PhoneWindowManager.this.mShouldEarlyShortPressOnPower && count == 1) {
                com.android.server.policy.PhoneWindowManager.this.powerPress(eventTime, 1, displayId);
            }
        }
    }

    private final class BackKeyRule extends com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule {
        BackKeyRule() {
            super(4);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportLongPress() {
            return com.android.server.policy.PhoneWindowManager.this.hasLongPressOnBackBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        int getMaxMultiPressCount() {
            return 1;
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onPress(long downTime, int unusedDisplayId) {
            com.android.server.policy.PhoneWindowManager.this.mBackKeyHandled |= com.android.server.policy.PhoneWindowManager.this.backKeyPress();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onLongPress(long downTime) {
            com.android.server.policy.PhoneWindowManager.this.backLongPress();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class StemPrimaryKeyRule extends com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule {
        StemPrimaryKeyRule() {
            super(264);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        boolean supportLongPress() {
            return com.android.server.policy.PhoneWindowManager.this.hasLongPressOnStemPrimaryBehavior();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        int getMaxMultiPressCount() {
            return com.android.server.policy.PhoneWindowManager.this.getMaxMultiPressStemPrimaryCount();
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onPress(long downTime, int unusedDisplayId) {
            if (shouldHandleStemPrimaryEarlyShortPress()) {
                return;
            }
            com.android.server.policy.PhoneWindowManager.this.mDeferredKeyActionExecutor.queueKeyAction(264, downTime, new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$StemPrimaryKeyRule$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onPress$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPress$0() {
            com.android.server.policy.PhoneWindowManager.this.stemPrimaryPress(1);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onLongPress(final long eventTime) {
            com.android.server.policy.PhoneWindowManager.this.mDeferredKeyActionExecutor.queueKeyAction(264, eventTime, new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$StemPrimaryKeyRule$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onLongPress$1(eventTime);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLongPress$1(long eventTime) {
            com.android.server.policy.PhoneWindowManager.this.stemPrimaryLongPress(eventTime);
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onMultiPress(long downTime, final int count, int unusedDisplayId) {
            if (count == 3 && com.android.server.policy.PhoneWindowManager.this.mTriplePressOnStemPrimaryBehavior == 1) {
                com.android.server.policy.PhoneWindowManager.this.mDeferredKeyActionExecutor.cancelQueuedAction(264);
                undoEarlySinglePress();
                com.android.server.policy.PhoneWindowManager.this.stemPrimaryPress(count);
                return;
            }
            com.android.server.policy.PhoneWindowManager.this.mDeferredKeyActionExecutor.queueKeyAction(264, downTime, new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$StemPrimaryKeyRule$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onMultiPress$2(count);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMultiPress$2(int count) {
            com.android.server.policy.PhoneWindowManager.this.stemPrimaryPress(count);
        }

        private void undoEarlySinglePress() {
            if (shouldHandleStemPrimaryEarlyShortPress() && com.android.server.policy.PhoneWindowManager.this.mFocusedTaskInfoOnStemPrimarySingleKeyUp != null) {
                try {
                    com.android.server.policy.PhoneWindowManager.this.mActivityManagerService.startActivityFromRecents(com.android.server.policy.PhoneWindowManager.this.mFocusedTaskInfoOnStemPrimarySingleKeyUp.taskId, (android.os.Bundle) null);
                } catch (android.os.RemoteException | java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(com.android.server.policy.PhoneWindowManager.TAG, "Failed to start task " + com.android.server.policy.PhoneWindowManager.this.mFocusedTaskInfoOnStemPrimarySingleKeyUp.taskId + " from recents", e);
                }
            }
        }

        @Override // com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule
        void onKeyUp(long eventTime, int count, int unusedDisplayId) {
            if (count == 1) {
                com.android.server.policy.PhoneWindowManager.this.mBackgroundRecentTaskInfoOnStemPrimarySingleKeyUp = com.android.server.policy.PhoneWindowManager.this.mActivityTaskManagerInternal.getMostRecentTaskFromBackground();
                com.android.server.policy.PhoneWindowManager.this.mFocusedTaskInfoOnStemPrimarySingleKeyUp = null;
                if (shouldHandleStemPrimaryEarlyShortPress()) {
                    com.android.server.policy.PhoneWindowManager.this.mDeferredKeyActionExecutor.queueKeyAction(264, eventTime, new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$StemPrimaryKeyRule$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onKeyUp$3();
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyUp$3() {
            android.util.Slog.d(com.android.server.policy.PhoneWindowManager.TAG, "StemPrimaryKeyRule: executing deferred onKeyUp");
            try {
                com.android.server.policy.PhoneWindowManager.this.mFocusedTaskInfoOnStemPrimarySingleKeyUp = com.android.server.policy.PhoneWindowManager.this.mActivityManagerService.getFocusedRootTaskInfo();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.policy.PhoneWindowManager.TAG, "StemPrimaryKeyRule: onKeyUp: error while getting focused task info.", e);
            }
            com.android.server.policy.PhoneWindowManager.this.stemPrimaryPress(1);
        }

        private boolean shouldHandleStemPrimaryEarlyShortPress() {
            return com.android.server.policy.PhoneWindowManager.this.mShouldEarlyShortPressOnStemPrimary && com.android.server.policy.PhoneWindowManager.this.mShortPressOnStemPrimaryBehavior == 1;
        }
    }

    private void initSingleKeyGestureRules(android.os.Looper looper) {
        this.mSingleKeyGestureDetector = com.android.server.policy.SingleKeyGestureDetector.get(this.mContext, looper);
        if (hasLongPressOnBackBehavior()) {
            this.mSingleKeyGestureDetector.addRule(new com.android.server.policy.PhoneWindowManager.BackKeyRule());
        }
        if (hasStemPrimaryBehavior()) {
            this.mSingleKeyGestureDetector.addRule(new com.android.server.policy.PhoneWindowManager.StemPrimaryKeyRule());
        }
    }

    private void readConfigurationDependentBehaviors() {
        android.content.res.Resources res = this.mContext.getResources();
        this.mLongPressOnHomeBehavior = res.getInteger(android.R.integer.config_keepPreloadsMinDays);
        this.mLongPressOnHomeBehavior = this.mPhoneWindowManagerExt.getInputExtension().updateConfigurationDependentBehaviors(this.mLongPressOnHomeBehavior);
        if (this.mLongPressOnHomeBehavior < 0 || this.mLongPressOnHomeBehavior > 3) {
            this.mLongPressOnHomeBehavior = 0;
        }
        this.mDoubleTapOnHomeBehavior = res.getInteger(android.R.integer.config_displayWhiteBalanceDecreaseDebounce);
        if (this.mDoubleTapOnHomeBehavior < 0 || this.mDoubleTapOnHomeBehavior > 2) {
            this.mDoubleTapOnHomeBehavior = 0;
        }
        this.mShortPressOnWindowBehavior = 0;
        if (this.mPackageManager.hasSystemFeature("android.software.picture_in_picture")) {
            this.mShortPressOnWindowBehavior = 1;
        }
        this.mSettingsKeyBehavior = res.getInteger(android.R.integer.config_satellite_location_query_throttle_interval_minutes);
        if (this.mSettingsKeyBehavior < 0 || this.mSettingsKeyBehavior > 2) {
            this.mSettingsKeyBehavior = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSettings() {
        updateSettings(null);
    }

    void updateSettings(android.os.Handler handler) {
        boolean kidsModeEnabled;
        if (handler != null) {
            handler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateSettings$1();
                }
            });
            return;
        }
        android.content.ContentResolver resolver = this.mContext.getContentResolver();
        boolean updateRotation = false;
        boolean updateKidsModeSettings = false;
        synchronized (this.mLock) {
            this.mEndcallBehavior = android.provider.Settings.System.getIntForUser(resolver, "end_button_behavior", 2, -2);
            this.mIncallPowerBehavior = android.provider.Settings.Secure.getIntForUser(resolver, "incall_power_button_behavior", 1, -2);
            this.mIncallBackBehavior = android.provider.Settings.Secure.getIntForUser(resolver, "incall_back_button_behavior", 0, -2);
            this.mSystemNavigationKeysEnabled = android.provider.Settings.Secure.getIntForUser(resolver, "system_navigation_keys_enabled", 0, -2) == 1;
            this.mRingerToggleChord = android.provider.Settings.Secure.getIntForUser(resolver, "volume_hush_gesture", 0, -2);
            this.mPowerButtonSuppressionDelayMillis = android.provider.Settings.Global.getInt(resolver, "power_button_suppression_delay_after_gesture_wake", 800);
            if (!this.mContext.getResources().getBoolean(android.R.bool.config_unfoldTransitionEnabled)) {
                this.mRingerToggleChord = 0;
            }
            boolean wakeGestureEnabledSetting = android.provider.Settings.Secure.getIntForUser(resolver, "wake_gesture_enabled", 0, -2) != 0;
            if (this.mWakeGestureEnabledSetting != wakeGestureEnabledSetting) {
                this.mWakeGestureEnabledSetting = wakeGestureEnabledSetting;
                updateWakeGestureListenerLp();
            }
            this.mLockScreenTimeout = android.provider.Settings.System.getIntForUser(resolver, "screen_off_timeout", 0, -2);
            java.lang.String imId = android.provider.Settings.Secure.getStringForUser(resolver, "default_input_method", -2);
            boolean hasSoftInput = imId != null && imId.length() > 0;
            if (this.mHasSoftInput != hasSoftInput && (!this.mSystemBooted || this.mPhoneWindowManagerExt.isTargetUserUnlocked(this.mCurrentUserId))) {
                this.mHasSoftInput = hasSoftInput;
                updateRotation = true;
            }
            this.mShortPressOnPowerBehavior = android.provider.Settings.Global.getInt(resolver, "power_button_short_press", this.mContext.getResources().getInteger(android.R.integer.config_satellite_max_retry_count_for_validating_possible_change_in_allowed_region));
            this.mDoublePressOnPowerBehavior = android.provider.Settings.Global.getInt(resolver, "power_button_double_press", this.mContext.getResources().getInteger(android.R.integer.config_displayWhiteBalanceColorTemperatureMin));
            this.mTriplePressOnPowerBehavior = android.provider.Settings.Global.getInt(resolver, "power_button_triple_press", this.mContext.getResources().getInteger(android.R.integer.config_searchKeyBehavior));
            int longPressOnPowerBehavior = android.provider.Settings.Global.getInt(resolver, "power_button_long_press", this.mContext.getResources().getInteger(android.R.integer.config_keyChordPowerVolumeUp));
            int veryLongPressOnPowerBehavior = android.provider.Settings.Global.getInt(resolver, "power_button_very_long_press", this.mContext.getResources().getInteger(android.R.integer.config_showOperatorNameDefault));
            if (this.mLongPressOnPowerBehavior != longPressOnPowerBehavior || this.mVeryLongPressOnPowerBehavior != veryLongPressOnPowerBehavior) {
                this.mLongPressOnPowerBehavior = longPressOnPowerBehavior;
                this.mVeryLongPressOnPowerBehavior = veryLongPressOnPowerBehavior;
            }
            this.mLongPressOnPowerAssistantTimeoutMs = android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "power_button_long_press_duration_ms", this.mContext.getResources().getInteger(android.R.integer.config_keyboardBacklightTimeoutMs));
            this.mPowerVolUpBehavior = android.provider.Settings.Global.getInt(resolver, "key_chord_power_volume_up", this.mContext.getResources().getInteger(android.R.integer.config_fingerprintMaxTemplatesPerUser));
            this.mShortPressOnStemPrimaryBehavior = android.provider.Settings.Global.getInt(resolver, "stem_primary_button_short_press", this.mContext.getResources().getInteger(android.R.integer.config_satellite_nb_iot_inactivity_timeout_millis));
            this.mDoublePressOnStemPrimaryBehavior = android.provider.Settings.Global.getInt(resolver, "stem_primary_button_double_press", this.mContext.getResources().getInteger(android.R.integer.config_displayWhiteBalanceColorTemperatureSensorRate));
            this.mTriplePressOnStemPrimaryBehavior = android.provider.Settings.Global.getInt(resolver, "stem_primary_button_triple_press", this.mContext.getResources().getInteger(android.R.integer.config_selected_udfps_touch_detection));
            this.mLongPressOnStemPrimaryBehavior = android.provider.Settings.Global.getInt(resolver, "stem_primary_button_long_press", this.mContext.getResources().getInteger(android.R.integer.config_keyguardDrawnTimeout));
            this.mShouldEarlyShortPressOnPower = this.mContext.getResources().getBoolean(android.R.bool.config_reduceBrightColorsAvailable);
            this.mShouldEarlyShortPressOnStemPrimary = this.mContext.getResources().getBoolean(android.R.bool.config_refreshRateSynchronizationEnabled);
            this.mStylusButtonsEnabled = android.provider.Settings.Secure.getIntForUser(resolver, "stylus_buttons_enabled", 1, -2) == 1;
            this.mInputManagerInternal.setStylusButtonMotionEventsEnabled(this.mStylusButtonsEnabled);
            kidsModeEnabled = android.provider.Settings.Secure.getIntForUser(resolver, "nav_bar_kids_mode", 0, -2) == 1;
            if (this.mKidsModeEnabled != kidsModeEnabled) {
                this.mKidsModeEnabled = kidsModeEnabled;
                updateKidsModeSettings = true;
            }
        }
        if (updateKidsModeSettings) {
            updateKidsModeSettings(kidsModeEnabled);
        }
        if (updateRotation) {
            updateRotation(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSettings$1() {
        updateSettings(null);
    }

    private void updateKidsModeSettings(boolean kidsModeEnabled) {
        if (!kidsModeEnabled) {
            this.mWindowManagerInternal.setOrientationRequestPolicy(false, null, null);
        } else if (this.mContext.getResources().getBoolean(android.R.bool.config_permissionsIndividuallyControlled)) {
            this.mWindowManagerInternal.setOrientationRequestPolicy(true, new int[]{0, 8}, new int[]{6, 6});
        } else {
            this.mWindowManagerInternal.setOrientationRequestPolicy(true, null, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.service.dreams.DreamManagerInternal getDreamManagerInternal() {
        if (this.mDreamManagerInternal == null) {
            this.mDreamManagerInternal = (android.service.dreams.DreamManagerInternal) com.android.server.LocalServices.getService(android.service.dreams.DreamManagerInternal.class);
        }
        return this.mDreamManagerInternal;
    }

    private void updateWakeGestureListenerLp() {
        if (shouldEnableWakeGestureLp()) {
            this.mWakeGestureListener.requestWakeUpTrigger();
        } else {
            this.mWakeGestureListener.cancelWakeUpTrigger();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldEnableWakeGestureLp() {
        return this.mWakeGestureEnabledSetting && !this.mDefaultDisplayPolicy.isAwake() && !(getLidBehavior() == 1 && this.mDefaultDisplayPolicy.getLidState() == 0) && this.mWakeGestureListener.isSupported();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int checkAddPermission(int type, boolean isRoundedCornerOverlay, java.lang.String packageName, int[] outAppOp) {
        android.content.pm.ApplicationInfo appInfo;
        if (isRoundedCornerOverlay && this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") != 0) {
            return -8;
        }
        outAppOp[0] = -1;
        if ((type >= 1 && type <= 99) || ((type >= 1000 && type <= 1999) || (type >= 2000 && type <= 2999))) {
            if (type < 2000 || type > 2999) {
                return 0;
            }
            if (!android.view.WindowManager.LayoutParams.isSystemAlertWindowType(type)) {
                switch (type) {
                    case 2005:
                        outAppOp[0] = 45;
                        return 0;
                    case 2032:
                        if (com.android.internal.hidden_from_bootclasspath.android.view.contentprotection.flags.Flags.createAccessibilityOverlayAppOpEnabled()) {
                            outAppOp[0] = 138;
                            return 0;
                        }
                    case 2011:
                    case 2013:
                    case 2024:
                    case 2030:
                    case 2031:
                    case 2035:
                    case 2037:
                        return 0;
                    default:
                        return this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") == 0 ? 0 : -8;
                }
            } else {
                outAppOp[0] = 24;
                int callingUid = android.os.Binder.getCallingUid();
                if (android.os.UserHandle.getAppId(callingUid) == 1000) {
                    return 0;
                }
                try {
                    try {
                        appInfo = this.mPackageManager.getApplicationInfoAsUser(packageName, 0, android.os.UserHandle.getUserId(callingUid));
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        appInfo = null;
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                }
                if (appInfo == null || (type != 2038 && appInfo.targetSdkVersion >= 26)) {
                    return this.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") == 0 ? 0 : -8;
                }
                if (this.mContext.checkCallingOrSelfPermission("android.permission.SYSTEM_APPLICATION_OVERLAY") == 0) {
                    return 0;
                }
                int mode = this.mAppOpsManager.noteOpNoThrow(outAppOp[0], callingUid, packageName, (java.lang.String) null, "check-add");
                switch (mode) {
                    case 0:
                    case 1:
                        return 0;
                    case 2:
                        return appInfo.targetSdkVersion < 23 ? 0 : -8;
                    default:
                        return this.mContext.checkCallingOrSelfPermission("android.permission.SYSTEM_ALERT_WINDOW") == 0 ? 0 : -8;
                }
            }
        }
        return -10;
    }

    void readLidState() {
        this.mDefaultDisplayPolicy.setLidState(this.mWindowManagerFuncs.getLidState());
    }

    private void readCameraLensCoverState() {
        this.mCameraLensCoverState = this.mWindowManagerFuncs.getCameraLensCoverState();
    }

    private boolean isHidden(int accessibilityMode) {
        int lidState = this.mDefaultDisplayPolicy.getLidState();
        switch (accessibilityMode) {
            case 1:
                return lidState == 0;
            case 2:
                return lidState == 1;
            default:
                return false;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void adjustConfigurationLw(android.content.res.Configuration config, int keyboardPresence, int navigationPresence) {
        this.mHaveBuiltInKeyboard = (keyboardPresence & 1) != 0;
        readConfigurationDependentBehaviors();
        readLidState();
        if (config.keyboard == 1 || (keyboardPresence == 1 && isHidden(this.mLidKeyboardAccessibility))) {
            config.hardKeyboardHidden = 2;
            if (!this.mHasSoftInput) {
                config.keyboardHidden = 2;
            }
        }
        if (config.navigation == 1 || (navigationPresence == 1 && isHidden(this.mLidNavigationAccessibility))) {
            config.navigationHidden = 2;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardHostWindow(android.view.WindowManager.LayoutParams attrs) {
        return attrs.type == 2040;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public android.view.animation.Animation createHiddenByKeyguardExit(boolean onWallpaper, boolean goingToNotificationShade, boolean subtleAnimation) {
        return com.android.internal.policy.TransitionAnimation.createHiddenByKeyguardExit(this.mContext, this.mLogDecelerateInterpolator, onWallpaper, goingToNotificationShade, subtleAnimation);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public android.view.animation.Animation createKeyguardWallpaperExit(boolean goingToNotificationShade) {
        if (goingToNotificationShade) {
            return null;
        }
        return android.view.animation.AnimationUtils.loadAnimation(this.mContext, android.R.anim.lock_screen_wallpaper_exit);
    }

    private static void awakenDreams() {
        android.service.dreams.IDreamManager dreamManager = getDreamManager();
        if (dreamManager != null) {
            try {
                dreamManager.awaken();
            } catch (android.os.RemoteException e) {
            }
        }
    }

    static android.service.dreams.IDreamManager getDreamManager() {
        return android.service.dreams.IDreamManager.Stub.asInterface(android.os.ServiceManager.checkService("dreams"));
    }

    android.telecom.TelecomManager getTelecommService() {
        return (android.telecom.TelecomManager) this.mContext.getSystemService("telecom");
    }

    android.app.NotificationManager getNotificationService() {
        return (android.app.NotificationManager) this.mContext.getSystemService(android.app.NotificationManager.class);
    }

    static android.media.IAudioService getAudioService() {
        android.media.IAudioService audioService = android.media.IAudioService.Stub.asInterface(android.os.ServiceManager.checkService("audio"));
        if (audioService == null) {
            android.util.Log.w(TAG, "Unable to find IAudioService interface.");
        }
        return audioService;
    }

    boolean keyguardOn() {
        return isKeyguardShowingAndNotOccluded() || inKeyguardRestrictedKeyInputMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyboardSystemEvent(com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent keyboardLogEvent, android.view.KeyEvent event) {
        android.view.InputDevice inputDevice = this.mInputManager.getInputDevice(event.getDeviceId());
        com.android.server.input.KeyboardMetricsCollector.logKeyboardSystemsEventReportedAtom(inputDevice, keyboardLogEvent, event.getMetaState(), event.getKeyCode());
        event.recycle();
    }

    private void logKeyboardSystemsEventOnActionUp(android.view.KeyEvent event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent keyboardSystemEvent) {
        if (event.getAction() != 1) {
            return;
        }
        logKeyboardSystemsEvent(event, keyboardSystemEvent);
    }

    private void logKeyboardSystemsEventOnActionDown(android.view.KeyEvent event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent keyboardSystemEvent) {
        if (event.getAction() != 0) {
            return;
        }
        logKeyboardSystemsEvent(event, keyboardSystemEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logKeyboardSystemsEvent(android.view.KeyEvent event, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent keyboardSystemEvent) {
        android.view.KeyEvent eventToLog = android.view.KeyEvent.obtain(event);
        this.mHandler.obtainMessage(26, keyboardSystemEvent.getIntValue(), 0, eventToLog).sendToTarget();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public long interceptKeyBeforeDispatching(android.os.IBinder focusedToken, android.view.KeyEvent event, int policyFlags) {
        int keyCode = event.getKeyCode();
        int flags = event.getFlags();
        int deviceId = event.getDeviceId();
        if (DEBUG_INPUT) {
            android.util.Log.d(TAG, "interceptKeyTi keyCode=" + keyCode + " action=" + event.getAction() + " repeatCount=" + event.getRepeatCount() + " keyguardOn=" + keyguardOn() + " canceled=" + event.isCanceled());
        }
        if (this.mKeyCombinationManager.isKeyConsumed(event)) {
            return -1L;
        }
        if ((flags & 1024) == 0) {
            long now = android.os.SystemClock.uptimeMillis();
            long interceptTimeout = this.mKeyCombinationManager.getKeyInterceptTimeout(keyCode);
            if (now < interceptTimeout) {
                return interceptTimeout - now;
            }
        }
        java.util.Set<java.lang.Integer> consumedKeys = this.mConsumedKeysForDevice.get(deviceId);
        if (consumedKeys == null) {
            consumedKeys = new java.util.HashSet();
            this.mConsumedKeysForDevice.put(deviceId, consumedKeys);
        }
        if (interceptSystemKeysAndShortcuts(focusedToken, event) && event.getAction() == 0 && event.getRepeatCount() == 0) {
            consumedKeys.add(java.lang.Integer.valueOf(keyCode));
            return -1L;
        }
        boolean needToConsumeKey = consumedKeys.contains(java.lang.Integer.valueOf(keyCode));
        if (event.getAction() == 1 || event.isCanceled()) {
            consumedKeys.remove(java.lang.Integer.valueOf(keyCode));
            if (consumedKeys.isEmpty()) {
                this.mConsumedKeysForDevice.remove(deviceId);
            }
        }
        return needToConsumeKey ? -1L : 0L;
    }

    /* JADX WARN: Removed duplicated region for block: B:305:0x0499  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x04c7  */
    /* JADX WARN: Removed duplicated region for block: B:323:0x04d5 A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Removed duplicated region for block: B:326:0x04db  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean interceptSystemKeysAndShortcuts(android.os.IBinder r24, android.view.KeyEvent r25) {
        /*
            Method dump skipped, instruction units count: 1494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.policy.PhoneWindowManager.interceptSystemKeysAndShortcuts(android.os.IBinder, android.view.KeyEvent):boolean");
    }

    private boolean prepareToSendSystemKeyToApplication(android.os.IBinder focusedToken, android.view.KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (!event.isSystem()) {
            android.util.Log.wtf(TAG, "Illegal keycode provided to prepareToSendSystemKeyToApplication: " + android.view.KeyEvent.keyCodeToString(keyCode));
            return false;
        }
        boolean isDown = event.getAction() == 0;
        if (isDown && event.getRepeatCount() == 0) {
            com.android.internal.policy.KeyInterceptionInfo info = this.mWindowManagerInternal.getKeyInterceptionInfoFromToken(focusedToken);
            if (info != null && this.mButtonOverridePermissionChecker.canAppOverrideSystemKey(this.mContext, info.windowOwnerUid)) {
                return true;
            }
            setDeferredKeyActionsExecutableAsync(keyCode, event.getDownTime());
            return false;
        }
        java.util.Set<java.lang.Integer> consumedKeys = this.mConsumedKeysForDevice.get(event.getDeviceId());
        boolean wasConsumed = consumedKeys != null && consumedKeys.contains(java.lang.Integer.valueOf(keyCode));
        return !wasConsumed;
    }

    private void setDeferredKeyActionsExecutableAsync(int keyCode, long downTime) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 27);
        msg.arg1 = keyCode;
        msg.obj = java.lang.Long.valueOf(downTime);
        msg.setAsynchronous(true);
        msg.sendToTarget();
    }

    private void injectBackGesture(long downtime) {
        android.view.KeyEvent downEvent = new android.view.KeyEvent(downtime, downtime, 0, 4, 0, 0, -1, 0, 72, 257);
        this.mInputManager.injectInputEvent(downEvent, 0);
        android.view.KeyEvent upEvent = android.view.KeyEvent.changeAction(downEvent, 1);
        this.mInputManager.injectInputEvent(upEvent, 0);
        downEvent.recycle();
        upEvent.recycle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleHomeShortcuts(android.os.IBinder focusedToken, android.view.KeyEvent event) {
        com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler handler = this.mDisplayHomeButtonHandlers.get(event.getDisplayId());
        if (handler == null) {
            handler = new com.android.server.policy.PhoneWindowManager.DisplayHomeButtonHandler(event.getDisplayId());
            this.mDisplayHomeButtonHandlers.put(event.getDisplayId(), handler);
        }
        return handler.handleHomeButton(focusedToken, event);
    }

    private void toggleMicrophoneMuteFromKey() {
        int toastTextResId;
        if (this.mSensorPrivacyManager.supportsSensorToggle(1, 1)) {
            boolean isEnabled = this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 1);
            this.mSensorPrivacyManager.setSensorPrivacy(1, !isEnabled);
            if (isEnabled) {
                toastTextResId = android.R.string.mediasize_na_junior_legal;
            } else {
                toastTextResId = android.R.string.mediasize_na_index_5x8;
            }
            android.widget.Toast.makeText(this.mContext, com.android.server.UiThread.get().getLooper(), this.mContext.getString(toastTextResId), 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptBugreportGestureTv() {
        this.mHandler.removeMessages(18);
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 18);
        msg.setAsynchronous(true);
        this.mHandler.sendMessageDelayed(msg, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelBugreportGestureTv() {
        this.mHandler.removeMessages(18);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interceptAccessibilityGestureTv() {
        this.mHandler.removeMessages(19);
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 19);
        msg.setAsynchronous(true);
        this.mHandler.sendMessageDelayed(msg, getAccessibilityShortcutTimeout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAccessibilityGestureTv() {
        this.mHandler.removeMessages(19);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestBugreportForTv() {
        try {
            if (!android.app.ActivityManager.getService().launchBugReportHandlerApp()) {
                android.app.ActivityManager.getService().requestInteractiveBugReport();
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error taking bugreport", e);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public android.view.KeyEvent dispatchUnhandledKey(android.os.IBinder focusedToken, android.view.KeyEvent event, int policyFlags) {
        android.view.KeyCharacterMap.FallbackAction fallbackAction;
        if (DEBUG_INPUT) {
            com.android.internal.policy.KeyInterceptionInfo info = this.mWindowManagerInternal.getKeyInterceptionInfoFromToken(focusedToken);
            java.lang.String title = info == null ? "<unknown>" : info.windowTitle;
            android.util.Slog.d(TAG, "Unhandled key: inputToken=" + focusedToken + ", title=" + title + ", action=" + event.getAction() + ", flags=" + event.getFlags() + ", keyCode=" + event.getKeyCode() + ", scanCode=" + event.getScanCode() + ", metaState=" + event.getMetaState() + ", repeatCount=" + event.getRepeatCount() + ", policyFlags=" + policyFlags);
        }
        if (interceptUnhandledKey(event, focusedToken)) {
            return null;
        }
        android.view.KeyEvent fallbackEvent = null;
        if ((event.getFlags() & 1024) == 0) {
            android.view.KeyCharacterMap kcm = event.getKeyCharacterMap();
            int keyCode = event.getKeyCode();
            int metaState = event.getMetaState();
            boolean initialDown = event.getAction() == 0 && event.getRepeatCount() == 0;
            if (initialDown) {
                fallbackAction = kcm.getFallbackAction(keyCode, metaState);
            } else {
                fallbackAction = this.mFallbackActions.get(keyCode);
            }
            if (fallbackAction != null) {
                if (DEBUG_INPUT) {
                    android.util.Slog.d(TAG, "Fallback: keyCode=" + fallbackAction.keyCode + " metaState=" + java.lang.Integer.toHexString(fallbackAction.metaState));
                }
                int flags = event.getFlags() | 1024;
                android.view.KeyEvent fallbackEvent2 = android.view.KeyEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), fallbackAction.keyCode, event.getRepeatCount(), fallbackAction.metaState, event.getDeviceId(), event.getScanCode(), flags, event.getSource(), event.getDisplayId(), null);
                if (interceptFallback(focusedToken, fallbackEvent2, policyFlags)) {
                    fallbackEvent = fallbackEvent2;
                } else {
                    fallbackEvent2.recycle();
                    fallbackEvent = null;
                }
                if (initialDown) {
                    this.mFallbackActions.put(keyCode, fallbackAction);
                } else if (event.getAction() == 1) {
                    this.mFallbackActions.remove(keyCode);
                    fallbackAction.recycle();
                }
            }
        }
        if (DEBUG_INPUT) {
            if (fallbackEvent == null) {
                android.util.Slog.d(TAG, "No fallback.");
            } else {
                android.util.Slog.d(TAG, "Performing fallback: " + fallbackEvent);
            }
        }
        return fallbackEvent;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean interceptUnhandledKey(android.view.KeyEvent event, android.os.IBinder focusedToken) {
        int keyCode = event.getKeyCode();
        int repeatCount = event.getRepeatCount();
        boolean down = event.getAction() == 0;
        int metaState = event.getModifiers();
        switch (keyCode) {
            case 54:
                if (down && android.view.KeyEvent.metaStateHasModifiers(metaState, 4098) && this.mAccessibilityShortcutController.isAccessibilityShortcutAvailable(isKeyguardLocked())) {
                    this.mHandler.sendMessage(this.mHandler.obtainMessage(17));
                    return true;
                }
                return false;
            case 62:
                if (down && repeatCount == 0 && android.view.KeyEvent.metaStateHasModifiers(metaState & (-194), 4096)) {
                    int direction = (metaState & 193) != 0 ? -1 : 1;
                    sendSwitchKeyboardLayout(event, focusedToken, direction);
                    return true;
                }
                return false;
            case 111:
                if (down && android.view.KeyEvent.metaStateHasNoModifiers(metaState) && repeatCount == 0) {
                    this.mContext.closeSystemDialogs();
                    return true;
                }
                return false;
            case 120:
                if (down && repeatCount == 0) {
                    interceptScreenshotChord(2, 0L);
                    return true;
                }
                return false;
            case 264:
                handleUnhandledSystemKey(event);
                sendSystemKeyToStatusBarAsync(event);
                return true;
            default:
                return false;
        }
    }

    private void handleUnhandledSystemKey(android.view.KeyEvent event) {
        if (!event.isSystem()) {
            android.util.Log.wtf(TAG, "Illegal keycode provided to handleUnhandledSystemKey: " + android.view.KeyEvent.keyCodeToString(event.getKeyCode()));
        } else if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            setDeferredKeyActionsExecutableAsync(event.getKeyCode(), event.getDownTime());
        }
    }

    private void sendSwitchKeyboardLayout(android.view.KeyEvent event, android.os.IBinder focusedToken, int direction) {
        com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject object = new com.android.server.policy.PhoneWindowManager.SwitchKeyboardLayoutMessageObject(event, focusedToken, direction);
        this.mHandler.obtainMessage(25, object).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSwitchKeyboardLayout(android.view.KeyEvent event, int direction, android.os.IBinder focusedToken) {
        android.os.IBinder targetWindowToken = this.mWindowManagerInternal.getTargetWindowTokenFromInputToken(focusedToken);
        com.android.server.inputmethod.InputMethodManagerInternal.get().onSwitchKeyboardLayoutShortcut(direction, event.getDisplayId(), targetWindowToken);
    }

    private boolean interceptFallback(android.os.IBinder focusedToken, android.view.KeyEvent fallbackEvent, int policyFlags) {
        int actions = interceptKeyBeforeQueueing(fallbackEvent, policyFlags);
        if ((actions & 1) != 0) {
            long delayMillis = interceptKeyBeforeDispatching(focusedToken, fallbackEvent, policyFlags);
            if (delayMillis == 0 && !interceptUnhandledKey(fallbackEvent, focusedToken)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setTopFocusedDisplay(int displayId) {
        this.mTopFocusedDisplayId = displayId;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void registerDisplayFoldListener(android.view.IDisplayFoldListener listener) {
        if (this.mDisplayFoldController != null) {
            this.mDisplayFoldController.registerDisplayFoldListener(listener);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void unregisterDisplayFoldListener(android.view.IDisplayFoldListener listener) {
        if (this.mDisplayFoldController != null) {
            this.mDisplayFoldController.unregisterDisplayFoldListener(listener);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setOverrideFoldedArea(android.graphics.Rect area) {
        if (this.mDisplayFoldController != null) {
            this.mDisplayFoldController.setOverrideFoldedArea(area);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public android.graphics.Rect getFoldedArea() {
        if (this.mDisplayFoldController != null) {
            return this.mDisplayFoldController.getFoldedArea();
        }
        return new android.graphics.Rect();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onDefaultDisplayFocusChangedLw(com.android.server.policy.WindowManagerPolicy.WindowState newFocus) {
        if (this.mDisplayFoldController != null) {
            this.mDisplayFoldController.onDefaultDisplayFocusChanged(newFocus != null ? newFocus.getOwningPackage() : null);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void registerShortcutKey(long shortcutCode, com.android.internal.policy.IShortcutService shortcutService) throws android.os.RemoteException {
        synchronized (this.mLock) {
            this.mModifierShortcutManager.registerShortcutKey(shortcutCode, shortcutService);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onKeyguardOccludedChangedLw(boolean occluded) {
        if (this.mKeyguardDelegate != null) {
            this.mPendingKeyguardOccluded = occluded;
            this.mKeyguardOccludedChanged = true;
            return;
        }
        if (this.mKeyguardOccludedChanged && this.mPendingKeyguardOccluded != occluded) {
            android.util.Slog.d(TAG, "force set mPendingKeyguardOccluded from " + this.mPendingKeyguardOccluded + " to " + occluded);
            this.mPendingKeyguardOccluded = occluded;
        }
        setKeyguardOccludedLw(occluded);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int applyKeyguardOcclusionChange() {
        if (this.mPhoneWindowManagerExt.applyKeyguardOcclusionChange(this.mKeyguardOccludedChanged)) {
            return 0;
        }
        if (DEBUG_KEYGUARD) {
            android.util.Slog.d(TAG, "transition/occluded commit occluded=" + this.mPendingKeyguardOccluded + " changed=" + this.mKeyguardOccludedChanged);
        }
        return setKeyguardOccludedLw(this.mPendingKeyguardOccluded) ? 5 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int handleTransitionForKeyguardLw(boolean startKeyguardExitAnimation, boolean notifyOccluded) {
        int redoLayout = 0;
        if (notifyOccluded) {
            redoLayout = applyKeyguardOcclusionChange();
        }
        if (startKeyguardExitAnimation) {
            if (DEBUG_KEYGUARD) {
                android.util.Slog.d(TAG, "Starting keyguard exit animation");
            }
            startKeyguardExitAnimation(android.os.SystemClock.uptimeMillis());
        }
        return redoLayout;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showDismissibleKeyguard() {
        this.mKeyguardDelegate.showDismissibleKeyguard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchAssistAction(java.lang.String hint, int deviceId, long eventTime, int invocationType, int launchModeEventNumber) {
        sendCloseSystemWindows(SYSTEM_DIALOG_REASON_ASSIST);
        if (!isUserSetupComplete()) {
            return;
        }
        android.os.Bundle args = new android.os.Bundle();
        if (deviceId != -2) {
            args.putInt("android.intent.extra.ASSIST_INPUT_DEVICE_ID", deviceId);
        }
        if (hint != null) {
            args.putBoolean(hint, true);
        }
        args.putLong("android.intent.extra.TIME", eventTime);
        args.putInt("invocation_type", invocationType);
        this.mPhoneWindowManagerExt.getInputExtension().handleAssistLaunchMode(launchModeEventNumber, args);
        android.app.SearchManager searchManager = (android.app.SearchManager) this.mContext.getSystemService(android.app.SearchManager.class);
        if (searchManager != null) {
            searchManager.launchAssist(args);
            return;
        }
        com.android.server.statusbar.StatusBarManagerInternal statusBar = getStatusBarManagerInternal();
        if (statusBar != null) {
            statusBar.startAssist(args);
        }
    }

    private void launchVoiceAssist(boolean allowDuringSetup) {
        boolean keyguardActive = this.mKeyguardDelegate != null && this.mKeyguardDelegate.isShowing();
        if (keyguardActive) {
            this.mKeyguardDelegate.dismissKeyguardToLaunch(new android.content.Intent("android.intent.action.VOICE_ASSIST"));
        } else {
            startActivityAsUser(new android.content.Intent("android.intent.action.VOICE_ASSIST"), null, android.os.UserHandle.CURRENT_OR_SELF, allowDuringSetup);
        }
    }

    private boolean isInRetailMode() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "device_demo_mode", 0) == 1;
    }

    private void startActivityAsUser(android.content.Intent intent, android.os.UserHandle handle) {
        startActivityAsUser(intent, null, handle);
    }

    private void startActivityAsUser(android.content.Intent intent, android.os.Bundle bundle, android.os.UserHandle handle) {
        startActivityAsUser(intent, bundle, handle, false);
    }

    private void startActivityAsUser(android.content.Intent intent, android.os.Bundle bundle, android.os.UserHandle handle, boolean allowDuringSetup) {
        if (allowDuringSetup || isUserSetupComplete()) {
            this.mContext.startActivityAsUser(intent, bundle, handle);
            dismissKeyboardShortcutsMenu();
        } else {
            android.util.Slog.i(TAG, "Not starting activity because user setup is in progress: " + intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void preloadRecentApps() {
        this.mPreloadedRecentApps = true;
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.preloadRecentApps();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelPreloadRecentApps() {
        if (this.mPreloadedRecentApps) {
            this.mPreloadedRecentApps = false;
            com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
            if (statusbar != null) {
                statusbar.cancelPreloadRecentApps();
            }
        }
    }

    private void toggleTaskbar() {
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.toggleTaskbar();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleRecentApps() {
        this.mPreloadedRecentApps = false;
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.toggleRecentApps();
            this.mPhoneWindowManagerExt.onRecentClicked();
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showRecentApps() {
        this.mHandler.removeMessages(9);
        this.mHandler.obtainMessage(9).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showRecentApps(boolean triggeredFromAltTab) {
        this.mPreloadedRecentApps = false;
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.showRecentApps(triggeredFromAltTab);
        }
        dismissKeyboardShortcutsMenu();
    }

    private void toggleKeyboardShortcutsMenu(int deviceId) {
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.toggleKeyboardShortcutsMenu(deviceId);
        }
    }

    private void dismissKeyboardShortcutsMenu() {
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.dismissKeyboardShortcutsMenu();
        }
    }

    private void hideRecentApps(boolean triggeredFromAltTab, boolean triggeredFromHome) {
        this.mPreloadedRecentApps = false;
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.hideRecentApps(triggeredFromAltTab, triggeredFromHome);
        }
    }

    private void moveFocusedTaskToStageSplit(int displayId, boolean leftOrTop) {
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.moveFocusedTaskToStageSplit(displayId, leftOrTop);
        }
    }

    private void setSplitscreenFocus(boolean leftOrTop) {
        com.android.server.statusbar.StatusBarManagerInternal statusbar = getStatusBarManagerInternal();
        if (statusbar != null) {
            statusbar.setSplitscreenFocus(leftOrTop);
        }
    }

    void launchHomeFromHotKey(int displayId) {
        launchHomeFromHotKey(displayId, true, true);
    }

    void launchHomeFromHotKey(final int displayId, final boolean awakenFromDreams, boolean respectKeyguard) {
        this.mPhoneWindowManagerExt.hookForInputLogV("launchHomeFromHotKey displayId=" + displayId + ", awaken=" + awakenFromDreams + ", respect=" + respectKeyguard + ", recentsvisible=" + this.mRecentsVisible);
        if (respectKeyguard) {
            if (isKeyguardShowingAndNotOccluded()) {
                android.util.Log.i(TAG, "Don't launch home because the Keyguard is showing, isKeyguardShowingAndNotOccluded=" + isKeyguardShowingAndNotOccluded());
                return;
            } else if (!isKeyguardOccluded() && this.mKeyguardDelegate.isInputRestricted()) {
                this.mKeyguardDelegate.verifyUnlock(new com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult() { // from class: com.android.server.policy.PhoneWindowManager.12
                    @Override // com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult
                    public void onKeyguardExitResult(boolean success) {
                        if (success) {
                            long origId = android.os.Binder.clearCallingIdentity();
                            try {
                                com.android.server.policy.PhoneWindowManager.this.startDockOrHome(displayId, true, awakenFromDreams);
                            } finally {
                                android.os.Binder.restoreCallingIdentity(origId);
                            }
                        }
                    }
                });
                android.util.Log.i(TAG, "now is in keyguard restricted mode , must unlock before launching home.");
                return;
            }
        }
        if (this.mRecentsVisible) {
            try {
                android.app.ActivityManager.getService().stopAppSwitches();
            } catch (android.os.RemoteException e) {
            }
            if (awakenFromDreams) {
                awakenDreams();
            }
            hideRecentApps(false, true);
            return;
        }
        startDockOrHome(displayId, true, awakenFromDreams);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setRecentsVisibilityLw(boolean visible) {
        this.mRecentsVisible = visible;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setPipVisibilityLw(boolean visible) {
        this.mPictureInPictureVisible = visible;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setNavBarVirtualKeyHapticFeedbackEnabledLw(boolean enabled) {
        this.mNavBarVirtualKeyHapticFeedbackEnabled = enabled;
    }

    private boolean setKeyguardOccludedLw(boolean isOccluded) {
        android.util.Slog.d(TAG, "setKeyguardOccludedLw occluded=" + isOccluded + "," + isKeyguardOccluded() + "," + this.mKeyguardOccludedChanged);
        this.mKeyguardOccludedChanged = false;
        this.mPendingKeyguardOccluded = isOccluded;
        this.mKeyguardDelegate.setOccluded(isOccluded, true);
        return this.mKeyguardDelegate.isShowing();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void notifyLidSwitchChanged(long j, boolean z) {
        if (z != this.mDefaultDisplayPolicy.getLidState()) {
            this.mDefaultDisplayPolicy.setLidState(z ? 1 : 0);
            applyLidSwitchState();
            updateRotation(true);
            if (!z) {
                if (getLidBehavior() != 1) {
                    this.mPowerManager.userActivity(android.os.SystemClock.uptimeMillis(), false);
                    return;
                }
                return;
            }
            this.mWindowWakeUpPolicy.wakeUpFromLid();
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void notifyCameraLensCoverSwitchChanged(long j, boolean z) {
        android.content.Intent intent;
        if (this.mCameraLensCoverState == z || !this.mContext.getResources().getBoolean(android.R.bool.config_hotswapCapable)) {
            return;
        }
        if (this.mCameraLensCoverState == 1 && !z) {
            if (this.mKeyguardDelegate == null ? false : this.mKeyguardDelegate.isShowing()) {
                intent = new android.content.Intent("android.media.action.STILL_IMAGE_CAMERA_SECURE");
            } else {
                intent = new android.content.Intent("android.media.action.STILL_IMAGE_CAMERA");
            }
            this.mWindowWakeUpPolicy.wakeUpFromCameraCover(j / 1000000);
            startActivityAsUser(intent, android.os.UserHandle.CURRENT_OR_SELF);
        }
        this.mCameraLensCoverState = z ? 1 : 0;
    }

    void initializeHdmiState() {
        int oldMask = android.os.StrictMode.allowThreadDiskReadsMask();
        try {
            initializeHdmiStateInternal();
        } finally {
            android.os.StrictMode.setThreadPolicyMask(oldMask);
        }
    }

    void initializeHdmiStateInternal() {
        boolean plugged = false;
        this.mPhoneWindowManagerSocExt.hookInitializeHdmiStateInternal();
        if (new java.io.File("/sys/devices/virtual/switch/hdmi/state").exists()) {
            this.mHDMIObserver.startObserving("DEVPATH=/devices/virtual/switch/hdmi");
            java.io.FileReader reader = null;
            try {
                try {
                    try {
                        try {
                            reader = new java.io.FileReader("/sys/class/switch/hdmi/state");
                            char[] buf = new char[15];
                            int n = reader.read(buf);
                            if (n > 1) {
                                plugged = java.lang.Integer.parseInt(new java.lang.String(buf, 0, n + (-1))) != 0;
                            }
                            reader.close();
                        } catch (java.io.IOException ex) {
                            android.util.Slog.w(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + ex);
                            if (reader != null) {
                                reader.close();
                            }
                        }
                    } catch (java.lang.NumberFormatException ex2) {
                        android.util.Slog.w(TAG, "Couldn't read hdmi state from /sys/class/switch/hdmi/state: " + ex2);
                        if (reader != null) {
                            reader.close();
                        }
                    }
                } catch (java.io.IOException e) {
                }
            } catch (java.lang.Throwable th) {
                if (0 != 0) {
                    try {
                        reader.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                throw th;
            }
        } else {
            java.util.List<com.android.server.ExtconUEventObserver.ExtconInfo> extcons = com.android.server.ExtconUEventObserver.ExtconInfo.getExtconInfoForTypes(new java.lang.String[]{com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_HDMI});
            if (!extcons.isEmpty()) {
                com.android.server.policy.PhoneWindowManager.HdmiVideoExtconUEventObserver observer = new com.android.server.policy.PhoneWindowManager.HdmiVideoExtconUEventObserver();
                plugged = observer.init(extcons.get(0));
                this.mHDMIObserver = observer;
            } else if (localLOGV) {
                android.util.Slog.v(TAG, "Not observing HDMI plug state because HDMI was not found.");
            }
        }
        this.mDefaultDisplayPolicy.setHdmiPlugged(plugged, true);
    }

    private boolean allowShowBfs() {
        boolean getXmlAllow = this.mPhoneWindowManagerExt.getBfsKeyAllowEvents();
        boolean getPropVersion = android.os.SystemProperties.get("ro.build.version.ota", "ota_version").contains("PRE");
        android.util.Slog.v(TAG, "getBfsKeyAllowEvents = " + this.mPhoneWindowManagerExt.getBfsKeyAllowEvents());
        return getPropVersion && getXmlAllow;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int interceptKeyBeforeQueueing(android.view.KeyEvent keyEvent, int i) {
        int i2;
        boolean z;
        boolean z2;
        boolean z3;
        android.telecom.TelecomManager telecommService;
        int mode;
        boolean z4;
        boolean z5;
        boolean z6;
        com.android.server.policy.PhoneWindowManager.HdmiControl hdmiControl;
        int keyCode = keyEvent.getKeyCode();
        boolean z7 = keyEvent.getAction() == 0;
        boolean z8 = (i & 1) != 0 || keyEvent.isWakeKey();
        if (!this.mSystemBooted) {
            boolean z9 = false;
            if (z7 && (keyCode == 26 || keyCode == 177)) {
                wakeUpFromWakeKey(keyEvent);
                z9 = true;
            } else if (z7 && ((z8 || keyCode == 224) && isWakeKeyWhenScreenOff(keyCode))) {
                wakeUpFromWakeKey(keyEvent);
                z9 = true;
            }
            if (z9 && (hdmiControl = getHdmiControl()) != null) {
                hdmiControl.turnOnTv();
            }
            return 0;
        }
        boolean z10 = (536870912 & i) != 0;
        boolean zIsCanceled = keyEvent.isCanceled();
        int displayId = keyEvent.getDisplayId();
        boolean z11 = (16777216 & i) != 0;
        android.util.Log.d(TAG, "interceptKeyTq keycode=" + keyCode + " interactive=" + z10 + " keyguardActive=" + (this.mKeyguardDelegate != null && (!z10 ? !this.mKeyguardDelegate.isShowing() : !isKeyguardShowingAndNotOccluded())) + " policyFlags=" + java.lang.Integer.toHexString(i) + " isInjected=" + z11 + " isWakeKey=" + z8);
        if (this.mAllowSetKeyData) {
            this.mTheiaManagerExt.setKeyEvents(z10, keyCode, "theia_bfs_data_catch");
        }
        this.mPhoneWindowManagerExt.hookForInputLogV("collect black screen end");
        if (z10 || (z11 && !z8)) {
            i2 = 1;
            if (!z10) {
                z = false;
            } else {
                if (keyCode == this.mPendingWakeKey && !z7) {
                    i2 = 0;
                }
                this.mPendingWakeKey = -1;
                z = false;
            }
        } else if (shouldDispatchInputWhenNonInteractive(displayId, keyCode)) {
            i2 = 1;
            this.mPendingWakeKey = -1;
            z = z8;
        } else {
            i2 = 0;
            if (z8 && (!z7 || !isWakeKeyWhenScreenOff(keyCode))) {
                z8 = false;
            }
            if (z8 && z7) {
                this.mPendingWakeKey = keyCode;
            }
            z = z8;
        }
        if (isValidGlobalKey(keyCode) && this.mGlobalKeyManager.shouldHandleGlobalKey(keyCode)) {
            if (!z10 && z && z7 && this.mGlobalKeyManager.shouldDispatchFromNonInteractive(keyCode)) {
                this.mGlobalKeyManager.setBeganFromNonInteractive();
                i2 = 1;
                this.mPendingWakeKey = -1;
            }
            if (z) {
                wakeUpFromWakeKey(keyEvent);
            }
            return i2;
        }
        android.hardware.hdmi.HdmiControlManager hdmiControlManager = getHdmiControlManager();
        if (keyCode == 177 && this.mHasFeatureLeanback && (hdmiControlManager == null || !hdmiControlManager.shouldHandleTvPowerKey())) {
            return interceptKeyBeforeQueueing(android.view.KeyEvent.obtain(keyEvent.getDownTime(), keyEvent.getEventTime(), keyEvent.getAction(), 26, keyEvent.getRepeatCount(), keyEvent.getMetaState(), keyEvent.getDeviceId(), keyEvent.getScanCode(), keyEvent.getFlags(), keyEvent.getSource(), keyEvent.getDisplayId(), null), i);
        }
        boolean zIsOnState = android.view.Display.isOnState(this.mDefaultDisplay.getState());
        boolean z12 = z10 && this.mDefaultDisplayPolicy.isAwake();
        if ((keyEvent.getFlags() & 1024) != 0) {
            z2 = z12;
        } else {
            z2 = z12;
            handleKeyGesture(keyEvent, z2, zIsOnState);
        }
        boolean zInterceptAppSwitchEventBeforeQueueing = z7 && (i & 2) != 0 && (!((keyEvent.getFlags() & 64) != 0) || this.mNavBarVirtualKeyHapticFeedbackEnabled) && keyEvent.getRepeatCount() == 0;
        if (this.mPhoneWindowManagerExt.interceptKeyEventForAppShareModeIfNeed(keyEvent)) {
            return 0;
        }
        this.mPhoneWindowManagerExt.hookForInputLogV("interceptKeyBeforeQueueing: Handle special keys.");
        boolean z13 = z;
        if (android.os.Trace.isTagEnabled(4L)) {
            android.os.Trace.traceBegin(4L, "HandleKey:keyCode=" + keyCode + " down?=" + z7);
        }
        switch (keyCode) {
            case 3:
                if (z7 && keyEvent.getDownTime() - keyEvent.getEventTime() == keyCode) {
                    zInterceptAppSwitchEventBeforeQueueing = false;
                    z3 = z13;
                }
                break;
            case 4:
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.BACK);
                if (z7) {
                    this.mWindowManagerInternal.moveFocusToAdjacentEmbeddedActivityIfNeeded();
                    this.mBackKeyHandled = false;
                    this.mPhoneWindowManagerExt.hookForInputLogV("interceptKeyBeforeQueueing back key : handle down end.");
                } else {
                    if (!hasLongPressOnBackBehavior()) {
                        this.mBackKeyHandled |= backKeyPress();
                    }
                    if (this.mBackKeyHandled) {
                        i2 &= -2;
                        z3 = z13;
                    }
                }
                z3 = z13;
                break;
            case 5:
                if (z7 && (telecommService = getTelecommService()) != null && telecommService.isRinging()) {
                    android.util.Log.i(TAG, "interceptKeyBeforeQueueing: CALL key-down while ringing: Answer the call!");
                    telecommService.acceptRingingCall();
                    i2 &= -2;
                }
                z3 = z13;
                break;
            case 6:
                i2 &= -2;
                if (!z7) {
                    if (!this.mEndCallKeyHandled) {
                        this.mHandler.removeCallbacks(this.mEndCallLongPress);
                        if (!zIsCanceled && (((this.mEndcallBehavior & 1) == 0 || !goHome()) && (this.mEndcallBehavior & 2) != 0)) {
                            sleepDefaultDisplay(keyEvent.getEventTime(), 4, 0);
                            z3 = false;
                            break;
                        }
                    }
                } else {
                    android.telecom.TelecomManager telecommService2 = getTelecommService();
                    boolean zEndCall = false;
                    if (telecommService2 != null) {
                        zEndCall = telecommService2.endCall();
                    }
                    if (!z10 || zEndCall) {
                        this.mEndCallKeyHandled = true;
                    } else {
                        this.mEndCallKeyHandled = false;
                        this.mHandler.postDelayed(this.mEndCallLongPress, android.view.ViewConfiguration.get(this.mContext).getDeviceGlobalActionKeyTimeout());
                    }
                }
                z3 = z13;
                break;
            case 24:
            case 25:
            case 164:
                logKeyboardSystemsEventOnActionDown(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.getVolumeEvent(keyCode));
                this.mPhoneWindowManagerExt.hookForInputLogV("interceptKeyBeforeQueueing volume key : " + i2 + " | " + this.mUseTvRouting + " | " + this.mHandleVolumeKeysInWM);
                if (!z7) {
                    zInterceptAppSwitchEventBeforeQueueing = zInterceptAppSwitchEventBeforeQueueing;
                } else {
                    sendSystemKeyToStatusBarAsync(keyEvent);
                    android.app.NotificationManager notificationService = getNotificationService();
                    if (notificationService != null && !this.mHandleVolumeKeysInWM) {
                        notificationService.silenceNotificationSound();
                    }
                    android.telecom.TelecomManager telecommService3 = getTelecommService();
                    if (telecommService3 != null && !this.mHandleVolumeKeysInWM && telecommService3.isRinging()) {
                        android.util.Log.i(TAG, "interceptKeyBeforeQueueing: VOLUME key-down while ringing: Silence ringer!");
                        telecommService3.silenceRinger();
                        i2 &= -2;
                        zInterceptAppSwitchEventBeforeQueueing = zInterceptAppSwitchEventBeforeQueueing;
                        z3 = z13;
                    } else {
                        try {
                            mode = getAudioService().getMode();
                        } catch (java.lang.Exception e) {
                            android.util.Log.e(TAG, "Error getting AudioService in interceptKeyBeforeQueueing.", e);
                            mode = 0;
                        }
                        if (((telecommService3 != null && telecommService3.isInCall()) || mode == 3) && (i2 & 1) == 0) {
                            zInterceptAppSwitchEventBeforeQueueing = zInterceptAppSwitchEventBeforeQueueing;
                            android.media.session.MediaSessionLegacyHelper.getHelper(this.mContext).sendVolumeKeyEvent(keyEvent, Integer.MIN_VALUE, false);
                        } else {
                            zInterceptAppSwitchEventBeforeQueueing = zInterceptAppSwitchEventBeforeQueueing;
                            this.mPhoneWindowManagerExt.hookForInputLogV("interceptKeyBeforeQueueing volume key : handle end");
                        }
                    }
                }
                if (this.mUseTvRouting || this.mHandleVolumeKeysInWM) {
                    i2 |= 1;
                    z3 = z13;
                } else if ((i2 & 1) == 0) {
                    android.media.session.MediaSessionLegacyHelper.getHelper(this.mContext).sendVolumeKeyEvent(keyEvent, Integer.MIN_VALUE, true);
                }
                break;
            case 26:
                z4 = zInterceptAppSwitchEventBeforeQueueing;
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.TOGGLE_POWER);
                com.android.server.policy.EventLogTags.writeInterceptPower(android.view.KeyEvent.actionToString(keyEvent.getAction()), this.mPowerKeyHandled ? 1 : 0, this.mSingleKeyGestureDetector.getKeyPressCounter(26));
                i2 &= -2;
                z3 = false;
                if (z7) {
                    interceptPowerKeyDown(keyEvent, z2);
                } else {
                    interceptPowerKeyUp(keyEvent, zIsCanceled);
                }
                zInterceptAppSwitchEventBeforeQueueing = z4;
                break;
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 126:
            case 127:
            case 130:
            case 222:
                z5 = zInterceptAppSwitchEventBeforeQueueing;
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.MEDIA_KEY);
                if (android.media.session.MediaSessionLegacyHelper.getHelper(this.mContext).isGlobalPriorityActive()) {
                    i2 &= -2;
                }
                if ((i2 & 1) == 0) {
                    this.mBroadcastWakeLock.acquire();
                    android.os.Message messageObtainMessage = this.mHandler.obtainMessage(3, new android.view.KeyEvent(keyEvent));
                    messageObtainMessage.setAsynchronous(true);
                    messageObtainMessage.sendToTarget();
                }
                zInterceptAppSwitchEventBeforeQueueing = z5;
                z3 = z13;
                break;
            case 91:
                z5 = zInterceptAppSwitchEventBeforeQueueing;
                i2 &= -2;
                if (z7 && keyEvent.getRepeatCount() == 0) {
                    logKeyboardSystemsEvent(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.SYSTEM_MUTE);
                    toggleMicrophoneMuteFromKey();
                }
                zInterceptAppSwitchEventBeforeQueueing = z5;
                z3 = z13;
                break;
            case 171:
                if (this.mShortPressOnWindowBehavior != 1 || !this.mPictureInPictureVisible) {
                    zInterceptAppSwitchEventBeforeQueueing = zInterceptAppSwitchEventBeforeQueueing;
                } else {
                    if (!z7) {
                        showPictureInPictureMenu(keyEvent);
                    }
                    i2 &= -2;
                    zInterceptAppSwitchEventBeforeQueueing = zInterceptAppSwitchEventBeforeQueueing;
                    z3 = z13;
                }
                break;
            case 177:
                z4 = zInterceptAppSwitchEventBeforeQueueing;
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.TOGGLE_POWER);
                i2 &= -2;
                z3 = false;
                if (z7 && hdmiControlManager != null) {
                    hdmiControlManager.toggleAndFollowTvPower();
                }
                zInterceptAppSwitchEventBeforeQueueing = z4;
                break;
            case 187:
            case 987:
                zInterceptAppSwitchEventBeforeQueueing = this.mPhoneWindowManagerExt.getInputExtension().interceptAppSwitchEventBeforeQueueing(keyEvent, zInterceptAppSwitchEventBeforeQueueing);
                z3 = z13;
                break;
            case 219:
                boolean z14 = keyEvent.getRepeatCount() > 0;
                if (z7 && !z14) {
                    android.os.Message messageObtainMessage2 = this.mHandler.obtainMessage(23, keyEvent.getDeviceId(), 0, java.lang.Long.valueOf(keyEvent.getEventTime()));
                    this.mPhoneWindowManagerExt.getInputExtension().setLaunchModeInBundleWithDefault(messageObtainMessage2);
                    messageObtainMessage2.setAsynchronous(true);
                    messageObtainMessage2.sendToTarget();
                    logKeyboardSystemsEvent(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.LAUNCH_ASSISTANT);
                }
                i2 &= -2;
                z3 = z13;
                break;
            case com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED /* 223 */:
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.SLEEP);
                i2 &= -2;
                if (!this.mPowerManager.isInteractive()) {
                    zInterceptAppSwitchEventBeforeQueueing = false;
                }
                if (z7) {
                    sleepPress();
                    z6 = false;
                } else {
                    z6 = false;
                    sleepRelease(keyEvent.getEventTime());
                }
                sendSystemKeyToStatusBarAsync(keyEvent);
                z3 = z6;
                break;
            case com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS /* 224 */:
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.WAKEUP);
                i2 &= -2;
                z3 = true;
                break;
            case 231:
                if (!z7) {
                    this.mBroadcastWakeLock.acquire();
                    android.os.Message messageObtainMessage3 = this.mHandler.obtainMessage(12);
                    messageObtainMessage3.setAsynchronous(true);
                    messageObtainMessage3.sendToTarget();
                    logKeyboardSystemsEvent(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.LAUNCH_VOICE_ASSISTANT);
                }
                i2 &= -2;
                z3 = z13;
                break;
            case 264:
                if (z7 && keyEvent.getRepeatCount() == 0 && (i2 & 1) == 0) {
                    setDeferredKeyActionsExecutableAsync(keyCode, keyEvent.getDownTime());
                }
                z3 = z13;
                break;
            case 276:
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.SLEEP);
                i2 &= -2;
                z3 = false;
                if (!z7) {
                    this.mPowerManagerInternal.setUserInactiveOverrideFromWindowManager();
                }
                sendSystemKeyToStatusBarAsync(keyEvent);
                break;
            case 280:
            case 281:
            case com.android.internal.util.FrameworkStatsLog.DISPLAY_WAKE_REPORTED /* 282 */:
            case 283:
                logKeyboardSystemsEventOnActionUp(keyEvent, com.android.server.input.KeyboardMetricsCollector.KeyboardLogEvent.SYSTEM_NAVIGATION);
                i2 &= -2;
                interceptSystemNavigationKey(keyEvent);
                z3 = z13;
                break;
            case 289:
            case 290:
            case 291:
            case 292:
            case 293:
            case 294:
            case 295:
            case 296:
            case 297:
            case com.android.internal.util.FrameworkStatsLog.BLOB_COMMITTED /* 298 */:
            case com.android.internal.util.FrameworkStatsLog.BLOB_LEASED /* 299 */:
            case 300:
            case 301:
            case 302:
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_SERVICE_LAUNCH /* 303 */:
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_KEY_CHAIN /* 304 */:
                i2 &= -2;
                z3 = z13;
                break;
            case 308:
            case 309:
            case 310:
            case 311:
                android.util.Slog.i(TAG, "Stylus buttons event: " + keyCode + " received. Should handle event? " + this.mStylusButtonsEnabled);
                if (this.mStylusButtonsEnabled) {
                    sendSystemKeyToStatusBarAsync(keyEvent);
                }
                i2 &= -2;
                z3 = z13;
                break;
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_BUTTON /* 313 */:
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_EVENT_SMS /* 314 */:
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_EVENT_MMS /* 315 */:
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_SHELL /* 316 */:
                i2 &= -2;
                z3 = z13;
                break;
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_SESSION_CALLBACK /* 317 */:
                if (!com.android.internal.hidden_from_bootclasspath.com.android.hardware.input.Flags.emojiAndScreenshotKeycodesAvailable()) {
                    i2 &= -2;
                    z3 = z13;
                }
                break;
            default:
                z3 = z13;
                break;
        }
        if (zInterceptAppSwitchEventBeforeQueueing) {
            performHapticFeedback(1, false, "Virtual Key - Press");
        }
        if (z3) {
            wakeUpFromWakeKey(keyEvent);
        }
        if ((i2 & 1) != 0 && displayId != -1 && displayId != this.mTopFocusedDisplayId) {
            android.util.Log.i(TAG, "Attempting to move non-focused display " + displayId + " to top because a key is targeting it");
            this.mWindowManagerFuncs.moveDisplayToTopIfAllowed(displayId);
            this.mPhoneWindowManagerExt.hookForInputLogV("moveDisplayToTopIfAllowed end, mTopFocusedDisplayId=" + this.mTopFocusedDisplayId + " result=" + i2);
        }
        android.os.Trace.traceEnd(4L);
        return i2;
    }

    private void handleKeyGesture(android.view.KeyEvent event, boolean interactive, boolean defaultDisplayOn) {
        if (this.mKeyCombinationManager.interceptKey(event, interactive)) {
            this.mSingleKeyGestureDetector.reset();
            return;
        }
        if (event.getKeyCode() == 26 && event.getAction() == 0) {
            this.mPowerKeyHandled = handleCameraGesture(event, interactive);
            if (this.mPowerKeyHandled) {
                this.mSingleKeyGestureDetector.reset();
                return;
            }
        }
        this.mSingleKeyGestureDetector.interceptKey(event, interactive, defaultDisplayOn);
    }

    private boolean handleCameraGesture(android.view.KeyEvent event, boolean interactive) {
        if (!this.mPhoneWindowManagerExt.getInputExtension().isCameraGestureEnabled() || this.mGestureLauncherService == null) {
            return false;
        }
        this.mCameraGestureTriggered = false;
        android.util.MutableBoolean outLaunched = new android.util.MutableBoolean(false);
        boolean intercept = this.mGestureLauncherService.interceptPowerKeyDown(event, interactive, outLaunched);
        if (!outLaunched.value) {
            return intercept;
        }
        this.mCameraGestureTriggered = true;
        if (this.mRequestedOrSleepingDefaultDisplay) {
            this.mCameraGestureTriggeredDuringGoingToSleep = true;
            this.mWindowWakeUpPolicy.wakeUpFromPowerKeyCameraGesture();
        }
        return true;
    }

    private void interceptSystemNavigationKey(android.view.KeyEvent event) {
        if (event.getAction() == 1) {
            if ((!this.mAccessibilityManager.isEnabled() || !this.mAccessibilityManager.sendFingerprintGesture(event.getKeyCode())) && this.mSystemNavigationKeysEnabled) {
                sendSystemKeyToStatusBarAsync(event);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSystemKeyToStatusBar(android.view.KeyEvent key) {
        com.android.internal.statusbar.IStatusBarService statusBar = getStatusBarService();
        if (statusBar != null) {
            try {
                statusBar.handleSystemKey(key);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void sendSystemKeyToStatusBarAsync(android.view.KeyEvent keyEvent) {
        android.os.Message message = this.mHandler.obtainMessage(21, android.view.KeyEvent.obtain(keyEvent));
        message.setAsynchronous(true);
        this.mHandler.sendMessage(message);
    }

    private static boolean isValidGlobalKey(int keyCode) {
        switch (keyCode) {
            case 26:
            case com.android.internal.util.FrameworkStatsLog.EXCLUSION_RECT_STATE_CHANGED /* 223 */:
            case com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS /* 224 */:
                return false;
            default:
                return true;
        }
    }

    private boolean isWakeKeyWhenScreenOff(int keyCode) {
        switch (keyCode) {
            case 4:
                return this.mWakeOnBackKeyPress;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                return this.mWakeOnDpadKeyPress;
            case 219:
                return this.mWakeOnAssistKeyPress;
            case 308:
            case 309:
            case 310:
            case 311:
                return this.mStylusButtonsEnabled;
            default:
                return true;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int interceptMotionBeforeQueueingNonInteractive(int displayId, int source, int action, long whenNanos, int policyFlags) {
        if ((policyFlags & 1) != 0) {
            if (this.mWindowWakeUpPolicy.wakeUpFromMotion(whenNanos / 1000000, source, action == 0)) {
                return 1;
            }
        }
        if (shouldDispatchInputWhenNonInteractive(displayId, 0)) {
            return 1;
        }
        if (isTheaterModeEnabled() && (policyFlags & 1) != 0) {
            if (this.mWindowWakeUpPolicy.wakeUpFromMotion(whenNanos / 1000000, source, action == 0)) {
                return 1;
            }
        }
        return 0;
    }

    private boolean shouldDispatchInputWhenNonInteractive(int displayId, int keyCode) {
        android.view.Display display;
        android.service.dreams.IDreamManager dreamManager;
        boolean isDefaultDisplay = displayId == 0 || displayId == -1;
        if (isDefaultDisplay) {
            display = this.mDefaultDisplay;
        } else {
            display = this.mDisplayManager.getDisplay(displayId);
        }
        boolean displayOff = display == null || display.getState() == 1;
        if (displayOff) {
            this.mPhoneWindowManagerExt.hookForInputLogV("shouldDispatchInputWhenNonInteractive : " + displayOff + " | " + isDefaultDisplay + " | " + this.mHasFeatureWatch);
            return false;
        }
        if (isKeyguardShowingAndNotOccluded()) {
            return true;
        }
        if (isDefaultDisplay && (dreamManager = getDreamManager()) != null) {
            try {
                if (dreamManager.isDreaming()) {
                    this.mPhoneWindowManagerExt.hookForInputLogV("shouldDispatchInputWhenNonInteractive dreaming!");
                    return true;
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException when checking if dreaming", e);
            }
        }
        return false;
    }

    private void dispatchDirectAudioEvent(android.view.KeyEvent event) {
        android.hardware.hdmi.HdmiAudioSystemClient audioSystemClient;
        android.hardware.hdmi.HdmiControlManager hdmiControlManager = getHdmiControlManager();
        if (hdmiControlManager != null && !hdmiControlManager.getSystemAudioMode() && shouldCecAudioDeviceForwardVolumeKeysSystemAudioModeOff() && (audioSystemClient = hdmiControlManager.getAudioSystemClient()) != null) {
            audioSystemClient.sendKeyEvent(event.getKeyCode(), event.getAction() == 0);
            return;
        }
        try {
            getAudioService().handleVolumeKey(event, this.mUseTvRouting, this.mContext.getOpPackageName(), TAG);
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Error dispatching volume key in handleVolumeKey for event:" + event, e);
        }
    }

    private android.hardware.hdmi.HdmiControlManager getHdmiControlManager() {
        if (!this.mHasFeatureHdmiCec) {
            return null;
        }
        return (android.hardware.hdmi.HdmiControlManager) this.mContext.getSystemService(android.hardware.hdmi.HdmiControlManager.class);
    }

    private boolean shouldCecAudioDeviceForwardVolumeKeysSystemAudioModeOff() {
        return com.android.internal.os.RoSystemProperties.CEC_AUDIO_DEVICE_FORWARD_VOLUME_KEYS_SYSTEM_AUDIO_MODE_OFF;
    }

    void dispatchMediaKeyWithWakeLock(android.view.KeyEvent event) {
        if (DEBUG_INPUT) {
            android.util.Slog.d(TAG, "dispatchMediaKeyWithWakeLock: " + event);
        }
        if (this.mHavePendingMediaKeyRepeatWithWakeLock) {
            if (DEBUG_INPUT) {
                android.util.Slog.d(TAG, "dispatchMediaKeyWithWakeLock: canceled repeat");
            }
            this.mHandler.removeMessages(4);
            this.mHavePendingMediaKeyRepeatWithWakeLock = false;
            this.mBroadcastWakeLock.release();
        }
        dispatchMediaKeyWithWakeLockToAudioService(event);
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            this.mHavePendingMediaKeyRepeatWithWakeLock = true;
            android.os.Message msg = this.mHandler.obtainMessage(4, event);
            msg.setAsynchronous(true);
            this.mHandler.sendMessageDelayed(msg, android.view.ViewConfiguration.getKeyRepeatTimeout());
            return;
        }
        this.mBroadcastWakeLock.release();
    }

    void dispatchMediaKeyRepeatWithWakeLock(android.view.KeyEvent event) {
        this.mHavePendingMediaKeyRepeatWithWakeLock = false;
        android.view.KeyEvent repeatEvent = android.view.KeyEvent.changeTimeRepeat(event, android.os.SystemClock.uptimeMillis(), 1, event.getFlags() | 128);
        if (DEBUG_INPUT) {
            android.util.Slog.d(TAG, "dispatchMediaKeyRepeatWithWakeLock: " + repeatEvent);
        }
        dispatchMediaKeyWithWakeLockToAudioService(repeatEvent);
        this.mBroadcastWakeLock.release();
    }

    void dispatchMediaKeyWithWakeLockToAudioService(android.view.KeyEvent event) {
        if (this.mActivityManagerInternal.isSystemReady()) {
            android.media.session.MediaSessionLegacyHelper.getHelper(this.mContext).sendMediaButtonEvent(event, true);
        }
    }

    void launchVoiceAssistWithWakeLock() {
        android.content.Intent voiceIntent;
        sendCloseSystemWindows(SYSTEM_DIALOG_REASON_ASSIST);
        if (!keyguardOn()) {
            voiceIntent = new android.content.Intent("android.speech.action.WEB_SEARCH");
        } else {
            android.os.DeviceIdleManager dim = (android.os.DeviceIdleManager) this.mContext.getSystemService(android.os.DeviceIdleManager.class);
            if (dim != null) {
                dim.endIdle("voice-search");
            }
            android.content.Intent voiceIntent2 = new android.content.Intent("android.speech.action.VOICE_SEARCH_HANDS_FREE");
            voiceIntent2.putExtra("android.speech.extras.EXTRA_SECURE", true);
            voiceIntent = voiceIntent2;
        }
        startActivityAsUser(voiceIntent, android.os.UserHandle.CURRENT_OR_SELF);
        this.mBroadcastWakeLock.release();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedWakingUpGlobal(int reason) {
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedWakingUpGlobal(int reason) {
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedGoingToSleepGlobal(int reason) {
        this.mDeviceGoingToSleep = true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedGoingToSleepGlobal(int reason) {
        this.mDeviceGoingToSleep = false;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedGoingToSleep(int displayGroupId, int pmSleepReason) {
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Started going to sleep... (groupId=" + displayGroupId + " why=" + android.view.WindowManagerPolicyConstants.offReasonToString(android.view.WindowManagerPolicyConstants.translateSleepReasonToOffReason(pmSleepReason)) + ")");
        }
        if (displayGroupId != 0) {
            return;
        }
        this.mRequestedOrSleepingDefaultDisplay = true;
        this.mIsGoingToSleepDefaultDisplay = true;
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.onStartedGoingToSleep(pmSleepReason);
            this.mPhoneWindowManagerExt.startedGoingToSleep();
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedGoingToSleep(int displayGroupId, int pmSleepReason) {
        if (displayGroupId != 0) {
            return;
        }
        com.android.server.policy.EventLogTags.writeScreenToggled(0);
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Finished going to sleep... (groupId=" + displayGroupId + " why=" + android.view.WindowManagerPolicyConstants.offReasonToString(android.view.WindowManagerPolicyConstants.translateSleepReasonToOffReason(pmSleepReason)) + ")");
        }
        com.android.internal.logging.MetricsLogger.histogram(this.mContext, "screen_timeout", this.mLockScreenTimeout / 1000);
        this.mRequestedOrSleepingDefaultDisplay = false;
        this.mIsGoingToSleepDefaultDisplay = false;
        this.mDefaultDisplayPolicy.setAwake(false);
        synchronized (this.mLock) {
            updateWakeGestureListenerLp();
            updateLockScreenTimeout();
        }
        this.mDefaultDisplayRotation.updateOrientationListener();
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.onFinishedGoingToSleep(pmSleepReason, this.mCameraGestureTriggeredDuringGoingToSleep);
        }
        if (this.mDisplayFoldController != null) {
            this.mDisplayFoldController.finishedGoingToSleep();
        }
        this.mCameraGestureTriggeredDuringGoingToSleep = false;
        this.mCameraGestureTriggered = false;
        this.mPhoneWindowManagerExt.resetDeviceFolded();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startedWakingUp(int displayGroupId, int pmWakeReason) {
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Started waking up... (groupId=" + displayGroupId + " why=" + android.view.WindowManagerPolicyConstants.onReasonToString(android.view.WindowManagerPolicyConstants.translateWakeReasonToOnReason(pmWakeReason)) + ")");
        }
        if (displayGroupId != 0) {
            return;
        }
        com.android.server.policy.EventLogTags.writeScreenToggled(1);
        this.mIsGoingToSleepDefaultDisplay = false;
        this.mDefaultDisplayPolicy.setAwake(true);
        synchronized (this.mLock) {
            updateWakeGestureListenerLp();
            updateLockScreenTimeout();
        }
        this.mDefaultDisplayRotation.updateOrientationListener();
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.onStartedWakingUp(pmWakeReason, this.mCameraGestureTriggered);
        }
        this.mCameraGestureTriggered = false;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void finishedWakingUp(int displayGroupId, int pmWakeReason) {
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Finished waking up... (groupId=" + displayGroupId + " why=" + android.view.WindowManagerPolicyConstants.onReasonToString(android.view.WindowManagerPolicyConstants.translateWakeReasonToOnReason(pmWakeReason)) + ")");
        }
        if (displayGroupId != 0) {
            return;
        }
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.onFinishedWakingUp();
        }
        if (this.mDisplayFoldController != null) {
            this.mDisplayFoldController.finishedWakingUp();
        }
    }

    private boolean shouldWakeUpWithHomeIntent() {
        if (this.mWakeUpToLastStateTimeout <= 0) {
            return false;
        }
        long sleepDurationRealtime = this.mPowerManagerInternal.getLastWakeup().sleepDurationRealtime;
        if (DEBUG_WAKEUP) {
            android.util.Log.i(TAG, "shouldWakeUpWithHomeIntent: sleepDurationRealtime= " + sleepDurationRealtime + " mWakeUpToLastStateTimeout= " + this.mWakeUpToLastStateTimeout);
        }
        return sleepDurationRealtime > this.mWakeUpToLastStateTimeout;
    }

    private void wakeUpFromWakeKey(android.view.KeyEvent event) {
        wakeUpFromWakeKey(event.getEventTime(), event.getKeyCode(), event.getAction() == 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpFromWakeKey(long eventTime, int keyCode, boolean isDown) {
        this.mPhoneWindowManagerExt.notePowerkeyProcessStagePoint("POWERKEY_wakeUpFromPowerKey");
        this.mPhoneWindowManagerExt.notifyWakeUpFromPowerKey("action", "screen_on");
        if (this.mPhoneWindowManagerExt.isPowerButtonFpSensor()) {
            this.mPhoneWindowManagerExt.notifyPowerKeyPressed("android.policy:POWER");
        }
        if (this.mWindowWakeUpPolicy.wakeUpFromKey(eventTime, keyCode, isDown)) {
            boolean keyCanLaunchHome = keyCode == 3 || keyCode == 26;
            if (shouldWakeUpWithHomeIntent() && keyCanLaunchHome) {
                startDockOrHome(0, keyCode == 3, true, "Wake from " + android.view.KeyEvent.keyCodeToString(keyCode));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishKeyguardDrawn() {
        if (!this.mDefaultDisplayPolicy.finishKeyguardDrawn()) {
            return;
        }
        this.mPhoneWindowManagerExt.setSwitchingTrackerKeyguardOndrawnEventLog();
        synchronized (this.mLock) {
            if (this.mKeyguardDelegate != null) {
                this.mHandler.removeMessages(6);
            }
        }
        android.os.Trace.asyncTraceBegin(32L, TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD, -1);
        this.mWindowManagerInternal.waitForAllWindowsDrawn(this.mHandler.obtainMessage(7, -1, 0), 1000L, -1);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurnedOff(int displayId, boolean isSwappingDisplay) {
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Display" + displayId + " turned off...");
        }
        if (displayId == 0) {
            boolean acquireSleepToken = !isSwappingDisplay || this.mIsGoingToSleepDefaultDisplay || this.mPhoneWindowManagerExt.shouldGoToSleep();
            this.mRequestedOrSleepingDefaultDisplay = false;
            this.mDefaultDisplayPolicy.screenTurnedOff(acquireSleepToken);
            synchronized (this.mLock) {
                if (this.mKeyguardDelegate != null) {
                    this.mKeyguardDelegate.onScreenTurnedOff();
                }
            }
            this.mPhoneWindowManagerExt.updateOrientationListenerAsyncIfNeeded(this.mDefaultDisplayRotation);
            reportScreenStateToVrManager(false);
        }
        this.mPhoneWindowManagerExt.screenTurnedOff(displayId);
        this.mPhoneWindowManagerExt.updateOrientationListener(displayId);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onDisplaySwitchStart(int displayId) {
        if (displayId == 0) {
            this.mDefaultDisplayPolicy.onDisplaySwitchStart();
        }
    }

    private long getKeyguardDrawnTimeout() {
        boolean bootCompleted = ((com.android.server.SystemServiceManager) com.android.server.LocalServices.getService(com.android.server.SystemServiceManager.class)).isBootCompleted();
        if (bootCompleted) {
            return this.mKeyguardDrawnTimeout;
        }
        return 5000L;
    }

    private com.android.server.wallpaper.WallpaperManagerInternal getWallpaperManagerInternal() {
        if (this.mWallpaperManagerInternal == null) {
            this.mWallpaperManagerInternal = (com.android.server.wallpaper.WallpaperManagerInternal) com.android.server.LocalServices.getService(com.android.server.wallpaper.WallpaperManagerInternal.class);
        }
        return this.mWallpaperManagerInternal;
    }

    private void reportScreenTurningOnToWallpaper(int displayId) {
        com.android.server.wallpaper.WallpaperManagerInternal wallpaperManagerInternal = getWallpaperManagerInternal();
        if (wallpaperManagerInternal != null) {
            wallpaperManagerInternal.onScreenTurningOn(displayId);
        }
    }

    private void reportScreenTurnedOnToWallpaper(int displayId) {
        com.android.server.wallpaper.WallpaperManagerInternal wallpaperManagerInternal = getWallpaperManagerInternal();
        if (wallpaperManagerInternal != null) {
            wallpaperManagerInternal.onScreenTurnedOn(displayId);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurningOn(int displayId, com.android.server.policy.WindowManagerPolicy.ScreenOnListener screenOnListener) {
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Display " + displayId + " turning on...");
        }
        reportScreenTurningOnToWallpaper(displayId);
        if (displayId == 0) {
            android.os.Trace.asyncTraceBegin(32L, "screenTurningOn", 0);
            this.mPhoneWindowManagerExt.setSwitchingTrackerScreenTurningOnEventLog(true);
            this.mDefaultDisplayPolicy.screenTurningOn(screenOnListener);
            this.mBootAnimationDismissable = false;
            synchronized (this.mLock) {
                if (this.mKeyguardDelegate != null && this.mKeyguardDelegate.hasKeyguard()) {
                    this.mHandler.removeMessages(6);
                    this.mHandler.sendEmptyMessageDelayed(6, getKeyguardDrawnTimeout());
                    this.mKeyguardDelegate.onScreenTurningOn(this.mKeyguardDrawnCallback);
                } else {
                    if (DEBUG_WAKEUP) {
                        android.util.Slog.d(TAG, "null mKeyguardDelegate: setting mKeyguardDrawComplete.");
                    }
                    this.mHandler.sendEmptyMessage(5);
                }
            }
            return;
        }
        this.mScreenOnListeners.put(displayId, screenOnListener);
        this.mPhoneWindowManagerExt.screenTurnedOn(displayId, screenOnListener);
        android.os.Trace.asyncTraceBegin(32L, TRACE_WAIT_FOR_ALL_WINDOWS_DRAWN_METHOD, displayId);
        this.mWindowManagerInternal.waitForAllWindowsDrawn(this.mHandler.obtainMessage(7, displayId, 0), 1000L, displayId);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurnedOn(int displayId) {
        if (DEBUG_WAKEUP) {
            android.util.Slog.i(TAG, "Display " + displayId + " turned on...");
        }
        reportScreenTurnedOnToWallpaper(displayId);
        if (displayId != 0) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mKeyguardDelegate != null) {
                this.mKeyguardDelegate.onScreenTurnedOn();
            }
        }
        this.mDefaultDisplayPolicy.screenTurnedOn();
        reportScreenStateToVrManager(true);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void screenTurningOff(int displayId, com.android.server.policy.WindowManagerPolicy.ScreenOffListener screenOffListener) {
        this.mWindowManagerFuncs.screenTurningOff(displayId, screenOffListener);
        if (displayId != 0) {
            return;
        }
        this.mRequestedOrSleepingDefaultDisplay = true;
        synchronized (this.mLock) {
            if (this.mKeyguardDelegate != null) {
                this.mKeyguardDelegate.onScreenTurningOff();
            }
        }
    }

    private void reportScreenStateToVrManager(boolean isScreenOn) {
        if (this.mVrManagerInternal == null) {
            return;
        }
        this.mVrManagerInternal.onScreenStateChanged(isScreenOn);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishWindowsDrawn(int displayId) {
        if (displayId != 0 && displayId != -1) {
            com.android.server.policy.WindowManagerPolicy.ScreenOnListener screenOnListener = (com.android.server.policy.WindowManagerPolicy.ScreenOnListener) this.mScreenOnListeners.removeReturnOld(displayId);
            if (screenOnListener != null) {
                screenOnListener.onScreenOn();
            }
            if (this.mPhoneWindowManagerExt.finishWindowsDrawn(displayId)) {
                this.mPhoneWindowManagerExt.updateOrientationListener(displayId);
                this.mPhoneWindowManagerExt.finishScreenTurningOn(displayId);
                return;
            }
            return;
        }
        this.mPhoneWindowManagerExt.finishWindowsDrawn(displayId);
        if (!this.mDefaultDisplayPolicy.finishWindowsDrawn()) {
            return;
        }
        finishScreenTurningOn();
    }

    private void finishScreenTurningOn() {
        this.mPhoneWindowManagerExt.updateOrientationListenerAsyncIfNeeded(this.mDefaultDisplayRotation);
        com.android.server.policy.WindowManagerPolicy.ScreenOnListener listener = this.mDefaultDisplayPolicy.getScreenOnListener();
        if (!this.mDefaultDisplayPolicy.finishScreenTurningOn()) {
            return;
        }
        android.os.Trace.asyncTraceEnd(32L, "screenTurningOn", 0);
        this.mPhoneWindowManagerExt.setSwitchingTrackerScreenTurningOnEventLog(false);
        enableScreen(listener, true);
    }

    private void enableScreen(com.android.server.policy.WindowManagerPolicy.ScreenOnListener listener, boolean report) {
        boolean enableScreen;
        boolean awake = this.mDefaultDisplayPolicy.isAwake();
        synchronized (this.mLock) {
            if (!this.mKeyguardDrawnOnce && awake) {
                this.mKeyguardDrawnOnce = true;
                enableScreen = true;
                if (this.mBootMessageNeedsHiding) {
                    this.mBootMessageNeedsHiding = false;
                    hideBootMessages();
                }
            } else {
                enableScreen = false;
            }
        }
        if (report && listener != null) {
            listener.onScreenOn();
        }
        if (enableScreen) {
            this.mWindowManagerFuncs.enableScreenIfNeeded();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHideBootMessage() {
        synchronized (this.mLock) {
            if (!this.mKeyguardDrawnOnce) {
                this.mBootMessageNeedsHiding = true;
            } else if (this.mBootMsgDialog != null) {
                if (DEBUG_WAKEUP) {
                    android.util.Slog.d(TAG, "handleHideBootMessage: dismissing");
                }
                this.mBootMsgDialog.dismiss();
                this.mBootMsgDialog = null;
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isScreenOn() {
        return this.mDefaultDisplayPolicy.isScreenOnEarly();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean okToAnimate(boolean ignoreScreenOn) {
        return (ignoreScreenOn || isScreenOn()) && !this.mDeviceGoingToSleep;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void enableKeyguard(boolean enabled) {
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.setKeyguardEnabled(enabled);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void exitKeyguardSecurely(com.android.server.policy.WindowManagerPolicy.OnKeyguardExitResult callback) {
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.verifyUnlock(callback);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardShowing() {
        if (this.mKeyguardDelegate == null) {
            return false;
        }
        return this.mKeyguardDelegate.isShowing();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardShowingAndNotOccluded() {
        return (this.mKeyguardDelegate == null || !this.mKeyguardDelegate.isShowing() || isKeyguardOccluded()) ? false : true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardTrustedLw() {
        if (this.mKeyguardDelegate == null) {
            return false;
        }
        return this.mKeyguardDelegate.isTrusted();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardLocked() {
        return keyguardOn();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardSecure(int userId) {
        if (this.mKeyguardDelegate == null) {
            return false;
        }
        return this.mKeyguardDelegate.isSecure(userId);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardOccluded() {
        if (this.mKeyguardDelegate == null) {
            return false;
        }
        return this.mKeyguardDelegate.isOccluded();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean inKeyguardRestrictedKeyInputMode() {
        if (this.mKeyguardDelegate == null) {
            return false;
        }
        return this.mKeyguardDelegate.isInputRestricted();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardUnoccluding() {
        return keyguardOn() && !this.mWindowManagerFuncs.isAppTransitionStateIdle();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void dismissKeyguardLw(com.android.internal.policy.IKeyguardDismissCallback callback, java.lang.CharSequence message) {
        if (this.mKeyguardDelegate != null && this.mKeyguardDelegate.isShowing()) {
            if (DEBUG_KEYGUARD) {
                android.util.Slog.d(TAG, "PWM.dismissKeyguardLw");
            }
            this.mKeyguardDelegate.dismiss(callback, message);
        } else if (callback != null) {
            try {
                callback.onDismissError();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to call callback", e);
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isKeyguardDrawnLw() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mKeyguardDrawnOnce;
        }
        return z;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void startKeyguardExitAnimation(long startTime) {
        if (this.mKeyguardDelegate != null) {
            if (DEBUG_KEYGUARD) {
                android.util.Slog.d(TAG, "PWM.startKeyguardExitAnimation");
            }
            this.mKeyguardDelegate.startKeyguardExitAnimation(startTime);
        }
    }

    void sendCloseSystemWindows() {
        com.android.internal.policy.PhoneWindow.sendCloseSystemWindows(this.mContext, (java.lang.String) null);
    }

    void sendCloseSystemWindows(java.lang.String reason) {
        com.android.internal.policy.PhoneWindow.sendCloseSystemWindows(this.mContext, reason);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setSafeMode(boolean safeMode) {
        this.mSafeMode = safeMode;
        if (safeMode) {
            performHapticFeedback(10001, true, "Safe Mode Enabled");
        }
    }

    private void bindKeyguard() {
        synchronized (this.mLock) {
            if (this.mKeyguardBound) {
                return;
            }
            this.mKeyguardBound = true;
            this.mKeyguardDelegate.bindService(this.mContext);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void onSystemUiStarted() {
        bindKeyguard();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void systemReady() {
        this.mKeyguardDelegate.onSystemReady();
        this.mVrManagerInternal = (com.android.server.vr.VrManagerInternal) com.android.server.LocalServices.getService(com.android.server.vr.VrManagerInternal.class);
        if (this.mVrManagerInternal != null) {
            this.mVrManagerInternal.addPersistentVrModeStateListener(this.mPersistentVrModeListener);
        }
        readCameraLensCoverState();
        updateUiMode();
        this.mDefaultDisplayRotation.updateOrientationListener();
        synchronized (this.mLock) {
            this.mSystemReady = true;
            updateSettings(this.mHandler);
            if (this.mSystemBooted) {
                this.mKeyguardDelegate.onBootCompleted();
            }
        }
        this.mAutofillManagerInternal = (android.view.autofill.AutofillManagerInternal) com.android.server.LocalServices.getService(android.view.autofill.AutofillManagerInternal.class);
        this.mGestureLauncherService = (com.android.server.GestureLauncherService) com.android.server.LocalServices.getService(com.android.server.GestureLauncherService.class);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void systemBooted() {
        boolean defaultDisplayOn;
        boolean defaultScreenTurningOn;
        bindKeyguard();
        synchronized (this.mLock) {
            this.mSystemBooted = true;
            if (this.mSystemReady) {
                this.mKeyguardDelegate.onBootCompleted();
            }
        }
        this.mSideFpsEventHandler.onFingerprintSensorReady();
        startedWakingUp(0, 0);
        finishedWakingUp(0, 0);
        int defaultDisplayState = this.mDisplayManager.getDisplay(0).getState();
        if (defaultDisplayState == 2) {
            defaultDisplayOn = true;
        } else {
            defaultDisplayOn = false;
        }
        if (this.mDefaultDisplayPolicy.getScreenOnListener() != null) {
            defaultScreenTurningOn = true;
        } else {
            defaultScreenTurningOn = false;
        }
        if (defaultDisplayOn || defaultScreenTurningOn) {
            screenTurningOn(0, this.mDefaultDisplayPolicy.getScreenOnListener());
            screenTurnedOn(0);
        } else {
            this.mBootAnimationDismissable = true;
            enableScreen(null, false);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean canDismissBootAnimation() {
        return this.mDefaultDisplayPolicy.isKeyguardDrawComplete() || this.mBootAnimationDismissable;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void showBootMessage(final java.lang.CharSequence msg, boolean always) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.policy.PhoneWindowManager.15
            @Override // java.lang.Runnable
            public void run() {
                int theme;
                if (com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog == null) {
                    if (com.android.server.policy.PhoneWindowManager.this.mPackageManager.hasSystemFeature("android.software.leanback")) {
                        theme = android.R.style.Theme.Holo.Dialog.FixedSize;
                    } else {
                        theme = 0;
                    }
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog = new android.app.ProgressDialog(com.android.server.policy.PhoneWindowManager.this.mContext, theme) { // from class: com.android.server.policy.PhoneWindowManager.15.1
                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchKeyEvent(android.view.KeyEvent event) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchKeyShortcutEvent(android.view.KeyEvent event) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchTrackballEvent(android.view.MotionEvent ev) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchGenericMotionEvent(android.view.MotionEvent ev) {
                            return true;
                        }

                        @Override // android.app.Dialog, android.view.Window.Callback
                        public boolean dispatchPopulateAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
                            return true;
                        }
                    };
                    if (com.android.server.policy.PhoneWindowManager.this.mPackageManager.isDeviceUpgrading()) {
                        com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.setTitle(android.R.string.allow);
                    } else {
                        com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.setTitle(android.R.string.aerr_wait);
                    }
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.setProgressStyle(0);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.setIndeterminate(true);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.getWindow().setType(2021);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.getWindow().addFlags(258);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.getWindow().setDimAmount(1.0f);
                    android.view.WindowManager.LayoutParams lp = com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.getWindow().getAttributes();
                    lp.screenOrientation = 5;
                    lp.setFitInsetsTypes(0);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.getWindow().setAttributes(lp);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.setCancelable(false);
                    com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.show();
                }
                com.android.server.policy.PhoneWindowManager.this.mBootMsgDialog.setMessage(msg);
            }
        });
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void hideBootMessages() {
        this.mHandler.sendEmptyMessage(11);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void userActivity(int displayGroupId, int event) {
        if (displayGroupId == 0 && event == 2) {
            this.mDefaultDisplayPolicy.onUserActivityEventTouch();
        }
        synchronized (this.mScreenLockTimeout) {
            if (this.mLockScreenTimerActive) {
                this.mHandler.removeCallbacks(this.mScreenLockTimeout);
                this.mHandler.postDelayed(this.mScreenLockTimeout, this.mLockScreenTimeout);
            }
        }
    }

    class ScreenLockTimeout implements java.lang.Runnable {
        android.os.Bundle options;

        ScreenLockTimeout() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this) {
                android.util.Log.v(com.android.server.policy.PhoneWindowManager.TAG, "mScreenLockTimeout activating keyguard");
                if (com.android.server.policy.PhoneWindowManager.this.mKeyguardDelegate != null) {
                    com.android.server.policy.PhoneWindowManager.this.mKeyguardDelegate.doKeyguardTimeout(this.options);
                }
                com.android.server.policy.PhoneWindowManager.this.mLockScreenTimerActive = false;
                com.android.server.policy.PhoneWindowManager.this.mLockNowPending = false;
                this.options = null;
            }
        }

        public void setLockOptions(android.os.Bundle options) {
            this.options = options;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void lockNow(android.os.Bundle options) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
        this.mHandler.removeCallbacks(this.mScreenLockTimeout);
        if (options != null) {
            this.mScreenLockTimeout.setLockOptions(options);
        }
        android.util.Slog.d(TAG, "lockNow  options=" + options + ",mLockScreenTimerActive=" + this.mLockScreenTimerActive);
        this.mHandler.post(this.mScreenLockTimeout);
        synchronized (this.mScreenLockTimeout) {
            this.mLockNowPending = true;
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setAllowLockscreenWhenOn(int displayId, boolean allow) {
        if (allow) {
            this.mAllowLockscreenWhenOnDisplays.add(java.lang.Integer.valueOf(displayId));
        } else {
            this.mAllowLockscreenWhenOnDisplays.remove(java.lang.Integer.valueOf(displayId));
        }
        updateLockScreenTimeout();
    }

    private void updateLockScreenTimeout() {
        synchronized (this.mScreenLockTimeout) {
            if (this.mLockNowPending) {
                android.util.Log.w(TAG, "lockNow pending, ignore updating lockscreen timeout");
                return;
            }
            boolean enable = !this.mAllowLockscreenWhenOnDisplays.isEmpty() && this.mDefaultDisplayPolicy.isAwake() && this.mKeyguardDelegate != null && this.mKeyguardDelegate.isSecure(this.mCurrentUserId);
            if (this.mLockScreenTimerActive != enable) {
                if (enable) {
                    android.util.Log.v(TAG, "setting lockscreen timer");
                    this.mHandler.removeCallbacks(this.mScreenLockTimeout);
                    this.mHandler.postDelayed(this.mScreenLockTimeout, this.mLockScreenTimeout);
                } else {
                    android.util.Log.v(TAG, "clearing lockscreen timer");
                    this.mHandler.removeCallbacks(this.mScreenLockTimeout);
                }
                this.mLockScreenTimerActive = enable;
            }
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void enableScreenAfterBoot() {
        readLidState();
        applyLidSwitchState();
        ((android.hardware.devicestate.DeviceStateManagerInternal) com.android.server.LocalServices.getService(android.hardware.devicestate.DeviceStateManagerInternal.class)).enableDeviceStateAfterBoot(true);
        updateRotation(true);
    }

    private void applyLidSwitchState() {
        int lidState = this.mDefaultDisplayPolicy.getLidState();
        if (lidState == 0) {
            int lidBehavior = getLidBehavior();
            switch (lidBehavior) {
                case 1:
                    sleepDefaultDisplay(android.os.SystemClock.uptimeMillis(), 3, 1);
                    break;
                case 2:
                    this.mWindowManagerFuncs.lockDeviceNow();
                    break;
            }
        }
        synchronized (this.mLock) {
            updateWakeGestureListenerLp();
        }
    }

    void updateUiMode() {
        if (this.mUiModeManager == null) {
            this.mUiModeManager = android.app.IUiModeManager.Stub.asInterface(android.os.ServiceManager.getService("uimode"));
        }
        try {
            this.mUiMode = this.mUiModeManager.getCurrentModeType();
        } catch (android.os.RemoteException e) {
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public int getUiMode() {
        return this.mUiMode;
    }

    void updateRotation(boolean alwaysSendConfiguration) {
        this.mWindowManagerFuncs.updateRotation(alwaysSendConfiguration, false);
    }

    android.content.Intent createHomeDockIntent() {
        android.content.Intent intent = null;
        if (this.mUiMode == 3) {
            if (this.mEnableCarDockHomeCapture) {
                intent = this.mCarDockIntent;
            }
        } else if (this.mUiMode != 2) {
            if (this.mUiMode == 6) {
                int dockMode = this.mDefaultDisplayPolicy.getDockMode();
                if (dockMode == 1 || dockMode == 4 || dockMode == 3) {
                    intent = this.mDeskDockIntent;
                }
            } else if (this.mUiMode == 7) {
                intent = this.mVrHeadsetHomeIntent;
            }
        }
        if (intent == null) {
            return null;
        }
        android.content.pm.ActivityInfo ai = null;
        android.content.pm.ResolveInfo info = this.mPackageManager.resolveActivityAsUser(intent, 65664, this.mCurrentUserId);
        if (info != null) {
            ai = info.activityInfo;
        }
        if (ai == null || ai.metaData == null || !ai.metaData.getBoolean("android.dock_home")) {
            return null;
        }
        android.content.Intent intent2 = new android.content.Intent(intent);
        intent2.setClassName(ai.packageName, ai.name);
        return intent2;
    }

    void startDockOrHome(int displayId, boolean fromHomeKey, boolean awakenFromDreams, java.lang.String startReason) {
        try {
            android.app.ActivityManager.getService().stopAppSwitches();
        } catch (android.os.RemoteException e) {
        }
        sendCloseSystemWindows(SYSTEM_DIALOG_REASON_HOME_KEY);
        if (awakenFromDreams) {
            awakenDreams();
        }
        if (!this.mHasFeatureAuto && !isUserSetupComplete()) {
            android.util.Slog.i(TAG, "Not going home because user setup is in progress.");
            return;
        }
        android.content.Intent dock = createHomeDockIntent();
        if (dock != null) {
            if (fromHomeKey) {
                try {
                    dock.putExtra("android.intent.extra.FROM_HOME_KEY", fromHomeKey);
                } catch (android.content.ActivityNotFoundException e2) {
                }
            }
            startActivityAsUser(dock, android.os.UserHandle.CURRENT);
            return;
        }
        this.mPhoneWindowManagerExt.hookForInputLogV("startDockOrHome: startReason= " + startReason);
        int userId = this.mUserManagerInternal.getUserAssignedToDisplay(displayId);
        this.mActivityTaskManagerInternal.startHomeOnDisplay(userId, startReason, displayId, true, fromHomeKey);
    }

    void startDockOrHome(int displayId, boolean fromHomeKey, boolean awakenFromDreams) {
        startDockOrHome(displayId, fromHomeKey, awakenFromDreams, "startDockOrHome");
    }

    boolean goHome() {
        int result;
        if (!isUserSetupComplete()) {
            android.util.Slog.i(TAG, "Not going home because user setup is in progress.");
            return false;
        }
        try {
            if (android.os.SystemProperties.getInt("persist.sys.uts-test-mode", 0) == 1) {
                android.util.Log.d(TAG, "UTS-TEST-MODE");
            } else {
                android.app.ActivityManager.getService().stopAppSwitches();
                sendCloseSystemWindows();
                android.content.Intent dock = createHomeDockIntent();
                if (dock != null) {
                    int result2 = android.app.ActivityTaskManager.getService().startActivityAsUser((android.app.IApplicationThread) null, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), dock, dock.resolveTypeIfNeeded(this.mContext.getContentResolver()), (android.os.IBinder) null, (java.lang.String) null, 0, 1, (android.app.ProfilerInfo) null, (android.os.Bundle) null, -2);
                    if (result2 == 1) {
                        return false;
                    }
                }
            }
            result = android.app.ActivityTaskManager.getService().startActivityAsUser((android.app.IApplicationThread) null, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), this.mHomeIntent, this.mHomeIntent.resolveTypeIfNeeded(this.mContext.getContentResolver()), (android.os.IBinder) null, (java.lang.String) null, 0, 1, (android.app.ProfilerInfo) null, (android.os.Bundle) null, -2);
        } catch (android.os.RemoteException e) {
        }
        return result != 1;
    }

    private boolean isTheaterModeEnabled() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "theater_mode_on", 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean performHapticFeedback(int effectId, boolean always, java.lang.String reason) {
        return performHapticFeedback(android.os.Process.myUid(), this.mContext.getOpPackageName(), effectId, always, reason, false);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean isGlobalKey(int keyCode) {
        return this.mGlobalKeyManager.shouldHandleGlobalKey(keyCode);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean performHapticFeedback(int uid, java.lang.String packageName, int effectId, boolean always, java.lang.String reason, boolean fromIme) {
        android.os.VibrationEffect effect;
        if (!this.mVibrator.hasVibrator() || (effect = this.mHapticFeedbackVibrationProvider.getVibrationForHapticFeedback(effectId)) == null) {
            return false;
        }
        android.os.VibrationAttributes attrs = this.mHapticFeedbackVibrationProvider.getVibrationAttributesForHapticFeedback(effectId, always, fromIme);
        com.android.server.vibrator.VibratorFrameworkStatsLogger.logPerformHapticsFeedbackIfKeyboard(uid, effectId);
        this.mVibrator.vibrate(uid, packageName, effect, reason, attrs);
        return true;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void keepScreenOnStartedLw() {
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void keepScreenOnStoppedLw() {
        if (isKeyguardShowingAndNotOccluded()) {
            this.mPowerManager.userActivity(android.os.SystemClock.uptimeMillis(), false);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public boolean hasNavigationBar() {
        return this.mDefaultDisplayPolicy.hasNavigationBar();
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setDismissImeOnBackKeyPressed(boolean newValue) {
        this.mDismissImeOnBackKeyPressed = newValue;
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setCurrentUserLw(int newUserId) {
        this.mCurrentUserId = newUserId;
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.setCurrentUser(newUserId);
        }
        if (this.mAccessibilityShortcutController != null) {
            this.mAccessibilityShortcutController.setCurrentUser(newUserId);
        }
        com.android.server.statusbar.StatusBarManagerInternal statusBar = getStatusBarManagerInternal();
        if (statusBar != null) {
            statusBar.setCurrentUser(newUserId);
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void setSwitchingUser(boolean switching) {
        this.mKeyguardDelegate.setSwitchingUser(switching);
        if (switching) {
            dismissKeyboardShortcutsMenu();
        }
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1159641169922L, this.mDefaultDisplayRotation.getUserRotationMode());
        proto.write(1159641169923L, this.mDefaultDisplayRotation.getUserRotation());
        proto.write(1159641169924L, this.mDefaultDisplayRotation.getCurrentAppOrientation());
        proto.write(1133871366149L, this.mDefaultDisplayPolicy.isScreenOnFully());
        proto.write(1133871366150L, this.mDefaultDisplayPolicy.isKeyguardDrawComplete());
        proto.write(1133871366151L, this.mDefaultDisplayPolicy.isWindowManagerDrawComplete());
        proto.write(1133871366156L, isKeyguardOccluded());
        proto.write(1133871366157L, this.mKeyguardOccludedChanged);
        proto.write(1133871366158L, this.mPendingKeyguardOccluded);
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.dumpDebug(proto, 1146756268052L);
        }
        proto.end(token);
    }

    @Override // com.android.server.policy.WindowManagerPolicy
    public void dump(java.lang.String prefix, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.print(prefix);
        pw.print("mSafeMode=");
        pw.print(this.mSafeMode);
        pw.print(" mSystemReady=");
        pw.print(this.mSystemReady);
        pw.print(" mSystemBooted=");
        pw.println(this.mSystemBooted);
        pw.print(prefix);
        pw.print("mCameraLensCoverState=");
        pw.println(com.android.server.policy.WindowManagerPolicy.WindowManagerFuncs.cameraLensStateToString(this.mCameraLensCoverState));
        pw.print(prefix);
        pw.print("mWakeGestureEnabledSetting=");
        pw.println(this.mWakeGestureEnabledSetting);
        pw.print(prefix);
        pw.print("mUiMode=");
        pw.print(android.content.res.Configuration.uiModeToString(this.mUiMode));
        pw.print("mEnableCarDockHomeCapture=");
        pw.println(this.mEnableCarDockHomeCapture);
        pw.print(prefix);
        pw.print("mLidKeyboardAccessibility=");
        pw.print(this.mLidKeyboardAccessibility);
        pw.print(" mLidNavigationAccessibility=");
        pw.print(this.mLidNavigationAccessibility);
        pw.print(" getLidBehavior=");
        pw.println(lidBehaviorToString(getLidBehavior()));
        pw.print(prefix);
        pw.print("mLongPressOnBackBehavior=");
        pw.println(longPressOnBackBehaviorToString(this.mLongPressOnBackBehavior));
        pw.print(prefix);
        pw.print("mLongPressOnHomeBehavior=");
        pw.println(longPressOnHomeBehaviorToString(this.mLongPressOnHomeBehavior));
        pw.print(prefix);
        pw.print("mDoubleTapOnHomeBehavior=");
        pw.println(doubleTapOnHomeBehaviorToString(this.mDoubleTapOnHomeBehavior));
        pw.print(prefix);
        pw.print("mShortPressOnPowerBehavior=");
        pw.println(shortPressOnPowerBehaviorToString(this.mShortPressOnPowerBehavior));
        pw.print(prefix);
        pw.print("mLongPressOnPowerBehavior=");
        pw.println(longPressOnPowerBehaviorToString(this.mLongPressOnPowerBehavior));
        pw.print(prefix);
        pw.print("mSettingsKeyBehavior=");
        pw.println(settingsKeyBehaviorToString(this.mSettingsKeyBehavior));
        pw.print(prefix);
        pw.print("mLongPressOnPowerAssistantTimeoutMs=");
        pw.println(this.mLongPressOnPowerAssistantTimeoutMs);
        pw.print(prefix);
        pw.print("mVeryLongPressOnPowerBehavior=");
        pw.println(veryLongPressOnPowerBehaviorToString(this.mVeryLongPressOnPowerBehavior));
        pw.print(prefix);
        pw.print("mDoublePressOnPowerBehavior=");
        pw.println(multiPressOnPowerBehaviorToString(this.mDoublePressOnPowerBehavior));
        pw.print(prefix);
        pw.print("mTriplePressOnPowerBehavior=");
        pw.println(multiPressOnPowerBehaviorToString(this.mTriplePressOnPowerBehavior));
        pw.print(prefix);
        pw.print("mSupportShortPressPowerWhenDefaultDisplayOn=");
        pw.println(this.mSupportShortPressPowerWhenDefaultDisplayOn);
        pw.print(prefix);
        pw.print("mPowerVolUpBehavior=");
        pw.println(powerVolumeUpBehaviorToString(this.mPowerVolUpBehavior));
        pw.print(prefix);
        pw.print("mShortPressOnSleepBehavior=");
        pw.println(shortPressOnSleepBehaviorToString(this.mShortPressOnSleepBehavior));
        pw.print(prefix);
        pw.print("mShortPressOnWindowBehavior=");
        pw.println(shortPressOnWindowBehaviorToString(this.mShortPressOnWindowBehavior));
        pw.print(prefix);
        pw.print("mShortPressOnStemPrimaryBehavior=");
        pw.println(shortPressOnStemPrimaryBehaviorToString(this.mShortPressOnStemPrimaryBehavior));
        pw.print(prefix);
        pw.print("mDoublePressOnStemPrimaryBehavior=");
        pw.println(doublePressOnStemPrimaryBehaviorToString(this.mDoublePressOnStemPrimaryBehavior));
        pw.print(prefix);
        pw.print("mTriplePressOnStemPrimaryBehavior=");
        pw.println(triplePressOnStemPrimaryBehaviorToString(this.mTriplePressOnStemPrimaryBehavior));
        pw.print(prefix);
        pw.print("mLongPressOnStemPrimaryBehavior=");
        pw.println(longPressOnStemPrimaryBehaviorToString(this.mLongPressOnStemPrimaryBehavior));
        pw.print(prefix);
        pw.print("mAllowStartActivityForLongPressOnPowerDuringSetup=");
        pw.println(this.mAllowStartActivityForLongPressOnPowerDuringSetup);
        pw.print(prefix);
        pw.print("mHasSoftInput=");
        pw.println(this.mHasSoftInput);
        pw.print(prefix);
        pw.print("mDismissImeOnBackKeyPressed=");
        pw.print(this.mDismissImeOnBackKeyPressed);
        pw.print(" mIncallPowerBehavior=");
        pw.println(incallPowerBehaviorToString(this.mIncallPowerBehavior));
        pw.print(prefix);
        pw.print("mIncallBackBehavior=");
        pw.print(incallBackBehaviorToString(this.mIncallBackBehavior));
        pw.print(" mEndcallBehavior=");
        pw.println(endcallBehaviorToString(this.mEndcallBehavior));
        pw.print(prefix);
        pw.println("mDisplayHomeButtonHandlers=");
        for (int i = 0; i < this.mDisplayHomeButtonHandlers.size(); i++) {
            int key = this.mDisplayHomeButtonHandlers.keyAt(i);
            pw.print(prefix);
            pw.print("  ");
            pw.println(this.mDisplayHomeButtonHandlers.get(key));
        }
        pw.print(prefix);
        pw.print("mKeyguardOccluded=");
        pw.print(isKeyguardOccluded());
        pw.print(" mKeyguardOccludedChanged=");
        pw.print(this.mKeyguardOccludedChanged);
        pw.print(" mPendingKeyguardOccluded=");
        pw.println(this.mPendingKeyguardOccluded);
        pw.print(prefix);
        pw.print("mAllowLockscreenWhenOnDisplays=");
        pw.print(!this.mAllowLockscreenWhenOnDisplays.isEmpty());
        pw.print(" mLockScreenTimeout=");
        pw.print(this.mLockScreenTimeout);
        pw.print(" mLockScreenTimerActive=");
        pw.println(this.mLockScreenTimerActive);
        pw.print(prefix);
        pw.print("mKidsModeEnabled=");
        pw.println(this.mKidsModeEnabled);
        this.mHapticFeedbackVibrationProvider.dump(prefix, pw);
        this.mGlobalKeyManager.dump(prefix, pw);
        this.mKeyCombinationManager.dump(prefix, pw);
        this.mSingleKeyGestureDetector.dump(prefix, pw);
        this.mDeferredKeyActionExecutor.dump(prefix, pw);
        if (this.mWakeGestureListener != null) {
            this.mWakeGestureListener.dump(pw, prefix);
        }
        if (this.mBurnInProtectionHelper != null) {
            this.mBurnInProtectionHelper.dump(prefix, pw);
        }
        if (this.mKeyguardDelegate != null) {
            this.mKeyguardDelegate.dump(prefix, pw);
        }
        pw.print(prefix);
        pw.println("Looper state:");
        this.mHandler.getLooper().dump(new android.util.PrintWriterPrinter(pw), prefix + "  ");
    }

    private static java.lang.String endcallBehaviorToString(int behavior) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((behavior & 1) != 0) {
            sb.append("home|");
        }
        if ((behavior & 2) != 0) {
            sb.append("sleep|");
        }
        int N = sb.length();
        if (N == 0) {
            return "<nothing>";
        }
        return sb.substring(0, N - 1);
    }

    private static java.lang.String incallPowerBehaviorToString(int behavior) {
        if ((behavior & 2) != 0) {
            return "hangup";
        }
        return "sleep";
    }

    private static java.lang.String incallBackBehaviorToString(int behavior) {
        if ((behavior & 1) != 0) {
            return "hangup";
        }
        return "<nothing>";
    }

    private static java.lang.String longPressOnBackBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "LONG_PRESS_BACK_NOTHING";
            case 1:
                return "LONG_PRESS_BACK_GO_TO_VOICE_ASSIST";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String longPressOnHomeBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "LONG_PRESS_HOME_NOTHING";
            case 1:
                return "LONG_PRESS_HOME_ALL_APPS";
            case 2:
                return "LONG_PRESS_HOME_ASSIST";
            case 3:
                return "LONG_PRESS_HOME_NOTIFICATION_PANEL";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String doubleTapOnHomeBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "DOUBLE_TAP_HOME_NOTHING";
            case 1:
                return "DOUBLE_TAP_HOME_RECENT_SYSTEM_UI";
            case 2:
                return "DOUBLE_TAP_HOME_PIP_MENU";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String shortPressOnPowerBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "SHORT_PRESS_POWER_NOTHING";
            case 1:
                return "SHORT_PRESS_POWER_GO_TO_SLEEP";
            case 2:
                return "SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP";
            case 3:
                return "SHORT_PRESS_POWER_REALLY_GO_TO_SLEEP_AND_GO_HOME";
            case 4:
                return "SHORT_PRESS_POWER_GO_HOME";
            case 5:
                return "SHORT_PRESS_POWER_CLOSE_IME_OR_GO_HOME";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String longPressOnPowerBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "LONG_PRESS_POWER_NOTHING";
            case 1:
                return "LONG_PRESS_POWER_GLOBAL_ACTIONS";
            case 2:
                return "LONG_PRESS_POWER_SHUT_OFF";
            case 3:
                return "LONG_PRESS_POWER_SHUT_OFF_NO_CONFIRM";
            case 4:
                return "LONG_PRESS_POWER_GO_TO_VOICE_ASSIST";
            case 5:
                return "LONG_PRESS_POWER_ASSISTANT";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String settingsKeyBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "SETTINGS_KEY_BEHAVIOR_SETTINGS_ACTIVITY";
            case 1:
                return "SETTINGS_KEY_BEHAVIOR_NOTIFICATION_PANEL";
            case 2:
                return "SETTINGS_KEY_BEHAVIOR_NOTHING";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String veryLongPressOnPowerBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "VERY_LONG_PRESS_POWER_NOTHING";
            case 1:
                return "VERY_LONG_PRESS_POWER_GLOBAL_ACTIONS";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String powerVolumeUpBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "POWER_VOLUME_UP_BEHAVIOR_NOTHING";
            case 1:
                return "POWER_VOLUME_UP_BEHAVIOR_MUTE";
            case 2:
                return "POWER_VOLUME_UP_BEHAVIOR_GLOBAL_ACTIONS";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String multiPressOnPowerBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "MULTI_PRESS_POWER_NOTHING";
            case 1:
                return "MULTI_PRESS_POWER_THEATER_MODE";
            case 2:
                return "MULTI_PRESS_POWER_BRIGHTNESS_BOOST";
            case 3:
                return "MULTI_PRESS_POWER_LAUNCH_TARGET_ACTIVITY";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String shortPressOnSleepBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "SHORT_PRESS_SLEEP_GO_TO_SLEEP";
            case 1:
                return "SHORT_PRESS_SLEEP_GO_TO_SLEEP_AND_GO_HOME";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String shortPressOnWindowBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "SHORT_PRESS_WINDOW_NOTHING";
            case 1:
                return "SHORT_PRESS_WINDOW_PICTURE_IN_PICTURE";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String shortPressOnStemPrimaryBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "SHORT_PRESS_PRIMARY_NOTHING";
            case 1:
                return "SHORT_PRESS_PRIMARY_LAUNCH_ALL_APPS";
            case 2:
                return "SHORT_PRESS_PRIMARY_LAUNCH_TARGET_ACTIVITY";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String doublePressOnStemPrimaryBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "DOUBLE_PRESS_PRIMARY_NOTHING";
            case 1:
                return "DOUBLE_PRESS_PRIMARY_SWITCH_RECENT_APP";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String triplePressOnStemPrimaryBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "TRIPLE_PRESS_PRIMARY_NOTHING";
            case 1:
                return "TRIPLE_PRESS_PRIMARY_TOGGLE_ACCESSIBILITY";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String longPressOnStemPrimaryBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "LONG_PRESS_PRIMARY_NOTHING";
            case 1:
                return "LONG_PRESS_PRIMARY_LAUNCH_VOICE_ASSISTANT";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    private static java.lang.String lidBehaviorToString(int behavior) {
        switch (behavior) {
            case 0:
                return "LID_BEHAVIOR_NONE";
            case 1:
                return "LID_BEHAVIOR_SLEEP";
            case 2:
                return "LID_BEHAVIOR_LOCK";
            default:
                return java.lang.Integer.toString(behavior);
        }
    }

    public static boolean isLongPressToAssistantEnabled(android.content.Context context) {
        android.content.ContentResolver resolver = context.getContentResolver();
        int longPressToAssistant = android.provider.Settings.System.getIntForUser(resolver, "clockwork_long_press_to_assistant_enabled", 1, -2);
        if (android.util.Log.isLoggable(TAG, 3)) {
            android.util.Log.d(TAG, "longPressToAssistant = " + longPressToAssistant);
        }
        return longPressToAssistant == 1;
    }

    private class HdmiVideoExtconUEventObserver extends com.android.server.ExtconStateObserver<java.lang.Boolean> {
        private static final java.lang.String DP_EXIST = "DP=1";
        private static final java.lang.String HDMI_EXIST = "HDMI=1";
        private static final java.lang.String NAME = "hdmi";

        private HdmiVideoExtconUEventObserver() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean init(com.android.server.ExtconUEventObserver.ExtconInfo hdmi) {
            boolean plugged = false;
            try {
                plugged = parseStateFromFile(hdmi).booleanValue();
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.w(com.android.server.policy.PhoneWindowManager.TAG, hdmi.getStatePath() + " not found while attempting to determine initial state", e);
            } catch (java.io.IOException e2) {
                android.util.Slog.e(com.android.server.policy.PhoneWindowManager.TAG, "Error reading " + hdmi.getStatePath() + " while attempting to determine initial state", e2);
            }
            startObserving(hdmi);
            return plugged;
        }

        @Override // com.android.server.ExtconStateObserver
        public void updateState(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, java.lang.String eventName, java.lang.Boolean state) {
            com.android.server.policy.PhoneWindowManager.this.mDefaultDisplayPolicy.setHdmiPlugged(state.booleanValue());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.ExtconStateObserver
        public java.lang.Boolean parseState(com.android.server.ExtconUEventObserver.ExtconInfo extconIfno, java.lang.String state) {
            return java.lang.Boolean.valueOf(state.contains(HDMI_EXIST) || state.contains(DP_EXIST));
        }
    }

    private void launchTargetSearchActivity() {
        android.content.Intent intent;
        if (this.mSearchKeyTargetActivity != null) {
            intent = new android.content.Intent();
            intent.setComponent(this.mSearchKeyTargetActivity);
        } else {
            intent = new android.content.Intent("android.intent.action.WEB_SEARCH");
        }
        intent.addFlags(270532608);
        try {
            startActivityAsUser(intent, android.os.UserHandle.CURRENT_OR_SELF);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Slog.e(TAG, "Could not resolve activity with : " + intent.getComponent().flattenToString() + " name.");
        }
    }

    static class ButtonOverridePermissionChecker {
        ButtonOverridePermissionChecker() {
        }

        boolean canAppOverrideSystemKey(android.content.Context context, int uid) {
            return android.content.PermissionChecker.checkPermissionForDataDelivery(context, "android.permission.OVERRIDE_SYSTEM_KEY_BEHAVIOR_IN_FOCUSED_WINDOW", -1, uid, (java.lang.String) null, (java.lang.String) null, (java.lang.String) null) == 0;
        }
    }

    public com.android.server.policy.IPhoneWindowManagerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class PhoneWindowManagerWrapper implements com.android.server.policy.IPhoneWindowManagerWrapper {
        private PhoneWindowManagerWrapper() {
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public com.android.server.policy.IPhoneWindowManagerExt getExtImpl() {
            return com.android.server.policy.PhoneWindowManager.this.mPhoneWindowManagerExt;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setlocalLOGV(boolean on) {
            com.android.server.policy.PhoneWindowManager.localLOGV = on;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setDebugInput(boolean on) {
            com.android.server.policy.PhoneWindowManager.DEBUG_INPUT = on;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setDebugKeyguard(boolean on) {
            com.android.server.policy.PhoneWindowManager.DEBUG_KEYGUARD = on;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void setDebugWakeup(boolean on) {
            com.android.server.policy.PhoneWindowManager.DEBUG_WAKEUP = on;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public boolean performHapticFeedback(int effectId, boolean always, java.lang.String reason) {
            return com.android.server.policy.PhoneWindowManager.this.performHapticFeedback(effectId, always, reason);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public com.android.server.policy.KeyCombinationManager getKeyCombinationManager() {
            return com.android.server.policy.PhoneWindowManager.this.mKeyCombinationManager;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void interceptRingerToggleChord() {
            com.android.server.policy.PhoneWindowManager.this.interceptRingerToggleChord();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void cancelPendingRingerToggleChordAction() {
            com.android.server.policy.PhoneWindowManager.this.cancelPendingRingerToggleChordAction();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void cancelGlobalActionsAction() {
            com.android.server.policy.PhoneWindowManager.this.cancelGlobalActionsAction();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public com.android.server.policy.SingleKeyGestureDetector getSingleKeyGestureDetector() {
            return com.android.server.policy.PhoneWindowManager.this.mSingleKeyGestureDetector;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void launchAssistAction(java.lang.String hint, int deviceId, long eventTime, int invocationType, int launchModeEventNumber) {
            com.android.server.policy.PhoneWindowManager.this.launchAssistAction(hint, deviceId, eventTime, invocationType, launchModeEventNumber);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void finishPowerKeyPress() {
            com.android.server.policy.PhoneWindowManager.this.finishPowerKeyPress();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void cancelPreloadRecentApps() {
            com.android.server.policy.PhoneWindowManager.this.cancelPreloadRecentApps();
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public java.lang.Object getLock() {
            return com.android.server.policy.PhoneWindowManager.this.mLock;
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void powerPress(long eventTime, boolean beganFromNonInteractive, int count, int displayId) {
            com.android.server.policy.PhoneWindowManager.this.powerPress(eventTime, count, displayId);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public void wakeUpFromPowerKey(long eventTime) {
            com.android.server.policy.PhoneWindowManager.this.wakeUpFromWakeKey(eventTime, 26, true);
        }

        @Override // com.android.server.policy.IPhoneWindowManagerWrapper
        public boolean handleHomeShortcuts(android.os.IBinder focusedToken, android.view.KeyEvent event) {
            return com.android.server.policy.PhoneWindowManager.this.handleHomeShortcuts(focusedToken, event);
        }
    }

    private int getTargetDisplayIdForKeyEvent(android.view.KeyEvent event) {
        int displayId = event.getDisplayId();
        if (displayId == -1) {
            displayId = this.mTopFocusedDisplayId;
        }
        if (displayId == -1) {
            return 0;
        }
        return displayId;
    }
}
