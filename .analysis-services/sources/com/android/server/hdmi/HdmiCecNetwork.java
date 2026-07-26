package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecNetwork {
    private static final java.lang.String TAG = "HdmiCecNetwork";
    private final android.os.Handler mHandler;
    private final com.android.server.hdmi.HdmiCecController mHdmiCecController;
    private final com.android.server.hdmi.HdmiControlService mHdmiControlService;
    private final com.android.server.hdmi.HdmiMhlControllerStub mHdmiMhlController;
    protected final java.lang.Object mLock;
    private com.android.server.hdmi.UnmodifiableSparseArray<android.hardware.hdmi.HdmiDeviceInfo> mPortDeviceMap;
    private com.android.server.hdmi.UnmodifiableSparseIntArray mPortIdMap;
    private com.android.server.hdmi.UnmodifiableSparseArray<android.hardware.hdmi.HdmiPortInfo> mPortInfoMap;
    private final android.util.SparseArray<com.android.server.hdmi.HdmiCecLocalDevice> mLocalDevices = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.hardware.hdmi.HdmiDeviceInfo> mDeviceInfos = new android.util.SparseArray<>();
    private final android.util.ArraySet<java.lang.Integer> mCecSwitches = new android.util.ArraySet<>();
    private java.util.List<android.hardware.hdmi.HdmiDeviceInfo> mSafeAllDeviceInfos = java.util.Collections.emptyList();
    private java.util.List<android.hardware.hdmi.HdmiDeviceInfo> mSafeExternalInputs = java.util.Collections.emptyList();
    private java.util.List<android.hardware.hdmi.HdmiPortInfo> mPortInfo = java.util.Collections.emptyList();
    private int mPhysicalAddress = 65535;

    HdmiCecNetwork(com.android.server.hdmi.HdmiControlService hdmiControlService, com.android.server.hdmi.HdmiCecController hdmiCecController, com.android.server.hdmi.HdmiMhlControllerStub hdmiMhlController) {
        this.mHdmiControlService = hdmiControlService;
        this.mHdmiCecController = hdmiCecController;
        this.mHdmiMhlController = hdmiMhlController;
        this.mHandler = new android.os.Handler(this.mHdmiControlService.getServiceLooper());
        this.mLock = this.mHdmiControlService.getServiceLock();
    }

    private static boolean isConnectedToCecSwitch(int path, java.util.Collection<java.lang.Integer> switches) {
        java.util.Iterator<java.lang.Integer> it = switches.iterator();
        while (it.hasNext()) {
            int switchPath = it.next().intValue();
            if (isParentPath(switchPath, path)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isParentPath(int parentPath, int childPath) {
        for (int i = 0; i <= 12; i += 4) {
            int nibble = (childPath >> i) & 15;
            if (nibble != 0) {
                int parentNibble = (parentPath >> i) & 15;
                return parentNibble == 0 && (childPath >> (i + 4)) == (parentPath >> (i + 4));
            }
        }
        return false;
    }

    public void addLocalDevice(int deviceType, com.android.server.hdmi.HdmiCecLocalDevice device) {
        this.mLocalDevices.put(deviceType, device);
    }

    com.android.server.hdmi.HdmiCecLocalDevice getLocalDevice(int deviceType) {
        return this.mLocalDevices.get(deviceType);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    java.util.List<com.android.server.hdmi.HdmiCecLocalDevice> getLocalDeviceList() {
        assertRunOnServiceThread();
        return com.android.server.hdmi.HdmiUtils.sparseArrayToList(this.mLocalDevices);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isAllocatedLocalDeviceAddress(int address) {
        assertRunOnServiceThread();
        for (int i = 0; i < this.mLocalDevices.size(); i++) {
            if (this.mLocalDevices.valueAt(i).isAddressOf(address)) {
                return true;
            }
        }
        return false;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void clearLocalDevices() {
        assertRunOnServiceThread();
        this.mLocalDevices.clear();
    }

    public android.hardware.hdmi.HdmiDeviceInfo getDeviceInfo(int id) {
        return this.mDeviceInfos.get(id);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private android.hardware.hdmi.HdmiDeviceInfo addDeviceInfo(android.hardware.hdmi.HdmiDeviceInfo deviceInfo) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo oldDeviceInfo = getCecDeviceInfo(deviceInfo.getLogicalAddress());
        this.mHdmiControlService.checkLogicalAddressConflictAndReallocate(deviceInfo.getLogicalAddress(), deviceInfo.getPhysicalAddress());
        if (oldDeviceInfo != null) {
            removeDeviceInfo(deviceInfo.getId());
        }
        this.mDeviceInfos.append(deviceInfo.getId(), deviceInfo);
        updateSafeDeviceInfoList();
        return oldDeviceInfo;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private android.hardware.hdmi.HdmiDeviceInfo removeDeviceInfo(int id) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo deviceInfo = this.mDeviceInfos.get(id);
        if (deviceInfo != null) {
            this.mDeviceInfos.remove(id);
        }
        updateSafeDeviceInfoList();
        return deviceInfo;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    android.hardware.hdmi.HdmiDeviceInfo getCecDeviceInfo(int logicalAddress) {
        assertRunOnServiceThread();
        return this.mDeviceInfos.get(android.hardware.hdmi.HdmiDeviceInfo.idForCecDevice(logicalAddress));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    final void addCecDevice(android.hardware.hdmi.HdmiDeviceInfo info) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo old = addDeviceInfo(info);
        if (isLocalDeviceAddress(info.getLogicalAddress())) {
            return;
        }
        this.mHdmiControlService.checkAndUpdateAbsoluteVolumeBehavior();
        if (info.getPhysicalAddress() == 65535) {
            return;
        }
        if (old == null || old.getPhysicalAddress() == 65535) {
            invokeDeviceEventListener(info, 1);
        } else if (!old.equals(info)) {
            invokeDeviceEventListener(old, 2);
            invokeDeviceEventListener(info, 1);
        }
    }

    private void invokeDeviceEventListener(android.hardware.hdmi.HdmiDeviceInfo info, int event) {
        if (!hideDevicesBehindLegacySwitch(info)) {
            this.mHdmiControlService.invokeDeviceEventListeners(info, event);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    final void updateCecDevice(android.hardware.hdmi.HdmiDeviceInfo info) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo old = addDeviceInfo(info);
        if (info.getPhysicalAddress() == 65535) {
            return;
        }
        if (old == null || old.getPhysicalAddress() == 65535) {
            invokeDeviceEventListener(info, 1);
        } else if (!old.equals(info)) {
            invokeDeviceEventListener(info, 3);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void updateSafeDeviceInfoList() {
        assertRunOnServiceThread();
        java.util.List<android.hardware.hdmi.HdmiDeviceInfo> copiedDevices = com.android.server.hdmi.HdmiUtils.sparseArrayToList(this.mDeviceInfos);
        java.util.List<android.hardware.hdmi.HdmiDeviceInfo> externalInputs = getInputDevices();
        this.mSafeAllDeviceInfos = copiedDevices;
        this.mSafeExternalInputs = externalInputs;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getDeviceInfoList(boolean includeLocalDevice) {
        assertRunOnServiceThread();
        if (includeLocalDevice) {
            return com.android.server.hdmi.HdmiUtils.sparseArrayToList(this.mDeviceInfos);
        }
        java.util.ArrayList<android.hardware.hdmi.HdmiDeviceInfo> infoList = new java.util.ArrayList<>();
        for (int i = 0; i < this.mDeviceInfos.size(); i++) {
            android.hardware.hdmi.HdmiDeviceInfo info = this.mDeviceInfos.valueAt(i);
            if (!isLocalDeviceAddress(info.getLogicalAddress())) {
                infoList.add(info);
            }
        }
        return infoList;
    }

    java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getSafeExternalInputsLocked() {
        return this.mSafeExternalInputs;
    }

    private java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getInputDevices() {
        java.util.ArrayList<android.hardware.hdmi.HdmiDeviceInfo> infoList = new java.util.ArrayList<>();
        for (int i = 0; i < this.mDeviceInfos.size(); i++) {
            android.hardware.hdmi.HdmiDeviceInfo info = this.mDeviceInfos.valueAt(i);
            if (!isLocalDeviceAddress(info.getLogicalAddress()) && info.isSourceType() && !hideDevicesBehindLegacySwitch(info)) {
                infoList.add(info);
            }
        }
        return infoList;
    }

    private boolean hideDevicesBehindLegacySwitch(android.hardware.hdmi.HdmiDeviceInfo info) {
        return (!isLocalDeviceAddress(0) || isConnectedToCecSwitch(info.getPhysicalAddress(), getCecSwitches()) || info.getPhysicalAddress() == 65535) ? false : true;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    final void removeCecDevice(com.android.server.hdmi.HdmiCecLocalDevice localDevice, int address) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo info = removeDeviceInfo(android.hardware.hdmi.HdmiDeviceInfo.idForCecDevice(address));
        this.mHdmiControlService.checkAndUpdateAbsoluteVolumeBehavior();
        localDevice.mCecMessageCache.flushMessagesFrom(address);
        if (info.getPhysicalAddress() == 65535) {
            return;
        }
        invokeDeviceEventListener(info, 2);
    }

    public void updateDevicePowerStatus(int logicalAddress, int newPowerStatus) {
        android.hardware.hdmi.HdmiDeviceInfo info = getCecDeviceInfo(logicalAddress);
        if (info == null) {
            android.util.Slog.w(TAG, "Can not update power status of non-existing device:" + logicalAddress);
        } else {
            if (info.getDevicePowerStatus() == newPowerStatus) {
                return;
            }
            updateCecDevice(info.toBuilder().setDevicePowerStatus(newPowerStatus).build());
        }
    }

    boolean isConnectedToArcPort(int physicalAddress) {
        int portId = physicalAddressToPortId(physicalAddress);
        if (portId != -1 && portId != 0) {
            return this.mPortInfoMap.get(portId).isArcSupported();
        }
        return false;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void initPortInfo() {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiPortInfo[] cecPortInfo = null;
        if (this.mHdmiCecController != null) {
            cecPortInfo = this.mHdmiCecController.getPortInfos();
            this.mPhysicalAddress = 65535;
        }
        if (cecPortInfo == null) {
            return;
        }
        android.util.SparseArray<android.hardware.hdmi.HdmiPortInfo> portInfoMap = new android.util.SparseArray<>();
        android.util.SparseIntArray portIdMap = new android.util.SparseIntArray();
        android.util.SparseArray<android.hardware.hdmi.HdmiDeviceInfo> portDeviceMap = new android.util.SparseArray<>();
        for (android.hardware.hdmi.HdmiPortInfo info : cecPortInfo) {
            portIdMap.put(info.getAddress(), info.getId());
            portInfoMap.put(info.getId(), info);
            portDeviceMap.put(info.getId(), android.hardware.hdmi.HdmiDeviceInfo.hardwarePort(info.getAddress(), info.getId()));
        }
        this.mPortIdMap = new com.android.server.hdmi.UnmodifiableSparseIntArray(portIdMap);
        this.mPortInfoMap = new com.android.server.hdmi.UnmodifiableSparseArray<>(portInfoMap);
        this.mPortDeviceMap = new com.android.server.hdmi.UnmodifiableSparseArray<>(portDeviceMap);
        if (this.mHdmiMhlController == null) {
            return;
        }
        android.hardware.hdmi.HdmiPortInfo[] mhlPortInfo = this.mHdmiMhlController.getPortInfos();
        android.util.ArraySet<java.lang.Integer> mhlSupportedPorts = new android.util.ArraySet<>(mhlPortInfo.length);
        for (android.hardware.hdmi.HdmiPortInfo info2 : mhlPortInfo) {
            if (info2.isMhlSupported()) {
                mhlSupportedPorts.add(java.lang.Integer.valueOf(info2.getId()));
            }
        }
        if (mhlSupportedPorts.isEmpty()) {
            setPortInfo(java.util.Collections.unmodifiableList(java.util.Arrays.asList(cecPortInfo)));
            return;
        }
        java.util.ArrayList<android.hardware.hdmi.HdmiPortInfo> result = new java.util.ArrayList<>(cecPortInfo.length);
        for (android.hardware.hdmi.HdmiPortInfo info3 : cecPortInfo) {
            if (mhlSupportedPorts.contains(java.lang.Integer.valueOf(info3.getId()))) {
                result.add(new android.hardware.hdmi.HdmiPortInfo.Builder(info3.getId(), info3.getType(), info3.getAddress()).setCecSupported(info3.isCecSupported()).setMhlSupported(true).setArcSupported(info3.isArcSupported()).setEarcSupported(info3.isEarcSupported()).build());
            } else {
                result.add(info3);
            }
        }
        setPortInfo(java.util.Collections.unmodifiableList(result));
    }

    android.hardware.hdmi.HdmiDeviceInfo getDeviceForPortId(int portId) {
        return this.mPortDeviceMap.get(portId, android.hardware.hdmi.HdmiDeviceInfo.INACTIVE_DEVICE);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    boolean isInDeviceList(int logicalAddress, int physicalAddress) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo device = getCecDeviceInfo(logicalAddress);
        return device != null && device.getPhysicalAddress() == physicalAddress;
    }

    private static int logicalAddressToDeviceType(int logicalAddress) {
        switch (logicalAddress) {
            case 0:
                return 0;
            case 1:
            case 2:
            case 9:
                return 1;
            case 3:
            case 6:
            case 7:
            case 10:
                return 3;
            case 4:
            case 8:
            case 11:
                return 4;
            case 5:
                return 5;
            default:
                return 2;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void handleCecMessage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int sourceAddress = message.getSource();
        if (getCecDeviceInfo(sourceAddress) == null) {
            android.hardware.hdmi.HdmiDeviceInfo newDevice = android.hardware.hdmi.HdmiDeviceInfo.cecDeviceBuilder().setLogicalAddress(sourceAddress).setDisplayName(com.android.server.hdmi.HdmiUtils.getDefaultDeviceName(sourceAddress)).setDeviceType(logicalAddressToDeviceType(sourceAddress)).build();
            addCecDevice(newDevice);
        }
        if (message instanceof com.android.server.hdmi.ReportFeaturesMessage) {
            handleReportFeatures((com.android.server.hdmi.ReportFeaturesMessage) message);
        }
        switch (message.getOpcode()) {
            case 0:
                handleFeatureAbort(message);
                break;
            case 71:
                handleSetOsdName(message);
                break;
            case 132:
                handleReportPhysicalAddress(message);
                break;
            case 135:
                handleDeviceVendorId(message);
                break;
            case 144:
                handleReportPowerStatus(message);
                break;
            case 158:
                handleCecVersion(message);
                break;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleReportFeatures(com.android.server.hdmi.ReportFeaturesMessage message) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo currentDeviceInfo = getCecDeviceInfo(message.getSource());
        android.hardware.hdmi.HdmiDeviceInfo newDeviceInfo = currentDeviceInfo.toBuilder().setCecVersion(message.getCecVersion()).updateDeviceFeatures(message.getDeviceFeatures()).build();
        updateCecDevice(newDeviceInfo);
        this.mHdmiControlService.checkAndUpdateAbsoluteVolumeBehavior();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleFeatureAbort(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (message.getParams().length < 2) {
            return;
        }
        int originalOpcode = message.getParams()[0] & 255;
        int reason = message.getParams()[1] & 255;
        if (originalOpcode == 115) {
            int featureSupport = reason == 0 ? 0 : 2;
            android.hardware.hdmi.HdmiDeviceInfo currentDeviceInfo = getCecDeviceInfo(message.getSource());
            android.hardware.hdmi.HdmiDeviceInfo newDeviceInfo = currentDeviceInfo.toBuilder().updateDeviceFeatures(currentDeviceInfo.getDeviceFeatures().toBuilder().setSetAudioVolumeLevelSupport(featureSupport).build()).build();
            updateCecDevice(newDeviceInfo);
            this.mHdmiControlService.checkAndUpdateAbsoluteVolumeBehavior();
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleCecVersion(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int version = java.lang.Byte.toUnsignedInt(message.getParams()[0]);
        updateDeviceCecVersion(message.getSource(), version);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleReportPhysicalAddress(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int logicalAddress = message.getSource();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        int type = message.getParams()[2];
        if (updateCecSwitchInfo(logicalAddress, type, physicalAddress)) {
            return;
        }
        android.hardware.hdmi.HdmiDeviceInfo deviceInfo = getCecDeviceInfo(logicalAddress);
        if (deviceInfo == null) {
            android.util.Slog.i(TAG, "Unknown source device info for <Report Physical Address> " + message);
        } else {
            android.hardware.hdmi.HdmiDeviceInfo updatedDeviceInfo = deviceInfo.toBuilder().setPhysicalAddress(physicalAddress).setPortId(physicalAddressToPortId(physicalAddress)).setDeviceType(type).build();
            updateCecDevice(updatedDeviceInfo);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleReportPowerStatus(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int newStatus = message.getParams()[0] & 255;
        updateDevicePowerStatus(message.getSource(), newStatus);
        if (message.getDestination() == 15) {
            updateDeviceCecVersion(message.getSource(), 6);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void updateDeviceCecVersion(int logicalAddress, int hdmiCecVersion) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiDeviceInfo deviceInfo = getCecDeviceInfo(logicalAddress);
        if (deviceInfo == null) {
            android.util.Slog.w(TAG, "Can not update CEC version of non-existing device:" + logicalAddress);
        } else {
            if (deviceInfo.getCecVersion() == hdmiCecVersion) {
                return;
            }
            android.hardware.hdmi.HdmiDeviceInfo updatedDeviceInfo = deviceInfo.toBuilder().setCecVersion(hdmiCecVersion).build();
            updateCecDevice(updatedDeviceInfo);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleSetOsdName(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int logicalAddress = message.getSource();
        android.hardware.hdmi.HdmiDeviceInfo deviceInfo = getCecDeviceInfo(logicalAddress);
        if (deviceInfo == null) {
            android.util.Slog.i(TAG, "No source device info for <Set Osd Name>." + message);
            return;
        }
        try {
            java.lang.String osdName = new java.lang.String(message.getParams(), "US-ASCII");
            if (deviceInfo.getDisplayName() != null && deviceInfo.getDisplayName().equals(osdName)) {
                android.util.Slog.d(TAG, "Ignore incoming <Set Osd Name> having same osd name:" + message);
                return;
            }
            android.util.Slog.d(TAG, "Updating device OSD name from " + deviceInfo.getDisplayName() + " to " + osdName);
            android.hardware.hdmi.HdmiDeviceInfo updatedDeviceInfo = deviceInfo.toBuilder().setDisplayName(osdName).build();
            updateCecDevice(updatedDeviceInfo);
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Slog.e(TAG, "Invalid <Set Osd Name> request:" + message, e);
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void handleDeviceVendorId(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int logicalAddress = message.getSource();
        int vendorId = com.android.server.hdmi.HdmiUtils.threeBytesToInt(message.getParams());
        android.hardware.hdmi.HdmiDeviceInfo deviceInfo = getCecDeviceInfo(logicalAddress);
        if (deviceInfo == null) {
            android.util.Slog.i(TAG, "Unknown source device info for <Device Vendor ID> " + message);
        } else {
            android.hardware.hdmi.HdmiDeviceInfo updatedDeviceInfo = deviceInfo.toBuilder().setVendorId(vendorId).build();
            updateCecDevice(updatedDeviceInfo);
        }
    }

    void addCecSwitch(int physicalAddress) {
        this.mCecSwitches.add(java.lang.Integer.valueOf(physicalAddress));
    }

    public android.util.ArraySet<java.lang.Integer> getCecSwitches() {
        return this.mCecSwitches;
    }

    void removeCecSwitches(int portId) {
        java.util.Iterator<java.lang.Integer> it = this.mCecSwitches.iterator();
        while (it.hasNext()) {
            int path = it.next().intValue();
            int devicePortId = physicalAddressToPortId(path);
            if (devicePortId == portId || devicePortId == -1) {
                it.remove();
            }
        }
    }

    void removeDevicesConnectedToPort(int portId) {
        removeCecSwitches(portId);
        java.util.List<java.lang.Integer> toRemove = new java.util.ArrayList<>();
        for (int i = 0; i < this.mDeviceInfos.size(); i++) {
            int key = this.mDeviceInfos.keyAt(i);
            int physicalAddress = this.mDeviceInfos.get(key).getPhysicalAddress();
            int devicePortId = physicalAddressToPortId(physicalAddress);
            if (devicePortId == portId || devicePortId == -1) {
                toRemove.add(java.lang.Integer.valueOf(key));
            }
        }
        java.util.Iterator<java.lang.Integer> it = toRemove.iterator();
        while (it.hasNext()) {
            removeDeviceInfo(it.next().intValue());
        }
    }

    boolean updateCecSwitchInfo(int address, int type, int path) {
        if (address == 15 && type == 6) {
            this.mCecSwitches.add(java.lang.Integer.valueOf(path));
            updateSafeDeviceInfoList();
            return true;
        }
        if (type == 5) {
            this.mCecSwitches.add(java.lang.Integer.valueOf(path));
            return false;
        }
        return false;
    }

    java.util.List<android.hardware.hdmi.HdmiDeviceInfo> getSafeCecDevicesLocked() {
        java.util.ArrayList<android.hardware.hdmi.HdmiDeviceInfo> infoList = new java.util.ArrayList<>();
        for (android.hardware.hdmi.HdmiDeviceInfo info : this.mSafeAllDeviceInfos) {
            if (!isLocalDeviceAddress(info.getLogicalAddress())) {
                infoList.add(info);
            }
        }
        return infoList;
    }

    android.hardware.hdmi.HdmiDeviceInfo getSafeCecDeviceInfo(int logicalAddress) {
        for (android.hardware.hdmi.HdmiDeviceInfo info : this.mSafeAllDeviceInfos) {
            if (info.isCecDevice() && info.getLogicalAddress() == logicalAddress) {
                return info;
            }
        }
        return null;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    final android.hardware.hdmi.HdmiDeviceInfo getDeviceInfoByPath(int path) {
        assertRunOnServiceThread();
        for (android.hardware.hdmi.HdmiDeviceInfo info : getDeviceInfoList(false)) {
            if (info.getPhysicalAddress() == path) {
                return info;
            }
        }
        return null;
    }

    android.hardware.hdmi.HdmiDeviceInfo getSafeDeviceInfoByPath(int path) {
        for (android.hardware.hdmi.HdmiDeviceInfo info : this.mSafeAllDeviceInfos) {
            if (info.getPhysicalAddress() == path) {
                return info;
            }
        }
        return null;
    }

    public int getPhysicalAddress() {
        if (this.mPhysicalAddress == 65535) {
            this.mPhysicalAddress = this.mHdmiCecController.getPhysicalAddress();
        }
        return this.mPhysicalAddress;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void clear() {
        assertRunOnServiceThread();
        initPortInfo();
        clearDeviceList();
        clearLocalDevices();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void removeUnusedLocalDevices(java.util.ArrayList<com.android.server.hdmi.HdmiCecLocalDevice> allocatedDevices) {
        java.util.ArrayList<java.lang.Integer> deviceTypesToRemove = new java.util.ArrayList<>();
        for (int i = 0; i < this.mLocalDevices.size(); i++) {
            final int deviceType = this.mLocalDevices.keyAt(i);
            boolean shouldRemoveLocalDevice = allocatedDevices.stream().noneMatch(new java.util.function.Predicate() { // from class: com.android.server.hdmi.HdmiCecNetwork$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.hdmi.HdmiCecNetwork.lambda$removeUnusedLocalDevices$0(deviceType, (com.android.server.hdmi.HdmiCecLocalDevice) obj);
                }
            });
            if (shouldRemoveLocalDevice) {
                deviceTypesToRemove.add(java.lang.Integer.valueOf(deviceType));
            }
        }
        java.util.Iterator<java.lang.Integer> it = deviceTypesToRemove.iterator();
        while (it.hasNext()) {
            this.mLocalDevices.remove(it.next().intValue());
        }
    }

    static /* synthetic */ boolean lambda$removeUnusedLocalDevices$0(int deviceType, com.android.server.hdmi.HdmiCecLocalDevice localDevice) {
        return localDevice.getDeviceInfo() != null && localDevice.getDeviceInfo().getDeviceType() == deviceType;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void removeLocalDeviceWithType(int deviceType) {
        this.mLocalDevices.remove(deviceType);
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void clearDeviceList() {
        assertRunOnServiceThread();
        for (android.hardware.hdmi.HdmiDeviceInfo info : com.android.server.hdmi.HdmiUtils.sparseArrayToList(this.mDeviceInfos)) {
            if (info.getPhysicalAddress() != getPhysicalAddress() && info.getPhysicalAddress() != 65535) {
                invokeDeviceEventListener(info, 2);
            }
        }
        this.mDeviceInfos.clear();
        updateSafeDeviceInfoList();
    }

    android.hardware.hdmi.HdmiPortInfo getPortInfo(int portId) {
        return this.mPortInfoMap.get(portId, null);
    }

    int portIdToPath(int portId) {
        if (portId == 0) {
            return getPhysicalAddress();
        }
        android.hardware.hdmi.HdmiPortInfo portInfo = getPortInfo(portId);
        if (portInfo == null) {
            android.util.Slog.e(TAG, "Cannot find the port info: " + portId);
            return 65535;
        }
        return portInfo.getAddress();
    }

    int physicalAddressToPortId(int path) {
        int physicalAddress = getPhysicalAddress();
        if (path == physicalAddress) {
            return 0;
        }
        int mask = 61440;
        int finalMask = 61440;
        int maskedAddress = physicalAddress;
        while (maskedAddress != 0) {
            maskedAddress = physicalAddress & mask;
            finalMask |= mask;
            mask >>= 4;
        }
        int portAddress = path & finalMask;
        return this.mPortIdMap.get(portAddress, -1);
    }

    java.util.List<android.hardware.hdmi.HdmiPortInfo> getPortInfo() {
        return this.mPortInfo;
    }

    void setPortInfo(java.util.List<android.hardware.hdmi.HdmiPortInfo> portInfo) {
        this.mPortInfo = portInfo;
    }

    private boolean isLocalDeviceAddress(int address) {
        for (int i = 0; i < this.mLocalDevices.size(); i++) {
            int key = this.mLocalDevices.keyAt(i);
            if (this.mLocalDevices.get(key).getDeviceInfo().getLogicalAddress() == address) {
                return true;
            }
        }
        return false;
    }

    private void assertRunOnServiceThread() {
        if (android.os.Looper.myLooper() != this.mHandler.getLooper()) {
            throw new java.lang.IllegalStateException("Should run on service thread.");
        }
    }

    protected void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("HDMI CEC Network");
        pw.increaseIndent();
        com.android.server.hdmi.HdmiUtils.dumpIterable(pw, "mPortInfo:", this.mPortInfo);
        for (int i = 0; i < this.mLocalDevices.size(); i++) {
            pw.println("HdmiCecLocalDevice #" + this.mLocalDevices.keyAt(i) + ":");
            pw.increaseIndent();
            this.mLocalDevices.valueAt(i).dump(pw);
            pw.println("Active Source history:");
            pw.increaseIndent();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.concurrent.ArrayBlockingQueue<com.android.server.hdmi.HdmiCecController.Dumpable> activeSourceHistory = this.mLocalDevices.valueAt(i).getActiveSourceHistory();
            for (com.android.server.hdmi.HdmiCecController.Dumpable activeSourceEvent : activeSourceHistory) {
                activeSourceEvent.dump(pw, sdf);
            }
            pw.decreaseIndent();
            pw.decreaseIndent();
        }
        com.android.server.hdmi.HdmiUtils.dumpIterable(pw, "mDeviceInfos:", this.mSafeAllDeviceInfos);
        pw.decreaseIndent();
    }
}
