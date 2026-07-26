package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecLocalDevicePlayback extends com.android.server.hdmi.HdmiCecLocalDeviceSource {
    static final long POPUP_AFTER_ACTIVE_SOURCE_LOST_DELAY_MS = 5000;
    static final long STANDBY_AFTER_ACTIVE_SOURCE_LOST_DELAY_MS = 30000;
    static final long STANDBY_AFTER_HOTPLUG_OUT_DELAY_MS = 30000;
    private static final java.lang.String TAG = "HdmiCecLocalDevicePlayback";
    private android.os.Handler mDelayedPopupOnActiveSourceLostHandler;
    private android.os.Handler mDelayedStandbyHandler;
    android.os.Handler mDelayedStandbyOnActiveSourceLostHandler;
    protected android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values mPlaybackDeviceActionOnRoutingControl;
    private com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock mWakeLock;

    private interface ActiveWakeLock {
        void acquire();

        boolean isHeld();

        void release();
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

    HdmiCecLocalDevicePlayback(com.android.server.hdmi.HdmiControlService service) {
        super(service, 4);
        this.mPlaybackDeviceActionOnRoutingControl = (android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values) android.sysprop.HdmiProperties.playback_device_action_on_routing_control().orElse(android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values.NONE);
        this.mDelayedStandbyHandler = new android.os.Handler(service.getServiceLooper());
        this.mDelayedStandbyOnActiveSourceLostHandler = new android.os.Handler(service.getServiceLooper());
        this.mDelayedPopupOnActiveSourceLostHandler = new android.os.Handler(service.getServiceLooper());
        this.mStandbyHandler = new com.android.server.hdmi.HdmiCecStandbyModeHandler(service, this);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onAddressAllocated(int logicalAddress, int reason) {
        assertRunOnServiceThread();
        com.android.server.hdmi.HdmiControlService hdmiControlService = this.mService;
        if (reason == 0) {
            this.mService.setAndBroadcastActiveSource(this.mService.getPhysicalAddress(), getDeviceInfo().getDeviceType(), 15, "HdmiCecLocalDevicePlayback#onAddressAllocated()");
        }
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildReportPhysicalAddressCommand(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress(), this.mDeviceType));
        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildDeviceVendorIdCommand(getDeviceInfo().getLogicalAddress(), this.mService.getVendorId()));
        buildAndSendSetOsdName(0);
        if (this.mService.audioSystem() == null) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildGiveSystemAudioModeStatus(getDeviceInfo().getLogicalAddress(), 5), new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.1
                @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
                public void onSendCompleted(int error) {
                    if (error == 1) {
                        com.android.server.hdmi.HdmiLogger.debug("AVR did not respond to <Give System Audio Mode Status>", new java.lang.Object[0]);
                        com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.setSystemAudioActivated(false);
                    }
                }
            });
        }
        launchDeviceDiscovery();
        startQueuedActions();
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private void launchDeviceDiscovery() {
        assertRunOnServiceThread();
        clearDeviceInfoList();
        if (hasAction(com.android.server.hdmi.DeviceDiscoveryAction.class)) {
            android.util.Slog.i(TAG, "Device Discovery Action is in progress. Restarting.");
            removeAction(com.android.server.hdmi.DeviceDiscoveryAction.class);
        }
        com.android.server.hdmi.DeviceDiscoveryAction action = new com.android.server.hdmi.DeviceDiscoveryAction(this, new com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.2
            @Override // com.android.server.hdmi.DeviceDiscoveryAction.DeviceDiscoveryCallback
            public void onDeviceDiscoveryDone(java.util.List<android.hardware.hdmi.HdmiDeviceInfo> deviceInfos) {
                for (android.hardware.hdmi.HdmiDeviceInfo info : deviceInfos) {
                    com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getHdmiCecNetwork().addCecDevice(info);
                }
                for (com.android.server.hdmi.HdmiCecLocalDevice device : com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getAllCecLocalDevices()) {
                    com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getHdmiCecNetwork().addCecDevice(device.getDeviceInfo());
                }
                java.util.List<com.android.server.hdmi.HotplugDetectionAction> hotplugActions = com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.getActions(com.android.server.hdmi.HotplugDetectionAction.class);
                if (hotplugActions.isEmpty()) {
                    com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.addAndStartAction(new com.android.server.hdmi.HotplugDetectionAction(com.android.server.hdmi.HdmiCecLocalDevicePlayback.this));
                }
            }
        });
        addAndStartAction(action);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int getPreferredAddress() {
        assertRunOnServiceThread();
        return android.os.SystemProperties.getInt("persist.sys.hdmi.addr.playback", 15);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void setPreferredAddress(int addr) {
        assertRunOnServiceThread();
        this.mService.writeStringSystemProperty("persist.sys.hdmi.addr.playback", java.lang.String.valueOf(addr));
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void deviceSelect(int id, android.hardware.hdmi.IHdmiControlCallback callback) {
        assertRunOnServiceThread();
        if (id == getDeviceInfo().getId()) {
            this.mService.oneTouchPlay(callback);
            return;
        }
        android.hardware.hdmi.HdmiDeviceInfo targetDevice = this.mService.getHdmiCecNetwork().getDeviceInfo(id);
        if (targetDevice == null) {
            invokeCallback(callback, 3);
            return;
        }
        int targetAddress = targetDevice.getLogicalAddress();
        if (isAlreadyActiveSource(targetDevice, targetAddress, callback)) {
            return;
        }
        if (!this.mService.isCecControlEnabled()) {
            setActiveSource(targetDevice, "HdmiCecLocalDevicePlayback#deviceSelect()");
            invokeCallback(callback, 6);
        } else {
            removeAction(com.android.server.hdmi.DeviceSelectActionFromPlayback.class);
            addAndStartAction(new com.android.server.hdmi.DeviceSelectActionFromPlayback(this, targetDevice, callback));
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void onHotplug(int portId, boolean connected) {
        assertRunOnServiceThread();
        this.mCecMessageCache.flushAll();
        if (connected) {
            this.mDelayedStandbyHandler.removeCallbacksAndMessages(null);
            return;
        }
        getWakeLock().release();
        this.mService.getHdmiCecNetwork().removeDevicesConnectedToPort(portId);
        this.mDelayedStandbyHandler.removeCallbacksAndMessages(null);
        this.mDelayedStandbyHandler.postDelayed(new com.android.server.hdmi.HdmiCecLocalDevicePlayback.DelayedStandbyRunnable(), 30000L);
    }

    private class DelayedStandbyRunnable implements java.lang.Runnable {
        private DelayedStandbyRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getPowerManagerInternal().wasDeviceIdleFor(30000L)) {
                com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.standby();
            } else {
                com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mDelayedStandbyHandler.postDelayed(com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.new DelayedStandbyRunnable(), 30000L);
            }
        }
    }

    private class DelayedStandbyOnActiveSourceLostRunnable implements java.lang.Runnable {
        private DelayedStandbyOnActiveSourceLostRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getPowerManagerInternal().wasDeviceIdleFor(30000L)) {
                com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.standby();
            } else {
                com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.setAndBroadcastActiveSource(com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getPhysicalAddress(), com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.getDeviceInfo().getDeviceType(), 0, "DelayedActiveSourceLostStandbyRunnable");
            }
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void dismissUiOnActiveSourceStatusRecovered() {
        assertRunOnServiceThread();
        android.content.Intent intent = new android.content.Intent("android.hardware.hdmi.action.ON_ACTIVE_SOURCE_RECOVERED_DISMISS_UI");
        this.mService.sendBroadcastAsUser(intent);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onStandby(boolean initiatedByCec, int standbyAction, final com.android.server.hdmi.HdmiCecLocalDevice.StandbyCompletedCallback callback) {
        assertRunOnServiceThread();
        if (!this.mService.isCecControlEnabled()) {
            invokeStandbyCompletedCallback(callback);
        }
        boolean wasActiveSource = isActiveSource();
        byte b = -1;
        this.mService.setActiveSource(-1, 65535, "HdmiCecLocalDevicePlayback#onStandby()");
        if (!wasActiveSource) {
            invokeStandbyCompletedCallback(callback);
            return;
        }
        com.android.server.hdmi.HdmiControlService.SendMessageCallback sendMessageCallback = new com.android.server.hdmi.HdmiControlService.SendMessageCallback() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.3
            @Override // com.android.server.hdmi.HdmiControlService.SendMessageCallback
            public void onSendCompleted(int error) {
                com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.invokeStandbyCompletedCallback(callback);
            }
        };
        if (initiatedByCec) {
            this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildInactiveSource(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress()), sendMessageCallback);
            return;
        }
        switch (standbyAction) {
            case 0:
                java.lang.String powerControlMode = this.mService.getHdmiCecConfig().getStringValue("power_control_mode");
                switch (powerControlMode.hashCode()) {
                    case -1744153479:
                        if (powerControlMode.equals("to_tv_and_audio_system")) {
                            b = 1;
                        }
                        break;
                    case -1618876223:
                        if (powerControlMode.equals("broadcast")) {
                            b = 2;
                        }
                        break;
                    case 3387192:
                        if (powerControlMode.equals("none")) {
                            b = 3;
                        }
                        break;
                    case 110530246:
                        if (powerControlMode.equals("to_tv")) {
                            b = 0;
                        }
                        break;
                }
                switch (b) {
                    case 0:
                        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 0), sendMessageCallback);
                        break;
                    case 1:
                        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 0));
                        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 5), sendMessageCallback);
                        break;
                    case 2:
                        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15), sendMessageCallback);
                        break;
                    case 3:
                        this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildInactiveSource(getDeviceInfo().getLogicalAddress(), this.mService.getPhysicalAddress()), sendMessageCallback);
                        break;
                }
                break;
            case 1:
                this.mService.sendCecCommand(com.android.server.hdmi.HdmiCecMessageBuilder.buildStandby(getDeviceInfo().getLogicalAddress(), 15), sendMessageCallback);
                break;
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void onInitializeCecComplete(int initiatedBy) {
        if (initiatedBy != 2) {
            return;
        }
        java.lang.String powerControlMode = this.mService.getHdmiCecConfig().getStringValue("power_control_mode");
        if (powerControlMode.equals("none")) {
            return;
        }
        oneTouchPlay(new android.hardware.hdmi.IHdmiControlCallback.Stub() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.4
            public void onComplete(int result) {
                if (result != 0) {
                    android.util.Slog.w(com.android.server.hdmi.HdmiCecLocalDevicePlayback.TAG, "Failed to complete One Touch Play. result=" + result);
                }
            }
        });
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void setActiveSource(int logicalAddress, int physicalAddress, java.lang.String caller) {
        assertRunOnServiceThread();
        super.setActiveSource(logicalAddress, physicalAddress, caller);
        if (isActiveSource()) {
            getWakeLock().acquire();
        } else {
            getWakeLock().release();
        }
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    private com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock getWakeLock() {
        assertRunOnServiceThread();
        if (this.mWakeLock == null) {
            if (android.os.SystemProperties.getBoolean("persist.sys.hdmi.keep_awake", true)) {
                this.mWakeLock = new com.android.server.hdmi.HdmiCecLocalDevicePlayback.SystemWakeLock();
            } else {
                this.mWakeLock = new com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock() { // from class: com.android.server.hdmi.HdmiCecLocalDevicePlayback.5
                    @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
                    public void acquire() {
                    }

                    @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
                    public void release() {
                    }

                    @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
                    public boolean isHeld() {
                        return false;
                    }
                };
                com.android.server.hdmi.HdmiLogger.debug("No wakelock is used to keep the display on.", new java.lang.Object[0]);
            }
        }
        return this.mWakeLock;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected boolean canGoToStandby() {
        return !getWakeLock().isHeld();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0033  */
    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onActiveSourceLost() {
        /*
            r4 = this;
            r4.assertRunOnServiceThread()
            com.android.server.hdmi.HdmiControlService r0 = r4.mService
            r0.pauseActiveMediaSessions()
            com.android.server.hdmi.HdmiControlService r0 = r4.mService
            com.android.server.hdmi.HdmiCecConfig r0 = r0.getHdmiCecConfig()
            java.lang.String r1 = "power_state_change_on_active_source_lost"
            java.lang.String r0 = r0.getStringValue(r1)
            int r1 = r0.hashCode()
            switch(r1) {
                case -1129124284: goto L28;
                case 3387192: goto L1d;
                default: goto L1c;
            }
        L1c:
            goto L33
        L1d:
            java.lang.String r1 = "none"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L1c
            r0 = 1
            goto L34
        L28:
            java.lang.String r1 = "standby_now"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L1c
            r0 = 0
            goto L34
        L33:
            r0 = -1
        L34:
            switch(r0) {
                case 0: goto L39;
                case 1: goto L38;
                default: goto L37;
            }
        L37:
            return
        L38:
            return
        L39:
            android.os.Handler r0 = r4.mDelayedPopupOnActiveSourceLostHandler
            r1 = 0
            r0.removeCallbacksAndMessages(r1)
            android.os.Handler r0 = r4.mDelayedPopupOnActiveSourceLostHandler
            com.android.server.hdmi.HdmiCecLocalDevicePlayback$6 r1 = new com.android.server.hdmi.HdmiCecLocalDevicePlayback$6
            r1.<init>()
            r2 = 5000(0x1388, double:2.4703E-320)
            r0.postDelayed(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiCecLocalDevicePlayback.onActiveSourceLost():void");
    }

    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    void startHdmiCecActiveSourceLostActivity() {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.content.Context context = this.mService.getContext();
                android.content.Intent intent = new android.content.Intent();
                intent.setComponent(android.content.ComponentName.unflattenFromString(context.getResources().getString(android.R.string.config_incidentReportApproverPackage)));
                intent.addFlags(268435456);
                context.startActivityAsUser(intent, context.getUser());
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.e(TAG, "Unable to start HdmiCecActiveSourceLostActivity");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleUserControlPressed(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        wakeUpIfActiveSource();
        return super.handleUserControlPressed(message);
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleSetMenuLanguage(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (this.mService.getHdmiCecConfig().getIntValue("set_menu_language") == 0) {
            return 0;
        }
        try {
            java.lang.String iso3Language = new java.lang.String(message.getParams(), 0, 3, "US-ASCII");
            java.util.Locale currentLocale = this.mService.getContext().getResources().getConfiguration().locale;
            com.android.server.hdmi.HdmiControlService hdmiControlService = this.mService;
            java.lang.String curIso3Language = com.android.server.hdmi.HdmiControlService.localeToMenuLanguage(currentLocale);
            com.android.server.hdmi.HdmiLogger.debug("handleSetMenuLanguage " + iso3Language + " cur:" + curIso3Language, new java.lang.Object[0]);
            if (curIso3Language.equals(iso3Language)) {
                return -1;
            }
            java.util.List<com.android.internal.app.LocalePicker.LocaleInfo> localeInfos = com.android.internal.app.LocalePicker.getAllAssetLocales(this.mService.getContext(), false);
            for (com.android.internal.app.LocalePicker.LocaleInfo localeInfo : localeInfos) {
                com.android.server.hdmi.HdmiControlService hdmiControlService2 = this.mService;
                if (com.android.server.hdmi.HdmiControlService.localeToMenuLanguage(localeInfo.getLocale()).equals(iso3Language)) {
                    startSetMenuLanguageActivity(localeInfo.getLocale());
                    return -1;
                }
            }
            android.util.Slog.w(TAG, "Can't handle <Set Menu Language> of " + iso3Language);
            return 3;
        } catch (java.io.UnsupportedEncodingException e) {
            android.util.Slog.w(TAG, "Can't handle <Set Menu Language>", e);
            return 3;
        }
    }

    private void startSetMenuLanguageActivity(java.util.Locale locale) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.content.Context context = this.mService.getContext();
                android.content.Intent intent = new android.content.Intent();
                intent.putExtra("android.hardware.hdmi.extra.LOCALE", locale.toLanguageTag());
                intent.setComponent(android.content.ComponentName.unflattenFromString(context.getResources().getString(android.R.string.config_inputEventCompatProcessorOverrideClassName)));
                intent.addFlags(268435456);
                context.startActivityAsUser(intent, context.getUser());
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.e(TAG, "unable to start HdmiCecSetMenuLanguageActivity");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleSetSystemAudioMode(com.android.server.hdmi.HdmiCecMessage message) {
        boolean setSystemAudioModeOn;
        if (message.getDestination() == 15 && message.getSource() == 5 && this.mService.audioSystem() == null && this.mService.isSystemAudioActivated() != (setSystemAudioModeOn = com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(message))) {
            this.mService.setSystemAudioActivated(setSystemAudioModeOn);
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int handleSystemAudioModeStatus(com.android.server.hdmi.HdmiCecMessage message) {
        boolean setSystemAudioModeOn;
        if (message.getDestination() == getDeviceInfo().getLogicalAddress() && message.getSource() == 5 && this.mService.isSystemAudioActivated() != (setSystemAudioModeOn = com.android.server.hdmi.HdmiUtils.parseCommandParamSystemAudioStatus(message))) {
            this.mService.setSystemAudioActivated(setSystemAudioModeOn);
            return -1;
        }
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingChange(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams(), 2);
        handleRoutingChangeAndInformation(physicalAddress, message);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected int handleRoutingInformation(com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(message.getParams());
        android.hardware.hdmi.HdmiDeviceInfo sourceDevice = this.mService.getHdmiCecNetwork().getCecDeviceInfo(message.getSource());
        if (sourceDevice != null && sourceDevice.getLogicalAddress() != 0 && sourceDevice.getPhysicalAddress() == physicalAddress) {
            android.util.Slog.d(TAG, "<Routing Information> is ignored, it is pointing to the same physical address as the message sender");
            return -1;
        }
        handleRoutingChangeAndInformation(physicalAddress, message);
        return -1;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void handleRoutingChangeAndInformation(int physicalAddress, com.android.server.hdmi.HdmiCecMessage message) {
        assertRunOnServiceThread();
        if (com.android.server.hdmi.HdmiUtils.isInActiveRoutingPath(this.mService.getPhysicalAddress(), physicalAddress) && physicalAddress != 0 && isActiveSource()) {
        }
        if (physicalAddress != this.mService.getPhysicalAddress()) {
            setActiveSource(physicalAddress, "HdmiCecLocalDevicePlayback#handleRoutingChangeAndInformation()");
            return;
        }
        if (!isActiveSource()) {
            setActiveSource(physicalAddress, "HdmiCecLocalDevicePlayback#handleRoutingChangeAndInformation()");
        }
        dismissUiOnActiveSourceStatusRecovered();
        switch (com.android.server.hdmi.HdmiCecLocalDevicePlayback.AnonymousClass7.$SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[this.mPlaybackDeviceActionOnRoutingControl.ordinal()]) {
            case 1:
                setAndBroadcastActiveSource(message, physicalAddress, "HdmiCecLocalDevicePlayback#handleRoutingChangeAndInformation()");
                break;
            case 2:
                this.mService.wakeUp();
                break;
        }
    }

    /* JADX INFO: renamed from: com.android.server.hdmi.HdmiCecLocalDevicePlayback$7, reason: invalid class name */
    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values = new int[android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values.values().length];

        static {
            try {
                $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values.WAKE_UP_AND_SEND_ACTIVE_SOURCE.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values.WAKE_UP_ONLY.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$sysprop$HdmiProperties$playback_device_action_on_routing_control_values[android.sysprop.HdmiProperties.playback_device_action_on_routing_control_values.NONE.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void preprocessBufferedMessages(java.util.List<com.android.server.hdmi.HdmiCecMessage> bufferedMessages) {
        for (com.android.server.hdmi.HdmiCecMessage message : bufferedMessages) {
            if (message.getOpcode() == 128 || message.getOpcode() == 134 || message.getOpcode() == 130) {
                removeAction(com.android.server.hdmi.ActiveSourceAction.class);
                removeAction(com.android.server.hdmi.OneTouchPlayAction.class);
                return;
            }
        }
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findKeyReceiverAddress() {
        return 0;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected int findAudioReceiverAddress() {
        if (this.mService.isSystemAudioActivated()) {
            return 5;
        }
        return 0;
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDeviceSource, com.android.server.hdmi.HdmiCecLocalDevice
    @com.android.server.hdmi.HdmiAnnotations.ServiceThreadOnly
    protected void disableDevice(boolean initiatedByCec, com.android.server.hdmi.HdmiCecLocalDevice.PendingActionClearedCallback callback) {
        assertRunOnServiceThread();
        removeAction(com.android.server.hdmi.DeviceDiscoveryAction.class);
        removeAction(com.android.server.hdmi.HotplugDetectionAction.class);
        removeAction(com.android.server.hdmi.NewDeviceAction.class);
        super.disableDevice(initiatedByCec, callback);
        clearDeviceInfoList();
        checkIfPendingActionsCleared();
    }

    @Override // com.android.server.hdmi.HdmiCecLocalDevice
    protected void dump(com.android.internal.util.IndentingPrintWriter pw) {
        super.dump(pw);
        pw.println("isActiveSource(): " + isActiveSource());
    }

    private class SystemWakeLock implements com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock {
        private final com.android.server.hdmi.WakeLockWrapper mWakeLock;

        public SystemWakeLock() {
            this.mWakeLock = com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.mService.getPowerManager().newWakeLock(1, com.android.server.hdmi.HdmiCecLocalDevicePlayback.TAG);
            this.mWakeLock.setReferenceCounted(false);
        }

        @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
        public void acquire() {
            this.mWakeLock.acquire();
            com.android.server.hdmi.HdmiLogger.debug("active source: %b. Wake lock acquired", java.lang.Boolean.valueOf(com.android.server.hdmi.HdmiCecLocalDevicePlayback.this.isActiveSource()));
        }

        @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
        public void release() {
            this.mWakeLock.release();
            com.android.server.hdmi.HdmiLogger.debug("Wake lock released", new java.lang.Object[0]);
        }

        @Override // com.android.server.hdmi.HdmiCecLocalDevicePlayback.ActiveWakeLock
        public boolean isHeld() {
            return this.mWakeLock.isHeld();
        }
    }
}
