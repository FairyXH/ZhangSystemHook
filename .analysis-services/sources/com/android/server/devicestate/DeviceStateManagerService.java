package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
public final class DeviceStateManagerService extends com.android.server.SystemService {
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final android.hardware.devicestate.DeviceState INVALID_DEVICE_STATE = new android.hardware.devicestate.DeviceState(new android.hardware.devicestate.DeviceState.Configuration.Builder(-1, "INVALID").build());
    private static final java.lang.String TAG = "DeviceStateManagerService";
    private java.util.Optional<com.android.server.devicestate.OverrideRequest> mActiveBaseStateOverride;
    private java.util.Optional<com.android.server.devicestate.OverrideRequest> mActiveOverride;
    public com.android.server.wm.ActivityTaskManagerInternal mActivityTaskManagerInternal;
    private java.util.Optional<android.hardware.devicestate.DeviceState> mBaseState;
    private final com.android.server.devicestate.DeviceStateManagerService.BinderService mBinderService;
    private java.util.Optional<android.hardware.devicestate.DeviceState> mCommittedState;
    private com.android.server.devicestate.IDeviceStateManagerServiceExt mDeviceStateManagerServiceExt;
    private final com.android.server.devicestate.DeviceStateNotificationController mDeviceStateNotificationController;
    private final com.android.server.devicestate.DeviceStatePolicy mDeviceStatePolicy;
    private final com.android.server.devicestate.DeviceStateManagerService.DeviceStateProviderListener mDeviceStateProviderListener;
    private android.util.SparseArray<android.hardware.devicestate.DeviceState> mDeviceStates;
    private java.util.Set<java.lang.Integer> mDeviceStatesAvailableForAppRequests;
    private boolean mDeviceStatesEnabled;
    private java.util.Set<java.lang.Integer> mFoldedDeviceStates;
    private final android.os.Handler mHandler;
    private boolean mIsPolicyWaitingForState;
    private final java.lang.Object mLock;
    private final com.android.server.devicestate.OverrideRequestController mOverrideRequestController;
    com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver mOverrideRequestScreenObserver;
    private java.util.Optional<android.hardware.devicestate.DeviceState> mPendingState;
    final android.app.IProcessObserver mProcessObserver;
    private final android.util.SparseArray<com.android.server.devicestate.DeviceStateManagerService.ProcessRecord> mProcessRecords;
    private com.android.server.devicestate.OverrideRequest mRearDisplayPendingOverrideRequest;
    private android.hardware.devicestate.DeviceState mRearDisplayState;
    private final com.android.server.devicestate.DeviceStateManagerService.SystemPropertySetter mSystemPropertySetter;
    public com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private com.android.server.devicestate.IDeviceStateManagerServiceWrapper mWrapper;

    interface SystemPropertySetter {
        void setDebugTracingDeviceStateProperty(java.lang.String str);
    }

    public DeviceStateManagerService(android.content.Context context) {
        this(context, com.android.server.devicestate.DeviceStatePolicy.Provider.fromResources(context.getResources()).instantiate(context));
    }

