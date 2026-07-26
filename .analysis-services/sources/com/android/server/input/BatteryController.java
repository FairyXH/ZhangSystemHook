package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class BatteryController {
    static final long POLLING_PERIOD_MILLIS = 10000;
    static final long USI_BATTERY_VALIDITY_DURATION_MILLIS = 3600000;
    private com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener mBluetoothBatteryListener;
    private final com.android.server.input.BatteryController.BluetoothBatteryManager mBluetoothBatteryManager;
    private final android.content.Context mContext;
    private final android.util.ArrayMap<java.lang.Integer, com.android.server.input.BatteryController.DeviceMonitor> mDeviceMonitors;
    private final android.os.Handler mHandler;
    private final android.hardware.input.InputManager.InputDeviceListener mInputDeviceListener;
    private boolean mIsInteractive;
    private boolean mIsPolling;
    private final android.util.ArrayMap<java.lang.Integer, com.android.server.input.BatteryController.ListenerRecord> mListenerRecords;
    private final java.lang.Object mLock;
    private final com.android.server.input.NativeInputManagerService mNative;
    private final com.android.server.input.UEventManager mUEventManager;
    private static final java.lang.String TAG = com.android.server.input.BatteryController.class.getSimpleName();
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    interface BluetoothBatteryManager {

        public interface BluetoothBatteryListener {
            void onBluetoothBatteryChanged(long j, java.lang.String str, int i);
        }

        void addBatteryListener(com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener bluetoothBatteryListener);

        void addMetadataListener(java.lang.String str, android.bluetooth.BluetoothAdapter.OnMetadataChangedListener onMetadataChangedListener);

        int getBatteryLevel(java.lang.String str);

        byte[] getMetadata(java.lang.String str, int i);

        void removeBatteryListener(com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener bluetoothBatteryListener);

        void removeMetadataListener(java.lang.String str, android.bluetooth.BluetoothAdapter.OnMetadataChangedListener onMetadataChangedListener);
    }

    BatteryController(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService, android.os.Looper looper, com.android.server.input.UEventManager uEventManager) {
        this(context, nativeService, looper, uEventManager, new com.android.server.input.BatteryController.LocalBluetoothBatteryManager(context, looper));
    }

    BatteryController(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService, android.os.Looper looper, com.android.server.input.UEventManager uEventManager, com.android.server.input.BatteryController.BluetoothBatteryManager bbm) {
        this.mLock = new java.lang.Object();
        this.mListenerRecords = new android.util.ArrayMap<>();
        this.mDeviceMonitors = new android.util.ArrayMap<>();
        this.mIsPolling = false;
        this.mIsInteractive = true;
        this.mInputDeviceListener = new android.hardware.input.InputManager.InputDeviceListener() { // from class: com.android.server.input.BatteryController.1
            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceAdded(int deviceId) {
                synchronized (com.android.server.input.BatteryController.this.mLock) {
                    if (com.android.server.input.BatteryController.this.isUsiDevice(deviceId) && !com.android.server.input.BatteryController.this.mDeviceMonitors.containsKey(java.lang.Integer.valueOf(deviceId))) {
                        com.android.server.input.BatteryController.this.mDeviceMonitors.put(java.lang.Integer.valueOf(deviceId), com.android.server.input.BatteryController.this.new UsiDeviceMonitor(deviceId));
                    }
                }
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceRemoved(int deviceId) {
            }

            @Override // android.hardware.input.InputManager.InputDeviceListener
            public void onInputDeviceChanged(int deviceId) {
                synchronized (com.android.server.input.BatteryController.this.mLock) {
                    com.android.server.input.BatteryController.DeviceMonitor monitor = (com.android.server.input.BatteryController.DeviceMonitor) com.android.server.input.BatteryController.this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId));
                    if (monitor == null) {
                        return;
                    }
                    long eventTime = android.os.SystemClock.uptimeMillis();
                    monitor.onConfiguration(eventTime);
                }
            }
        };
        this.mContext = context;
        this.mNative = nativeService;
        this.mHandler = new android.os.Handler(looper);
        this.mUEventManager = uEventManager;
        this.mBluetoothBatteryManager = bbm;
    }

    public void systemRunning() {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        inputManager.registerInputDeviceListener(this.mInputDeviceListener, this.mHandler);
        for (int deviceId : inputManager.getInputDeviceIds()) {
            this.mInputDeviceListener.onInputDeviceAdded(deviceId);
        }
    }

    public void registerBatteryListener(int deviceId, android.hardware.input.IInputDeviceBatteryListener listener, int pid) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.ListenerRecord listenerRecord = this.mListenerRecords.get(java.lang.Integer.valueOf(pid));
            if (listenerRecord == null) {
                listenerRecord = new com.android.server.input.BatteryController.ListenerRecord(pid, listener);
                try {
                    listener.asBinder().linkToDeath(listenerRecord.mDeathRecipient, 0);
                    this.mListenerRecords.put(java.lang.Integer.valueOf(pid), listenerRecord);
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Battery listener added for pid " + pid);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.i(TAG, "Client died before battery listener could be registered.");
                    return;
                }
            }
            if (listenerRecord.mListener.asBinder() != listener.asBinder()) {
                throw new java.lang.SecurityException("Cannot register a new battery listener when there is already another registered listener for pid " + pid);
            }
            if (!listenerRecord.mMonitoredDevices.add(java.lang.Integer.valueOf(deviceId))) {
                throw new java.lang.IllegalArgumentException("The battery listener for pid " + pid + " is already monitoring deviceId " + deviceId);
            }
            com.android.server.input.BatteryController.DeviceMonitor monitor = this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId));
            if (monitor == null) {
                monitor = new com.android.server.input.BatteryController.DeviceMonitor(deviceId);
                this.mDeviceMonitors.put(java.lang.Integer.valueOf(deviceId), monitor);
                updateBluetoothBatteryMonitoring();
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "Battery listener for pid " + pid + " is monitoring deviceId " + deviceId);
            }
            updatePollingLocked(true);
            notifyBatteryListener(listenerRecord, monitor.getBatteryStateForReporting());
        }
    }

    private static void notifyBatteryListener(com.android.server.input.BatteryController.ListenerRecord listenerRecord, com.android.server.input.BatteryController.State state) {
        try {
            listenerRecord.mListener.onBatteryStateChanged(state);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to notify listener", e);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Notified battery listener from pid " + listenerRecord.mPid + " of state of deviceId " + state.deviceId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAllListenersForDevice(final com.android.server.input.BatteryController.State state) {
        synchronized (this.mLock) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Notifying all listeners of battery state: " + state);
            }
            this.mListenerRecords.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda6
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    com.android.server.input.BatteryController.lambda$notifyAllListenersForDevice$0(state, (java.lang.Integer) obj, (com.android.server.input.BatteryController.ListenerRecord) obj2);
                }
            });
        }
    }

    static /* synthetic */ void lambda$notifyAllListenersForDevice$0(com.android.server.input.BatteryController.State state, java.lang.Integer pid, com.android.server.input.BatteryController.ListenerRecord listenerRecord) {
        if (listenerRecord.mMonitoredDevices.contains(java.lang.Integer.valueOf(state.deviceId))) {
            notifyBatteryListener(listenerRecord, state);
        }
    }

    private void updatePollingLocked(boolean delayStart) {
        if (!this.mIsInteractive || !anyOf(this.mDeviceMonitors, new java.util.function.Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.input.BatteryController.DeviceMonitor) obj).requiresPolling();
            }
        })) {
            this.mIsPolling = false;
            this.mHandler.removeCallbacks(new com.android.server.input.BatteryController$$ExternalSyntheticLambda8(this));
        } else {
            if (this.mIsPolling) {
                return;
            }
            this.mIsPolling = true;
            this.mHandler.postDelayed(new com.android.server.input.BatteryController$$ExternalSyntheticLambda8(this), delayStart ? 10000L : 0L);
        }
    }

    private <R> R processInputDevice(int deviceId, R defaultValue, java.util.function.Function<android.view.InputDevice, R> func) {
        android.view.InputDevice device = ((android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class))).getInputDevice(deviceId);
        return device == null ? defaultValue : func.apply(device);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getInputDeviceName(int deviceId) {
        return (java.lang.String) processInputDevice(deviceId, "<none>", new java.util.function.Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.view.InputDevice) obj).getName();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasBattery(int deviceId) {
        return ((java.lang.Boolean) processInputDevice(deviceId, false, new java.util.function.Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Boolean.valueOf(((android.view.InputDevice) obj).hasBattery());
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUsiDevice(int deviceId) {
        return ((java.lang.Boolean) processInputDevice(deviceId, false, new java.util.function.Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Boolean.valueOf(((android.view.InputDevice) obj).getHostUsiVersion() != null);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.bluetooth.BluetoothDevice getBluetoothDevice(int inputDeviceId) {
        return getBluetoothDevice(this.mContext, (java.lang.String) processInputDevice(inputDeviceId, null, new java.util.function.Function() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.view.InputDevice) obj).getBluetoothAddress();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.bluetooth.BluetoothDevice getBluetoothDevice(android.content.Context context, java.lang.String address) {
        if (address == null) {
            return null;
        }
        android.bluetooth.BluetoothAdapter adapter = ((android.bluetooth.BluetoothManager) java.util.Objects.requireNonNull((android.bluetooth.BluetoothManager) context.getSystemService(android.bluetooth.BluetoothManager.class))).getAdapter();
        return adapter.getRemoteDevice(address);
    }

    private com.android.server.input.BatteryController.DeviceMonitor getDeviceMonitorOrThrowLocked(int deviceId) {
        return (com.android.server.input.BatteryController.DeviceMonitor) java.util.Objects.requireNonNull(this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId)), "Maps are out of sync: Cannot find device state for deviceId " + deviceId);
    }

    public void unregisterBatteryListener(int deviceId, android.hardware.input.IInputDeviceBatteryListener listener, int pid) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.ListenerRecord listenerRecord = this.mListenerRecords.get(java.lang.Integer.valueOf(pid));
            if (listenerRecord == null) {
                throw new java.lang.IllegalArgumentException("Cannot unregister battery callback: No listener registered for pid " + pid);
            }
            if (listenerRecord.mListener.asBinder() != listener.asBinder()) {
                throw new java.lang.IllegalArgumentException("Cannot unregister battery callback: The listener is not the one that is registered for pid " + pid);
            }
            if (!listenerRecord.mMonitoredDevices.contains(java.lang.Integer.valueOf(deviceId))) {
                throw new java.lang.IllegalArgumentException("Cannot unregister battery callback: The device is not being monitored for deviceId " + deviceId);
            }
            unregisterRecordLocked(listenerRecord, deviceId);
        }
    }

    private void unregisterRecordLocked(com.android.server.input.BatteryController.ListenerRecord listenerRecord, int deviceId) {
        int pid = listenerRecord.mPid;
        if (!listenerRecord.mMonitoredDevices.remove(java.lang.Integer.valueOf(deviceId))) {
            throw new java.lang.IllegalStateException("Cannot unregister battery callback: The deviceId " + deviceId + " is not being monitored by pid " + pid);
        }
        if (!hasRegisteredListenerForDeviceLocked(deviceId)) {
            com.android.server.input.BatteryController.DeviceMonitor monitor = getDeviceMonitorOrThrowLocked(deviceId);
            if (!monitor.isPersistent()) {
                monitor.onMonitorDestroy();
                this.mDeviceMonitors.remove(java.lang.Integer.valueOf(deviceId));
            }
        }
        if (listenerRecord.mMonitoredDevices.isEmpty()) {
            listenerRecord.mListener.asBinder().unlinkToDeath(listenerRecord.mDeathRecipient, 0);
            this.mListenerRecords.remove(java.lang.Integer.valueOf(pid));
            if (DEBUG) {
                android.util.Slog.d(TAG, "Battery listener removed for pid " + pid);
            }
        }
        updatePollingLocked(false);
    }

    private boolean hasRegisteredListenerForDeviceLocked(int deviceId) {
        for (int i = 0; i < this.mListenerRecords.size(); i++) {
            if (this.mListenerRecords.valueAt(i).mMonitoredDevices.contains(java.lang.Integer.valueOf(deviceId))) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleListeningProcessDied(int pid) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.ListenerRecord listenerRecord = this.mListenerRecords.get(java.lang.Integer.valueOf(pid));
            if (listenerRecord == null) {
                return;
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "Removing battery listener for pid " + pid + " because the process died");
            }
            java.util.Iterator<java.lang.Integer> it = listenerRecord.mMonitoredDevices.iterator();
            while (it.hasNext()) {
                int deviceId = it.next().intValue();
                unregisterRecordLocked(listenerRecord, deviceId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUEventNotification(int deviceId, long eventTime) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.DeviceMonitor monitor = this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId));
            if (monitor == null) {
                return;
            }
            monitor.onUEvent(eventTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePollEvent() {
        synchronized (this.mLock) {
            if (this.mIsPolling) {
                final long eventTime = android.os.SystemClock.uptimeMillis();
                this.mDeviceMonitors.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda9
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.input.BatteryController.DeviceMonitor) obj2).onPoll(eventTime);
                    }
                });
                this.mHandler.postDelayed(new com.android.server.input.BatteryController$$ExternalSyntheticLambda8(this), 10000L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMonitorTimeout(int deviceId) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.DeviceMonitor monitor = this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId));
            if (monitor == null) {
                return;
            }
            long updateTime = android.os.SystemClock.uptimeMillis();
            monitor.onTimeout(updateTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBluetoothBatteryLevelChange(long eventTime, final java.lang.String address, int batteryLevel) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.DeviceMonitor monitor = (com.android.server.input.BatteryController.DeviceMonitor) findIf(this.mDeviceMonitors, new java.util.function.Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.input.BatteryController.lambda$handleBluetoothBatteryLevelChange$3(address, (com.android.server.input.BatteryController.DeviceMonitor) obj);
                }
            });
            if (monitor != null) {
                monitor.onBluetoothBatteryChanged(eventTime, batteryLevel);
            }
        }
    }

    static /* synthetic */ boolean lambda$handleBluetoothBatteryLevelChange$3(java.lang.String address, com.android.server.input.BatteryController.DeviceMonitor m) {
        return m.mBluetoothDevice != null && address.equals(m.mBluetoothDevice.getAddress());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBluetoothMetadataChange(final android.bluetooth.BluetoothDevice device, int key, byte[] value) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.DeviceMonitor monitor = (com.android.server.input.BatteryController.DeviceMonitor) findIf(this.mDeviceMonitors, new java.util.function.Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return device.equals(((com.android.server.input.BatteryController.DeviceMonitor) obj).mBluetoothDevice);
                }
            });
            if (monitor != null) {
                long eventTime = android.os.SystemClock.uptimeMillis();
                monitor.onBluetoothMetadataChanged(eventTime, key, value);
            }
        }
    }

    public android.hardware.input.IInputDeviceBatteryState getBatteryState(int deviceId) {
        synchronized (this.mLock) {
            long updateTime = android.os.SystemClock.uptimeMillis();
            com.android.server.input.BatteryController.DeviceMonitor monitor = this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId));
            if (monitor == null) {
                return queryBatteryStateFromNative(deviceId, updateTime, hasBattery(deviceId));
            }
            monitor.onPoll(updateTime);
            return monitor.getBatteryStateForReporting();
        }
    }

    public void onInteractiveChanged(boolean interactive) {
        synchronized (this.mLock) {
            this.mIsInteractive = interactive;
            updatePollingLocked(false);
        }
    }

    public void notifyStylusGestureStarted(int deviceId, long eventTime) {
        synchronized (this.mLock) {
            com.android.server.input.BatteryController.DeviceMonitor monitor = this.mDeviceMonitors.get(java.lang.Integer.valueOf(deviceId));
            if (monitor == null) {
                return;
            }
            monitor.onStylusGestureStarted(eventTime);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
        synchronized (this.mLock) {
            ipw.println(TAG + ":");
            ipw.increaseIndent();
            ipw.println("State: Polling = " + this.mIsPolling + ", Interactive = " + this.mIsInteractive);
            ipw.println("Listeners: " + this.mListenerRecords.size() + " battery listeners");
            ipw.increaseIndent();
            for (int i = 0; i < this.mListenerRecords.size(); i++) {
                ipw.println(i + ": " + this.mListenerRecords.valueAt(i));
            }
            ipw.decreaseIndent();
            ipw.println("Device Monitors: " + this.mDeviceMonitors.size() + " monitors");
            ipw.increaseIndent();
            for (int i2 = 0; i2 < this.mDeviceMonitors.size(); i2++) {
                ipw.println(i2 + ": " + this.mDeviceMonitors.valueAt(i2));
            }
            ipw.decreaseIndent();
            ipw.decreaseIndent();
        }
    }

    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ListenerRecord {
        public final android.os.IBinder.DeathRecipient mDeathRecipient;
        public final android.hardware.input.IInputDeviceBatteryListener mListener;
        public final java.util.Set<java.lang.Integer> mMonitoredDevices = new android.util.ArraySet();
        public final int mPid;

        ListenerRecord(final int pid, android.hardware.input.IInputDeviceBatteryListener listener) {
            this.mPid = pid;
            this.mListener = listener;
            this.mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.input.BatteryController$ListenerRecord$$ExternalSyntheticLambda0
                @Override // android.os.IBinder.DeathRecipient
                public final void binderDied() {
                    this.f$0.lambda$new$0(pid);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(int pid) {
            com.android.server.input.BatteryController.this.handleListeningProcessDied(pid);
        }

        public java.lang.String toString() {
            return "pid=" + this.mPid + ", monitored devices=" + java.util.Arrays.toString(this.mMonitoredDevices.toArray());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.input.BatteryController.State queryBatteryStateFromNative(int deviceId, long updateTime, boolean isPresent) {
        return new com.android.server.input.BatteryController.State(deviceId, updateTime, isPresent, isPresent ? this.mNative.getBatteryStatus(deviceId) : 1, isPresent ? this.mNative.getBatteryCapacity(deviceId) / 100.0f : Float.NaN);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBluetoothBatteryMonitoring() {
        synchronized (this.mLock) {
            if (anyOf(this.mDeviceMonitors, new java.util.function.Predicate() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.input.BatteryController.lambda$updateBluetoothBatteryMonitoring$5((com.android.server.input.BatteryController.DeviceMonitor) obj);
                }
            })) {
                if (this.mBluetoothBatteryListener == null) {
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Registering bluetooth battery listener");
                    }
                    this.mBluetoothBatteryListener = new com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener() { // from class: com.android.server.input.BatteryController$$ExternalSyntheticLambda11
                        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener
                        public final void onBluetoothBatteryChanged(long j, java.lang.String str, int i) {
                            this.f$0.handleBluetoothBatteryLevelChange(j, str, i);
                        }
                    };
                    this.mBluetoothBatteryManager.addBatteryListener(this.mBluetoothBatteryListener);
                }
            } else if (this.mBluetoothBatteryListener != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Unregistering bluetooth battery listener");
                }
                this.mBluetoothBatteryManager.removeBatteryListener(this.mBluetoothBatteryListener);
                this.mBluetoothBatteryListener = null;
            }
        }
    }

    static /* synthetic */ boolean lambda$updateBluetoothBatteryMonitoring$5(com.android.server.input.BatteryController.DeviceMonitor m) {
        return m.mBluetoothDevice != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DeviceMonitor {
        private android.bluetooth.BluetoothDevice mBluetoothDevice;
        private android.bluetooth.BluetoothAdapter.OnMetadataChangedListener mBluetoothMetadataListener;
        protected final com.android.server.input.BatteryController.State mState;
        private com.android.server.input.BatteryController.UEventBatteryListener mUEventBatteryListener;
        protected boolean mHasBattery = false;
        long mBluetoothEventTime = 0;
        int mBluetoothBatteryLevel = -1;
        int mBluetoothMetadataBatteryLevel = -1;
        int mBluetoothMetadataBatteryStatus = 1;

        DeviceMonitor(int deviceId) {
            this.mState = new com.android.server.input.BatteryController.State(deviceId);
            long eventTime = android.os.SystemClock.uptimeMillis();
            configureDeviceMonitor(eventTime);
        }

        protected void processChangesAndNotify(long eventTime, java.util.function.Consumer<java.lang.Long> changes) {
            com.android.server.input.BatteryController.State oldState = getBatteryStateForReporting();
            changes.accept(java.lang.Long.valueOf(eventTime));
            com.android.server.input.BatteryController.State newState = getBatteryStateForReporting();
            if (!oldState.equalsIgnoringUpdateTime(newState)) {
                com.android.server.input.BatteryController.this.notifyAllListenersForDevice(newState);
            }
        }

        public void onConfiguration(long eventTime) {
            processChangesAndNotify(eventTime, new java.util.function.Consumer() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.configureDeviceMonitor(((java.lang.Long) obj).longValue());
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void configureDeviceMonitor(long eventTime) {
            int deviceId = this.mState.deviceId;
            if (this.mHasBattery != com.android.server.input.BatteryController.this.hasBattery(this.mState.deviceId)) {
                this.mHasBattery = !this.mHasBattery;
                if (this.mHasBattery) {
                    startNativeMonitoring();
                } else {
                    stopNativeMonitoring();
                }
                updateBatteryStateFromNative(eventTime);
            }
            android.bluetooth.BluetoothDevice bluetoothDevice = com.android.server.input.BatteryController.this.getBluetoothDevice(deviceId);
            if (!java.util.Objects.equals(this.mBluetoothDevice, bluetoothDevice)) {
                if (com.android.server.input.BatteryController.DEBUG) {
                    android.util.Slog.d(com.android.server.input.BatteryController.TAG, "Bluetooth device is now " + (bluetoothDevice != null ? "" : "not") + " present for deviceId " + deviceId);
                }
                this.mBluetoothBatteryLevel = -1;
                stopBluetoothMetadataMonitoring();
                this.mBluetoothDevice = bluetoothDevice;
                com.android.server.input.BatteryController.this.updateBluetoothBatteryMonitoring();
                if (this.mBluetoothDevice != null) {
                    this.mBluetoothBatteryLevel = com.android.server.input.BatteryController.this.mBluetoothBatteryManager.getBatteryLevel(this.mBluetoothDevice.getAddress());
                    startBluetoothMetadataMonitoring(eventTime);
                }
            }
        }

        private void startNativeMonitoring() {
            java.lang.String batteryPath = com.android.server.input.BatteryController.this.mNative.getBatteryDevicePath(this.mState.deviceId);
            if (batteryPath == null) {
                return;
            }
            final int deviceId = this.mState.deviceId;
            this.mUEventBatteryListener = new com.android.server.input.BatteryController.UEventBatteryListener() { // from class: com.android.server.input.BatteryController.DeviceMonitor.1
                @Override // com.android.server.input.BatteryController.UEventBatteryListener
                public void onBatteryUEvent(long eventTime) {
                    com.android.server.input.BatteryController.this.handleUEventNotification(deviceId, eventTime);
                }
            };
            com.android.server.input.BatteryController.this.mUEventManager.addListener(this.mUEventBatteryListener, "DEVPATH=" + formatDevPath(batteryPath));
        }

        private java.lang.String formatDevPath(java.lang.String path) {
            return path.startsWith("/sys") ? path.substring(4) : path;
        }

        private void stopNativeMonitoring() {
            if (this.mUEventBatteryListener != null) {
                com.android.server.input.BatteryController.this.mUEventManager.removeListener(this.mUEventBatteryListener);
                this.mUEventBatteryListener = null;
            }
        }

        private void startBluetoothMetadataMonitoring(long eventTime) {
            java.util.Objects.requireNonNull(this.mBluetoothDevice);
            final com.android.server.input.BatteryController batteryController = com.android.server.input.BatteryController.this;
            this.mBluetoothMetadataListener = new android.bluetooth.BluetoothAdapter.OnMetadataChangedListener() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda1
                public final void onMetadataChanged(android.bluetooth.BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
                    batteryController.handleBluetoothMetadataChange(bluetoothDevice, i, bArr);
                }
            };
            com.android.server.input.BatteryController.this.mBluetoothBatteryManager.addMetadataListener(this.mBluetoothDevice.getAddress(), this.mBluetoothMetadataListener);
            updateBluetoothMetadataState(eventTime, 18, com.android.server.input.BatteryController.this.mBluetoothBatteryManager.getMetadata(this.mBluetoothDevice.getAddress(), 18));
            updateBluetoothMetadataState(eventTime, 19, com.android.server.input.BatteryController.this.mBluetoothBatteryManager.getMetadata(this.mBluetoothDevice.getAddress(), 19));
        }

        private void stopBluetoothMetadataMonitoring() {
            if (this.mBluetoothMetadataListener == null) {
                return;
            }
            java.util.Objects.requireNonNull(this.mBluetoothDevice);
            com.android.server.input.BatteryController.this.mBluetoothBatteryManager.removeMetadataListener(this.mBluetoothDevice.getAddress(), this.mBluetoothMetadataListener);
            this.mBluetoothMetadataListener = null;
            this.mBluetoothMetadataBatteryLevel = -1;
            this.mBluetoothMetadataBatteryStatus = 1;
        }

        public void onMonitorDestroy() {
            stopNativeMonitoring();
            stopBluetoothMetadataMonitoring();
            this.mBluetoothDevice = null;
            com.android.server.input.BatteryController.this.updateBluetoothBatteryMonitoring();
        }

        protected void updateBatteryStateFromNative(long eventTime) {
            this.mState.updateIfChanged(com.android.server.input.BatteryController.this.queryBatteryStateFromNative(this.mState.deviceId, eventTime, this.mHasBattery));
        }

        public void onPoll(long eventTime) {
            processChangesAndNotify(eventTime, new com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda0(this));
        }

        public void onUEvent(long eventTime) {
            processChangesAndNotify(eventTime, new com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda0(this));
        }

        public void onBluetoothBatteryChanged(long eventTime, final int bluetoothBatteryLevel) {
            processChangesAndNotify(eventTime, new java.util.function.Consumer() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onBluetoothBatteryChanged$0(bluetoothBatteryLevel, (java.lang.Long) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBluetoothBatteryChanged$0(int bluetoothBatteryLevel, java.lang.Long time) {
            this.mBluetoothBatteryLevel = bluetoothBatteryLevel;
            this.mBluetoothEventTime = time.longValue();
        }

        public void onBluetoothMetadataChanged(long eventTime, final int key, final byte[] value) {
            processChangesAndNotify(eventTime, new java.util.function.Consumer() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onBluetoothMetadataChanged$1(key, value, (java.lang.Long) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBluetoothMetadataChanged$1(int key, byte[] value, java.lang.Long time) {
            updateBluetoothMetadataState(time.longValue(), key, value);
        }

        private void updateBluetoothMetadataState(long eventTime, int key, byte[] value) {
            int i;
            switch (key) {
                case 18:
                    this.mBluetoothEventTime = eventTime;
                    this.mBluetoothMetadataBatteryLevel = -1;
                    if (value != null) {
                        try {
                            this.mBluetoothMetadataBatteryLevel = java.lang.Integer.parseInt(new java.lang.String(value));
                        } catch (java.lang.NumberFormatException e) {
                            android.util.Slog.wtf(com.android.server.input.BatteryController.TAG, "Failed to parse bluetooth METADATA_MAIN_BATTERY with value '" + new java.lang.String(value) + "' for device " + this.mBluetoothDevice);
                            return;
                        }
                    }
                    break;
                case 19:
                    this.mBluetoothEventTime = eventTime;
                    if (value != null) {
                        if (java.lang.Boolean.parseBoolean(new java.lang.String(value))) {
                            i = 2;
                        } else {
                            i = 3;
                        }
                        this.mBluetoothMetadataBatteryStatus = i;
                    } else {
                        this.mBluetoothMetadataBatteryStatus = 1;
                    }
                    break;
            }
        }

        public boolean requiresPolling() {
            return true;
        }

        public boolean isPersistent() {
            return false;
        }

        public void onTimeout(long eventTime) {
        }

        public void onStylusGestureStarted(long eventTime) {
        }

        public com.android.server.input.BatteryController.State getBatteryStateForReporting() {
            return (com.android.server.input.BatteryController.State) java.util.Objects.requireNonNullElseGet(resolveBluetoothBatteryState(), new java.util.function.Supplier() { // from class: com.android.server.input.BatteryController$DeviceMonitor$$ExternalSyntheticLambda4
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getBatteryStateForReporting$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.input.BatteryController.State lambda$getBatteryStateForReporting$2() {
            return new com.android.server.input.BatteryController.State(this.mState);
        }

        protected com.android.server.input.BatteryController.State resolveBluetoothBatteryState() {
            int level;
            if (this.mBluetoothMetadataBatteryLevel >= 0 && this.mBluetoothMetadataBatteryLevel <= 100) {
                level = this.mBluetoothMetadataBatteryLevel;
            } else {
                int level2 = this.mBluetoothBatteryLevel;
                if (level2 >= 0 && this.mBluetoothBatteryLevel <= 100) {
                    level = this.mBluetoothBatteryLevel;
                } else {
                    return null;
                }
            }
            return new com.android.server.input.BatteryController.State(this.mState.deviceId, this.mBluetoothEventTime, true, this.mBluetoothMetadataBatteryStatus, level / 100.0f);
        }

        public java.lang.String toString() {
            return "DeviceId=" + this.mState.deviceId + ", Name='" + com.android.server.input.BatteryController.this.getInputDeviceName(this.mState.deviceId) + "', NativeBattery=" + this.mState + ", UEventListener=" + (this.mUEventBatteryListener != null ? "added" : "none") + ", BluetoothState=" + resolveBluetoothBatteryState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class UsiDeviceMonitor extends com.android.server.input.BatteryController.DeviceMonitor {
        private java.lang.Runnable mValidityTimeoutCallback;

        UsiDeviceMonitor(int deviceId) {
            super(deviceId);
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onPoll(long eventTime) {
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onUEvent(long eventTime) {
            processChangesAndNotify(eventTime, new java.util.function.Consumer() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onUEvent$0((java.lang.Long) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUEvent$0(java.lang.Long time) {
            updateBatteryStateFromNative(time.longValue());
            markUsiBatteryValid();
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onStylusGestureStarted(long eventTime) {
            processChangesAndNotify(eventTime, new java.util.function.Consumer() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onStylusGestureStarted$1((java.lang.Long) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStylusGestureStarted$1(java.lang.Long time) {
            boolean wasValid = this.mValidityTimeoutCallback != null;
            if (!wasValid && this.mState.capacity == 0.0f) {
                return;
            }
            markUsiBatteryValid();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTimeout$2(java.lang.Long time) {
            markUsiBatteryInvalid();
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onTimeout(long eventTime) {
            processChangesAndNotify(eventTime, new java.util.function.Consumer() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onTimeout$2((java.lang.Long) obj);
                }
            });
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public void onConfiguration(long eventTime) {
            super.onConfiguration(eventTime);
            if (!this.mHasBattery) {
                throw new java.lang.IllegalStateException("UsiDeviceMonitor: USI devices are always expected to report a valid battery, but no battery was detected!");
            }
        }

        private void markUsiBatteryValid() {
            if (this.mValidityTimeoutCallback != null) {
                com.android.server.input.BatteryController.this.mHandler.removeCallbacks(this.mValidityTimeoutCallback);
            } else {
                final int deviceId = this.mState.deviceId;
                this.mValidityTimeoutCallback = new java.lang.Runnable() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$markUsiBatteryValid$3(deviceId);
                    }
                };
            }
            com.android.server.input.BatteryController.this.mHandler.postDelayed(this.mValidityTimeoutCallback, 3600000L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$markUsiBatteryValid$3(int deviceId) {
            com.android.server.input.BatteryController.this.handleMonitorTimeout(deviceId);
        }

        private void markUsiBatteryInvalid() {
            if (this.mValidityTimeoutCallback == null) {
                return;
            }
            com.android.server.input.BatteryController.this.mHandler.removeCallbacks(this.mValidityTimeoutCallback);
            this.mValidityTimeoutCallback = null;
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public com.android.server.input.BatteryController.State getBatteryStateForReporting() {
            return (com.android.server.input.BatteryController.State) java.util.Objects.requireNonNullElseGet(resolveBluetoothBatteryState(), new java.util.function.Supplier() { // from class: com.android.server.input.BatteryController$UsiDeviceMonitor$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$getBatteryStateForReporting$4();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.input.BatteryController.State lambda$getBatteryStateForReporting$4() {
            return this.mValidityTimeoutCallback != null ? new com.android.server.input.BatteryController.State(this.mState) : new com.android.server.input.BatteryController.State(this.mState.deviceId);
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public boolean requiresPolling() {
            return false;
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public boolean isPersistent() {
            return true;
        }

        @Override // com.android.server.input.BatteryController.DeviceMonitor
        public java.lang.String toString() {
            return super.toString() + ", UsiStateIsValid=" + (this.mValidityTimeoutCallback != null);
        }
    }

    static abstract class UEventBatteryListener extends com.android.server.input.UEventManager.UEventListener {
        public abstract void onBatteryUEvent(long j);

        UEventBatteryListener() {
        }

        @Override // com.android.server.input.UEventManager.UEventListener
        public void onUEvent(android.os.UEventObserver.UEvent event) {
            long eventTime = android.os.SystemClock.uptimeMillis();
            if (com.android.server.input.BatteryController.DEBUG) {
                android.util.Slog.d(com.android.server.input.BatteryController.TAG, "UEventListener: Received UEvent: " + event + " eventTime: " + eventTime);
            }
            if (!"CHANGE".equalsIgnoreCase(event.get("ACTION")) || !"POWER_SUPPLY".equalsIgnoreCase(event.get("SUBSYSTEM"))) {
                return;
            }
            onBatteryUEvent(eventTime);
        }
    }

    private static class LocalBluetoothBatteryManager implements com.android.server.input.BatteryController.BluetoothBatteryManager {
        private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.input.BatteryController.LocalBluetoothBatteryManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                android.bluetooth.BluetoothDevice bluetoothDevice;
                if (!"android.bluetooth.device.action.BATTERY_LEVEL_CHANGED".equals(intent.getAction()) || (bluetoothDevice = (android.bluetooth.BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", android.bluetooth.BluetoothDevice.class)) == null) {
                    return;
                }
                int batteryLevel = intent.getIntExtra("android.bluetooth.device.extra.BATTERY_LEVEL", -1);
                synchronized (com.android.server.input.BatteryController.LocalBluetoothBatteryManager.this.mBroadcastReceiver) {
                    if (com.android.server.input.BatteryController.LocalBluetoothBatteryManager.this.mRegisteredListener != null) {
                        long eventTime = android.os.SystemClock.uptimeMillis();
                        com.android.server.input.BatteryController.LocalBluetoothBatteryManager.this.mRegisteredListener.onBluetoothBatteryChanged(eventTime, bluetoothDevice.getAddress(), batteryLevel);
                    }
                }
            }
        };
        private final android.content.Context mContext;
        private final java.util.concurrent.Executor mExecutor;
        private com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener mRegisteredListener;

        LocalBluetoothBatteryManager(android.content.Context context, android.os.Looper looper) {
            this.mContext = context;
            this.mExecutor = new android.os.HandlerExecutor(new android.os.Handler(looper));
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void addBatteryListener(com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener listener) {
            synchronized (this.mBroadcastReceiver) {
                if (this.mRegisteredListener != null) {
                    throw new java.lang.IllegalStateException("Only one bluetooth battery listener can be registered at once.");
                }
                this.mRegisteredListener = listener;
                this.mContext.registerReceiver(this.mBroadcastReceiver, new android.content.IntentFilter("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED"));
            }
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void removeBatteryListener(com.android.server.input.BatteryController.BluetoothBatteryManager.BluetoothBatteryListener listener) {
            synchronized (this.mBroadcastReceiver) {
                if (!listener.equals(this.mRegisteredListener)) {
                    throw new java.lang.IllegalStateException("Listener is not registered.");
                }
                this.mRegisteredListener = null;
                this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            }
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public int getBatteryLevel(java.lang.String address) {
            return com.android.server.input.BatteryController.getBluetoothDevice(this.mContext, address).getBatteryLevel();
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void addMetadataListener(java.lang.String address, android.bluetooth.BluetoothAdapter.OnMetadataChangedListener listener) {
            ((android.bluetooth.BluetoothManager) java.util.Objects.requireNonNull((android.bluetooth.BluetoothManager) this.mContext.getSystemService(android.bluetooth.BluetoothManager.class))).getAdapter().addOnMetadataChangedListener(com.android.server.input.BatteryController.getBluetoothDevice(this.mContext, address), this.mExecutor, listener);
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public void removeMetadataListener(java.lang.String address, android.bluetooth.BluetoothAdapter.OnMetadataChangedListener listener) {
            ((android.bluetooth.BluetoothManager) java.util.Objects.requireNonNull((android.bluetooth.BluetoothManager) this.mContext.getSystemService(android.bluetooth.BluetoothManager.class))).getAdapter().removeOnMetadataChangedListener(com.android.server.input.BatteryController.getBluetoothDevice(this.mContext, address), listener);
        }

        @Override // com.android.server.input.BatteryController.BluetoothBatteryManager
        public byte[] getMetadata(java.lang.String address, int key) {
            return com.android.server.input.BatteryController.getBluetoothDevice(this.mContext, address).getMetadata(key);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class State extends android.hardware.input.IInputDeviceBatteryState {
        State(int deviceId) {
            reset(deviceId);
        }

        State(android.hardware.input.IInputDeviceBatteryState s) {
            copyFrom(s);
        }

        State(int deviceId, long updateTime, boolean isPresent, int status, float capacity) {
            initialize(deviceId, updateTime, isPresent, status, capacity);
        }

        public void updateIfChanged(android.hardware.input.IInputDeviceBatteryState other) {
            if (!equalsIgnoringUpdateTime(other)) {
                copyFrom(other);
            }
        }

        public void reset(int deviceId) {
            initialize(deviceId, 0L, false, 1, Float.NaN);
        }

        private void copyFrom(android.hardware.input.IInputDeviceBatteryState s) {
            initialize(s.deviceId, s.updateTime, s.isPresent, s.status, s.capacity);
        }

        private void initialize(int deviceId, long updateTime, boolean isPresent, int status, float capacity) {
            this.deviceId = deviceId;
            this.updateTime = updateTime;
            this.isPresent = isPresent;
            this.status = status;
            this.capacity = capacity;
        }

        public boolean equalsIgnoringUpdateTime(android.hardware.input.IInputDeviceBatteryState other) {
            long updateTime = this.updateTime;
            this.updateTime = other.updateTime;
            boolean eq = equals(other);
            this.updateTime = updateTime;
            return eq;
        }

        public java.lang.String toString() {
            if (!this.isPresent) {
                return "State{<not present>}";
            }
            return "State{time=" + this.updateTime + ", isPresent=" + this.isPresent + ", status=" + this.status + ", capacity=" + this.capacity + "}";
        }
    }

    private static <K, V> boolean anyOf(android.util.ArrayMap<K, V> arrayMap, java.util.function.Predicate<V> test) {
        return findIf(arrayMap, test) != null;
    }

    private static <K, V> V findIf(android.util.ArrayMap<K, V> arrayMap, java.util.function.Predicate<V> test) {
        for (int i = 0; i < arrayMap.size(); i++) {
            V value = arrayMap.valueAt(i);
            if (test.test(value)) {
                return value;
            }
        }
        return null;
    }
}
