package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecLocalDeviceAudioSystem extends com.android.server.hdmi.HdmiCecLocalDeviceSource {
    private static final java.util.HashMap<java.lang.Integer, java.util.List<java.lang.Integer>> AUDIO_CODECS_MAP = mapAudioCodecWithAudioFormat();
    private static final int MAX_CHANNELS = 8;
    private static final java.lang.String SHORT_AUDIO_DESCRIPTOR_CONFIG_PATH = "/vendor/etc/sadConfig.xml";
    private static final java.lang.String TAG = "HdmiCecLocalDeviceAudioSystem";
    private static final boolean WAKE_ON_HOTPLUG = false;

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private boolean mArcEstablished;
    private boolean mArcIntentUsed;
    private final com.android.server.hdmi.DelayedMessageBuffer mDelayedMessageBuffer;
    private final java.util.HashMap<java.lang.Integer, java.lang.String> mPortIdToTvInputs;
    private boolean mSystemAudioControlFeatureEnabled;
    private final android.media.tv.TvInputManager.TvInputCallback mTvInputCallback;
    private final java.util.HashMap<java.lang.String, android.hardware.hdmi.HdmiDeviceInfo> mTvInputsToDeviceInfo;
    private java.lang.Boolean mTvSystemAudioModeSupport;

    interface TvSystemAudioModeSupportedCallback {
        void onResult(boolean z);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    public /* bridge */ /* synthetic */ java.util.concurrent.ArrayBlockingQueue getActiveSourceHistory() {
        return super.getActiveSourceHistory();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public /* bridge */ /* synthetic */ void invokeStandbyCompletedCallback(com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback standbyCompletedCallback) {
        super.invokeStandbyCompletedCallback(standbyCompletedCallback);
    }

    protected HdmiCecLocalDeviceAudioSystem(com.android.server.hdmi.HdmiControlService service) {
        super(service, 5);
        this.mTvSystemAudioModeSupport = null;
        this.mArcEstablished = false;
        this.mArcIntentUsed = ((java.lang.String) android.sysprop.HdmiProperties.arc_port().orElse("0")).contains("tvinput");
        this.mPortIdToTvInputs = new java.util.HashMap<>();
        this.mTvInputsToDeviceInfo = new java.util.HashMap<>();
        this.mDelayedMessageBuffer = new com.android.server.hdmi.DelayedMessageBuffer(this);
        this.mTvInputCallback = new android.media.tv.TvInputManager.TvInputCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.1
            @Override // android.media.tv.TvInputManager.TvInputCallback
            public void onInputAdded(java.lang.String inputId) {
                com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.addOrUpdateTvInput(inputId);
            }

            @Override // android.media.tv.TvInputManager.TvInputCallback
            public void onInputRemoved(java.lang.String inputId) {
                com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.removeTvInput(inputId);
            }

            @Override // android.media.tv.TvInputManager.TvInputCallback
            public void onInputUpdated(java.lang.String inputId) {
                com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.addOrUpdateTvInput(inputId);
            }
        };
        this.mRoutingControlFeatureEnabled = this.mService.getHdmiCecConfig().getIntValue("routing_control") == 1;
        this.mSystemAudioControlFeatureEnabled = this.mService.getHdmiCecConfig().getIntValue("system_audio_control") == 1;
        this.mStandbyHandler = new com.android.server.hdmi.HdmiCecStandbyModeHandler(service, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void addOrUpdateTvInput(java.lang.String inputId) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            android.media.tv.TvInputInfo tvInfo = this.mService.getTvInputManager().getTvInputInfo(inputId);
            if (tvInfo == null) {
                return;
            }
            android.hardware.hdmi.HdmiDeviceInfo info = tvInfo.getHdmiDeviceInfo();
            if (info == null) {
                return;
            }
            this.mPortIdToTvInputs.put(java.lang.Integer.valueOf(info.getPortId()), inputId);
            this.mTvInputsToDeviceInfo.put(inputId, info);
            if (info.isCecDevice()) {
                processDelayedActiveSource(info.getLogicalAddress());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    public void removeTvInput(java.lang.String inputId) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            if (this.mTvInputsToDeviceInfo.get(inputId) == null) {
                return;
            }
            int portId = this.mTvInputsToDeviceInfo.get(inputId).getPortId();
            this.mPortIdToTvInputs.remove(java.lang.Integer.valueOf(portId));
            this.mTvInputsToDeviceInfo.remove(inputId);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected boolean isInputReady(int portId) {
        assertRunOnServiceThread();
        java.lang.String tvInputId = this.mPortIdToTvInputs.get(java.lang.Integer.valueOf(portId));
        android.hardware.hdmi.HdmiDeviceInfo info = this.mTvInputsToDeviceInfo.get(tvInputId);
        return info != null;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected android.hardware.hdmi.DeviceFeatures computeDeviceFeatures() {
        return android.hardware.hdmi.DeviceFeatures.NO_FEATURES_SUPPORTED.toBuilder().setArcRxSupport(android.os.SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true) ? 1 : 0).build();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onHotplug(int portId, boolean connected) {
        assertRunOnServiceThread();
        android.hardware.hdmi.HdmiPortInfo portInfo = this.mService.getPortInfo(portId);
        if (portInfo != null && portInfo.getType() == 1) {
            this.mCecMessageCache.flushAll();
            if (!connected) {
                if (isSystemAudioActivated()) {
                    this.mTvSystemAudioModeSupport = null;
                    checkSupportAndSetSystemAudioMode(false);
                }
                if (isArcEnabled()) {
                    setArcStatus(false);
                    return;
                }
                return;
            }
            return;
        }
        if (!connected && this.mPortIdToTvInputs.get(java.lang.Integer.valueOf(portId)) != null) {
            java.lang.String tvInputId = this.mPortIdToTvInputs.get(java.lang.Integer.valueOf(portId));
            android.hardware.hdmi.HdmiDeviceInfo info = this.mTvInputsToDeviceInfo.get(tvInputId);
            if (info == null) {
                return;
            }
            this.mService.getHdmiCecNetwork().removeCecDevice(this, info.getLogicalAddress());
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void disableDevice(boolean initiatedByCec, com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback callback) {
        terminateAudioReturnChannel();
        super.disableDevice(initiatedByCec, callback);
        assertRunOnServiceThread();
        this.mService.unregisterTvInputCallback(this.mTvInputCallback);
        removeAllActions();
        checkIfPendingActionsCleared();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onStandby(boolean initiatedByCec, int standbyAction, com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback) {
        assertRunOnServiceThread();
        this.mService.setActiveSource(-1, 65535, "HdmiCecLocalDeviceAudioSystem#onStandby()");
        this.mTvSystemAudioModeSupport = null;
        synchronized (this.mLock) {
            this.mService.writeStringSystemProperty("persist.sys.hdmi.last_system_audio_control", isSystemAudioActivated() ? "true" : "false");
        }
        terminateSystemAudioMode(callback);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onAddressAllocated(int logicalAddress, int reason) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiControlService hdmiControlService = this.mService;
        if (reason == 0) {
            this.mService.setAndBroadcastActiveSource(this.mService.getPhysicalAddress(), getDeviceInfo().getDeviceType(), 15, "HdmiCecLocalDeviceAudioSystem#onAddressAllocated()");
        }
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress(), this.mDeviceType));
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildDeviceVendorIdCommand(getDeviceInfo().getLogicalAddress(), this.mService.getVendorId()));
        this.mService.registerTvInputCallback(this.mTvInputCallback);
        initArcOnFromAvr();
        if (!this.mService.isScreenOff()) {
            int systemAudioControlOnPowerOnProp = android.os.SystemProperties.getInt("persist.sys.hdmi.system_audio_control_on_power_on", 0);
            boolean lastSystemAudioControlStatus = android.os.SystemProperties.getBoolean("persist.sys.hdmi.last_system_audio_control", true);
            systemAudioControlOnPowerOn(systemAudioControlOnPowerOnProp, lastSystemAudioControlStatus);
        }
        this.mService.getHdmiCecNetwork().clearDeviceList();
        launchDeviceDiscovery();
        startQueuedActions();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findKeyReceiverAddress() {
        if (getActiveSource().isValid()) {
            return getActiveSource().logicalAddress;
        }
        return -1;
    }

    protected void systemAudioControlOnPowerOn(int systemAudioOnPowerOnProp, boolean lastSystemAudioControlStatus) {
        if (systemAudioOnPowerOnProp == 0 || (systemAudioOnPowerOnProp == 1 && lastSystemAudioControlStatus && isSystemAudioControlFeatureEnabled())) {
            if (hasAction(com.android.server.hdmi.SystemAudioInitiationActionFromAvr.class)) {
                android.util.Slog.i(TAG, "SystemAudioInitiationActionFromAvr is in progress. Restarting.");
                removeAction(com.android.server.hdmi.SystemAudioInitiationActionFromAvr.class);
            }
            addAndStartAction(new com.android.server.hdmi.SystemAudioInitiationActionFromAvr(this));
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int getPreferredAddress() {
        assertRunOnServiceThread();
        return android.os.SystemProperties.getInt("persist.sys.hdmi.addr.audiosystem", 15);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void setPreferredAddress(int addr) {
        assertRunOnServiceThread();
        this.mService.writeStringSystemProperty("persist.sys.hdmi.addr.audiosystem", java.lang.String.valueOf(addr));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void processDelayedActiveSource(int address) {
        assertRunOnServiceThread();
        this.mDelayedMessageBuffer.processActiveSource(address);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleActiveSource(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int logicalAddress = message.getSource();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        if (com.android.server.hdmi.HdmiUtils.getLocalPortFromPhysicalAddress(physicalAddress, this.mService.getPhysicalAddress()) == -1) {
            return super.handleActiveSource(message);
        }
        android.hardware.hdmi.HdmiDeviceInfo info = this.mService.getHdmiCecNetwork().getCecDeviceInfo(logicalAddress);
        if (info == null) {
            com.android.server.hdmi.HdmiLogger.debug("Device info %X not found; buffering the command", java.lang.Integer.valueOf(logicalAddress));
            this.mDelayedMessageBuffer.add(message);
        } else if (!isInputReady(info.getPortId())) {
            com.android.server.hdmi.HdmiLogger.debug("Input not ready for device: %X; buffering the command", java.lang.Integer.valueOf(info.getId()));
            this.mDelayedMessageBuffer.add(message);
        } else {
            this.mDelayedMessageBuffer.removeActiveSource();
            return super.handleActiveSource(message);
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleInitiateArc(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("HdmiCecLocalDeviceAudioSystemStub handleInitiateArc", new java.lang.Object[0]);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleReportArcInitiate(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleReportArcTermination(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        processArcTermination();
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGiveAudioStatus(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (isSystemAudioControlFeatureEnabled() && this.mService.getHdmiCecVolumeControl() == 1) {
            reportAudioStatus(message.getSource());
            return -1;
        }
        return 4;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleGiveSystemAudioModeStatus(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        boolean isSystemAudioModeOnOrTurningOn = isSystemAudioActivated();
        if (!isSystemAudioModeOnOrTurningOn && message.getSource() == 0 && hasAction(com.android.server.hdmi.SystemAudioInitiationActionFromAvr.class)) {
            isSystemAudioModeOnOrTurningOn = true;
        }
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportSystemAudioMode(getDeviceInfo().getLogicalAddress(), message.getSource(), isSystemAudioModeOnOrTurningOn));
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestArcInitiate(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        removeAction(com.android.server.hdmi.ArcInitiationActionFromAvr.class);
        if (!this.mService.readBooleanSystemProperty("persist.sys.hdmi.property_arc_support", true)) {
            return 0;
        }
        if (!isDirectConnectToTv()) {
            com.android.server.hdmi.HdmiLogger.debug("AVR device is not directly connected with TV", new java.lang.Object[0]);
            return 1;
        }
        addAndStartAction(new com.android.server.hdmi.ArcInitiationActionFromAvr(this));
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestArcTermination(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!android.os.SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true)) {
            return 0;
        }
        if (!isArcEnabled()) {
            com.android.server.hdmi.HdmiLogger.debug("ARC is not established between TV and AVR device", new java.lang.Object[0]);
            return 1;
        }
        if (!getActions(com.android.server.hdmi.ArcTerminationActionFromAvr.class).isEmpty() && !((com.android.server.hdmi.ArcTerminationActionFromAvr) getActions(com.android.server.hdmi.ArcTerminationActionFromAvr.class).get(0)).mCallbacks.isEmpty()) {
            android.hardware.hdmi.IHdmiControlCallback callback = ((com.android.server.hdmi.ArcTerminationActionFromAvr) getActions(com.android.server.hdmi.ArcTerminationActionFromAvr.class).get(0)).mCallbacks.get(0);
            removeAction(com.android.server.hdmi.ArcTerminationActionFromAvr.class);
            addAndStartAction(new com.android.server.hdmi.ArcTerminationActionFromAvr(this, callback));
            return -1;
        }
        removeAction(com.android.server.hdmi.ArcTerminationActionFromAvr.class);
        addAndStartAction(new com.android.server.hdmi.ArcTerminationActionFromAvr(this));
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRequestShortAudioDescriptor(com.android.server.hdmi.HdmiCecMessage message) {
        byte[] sadBytes;
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("HdmiCecLocalDeviceAudioSystemStub handleRequestShortAudioDescriptor", new java.lang.Object[0]);
        if (!isSystemAudioControlFeatureEnabled()) {
            return 4;
        }
        if (!isSystemAudioActivated()) {
            return 1;
        }
        java.util.List<com.android.server.hdmi.HdmiUtils.DeviceConfig> config = null;
        java.io.File file = new java.io.File(SHORT_AUDIO_DESCRIPTOR_CONFIG_PATH);
        if (file.exists()) {
            try {
                java.io.InputStream in = new java.io.FileInputStream(file);
                config = com.android.server.hdmi.HdmiUtils.ShortAudioDescriptorXmlParser.parse(in);
                in.close();
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Error reading file: " + file, e);
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                android.util.Slog.e(TAG, "Unable to parse file: " + file, e2);
            }
        }
        int[] audioCodecs = parseAudioCodecs(message.getParams());
        if (config != null && config.size() > 0) {
            sadBytes = getSupportedShortAudioDescriptorsFromConfig(config, audioCodecs);
        } else {
            android.media.AudioDeviceInfo deviceInfo = getSystemAudioDeviceInfo();
            if (deviceInfo == null) {
                return 5;
            }
            sadBytes = getSupportedShortAudioDescriptors(deviceInfo, audioCodecs);
        }
        if (sadBytes.length == 0) {
            return 3;
        }
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportShortAudioDescriptor(getDeviceInfo().getLogicalAddress(), message.getSource(), sadBytes));
        return -1;
    }

    byte[] getSupportedShortAudioDescriptors(android.media.AudioDeviceInfo deviceInfo, int[] audioCodecs) {
        java.util.ArrayList<byte[]> sads = new java.util.ArrayList<>(audioCodecs.length);
        for (int audioCodec : audioCodecs) {
            byte[] sad = getSupportedShortAudioDescriptor(deviceInfo, audioCodec);
            if (sad != null) {
                if (sad.length == 3) {
                    sads.add(sad);
                } else {
                    com.android.server.hdmi.HdmiLogger.warning("Dropping Short Audio Descriptor with length %d for requested codec %x", java.lang.Integer.valueOf(sad.length), java.lang.Integer.valueOf(audioCodec));
                }
            }
        }
        return getShortAudioDescriptorBytes(sads);
    }

    private byte[] getSupportedShortAudioDescriptorsFromConfig(java.util.List<com.android.server.hdmi.HdmiUtils.DeviceConfig> deviceConfig, int[] audioCodecs) {
        byte[] sad;
        com.android.server.hdmi.HdmiUtils.DeviceConfig deviceConfigToUse = null;
        java.lang.String audioDeviceName = android.os.SystemProperties.get("persist.sys.hdmi.property_sytem_audio_mode_audio_port", "VX_AUDIO_DEVICE_IN_HDMI_ARC");
        java.util.Iterator<com.android.server.hdmi.HdmiUtils.DeviceConfig> it = deviceConfig.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.hdmi.HdmiUtils.DeviceConfig device = it.next();
            if (device.name.equals(audioDeviceName)) {
                deviceConfigToUse = device;
                break;
            }
        }
        if (deviceConfigToUse == null) {
            android.util.Slog.w(TAG, "sadConfig.xml does not have required device info for " + audioDeviceName);
            return new byte[0];
        }
        java.util.HashMap<java.lang.Integer, byte[]> map = new java.util.HashMap<>();
        java.util.ArrayList<byte[]> sads = new java.util.ArrayList<>(audioCodecs.length);
        for (com.android.server.hdmi.HdmiUtils.CodecSad codecSad : deviceConfigToUse.supportedCodecs) {
            map.put(java.lang.Integer.valueOf(codecSad.audioCodec), codecSad.sad);
        }
        for (int i = 0; i < audioCodecs.length; i++) {
            if (map.containsKey(java.lang.Integer.valueOf(audioCodecs[i])) && (sad = map.get(java.lang.Integer.valueOf(audioCodecs[i]))) != null && sad.length == 3) {
                sads.add(sad);
            }
        }
        return getShortAudioDescriptorBytes(sads);
    }

    private byte[] getShortAudioDescriptorBytes(java.util.ArrayList<byte[]> sads) {
        byte[] bytes = new byte[sads.size() * 3];
        int index = 0;
        for (byte[] sad : sads) {
            java.lang.System.arraycopy(sad, 0, bytes, index, 3);
            index += 3;
        }
        return bytes;
    }

    byte[] getSupportedShortAudioDescriptor(android.media.AudioDeviceInfo deviceInfo, int audioCodec) {
        byte[] shortAudioDescriptor = new byte[3];
        int[] deviceSupportedAudioFormats = deviceInfo.getEncodings();
        if (!AUDIO_CODECS_MAP.containsKey(java.lang.Integer.valueOf(audioCodec)) || deviceSupportedAudioFormats.length == 0) {
            return null;
        }
        java.util.List<java.lang.Integer> audioCodecSupportedAudioFormats = AUDIO_CODECS_MAP.get(java.lang.Integer.valueOf(audioCodec));
        for (int supportedAudioFormat : deviceSupportedAudioFormats) {
            if (audioCodecSupportedAudioFormats.contains(java.lang.Integer.valueOf(supportedAudioFormat))) {
                shortAudioDescriptor[0] = getFirstByteOfSAD(deviceInfo, audioCodec);
                shortAudioDescriptor[1] = getSecondByteOfSAD(deviceInfo);
                switch (audioCodec) {
                    case 0:
                        return null;
                    case 1:
                        if (supportedAudioFormat == 2) {
                            shortAudioDescriptor[2] = 1;
                        } else if (supportedAudioFormat == 21) {
                            shortAudioDescriptor[2] = 4;
                        } else {
                            shortAudioDescriptor[2] = 0;
                        }
                        return shortAudioDescriptor;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        shortAudioDescriptor[2] = getThirdSadByteForCodecs2Through8(deviceInfo);
                        return shortAudioDescriptor;
                    case 8:
                    case 9:
                    default:
                        return null;
                    case 10:
                    case 11:
                    case 12:
                        shortAudioDescriptor[2] = 0;
                        return shortAudioDescriptor;
                }
            }
        }
        return null;
    }

    private static java.util.HashMap<java.lang.Integer, java.util.List<java.lang.Integer>> mapAudioCodecWithAudioFormat() {
        java.util.HashMap<java.lang.Integer, java.util.List<java.lang.Integer>> audioCodecsMap = new java.util.HashMap<>();
        audioCodecsMap.put(0, java.util.List.of(1));
        audioCodecsMap.put(1, java.util.List.of(3, 2, 4, 21, 22));
        audioCodecsMap.put(2, java.util.List.of(5));
        audioCodecsMap.put(3, java.util.List.of(11));
        audioCodecsMap.put(5, java.util.List.of(12));
        audioCodecsMap.put(4, java.util.List.of(9));
        audioCodecsMap.put(6, java.util.List.of(10));
        audioCodecsMap.put(7, java.util.List.of(7));
        audioCodecsMap.put(10, java.util.List.of(6, 18));
        audioCodecsMap.put(11, java.util.List.of(8));
        audioCodecsMap.put(12, java.util.List.of(14, 19));
        return audioCodecsMap;
    }

    private byte getFirstByteOfSAD(android.media.AudioDeviceInfo deviceInfo, int audioCodec) {
        int maxNumberOfChannels = getMaxNumberOfChannels(deviceInfo);
        byte firstByte = (byte) ((maxNumberOfChannels - 1) | 0);
        return (byte) ((audioCodec << 3) | firstByte);
    }

    private byte getSecondByteOfSAD(android.media.AudioDeviceInfo deviceInfo) {
        java.util.ArrayList<java.lang.Integer> samplingRates = new java.util.ArrayList<>(java.util.Arrays.asList(32, 44, 48, 88, 96, 176, 192));
        int[] samplingRatesDeviceSupports = deviceInfo.getSampleRates();
        if (samplingRatesDeviceSupports.length == 0) {
            android.util.Slog.e(TAG, "Device supports arbitrary rates");
            return (byte) 127;
        }
        byte secondByte = 0;
        for (int supportedSampleRate : samplingRatesDeviceSupports) {
            if (samplingRates.contains(java.lang.Integer.valueOf(supportedSampleRate))) {
                int index = samplingRates.indexOf(java.lang.Integer.valueOf(supportedSampleRate));
                secondByte = (byte) ((1 << index) | secondByte);
            }
        }
        return secondByte;
    }

    private int getMaxNumberOfChannels(android.media.AudioDeviceInfo deviceInfo) {
        int[] channelCounts = deviceInfo.getChannelCounts();
        if (channelCounts.length == 0) {
            return 8;
        }
        int maxNumberOfChannels = channelCounts[channelCounts.length - 1];
        return maxNumberOfChannels <= 8 ? maxNumberOfChannels : 8;
    }

    private byte getThirdSadByteForCodecs2Through8(android.media.AudioDeviceInfo deviceInfo) {
        int maxSamplingRate = 0;
        int[] samplingRatesDeviceSupports = deviceInfo.getSampleRates();
        if (samplingRatesDeviceSupports.length == 0) {
            maxSamplingRate = 192;
        } else {
            for (int sampleRate : samplingRatesDeviceSupports) {
                if (maxSamplingRate < sampleRate) {
                    maxSamplingRate = sampleRate;
                }
            }
        }
        return (byte) (maxSamplingRate / 8);
    }

    private android.media.AudioDeviceInfo getSystemAudioDeviceInfo() {
        android.media.AudioManager audioManager = (android.media.AudioManager) this.mService.getContext().getSystemService(android.media.AudioManager.class);
        if (audioManager == null) {
            com.android.server.hdmi.HdmiLogger.error("Error getting system audio device because AudioManager not available.", new java.lang.Object[0]);
            return null;
        }
        android.media.AudioDeviceInfo[] devices = audioManager.getDevices(1);
        com.android.server.hdmi.HdmiLogger.debug("Found %d audio input devices", java.lang.Integer.valueOf(devices.length));
        for (android.media.AudioDeviceInfo device : devices) {
            com.android.server.hdmi.HdmiLogger.debug("%s at port %s", device.getProductName(), device.getPort());
            com.android.server.hdmi.HdmiLogger.debug("Supported encodings are %s", java.util.Arrays.stream(device.getEncodings()).mapToObj(new java.util.function.IntFunction() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem$$ExternalSyntheticLambda0
                @Override // java.util.function.IntFunction
                public final java.lang.Object apply(int i) {
                    return android.media.AudioFormat.toLogFriendlyEncoding(i);
                }
            }).collect(java.util.stream.Collectors.joining(", ")));
            if (device.getType() == 10) {
                return device;
            }
        }
        return null;
    }

    private int[] parseAudioCodecs(byte[] params) {
        int[] audioCodecs = new int[params.length];
        for (int i = 0; i < params.length; i++) {
            byte val = params[i];
            audioCodecs[i] = (val < 1 || val > 15) ? (byte) 0 : val;
        }
        return audioCodecs;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSystemAudioModeRequest(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        boolean systemAudioStatusOn = message.getParams().length != 0;
        if (message.getSource() != 0) {
            if (systemAudioStatusOn) {
                return handleSystemAudioModeOnFromNonTvDevice(message);
            }
        } else {
            setTvSystemAudioModeSupport(true);
        }
        if (!checkSupportAndSetSystemAudioMode(systemAudioStatusOn)) {
            return 4;
        }
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(getDeviceInfo().getLogicalAddress(), 15, systemAudioStatusOn));
        if (systemAudioStatusOn) {
            int sourcePhysicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
            if (com.android.server.hdmi.HdmiUtils.getLocalPortFromPhysicalAddress(sourcePhysicalAddress, getDeviceInfo().getPhysicalAddress()) != -1) {
                return -1;
            }
            android.hardware.hdmi.HdmiDeviceInfo safeDeviceInfoByPath = this.mService.getHdmiCecNetwork().getSafeDeviceInfoByPath(sourcePhysicalAddress);
            if (safeDeviceInfoByPath == null) {
                switchInputOnReceivingNewActivePath(sourcePhysicalAddress);
            }
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSetSystemAudioMode(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!checkSupportAndSetSystemAudioMode(com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(message))) {
            return 4;
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSystemAudioModeStatus(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (!checkSupportAndSetSystemAudioMode(com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(message))) {
            return 4;
        }
        return -1;
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setArcStatus(boolean enabled) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiLogger.debug("Set Arc Status[old:%b new:%b]", java.lang.Boolean.valueOf(this.mArcEstablished), java.lang.Boolean.valueOf(enabled));
        enableAudioReturnChannel(enabled);
        notifyArcStatusToAudioService(enabled);
        this.mArcEstablished = enabled;
    }

    void processArcTermination() {
        setArcStatus(false);
        if (getLocalActivePort() == 17) {
            routeToInputFromPortId(getRoutingPort());
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void enableAudioReturnChannel(boolean enabled) {
        assertRunOnServiceThread();
        this.mService.enableAudioReturnChannel(java.lang.Integer.parseInt((java.lang.String) android.sysprop.HdmiProperties.arc_port().orElse("0")), enabled);
    }

    private void notifyArcStatusToAudioService(boolean z) {
        this.mService.getAudioManager().setWiredDeviceConnectionState(-2013265920, z ? 1 : 0, "", "");
    }

    void reportAudioStatus(int source) {
        assertRunOnServiceThread();
        if (this.mService.getHdmiCecVolumeControl() == 0) {
            return;
        }
        int volume = this.mService.getAudioManager().getStreamVolume(3);
        boolean mute = this.mService.getAudioManager().isStreamMute(3);
        int maxVolume = this.mService.getAudioManager().getStreamMaxVolume(3);
        int minVolume = this.mService.getAudioManager().getStreamMinVolume(3);
        int scaledVolume = com.android.server.hdmi.VolumeControlAction.scaleToCecVolume(volume, maxVolume);
        com.android.server.hdmi.HdmiLogger.debug("Reporting volume %d (%d-%d) as CEC volume %d", java.lang.Integer.valueOf(volume), java.lang.Integer.valueOf(minVolume), java.lang.Integer.valueOf(maxVolume), java.lang.Integer.valueOf(scaledVolume));
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportAudioStatus(getDeviceInfo().getLogicalAddress(), source, scaledVolume, mute));
    }

    protected boolean checkSupportAndSetSystemAudioMode(boolean newSystemAudioMode) {
        if (!isSystemAudioControlFeatureEnabled()) {
            com.android.server.hdmi.HdmiLogger.debug("Cannot turn " + (newSystemAudioMode ? kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON : kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF) + "system audio mode because the System Audio Control feature is disabled.", new java.lang.Object[0]);
            return false;
        }
        com.android.server.hdmi.HdmiLogger.debug("System Audio Mode change[old:%b new:%b]", java.lang.Boolean.valueOf(isSystemAudioActivated()), java.lang.Boolean.valueOf(newSystemAudioMode));
        if (newSystemAudioMode) {
            this.mService.wakeUp();
        }
        setSystemAudioMode(newSystemAudioMode);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSystemAudioMode(boolean newSystemAudioMode) {
        int i;
        int targetPhysicalAddress = getActiveSource().physicalAddress;
        int port = this.mService.pathToPortId(targetPhysicalAddress);
        if (newSystemAudioMode && port >= 0) {
            switchToAudioInput();
        }
        boolean systemAudioModeMutingEnabled = this.mService.getHdmiCecConfig().getIntValue("system_audio_mode_muting") == 1;
        boolean currentMuteStatus = this.mService.getAudioManager().isStreamMute(3);
        if (currentMuteStatus == newSystemAudioMode && (systemAudioModeMutingEnabled || newSystemAudioMode)) {
            com.android.server.hdmi.AudioManagerWrapper audioManager = this.mService.getAudioManager();
            if (newSystemAudioMode) {
                i = 100;
            } else {
                i = -100;
            }
            audioManager.adjustStreamVolume(3, i, 0);
        }
        updateAudioManagerForSystemAudio(newSystemAudioMode);
        synchronized (this.mLock) {
            if (isSystemAudioActivated() != newSystemAudioMode) {
                this.mService.setSystemAudioActivated(newSystemAudioMode);
                this.mService.announceSystemAudioModeChange(newSystemAudioMode);
            }
        }
        if (this.mArcIntentUsed && !systemAudioModeMutingEnabled && !newSystemAudioMode && getLocalActivePort() == 17) {
            routeToInputFromPortId(getRoutingPort());
        }
        if (android.os.SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true) && isDirectConnectToTv() && this.mService.isSystemAudioActivated() && !hasAction(com.android.server.hdmi.ArcInitiationActionFromAvr.class)) {
            addAndStartAction(new com.android.server.hdmi.ArcInitiationActionFromAvr(this));
        }
    }

    protected void switchToAudioInput() {
    }

    protected boolean isDirectConnectToTv() {
        int myPhysicalAddress = this.mService.getPhysicalAddress();
        return (61440 & myPhysicalAddress) == myPhysicalAddress;
    }

    private void updateAudioManagerForSystemAudio(boolean on) {
        int device = this.mService.getAudioManager().setHdmiSystemAudioSupported(on);
        com.android.server.hdmi.HdmiLogger.debug("[A]UpdateSystemAudio mode[on=%b] output=[%X]", java.lang.Boolean.valueOf(on), java.lang.Integer.valueOf(device));
    }

    void onSystemAudioControlFeatureSupportChanged(boolean enabled) {
        setSystemAudioControlFeatureEnabled(enabled);
        if (enabled) {
            if (hasAction(com.android.server.hdmi.SystemAudioInitiationActionFromAvr.class)) {
                android.util.Slog.i(TAG, "SystemAudioInitiationActionFromAvr is in progress. Restarting.");
                removeAction(com.android.server.hdmi.SystemAudioInitiationActionFromAvr.class);
            }
            addAndStartAction(new com.android.server.hdmi.SystemAudioInitiationActionFromAvr(this));
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setSystemAudioControlFeatureEnabled(boolean enabled) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mSystemAudioControlFeatureEnabled = enabled;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void setRoutingControlFeatureEnabled(boolean enabled) {
        assertRunOnServiceThread();
        synchronized (this.mLock) {
            this.mRoutingControlFeatureEnabled = enabled;
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void doManualPortSwitching(int portId, android.hardware.hdmi.IHdmiControlCallback callback) {
        int oldPath;
        assertRunOnServiceThread();
        if (!this.mService.isValidPortId(portId)) {
            invokeCallback(callback, 3);
            return;
        }
        if (portId == getLocalActivePort()) {
            invokeCallback(callback, 0);
            return;
        }
        if (!this.mService.isCecControlEnabled()) {
            setRoutingPort(portId);
            setLocalActivePort(portId);
            invokeCallback(callback, 6);
            return;
        }
        if (getRoutingPort() != 0) {
            oldPath = this.mService.portIdToPath(getRoutingPort());
        } else {
            oldPath = getDeviceInfo().getPhysicalAddress();
        }
        int newPath = this.mService.portIdToPath(portId);
        if (oldPath == newPath) {
            return;
        }
        setRoutingPort(portId);
        setLocalActivePort(portId);
        com.android.server.hdmi.HdmiCecMessage routingChange = com.android.server.hdmi.HdmiCecMessageBuilder.buildRoutingChange(getDeviceInfo().getLogicalAddress(), oldPath, newPath);
        this.mService.sendCecCommand(routingChange);
    }

    boolean isSystemAudioControlFeatureEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSystemAudioControlFeatureEnabled;
        }
        return z;
    }

    protected boolean isSystemAudioActivated() {
        return this.mService.isSystemAudioActivated();
    }

    protected void terminateSystemAudioMode() {
        terminateSystemAudioMode(null);
    }

    protected void terminateSystemAudioMode(final com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback) {
        removeAction(com.android.server.hdmi.SystemAudioInitiationActionFromAvr.class);
        if (!isSystemAudioActivated()) {
            invokeStandbyCompletedCallback(callback);
        } else if (checkSupportAndSetSystemAudioMode(false)) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(getDeviceInfo().getLogicalAddress(), 15, false), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.2
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int error) {
                    com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.invokeStandbyCompletedCallback(callback);
                }
            });
        }
    }

    private void terminateAudioReturnChannel() {
        removeAction(com.android.server.hdmi.ArcInitiationActionFromAvr.class);
        if (!isArcEnabled() || !this.mService.readBooleanSystemProperty("persist.sys.hdmi.property_arc_support", true)) {
            return;
        }
        addAndStartAction(new com.android.server.hdmi.ArcTerminationActionFromAvr(this));
    }

    void queryTvSystemAudioModeSupport(com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback callback) {
        if (this.mTvSystemAudioModeSupport == null) {
            addAndStartAction(new com.android.server.hdmi.DetectTvSystemAudioModeSupportAction(this, callback));
        } else {
            callback.onResult(this.mTvSystemAudioModeSupport.booleanValue());
        }
    }

    int handleSystemAudioModeOnFromNonTvDevice(final com.android.server.hdmi.HdmiCecMessage message) {
        if (!isSystemAudioControlFeatureEnabled()) {
            com.android.server.hdmi.HdmiLogger.debug("Cannot turn onsystem audio mode because the System Audio Control feature is disabled.", new java.lang.Object[0]);
            return 4;
        }
        this.mService.wakeUp();
        if (this.mService.pathToPortId(getActiveSource().physicalAddress) != -1) {
            setSystemAudioMode(true);
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(getDeviceInfo().getLogicalAddress(), 15, true));
            return -1;
        }
        queryTvSystemAudioModeSupport(new com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.3
            @Override // com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.TvSystemAudioModeSupportedCallback
            public void onResult(boolean supported) {
                if (supported) {
                    com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.setSystemAudioMode(true);
                    com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildSetSystemAudioMode(com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.getDeviceInfo().getLogicalAddress(), 15, true));
                } else {
                    com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.mService.maySendFeatureAbortCommand(message, 4);
                }
            }
        });
        return -1;
    }

    void setTvSystemAudioModeSupport(boolean supported) {
        this.mTvSystemAudioModeSupport = java.lang.Boolean.valueOf(supported);
    }

    protected boolean isArcEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mArcEstablished;
        }
        return z;
    }

    private void initArcOnFromAvr() {
        removeAction(com.android.server.hdmi.ArcTerminationActionFromAvr.class);
        if (android.os.SystemProperties.getBoolean("persist.sys.hdmi.property_arc_support", true) && isDirectConnectToTv() && !isArcEnabled()) {
            removeAction(com.android.server.hdmi.ArcInitiationActionFromAvr.class);
            addAndStartAction(new com.android.server.hdmi.ArcInitiationActionFromAvr(this));
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource
    protected void switchInputOnReceivingNewActivePath(int physicalAddress) {
        int port = this.mService.pathToPortId(physicalAddress);
        if (isSystemAudioActivated() && port < 0) {
            routeToInputFromPortId(17);
        } else if (this.mIsSwitchDevice && port >= 0) {
            routeToInputFromPortId(port);
        }
    }

    protected void routeToInputFromPortId(int portId) {
        if (!isRoutingControlFeatureEnabled()) {
            com.android.server.hdmi.HdmiLogger.debug("Routing Control Feature is not enabled.", new java.lang.Object[0]);
        } else if (this.mArcIntentUsed) {
            routeToTvInputFromPortId(portId);
        }
    }

    protected void routeToTvInputFromPortId(int portId) {
        if (portId < 0 || portId >= 21) {
            com.android.server.hdmi.HdmiLogger.debug("Invalid port number for Tv Input switching.", new java.lang.Object[0]);
            return;
        }
        this.mService.wakeUp();
        if (getLocalActivePort() == portId && portId != 17) {
            com.android.server.hdmi.HdmiLogger.debug("Not switching to the same port " + portId + " except for arc", new java.lang.Object[0]);
            return;
        }
        if (portId == 0 && this.mService.isPlaybackDevice()) {
            switchToHomeTvInput();
        } else if (portId == 17) {
            switchToTvInput((java.lang.String) android.sysprop.HdmiProperties.arc_port().orElse("0"));
            setLocalActivePort(portId);
            return;
        } else {
            java.lang.String uri = this.mPortIdToTvInputs.get(java.lang.Integer.valueOf(portId));
            if (uri != null) {
                switchToTvInput(uri);
            } else {
                com.android.server.hdmi.HdmiLogger.debug("Port number does not match any Tv Input.", new java.lang.Object[0]);
                return;
            }
        }
        setLocalActivePort(portId);
        setRoutingPort(portId);
    }

    private void switchToTvInput(java.lang.String uri) {
        try {
            this.mService.getContext().startActivity(new android.content.Intent("android.intent.action.VIEW", android.media.tv.TvContract.buildChannelUriForPassthroughInput(uri)).addFlags(268435456));
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Slog.e(TAG, "Can't find activity to switch to " + uri, e);
        }
    }

    private void switchToHomeTvInput() {
        try {
            android.content.Intent activityIntent = new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME").setFlags(872480768);
            this.mService.getContext().startActivity(activityIntent);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Slog.e(TAG, "Can't find activity to switch to HOME", e);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource
    protected void handleRoutingChangeAndInformation(int physicalAddress, com.android.server.hdmi.HdmiCecMessage message) {
        int port = this.mService.pathToPortId(physicalAddress);
        if (port > 0) {
            return;
        }
        if (port < 0 && isSystemAudioActivated()) {
            handleRoutingChangeAndInformationForSystemAudio();
        } else if (port == 0) {
            handleRoutingChangeAndInformationForSwitch(message);
        }
    }

    private void handleRoutingChangeAndInformationForSystemAudio() {
        routeToInputFromPortId(17);
    }

    private void handleRoutingChangeAndInformationForSwitch(com.android.server.hdmi.HdmiCecMessage message) {
        if (getRoutingPort() == 0 && this.mService.isPlaybackDevice()) {
            routeToInputFromPortId(0);
            this.mService.setAndBroadcastActiveSourceFromOneDeviceType(message.getSource(), this.mService.getPhysicalAddress(), "HdmiCecLocalDeviceAudioSystem#handleRoutingChangeAndInformationForSwitch()");
            return;
        }
        int routingInformationPath = this.mService.portIdToPath(getRoutingPort());
        if (routingInformationPath == this.mService.getPhysicalAddress()) {
            com.android.server.hdmi.HdmiLogger.debug("Current device can't assign valid physical addressto devices under it any more. It's physical address is " + routingInformationPath, new java.lang.Object[0]);
        } else {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildRoutingInformation(getDeviceInfo().getLogicalAddress(), routingInformationPath));
            routeToInputFromPortId(getRoutingPort());
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void launchDeviceDiscovery() {
        assertRunOnServiceThread();
        if (this.mService.isDeviceDiscoveryHandledByPlayback()) {
            return;
        }
        if (hasAction(com.android.server.hdmi.DeviceDiscoveryAction.class)) {
            android.util.Slog.i(TAG, "Device Discovery Action is in progress. Restarting.");
            removeAction(com.android.server.hdmi.DeviceDiscoveryAction.class);
        }
        com.android.server.hdmi.DeviceDiscoveryAction action = new com.android.server.hdmi.DeviceDiscoveryAction(this, new com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.4
            @Override // com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback
            public void onDeviceDiscoveryDone(java.util.List<android.hardware.hdmi.HdmiDeviceInfo> deviceInfos) {
                for (android.hardware.hdmi.HdmiDeviceInfo info : deviceInfos) {
                    com.android.server.hdmi.HdmiCecLocalDeviceAudioSystem.this.mService.getHdmiCecNetwork().addCecDevice(info);
                }
            }
        });
        addAndStartAction(action);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void dump(com.android.internal.util.IndentingPrintWriter pw) {
        pw.println("HdmiCecLocalDeviceAudioSystem:");
        pw.increaseIndent();
        pw.println("isRoutingFeatureEnabled " + isRoutingControlFeatureEnabled());
        pw.println("mSystemAudioControlFeatureEnabled: " + this.mSystemAudioControlFeatureEnabled);
        pw.println("mTvSystemAudioModeSupport: " + this.mTvSystemAudioModeSupport);
        pw.println("mArcEstablished: " + this.mArcEstablished);
        pw.println("mArcIntentUsed: " + this.mArcIntentUsed);
        pw.println("mRoutingPort: " + getRoutingPort());
        pw.println("mLocalActivePort: " + getLocalActivePort());
        com.android.server.hdmi.HdmiUtils.dumpMap(pw, "mPortIdToTvInputs:", this.mPortIdToTvInputs);
        com.android.server.hdmi.HdmiUtils.dumpMap(pw, "mTvInputsToDeviceInfo:", this.mTvInputsToDeviceInfo);
        pw.decreaseIndent();
        super.dump(pw);
    }
}
