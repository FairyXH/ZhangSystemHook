package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
class TvInputHardwareManager implements com.android.server.tv.TvInputHal.Callback {
    private static final java.lang.String TAG = com.android.server.tv.TvInputHardwareManager.class.getSimpleName();
    private final android.media.AudioManager mAudioManager;
    private final android.content.Context mContext;
    private final com.android.server.tv.TvInputHardwareManager.Listener mListener;
    private final com.android.server.tv.TvInputHal mHal = new com.android.server.tv.TvInputHal(this);
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.tv.TvInputHardwareManager.Connection> mConnections = new android.util.SparseArray<>();
    private final java.util.List<android.media.tv.TvInputHardwareInfo> mHardwareList = new java.util.ArrayList();
    private final java.util.List<android.hardware.hdmi.HdmiDeviceInfo> mHdmiDeviceList = new java.util.ArrayList();
    private final android.util.SparseArray<java.lang.String> mHardwareInputIdMap = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.lang.String> mHdmiInputIdMap = new android.util.SparseArray<>();
    private final java.util.Map<java.lang.String, android.media.tv.TvInputInfo> mInputMap = new android.util.ArrayMap();
    private final java.util.Map<java.lang.String, java.util.List<java.lang.String>> mHdmiParentInputMap = new android.util.ArrayMap();
    private final android.hardware.hdmi.IHdmiHotplugEventListener mHdmiHotplugEventListener = new com.android.server.tv.TvInputHardwareManager.HdmiHotplugEventListener();
    private final android.hardware.hdmi.IHdmiDeviceEventListener mHdmiDeviceEventListener = new com.android.server.tv.TvInputHardwareManager.HdmiDeviceEventListener();
    private final android.hardware.hdmi.IHdmiSystemAudioModeChangeListener mHdmiSystemAudioModeChangeListener = new com.android.server.tv.TvInputHardwareManager.HdmiSystemAudioModeChangeListener();
    private final android.content.BroadcastReceiver mVolumeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.tv.TvInputHardwareManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.tv.TvInputHardwareManager.this.handleVolumeChange(context, intent);
        }
    };
    private int mCurrentIndex = 0;
    private int mCurrentMaxIndex = 0;
    private final android.util.SparseBooleanArray mHdmiStateMap = new android.util.SparseBooleanArray();
    private final java.util.List<android.os.Message> mPendingHdmiDeviceEvents = new java.util.ArrayList();
    private final java.util.List<android.os.Message> mPendingTvinputInfoEvents = new java.util.ArrayList();
    private final android.os.Handler mHandler = new com.android.server.tv.TvInputHardwareManager.ListenerHandler();

    interface Listener {
        void onHardwareDeviceAdded(android.media.tv.TvInputHardwareInfo tvInputHardwareInfo);

        void onHardwareDeviceRemoved(android.media.tv.TvInputHardwareInfo tvInputHardwareInfo);

        void onHdmiDeviceAdded(android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo);

        void onHdmiDeviceRemoved(android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo);

        void onHdmiDeviceUpdated(java.lang.String str, android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo);

        void onStateChanged(java.lang.String str, int i);

        void onTvMessage(java.lang.String str, int i, android.os.Bundle bundle);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TvInputHardwareManager(android.content.Context context, com.android.server.tv.TvInputHardwareManager.Listener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mAudioManager = (android.media.AudioManager) context.getSystemService("audio");
        this.mHal.init();
    }

    public void onBootPhase(int phase) {
        if (phase == 500) {
            android.hardware.hdmi.IHdmiControlService hdmiControlService = android.hardware.hdmi.IHdmiControlService.Stub.asInterface(android.os.ServiceManager.getService("hdmi_control"));
            if (hdmiControlService != null) {
                try {
                    hdmiControlService.addHotplugEventListener(this.mHdmiHotplugEventListener);
                    hdmiControlService.addDeviceEventListener(this.mHdmiDeviceEventListener);
                    hdmiControlService.addSystemAudioModeChangeListener(this.mHdmiSystemAudioModeChangeListener);
                    synchronized (this.mLock) {
                        this.mHdmiDeviceList.addAll(hdmiControlService.getInputDevices());
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Error registering listeners to HdmiControlService:", e);
                }
            } else {
                android.util.Slog.w(TAG, "HdmiControlService is not available");
            }
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.media.VOLUME_CHANGED_ACTION");
            filter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
            this.mContext.registerReceiver(this.mVolumeReceiver, filter);
            updateVolume();
        }
    }

    @Override // com.android.server.tv.TvInputHal.Callback
    public void onDeviceAvailable(android.media.tv.TvInputHardwareInfo info, android.media.tv.TvStreamConfig[] configs) {
        synchronized (this.mLock) {
            com.android.server.tv.TvInputHardwareManager.Connection connection = new com.android.server.tv.TvInputHardwareManager.Connection(info);
            connection.updateConfigsLocked(configs);
            connection.updateCableConnectionStatusLocked(info.getCableConnectionStatus());
            this.mConnections.put(info.getDeviceId(), connection);
            buildHardwareListLocked();
            this.mHandler.obtainMessage(2, 0, 0, info).sendToTarget();
            if (info.getType() == 9) {
                processPendingHdmiDeviceEventsLocked();
            }
        }
    }

    private void buildHardwareListLocked() {
        this.mHardwareList.clear();
        for (int i = 0; i < this.mConnections.size(); i++) {
            this.mHardwareList.add(this.mConnections.valueAt(i).getHardwareInfoLocked());
        }
    }

    @Override // com.android.server.tv.TvInputHal.Callback
    public void onDeviceUnavailable(int deviceId) {
        synchronized (this.mLock) {
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            if (connection == null) {
                android.util.Slog.e(TAG, "onDeviceUnavailable: Cannot find a connection with " + deviceId);
                return;
            }
            connection.resetLocked(null, null, null, null, null, null);
            this.mConnections.remove(deviceId);
            buildHardwareListLocked();
            android.media.tv.TvInputHardwareInfo info = connection.getHardwareInfoLocked();
            if (info.getType() == 9) {
                java.util.Iterator<android.hardware.hdmi.HdmiDeviceInfo> it = this.mHdmiDeviceList.iterator();
                while (it.hasNext()) {
                    android.hardware.hdmi.HdmiDeviceInfo deviceInfo = it.next();
                    if (deviceInfo.getPortId() == info.getHdmiPortId()) {
                        this.mHandler.obtainMessage(5, 0, 0, deviceInfo).sendToTarget();
                        it.remove();
                    }
                }
            }
            this.mHandler.obtainMessage(3, 0, 0, info).sendToTarget();
        }
    }

    @Override // com.android.server.tv.TvInputHal.Callback
    public void onStreamConfigurationChanged(final int deviceId, android.media.tv.TvStreamConfig[] configs, int cableConnectionStatus) {
        synchronized (this.mLock) {
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            if (connection == null) {
                android.util.Slog.e(TAG, "StreamConfigurationChanged: Cannot find a connection with " + deviceId);
                return;
            }
            int previousConfigsLength = connection.getConfigsLengthLocked();
            int previousCableConnectionStatus = connection.getInputStateLocked();
            connection.updateConfigsLocked(configs);
            java.lang.String inputId = this.mHardwareInputIdMap.get(deviceId);
            if (inputId != null) {
                if (connection.updateCableConnectionStatusLocked(cableConnectionStatus)) {
                    if (previousCableConnectionStatus != connection.getInputStateLocked()) {
                        this.mHandler.obtainMessage(1, connection.getInputStateLocked(), 0, inputId).sendToTarget();
                    }
                } else {
                    if ((previousConfigsLength == 0) != (connection.getConfigsLengthLocked() == 0)) {
                        this.mHandler.obtainMessage(1, connection.getInputStateLocked(), 0, inputId).sendToTarget();
                    }
                }
            } else {
                android.os.Message msg = this.mHandler.obtainMessage(7, deviceId, cableConnectionStatus, connection);
                this.mPendingTvinputInfoEvents.removeIf(new java.util.function.Predicate() { // from class: com.android.server.tv.TvInputHardwareManager$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.tv.TvInputHardwareManager.lambda$onStreamConfigurationChanged$0(deviceId, (android.os.Message) obj);
                    }
                });
                this.mPendingTvinputInfoEvents.add(msg);
            }
            android.media.tv.ITvInputHardwareCallback callback = connection.getCallbackLocked();
            if (callback != null) {
                try {
                    callback.onStreamConfigChanged(configs);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "error in onStreamConfigurationChanged", e);
                }
            }
        }
    }

    static /* synthetic */ boolean lambda$onStreamConfigurationChanged$0(int deviceId, android.os.Message message) {
        return message.arg1 == deviceId;
    }

    @Override // com.android.server.tv.TvInputHal.Callback
    public void onFirstFrameCaptured(int deviceId, int streamId) {
        synchronized (this.mLock) {
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            if (connection == null) {
                android.util.Slog.e(TAG, "FirstFrameCaptured: Cannot find a connection with " + deviceId);
                return;
            }
            java.lang.Runnable runnable = connection.getOnFirstFrameCapturedLocked();
            if (runnable != null) {
                runnable.run();
                connection.setOnFirstFrameCapturedLocked(null);
            }
        }
    }

    @Override // com.android.server.tv.TvInputHal.Callback
    public void onTvMessage(int deviceId, int type, android.os.Bundle data) {
        synchronized (this.mLock) {
            java.lang.String inputId = this.mHardwareInputIdMap.get(deviceId);
            if (inputId == null) {
                return;
            }
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = this.mHardwareInputIdMap.get(deviceId);
            args.arg2 = data;
            this.mHandler.obtainMessage(8, type, 0, args).sendToTarget();
        }
    }

    public java.util.List<android.media.tv.TvInputHardwareInfo> getHardwareList() {
        java.util.List<android.media.tv.TvInputHardwareInfo> listUnmodifiableList;
        synchronized (this.mLock) {
            listUnmodifiableList = java.util.Collections.unmodifiableList(this.mHardwareList);
        }
        return listUnmodifiableList;
    }

    public java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getHdmiDeviceList() {
        java.util.List<android.hardware.hdmi.HdmiDeviceInfo> listUnmodifiableList;
        synchronized (this.mLock) {
            listUnmodifiableList = java.util.Collections.unmodifiableList(this.mHdmiDeviceList);
        }
        return listUnmodifiableList;
    }

    public android.util.SparseArray<java.lang.String> getHardwareInputIdMap() {
        android.util.SparseArray<java.lang.String> sparseArrayClone;
        synchronized (this.mLock) {
            sparseArrayClone = this.mHardwareInputIdMap.clone();
        }
        return sparseArrayClone;
    }

    public android.util.SparseArray<java.lang.String> getHdmiInputIdMap() {
        android.util.SparseArray<java.lang.String> sparseArrayClone;
        synchronized (this.mLock) {
            sparseArrayClone = this.mHdmiInputIdMap.clone();
        }
        return sparseArrayClone;
    }

    public java.util.Map<java.lang.String, android.media.tv.TvInputInfo> getInputMap() {
        java.util.Map<java.lang.String, android.media.tv.TvInputInfo> mapUnmodifiableMap;
        synchronized (this.mLock) {
            mapUnmodifiableMap = java.util.Collections.unmodifiableMap(this.mInputMap);
        }
        return mapUnmodifiableMap;
    }

    public java.util.Map<java.lang.String, java.util.List<java.lang.String>> getHdmiParentInputMap() {
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> mapUnmodifiableMap;
        synchronized (this.mLock) {
            mapUnmodifiableMap = java.util.Collections.unmodifiableMap(this.mHdmiParentInputMap);
        }
        return mapUnmodifiableMap;
    }

    private boolean checkUidChangedLocked(com.android.server.tv.TvInputHardwareManager.Connection connection, int callingUid, int resolvedUserId) {
        java.lang.Integer connectionCallingUid = connection.getCallingUidLocked();
        java.lang.Integer connectionResolvedUserId = connection.getResolvedUserIdLocked();
        return connectionCallingUid == null || connectionResolvedUserId == null || connectionCallingUid.intValue() != callingUid || connectionResolvedUserId.intValue() != resolvedUserId;
    }

    public void addHardwareInput(int deviceId, android.media.tv.TvInputInfo info) {
        java.lang.String inputId;
        int state;
        synchronized (this.mLock) {
            java.lang.String oldInputId = this.mHardwareInputIdMap.get(deviceId);
            if (oldInputId != null) {
                android.util.Slog.w(TAG, "Trying to override previous registration: old = " + this.mInputMap.get(oldInputId) + ":" + deviceId + ", new = " + info + ":" + deviceId);
            }
            this.mHardwareInputIdMap.put(deviceId, info.getId());
            this.mInputMap.put(info.getId(), info);
            processPendingTvInputInfoEventsLocked();
            android.util.Slog.d(TAG, "deviceId =" + deviceId + ", tvinputinfo = " + info);
            for (int i = 0; i < this.mHdmiStateMap.size(); i++) {
                android.media.tv.TvInputHardwareInfo hardwareInfo = findHardwareInfoForHdmiPortLocked(this.mHdmiStateMap.keyAt(i));
                if (hardwareInfo != null && (inputId = this.mHardwareInputIdMap.get(hardwareInfo.getDeviceId())) != null && inputId.equals(info.getId())) {
                    if (this.mHdmiStateMap.valueAt(i)) {
                        state = 0;
                    } else {
                        state = 1;
                    }
                    this.mHandler.obtainMessage(1, state, 0, inputId).sendToTarget();
                    return;
                }
            }
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            if (connection != null) {
                this.mHandler.obtainMessage(1, connection.getInputStateLocked(), 0, info.getId()).sendToTarget();
            }
        }
    }

    private static <T> int indexOfEqualValue(android.util.SparseArray<T> map, T value) {
        for (int i = 0; i < map.size(); i++) {
            if (map.valueAt(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean intArrayContains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public void addHdmiInput(int id, android.media.tv.TvInputInfo info) {
        if (info.getType() != 1007) {
            throw new java.lang.IllegalArgumentException("info (" + info + ") has non-HDMI type.");
        }
        synchronized (this.mLock) {
            java.lang.String parentId = info.getParentId();
            int parentIndex = indexOfEqualValue(this.mHardwareInputIdMap, parentId);
            if (parentIndex < 0) {
                throw new java.lang.IllegalArgumentException("info (" + info + ") has invalid parentId.");
            }
            java.lang.String oldInputId = this.mHdmiInputIdMap.get(id);
            if (oldInputId != null) {
                android.util.Slog.w(TAG, "Trying to override previous registration: old = " + this.mInputMap.get(oldInputId) + ":" + id + ", new = " + info + ":" + id);
            }
            this.mHdmiInputIdMap.put(id, info.getId());
            this.mInputMap.put(info.getId(), info);
            if (!this.mHdmiParentInputMap.containsKey(parentId)) {
                this.mHdmiParentInputMap.put(parentId, new java.util.ArrayList());
            }
            this.mHdmiParentInputMap.get(parentId).add(info.getId());
        }
    }

    public void removeHardwareInput(java.lang.String inputId) {
        synchronized (this.mLock) {
            int hardwareIndex = indexOfEqualValue(this.mHardwareInputIdMap, inputId);
            if (hardwareIndex >= 0) {
                this.mHardwareInputIdMap.removeAt(hardwareIndex);
            }
            int deviceIndex = indexOfEqualValue(this.mHdmiInputIdMap, inputId);
            if (deviceIndex >= 0) {
                this.mHdmiInputIdMap.removeAt(deviceIndex);
            }
            if (this.mInputMap.containsKey(inputId)) {
                java.lang.String parentId = this.mInputMap.get(inputId).getParentId();
                if (parentId != null && this.mHdmiParentInputMap.containsKey(parentId)) {
                    java.util.List<java.lang.String> parentInputList = this.mHdmiParentInputMap.get(parentId);
                    parentInputList.remove(inputId);
                    if (parentInputList.isEmpty()) {
                        this.mHdmiParentInputMap.remove(parentId);
                    }
                }
                this.mInputMap.remove(inputId);
            }
        }
    }

    public void updateInputInfo(android.media.tv.TvInputInfo info) {
        synchronized (this.mLock) {
            if (this.mInputMap.containsKey(info.getId())) {
                android.util.Slog.w(TAG, "update inputInfo for input id " + info.getId());
                this.mInputMap.put(info.getId(), info);
            }
        }
    }

    public android.media.tv.ITvInputHardware acquireHardware(int deviceId, android.media.tv.ITvInputHardwareCallback callback, android.media.tv.TvInputInfo info, int callingUid, int resolvedUserId, java.lang.String tvInputSessionId, int priorityHint) throws android.os.RemoteException {
        if (callback == null) {
            throw new java.lang.NullPointerException();
        }
        android.media.tv.tunerresourcemanager.TunerResourceManager trm = (android.media.tv.tunerresourcemanager.TunerResourceManager) this.mContext.getSystemService("tv_tuner_resource_mgr");
        synchronized (this.mLock) {
            try {
                com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
                if (connection == null) {
                    android.util.Slog.e(TAG, "Invalid deviceId : " + deviceId);
                    return null;
                }
                android.media.tv.tunerresourcemanager.ResourceClientProfile profile = new android.media.tv.tunerresourcemanager.ResourceClientProfile();
                try {
                    profile.tvInputSessionId = tvInputSessionId;
                } catch (java.lang.Throwable th) {
                    e = th;
                    throw e;
                }
                try {
                    profile.useCase = priorityHint;
                    android.media.tv.tunerresourcemanager.ResourceClientProfile holderProfile = connection.getResourceClientProfileLocked();
                    if (holderProfile == null || trm == null || trm.isHigherPriority(profile, holderProfile)) {
                        com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl hardware = new com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl(connection.getHardwareInfoLocked());
                        try {
                            callback.asBinder().linkToDeath(connection, 0);
                            connection.resetLocked(hardware, callback, info, java.lang.Integer.valueOf(callingUid), java.lang.Integer.valueOf(resolvedUserId), profile);
                            return connection.getHardwareLocked();
                        } catch (android.os.RemoteException e) {
                            hardware.release();
                            return null;
                        }
                    }
                    android.util.Slog.d(TAG, "Acquiring does not show higher priority than the current holder. Device id:" + deviceId);
                    return null;
                } catch (java.lang.Throwable th2) {
                    e = th2;
                    throw e;
                }
            } catch (java.lang.Throwable th3) {
                e = th3;
            }
        }
    }

    public void releaseHardware(int deviceId, android.media.tv.ITvInputHardware hardware, int callingUid, int resolvedUserId) {
        synchronized (this.mLock) {
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            if (connection == null) {
                android.util.Slog.e(TAG, "Invalid deviceId : " + deviceId);
                return;
            }
            if (connection.getHardwareLocked() == hardware && !checkUidChangedLocked(connection, callingUid, resolvedUserId)) {
                android.media.tv.ITvInputHardwareCallback callback = connection.getCallbackLocked();
                if (callback != null) {
                    callback.asBinder().unlinkToDeath(connection, 0);
                }
                connection.resetLocked(null, null, null, null, null, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.tv.TvInputHardwareInfo findHardwareInfoForHdmiPortLocked(int port) {
        for (android.media.tv.TvInputHardwareInfo hardwareInfo : this.mHardwareList) {
            if (hardwareInfo.getType() == 9 && hardwareInfo.getHdmiPortId() == port) {
                return hardwareInfo;
            }
        }
        return null;
    }

    private int findDeviceIdForInputIdLocked(java.lang.String inputId) {
        for (int i = 0; i < this.mConnections.size(); i++) {
            int key = this.mConnections.keyAt(i);
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(key);
            if (connection != null && connection.getInfoLocked() != null && connection.getInfoLocked().getId().equals(inputId)) {
                return key;
            }
        }
        return -1;
    }

    public java.util.List<android.media.tv.TvStreamConfig> getAvailableTvStreamConfigList(java.lang.String inputId, int callingUid, int resolvedUserId) {
        java.util.List<android.media.tv.TvStreamConfig> configsList = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            int deviceId = findDeviceIdForInputIdLocked(inputId);
            if (deviceId < 0) {
                android.util.Slog.e(TAG, "Invalid inputId : " + inputId);
                return configsList;
            }
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            for (android.media.tv.TvStreamConfig config : connection.getConfigsLocked()) {
                if (config.getType() == 2) {
                    configsList.add(config);
                }
            }
            return configsList;
        }
    }

    public boolean setTvMessageEnabled(java.lang.String inputId, int type, boolean enabled) {
        synchronized (this.mLock) {
            int deviceId = findDeviceIdForInputIdLocked(inputId);
            if (deviceId < 0) {
                android.util.Slog.e(TAG, "Invalid inputId : " + inputId);
                return false;
            }
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            boolean success = true;
            for (android.media.tv.TvStreamConfig config : connection.getConfigsLocked()) {
                success = success && this.mHal.setTvMessageEnabled(deviceId, config, type, enabled) == 0;
            }
            return success;
        }
    }

    public boolean captureFrame(java.lang.String inputId, android.view.Surface surface, final android.media.tv.TvStreamConfig config, int callingUid, int resolvedUserId) {
        synchronized (this.mLock) {
            int deviceId = findDeviceIdForInputIdLocked(inputId);
            if (deviceId < 0) {
                android.util.Slog.e(TAG, "Invalid inputId : " + inputId);
                return false;
            }
            com.android.server.tv.TvInputHardwareManager.Connection connection = this.mConnections.get(deviceId);
            final com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl hardwareImpl = connection.getHardwareImplLocked();
            if (hardwareImpl == null) {
                return false;
            }
            java.lang.Runnable runnable = connection.getOnFirstFrameCapturedLocked();
            if (runnable != null) {
                runnable.run();
                connection.setOnFirstFrameCapturedLocked(null);
            }
            boolean result = hardwareImpl.startCapture(surface, config);
            if (result) {
                connection.setOnFirstFrameCapturedLocked(new java.lang.Runnable() { // from class: com.android.server.tv.TvInputHardwareManager.2
                    @Override // java.lang.Runnable
                    public void run() {
                        hardwareImpl.stopCapture(config);
                    }
                });
            }
            return result;
        }
    }

    private void processPendingHdmiDeviceEventsLocked() {
        java.util.Iterator<android.os.Message> it = this.mPendingHdmiDeviceEvents.iterator();
        while (it.hasNext()) {
            android.os.Message msg = it.next();
            android.hardware.hdmi.HdmiDeviceInfo deviceInfo = (android.hardware.hdmi.HdmiDeviceInfo) msg.obj;
            android.media.tv.TvInputHardwareInfo hardwareInfo = findHardwareInfoForHdmiPortLocked(deviceInfo.getPortId());
            if (hardwareInfo != null) {
                msg.sendToTarget();
                it.remove();
            }
        }
    }

    private void processPendingTvInputInfoEventsLocked() {
        java.util.Iterator<android.os.Message> it = this.mPendingTvinputInfoEvents.iterator();
        while (it.hasNext()) {
            android.os.Message msg = it.next();
            int deviceId = msg.arg1;
            java.lang.String inputId = this.mHardwareInputIdMap.get(deviceId);
            if (inputId != null) {
                msg.sendToTarget();
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVolume() {
        this.mCurrentMaxIndex = this.mAudioManager.getStreamMaxVolume(3);
        this.mCurrentIndex = this.mAudioManager.getStreamVolume(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleVolumeChange(android.content.Context context, android.content.Intent intent) {
        byte b;
        int index;
        java.lang.String action = intent.getAction();
        switch (action.hashCode()) {
            case -1940635523:
                b = action.equals("android.media.VOLUME_CHANGED_ACTION") ? (byte) 0 : (byte) -1;
                break;
            case 1920758225:
                b = action.equals("android.media.STREAM_MUTE_CHANGED_ACTION") ? (byte) 1 : (byte) -1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                if (streamType != 3 || (index = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0)) == this.mCurrentIndex) {
                    return;
                }
                this.mCurrentIndex = index;
                break;
                break;
            case 1:
                int streamType2 = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                if (streamType2 != 3) {
                    return;
                }
                break;
            default:
                android.util.Slog.w(TAG, "Unrecognized intent: " + intent);
                return;
        }
        synchronized (this.mLock) {
            for (int i = 0; i < this.mConnections.size(); i++) {
                com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl hardwareImpl = this.mConnections.valueAt(i).getHardwareImplLocked();
                if (hardwareImpl != null) {
                    hardwareImpl.onMediaStreamVolumeChanged();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getMediaStreamVolume() {
        return this.mCurrentIndex / this.mCurrentMaxIndex;
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            synchronized (this.mLock) {
                pw.println("TvInputHardwareManager Info:");
                pw.increaseIndent();
                pw.println("mConnections: deviceId -> Connection");
                pw.increaseIndent();
                for (int i = 0; i < this.mConnections.size(); i++) {
                    int deviceId = this.mConnections.keyAt(i);
                    com.android.server.tv.TvInputHardwareManager.Connection mConnection = this.mConnections.valueAt(i);
                    pw.println(deviceId + ": " + mConnection);
                }
                pw.decreaseIndent();
                pw.println("mHardwareList:");
                pw.increaseIndent();
                for (android.media.tv.TvInputHardwareInfo tvInputHardwareInfo : this.mHardwareList) {
                    pw.println(tvInputHardwareInfo);
                }
                pw.decreaseIndent();
                pw.println("mHdmiDeviceList:");
                pw.increaseIndent();
                for (android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo : this.mHdmiDeviceList) {
                    pw.println(hdmiDeviceInfo);
                }
                pw.decreaseIndent();
                pw.println("mHardwareInputIdMap: deviceId -> inputId");
                pw.increaseIndent();
                for (int i2 = 0; i2 < this.mHardwareInputIdMap.size(); i2++) {
                    int deviceId2 = this.mHardwareInputIdMap.keyAt(i2);
                    java.lang.String inputId = this.mHardwareInputIdMap.valueAt(i2);
                    pw.println(deviceId2 + ": " + inputId);
                }
                pw.decreaseIndent();
                pw.println("mHdmiInputIdMap: id -> inputId");
                pw.increaseIndent();
                for (int i3 = 0; i3 < this.mHdmiInputIdMap.size(); i3++) {
                    int id = this.mHdmiInputIdMap.keyAt(i3);
                    java.lang.String inputId2 = this.mHdmiInputIdMap.valueAt(i3);
                    pw.println(id + ": " + inputId2);
                }
                pw.decreaseIndent();
                pw.println("mInputMap: inputId -> inputInfo");
                pw.increaseIndent();
                for (java.util.Map.Entry<java.lang.String, android.media.tv.TvInputInfo> entry : this.mInputMap.entrySet()) {
                    pw.println(entry.getKey() + ": " + entry.getValue());
                }
                pw.decreaseIndent();
                pw.decreaseIndent();
            }
        }
    }

    private class Connection implements android.os.IBinder.DeathRecipient {
        private android.media.tv.ITvInputHardwareCallback mCallback;
        private android.media.tv.TvInputHardwareInfo mHardwareInfo;
        private android.media.tv.TvInputInfo mInfo;
        private java.lang.Runnable mOnFirstFrameCaptured;
        private com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl mHardware = null;
        private android.media.tv.TvStreamConfig[] mConfigs = null;
        private java.lang.Integer mCallingUid = null;
        private java.lang.Integer mResolvedUserId = null;
        private android.media.tv.tunerresourcemanager.ResourceClientProfile mResourceClientProfile = null;
        private boolean mIsCableConnectionStatusUpdated = false;

        public Connection(android.media.tv.TvInputHardwareInfo hardwareInfo) {
            this.mHardwareInfo = hardwareInfo;
        }

        public void resetLocked(com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl hardware, android.media.tv.ITvInputHardwareCallback callback, android.media.tv.TvInputInfo info, java.lang.Integer callingUid, java.lang.Integer resolvedUserId, android.media.tv.tunerresourcemanager.ResourceClientProfile profile) {
            if (this.mHardware != null) {
                try {
                    this.mCallback.onReleased();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.tv.TvInputHardwareManager.TAG, "error in Connection::resetLocked", e);
                }
                this.mHardware.release();
            }
            this.mHardware = hardware;
            this.mCallback = callback;
            this.mInfo = info;
            this.mCallingUid = callingUid;
            this.mResolvedUserId = resolvedUserId;
            this.mOnFirstFrameCaptured = null;
            this.mResourceClientProfile = profile;
            if (this.mHardware != null && this.mCallback != null) {
                try {
                    this.mCallback.onStreamConfigChanged(getConfigsLocked());
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(com.android.server.tv.TvInputHardwareManager.TAG, "error in Connection::resetLocked", e2);
                }
            }
        }

        public void updateConfigsLocked(android.media.tv.TvStreamConfig[] configs) {
            this.mConfigs = configs;
        }

        public android.media.tv.TvInputHardwareInfo getHardwareInfoLocked() {
            return this.mHardwareInfo;
        }

        public android.media.tv.TvInputInfo getInfoLocked() {
            return this.mInfo;
        }

        public android.media.tv.ITvInputHardware getHardwareLocked() {
            return this.mHardware;
        }

        public com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl getHardwareImplLocked() {
            return this.mHardware;
        }

        public android.media.tv.ITvInputHardwareCallback getCallbackLocked() {
            return this.mCallback;
        }

        public android.media.tv.TvStreamConfig[] getConfigsLocked() {
            return this.mConfigs;
        }

        public java.lang.Integer getCallingUidLocked() {
            return this.mCallingUid;
        }

        public java.lang.Integer getResolvedUserIdLocked() {
            return this.mResolvedUserId;
        }

        public void setOnFirstFrameCapturedLocked(java.lang.Runnable runnable) {
            this.mOnFirstFrameCaptured = runnable;
        }

        public java.lang.Runnable getOnFirstFrameCapturedLocked() {
            return this.mOnFirstFrameCaptured;
        }

        public android.media.tv.tunerresourcemanager.ResourceClientProfile getResourceClientProfileLocked() {
            return this.mResourceClientProfile;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.tv.TvInputHardwareManager.this.mLock) {
                resetLocked(null, null, null, null, null, null);
            }
        }

        public java.lang.String toString() {
            return "Connection{ mHardwareInfo: " + this.mHardwareInfo + ", mInfo: " + this.mInfo + ", mCallback: " + this.mCallback + ", mHardware: " + this.mHardware + ", mConfigs: " + java.util.Arrays.toString(this.mConfigs) + ", mCallingUid: " + this.mCallingUid + ", mResolvedUserId: " + this.mResolvedUserId + ", mResourceClientProfile: " + this.mResourceClientProfile + " }";
        }

        public boolean updateCableConnectionStatusLocked(int cableConnectionStatus) {
            if (cableConnectionStatus != 0 || this.mIsCableConnectionStatusUpdated) {
                this.mIsCableConnectionStatusUpdated = true;
                this.mHardwareInfo = this.mHardwareInfo.toBuilder().cableConnectionStatus(cableConnectionStatus).build();
            }
            return this.mIsCableConnectionStatusUpdated;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getConfigsLengthLocked() {
            if (this.mConfigs == null) {
                return 0;
            }
            return this.mConfigs.length;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getInputStateLocked() {
            int configsLength = getConfigsLengthLocked();
            if (configsLength <= 0 || this.mIsCableConnectionStatusUpdated) {
                switch (this.mHardwareInfo.getCableConnectionStatus()) {
                }
                return 0;
            }
            return 0;
        }
    }

    private class TvInputHardwareImpl extends android.media.tv.ITvInputHardware.Stub {
        private android.media.AudioDevicePort mAudioSource;
        private final android.media.tv.TvInputHardwareInfo mInfo;
        private final java.lang.Object mImplLock = new java.lang.Object();
        private final android.media.AudioManager.OnAudioPortUpdateListener mAudioListener = new android.media.AudioManager.OnAudioPortUpdateListener() { // from class: com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.1
            public void onAudioPortListUpdate(android.media.AudioPort[] portList) {
                synchronized (com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mImplLock) {
                    com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.updateAudioConfigLocked();
                }
            }

            public void onAudioPatchListUpdate(android.media.AudioPatch[] patchList) {
            }

            public void onServiceDied() {
                synchronized (com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mImplLock) {
                    com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mAudioSource = null;
                    com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mAudioSink.clear();
                    if (com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mAudioPatch != null) {
                        android.media.AudioManager unused = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
                        android.media.AudioManager.releaseAudioPatch(com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mAudioPatch);
                        com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl.this.mAudioPatch = null;
                    }
                }
            }
        };
        private boolean mReleased = false;
        private int mOverrideAudioType = 0;
        private java.lang.String mOverrideAudioAddress = "";
        private java.util.List<android.media.AudioDevicePort> mAudioSink = new java.util.ArrayList();
        private android.media.AudioPatch mAudioPatch = null;
        private float mCommittedVolume = -1.0f;
        private float mSourceVolume = 0.0f;
        private android.media.tv.TvStreamConfig mActiveConfig = null;
        private int mDesiredSamplingRate = 0;
        private int mDesiredChannelMask = 1;
        private int mDesiredFormat = 1;

        public TvInputHardwareImpl(android.media.tv.TvInputHardwareInfo info) {
            this.mInfo = info;
            com.android.server.tv.TvInputHardwareManager.this.mAudioManager.registerAudioPortUpdateListener(this.mAudioListener);
            if (this.mInfo.getAudioType() != 0) {
                synchronized (this.mImplLock) {
                    this.mAudioSource = findAudioDevicePort(this.mInfo.getAudioType(), this.mInfo.getAudioAddress());
                    findAudioSinkFromAudioPolicy(this.mAudioSink);
                }
            }
        }

        private void findAudioSinkFromAudioPolicy(java.util.List<android.media.AudioDevicePort> sinks) {
            sinks.clear();
            java.util.ArrayList<android.media.AudioDevicePort> devicePorts = new java.util.ArrayList<>();
            android.media.AudioManager unused = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
            if (android.media.AudioManager.listAudioDevicePorts(devicePorts) != 0) {
                return;
            }
            int sinkDevice = com.android.server.tv.TvInputHardwareManager.this.mAudioManager.getDevicesForStream(3);
            for (android.media.AudioDevicePort port : devicePorts) {
                if ((port.type() & sinkDevice) != 0 && !android.media.AudioSystem.isInputDevice(port.type())) {
                    sinks.add(port);
                }
            }
        }

        private android.media.AudioDevicePort findAudioDevicePort(int type, java.lang.String address) {
            if (type == 0) {
                return null;
            }
            java.util.ArrayList<android.media.AudioDevicePort> devicePorts = new java.util.ArrayList<>();
            android.media.AudioManager unused = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
            if (android.media.AudioManager.listAudioDevicePorts(devicePorts) != 0) {
                return null;
            }
            for (android.media.AudioDevicePort port : devicePorts) {
                if (port.type() == type && port.address().equals(address)) {
                    return port;
                }
            }
            return null;
        }

        public void release() {
            synchronized (this.mImplLock) {
                com.android.server.tv.TvInputHardwareManager.this.mAudioManager.unregisterAudioPortUpdateListener(this.mAudioListener);
                if (this.mAudioPatch != null) {
                    android.media.AudioManager unused = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
                    android.media.AudioManager.releaseAudioPatch(this.mAudioPatch);
                    this.mAudioPatch = null;
                }
                this.mReleased = true;
            }
        }

        public boolean setSurface(android.view.Surface surface, android.media.tv.TvStreamConfig config) throws android.os.RemoteException {
            synchronized (this.mImplLock) {
                if (this.mReleased) {
                    throw new java.lang.IllegalStateException("Device already released.");
                }
                int result = 0;
                boolean z = true;
                if (surface == null) {
                    if (this.mActiveConfig == null) {
                        return true;
                    }
                    result = com.android.server.tv.TvInputHardwareManager.this.mHal.removeStream(this.mInfo.getDeviceId(), this.mActiveConfig);
                    this.mActiveConfig = null;
                } else {
                    if (config == null) {
                        return false;
                    }
                    if (this.mActiveConfig != null && !config.equals(this.mActiveConfig) && (result = com.android.server.tv.TvInputHardwareManager.this.mHal.removeStream(this.mInfo.getDeviceId(), this.mActiveConfig)) != 0) {
                        this.mActiveConfig = null;
                    }
                    if (result == 0 && (result = com.android.server.tv.TvInputHardwareManager.this.mHal.addOrUpdateStream(this.mInfo.getDeviceId(), surface, config)) == 0) {
                        this.mActiveConfig = config;
                    }
                }
                updateAudioConfigLocked();
                if (result != 0) {
                    z = false;
                }
                return z;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAudioConfigLocked() {
            char c;
            int gainValue;
            boolean sinkUpdated = updateAudioSinkLocked();
            boolean sourceUpdated = updateAudioSourceLocked();
            if (this.mAudioSource == null || this.mAudioSink.isEmpty() || this.mActiveConfig == null) {
                if (this.mAudioPatch != null) {
                    android.media.AudioManager unused = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
                    android.media.AudioManager.releaseAudioPatch(this.mAudioPatch);
                    this.mAudioPatch = null;
                    return;
                }
                return;
            }
            com.android.server.tv.TvInputHardwareManager.this.updateVolume();
            float volume = this.mSourceVolume * com.android.server.tv.TvInputHardwareManager.this.getMediaStreamVolume();
            android.media.AudioGainConfig sourceGainConfig = null;
            int i = 1;
            if (this.mAudioSource.gains().length > 0 && volume != this.mCommittedVolume) {
                android.media.AudioGain sourceGain = null;
                android.media.AudioGain[] audioGainArrGains = this.mAudioSource.gains();
                int length = audioGainArrGains.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    android.media.AudioGain gain = audioGainArrGains[i2];
                    if ((gain.mode() & 1) == 0) {
                        i2++;
                    } else {
                        sourceGain = gain;
                        break;
                    }
                }
                if (sourceGain != null) {
                    int steps = (sourceGain.maxValue() - sourceGain.minValue()) / sourceGain.stepValue();
                    int gainValue2 = sourceGain.minValue();
                    if (volume < 1.0f) {
                        gainValue = gainValue2 + (sourceGain.stepValue() * ((int) (((double) (steps * volume)) + 0.5d)));
                    } else {
                        gainValue = sourceGain.maxValue();
                    }
                    int[] gainValues = {gainValue};
                    sourceGainConfig = sourceGain.buildConfig(1, sourceGain.channelMask(), gainValues, 0);
                } else {
                    android.util.Slog.w(com.android.server.tv.TvInputHardwareManager.TAG, "No audio source gain with MODE_JOINT support exists.");
                }
            }
            android.media.AudioPortConfig sourceConfig = this.mAudioSource.activeConfig();
            java.util.List<android.media.AudioPortConfig> sinkConfigs = new java.util.ArrayList<>();
            android.media.AudioPatch[] audioPatchArray = {this.mAudioPatch};
            boolean shouldRecreateAudioPatch = sourceUpdated || sinkUpdated || this.mAudioPatch == null;
            for (android.media.AudioDevicePort audioSink : this.mAudioSink) {
                android.media.AudioDevicePortConfig audioDevicePortConfigActiveConfig = audioSink.activeConfig();
                int sinkSamplingRate = this.mDesiredSamplingRate;
                int sinkChannelMask = this.mDesiredChannelMask;
                int sinkFormat = this.mDesiredFormat;
                if (audioDevicePortConfigActiveConfig != null) {
                    if (sinkSamplingRate == 0) {
                        sinkSamplingRate = audioDevicePortConfigActiveConfig.samplingRate();
                    }
                    if (sinkChannelMask == i) {
                        sinkChannelMask = audioDevicePortConfigActiveConfig.channelMask();
                    }
                    if (sinkFormat == i) {
                        sinkFormat = audioDevicePortConfigActiveConfig.format();
                    }
                }
                if (audioDevicePortConfigActiveConfig == null || audioDevicePortConfigActiveConfig.samplingRate() != sinkSamplingRate || audioDevicePortConfigActiveConfig.channelMask() != sinkChannelMask || audioDevicePortConfigActiveConfig.format() != sinkFormat) {
                    if (!com.android.server.tv.TvInputHardwareManager.intArrayContains(audioSink.samplingRates(), sinkSamplingRate) && audioSink.samplingRates().length > 0) {
                        sinkSamplingRate = audioSink.samplingRates()[0];
                    }
                    if (!com.android.server.tv.TvInputHardwareManager.intArrayContains(audioSink.channelMasks(), sinkChannelMask)) {
                        sinkChannelMask = 1;
                    }
                    if (!com.android.server.tv.TvInputHardwareManager.intArrayContains(audioSink.formats(), sinkFormat)) {
                        sinkFormat = 1;
                    }
                    audioDevicePortConfigActiveConfig = audioSink.buildConfig(sinkSamplingRate, sinkChannelMask, sinkFormat, (android.media.AudioGainConfig) null);
                    shouldRecreateAudioPatch = true;
                }
                sinkConfigs.add(audioDevicePortConfigActiveConfig);
                i = 1;
            }
            android.media.AudioPortConfig sinkConfig = sinkConfigs.get(0);
            if (sourceConfig == null || sourceGainConfig != null) {
                int sourceSamplingRate = 0;
                if (com.android.server.tv.TvInputHardwareManager.intArrayContains(this.mAudioSource.samplingRates(), sinkConfig.samplingRate())) {
                    sourceSamplingRate = sinkConfig.samplingRate();
                } else if (this.mAudioSource.samplingRates().length > 0) {
                    sourceSamplingRate = this.mAudioSource.samplingRates()[0];
                }
                int sourceChannelMask = 1;
                int[] iArrChannelMasks = this.mAudioSource.channelMasks();
                int length2 = iArrChannelMasks.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    }
                    int inChannelMask = iArrChannelMasks[i3];
                    android.media.AudioPortConfig sourceConfig2 = sourceConfig;
                    int iChannelCountFromOutChannelMask = android.media.AudioFormat.channelCountFromOutChannelMask(sinkConfig.channelMask());
                    int sourceChannelMask2 = sourceChannelMask;
                    int sourceChannelMask3 = android.media.AudioFormat.channelCountFromInChannelMask(inChannelMask);
                    if (iChannelCountFromOutChannelMask != sourceChannelMask3) {
                        i3++;
                        sourceChannelMask = sourceChannelMask2;
                        sourceConfig = sourceConfig2;
                    } else {
                        sourceChannelMask = inChannelMask;
                        break;
                    }
                }
                int sourceFormat = 1;
                if (com.android.server.tv.TvInputHardwareManager.intArrayContains(this.mAudioSource.formats(), sinkConfig.format())) {
                    sourceFormat = sinkConfig.format();
                }
                shouldRecreateAudioPatch = true;
                sourceConfig = this.mAudioSource.buildConfig(sourceSamplingRate, sourceChannelMask, sourceFormat, sourceGainConfig);
            }
            if (shouldRecreateAudioPatch) {
                this.mCommittedVolume = volume;
                if (this.mAudioPatch == null || sinkUpdated || sourceUpdated) {
                    if (this.mAudioPatch == null) {
                        c = 0;
                    } else {
                        android.media.AudioManager unused2 = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
                        android.media.AudioManager.releaseAudioPatch(this.mAudioPatch);
                        c = 0;
                        audioPatchArray[0] = null;
                    }
                    android.media.AudioManager unused3 = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
                    android.media.AudioPortConfig[] audioPortConfigArr = new android.media.AudioPortConfig[1];
                    audioPortConfigArr[c] = sourceConfig;
                    android.media.AudioManager.createAudioPatch(audioPatchArray, audioPortConfigArr, (android.media.AudioPortConfig[]) sinkConfigs.toArray(new android.media.AudioPortConfig[sinkConfigs.size()]));
                    this.mAudioPatch = audioPatchArray[c];
                }
            }
            if (sourceGainConfig != null) {
                android.media.AudioManager unused4 = com.android.server.tv.TvInputHardwareManager.this.mAudioManager;
                android.media.AudioManager.setAudioPortGain(this.mAudioSource, sourceGainConfig);
            }
        }

        public void setStreamVolume(float volume) throws android.os.RemoteException {
            synchronized (this.mImplLock) {
                if (this.mReleased) {
                    throw new java.lang.IllegalStateException("Device already released.");
                }
                this.mSourceVolume = volume;
                updateAudioConfigLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean startCapture(android.view.Surface surface, android.media.tv.TvStreamConfig config) {
            synchronized (this.mImplLock) {
                if (this.mReleased) {
                    return false;
                }
                if (surface != null && config != null) {
                    if (config.getType() != 2) {
                        return false;
                    }
                    int result = com.android.server.tv.TvInputHardwareManager.this.mHal.addOrUpdateStream(this.mInfo.getDeviceId(), surface, config);
                    return result == 0;
                }
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean stopCapture(android.media.tv.TvStreamConfig config) {
            synchronized (this.mImplLock) {
                if (this.mReleased) {
                    return false;
                }
                if (config == null) {
                    return false;
                }
                int result = com.android.server.tv.TvInputHardwareManager.this.mHal.removeStream(this.mInfo.getDeviceId(), config);
                return result == 0;
            }
        }

        private boolean updateAudioSourceLocked() {
            if (this.mInfo.getAudioType() == 0) {
                return false;
            }
            android.media.AudioDevicePort previousSource = this.mAudioSource;
            this.mAudioSource = findAudioDevicePort(this.mInfo.getAudioType(), this.mInfo.getAudioAddress());
            return this.mAudioSource == null ? previousSource != null : !this.mAudioSource.equals(previousSource);
        }

        private boolean updateAudioSinkLocked() {
            if (this.mInfo.getAudioType() == 0) {
                return false;
            }
            java.util.List<android.media.AudioDevicePort> previousSink = this.mAudioSink;
            this.mAudioSink = new java.util.ArrayList();
            if (this.mOverrideAudioType == 0) {
                findAudioSinkFromAudioPolicy(this.mAudioSink);
            } else {
                android.media.AudioDevicePort audioSink = findAudioDevicePort(this.mOverrideAudioType, this.mOverrideAudioAddress);
                if (audioSink != null) {
                    this.mAudioSink.add(audioSink);
                }
            }
            if (this.mAudioSink.size() != previousSink.size()) {
                return true;
            }
            previousSink.removeAll(this.mAudioSink);
            return !previousSink.isEmpty();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleAudioSinkUpdated() {
            synchronized (this.mImplLock) {
                updateAudioConfigLocked();
            }
        }

        public void overrideAudioSink(int audioType, java.lang.String audioAddress, int samplingRate, int channelMask, int format) {
            synchronized (this.mImplLock) {
                this.mOverrideAudioType = audioType;
                this.mOverrideAudioAddress = audioAddress;
                this.mDesiredSamplingRate = samplingRate;
                this.mDesiredChannelMask = channelMask;
                this.mDesiredFormat = format;
                updateAudioConfigLocked();
            }
        }

        public void onMediaStreamVolumeChanged() {
            synchronized (this.mImplLock) {
                updateAudioConfigLocked();
            }
        }
    }

    private class ListenerHandler extends android.os.Handler {
        private static final int HARDWARE_DEVICE_ADDED = 2;
        private static final int HARDWARE_DEVICE_REMOVED = 3;
        private static final int HDMI_DEVICE_ADDED = 4;
        private static final int HDMI_DEVICE_REMOVED = 5;
        private static final int HDMI_DEVICE_UPDATED = 6;
        private static final int STATE_CHANGED = 1;
        private static final int TVINPUT_INFO_ADDED = 7;
        private static final int TV_MESSAGE_RECEIVED = 8;

        private ListenerHandler() {
        }

        @Override // android.os.Handler
        public final void handleMessage(android.os.Message msg) {
            java.lang.String inputId;
            switch (msg.what) {
                case 1:
                    java.lang.String inputId2 = (java.lang.String) msg.obj;
                    int state = msg.arg1;
                    com.android.server.tv.TvInputHardwareManager.this.mListener.onStateChanged(inputId2, state);
                    return;
                case 2:
                    android.media.tv.TvInputHardwareInfo info = (android.media.tv.TvInputHardwareInfo) msg.obj;
                    com.android.server.tv.TvInputHardwareManager.this.mListener.onHardwareDeviceAdded(info);
                    return;
                case 3:
                    android.media.tv.TvInputHardwareInfo info2 = (android.media.tv.TvInputHardwareInfo) msg.obj;
                    com.android.server.tv.TvInputHardwareManager.this.mListener.onHardwareDeviceRemoved(info2);
                    return;
                case 4:
                    android.hardware.hdmi.HdmiDeviceInfo info3 = (android.hardware.hdmi.HdmiDeviceInfo) msg.obj;
                    com.android.server.tv.TvInputHardwareManager.this.mListener.onHdmiDeviceAdded(info3);
                    return;
                case 5:
                    android.hardware.hdmi.HdmiDeviceInfo info4 = (android.hardware.hdmi.HdmiDeviceInfo) msg.obj;
                    com.android.server.tv.TvInputHardwareManager.this.mListener.onHdmiDeviceRemoved(info4);
                    return;
                case 6:
                    android.hardware.hdmi.HdmiDeviceInfo info5 = (android.hardware.hdmi.HdmiDeviceInfo) msg.obj;
                    synchronized (com.android.server.tv.TvInputHardwareManager.this.mLock) {
                        inputId = (java.lang.String) com.android.server.tv.TvInputHardwareManager.this.mHdmiInputIdMap.get(info5.getId());
                        break;
                    }
                    if (inputId != null) {
                        com.android.server.tv.TvInputHardwareManager.this.mListener.onHdmiDeviceUpdated(inputId, info5);
                        return;
                    } else {
                        android.util.Slog.w(com.android.server.tv.TvInputHardwareManager.TAG, "Could not resolve input ID matching the device info; ignoring.");
                        return;
                    }
                case 7:
                    int deviceId = msg.arg1;
                    int cableConnectionStatus = msg.arg2;
                    com.android.server.tv.TvInputHardwareManager.Connection connection = (com.android.server.tv.TvInputHardwareManager.Connection) msg.obj;
                    int previousConfigsLength = connection.getConfigsLengthLocked();
                    int previousCableConnectionStatus = connection.getInputStateLocked();
                    java.lang.String inputId3 = (java.lang.String) com.android.server.tv.TvInputHardwareManager.this.mHardwareInputIdMap.get(deviceId);
                    if (inputId3 != null) {
                        synchronized (com.android.server.tv.TvInputHardwareManager.this.mLock) {
                            if (connection.updateCableConnectionStatusLocked(cableConnectionStatus)) {
                                if (previousCableConnectionStatus != connection.getInputStateLocked()) {
                                    com.android.server.tv.TvInputHardwareManager.this.mHandler.obtainMessage(1, connection.getInputStateLocked(), 0, inputId3).sendToTarget();
                                }
                            } else {
                                if ((previousConfigsLength == 0) != (connection.getConfigsLengthLocked() == 0)) {
                                    com.android.server.tv.TvInputHardwareManager.this.mHandler.obtainMessage(1, connection.getInputStateLocked(), 0, inputId3).sendToTarget();
                                }
                            }
                            break;
                        }
                        return;
                    }
                    return;
                case 8:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    java.lang.String inputId4 = (java.lang.String) args.arg1;
                    android.os.Bundle data = (android.os.Bundle) args.arg2;
                    int type = msg.arg1;
                    com.android.server.tv.TvInputHardwareManager.this.mListener.onTvMessage(inputId4, type, data);
                    args.recycle();
                    return;
                default:
                    android.util.Slog.w(com.android.server.tv.TvInputHardwareManager.TAG, "Unhandled message: " + msg);
                    return;
            }
        }
    }

    private final class HdmiHotplugEventListener extends android.hardware.hdmi.IHdmiHotplugEventListener.Stub {
        private HdmiHotplugEventListener() {
        }

        public void onReceived(android.hardware.hdmi.HdmiHotplugEvent event) {
            int state;
            synchronized (com.android.server.tv.TvInputHardwareManager.this.mLock) {
                com.android.server.tv.TvInputHardwareManager.this.mHdmiStateMap.put(event.getPort(), event.isConnected());
                android.media.tv.TvInputHardwareInfo hardwareInfo = com.android.server.tv.TvInputHardwareManager.this.findHardwareInfoForHdmiPortLocked(event.getPort());
                if (hardwareInfo == null) {
                    return;
                }
                java.lang.String inputId = (java.lang.String) com.android.server.tv.TvInputHardwareManager.this.mHardwareInputIdMap.get(hardwareInfo.getDeviceId());
                if (inputId == null) {
                    return;
                }
                if (event.isConnected()) {
                    state = 0;
                } else {
                    state = 1;
                }
                com.android.server.tv.TvInputHardwareManager.this.mHandler.obtainMessage(1, state, 0, inputId).sendToTarget();
            }
        }
    }

    private final class HdmiDeviceEventListener extends android.hardware.hdmi.IHdmiDeviceEventListener.Stub {
        private HdmiDeviceEventListener() {
        }

        public void onStatusChanged(android.hardware.hdmi.HdmiDeviceInfo deviceInfo, int status) {
            if (deviceInfo.isSourceType()) {
                synchronized (com.android.server.tv.TvInputHardwareManager.this.mLock) {
                    int messageType = 0;
                    java.lang.Object obj = null;
                    switch (status) {
                        case 1:
                            if (findHdmiDeviceInfo(deviceInfo.getId()) == null) {
                                com.android.server.tv.TvInputHardwareManager.this.mHdmiDeviceList.add(deviceInfo);
                                messageType = 4;
                                obj = deviceInfo;
                            } else {
                                android.util.Slog.w(com.android.server.tv.TvInputHardwareManager.TAG, "The list already contains " + deviceInfo + "; ignoring.");
                                return;
                            }
                            break;
                        case 2:
                            android.hardware.hdmi.HdmiDeviceInfo originalDeviceInfo = findHdmiDeviceInfo(deviceInfo.getId());
                            if (!com.android.server.tv.TvInputHardwareManager.this.mHdmiDeviceList.remove(originalDeviceInfo)) {
                                android.util.Slog.w(com.android.server.tv.TvInputHardwareManager.TAG, "The list doesn't contain " + deviceInfo + "; ignoring.");
                                return;
                            } else {
                                messageType = 5;
                                obj = deviceInfo;
                            }
                            break;
                        case 3:
                            android.hardware.hdmi.HdmiDeviceInfo originalDeviceInfo2 = findHdmiDeviceInfo(deviceInfo.getId());
                            if (!com.android.server.tv.TvInputHardwareManager.this.mHdmiDeviceList.remove(originalDeviceInfo2)) {
                                android.util.Slog.w(com.android.server.tv.TvInputHardwareManager.TAG, "The list doesn't contain " + deviceInfo + "; ignoring.");
                                return;
                            }
                            com.android.server.tv.TvInputHardwareManager.this.mHdmiDeviceList.add(deviceInfo);
                            messageType = 6;
                            obj = deviceInfo;
                            break;
                            break;
                    }
                    android.os.Message msg = com.android.server.tv.TvInputHardwareManager.this.mHandler.obtainMessage(messageType, 0, 0, obj);
                    if (com.android.server.tv.TvInputHardwareManager.this.findHardwareInfoForHdmiPortLocked(deviceInfo.getPortId()) != null) {
                        msg.sendToTarget();
                    } else {
                        com.android.server.tv.TvInputHardwareManager.this.mPendingHdmiDeviceEvents.add(msg);
                    }
                }
            }
        }

        private android.hardware.hdmi.HdmiDeviceInfo findHdmiDeviceInfo(int id) {
            for (android.hardware.hdmi.HdmiDeviceInfo info : com.android.server.tv.TvInputHardwareManager.this.mHdmiDeviceList) {
                if (info.getId() == id) {
                    return info;
                }
            }
            return null;
        }
    }

    private final class HdmiSystemAudioModeChangeListener extends android.hardware.hdmi.IHdmiSystemAudioModeChangeListener.Stub {
        private HdmiSystemAudioModeChangeListener() {
        }

        public void onStatusChanged(boolean enabled) throws android.os.RemoteException {
            synchronized (com.android.server.tv.TvInputHardwareManager.this.mLock) {
                for (int i = 0; i < com.android.server.tv.TvInputHardwareManager.this.mConnections.size(); i++) {
                    com.android.server.tv.TvInputHardwareManager.TvInputHardwareImpl impl = ((com.android.server.tv.TvInputHardwareManager.Connection) com.android.server.tv.TvInputHardwareManager.this.mConnections.valueAt(i)).getHardwareImplLocked();
                    if (impl != null) {
                        impl.handleAudioSinkUpdated();
                    }
                }
            }
        }
    }
}
