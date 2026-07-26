package com.android.media.audio;

/* JADX INFO: loaded from: classes.dex */
public final class FeatureFlagsImpl implements com.android.media.audio.FeatureFlags {
    @Override // com.android.media.audio.FeatureFlags
    public boolean absVolumeIndexFix() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean alarmMinVolumeZero() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean asDeviceConnectionFailure() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean audioserverPermissions() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean bluetoothMacAddressAnonymization() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean disablePrescaleAbsoluteVolume() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean dsaOverBtLeAudio() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean musicFxEdgeToEdge() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean portToPiidSimplification() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean replaceStreamBtSco() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean ringerModeAffectsAlarm() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean setStreamVolumeOrder() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean spatializerOffload() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean spatializerUpmix() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean stereoSpatialization() {
        return false;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean vgsVssSyncMuteOrder() {
        return true;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean volumeRefactoring() {
        return false;
    }
}
