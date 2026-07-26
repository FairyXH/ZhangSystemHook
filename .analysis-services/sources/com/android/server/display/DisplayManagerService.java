package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class DisplayManagerService extends com.android.server.SystemService {
    static final long DISPLAY_MODE_RETURNS_PHYSICAL_REFRESH_RATE = 170503758;
    private static final java.lang.String FORCE_WIFI_DISPLAY_ENABLE = "persist.debug.wfd.enable";
    private static final int MAX_ENTRIES = 100;
    private static final int MSG_DELIVER_DISPLAY_EVENT = 3;
    private static final int MSG_DELIVER_DISPLAY_EVENT_FRAME_RATE_OVERRIDE = 7;
    private static final int MSG_DELIVER_DISPLAY_GROUP_EVENT = 8;
    private static final int MSG_LOAD_BRIGHTNESS_CONFIGURATIONS = 6;
    private static final int MSG_RECEIVED_DEVICE_STATE = 9;
    private static final int MSG_REGISTER_ADDITIONAL_DISPLAY_ADAPTERS = 2;
    private static final int MSG_REGISTER_DEFAULT_DISPLAY_ADAPTERS = 1;
    private static final int MSG_REQUEST_TRAVERSAL = 4;
    private static final int MSG_UPDATE_VIEWPORT = 5;
    private static final java.lang.String PROP_DEFAULT_DISPLAY_TOP_INSET = "persist.sys.displayinset.top";
    private static final java.lang.String TAG = "DisplayManagerService";
    private static final float THRESHOLD_FOR_REFRESH_RATES_DIVISORS = 9.0E-4f;
    private static final long WAIT_FOR_DEFAULT_DISPLAY_TIMEOUT = 10000;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private boolean mAreUserDisabledHdrTypesAllowed;
    private final java.util.LinkedHashMap<java.lang.Integer, java.lang.Long> mBinderDiedPids;
    private boolean mBootCompleted;
    private final com.android.internal.display.BrightnessSynchronizer mBrightnessSynchronizer;
    private com.android.server.display.BrightnessTracker mBrightnessTracker;
    private final android.util.SparseArray<com.android.server.display.DisplayManagerService.CallbackRecord> mCallbacks;
    private final com.android.server.display.feature.DeviceConfigParameterProvider mConfigParameterProvider;
    private final android.content.Context mContext;
    private int mCurrentUserId;
    public com.android.server.display.IOplusDisplayManagerServiceEx mDMSEx;
    private final int mDefaultDisplayDefaultColorMode;
    private int mDefaultDisplayTopInset;
    private final int mDefaultHdrConversionMode;
    private android.hardware.devicestate.DeviceStateManagerInternal mDeviceStateManager;
    private final android.util.SparseArray<android.util.IntArray> mDisplayAccessUIDs;
    private final java.util.ArrayList<com.android.server.display.DisplayAdapter> mDisplayAdapters;
    private final com.android.server.display.DisplayBlanker mDisplayBlanker;
    private final android.util.SparseArray<com.android.server.display.DisplayManagerService.BrightnessPair> mDisplayBrightnesses;
    private final com.android.server.display.mode.DisplayModeDirector.DisplayDeviceConfigProvider mDisplayDeviceConfigProvider;
    private final com.android.server.display.DisplayDeviceRepository mDisplayDeviceRepo;
    private final java.util.concurrent.CopyOnWriteArrayList<android.hardware.display.DisplayManagerInternal.DisplayGroupListener> mDisplayGroupListeners;
    private final com.android.server.display.mode.DisplayModeDirector mDisplayModeDirector;
    private final com.android.server.display.notifications.DisplayNotificationManager mDisplayNotificationManager;
    private android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks mDisplayPowerCallbacks;
    private final android.util.SparseArray<com.android.server.display.DisplayPowerController> mDisplayPowerControllers;
    private final android.util.SparseIntArray mDisplayStates;
    private final java.util.concurrent.CopyOnWriteArrayList<android.hardware.display.DisplayManagerInternal.DisplayTransactionListener> mDisplayTransactionListeners;
    final android.util.SparseArray<android.util.Pair<android.companion.virtual.IVirtualDevice, android.window.DisplayWindowPolicyController>> mDisplayWindowPolicyControllers;
    public com.android.server.display.IDisplayManagerServiceExt mDmsExt;
    private com.android.server.display.DisplayManagerService.DisplayManagerServiceWrapper mDmsWrapper;
    private boolean mDumpInProgress;
    private final com.android.server.display.ExternalDisplayPolicy mExternalDisplayPolicy;
    private final com.android.server.display.ExternalDisplayStatsService mExternalDisplayStatsService;
    private final boolean mExtraDisplayEventLogging;
    private final java.lang.String mExtraDisplayLoggingPackageName;
    private final com.android.server.display.feature.DisplayManagerFlags mFlags;
    private final com.android.server.display.DisplayManagerService.DisplayManagerHandler mHandler;
    private android.hardware.display.HdrConversionMode mHdrConversionMode;
    private final com.android.server.display.HighBrightnessModeMetadataMapper mHighBrightnessModeMetadataMapper;
    private final android.content.BroadcastReceiver mIdleModeReceiver;
    private final com.android.server.display.DisplayManagerService.Injector mInjector;
    private com.android.server.input.InputManagerInternal mInputManagerInternal;
    private boolean mIsDocked;
    private boolean mIsDreaming;
    private volatile boolean mIsHdrOutputControlEnabled;
    private final com.android.server.display.LogicalDisplayMapper mLogicalDisplayMapper;
    private boolean mMinimalPostProcessingAllowed;
    private final android.hardware.display.Curve mMinimumBrightnessCurve;
    private final android.util.Spline mMinimumBrightnessSpline;
    private final android.hardware.OverlayProperties mOverlayProperties;
    private android.hardware.display.HdrConversionMode mOverrideHdrConversionMode;
    private final android.util.SparseArray<android.util.SparseArray<com.android.server.display.DisplayManagerService.PendingCallback>> mPendingCallbackSelfLocked;
    private boolean mPendingTraversal;
    private boolean mPendingTraversalCompleted;
    private final com.android.server.display.PersistentDataStore mPersistentDataStore;
    private android.os.Handler mPowerHandler;
    private android.media.projection.IMediaProjectionManager mProjectionService;
    private final android.content.BroadcastReceiver mResolutionRestoreReceiver;
    public boolean mSafeMode;
    private android.hardware.SensorManager mSensorManager;
    private com.android.server.display.DisplayManagerService.SettingsObserver mSettingsObserver;
    private com.android.server.display.SmallAreaDetectionController mSmallAreaDetectionController;
    private android.graphics.Point mStableDisplaySize;
    private int[] mSupportedHdrOutputType;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncDump;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncRoot;
    private int mSystemPreferredHdrOutputType;
    private boolean mSystemReady;
    private final java.util.ArrayList<com.android.server.display.DisplayManagerService.CallbackRecord> mTempCallbacks;
    private final java.util.ArrayList<android.hardware.display.DisplayViewport> mTempViewports;
    private final android.os.Handler mUiHandler;
    private final com.android.server.display.DisplayManagerService.UidImportanceListener mUidImportanceListener;
    private int[] mUserDisabledHdrTypes;
    private android.view.Display.Mode mUserPreferredMode;
    private final java.util.ArrayList<android.hardware.display.DisplayViewport> mViewports;
    private com.android.server.display.VirtualDisplayAdapter mVirtualDisplayAdapter;
    private final android.graphics.ColorSpace mWideColorSpace;
    private com.android.server.display.WifiDisplayAdapter mWifiDisplayAdapter;
    private int mWifiDisplayScanRequestCount;
    private com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private static final boolean PANIC_DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean DEBUG = false;
    private static final int[] EMPTY_ARRAY = new int[0];
    private static final android.hardware.display.HdrConversionMode HDR_CONVERSION_MODE_UNSUPPORTED = new android.hardware.display.HdrConversionMode(0);

    public interface Clock {
        long uptimeMillis();
    }

    public static final class SyncRoot {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.display.DisplayDeviceConfig lambda$new$0(int displayId) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayDevice device = getDeviceForDisplayLocked(displayId);
            if (device == null) {
                return null;
            }
            return device.getDisplayDeviceConfig();
        }
    }

    public DisplayManagerService(android.content.Context context) {
        this(context, new com.android.server.display.DisplayManagerService.Injector());
    }

    /* JADX WARN: Multi-variable type inference failed */
    DisplayManagerService(android.content.Context context, com.android.server.display.DisplayManagerService.Injector injector) {
        int i;
        super(context);
        this.mUidImportanceListener = new com.android.server.display.DisplayManagerService.UidImportanceListener();
        this.mUserDisabledHdrTypes = new int[0];
        this.mAreUserDisabledHdrTypesAllowed = true;
        this.mHdrConversionMode = null;
        this.mOverrideHdrConversionMode = null;
        this.mSystemPreferredHdrOutputType = -1;
        this.mSyncRoot = new com.android.server.display.DisplayManagerService.SyncRoot();
        this.mBinderDiedPids = new java.util.LinkedHashMap<java.lang.Integer, java.lang.Long>() { // from class: com.android.server.display.DisplayManagerService.1
            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(java.util.Map.Entry<java.lang.Integer, java.lang.Long> eldest) {
                return size() > 100;
            }
        };
        this.mCallbacks = new android.util.SparseArray<>();
        this.mDisplayWindowPolicyControllers = new android.util.SparseArray<>();
        this.mHighBrightnessModeMetadataMapper = new com.android.server.display.HighBrightnessModeMetadataMapper();
        this.mDisplayAdapters = new java.util.ArrayList<>();
        this.mDisplayTransactionListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mDisplayGroupListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        this.mDisplayPowerControllers = new android.util.SparseArray<>();
        this.mDisplayBlanker = new com.android.server.display.DisplayBlanker() { // from class: com.android.server.display.DisplayManagerService.2
            @Override // com.android.server.display.DisplayBlanker
            public synchronized void requestDisplayState(int displayId, int state, float brightness, float sdrBrightness) {
                boolean stateChanged;
                int index;
                boolean allInactive = true;
                boolean allOff = true;
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    try {
                        int index2 = com.android.server.display.DisplayManagerService.this.mDisplayStates.indexOfKey(displayId);
                        int i2 = 1;
                        if (index2 > -1) {
                            int currentState = com.android.server.display.DisplayManagerService.this.mDisplayStates.valueAt(index2);
                            stateChanged = state != currentState;
                            if (stateChanged) {
                                int size = com.android.server.display.DisplayManagerService.this.mDisplayStates.size();
                                int i3 = 0;
                                while (i3 < size) {
                                    int displayState = i3 == index2 ? state : com.android.server.display.DisplayManagerService.this.mDisplayStates.valueAt(i3);
                                    if (displayState == i2) {
                                        index = index2;
                                    } else {
                                        index = index2;
                                        allOff = com.android.server.display.DisplayManagerService.this.mDmsExt.onDisplayStateChange(state, displayState, com.android.server.display.DisplayManagerService.this.mDisplayStates.keyAt(i3), com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper);
                                    }
                                    if (android.view.Display.isActiveState(displayState)) {
                                        allInactive = false;
                                    }
                                    if (!allOff && !allInactive) {
                                        break;
                                    }
                                    i3++;
                                    index2 = index;
                                    i2 = 1;
                                }
                            }
                        } else {
                            stateChanged = false;
                        }
                    } finally {
                        th = th;
                        while (true) {
                            try {
                            } catch (java.lang.Throwable th) {
                                th = th;
                            }
                        }
                    }
                }
                if (state == 1) {
                    com.android.server.display.DisplayManagerService.this.requestDisplayStateInternal(displayId, state, brightness, sdrBrightness);
                }
                if (stateChanged) {
                    com.android.server.display.DisplayManagerService.this.mDisplayPowerCallbacks.onDisplayStateChange(allInactive, allOff);
                }
                if (state != 1) {
                    com.android.server.display.DisplayManagerService.this.requestDisplayStateInternal(displayId, state, brightness, sdrBrightness);
                }
            }
        };
        this.mDisplayStates = new android.util.SparseIntArray();
        this.mDisplayBrightnesses = new android.util.SparseArray<>();
        this.mStableDisplaySize = new android.graphics.Point();
        this.mViewports = new java.util.ArrayList<>();
        this.mPersistentDataStore = new com.android.server.display.PersistentDataStore();
        this.mTempCallbacks = new java.util.ArrayList<>();
        this.mPendingCallbackSelfLocked = new android.util.SparseArray<>();
        this.mTempViewports = new java.util.ArrayList<>();
        this.mDisplayAccessUIDs = new android.util.SparseArray<>();
        this.mBootCompleted = false;
        this.mIdleModeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.display.DisplayManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.intent.action.DOCK_EVENT".equals(intent.getAction())) {
                    int dockState = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
                    com.android.server.display.DisplayManagerService.this.mIsDocked = dockState == 1 || dockState == 3 || dockState == 4;
                }
                if ("android.intent.action.DREAMING_STARTED".equals(intent.getAction())) {
                    com.android.server.display.DisplayManagerService.this.mIsDreaming = true;
                } else if ("android.intent.action.DREAMING_STOPPED".equals(intent.getAction())) {
                    com.android.server.display.DisplayManagerService.this.mIsDreaming = false;
                }
                com.android.server.display.DisplayManagerService.this.setDockedAndIdleEnabled(com.android.server.display.DisplayManagerService.this.mIsDocked && com.android.server.display.DisplayManagerService.this.mIsDreaming, 0);
            }
        };
        this.mResolutionRestoreReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.display.DisplayManagerService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                if ("android.os.action.SETTING_RESTORED".equals(intent.getAction()) && "screen_resolution_mode".equals(intent.getExtra("setting_name"))) {
                    com.android.server.display.DisplayManagerService.this.restoreResolutionFromBackup();
                }
            }
        };
        this.mDisplayDeviceConfigProvider = new com.android.server.display.mode.DisplayModeDirector.DisplayDeviceConfigProvider() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda11
            @Override // com.android.server.display.mode.DisplayModeDirector.DisplayDeviceConfigProvider
            public final com.android.server.display.DisplayDeviceConfig getDisplayDeviceConfig(int i2) {
                return this.f$0.lambda$new$0(i2);
            }
        };
        this.mSyncDump = new com.android.server.display.DisplayManagerService.SyncRoot();
        this.mDmsExt = (com.android.server.display.IDisplayManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDisplayManagerServiceExt.class).base(this).create();
        this.mDMSEx = null;
        this.mPendingTraversalCompleted = true;
        this.mDmsWrapper = new com.android.server.display.DisplayManagerService.DisplayManagerServiceWrapper();
        com.android.server.utils.FoldSettingProvider foldSettingProvider = new com.android.server.utils.FoldSettingProvider(context, new com.android.internal.util.SettingsWrapper(), new com.android.internal.foldables.FoldLockSettingAvailabilityProvider(context.getResources()));
        this.mInjector = injector;
        this.mContext = context;
        this.mFlags = injector.getFlags();
        this.mHandler = new com.android.server.display.DisplayManagerService.DisplayManagerHandler(com.android.server.DisplayThread.get().getLooper());
        this.mUiHandler = com.android.server.UiThread.getHandler();
        this.mDisplayDeviceRepo = new com.android.server.display.DisplayDeviceRepository(this.mSyncRoot, this.mPersistentDataStore);
        this.mLogicalDisplayMapper = new com.android.server.display.LogicalDisplayMapper(this.mContext, foldSettingProvider, new com.android.internal.foldables.FoldGracePeriodProvider(), this.mDisplayDeviceRepo, new com.android.server.display.DisplayManagerService.LogicalDisplayListener(), this.mSyncRoot, this.mHandler, this.mFlags);
        this.mDisplayModeDirector = new com.android.server.display.mode.DisplayModeDirector(context, this.mHandler, this.mFlags, this.mDisplayDeviceConfigProvider);
        this.mBrightnessSynchronizer = new com.android.internal.display.BrightnessSynchronizer(this.mContext, this.mFlags.isBrightnessIntRangeUserPerceptionEnabled());
        android.content.res.Resources resources = this.mContext.getResources();
        this.mDefaultDisplayDefaultColorMode = this.mContext.getResources().getInteger(android.R.integer.config_defaultActionModeHideDurationMillis);
        this.mDefaultDisplayTopInset = android.os.SystemProperties.getInt(PROP_DEFAULT_DISPLAY_TOP_INSET, -1);
        if (this.mContext.getResources().getBoolean(android.R.bool.config_enableCarDockHomeLaunch)) {
            i = 1;
        } else {
            i = 2;
        }
        this.mDefaultHdrConversionMode = i;
        float[] floatArray = getFloatArray(resources.obtainTypedArray(android.R.array.config_mainBuiltInDisplayWaterfallCutout));
        float[] floatArray2 = getFloatArray(resources.obtainTypedArray(android.R.array.config_mappedColorModes));
        this.mMinimumBrightnessCurve = new android.hardware.display.Curve(floatArray, floatArray2);
        this.mMinimumBrightnessSpline = android.util.Spline.createSpline(floatArray, floatArray2);
        this.mCurrentUserId = 0;
        this.mWideColorSpace = android.view.SurfaceControl.getCompositionColorSpaces()[1];
        this.mOverlayProperties = android.view.SurfaceControl.getOverlaySupport();
        this.mSystemReady = false;
        this.mConfigParameterProvider = new com.android.server.display.feature.DeviceConfigParameterProvider(android.provider.DeviceConfigInterface.REAL);
        this.mExtraDisplayLoggingPackageName = (java.lang.String) android.sysprop.DisplayProperties.debug_vri_package().orElse(null);
        this.mExtraDisplayEventLogging = true ^ android.text.TextUtils.isEmpty(this.mExtraDisplayLoggingPackageName);
        this.mExternalDisplayStatsService = new com.android.server.display.ExternalDisplayStatsService(this.mContext, this.mHandler);
        this.mDisplayNotificationManager = new com.android.server.display.notifications.DisplayNotificationManager(this.mFlags, this.mContext, this.mExternalDisplayStatsService);
        this.mExternalDisplayPolicy = new com.android.server.display.ExternalDisplayPolicy(new com.android.server.display.DisplayManagerService.ExternalDisplayPolicyInjector());
        this.mDumpInProgress = false;
        this.mDmsExt.init(context);
        this.mDmsExt.setUiHandler(this.mUiHandler);
        this.mDmsExt.setLogicalDisplayMapper(this.mLogicalDisplayMapper);
        this.mDMSEx = (com.android.server.display.IOplusDisplayManagerServiceEx) com.android.server.OplusServiceFactory.getInstance().getFeature(com.android.server.display.IOplusDisplayManagerServiceEx.DEFAULT, new java.lang.Object[]{getContext(), this});
    }

    public void setupSchedulerPolicies() {
        android.os.Process.setThreadGroupAndCpuset(com.android.server.DisplayThread.get().getThreadId(), 5);
        android.os.Process.setThreadGroupAndCpuset(com.android.server.AnimationThread.get().getThreadId(), 5);
        android.os.Process.setThreadGroupAndCpuset(com.android.server.wm.SurfaceAnimationThread.get().getThreadId(), 5);
        resetThreadGroup();
    }

    private void resetThreadGroup() {
        this.mDmsExt.setThreadSchedPolicy(com.android.server.DisplayThread.get().getThreadId(), com.android.server.DisplayThread.get().getName(), 14);
        this.mDmsExt.setThreadSchedPolicy(com.android.server.AnimationThread.get().getThreadId(), com.android.server.AnimationThread.get().getName(), 14);
        this.mDmsExt.setThreadSchedPolicy(com.android.server.wm.SurfaceAnimationThread.get().getThreadId(), com.android.server.wm.SurfaceAnimationThread.get().getName(), 14);
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
        synchronized (this.mSyncRoot) {
            this.mPersistentDataStore.loadIfNeeded();
            loadStableDisplayValuesLocked();
        }
        this.mHandler.sendEmptyMessage(1);
        android.hardware.display.DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
        android.hardware.display.IDisplayManager.Stub binderService = new com.android.server.display.DisplayManagerService.BinderService();
        this.mDmsExt.onStart(binderService);
        publishBinderService("display", binderService, true);
        publishLocalService(android.hardware.display.DisplayManagerInternal.class, new com.android.server.display.DisplayManagerService.LocalService());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 100) {
            synchronized (this.mSyncRoot) {
                long timeout = android.os.SystemClock.uptimeMillis() + this.mInjector.getDefaultDisplayDelayTimeout();
                while (true) {
                    if (this.mLogicalDisplayMapper.getDisplayLocked(0) != null && this.mVirtualDisplayAdapter != null) {
                    }
                    long delay = timeout - android.os.SystemClock.uptimeMillis();
                    if (delay <= 0) {
                        throw new java.lang.RuntimeException("Timeout waiting for default display to be initialized. DefaultDisplay=" + this.mLogicalDisplayMapper.getDisplayLocked(0) + ", mVirtualDisplayAdapter=" + this.mVirtualDisplayAdapter);
                    }
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "waitForDefaultDisplay: waiting, timeout=" + delay);
                    }
                    try {
                        this.mSyncRoot.wait(delay);
                    } catch (java.lang.InterruptedException e) {
                    }
                }
            }
            break;
        }
        if (phase == 1000) {
            synchronized (this.mSyncRoot) {
                this.mBootCompleted = true;
                for (int i = 0; i < this.mDisplayPowerControllers.size(); i++) {
                    this.mDisplayPowerControllers.valueAt(i).onBootCompleted();
                }
            }
            this.mDisplayModeDirector.onBootCompleted();
            this.mLogicalDisplayMapper.onBootCompleted();
            this.mDisplayNotificationManager.onBootCompleted();
            this.mExternalDisplayPolicy.onBootCompleted();
        }
        this.mDmsExt.onBootComplete(phase, this.mDisplayPowerControllers.get(0), this.mSyncRoot);
        com.android.server.policy.DeviceStateProviderImpl.sExtImpl.onBootPhase(phase);
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        final int newUserId = to.getUserIdentifier();
        final int userSerial = getUserManager().getUserSerialNumber(newUserId);
        synchronized (this.mSyncRoot) {
            final boolean userSwitching = this.mCurrentUserId != newUserId;
            if (userSwitching) {
                this.mCurrentUserId = newUserId;
            }
            this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda16
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onUserSwitching$1(userSwitching, userSerial, newUserId, (com.android.server.display.LogicalDisplay) obj);
                }
            });
            handleSettingsChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUserSwitching$1(boolean userSwitching, int userSerial, int newUserId, com.android.server.display.LogicalDisplay logicalDisplay) {
        com.android.server.display.DisplayPowerController dpc;
        if (logicalDisplay.getDisplayInfoLocked().type != 1 || (dpc = this.mDisplayPowerControllers.get(logicalDisplay.getDisplayIdLocked())) == null) {
            return;
        }
        if (userSwitching) {
            getBrightnessConfigForDisplayWithPdsFallbackLocked(logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId(), userSerial);
            android.util.Slog.w(TAG, "disconnect WFD when switch user by feature");
            disconnectWifiDisplayInternal();
        }
        float newBrightness = dpc.getBrightnessInfo().adjustedBrightness;
        if (java.lang.Float.isNaN(newBrightness)) {
            newBrightness = logicalDisplay.getDisplayInfoLocked().brightnessDefault;
        }
        dpc.onSwitchUser(newUserId, userSerial, newBrightness);
    }

    public void windowManagerAndInputReady() {
        synchronized (this.mSyncRoot) {
            this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
            this.mInputManagerInternal = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
            this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            android.app.ActivityManager activityManager = (android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class);
            activityManager.addOnUidImportanceListener(this.mUidImportanceListener, 400);
            this.mDeviceStateManager = (android.hardware.devicestate.DeviceStateManagerInternal) com.android.server.LocalServices.getService(android.hardware.devicestate.DeviceStateManagerInternal.class);
            ((android.hardware.devicestate.DeviceStateManager) this.mContext.getSystemService(android.hardware.devicestate.DeviceStateManager.class)).registerCallback(new android.os.HandlerExecutor(this.mHandler), new com.android.server.display.DisplayManagerService.DeviceStateListener());
            this.mLogicalDisplayMapper.onWindowManagerReady();
            scheduleTraversalLocked(false);
        }
    }

    public void systemReady(boolean safeMode) {
        synchronized (this.mSyncRoot) {
            this.mSafeMode = safeMode;
            this.mSystemReady = true;
            this.mIsHdrOutputControlEnabled = this.mConfigParameterProvider.isHdrOutputControlFeatureEnabled();
            this.mConfigParameterProvider.addOnPropertiesChangedListener(com.android.internal.os.BackgroundThread.getExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda8
                public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                    this.f$0.lambda$systemReady$2(properties);
                }
            });
            recordTopInsetLocked(this.mLogicalDisplayMapper.getDisplayLocked(0));
            updateSettingsLocked();
            updateUserDisabledHdrTypesFromSettingsLocked();
            updateUserPreferredDisplayModeSettingsLocked();
            if (this.mIsHdrOutputControlEnabled) {
                updateHdrConversionModeSettingsLocked();
            }
        }
        this.mDisplayModeDirector.setDesiredDisplayModeSpecsListener(new com.android.server.display.DisplayManagerService.DesiredDisplayModeSpecsObserver());
        this.mDisplayModeDirector.start(this.mSensorManager);
        this.mHandler.sendEmptyMessage(2);
        this.mSettingsObserver = new com.android.server.display.DisplayManagerService.SettingsObserver();
        this.mBrightnessSynchronizer.startSynchronizing();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.DREAMING_STARTED");
        filter.addAction("android.intent.action.DREAMING_STOPPED");
        filter.addAction("android.intent.action.DOCK_EVENT");
        this.mContext.registerReceiver(this.mIdleModeReceiver, filter);
        this.mDmsExt.onSystemReady();
        this.mDMSEx.systemReady();
        if (this.mFlags.isResolutionBackupRestoreEnabled()) {
            android.content.IntentFilter restoreFilter = new android.content.IntentFilter("android.os.action.SETTING_RESTORED");
            this.mContext.registerReceiver(this.mResolutionRestoreReceiver, restoreFilter);
        }
        this.mSmallAreaDetectionController = this.mFlags.isSmallAreaDetectionEnabled() ? com.android.server.display.SmallAreaDetectionController.create(this.mContext) : null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$2(android.provider.DeviceConfig.Properties properties) {
        this.mIsHdrOutputControlEnabled = this.mConfigParameterProvider.isHdrOutputControlFeatureEnabled();
    }

    android.os.Handler getDisplayHandler() {
        return this.mHandler;
    }

    com.android.server.display.DisplayDeviceRepository getDisplayDeviceRepository() {
        return this.mDisplayDeviceRepo;
    }

    com.android.server.display.LogicalDisplayMapper getLogicalDisplayMapper() {
        return this.mLogicalDisplayMapper;
    }

    boolean isMinimalPostProcessingAllowed() {
        boolean z;
        synchronized (this.mSyncRoot) {
            z = this.mMinimalPostProcessingAllowed;
        }
        return z;
    }

    void setMinimalPostProcessingAllowed(boolean allowed) {
        synchronized (this.mSyncRoot) {
            this.mMinimalPostProcessingAllowed = allowed;
        }
    }

    com.android.server.display.notifications.DisplayNotificationManager getDisplayNotificationManager() {
        return this.mDisplayNotificationManager;
    }

    private void loadStableDisplayValuesLocked() {
        android.graphics.Point size = this.mPersistentDataStore.getStableDisplaySize();
        if (size.x > 0 && size.y > 0) {
            this.mStableDisplaySize.set(size.x, size.y);
            return;
        }
        android.content.res.Resources res = this.mContext.getResources();
        int width = res.getInteger(android.R.integer.config_screen_rotation_color_transition);
        int height = res.getInteger(android.R.integer.config_screen_magnification_multi_tap_adjustment);
        if (width > 0 && height > 0) {
            setStableDisplaySizeLocked(width, height);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.graphics.Point getStableDisplaySizeInternal() {
        android.graphics.Point r = new android.graphics.Point();
        synchronized (this.mSyncRoot) {
            if (this.mStableDisplaySize.x > 0 && this.mStableDisplaySize.y > 0) {
                r.set(this.mStableDisplaySize.x, this.mStableDisplaySize.y);
            }
        }
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDisplayTransactionListenerInternal(android.hardware.display.DisplayManagerInternal.DisplayTransactionListener listener) {
        this.mDisplayTransactionListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDisplayTransactionListenerInternal(android.hardware.display.DisplayManagerInternal.DisplayTransactionListener listener) {
        this.mDisplayTransactionListeners.remove(listener);
    }

    void setDisplayInfoOverrideFromWindowManagerInternal(int displayId, android.view.DisplayInfo info) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display != null && display.setDisplayInfoOverrideFromWindowManagerLocked(info)) {
                handleLogicalDisplayChangedLocked(display);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getNonOverrideDisplayInfoInternal(int displayId, android.view.DisplayInfo outInfo) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display != null) {
                display.getNonOverrideDisplayInfoLocked(outInfo);
            }
        }
    }

    void performTraversalInternal(android.view.SurfaceControl.Transaction t, android.util.SparseArray<android.view.SurfaceControl.Transaction> displayTransactions) {
        synchronized (this.mSyncRoot) {
            if (this.mPendingTraversal || !this.mPendingTraversalCompleted) {
                this.mPendingTraversal = false;
                android.util.Slog.d(TAG, "performTraversalInternal pendingTraversal");
                performTraversalLocked(t, displayTransactions);
                for (android.hardware.display.DisplayManagerInternal.DisplayTransactionListener listener : this.mDisplayTransactionListeners) {
                    listener.onDisplayTransaction(t);
                }
            }
        }
    }

    private float clampBrightness(int displayState, float brightnessState) {
        if (displayState == 1) {
            brightnessState = -1.0f;
        } else if (brightnessState != -1.0f && brightnessState < 0.0f) {
            brightnessState = Float.NaN;
        }
        if (displayState != 1 && displayState != 3 && displayState != 4 && brightnessState != -1.0f) {
            return this.mDmsExt.oplusAdjustBrightness(brightnessState);
        }
        return brightnessState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestDisplayStateInternal(int displayId, int state, float brightnessState, float sdrBrightnessState) {
        java.lang.Runnable runnable;
        if (state == 0) {
            state = 2;
        }
        float brightnessState2 = clampBrightness(state, brightnessState);
        float sdrBrightnessState2 = clampBrightness(state, sdrBrightnessState);
        synchronized (this.mSyncRoot) {
            int index = this.mDisplayStates.indexOfKey(displayId);
            com.android.server.display.DisplayManagerService.BrightnessPair brightnessPair = index < 0 ? null : this.mDisplayBrightnesses.valueAt(index);
            if (index >= 0 && (this.mDisplayStates.valueAt(index) != state || !com.android.internal.display.BrightnessSynchronizer.floatEquals(brightnessPair.brightness, brightnessState2) || !com.android.internal.display.BrightnessSynchronizer.floatEquals(brightnessPair.sdrBrightness, sdrBrightnessState2))) {
                if (android.os.Trace.isTagEnabled(131072L)) {
                    java.lang.String traceMessage = android.view.Display.stateToString(state) + ", brightness=" + brightnessState2 + ", sdrBrightness=" + sdrBrightnessState2;
                    android.os.Trace.asyncTraceForTrackBegin(131072L, "requestDisplayStateInternal:" + displayId, traceMessage, displayId);
                }
                this.mDisplayStates.setValueAt(index, state);
                brightnessPair.brightness = brightnessState2;
                brightnessPair.sdrBrightness = sdrBrightnessState2;
                com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (display != null && (display.isEnabledLocked() || state == 1)) {
                    if (android.os.Trace.isTagEnabled(131072L)) {
                        android.os.Trace.asyncTraceForTrackEnd(131072L, "requestDisplayStateInternal:" + displayId, displayId);
                    }
                    if (display != null) {
                        runnable = updateDisplayStateLocked(display.getPrimaryDisplayDeviceLocked());
                    } else {
                        runnable = null;
                    }
                    this.mDmsExt.enterDCMode(this.mWindowManagerInternal, brightnessState2);
                    com.android.server.display.DisplayPowerController dpc = this.mDisplayPowerControllers.get(0);
                    if (dpc != null) {
                        dpc.updateFpsIfNeeded(brightnessState2);
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
        }
    }

    private class UidImportanceListener implements android.app.ActivityManager.OnUidImportanceListener {
        private UidImportanceListener() {
        }

        public void onUidImportance(int uid, int importance) {
            synchronized (com.android.server.display.DisplayManagerService.this.mPendingCallbackSelfLocked) {
                try {
                    if (importance >= 1000) {
                        android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "Drop pending events for gone uid " + uid);
                        com.android.server.display.DisplayManagerService.this.mPendingCallbackSelfLocked.delete(uid);
                        return;
                    }
                    if (importance >= 400) {
                        return;
                    }
                    android.util.SparseArray<com.android.server.display.DisplayManagerService.PendingCallback> pendingCallbacks = (android.util.SparseArray) com.android.server.display.DisplayManagerService.this.mPendingCallbackSelfLocked.get(uid);
                    if (pendingCallbacks == null) {
                        return;
                    }
                    if (com.android.server.display.DisplayManagerService.DEBUG) {
                        android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "Uid " + uid + " becomes " + importance + " " + com.android.server.display.DisplayManagerService.this.mPendingCallbackSelfLocked);
                    }
                    for (int i = 0; i < pendingCallbacks.size(); i++) {
                        com.android.server.display.DisplayManagerService.PendingCallback pendingCallback = pendingCallbacks.valueAt(i);
                        if (pendingCallback != null) {
                            pendingCallback.sendPendingDisplayEvent();
                        }
                    }
                    com.android.server.display.DisplayManagerService.this.mPendingCallbackSelfLocked.delete(uid);
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
        }
    }

    private class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver() {
            super(com.android.server.display.DisplayManagerService.this.mHandler);
            com.android.server.display.DisplayManagerService.this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor("minimal_post_processing_allowed"), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            com.android.server.display.DisplayManagerService.this.handleSettingsChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSettingsChange() {
        synchronized (this.mSyncRoot) {
            updateSettingsLocked();
            scheduleTraversalLocked(false);
        }
    }

    private void updateSettingsLocked() {
        setMinimalPostProcessingAllowed(android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "minimal_post_processing_allowed", 1, -2) != 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreResolutionFromBackup() {
        int savedMode = android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "screen_resolution_mode", 0, -2);
        if (savedMode == 0) {
            return;
        }
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(0);
            com.android.server.display.DisplayDevice device = display == null ? null : display.getPrimaryDisplayDeviceLocked();
            if (device == null) {
                android.util.Slog.w(TAG, "No default display device present to restore resolution mode");
                return;
            }
            android.graphics.Point[] supportedRes = device.getSupportedResolutionsLocked();
            if (supportedRes.length != 2) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Skipping resolution restore - " + supportedRes.length);
                }
                return;
            }
            int index = savedMode == 1 ? 0 : 1;
            android.graphics.Point res = supportedRes[index];
            android.view.Display.Mode newMode = new android.view.Display.Mode(res.x, res.y, 0.0f);
            android.util.Slog.i(TAG, "Restoring resolution from backup: (" + savedMode + ") " + res.x + "x" + res.y);
            setUserPreferredDisplayModeInternal(0, newMode);
        }
    }

    private void updateUserDisabledHdrTypesFromSettingsLocked() {
        this.mAreUserDisabledHdrTypesAllowed = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "are_user_disabled_hdr_formats_allowed", 1) != 0;
        java.lang.String userDisabledHdrTypes = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), "user_disabled_hdr_formats");
        if (userDisabledHdrTypes != null) {
            try {
                java.lang.String[] userDisabledHdrTypeStrings = android.text.TextUtils.split(userDisabledHdrTypes, ",");
                this.mUserDisabledHdrTypes = new int[userDisabledHdrTypeStrings.length];
                for (int i = 0; i < userDisabledHdrTypeStrings.length; i++) {
                    this.mUserDisabledHdrTypes[i] = java.lang.Integer.parseInt(userDisabledHdrTypeStrings[i]);
                }
                if (!this.mAreUserDisabledHdrTypesAllowed) {
                    this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda14
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$updateUserDisabledHdrTypesFromSettingsLocked$3((com.android.server.display.LogicalDisplay) obj);
                        }
                    });
                    return;
                }
                return;
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.e(TAG, "Failed to parse USER_DISABLED_HDR_FORMATS. Clearing the setting.", e);
                clearUserDisabledHdrTypesLocked();
                return;
            }
        }
        clearUserDisabledHdrTypesLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUserDisabledHdrTypesFromSettingsLocked$3(com.android.server.display.LogicalDisplay display) {
        display.setUserDisabledHdrTypes(this.mUserDisabledHdrTypes);
        handleLogicalDisplayChangedLocked(display);
    }

    private void clearUserDisabledHdrTypesLocked() {
        synchronized (this.mSyncRoot) {
            this.mUserDisabledHdrTypes = new int[0];
            android.provider.Settings.Global.putString(this.mContext.getContentResolver(), "user_disabled_hdr_formats", "");
        }
    }

    private void updateUserPreferredDisplayModeSettingsLocked() {
        float refreshRate = android.provider.Settings.Global.getFloat(this.mContext.getContentResolver(), "user_preferred_refresh_rate", 0.0f);
        int height = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "user_preferred_resolution_height", -1);
        int width = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "user_preferred_resolution_width", -1);
        final android.view.Display.Mode mode = new android.view.Display.Mode(width, height, refreshRate);
        this.mUserPreferredMode = isResolutionAndRefreshRateValid(mode) ? mode : null;
        if (this.mUserPreferredMode != null) {
            this.mDisplayDeviceRepo.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.display.DisplayDevice) obj).setUserPreferredDisplayModeLocked(mode);
                }
            });
        } else {
            this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.configurePreferredDisplayModeLocked((com.android.server.display.LogicalDisplay) obj);
                }
            });
        }
    }

    private android.view.DisplayInfo getDisplayInfoForFrameRateOverride(android.view.DisplayEventReceiver.FrameRateOverride[] frameRateOverrides, android.view.DisplayInfo info, int callingUid) {
        float frameRateHz = info.renderFrameRate;
        int length = frameRateOverrides.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            android.view.DisplayEventReceiver.FrameRateOverride frameRateOverride = frameRateOverrides[i];
            if (frameRateOverride.uid != callingUid) {
                i++;
            } else {
                frameRateHz = frameRateOverride.frameRateHz;
                break;
            }
        }
        if (frameRateHz == 0.0f) {
            return info;
        }
        boolean displayModeReturnsPhysicalRefreshRate = callingUid < 10000 || android.app.compat.CompatChanges.isChangeEnabled(DISPLAY_MODE_RETURNS_PHYSICAL_REFRESH_RATE, callingUid);
        android.view.Display.Mode currentMode = info.getMode();
        float numPeriods = currentMode.getRefreshRate() / frameRateHz;
        float numPeriodsRound = java.lang.Math.round(numPeriods);
        if (java.lang.Math.abs(numPeriods - numPeriodsRound) > THRESHOLD_FOR_REFRESH_RATES_DIVISORS) {
            return info;
        }
        float frameRateHz2 = currentMode.getRefreshRate() / numPeriodsRound;
        android.view.DisplayInfo overriddenInfo = new android.view.DisplayInfo();
        overriddenInfo.copyFrom(info);
        for (android.view.Display.Mode mode : info.supportedModes) {
            if (mode.equalsExceptRefreshRate(currentMode) && mode.getRefreshRate() >= frameRateHz2 - THRESHOLD_FOR_REFRESH_RATES_DIVISORS && mode.getRefreshRate() <= frameRateHz2 + THRESHOLD_FOR_REFRESH_RATES_DIVISORS) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "found matching modeId " + mode.getModeId());
                }
                overriddenInfo.refreshRateOverride = mode.getRefreshRate();
                if (!displayModeReturnsPhysicalRefreshRate) {
                    overriddenInfo.modeId = mode.getModeId();
                }
                return overriddenInfo;
            }
        }
        overriddenInfo.refreshRateOverride = frameRateHz2;
        if (!displayModeReturnsPhysicalRefreshRate) {
            overriddenInfo.supportedModes = (android.view.Display.Mode[]) java.util.Arrays.copyOf(info.supportedModes, info.supportedModes.length + 1);
            overriddenInfo.supportedModes[overriddenInfo.supportedModes.length - 1] = new android.view.Display.Mode(255, currentMode.getPhysicalWidth(), currentMode.getPhysicalHeight(), overriddenInfo.refreshRateOverride, currentMode.getVsyncRate(), new float[0], currentMode.getSupportedHdrTypes());
            overriddenInfo.modeId = overriddenInfo.supportedModes[overriddenInfo.supportedModes.length - 1].getModeId();
        }
        return overriddenInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.view.DisplayInfo getDisplayInfoInternal(int displayId, int callingUid) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display != null) {
                android.view.DisplayInfo tmpInfo = display.getDisplayInfoLocked();
                if (tmpInfo != null && tmpInfo.name != null) {
                    android.view.DisplayInfo info = getDisplayInfoForFrameRateOverride(display.getFrameRateOverrides(), tmpInfo, callingUid);
                    if (info.hasAccess(callingUid) || isUidPresentOnDisplayInternal(callingUid, displayId)) {
                        return this.mDmsExt.getZoomModeDisplayInfo(this.mDmsExt.getBacklightTypeDisplayInfo(info, displayId), displayId, callingUid);
                    }
                }
                return null;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCallbackInternal(android.hardware.display.IDisplayManagerCallback callback, int callingPid, int callingUid, long eventsMask) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayManagerService.CallbackRecord record = this.mCallbacks.get(callingPid);
            if (record != null) {
                record.updateEventsMask(eventsMask);
                return;
            }
            com.android.server.display.DisplayManagerService.CallbackRecord record2 = new com.android.server.display.DisplayManagerService.CallbackRecord(callingPid, callingUid, callback, eventsMask);
            try {
                android.os.IBinder binder = callback.asBinder();
                binder.linkToDeath(record2, 0);
                this.mCallbacks.put(callingPid, record2);
                this.mDmsExt.addProxyBinder(callback.asBinder(), callingUid, callingPid);
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCallbackDied(com.android.server.display.DisplayManagerService.CallbackRecord record) {
        synchronized (this.mSyncRoot) {
            this.mCallbacks.remove(record.mPid);
            this.mBinderDiedPids.put(java.lang.Integer.valueOf(record.mPid), java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
            stopWifiDisplayScanLocked(record);
        }
        this.mDmsExt.removeProxyBinder(record.mCallback.asBinder(), record.mUid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWifiDisplayScanInternal(int callingPid) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayManagerService.CallbackRecord record = this.mCallbacks.get(callingPid);
            if (record == null) {
                throw new java.lang.IllegalStateException("The calling process has not registered an IDisplayManagerCallback.");
            }
            startWifiDisplayScanLocked(record);
        }
    }

    private void startWifiDisplayScanLocked(com.android.server.display.DisplayManagerService.CallbackRecord record) {
        if (!record.mWifiDisplayScanRequested) {
            record.mWifiDisplayScanRequested = true;
            int i = this.mWifiDisplayScanRequestCount;
            this.mWifiDisplayScanRequestCount = i + 1;
            if (i == 0 && this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestStartScanLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopWifiDisplayScanInternal(int callingPid) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayManagerService.CallbackRecord record = this.mCallbacks.get(callingPid);
            if (record == null) {
                throw new java.lang.IllegalStateException("The calling process has not registered an IDisplayManagerCallback.");
            }
            stopWifiDisplayScanLocked(record);
        }
    }

    private void stopWifiDisplayScanLocked(com.android.server.display.DisplayManagerService.CallbackRecord record) {
        if (record.mWifiDisplayScanRequested) {
            record.mWifiDisplayScanRequested = false;
            int i = this.mWifiDisplayScanRequestCount - 1;
            this.mWifiDisplayScanRequestCount = i;
            if (i == 0) {
                if (this.mWifiDisplayAdapter != null) {
                    this.mWifiDisplayAdapter.requestStopScanLocked();
                }
            } else if (this.mWifiDisplayScanRequestCount < 0) {
                android.util.Slog.wtf(TAG, "mWifiDisplayScanRequestCount became negative: " + this.mWifiDisplayScanRequestCount);
                this.mWifiDisplayScanRequestCount = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectWifiDisplayInternal(java.lang.String address) {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestConnectLocked(address);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseWifiDisplayInternal() {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestPauseLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeWifiDisplayInternal() {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestResumeLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectWifiDisplayInternal() {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestDisconnectLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameWifiDisplayInternal(java.lang.String address, java.lang.String alias) {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestRenameLocked(address, alias);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forgetWifiDisplayInternal(java.lang.String address) {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                this.mWifiDisplayAdapter.requestForgetLocked(address);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.display.WifiDisplayStatus getWifiDisplayStatusInternal() {
        synchronized (this.mSyncRoot) {
            if (this.mWifiDisplayAdapter != null) {
                return this.mWifiDisplayAdapter.getWifiDisplayStatusLocked();
            }
            return new android.hardware.display.WifiDisplayStatus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserDisabledHdrTypesInternal(final int[] userDisabledHdrTypes) {
        synchronized (this.mSyncRoot) {
            if (userDisabledHdrTypes == null) {
                android.util.Slog.e(TAG, "Null is not an expected argument to setUserDisabledHdrTypesInternal");
                return;
            }
            if (!isSubsetOf(android.view.Display.HdrCapabilities.HDR_TYPES, userDisabledHdrTypes)) {
                android.util.Slog.e(TAG, "userDisabledHdrTypes contains unexpected types");
                return;
            }
            java.util.Arrays.sort(userDisabledHdrTypes);
            if (java.util.Arrays.equals(this.mUserDisabledHdrTypes, userDisabledHdrTypes)) {
                return;
            }
            java.lang.String userDisabledFormatsString = "";
            if (userDisabledHdrTypes.length != 0) {
                userDisabledFormatsString = android.text.TextUtils.join(",", java.util.Arrays.stream(userDisabledHdrTypes).boxed().toArray());
            }
            android.provider.Settings.Global.putString(this.mContext.getContentResolver(), "user_disabled_hdr_formats", userDisabledFormatsString);
            this.mUserDisabledHdrTypes = userDisabledHdrTypes;
            if (!this.mAreUserDisabledHdrTypesAllowed) {
                this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$setUserDisabledHdrTypesInternal$5(userDisabledHdrTypes, (com.android.server.display.LogicalDisplay) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setUserDisabledHdrTypesInternal$5(int[] userDisabledHdrTypes, com.android.server.display.LogicalDisplay display) {
        display.setUserDisabledHdrTypes(userDisabledHdrTypes);
        handleLogicalDisplayChangedLocked(display);
    }

    private boolean isSubsetOf(int[] sortedSuperset, int[] subset) {
        for (int i : subset) {
            if (java.util.Arrays.binarySearch(sortedSuperset, i) < 0) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAreUserDisabledHdrTypesAllowedInternal(boolean areUserDisabledHdrTypesAllowed) {
        synchronized (this.mSyncRoot) {
            if (this.mAreUserDisabledHdrTypesAllowed == areUserDisabledHdrTypesAllowed) {
                return;
            }
            this.mAreUserDisabledHdrTypesAllowed = areUserDisabledHdrTypesAllowed;
            if (this.mUserDisabledHdrTypes.length == 0) {
                return;
            }
            android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "are_user_disabled_hdr_formats_allowed", areUserDisabledHdrTypesAllowed ? 1 : 0);
            int[] userDisabledHdrTypes = new int[0];
            if (!this.mAreUserDisabledHdrTypesAllowed) {
                userDisabledHdrTypes = this.mUserDisabledHdrTypes;
            }
            final int[] finalUserDisabledHdrTypes = userDisabledHdrTypes;
            this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$setAreUserDisabledHdrTypesAllowedInternal$6(finalUserDisabledHdrTypes, (com.android.server.display.LogicalDisplay) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAreUserDisabledHdrTypesAllowedInternal$6(int[] finalUserDisabledHdrTypes, com.android.server.display.LogicalDisplay display) {
        display.setUserDisabledHdrTypes(finalUserDisabledHdrTypes);
        handleLogicalDisplayChangedLocked(display);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestColorModeInternal(int displayId, int colorMode) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display != null && display.getRequestedColorModeLocked() != colorMode) {
                display.setRequestedColorModeLocked(colorMode);
                scheduleTraversalLocked(false);
            }
        }
    }

    private boolean validatePackageName(int uid, java.lang.String packageName) {
        java.lang.String[] packageNames;
        if (uid == 0) {
            return true;
        }
        if (packageName != null && (packageNames = this.mContext.getPackageManager().getPackagesForUid(uid)) != null) {
            for (java.lang.String n : packageNames) {
                if (n.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canProjectVideo(android.media.projection.IMediaProjection projection) {
        if (projection != null) {
            try {
                if (projection.canProjectVideo()) {
                    return true;
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to query projection service for permissions", e);
            }
        }
        if (checkCallingPermission("android.permission.CAPTURE_VIDEO_OUTPUT", "canProjectVideo()")) {
            return true;
        }
        return canProjectSecureVideo(projection);
    }

    private boolean canProjectSecureVideo(android.media.projection.IMediaProjection projection) {
        if (projection != null) {
            try {
                if (projection.canProjectSecureVideo()) {
                    return true;
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Unable to query projection service for permissions", e);
            }
        }
        return checkCallingPermission("android.permission.CAPTURE_SECURE_VIDEO_OUTPUT", "canProjectSecureVideo()");
    }

    private boolean checkCallingPermission(java.lang.String permission, java.lang.String func) {
        if (this.mContext.checkCallingPermission(permission) == 0) {
            return true;
        }
        java.lang.String msg = "Permission Denial: " + func + " from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " requires " + permission;
        android.util.Slog.w(TAG, msg);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:143:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x024e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int createVirtualDisplayInternal(android.hardware.display.VirtualDisplayConfig r27, android.hardware.display.IVirtualDisplayCallback r28, android.media.projection.IMediaProjection r29, android.companion.virtual.IVirtualDevice r30, android.window.DisplayWindowPolicyController r31, java.lang.String r32) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 812
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.DisplayManagerService.createVirtualDisplayInternal(android.hardware.display.VirtualDisplayConfig, android.hardware.display.IVirtualDisplayCallback, android.media.projection.IMediaProjection, android.companion.virtual.IVirtualDevice, android.window.DisplayWindowPolicyController, java.lang.String):int");
    }

    private int createVirtualDisplayLocked(android.hardware.display.IVirtualDisplayCallback callback, android.media.projection.IMediaProjection projection, int callingUid, java.lang.String packageName, java.lang.String uniqueId, android.companion.virtual.IVirtualDevice virtualDevice, android.view.Surface surface, int flags, android.hardware.display.VirtualDisplayConfig virtualDisplayConfig) {
        if (this.mVirtualDisplayAdapter == null) {
            android.util.Slog.w(TAG, "Rejecting request to create private virtual display because the virtual display adapter is not available.");
            return -1;
        }
        android.util.Slog.d(TAG, "Virtual Display: creating DisplayDevice with VirtualDisplayAdapter");
        com.android.server.display.DisplayDevice device = this.mVirtualDisplayAdapter.createVirtualDisplayLocked(callback, projection, callingUid, packageName, uniqueId, surface, flags, virtualDisplayConfig);
        if (device == null) {
            android.util.Slog.w(TAG, "Virtual Display: VirtualDisplayAdapter failed to create DisplayDevice");
            return -1;
        }
        if ((flags & 32768) != 0) {
            if (virtualDevice != null) {
                try {
                    int virtualDeviceId = virtualDevice.getDeviceId();
                    this.mLogicalDisplayMapper.associateDisplayDeviceWithVirtualDevice(device, virtualDeviceId);
                } catch (android.os.RemoteException e) {
                    e.rethrowFromSystemServer();
                }
            } else {
                android.util.Slog.i(TAG, "Display created with VIRTUAL_DISPLAY_FLAG_DEVICE_DISPLAY_GROUP set, but no virtual device. The display will not be added to a device display group.");
            }
        }
        this.mDisplayDeviceRepo.onDisplayDeviceEvent(device, 1);
        com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(device);
        if (display != null) {
            return display.getDisplayIdLocked();
        }
        android.util.Slog.w(TAG, "Rejecting request to create virtual display because the logical display was not created.");
        this.mVirtualDisplayAdapter.releaseVirtualDisplayLocked(callback.asBinder());
        this.mDisplayDeviceRepo.onDisplayDeviceEvent(device, 3);
        return -1;
    }

    private static boolean isMirroringSupportedByVirtualDevice(android.companion.virtual.IVirtualDevice virtualDevice) {
        return android.companion.virtual.flags.Flags.interactiveScreenMirror() && virtualDevice != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resizeVirtualDisplayInternal(android.os.IBinder appToken, int width, int height, int densityDpi) {
        synchronized (this.mSyncRoot) {
            if (this.mVirtualDisplayAdapter == null) {
                return;
            }
            this.mVirtualDisplayAdapter.resizeVirtualDisplayLocked(appToken, width, height, densityDpi);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVirtualDisplaySurfaceInternal(android.os.IBinder appToken, android.view.Surface surface) {
        synchronized (this.mSyncRoot) {
            if (this.mVirtualDisplayAdapter == null) {
                return;
            }
            this.mVirtualDisplayAdapter.setVirtualDisplaySurfaceLocked(appToken, surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseVirtualDisplayInternal(android.os.IBinder appToken) {
        synchronized (this.mSyncRoot) {
            if (this.mVirtualDisplayAdapter == null) {
                return;
            }
            com.android.server.display.DisplayDevice device = this.mVirtualDisplayAdapter.releaseVirtualDisplayLocked(appToken);
            android.util.Slog.d(TAG, "Virtual Display: Display Device released");
            if (device != null) {
                this.mDisplayDeviceRepo.onDisplayDeviceEvent(device, 3);
            }
            if (device == null) {
                this.mDmsExt.hookUpdateScreenRecorderState(-1, "all", false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVirtualDisplayStateInternal(android.os.IBinder appToken, boolean isOn) {
        synchronized (this.mSyncRoot) {
            if (this.mVirtualDisplayAdapter == null) {
                return;
            }
            this.mVirtualDisplayAdapter.setVirtualDisplayStateLocked(appToken, isOn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerDefaultDisplayAdapters() {
        synchronized (this.mSyncRoot) {
            registerDisplayAdapterLocked(this.mInjector.getLocalDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo, this.mFlags, this.mDisplayNotificationManager));
            this.mVirtualDisplayAdapter = this.mInjector.getVirtualDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo, this.mFlags);
            if (this.mVirtualDisplayAdapter != null) {
                registerDisplayAdapterLocked(this.mVirtualDisplayAdapter);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerAdditionalDisplayAdapters() {
        synchronized (this.mSyncRoot) {
            if (shouldRegisterNonEssentialDisplayAdaptersLocked()) {
                registerOverlayDisplayAdapterLocked();
                registerWifiDisplayAdapterLocked();
            }
        }
    }

    private void registerOverlayDisplayAdapterLocked() {
        registerDisplayAdapterLocked(new com.android.server.display.OverlayDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo, this.mUiHandler, this.mFlags));
        this.mDmsExt.setActivityPreloadDisplayAdapter(this.mDisplayAdapters);
    }

    private void registerWifiDisplayAdapterLocked() {
        if (this.mContext.getResources().getBoolean(android.R.bool.config_enableSearchTileHideIllustrationInPrivateSpace) || android.os.SystemProperties.getInt(FORCE_WIFI_DISPLAY_ENABLE, -1) == 1 || (android.os.Build.isMtkPlatform() && android.os.SystemProperties.get("ro.vendor.mtk_wfd_support").equals("1"))) {
            this.mWifiDisplayAdapter = new com.android.server.display.WifiDisplayAdapter(this.mSyncRoot, this.mContext, this.mHandler, this.mDisplayDeviceRepo, this.mPersistentDataStore, this.mFlags);
            registerDisplayAdapterLocked(this.mWifiDisplayAdapter);
        }
    }

    private boolean shouldRegisterNonEssentialDisplayAdaptersLocked() {
        return !this.mSafeMode;
    }

    private void registerDisplayAdapterLocked(com.android.server.display.DisplayAdapter adapter) {
        this.mDisplayAdapters.add(adapter);
        adapter.registerLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayDisconnectedLocked(com.android.server.display.LogicalDisplay display) {
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            android.util.Slog.e(TAG, "DisplayDisconnected shouldn't be received when the flag is off");
        } else {
            releaseDisplayAndEmitEvent(display, 7);
            this.mExternalDisplayPolicy.handleLogicalDisplayDisconnectedLocked(display);
        }
    }

    private void setupLogicalDisplay(com.android.server.display.LogicalDisplay display) {
        com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
        int displayId = display.getDisplayIdLocked();
        boolean isDefault = displayId == 0;
        configureColorModeLocked(display, device);
        if (!this.mAreUserDisabledHdrTypesAllowed) {
            display.setUserDisabledHdrTypes(this.mUserDisabledHdrTypes);
        }
        if (isDefault) {
            notifyDefaultDisplayDeviceUpdated(display);
            recordStableDisplayStatsIfNeededLocked(display);
            recordTopInsetLocked(display);
        }
        if (this.mUserPreferredMode != null) {
            device.setUserPreferredDisplayModeLocked(this.mUserPreferredMode);
        } else {
            configurePreferredDisplayModeLocked(display);
        }
        addDisplayPowerControllerLocked(display);
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        if (info.type == 1) {
            this.mDisplayStates.append(displayId, 0);
        } else {
            this.mDisplayStates.append(displayId, 2);
        }
        float brightnessDefault = display.getDisplayInfoLocked().brightnessDefault;
        this.mDisplayBrightnesses.append(displayId, new com.android.server.display.DisplayManagerService.BrightnessPair(brightnessDefault, brightnessDefault));
        android.hardware.display.DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
    }

    private void updateLogicalDisplayState(com.android.server.display.LogicalDisplay display) {
        java.lang.Runnable work = updateDisplayStateLocked(display.getPrimaryDisplayDeviceLocked());
        if (work != null) {
            work.run();
        }
        scheduleTraversalLocked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayConnectedLocked(com.android.server.display.LogicalDisplay display) {
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            android.util.Slog.e(TAG, "DisplayConnected shouldn't be received when the flag is off");
            return;
        }
        setupLogicalDisplay(display);
        if (com.android.server.display.ExternalDisplayPolicy.isExternalDisplayLocked(display)) {
            this.mExternalDisplayPolicy.handleExternalDisplayConnectedLocked(display);
        } else {
            sendDisplayEventLocked(display, 6);
        }
        updateLogicalDisplayState(display);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayAddedLocked(com.android.server.display.LogicalDisplay display) {
        int displayId = display.getDisplayIdLocked();
        boolean isDefault = displayId == 0;
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            setupLogicalDisplay(display);
        }
        if (isDefault) {
            this.mSyncRoot.notifyAll();
        }
        sendDisplayEventIfEnabledLocked(display, 1);
        this.mDmsExt.handleLogicalDisplayAddedLocked(display);
        updateLogicalDisplayState(display);
        this.mExternalDisplayPolicy.handleLogicalDisplayAddedLocked(display);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayChangedLocked(com.android.server.display.LogicalDisplay display) {
        updateViewportPowerStateLocked(display);
        int displayId = display.getDisplayIdLocked();
        if (displayId == 0) {
            recordTopInsetLocked(display);
        }
        sendDisplayEventIfEnabledLocked(display, 2);
        applyDisplayChangedLocked(display);
    }

    private void applyDisplayChangedLocked(com.android.server.display.LogicalDisplay display) {
        int displayId = display.getDisplayIdLocked();
        scheduleTraversalLocked(false);
        this.mPersistentDataStore.saveIfNeeded();
        this.mDmsExt.handleLogicalDisplayChangedLocked(display);
        com.android.server.display.DisplayPowerController dpc = this.mDisplayPowerControllers.get(displayId);
        if (dpc != null) {
            int leadDisplayId = display.getLeadDisplayIdLocked();
            updateDisplayPowerControllerLeaderLocked(dpc, leadDisplayId);
            com.android.server.display.HighBrightnessModeMetadata hbmMetadata = this.mHighBrightnessModeMetadataMapper.getHighBrightnessModeMetadataLocked(display);
            if (hbmMetadata != null) {
                dpc.onDisplayChanged(hbmMetadata, leadDisplayId);
            }
        }
    }

    private void updateDisplayPowerControllerLeaderLocked(com.android.server.display.DisplayPowerControllerInterface dpc, int leadDisplayId) {
        dpc.getLeadDisplayId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayFrameRateOverridesChangedLocked(com.android.server.display.LogicalDisplay display) {
        int displayId = display.getDisplayIdLocked();
        sendDisplayEventFrameRateOverrideLocked(displayId);
        scheduleTraversalLocked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayRemovedLocked(com.android.server.display.LogicalDisplay display) {
        if (this.mFlags.isConnectedDisplayManagementEnabled()) {
            if (display.isValidLocked()) {
                updateViewportPowerStateLocked(display);
            }
            sendDisplayEventLocked(display, 3);
            if (display.isValidLocked()) {
                applyDisplayChangedLocked(display);
                return;
            }
            return;
        }
        releaseDisplayAndEmitEvent(display, 3);
    }

    private void releaseDisplayAndEmitEvent(com.android.server.display.LogicalDisplay display, int event) {
        final android.companion.virtual.IVirtualDevice virtualDevice;
        this.mDmsExt.handleLogicalDisplayRemovedLocked(display);
        final int displayId = display.getDisplayIdLocked();
        com.android.server.display.DisplayPowerControllerInterface dpc = (com.android.server.display.DisplayPowerControllerInterface) this.mDisplayPowerControllers.removeReturnOld(displayId);
        if (dpc != null) {
            updateDisplayPowerControllerLeaderLocked(dpc, -1);
            dpc.stop();
        }
        this.mDisplayStates.delete(displayId);
        this.mDisplayBrightnesses.delete(displayId);
        android.hardware.display.DisplayManagerGlobal.invalidateLocalDisplayInfoCaches();
        if (this.mDisplayWindowPolicyControllers.contains(displayId) && (virtualDevice = (android.companion.virtual.IVirtualDevice) ((android.util.Pair) this.mDisplayWindowPolicyControllers.removeReturnOld(displayId)).first) != null) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$releaseDisplayAndEmitEvent$7(virtualDevice, displayId);
                }
            });
        }
        sendDisplayEventLocked(display, event);
        scheduleTraversalLocked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$releaseDisplayAndEmitEvent$7(android.companion.virtual.IVirtualDevice virtualDevice, int displayId) {
        ((com.android.server.companion.virtual.VirtualDeviceManagerInternal) getLocalService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class)).onVirtualDisplayRemoved(virtualDevice, displayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplaySwappedLocked(com.android.server.display.LogicalDisplay display) {
        this.mDmsExt.handleLogicalDisplaySwappedLocked(display);
        handleLogicalDisplayChangedLocked(display);
        int displayId = display.getDisplayIdLocked();
        if (displayId == 0) {
            notifyDefaultDisplayDeviceUpdated(display);
        }
        this.mHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayHdrSdrRatioChangedLocked(com.android.server.display.LogicalDisplay display) {
        sendDisplayEventIfEnabledLocked(display, 5);
    }

    private void notifyDefaultDisplayDeviceUpdated(com.android.server.display.LogicalDisplay display) {
        this.mDisplayModeDirector.defaultDisplayDeviceUpdated(display.getPrimaryDisplayDeviceLocked().mDisplayDeviceConfig);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogicalDisplayDeviceStateTransitionLocked(com.android.server.display.LogicalDisplay display) {
        int displayId = display.getDisplayIdLocked();
        com.android.server.display.DisplayPowerController dpc = this.mDisplayPowerControllers.get(displayId);
        if (dpc != null) {
            int leadDisplayId = display.getLeadDisplayIdLocked();
            updateDisplayPowerControllerLeaderLocked(dpc, leadDisplayId);
            com.android.server.display.HighBrightnessModeMetadata hbmMetadata = this.mHighBrightnessModeMetadataMapper.getHighBrightnessModeMetadataLocked(display);
            if (hbmMetadata != null) {
                dpc.onDisplayChanged(hbmMetadata, leadDisplayId);
            }
        }
        this.mDmsExt.handleLogicalDisplayDeviceStateTransitionLocked(display);
    }

    private java.lang.Runnable updateDisplayStateLocked(com.android.server.display.DisplayDevice device) {
        com.android.server.display.LogicalDisplay display;
        int displayId;
        int state;
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        if ((info.flags & 32) != 0 || (display = this.mLogicalDisplayMapper.getDisplayLocked(device)) == null || (state = this.mDisplayStates.get((displayId = display.getDisplayIdLocked()))) == 0) {
            return null;
        }
        com.android.server.display.DisplayManagerService.BrightnessPair brightnessPair = this.mDisplayBrightnesses.get(displayId);
        return device.requestDisplayStateLocked(state, brightnessPair.brightness, brightnessPair.sdrBrightness, display.getDisplayOffloadSessionLocked());
    }

    private void configureColorModeLocked(com.android.server.display.LogicalDisplay display, com.android.server.display.DisplayDevice device) {
        if (display.getPrimaryDisplayDeviceLocked() == device) {
            int colorMode = this.mPersistentDataStore.getColorMode(device);
            if (colorMode == -1) {
                if (display.getDisplayIdLocked() == 0) {
                    colorMode = this.mDefaultDisplayDefaultColorMode;
                } else {
                    colorMode = 0;
                }
            }
            display.setRequestedColorModeLocked(colorMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void configurePreferredDisplayModeLocked(com.android.server.display.LogicalDisplay display) {
        com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
        android.graphics.Point userPreferredResolution = this.mPersistentDataStore.getUserPreferredResolution(device);
        float refreshRate = this.mPersistentDataStore.getUserPreferredRefreshRate(device);
        if (userPreferredResolution == null && java.lang.Float.isNaN(refreshRate)) {
            return;
        }
        android.view.Display.Mode.Builder modeBuilder = new android.view.Display.Mode.Builder();
        if (userPreferredResolution != null) {
            modeBuilder.setResolution(userPreferredResolution.x, userPreferredResolution.y);
        }
        if (!java.lang.Float.isNaN(refreshRate)) {
            modeBuilder.setRefreshRate(refreshRate);
        }
        device.setUserPreferredDisplayModeLocked(modeBuilder.build());
    }

    private void storeHdrConversionModeLocked(android.hardware.display.HdrConversionMode hdrConversionMode) {
        int preferredHdrOutputType;
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "hdr_conversion_mode", hdrConversionMode.getConversionMode());
        if (hdrConversionMode.getConversionMode() == 3) {
            preferredHdrOutputType = hdrConversionMode.getPreferredHdrOutputType();
        } else {
            preferredHdrOutputType = -1;
        }
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "hdr_force_conversion_type", preferredHdrOutputType);
    }

    void updateHdrConversionModeSettingsLocked() {
        int preferredHdrOutputType;
        int conversionMode = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "hdr_conversion_mode", this.mDefaultHdrConversionMode);
        if (conversionMode == 3) {
            preferredHdrOutputType = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "hdr_force_conversion_type", 1);
        } else {
            preferredHdrOutputType = -1;
        }
        this.mHdrConversionMode = new android.hardware.display.HdrConversionMode(conversionMode, preferredHdrOutputType);
        setHdrConversionModeInternal(this.mHdrConversionMode);
    }

    private void recordStableDisplayStatsIfNeededLocked(com.android.server.display.LogicalDisplay d) {
        if (this.mStableDisplaySize.x <= 0 && this.mStableDisplaySize.y <= 0) {
            android.view.DisplayInfo info = d.getDisplayInfoLocked();
            setStableDisplaySizeLocked(info.getNaturalWidth(), info.getNaturalHeight());
        }
    }

    private void recordTopInsetLocked(com.android.server.display.LogicalDisplay d) {
        int topInset;
        if (!this.mSystemReady || d == null || (topInset = d.getInsets().top) == this.mDefaultDisplayTopInset) {
            return;
        }
        this.mDefaultDisplayTopInset = topInset;
        android.os.SystemProperties.set(PROP_DEFAULT_DISPLAY_TOP_INSET, java.lang.Integer.toString(topInset));
    }

    private void setStableDisplaySizeLocked(int width, int height) {
        this.mStableDisplaySize = new android.graphics.Point(width, height);
        try {
            this.mPersistentDataStore.setStableDisplaySize(this.mStableDisplaySize);
        } finally {
            this.mPersistentDataStore.saveIfNeeded();
        }
    }

    android.hardware.display.Curve getMinimumBrightnessCurveInternal() {
        return this.mMinimumBrightnessCurve;
    }

    int getPreferredWideGamutColorSpaceIdInternal() {
        return this.mWideColorSpace.getId();
    }

    android.hardware.OverlayProperties getOverlaySupportInternal() {
        return this.mOverlayProperties;
    }

    void setUserPreferredDisplayModeInternal(int displayId, android.view.Display.Mode mode) {
        int resolutionHeight;
        int resolutionWidth;
        synchronized (this.mSyncRoot) {
            if (mode != null) {
                try {
                    if (!isResolutionAndRefreshRateValid(mode) && displayId == -1) {
                        throw new java.lang.IllegalArgumentException("width, height and refresh rate of mode should be greater than 0 when setting the global user preferred display mode.");
                    }
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
            if (mode == null) {
                resolutionHeight = -1;
            } else {
                resolutionHeight = mode.getPhysicalHeight();
            }
            if (mode == null) {
                resolutionWidth = -1;
            } else {
                resolutionWidth = mode.getPhysicalWidth();
            }
            float refreshRate = mode == null ? 0.0f : mode.getRefreshRate();
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            android.util.Slog.d(TAG, "setUserPreferredDisplayMode id=" + displayId + " mode=" + mode + " pid=" + pid + " uid=" + uid);
            storeModeInPersistentDataStoreLocked(displayId, resolutionWidth, resolutionHeight, refreshRate);
            if (displayId != -1) {
                setUserPreferredModeForDisplayLocked(displayId, mode);
            } else {
                this.mUserPreferredMode = mode;
                storeModeInGlobalSettingsLocked(resolutionWidth, resolutionHeight, refreshRate, mode);
            }
        }
    }

    private void storeModeInPersistentDataStoreLocked(int displayId, int resolutionWidth, int resolutionHeight, float refreshRate) {
        com.android.server.display.DisplayDevice displayDevice = getDeviceForDisplayLocked(displayId);
        if (displayDevice == null) {
            return;
        }
        try {
            this.mPersistentDataStore.setUserPreferredResolution(displayDevice, resolutionWidth, resolutionHeight);
            this.mPersistentDataStore.setUserPreferredRefreshRate(displayDevice, refreshRate);
        } finally {
            this.mPersistentDataStore.saveIfNeeded();
        }
    }

    private void setUserPreferredModeForDisplayLocked(int displayId, android.view.Display.Mode mode) {
        com.android.server.display.DisplayDevice displayDevice = getDeviceForDisplayLocked(displayId);
        if (displayDevice == null) {
            return;
        }
        if (this.mFlags.isResolutionBackupRestoreEnabled() && displayId == 0) {
            android.graphics.Point[] resolutions = displayDevice.getSupportedResolutionsLocked();
            int resolutionMode = 2;
            if (resolutions.length == 2) {
                android.graphics.Point newMode = new android.graphics.Point(mode.getPhysicalWidth(), mode.getPhysicalHeight());
                if (newMode.equals(resolutions[0])) {
                    resolutionMode = 1;
                } else if (!newMode.equals(resolutions[1])) {
                    resolutionMode = 0;
                }
                android.provider.Settings.Secure.putIntForUser(this.mContext.getContentResolver(), "screen_resolution_mode", resolutionMode, -2);
            }
        }
        displayDevice.setUserPreferredDisplayModeLocked(mode);
    }

    private void storeModeInGlobalSettingsLocked(int resolutionWidth, int resolutionHeight, float refreshRate, final android.view.Display.Mode mode) {
        android.provider.Settings.Global.putFloat(this.mContext.getContentResolver(), "user_preferred_refresh_rate", refreshRate);
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "user_preferred_resolution_height", resolutionHeight);
        android.provider.Settings.Global.putInt(this.mContext.getContentResolver(), "user_preferred_resolution_width", resolutionWidth);
        this.mDisplayDeviceRepo.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.display.DisplayDevice) obj).setUserPreferredDisplayModeLocked(mode);
            }
        });
    }

    private int[] getEnabledAutoHdrTypesLocked() {
        android.util.IntArray autoHdrOutputTypesArray = new android.util.IntArray();
        for (int type : getSupportedHdrOutputTypesInternal()) {
            boolean isDisabled = false;
            int[] iArr = this.mUserDisabledHdrTypes;
            int length = iArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                int disabledType = iArr[i];
                if (type != disabledType) {
                    i++;
                } else {
                    isDisabled = true;
                    break;
                }
            }
            if (!isDisabled) {
                autoHdrOutputTypesArray.add(type);
            }
        }
        return autoHdrOutputTypesArray.toArray();
    }

    private boolean hdrConversionIntroducesLatencyLocked() {
        android.hardware.display.HdrConversionMode mode = getHdrConversionModeSettingInternal();
        int preferredHdrOutputType = mode.getConversionMode() == 2 ? this.mSystemPreferredHdrOutputType : mode.getPreferredHdrOutputType();
        if (preferredHdrOutputType != -1) {
            int[] hdrTypesWithLatency = this.mInjector.getHdrOutputTypesWithLatency();
            return com.android.internal.util.ArrayUtils.contains(hdrTypesWithLatency, preferredHdrOutputType);
        }
        return false;
    }

    android.view.Display.Mode getUserPreferredDisplayModeInternal(int displayId) {
        synchronized (this.mSyncRoot) {
            if (displayId == -1) {
                return this.mUserPreferredMode;
            }
            com.android.server.display.DisplayDevice displayDevice = getDeviceForDisplayLocked(displayId);
            if (displayDevice == null) {
                return null;
            }
            return displayDevice.getUserPreferredDisplayModeLocked();
        }
    }

    android.view.Display.Mode getSystemPreferredDisplayModeInternal(int displayId) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayDevice device = getDeviceForDisplayLocked(displayId);
            if (device == null) {
                return null;
            }
            return device.getSystemPreferredDisplayModeLocked();
        }
    }

    void setHdrConversionModeInternal(android.hardware.display.HdrConversionMode hdrConversionMode) {
        if (!this.mInjector.getHdrOutputConversionSupport()) {
            return;
        }
        int[] autoHdrOutputTypes = null;
        synchronized (this.mSyncRoot) {
            if (hdrConversionMode.getConversionMode() == 2 && hdrConversionMode.getPreferredHdrOutputType() != -1) {
                throw new java.lang.IllegalArgumentException("preferredHdrOutputType must not be set if the conversion mode is HDR_CONVERSION_SYSTEM");
            }
            this.mHdrConversionMode = hdrConversionMode;
            storeHdrConversionModeLocked(this.mHdrConversionMode);
            if (hdrConversionMode.getConversionMode() == 2) {
                autoHdrOutputTypes = getEnabledAutoHdrTypesLocked();
            }
            int conversionMode = hdrConversionMode.getConversionMode();
            int preferredHdrType = hdrConversionMode.getPreferredHdrOutputType();
            if (this.mOverrideHdrConversionMode == null) {
                if (conversionMode == 3 && preferredHdrType == -1) {
                    conversionMode = 1;
                }
            } else {
                conversionMode = this.mOverrideHdrConversionMode.getConversionMode();
                preferredHdrType = this.mOverrideHdrConversionMode.getPreferredHdrOutputType();
                autoHdrOutputTypes = null;
            }
            this.mSystemPreferredHdrOutputType = this.mInjector.setHdrConversionMode(conversionMode, preferredHdrType, autoHdrOutputTypes);
        }
    }

    android.hardware.display.HdrConversionMode getHdrConversionModeSettingInternal() {
        if (!this.mInjector.getHdrOutputConversionSupport()) {
            return HDR_CONVERSION_MODE_UNSUPPORTED;
        }
        synchronized (this.mSyncRoot) {
            if (this.mHdrConversionMode != null) {
                return this.mHdrConversionMode;
            }
            return new android.hardware.display.HdrConversionMode(this.mDefaultHdrConversionMode);
        }
    }

    android.hardware.display.HdrConversionMode getHdrConversionModeInternal() {
        android.hardware.display.HdrConversionMode mode;
        if (!this.mInjector.getHdrOutputConversionSupport()) {
            return HDR_CONVERSION_MODE_UNSUPPORTED;
        }
        synchronized (this.mSyncRoot) {
            if (this.mOverrideHdrConversionMode != null) {
                mode = this.mOverrideHdrConversionMode;
            } else {
                mode = this.mHdrConversionMode;
            }
            if (mode == null && this.mDefaultHdrConversionMode == 1) {
                return new android.hardware.display.HdrConversionMode(1);
            }
            if (mode != null && mode.getConversionMode() != 2) {
                return mode;
            }
            return new android.hardware.display.HdrConversionMode(2, this.mSystemPreferredHdrOutputType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getSupportedHdrOutputTypesInternal() {
        if (this.mSupportedHdrOutputType == null) {
            this.mSupportedHdrOutputType = this.mInjector.getSupportedHdrOutputTypes();
        }
        return this.mSupportedHdrOutputType;
    }

    void setShouldAlwaysRespectAppRequestedModeInternal(boolean enabled) {
        this.mDisplayModeDirector.setShouldAlwaysRespectAppRequestedMode(enabled);
    }

    boolean shouldAlwaysRespectAppRequestedModeInternal() {
        return this.mDisplayModeDirector.shouldAlwaysRespectAppRequestedMode();
    }

    void setRefreshRateSwitchingTypeInternal(int newValue) {
        this.mDisplayModeDirector.setModeSwitchingType(newValue);
    }

    int getRefreshRateSwitchingTypeInternal() {
        return this.mDisplayModeDirector.getModeSwitchingType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.graphics.common.DisplayDecorationSupport getDisplayDecorationSupportInternal(int displayId) {
        android.os.IBinder displayToken = getDisplayToken(displayId);
        if (displayToken == null) {
            return null;
        }
        return android.view.SurfaceControl.getDisplayDecorationSupport(displayToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBrightnessConfigurationForDisplayInternal(android.hardware.display.BrightnessConfiguration c, java.lang.String uniqueId, int userId, java.lang.String packageName) {
        validateBrightnessConfiguration(c);
        int userSerial = getUserManager().getUserSerialNumber(userId);
        synchronized (this.mSyncRoot) {
            try {
                com.android.server.display.DisplayDevice displayDevice = this.mDisplayDeviceRepo.getByUniqueIdLocked(uniqueId);
                if (displayDevice == null) {
                    return;
                }
                if (this.mLogicalDisplayMapper.getDisplayLocked(displayDevice) != null && this.mLogicalDisplayMapper.getDisplayLocked(displayDevice).getDisplayInfoLocked().type == 1 && c != null) {
                    com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.BRIGHTNESS_CONFIGURATION_UPDATED, (float[]) c.getCurve().first, (float[]) c.getCurve().second, uniqueId);
                }
                this.mPersistentDataStore.setBrightnessConfigurationForDisplayLocked(c, displayDevice, userSerial, packageName);
                this.mPersistentDataStore.saveIfNeeded();
                if (userId != this.mCurrentUserId) {
                    return;
                }
                com.android.server.display.DisplayPowerController dpc = getDpcFromUniqueIdLocked(uniqueId);
                if (dpc != null) {
                    dpc.setBrightnessConfiguration(c, true);
                }
            } finally {
                this.mPersistentDataStore.saveIfNeeded();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.display.DisplayPowerController getDpcFromUniqueIdLocked(java.lang.String uniqueId) {
        com.android.server.display.DisplayDevice displayDevice = this.mDisplayDeviceRepo.getByUniqueIdLocked(uniqueId);
        com.android.server.display.LogicalDisplay logicalDisplay = this.mLogicalDisplayMapper.getDisplayLocked(displayDevice);
        if (logicalDisplay != null) {
            int displayId = logicalDisplay.getDisplayIdLocked();
            return this.mDisplayPowerControllers.get(displayId);
        }
        return null;
    }

    void validateBrightnessConfiguration(android.hardware.display.BrightnessConfiguration config) {
        if (config != null && isBrightnessConfigurationTooDark(config)) {
            throw new java.lang.IllegalArgumentException("brightness curve is too dark");
        }
    }

    private boolean isBrightnessConfigurationTooDark(android.hardware.display.BrightnessConfiguration config) {
        android.util.Pair<float[], float[]> curve = config.getCurve();
        float[] lux = (float[]) curve.first;
        float[] nits = (float[]) curve.second;
        for (int i = 0; i < lux.length; i++) {
            if (nits[i] < this.mMinimumBrightnessSpline.interpolate(lux[i])) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBrightnessConfigurations() {
        final int userSerial = getUserManager().getUserSerialNumber(this.mContext.getUserId());
        synchronized (this.mSyncRoot) {
            this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$loadBrightnessConfigurations$9(userSerial, (com.android.server.display.LogicalDisplay) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBrightnessConfigurations$9(int userSerial, com.android.server.display.LogicalDisplay logicalDisplay) {
        com.android.server.display.DisplayPowerController dpc;
        java.lang.String uniqueId = logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        android.hardware.display.BrightnessConfiguration config = getBrightnessConfigForDisplayWithPdsFallbackLocked(uniqueId, userSerial);
        if (config != null && (dpc = this.mDisplayPowerControllers.get(logicalDisplay.getDisplayIdLocked())) != null) {
            dpc.setBrightnessConfiguration(config, false);
        }
    }

    private void performTraversalLocked(final android.view.SurfaceControl.Transaction t, final android.util.SparseArray<android.view.SurfaceControl.Transaction> displayTransactions) {
        clearViewportsLocked();
        this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda13
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$performTraversalLocked$10(displayTransactions, t, (com.android.server.display.LogicalDisplay) obj);
            }
        }, true);
        if (this.mInputManagerInternal != null) {
            this.mHandler.sendEmptyMessage(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performTraversalLocked$10(android.util.SparseArray displayTransactions, android.view.SurfaceControl.Transaction t, com.android.server.display.LogicalDisplay display) {
        com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
        android.view.SurfaceControl.Transaction displayTransaction = (android.view.SurfaceControl.Transaction) displayTransactions.get(display.getDisplayIdLocked(), t);
        if (device != null) {
            configureDisplayLocked(displayTransaction, device);
            device.performTraversalLocked(displayTransaction);
        }
    }

    void setDisplayPropertiesInternal(int displayId, boolean hasContent, float requestedRefreshRate, int requestedModeId, float requestedMinRefreshRate, float requestedMaxRefreshRate, boolean preferMinimalPostProcessing, boolean disableHdrConversion, boolean inTraversal) {
        boolean shouldScheduleTraversal;
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display == null) {
                return;
            }
            if (display.hasContentLocked() == hasContent) {
                shouldScheduleTraversal = false;
            } else {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Display " + displayId + " hasContent flag changed: hasContent=" + hasContent + ", inTraversal=" + inTraversal);
                }
                display.setHasContentLocked(hasContent);
                shouldScheduleTraversal = true;
            }
            this.mDisplayModeDirector.getAppRequestObserver().setAppRequest(displayId, requestedModeId, requestedRefreshRate, requestedMinRefreshRate, requestedMaxRefreshRate);
            boolean disableHdrConversionForLatency = false;
            boolean mppRequest = isMinimalPostProcessingAllowed() && preferMinimalPostProcessing;
            if (mppRequest && hdrConversionIntroducesLatencyLocked()) {
                disableHdrConversionForLatency = true;
            }
            if (display.getRequestedMinimalPostProcessingLocked() != mppRequest) {
                display.setRequestedMinimalPostProcessingLocked(mppRequest);
                shouldScheduleTraversal = true;
            }
            if (shouldScheduleTraversal) {
                scheduleTraversalLocked(inTraversal);
            }
            if (this.mHdrConversionMode == null) {
                return;
            }
            if (this.mOverrideHdrConversionMode == null && (disableHdrConversion || disableHdrConversionForLatency)) {
                this.mOverrideHdrConversionMode = new android.hardware.display.HdrConversionMode(1);
                setHdrConversionModeInternal(this.mHdrConversionMode);
                handleLogicalDisplayChangedLocked(display);
            } else if (this.mOverrideHdrConversionMode != null && !disableHdrConversion && !disableHdrConversionForLatency) {
                this.mOverrideHdrConversionMode = null;
                setHdrConversionModeInternal(this.mHdrConversionMode);
                handleLogicalDisplayChangedLocked(display);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayOffsetsInternal(int displayId, int x, int y) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display == null) {
                return;
            }
            if (display.getDisplayOffsetXLocked() != x || display.getDisplayOffsetYLocked() != y) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Display " + displayId + " burn-in offset set to (" + x + ", " + y + ")");
                }
                display.setDisplayOffsetsLocked(x, y);
                scheduleTraversalLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayScalingDisabledInternal(int displayId, boolean disable) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display == null) {
                return;
            }
            if (display.isDisplayScalingDisabled() != disable) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Display " + displayId + " content scaling disabled = " + disable);
                }
                display.setDisplayScalingDisabledLocked(disable);
                scheduleTraversalLocked(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDisplayAccessUIDsInternal(android.util.SparseArray<android.util.IntArray> newDisplayAccessUIDs) {
        synchronized (this.mSyncRoot) {
            this.mDisplayAccessUIDs.clear();
            for (int i = newDisplayAccessUIDs.size() - 1; i >= 0; i--) {
                this.mDisplayAccessUIDs.append(newDisplayAccessUIDs.keyAt(i), newDisplayAccessUIDs.valueAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUidPresentOnDisplayInternal(int uid, int displayId) {
        boolean z;
        synchronized (this.mSyncRoot) {
            android.util.IntArray displayUIDs = this.mDisplayAccessUIDs.get(displayId);
            z = (displayUIDs == null || displayUIDs.indexOf(uid) == -1) ? false : true;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.IBinder getDisplayToken(int displayId) {
        com.android.server.display.DisplayDevice device;
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display != null && (device = display.getPrimaryDisplayDeviceLocked()) != null) {
                return device.getDisplayTokenLocked();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.window.ScreenCapture.ScreenshotHardwareBuffer systemScreenshotInternal(int displayId) {
        synchronized (this.mSyncRoot) {
            android.os.IBinder token = getDisplayToken(displayId);
            if (token == null) {
                return null;
            }
            com.android.server.display.LogicalDisplay logicalDisplay = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (logicalDisplay == null) {
                return null;
            }
            android.view.DisplayInfo displayInfo = logicalDisplay.getDisplayInfoLocked();
            android.window.ScreenCapture.DisplayCaptureArgs captureArgs = new android.window.ScreenCapture.DisplayCaptureArgs.Builder(token).setSize(displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight()).setCaptureSecureLayers(true).setAllowProtected(true).build();
            return android.window.ScreenCapture.captureDisplay(captureArgs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.window.ScreenCapture.ScreenshotHardwareBuffer userScreenshotInternal(int displayId) {
        synchronized (this.mSyncRoot) {
            android.os.IBinder token = getDisplayToken(displayId);
            if (token == null) {
                return null;
            }
            android.window.ScreenCapture.DisplayCaptureArgs captureArgs = new android.window.ScreenCapture.DisplayCaptureArgs.Builder(token).build();
            return android.window.ScreenCapture.captureDisplay(captureArgs);
        }
    }

    android.hardware.display.DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributesInternal(int displayId) {
        android.os.IBinder token = getDisplayToken(displayId);
        if (token == null) {
            return null;
        }
        return android.view.SurfaceControl.getDisplayedContentSamplingAttributes(token);
    }

    boolean setDisplayedContentSamplingEnabledInternal(int displayId, boolean enable, int componentMask, int maxFrames) {
        android.os.IBinder token = getDisplayToken(displayId);
        if (token == null) {
            return false;
        }
        return android.view.SurfaceControl.setDisplayedContentSamplingEnabled(token, enable, componentMask, maxFrames);
    }

    android.hardware.display.DisplayedContentSample getDisplayedContentSampleInternal(int displayId, long maxFrames, long timestamp) {
        android.os.IBinder token = getDisplayToken(displayId);
        if (token == null) {
            return null;
        }
        return android.view.SurfaceControl.getDisplayedContentSample(token, maxFrames, timestamp);
    }

    void resetBrightnessConfigurations() {
        this.mPersistentDataStore.setBrightnessConfigurationForUser(null, this.mContext.getUserId(), this.mContext.getPackageName());
        this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$resetBrightnessConfigurations$11((com.android.server.display.LogicalDisplay) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetBrightnessConfigurations$11(com.android.server.display.LogicalDisplay logicalDisplay) {
        if (logicalDisplay.getDisplayInfoLocked().type != 1) {
            return;
        }
        java.lang.String uniqueId = logicalDisplay.getPrimaryDisplayDeviceLocked().getUniqueId();
        setBrightnessConfigurationForDisplayInternal(null, uniqueId, this.mContext.getUserId(), this.mContext.getPackageName());
    }

    void setAutoBrightnessLoggingEnabled(boolean enabled) {
        DEBUG = enabled;
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
            if (displayPowerController != null) {
                displayPowerController.setAutoBrightnessLoggingEnabled(enabled);
            }
        }
    }

    void setDisplayWhiteBalanceLoggingEnabled(boolean enabled) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
            if (displayPowerController != null) {
                displayPowerController.setDisplayWhiteBalanceLoggingEnabled(enabled);
            }
        }
    }

    void setDisplayModeDirectorLoggingEnabled(boolean enabled) {
        synchronized (this.mSyncRoot) {
            this.mDisplayModeDirector.setLoggingEnabled(enabled);
        }
    }

    android.view.Display.Mode getActiveDisplayModeAtStart(int displayId) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayDevice device = getDeviceForDisplayLocked(displayId);
            if (device == null) {
                return null;
            }
            return device.getActiveDisplayModeAtStartLocked();
        }
    }

    void setAmbientColorTemperatureOverride(float cct) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(0);
            if (displayPowerController != null) {
                displayPowerController.setAmbientColorTemperatureOverride(cct);
            }
        }
    }

    void setDockedAndIdleEnabled(boolean enabled, int displayId) {
        int i;
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayPowerController displayPowerController = this.mDisplayPowerControllers.get(displayId);
            if (displayPowerController != null) {
                if (enabled) {
                    i = 1;
                } else {
                    i = 0;
                }
                displayPowerController.setAutomaticScreenBrightnessMode(i);
            }
        }
    }

    private void clearViewportsLocked() {
        this.mViewports.clear();
    }

    private java.util.Optional<java.lang.Integer> getViewportType(com.android.server.display.DisplayDeviceInfo info) {
        switch (info.touch) {
            case 1:
                return java.util.Optional.of(1);
            case 2:
                return java.util.Optional.of(2);
            case 3:
                if (!android.text.TextUtils.isEmpty(info.uniqueId)) {
                    return java.util.Optional.of(3);
                }
                break;
        }
        if (DEBUG) {
            android.util.Slog.w(TAG, "Display " + info + " does not support input device matching.");
        }
        return java.util.Optional.empty();
    }

    private void configureDisplayLocked(android.view.SurfaceControl.Transaction t, com.android.server.display.DisplayDevice device) {
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(device, true);
        int tmpId = -1;
        if (display != null) {
            tmpId = display.getDisplayIdLocked();
        }
        boolean hasContent = display != null ? display.hasContentLocked() : false;
        int boundDisplayId = ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).getBondDisplayIdLocked(tmpId, info, hasContent);
        if (boundDisplayId != -1) {
            com.android.server.display.LogicalDisplay tmpDisplay = this.mLogicalDisplayMapper.getDisplayLocked(boundDisplayId);
            if (tmpDisplay != null) {
                display = tmpDisplay;
            } else {
                boundDisplayId = -1;
                android.util.Slog.d(TAG, "LogicalDisplay has destroy but mirage display not remove");
            }
        }
        if (device.mDisplayDeviceExt.isMirageDisplayChangeToMirror(boundDisplayId) && display != null) {
            final int tmpdisplayId = display.getDisplayIdLocked();
            this.mUiHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).onMirrorOutputSurfaceOrientationChanged(tmpdisplayId);
                }
            });
        }
        if (display == null) {
            android.util.Slog.w(TAG, "Missing logical display to use for physical display device: " + device.getDisplayDeviceInfoLocked());
            return;
        }
        if (PANIC_DEBUG) {
            android.util.Slog.d(TAG, "configureDisplayLocked state=" + android.view.Display.stateToString(info.state) + " logicalDisplay=" + display.toStringMini());
        }
        display.configureDisplayLocked(t, device, info.state == 1);
        java.util.Optional<java.lang.Integer> viewportType = getViewportType(info);
        if (viewportType.isPresent()) {
            populateViewportLocked(viewportType.get().intValue(), display.getDisplayIdLocked(), device, info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpecBrightnessInternal(int gear, java.lang.String reason, int rate) {
        synchronized (this.mSyncRoot) {
            this.mDmsExt.setSpecBrightness(gear, reason, rate);
        }
    }

    private android.hardware.display.DisplayViewport getViewportLocked(int viewportType, java.lang.String uniqueId) {
        if (viewportType != 1 && viewportType != 2 && viewportType != 3) {
            android.util.Slog.wtf(TAG, "Cannot call getViewportByTypeLocked for type " + android.hardware.display.DisplayViewport.typeToString(viewportType));
            return null;
        }
        int count = this.mViewports.size();
        for (int i = 0; i < count; i++) {
            android.hardware.display.DisplayViewport viewport = this.mViewports.get(i);
            if (viewport.type == viewportType && uniqueId.equals(viewport.uniqueId)) {
                return viewport;
            }
        }
        android.hardware.display.DisplayViewport viewport2 = new android.hardware.display.DisplayViewport();
        viewport2.type = viewportType;
        viewport2.uniqueId = uniqueId;
        this.mViewports.add(viewport2);
        return viewport2;
    }

    private void populateViewportLocked(int viewportType, int displayId, com.android.server.display.DisplayDevice device, com.android.server.display.DisplayDeviceInfo info) {
        android.hardware.display.DisplayViewport viewport = getViewportLocked(viewportType, info.uniqueId);
        device.populateViewportLocked(viewport);
        viewport.valid = true;
        viewport.displayId = displayId;
        viewport.isActive = android.view.Display.isActiveState(info.state);
    }

    private void updateViewportPowerStateLocked(com.android.server.display.LogicalDisplay display) {
        com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        java.util.Optional<java.lang.Integer> viewportType = getViewportType(info);
        if (viewportType.isPresent()) {
            for (android.hardware.display.DisplayViewport d : this.mViewports) {
                if (d.type == viewportType.get().intValue() && info.uniqueId.equals(d.uniqueId)) {
                    d.isActive = android.view.Display.isActiveState(info.state);
                }
            }
            if (this.mInputManagerInternal != null) {
                this.mHandler.sendEmptyMessage(5);
            }
        }
    }

    private void sendDisplayEventIfEnabledLocked(com.android.server.display.LogicalDisplay display, int event) {
        boolean displayIsEnabled = display.isEnabledLocked();
        if (android.os.Trace.isTagEnabled(131072L)) {
            android.os.Trace.instant(131072L, "sendDisplayEventLocked#event=" + event + ",displayEnabled=" + displayIsEnabled);
        }
        if (displayIsEnabled) {
            sendDisplayEventLocked(display, event);
        } else if (this.mExtraDisplayEventLogging) {
            android.util.Slog.i(TAG, "Not Sending Display Event; display is not enabled: " + display);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDisplayEventLocked(com.android.server.display.LogicalDisplay display, int event) {
        int displayId = display.getDisplayIdLocked();
        android.os.Message msg = this.mHandler.obtainMessage(3, displayId, event);
        if (this.mExtraDisplayEventLogging) {
            android.util.Slog.i(TAG, "Deliver Display Event on Handler: " + event);
        }
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDisplayGroupEvent(int groupId, int event) {
        android.os.Message msg = this.mHandler.obtainMessage(8, groupId, event);
        this.mHandler.sendMessage(msg);
    }

    private void sendDisplayEventFrameRateOverrideLocked(int displayId) {
        android.os.Message msg = this.mHandler.obtainMessage(7, displayId, 2);
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleTraversalLocked(boolean inTraversal) {
        if (!this.mPendingTraversal && this.mWindowManagerInternal != null) {
            this.mDmsExt.scheduleTraversalLocked(inTraversal);
            if (!inTraversal) {
                this.mPendingTraversalCompleted = false;
            }
            this.mPendingTraversal = true;
            if (!inTraversal) {
                this.mHandler.sendEmptyMessage(4);
            }
        }
    }

    private boolean isUidCached(int uid) {
        if (this.mActivityManagerInternal == null || uid < 10000) {
            return false;
        }
        int procState = this.mActivityManagerInternal.getUidProcessState(uid);
        int importance = android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(procState);
        return importance >= 400;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverDisplayEvent(int displayId, android.util.ArraySet<java.lang.Integer> uids, int event) {
        if (4 != event) {
            android.util.Slog.d(TAG, "Delivering display event: displayId=" + displayId + ", event=" + android.hardware.display.DisplayManagerGlobal.eventToString(event) + (uids != null ? ", uids=" + uids : ""));
        }
        if (android.os.Trace.isTagEnabled(131072L)) {
            android.os.Trace.instant(131072L, "deliverDisplayEvent#event=" + event + ",displayId=" + displayId + (uids != null ? ", uids=" + uids : ""));
        }
        synchronized (this.mSyncRoot) {
            int count = this.mCallbacks.size();
            this.mTempCallbacks.clear();
            for (int i = 0; i < count; i++) {
                if (uids == null || uids.contains(java.lang.Integer.valueOf(this.mCallbacks.valueAt(i).mUid))) {
                    this.mTempCallbacks.add(this.mCallbacks.valueAt(i));
                }
            }
        }
        for (int i2 = 0; i2 < this.mTempCallbacks.size(); i2++) {
            com.android.server.display.DisplayManagerService.CallbackRecord callbackRecord = this.mTempCallbacks.get(i2);
            int uid = callbackRecord.mUid;
            int pid = callbackRecord.mPid;
            if (isUidCached(uid)) {
                synchronized (this.mPendingCallbackSelfLocked) {
                    android.util.SparseArray<com.android.server.display.DisplayManagerService.PendingCallback> pendingCallbacks = this.mPendingCallbackSelfLocked.get(uid);
                    if (extraLogging(callbackRecord.mPackageName)) {
                        android.util.Slog.i(TAG, "Uid is cached: " + uid + ", pendingCallbacks: " + pendingCallbacks);
                    }
                    if (pendingCallbacks == null) {
                        pendingCallbacks = new android.util.SparseArray<>();
                        this.mPendingCallbackSelfLocked.put(uid, pendingCallbacks);
                    }
                    com.android.server.display.DisplayManagerService.PendingCallback pendingCallback = pendingCallbacks.get(pid);
                    if (pendingCallback == null) {
                        pendingCallbacks.put(pid, new com.android.server.display.DisplayManagerService.PendingCallback(callbackRecord, displayId, event));
                    } else {
                        pendingCallback.addDisplayEvent(displayId, event);
                    }
                }
            } else {
                callbackRecord.notifyDisplayEventAsync(displayId, event);
            }
        }
        this.mTempCallbacks.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean extraLogging(java.lang.String packageName) {
        return this.mExtraDisplayEventLogging && this.mExtraDisplayLoggingPackageName.equals(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliverDisplayGroupEvent(int groupId, int event) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Delivering display group event: groupId=" + groupId + ", event=" + event);
        }
        switch (event) {
            case 1:
                for (android.hardware.display.DisplayManagerInternal.DisplayGroupListener listener : this.mDisplayGroupListeners) {
                    listener.onDisplayGroupAdded(groupId);
                }
                break;
            case 2:
                for (android.hardware.display.DisplayManagerInternal.DisplayGroupListener listener2 : this.mDisplayGroupListeners) {
                    listener2.onDisplayGroupChanged(groupId);
                }
                break;
            case 3:
                for (android.hardware.display.DisplayManagerInternal.DisplayGroupListener listener3 : this.mDisplayGroupListeners) {
                    listener3.onDisplayGroupRemoved(groupId);
                }
                break;
        }
    }

    private android.media.projection.IMediaProjectionManager getProjectionService() {
        if (this.mProjectionService == null) {
            this.mProjectionService = this.mInjector.getProjectionService();
        }
        return this.mProjectionService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.UserManager getUserManager() {
        return (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(final java.io.PrintWriter pw, java.lang.String[] args) {
        com.android.server.display.BrightnessTracker brightnessTrackerLocal;
        synchronized (this.mSyncDump) {
            if (this.mDumpInProgress) {
                pw.println("One dump is in service already.");
                return;
            }
            this.mDumpInProgress = true;
            pw.println("DISPLAY MANAGER (dumpsys display)");
            synchronized (this.mSyncRoot) {
                brightnessTrackerLocal = this.mBrightnessTracker;
                pw.println("  mSafeMode=" + this.mSafeMode);
                pw.println("  mPendingTraversal=" + this.mPendingTraversal);
                pw.println("  mViewports=" + this.mViewports);
                pw.println("  mDefaultDisplayDefaultColorMode=" + this.mDefaultDisplayDefaultColorMode);
                pw.println("  mWifiDisplayScanRequestCount=" + this.mWifiDisplayScanRequestCount);
                pw.println("  mStableDisplaySize=" + this.mStableDisplaySize);
                pw.println("  mMinimumBrightnessCurve=" + this.mMinimumBrightnessCurve);
                if (this.mUserPreferredMode != null) {
                    pw.println(" mUserPreferredMode=" + this.mUserPreferredMode);
                }
                pw.println();
                if (!this.mAreUserDisabledHdrTypesAllowed) {
                    pw.println("  mUserDisabledHdrTypes: size=" + this.mUserDisabledHdrTypes.length);
                    for (int type : this.mUserDisabledHdrTypes) {
                        pw.println("  " + type);
                    }
                }
                if (this.mHdrConversionMode != null) {
                    pw.println("  mHdrConversionMode=" + this.mHdrConversionMode);
                }
                pw.println();
                int displayStateCount = this.mDisplayStates.size();
                pw.println("Display States: size=" + displayStateCount);
                for (int i = 0; i < displayStateCount; i++) {
                    int displayId = this.mDisplayStates.keyAt(i);
                    int displayState = this.mDisplayStates.valueAt(i);
                    com.android.server.display.DisplayManagerService.BrightnessPair brightnessPair = this.mDisplayBrightnesses.valueAt(i);
                    pw.println("  Display Id=" + displayId);
                    pw.println("  Display State=" + android.view.Display.stateToString(displayState));
                    pw.println("  Display Brightness=" + brightnessPair.brightness);
                    pw.println("  Display SdrBrightness=" + brightnessPair.sdrBrightness);
                }
                final java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(pw, "    ");
                indentingPrintWriter.increaseIndent();
                pw.println();
                pw.println("Display Adapters: size=" + this.mDisplayAdapters.size());
                for (com.android.server.display.DisplayAdapter adapter : this.mDisplayAdapters) {
                    pw.println("  " + adapter.getName());
                    adapter.dumpLocked(indentingPrintWriter);
                }
                pw.println();
                pw.println("Display Devices: size=" + this.mDisplayDeviceRepo.sizeLocked());
                this.mDisplayDeviceRepo.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda9
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.display.DisplayManagerService.lambda$dumpInternal$13(pw, indentingPrintWriter, (com.android.server.display.DisplayDevice) obj);
                    }
                });
                pw.println();
                this.mLogicalDisplayMapper.dumpLocked(pw);
                int callbackCount = this.mCallbacks.size();
                pw.println();
                pw.println("Callbacks: size=" + callbackCount);
                for (int i2 = 0; i2 < callbackCount; i2++) {
                    com.android.server.display.DisplayManagerService.CallbackRecord callback = this.mCallbacks.valueAt(i2);
                    pw.println("  " + i2 + ": " + callback + ", mWifiDisplayScanRequested=" + callback.mWifiDisplayScanRequested);
                }
                int pendingCount = this.mPendingCallbackSelfLocked.size();
                pw.println("PendingCallbacks: size=" + pendingCount);
                for (int i3 = 0; i3 < pendingCount; i3++) {
                    android.util.SparseArray<com.android.server.display.DisplayManagerService.PendingCallback> pendingCallbacks = this.mPendingCallbackSelfLocked.valueAt(i3);
                    pw.println("  " + i3 + ": " + pendingCallbacks);
                }
                int binderDiedCount = this.mBinderDiedPids.size();
                pw.println("BinderDiedPids: size=" + binderDiedCount);
                try {
                    for (java.util.Map.Entry<java.lang.Integer, java.lang.Long> entry : this.mBinderDiedPids.entrySet()) {
                        java.time.LocalDateTime dateTime = java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(entry.getValue().longValue()), java.time.ZoneId.systemDefault());
                        pw.println("binderDied: " + dateTime + ", Pid: " + entry.getKey());
                    }
                } catch (java.lang.NumberFormatException | java.time.DateTimeException e) {
                }
                int displayPowerControllerCount = this.mDisplayPowerControllers.size();
                pw.println();
                pw.println("Display Power Controllers: size=" + displayPowerControllerCount);
                for (int i4 = 0; i4 < displayPowerControllerCount; i4++) {
                    this.mDisplayPowerControllers.valueAt(i4).dump(pw);
                }
                pw.println();
                this.mPersistentDataStore.dump(pw);
                int displayWindowPolicyControllerCount = this.mDisplayWindowPolicyControllers.size();
                pw.println();
                pw.println("Display Window Policy Controllers: size=" + displayWindowPolicyControllerCount);
                for (int i5 = 0; i5 < displayWindowPolicyControllerCount; i5++) {
                    pw.print("Display " + this.mDisplayWindowPolicyControllers.keyAt(i5) + ":");
                    ((android.window.DisplayWindowPolicyController) this.mDisplayWindowPolicyControllers.valueAt(i5).second).dump("  ", pw);
                }
            }
            if (brightnessTrackerLocal != null) {
                pw.println();
                brightnessTrackerLocal.dump(pw);
            }
            pw.println();
            this.mDisplayModeDirector.dump(pw);
            this.mBrightnessSynchronizer.dump(pw);
            if (this.mSmallAreaDetectionController != null) {
                this.mSmallAreaDetectionController.dump(pw);
            }
            pw.println();
            this.mFlags.dump(pw);
            synchronized (this.mSyncDump) {
                this.mDumpInProgress = false;
            }
        }
    }

    static /* synthetic */ void lambda$dumpInternal$13(java.io.PrintWriter pw, android.util.IndentingPrintWriter ipw, com.android.server.display.DisplayDevice device) {
        pw.println("  " + device.getDisplayDeviceInfoLocked());
        device.dumpLocked(ipw);
    }

    private static float[] getFloatArray(android.content.res.TypedArray array) {
        int length = array.length();
        float[] floatArray = new float[length];
        for (int i = 0; i < length; i++) {
            floatArray[i] = array.getFloat(i, Float.NaN);
        }
        array.recycle();
        return floatArray;
    }

    private static boolean isResolutionAndRefreshRateValid(android.view.Display.Mode mode) {
        return mode.getPhysicalWidth() > 0 && mode.getPhysicalHeight() > 0 && mode.getRefreshRate() > 0.0f;
    }

    void enableConnectedDisplay(int displayId, boolean enabled) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay logicalDisplay = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (logicalDisplay == null) {
                android.util.Slog.w(TAG, "enableConnectedDisplay: Can not find displayId=" + displayId);
            } else if (com.android.server.display.ExternalDisplayPolicy.isExternalDisplayLocked(logicalDisplay)) {
                this.mExternalDisplayPolicy.setExternalDisplayEnabledLocked(logicalDisplay, enabled);
            } else {
                this.mLogicalDisplayMapper.setDisplayEnabledLocked(logicalDisplay, enabled);
            }
        }
    }

    boolean requestDisplayPower(int displayId, boolean on) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display == null) {
                android.util.Slog.w(TAG, "requestDisplayPower: Cannot find a display with displayId=" + displayId);
                return false;
            }
            com.android.server.display.DisplayManagerService.BrightnessPair brightnessPair = this.mDisplayBrightnesses.get(displayId);
            java.lang.Runnable runnable = display.getPrimaryDisplayDeviceLocked().requestDisplayStateLocked(on ? 2 : 1, on ? brightnessPair.brightness : -1.0f, brightnessPair.sdrBrightness, display.getDisplayOffloadSessionLocked());
            if (runnable == null) {
                android.util.Slog.w(TAG, "requestDisplayPower: Cannot update the power state to ON=" + on + " for a display with displayId=" + displayId + ", runnable is null");
                return false;
            }
            runnable.run();
            android.util.Slog.i(TAG, "requestDisplayPower(displayId=" + displayId + ", on=" + on + ")");
            return true;
        }
    }

    static class Injector {
        Injector() {
        }

        com.android.server.display.VirtualDisplayAdapter getVirtualDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener displayAdapterListener, com.android.server.display.feature.DisplayManagerFlags flags) {
            return new com.android.server.display.VirtualDisplayAdapter(syncRoot, context, handler, displayAdapterListener, flags);
        }

        com.android.server.display.LocalDisplayAdapter getLocalDisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener displayAdapterListener, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.notifications.DisplayNotificationManager displayNotificationManager) {
            return new com.android.server.display.LocalDisplayAdapter(syncRoot, context, handler, displayAdapterListener, flags, displayNotificationManager);
        }

        long getDefaultDisplayDelayTimeout() {
            return 10000L;
        }

        int setHdrConversionMode(int conversionMode, int preferredHdrOutputType, int[] autoHdrTypes) {
            return com.android.server.display.DisplayControl.setHdrConversionMode(conversionMode, preferredHdrOutputType, autoHdrTypes);
        }

        int[] getSupportedHdrOutputTypes() {
            return com.android.server.display.DisplayControl.getSupportedHdrOutputTypes();
        }

        int[] getHdrOutputTypesWithLatency() {
            return com.android.server.display.DisplayControl.getHdrOutputTypesWithLatency();
        }

        boolean getHdrOutputConversionSupport() {
            return com.android.server.display.DisplayControl.getHdrOutputConversionSupport();
        }

        android.media.projection.IMediaProjectionManager getProjectionService() {
            android.os.IBinder b = android.os.ServiceManager.getService("media_projection");
            return android.media.projection.IMediaProjectionManager.Stub.asInterface(b);
        }

        com.android.server.display.feature.DisplayManagerFlags getFlags() {
            return new com.android.server.display.feature.DisplayManagerFlags();
        }
    }

    com.android.server.display.DisplayDeviceInfo getDisplayDeviceInfoInternal(int displayId) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (display == null) {
                return null;
            }
            com.android.server.display.DisplayDevice displayDevice = display.getPrimaryDisplayDeviceLocked();
            return displayDevice.getDisplayDeviceInfoLocked();
        }
    }

    android.view.Surface getVirtualDisplaySurfaceInternal(android.os.IBinder appToken) {
        synchronized (this.mSyncRoot) {
            if (this.mVirtualDisplayAdapter == null) {
                return null;
            }
            return this.mVirtualDisplayAdapter.getVirtualDisplaySurfaceLocked(appToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeDisplayPowerControllersLocked() {
        this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.addDisplayPowerControllerLocked((com.android.server.display.LogicalDisplay) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDisplayPowerControllerLocked(final com.android.server.display.LogicalDisplay display) {
        if (this.mPowerHandler == null) {
            return;
        }
        com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        int type = info != null ? info.type : 0;
        if (type == 1 && display.isEnabledLocked()) {
            if (this.mBrightnessTracker == null && display.getDisplayIdLocked() == 0) {
                this.mBrightnessTracker = new com.android.server.display.BrightnessTracker(this.mContext, null);
            }
            int userSerial = getUserManager().getUserSerialNumber(this.mContext.getUserId());
            com.android.server.display.BrightnessSetting brightnessSetting = new com.android.server.display.BrightnessSetting(userSerial, this.mPersistentDataStore, display, this.mSyncRoot);
            com.android.server.display.HighBrightnessModeMetadata hbmMetadata = this.mHighBrightnessModeMetadataMapper.getHighBrightnessModeMetadataLocked(display);
            com.android.server.display.DisplayPowerController displayPowerController = new com.android.server.display.DisplayPowerController(this.mContext, null, this.mDisplayPowerCallbacks, this.mPowerHandler, this.mSensorManager, this.mDisplayBlanker, display, this.mBrightnessTracker, brightnessSetting, new java.lang.Runnable() { // from class: com.android.server.display.DisplayManagerService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$addDisplayPowerControllerLocked$14(display);
                }
            }, hbmMetadata, this.mBootCompleted, this.mFlags);
            this.mDisplayPowerControllers.append(display.getDisplayIdLocked(), displayPowerController);
            displayPowerController.getWrapper().setLogicalDisplayMapper(this.mLogicalDisplayMapper);
            this.mDmsExt.addDisplayPowerControllerLocked(display, displayPowerController);
            return;
        }
        if (type == 0) {
            android.util.Slog.e(TAG, "maybe display init error display=" + display);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleBrightnessChange, reason: merged with bridge method [inline-methods] */
    public void lambda$addDisplayPowerControllerLocked$14(com.android.server.display.LogicalDisplay display) {
        synchronized (this.mSyncRoot) {
            sendDisplayEventIfEnabledLocked(display, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.display.DisplayDevice getDeviceForDisplayLocked(int displayId) {
        com.android.server.display.LogicalDisplay display = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
        if (display == null) {
            return null;
        }
        return display.getPrimaryDisplayDeviceLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.display.BrightnessConfiguration getBrightnessConfigForDisplayWithPdsFallbackLocked(java.lang.String uniqueId, int userSerial) {
        android.hardware.display.BrightnessConfiguration config = this.mPersistentDataStore.getBrightnessConfigurationForDisplayLocked(uniqueId, userSerial);
        if (config == null) {
            return this.mPersistentDataStore.getBrightnessConfiguration(userSerial);
        }
        return config;
    }

    private final class DisplayManagerHandler extends android.os.Handler {
        public DisplayManagerHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            boolean changed;
            switch (msg.what) {
                case 1:
                    com.android.server.display.DisplayManagerService.this.registerDefaultDisplayAdapters();
                    return;
                case 2:
                    com.android.server.display.DisplayManagerService.this.registerAdditionalDisplayAdapters();
                    return;
                case 3:
                    com.android.server.display.DisplayManagerService.this.deliverDisplayEvent(msg.arg1, null, msg.arg2);
                    return;
                case 4:
                    android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "MSG_REQUEST_TRAVERSAL pendingCompleted=" + com.android.server.display.DisplayManagerService.this.mPendingTraversalCompleted);
                    com.android.server.display.DisplayManagerService.this.mPendingTraversalCompleted = true;
                    com.android.server.display.DisplayManagerService.this.mWindowManagerInternal.requestTraversalFromDisplayManager();
                    return;
                case 5:
                    synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                        changed = true ^ com.android.server.display.DisplayManagerService.this.mTempViewports.equals(com.android.server.display.DisplayManagerService.this.mViewports);
                        if (changed) {
                            com.android.server.display.DisplayManagerService.this.mTempViewports.clear();
                            for (android.hardware.display.DisplayViewport d : com.android.server.display.DisplayManagerService.this.mViewports) {
                                com.android.server.display.DisplayManagerService.this.mTempViewports.add(d.makeCopy());
                            }
                        }
                        break;
                    }
                    if (changed) {
                        com.android.server.display.DisplayManagerService.this.mInputManagerInternal.setDisplayViewports(com.android.server.display.DisplayManagerService.this.mTempViewports);
                        return;
                    }
                    return;
                case 6:
                    com.android.server.display.DisplayManagerService.this.loadBrightnessConfigurations();
                    return;
                case 7:
                    synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                        int displayId = msg.arg1;
                        com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                        if (display != null) {
                            android.util.ArraySet<java.lang.Integer> uids = display.getPendingFrameRateOverrideUids();
                            display.clearPendingFrameRateOverrideUids();
                            com.android.server.display.DisplayManagerService.this.deliverDisplayEvent(msg.arg1, uids, msg.arg2);
                        }
                    }
                    return;
                case 8:
                    com.android.server.display.DisplayManagerService.this.deliverDisplayGroupEvent(msg.arg1, msg.arg2);
                    return;
                case 9:
                    com.android.server.display.DisplayManagerService.this.mWindowManagerInternal.onDisplayManagerReceivedDeviceState(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    private final class LogicalDisplayListener implements com.android.server.display.LogicalDisplayMapper.Listener {
        private LogicalDisplayListener() {
        }

        @Override // com.android.server.display.LogicalDisplayMapper.Listener
        public void onLogicalDisplayEventLocked(com.android.server.display.LogicalDisplay display, int event) {
            switch (event) {
                case 1:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayAddedLocked(display);
                    break;
                case 2:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayChangedLocked(display);
                    break;
                case 3:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayRemovedLocked(display);
                    break;
                case 4:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplaySwappedLocked(display);
                    break;
                case 5:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayFrameRateOverridesChangedLocked(display);
                    break;
                case 6:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayDeviceStateTransitionLocked(display);
                    break;
                case 7:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayHdrSdrRatioChangedLocked(display);
                    break;
                case 8:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayConnectedLocked(display);
                    break;
                case 9:
                    com.android.server.display.DisplayManagerService.this.handleLogicalDisplayDisconnectedLocked(display);
                    break;
            }
        }

        @Override // com.android.server.display.LogicalDisplayMapper.Listener
        public void onDisplayGroupEventLocked(int groupId, int event) {
            com.android.server.display.DisplayManagerService.this.sendDisplayGroupEvent(groupId, event);
        }

        @Override // com.android.server.display.LogicalDisplayMapper.Listener
        public void onTraversalRequested() {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayManagerService.this.scheduleTraversalLocked(false);
            }
        }
    }

    private final class CallbackRecord implements android.os.IBinder.DeathRecipient {
        private final android.hardware.display.IDisplayManagerCallback mCallback;
        private java.util.concurrent.atomic.AtomicLong mEventsMask;
        private final java.lang.String mPackageName;
        public final int mPid;
        public final int mUid;
        public boolean mWifiDisplayScanRequested;

        CallbackRecord(int pid, int uid, android.hardware.display.IDisplayManagerCallback callback, long eventsMask) {
            this.mPid = pid;
            this.mUid = uid;
            this.mCallback = callback;
            this.mEventsMask = new java.util.concurrent.atomic.AtomicLong(eventsMask);
            java.lang.String[] packageNames = com.android.server.display.DisplayManagerService.this.mContext.getPackageManager().getPackagesForUid(uid);
            this.mPackageName = packageNames == null ? null : packageNames[0];
        }

        public void updateEventsMask(long eventsMask) {
            this.mEventsMask.set(eventsMask);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.display.DisplayManagerService.DEBUG || com.android.server.display.DisplayManagerService.this.extraLogging(this.mPackageName)) {
                android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "Display listener for pid " + this.mPid + " died.");
            }
            if (android.os.Trace.isTagEnabled(131072L)) {
                android.os.Trace.instant(131072L, "displayManagerBinderDied#mPid=" + this.mPid);
            }
            com.android.server.display.DisplayManagerService.this.onCallbackDied(this);
        }

        public boolean notifyDisplayEventAsync(int displayId, int event) {
            if (!shouldSendEvent(event)) {
                if (com.android.server.display.DisplayManagerService.this.extraLogging(this.mPackageName)) {
                    android.util.Slog.i(com.android.server.display.DisplayManagerService.TAG, "Not sending displayEvent: " + event + " due to mask:" + this.mEventsMask);
                }
                if (android.os.Trace.isTagEnabled(131072L)) {
                    android.os.Trace.instant(131072L, "notifyDisplayEventAsync#notSendingEvent=" + event + ",mEventsMask=" + this.mEventsMask);
                }
                return true;
            }
            try {
                this.mCallback.onDisplayEvent(displayId, event);
                return true;
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.display.DisplayManagerService.TAG, "Failed to notify process " + this.mPid + " that displays changed, assuming it died.", ex);
                binderDied();
                return false;
            }
        }

        private boolean shouldSendEvent(int event) {
            long mask = this.mEventsMask.get();
            switch (event) {
                case 1:
                    if ((1 & mask) != 0) {
                    }
                    break;
                case 2:
                    if ((4 & mask) != 0) {
                    }
                    break;
                case 3:
                    if ((2 & mask) != 0) {
                    }
                    break;
                case 4:
                    if ((8 & mask) != 0) {
                    }
                    break;
                case 5:
                    if ((16 & mask) != 0) {
                    }
                    break;
                case 6:
                case 7:
                    if ((32 & mask) != 0) {
                    }
                    break;
                default:
                    android.util.Slog.e(com.android.server.display.DisplayManagerService.TAG, "Unknown display event " + event);
                    break;
            }
            return true;
        }

        public java.lang.String toString() {
            java.lang.String info = java.lang.String.format("name=%s, ids=[%d,%d,%d]", this.mPackageName, java.lang.Integer.valueOf(this.mPid), java.lang.Integer.valueOf(this.mUid), java.lang.Long.valueOf(this.mEventsMask.get()));
            return info;
        }
    }

    private static final class PendingCallback {
        private final com.android.server.display.DisplayManagerService.CallbackRecord mCallbackRecord;
        private final java.util.ArrayList<android.util.Pair<java.lang.Integer, java.lang.Integer>> mDisplayEvents = new java.util.ArrayList<>();

        PendingCallback(com.android.server.display.DisplayManagerService.CallbackRecord cr, int displayId, int event) {
            this.mCallbackRecord = cr;
            this.mDisplayEvents.add(new android.util.Pair<>(java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(event)));
        }

        public void addDisplayEvent(int displayId, int event) {
            android.util.Pair<java.lang.Integer, java.lang.Integer> last = this.mDisplayEvents.get(this.mDisplayEvents.size() - 1);
            if (((java.lang.Integer) last.first).intValue() == displayId && ((java.lang.Integer) last.second).intValue() == event) {
                return;
            }
            this.mDisplayEvents.add(new android.util.Pair<>(java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(event)));
        }

        public void sendPendingDisplayEvent() {
            int i = 0;
            while (true) {
                if (i >= this.mDisplayEvents.size()) {
                    break;
                }
                android.util.Pair<java.lang.Integer, java.lang.Integer> displayEvent = this.mDisplayEvents.get(i);
                if (com.android.server.display.DisplayManagerService.DEBUG) {
                    android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "Send pending display event #" + i + " " + displayEvent.first + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + displayEvent.second + " to " + this.mCallbackRecord.mUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mCallbackRecord.mPid);
                }
                if (this.mCallbackRecord.notifyDisplayEventAsync(((java.lang.Integer) displayEvent.first).intValue(), ((java.lang.Integer) displayEvent.second).intValue())) {
                    i++;
                } else {
                    android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "Drop pending events for dead process " + this.mCallbackRecord.mPid);
                    break;
                }
            }
            this.mDisplayEvents.clear();
        }

        public java.lang.String toString() {
            return this.mCallbackRecord.toString() + "event size=" + this.mDisplayEvents.size();
        }
    }

    final class BinderService extends android.hardware.display.IDisplayManager.Stub {
        BinderService() {
        }

        public android.view.DisplayInfo getDisplayInfo(int displayId) {
            int callingUid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getDisplayInfoInternal(displayId, callingUid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int[] getDisplayIds(boolean includeDisabled) {
            int[] displayIdsLocked;
            int callingUid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    displayIdsLocked = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayIdsLocked(callingUid, includeDisabled);
                }
                return displayIdsLocked;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isUidPresentOnDisplay(int uid, int displayId) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.isUidPresentOnDisplayInternal(uid, displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.graphics.Point getStableDisplaySize() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getStableDisplaySizeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void registerCallback(android.hardware.display.IDisplayManagerCallback callback) {
            registerCallbackWithEventMask(callback, 7L);
        }

        public void registerCallbackWithEventMask(android.hardware.display.IDisplayManagerCallback callback, long eventsMask) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("listener must not be null");
            }
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            if (com.android.server.display.DisplayManagerService.this.mFlags.isConnectedDisplayManagementEnabled() && (32 & eventsMask) != 0) {
                com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DISPLAYS", "Permission required to get signals about connection events.");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.registerCallbackInternal(callback, callingPid, callingUid, eventsMask);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void startWifiDisplayScan() {
            startWifiDisplayScan_enforcePermission();
            int callingPid = android.os.Binder.getCallingPid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.startWifiDisplayScanInternal(callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void stopWifiDisplayScan() {
            stopWifiDisplayScan_enforcePermission();
            int callingPid = android.os.Binder.getCallingPid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.stopWifiDisplayScanInternal(callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void connectWifiDisplay(java.lang.String address) {
            if (address == null) {
                throw new java.lang.IllegalArgumentException("address must not be null");
            }
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to connect to a wifi display");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.connectWifiDisplayInternal(address);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void disconnectWifiDisplay() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.disconnectWifiDisplayInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void renameWifiDisplay(java.lang.String address, java.lang.String alias) {
            if (address == null) {
                throw new java.lang.IllegalArgumentException("address must not be null");
            }
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to rename to a wifi display");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.renameWifiDisplayInternal(address, alias);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void forgetWifiDisplay(java.lang.String address) {
            if (address == null) {
                throw new java.lang.IllegalArgumentException("address must not be null");
            }
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY", "Permission required to forget to a wifi display");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.forgetWifiDisplayInternal(address);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void pauseWifiDisplay() {
            pauseWifiDisplay_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.pauseWifiDisplayInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void resumeWifiDisplay() {
            resumeWifiDisplay_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.resumeWifiDisplayInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.WifiDisplayStatus getWifiDisplayStatus() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getWifiDisplayStatusInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setUserDisabledHdrTypes(int[] userDisabledFormats) {
            setUserDisabledHdrTypes_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setUserDisabledHdrTypesInternal(userDisabledFormats);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void overrideHdrTypes(int displayId, int[] modes) {
            android.os.IBinder displayToken;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                displayToken = com.android.server.display.DisplayManagerService.this.getDisplayToken(displayId);
                if (displayToken == null) {
                    throw new java.lang.IllegalArgumentException("Invalid display: " + displayId);
                }
            }
            com.android.server.display.DisplayControl.overrideHdrTypes(displayToken, modes);
        }

        public void setAreUserDisabledHdrTypesAllowed(boolean areUserDisabledHdrTypesAllowed) {
            setAreUserDisabledHdrTypesAllowed_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setAreUserDisabledHdrTypesAllowedInternal(areUserDisabledHdrTypesAllowed);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean areUserDisabledHdrTypesAllowed() {
            boolean z;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                z = com.android.server.display.DisplayManagerService.this.mAreUserDisabledHdrTypesAllowed;
            }
            return z;
        }

        public int[] getUserDisabledHdrTypes() {
            int[] iArr;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                iArr = com.android.server.display.DisplayManagerService.this.mUserDisabledHdrTypes;
            }
            return iArr;
        }

        public void requestColorMode(int displayId, int colorMode) {
            requestColorMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.requestColorModeInternal(displayId, colorMode);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int createVirtualDisplay(android.hardware.display.VirtualDisplayConfig virtualDisplayConfig, android.hardware.display.IVirtualDisplayCallback callback, android.media.projection.IMediaProjection projection, java.lang.String packageName) {
            return com.android.server.display.DisplayManagerService.this.createVirtualDisplayInternal(virtualDisplayConfig, callback, projection, null, null, packageName);
        }

        public void resizeVirtualDisplay(android.hardware.display.IVirtualDisplayCallback callback, int width, int height, int densityDpi) {
            if (width <= 0 || height <= 0 || densityDpi <= 0) {
                throw new java.lang.IllegalArgumentException("width, height, and densityDpi must be greater than 0");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.resizeVirtualDisplayInternal(callback.asBinder(), width, height, densityDpi);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setVirtualDisplaySurface(android.hardware.display.IVirtualDisplayCallback callback, android.view.Surface surface) {
            if (surface != null && surface.isSingleBuffered()) {
                throw new java.lang.IllegalArgumentException("Surface can't be single-buffered");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setVirtualDisplaySurfaceInternal(callback.asBinder(), surface);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void releaseVirtualDisplay(android.hardware.display.IVirtualDisplayCallback callback) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.releaseVirtualDisplayInternal(callback.asBinder());
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setVirtualDisplayState(android.hardware.display.IVirtualDisplayCallback callback, boolean isOn) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setVirtualDisplayStateInternal(callback.asBinder(), isOn);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.display.DisplayManagerService.this.mContext, com.android.server.display.DisplayManagerService.TAG, pw) || com.android.server.display.DisplayManagerService.this.mDmsExt.dynamicallyConfigDebug(pw, args)) {
                return;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.dumpInternal(pw, args);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.content.pm.ParceledListSlice<android.hardware.display.BrightnessChangeEvent> getBrightnessEvents(java.lang.String callingPackage) {
            boolean hasUsageStats;
            android.content.pm.ParceledListSlice<android.hardware.display.BrightnessChangeEvent> brightnessEvents;
            getBrightnessEvents_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            android.app.AppOpsManager appOpsManager = (android.app.AppOpsManager) com.android.server.display.DisplayManagerService.this.mContext.getSystemService(android.app.AppOpsManager.class);
            int mode = appOpsManager.noteOp(43, callingUid, callingPackage);
            if (mode == 3) {
                hasUsageStats = com.android.server.display.DisplayManagerService.this.mContext.checkCallingPermission("android.permission.PACKAGE_USAGE_STATS") == 0;
            } else {
                hasUsageStats = mode == 0;
            }
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    brightnessEvents = ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).getBrightnessEvents(userId, hasUsageStats);
                }
                return brightnessEvents;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.content.pm.ParceledListSlice<android.hardware.display.AmbientBrightnessDayStats> getAmbientBrightnessStats() {
            android.content.pm.ParceledListSlice<android.hardware.display.AmbientBrightnessDayStats> ambientBrightnessStats;
            getAmbientBrightnessStats_enforcePermission();
            int callingUid = android.os.Binder.getCallingUid();
            int userId = android.os.UserHandle.getUserId(callingUid);
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    ambientBrightnessStats = ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).getAmbientBrightnessStats(userId);
                }
                return ambientBrightnessStats;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setBrightnessConfigurationForUser(final android.hardware.display.BrightnessConfiguration c, final int userId, final java.lang.String packageName) {
            setBrightnessConfigurationForUser_enforcePermission();
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "Permission required to change the display brightness configuration of another user");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$BinderService$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$setBrightnessConfigurationForUser$0(c, userId, packageName, (com.android.server.display.LogicalDisplay) obj);
                        }
                    });
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setBrightnessConfigurationForUser$0(android.hardware.display.BrightnessConfiguration c, int userId, java.lang.String packageName, com.android.server.display.LogicalDisplay logicalDisplay) {
            if (logicalDisplay.getDisplayInfoLocked().type != 1) {
                return;
            }
            com.android.server.display.DisplayDevice displayDevice = logicalDisplay.getPrimaryDisplayDeviceLocked();
            com.android.server.display.DisplayManagerService.this.setBrightnessConfigurationForDisplayInternal(c, displayDevice.getUniqueId(), userId, packageName);
        }

        public void setBrightnessConfigurationForDisplay(android.hardware.display.BrightnessConfiguration c, java.lang.String uniqueId, int userId, java.lang.String packageName) {
            setBrightnessConfigurationForDisplay_enforcePermission();
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "Permission required to change the display brightness configuration of another user");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setBrightnessConfigurationForDisplayInternal(c, uniqueId, userId, packageName);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.BrightnessConfiguration getBrightnessConfigurationForDisplay(java.lang.String uniqueId, int userId) {
            android.hardware.display.BrightnessConfiguration config;
            com.android.server.display.DisplayPowerController dpc;
            getBrightnessConfigurationForDisplay_enforcePermission();
            if (userId != android.os.UserHandle.getCallingUserId()) {
                com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS", "Permission required to read the display brightness configuration of another user");
            }
            long token = android.os.Binder.clearCallingIdentity();
            int userSerial = com.android.server.display.DisplayManagerService.this.getUserManager().getUserSerialNumber(userId);
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    config = com.android.server.display.DisplayManagerService.this.getBrightnessConfigForDisplayWithPdsFallbackLocked(uniqueId, userSerial);
                    if (config == null && (dpc = com.android.server.display.DisplayManagerService.this.getDpcFromUniqueIdLocked(uniqueId)) != null) {
                        config = dpc.getDefaultBrightnessConfiguration();
                    }
                }
                return config;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.BrightnessConfiguration getBrightnessConfigurationForUser(int userId) {
            java.lang.String uniqueId;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayDevice displayDevice = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(0).getPrimaryDisplayDeviceLocked();
                uniqueId = displayDevice.getUniqueId();
            }
            return getBrightnessConfigurationForDisplay(uniqueId, userId);
        }

        public android.hardware.display.BrightnessConfiguration getDefaultBrightnessConfiguration() {
            android.hardware.display.BrightnessConfiguration defaultBrightnessConfiguration;
            getDefaultBrightnessConfiguration_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    defaultBrightnessConfiguration = ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).getDefaultBrightnessConfiguration();
                }
                return defaultBrightnessConfiguration;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.BrightnessInfo getBrightnessInfo(int displayId) {
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to read the display's brightness info.");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId, false);
                    if (display != null && display.isEnabledLocked()) {
                        com.android.server.display.DisplayPowerController dpc = (com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId);
                        if (dpc == null) {
                            return null;
                        }
                        return dpc.getBrightnessInfo();
                    }
                    return null;
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isMinimalPostProcessingRequested(int displayId) {
            boolean requestedMinimalPostProcessingLocked;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                requestedMinimalPostProcessingLocked = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId).getRequestedMinimalPostProcessingLocked();
            }
            return requestedMinimalPostProcessingLocked;
        }

        public void setTemporaryBrightness(int displayId, float brightness) {
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's brightness");
            if (com.android.server.display.DisplayManagerService.PANIC_DEBUG) {
                int uid = android.os.Binder.getCallingUid();
                int pid = android.os.Binder.getCallingPid();
                java.lang.String msg = java.lang.String.format("id=%d,brightness=%f,calling(%d,%d)", java.lang.Integer.valueOf(displayId), java.lang.Float.valueOf(brightness), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid));
                android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "setTemporaryBrightness=" + msg);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId)).setTemporaryBrightness(brightness);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setBrightness(int displayId, float brightness) {
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's brightness");
            if (!com.android.server.display.DisplayManagerService.this.mDmsExt.isValidBrightness(displayId, brightness)) {
                android.util.Slog.w(com.android.server.display.DisplayManagerService.TAG, "Attempted to set invalid brightness" + brightness);
                return;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    com.android.server.display.DisplayPowerController dpc = (com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId);
                    if (dpc != null) {
                        dpc.setBrightness(brightness);
                    }
                    com.android.server.display.DisplayManagerService.this.mPersistentDataStore.saveIfNeeded();
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public float getBrightness(int displayId) {
            float brightness = Float.NaN;
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's brightness");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    com.android.server.display.DisplayPowerController dpc = (com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId);
                    if (dpc != null) {
                        brightness = dpc.getScreenBrightnessSetting();
                    }
                }
                return brightness;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setTemporaryAutoBrightnessAdjustment(float adjustment) {
            int uid = android.os.Binder.getCallingUid();
            int pid = android.os.Binder.getCallingPid();
            if (com.android.server.display.DisplayManagerService.PANIC_DEBUG) {
                java.lang.String info = java.lang.String.format("%f,calling(%d,%d)", java.lang.Float.valueOf(adjustment), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid));
                android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "setTemporaryAutoBrightnessAdjustment: adjustment = " + info);
            }
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_BRIGHTNESS", "Permission required to set the display's auto brightness adjustment");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                    com.android.server.display.DisplayManagerService.this.mDmsExt.setTemporaryAutoBrightnessAdjustment(adjustment);
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.display.DisplayManagerShellCommand(com.android.server.display.DisplayManagerService.this, com.android.server.display.DisplayManagerService.this.mFlags).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public android.hardware.display.Curve getMinimumBrightnessCurve() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getMinimumBrightnessCurveInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getPreferredWideGamutColorSpaceId() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getPreferredWideGamutColorSpaceIdInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setUserPreferredDisplayMode(int displayId, android.view.Display.Mode mode) {
            setUserPreferredDisplayMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setUserPreferredDisplayModeInternal(displayId, mode);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.view.Display.Mode getUserPreferredDisplayMode(int displayId) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getUserPreferredDisplayModeInternal(displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.view.Display.Mode getSystemPreferredDisplayMode(int displayId) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getSystemPreferredDisplayModeInternal(displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setHdrConversionMode(android.hardware.display.HdrConversionMode hdrConversionMode) {
            if (!com.android.server.display.DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return;
            }
            com.android.server.display.DisplayManagerService.this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_HDR_CONVERSION_MODE", "Permission required to set the HDR conversion mode.");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setHdrConversionModeInternal(hdrConversionMode);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.HdrConversionMode getHdrConversionModeSetting() {
            if (!com.android.server.display.DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return com.android.server.display.DisplayManagerService.HDR_CONVERSION_MODE_UNSUPPORTED;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getHdrConversionModeSettingInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.HdrConversionMode getHdrConversionMode() {
            if (!com.android.server.display.DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return com.android.server.display.DisplayManagerService.HDR_CONVERSION_MODE_UNSUPPORTED;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getHdrConversionModeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int[] getSupportedHdrOutputTypes() {
            if (!com.android.server.display.DisplayManagerService.this.mIsHdrOutputControlEnabled) {
                return com.android.server.display.DisplayManagerService.EMPTY_ARRAY;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getSupportedHdrOutputTypesInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setShouldAlwaysRespectAppRequestedMode(boolean enabled) {
            setShouldAlwaysRespectAppRequestedMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setShouldAlwaysRespectAppRequestedModeInternal(enabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean shouldAlwaysRespectAppRequestedMode() {
            shouldAlwaysRespectAppRequestedMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.shouldAlwaysRespectAppRequestedModeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setRefreshRateSwitchingType(int newValue) {
            setRefreshRateSwitchingType_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.DisplayManagerService.this.setRefreshRateSwitchingTypeInternal(newValue);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getRefreshRateSwitchingType() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getRefreshRateSwitchingTypeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.graphics.common.DisplayDecorationSupport getDisplayDecorationSupport(int displayId) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getDisplayDecorationSupportInternal(displayId);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setDisplayIdToMirror(android.os.IBinder token, int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (com.android.server.display.DisplayManagerService.this.mVirtualDisplayAdapter != null) {
                    com.android.server.display.DisplayManagerService.this.mVirtualDisplayAdapter.setDisplayIdToMirror(token, display == null ? -1 : displayId);
                }
            }
        }

        public android.hardware.OverlayProperties getOverlaySupport() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.DisplayManagerService.this.getOverlaySupportInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void enableConnectedDisplay(int displayId) {
            enableConnectedDisplay_enforcePermission();
            com.android.server.display.DisplayManagerService.this.enableConnectedDisplay(displayId, true);
        }

        public void disableConnectedDisplay(int displayId) {
            disableConnectedDisplay_enforcePermission();
            com.android.server.display.DisplayManagerService.this.enableConnectedDisplay(displayId, false);
        }

        public void setSpecBrightness(int gear, java.lang.String reason, int rate) {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "setSpecBrightness gear = " + gear + " reason = " + reason + " rate = " + rate);
                com.android.server.display.DisplayManagerService.this.setSpecBrightnessInternal(gear, reason, rate);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            if (super.onTransact(code, data, reply, flags)) {
                return true;
            }
            return com.android.server.display.DisplayManagerService.this.mDMSEx != null && com.android.server.display.DisplayManagerService.this.mDMSEx.onTransact(code, data, reply, flags);
        }

        public boolean requestDisplayPower(int displayId, boolean on) {
            requestDisplayPower_enforcePermission();
            return com.android.server.display.DisplayManagerService.this.requestDisplayPower(displayId, on);
        }

        public void requestDisplayModes(android.os.IBinder token, int displayId, int[] modeIds) {
            requestDisplayModes_enforcePermission();
            com.android.server.display.DisplayManagerService.this.mDisplayModeDirector.requestDisplayModes(token, displayId, modeIds);
        }
    }

    private static boolean isValidBrightness(float brightness) {
        return !java.lang.Float.isNaN(brightness) && brightness >= 0.0f && brightness <= 1.0f;
    }

    void overrideSensorManager(android.hardware.SensorManager sensorManager) {
        synchronized (this.mSyncRoot) {
            this.mSensorManager = sensorManager;
        }
    }

    final class LocalService extends android.hardware.display.DisplayManagerInternal {
        LocalService() {
        }

        public void initPowerManagement(android.hardware.display.DisplayManagerInternal.DisplayPowerCallbacks callbacks, android.os.Handler handler, android.hardware.SensorManager sensorManager) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayManagerService.this.mDisplayPowerCallbacks = callbacks;
                com.android.server.display.DisplayManagerService.this.mSensorManager = sensorManager;
                com.android.server.display.DisplayManagerService.this.mPowerHandler = handler;
                com.android.server.display.DisplayManagerService.this.initializeDisplayPowerControllersLocked();
                com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.setPowerHandler(handler);
            }
            com.android.server.display.DisplayManagerService.this.mHandler.sendEmptyMessage(6);
        }

        public int createVirtualDisplay(android.hardware.display.VirtualDisplayConfig config, android.hardware.display.IVirtualDisplayCallback callback, android.companion.virtual.IVirtualDevice virtualDevice, android.window.DisplayWindowPolicyController dwpc, java.lang.String packageName) {
            return com.android.server.display.DisplayManagerService.this.createVirtualDisplayInternal(config, callback, null, virtualDevice, dwpc, packageName);
        }

        public boolean requestPowerState(int groupId, android.hardware.display.DisplayManagerInternal.DisplayPowerRequest request, boolean waitForNegativeProximity) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayGroup displayGroup = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayGroupLocked(groupId);
                if (displayGroup == null) {
                    return true;
                }
                boolean ready = true & com.android.server.display.DisplayManagerService.this.mDmsExt.requestPowerState(com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper, groupId, request, waitForNegativeProximity);
                return ready;
            }
        }

        public boolean isProximitySensorAvailable() {
            boolean zIsProximitySensorAvailable;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                zIsProximitySensorAvailable = ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).isProximitySensorAvailable();
            }
            return zIsProximitySensorAvailable;
        }

        public void registerDisplayGroupListener(android.hardware.display.DisplayManagerInternal.DisplayGroupListener listener) {
            com.android.server.display.DisplayManagerService.this.mDisplayGroupListeners.add(listener);
        }

        public void unregisterDisplayGroupListener(android.hardware.display.DisplayManagerInternal.DisplayGroupListener listener) {
            com.android.server.display.DisplayManagerService.this.mDisplayGroupListeners.remove(listener);
        }

        public android.window.ScreenCapture.ScreenshotHardwareBuffer systemScreenshot(int displayId) {
            return com.android.server.display.DisplayManagerService.this.systemScreenshotInternal(displayId);
        }

        public android.window.ScreenCapture.ScreenshotHardwareBuffer userScreenshot(int displayId) {
            return com.android.server.display.DisplayManagerService.this.userScreenshotInternal(displayId);
        }

        public android.view.DisplayInfo getDisplayInfo(int displayId) {
            return com.android.server.display.DisplayManagerService.this.getDisplayInfoInternal(displayId, android.os.Process.myUid());
        }

        public java.util.Set<android.view.DisplayInfo> getPossibleDisplayInfo(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                java.util.Set<android.view.DisplayInfo> possibleInfo = new android.util.ArraySet<>();
                if (com.android.server.display.DisplayManagerService.this.mDeviceStateManager == null) {
                    android.util.Slog.w(com.android.server.display.DisplayManagerService.TAG, "Can't get supported states since DeviceStateManager not ready");
                    return possibleInfo;
                }
                int[] supportedStates = com.android.server.display.DisplayManagerService.this.mDeviceStateManager.getSupportedStateIdentifiers();
                android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "supportedStates=" + java.util.Arrays.toString(supportedStates));
                for (int state : supportedStates) {
                    android.view.DisplayInfo displayInfo = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayInfoForStateLocked(state, displayId);
                    if (displayInfo != null) {
                        possibleInfo.add(displayInfo);
                    }
                }
                android.util.Slog.d(com.android.server.display.DisplayManagerService.TAG, "possibleInfos=" + possibleInfo);
                return possibleInfo;
            }
        }

        public android.graphics.Point getDisplayPosition(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (display == null) {
                    return null;
                }
                return display.getDisplayPosition();
            }
        }

        public void registerDisplayTransactionListener(android.hardware.display.DisplayManagerInternal.DisplayTransactionListener listener) {
            if (listener == null) {
                throw new java.lang.IllegalArgumentException("listener must not be null");
            }
            com.android.server.display.DisplayManagerService.this.registerDisplayTransactionListenerInternal(listener);
        }

        public void unregisterDisplayTransactionListener(android.hardware.display.DisplayManagerInternal.DisplayTransactionListener listener) {
            if (listener == null) {
                throw new java.lang.IllegalArgumentException("listener must not be null");
            }
            com.android.server.display.DisplayManagerService.this.unregisterDisplayTransactionListenerInternal(listener);
        }

        public void setDisplayInfoOverrideFromWindowManager(int displayId, android.view.DisplayInfo info) {
            com.android.server.display.DisplayManagerService.this.setDisplayInfoOverrideFromWindowManagerInternal(displayId, info);
        }

        public void getNonOverrideDisplayInfo(int displayId, android.view.DisplayInfo outInfo) {
            com.android.server.display.DisplayManagerService.this.getNonOverrideDisplayInfoInternal(displayId, outInfo);
        }

        public void performTraversal(android.view.SurfaceControl.Transaction t, android.util.SparseArray<android.view.SurfaceControl.Transaction> displayTransactions) {
            com.android.server.display.DisplayManagerService.this.performTraversalInternal(t, displayTransactions);
        }

        public void setDisplayProperties(int displayId, boolean hasContent, float requestedRefreshRate, int requestedMode, float requestedMinRefreshRate, float requestedMaxRefreshRate, boolean requestedMinimalPostProcessing, boolean disableHdrConversion, boolean inTraversal) {
            com.android.server.display.DisplayManagerService.this.setDisplayPropertiesInternal(displayId, hasContent, requestedRefreshRate, requestedMode, requestedMinRefreshRate, requestedMaxRefreshRate, requestedMinimalPostProcessing, disableHdrConversion, inTraversal);
        }

        public void setDisplayOffsets(int displayId, int x, int y) {
            com.android.server.display.DisplayManagerService.this.setDisplayOffsetsInternal(displayId, x, y);
        }

        public void setDisplayScalingDisabled(int displayId, boolean disableScaling) {
            com.android.server.display.DisplayManagerService.this.setDisplayScalingDisabledInternal(displayId, disableScaling);
        }

        public void setDisplayAccessUIDs(android.util.SparseArray<android.util.IntArray> newDisplayAccessUIDs) {
            com.android.server.display.DisplayManagerService.this.setDisplayAccessUIDsInternal(newDisplayAccessUIDs);
        }

        public void persistBrightnessTrackerState() {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).persistBrightnessTrackerState();
            }
        }

        public void onOverlayChanged() {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayManagerService.this.mDisplayDeviceRepo.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$LocalService$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.display.DisplayDevice) obj).onOverlayChangedLocked();
                    }
                });
            }
        }

        public android.hardware.display.DisplayedContentSamplingAttributes getDisplayedContentSamplingAttributes(int displayId) {
            return com.android.server.display.DisplayManagerService.this.getDisplayedContentSamplingAttributesInternal(displayId);
        }

        public boolean setDisplayedContentSamplingEnabled(int displayId, boolean enable, int componentMask, int maxFrames) {
            return com.android.server.display.DisplayManagerService.this.setDisplayedContentSamplingEnabledInternal(displayId, enable, componentMask, maxFrames);
        }

        public android.hardware.display.DisplayedContentSample getDisplayedContentSample(int displayId, long maxFrames, long timestamp) {
            return com.android.server.display.DisplayManagerService.this.getDisplayedContentSampleInternal(displayId, maxFrames, timestamp);
        }

        public void ignoreProximitySensorUntilChanged() {
            ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).ignoreProximitySensorUntilChanged();
        }

        public int getRefreshRateSwitchingType() {
            return com.android.server.display.DisplayManagerService.this.getRefreshRateSwitchingTypeInternal();
        }

        public void notifyRefreshRatePolicyChange() {
            com.android.server.display.DisplayManagerService.this.scheduleTraversalLocked(false);
        }

        public void blockScreenOnByBiometrics(java.lang.String reason) {
            try {
                int displayStateCount = com.android.server.display.DisplayManagerService.this.mDisplayStates.size();
                for (int i = 0; i < displayStateCount; i++) {
                    int displayId = com.android.server.display.DisplayManagerService.this.mDisplayStates.keyAt(i);
                    if (com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers != null && com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId) != null && ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId)).mDpcExt != null) {
                        ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId)).mDpcExt.blockScreenOnByBiometrics(reason);
                        android.util.Slog.i(com.android.server.display.DisplayManagerService.TAG, "blockScreenOnByBiometrics displayId = " + displayId);
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.display.DisplayManagerService.TAG, "blockScreenOnByBiometrics", e);
            }
        }

        public void unblockScreenOnByBiometrics(java.lang.String reason) {
            try {
                int displayStateCount = com.android.server.display.DisplayManagerService.this.mDisplayStates.size();
                for (int i = 0; i < displayStateCount; i++) {
                    int displayId = com.android.server.display.DisplayManagerService.this.mDisplayStates.keyAt(i);
                    if (com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers != null && com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId) != null && ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId)).mDpcExt != null && ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId)).mDpcExt.isBlockScreenOnByBiometrics()) {
                        ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(displayId)).mDpcExt.unblockScreenOnByBiometrics(reason);
                        android.util.Slog.i(com.android.server.display.DisplayManagerService.TAG, "unblockScreenOnByBiometrics displayId = " + displayId);
                    }
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.display.DisplayManagerService.TAG, "unblockScreenOnByBiometrics", e);
            }
        }

        public boolean hasBiometricsBlockedReason(java.lang.String reason) {
            return ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.hasBiometricsBlockedReason(reason);
        }

        public boolean isBlockDisplayByBiometrics() {
            if (com.android.server.display.DisplayManagerService.this.getOplusDisplayPowerControllerExt() != null) {
                return com.android.server.display.DisplayManagerService.this.getOplusDisplayPowerControllerExt().isBlockDisplayByBiometrics();
            }
            return false;
        }

        public boolean isBlockScreenOnByBiometrics() {
            return ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.isBlockScreenOnByBiometrics();
        }

        public int getScreenState() {
            return ((com.android.server.display.DisplayPowerController) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(0)).mDpcExt.getScreenState();
        }

        public android.view.SurfaceControl.RefreshRateRange getRefreshRateForDisplayAndSensor(int displayId, java.lang.String sensorName, java.lang.String sensorType) {
            android.hardware.SensorManager sensorManager;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                sensorManager = com.android.server.display.DisplayManagerService.this.mSensorManager;
            }
            if (sensorManager == null) {
                return null;
            }
            android.hardware.Sensor sensor = com.android.server.display.utils.SensorUtils.findSensor(sensorManager, sensorType, sensorName, 0);
            if (sensor == null) {
                return null;
            }
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (display == null) {
                    return null;
                }
                com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
                if (device == null) {
                    return null;
                }
                com.android.server.display.DisplayDeviceConfig config = device.getDisplayDeviceConfig();
                com.android.server.display.config.SensorData sensorData = config.getProximitySensor();
                if (sensorData == null || !sensorData.matches(sensorName, sensorType)) {
                    return null;
                }
                return new android.view.SurfaceControl.RefreshRateRange(sensorData.minRefreshRate, sensorData.maxRefreshRate);
            }
        }

        public java.util.List<android.hardware.display.DisplayManagerInternal.RefreshRateLimitation> getRefreshRateLimitations(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayDevice device = com.android.server.display.DisplayManagerService.this.getDeviceForDisplayLocked(displayId);
                if (device == null) {
                    return null;
                }
                com.android.server.display.DisplayDeviceConfig config = device.getDisplayDeviceConfig();
                return config.getRefreshRateLimitations();
            }
        }

        public void setWindowManagerMirroring(int displayId, boolean isMirroring) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayDevice device = com.android.server.display.DisplayManagerService.this.getDeviceForDisplayLocked(displayId);
                if (device != null) {
                    device.setWindowManagerMirroringLocked(isMirroring);
                }
            }
        }

        public android.graphics.Point getDisplaySurfaceDefaultSize(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayDevice device = com.android.server.display.DisplayManagerService.this.getDeviceForDisplayLocked(displayId);
                if (device == null) {
                    return null;
                }
                return device.getDisplaySurfaceDefaultSizeLocked();
            }
        }

        public void onEarlyInteractivityChange(boolean interactive) {
            com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.onEarlyInteractivityChange(interactive);
        }

        public android.window.DisplayWindowPolicyController getDisplayWindowPolicyController(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                if (!com.android.server.display.DisplayManagerService.this.mDisplayWindowPolicyControllers.contains(displayId)) {
                    return null;
                }
                return (android.window.DisplayWindowPolicyController) com.android.server.display.DisplayManagerService.this.mDisplayWindowPolicyControllers.get(displayId).second;
            }
        }

        public int getDisplayIdToMirror(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (display == null) {
                    return -1;
                }
                if (com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.isRemapDisabledSecondaryDisplayId(displayId)) {
                    return -1;
                }
                if (((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).isMirageDisplay(displayId)) {
                    return -1;
                }
                com.android.server.display.DisplayDevice displayDevice = display.getPrimaryDisplayDeviceLocked();
                boolean isRearDisplay = display.getDevicePositionLocked() == 1;
                boolean ownContent = (displayDevice.getDisplayDeviceInfoLocked().flags & 128) != 0 || isRearDisplay;
                if (!ownContent && !displayDevice.isWindowManagerMirroringLocked()) {
                    int displayIdToMirror = displayDevice.getDisplayIdToMirrorLocked();
                    com.android.server.display.LogicalDisplay displayToMirror = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayIdToMirror);
                    if (displayToMirror == null) {
                        displayIdToMirror = 0;
                    }
                    return displayIdToMirror;
                }
                return -1;
            }
        }

        public android.view.SurfaceControl.DisplayPrimaries getDisplayNativePrimaries(int displayId) {
            android.os.IBinder displayToken;
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                displayToken = com.android.server.display.DisplayManagerService.this.getDisplayToken(displayId);
                if (displayToken == null) {
                    throw new java.lang.IllegalArgumentException("Invalid displayId=" + displayId);
                }
            }
            return android.view.SurfaceControl.getDisplayNativePrimaries(displayToken);
        }

        public android.hardware.input.HostUsiVersion getHostUsiVersion(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (display == null) {
                    return null;
                }
                return display.getPrimaryDisplayDeviceLocked().getDisplayDeviceConfig().getHostUsiVersion();
            }
        }

        public android.hardware.display.DisplayManagerInternal.AmbientLightSensorData getAmbientLightSensorData(int displayId) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay display = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (display == null) {
                    return null;
                }
                com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
                if (device == null) {
                    return null;
                }
                com.android.server.display.config.SensorData data = device.getDisplayDeviceConfig().getAmbientLightSensor();
                return new android.hardware.display.DisplayManagerInternal.AmbientLightSensorData(data.name, data.type);
            }
        }

        public android.util.IntArray getDisplayGroupIds() {
            final java.util.Set<java.lang.Integer> visitedIds = new android.util.ArraySet<>();
            final android.util.IntArray displayGroupIds = new android.util.IntArray();
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$LocalService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$getDisplayGroupIds$0(visitedIds, displayGroupIds, (com.android.server.display.LogicalDisplay) obj);
                    }
                });
            }
            return displayGroupIds;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getDisplayGroupIds$0(java.util.Set visitedIds, android.util.IntArray displayGroupIds, com.android.server.display.LogicalDisplay logicalDisplay) {
            int groupId = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayGroupIdFromDisplayIdLocked(logicalDisplay.getDisplayIdLocked());
            if (!visitedIds.contains(java.lang.Integer.valueOf(groupId))) {
                visitedIds.add(java.lang.Integer.valueOf(groupId));
                displayGroupIds.add(groupId);
            }
        }

        public android.hardware.display.DisplayManagerInternal.DisplayOffloadSession registerDisplayOffloader(int displayId, android.hardware.display.DisplayManagerInternal.DisplayOffloader displayOffloader) {
            if (!com.android.server.display.DisplayManagerService.this.mFlags.isDisplayOffloadEnabled()) {
                return null;
            }
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                com.android.server.display.LogicalDisplay logicalDisplay = com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.getDisplayLocked(displayId);
                if (logicalDisplay == null) {
                    android.util.Slog.w(com.android.server.display.DisplayManagerService.TAG, "registering DisplayOffloader: LogicalDisplay for displayId=" + displayId + " is not found. No Op.");
                    return null;
                }
                com.android.server.display.DisplayPowerControllerInterface displayPowerController = (com.android.server.display.DisplayPowerControllerInterface) com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers.get(logicalDisplay.getDisplayIdLocked());
                if (displayPowerController == null) {
                    android.util.Slog.w(com.android.server.display.DisplayManagerService.TAG, "setting doze state override: DisplayPowerController for displayId=" + displayId + " is unavailable. No Op.");
                    return null;
                }
                com.android.server.display.DisplayOffloadSessionImpl session = new com.android.server.display.DisplayOffloadSessionImpl(displayOffloader, displayPowerController);
                logicalDisplay.setDisplayOffloadSessionLocked(session);
                displayPowerController.setDisplayOffloadSession(session);
                return session;
            }
        }

        public void onPresentation(int displayId, boolean isShown) {
            com.android.server.display.DisplayManagerService.this.mExternalDisplayPolicy.onPresentation(displayId, isShown);
        }
    }

    class DesiredDisplayModeSpecsObserver implements com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener {
        private final java.util.function.Consumer<com.android.server.display.LogicalDisplay> mSpecsChangedConsumer = new java.util.function.Consumer() { // from class: com.android.server.display.DisplayManagerService$DesiredDisplayModeSpecsObserver$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$new$0((com.android.server.display.LogicalDisplay) obj);
            }
        };
        private boolean mChanged = false;

        DesiredDisplayModeSpecsObserver() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(com.android.server.display.LogicalDisplay display) {
            int displayId = display.getDisplayIdLocked();
            com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs = com.android.server.display.DisplayManagerService.this.mDisplayModeDirector.getDesiredDisplayModeSpecs(displayId);
            com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecs existingDesiredDisplayModeSpecs = display.getDesiredDisplayModeSpecsLocked();
            if (com.android.server.display.DisplayManagerService.DEBUG) {
                android.util.Slog.i(com.android.server.display.DisplayManagerService.TAG, "Comparing display specs: " + desiredDisplayModeSpecs + ", existing: " + existingDesiredDisplayModeSpecs);
            }
            if (!desiredDisplayModeSpecs.equals(existingDesiredDisplayModeSpecs)) {
                display.setDesiredDisplayModeSpecsLocked(desiredDisplayModeSpecs);
                float max = desiredDisplayModeSpecs.primary.render.max;
                com.android.server.display.DisplayManagerService.this.mDmsExt.notifyDisplayModeSpecsChanged(displayId, max);
                this.mChanged = true;
            }
        }

        @Override // com.android.server.display.mode.DisplayModeDirector.DesiredDisplayModeSpecsListener
        public void onDesiredDisplayModeSpecsChanged() {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                this.mChanged = false;
                com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.forEachLocked(this.mSpecsChangedConsumer, false);
                if (this.mChanged) {
                    com.android.server.display.DisplayManagerService.this.scheduleTraversalLocked(false);
                    this.mChanged = false;
                }
            }
        }
    }

    class DeviceStateListener implements android.hardware.devicestate.DeviceStateManager.DeviceStateCallback {
        DeviceStateListener() {
        }

        public void onDeviceStateChanged(android.hardware.devicestate.DeviceState deviceState) {
            synchronized (com.android.server.display.DisplayManagerService.this.mSyncRoot) {
                android.os.Message msg = com.android.server.display.DisplayManagerService.this.mHandler.obtainMessage(9);
                msg.arg1 = deviceState.getIdentifier();
                com.android.server.display.DisplayManagerService.this.mHandler.sendMessage(msg);
                com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper.setDeviceStateLocked(deviceState.getIdentifier());
            }
        }
    }

    private static class BrightnessPair {
        public float brightness;
        public float sdrBrightness;

        BrightnessPair(float brightness, float sdrBrightness) {
            this.brightness = brightness;
            this.sdrBrightness = sdrBrightness;
        }
    }

    private class ExternalDisplayPolicyInjector implements com.android.server.display.ExternalDisplayPolicy.Injector {
        private ExternalDisplayPolicyInjector() {
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public void sendExternalDisplayEventLocked(com.android.server.display.LogicalDisplay display, int event) {
            com.android.server.display.DisplayManagerService.this.sendDisplayEventLocked(display, event);
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public android.os.IThermalService getThermalService() {
            return android.os.IThermalService.Stub.asInterface(android.os.ServiceManager.getService("thermalservice"));
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public com.android.server.display.feature.DisplayManagerFlags getFlags() {
            return com.android.server.display.DisplayManagerService.this.mFlags;
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public com.android.server.display.LogicalDisplayMapper getLogicalDisplayMapper() {
            return com.android.server.display.DisplayManagerService.this.mLogicalDisplayMapper;
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public com.android.server.display.DisplayManagerService.SyncRoot getSyncRoot() {
            return com.android.server.display.DisplayManagerService.this.mSyncRoot;
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public com.android.server.display.notifications.DisplayNotificationManager getDisplayNotificationManager() {
            return com.android.server.display.DisplayManagerService.this.mDisplayNotificationManager;
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public android.os.Handler getHandler() {
            return com.android.server.display.DisplayManagerService.this.mHandler;
        }

        @Override // com.android.server.display.ExternalDisplayPolicy.Injector
        public com.android.server.display.ExternalDisplayStatsService getExternalDisplayStatsService() {
            return com.android.server.display.DisplayManagerService.this.mExternalDisplayStatsService;
        }
    }

    public com.android.server.display.DisplayManagerService.DisplayManagerServiceWrapper getWrapper() {
        return this.mDmsWrapper;
    }

    public class DisplayManagerServiceWrapper {
        public DisplayManagerServiceWrapper() {
        }

        public android.util.SparseArray<com.android.server.display.DisplayPowerController> getDisplayPowerControllers() {
            return com.android.server.display.DisplayManagerService.this.mDisplayPowerControllers;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.display.IOplusDisplayPowerControllerExt getOplusDisplayPowerControllerExt() {
        if (this.mDisplayPowerControllers != null && this.mDisplayPowerControllers.get(0) != null && this.mDisplayPowerControllers.get(0).mDpcExt != null) {
            return this.mDisplayPowerControllers.get(0).mDpcExt;
        }
        android.util.Slog.w(TAG, "DisplayPowerController is null !");
        return null;
    }
}
