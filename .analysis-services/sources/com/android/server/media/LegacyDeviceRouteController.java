package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class LegacyDeviceRouteController implements com.android.server.media.DeviceRouteController {
    private static final java.lang.String DEVICE_ROUTE_ID = "DEVICE_ROUTE";
    private static final java.lang.String TAG = "LDeviceRouteController";
    private final android.media.AudioManager mAudioManager;
    private final com.android.server.media.LegacyDeviceRouteController.AudioRoutesObserver mAudioRoutesObserver = new com.android.server.media.LegacyDeviceRouteController.AudioRoutesObserver();
    private final android.media.IAudioService mAudioService;
    private final int mBuiltInSpeakerSuitabilityStatus;
    private final android.content.Context mContext;
    private android.media.MediaRoute2Info mDeviceRoute;
    private int mDeviceVolume;
    private final com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener mOnDeviceRouteChangedListener;

    LegacyDeviceRouteController(android.content.Context context, android.media.AudioManager audioManager, android.media.IAudioService audioService, com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener onDeviceRouteChangedListener) {
        java.util.Objects.requireNonNull(context);
        java.util.Objects.requireNonNull(audioManager);
        java.util.Objects.requireNonNull(audioService);
        java.util.Objects.requireNonNull(onDeviceRouteChangedListener);
        this.mContext = context;
        this.mOnDeviceRouteChangedListener = onDeviceRouteChangedListener;
        this.mAudioManager = audioManager;
        this.mAudioService = audioService;
        this.mBuiltInSpeakerSuitabilityStatus = com.android.server.media.DeviceRouteController.getBuiltInSpeakerSuitabilityStatus(this.mContext);
        android.media.AudioRoutesInfo newAudioRoutes = null;
        try {
            newAudioRoutes = this.mAudioService.startWatchingRoutes(this.mAudioRoutesObserver);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Cannot connect to audio service to start listen to routes", e);
        }
        this.mDeviceRoute = createRouteFromAudioInfo(newAudioRoutes);
    }

    @Override // com.android.server.media.DeviceRouteController
    public void start(android.os.UserHandle mUser) {
    }

    @Override // com.android.server.media.DeviceRouteController
    public void stop() {
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized android.media.MediaRoute2Info getSelectedRoute() {
        return this.mDeviceRoute;
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized java.util.List<android.media.MediaRoute2Info> getAvailableRoutes() {
        return java.util.Collections.emptyList();
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized void transferTo(java.lang.String routeId) {
    }

    @Override // com.android.server.media.DeviceRouteController
    public synchronized boolean updateVolume(int volume) {
        if (this.mDeviceVolume == volume) {
            return false;
        }
        this.mDeviceVolume = volume;
        this.mDeviceRoute = new android.media.MediaRoute2Info.Builder(this.mDeviceRoute).setVolume(volume).build();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.MediaRoute2Info createRouteFromAudioInfo(android.media.AudioRoutesInfo newRoutes) {
        android.media.MediaRoute2Info mediaRoute2InfoBuild;
        int name = android.R.string.default_card_name;
        int type = 2;
        int i = 1;
        if (newRoutes != null) {
            if ((newRoutes.mainType & 2) == 0) {
                if ((newRoutes.mainType & 1) != 0) {
                    type = 3;
                    name = android.R.string.delete;
                } else if ((newRoutes.mainType & 4) != 0) {
                    type = 13;
                    name = android.R.string.default_notification_channel_label;
                } else if ((newRoutes.mainType & 8) != 0) {
                    type = 9;
                    name = android.R.string.default_sms_application;
                } else if ((newRoutes.mainType & 16) != 0) {
                    type = 11;
                    name = android.R.string.deleteText;
                }
            } else {
                type = 4;
                name = android.R.string.delete;
            }
        }
        synchronized (this) {
            android.media.MediaRoute2Info.Builder builder = new android.media.MediaRoute2Info.Builder(DEVICE_ROUTE_ID, this.mContext.getResources().getText(name).toString());
            if (this.mAudioManager.isVolumeFixed()) {
                i = 0;
            }
            android.media.MediaRoute2Info.Builder builder2 = builder.setVolumeHandling(i).setVolume(this.mDeviceVolume).setVolumeMax(this.mAudioManager.getStreamMaxVolume(3)).setType(type).addFeature("android.media.route.feature.LIVE_AUDIO").addFeature("android.media.route.feature.LIVE_VIDEO").addFeature("android.media.route.feature.LOCAL_PLAYBACK").setConnectionState(2);
            if (type == 2) {
                builder2.setSuitabilityStatus(this.mBuiltInSpeakerSuitabilityStatus);
            }
            mediaRoute2InfoBuild = builder2.build();
        }
        return mediaRoute2InfoBuild;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceRouteUpdate() {
        this.mOnDeviceRouteChangedListener.onDeviceRouteChanged();
    }

    private class AudioRoutesObserver extends android.media.IAudioRoutesObserver.Stub {
        private AudioRoutesObserver() {
        }

        public void dispatchAudioRoutesChanged(android.media.AudioRoutesInfo newAudioRoutes) {
            android.media.MediaRoute2Info deviceRoute = com.android.server.media.LegacyDeviceRouteController.this.createRouteFromAudioInfo(newAudioRoutes);
            synchronized (com.android.server.media.LegacyDeviceRouteController.this) {
                com.android.server.media.LegacyDeviceRouteController.this.mDeviceRoute = deviceRoute;
            }
            com.android.server.media.LegacyDeviceRouteController.this.notifyDeviceRouteUpdate();
        }
    }
}
