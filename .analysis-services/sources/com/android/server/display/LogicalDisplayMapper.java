package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class LogicalDisplayMapper implements com.android.server.display.DisplayDeviceRepository.Listener {
    private static final int DEVICE_STATE_CLOSE = 0;
    private static final int DEVICE_STATE_HALF = 1;
    private static final int DEVICE_STATE_OPEN = 2;
    public static final int DISPLAY_GROUP_EVENT_ADDED = 1;
    public static final int DISPLAY_GROUP_EVENT_CHANGED = 2;
    public static final int DISPLAY_GROUP_EVENT_REMOVED = 3;
    public static final int LOGICAL_DISPLAY_EVENT_ADDED = 1;
    public static final int LOGICAL_DISPLAY_EVENT_CHANGED = 2;
    public static final int LOGICAL_DISPLAY_EVENT_CONNECTED = 8;
    public static final int LOGICAL_DISPLAY_EVENT_DEVICE_STATE_TRANSITION = 6;
    public static final int LOGICAL_DISPLAY_EVENT_DISCONNECTED = 9;
    public static final int LOGICAL_DISPLAY_EVENT_FRAME_RATE_OVERRIDES_CHANGED = 5;
    public static final int LOGICAL_DISPLAY_EVENT_HDR_SDR_RATIO_CHANGED = 7;
    public static final int LOGICAL_DISPLAY_EVENT_REMOVED = 3;
    public static final int LOGICAL_DISPLAY_EVENT_SWAPPED = 4;
    private static final int MSG_TRANSITION_TO_PENDING_DEVICE_STATE = 1;
    private static final java.lang.String TAG = "LogicalDisplayMapper";
    private static final int TIMEOUT_STATE_TRANSITION_MILLIS = 500;
    private static final int UPDATE_STATE_NEW = 0;
    private static final int UPDATE_STATE_TRANSITION = 1;
    private static final int UPDATE_STATE_UPDATED = 2;
    private boolean mBootCompleted;
    private android.content.Context mContext;
    private com.android.server.display.layout.Layout mCurrentLayout;
    private final android.util.SparseIntArray mDeviceDisplayGroupIds;
    private int mDeviceState;
    private int mDeviceStateToBeAppliedAfterBoot;
    private final com.android.server.display.DeviceStateToLayoutMap mDeviceStateToLayoutMap;
    private final android.util.SparseBooleanArray mDeviceStatesOnWhichToSelectiveSleep;
    private final android.util.SparseBooleanArray mDeviceStatesOnWhichToWakeUp;
    private final com.android.server.display.DisplayDeviceRepository mDisplayDeviceRepo;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mDisplayGroupIdsByName;
    private final android.util.SparseArray<com.android.server.display.DisplayGroup> mDisplayGroups;
    private final android.util.SparseIntArray mDisplayGroupsToUpdate;
    private final android.util.SparseBooleanArray mDisplaysEnabledCache;
    private final com.android.server.display.feature.DisplayManagerFlags mFlags;
    private final com.android.internal.foldables.FoldGracePeriodProvider mFoldGracePeriodProvider;
    private final com.android.server.utils.FoldSettingProvider mFoldSettingProvider;
    private final com.android.server.display.LogicalDisplayMapper.LogicalDisplayMapperHandler mHandler;
    private final com.android.server.display.layout.DisplayIdProducer mIdProducer;
    private boolean mInteractive;
    private final com.android.server.display.LogicalDisplayMapper.Listener mListener;
    private com.android.server.display.ILogicalDisplayMapperExt mLogicalDisplayMapperExt;
    private final android.util.SparseArray<com.android.server.display.LogicalDisplay> mLogicalDisplays;
    private final android.util.SparseIntArray mLogicalDisplaysToUpdate;
    private int mNextBuiltInDisplayId;
    private int mNextNonDefaultGroupId;
    private int mPendingDeviceState;
    private final android.os.PowerManager mPowerManager;
    private final boolean mSingleDisplayDemoMode;
    private final boolean mSupportsConcurrentInternalDisplays;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncRoot;
    private final com.android.server.display.mode.SyntheticModeManager mSyntheticModeManager;
    private final android.view.DisplayInfo mTempDisplayInfo;
    private final android.view.DisplayInfo mTempNonOverrideDisplayInfo;
    private final android.util.SparseIntArray mUpdatedDisplayGroups;
    private final android.util.SparseIntArray mUpdatedLogicalDisplays;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mVirtualDeviceDisplayMapping;
    private com.android.server.policy.WindowManagerPolicy mWindowManagerPolicy;
    private com.android.server.display.IOplusLogicDisplayMapperWrapper mWrapper;
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static int sNextNonDefaultDisplayId = 1;

    public interface Listener {
        void onDisplayGroupEventLocked(int i, int i2);

        void onLogicalDisplayEventLocked(com.android.server.display.LogicalDisplay logicalDisplay, int i);

        void onTraversalRequested();
    }

    static /* synthetic */ int lambda$new$0(boolean isDefault) {
        if (isDefault) {
            return 0;
        }
        int i = sNextNonDefaultDisplayId;
        sNextNonDefaultDisplayId = i + 1;
        return i;
    }

    LogicalDisplayMapper(android.content.Context context, com.android.server.utils.FoldSettingProvider foldSettingProvider, com.android.internal.foldables.FoldGracePeriodProvider foldGracePeriodProvider, com.android.server.display.DisplayDeviceRepository repo, com.android.server.display.LogicalDisplayMapper.Listener listener, com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.os.Handler handler, com.android.server.display.feature.DisplayManagerFlags flags) {
        this(context, foldSettingProvider, foldGracePeriodProvider, repo, listener, syncRoot, handler, new com.android.server.display.DeviceStateToLayoutMap(new com.android.server.display.layout.DisplayIdProducer() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda3
            @Override // com.android.server.display.layout.DisplayIdProducer
            public final int getId(boolean z) {
                return com.android.server.display.LogicalDisplayMapper.lambda$new$1(z);
            }
        }, flags), flags, new com.android.server.display.mode.SyntheticModeManager(flags));
    }

    static /* synthetic */ int lambda$new$1(boolean isDefault) {
        if (isDefault) {
            return 0;
        }
        int i = sNextNonDefaultDisplayId;
        sNextNonDefaultDisplayId = i + 1;
        return i;
    }

    LogicalDisplayMapper(android.content.Context context, com.android.server.utils.FoldSettingProvider foldSettingProvider, com.android.internal.foldables.FoldGracePeriodProvider foldGracePeriodProvider, com.android.server.display.DisplayDeviceRepository repo, com.android.server.display.LogicalDisplayMapper.Listener listener, com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.os.Handler handler, com.android.server.display.DeviceStateToLayoutMap deviceStateToLayoutMap, com.android.server.display.feature.DisplayManagerFlags flags, com.android.server.display.mode.SyntheticModeManager syntheticModeManager) {
        this.mTempDisplayInfo = new android.view.DisplayInfo();
        this.mTempNonOverrideDisplayInfo = new android.view.DisplayInfo();
        this.mLogicalDisplays = new android.util.SparseArray<>();
        this.mNextBuiltInDisplayId = 4096;
        this.mDisplaysEnabledCache = new android.util.SparseBooleanArray();
        this.mDisplayGroups = new android.util.SparseArray<>();
        this.mDeviceDisplayGroupIds = new android.util.SparseIntArray();
        this.mDisplayGroupIdsByName = new android.util.ArrayMap<>();
        this.mUpdatedLogicalDisplays = new android.util.SparseIntArray();
        this.mUpdatedDisplayGroups = new android.util.SparseIntArray();
        this.mLogicalDisplaysToUpdate = new android.util.SparseIntArray();
        this.mDisplayGroupsToUpdate = new android.util.SparseIntArray();
        this.mVirtualDeviceDisplayMapping = new android.util.ArrayMap<>();
        this.mNextNonDefaultGroupId = 1;
        this.mIdProducer = new com.android.server.display.layout.DisplayIdProducer() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda0
            @Override // com.android.server.display.layout.DisplayIdProducer
            public final int getId(boolean z) {
                return com.android.server.display.LogicalDisplayMapper.lambda$new$0(z);
            }
        };
        this.mCurrentLayout = null;
        this.mDeviceState = -1;
        this.mPendingDeviceState = -1;
        this.mDeviceStateToBeAppliedAfterBoot = -1;
        this.mBootCompleted = false;
        this.mLogicalDisplayMapperExt = (com.android.server.display.ILogicalDisplayMapperExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.ILogicalDisplayMapperExt.class).base(this).create();
        this.mWrapper = new com.android.server.display.LogicalDisplayMapper.OplusLogicDisplayMapperWrapper();
        this.mSyncRoot = syncRoot;
        this.mPowerManager = (android.os.PowerManager) context.getSystemService(android.os.PowerManager.class);
        this.mInteractive = this.mPowerManager.isInteractive();
        this.mHandler = new com.android.server.display.LogicalDisplayMapper.LogicalDisplayMapperHandler(handler.getLooper());
        this.mDisplayDeviceRepo = repo;
        this.mListener = listener;
        this.mFoldSettingProvider = foldSettingProvider;
        this.mFoldGracePeriodProvider = foldGracePeriodProvider;
        this.mSingleDisplayDemoMode = android.os.SystemProperties.getBoolean("persist.demo.singledisplay", false);
        this.mSupportsConcurrentInternalDisplays = context.getResources().getBoolean(android.R.bool.config_skipSensorAvailable);
        this.mDeviceStatesOnWhichToWakeUp = toSparseBooleanArray(context.getResources().getIntArray(android.R.array.config_default_vm_number));
        this.mDeviceStatesOnWhichToSelectiveSleep = toSparseBooleanArray(context.getResources().getIntArray(android.R.array.config_defaultPinnerServiceFiles));
        this.mDisplayDeviceRepo.addListener(this);
        this.mDeviceStateToLayoutMap = deviceStateToLayoutMap;
        this.mFlags = flags;
        this.mContext = context;
        this.mLogicalDisplayMapperExt.setDisplayLayout(this.mDeviceStateToLayoutMap.getLayoutMap());
        this.mLogicalDisplayMapperExt.initDvMultiDisplay();
        this.mSyntheticModeManager = syntheticModeManager;
    }

    @Override // com.android.server.display.DisplayDeviceRepository.Listener
    public void onDisplayDeviceEventLocked(com.android.server.display.DisplayDevice device, int event) {
        switch (event) {
            case 1:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Display device added: " + device.getDisplayDeviceInfoLocked());
                }
                handleDisplayDeviceAddedLocked(device);
                break;
            case 3:
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Display device removed: " + device.getDisplayDeviceInfoLocked());
                }
                handleDisplayDeviceRemovedLocked(device);
                updateLogicalDisplaysLocked();
                break;
        }
    }

    @Override // com.android.server.display.DisplayDeviceRepository.Listener
    public void onDisplayDeviceChangedLocked(com.android.server.display.DisplayDevice device, int diff) {
        if (DEBUG || diff == 2 || diff == 4) {
            android.util.Slog.d(TAG, "Display device changed: " + device.getDisplayDeviceInfoLocked() + " diff=" + diff);
        }
        finishStateTransitionLocked(false);
        updateLogicalDisplaysLocked(diff);
    }

    @Override // com.android.server.display.DisplayDeviceRepository.Listener
    public void onTraversalRequested() {
        this.mListener.onTraversalRequested();
    }

    public void onWindowManagerReady() {
        this.mWindowManagerPolicy = (com.android.server.policy.WindowManagerPolicy) com.android.server.LocalServices.getService(com.android.server.policy.WindowManagerPolicy.class);
    }

    public com.android.server.display.LogicalDisplay getDisplayLocked(int displayId) {
        return getDisplayLocked(displayId, true);
    }

    public com.android.server.display.LogicalDisplay getDisplayLocked(int displayId, boolean includeDisabled) {
        com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.get(displayId);
        if (display == null || display.isEnabledLocked() || includeDisabled) {
            return display;
        }
        return null;
    }

    public com.android.server.display.LogicalDisplay getDisplayLocked(com.android.server.display.DisplayDevice device) {
        return getDisplayLocked(device, true);
    }

    public com.android.server.display.LogicalDisplay getDisplayLocked(com.android.server.display.DisplayDevice device, boolean includeDisabled) {
        if (device == null) {
            return null;
        }
        int count = this.mLogicalDisplays.size();
        for (int i = 0; i < count; i++) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i);
            if (display.getPrimaryDisplayDeviceLocked() == device) {
                if (!display.isEnabledLocked() && !includeDisabled) {
                    return null;
                }
                return display;
            }
        }
        return null;
    }

    public int[] getDisplayIdsLocked(int callingUid, boolean includeDisabled) {
        int count = this.mLogicalDisplays.size();
        int[] displayIds = new int[count];
        int n = 0;
        for (int i = 0; i < count; i++) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i);
            if (display.isEnabledLocked() || includeDisabled) {
                android.view.DisplayInfo info = display.getDisplayInfoLocked();
                if (info.hasAccess(callingUid) && !this.mLogicalDisplayMapperExt.filterSecondaryDisplay(this.mContext, display.getDisplayIdLocked(), 0, callingUid)) {
                    displayIds[n] = this.mLogicalDisplays.keyAt(i);
                    n++;
                }
            }
        }
        if (n != count) {
            return java.util.Arrays.copyOfRange(displayIds, 0, n);
        }
        return displayIds;
    }

    public void forEachLocked(java.util.function.Consumer<com.android.server.display.LogicalDisplay> consumer) {
        forEachLocked(consumer, false);
    }

    public void forEachLocked(java.util.function.Consumer<com.android.server.display.LogicalDisplay> consumer, boolean includeDisabled) {
        int count = this.mLogicalDisplays.size();
        for (int i = 0; i < count; i++) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i);
            if (display.isEnabledLocked() || includeDisabled) {
                consumer.accept(display);
            }
        }
    }

    public int getDisplayGroupIdFromDisplayIdLocked(int displayId) {
        com.android.server.display.LogicalDisplay display = getDisplayLocked(displayId);
        if (display == null) {
            return -1;
        }
        int size = this.mDisplayGroups.size();
        for (int i = 0; i < size; i++) {
            com.android.server.display.DisplayGroup displayGroup = this.mDisplayGroups.valueAt(i);
            if (displayGroup.containsLocked(display)) {
                return this.mDisplayGroups.keyAt(i);
            }
        }
        return -1;
    }

    public com.android.server.display.DisplayGroup getDisplayGroupLocked(int groupId) {
        return this.mDisplayGroups.get(groupId);
    }

    public void setPowerHandler(android.os.Handler handler) {
        this.mLogicalDisplayMapperExt.setPowerHandler(handler);
    }

    public android.view.DisplayInfo getDisplayInfoForStateLocked(int deviceState, int displayId) {
        com.android.server.display.layout.Layout layout = this.mDeviceStateToLayoutMap.get(deviceState);
        if (layout == null) {
            android.util.Slog.d(TAG, "Cannot get layout for given state:" + deviceState);
            return null;
        }
        com.android.server.display.layout.Layout.Display display = layout.getById(displayId);
        if (display == null) {
            android.util.Slog.d(TAG, "Cannot get display for given layout:" + layout);
            return null;
        }
        com.android.server.display.DisplayDevice device = this.mDisplayDeviceRepo.getByAddressLocked(display.getAddress());
        if (device == null) {
            android.util.Slog.w(TAG, "The display device (" + display.getAddress() + "), is not available for the display state " + this.mDeviceState);
            return null;
        }
        com.android.server.display.LogicalDisplay logicalDisplay = getDisplayLocked(device, true);
        if (logicalDisplay == null) {
            android.util.Slog.w(TAG, "The logical display associated with address (" + display.getAddress() + "), is not available for the display state " + this.mDeviceState);
            return null;
        }
        android.view.DisplayInfo displayInfo = new android.view.DisplayInfo(logicalDisplay.getDisplayInfoLocked());
        displayInfo.displayId = displayId;
        return displayInfo;
    }

    public boolean isRemapDisabledSecondaryDisplayId(int displayId) {
        return this.mLogicalDisplayMapperExt.isRemapDisabledSecondaryDisplayId(displayId);
    }

    public void dumpLocked(java.io.PrintWriter pw) {
        pw.println("LogicalDisplayMapper:");
        java.io.PrintWriter indentingPrintWriter = new android.util.IndentingPrintWriter(pw, "  ");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("mSingleDisplayDemoMode=" + this.mSingleDisplayDemoMode);
        indentingPrintWriter.println("mCurrentLayout=" + this.mCurrentLayout);
        indentingPrintWriter.println("mDeviceStatesOnWhichToWakeUp=" + this.mDeviceStatesOnWhichToWakeUp);
        indentingPrintWriter.println("mDeviceStatesOnWhichSelectiveSleep=" + this.mDeviceStatesOnWhichToSelectiveSleep);
        indentingPrintWriter.println("mInteractive=" + this.mInteractive);
        indentingPrintWriter.println("mBootCompleted=" + this.mBootCompleted);
        indentingPrintWriter.println();
        indentingPrintWriter.println("mDeviceState=" + this.mDeviceState);
        indentingPrintWriter.println("mPendingDeviceState=" + this.mPendingDeviceState);
        indentingPrintWriter.println("mDeviceStateToBeAppliedAfterBoot=" + this.mDeviceStateToBeAppliedAfterBoot);
        indentingPrintWriter.println("mDeviceState=" + this.mDeviceState);
        indentingPrintWriter.println("mPendingDeviceState=" + this.mPendingDeviceState);
        int logicalDisplayCount = this.mLogicalDisplays.size();
        indentingPrintWriter.println();
        indentingPrintWriter.println("Logical Displays: size=" + logicalDisplayCount);
        for (int i = 0; i < logicalDisplayCount; i++) {
            int displayId = this.mLogicalDisplays.keyAt(i);
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i);
            indentingPrintWriter.println("Display " + displayId + ":");
            indentingPrintWriter.increaseIndent();
            display.dumpLocked(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
        }
        this.mDeviceStateToLayoutMap.dumpLocked(indentingPrintWriter);
    }

    void associateDisplayDeviceWithVirtualDevice(com.android.server.display.DisplayDevice displayDevice, int virtualDeviceUniqueId) {
        this.mVirtualDeviceDisplayMapping.put(displayDevice.getUniqueId(), java.lang.Integer.valueOf(virtualDeviceUniqueId));
    }

    void setDeviceStateLocked(int state) {
        int state2 = this.mLogicalDisplayMapperExt.interceptBaseDeviceState(this.mPendingDeviceState, state);
        if (state2 == -1) {
            return;
        }
        if (!this.mBootCompleted) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Postponing transition to state: " + this.mPendingDeviceState + " until boot is completed");
            }
            this.mDeviceStateToBeAppliedAfterBoot = state2;
            return;
        }
        android.util.Slog.i(TAG, "Requesting Transition to state: " + state2 + ", from state=" + this.mDeviceState + ", interactive=" + this.mInteractive + ", mBootCompleted=" + this.mBootCompleted);
        resetLayoutLocked(this.mDeviceState, state2, true);
        this.mPendingDeviceState = state2;
        this.mDeviceStateToBeAppliedAfterBoot = -1;
        boolean wakeDevice = shouldDeviceBeWoken(this.mPendingDeviceState, this.mDeviceState, this.mInteractive, this.mBootCompleted);
        boolean sleepDevice = shouldDeviceBePutToSleep(this.mPendingDeviceState, this.mDeviceState, this.mInteractive, this.mBootCompleted);
        boolean oplusSleepDevice = this.mLogicalDisplayMapperExt.getOplusSleepDevice(sleepDevice, this.mContext, state2, this.mDeviceState);
        android.util.Slog.d(TAG, "setDeviceStateLocked state=" + this.mDeviceState + "->" + this.mPendingDeviceState + " wake=" + this.mDeviceStatesOnWhichToWakeUp.get(this.mDeviceState) + "->" + this.mDeviceStatesOnWhichToWakeUp.get(this.mPendingDeviceState) + " wakeDevice=" + wakeDevice + " sleepDevice=" + sleepDevice + " mInteractive=" + this.mInteractive + " displaysOff=" + areAllTransitioningDisplaysOffLocked());
        if (this.mPendingDeviceState == 3 || this.mPendingDeviceState == 0) {
            this.mLogicalDisplayMapperExt.updateDvsParam(this.mPendingDeviceState);
        }
        if (areAllTransitioningDisplaysOffLocked() && !wakeDevice && !sleepDevice && (!this.mLogicalDisplayMapperExt.hasFoldRemapDisplayDisableFeature() || !this.mInteractive || this.mPendingDeviceState == 99)) {
            transitionToPendingStateLocked();
            this.mLogicalDisplayMapperExt.transitionToPendingStateLocked();
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Postponing transition to state: " + this.mPendingDeviceState);
        }
        updateLogicalDisplaysLocked();
        this.mLogicalDisplayMapperExt.fastFreezeOnWakeup(this.mDeviceState, this.mPendingDeviceState);
        this.mLogicalDisplayMapperExt.setUxOnWakeup(this.mDeviceState, this.mPendingDeviceState);
        this.mLogicalDisplayMapperExt.screenOnCpuBoost(this.mDeviceState, this.mPendingDeviceState);
        if (wakeDevice || oplusSleepDevice) {
            if (wakeDevice) {
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setDeviceStateLocked$2();
                    }
                });
            } else if (oplusSleepDevice) {
                final int goToSleepFlag = 0;
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.LogicalDisplayMapper$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$setDeviceStateLocked$3(goToSleepFlag);
                    }
                });
            }
        }
        if (!this.mLogicalDisplayMapperExt.hasFoldRemapDisplayDisableFeature()) {
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
        } else {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageDelayed(1, 500L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDeviceStateLocked$2() {
        this.mPowerManager.wakeUp(android.os.SystemClock.uptimeMillis(), 12, "server.display:unfold");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDeviceStateLocked$3(int goToSleepFlag) {
        this.mPowerManager.goToSleep(android.os.SystemClock.uptimeMillis(), 13, goToSleepFlag);
    }

    void onBootCompleted() {
        synchronized (this.mSyncRoot) {
            this.mBootCompleted = true;
            requestDisplaySwitchOff();
            if (this.mDeviceStateToBeAppliedAfterBoot != -1) {
                setDeviceStateLocked(this.mDeviceStateToBeAppliedAfterBoot);
            }
        }
    }

    void onEarlyInteractivityChange(boolean interactive) {
        synchronized (this.mSyncRoot) {
            if (this.mInteractive != interactive) {
                this.mInteractive = interactive;
                finishStateTransitionLocked(false);
            }
        }
    }

    boolean shouldDeviceBeWoken(int pendingState, int currentState, boolean isInteractive, boolean isBootCompleted) {
        return this.mDeviceStatesOnWhichToWakeUp.get(pendingState) && !this.mDeviceStatesOnWhichToWakeUp.get(currentState) && !isInteractive && isBootCompleted;
    }

    boolean shouldDeviceBePutToSleep(int pendingState, int currentState, boolean isInteractive, boolean isBootCompleted) {
        return currentState != -1 && this.mDeviceStatesOnWhichToSelectiveSleep.get(pendingState) && !this.mDeviceStatesOnWhichToSelectiveSleep.get(currentState) && isInteractive && isBootCompleted && !this.mFoldSettingProvider.shouldStayAwakeOnFold();
    }

    private boolean areAllTransitioningDisplaysOffLocked() {
        com.android.server.display.DisplayDevice device;
        int count = this.mLogicalDisplays.size();
        for (int i = 0; i < count; i++) {
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i);
            if (display.isInTransitionLocked() && (device = display.getPrimaryDisplayDeviceLocked()) != null) {
                com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
                if (info.state != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void transitionToPendingStateLocked() {
        resetLayoutLocked(this.mDeviceState, this.mPendingDeviceState, false);
        this.mDeviceState = this.mPendingDeviceState;
        this.mPendingDeviceState = -1;
        applyLayoutLocked();
        updateLogicalDisplaysLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishStateTransitionLocked(boolean force) {
        if (this.mPendingDeviceState == -1) {
            return;
        }
        boolean isReadyToTransition = false;
        boolean waitingToWakeDevice = this.mDeviceStatesOnWhichToWakeUp.get(this.mPendingDeviceState) && !this.mDeviceStatesOnWhichToWakeUp.get(this.mDeviceState) && !this.mInteractive && this.mBootCompleted;
        boolean waitingToSleepDevice = this.mDeviceStatesOnWhichToSelectiveSleep.get(this.mPendingDeviceState) && !this.mDeviceStatesOnWhichToSelectiveSleep.get(this.mDeviceState) && this.mInteractive && this.mBootCompleted && !shouldStayAwakeOnFold();
        boolean displaysOff = areAllTransitioningDisplaysOffLocked();
        if (displaysOff && !waitingToWakeDevice && !waitingToSleepDevice) {
            isReadyToTransition = true;
        }
        android.util.Slog.d(TAG, "finishStateTransitionLocked state=" + this.mDeviceState + "->" + this.mPendingDeviceState + " wake=" + this.mDeviceStatesOnWhichToWakeUp.get(this.mDeviceState) + "->" + this.mDeviceStatesOnWhichToWakeUp.get(this.mPendingDeviceState) + " waitingWake=" + waitingToWakeDevice + " waitingSleep=" + waitingToSleepDevice + " mInteractive=" + this.mInteractive + " force=" + force + " displaysOff=" + displaysOff + " isReadyToTransition=" + isReadyToTransition);
        if (isReadyToTransition || force) {
            transitionToPendingStateLocked();
            this.mHandler.removeMessages(1);
            this.mLogicalDisplayMapperExt.transitionToPendingStateLocked();
        } else if (DEBUG) {
            android.util.Slog.d(TAG, "Not yet ready to transition to state=" + this.mPendingDeviceState + " with displays-off=" + displaysOff + ", force=" + force + ", mInteractive=" + this.mInteractive + ", isReady=" + isReadyToTransition);
        }
    }

    private void handleDisplayDeviceAddedLocked(com.android.server.display.DisplayDevice device) {
        com.android.server.display.DisplayDeviceInfo deviceInfo = device.getDisplayDeviceInfoLocked();
        if ((deviceInfo.flags & 1) != 0) {
            initializeDefaultDisplayDeviceLocked(device);
        }
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        createNewLogicalDisplayLocked(device, com.android.server.display.layout.Layout.assignDisplayIdLocked(false, info.address));
        this.mLogicalDisplayMapperExt.setMainDisplayUniqueId(info.uniqueId);
        applyLayoutLocked();
        updateLogicalDisplaysLocked();
    }

    private void handleDisplayDeviceRemovedLocked(com.android.server.display.DisplayDevice device) {
        com.android.server.display.layout.Layout layout = this.mDeviceStateToLayoutMap.get(-1);
        com.android.server.display.layout.Layout.Display layoutDisplay = layout.getById(0);
        if (layoutDisplay == null) {
            return;
        }
        com.android.server.display.DisplayDeviceInfo deviceInfo = device.getDisplayDeviceInfoLocked();
        this.mVirtualDeviceDisplayMapping.remove(device.getUniqueId());
        if (layoutDisplay.getAddress().equals(deviceInfo.address)) {
            layout.removeDisplayLocked(0);
            for (int i = 0; i < this.mLogicalDisplays.size(); i++) {
                com.android.server.display.LogicalDisplay nextDisplay = this.mLogicalDisplays.valueAt(i);
                com.android.server.display.DisplayDevice nextDevice = nextDisplay.getPrimaryDisplayDeviceLocked();
                if (nextDevice != null) {
                    com.android.server.display.DisplayDeviceInfo nextDeviceInfo = nextDevice.getDisplayDeviceInfoLocked();
                    if ((nextDeviceInfo.flags & 1) != 0 && !nextDeviceInfo.address.equals(deviceInfo.address)) {
                        layout.createDefaultDisplayLocked(nextDeviceInfo.address, this.mIdProducer);
                        applyLayoutLocked();
                        return;
                    }
                }
            }
        }
    }

    void updateLogicalDisplays() {
        synchronized (this.mSyncRoot) {
            updateLogicalDisplaysLocked();
        }
    }

    void updateLogicalDisplaysLocked() {
        updateLogicalDisplaysLocked(-1);
    }

    private void updateLogicalDisplaysLocked(int diff) {
        updateLogicalDisplaysLocked(diff, false);
    }

    private void updateLogicalDisplaysLocked(int diff, boolean isSecondLoop) {
        int i;
        boolean reloop = false;
        boolean z = true;
        int i2 = this.mLogicalDisplays.size() - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            int displayId = this.mLogicalDisplays.keyAt(i2);
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i2);
            assignDisplayGroupLocked(display);
            boolean wasDirty = display.isDirtyLocked();
            this.mTempDisplayInfo.copyFrom(display.getDisplayInfoLocked());
            display.getNonOverrideDisplayInfoLocked(this.mTempNonOverrideDisplayInfo);
            display.updateLocked(this.mDisplayDeviceRepo, this.mSyntheticModeManager);
            android.view.DisplayInfo newDisplayInfo = display.getDisplayInfoLocked();
            int updateState = this.mUpdatedLogicalDisplays.get(displayId, 0);
            boolean wasPreviouslyUpdated = updateState != 0 ? z : false;
            boolean wasPreviouslyEnabled = this.mDisplaysEnabledCache.get(displayId);
            boolean isCurrentlyEnabled = display.isEnabledLocked();
            if (!display.isValidLocked()) {
                com.android.server.display.DisplayGroup displayGroup = getDisplayGroupLocked(getDisplayGroupIdFromDisplayIdLocked(displayId));
                if (displayGroup != null) {
                    displayGroup.removeDisplayLocked(display);
                }
                if (wasPreviouslyUpdated) {
                    if (this.mFlags.isConnectedDisplayManagementEnabled()) {
                        if (this.mDisplaysEnabledCache.get(displayId)) {
                            reloop = true;
                            this.mLogicalDisplaysToUpdate.put(displayId, 3);
                        } else {
                            this.mUpdatedLogicalDisplays.delete(displayId);
                            this.mLogicalDisplaysToUpdate.put(displayId, 9);
                        }
                    } else {
                        this.mUpdatedLogicalDisplays.delete(displayId);
                        this.mLogicalDisplaysToUpdate.put(displayId, 3);
                    }
                } else {
                    this.mLogicalDisplays.removeAt(i2);
                }
                android.util.Slog.d(TAG, "invalid display:" + display.toStringMini());
            } else {
                if (!wasPreviouslyUpdated) {
                    if (this.mFlags.isConnectedDisplayManagementEnabled()) {
                        reloop = true;
                        this.mLogicalDisplaysToUpdate.put(displayId, 8);
                        i = 2;
                    } else {
                        this.mLogicalDisplaysToUpdate.put(displayId, 1);
                        i = 2;
                    }
                } else if (!android.text.TextUtils.equals(this.mTempDisplayInfo.uniqueId, newDisplayInfo.uniqueId)) {
                    android.util.Slog.d(TAG, "uniqueId is not equal so send swapped event");
                    this.mLogicalDisplaysToUpdate.put(displayId, 4);
                    i = 2;
                } else if (!this.mFlags.isConnectedDisplayManagementEnabled() || wasPreviouslyEnabled == isCurrentlyEnabled) {
                    if (wasDirty || !this.mTempDisplayInfo.equals(newDisplayInfo)) {
                        if (diff == 16) {
                            this.mLogicalDisplaysToUpdate.put(displayId, 7);
                            i = 2;
                        } else {
                            i = 2;
                            this.mLogicalDisplaysToUpdate.put(displayId, 2);
                        }
                    } else if (updateState == 1) {
                        this.mLogicalDisplaysToUpdate.put(displayId, 6);
                        i = 2;
                    } else if (!display.getPendingFrameRateOverrideUids().isEmpty()) {
                        this.mLogicalDisplaysToUpdate.put(displayId, 5);
                        i = 2;
                    } else if (this.mLogicalDisplayMapperExt.updateLogicalDisplaysLocked(display)) {
                        assignDisplayGroupLocked(display);
                        i = 2;
                        this.mLogicalDisplaysToUpdate.put(displayId, 2);
                    } else if (this.mLogicalDisplayMapperExt.hasFoldRemapDisplayDisableFeature() && this.mInteractive && newDisplayInfo.state == 1) {
                        this.mLogicalDisplaysToUpdate.put(displayId, 6);
                        i = 2;
                    } else {
                        display.getNonOverrideDisplayInfoLocked(this.mTempDisplayInfo);
                        if (this.mTempNonOverrideDisplayInfo.equals(this.mTempDisplayInfo)) {
                            i = 2;
                        } else {
                            i = 2;
                            this.mLogicalDisplaysToUpdate.put(displayId, 2);
                        }
                    }
                } else {
                    int event = isCurrentlyEnabled ? 1 : 3;
                    this.mLogicalDisplaysToUpdate.put(displayId, event);
                    i = 2;
                }
                this.mLogicalDisplayMapperExt.resetPowerModeChanged(display);
                this.mUpdatedLogicalDisplays.put(displayId, i);
            }
            i2--;
            z = true;
        }
        for (int i3 = this.mDisplayGroups.size() - 1; i3 >= 0; i3--) {
            int groupId = this.mDisplayGroups.keyAt(i3);
            com.android.server.display.DisplayGroup group = this.mDisplayGroups.valueAt(i3);
            boolean wasPreviouslyUpdated2 = this.mUpdatedDisplayGroups.indexOfKey(groupId) > -1;
            int changeCount = group.getChangeCountLocked();
            if (group.isEmptyLocked()) {
                this.mUpdatedDisplayGroups.delete(groupId);
                if (wasPreviouslyUpdated2) {
                    this.mDisplayGroupsToUpdate.put(groupId, 3);
                }
            } else {
                if (!wasPreviouslyUpdated2) {
                    this.mDisplayGroupsToUpdate.put(groupId, 1);
                } else if (this.mUpdatedDisplayGroups.get(groupId) != changeCount) {
                    this.mDisplayGroupsToUpdate.put(groupId, 2);
                }
                this.mUpdatedDisplayGroups.put(groupId, changeCount);
            }
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateLogicalDisplaysLocked updated=" + this.mUpdatedLogicalDisplays + " toUpdate=" + this.mLogicalDisplaysToUpdate + " toGroups=" + this.mDisplayGroupsToUpdate + " size=" + this.mLogicalDisplays.size());
        }
        sendUpdatesForDisplaysLocked(6);
        sendUpdatesForGroupsLocked(1);
        sendUpdatesForDisplaysLocked(3);
        if (this.mFlags.isConnectedDisplayManagementEnabled()) {
            sendUpdatesForDisplaysLocked(9);
        }
        sendUpdatesForDisplaysLocked(2);
        sendUpdatesForDisplaysLocked(5);
        sendUpdatesForDisplaysLocked(4);
        if (this.mFlags.isConnectedDisplayManagementEnabled()) {
            sendUpdatesForDisplaysLocked(8);
        }
        sendUpdatesForDisplaysLocked(1);
        sendUpdatesForDisplaysLocked(7);
        sendUpdatesForGroupsLocked(2);
        sendUpdatesForGroupsLocked(3);
        this.mLogicalDisplaysToUpdate.clear();
        this.mDisplayGroupsToUpdate.clear();
        if (reloop) {
            if (!isSecondLoop) {
                updateLogicalDisplaysLocked(diff, true);
            } else {
                android.util.Slog.wtf(TAG, "Trying to loop a third time");
            }
        }
    }

    private void sendUpdatesForDisplaysLocked(int msg) {
        for (int i = this.mLogicalDisplaysToUpdate.size() - 1; i >= 0; i--) {
            int currMsg = this.mLogicalDisplaysToUpdate.valueAt(i);
            if (currMsg == msg) {
                int id = this.mLogicalDisplaysToUpdate.keyAt(i);
                com.android.server.display.LogicalDisplay display = getDisplayLocked(id);
                if (DEBUG) {
                    com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
                    java.lang.String uniqueId = device == null ? "null" : device.getUniqueId();
                    android.util.Slog.d(TAG, "Sending " + displayEventToString(msg) + " for display=" + id + " with device=" + uniqueId);
                }
                if (this.mFlags.isConnectedDisplayManagementEnabled()) {
                    if (msg == 1) {
                        this.mDisplaysEnabledCache.put(id, true);
                    } else if (msg == 3) {
                        this.mDisplaysEnabledCache.delete(id);
                    }
                }
                this.mListener.onLogicalDisplayEventLocked(display, msg);
                if (this.mFlags.isConnectedDisplayManagementEnabled()) {
                    if (msg == 9) {
                        this.mLogicalDisplays.delete(id);
                    }
                } else if (msg == 3) {
                    this.mLogicalDisplays.delete(id);
                }
            }
        }
    }

    private void sendUpdatesForGroupsLocked(int msg) {
        for (int i = this.mDisplayGroupsToUpdate.size() - 1; i >= 0; i--) {
            int currMsg = this.mDisplayGroupsToUpdate.valueAt(i);
            if (currMsg == msg) {
                int id = this.mDisplayGroupsToUpdate.keyAt(i);
                this.mListener.onDisplayGroupEventLocked(id, msg);
                if (msg == 3) {
                    this.mDisplayGroups.delete(id);
                    int deviceIndex = this.mDeviceDisplayGroupIds.indexOfValue(id);
                    if (deviceIndex >= 0) {
                        this.mDeviceDisplayGroupIds.removeAt(deviceIndex);
                    }
                }
            }
        }
    }

    private void assignDisplayGroupLocked(com.android.server.display.LogicalDisplay display) {
        if (!display.isValidLocked()) {
            return;
        }
        com.android.server.display.DisplayDevice displayDevice = display.getPrimaryDisplayDeviceLocked();
        int displayId = display.getDisplayIdLocked();
        java.lang.String primaryDisplayUniqueId = displayDevice.getUniqueId();
        java.lang.Integer linkedDeviceUniqueId = this.mVirtualDeviceDisplayMapping.get(primaryDisplayUniqueId);
        int groupId = getDisplayGroupIdFromDisplayIdLocked(displayId);
        java.lang.Integer deviceDisplayGroupId = null;
        if (linkedDeviceUniqueId != null && this.mDeviceDisplayGroupIds.indexOfKey(linkedDeviceUniqueId.intValue()) > 0) {
            deviceDisplayGroupId = java.lang.Integer.valueOf(this.mDeviceDisplayGroupIds.get(linkedDeviceUniqueId.intValue()));
        }
        com.android.server.display.DisplayGroup oldGroup = getDisplayGroupLocked(groupId);
        java.lang.String groupName = display.getDisplayGroupNameLocked();
        com.android.server.display.DisplayDeviceInfo displayDeviceInfo = displayDevice.getDisplayDeviceInfoLocked();
        boolean needsOwnDisplayGroup = ((displayDeviceInfo.flags & 16384) == 0 && android.text.TextUtils.isEmpty(groupName)) ? false : true;
        boolean hasOwnDisplayGroup = groupId != 0;
        boolean needsDeviceDisplayGroup = (needsOwnDisplayGroup || linkedDeviceUniqueId == null) ? false : true;
        boolean hasDeviceDisplayGroup = deviceDisplayGroupId != null && groupId == deviceDisplayGroupId.intValue();
        if (groupId == -1 || hasOwnDisplayGroup != needsOwnDisplayGroup || hasDeviceDisplayGroup != needsDeviceDisplayGroup) {
            groupId = assignDisplayGroupIdLocked(needsOwnDisplayGroup, display.getDisplayGroupNameLocked(), needsDeviceDisplayGroup, linkedDeviceUniqueId);
        }
        com.android.server.display.DisplayGroup newGroup = getDisplayGroupLocked(groupId);
        if (newGroup == null) {
            newGroup = new com.android.server.display.DisplayGroup(groupId);
            this.mDisplayGroups.append(groupId, newGroup);
        }
        if (oldGroup != newGroup) {
            if (oldGroup != null) {
                oldGroup.removeDisplayLocked(display);
            }
            newGroup.addDisplayLocked(display);
            display.updateDisplayGroupIdLocked(groupId);
            android.util.Slog.i(TAG, "Setting new display group " + groupId + " for display " + displayId + ", from previous group: " + (oldGroup != null ? java.lang.Integer.valueOf(oldGroup.getGroupId()) : "null"));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0151 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void resetLayoutLocked(int r23, int r24, boolean r25) {
        /*
            Method dump skipped, instruction units count: 350
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.LogicalDisplayMapper.resetLayoutLocked(int, int, boolean):void");
    }

    private void applyLayoutLocked() {
        java.lang.String thermalBrightnessThrottlingMapId;
        com.android.server.display.layout.Layout oldLayout = this.mCurrentLayout;
        this.mCurrentLayout = this.mDeviceStateToLayoutMap.get(this.mDeviceState);
        android.util.Slog.i(TAG, "Applying layout: " + this.mCurrentLayout + ", Previous layout: " + oldLayout);
        int size = this.mCurrentLayout.size();
        for (int i = 0; i < size; i++) {
            com.android.server.display.layout.Layout.Display displayLayout = this.mCurrentLayout.getAt(i);
            android.view.DisplayAddress address = displayLayout.getAddress();
            com.android.server.display.DisplayDevice device = this.mDisplayDeviceRepo.getByAddressLocked(address);
            if (device == null) {
                android.util.Slog.w(TAG, "applyLayoutLocked: The display device (" + address + "), is not available for the display state " + this.mDeviceState);
            } else {
                int logicalDisplayId = displayLayout.getLogicalDisplayId();
                com.android.server.display.LogicalDisplay newDisplay = getDisplayLocked(logicalDisplayId);
                boolean newDisplayCreated = false;
                if (newDisplay == null) {
                    newDisplay = createNewLogicalDisplayLocked(null, logicalDisplayId);
                    newDisplayCreated = true;
                }
                com.android.server.display.LogicalDisplay oldDisplay = getDisplayLocked(device);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "old:" + oldDisplay.toStringMini() + " new:" + newDisplay.toStringMini());
                }
                if (newDisplay != oldDisplay) {
                    if (!newDisplayCreated && this.mWindowManagerPolicy != null) {
                        this.mWindowManagerPolicy.onDisplaySwitchStart(newDisplay.getDisplayIdLocked());
                    }
                    newDisplay.swapDisplaysLocked(oldDisplay);
                }
                com.android.server.display.DisplayDeviceConfig config = device.getDisplayDeviceConfig();
                newDisplay.setDevicePositionLocked(displayLayout.getPosition());
                newDisplay.setLeadDisplayLocked(displayLayout.getLeadDisplayId());
                newDisplay.updateLayoutLimitedRefreshRateLocked(config.getRefreshRange(displayLayout.getRefreshRateZoneId()));
                newDisplay.updateThermalRefreshRateThrottling(config.getThermalRefreshRateThrottlingData(displayLayout.getRefreshRateThermalThrottlingMapId()));
                boolean overrideState = this.mLogicalDisplayMapperExt.getOverrideState(displayLayout.isEnabled(), newDisplay);
                setEnabledLocked(newDisplay, overrideState);
                if (displayLayout.getThermalBrightnessThrottlingMapId() == null) {
                    thermalBrightnessThrottlingMapId = "default";
                } else {
                    thermalBrightnessThrottlingMapId = displayLayout.getThermalBrightnessThrottlingMapId();
                }
                newDisplay.setThermalBrightnessThrottlingDataIdLocked(thermalBrightnessThrottlingMapId);
                newDisplay.setPowerThrottlingDataIdLocked(displayLayout.getPowerThrottlingMapId() != null ? displayLayout.getPowerThrottlingMapId() : "default");
                newDisplay.setDisplayGroupNameLocked(displayLayout.getDisplayGroupName());
            }
        }
    }

    private com.android.server.display.LogicalDisplay createNewLogicalDisplayLocked(com.android.server.display.DisplayDevice device, int displayId) {
        int assignedDisplayId = ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).getLastAssignedDisplayId(device);
        if (assignedDisplayId != -1) {
            displayId = assignedDisplayId;
            android.util.Slog.d(TAG, "createNewLogicalDisplay and assign displayId: " + displayId);
        }
        if (device != null && device.getDisplayDeviceInfoLocked().type != 1) {
            ((com.android.server.display.IMirageDisplayManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IMirageDisplayManagerExt.class).create()).recordDisplayIdForDisplay(device, displayId);
        }
        int layerStack = assignLayerStackLocked(displayId);
        com.android.server.display.LogicalDisplay display = new com.android.server.display.LogicalDisplay(displayId, layerStack, device, this.mFlags.isPixelAnisotropyCorrectionInLogicalDisplayEnabled(), this.mFlags.isAlwaysRotateDisplayDeviceEnabled());
        display.updateLocked(this.mDisplayDeviceRepo, this.mSyntheticModeManager);
        display.getDisplayInfoLocked();
        this.mLogicalDisplays.put(displayId, display);
        return display;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0012  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void setEnabledLocked(com.android.server.display.LogicalDisplay r7, boolean r8) {
        /*
            r6 = this;
            int r0 = r7.getDisplayIdLocked()
            android.view.DisplayInfo r1 = r7.getDisplayInfoLocked()
            boolean r2 = r6.mSingleDisplayDemoMode
            if (r2 == 0) goto L12
            int r2 = r1.type
            r3 = 1
            if (r2 == r3) goto L12
            goto L13
        L12:
            r3 = 0
        L13:
            r2 = r3
            java.lang.String r3 = "LogicalDisplayMapper"
            if (r8 == 0) goto L35
            if (r2 == 0) goto L35
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Not creating a logical display for a secondary display because single display demo mode is enabled: "
            java.lang.StringBuilder r4 = r4.append(r5)
            android.view.DisplayInfo r5 = r7.getDisplayInfoLocked()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Slog.i(r3, r4)
            r8 = 0
        L35:
            boolean r4 = r7.isEnabledLocked()
            if (r4 == r8) goto L5e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "SetEnabled on display "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r5 = ": "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r8)
            java.lang.String r4 = r4.toString()
            android.util.Slog.i(r3, r4)
            r7.setEnabledLocked(r8)
        L5e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.LogicalDisplayMapper.setEnabledLocked(com.android.server.display.LogicalDisplay, boolean):void");
    }

    private int assignDisplayGroupIdLocked(boolean isOwnDisplayGroup, java.lang.String displayGroupName, boolean isDeviceDisplayGroup, java.lang.Integer linkedDeviceUniqueId) {
        if (isDeviceDisplayGroup && linkedDeviceUniqueId != null) {
            int deviceDisplayGroupId = this.mDeviceDisplayGroupIds.get(linkedDeviceUniqueId.intValue());
            if (deviceDisplayGroupId == 0) {
                int deviceDisplayGroupId2 = this.mNextNonDefaultGroupId;
                this.mNextNonDefaultGroupId = deviceDisplayGroupId2 + 1;
                this.mDeviceDisplayGroupIds.put(linkedDeviceUniqueId.intValue(), deviceDisplayGroupId2);
                return deviceDisplayGroupId2;
            }
            return deviceDisplayGroupId;
        }
        if (!isOwnDisplayGroup) {
            return 0;
        }
        java.lang.Integer displayGroupId = this.mDisplayGroupIdsByName.get(displayGroupName);
        if (displayGroupId == null) {
            int i = this.mNextNonDefaultGroupId;
            this.mNextNonDefaultGroupId = i + 1;
            displayGroupId = java.lang.Integer.valueOf(i);
            this.mDisplayGroupIdsByName.put(displayGroupName, displayGroupId);
        }
        return displayGroupId.intValue();
    }

    private void initializeDefaultDisplayDeviceLocked(com.android.server.display.DisplayDevice device) {
        com.android.server.display.layout.Layout layout = this.mDeviceStateToLayoutMap.get(-1);
        if (layout.getById(0) != null) {
            return;
        }
        com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
        layout.createDefaultDisplayLocked(info.address, this.mIdProducer);
    }

    private int assignLayerStackLocked(int displayId) {
        return displayId;
    }

    public android.util.SparseArray<com.android.server.display.LogicalDisplay> getLogicalDisplays() {
        return this.mLogicalDisplays;
    }

    private void requestDisplaySwitchOff() {
        com.android.server.display.DisplayDevice displayDevice;
        for (int i = this.mLogicalDisplays.size() - 1; i >= 0; i--) {
            int displayId = this.mLogicalDisplays.keyAt(i);
            com.android.server.display.LogicalDisplay display = this.mLogicalDisplays.valueAt(i);
            android.view.DisplayInfo newDisplayInfo = display.getDisplayInfoLocked();
            this.mUpdatedLogicalDisplays.get(displayId, 0);
            if (!display.isEnabledLocked() && newDisplayInfo.state == 2 && (displayDevice = display.getPrimaryDisplayDeviceLocked()) != null) {
                java.lang.Runnable work = displayDevice.requestDisplayStateLocked(1, -1.0f, -1.0f, null);
                android.util.Slog.d(TAG, "updateLogicalDisplaysLocked OFF id=" + displayDevice.getUniqueId());
                if (work != null) {
                    this.mHandler.post(work);
                }
            }
        }
    }

    private android.util.SparseBooleanArray toSparseBooleanArray(int[] input) {
        android.util.SparseBooleanArray retval = new android.util.SparseBooleanArray(2);
        for (int i = 0; input != null && i < input.length; i++) {
            retval.put(input[i], true);
        }
        return retval;
    }

    private boolean shouldStayAwakeOnFold() {
        return this.mFoldSettingProvider.shouldStayAwakeOnFold() || (this.mFoldSettingProvider.shouldSelectiveStayAwakeOnFold() && this.mFoldGracePeriodProvider.isEnabled());
    }

    private java.lang.String displayEventToString(int msg) {
        switch (msg) {
            case 1:
                return "added";
            case 2:
                return "changed";
            case 3:
                return "removed";
            case 4:
                return "swapped";
            case 5:
                return "framerate_override";
            case 6:
                return "transition";
            case 7:
                return "hdr_sdr_ratio_changed";
            case 8:
                return "connected";
            case 9:
                return "disconnected";
            default:
                return null;
        }
    }

    void setDisplayEnabledLocked(com.android.server.display.LogicalDisplay display, boolean enabled) {
        boolean isEnabled = display.isEnabledLocked();
        if (isEnabled == enabled) {
            android.util.Slog.w(TAG, "Display is already " + (isEnabled ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED) + ": " + display.getDisplayIdLocked());
        } else {
            setEnabledLocked(display, enabled);
            updateLogicalDisplaysLocked();
        }
    }

    private class LogicalDisplayMapperHandler extends android.os.Handler {
        LogicalDisplayMapperHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    synchronized (com.android.server.display.LogicalDisplayMapper.this.mSyncRoot) {
                        com.android.server.display.LogicalDisplayMapper.this.finishStateTransitionLocked(true);
                        break;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public com.android.server.display.IOplusLogicDisplayMapperWrapper getWrapper() {
        return this.mWrapper;
    }

    private class OplusLogicDisplayMapperWrapper implements com.android.server.display.IOplusLogicDisplayMapperWrapper {
        private OplusLogicDisplayMapperWrapper() {
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public int getPendingDeviceState() {
            return com.android.server.display.LogicalDisplayMapper.this.mPendingDeviceState;
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public void setPendingDeviceState(int state) {
            com.android.server.display.LogicalDisplayMapper.this.mPendingDeviceState = state;
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public int getDeviceState() {
            return com.android.server.display.LogicalDisplayMapper.this.mDeviceState;
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public void dispatchDelayedDeviceState(int delayedState) {
            synchronized (com.android.server.display.LogicalDisplayMapper.this.mSyncRoot) {
                com.android.server.display.LogicalDisplayMapper.this.setDeviceStateLocked(delayedState);
            }
        }

        @Override // com.android.server.display.IOplusLogicDisplayMapperWrapper
        public android.os.Handler getHandler() {
            return com.android.server.display.LogicalDisplayMapper.this.mHandler;
        }
    }
}
