package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class BluetoothProfileMonitor {
    static final long GROUP_ID_NO_GROUP = -1;
    private android.bluetooth.BluetoothA2dp mA2dpProfile;
    private final android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    private final android.content.Context mContext;
    private android.bluetooth.BluetoothHearingAid mHearingAidProfile;
    private android.bluetooth.BluetoothLeAudio mLeAudioProfile;
    private final com.android.server.media.BluetoothProfileMonitor.ProfileListener mProfileListener = new com.android.server.media.BluetoothProfileMonitor.ProfileListener();

    BluetoothProfileMonitor(android.content.Context context, android.bluetooth.BluetoothAdapter bluetoothAdapter) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mBluetoothAdapter = (android.bluetooth.BluetoothAdapter) java.util.Objects.requireNonNull(bluetoothAdapter);
    }

    void start() {
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 2);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 21);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 22);
    }

    boolean isProfileSupported(int profile, android.bluetooth.BluetoothDevice device) {
        android.bluetooth.BluetoothProfile bluetoothProfile;
        synchronized (this) {
            switch (profile) {
                case 2:
                    bluetoothProfile = this.mA2dpProfile;
                    break;
                case 21:
                    bluetoothProfile = this.mHearingAidProfile;
                    break;
                case 22:
                    bluetoothProfile = this.mLeAudioProfile;
                    break;
                default:
                    throw new java.lang.IllegalArgumentException(profile + " is not supported as Bluetooth profile");
            }
        }
        if (bluetoothProfile == null) {
            return false;
        }
        return bluetoothProfile.getConnectedDevices().contains(device);
    }

    long getGroupId(int profile, android.bluetooth.BluetoothDevice device) {
        synchronized (this) {
            try {
                switch (profile) {
                    case 2:
                        return -1L;
                    case 21:
                        return this.mHearingAidProfile != null ? this.mHearingAidProfile.getHiSyncId(device) : -1L;
                    case 22:
                        if (this.mLeAudioProfile != null) {
                            groupId = this.mLeAudioProfile.getGroupId(device);
                        }
                        return groupId;
                    default:
                        throw new java.lang.IllegalArgumentException(profile + " is not supported as Bluetooth profile");
                }
            } finally {
            }
        }
    }

    private final class ProfileListener implements android.bluetooth.BluetoothProfile.ServiceListener {
        private ProfileListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, android.bluetooth.BluetoothProfile proxy) {
            synchronized (com.android.server.media.BluetoothProfileMonitor.this) {
                switch (profile) {
                    case 2:
                        com.android.server.media.BluetoothProfileMonitor.this.mA2dpProfile = (android.bluetooth.BluetoothA2dp) proxy;
                        break;
                    case 21:
                        com.android.server.media.BluetoothProfileMonitor.this.mHearingAidProfile = (android.bluetooth.BluetoothHearingAid) proxy;
                        break;
                    case 22:
                        com.android.server.media.BluetoothProfileMonitor.this.mLeAudioProfile = (android.bluetooth.BluetoothLeAudio) proxy;
                        break;
                }
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            synchronized (com.android.server.media.BluetoothProfileMonitor.this) {
                switch (profile) {
                    case 2:
                        com.android.server.media.BluetoothProfileMonitor.this.mA2dpProfile = null;
                        break;
                    case 21:
                        com.android.server.media.BluetoothProfileMonitor.this.mHearingAidProfile = null;
                        break;
                    case 22:
                        com.android.server.media.BluetoothProfileMonitor.this.mLeAudioProfile = null;
                        break;
                }
            }
        }
    }
}
