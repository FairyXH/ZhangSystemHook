package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
interface DeviceRouteController {

    public interface OnDeviceRouteChangedListener {
        void onDeviceRouteChanged();
    }

    java.util.List<android.media.MediaRoute2Info> getAvailableRoutes();

    android.media.MediaRoute2Info getSelectedRoute();

    void start(android.os.UserHandle userHandle);

    void stop();

    void transferTo(java.lang.String str);

    boolean updateVolume(int i);

    static com.android.server.media.DeviceRouteController createInstance(android.content.Context context, android.os.Looper looper, com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener onDeviceRouteChangedListener) {
        android.media.AudioManager audioManager = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        android.media.audiopolicy.AudioProductStrategy strategyForMedia = com.android.server.media.AudioRoutingUtils.getMediaAudioProductStrategy();
        android.bluetooth.BluetoothManager bluetoothManager = (android.bluetooth.BluetoothManager) context.getSystemService(android.bluetooth.BluetoothManager.class);
        android.bluetooth.BluetoothAdapter btAdapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
        if (strategyForMedia != null && btAdapter != null && com.android.media.flags.Flags.enableAudioPoliciesDeviceAndBluetoothController()) {
            return new com.android.server.media.AudioManagerRouteController(context, audioManager, looper, strategyForMedia, btAdapter, onDeviceRouteChangedListener);
        }
        android.media.IAudioService audioService = android.media.IAudioService.Stub.asInterface(android.os.ServiceManager.getService("audio"));
        return new com.android.server.media.LegacyDeviceRouteController(context, audioManager, audioService, onDeviceRouteChangedListener);
    }

    static int getBuiltInSpeakerSuitabilityStatus(android.content.Context context) {
        if (!com.android.media.flags.Flags.enableBuiltInSpeakerRouteSuitabilityStatuses()) {
            return 0;
        }
        int availabilityStatus = context.getResources().getInteger(android.R.integer.config_lowBatteryCloseWarningBump);
        switch (availabilityStatus) {
        }
        return 0;
    }
}
