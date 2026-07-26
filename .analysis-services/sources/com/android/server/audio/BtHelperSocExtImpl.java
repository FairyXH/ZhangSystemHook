package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class BtHelperSocExtImpl implements com.android.server.audio.IBtHelperSocExt {
    private static final java.lang.String TAG = "BtHelperSocExtImpl";
    private com.android.server.audio.BtHelper mBtHelper;

    public BtHelperSocExtImpl(java.lang.Object btHelper) {
        this.mBtHelper = (com.android.server.audio.BtHelper) btHelper;
    }

    @Override // com.android.server.audio.IBtHelperSocExt
    public boolean isLeAudioDevice(android.content.Intent intent) {
        return false;
    }

    @Override // com.android.server.audio.IBtHelperSocExt
    public boolean isNextBtActiveDeviceAvailableForMusic(android.bluetooth.BluetoothA2dp a2dp, android.bluetooth.BluetoothLeAudio leAudio) {
        return false;
    }

    @Override // com.android.server.audio.IBtHelperSocExt
    public boolean isBluetoothScoOn() {
        return true;
    }
}