    private DeviceStateManagerService(android.content.Context context, com.android.server.devicestate.DeviceStatePolicy policy) {
        this(context, policy, new com.android.server.devicestate.DeviceStateManagerService.SystemPropertySetter() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda3
            @Override // com.android.server.devicestate.DeviceStateManagerService.SystemPropertySetter
            public final void setDebugTracingDeviceStateProperty(java.lang.String str) {
                android.os.SystemProperties.set("debug.tracing.device_state", str);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    DeviceStateManagerService(android.content.Context context, com.android.server.devicestate.DeviceStatePolicy deviceStatePolicy, com.android.server.devicestate.DeviceStateManagerService.SystemPropertySetter systemPropertySetter) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mDeviceStates = new android.util.SparseArray<>();
        this.mCommittedState = java.util.Optional.empty();
        this.mPendingState = java.util.Optional.empty();
        this.mIsPolicyWaitingForState = false;
        this.mBaseState = java.util.Optional.empty();
        this.mActiveOverride = java.util.Optional.empty();
        this.mActiveBaseStateOverride = java.util.Optional.empty();
        this.mProcessRecords = new android.util.SparseArray<>();
        this.mDeviceStatesAvailableForAppRequests = new java.util.HashSet();
        this.mDeviceStateManagerServiceExt = (com.android.server.devicestate.IDeviceStateManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.devicestate.IDeviceStateManagerServiceExt.class).base(this).create();
        this.mProcessObserver = new android.app.IProcessObserver.Stub() { // from class: com.android.server.devicestate.DeviceStateManagerService.1
            public void onForegroundActivitiesChanged(int pid, int uid, boolean fg) {
                synchronized (com.android.server.devicestate.DeviceStateManagerService.this.mLock) {
                    if (com.android.server.devicestate.DeviceStateManagerService.this.shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                        com.android.server.devicestate.OverrideRequest request = (com.android.server.devicestate.OverrideRequest) com.android.server.devicestate.DeviceStateManagerService.this.mActiveOverride.get();
                        if (pid == request.getPid() && uid == request.getUid()) {
                            if (!fg) {
                                com.android.server.devicestate.DeviceStateManagerService.this.mOverrideRequestController.cancelRequest(request);
                            }
                        }
                    }
                }
            }

            public void onProcessStarted(int pid, int processUid, int packageUid, java.lang.String packageName, java.lang.String processName) {
            }

            public void onProcessDied(int pid, int uid) {
            }

            public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) {
            }
        };
        this.mOverrideRequestScreenObserver = new com.android.server.devicestate.DeviceStateManagerService.OverrideRequestScreenObserver();
        this.mWrapper = new com.android.server.devicestate.DeviceStateManagerService.DeviceStateManagerServiceWrapper();
        this.mSystemPropertySetter = systemPropertySetter;
        this.mHandler = new android.os.Handler(com.android.server.DisplayThread.get().getLooper());
        this.mOverrideRequestController = new com.android.server.devicestate.OverrideRequestController(new com.android.server.devicestate.OverrideRequestController.StatusChangeListener() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda6
            @Override // com.android.server.devicestate.OverrideRequestController.StatusChangeListener
            public final void onStatusChanged(com.android.server.devicestate.OverrideRequest overrideRequest, int i, int i2) {
                this.f$0.onOverrideRequestStatusChangedLocked(overrideRequest, i, i2);
            }
        });
        this.mDeviceStatePolicy = deviceStatePolicy;
        this.mDeviceStateProviderListener = new com.android.server.devicestate.DeviceStateManagerService.DeviceStateProviderListener();
        this.mDeviceStatePolicy.getDeviceStateProvider().setListener(this.mDeviceStateProviderListener);
        this.mDeviceStatePolicy.getDeviceStateProvider().registerSensor();
        this.mBinderService = new com.android.server.devicestate.DeviceStateManagerService.BinderService();
        this.mActivityTaskManagerInternal = (com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class);
        this.mDeviceStateNotificationController = new com.android.server.devicestate.DeviceStateNotificationController(context, this.mHandler, new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        synchronized (this.mLock) {
            java.util.Optional<com.android.server.devicestate.OverrideRequest> optional = this.mActiveOverride;
            com.android.server.devicestate.OverrideRequestController overrideRequestController = this.mOverrideRequestController;
            java.util.Objects.requireNonNull(overrideRequestController);
            optional.ifPresent(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda4(overrideRequestController));
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("device_state", this.mBinderService);
        publishLocalService(android.hardware.devicestate.DeviceStateManagerInternal.class, new com.android.server.devicestate.DeviceStateManagerService.LocalService());
        synchronized (this.mLock) {
            readStatesAvailableForRequestFromApps();
            this.mFoldedDeviceStates = readFoldedStates();
        }
        this.mActivityTaskManagerInternal.registerScreenObserver(this.mOverrideRequestScreenObserver);
        ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).registerProcessObserver(this.mProcessObserver);
    }

    android.os.Handler getHandler() {
        return this.mHandler;
    }

    java.util.Optional<android.hardware.devicestate.DeviceState> getCommittedState() {
        java.util.Optional<android.hardware.devicestate.DeviceState> optional;
        synchronized (this.mLock) {
            optional = this.mCommittedState;
        }
        return optional;
    }

    java.util.Optional<android.hardware.devicestate.DeviceState> getPendingState() {
        java.util.Optional<android.hardware.devicestate.DeviceState> optional;
        synchronized (this.mLock) {
            optional = this.mPendingState;
        }
        return optional;
    }

    java.util.Optional<android.hardware.devicestate.DeviceState> getBaseState() {
        java.util.Optional<android.hardware.devicestate.DeviceState> optional;
        synchronized (this.mLock) {
            optional = this.mBaseState;
        }
        return optional;
    }

    java.util.Optional<android.hardware.devicestate.DeviceState> getOverrideState() {
        synchronized (this.mLock) {
            if (this.mActiveOverride.isPresent()) {
                return getStateLocked(this.mActiveOverride.get().getRequestedStateIdentifier());
            }
            return java.util.Optional.empty();
        }
    }

    java.util.Optional<android.hardware.devicestate.DeviceState> getOverrideBaseState() {
        synchronized (this.mLock) {
            if (this.mActiveBaseStateOverride.isPresent()) {
                return getStateLocked(this.mActiveBaseStateOverride.get().getRequestedStateIdentifier());
            }
            return java.util.Optional.empty();
        }
    }

    java.util.List<android.hardware.devicestate.DeviceState> getSupportedStates() {
        java.util.List<android.hardware.devicestate.DeviceState> supportedStatesLocked;
        synchronized (this.mLock) {
            supportedStatesLocked = getSupportedStatesLocked();
        }
        return supportedStatesLocked;
    }

    private java.util.List<android.hardware.devicestate.DeviceState> getSupportedStatesLocked() {
        java.util.List<android.hardware.devicestate.DeviceState> supportedStates = new java.util.ArrayList<>(this.mDeviceStates.size());
        for (int i = 0; i < this.mDeviceStates.size(); i++) {
            supportedStates.add(i, this.mDeviceStates.valueAt(i));
        }
        return this.mDeviceStateManagerServiceExt.getSupportedStates(supportedStates, this.mDeviceStates);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getSupportedStateIdentifiersLocked() {
        int[] supportedStates = new int[this.mDeviceStates.size()];
        for (int i = 0; i < supportedStates.length; i++) {
            supportedStates[i] = this.mDeviceStates.valueAt(i).getIdentifier();
        }
        return this.mDeviceStateManagerServiceExt.getSupportedStateIdentifiersLocked(supportedStates, this.mDeviceStates);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.devicestate.DeviceStateInfo getDeviceStateInfoLocked() {
        java.util.List<android.hardware.devicestate.DeviceState> supportedStates = getSupportedStatesLocked();
        android.hardware.devicestate.DeviceState baseState = this.mBaseState.orElse(INVALID_DEVICE_STATE);
        android.hardware.devicestate.DeviceState currentState = this.mCommittedState.orElse(INVALID_DEVICE_STATE);
        return new android.hardware.devicestate.DeviceStateInfo(new java.util.ArrayList(supportedStates), baseState, createMergedDeviceState(currentState, baseState));
    }

    private android.hardware.devicestate.DeviceState createMergedDeviceState(android.hardware.devicestate.DeviceState committedState, android.hardware.devicestate.DeviceState baseState) {
        if (committedState.equals(INVALID_DEVICE_STATE)) {
            return INVALID_DEVICE_STATE;
        }
        java.util.Set<java.lang.Integer> systemProperties = committedState.getConfiguration().getSystemProperties();
        java.util.Set<java.lang.Integer> physicalProperties = baseState.getConfiguration().getPhysicalProperties();
        android.hardware.devicestate.DeviceState.Configuration deviceStateConfiguration = new android.hardware.devicestate.DeviceState.Configuration.Builder(committedState.getIdentifier(), committedState.getName()).setSystemProperties(systemProperties).setPhysicalProperties(physicalProperties).build();
        return new android.hardware.devicestate.DeviceState(deviceStateConfiguration);
    }

    android.hardware.devicestate.IDeviceStateManager getBinderService() {
        return this.mBinderService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSupportedStates(android.hardware.devicestate.DeviceState[] supportedDeviceStates, int reason) {
        synchronized (this.mLock) {
            int[] oldStateIdentifiers = getSupportedStateIdentifiersLocked();
            boolean hasTerminalDeviceState = false;
            this.mDeviceStates.clear();
            for (android.hardware.devicestate.DeviceState state : supportedDeviceStates) {
                if (state.hasProperty(4)) {
                    hasTerminalDeviceState = true;
                }
                this.mDeviceStates.put(state.getIdentifier(), state);
            }
            this.mOverrideRequestController.setStickyRequestsAllowed(hasTerminalDeviceState);
            int[] newStateIdentifiers = getSupportedStateIdentifiersLocked();
            if (java.util.Arrays.equals(oldStateIdentifiers, newStateIdentifiers)) {
                return;
            }
            this.mOverrideRequestController.handleNewSupportedStates(newStateIdentifiers, reason);
            updatePendingStateLocked();
            setRearDisplayStateLocked();
            notifyDeviceStateInfoChangedAsync();
            this.mHandler.post(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda0(this));
        }
    }

    private void setRearDisplayStateLocked() {
        int rearDisplayIdentifier = getContext().getResources().getInteger(android.R.integer.config_defaultUndimsRequired);
        if (rearDisplayIdentifier != -1) {
            this.mRearDisplayState = this.mDeviceStates.get(rearDisplayIdentifier);
        }
    }

    private boolean isSupportedStateLocked(int identifier) {
        return this.mDeviceStates.contains(identifier);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.Optional<android.hardware.devicestate.DeviceState> getStateLocked(int identifier) {
        return java.util.Optional.ofNullable(this.mDeviceStates.get(identifier));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBaseState(int identifier) {
        synchronized (this.mLock) {
            java.util.Optional<android.hardware.devicestate.DeviceState> baseStateOptional = getStateLocked(identifier);
            if (baseStateOptional.isEmpty()) {
                throw new java.lang.IllegalArgumentException("Base state is not supported");
            }
            android.hardware.devicestate.DeviceState baseState = baseStateOptional.get();
            if (this.mBaseState.isPresent() && this.mBaseState.get().equals(baseState)) {
                return;
            }
            if (this.mRearDisplayPendingOverrideRequest != null) {
                handleRearDisplayBaseStateChangedLocked(identifier);
            }
            this.mBaseState = java.util.Optional.of(baseState);
            int overrideIdentifier = this.mDeviceStateManagerServiceExt.overrideBaseState(this.mBaseState, identifier);
            java.util.Optional<android.hardware.devicestate.DeviceState> overrideBaseState = getStateLocked(overrideIdentifier);
            if (overrideBaseState.isPresent()) {
                this.mBaseState = java.util.Optional.of(overrideBaseState.get());
                boolean canCancel = this.mDeviceStateManagerServiceExt.canCancelRequestState();
                if (shouldCancelOverrideRequest() && (canCancel || this.mDeviceStateManagerServiceExt.hasFoldRemapDisplayDisableFeature())) {
                    this.mOverrideRequestController.cancelOverrideRequest();
                }
                this.mOverrideRequestController.handleBaseStateChanged(identifier);
                updatePendingStateLocked();
                notifyDeviceStateInfoChangedAsync();
                if (this.mDeviceStateManagerServiceExt.notifyPolicyImmediately()) {
                    this.mHandler.postAtFrontOfQueue(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda0(this));
                } else {
                    this.mHandler.post(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda0(this));
                }
            }
        }
    }

    private boolean updatePendingStateLocked() {
        android.hardware.devicestate.DeviceState stateToConfigure;
        if (this.mPendingState.isPresent()) {
            return false;
        }
        if (this.mActiveOverride.isPresent()) {
            stateToConfigure = getStateLocked(this.mActiveOverride.get().getRequestedStateIdentifier()).get();
        } else if (this.mBaseState.isPresent() && isSupportedStateLocked(this.mBaseState.get().getIdentifier())) {
            stateToConfigure = this.mBaseState.get();
        } else {
            stateToConfigure = null;
        }
        if (stateToConfigure == null) {
            return false;
        }
        if (this.mCommittedState.isPresent() && stateToConfigure.equals(this.mCommittedState.get())) {
            return false;
        }
        this.mPendingState = java.util.Optional.of(stateToConfigure);
        this.mIsPolicyWaitingForState = true;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPolicyIfNeeded() {
        if (java.lang.Thread.holdsLock(this.mLock)) {
            java.lang.Throwable error = new java.lang.Throwable("Attempting to notify DeviceStatePolicy with service lock held");
            error.fillInStackTrace();
            android.util.Slog.w(TAG, error);
        }
        synchronized (this.mLock) {
            if (this.mIsPolicyWaitingForState) {
                this.mIsPolicyWaitingForState = false;
                int state = this.mPendingState.get().getIdentifier();
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Notifying policy to configure state: " + state);
                }
                if (state == 3 || state == 0 || state == 2) {
                    this.mDeviceStateManagerServiceExt.setSwitchingTrackerSensorEventLog();
                }
                this.mDeviceStatePolicy.configureDeviceForState(state, new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.commitPendingState();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void commitPendingState() {
        com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord;
        synchronized (this.mLock) {
            android.hardware.devicestate.DeviceState newState = this.mPendingState.get();
            if (DEBUG) {
                android.util.Slog.d(TAG, "Committing state: " + newState);
            }
            com.android.internal.util.FrameworkStatsLog.write(350, newState.getIdentifier(), !this.mCommittedState.isPresent());
            java.lang.String traceString = newState.getIdentifier() + ":" + newState.getName();
            android.os.Trace.instantForTrack(524288L, "DeviceStateChanged", traceString);
            this.mSystemPropertySetter.setDebugTracingDeviceStateProperty(traceString);
            this.mCommittedState = java.util.Optional.of(newState);
            this.mPendingState = java.util.Optional.empty();
            updatePendingStateLocked();
            notifyDeviceStateInfoChangedAsync();
            com.android.server.devicestate.OverrideRequest activeRequest = this.mActiveOverride.orElse(null);
            if (activeRequest != null && activeRequest.getRequestedStateIdentifier() == newState.getIdentifier() && (processRecord = this.mProcessRecords.get(activeRequest.getPid())) != null) {
                processRecord.notifyRequestActiveAsync(activeRequest.getToken());
            }
            this.mHandler.post(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda0(this));
        }
    }

    private void notifyDeviceStateInfoChangedAsync() {
        synchronized (this.mLock) {
            if (this.mPendingState.isPresent()) {
                android.util.Slog.i(TAG, "Cannot notify device state info change when pending state is present.");
                return;
            }
            if (this.mBaseState.isPresent() && this.mCommittedState.isPresent()) {
                if (this.mCommittedState.isPresent()) {
                    this.mDeviceStateManagerServiceExt.setDeviceStateInfo(getDeviceStateInfoLocked());
                }
                if (this.mProcessRecords.size() == 0) {
                    return;
                }
                java.util.ArrayList<com.android.server.devicestate.DeviceStateManagerService.ProcessRecord> registeredProcesses = new java.util.ArrayList<>();
                for (int i = 0; i < this.mProcessRecords.size(); i++) {
                    registeredProcesses.add(this.mProcessRecords.valueAt(i));
                }
                this.mDeviceStateManagerServiceExt.shouldInjectTransitoryState(this.mCommittedState);
                android.hardware.devicestate.DeviceStateInfo info = getDeviceStateInfoLocked();
                for (int i2 = 0; i2 < registeredProcesses.size(); i2++) {
                    registeredProcesses.get(i2).notifyDeviceStateInfoAsync(info);
                }
                return;
            }
            android.util.Slog.e(TAG, "Cannot notify device state info change before the initial state has been committed.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOverrideRequestStatusChangedLocked(com.android.server.devicestate.OverrideRequest request, int status, int flags) {
        if (request.getRequestType() == 1) {
            switch (status) {
                case 1:
                    enableBaseStateRequestLocked(request);
                    return;
                case 2:
                    if (this.mActiveBaseStateOverride.isPresent() && this.mActiveBaseStateOverride.get() == request) {
                        this.mActiveBaseStateOverride = java.util.Optional.empty();
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException("Unknown request status: " + status);
            }
        } else if (request.getRequestType() == 0) {
            switch (status) {
                case 1:
                    this.mActiveOverride = java.util.Optional.of(request);
                    this.mDeviceStateNotificationController.showStateActiveNotificationIfNeeded(request.getRequestedStateIdentifier(), request.getUid());
                    break;
                case 2:
                    if (this.mActiveOverride.isPresent() && this.mActiveOverride.get() == request) {
                        this.mActiveOverride = java.util.Optional.empty();
                        this.mDeviceStateNotificationController.cancelNotification(request.getRequestedStateIdentifier());
                        if ((flags & 1) == 1) {
                            this.mDeviceStateNotificationController.showThermalCriticalNotificationIfNeeded(request.getRequestedStateIdentifier());
                        } else if ((flags & 2) == 2) {
                            this.mDeviceStateNotificationController.showPowerSaveNotificationIfNeeded(request.getRequestedStateIdentifier());
                        }
                    }
                    break;
                default:
                    throw new java.lang.IllegalArgumentException("Unknown request status: " + status);
            }
        } else {
            throw new java.lang.IllegalArgumentException("Unknown OverrideRest type: " + request.getRequestType());
        }
        this.mDeviceStateManagerServiceExt.setRequestState(status, request.getRequestedStateIdentifier(), request.getPid(), request.getFlags());
        if (DEBUG) {
            android.util.Slog.i(TAG, "overrideRequestStatusChange " + com.android.server.devicestate.OverrideRequestController.statusToString(status) + " requestedState:" + request.getRequestedStateIdentifier() + " flags:" + request.getFlags() + " pid:" + request.getPid() + " " + android.os.Debug.getCallers(15));
        }
        boolean updatedPendingState = updatePendingStateLocked();
        com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(request.getPid());
        if (processRecord == null) {
            this.mHandler.post(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda0(this));
            return;
        }
        if (status == 1) {
            if (!updatedPendingState && !this.mPendingState.isPresent()) {
                processRecord.notifyRequestActiveAsync(request.getToken());
            }
        } else {
            processRecord.notifyRequestCanceledAsync(request.getToken());
        }
        this.mHandler.post(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda0(this));
    }

    private void enableBaseStateRequestLocked(com.android.server.devicestate.OverrideRequest request) {
        setBaseState(request.getRequestedStateIdentifier());
        this.mActiveBaseStateOverride = java.util.Optional.of(request);
        com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(request.getPid());
        processRecord.notifyRequestActiveAsync(request.getToken());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerProcess(int pid, android.hardware.devicestate.IDeviceStateManagerCallback callback) {
        synchronized (this.mLock) {
            if (this.mProcessRecords.contains(pid)) {
                throw new java.lang.SecurityException("The calling process has already registered an IDeviceStateManagerCallback.");
            }
            com.android.server.devicestate.DeviceStateManagerService.ProcessRecord record = new com.android.server.devicestate.DeviceStateManagerService.ProcessRecord(callback, pid, new com.android.server.devicestate.DeviceStateManagerService.ProcessRecord.DeathListener() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda2
                @Override // com.android.server.devicestate.DeviceStateManagerService.ProcessRecord.DeathListener
                public final void onProcessDied(com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord) {
                    this.f$0.handleProcessDied(processRecord);
                }
            }, this.mHandler);
            try {
                callback.asBinder().linkToDeath(record, 0);
                this.mProcessRecords.put(pid, record);
                android.hardware.devicestate.DeviceStateInfo currentInfo = this.mCommittedState.isPresent() ? getDeviceStateInfoLocked() : null;
                if (currentInfo != null) {
                    record.notifyDeviceStateInfoAsync(currentInfo);
                }
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleProcessDied(com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord) {
        synchronized (this.mLock) {
            this.mProcessRecords.remove(processRecord.mPid);
            this.mOverrideRequestController.handleProcessDied(processRecord.mPid);
            if (shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                com.android.server.devicestate.OverrideRequest request = this.mActiveOverride.get();
                this.mOverrideRequestController.cancelRequest(request);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestStateInternal(int state, int flags, int callingPid, int callingUid, android.os.IBinder token, boolean hasControlDeviceStatePermission) {
        synchronized (this.mLock) {
            com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(callingPid);
            if (processRecord == null) {
                throw new java.lang.IllegalStateException("Process " + callingPid + " has no registered callback.");
            }
            if (this.mOverrideRequestController.hasRequest(token, 0)) {
                throw new java.lang.IllegalStateException("Request has already been made for the supplied token: " + token);
            }
            java.util.Optional<android.hardware.devicestate.DeviceState> deviceState = getStateLocked(state);
            if (deviceState.isPresent()) {
                if (this.mDeviceStateManagerServiceExt.canRequestState(this.mBaseState, state)) {
                    if (this.mBaseState.isPresent()) {
                        int baseState = this.mBaseState.get().getIdentifier();
                        if (baseState == 3 && state == 101) {
                            android.util.Slog.w(TAG, "Requested state: " + state + " is not supported when baseState is DEVICE_STATE_OPEN");
                            return;
                        }
                    }
                    com.android.server.devicestate.OverrideRequest request = new com.android.server.devicestate.OverrideRequest(token, callingPid, callingUid, deviceState.get(), flags, 0);
                    if (!hasControlDeviceStatePermission && this.mRearDisplayState != null && state == this.mRearDisplayState.getIdentifier()) {
                        showRearDisplayEducationalOverlayLocked(request);
                    } else {
                        this.mOverrideRequestController.addRequest(request);
                    }
                    return;
                }
                return;
            }
            throw new java.lang.IllegalArgumentException("Requested state: " + state + " is not supported.");
        }
    }

    private void showRearDisplayEducationalOverlayLocked(com.android.server.devicestate.OverrideRequest request) {
        this.mRearDisplayPendingOverrideRequest = request;
        com.android.server.statusbar.StatusBarManagerInternal statusBar = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
        if (statusBar != null) {
            statusBar.showRearDisplayDialog(this.mBaseState.get().getIdentifier());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelStateRequestInternal(int callingPid) {
        synchronized (this.mLock) {
            com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(callingPid);
            if (processRecord == null) {
                throw new java.lang.IllegalStateException("Process " + callingPid + " has no registered callback.");
            }
            java.util.Optional<com.android.server.devicestate.OverrideRequest> optional = this.mActiveOverride;
            com.android.server.devicestate.OverrideRequestController overrideRequestController = this.mOverrideRequestController;
            java.util.Objects.requireNonNull(overrideRequestController);
            optional.ifPresent(new com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda4(overrideRequestController));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestBaseStateOverrideInternal(int state, int flags, int callingPid, int callingUid, android.os.IBinder token) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    java.util.Optional<android.hardware.devicestate.DeviceState> deviceState = getStateLocked(state);
                    if (!deviceState.isPresent()) {
                        throw new java.lang.IllegalArgumentException("Requested state: " + state + " is not supported.");
                    }
                    com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(callingPid);
                    if (processRecord == null) {
                        throw new java.lang.IllegalStateException("Process " + callingPid + " has no registered callback.");
                    }
                    if (this.mOverrideRequestController.hasRequest(token, 1)) {
                        throw new java.lang.IllegalStateException("Request has already been made for the supplied token: " + token);
                    }
                    com.android.server.devicestate.OverrideRequest request = new com.android.server.devicestate.OverrideRequest(token, callingPid, callingUid, deviceState.get(), flags, 1);
                    this.mOverrideRequestController.addBaseStateRequest(request);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelBaseStateOverrideInternal(int callingPid) {
        synchronized (this.mLock) {
            com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(callingPid);
            if (processRecord == null) {
                throw new java.lang.IllegalStateException("Process " + callingPid + " has no registered callback.");
            }
            setBaseState(this.mDeviceStateProviderListener.mCurrentBaseState);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStateRequestOverlayDismissedInternal(boolean shouldCancelRequest) {
        synchronized (this.mLock) {
            if (this.mRearDisplayPendingOverrideRequest != null) {
                if (shouldCancelRequest) {
                    com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.get(this.mRearDisplayPendingOverrideRequest.getPid());
                    processRecord.notifyRequestCanceledAsync(this.mRearDisplayPendingOverrideRequest.getToken());
                } else {
                    this.mOverrideRequestController.addRequest(this.mRearDisplayPendingOverrideRequest);
                }
                this.mRearDisplayPendingOverrideRequest = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(java.io.PrintWriter pw) {
        pw.println("DEVICE STATE MANAGER (dumpsys device_state)");
        synchronized (this.mLock) {
            pw.println("  mCommittedState=" + this.mCommittedState);
            pw.println("  mPendingState=" + this.mPendingState);
            pw.println("  mBaseState=" + this.mBaseState);
            pw.println("  mOverrideState=" + getOverrideState());
            int processCount = this.mProcessRecords.size();
            pw.println();
            pw.println("Registered processes: size=" + processCount);
            for (int i = 0; i < processCount; i++) {
                com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord = this.mProcessRecords.valueAt(i);
                pw.println("  " + i + ": mPid=" + processRecord.mPid);
            }
            this.mOverrideRequestController.dumpInternal(pw);
            pw.println();
            this.mDeviceStatePolicy.dump(pw, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertCanRequestDeviceState(int callingPid, int callingUid, int state) {
        boolean isTopApp = isTopApp(callingPid);
        boolean isForegroundApp = isForegroundApp(callingPid, callingUid);
        boolean isStateAvailableForAppRequests = isStateAvailableForAppRequests(state);
        boolean canRequestState = isTopApp && isForegroundApp && isStateAvailableForAppRequests;
        if (!canRequestState) {
            getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to request device state, or the call must come from the top app and be a device state that is available for apps to request.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertCanControlDeviceState(int callingPid, int callingUid) {
        boolean isTopApp = isTopApp(callingPid);
        boolean isForegroundApp = isForegroundApp(callingPid, callingUid);
        boolean canControlState = isTopApp && isForegroundApp;
        if (!canControlState) {
            getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to request device state, or the call must come from the top app.");
        }
    }

    private boolean isForegroundApp(int callingPid, int callingUid) {
        try {
            java.util.List<android.app.ActivityManager.RunningAppProcessInfo> procs = android.app.ActivityManager.getService().getRunningAppProcesses();
            for (int i = 0; i < procs.size(); i++) {
                android.app.ActivityManager.RunningAppProcessInfo proc = procs.get(i);
                if (proc.pid == callingPid && proc.uid == callingUid && proc.importance <= 100) {
                    return true;
                }
            }
            return false;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "am.getRunningAppProcesses() failed", e);
            return false;
        }
    }

    private boolean isTopApp(int callingPid) {
        com.android.server.wm.WindowProcessController topApp = this.mActivityTaskManagerInternal.getTopApp();
        return topApp != null && topApp.getPid() == callingPid;
    }

    private boolean isStateAvailableForAppRequests(int state) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mDeviceStatesAvailableForAppRequests.contains(java.lang.Integer.valueOf(state));
        }
        return zContains;
    }

    private void readStatesAvailableForRequestFromApps() {
        java.lang.String[] availableAppStatesConfigIdentifiers = getContext().getResources().getStringArray(android.R.array.config_defaultNotificationVibeWaveform);
        for (java.lang.String identifierToFetch : availableAppStatesConfigIdentifiers) {
            int configValueIdentifier = getContext().getResources().getIdentifier(identifierToFetch, "integer", com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            int state = getContext().getResources().getInteger(configValueIdentifier);
            if (isValidState(state)) {
                this.mDeviceStatesAvailableForAppRequests.add(java.lang.Integer.valueOf(state));
            } else {
                android.util.Slog.e(TAG, "Invalid device state was found in the configuration file. State id: " + state);
            }
        }
    }

    private java.util.Set<java.lang.Integer> readFoldedStates() {
        java.util.Set<java.lang.Integer> foldedStates = new java.util.HashSet<>();
        int[] mFoldedStatesArray = getContext().getResources().getIntArray(android.R.array.config_face_acquire_vendor_keyguard_ignorelist);
        for (int i : mFoldedStatesArray) {
            foldedStates.add(java.lang.Integer.valueOf(i));
        }
        return foldedStates;
    }

    private boolean isValidState(int state) {
        for (int i = 0; i < this.mDeviceStates.size(); i++) {
            if (state == this.mDeviceStates.valueAt(i).getIdentifier()) {
                return true;
            }
        }
        return false;
    }

    private void handleRearDisplayBaseStateChangedLocked(int newBaseState) {
        if (isDeviceOpeningLocked(newBaseState)) {
            onStateRequestOverlayDismissedInternal(false);
        }
    }

    private boolean isDeviceOpeningLocked(final int newBaseState) {
        return this.mBaseState.filter(new java.util.function.Predicate() { // from class: com.android.server.devicestate.DeviceStateManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$isDeviceOpeningLocked$2(newBaseState, (android.hardware.devicestate.DeviceState) obj);
            }
        }).isPresent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$isDeviceOpeningLocked$2(int newBaseState, android.hardware.devicestate.DeviceState deviceState) {
        return this.mFoldedDeviceStates.contains(java.lang.Integer.valueOf(deviceState.getIdentifier())) && !this.mFoldedDeviceStates.contains(java.lang.Integer.valueOf(newBaseState));
    }

    private final class DeviceStateProviderListener implements com.android.server.devicestate.DeviceStateProvider.Listener {
        int mCurrentBaseState;

        private DeviceStateProviderListener() {
        }

        @Override // com.android.server.devicestate.DeviceStateProvider.Listener
        public void onSupportedDeviceStatesChanged(android.hardware.devicestate.DeviceState[] newDeviceStates, int reason) {
            if (newDeviceStates.length == 0) {
                throw new java.lang.IllegalArgumentException("Supported device states must not be empty");
            }
            com.android.server.devicestate.DeviceStateManagerService.this.updateSupportedStates(newDeviceStates, reason);
        }

        @Override // com.android.server.devicestate.DeviceStateProvider.Listener
        public void onStateChanged(int identifier) {
            if (identifier < 0 || identifier > 10000) {
                throw new java.lang.IllegalArgumentException("Invalid identifier: " + identifier);
            }
            this.mCurrentBaseState = identifier;
            com.android.server.devicestate.DeviceStateManagerService.this.setBaseState(identifier);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class ProcessRecord implements android.os.IBinder.DeathRecipient {
        private static final int STATUS_ACTIVE = 0;
        private static final int STATUS_CANCELED = 2;
        private static final int STATUS_SUSPENDED = 1;
        private final android.hardware.devicestate.IDeviceStateManagerCallback mCallback;
        private final com.android.server.devicestate.DeviceStateManagerService.ProcessRecord.DeathListener mDeathListener;
        private final android.os.Handler mHandler;
        private final java.util.WeakHashMap<android.os.IBinder, java.lang.Integer> mLastNotifiedStatus = new java.util.WeakHashMap<>();
        private final int mPid;

        public interface DeathListener {
            void onProcessDied(com.android.server.devicestate.DeviceStateManagerService.ProcessRecord processRecord);
        }

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        private @interface RequestStatus {
        }

        ProcessRecord(android.hardware.devicestate.IDeviceStateManagerCallback callback, int pid, com.android.server.devicestate.DeviceStateManagerService.ProcessRecord.DeathListener deathListener, android.os.Handler handler) {
            this.mCallback = callback;
            this.mPid = pid;
            this.mDeathListener = deathListener;
            this.mHandler = handler;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mDeathListener.onProcessDied(this);
        }

        public void notifyDeviceStateInfoAsync(final android.hardware.devicestate.DeviceStateInfo info) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$ProcessRecord$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyDeviceStateInfoAsync$0(info);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyDeviceStateInfoAsync$0(android.hardware.devicestate.DeviceStateInfo info) {
            boolean tracingEnabled = android.os.Trace.isTagEnabled(524288L);
            if (tracingEnabled) {
                android.os.Trace.traceBegin(524288L, "notifyDeviceStateInfoAsync(pid=" + this.mPid + ")");
            }
            try {
                try {
                    this.mCallback.onDeviceStateInfoChanged(info);
                    if (!tracingEnabled) {
                        return;
                    }
                } catch (android.os.RemoteException ex) {
                    android.util.Slog.w(com.android.server.devicestate.DeviceStateManagerService.TAG, "Failed to notify process " + this.mPid + " that device state changed.", ex);
                    if (!tracingEnabled) {
                        return;
                    }
                }
                android.os.Trace.traceEnd(524288L);
            } catch (java.lang.Throwable th) {
                if (tracingEnabled) {
                    android.os.Trace.traceEnd(524288L);
                }
                throw th;
            }
        }

        public void notifyRequestActiveAsync(final android.os.IBinder token) {
            java.lang.Integer lastStatus = this.mLastNotifiedStatus.get(token);
            if (lastStatus != null && (lastStatus.intValue() == 0 || lastStatus.intValue() == 2)) {
                return;
            }
            this.mLastNotifiedStatus.put(token, 0);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$ProcessRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$notifyRequestActiveAsync$1(token);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyRequestActiveAsync$1(android.os.IBinder token) {
            try {
                this.mCallback.onRequestActive(token);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.devicestate.DeviceStateManagerService.TAG, "Failed to notify process " + this.mPid + " that request state changed.", ex);
            }
        }

        public void notifyRequestCanceledAsync(final android.os.IBinder token) {
            java.lang.Integer lastStatus = this.mLastNotifiedStatus.get(token);
            if (lastStatus == null || lastStatus.intValue() != 2) {
                this.mLastNotifiedStatus.put(token, 2);
                this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.devicestate.DeviceStateManagerService$ProcessRecord$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$notifyRequestCanceledAsync$2(token);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyRequestCanceledAsync$2(android.os.IBinder token) {
            try {
                this.mCallback.onRequestCanceled(token);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.devicestate.DeviceStateManagerService.TAG, "Failed to notify process " + this.mPid + " that request state changed.", ex);
            }
        }
    }

    private final class BinderService extends android.hardware.devicestate.IDeviceStateManager.Stub {
        private BinderService() {
        }

        public android.hardware.devicestate.DeviceStateInfo getDeviceStateInfo() {
            android.hardware.devicestate.DeviceStateInfo deviceStateInfoLocked;
            synchronized (com.android.server.devicestate.DeviceStateManagerService.this.mLock) {
                deviceStateInfoLocked = com.android.server.devicestate.DeviceStateManagerService.this.getDeviceStateInfoLocked();
            }
            return deviceStateInfoLocked;
        }

        public void registerCallback(android.hardware.devicestate.IDeviceStateManagerCallback callback) {
            if (callback == null) {
                throw new java.lang.IllegalArgumentException("Device state callback must not be null.");
            }
            int callingPid = android.os.Binder.getCallingPid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.devicestate.DeviceStateManagerService.this.registerProcess(callingPid, callback);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void requestState(android.os.IBinder token, int state, int flags) {
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.devicestate.DeviceStateManagerService.this.assertCanRequestDeviceState(callingPid, callingUid, state);
            if (token == null) {
                throw new java.lang.IllegalArgumentException("Request token must not be null.");
            }
            boolean hasControlStatePermission = com.android.server.devicestate.DeviceStateManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE") == 0;
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.devicestate.DeviceStateManagerService.this.requestStateInternal(state, flags, callingPid, callingUid, token, hasControlStatePermission);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }

        public void cancelStateRequest() {
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.devicestate.DeviceStateManagerService.this.assertCanControlDeviceState(callingPid, callingUid);
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.devicestate.DeviceStateManagerService.this.cancelStateRequestInternal(callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }

        public void requestBaseStateOverride(android.os.IBinder token, int state, int flags) {
            int callingPid = android.os.Binder.getCallingPid();
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.devicestate.DeviceStateManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to control base state of device.");
            if (token == null) {
                throw new java.lang.IllegalArgumentException("Request token must not be null.");
            }
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.devicestate.DeviceStateManagerService.this.requestBaseStateOverrideInternal(state, flags, callingPid, callingUid, token);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }

        public void cancelBaseStateOverride() {
            int callingPid = android.os.Binder.getCallingPid();
            com.android.server.devicestate.DeviceStateManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DEVICE_STATE", "Permission required to control base state of device.");
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.devicestate.DeviceStateManagerService.this.cancelBaseStateOverrideInternal(callingPid);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }

        public void onStateRequestOverlayDismissed(boolean shouldCancelRequest) {
            onStateRequestOverlayDismissed_enforcePermission();
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.devicestate.DeviceStateManagerService.this.onStateRequestOverlayDismissedInternal(shouldCancelRequest);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver result) {
            new com.android.server.devicestate.DeviceStateManagerShellCommand(com.android.server.devicestate.DeviceStateManagerService.this).exec(this, in, out, err, args, callback, result);
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.devicestate.DeviceStateManagerService.this.getContext(), com.android.server.devicestate.DeviceStateManagerService.TAG, pw)) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.devicestate.DeviceStateManagerService.this.dumpInternal(pw);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }
    }

    void notifyKeyguardShowOrSleepUnLocked(boolean show) {
        this.mDeviceStatePolicy.getDeviceStateProvider().notifyKeyguardShowOrSleep(show);
    }

    private final class LocalService extends android.hardware.devicestate.DeviceStateManagerInternal {
        private LocalService() {
        }

        public int[] getSupportedStateIdentifiers() {
            int[] supportedStateIdentifiersLocked;
            synchronized (com.android.server.devicestate.DeviceStateManagerService.this.mLock) {
                supportedStateIdentifiersLocked = com.android.server.devicestate.DeviceStateManagerService.this.getSupportedStateIdentifiersLocked();
            }
            return supportedStateIdentifiersLocked;
        }

        public void notifyKeyguardShowOrSleep(boolean show) {
            com.android.server.devicestate.DeviceStateManagerService.this.notifyKeyguardShowOrSleepUnLocked(show);
        }

        public void enableDeviceStateAfterBoot(boolean enabled) {
            synchronized (com.android.server.devicestate.DeviceStateManagerService.this.mLock) {
                com.android.server.devicestate.DeviceStateManagerService.this.mDeviceStatesEnabled = enabled;
                com.android.server.devicestate.DeviceStateManagerService.this.mDeviceStateManagerServiceExt.enableDeviceStateAfterBoot(enabled);
            }
        }

        public void setSecondaryDisplayKeepTurnOn(boolean keepOn) {
            if (com.android.server.devicestate.DeviceStateManagerService.this.mBaseState.isEmpty()) {
                return;
            }
            if (keepOn && com.android.server.devicestate.DeviceStateManagerService.this.isDeviceCurrentOpened()) {
                if (com.android.server.devicestate.DeviceStateManagerService.this.getOverrideState().isPresent() && com.android.server.devicestate.DeviceStateManagerService.this.getOverrideState().get().getIdentifier() == 99) {
                    return;
                }
                android.os.IBinder token = new android.os.Binder();
                com.android.server.devicestate.DeviceStateManagerService.this.requestStateInternal(99, 0, android.os.Process.myPid(), android.os.Process.myUid(), token, true);
            }
            if (!keepOn && com.android.server.devicestate.DeviceStateManagerService.this.getOverrideState().isPresent() && com.android.server.devicestate.DeviceStateManagerService.this.getOverrideState().get().getIdentifier() == 99) {
                com.android.server.devicestate.DeviceStateManagerService.this.cancelStateRequestInternal(android.os.Process.myPid());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceCurrentOpened() {
        return (this.mBaseState.get().getIdentifier() == 0 || this.mBaseState.get().getIdentifier() == 1 || this.mCommittedState.isEmpty() || this.mCommittedState.get().getIdentifier() == 0 || this.mCommittedState.get().getIdentifier() == 1) ? false : true;
    }

    private com.android.server.wm.WindowManagerInternal getWindowManagerInternal() {
        if (this.mWindowManagerInternal == null) {
            this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        }
        return this.mWindowManagerInternal;
    }

    private boolean shouldSimultaneousDisplay() {
        if (this.mWindowManagerInternal == null) {
            getWindowManagerInternal();
        }
        if ((this.mBaseState.get().getIdentifier() == 2 || this.mBaseState.get().getIdentifier() == 3) && this.mWindowManagerInternal != null && this.mWindowManagerInternal.keepSimultaneousDisplay()) {
            if (getOverrideState().isEmpty() && this.mBaseState.isPresent()) {
                android.os.IBinder token = new android.os.Binder();
                requestStateInternal(99, 1, android.os.Process.myPid(), android.os.Process.myUid(), token, true);
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean shouldCancelOverrideRequest() {
        if (this.mBaseState.get().getIdentifier() == 0 && getOverrideState().isPresent() && getOverrideState().get().getIdentifier() == 101) {
            return false;
        }
        if (this.mBaseState.get().hasProperty(4)) {
            return true;
        }
        return this.mBaseState.get().getIdentifier() == 3 && getOverrideState().isPresent() && getOverrideState().get().getIdentifier() == 101;
    }

    public com.android.server.devicestate.IDeviceStateManagerServiceWrapper getWrapper() {
        return this.mWrapper;
    }

    private class DeviceStateManagerServiceWrapper implements com.android.server.devicestate.IDeviceStateManagerServiceWrapper {
        private DeviceStateManagerServiceWrapper() {
        }

        @Override // com.android.server.devicestate.IDeviceStateManagerServiceWrapper
        public java.util.Optional<android.hardware.devicestate.DeviceState> getCommittedState() {
            return com.android.server.devicestate.DeviceStateManagerService.this.mCommittedState;
        }

        @Override // com.android.server.devicestate.IDeviceStateManagerServiceWrapper
        public java.util.Optional<android.hardware.devicestate.DeviceState> getBaseState() {
            return com.android.server.devicestate.DeviceStateManagerService.this.mBaseState;
        }

        @Override // com.android.server.devicestate.IDeviceStateManagerServiceWrapper
        public java.util.Optional<android.hardware.devicestate.DeviceState> getStateLocked(int identifier) {
            return com.android.server.devicestate.DeviceStateManagerService.this.getStateLocked(identifier);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldCancelOverrideRequestWhenRequesterNotOnTop() {
        if (this.mActiveOverride.isEmpty()) {
            return false;
        }
        int identifier = this.mActiveOverride.get().getRequestedStateIdentifier();
        android.hardware.devicestate.DeviceState deviceState = this.mDeviceStates.get(identifier);
        return deviceState.hasProperty(5);
    }

    private class OverrideRequestScreenObserver implements com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver {
        private OverrideRequestScreenObserver() {
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
        public void onAwakeStateChanged(boolean isAwake) {
            synchronized (com.android.server.devicestate.DeviceStateManagerService.this.mLock) {
                if (!isAwake) {
                    if (com.android.server.devicestate.DeviceStateManagerService.this.shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                        com.android.server.devicestate.DeviceStateManagerService.this.mOverrideRequestController.cancelRequest((com.android.server.devicestate.OverrideRequest) com.android.server.devicestate.DeviceStateManagerService.this.mActiveOverride.get());
                    }
                }
            }
        }

        @Override // com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver
        public void onKeyguardStateChanged(boolean isShowing) {
            synchronized (com.android.server.devicestate.DeviceStateManagerService.this.mLock) {
                if (isShowing) {
                    if (com.android.server.devicestate.DeviceStateManagerService.this.shouldCancelOverrideRequestWhenRequesterNotOnTop()) {
                        com.android.server.devicestate.DeviceStateManagerService.this.mOverrideRequestController.cancelRequest((com.android.server.devicestate.OverrideRequest) com.android.server.devicestate.DeviceStateManagerService.this.mActiveOverride.get());
                    }
                }
            }
        }
    }
}
