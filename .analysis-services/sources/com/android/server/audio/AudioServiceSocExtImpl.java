package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioServiceSocExtImpl implements com.android.server.audio.IAudioServiceSocExt {
    private static final java.lang.String TAG = "AudioServiceSocExtImpl";
    com.android.server.audio.AudioService mAudioService;

    public AudioServiceSocExtImpl(java.lang.Object service) {
        this.mAudioService = (com.android.server.audio.AudioService) service;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void onSystemReadyExt() {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void initAudioServiceExtInstance() {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void getBleIntentFilters(android.content.IntentFilter intentFilter) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean setCommunicationDeviceExt(android.os.IBinder cb, int pid, android.media.AudioDeviceInfo device, java.lang.String eventSource) {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void setBluetoothLeCgOn(boolean on) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBluetoothLeTbsDeviceActive() {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBluetoothLeCgOn() {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void startBluetoothLeCg(int pid, int uid, int setMode, android.os.IBinder cb) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void startBluetoothLeCg(android.os.IBinder cb, int targetSdkVersion) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean stopBluetoothLeCg(android.os.IBinder cb) {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void stopBluetoothLeCgLater(android.os.IBinder cb) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void onReceiveExt(android.content.Context context, android.content.Intent intent) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBleAudioFeatureSupported() {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void handleMessageExt(android.os.Message msg) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public android.media.AudioDeviceAttributes preferredCommunicationDevice() {
        return null;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void restartScoInVoipCall() {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void setPreferredDeviceForHfpInbandRinging(int pid, int uid, int mode, android.os.IBinder cb, boolean enable) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void startBluetoothLeCgForRecord(android.os.IBinder cb, int uid, int sampleRate) {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean stopBluetoothLeCgForRecord(android.os.IBinder cb, int uid) {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public android.os.IBinder getModeCb() {
        return null;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void restartBleRecord() {
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public android.media.AudioDeviceAttributes getLeAudioDevice() {
        return null;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public boolean isBluetoothLeCgStateOn() {
        return false;
    }

    @Override // com.android.server.audio.IAudioServiceSocExt
    public void notifyCgState(boolean state) {
    }
}
