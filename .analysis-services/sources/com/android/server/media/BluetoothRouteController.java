package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
interface BluetoothRouteController {

    public interface BluetoothRoutesUpdatedListener {
        void onBluetoothRoutesUpdated();
    }

    java.util.List<android.media.MediaRoute2Info> getAllBluetoothRoutes();

    android.media.MediaRoute2Info getSelectedRoute();

    java.util.List<android.media.MediaRoute2Info> getTransferableRoutes();

    void start(android.os.UserHandle userHandle);

    void stop();

    void transferTo(java.lang.String str);

    boolean updateVolumeForDevices(int i, int i2);

    static com.android.server.media.BluetoothRouteController createInstance(android.content.Context context, com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener listener) {
        java.util.Objects.requireNonNull(listener);
        android.bluetooth.BluetoothAdapter btAdapter = ((android.bluetooth.BluetoothManager) context.getSystemService(android.bluetooth.BluetoothManager.class)).getAdapter();
        if (btAdapter == null || com.android.media.flags.Flags.enableAudioPoliciesDeviceAndBluetoothController()) {
            return new com.android.server.media.BluetoothRouteController.NoOpBluetoothRouteController();
        }
        return new com.android.server.media.LegacyBluetoothRouteController(context, btAdapter, listener);
    }

    public static class NoOpBluetoothRouteController implements com.android.server.media.BluetoothRouteController {
        @Override // com.android.server.media.BluetoothRouteController
        public void start(android.os.UserHandle userHandle) {
        }

        @Override // com.android.server.media.BluetoothRouteController
        public void stop() {
        }

        @Override // com.android.server.media.BluetoothRouteController
        public void transferTo(java.lang.String routeId) {
        }

        @Override // com.android.server.media.BluetoothRouteController
        public android.media.MediaRoute2Info getSelectedRoute() {
            return null;
        }

        @Override // com.android.server.media.BluetoothRouteController
        public java.util.List<android.media.MediaRoute2Info> getTransferableRoutes() {
            return java.util.Collections.emptyList();
        }

        @Override // com.android.server.media.BluetoothRouteController
        public java.util.List<android.media.MediaRoute2Info> getAllBluetoothRoutes() {
            return java.util.Collections.emptyList();
        }

        @Override // com.android.server.media.BluetoothRouteController
        public boolean updateVolumeForDevices(int devices, int volume) {
            return false;
        }
    }
}
