package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class AudioManagerRouteController implements com.android.server.media.DeviceRouteController {
    private static final java.lang.String TAG = "MR2SystemProvider";
    private final android.media.AudioManager mAudioManager;
    private final com.android.server.media.BluetoothDeviceRoutesManager mBluetoothRouteController;
    private final int mBuiltInSpeakerSuitabilityStatus;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private final com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener mOnDeviceRouteChangedListener;
    private android.media.MediaRoute2Info mSelectedRoute;
    private final android.media.audiopolicy.AudioProductStrategy mStrategyForMedia;
    private static final android.media.AudioAttributes MEDIA_USAGE_AUDIO_ATTRIBUTES = new android.media.AudioAttributes.Builder().setUsage(1).build();
    private static final android.util.SparseArray<com.android.server.media.AudioManagerRouteController.SystemRouteInfo> AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO = new android.util.SparseArray<>();
    private final android.media.AudioDeviceCallback mAudioDeviceCallback = new com.android.server.media.AudioManagerRouteController.AudioDeviceCallbackImpl();
    private final android.media.AudioManager.OnDevicesForAttributesChangedListener mOnDevicesForAttributesChangedListener = new android.media.AudioManager.OnDevicesForAttributesChangedListener() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda3
        public final void onDevicesForAttributesChanged(android.media.AudioAttributes audioAttributes, java.util.List list) {
            this.f$0.onDevicesForAttributesChangedListener(audioAttributes, list);
        }
    };
    private final java.util.Map<java.lang.String, com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder> mRouteIdToAvailableDeviceRoutes = new java.util.HashMap();

    static {
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(2, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(2, "ROUTE_ID_BUILTIN_SPEAKER", android.R.string.default_card_name));
        android.util.SparseArray<com.android.server.media.AudioManagerRouteController.SystemRouteInfo> sparseArray = AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO;
        int i = android.R.string.delete;
        sparseArray.put(3, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(3, "ROUTE_ID_WIRED_HEADSET", i));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(4, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(4, "ROUTE_ID_WIRED_HEADPHONES", i));
        android.util.SparseArray<com.android.server.media.AudioManagerRouteController.SystemRouteInfo> sparseArray2 = AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO;
        int i2 = android.R.string.bugreport_option_full_title;
        sparseArray2.put(8, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(8, "ROUTE_ID_BLUETOOTH_A2DP", i2));
        android.util.SparseArray<com.android.server.media.AudioManagerRouteController.SystemRouteInfo> sparseArray3 = AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO;
        int i3 = android.R.string.default_sms_application;
        sparseArray3.put(9, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(9, "ROUTE_ID_HDMI", i3));
        android.util.SparseArray<com.android.server.media.AudioManagerRouteController.SystemRouteInfo> sparseArray4 = AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO;
        int i4 = 13;
        int i5 = android.R.string.default_notification_channel_label;
        sparseArray4.put(13, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i4, "ROUTE_ID_DOCK", i5));
        android.util.SparseArray<com.android.server.media.AudioManagerRouteController.SystemRouteInfo> sparseArray5 = AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO;
        int i6 = android.R.string.deleteText;
        sparseArray5.put(11, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(11, "ROUTE_ID_USB_DEVICE", i6));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(22, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(22, "ROUTE_ID_USB_HEADSET", i6));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(10, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(10, "ROUTE_ID_HDMI_ARC", i3));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(29, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(29, "ROUTE_ID_HDMI_EARC", i3));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(23, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(23, "ROUTE_ID_HEARING_AID", i2));
        int i7 = 26;
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(26, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i7, "ROUTE_ID_BLE_HEADSET", i2));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(27, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i7, "ROUTE_ID_BLE_SPEAKER", i2));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(30, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i7, "ROUTE_ID_BLE_BROADCAST", i2));
        int i8 = 0;
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(6, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i8, "ROUTE_ID_LINE_DIGITAL", i3));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(5, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i8, "ROUTE_ID_LINE_ANALOG", i3));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(19, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i8, "ROUTE_ID_AUX_LINE", i3));
        AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.put(31, new com.android.server.media.AudioManagerRouteController.SystemRouteInfo(i4, "ROUTE_ID_DOCK_ANALOG", i5));
    }

    AudioManagerRouteController(android.content.Context context, android.media.AudioManager audioManager, android.os.Looper looper, android.media.audiopolicy.AudioProductStrategy strategyForMedia, android.bluetooth.BluetoothAdapter btAdapter, com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener onDeviceRouteChangedListener) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mAudioManager = (android.media.AudioManager) java.util.Objects.requireNonNull(audioManager);
        this.mHandler = new android.os.Handler((android.os.Looper) java.util.Objects.requireNonNull(looper));
        this.mStrategyForMedia = (android.media.audiopolicy.AudioProductStrategy) java.util.Objects.requireNonNull(strategyForMedia);
        this.mOnDeviceRouteChangedListener = (com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener) java.util.Objects.requireNonNull(onDeviceRouteChangedListener);
        this.mBuiltInSpeakerSuitabilityStatus = com.android.server.media.DeviceRouteController.getBuiltInSpeakerSuitabilityStatus(this.mContext);
        this.mBluetoothRouteController = new com.android.server.media.BluetoothDeviceRoutesManager(this.mContext, this.mHandler, btAdapter, new com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda4
            @Override // com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener
            public final void onBluetoothRoutesUpdated() {
                this.f$0.rebuildAvailableRoutesAndNotify();
            }
        });
        rebuildAvailableRoutes();
    }

    @Override // com.android.server.media.DeviceRouteController
    public void start(android.os.UserHandle mUser) {
        this.mBluetoothRouteController.start(mUser);
        this.mAudioManager.registerAudioDeviceCallback(this.mAudioDeviceCallback, this.mHandler);
        this.mAudioManager.addOnDevicesForAttributesChangedListener(com.android.server.media.AudioRoutingUtils.ATTRIBUTES_MEDIA, new android.os.HandlerExecutor(this.mHandler), this.mOnDevicesForAttributesChangedListener);
    }

    @Override // com.android.server.media.DeviceRouteController
    public void stop() {
        this.mAudioManager.removeOnDevicesForAttributesChangedListener(this.mOnDevicesForAttributesChangedListener);
        this.mAudioManager.unregisterAudioDeviceCallback(this.mAudioDeviceCallback);
        this.mBluetoothRouteController.stop();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized android.media.MediaRoute2Info getSelectedRoute() {
        return this.mSelectedRoute;
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized java.util.List<android.media.MediaRoute2Info> getAvailableRoutes() {
        return this.mRouteIdToAvailableDeviceRoutes.values().stream().map(new java.util.function.Function() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder) obj).mMediaRoute2Info;
            }
        }).toList();
    }

    @Override // com.android.server.media.DeviceRouteController
    public void transferTo(final java.lang.String routeId) {
        com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder mediaRoute2InfoHolder;
        if (routeId == null) {
            android.util.Slog.e(TAG, "Unexpected call to AudioPoliciesDeviceRouteController#transferTo(null)");
            return;
        }
        synchronized (this) {
            mediaRoute2InfoHolder = this.mRouteIdToAvailableDeviceRoutes.get(routeId);
        }
        if (mediaRoute2InfoHolder == null) {
            android.util.Slog.w(TAG, "transferTo: Ignoring transfer request to unknown route id : " + routeId);
            return;
        }
        final java.lang.Runnable transferAction = getTransferActionForRoute(mediaRoute2InfoHolder);
        java.lang.Runnable guardedTransferAction = new java.lang.Runnable() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$transferTo$1(transferAction, routeId);
            }
        };
        this.mHandler.post(guardedTransferAction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$transferTo$1(java.lang.Runnable transferAction, java.lang.String routeId) {
        try {
            transferAction.run();
        } catch (java.lang.Throwable throwable) {
            android.util.Slog.e(TAG, "Unexpected exception while transferring to route id: " + routeId, throwable);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.rebuildAvailableRoutesAndNotify();
                }
            });
        }
    }

    @Override // com.android.server.media.DeviceRouteController
    public boolean updateVolume(int volume) {
        rebuildAvailableRoutesAndNotify();
        return true;
    }

    private java.lang.Runnable getTransferActionForRoute(com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder mediaRoute2InfoHolder) {
        if (mediaRoute2InfoHolder.mCorrespondsToInactiveBluetoothRoute) {
            final java.lang.String deviceAddress = mediaRoute2InfoHolder.mMediaRoute2Info.getAddress();
            return new java.lang.Runnable() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getTransferActionForRoute$2(deviceAddress);
                }
            };
        }
        final android.media.AudioDeviceAttributes deviceAttributes = new android.media.AudioDeviceAttributes(2, mediaRoute2InfoHolder.mAudioDeviceInfoType, "");
        return new java.lang.Runnable() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getTransferActionForRoute$3(deviceAttributes);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTransferActionForRoute$2(java.lang.String deviceAddress) {
        this.mBluetoothRouteController.activateBluetoothDeviceWithAddress(deviceAddress);
        this.mAudioManager.removePreferredDeviceForStrategy(this.mStrategyForMedia);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTransferActionForRoute$3(android.media.AudioDeviceAttributes deviceAttributes) {
        this.mAudioManager.setPreferredDeviceForStrategy(this.mStrategyForMedia, deviceAttributes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDevicesForAttributesChangedListener(android.media.AudioAttributes attributes, java.util.List<android.media.AudioDeviceAttributes> unusedAudioDeviceAttributes) {
        if (attributes.getUsage() == 1) {
            rebuildAvailableRoutesAndNotify();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void rebuildAvailableRoutesAndNotify() {
        rebuildAvailableRoutes();
        this.mOnDeviceRouteChangedListener.onDeviceRouteChanged();
    }

    private void rebuildAvailableRoutes() {
        int selectedDeviceAttributesType;
        java.util.List<android.media.AudioDeviceAttributes> attributesOfSelectedOutputDevices = this.mAudioManager.getDevicesForAttributes(MEDIA_USAGE_AUDIO_ATTRIBUTES);
        if (attributesOfSelectedOutputDevices.isEmpty()) {
            android.util.Slog.e(TAG, "Unexpected empty list of output devices for media. Using built-in speakers.");
            selectedDeviceAttributesType = 2;
        } else {
            int selectedDeviceAttributesType2 = attributesOfSelectedOutputDevices.size();
            if (selectedDeviceAttributesType2 > 1) {
                android.util.Slog.w(TAG, "AudioManager.getDevicesForAttributes returned more than one element. Using the first one.");
            }
            selectedDeviceAttributesType = attributesOfSelectedOutputDevices.get(0).getType();
        }
        updateAvailableRoutes(selectedDeviceAttributesType, this.mAudioManager.getDevices(2), this.mBluetoothRouteController.getAvailableBluetoothRoutes(), this.mAudioManager.getStreamVolume(3), this.mAudioManager.getStreamMaxVolume(3), this.mAudioManager.isVolumeFixed());
    }

    private synchronized void updateAvailableRoutes(int selectedDeviceAttributesType, android.media.AudioDeviceInfo[] audioDeviceInfos, java.util.List<android.media.MediaRoute2Info> availableBluetoothRoutes, int musicVolume, int musicMaxVolume, boolean isVolumeFixed) {
        this.mRouteIdToAvailableDeviceRoutes.clear();
        com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder newSelectedRouteHolder = null;
        for (android.media.AudioDeviceInfo audioDeviceInfo : audioDeviceInfos) {
            android.media.MediaRoute2Info mediaRoute2Info = createMediaRoute2InfoFromAudioDeviceInfo(audioDeviceInfo);
            if (mediaRoute2Info != null) {
                int audioDeviceInfoType = audioDeviceInfo.getType();
                com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder newHolder = com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder.createForAudioManagerRoute(mediaRoute2Info, audioDeviceInfoType);
                this.mRouteIdToAvailableDeviceRoutes.put(mediaRoute2Info.getId(), newHolder);
                if (selectedDeviceAttributesType == audioDeviceInfoType) {
                    newSelectedRouteHolder = newHolder;
                }
            }
        }
        if (this.mRouteIdToAvailableDeviceRoutes.isEmpty()) {
            android.util.Slog.e(TAG, "Ended up with an empty list of routes. Creating a placeholder route.");
            com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder placeholderRouteHolder = createPlaceholderBuiltinSpeakerRoute();
            java.lang.String placeholderRouteId = placeholderRouteHolder.mMediaRoute2Info.getId();
            this.mRouteIdToAvailableDeviceRoutes.put(placeholderRouteId, placeholderRouteHolder);
        }
        if (newSelectedRouteHolder == null) {
            android.util.Slog.e(TAG, "Could not map this selected device attribute type to an available route: " + selectedDeviceAttributesType);
            newSelectedRouteHolder = this.mRouteIdToAvailableDeviceRoutes.values().iterator().next();
        }
        com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder selectedRouteHolderWithUpdatedVolumeInfo = newSelectedRouteHolder.copyWithVolumeInfo(musicVolume, musicMaxVolume, isVolumeFixed);
        this.mRouteIdToAvailableDeviceRoutes.put(newSelectedRouteHolder.mMediaRoute2Info.getId(), selectedRouteHolderWithUpdatedVolumeInfo);
        this.mSelectedRoute = selectedRouteHolderWithUpdatedVolumeInfo.mMediaRoute2Info;
        availableBluetoothRoutes.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$updateAvailableRoutes$4((android.media.MediaRoute2Info) obj);
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder.createForInactiveBluetoothRoute((android.media.MediaRoute2Info) obj);
            }
        }).forEach(new java.util.function.Consumer() { // from class: com.android.server.media.AudioManagerRouteController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateAvailableRoutes$5((com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateAvailableRoutes$4(android.media.MediaRoute2Info it) {
        return !this.mRouteIdToAvailableDeviceRoutes.containsKey(it.getId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateAvailableRoutes$5(com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder it) {
        this.mRouteIdToAvailableDeviceRoutes.put(it.mMediaRoute2Info.getId(), it);
    }

    private com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder createPlaceholderBuiltinSpeakerRoute() {
        return com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder.createForAudioManagerRoute(createMediaRoute2Info(null, 2, null, null), 2);
    }

    private android.media.MediaRoute2Info createMediaRoute2InfoFromAudioDeviceInfo(android.media.AudioDeviceInfo audioDeviceInfo) {
        java.lang.String address = audioDeviceInfo.getAddress();
        java.lang.String routeId = null;
        java.lang.String deviceName = audioDeviceInfo.getPort().name();
        if (!android.text.TextUtils.isEmpty(address)) {
            routeId = this.mBluetoothRouteController.getRouteIdForBluetoothAddress(address);
            deviceName = this.mBluetoothRouteController.getNameForBluetoothAddress(address);
        }
        return createMediaRoute2Info(routeId, audioDeviceInfo.getType(), deviceName, address);
    }

    private android.media.MediaRoute2Info createMediaRoute2Info(java.lang.String routeId, int audioDeviceInfoType, java.lang.CharSequence deviceName, java.lang.String address) {
        com.android.server.media.AudioManagerRouteController.SystemRouteInfo systemRouteInfo = AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.get(audioDeviceInfoType);
        if (systemRouteInfo == null) {
            return null;
        }
        java.lang.CharSequence humanReadableName = deviceName;
        if (android.text.TextUtils.isEmpty(humanReadableName)) {
            humanReadableName = this.mContext.getResources().getText(systemRouteInfo.mNameResource);
        }
        if (routeId == null) {
            routeId = systemRouteInfo.mDefaultRouteId;
        }
        android.media.MediaRoute2Info.Builder builder = new android.media.MediaRoute2Info.Builder(routeId, humanReadableName).setType(systemRouteInfo.mMediaRoute2InfoType).setAddress(address).setSystemRoute(true).addFeature("android.media.route.feature.LIVE_AUDIO").addFeature("android.media.route.feature.LOCAL_PLAYBACK").setConnectionState(2);
        if (systemRouteInfo.mMediaRoute2InfoType == 2) {
            builder.setSuitabilityStatus(this.mBuiltInSpeakerSuitabilityStatus);
        }
        return builder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class MediaRoute2InfoHolder {
        public final int mAudioDeviceInfoType;
        public final boolean mCorrespondsToInactiveBluetoothRoute;
        public final android.media.MediaRoute2Info mMediaRoute2Info;

        public static com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder createForAudioManagerRoute(android.media.MediaRoute2Info mediaRoute2Info, int audioDeviceInfoType) {
            return new com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder(mediaRoute2Info, audioDeviceInfoType, false);
        }

        public static com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder createForInactiveBluetoothRoute(android.media.MediaRoute2Info mediaRoute2Info) {
            return new com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder(mediaRoute2Info, 0, true);
        }

        private MediaRoute2InfoHolder(android.media.MediaRoute2Info mediaRoute2Info, int audioDeviceInfoType, boolean correspondsToInactiveBluetoothRoute) {
            this.mMediaRoute2Info = mediaRoute2Info;
            this.mAudioDeviceInfoType = audioDeviceInfoType;
            this.mCorrespondsToInactiveBluetoothRoute = correspondsToInactiveBluetoothRoute;
        }

        public com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder copyWithVolumeInfo(int musicVolume, int musicMaxVolume, boolean isVolumeFixed) {
            int i;
            android.media.MediaRoute2Info.Builder builder = new android.media.MediaRoute2Info.Builder(this.mMediaRoute2Info);
            if (isVolumeFixed) {
                i = 0;
            } else {
                i = 1;
            }
            android.media.MediaRoute2Info routeInfoWithVolumeInfo = builder.setVolumeHandling(i).setVolume(musicVolume).setVolumeMax(musicMaxVolume).build();
            return new com.android.server.media.AudioManagerRouteController.MediaRoute2InfoHolder(routeInfoWithVolumeInfo, this.mAudioDeviceInfoType, this.mCorrespondsToInactiveBluetoothRoute);
        }
    }

    private static class SystemRouteInfo {
        public final java.lang.String mDefaultRouteId;
        public final int mMediaRoute2InfoType;
        public final int mNameResource;

        private SystemRouteInfo(int mediaRoute2InfoType, java.lang.String defaultRouteId, int nameResource) {
            this.mMediaRoute2InfoType = mediaRoute2InfoType;
            this.mDefaultRouteId = defaultRouteId;
            this.mNameResource = nameResource;
        }
    }

    private class AudioDeviceCallbackImpl extends android.media.AudioDeviceCallback {
        private AudioDeviceCallbackImpl() {
        }

        @Override // android.media.AudioDeviceCallback
        public void onAudioDevicesAdded(android.media.AudioDeviceInfo[] addedDevices) {
            for (android.media.AudioDeviceInfo deviceInfo : addedDevices) {
                if (com.android.server.media.AudioManagerRouteController.AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.contains(deviceInfo.getType())) {
                    com.android.server.media.AudioManagerRouteController.this.mAudioManager.removePreferredDeviceForStrategy(com.android.server.media.AudioManagerRouteController.this.mStrategyForMedia);
                    com.android.server.media.AudioManagerRouteController.this.rebuildAvailableRoutesAndNotify();
                    return;
                }
            }
        }

        @Override // android.media.AudioDeviceCallback
        public void onAudioDevicesRemoved(android.media.AudioDeviceInfo[] removedDevices) {
            for (android.media.AudioDeviceInfo deviceInfo : removedDevices) {
                if (com.android.server.media.AudioManagerRouteController.AUDIO_DEVICE_INFO_TYPE_TO_ROUTE_INFO.contains(deviceInfo.getType())) {
                    com.android.server.media.AudioManagerRouteController.this.rebuildAvailableRoutesAndNotify();
                    return;
                }
            }
        }
    }
}
