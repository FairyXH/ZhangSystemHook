package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public class InputManagerService extends android.hardware.input.IInputManager.Stub implements com.android.server.Watchdog.Monitor {
    public static final int BTN_MOUSE = 272;
    private static final int DEFAULT_VIBRATION_MAGNITUDE = 192;
    private static final java.lang.String EXCLUDED_DEVICES_PATH = "etc/excluded-input-devices.xml";
    private static final int INJECTION_TIMEOUT_MILLIS = 30000;
    public static final int INPUT_OVERLAY_LAYER_GESTURE_MONITOR = 1;
    public static final int INPUT_OVERLAY_LAYER_HANDWRITING_SURFACE = 2;
    public static final int KEY_STATE_DOWN = 1;
    public static final int KEY_STATE_UNKNOWN = -1;
    public static final int KEY_STATE_UP = 0;
    public static final int KEY_STATE_VIRTUAL = 2;
    private static final int MSG_DELIVER_INPUT_DEVICES_CHANGED = 1;
    private static final int MSG_DELIVER_TABLET_MODE_CHANGED = 3;
    private static final int MSG_RELOAD_DEVICE_ALIASES = 2;
    private static final java.lang.String PORT_ASSOCIATIONS_PATH = "etc/input-port-associations.xml";
    public static final int SW_CAMERA_LENS_COVER = 9;
    public static final int SW_CAMERA_LENS_COVER_BIT = 512;
    public static final int SW_HEADPHONE_INSERT = 2;
    public static final int SW_HEADPHONE_INSERT_BIT = 4;
    public static final int SW_JACK_BITS = 212;
    public static final int SW_JACK_PHYSICAL_INSERT = 7;
    public static final int SW_JACK_PHYSICAL_INSERT_BIT = 128;
    public static final int SW_KEYPAD_SLIDE = 10;
    public static final int SW_KEYPAD_SLIDE_BIT = 1024;
    public static final int SW_LID = 0;
    public static final int SW_LID_BIT = 1;
    public static final int SW_LINEOUT_INSERT = 6;
    public static final int SW_LINEOUT_INSERT_BIT = 64;
    public static final int SW_MICROPHONE_INSERT = 4;
    public static final int SW_MICROPHONE_INSERT_BIT = 16;
    public static final int SW_MUTE_DEVICE = 14;
    public static final int SW_MUTE_DEVICE_BIT = 16384;
    public static final int SW_TABLET_MODE = 1;
    public static final int SW_TABLET_MODE_BIT = 2;
    private static final java.lang.String VELOCITYTRACKER_STRATEGY_PROPERTY = "velocitytracker_strategy";
    private final android.util.SparseArray<com.android.server.input.InputManagerService.AdditionalDisplayInputProperties> mAdditionalDisplayInputProperties;
    private final java.lang.Object mAdditionalDisplayInputPropertiesLock;
    private final java.lang.Object mAssociationsLock;
    private final com.android.server.input.BatteryController mBatteryController;
    private final android.content.Context mContext;
    private final com.android.server.input.PersistentDataStore mDataStore;
    private final java.util.Map<java.lang.String, java.lang.String> mDeviceTypeAssociations;
    private android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private final java.io.File mDoubleTouchGestureEnableFile;
    private com.android.server.input.IInputManagerServiceExt mExt;
    private com.android.server.input.debug.FocusEventDebugView mFocusEventDebugView;
    private final java.lang.Object mFocusEventDebugViewLock;
    private final com.android.server.input.InputManagerService.InputManagerHandler mHandler;
    private android.view.InputDevice[] mInputDevices;
    private final android.util.SparseArray<com.android.server.input.InputManagerService.InputDevicesChangedListenerRecord> mInputDevicesChangedListeners;
    private boolean mInputDevicesChangedPending;
    private final java.lang.Object mInputDevicesLock;
    android.view.IInputFilter mInputFilter;
    com.android.server.input.InputManagerService.InputFilterHost mInputFilterHost;
    final java.lang.Object mInputFilterLock;
    private com.android.server.inputmethod.InputMethodManagerInternal mInputMethodManagerInternal;
    final java.util.Map<android.os.IBinder, com.android.server.input.GestureMonitorSpyWindow> mInputMonitors;
    private final android.util.SparseBooleanArray mIsVibrating;
    private final com.android.server.input.KeyRemapper mKeyRemapper;
    private final com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface mKeyboardBacklightController;
    private final java.util.Map<java.lang.String, java.lang.String> mKeyboardLayoutAssociations;
    private final com.android.server.input.KeyboardLayoutManager mKeyboardLayoutManager;
    private final com.android.server.input.KeyboardLedController mKeyboardLedController;
    private final java.util.List<com.android.server.input.InputManagerInternal.LidSwitchCallback> mLidSwitchCallbacks;
    private final java.lang.Object mLidSwitchLock;
    private final java.lang.Object mLightLock;
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.input.InputManagerService.LightSession> mLightSessions;
    private final com.android.server.input.NativeInputManagerService mNative;
    private int mNextVibratorTokenValue;
    private final com.android.server.input.PointerIconCache mPointerIconCache;
    private final java.util.Map<java.lang.String, java.lang.Integer> mRuntimeAssociations;
    private final java.util.List<com.android.server.input.InputManagerService.SensorEventListenerRecord> mSensorAccuracyListenersToNotify;
    private final android.util.SparseArray<com.android.server.input.InputManagerService.SensorEventListenerRecord> mSensorEventListeners;
    private final java.util.List<com.android.server.input.InputManagerService.SensorEventListenerRecord> mSensorEventListenersToNotify;
    private final java.lang.Object mSensorEventLock;
    private final com.android.server.input.InputSettingsObserver mSettingsObserver;
    private boolean mShowKeyPresses;
    private boolean mShowRotaryInput;
    private final java.util.Map<java.lang.String, java.lang.Integer> mStaticAssociations;
    private final com.android.server.input.StickyModifierStateController mStickyModifierStateController;
    private boolean mSystemReady;
    private final android.util.SparseArray<com.android.server.input.InputManagerService.TabletModeChangedListenerRecord> mTabletModeChangedListeners;
    private final java.lang.Object mTabletModeLock;
    private final java.util.ArrayList<com.android.server.input.InputManagerService.InputDevicesChangedListenerRecord> mTempInputDevicesChangedListenersToNotify;
    private final java.util.List<com.android.server.input.InputManagerService.TabletModeChangedListenerRecord> mTempTabletModeChangedListenersToNotify;
    private final java.util.Map<java.lang.String, java.lang.String> mUniqueIdAssociationsByDescriptor;
    private final java.util.Map<java.lang.String, java.lang.String> mUniqueIdAssociationsByPort;
    final boolean mUseDevInputEventForAudioJack;
    private final java.lang.String mVelocityTrackerStrategy;
    private final java.lang.Object mVibratorLock;
    private final android.util.SparseArray<android.os.RemoteCallbackList<android.os.IVibratorStateListener>> mVibratorStateListeners;
    private final java.util.Map<android.os.IBinder, com.android.server.input.InputManagerService.VibratorToken> mVibratorTokens;
    private com.android.server.input.InputManagerService.WindowManagerCallbacks mWindowManagerCallbacks;
    private com.android.server.input.InputManagerService.WiredAccessoryCallbacks mWiredAccessoryCallbacks;
    private com.android.server.input.IInputManagerServiceWrapper mWrapper;
    static final java.lang.String TAG = "InputManager";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final com.android.server.input.InputManagerService.AdditionalDisplayInputProperties DEFAULT_ADDITIONAL_DISPLAY_INPUT_PROPERTIES = new com.android.server.input.InputManagerService.AdditionalDisplayInputProperties();
    private static final boolean IS_AGING_VERSION = "1".equals(android.os.SystemProperties.get("persist.sys.agingtest", ""));

    public interface WindowManagerCallbacks extends com.android.server.input.InputManagerInternal.LidSwitchCallback {
        android.view.SurfaceControl createSurfaceForGestureMonitor(java.lang.String str, int i);

        android.view.KeyEvent dispatchUnhandledKey(android.os.IBinder iBinder, android.view.KeyEvent keyEvent, int i);

        android.view.SurfaceControl getParentSurfaceForPointers(int i);

        int getPointerDisplayId();

        int getPointerLayer();

        long interceptKeyBeforeDispatching(android.os.IBinder iBinder, android.view.KeyEvent keyEvent, int i);

        int interceptKeyBeforeQueueing(android.view.KeyEvent keyEvent, int i);

        int interceptMotionBeforeQueueingNonInteractive(int i, int i2, int i3, long j, int i4);

        boolean isAnimating();

        void notifyCameraLensCoverSwitchChanged(long j, boolean z);

        void notifyConfigurationChanged();

        void notifyDropWindow(android.os.IBinder iBinder, float f, float f2);

        void notifyFocusChanged(android.os.IBinder iBinder, android.os.IBinder iBinder2);

        void notifyInputChannelBroken(android.os.IBinder iBinder);

        void notifyNoFocusedWindowAnr(android.view.InputApplicationHandle inputApplicationHandle);

        void notifyPointerLocationChanged(boolean z);

        void notifyWindowResponsive(android.os.IBinder iBinder, java.util.OptionalInt optionalInt);

        void notifyWindowUnresponsive(android.os.IBinder iBinder, java.util.OptionalInt optionalInt, java.lang.String str);

        void onPointerDownOutsideFocus(android.os.IBinder iBinder);
    }

    public interface WiredAccessoryCallbacks {
        void notifyWiredAccessoryChanged(long j, int i, int i2);

        void systemReady();
    }

    static class Injector {
        private final android.content.Context mContext;
        private final android.os.Looper mLooper;
        private final com.android.server.input.UEventManager mUEventManager;

        Injector(android.content.Context context, android.os.Looper looper, com.android.server.input.UEventManager uEventManager) {
            this.mContext = context;
            this.mLooper = looper;
            this.mUEventManager = uEventManager;
        }

        android.content.Context getContext() {
            return this.mContext;
        }

        android.os.Looper getLooper() {
            return this.mLooper;
        }

        com.android.server.input.UEventManager getUEventManager() {
            return this.mUEventManager;
        }

        com.android.server.input.NativeInputManagerService getNativeService(com.android.server.input.InputManagerService service) {
            return new com.android.server.input.NativeInputManagerService.NativeImpl(service, this.mLooper.getQueue());
        }

        void registerLocalService(com.android.server.input.InputManagerInternal localService) {
            com.android.server.LocalServices.addService(com.android.server.input.InputManagerInternal.class, localService);
        }
    }

    public InputManagerService(android.content.Context context) {
        this(new com.android.server.input.InputManagerService.Injector(context, com.android.server.DisplayThread.get().getLooper(), new com.android.server.input.UEventManager() { // from class: com.android.server.input.InputManagerService.1
        }));
    }

    /* JADX WARN: Multi-variable type inference failed */
    InputManagerService(com.android.server.input.InputManagerService.Injector injector) {
        com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface keyboardBacklightController;
        this.mTabletModeLock = new java.lang.Object();
        this.mTabletModeChangedListeners = new android.util.SparseArray<>();
        this.mTempTabletModeChangedListenersToNotify = new java.util.ArrayList();
        this.mSensorEventLock = new java.lang.Object();
        this.mSensorEventListeners = new android.util.SparseArray<>();
        this.mSensorEventListenersToNotify = new java.util.ArrayList();
        this.mSensorAccuracyListenersToNotify = new java.util.ArrayList();
        this.mDataStore = new com.android.server.input.PersistentDataStore();
        this.mInputDevicesLock = new java.lang.Object();
        this.mInputDevices = new android.view.InputDevice[0];
        this.mInputDevicesChangedListeners = new android.util.SparseArray<>();
        this.mTempInputDevicesChangedListenersToNotify = new java.util.ArrayList<>();
        this.mVibratorLock = new java.lang.Object();
        this.mVibratorTokens = new android.util.ArrayMap();
        this.mVibratorStateListeners = new android.util.SparseArray<>();
        this.mIsVibrating = new android.util.SparseBooleanArray();
        this.mLightLock = new java.lang.Object();
        this.mLightSessions = new android.util.ArrayMap<>();
        this.mLidSwitchLock = new java.lang.Object();
        this.mLidSwitchCallbacks = new java.util.ArrayList();
        this.mInputFilterLock = new java.lang.Object();
        this.mAssociationsLock = new java.lang.Object();
        this.mRuntimeAssociations = new android.util.ArrayMap();
        this.mUniqueIdAssociationsByPort = new android.util.ArrayMap();
        this.mUniqueIdAssociationsByDescriptor = new android.util.ArrayMap();
        this.mKeyboardLayoutAssociations = new android.util.ArrayMap();
        this.mDeviceTypeAssociations = new android.util.ArrayMap();
        this.mAdditionalDisplayInputPropertiesLock = new java.lang.Object();
        this.mAdditionalDisplayInputProperties = new android.util.SparseArray<>();
        this.mInputMonitors = new java.util.HashMap();
        this.mFocusEventDebugViewLock = new java.lang.Object();
        this.mShowKeyPresses = false;
        this.mShowRotaryInput = false;
        java.lang.Object[] objArr = 0;
        this.mWrapper = new com.android.server.input.InputManagerService.InputManagerServiceWrapper();
        this.mExt = (com.android.server.input.IInputManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.input.IInputManagerServiceExt.class).base(this).create();
        this.mStaticAssociations = loadStaticInputPortAssociations();
        this.mContext = injector.getContext();
        this.mHandler = new com.android.server.input.InputManagerService.InputManagerHandler(injector.getLooper());
        this.mNative = injector.getNativeService(this);
        this.mSettingsObserver = new com.android.server.input.InputSettingsObserver(this.mContext, this.mHandler, this, this.mNative);
        this.mKeyboardLayoutManager = new com.android.server.input.KeyboardLayoutManager(this.mContext, this.mNative, this.mDataStore, injector.getLooper());
        this.mBatteryController = new com.android.server.input.BatteryController(this.mContext, this.mNative, injector.getLooper(), injector.getUEventManager());
        if (com.android.server.input.InputFeatureFlagProvider.isKeyboardBacklightControlEnabled()) {
            keyboardBacklightController = new com.android.server.input.KeyboardBacklightController(this.mContext, this.mNative, this.mDataStore, injector.getLooper(), injector.getUEventManager());
        } else {
            keyboardBacklightController = new com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface() { // from class: com.android.server.input.InputManagerService.2
            };
        }
        this.mKeyboardBacklightController = keyboardBacklightController;
        this.mStickyModifierStateController = new com.android.server.input.StickyModifierStateController();
        this.mKeyboardLedController = new com.android.server.input.KeyboardLedController(this.mContext, injector.getLooper(), this.mNative);
        this.mKeyRemapper = new com.android.server.input.KeyRemapper(this.mContext, this.mNative, this.mDataStore, injector.getLooper());
        this.mPointerIconCache = new com.android.server.input.PointerIconCache(this.mContext, this.mNative);
        this.mUseDevInputEventForAudioJack = this.mContext.getResources().getBoolean(android.R.bool.config_supportsMicToggle);
        android.util.Slog.i(TAG, "Initializing input manager, mUseDevInputEventForAudioJack=" + this.mUseDevInputEventForAudioJack);
        java.lang.String string = this.mContext.getResources().getString(android.R.string.config_extensionFallbackServiceName);
        this.mDoubleTouchGestureEnableFile = android.text.TextUtils.isEmpty(string) ? null : new java.io.File(string);
        this.mVelocityTrackerStrategy = android.provider.DeviceConfig.getProperty("input_native_boot", VELOCITYTRACKER_STRATEGY_PROPERTY);
        injector.registerLocalService(new com.android.server.input.InputManagerService.LocalService());
        this.mExt.init(this.mContext, this.mHandler, ((com.android.server.input.NativeInputManagerService.NativeImpl) this.mNative).mPtr);
    }

    public void setWindowManagerCallbacks(com.android.server.input.InputManagerService.WindowManagerCallbacks callbacks) {
        if (this.mWindowManagerCallbacks != null) {
            unregisterLidSwitchCallbackInternal(this.mWindowManagerCallbacks);
        }
        this.mWindowManagerCallbacks = callbacks;
        registerLidSwitchCallbackInternal(this.mWindowManagerCallbacks);
    }

    public void setWiredAccessoryCallbacks(com.android.server.input.InputManagerService.WiredAccessoryCallbacks callbacks) {
        this.mWiredAccessoryCallbacks = callbacks;
    }

    void registerLidSwitchCallbackInternal(com.android.server.input.InputManagerInternal.LidSwitchCallback callback) {
        synchronized (this.mLidSwitchLock) {
            this.mLidSwitchCallbacks.add(callback);
            if (this.mSystemReady) {
                boolean lidOpen = getSwitchState(-1, -256, 0) == 0;
                callback.notifyLidSwitchChanged(0L, lidOpen);
            }
        }
    }

    void unregisterLidSwitchCallbackInternal(com.android.server.input.InputManagerInternal.LidSwitchCallback callback) {
        synchronized (this.mLidSwitchLock) {
            this.mLidSwitchCallbacks.remove(callback);
        }
    }

    public void start() {
        android.util.Slog.i(TAG, "Starting input manager");
        this.mNative.start();
        com.android.server.Watchdog.getInstance().addMonitor(this);
        this.mExt.start();
    }

    public void systemRunning() {
        boolean z;
        if (DEBUG) {
            android.util.Slog.d(TAG, "System ready.");
        }
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        this.mInputMethodManagerInternal = (com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class);
        this.mSettingsObserver.registerAndUpdate();
        synchronized (this.mLidSwitchLock) {
            this.mSystemReady = true;
            int switchState = getSwitchState(-1, -256, 0);
            for (int i = 0; i < this.mLidSwitchCallbacks.size(); i++) {
                com.android.server.input.InputManagerInternal.LidSwitchCallback callback = this.mLidSwitchCallbacks.get(i);
                if (switchState == 0) {
                    z = true;
                } else {
                    z = false;
                }
                callback.notifyLidSwitchChanged(0L, z);
            }
        }
        int micMuteState = getSwitchState(-1, -256, 14);
        if (micMuteState == 1) {
            setSensorPrivacy(1, true);
        }
        int cameraMuteState = getSwitchState(-1, -256, 9);
        if (cameraMuteState == 1) {
            setSensorPrivacy(2, true);
        }
        android.content.IntentFilter filter = new android.content.IntentFilter("android.bluetooth.device.action.ALIAS_CHANGED");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.input.InputManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                com.android.server.input.InputManagerService.this.reloadDeviceAliases();
            }
        }, filter, null, this.mHandler);
        this.mHandler.sendEmptyMessage(2);
        if (this.mWiredAccessoryCallbacks != null) {
            this.mWiredAccessoryCallbacks.systemReady();
        }
        this.mExt.onSystemRunning();
        this.mKeyboardLayoutManager.systemRunning();
        this.mBatteryController.systemRunning();
        this.mKeyboardBacklightController.systemRunning();
        this.mKeyboardLedController.systemRunning();
        this.mKeyRemapper.systemRunning();
        this.mPointerIconCache.systemRunning();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadDeviceAliases() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Reloading device names.");
        }
        this.mNative.reloadDeviceAliases();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayViewportsInternal(java.util.List<android.hardware.display.DisplayViewport> viewports) {
        this.mExt.setDisplayViewportsInternal(viewports);
        android.hardware.display.DisplayViewport[] vArray = new android.hardware.display.DisplayViewport[viewports.size()];
        for (int i = viewports.size() - 1; i >= 0; i--) {
            vArray[i] = viewports.get(i);
        }
        this.mNative.setDisplayViewports(vArray);
        this.mNative.setPointerDisplayId(this.mWindowManagerCallbacks.getPointerDisplayId());
    }

    public int getKeyCodeState(int deviceId, int sourceMask, int keyCode) {
        return this.mNative.getKeyCodeState(deviceId, sourceMask, keyCode);
    }

    public int getScanCodeState(int deviceId, int sourceMask, int scanCode) {
        return this.mNative.getScanCodeState(deviceId, sourceMask, scanCode);
    }

    public int getSwitchState(int deviceId, int sourceMask, int switchCode) {
        return this.mNative.getSwitchState(deviceId, sourceMask, switchCode);
    }

    public boolean hasKeys(int deviceId, int sourceMask, int[] keyCodes, boolean[] keyExists) {
        java.util.Objects.requireNonNull(keyCodes, "keyCodes must not be null");
        java.util.Objects.requireNonNull(keyExists, "keyExists must not be null");
        if (keyExists.length < keyCodes.length) {
            throw new java.lang.IllegalArgumentException("keyExists must be at least as large as keyCodes");
        }
        return this.mNative.hasKeys(deviceId, sourceMask, keyCodes, keyExists);
    }

    public int getKeyCodeForKeyLocation(int deviceId, int locationKeyCode) {
        if (locationKeyCode <= 0 || locationKeyCode > android.view.KeyEvent.getMaxKeyCode()) {
            return 0;
        }
        return this.mNative.getKeyCodeForKeyLocation(deviceId, locationKeyCode);
    }

    public android.view.KeyCharacterMap getKeyCharacterMap(java.lang.String layoutDescriptor) {
        java.util.Objects.requireNonNull(layoutDescriptor, "layoutDescriptor must not be null");
        return this.mKeyboardLayoutManager.getKeyCharacterMap(layoutDescriptor);
    }

    @java.lang.Deprecated
    public boolean transferTouch(android.os.IBinder destChannelToken, int displayId) {
        java.util.Objects.requireNonNull(destChannelToken, "destChannelToken must not be null");
        return this.mNative.transferTouch(destChannelToken, displayId);
    }

    public android.view.InputChannel monitorInput(java.lang.String inputChannelName, int displayId) {
        java.util.Objects.requireNonNull(inputChannelName, "inputChannelName not be null");
        if (displayId < 0) {
            throw new java.lang.IllegalArgumentException("displayId must >= 0.");
        }
        return this.mNative.createInputMonitor(displayId, inputChannelName, android.os.Binder.getCallingPid());
    }

    private android.view.InputChannel createSpyWindowGestureMonitor(android.os.IBinder monitorToken, java.lang.String name, android.view.SurfaceControl sc, int displayId, int pid, int uid) {
        final android.view.InputChannel channel = createInputChannel(name);
        try {
            try {
                monitorToken.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda8
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        this.f$0.lambda$createSpyWindowGestureMonitor$0(channel);
                    }
                }, 0);
                synchronized (this.mInputMonitors) {
                    this.mInputMonitors.put(channel.getToken(), new com.android.server.input.GestureMonitorSpyWindow(monitorToken, name, displayId, pid, uid, sc, channel));
                }
                android.view.InputChannel outInputChannel = new android.view.InputChannel();
                channel.copyTo(outInputChannel);
                return outInputChannel;
            } catch (android.os.RemoteException e) {
                android.util.Slog.i(TAG, "Client died before '" + name + "' could be created.");
                return null;
            }
        } catch (android.os.RemoteException e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSpyWindowGestureMonitor$0(android.view.InputChannel channel) {
        removeSpyWindowGestureMonitor(channel.getToken());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSpyWindowGestureMonitor(android.os.IBinder inputChannelToken) {
        com.android.server.input.GestureMonitorSpyWindow monitor;
        synchronized (this.mInputMonitors) {
            monitor = this.mInputMonitors.remove(inputChannelToken);
        }
        removeInputChannel(inputChannelToken);
        if (monitor == null) {
            return;
        }
        monitor.remove();
    }

    public android.view.InputMonitor monitorGestureInput(android.os.IBinder monitorToken, java.lang.String requestedName, int displayId) {
        if (!checkCallingPermission("android.permission.MONITOR_INPUT", "monitorGestureInput()")) {
            throw new java.lang.SecurityException("Requires MONITOR_INPUT permission");
        }
        java.util.Objects.requireNonNull(requestedName, "name must not be null.");
        java.util.Objects.requireNonNull(monitorToken, "token must not be null.");
        if (displayId < 0) {
            throw new java.lang.IllegalArgumentException("displayId must >= 0.");
        }
        java.lang.String name = "[Gesture Monitor] " + requestedName;
        int pid = android.os.Binder.getCallingPid();
        int uid = android.os.Binder.getCallingUid();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.view.SurfaceControl sc = this.mWindowManagerCallbacks.createSurfaceForGestureMonitor(name, displayId);
            if (sc == null) {
                throw new java.lang.IllegalArgumentException("Could not create gesture monitor surface on display: " + displayId);
            }
            android.view.InputChannel inputChannel = createSpyWindowGestureMonitor(monitorToken, name, sc, displayId, pid, uid);
            return new android.view.InputMonitor(inputChannel, new com.android.server.input.InputManagerService.InputMonitorHost(inputChannel.getToken()), new android.view.SurfaceControl(sc, "IMS.monitorGestureInput"));
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public android.view.InputChannel createInputChannel(java.lang.String name) {
        return this.mNative.createInputChannel(name);
    }

    public void removeInputChannel(android.os.IBinder connectionToken) {
        java.util.Objects.requireNonNull(connectionToken, "connectionToken must not be null");
        this.mNative.removeInputChannel(connectionToken);
    }

    public void setInputFilter(android.view.IInputFilter filter) {
        synchronized (this.mInputFilterLock) {
            android.view.IInputFilter oldFilter = this.mInputFilter;
            if (oldFilter == filter) {
                return;
            }
            if (oldFilter != null) {
                this.mInputFilter = null;
                this.mInputFilterHost.disconnectLocked();
                this.mInputFilterHost = null;
                try {
                    oldFilter.uninstall();
                } catch (android.os.RemoteException e) {
                }
            }
            if (filter != null) {
                this.mInputFilter = filter;
                this.mInputFilterHost = new com.android.server.input.InputManagerService.InputFilterHost();
                try {
                    filter.install(this.mInputFilterHost);
                } catch (android.os.RemoteException e2) {
                }
            }
            boolean z = true;
            this.mNative.setInputFilterEnabled(filter != null);
            com.android.server.input.IInputManagerServiceExt iInputManagerServiceExt = this.mExt;
            if (filter == null) {
                z = false;
            }
            iInputManagerServiceExt.setAccessibilityStatus(z);
        }
    }

    public boolean setInTouchMode(boolean inTouchMode, int pid, int uid, boolean hasPermission, int displayId) {
        return this.mNative.setInTouchMode(inTouchMode, pid, uid, hasPermission, displayId);
    }

    public boolean injectInputEvent(android.view.InputEvent event, int mode) {
        return injectInputEventToTarget(event, mode, -1);
    }

    public boolean injectInputEventToTarget(android.view.InputEvent event, int mode, int targetUid) {
        if (!checkCallingPermission("android.permission.INJECT_EVENTS", "injectInputEvent()", true)) {
            throw new java.lang.SecurityException("Injecting input events requires the caller (or the source of the instrumentation, if any) to have the INJECT_EVENTS permission.");
        }
        java.util.Objects.requireNonNull(event, "event must not be null");
        if (mode != 0 && mode != 2 && mode != 1) {
            throw new java.lang.IllegalArgumentException("mode is invalid");
        }
        int pid = android.os.Binder.getCallingPid();
        this.mExt.debugInputKeyInject(pid, event, "injectInputEvent");
        long ident = android.os.Binder.clearCallingIdentity();
        boolean injectIntoUid = targetUid != -1;
        try {
            int policyFlags = ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageCarMode(event.getDisplayId()) ? 134217728 | 1073741824 : 134217728;
            int result = this.mNative.injectInputEvent(event, injectIntoUid, targetUid, mode, 30000, policyFlags);
            android.os.Binder.restoreCallingIdentity(ident);
            switch (result) {
                case 0:
                    return true;
                case 1:
                    if (!injectIntoUid) {
                        throw new java.lang.IllegalStateException("Injection should not result in TARGET_MISMATCH when it is not targeted into to a specific uid.");
                    }
                    throw new java.lang.IllegalArgumentException("Targeted input event injection from pid " + pid + " was not directed at a window owned by uid " + targetUid + ".");
                case 2:
                default:
                    android.util.Slog.w(TAG, "Input event injection from pid " + pid + " failed.");
                    return false;
                case 3:
                    android.util.Slog.w(TAG, "Input event injection from pid " + pid + " timed out.");
                    return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    public android.view.VerifiedInputEvent verifyInputEvent(android.view.InputEvent event) {
        java.util.Objects.requireNonNull(event, "event must not be null");
        return this.mNative.verifyInputEvent(event);
    }

    public java.lang.String getVelocityTrackerStrategy() {
        return this.mVelocityTrackerStrategy;
    }

    public android.view.InputDevice getInputDevice(int deviceId) {
        synchronized (this.mInputDevicesLock) {
            for (android.view.InputDevice inputDevice : this.mInputDevices) {
                if (inputDevice.getId() == deviceId) {
                    return inputDevice;
                }
            }
            return null;
        }
    }

    public void enableInputDevice(int deviceId) {
        if (!checkCallingPermission("android.permission.DISABLE_INPUT_DEVICE", "enableInputDevice()")) {
            throw new java.lang.SecurityException("Requires DISABLE_INPUT_DEVICE permission");
        }
        this.mNative.enableInputDevice(deviceId);
    }

    public void disableInputDevice(int deviceId) {
        if (!checkCallingPermission("android.permission.DISABLE_INPUT_DEVICE", "disableInputDevice()")) {
            throw new java.lang.SecurityException("Requires DISABLE_INPUT_DEVICE permission");
        }
        this.mNative.disableInputDevice(deviceId);
    }

    public int[] getInputDeviceIds() {
        int[] ids;
        synchronized (this.mInputDevicesLock) {
            int count = this.mInputDevices.length;
            ids = new int[count];
            for (int i = 0; i < count; i++) {
                ids[i] = this.mInputDevices[i].getId();
            }
        }
        return ids;
    }

    public android.view.InputDevice[] getInputDevices() {
        android.view.InputDevice[] inputDeviceArr;
        synchronized (this.mInputDevicesLock) {
            inputDeviceArr = this.mInputDevices;
        }
        return inputDeviceArr;
    }

    public void registerInputDevicesChangedListener(android.hardware.input.IInputDevicesChangedListener listener) {
        java.util.Objects.requireNonNull(listener, "listener must not be null");
        synchronized (this.mInputDevicesLock) {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            if (this.mInputDevicesChangedListeners.get(callingPid) != null) {
                android.util.Slog.w(TAG, "The calling process may have already registered an InputDevicesChangedListener.");
                com.android.server.input.InputManagerService.InputDevicesChangedListenerRecord record = this.mInputDevicesChangedListeners.get(callingPid);
                if (IS_AGING_VERSION) {
                    if (record.mListener.asBinder() == listener.asBinder()) {
                        throw new java.lang.SecurityException("The calling process has already registered an InputDevicesChangedListener.");
                    }
                } else {
                    throw new java.lang.SecurityException("The calling process has already registered an InputDevicesChangedListener.");
                }
            }
            com.android.server.input.InputManagerService.InputDevicesChangedListenerRecord record2 = new com.android.server.input.InputManagerService.InputDevicesChangedListenerRecord(callingPid, listener, callingUid);
            try {
                android.os.IBinder binder = listener.asBinder();
                binder.linkToDeath(record2, 0);
                this.mExt.addProxyBinder(binder, callingUid, callingPid);
                this.mInputDevicesChangedListeners.put(callingPid, record2);
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInputDevicesChangedListenerDied(int pid) {
        synchronized (this.mInputDevicesLock) {
            this.mInputDevicesChangedListeners.remove(pid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverInputDevicesChanged(android.view.InputDevice[] oldInputDevices) {
        this.mTempInputDevicesChangedListenersToNotify.clear();
        synchronized (this.mInputDevicesLock) {
            if (this.mInputDevicesChangedPending) {
                this.mInputDevicesChangedPending = false;
                int numListeners = this.mInputDevicesChangedListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    this.mTempInputDevicesChangedListenersToNotify.add(this.mInputDevicesChangedListeners.valueAt(i));
                }
                int numDevices = this.mInputDevices.length;
                int[] deviceIdAndGeneration = new int[numDevices * 2];
                for (int i2 = 0; i2 < numDevices; i2++) {
                    android.view.InputDevice inputDevice = this.mInputDevices[i2];
                    deviceIdAndGeneration[i2 * 2] = inputDevice.getId();
                    deviceIdAndGeneration[(i2 * 2) + 1] = inputDevice.getGeneration();
                    if (DEBUG) {
                        android.util.Log.d(TAG, "device " + inputDevice.getId() + " generation " + inputDevice.getGeneration());
                    }
                }
                for (int i3 = 0; i3 < numListeners; i3++) {
                    this.mTempInputDevicesChangedListenersToNotify.get(i3).notifyInputDevicesChanged(deviceIdAndGeneration);
                }
                this.mTempInputDevicesChangedListenersToNotify.clear();
            }
        }
    }

    public android.hardware.input.TouchCalibration getTouchCalibrationForInputDevice(java.lang.String inputDeviceDescriptor, int surfaceRotation) {
        android.hardware.input.TouchCalibration touchCalibration;
        java.util.Objects.requireNonNull(inputDeviceDescriptor, "inputDeviceDescriptor must not be null");
        synchronized (this.mDataStore) {
            touchCalibration = this.mDataStore.getTouchCalibration(inputDeviceDescriptor, surfaceRotation);
        }
        return touchCalibration;
    }

    public void setTouchCalibrationForInputDevice(java.lang.String inputDeviceDescriptor, int surfaceRotation, android.hardware.input.TouchCalibration calibration) {
        if (!checkCallingPermission("android.permission.SET_INPUT_CALIBRATION", "setTouchCalibrationForInputDevice()")) {
            throw new java.lang.SecurityException("Requires SET_INPUT_CALIBRATION permission");
        }
        java.util.Objects.requireNonNull(inputDeviceDescriptor, "inputDeviceDescriptor must not be null");
        java.util.Objects.requireNonNull(calibration, "calibration must not be null");
        if (surfaceRotation < 0 || surfaceRotation > 3) {
            throw new java.lang.IllegalArgumentException("surfaceRotation value out of bounds");
        }
        synchronized (this.mDataStore) {
            try {
                if (this.mDataStore.setTouchCalibration(inputDeviceDescriptor, surfaceRotation, calibration)) {
                    this.mNative.reloadCalibration();
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    public int isInTabletMode() {
        if (!checkCallingPermission("android.permission.TABLET_MODE", "isInTabletMode()")) {
            throw new java.lang.SecurityException("Requires TABLET_MODE permission");
        }
        return getSwitchState(-1, -256, 1);
    }

    public int isMicMuted() {
        return getSwitchState(-1, -256, 14);
    }

    public void registerTabletModeChangedListener(android.hardware.input.ITabletModeChangedListener listener) {
        if (!checkCallingPermission("android.permission.TABLET_MODE", "registerTabletModeChangedListener()")) {
            throw new java.lang.SecurityException("Requires TABLET_MODE_LISTENER permission");
        }
        java.util.Objects.requireNonNull(listener, "event must not be null");
        synchronized (this.mTabletModeLock) {
            int callingPid = android.os.Binder.getCallingPid();
            if (this.mTabletModeChangedListeners.get(callingPid) != null) {
                throw new java.lang.IllegalStateException("The calling process has already registered a TabletModeChangedListener.");
            }
            com.android.server.input.InputManagerService.TabletModeChangedListenerRecord record = new com.android.server.input.InputManagerService.TabletModeChangedListenerRecord(callingPid, listener);
            try {
                android.os.IBinder binder = listener.asBinder();
                binder.linkToDeath(record, 0);
                this.mTabletModeChangedListeners.put(callingPid, record);
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTabletModeChangedListenerDied(int pid) {
        synchronized (this.mTabletModeLock) {
            this.mTabletModeChangedListeners.remove(pid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverTabletModeChanged(long whenNanos, boolean inTabletMode) {
        int numListeners;
        this.mTempTabletModeChangedListenersToNotify.clear();
        synchronized (this.mTabletModeLock) {
            numListeners = this.mTabletModeChangedListeners.size();
            for (int i = 0; i < numListeners; i++) {
                this.mTempTabletModeChangedListenersToNotify.add(this.mTabletModeChangedListeners.valueAt(i));
            }
        }
        for (int i2 = 0; i2 < numListeners; i2++) {
            this.mTempTabletModeChangedListenersToNotify.get(i2).notifyTabletModeChanged(whenNanos, inTabletMode);
        }
    }

    public android.hardware.input.KeyboardLayout[] getKeyboardLayouts() {
        return this.mKeyboardLayoutManager.getKeyboardLayouts();
    }

    public android.hardware.input.KeyboardLayout getKeyboardLayout(java.lang.String keyboardLayoutDescriptor) {
        return this.mKeyboardLayoutManager.getKeyboardLayout(keyboardLayoutDescriptor);
    }

    public android.hardware.input.KeyboardLayoutSelectionResult getKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, int userId, android.view.inputmethod.InputMethodInfo imeInfo, android.view.inputmethod.InputMethodSubtype imeSubtype) {
        return this.mKeyboardLayoutManager.getKeyboardLayoutForInputDevice(identifier, userId, imeInfo, imeSubtype);
    }

    public void setKeyboardLayoutForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, int userId, android.view.inputmethod.InputMethodInfo imeInfo, android.view.inputmethod.InputMethodSubtype imeSubtype, java.lang.String keyboardLayoutDescriptor) {
        super.setKeyboardLayoutForInputDevice_enforcePermission();
        this.mKeyboardLayoutManager.setKeyboardLayoutForInputDevice(identifier, userId, imeInfo, imeSubtype, keyboardLayoutDescriptor);
    }

    public android.hardware.input.KeyboardLayout[] getKeyboardLayoutListForInputDevice(android.hardware.input.InputDeviceIdentifier identifier, int userId, android.view.inputmethod.InputMethodInfo imeInfo, android.view.inputmethod.InputMethodSubtype imeSubtype) {
        return this.mKeyboardLayoutManager.getKeyboardLayoutListForInputDevice(identifier, userId, imeInfo, imeSubtype);
    }

    public void setFocusedApplication(int displayId, android.view.InputApplicationHandle application) {
        this.mNative.setFocusedApplication(displayId, application);
    }

    public void setFocusedDisplay(int displayId) {
        this.mNative.setFocusedDisplay(displayId);
    }

    public void onDisplayRemoved(int displayId) {
        updateAdditionalDisplayInputProperties(displayId, new java.util.function.Consumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.input.InputManagerService.AdditionalDisplayInputProperties) obj).reset();
            }
        });
        this.mNative.displayRemoved(displayId);
    }

    public void requestPointerCapture(android.os.IBinder inputChannelToken, boolean enabled) {
        java.util.Objects.requireNonNull(inputChannelToken, "event must not be null");
        this.mNative.requestPointerCapture(inputChannelToken, enabled);
    }

    public void setInputDispatchMode(boolean enabled, boolean frozen) {
        this.mNative.setInputDispatchMode(enabled, frozen);
    }

    public void setSystemUiLightsOut(boolean lightsOut) {
        this.mNative.setSystemUiLightsOut(lightsOut);
    }

    public boolean startDragAndDrop(android.view.InputChannel fromChannel, android.view.InputChannel dragAndDropChannel) {
        return this.mNative.transferTouchGesture(fromChannel.getToken(), dragAndDropChannel.getToken(), true);
    }

    public boolean transferTouchGesture(android.os.IBinder fromChannelToken, android.os.IBinder toChannelToken) {
        java.util.Objects.requireNonNull(fromChannelToken);
        java.util.Objects.requireNonNull(toChannelToken);
        return this.mNative.transferTouchGesture(fromChannelToken, toChannelToken, false);
    }

    public int getMousePointerSpeed() {
        return this.mNative.getMousePointerSpeed();
    }

    public void tryPointerSpeed(int speed) {
        if (!checkCallingPermission("android.permission.SET_POINTER_SPEED", "tryPointerSpeed()")) {
            throw new java.lang.SecurityException("Requires SET_POINTER_SPEED permission");
        }
        if (speed < -7 || speed > 7) {
            throw new java.lang.IllegalArgumentException("speed out of range");
        }
        setPointerSpeedUnchecked(speed);
    }

    private void setPointerSpeedUnchecked(int speed) {
        this.mNative.setPointerSpeed(java.lang.Math.min(java.lang.Math.max(speed, -7), 7));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMousePointerAccelerationEnabled(final boolean enabled, int displayId) {
        updateAdditionalDisplayInputProperties(displayId, new java.util.function.Consumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.input.InputManagerService.AdditionalDisplayInputProperties) obj).mousePointerAccelerationEnabled = enabled;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPointerIconVisible(final boolean visible, int displayId) {
        updateAdditionalDisplayInputProperties(displayId, new java.util.function.Consumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.input.InputManagerService.AdditionalDisplayInputProperties) obj).pointerIconVisible = visible;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayEligibilityForPointerCapture(int displayId, boolean isEligible) {
        this.mNative.setDisplayEligibilityForPointerCapture(displayId, isEligible);
    }

    private static class VibrationInfo {
        private final int[] mAmplitudes;
        private final long[] mPattern;
        private final int mRepeat;

        public long[] getPattern() {
            return this.mPattern;
        }

        public int[] getAmplitudes() {
            return this.mAmplitudes;
        }

        public int getRepeatIndex() {
            return this.mRepeat;
        }

        VibrationInfo(android.os.VibrationEffect effect) {
            long[] pattern = null;
            int[] amplitudes = null;
            int patternRepeatIndex = -1;
            int amplitudeCount = -1;
            if (effect instanceof android.os.VibrationEffect.Composed) {
                android.os.VibrationEffect.Composed composed = (android.os.VibrationEffect.Composed) effect;
                int segmentCount = composed.getSegments().size();
                pattern = new long[segmentCount];
                amplitudes = new int[segmentCount];
                patternRepeatIndex = composed.getRepeatIndex();
                amplitudeCount = 0;
                int i = 0;
                while (true) {
                    if (i >= segmentCount) {
                        break;
                    }
                    android.os.vibrator.StepSegment stepSegment = (android.os.vibrator.VibrationEffectSegment) composed.getSegments().get(i);
                    patternRepeatIndex = composed.getRepeatIndex() == i ? amplitudeCount : patternRepeatIndex;
                    if (!(stepSegment instanceof android.os.vibrator.StepSegment)) {
                        android.util.Slog.w(com.android.server.input.InputManagerService.TAG, "Input devices don't support segment " + stepSegment);
                        amplitudeCount = -1;
                        break;
                    }
                    float amplitude = stepSegment.getAmplitude();
                    if (java.lang.Float.compare(amplitude, -1.0f) == 0) {
                        amplitudes[amplitudeCount] = 192;
                    } else {
                        amplitudes[amplitudeCount] = (int) (255.0f * amplitude);
                    }
                    pattern[amplitudeCount] = stepSegment.getDuration();
                    i++;
                    amplitudeCount++;
                }
            }
            if (amplitudeCount < 0) {
                android.util.Slog.w(com.android.server.input.InputManagerService.TAG, "Only oneshot and step waveforms are supported on input devices");
                this.mPattern = new long[0];
                this.mAmplitudes = new int[0];
                this.mRepeat = -1;
                return;
            }
            this.mRepeat = patternRepeatIndex;
            this.mPattern = new long[amplitudeCount];
            this.mAmplitudes = new int[amplitudeCount];
            java.lang.System.arraycopy(pattern, 0, this.mPattern, 0, amplitudeCount);
            java.lang.System.arraycopy(amplitudes, 0, this.mAmplitudes, 0, amplitudeCount);
            if (this.mRepeat >= this.mPattern.length) {
                throw new java.lang.ArrayIndexOutOfBoundsException("Repeat index " + this.mRepeat + " must be within the bounds of the pattern.length " + this.mPattern.length);
            }
        }
    }

    private com.android.server.input.InputManagerService.VibratorToken getVibratorToken(int deviceId, android.os.IBinder token) {
        com.android.server.input.InputManagerService.VibratorToken v;
        synchronized (this.mVibratorLock) {
            v = this.mVibratorTokens.get(token);
            if (v == null) {
                int i = this.mNextVibratorTokenValue;
                this.mNextVibratorTokenValue = i + 1;
                v = new com.android.server.input.InputManagerService.VibratorToken(deviceId, token, i);
                try {
                    token.linkToDeath(v, 0);
                    this.mVibratorTokens.put(token, v);
                } catch (android.os.RemoteException ex) {
                    throw new java.lang.RuntimeException(ex);
                }
            }
        }
        return v;
    }

    public void vibrate(int deviceId, android.os.VibrationEffect effect, android.os.IBinder token) {
        com.android.server.input.InputManagerService.VibrationInfo info = new com.android.server.input.InputManagerService.VibrationInfo(effect);
        com.android.server.input.InputManagerService.VibratorToken v = getVibratorToken(deviceId, token);
        synchronized (v) {
            v.mVibrating = true;
            this.mNative.vibrate(deviceId, info.getPattern(), info.getAmplitudes(), info.getRepeatIndex(), v.mTokenValue);
        }
    }

    public int[] getVibratorIds(int deviceId) {
        return this.mNative.getVibratorIds(deviceId);
    }

    public boolean isVibrating(int deviceId) {
        return this.mNative.isVibrating(deviceId);
    }

    public void vibrateCombined(int deviceId, android.os.CombinedVibration effect, android.os.IBinder token) {
        com.android.server.input.InputManagerService.VibratorToken v = getVibratorToken(deviceId, token);
        synchronized (v) {
            if (!(effect instanceof android.os.CombinedVibration.Mono) && !(effect instanceof android.os.CombinedVibration.Stereo)) {
                android.util.Slog.e(TAG, "Only Mono and Stereo effects are supported");
                return;
            }
            v.mVibrating = true;
            if (effect instanceof android.os.CombinedVibration.Mono) {
                android.os.CombinedVibration.Mono mono = (android.os.CombinedVibration.Mono) effect;
                com.android.server.input.InputManagerService.VibrationInfo info = new com.android.server.input.InputManagerService.VibrationInfo(mono.getEffect());
                this.mNative.vibrate(deviceId, info.getPattern(), info.getAmplitudes(), info.getRepeatIndex(), v.mTokenValue);
            } else if (effect instanceof android.os.CombinedVibration.Stereo) {
                android.os.CombinedVibration.Stereo stereo = (android.os.CombinedVibration.Stereo) effect;
                android.util.SparseArray<android.os.VibrationEffect> effects = stereo.getEffects();
                long[] pattern = new long[0];
                android.util.SparseArray<int[]> amplitudes = new android.util.SparseArray<>(effects.size());
                long[] pattern2 = pattern;
                int repeat = Integer.MIN_VALUE;
                for (int i = 0; i < effects.size(); i++) {
                    com.android.server.input.InputManagerService.VibrationInfo info2 = new com.android.server.input.InputManagerService.VibrationInfo(effects.valueAt(i));
                    if (pattern2.length == 0) {
                        pattern2 = info2.getPattern();
                    }
                    if (repeat == Integer.MIN_VALUE) {
                        repeat = info2.getRepeatIndex();
                    }
                    amplitudes.put(effects.keyAt(i), info2.getAmplitudes());
                }
                this.mNative.vibrateCombined(deviceId, pattern2, amplitudes, repeat, v.mTokenValue);
            }
        }
    }

    public void cancelVibrate(int deviceId, android.os.IBinder token) {
        synchronized (this.mVibratorLock) {
            com.android.server.input.InputManagerService.VibratorToken v = this.mVibratorTokens.get(token);
            if (v != null && v.mDeviceId == deviceId) {
                cancelVibrateIfNeeded(v);
            }
        }
    }

    void onVibratorTokenDied(com.android.server.input.InputManagerService.VibratorToken v) {
        synchronized (this.mVibratorLock) {
            this.mVibratorTokens.remove(v.mToken);
        }
        cancelVibrateIfNeeded(v);
    }

    private void cancelVibrateIfNeeded(com.android.server.input.InputManagerService.VibratorToken v) {
        synchronized (v) {
            if (v.mVibrating) {
                this.mNative.cancelVibrate(v.mDeviceId, v.mTokenValue);
                v.mVibrating = false;
            }
        }
    }

    private void notifyVibratorState(int deviceId, boolean isOn) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "notifyVibratorState: deviceId=" + deviceId + " isOn=" + isOn);
        }
        synchronized (this.mVibratorLock) {
            this.mIsVibrating.put(deviceId, isOn);
            notifyVibratorStateListenersLocked(deviceId);
        }
    }

    private void notifyVibratorStateListenersLocked(int deviceId) {
        if (!this.mVibratorStateListeners.contains(deviceId)) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Device " + deviceId + " doesn't have vibrator state listener.");
                return;
            }
            return;
        }
        android.os.RemoteCallbackList<android.os.IVibratorStateListener> listeners = this.mVibratorStateListeners.get(deviceId);
        int length = listeners.beginBroadcast();
        for (int i = 0; i < length; i++) {
            try {
                notifyVibratorStateListenerLocked(deviceId, (android.os.IVibratorStateListener) listeners.getBroadcastItem(i));
            } finally {
                listeners.finishBroadcast();
            }
        }
    }

    private void notifyVibratorStateListenerLocked(int deviceId, android.os.IVibratorStateListener listener) {
        try {
            listener.onVibrating(this.mIsVibrating.get(deviceId));
        } catch (android.os.RemoteException | java.lang.RuntimeException e) {
            android.util.Slog.e(TAG, "Vibrator state listener failed to call", e);
        }
    }

    public boolean registerVibratorStateListener(int deviceId, android.os.IVibratorStateListener listener) {
        android.os.RemoteCallbackList<android.os.IVibratorStateListener> listeners;
        java.util.Objects.requireNonNull(listener, "listener must not be null");
        synchronized (this.mVibratorLock) {
            if (!this.mVibratorStateListeners.contains(deviceId)) {
                listeners = new android.os.RemoteCallbackList<>();
                this.mVibratorStateListeners.put(deviceId, listeners);
            } else {
                listeners = this.mVibratorStateListeners.get(deviceId);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!listeners.register(listener)) {
                    android.util.Slog.e(TAG, "Could not register vibrator state listener " + listener);
                    return false;
                }
                notifyVibratorStateListenerLocked(deviceId, listener);
                return true;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    public boolean unregisterVibratorStateListener(int deviceId, android.os.IVibratorStateListener listener) {
        synchronized (this.mVibratorLock) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if (!this.mVibratorStateListeners.contains(deviceId)) {
                    android.util.Slog.w(TAG, "Vibrator state listener " + deviceId + " doesn't exist");
                    return false;
                }
                android.os.RemoteCallbackList<android.os.IVibratorStateListener> listeners = this.mVibratorStateListeners.get(deviceId);
                return listeners.unregister(listener);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }

    public android.hardware.input.IInputDeviceBatteryState getBatteryState(int deviceId) {
        return this.mBatteryController.getBatteryState(deviceId);
    }

    public boolean setPointerIcon(android.view.PointerIcon icon, int displayId, int deviceId, int pointerId, android.os.IBinder inputToken) {
        java.util.Objects.requireNonNull(icon);
        return this.mNative.setPointerIcon(icon, displayId, deviceId, pointerId, inputToken);
    }

    public void addPortAssociation(java.lang.String inputPort, int displayPort) {
        if (!checkCallingPermission("android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", "addPortAssociation()")) {
            throw new java.lang.SecurityException("Requires ASSOCIATE_INPUT_DEVICE_TO_DISPLAY permission");
        }
        java.util.Objects.requireNonNull(inputPort);
        synchronized (this.mAssociationsLock) {
            this.mRuntimeAssociations.put(inputPort, java.lang.Integer.valueOf(displayPort));
        }
        this.mNative.notifyPortAssociationsChanged();
    }

    public void removePortAssociation(java.lang.String inputPort) {
        if (!checkCallingPermission("android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", "removePortAssociation()")) {
            throw new java.lang.SecurityException("Requires ASSOCIATE_INPUT_DEVICE_TO_DISPLAY permission");
        }
        java.util.Objects.requireNonNull(inputPort);
        synchronized (this.mAssociationsLock) {
            this.mRuntimeAssociations.remove(inputPort);
        }
        this.mNative.notifyPortAssociationsChanged();
    }

    public void addUniqueIdAssociationByPort(java.lang.String inputPort, java.lang.String displayUniqueId) {
        if (!checkCallingPermission("android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", "addUniqueIdAssociation()")) {
            throw new java.lang.SecurityException("Requires ASSOCIATE_INPUT_DEVICE_TO_DISPLAY permission");
        }
        java.util.Objects.requireNonNull(inputPort);
        java.util.Objects.requireNonNull(displayUniqueId);
        synchronized (this.mAssociationsLock) {
            this.mUniqueIdAssociationsByPort.put(inputPort, displayUniqueId);
        }
        this.mNative.changeUniqueIdAssociation();
    }

    public void removeUniqueIdAssociationByPort(java.lang.String inputPort) {
        if (!checkCallingPermission("android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", "removeUniqueIdAssociation()")) {
            throw new java.lang.SecurityException("Requires ASSOCIATE_INPUT_DEVICE_TO_DISPLAY permission");
        }
        java.util.Objects.requireNonNull(inputPort);
        synchronized (this.mAssociationsLock) {
            this.mUniqueIdAssociationsByPort.remove(inputPort);
        }
        this.mNative.changeUniqueIdAssociation();
    }

    public void addUniqueIdAssociationByDescriptor(java.lang.String inputDeviceDescriptor, java.lang.String displayUniqueId) {
        if (!checkCallingPermission("android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", "addUniqueIdAssociationByDescriptor()")) {
            throw new java.lang.SecurityException("Requires ASSOCIATE_INPUT_DEVICE_TO_DISPLAY permission");
        }
        java.util.Objects.requireNonNull(inputDeviceDescriptor);
        java.util.Objects.requireNonNull(displayUniqueId);
        synchronized (this.mAssociationsLock) {
            this.mUniqueIdAssociationsByDescriptor.put(inputDeviceDescriptor, displayUniqueId);
        }
        this.mNative.changeUniqueIdAssociation();
    }

    public void removeUniqueIdAssociationByDescriptor(java.lang.String inputDeviceDescriptor) {
        if (!checkCallingPermission("android.permission.ASSOCIATE_INPUT_DEVICE_TO_DISPLAY", "removeUniqueIdAssociationByDescriptor()")) {
            throw new java.lang.SecurityException("Requires ASSOCIATE_INPUT_DEVICE_TO_DISPLAY permission");
        }
        java.util.Objects.requireNonNull(inputDeviceDescriptor);
        synchronized (this.mAssociationsLock) {
            this.mUniqueIdAssociationsByDescriptor.remove(inputDeviceDescriptor);
        }
        this.mNative.changeUniqueIdAssociation();
    }

    void setTypeAssociationInternal(java.lang.String inputPort, java.lang.String type) {
        java.util.Objects.requireNonNull(inputPort);
        java.util.Objects.requireNonNull(type);
        synchronized (this.mAssociationsLock) {
            this.mDeviceTypeAssociations.put(inputPort, type);
        }
        this.mNative.changeTypeAssociation();
    }

    void unsetTypeAssociationInternal(java.lang.String inputPort) {
        java.util.Objects.requireNonNull(inputPort);
        synchronized (this.mAssociationsLock) {
            this.mDeviceTypeAssociations.remove(inputPort);
        }
        this.mNative.changeTypeAssociation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addKeyboardLayoutAssociation(java.lang.String inputPort, java.lang.String languageTag, java.lang.String layoutType) {
        java.util.Objects.requireNonNull(inputPort);
        java.util.Objects.requireNonNull(languageTag);
        java.util.Objects.requireNonNull(layoutType);
        synchronized (this.mAssociationsLock) {
            this.mKeyboardLayoutAssociations.put(inputPort, android.text.TextUtils.formatSimple("%s,%s", new java.lang.Object[]{languageTag, layoutType}));
        }
        this.mNative.changeKeyboardLayoutAssociation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeKeyboardLayoutAssociation(java.lang.String inputPort) {
        java.util.Objects.requireNonNull(inputPort);
        synchronized (this.mAssociationsLock) {
            this.mKeyboardLayoutAssociations.remove(inputPort);
        }
        this.mNative.changeKeyboardLayoutAssociation();
    }

    public android.hardware.input.InputSensorInfo[] getSensorList(int deviceId) {
        return this.mNative.getSensorList(deviceId);
    }

    public boolean registerSensorListener(android.hardware.input.IInputSensorEventListener listener) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "registerSensorListener: listener=" + listener + " callingPid=" + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(listener, "listener must not be null");
        synchronized (this.mSensorEventLock) {
            int callingPid = android.os.Binder.getCallingPid();
            if (this.mSensorEventListeners.get(callingPid) != null) {
                android.util.Slog.e(TAG, "The calling process " + callingPid + " has already registered an InputSensorEventListener.");
                return false;
            }
            com.android.server.input.InputManagerService.SensorEventListenerRecord record = new com.android.server.input.InputManagerService.SensorEventListenerRecord(callingPid, listener);
            try {
                android.os.IBinder binder = listener.asBinder();
                binder.linkToDeath(record, 0);
                this.mSensorEventListeners.put(callingPid, record);
                return true;
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    public void unregisterSensorListener(android.hardware.input.IInputSensorEventListener listener) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "unregisterSensorListener: listener=" + listener + " callingPid=" + android.os.Binder.getCallingPid());
        }
        java.util.Objects.requireNonNull(listener, "listener must not be null");
        synchronized (this.mSensorEventLock) {
            int callingPid = android.os.Binder.getCallingPid();
            if (this.mSensorEventListeners.get(callingPid) != null) {
                com.android.server.input.InputManagerService.SensorEventListenerRecord record = this.mSensorEventListeners.get(callingPid);
                if (record.getListener().asBinder() != listener.asBinder()) {
                    throw new java.lang.IllegalArgumentException("listener is not registered");
                }
                this.mSensorEventListeners.remove(callingPid);
            }
        }
    }

    public boolean flushSensor(int deviceId, int sensorType) {
        synchronized (this.mSensorEventLock) {
            int callingPid = android.os.Binder.getCallingPid();
            com.android.server.input.InputManagerService.SensorEventListenerRecord listener = this.mSensorEventListeners.get(callingPid);
            if (listener == null) {
                return false;
            }
            return this.mNative.flushSensor(deviceId, sensorType);
        }
    }

    public boolean enableSensor(int deviceId, int sensorType, int samplingPeriodUs, int maxBatchReportLatencyUs) {
        boolean zEnableSensor;
        synchronized (this.mInputDevicesLock) {
            zEnableSensor = this.mNative.enableSensor(deviceId, sensorType, samplingPeriodUs, maxBatchReportLatencyUs);
        }
        return zEnableSensor;
    }

    public void disableSensor(int deviceId, int sensorType) {
        synchronized (this.mInputDevicesLock) {
            this.mNative.disableSensor(deviceId, sensorType);
        }
    }

    private final class LightSession implements android.os.IBinder.DeathRecipient {
        private final int mDeviceId;
        private int[] mLightIds;
        private android.hardware.lights.LightState[] mLightStates;
        private final java.lang.String mOpPkg;
        private final android.os.IBinder mToken;

        LightSession(int deviceId, java.lang.String opPkg, android.os.IBinder token) {
            this.mDeviceId = deviceId;
            this.mOpPkg = opPkg;
            this.mToken = token;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.InputManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.input.InputManagerService.TAG, "Light token died.");
            }
            synchronized (com.android.server.input.InputManagerService.this.mLightLock) {
                com.android.server.input.InputManagerService.this.closeLightSession(this.mDeviceId, this.mToken);
                com.android.server.input.InputManagerService.this.mLightSessions.remove(this.mToken);
            }
        }
    }

    public java.util.List<android.hardware.lights.Light> getLights(int deviceId) {
        return this.mNative.getLights(deviceId);
    }

    private void setLightStateInternal(int deviceId, android.hardware.lights.Light light, android.hardware.lights.LightState lightState) {
        java.util.Objects.requireNonNull(light, "light does not exist");
        if (DEBUG) {
            android.util.Slog.d(TAG, "setLightStateInternal device " + deviceId + " light " + light + "lightState " + lightState);
        }
        if (light.getType() == 10002) {
            this.mNative.setLightPlayerId(deviceId, light.getId(), lightState.getPlayerId());
        } else {
            this.mNative.setLightColor(deviceId, light.getId(), lightState.getColor());
        }
    }

    private void setLightStatesInternal(int deviceId, int[] lightIds, android.hardware.lights.LightState[] lightStates) {
        java.util.List<android.hardware.lights.Light> lights = this.mNative.getLights(deviceId);
        android.util.SparseArray<android.hardware.lights.Light> lightArray = new android.util.SparseArray<>();
        for (int i = 0; i < lights.size(); i++) {
            lightArray.put(lights.get(i).getId(), lights.get(i));
        }
        for (int i2 = 0; i2 < lightIds.length; i2++) {
            if (lightArray.contains(lightIds[i2])) {
                setLightStateInternal(deviceId, lightArray.get(lightIds[i2]), lightStates[i2]);
            }
        }
    }

    public void setLightStates(int deviceId, int[] lightIds, android.hardware.lights.LightState[] lightStates, android.os.IBinder token) {
        boolean z = true;
        com.android.internal.util.Preconditions.checkArgument(lightIds.length == lightStates.length, "lights and light states are not same length");
        synchronized (this.mLightLock) {
            com.android.server.input.InputManagerService.LightSession lightSession = this.mLightSessions.get(token);
            com.android.internal.util.Preconditions.checkArgument(lightSession != null, "not registered");
            if (lightSession.mDeviceId != deviceId) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkState(z, "Incorrect device ID");
            lightSession.mLightIds = (int[]) lightIds.clone();
            lightSession.mLightStates = (android.hardware.lights.LightState[]) lightStates.clone();
            if (DEBUG) {
                android.util.Slog.d(TAG, "setLightStates for " + lightSession.mOpPkg + " device " + deviceId);
            }
        }
        setLightStatesInternal(deviceId, lightIds, lightStates);
    }

    public android.hardware.lights.LightState getLightState(int deviceId, int lightId) {
        android.hardware.lights.LightState lightState;
        synchronized (this.mLightLock) {
            int color = this.mNative.getLightColor(deviceId, lightId);
            int playerId = this.mNative.getLightPlayerId(deviceId, lightId);
            lightState = new android.hardware.lights.LightState(color, playerId);
        }
        return lightState;
    }

    public void openLightSession(int deviceId, java.lang.String opPkg, android.os.IBinder token) {
        java.util.Objects.requireNonNull(token);
        synchronized (this.mLightLock) {
            com.android.internal.util.Preconditions.checkState(this.mLightSessions.get(token) == null, "already registered");
            com.android.server.input.InputManagerService.LightSession lightSession = new com.android.server.input.InputManagerService.LightSession(deviceId, opPkg, token);
            try {
                token.linkToDeath(lightSession, 0);
            } catch (android.os.RemoteException ex) {
                ex.rethrowAsRuntimeException();
            }
            this.mLightSessions.put(token, lightSession);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Open light session for " + opPkg + " device " + deviceId);
            }
        }
    }

    public void closeLightSession(int deviceId, android.os.IBinder token) {
        java.util.Objects.requireNonNull(token);
        synchronized (this.mLightLock) {
            com.android.server.input.InputManagerService.LightSession lightSession = this.mLightSessions.get(token);
            com.android.internal.util.Preconditions.checkState(lightSession != null, "not registered");
            java.util.Arrays.fill(lightSession.mLightStates, new android.hardware.lights.LightState(0));
            setLightStatesInternal(deviceId, lightSession.mLightIds, lightSession.mLightStates);
            this.mLightSessions.remove(token);
            if (!this.mLightSessions.isEmpty()) {
                com.android.server.input.InputManagerService.LightSession nextSession = this.mLightSessions.valueAt(0);
                setLightStatesInternal(deviceId, nextSession.mLightIds, nextSession.mLightStates);
            }
        }
    }

    public void cancelCurrentTouch() {
        if (!checkCallingPermission("android.permission.MONITOR_INPUT", "cancelCurrentTouch()")) {
            throw new java.lang.SecurityException("Requires MONITOR_INPUT permission");
        }
        this.mNative.cancelCurrentTouch();
    }

    public void registerBatteryListener(int deviceId, android.hardware.input.IInputDeviceBatteryListener listener) {
        java.util.Objects.requireNonNull(listener);
        this.mBatteryController.registerBatteryListener(deviceId, listener, android.os.Binder.getCallingPid());
    }

    public void unregisterBatteryListener(int deviceId, android.hardware.input.IInputDeviceBatteryListener listener) {
        java.util.Objects.requireNonNull(listener);
        this.mBatteryController.unregisterBatteryListener(deviceId, listener, android.os.Binder.getCallingPid());
    }

    public java.lang.String getInputDeviceBluetoothAddress(int deviceId) {
        super.getInputDeviceBluetoothAddress_enforcePermission();
        java.lang.String address = this.mNative.getBluetoothAddress(deviceId);
        if (address == null) {
            return null;
        }
        if (!android.bluetooth.BluetoothAdapter.checkBluetoothAddress(address)) {
            throw new java.lang.IllegalStateException("The Bluetooth address of input device " + deviceId + " should not be invalid: address=" + address);
        }
        return address;
    }

    public void pilferPointers(android.os.IBinder inputChannelToken) {
        super.pilferPointers_enforcePermission();
        java.util.Objects.requireNonNull(inputChannelToken);
        this.mNative.pilferPointers(inputChannelToken);
    }

    public void registerKeyboardBacklightListener(android.hardware.input.IKeyboardBacklightListener listener) {
        super.registerKeyboardBacklightListener_enforcePermission();
        java.util.Objects.requireNonNull(listener);
        this.mKeyboardBacklightController.registerKeyboardBacklightListener(listener, android.os.Binder.getCallingPid());
    }

    public void unregisterKeyboardBacklightListener(android.hardware.input.IKeyboardBacklightListener listener) {
        super.unregisterKeyboardBacklightListener_enforcePermission();
        java.util.Objects.requireNonNull(listener);
        this.mKeyboardBacklightController.unregisterKeyboardBacklightListener(listener, android.os.Binder.getCallingPid());
    }

    public android.hardware.input.HostUsiVersion getHostUsiVersionFromDisplayConfig(int displayId) {
        return this.mDisplayManagerInternal.getHostUsiVersion(displayId);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw) || this.mExt.dynamicallyAdjustDump(pw, args)) {
            return;
        }
        java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(pw, "  ");
        indentingPrintWriter.println("INPUT MANAGER (dumpsys input)\n");
        java.lang.String dumpStr = this.mNative.dump();
        if (dumpStr != null) {
            pw.println(dumpStr);
        }
        indentingPrintWriter.println("Input Manager Service (Java) State:");
        indentingPrintWriter.increaseIndent();
        dumpAssociations(indentingPrintWriter);
        dumpSpyWindowGestureMonitors(indentingPrintWriter);
        dumpDisplayInputPropertiesValues(indentingPrintWriter);
        this.mBatteryController.dump(indentingPrintWriter);
        this.mKeyboardBacklightController.dump(indentingPrintWriter);
        this.mKeyboardLedController.dump(indentingPrintWriter);
    }

    private void dumpAssociations(final android.util.IndentingPrintWriter pw) {
        if (!this.mStaticAssociations.isEmpty()) {
            pw.println("Static Associations:");
            this.mStaticAssociations.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.input.InputManagerService.lambda$dumpAssociations$3(pw, (java.lang.String) obj, (java.lang.Integer) obj2);
                }
            });
        }
        synchronized (this.mAssociationsLock) {
            if (!this.mRuntimeAssociations.isEmpty()) {
                pw.println("Runtime Associations:");
                this.mRuntimeAssociations.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda4
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.input.InputManagerService.lambda$dumpAssociations$4(pw, (java.lang.String) obj, (java.lang.Integer) obj2);
                    }
                });
            }
            if (!this.mUniqueIdAssociationsByPort.isEmpty()) {
                pw.println("Unique Id Associations:");
                this.mUniqueIdAssociationsByPort.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda5
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.input.InputManagerService.lambda$dumpAssociations$5(pw, (java.lang.String) obj, (java.lang.String) obj2);
                    }
                });
            }
            if (!this.mUniqueIdAssociationsByDescriptor.isEmpty()) {
                pw.println("Unique Id Associations:");
                this.mUniqueIdAssociationsByDescriptor.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda6
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.input.InputManagerService.lambda$dumpAssociations$6(pw, (java.lang.String) obj, (java.lang.String) obj2);
                    }
                });
            }
            if (!this.mDeviceTypeAssociations.isEmpty()) {
                pw.println("Type Associations:");
                this.mDeviceTypeAssociations.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda7
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        com.android.server.input.InputManagerService.lambda$dumpAssociations$7(pw, (java.lang.String) obj, (java.lang.String) obj2);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$dumpAssociations$3(android.util.IndentingPrintWriter pw, java.lang.String k, java.lang.Integer v) {
        pw.print("  port: " + k);
        pw.println("  display: " + v);
    }

    static /* synthetic */ void lambda$dumpAssociations$4(android.util.IndentingPrintWriter pw, java.lang.String k, java.lang.Integer v) {
        pw.print("  port: " + k);
        pw.println("  display: " + v);
    }

    static /* synthetic */ void lambda$dumpAssociations$5(android.util.IndentingPrintWriter pw, java.lang.String k, java.lang.String v) {
        pw.print("  port: " + k);
        pw.println("  uniqueId: " + v);
    }

    static /* synthetic */ void lambda$dumpAssociations$6(android.util.IndentingPrintWriter pw, java.lang.String k, java.lang.String v) {
        pw.print("  descriptor: " + k);
        pw.println("  uniqueId: " + v);
    }

    static /* synthetic */ void lambda$dumpAssociations$7(android.util.IndentingPrintWriter pw, java.lang.String k, java.lang.String v) {
        pw.print("  port: " + k);
        pw.println("  type: " + v);
    }

    private void dumpSpyWindowGestureMonitors(android.util.IndentingPrintWriter pw) {
        synchronized (this.mInputMonitors) {
            if (this.mInputMonitors.isEmpty()) {
                return;
            }
            pw.println("Gesture Monitors (implemented as spy windows):");
            int i = 0;
            for (com.android.server.input.GestureMonitorSpyWindow monitor : this.mInputMonitors.values()) {
                pw.append("  " + i + ": ").println(monitor.dump());
                i++;
            }
        }
    }

    private void dumpDisplayInputPropertiesValues(android.util.IndentingPrintWriter pw) {
        synchronized (this.mAdditionalDisplayInputPropertiesLock) {
            pw.println("mAdditionalDisplayInputProperties:");
            pw.increaseIndent();
            try {
                if (this.mAdditionalDisplayInputProperties.size() == 0) {
                    pw.println("<none>");
                    return;
                }
                for (int i = 0; i < this.mAdditionalDisplayInputProperties.size(); i++) {
                    pw.println("displayId: " + this.mAdditionalDisplayInputProperties.keyAt(i));
                    com.android.server.input.InputManagerService.AdditionalDisplayInputProperties properties = this.mAdditionalDisplayInputProperties.valueAt(i);
                    pw.println("mousePointerAccelerationEnabled: " + properties.mousePointerAccelerationEnabled);
                    pw.println("pointerIconVisible: " + properties.pointerIconVisible);
                }
            } finally {
                pw.decreaseIndent();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkCallingPermission(java.lang.String permission, java.lang.String func) {
        return checkCallingPermission(permission, func, false);
    }

    private boolean checkCallingPermission(java.lang.String permission, java.lang.String func, boolean checkInstrumentationSource) {
        if (android.os.Binder.getCallingPid() == android.os.Process.myPid() || this.mContext.checkCallingPermission(permission) == 0) {
            return true;
        }
        if (checkInstrumentationSource) {
            android.app.ActivityManagerInternal ami = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            java.util.Objects.requireNonNull(ami, "ActivityManagerInternal should not be null.");
            int instrumentationUid = ami.getInstrumentationSourceUid(android.os.Binder.getCallingUid());
            if (instrumentationUid != -1) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    if (this.mContext.checkPermission(permission, -1, instrumentationUid) == 0) {
                        return true;
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires " + permission;
        android.util.Slog.w(TAG, msg);
        return false;
    }

    @Override // com.android.server.Watchdog.Monitor
    public void monitor() {
        synchronized (this.mInputFilterLock) {
        }
        synchronized (this.mAssociationsLock) {
        }
        synchronized (this.mLidSwitchLock) {
        }
        synchronized (this.mInputMonitors) {
        }
        synchronized (this.mAdditionalDisplayInputPropertiesLock) {
        }
        this.mBatteryController.monitor();
        this.mPointerIconCache.monitor();
        this.mNative.monitor();
    }

    private void notifyConfigurationChanged(long whenNanos) {
        this.mWindowManagerCallbacks.notifyConfigurationChanged();
    }

    private void notifyInputDevicesChanged(android.view.InputDevice[] inputDevices) {
        synchronized (this.mInputDevicesLock) {
            if (!this.mInputDevicesChangedPending) {
                this.mInputDevicesChangedPending = true;
                this.mHandler.obtainMessage(1, this.mInputDevices).sendToTarget();
            }
            this.mInputDevices = inputDevices;
        }
    }

    private void notifyInputReaderThread(int tid) {
        this.mExt.notifyInputReaderThread(tid);
    }

    private void notifySwitch(long whenNanos, int switchValues, int switchMask) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "notifySwitch: values=" + java.lang.Integer.toHexString(switchValues) + ", mask=" + java.lang.Integer.toHexString(switchMask));
        }
        if ((switchMask & 1) != 0) {
            boolean lidOpen = (switchValues & 1) == 0;
            synchronized (this.mLidSwitchLock) {
                if (this.mSystemReady) {
                    for (int i = 0; i < this.mLidSwitchCallbacks.size(); i++) {
                        com.android.server.input.InputManagerInternal.LidSwitchCallback callbacks = this.mLidSwitchCallbacks.get(i);
                        callbacks.notifyLidSwitchChanged(whenNanos, lidOpen);
                    }
                }
            }
        }
        if ((switchMask & 512) != 0) {
            boolean lensCovered = (switchValues & 512) != 0;
            this.mWindowManagerCallbacks.notifyCameraLensCoverSwitchChanged(whenNanos, lensCovered);
            setSensorPrivacy(2, lensCovered);
        }
        if (this.mUseDevInputEventForAudioJack && (switchMask & 212) != 0) {
            this.mWiredAccessoryCallbacks.notifyWiredAccessoryChanged(whenNanos, switchValues, switchMask);
        }
        if ((switchMask & 2) != 0) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.argi1 = (int) ((-1) & whenNanos);
            args.argi2 = (int) (whenNanos >> 32);
            args.arg1 = java.lang.Boolean.valueOf((switchValues & 2) != 0);
            this.mHandler.obtainMessage(3, args).sendToTarget();
        }
        if ((switchMask & 16384) != 0) {
            boolean micMute = (switchValues & 16384) != 0;
            android.media.AudioManager audioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
            audioManager.setMicrophoneMuteFromSwitch(micMute);
            setSensorPrivacy(1, micMute);
        }
    }

    private void setSensorPrivacy(int sensor, boolean enablePrivacy) {
        android.hardware.SensorPrivacyManagerInternal sensorPrivacyManagerInternal = (android.hardware.SensorPrivacyManagerInternal) com.android.server.LocalServices.getService(android.hardware.SensorPrivacyManagerInternal.class);
        sensorPrivacyManagerInternal.setPhysicalToggleSensorPrivacy(-2, sensor, enablePrivacy);
    }

    private void notifyInputChannelBroken(android.os.IBinder token) {
        synchronized (this.mInputMonitors) {
            if (this.mInputMonitors.containsKey(token)) {
                removeSpyWindowGestureMonitor(token);
            }
        }
        this.mWindowManagerCallbacks.notifyInputChannelBroken(token);
    }

    private void notifyFocusChanged(android.os.IBinder oldToken, android.os.IBinder newToken) {
        this.mWindowManagerCallbacks.notifyFocusChanged(oldToken, newToken);
    }

    private void notifyDropWindow(android.os.IBinder token, float x, float y) {
        this.mWindowManagerCallbacks.notifyDropWindow(token, x, y);
    }

    private void notifyUntrustedTouch(java.lang.String packageName) {
        android.util.Log.i(TAG, "Suppressing untrusted touch toast for " + packageName);
        try {
            this.mExt.showTipsDialog(packageName, this.mContext);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "notifyUntrustedTouch err ", e);
        }
    }

    private boolean isOplusTrustedApp(java.lang.String packageName, int uid, java.lang.String extras) {
        return this.mExt.isOplusTrustedApp(packageName, uid, extras);
    }

    private void notifyCollectData(java.lang.String type, java.lang.String reason) {
        this.mExt.uploadCollectData(type, reason);
    }

    private void notifyNoFocusedWindowAnr(android.view.InputApplicationHandle inputApplicationHandle) {
        this.mWindowManagerCallbacks.notifyNoFocusedWindowAnr(inputApplicationHandle);
    }

    private void notifyWindowUnresponsive(android.os.IBinder token, int pid, boolean isPidValid, java.lang.String reason) {
        if (this.mExt.notifyGestureMonitorUnresponsive(pid, reason)) {
            return;
        }
        this.mWindowManagerCallbacks.notifyWindowUnresponsive(token, isPidValid ? java.util.OptionalInt.of(pid) : java.util.OptionalInt.empty(), reason);
    }

    private void notifyWindowResponsive(android.os.IBinder token, int pid, boolean isPidValid) {
        this.mWindowManagerCallbacks.notifyWindowResponsive(token, isPidValid ? java.util.OptionalInt.of(pid) : java.util.OptionalInt.empty());
    }

    private void notifySensorEvent(int deviceId, int sensorType, int accuracy, long timestamp, float[] values) {
        int numListeners;
        if (DEBUG) {
            android.util.Slog.d(TAG, "notifySensorEvent: deviceId=" + deviceId + " sensorType=" + sensorType + " values=" + java.util.Arrays.toString(values));
        }
        this.mSensorEventListenersToNotify.clear();
        synchronized (this.mSensorEventLock) {
            numListeners = this.mSensorEventListeners.size();
            for (int i = 0; i < numListeners; i++) {
                this.mSensorEventListenersToNotify.add(this.mSensorEventListeners.valueAt(i));
            }
        }
        for (int i2 = 0; i2 < numListeners; i2++) {
            this.mSensorEventListenersToNotify.get(i2).notifySensorEvent(deviceId, sensorType, accuracy, timestamp, values);
        }
        this.mSensorEventListenersToNotify.clear();
    }

    private void notifySensorAccuracy(int deviceId, int sensorType, int accuracy) {
        int numListeners;
        this.mSensorAccuracyListenersToNotify.clear();
        synchronized (this.mSensorEventLock) {
            numListeners = this.mSensorEventListeners.size();
            for (int i = 0; i < numListeners; i++) {
                this.mSensorAccuracyListenersToNotify.add(this.mSensorEventListeners.valueAt(i));
            }
        }
        for (int i2 = 0; i2 < numListeners; i2++) {
            this.mSensorAccuracyListenersToNotify.get(i2).notifySensorAccuracy(deviceId, sensorType, accuracy);
        }
        this.mSensorAccuracyListenersToNotify.clear();
    }

    final boolean filterInputEvent(android.view.InputEvent event, int policyFlags) {
        synchronized (this.mInputFilterLock) {
            if (this.mInputFilter != null) {
                try {
                    this.mInputFilter.filterInputEvent(event, policyFlags);
                } catch (android.os.RemoteException e) {
                }
                return false;
            }
            if (this.mExt.isNeedIntermittentIntercept(event)) {
                return false;
            }
            event.recycle();
            return true;
        }
    }

    private int interceptKeyBeforeQueueing(android.view.KeyEvent event, int policyFlags) {
        synchronized (this.mFocusEventDebugViewLock) {
            if (this.mFocusEventDebugView != null) {
                this.mFocusEventDebugView.reportKeyEvent(event);
            }
        }
        return this.mWindowManagerCallbacks.interceptKeyBeforeQueueing(event, policyFlags);
    }

    private int interceptMotionBeforeQueueingNonInteractive(int displayId, int source, int action, long whenNanos, int policyFlags) {
        return this.mWindowManagerCallbacks.interceptMotionBeforeQueueingNonInteractive(displayId, source, action, whenNanos, policyFlags);
    }

    private long interceptKeyBeforeDispatching(android.os.IBinder focus, android.view.KeyEvent event, int policyFlags) {
        return this.mWindowManagerCallbacks.interceptKeyBeforeDispatching(focus, event, policyFlags);
    }

    private android.view.KeyEvent dispatchUnhandledKey(android.os.IBinder focus, android.view.KeyEvent event, int policyFlags) {
        return this.mWindowManagerCallbacks.dispatchUnhandledKey(focus, event, policyFlags);
    }

    private void onPointerDownOutsideFocus(android.os.IBinder touchedToken) {
        this.mWindowManagerCallbacks.onPointerDownOutsideFocus(touchedToken);
    }

    private int getVirtualKeyQuietTimeMillis() {
        return this.mContext.getResources().getInteger(android.R.integer.config_sidefpsSkipWaitForPowerAcquireMessage);
    }

    private static java.lang.String[] getExcludedDeviceNames() {
        java.util.List<java.lang.String> names = new java.util.ArrayList<>();
        java.io.File[] baseDirs = {android.os.Environment.getRootDirectory(), android.os.Environment.getVendorDirectory()};
        for (java.io.File baseDir : baseDirs) {
            java.io.File confFile = new java.io.File(baseDir, EXCLUDED_DEVICES_PATH);
            try {
                java.io.InputStream stream = new java.io.FileInputStream(confFile);
                try {
                    names.addAll(com.android.server.input.ConfigurationProcessor.processExcludedDeviceNames(stream));
                    stream.close();
                } catch (java.lang.Throwable th) {
                    try {
                        stream.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.io.FileNotFoundException e) {
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(TAG, "Could not parse '" + confFile.getAbsolutePath() + "'", e2);
            }
        }
        return (java.lang.String[]) names.toArray(new java.lang.String[0]);
    }

    private boolean isPerDisplayTouchModeEnabled() {
        return this.mContext.getResources().getBoolean(android.R.bool.config_perDisplayFocusEnabled);
    }

    private void notifyStylusGestureStarted(int deviceId, long eventTime) {
        this.mBatteryController.notifyStylusGestureStarted(deviceId, eventTime);
    }

    private static <T> java.lang.String[] flatten(java.util.Map<java.lang.String, T> map) {
        final java.util.List<java.lang.String> list = new java.util.ArrayList<>(map.size() * 2);
        map.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputManagerService$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.input.InputManagerService.lambda$flatten$8(list, (java.lang.String) obj, obj2);
            }
        });
        return (java.lang.String[]) list.toArray(new java.lang.String[0]);
    }

    static /* synthetic */ void lambda$flatten$8(java.util.List list, java.lang.String k, java.lang.Object v) {
        list.add(k);
        list.add(v.toString());
    }

    private static java.util.Map<java.lang.String, java.lang.Integer> loadStaticInputPortAssociations() {
        java.io.File baseDir = android.os.Environment.getOdmDirectory();
        java.io.File confFile = new java.io.File(baseDir, PORT_ASSOCIATIONS_PATH);
        if (!confFile.exists()) {
            java.io.File baseDir2 = android.os.Environment.getVendorDirectory();
            confFile = new java.io.File(baseDir2, PORT_ASSOCIATIONS_PATH);
        }
        try {
            java.io.InputStream stream = new java.io.FileInputStream(confFile);
            try {
                java.util.Map<java.lang.String, java.lang.Integer> mapProcessInputPortAssociations = com.android.server.input.ConfigurationProcessor.processInputPortAssociations(stream);
                stream.close();
                return mapProcessInputPortAssociations;
            } catch (java.lang.Throwable th) {
                try {
                    stream.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.FileNotFoundException e) {
            return new java.util.HashMap();
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "Could not parse '" + confFile.getAbsolutePath() + "'", e2);
            return new java.util.HashMap();
        }
    }

    private java.lang.String[] getInputPortAssociations() {
        java.util.Map<java.lang.String, java.lang.Integer> associations = new java.util.HashMap<>(this.mStaticAssociations);
        synchronized (this.mAssociationsLock) {
            associations.putAll(this.mRuntimeAssociations);
        }
        return flatten(associations);
    }

    private java.lang.String[] getInputUniqueIdAssociationsByPort() {
        java.util.Map<java.lang.String, java.lang.String> associations;
        synchronized (this.mAssociationsLock) {
            associations = new java.util.HashMap<>(this.mUniqueIdAssociationsByPort);
        }
        return flatten(associations);
    }

    private java.lang.String[] getInputUniqueIdAssociationsByDescriptor() {
        java.util.Map<java.lang.String, java.lang.String> associations;
        synchronized (this.mAssociationsLock) {
            associations = new java.util.HashMap<>(this.mUniqueIdAssociationsByDescriptor);
        }
        return flatten(associations);
    }

    java.lang.String[] getDeviceTypeAssociations() {
        java.util.Map<java.lang.String, java.lang.String> associations;
        synchronized (this.mAssociationsLock) {
            associations = new java.util.HashMap<>(this.mDeviceTypeAssociations);
        }
        return flatten(associations);
    }

    private java.lang.String[] getKeyboardLayoutAssociations() {
        java.util.Map<java.lang.String, java.lang.String> configs = new android.util.ArrayMap<>();
        synchronized (this.mAssociationsLock) {
            configs.putAll(this.mKeyboardLayoutAssociations);
        }
        return flatten(configs);
    }

    public boolean canDispatchToDisplay(int deviceId, int displayId) {
        return this.mNative.canDispatchToDisplay(deviceId, displayId);
    }

    private int getHoverTapTimeout() {
        return android.view.ViewConfiguration.getHoverTapTimeout();
    }

    private int getHoverTapSlop() {
        return android.view.ViewConfiguration.getHoverTapSlop();
    }

    private int getDoubleTapTimeout() {
        return android.view.ViewConfiguration.getDoubleTapTimeout();
    }

    private int getLongPressTimeout() {
        return android.view.ViewConfiguration.getLongPressTimeout();
    }

    private int getPointerLayer() {
        return this.mWindowManagerCallbacks.getPointerLayer();
    }

    private android.view.PointerIcon getLoadedPointerIcon(int displayId, int type) {
        return this.mPointerIconCache.getLoadedPointerIcon(displayId, type);
    }

    private long getParentSurfaceForPointers(int displayId) {
        android.view.SurfaceControl sc = this.mWindowManagerCallbacks.getParentSurfaceForPointers(displayId);
        if (sc == null) {
            return 0L;
        }
        return sc.mNativeObject;
    }

    private java.lang.String[] getKeyboardLayoutOverlay(android.hardware.input.InputDeviceIdentifier identifier, java.lang.String languageTag, java.lang.String layoutType) {
        if (!this.mSystemReady) {
            return null;
        }
        return this.mKeyboardLayoutManager.getKeyboardLayoutOverlay(identifier, languageTag, layoutType);
    }

    public void remapModifierKey(int fromKey, int toKey) {
        super.remapModifierKey_enforcePermission();
        this.mKeyRemapper.remapKey(fromKey, toKey);
    }

    public void clearAllModifierKeyRemappings() {
        super.clearAllModifierKeyRemappings_enforcePermission();
        this.mKeyRemapper.clearAllKeyRemappings();
    }

    public java.util.Map<java.lang.Integer, java.lang.Integer> getModifierKeyRemapping() {
        super.getModifierKeyRemapping_enforcePermission();
        return this.mKeyRemapper.getKeyRemapping();
    }

    private java.lang.String getDeviceAlias(java.lang.String uniqueId) {
        android.bluetooth.BluetoothAdapter.checkBluetoothAddress(uniqueId);
        return null;
    }

    public void registerStickyModifierStateListener(android.hardware.input.IStickyModifierStateListener listener) {
        super.registerStickyModifierStateListener_enforcePermission();
        java.util.Objects.requireNonNull(listener);
        this.mStickyModifierStateController.registerStickyModifierStateListener(listener, android.os.Binder.getCallingPid());
    }

    public void unregisterStickyModifierStateListener(android.hardware.input.IStickyModifierStateListener listener) {
        super.unregisterStickyModifierStateListener_enforcePermission();
        java.util.Objects.requireNonNull(listener);
        this.mStickyModifierStateController.unregisterStickyModifierStateListener(listener, android.os.Binder.getCallingPid());
    }

    void notifyStickyModifierStateChanged(int modifierState, int lockedModifierState) {
        this.mStickyModifierStateController.notifyStickyModifierStateChanged(modifierState, lockedModifierState);
    }

    private final class InputManagerHandler extends android.os.Handler {
        public InputManagerHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.input.InputManagerService.this.deliverInputDevicesChanged((android.view.InputDevice[]) msg.obj);
                    break;
                case 2:
                    com.android.server.input.InputManagerService.this.reloadDeviceAliases();
                    break;
                case 3:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    long whenNanos = (((long) args.argi1) & 4294967295L) | (((long) args.argi2) << 32);
                    boolean inTabletMode = ((java.lang.Boolean) args.arg1).booleanValue();
                    com.android.server.input.InputManagerService.this.deliverTabletModeChanged(whenNanos, inTabletMode);
                    break;
            }
        }
    }

    private final class InputFilterHost extends android.view.IInputFilterHost.Stub {
        private boolean mDisconnected;

        private InputFilterHost() {
        }

        public void disconnectLocked() {
            this.mDisconnected = true;
        }

        public void sendInputEvent(android.view.InputEvent event, int policyFlags) {
            if (!com.android.server.input.InputManagerService.this.checkCallingPermission("android.permission.INJECT_EVENTS", "sendInputEvent()")) {
                throw new java.lang.SecurityException("The INJECT_EVENTS permission is required for injecting input events.");
            }
            java.util.Objects.requireNonNull(event, "event must not be null");
            synchronized (com.android.server.input.InputManagerService.this.mInputFilterLock) {
                if (!this.mDisconnected) {
                    int pid = android.os.Binder.getCallingPid();
                    com.android.server.input.InputManagerService.this.mExt.debugInputKeyInject(pid, event, "sendInputEvent");
                    com.android.server.input.InputManagerService.this.mNative.injectInputEvent(event, false, -1, 0, 0, policyFlags | 67108864);
                }
            }
        }
    }

    private final class InputMonitorHost extends android.view.IInputMonitorHost.Stub {
        private final android.os.IBinder mInputChannelToken;

        InputMonitorHost(android.os.IBinder inputChannelToken) {
            this.mInputChannelToken = inputChannelToken;
        }

        public void pilferPointers() {
            com.android.server.input.InputManagerService.this.mNative.pilferPointers(this.mInputChannelToken);
        }

        public void dispose() {
            com.android.server.input.InputManagerService.this.removeSpyWindowGestureMonitor(this.mInputChannelToken);
        }
    }

    private final class InputDevicesChangedListenerRecord implements android.os.IBinder.DeathRecipient {
        public final android.hardware.input.IInputDevicesChangedListener mListener;
        private final int mPid;
        private final int mUid;

        public InputDevicesChangedListenerRecord(int pid, android.hardware.input.IInputDevicesChangedListener listener, int uid) {
            this.mUid = uid;
            this.mPid = pid;
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.InputManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.input.InputManagerService.TAG, "Input devices changed listener for pid " + this.mPid + " died.");
            }
            com.android.server.input.InputManagerService.this.onInputDevicesChangedListenerDied(this.mPid);
            com.android.server.input.InputManagerService.this.mExt.removeProxyBinder(this.mListener.asBinder(), this.mUid);
        }

        public void notifyInputDevicesChanged(int[] info) {
            try {
                this.mListener.onInputDevicesChanged(info);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.input.InputManagerService.TAG, "Failed to notify process " + this.mPid + " that input devices changed, assuming it died.", ex);
                binderDied();
            }
        }
    }

    private final class TabletModeChangedListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.input.ITabletModeChangedListener mListener;
        private final int mPid;

        public TabletModeChangedListenerRecord(int pid, android.hardware.input.ITabletModeChangedListener listener) {
            this.mPid = pid;
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.InputManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.input.InputManagerService.TAG, "Tablet mode changed listener for pid " + this.mPid + " died.");
            }
            com.android.server.input.InputManagerService.this.onTabletModeChangedListenerDied(this.mPid);
        }

        public void notifyTabletModeChanged(long whenNanos, boolean inTabletMode) {
            try {
                this.mListener.onTabletModeChanged(whenNanos, inTabletMode);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.input.InputManagerService.TAG, "Failed to notify process " + this.mPid + " that tablet mode changed, assuming it died.", ex);
                binderDied();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSensorEventListenerDied(int pid) {
        synchronized (this.mSensorEventLock) {
            this.mSensorEventListeners.remove(pid);
        }
    }

    private final class SensorEventListenerRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.input.IInputSensorEventListener mListener;
        private final int mPid;

        SensorEventListenerRecord(int pid, android.hardware.input.IInputSensorEventListener listener) {
            this.mPid = pid;
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.InputManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.input.InputManagerService.TAG, "Sensor event listener for pid " + this.mPid + " died.");
            }
            com.android.server.input.InputManagerService.this.onSensorEventListenerDied(this.mPid);
        }

        public android.hardware.input.IInputSensorEventListener getListener() {
            return this.mListener;
        }

        public void notifySensorEvent(int deviceId, int sensorType, int accuracy, long timestamp, float[] values) {
            try {
                this.mListener.onInputSensorChanged(deviceId, sensorType, accuracy, timestamp, values);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.input.InputManagerService.TAG, "Failed to notify process " + this.mPid + " that sensor event notified, assuming it died.", ex);
                binderDied();
            }
        }

        public void notifySensorAccuracy(int deviceId, int sensorType, int accuracy) {
            try {
                this.mListener.onInputSensorAccuracyChanged(deviceId, sensorType, accuracy);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.input.InputManagerService.TAG, "Failed to notify process " + this.mPid + " that sensor accuracy notified, assuming it died.", ex);
                binderDied();
            }
        }
    }

    private final class VibratorToken implements android.os.IBinder.DeathRecipient {
        public final int mDeviceId;
        public final android.os.IBinder mToken;
        public final int mTokenValue;
        public boolean mVibrating;

        public VibratorToken(int deviceId, android.os.IBinder token, int tokenValue) {
            this.mDeviceId = deviceId;
            this.mToken = token;
            this.mTokenValue = tokenValue;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.InputManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.input.InputManagerService.TAG, "Vibrator token died.");
            }
            com.android.server.input.InputManagerService.this.onVibratorTokenDied(this);
        }
    }

    private final class LocalService extends com.android.server.input.InputManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setDisplayViewports(java.util.List<android.hardware.display.DisplayViewport> viewports) {
            com.android.server.input.InputManagerService.this.setDisplayViewportsInternal(viewports);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setInteractive(boolean interactive) {
            com.android.server.input.InputManagerService.this.mNative.setInteractive(interactive);
            com.android.server.input.InputManagerService.this.mBatteryController.onInteractiveChanged(interactive);
            com.android.server.input.InputManagerService.this.mKeyboardBacklightController.onInteractiveChanged(interactive);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void toggleCapsLock(int deviceId) {
            com.android.server.input.InputManagerService.this.mNative.toggleCapsLock(deviceId);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setPulseGestureEnabled(boolean enabled) {
            if (com.android.server.input.InputManagerService.this.mDoubleTouchGestureEnableFile != null) {
                java.io.FileWriter writer = null;
                try {
                    try {
                        writer = new java.io.FileWriter(com.android.server.input.InputManagerService.this.mDoubleTouchGestureEnableFile);
                        writer.write(enabled ? "1" : "0");
                    } catch (java.io.IOException e) {
                        android.util.Log.wtf(com.android.server.input.InputManagerService.TAG, "Unable to setPulseGestureEnabled", e);
                    }
                } finally {
                    libcore.io.IoUtils.closeQuietly(writer);
                }
            }
        }

        @Override // com.android.server.input.InputManagerInternal
        public boolean transferTouchGesture(android.os.IBinder fromChannelToken, android.os.IBinder toChannelToken) {
            return com.android.server.input.InputManagerService.this.transferTouchGesture(fromChannelToken, toChannelToken);
        }

        @Override // com.android.server.input.InputManagerInternal
        public android.graphics.PointF getCursorPosition(int displayId) {
            float[] p = com.android.server.input.InputManagerService.this.mNative.getMouseCursorPosition(displayId);
            if (p == null || p.length != 2) {
                throw new java.lang.IllegalStateException("Failed to get mouse cursor position");
            }
            return new android.graphics.PointF(p[0], p[1]);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setMousePointerAccelerationEnabled(boolean enabled, int displayId) {
            com.android.server.input.InputManagerService.this.setMousePointerAccelerationEnabled(enabled, displayId);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setDisplayEligibilityForPointerCapture(int displayId, boolean isEligible) {
            com.android.server.input.InputManagerService.this.setDisplayEligibilityForPointerCapture(displayId, isEligible);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setPointerIconVisible(boolean visible, int displayId) {
            com.android.server.input.InputManagerService.this.setPointerIconVisible(visible, displayId);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void registerLidSwitchCallback(com.android.server.input.InputManagerInternal.LidSwitchCallback callbacks) {
            com.android.server.input.InputManagerService.this.registerLidSwitchCallbackInternal(callbacks);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void unregisterLidSwitchCallback(com.android.server.input.InputManagerInternal.LidSwitchCallback callbacks) {
            com.android.server.input.InputManagerService.this.unregisterLidSwitchCallbackInternal(callbacks);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void notifyInputMethodConnectionActive(boolean connectionIsActive) {
            com.android.server.input.InputManagerService.this.mNative.setInputMethodConnectionIsActive(connectionIsActive);
        }

        @Override // com.android.server.input.InputManagerInternal
        public android.view.InputChannel createInputChannel(java.lang.String inputChannelName) {
            return com.android.server.input.InputManagerService.this.createInputChannel(inputChannelName);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void pilferPointers(android.os.IBinder token) {
            com.android.server.input.InputManagerService.this.mNative.pilferPointers(token);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void onInputMethodSubtypeChangedForKeyboardLayoutMapping(int userId, com.android.internal.inputmethod.InputMethodSubtypeHandle subtypeHandle, android.view.inputmethod.InputMethodSubtype subtype) {
            com.android.server.input.InputManagerService.this.mKeyboardLayoutManager.onInputMethodSubtypeChanged(userId, subtypeHandle, subtype);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void notifyUserActivity() {
            com.android.server.input.InputManagerService.this.mKeyboardBacklightController.notifyUserActivity();
        }

        @Override // com.android.server.input.InputManagerInternal
        public void incrementKeyboardBacklight(int deviceId) {
            com.android.server.input.InputManagerService.this.mKeyboardBacklightController.incrementKeyboardBacklight(deviceId);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void decrementKeyboardBacklight(int deviceId) {
            com.android.server.input.InputManagerService.this.mKeyboardBacklightController.decrementKeyboardBacklight(deviceId);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setTypeAssociation(java.lang.String inputPort, java.lang.String type) {
            com.android.server.input.InputManagerService.this.setTypeAssociationInternal(inputPort, type);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void unsetTypeAssociation(java.lang.String inputPort) {
            com.android.server.input.InputManagerService.this.unsetTypeAssociationInternal(inputPort);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void addKeyboardLayoutAssociation(java.lang.String inputPort, java.lang.String languageTag, java.lang.String layoutType) {
            com.android.server.input.InputManagerService.this.addKeyboardLayoutAssociation(inputPort, languageTag, layoutType);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void removeKeyboardLayoutAssociation(java.lang.String inputPort) {
            com.android.server.input.InputManagerService.this.removeKeyboardLayoutAssociation(inputPort);
        }

        @Override // com.android.server.input.InputManagerInternal
        public void setStylusButtonMotionEventsEnabled(boolean enabled) {
            com.android.server.input.InputManagerService.this.mNative.setStylusButtonMotionEventsEnabled(enabled);
        }

        @Override // com.android.server.input.InputManagerInternal
        public int getLastUsedInputDeviceId() {
            return com.android.server.input.InputManagerService.this.mNative.getLastUsedInputDeviceId();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.input.InputShellCommand().exec(this, in, out, err, args, callback, resultReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class AdditionalDisplayInputProperties {
        static final boolean DEFAULT_MOUSE_POINTER_ACCELERATION_ENABLED = true;
        static final boolean DEFAULT_POINTER_ICON_VISIBLE = true;
        public boolean mousePointerAccelerationEnabled;
        public boolean pointerIconVisible;

        AdditionalDisplayInputProperties() {
            reset();
        }

        public boolean allDefaults() {
            return this.mousePointerAccelerationEnabled && this.pointerIconVisible;
        }

        public void reset() {
            this.mousePointerAccelerationEnabled = true;
            this.pointerIconVisible = true;
        }
    }

    private void updateAdditionalDisplayInputProperties(int displayId, java.util.function.Consumer<com.android.server.input.InputManagerService.AdditionalDisplayInputProperties> updater) {
        synchronized (this.mAdditionalDisplayInputPropertiesLock) {
            com.android.server.input.InputManagerService.AdditionalDisplayInputProperties properties = this.mAdditionalDisplayInputProperties.get(displayId);
            if (properties == null) {
                properties = new com.android.server.input.InputManagerService.AdditionalDisplayInputProperties();
                this.mAdditionalDisplayInputProperties.put(displayId, properties);
            }
            boolean oldPointerIconVisible = properties.pointerIconVisible;
            boolean oldMouseAccelerationEnabled = properties.mousePointerAccelerationEnabled;
            updater.accept(properties);
            if (oldPointerIconVisible != properties.pointerIconVisible) {
                this.mNative.setPointerIconVisibility(displayId, properties.pointerIconVisible);
            }
            if (oldMouseAccelerationEnabled != properties.mousePointerAccelerationEnabled) {
                this.mNative.setMousePointerAccelerationEnabled(displayId, properties.mousePointerAccelerationEnabled);
            }
            if (properties.allDefaults()) {
                this.mAdditionalDisplayInputProperties.remove(displayId);
            }
        }
    }

    void updatePointerLocationEnabled(boolean enabled) {
        this.mWindowManagerCallbacks.notifyPointerLocationChanged(enabled);
    }

    void updateShowKeyPresses(boolean enabled) {
        if (this.mShowKeyPresses == enabled) {
            return;
        }
        this.mShowKeyPresses = enabled;
        updateFocusEventDebugViewEnabled();
        synchronized (this.mFocusEventDebugViewLock) {
            if (this.mFocusEventDebugView != null) {
                this.mFocusEventDebugView.updateShowKeyPresses(enabled);
            }
        }
    }

    void updateShowRotaryInput(boolean enabled) {
        if (this.mShowRotaryInput == enabled) {
            return;
        }
        this.mShowRotaryInput = enabled;
        updateFocusEventDebugViewEnabled();
        synchronized (this.mFocusEventDebugViewLock) {
            if (this.mFocusEventDebugView != null) {
                this.mFocusEventDebugView.updateShowRotaryInput(enabled);
            }
        }
    }

    private void updateFocusEventDebugViewEnabled() {
        com.android.server.input.debug.FocusEventDebugView view;
        boolean enabled = this.mShowKeyPresses || this.mShowRotaryInput;
        synchronized (this.mFocusEventDebugViewLock) {
            if (enabled == (this.mFocusEventDebugView != null)) {
                return;
            }
            if (enabled) {
                this.mFocusEventDebugView = new com.android.server.input.debug.FocusEventDebugView(this.mContext, this);
                view = this.mFocusEventDebugView;
            } else {
                view = this.mFocusEventDebugView;
                this.mFocusEventDebugView = null;
            }
            java.util.Objects.requireNonNull(view);
            android.view.WindowManager wm = (android.view.WindowManager) java.util.Objects.requireNonNull((android.view.WindowManager) this.mContext.getSystemService(android.view.WindowManager.class));
            if (!enabled) {
                wm.removeView(view);
                return;
            }
            android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams();
            lp.type = 2015;
            lp.flags = 280;
            lp.privateFlags |= 16;
            lp.setFitInsetsTypes(0);
            lp.layoutInDisplayCutoutMode = 3;
            lp.format = -3;
            lp.setTitle("FocusEventDebugView - display " + this.mContext.getDisplayId());
            lp.inputFeatures |= 1;
            wm.addView(view, lp);
        }
    }

    public void setAccessibilityBounceKeysThreshold(int thresholdTimeMs) {
        this.mNative.setAccessibilityBounceKeysThreshold(thresholdTimeMs);
    }

    public void setAccessibilitySlowKeysThreshold(int thresholdTimeMs) {
        this.mNative.setAccessibilitySlowKeysThreshold(thresholdTimeMs);
    }

    public void setAccessibilityStickyKeysEnabled(boolean enabled) {
        this.mNative.setAccessibilityStickyKeysEnabled(enabled);
    }

    void setUseLargePointerIcons(boolean useLargeIcons) {
        this.mPointerIconCache.setUseLargePointerIcons(useLargeIcons);
    }

    private void onPointerDownOutsideFocusWithXY(android.os.IBinder touchedToken, float x, float y) {
        if (!((com.android.server.wm.IZoomWindowManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IZoomWindowManagerExt.class).create()).checkInSideGestureHotZone(x, y)) {
            this.mWindowManagerCallbacks.onPointerDownOutsideFocus(touchedToken);
        }
    }

    private void notifyInputDispatcherThread(int tid) {
        this.mExt.notifyInputDispatcherThread(tid);
    }

    private void inputCancelFromNative(int uid) {
        this.mExt.inputCancelFromNative(uid);
    }

    private void notifyInputJitter(java.lang.String info) {
        this.mExt.notifyInputJitter(info);
    }

    private void sendLaserDelta(float deltaX, float deltaY) {
        this.mExt.sendLaserDelta(deltaX, deltaY);
    }

    private void interceptNotifyMotion(int action, java.lang.String toolType) {
        this.mExt.interceptNotifyMotion(action, toolType);
    }

    void fadePointer(int displayId) {
        this.mNative.fadePointer(displayId);
    }

    void setNotifyEnable(boolean enabled) {
        this.mNative.setNotifyEnabled(enabled);
    }

    void setLaserEnabled(boolean enabled) {
        this.mNative.setLaserEnabled(enabled);
    }

    public com.android.server.input.IInputManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class InputManagerServiceWrapper implements com.android.server.input.IInputManagerServiceWrapper {
        private InputManagerServiceWrapper() {
        }

        @Override // com.android.server.input.IInputManagerServiceWrapper
        public com.android.server.input.NativeInputManagerService getNative() {
            return com.android.server.input.InputManagerService.this.mNative;
        }

        @Override // com.android.server.input.IInputManagerServiceWrapper
        public com.android.server.input.IInputManagerServiceExt getExtImpl() {
            return com.android.server.input.InputManagerService.this.mExt;
        }

        @Override // com.android.server.input.IInputManagerServiceWrapper
        public java.lang.Object getInputFilterLock() {
            return com.android.server.input.InputManagerService.this.mInputFilterLock;
        }

        @Override // com.android.server.input.IInputManagerServiceWrapper
        public com.android.server.input.InputManagerService.WindowManagerCallbacks getWindowManagerCallbacks() {
            return com.android.server.input.InputManagerService.this.mWindowManagerCallbacks;
        }
    }

    void setPointerFillStyle(int fillStyle) {
        this.mPointerIconCache.setPointerFillStyle(fillStyle);
    }

    void setPointerScale(float scale) {
        this.mPointerIconCache.setPointerScale(scale);
    }

    interface KeyboardBacklightControllerInterface {
        default void incrementKeyboardBacklight(int deviceId) {
        }

        default void decrementKeyboardBacklight(int deviceId) {
        }

        default void registerKeyboardBacklightListener(android.hardware.input.IKeyboardBacklightListener l, int pid) {
        }

        default void unregisterKeyboardBacklightListener(android.hardware.input.IKeyboardBacklightListener l, int pid) {
        }

        default void onInteractiveChanged(boolean isInteractive) {
        }

        default void notifyUserActivity() {
        }

        default void systemRunning() {
        }

        default void dump(java.io.PrintWriter pw) {
        }
    }
}
